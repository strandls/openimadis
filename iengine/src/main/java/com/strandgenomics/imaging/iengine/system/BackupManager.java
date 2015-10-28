package com.strandgenomics.imaging.iengine.system;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.backup.BackupService;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.BackupDataObject;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;

/**
 * manager class to handle project backup(archival) and restoration 
 * 
 * @author Anup Kulkarni
 */
public class BackupManager extends SystemManager {

	BackupManager()
	{}
	
	/**
	 * submits project archival request
	 * @param actorLogin current logged in user
	 * @param projectID specified project
	 * @throws IOException 
	 */
	public void submitProjectForArchival(String actorLogin, int projectID) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorLogin))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		
		File archiveRoot = Constants.getArchiveStorageRoot();
		if(!isSpaceAvailable(archiveRoot, project))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INSUFFICIENT_DISK_SPACE));
		}
		
		String locationName = SysManagerFactory.getStorageManager().getProjectStorageDirectory(project.getName()).getName();
		File backupLocation = new File(archiveRoot, locationName);
		if(project.getStatus() == ProjectStatus.Active)
		{
			// submit the request to worker
			try
			{
				submitProjectArchiveRequest(actorLogin, projectID, backupLocation.getAbsolutePath());
				// update the project status
				SysManagerFactory.getProjectManager().updateProjectDetails(actorLogin, project.getName(), project.getNotes(), ProjectStatus.ArchiveQ, project.getStorageQuota());
			}
			catch (IOException e)
			{
				logger.logp(Level.WARNING, "BackupManager", "submitProjectForArchival", "failed to submit the archival request ", e);
			}
		}
	}
	
	private boolean isSpaceAvailable(File dest, Project project)
	{
		long usableSpace = dest.getUsableSpace();
		double availableSpace = (usableSpace*1.0)/(1024*1024*1024); // converting to GB
		
		double usage = project.getSpaceUsage(); // in GB
		
		return availableSpace>=usage;
	}
	
	/**
	 * submits project restoration request
	 * @param actorLogin current logged in user
	 * @param projectID specified project
	 * @throws IOException 
	 */
	public void submitProjectForRestoration(String actorLogin, int projectID) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorLogin))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		
		if (project.getStatus() == ProjectStatus.Archived)
		{
			File archiveRoot = Constants.getArchiveStorageRoot();
			
			BackupDataObject details = getProjectBackupDetails(actorLogin, projectID);
			File restoreFrom = new File(archiveRoot, details.location);
			
			submitProjectRestoreRequest(actorLogin, projectID, restoreFrom.getAbsolutePath(), details.sign);
			// update the project status
			SysManagerFactory.getProjectManager().updateProjectDetails(actorLogin, project.getName(), project.getNotes(), ProjectStatus.RestorationQ, project.getStorageQuota());
		}
	}
	
	/**
	 * mark the project as archived
	 * @param actorLogin logged in user
	 * @param details details of the backed up project
	 * @throws IOException 
	 */
	public void markProjectAsArchived(String actorLogin, BackupDataObject details) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorLogin))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		Project project = SysManagerFactory.getProjectManager().getProject(details.projectId);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		factory.getBackupDAO().insertBackupData(details.projectId, details.location, details.sign);
		
		SysManagerFactory.getProjectManager().updateProjectDetails(actorLogin, project.getName(), project.getNotes(), ProjectStatus.Archived, project.getStorageQuota());
		
		removeFromArchiveCache(details.projectId);
	}
	
	/**
	 * returns the backup details of the project
	 * @param actorLogin
	 * @param projectId
	 * @return the backup details of the project
	 * @throws DataAccessException
	 */
	public BackupDataObject getProjectBackupDetails(String actorLogin, int projectId) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorLogin))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		return DBImageSpaceDAOFactory.getDAOFactory().getBackupDAO().getBackupData(projectId);
	}
	
	/**
	 * mark the project as restored
	 * @param actorLogin logged in user
	 * @param projectId specified project
	 * @throws IOException 
	 */
	public void markProjectAsRestored(String actorLogin, int projectId) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorLogin))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectId);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		factory.getBackupDAO().deleteBackupData(projectId);
		
		SysManagerFactory.getProjectManager().updateProjectDetails(actorLogin, project.getName(), project.getNotes(), ProjectStatus.Active, project.getStorageQuota());
		
		removeFromRestoreCache(projectId);
	}
	
	private void removeFromRestoreCache(int projectID) throws IOException
	{
		logger.logp(Level.INFO, "BackupManager", "removeFromRestoreCache", "clearing restore cache for project "+projectID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
        BackupService serviceStub = null;
		try 
		{
			serviceStub = (BackupService) registry.lookup(BackupService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "BackupManager", "removeFromRestoreCache", "error in clearing restore cache for project "+projectID);
			throw new IOException(e);
		}
		
        serviceStub.cleanRestoreCache(projectID);
	}
	
	private void removeFromArchiveCache(int projectID) throws IOException
	{
		logger.logp(Level.INFO, "BackupManager", "removeFromArchiveCache", "clearing archival cache for project "+projectID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
        BackupService serviceStub = null;
		try 
		{
			serviceStub = (BackupService) registry.lookup(BackupService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "BackupManager", "removeFromArchiveCache", "error in cancelling archival request for project "+projectID);
			throw new IOException(e);
		}
		
        serviceStub.cleanArchivalCache(projectID);
	}
	
	/**
	 * cancells project archiving
	 * @param actorLogin logged in user
	 * @param projectID specified project
	 * @throws IOException 
	 */
	public void cancelProjectArchiving(String actorLogin, int projectID) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(actorLogin))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		switch (project.getStatus())
		{
			case Archived:
				// project is already archived
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_IS_ARCHIVED));
			case ArchiveQ:
				submitCancelArchivalRequest(actorLogin, projectID);
				break;
			case Archiving:
				submitCancelArchivalRequest(actorLogin, projectID);
				break;
			case Active:
			default:
				break;
		}
	}
	
	private synchronized void submitCancelArchivalRequest(String actorLogin, int projectID) throws IOException
	{
		logger.logp(Level.INFO, "BackupManager", "submitCancelArchivalRequest", "cancelling archival request for project "+projectID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
        BackupService serviceStub = null;
		try 
		{
			serviceStub = (BackupService) registry.lookup(BackupService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "BackupManager", "submitCancelArchivalRequest", "error in cancelling archival request for project "+projectID);
			throw new IOException(e);
		}
		
        serviceStub.cancelArchivalRequest(actorLogin, projectID);
	}
	
	private synchronized void submitCancelRestoreRequest(String actorLogin, int projectID) throws IOException
	{
		logger.logp(Level.INFO, "BackupManager", "submitCancelRestoreRequest", "cancelling archival restore for project "+projectID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
        BackupService serviceStub = null;
		try 
		{
			serviceStub = (BackupService) registry.lookup(BackupService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "BackupManager", "submitCancelRestoreRequest", "error in cancelling restore request for project "+projectID);
			throw new IOException(e);
		}
		
        serviceStub.cancelRestorationRequest(actorLogin, projectID);
	}
	
	private synchronized void submitProjectArchiveRequest(String actorLogin, int projectID, String backupPath) throws IOException
	{
		logger.logp(Level.INFO, "BackupManager", "submitProjectArchiveRequest", "submitting archival request for project "+projectID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
        BackupService serviceStub = null;
		try 
		{
			serviceStub = (BackupService) registry.lookup(BackupService.class.getCanonicalName());
		} 
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "BackupManager", "submitProjectArchiveRequest", "error in submitting archival request for project "+projectID);
			throw new IOException(e);
		}
		
        serviceStub.submitArchivalRequest(actorLogin, projectID, backupPath);
	}
	
	private synchronized void submitProjectRestoreRequest(String actorLogin, int projectID, String location, String sign) throws IOException
	{
		logger.logp(Level.INFO, "BackupManager", "submitProjectRestoreRequest", "submitting restoration request for project "+projectID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
        BackupService serviceStub = null;
		try 
		{
			serviceStub = (BackupService) registry.lookup(BackupService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "BackupManager", "submitProjectArchiveRequest", "error in submitting archival request for project "+projectID);
			throw new IOException(e);
		}
		
        serviceStub.submitRestorationRequest(actorLogin, projectID, location, sign);
	}

	/**
	 * cancel project restoration
	 * @param loginUser
	 * @param projectId
	 * @throws IOException
	 */
	public void cancelProjectRestoring(String loginUser, int projectId) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().isAdministrator(loginUser))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		Project project = SysManagerFactory.getProjectManager().getProject(projectId);
		switch (project.getStatus())
		{
			case Active:
				// project is already restored
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_IS_ARCHIVED));
			case RestorationQ:
				submitCancelRestoreRequest(loginUser, projectId);
				break;
			case Restoring:
				submitCancelRestoreRequest(loginUser, projectId);
				break;
			case Archived:
			default:
				break;
		}
	}
}
