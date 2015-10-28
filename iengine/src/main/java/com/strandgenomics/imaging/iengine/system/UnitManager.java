package com.strandgenomics.imaging.iengine.system;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.UnitAssociationDAO;
import com.strandgenomics.imaging.iengine.dao.UnitDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.Unit;
import com.strandgenomics.imaging.iengine.models.UnitAssociation;
import com.strandgenomics.imaging.iengine.models.UnitType;

/**
 * This class encapsulates the functionality associated with units
 * @author Anup Kulkarni
 *
 */
public class UnitManager extends SystemManager {
	UnitManager()
	{ }
	
	/**
	 * creates new Unit from given details
	 * @param actorName logged in user
	 * @param unitName name of the unit
	 * @param type type of the unit
	 * @param globalStorage amount of physical storage it has globally
	 * @param pointOfContact email address
	 * @throws DataAccessException 
	 */
	public void createUnit(String actorName, String unitName, UnitType type, double globalStorage, String pointOfContact) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		Unit unit = new Unit(unitName, globalStorage, type, pointOfContact);
		
		UnitDAO unitDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitDAO();
		unitDao.insertUnit(unit);
	}
	
	/**
	 * returns list of all units
	 * @param actorName
	 * @return
	 * @throws DataAccessException 
	 */
	public List<Unit> listUnits(String actorName) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		UnitDAO unitDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitDAO();
		return unitDao.listAllUnits();
	}
	
	/**
	 * returns specified unit
	 * @param actorName
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public Unit getUnit(String actorName, String name) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		UnitDAO unitDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitDAO();
		return unitDao.getUnit(name);
	}
	
	/**
	 * removes the specified unit
	 * @param actorName
	 * @param unitName
	 * @throws DataAccessException
	 */
	public void removeUnit(String actorName, String unitName) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		List<UnitAssociation> associations = unitAssociationDao.getAssociationsForUnit(unitName);
		if(associations!=null)
		{
			// check if removing this unit does not take away required space
			// from any of the associated projects
			for(UnitAssociation association:associations)
			{
				Project p = SysManagerFactory.getProjectManager().getProject(association.projectId);
				if(!isSafeToUpdate(actorName, unitName, p.getName(), 0))
				{
					// if this unit is removed the project will not have enough
					// space to accounts its current usage
					throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_SPACE_VIOLATION));
				}
			}
			
			// safe to remove unit
			
			// update the project space quota
			
			// check if removing this unit does not take away required space
			// from any of the associated projects
			for(UnitAssociation association:associations)
			{
				// calculate new storage quota for the project
				Project p = SysManagerFactory.getProjectManager().getProject(association.projectId);
				double newStorageQuota = p.getStorageQuota() - association.storageSpace;
				
				// update the storage quota for project
				SysManagerFactory.getProjectManager().updateProjectDetails(actorName, p.getName(), p.getNotes(), p.getStatus(), newStorageQuota);
				
				// update the association
				unitAssociationDao.removeProject(unitName, p.getID());
			}
		}
		
		// remove unit
		UnitDAO unitDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitDAO();
		unitDao.removeUnit(unitName);
	}
	
	/**
	 * updates the unit-project association 
	 * @param actorName logged in user
	 * @param projectName specified project
	 * @param unitName specified unit
	 * @param newSpace new space
	 * @throws DataAccessException
	 */
	public void updateAssociation(String actorName, String projectName, String unitName, double newSpace) throws DataAccessException
	{
		logger.logp(Level.INFO, "UnitManager", "updateAssociation", "space="+newSpace);
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		// update the association
		List<UnitAssociation> associations = getUnitAssociations(actorName, unitName, projectName);
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		
		double updatedSpace = p.getStorageQuota();
		if(associations!=null)
		{
			if(!isSafeToUpdate(actorName, unitName, projectName, newSpace))
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_SPACE_VIOLATION));
			
			double changeInSpace = getSpaceChange(actorName, unitName, projectName, newSpace);
			updatedSpace = p.getStorageQuota() - changeInSpace;
			
			unitAssociationDao.updateProjectSpace(unitName, p.getID(), newSpace);
		}
		else
		{
			unitAssociationDao.associateProject(unitName, p.getID(), newSpace);
			updatedSpace = p.getStorageQuota() + newSpace;
		}
		
		// update the storage quota for project
		logger.logp(Level.INFO, "UnitManager", "updateAssociation", "project space ="+updatedSpace);
		SysManagerFactory.getProjectManager().updateProjectDetails(actorName, p.getName(), p.getNotes(), p.getStatus(), updatedSpace);
		
		projectAssociationNotification(actorName, unitName, p);
	}
	/**
	 * send email notification of team-project association
	 * @param actorLogin
	 * @param unitName
	 * @param project
	 * @throws DataAccessException
	 */
	private void projectAssociationNotification(String actorLogin, String unitName, Project project) throws DataAccessException{
		
			Unit unit = SysManagerFactory.getUnitManager().getUnit(actorLogin, unitName);
	
			List<String> receivers = new ArrayList<String>();
			
			receivers.add(unit.getPointOfContact());
			
			double contribution = ImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO().getAssociation(unit.getUnitName(), project.getID()).storageSpace;
			
			double availableSpace = getAvailableSpace(actorLogin, unit.getUnitName());
			
			try{
				
				SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, null, NotificationMessageType.TEAM_QUOTA, project.getName(), String.valueOf(project.getStorageQuota()), String.valueOf(contribution), String.valueOf(availableSpace) );
			}
			catch(Exception e){
				
				logger.logp(Level.WARNING, "ProjectManager", "createNewProject", "failed sending email notification to "+ receivers, e);
			}
	}
	
	/**
	 * get available storage of a unit
	 * @param user
	 * @param unitName
	 * @return
	 * @throws DataAccessException
	 */
    private double getAvailableSpace(String user, String unitName) throws DataAccessException
    {
    	List<UnitAssociation> associations = SysManagerFactory.getUnitManager().getUnitAssociations(user, unitName, null);
    	
    	double available = SysManagerFactory.getUnitManager().getUnit(user, unitName).getGlobalStorage();
    	if(associations!=null)
    	{
    		for(UnitAssociation association:associations)
    		{
    			available -= association.storageSpace;
    		}
    	}
    	
    	return available;
    }
	
	/**
	 * update the details of unit
	 * @param actorName logged in user
	 * @param unitName specified unit
	 * @param type new type
	 * @param pointOfContact new point of contact
	 * @throws DataAccessException
	 */
	public void updateUnitDetails(String actorName, String unitName, UnitType type, String pointOfContact) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		Unit unit = getUnit(actorName, unitName);
		
		UnitDAO unitDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitDAO();
		unitDao.updateUnitDetails(unitName, unit.getGlobalStorage(), type, pointOfContact);
	}
	
	/**
	 * returns the unit-project association for specified unit
	 * @param actorName
	 * @param unitName
	 * @param projectName
	 * @return
	 * @throws DataAccessException
	 */
	public List<UnitAssociation> getUnitAssociations(String actorName, String unitName, String projectName) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		
		Integer projectId = null;
		if (projectName != null && !projectName.isEmpty())
		{
			projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		}
		
		if(unitName!=null && unitName.isEmpty())
			unitName = null;
		
		List<UnitAssociation> associations = unitAssociationDao.getAssociations(unitName, projectId);
		
		return associations;
	}
	
	/**
	 * removes the unit-project association
	 * @param user
	 * @param unitName
	 * @param projectName
	 * @throws DataAccessException
	 */
	public void removeUnitAssociation(String user, String unitName, String projectName) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(user))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		if(!isSafeToUpdate(user, unitName, projectName, 0))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_SPACE_VIOLATION));
		
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		updateAssociation(user, projectName, unitName, 0);
		
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		unitAssociationDao.removeProject(unitName, p.getID());
	}
	
	/**
	 * update the global storage associated with unit
	 * @param actorName logged in user
	 * @param unitName specified unit
	 * @param newSpace new global storage space
	 * @throws DataAccessException
	 */
	public void updateUnitSpace(String actorName, String unitName, double newSpace) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		List<UnitAssociation> associations = unitAssociationDao.getAssociationsForUnit(unitName);
		
		if(associations!=null)
		{
			// the new space of unit has to be greater than sum of the storage
			// it has contributed to projects
			long sum = 0;
			for(UnitAssociation association:associations)
			{
				sum += association.storageSpace;
			}
			
			if(newSpace<sum)
			{
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_SPACE_VIOLATION));
			}
		}
		
		Unit unit = getUnit(actorName, unitName);
		
		UnitDAO unitDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitDAO();
		unitDao.updateUnitDetails(unitName, newSpace, unit.getType(), unit.getPointOfContact());
	}
	
	private double getSpaceChange(String actorName, String unitName, String projectName, double newSpace) throws DataAccessException
	{
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		UnitAssociation association = unitAssociationDao.getAssociation(unitName, p.getID());
		
		double prevSpace = 0.0;
		if(association!=null)
			prevSpace = association.storageSpace;
		
		double changeInSpace = prevSpace - newSpace;
		
		return changeInSpace;
	}
	
	/**
	 * returns true if deleting the unit does not violate project space usage
	 * @param actorName
	 * @param unitName
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isSafeToDelete(String actorName, String unitName) throws DataAccessException
	{
		UnitAssociationDAO unitAssociationDao = DBImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO();
		List<UnitAssociation> associations = unitAssociationDao.getAssociationsForUnit(unitName);
		
		if(associations!=null)
		{
			for(UnitAssociation association: associations)
			{
				String projectName = SysManagerFactory.getProjectManager().getProject(association.projectId).getName();
				if(!isSafeToUpdate(actorName, unitName, projectName, 0))
					return false;
			}
		}
		
		return true;
	}
	
	private boolean isSafeToUpdate(String actorName, String unitName, String projectName, double newSpace) throws DataAccessException
	{
		double changeInSpace = getSpaceChange(actorName, unitName, projectName, newSpace);
		
		Project p = SysManagerFactory.getProjectManager().getProject(projectName);
		
		if((p.getStorageQuota() - changeInSpace) < p.getSpaceUsage())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * get all unit associations for a project
	 * @param projectId
	 * @return
	 * @throws DataAccessException
	 */
	public List<UnitAssociation> getAssociationsForProject(int projectId) throws DataAccessException{
		
		return ImageSpaceDAOFactory.getDAOFactory().getUnitAssociationDAO().getAssociationsForProject(projectId);
		
	}
}
