/*
 * ProjectManager.java
 *
 * AVADIS Image Management System
 * Core Engine
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.system;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.HistoryDAO;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.MetaDataDAO;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectCreationDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.UserPreferencesDAO;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysDAO;
import com.strandgenomics.imaging.iengine.models.Archive;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Job;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.Unit;
import com.strandgenomics.imaging.iengine.models.UnitAssociation;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserMembership;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;

/**
 * This class encapsulates the functionality associated with projects
 * @author arunabha
 *
 */
public class ProjectManager extends SystemManager {
	
	ProjectManager()
	{ }
	
	/**
	 * returns list of display columns i.e., fixed fields associated with record
	 * @return list of display columns
	 * @throws DataAccessException 
	 */
	public Collection<SearchColumn> getRecordFixedFields(String actorLogin, String projectName) throws DataAccessException
	{
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
		Project currentProject = SysManagerFactory.getProjectManager().getProject(projectName);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO dao = factory.getNavigationDAO(currentProject.getID());
		return dao.getFixedFields();
	}
    
    /**
     * Returns a list of meta-data names that are navigable
     * @return a list of meta-data names that are navigable
     */
    public Collection<SearchColumn> getNavigableFields(String actorLogin, String projectName) throws DataAccessException
    {
    	UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO navDao = factory.getNavigationDAO( getProject(projectName).getID() );
		return navDao.getNavigableFields();
    }
    
    public void updateProjectDetails(String actorLogin, String projectName, String notes, ProjectStatus status, Double storageQuota) throws DataAccessException
    {
    	UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		Project project = getProject(projectName);
		notes = notes == null ? project.getNotes() : notes;
		status = status == null ? project.getStatus() : status;
		storageQuota = storageQuota == null ? project.getStorageQuota() : storageQuota;
		projectDao.updateProjectDetails(project.getID(), notes, status, storageQuota);
    }
    
    public Collection<SearchColumn> getUserAnnotationFields(String actorLogin, String projectName) throws DataAccessException
    {
    	UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
    	NavigationDAO navDao = factory.getNavigationDAO( getProject(projectName).getID() );
    	return navDao.getUserAnnotationFields();
    }
    
	public Project getProjectForRecord(String actorLogin, long guid) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		return getProject(projectID);
	}
	
	public Project getProject(int projectID)
	{
		return SysManagerFactory.getUserPermissionManager().getProject(projectID);
	}

	public Project getProject(String name)
	{
		return SysManagerFactory.getUserPermissionManager().getProject(name);
	}
	
	public Archive findArchive(BigInteger archiveSignature) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO dao = factory.getArchiveDAO();
		return dao.findArchive(archiveSignature);
	}
	
	
	/**
	 * removes all the user annotations on specified project by specified name
	 * @param app application used for accessing the method
	 * @param actorLogin authorised user
	 * @param projectID specified project
	 * @param annotationName specified annotation name
	 * @throws DataAccessException
	 */
	public void removeUserAnnotation(Application app, String actorLogin, String accessToken, int projectID, String annotationName)  throws DataAccessException 
	{
		String projectName = getProject(projectID).getName();
		
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canWrite(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		//Remove from navigation tables
		NavigationDAO navDao = factory.getNavigationDAO(projectID);
		navDao.removeAnnotationColumn(projectID, annotationName);
		navDao.removeAnnotationColumnFromInfo(projectID, annotationName);
		
		//Remove from user preferences table
		UserPreferencesDAO prefDao = factory.getUserPreferencesDAO();
		List<User> users = SysManagerFactory.getUserPermissionManager().listUsers();
		for(User user: users)
		{
			List<SearchColumn> colms = null;
			try {
				colms = prefDao.getNavigationColumns(user.login, projectID);
			} catch (Exception e) {
			}
			
			if(colms!=null)
			{
				for(SearchColumn col:colms)
				{
					if(col.getColumn().equals(annotationName))
					{
						colms.remove(col);
						break;
					}
				}
				prefDao.setNavigationColumns(user.login, projectID, colms);
			}
			
			try {
				colms = prefDao.getSpreadSheetColumns(user.login, projectID);
			} catch (Exception e) {
			}
			
			if(colms!=null)
			{
				for(SearchColumn col:colms)
				{
					if(col.getColumn().equals(annotationName))
					{
						colms.remove(col);
						break;
					}
				}
				prefDao.setSpreadSheetColumns(user.login, projectID, colms);
			}
		}
		
		//Remove from metadata tables
		MetaDataDAO metaDao = factory.getMetaDataDAO(AnnotationType.Text);
		AnnotationType type = metaDao.getAnnotationType(projectID, annotationName);
		
		metaDao = factory.getMetaDataDAO(type);
		long[] guids = metaDao.getRecordsForUserAnnotation(projectID, annotationName);
		
		metaDao.deleteUserAnnotation(projectID, annotationName);
		metaDao.deleteUserAnnotationFromRegistry(projectID, annotationName);
		
		// add history
		if(guids!=null)
		{
			for(long guid:guids)
			{
				addHistory(guid, app, actorLogin, accessToken, HistoryType.USER_ANNOTATION_DELETED, annotationName);
			}
		}
	}
	
	private void addHistory(long guid, Application app, String user, String accessToken, HistoryType type, String... args) throws DataAccessException
	{
		SysManagerFactory.getHistoryManager().insertHistory(guid, app, user, accessToken, type, type.getDescription(guid, user, args), args);
	}
	
	/**
	 * returns the list of all distinct user annotation values for given annotation key on given project
	 * @param actorLogin current logged in user
	 * @param projectName name of project
	 * @param annotationKey specified user annotation key
	 * @return set of unique user annotation values 
	 * @throws DataAccessException 
	 */
	public Set<MetaData> getUniqueUserAnnotationValues(String actorLogin, String projectName, String annotationKey, AnnotationType type, int limit) throws DataAccessException
	{
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		MetaDataDAO mdao = factory.getMetaDataDAO(type);
		
		return mdao.getUniqueAnnotationValues(getProject(projectName).getID(), annotationKey, type, limit);
	}
	
	/**
	 * Returns the list of archives associated with the specified project
	 * @param projectID project ID
	 * @return list of archive signatures
	 * @throws DataAccessException
	 */
	public Set<BigInteger> getArchivesForProject(String actorLogin, String projectName) throws DataAccessException
	{
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
    	RecordDAO dao = factory.getRecordDAO();
    	return dao.getArchivesForProject( getProject(projectName).getID() );
	}
	
	public List<String> findProjectForArchive(String actorLogin, BigInteger archiveSignature) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO dao = factory.getRecordDAO();
		long[] guids = dao.getGUIDsForArchive(archiveSignature);
		
		if(guids == null || guids.length == 0) return null;
		
		Set<String> projects = new HashSet<String>();
		RecordManager rm = SysManagerFactory.getRecordManager();

		for(long guid : guids)
		{
			int projectID = rm.getProjectID(guid);
			projects.add( getProject(projectID).getName() );
		}
		
		return projects == null || projects.isEmpty() ? null : new ArrayList<String>(projects);
	}
	
	
	public long[] getGUIDsForArchive(String actorLogin, BigInteger archiveSignature) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO dao = factory.getRecordDAO();
		long[] guids = dao.getGUIDsForArchive(archiveSignature);
		
		if(guids == null || guids.length == 0)
			return null;

		List<Long> selectedGuids = new ArrayList<Long>();
		for(long guid : guids)
		{
			if(SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
				selectedGuids.add( guid );
		}
		
		if(selectedGuids.isEmpty())
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		long[] selection = new long[selectedGuids.size()];
		for(int i = 0; i < selection.length; i++)
		{
			selection[i] = selectedGuids.get(i);
		}
		return selection; 
	}

	/**
	 * returns the number of records in the given project
	 * @param actorLogin logged in user
	 * @param projectName specified project
	 * @return count of records in that project; -1 if user cant access the project
	 * @throws DataAccessException 
	 */
	public int getRecordCount(String actorLogin, String projectName) throws DataAccessException
	{
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		Project project = projectDao.findProject(projectName);
		return project.getNoOfRecords();
	}
	
	public List<String> getActiveProjects(String actorLogin) throws DataAccessException
	{
		List<Project> pList = getProjects(actorLogin, ProjectStatus.Active);
		if(pList == null || pList.isEmpty()) return null;
		
		List<String> projectNames = new ArrayList<String>();
		for(Project p : pList)
		{
			projectNames.add( p.getName() );
		}
		
		return projectNames;
	}
	
	public List<String> getArchivedProjects(String actorLogin) throws DataAccessException
	{
		List<Project> pList = getProjects(actorLogin, ProjectStatus.Archived);
		if(pList == null || pList.isEmpty()) return null;
		
		List<String> projectNames = new ArrayList<String>();
		for(Project p : pList)
		{
			projectNames.add( p.getName() );
		}
		
		return projectNames;
	}
	
	/**
	 * Returns the list of projects accessible to the specified user
	 * @param actorLogin the user making the request
	 * @param status the status of the project
	 * @return list of projects accessible to the specified user
	 * @throws DataAccessException
	 */
	public List<Project> getProjects(String actorLogin, ProjectStatus status) throws DataAccessException
	{
		User actor = SysManagerFactory.getUserManager().getUser(actorLogin);
    	logger.logp(Level.INFO, "ProjectManager", "getProject", "listing projects for actor "+actor);
    	
		//check for access rights
		switch(actor.getRank())
		{
			case Administrator:
			case FacilityManager:
				return getAllProjects(status);
			case TeamLeader:
			case TeamMember:
				return getAllProjects(actor, status);
		}
		
		return null;
	}
	
	/**
	 * returns true if addition of specified bytes will exceed project quota
	 * @param project specified project
	 * @param clientFiles files to add
	 * @return true if addition of specified bytes will exceed project quota; false otherwise
	 */
	public boolean isQuotaExceeded(String project, List<ISourceReference> clientFiles)
	{
		logger.logp(Level.FINE, "ProjectManager", "isQuotaExceeded", "checking for project quota "+project);
		if(clientFiles == null || clientFiles.size() < 1) return false;
		
		long size = 0;
		for(ISourceReference clientFile:clientFiles)
		{
			size += clientFile.getSize();
		}
		
		return isQuotaExceeded(getProject(project).getID(), size);
	}
	
	/**
	 * returns true if addition of specified bytes will exceed project quota
	 * @param projectID specified project
	 * @param bytesToAdd specified number of bytes to add
	 * @return true if addition of specified bytes will exceed project quota; false otherwise
	 */
	boolean isQuotaExceeded(int projectID, long bytesToAdd)
	{
		double storageQuota = getProject(projectID).getStorageQuota();
		double diskUsage = getProject(projectID).getSpaceUsage();
		double gbToAdd = (double)bytesToAdd/ (double)(1024 * 1024 * 1024);
		
		logger.logp(Level.INFO, "ProjectManager", "isQuotaExceeded", "storage quota="+storageQuota+"diskUsage="+diskUsage+"gbToAdd="+gbToAdd);
		
		if(diskUsage + gbToAdd > storageQuota)
		{
			// quota is exceeded 
			return true;
		}
		
		return false;
	}
	
	private List<Project> getAllProjects(ProjectStatus status) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		List<Project> unsortedList = projectDao.findProjectByStatus(status);
		
		return unsortedList == null ? null : getSortedProjectList(unsortedList);
	}
	
	public List<Project> getAllProjectsByManager(String user) throws DataAccessException
	{
		List<Project> unsortedList = SysManagerFactory.getUserPermissionManager().getManagedProjects(user);
		
		return unsortedList == null ? null : getSortedProjectList(unsortedList);
	}
	
	private List<Project> getAllProjects(User actor, ProjectStatus status) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		List<Project> unsortedList = projectDao.findProject(actor.login, status);
		
		return unsortedList == null ? null : getSortedProjectList(unsortedList);
	}
		
	public Project getProject(String actorLogin, String projectName) throws DataAccessException
	{
		logger.logp(Level.INFO, "ProjectManager", "getProject", "finding project "+projectName +" for user "+actorLogin);
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
		
    	if(!permissionManager.canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
    	
    	return getProject(projectName);
	}
	
	private List<Project> getSortedProjectList(List<Project> unsortedList)
	{
		Comparator<Project> projectComparator = new Comparator<Project>() 
		{

			@Override
			public int compare(Project o1, Project o2)
			{
				if(o1 == null) return -1;
				
				if(o2 == null) return 1;
				
				String name1 = o1.getName();
				String name2 = o2.getName();
				
				return name1.compareTo(name2);
			}
			
		};
		Collections.sort(unsortedList, projectComparator);
		
		return unsortedList;
	}

	
	/**
	 * List all legal members of this projects, may return null if the concerned user do not have permission
	 * to get this list
	 * @return all legal members of this projects
	 */
    public Set<User> getProjectMembers(String actorLogin, String projectName) throws DataAccessException
    {
    	logger.logp(Level.INFO, "ProjectManager", "getProjectMembers", "finding project members in "+projectName);

    	if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	
    	Project existingProject = SysManagerFactory.getUserPermissionManager().getProject(projectName);

    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		String[] members = projectDao.getProjectMembers(existingProject.getID(), null);
		Set<User> userList = new HashSet<User>();
		
		if(members == null) return userList; //return an empty list
		
		UserManager um = SysManagerFactory.getUserManager();
		for(String memberID : members)
		{
			userList.add(um.getUser(memberID));
		}
    	
		return userList;
    }
    
    
    /**
	 * List all legal members of this projects, may return null if the concerned user do not have permission
	 * to get this list
	 * @return all legal members of this projects
	 */
    public List<UserMembership> getMemberships(String actorLogin, String projectName, String userLogin) throws DataAccessException
    {
    	logger.logp(Level.INFO, "ProjectManager", "getProjectMemberships", "finding project members in "+projectName);

    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		if (projectName != null)
		{
			if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		else
		{
			// Only facility manager can get membership details of all projects
			// (by passing null for projectname)
			if (!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorLogin))
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}

		Integer projectID = null;
		if (projectName != null)
		{
			projectID = getProject(projectName).getID();
		}
    	
    	return projectDao.getMemberships(projectID , userLogin);
    }
    
    
    /**
     * Returns the project manager of this project
     * @return the project manager of this project
     */
    public Set<User> getProjectManager(String actorLogin, String projectName)
    {
    	UserPermissionManager pm = SysManagerFactory.getUserPermissionManager();
    	if(!pm.canRead(actorLogin, projectName))
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	
    	int projectID = pm.getProject(projectName).getID();
    	
		try
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			ProjectDAO projectDao = factory.getProjectDAO();
			
			String[] userID = projectDao.getProjectMembers(projectID, Permission.Manager);
			if(userID == null || userID.length == 0)
				return null;
			
			UserManager um = SysManagerFactory.getUserManager();
			Set<User> managers = new HashSet<User>();
			
			for(String name : userID)
			{
				managers.add( um.getUser(name) );
			}
			return managers;
		}
		catch(DataAccessException ex)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR));
		}
    }
    
    /**
     * sets the permission of specified user on specified project
     * @param actorLogin current logged in user
     * @param user specified user
     * @param projectName specified project
     * @param permission specified permission
     * @throws DataAccessException 
     */
    public void setUserPermission(String actorLogin, String user, String projectName, Permission permission) throws DataAccessException
    {
    	SysManagerFactory.getUserPermissionManager().setUserPermission(actorLogin, projectName, user, permission);
    }

    /**
     * Removes the specified users as the member of the this project.
     * Note that the login user must be the project Manager of this project for this call to be successful
     * @param users list of users to add to the project
     * @return the list of users who are successfully removed
     */
    public List<String> removeProjectMembers(String actor, String projectName, String ... users)
    {
    	logger.logp(Level.INFO, "ProjectManager", "removeProjectMembers", "removing members in "+projectName);
    	
    	List<User> userList = SysManagerFactory.getUserManager().getUser(users);
    	if(userList== null || userList.isEmpty()) return null;
    	
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		Project existingProject;
		try 
		{
			existingProject = projectDao.findProject(projectName);
		} 
		catch (DataAccessException e) 
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR));
		}
		
		if(existingProject == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_DO_NOT_EXIT, projectName));
		}
		
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
		if(!permissionManager.canManageUsers(actor, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		List<String> removedMembers = new ArrayList<String>();
		for(User u : userList)
		{
			try 
			{
				//first remove permission from project_membership table
				permissionManager.removeMemberUser(actor, projectName, u.getLogin());
				
				//now remove entry from user_recent_projects
				factory.getUserPreferencesDAO().deleteUserPreferences(existingProject.getID(),u.getLogin());
				
				removedMembers.add( u.getLogin() );
			} 
			catch (DataAccessException ex)
			{
				logger.logp(Level.INFO, "ProjectManager", "removeProjectMembers", "unable to remove user "+u +" to project "+existingProject, ex);
			}
		}
		
		return removedMembers;
    }
    
    /**
     * Deletes this project - place a request to delete this project. Projects are not deleted right away
     * Note that the login user may not have permission to delete, in that case an exception will be thrown
     */
    public Job deleteProject(User actor, String projectName)
    {
    	return null;
    }
    
    
    /**
     * Archives this project - place a request to archive this project
     * Note that the login user may not have permission to archive, in that case an exception will be thrown
     */
    public Job archiveProject(User actor, String projectName)
    {
    	return null;
    }
    
    
    /**
     * Restores this project from archive - place a request to restore this project
     * Note that the login user may not have permission to restore, in that case an exception will be thrown
     */
    public Job restoreProject(User actor, String projectName)
    {
    	return null;
    }

	/**
	 * Creates a new project with the specified name etc
	 * @param actor the user creating it
	 * @param projectName name of the project
	 * @param notes description of the project
	 * @param diskQuota disk quota in GB
	 * @return newly created project
	 */
	public synchronized Project createNewProject(String actorLogin, String projectName, String notes, double diskQuota)
	{
		User actor = SysManagerFactory.getUserManager().getUser(actorLogin);
		logger.logp(Level.INFO, "ProjectManager", "createNewProject", "trying to create new project "+projectName);
		//check for access rights
		switch(actor.getRank())
		{
			case Administrator:
			case FacilityManager:
			case TeamLeader:
				break;
			case TeamMember:
			default:
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		try
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			ProjectDAO projectDao = factory.getProjectDAO();
			ProjectCreationDAO creationDao = factory.getProjectCreationDAO();
			
			Project existingProject = projectDao.findProject(projectName);
			if(existingProject != null)
			{
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_ALREADY_EXIT, projectName));
			}
			
			//find a unique location (folder name) for this project
			String location = findUniqueProjectLocation(projectName);
			//create the project
			Project p = creationDao.createProject(projectName, notes, actor.login, diskQuota, location);
			//register this project
			updateProjectCache(p);
			 //the creator is the default project manager
			addProjectMembers(actorLogin, projectName, Permission.Manager, actorLogin);
			
			factory.getProjectClientDAO().createNewFolder(p.getID(), "Default", 0);
			
			return p;
		}
		catch(DataAccessException ex)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR, ex.getMessage()));
		}
	}
	
	public List<User> addProjectMembers(String actorLogin, String projectName, Permission user_role, String... users)
	{
		logger.logp(Level.INFO, "ProjectManager", "addProjectMembers", "adding members in "+projectName);
    	
    	List<User> userList = SysManagerFactory.getUserManager().getUser(users);
    	if(userList== null || userList.isEmpty()) return null;
    	
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		Project existingProject;
		try 
		{
			existingProject = projectDao.findProject(projectName);
		} 
		catch (DataAccessException e) 
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR));
		}
		
		if(existingProject == null)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_DO_NOT_EXIT, projectName));
		}
		
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
		if(!permissionManager.canManageUsers(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		List<User> newMembers = new ArrayList<User>();
		for(User u : userList)
		{
			try 
			{
				permissionManager.setUserPermission(actorLogin, projectName, u.getLogin(), user_role);
				newMembers.add( u );
			} 
			catch (DataAccessException ex)
			{
				logger.logp(Level.INFO, "ProjectManager", "addProjectMembers", "unable to add user "+u +" to project "+existingProject, ex);
			}
		}
		
		return newMembers;
	}

	private String findUniqueProjectLocation(String projectName) throws DataAccessException
	{
		String location = Util.asciiText(projectName, '_'); //sanitize, remove junk chars
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		int counter = 0;
		String newLocation = location;
		//try to find a unique name for this
		do //finite loop
		{
			Project p = projectDao.findByLocation(newLocation);
			if(p == null) //not there, so done
				return newLocation;
			
			newLocation = location+"_"+counter;
		}
		while(++counter < 100);
		
		throw new DataAccessException("unique storage location for projects cannot be found "+projectName);
	}
	
	/**
	 * This method is called by the storage manager to report storage of a new archive
	 * @param projectID
	 * @param noOfRecords
	 * @param increaseInGB
	 * @throws DataAccessException 
	 */
	void updateProjectRecords(int projectID, int noOfRecords, double increaseInGB) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		projectDao.updateProjectRecords(projectID, noOfRecords, increaseInGB);
		
		Project p = getProject(projectID);
		p.incrementUsage(increaseInGB);
	}
	
	private void updateProjectCache(Project p)
	{
		SysManagerFactory.getUserPermissionManager().updateProject(p);
	}

	/**
	 * transfer specified guids from source project to target project
	 * @param ids specified guids
	 * @param sourceProjectId source project
	 * @param targetProjectId target project
	 * @param diskUsage disk space used by the records in GB
	 * @throws DataAccessException 
	 */
	void transferRecords(String actorLogin, long[] ids, int sourceProjectId, int targetProjectId, double diskUsage) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		// update storage db tables
		
		for(long guid:ids)
		{
			//1. image metadata table
			ImagePixelDataDAO pixeldataDao = factory.getImagePixelDataDAO();
			List<ImagePixelData> imageMetaData = pixeldataDao.find(guid);// read the exisiting data
			pixeldataDao.deletePixelDataForRecord(guid);// delete from source table
			
			for(ImagePixelData meatadata:imageMetaData)
			{
				pixeldataDao.insertPixelDataForRecord(targetProjectId, guid, meatadata.getX(),
						meatadata.getY(), meatadata.getZ(),
						meatadata.getElapsed_time(),
						meatadata.getExposureTime(), meatadata.getTimestamp(),
						meatadata.getDimension().frameNo,
						meatadata.getDimension().sliceNo,
						meatadata.getDimension().channelNo,
						meatadata.getDimension().siteNo);
			}
			
			//2. user annotations
			List<MetaData> intMetadata = factory.getMetaDataDAO(AnnotationType.Integer).find(sourceProjectId, guid);
			List<MetaData> realMetadata = factory.getMetaDataDAO(AnnotationType.Real).find(sourceProjectId, guid);
			List<MetaData> textMetadata = factory.getMetaDataDAO(AnnotationType.Text).find(sourceProjectId, guid);
			List<MetaData> timeMetadata = factory.getMetaDataDAO(AnnotationType.Time).find(sourceProjectId, guid);
			
			List<MetaData> allMetadata = new ArrayList<MetaData>();
			if(intMetadata!=null)
				allMetadata.addAll(intMetadata);
			if(realMetadata!=null)
				allMetadata.addAll(realMetadata);
			if(textMetadata!=null)
				allMetadata.addAll(textMetadata);
			if(timeMetadata!=null)
				allMetadata.addAll(timeMetadata);
			for(MetaData metadata:allMetadata)
			{
				// remove from source table
				factory.getMetaDataDAO(metadata.getType()).delete(sourceProjectId, guid, metadata.getName());
				// insert in target table
				factory.getMetaDataDAO(metadata.getType()).insertUserAnnotation(targetProjectId, guid, actorLogin, metadata.getName(), metadata.getValue());
			}
			
			//3. record navigation table
			NavigationDAO sourceNavigatioDao = factory.getNavigationDAO(sourceProjectId);
			sourceNavigatioDao.deleteRecord(guid); // remove from old table

			//3a. insert record in new table
			NavigationDAO targetNavigatioDao = factory.getNavigationDAO(targetProjectId);
			Record record = factory.getRecordDAO().findRecord(guid);

			targetNavigatioDao.registerRecord(guid, record.uploadedBy,
					record.numberOfSlices, record.numberOfFrames,
					record.numberOfChannels, record.numberOfSites,
					record.imageWidth, record.imageHeight, record.uploadTime,
					record.sourceTime, record.creationTime,
					record.acquiredTime, record.imageDepth, record.getXPixelSize(),
					record.getYPixelSize(), record.getZPixelSize(), record.getSourceFormat(),
					record.imageType, record.machineIP, record.macAddress,
					record.sourceFolder, record.sourceFilename);
			
			//3b. insert user annotations in navigation table
			for(MetaData metadata:allMetadata)
			{
				targetNavigatioDao.insertUserAnnotation(guid, metadata);
			}
			
			//4. visual annotations
			VisualOverlaysDAO visualOverlaysDao = factory.getVisualOverlaysDAO();
			
			VisualObjectsDAO visualObjectsDao = factory.getVisualObjectsDAO();
			
			for(int site=0;site<record.numberOfSites;site++)
			{
				List<String> names = visualOverlaysDao.getAvailableVisualOverlays(sourceProjectId, guid, site);
				if(names!=null)
				{
					// create overlays
					for(String name:names)
					{
						VisualOverlay vo = visualOverlaysDao.getVisualOverlay(sourceProjectId, guid, new Dimension(0,0,0,site), name);
						visualOverlaysDao.createVisualOverlays(actorLogin, targetProjectId, guid, record.imageWidth, record.imageHeight, site, name, vo.handCreated);
					}
					
					for(int slice=0;slice<record.numberOfSlices;slice++)
					{
						for(int frame=0;frame<record.numberOfFrames;frame++) 
						{
							for(String name:names)
							{
								// read objects from the overlays
								VODimension imageCoordinate = new VODimension(frame, slice, site);
								List<VisualObject> objects = visualObjectsDao.getVisualObjects(sourceProjectId, guid, name, imageCoordinate);
								
								//put in target project
								visualObjectsDao.addVisualObjects(targetProjectId, guid, name, objects, imageCoordinate);
							}
						}
					}
					
					for(String name:names)
					{
						// delete from source project
						visualOverlaysDao.deleteVisualOverlays(sourceProjectId, guid, site, name);
					}
				}
			}
		
			//5. transfer history
			HistoryDAO historyDao = factory.getHistoryDAO();
			List<HistoryObject> history = historyDao.getRecordHistory(sourceProjectId, guid);
			if(history!=null)
			{
				for(HistoryObject h:history)
				{
					historyDao.insertHistory(targetProjectId, guid, h.getClient().name, h.getClient().version, h.getModifiedBy(), h.getModificationTime(), h.getAccessToken(), h.getType(), h.getDescription(), h.getArguments());
					historyDao.deleteHistory(sourceProjectId, guid);
				}
			}
		}
		
		// update disk usage
		updateProjectRecords(targetProjectId, ids.length, diskUsage);
		updateProjectRecords(sourceProjectId, -1*ids.length, -1*diskUsage);
	}
}
