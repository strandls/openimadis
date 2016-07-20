/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * ImageSpaceManagementImpl.java
 *
 * AVADIS Image Management System
 * Web Service Implementation
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
package com.strandgenomics.imaging.iserver.services.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.models.AuthenticationType;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.ws.manage.ImageSpaceManagement;
import com.strandgenomics.imaging.iserver.services.ws.manage.Task;
import com.strandgenomics.imaging.iserver.services.ws.manage.User;

/**
 * Implementation of iManage webservice
 * @author arunabha
 *
 */
public class ImageSpaceManagementImpl implements ImageSpaceManagement, Serializable {
	
	private static final long serialVersionUID = 276586843632433849L;
	private transient Logger logger = null;
	
	public ImageSpaceManagementImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}
	
	/**
	 * Creates a new project with the specified name and notes. 
	 * Note that the login user must be a TeamLeader and above to create a new project
	 * Note that by default, the creator is assigned as the project manager
	 * @param accessToken the access token required to make this call
	 * @param projectName name of the project (must be unique across all projects)
	 * @param notes project description
	 * @param diskQuota disk space quota in GB
	 * @return newly created project
	 */
	@Override
	public boolean createNewProject(String accessToken, String projectName, String notes, double diskQuota)
			throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);

		try
		{
			logger.logp(Level.INFO, "ImageSpaceManagementImpl", "createNewProject", "creating project "+ projectName);

			Project proj = SysManagerFactory.getProjectManager().createNewProject(actorLogin, projectName, notes, diskQuota);
			return proj != null;
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceManagementImpl", "createNewProject", "error", ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Adds the specified users as the member of the this project.
     * Note that the login user must be the project Manager of this project for this call to be successful
     * @param accessToken the access token required to make this call
     * @param users list of users to add to the project
     * @return the list of users who are successfully added
     */
	@Override
    public void addProjectMembers(String accessToken, String projectName, String[] userlogins, String permission) 
    		throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "addProjectMembers", "adding members to project "+projectName +" with permission "+permission);
		if(userlogins == null || userlogins.length == 0 || projectName == null)
			return;
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceManagementImpl", "addProjectMembers", "creating project "+ projectName);
			Permission p = Permission.valueOf(permission);

			SysManagerFactory.getProjectManager().addProjectMembers(actorLogin, projectName, p, userlogins);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceManagementImpl", "addProjectMembers", "error", ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    		
    
	/**
	 * List all legal members of the specified project, 
	 * may return null if the concerned user do not have permission to get this list
	 * @param accessToken the access token required to make this call
	 * @return all legal members of this projects
	 */
	@Override
    public User[] getProjectMembers(String accessToken, String projectName) throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "getProjectMembers", "getting members of project "+projectName);
		if(projectName == null)
			return null;
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceManagementImpl", "getProjectMembers", "getting memebers "+ projectName +" by "+actorLogin);

			return CoercionHelper.toRemoteUser( SysManagerFactory.getProjectManager().getProjectMembers(actorLogin, projectName) );
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceManagementImpl", "getProjectMembers", "error", ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
	/**
	 * Renames the specified project - note that project names are unique
	 * @param accessToken the access token required to make this call 
	 * @param oldName existing name of the project
	 * @param newName new name of the project
	 * @throws RemoteException
	 */
	@Override
    public boolean renameProject(String accessToken, String oldName, String newName)
    		throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "renameProject", "renaming project, oldName="+oldName +", newName="+newName);
		return false; //TODO
	}

	/**
	 * Returns the project Manager(s) for the specified project
	 * @param accessToken the access token required to make this call
	 * @param projectName name of the project
	 * @return the project manager of this project
	 */
	@Override
	public User[] getProjectManager(String accessToken, String projectName)
			throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "getProjectManager", "pm of project, projectName="+projectName);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceManagementImpl", "getProjectMembers", "getting managers for "+ projectName +" by "+actorLogin);
			return CoercionHelper.toRemoteUser( SysManagerFactory.getProjectManager().getProjectManager(actorLogin, projectName) );
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceManagementImpl", "getProjectMembers", "error", ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Deletes this project - place a request to delete this project. Projects are not deleted right away
     * Note that the login user may not have permission to delete, in that case an exception will be thrown
     * @param accessToken the access token required to make this call
     */
	@Override
    public Task deleteProject(String accessToken, String projectName) 
    		throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "deleteProject", "deleting projectName="+projectName);
		return null; //TODO
	}

    /**
     * Archives this project - place a request to archive this project
     * Note that the login user may not have permission to archive, in that case an exception will be thrown
     * @param accessToken the access token required to make this call
     */
	@Override
    public Task archiveProject(String accessToken, String projectName) 
    		throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "archiveProject", "archiving projectName="+projectName);
		return null; //TODO
	}
    
    /**
     * Restores this project from archive - place a request to restore this project
     * Note that the login user may not have permission to restore, in that case an exception will be thrown
     * @param accessToken the access token required to make this call
     */
	@Override
    public Task restoreProject(String accessToken, String projectName) 
    		throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "restoreProject", "restoring projectName="+projectName);
		return null; //TODO
	}
    
    /**
     * Returns the status of the specified job
     * @param accessToken the access token required to make this call
     * @param job the job under consideration 
     * @return the current status of the job
     */
	@Override
    public String getJobStatus(String accessToken, Task job) throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "getJobStatus", "getting ststus of Task "+job.getId());
		return null; //TODO
	}
	
	/**
	 * Note that the login user needs to an Administrator for this API will be successfully 
	 * Allows a new user with the specified login and other details to access the system with
	 * external authentication - say LDAP
	 * @param accessToken the access token required to make this call
	 * @param login login name of the user, must be unique
	 * @param email email of the user
	 * @param rank rank of the user
	 * @return true is successful, false otherwise
	 */
	@Override
	public boolean allowExternalUser(String accessToken, String login, String email, String rank)
			throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "allowExternalUser", "allowing external user "+login);
		return false; //TODO
	}
	
	/**
	 * Note that the login user needs to an Administrator for this API will be successfully 
	 * Creates a new user with the specified login and other details
	 * @param accessToken the access token required to make this call
	 * @param login login name of the user, must be unique
	 * @param password password of the user
	 * @param email email of the user
	 * @param fullName formal name of the user
	 * @param rank rank of the user
	 * @return true is successful, false otherwise
	 */
	@Override
	public boolean createInternalUser(String accessToken, String login, String password, String email, String fullName, String rank)
			throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);

		try
		{
			logger.logp(Level.INFO, "ImageSpaceManagementImpl", "createInternalUser", "creating internal user "+ login +" by "+actorLogin);

			SysManagerFactory.getUserManager().createUser(actorLogin, login, password, 
					AuthenticationType.Internal, Rank.valueOf(rank), email, fullName);
			return true;
		}
		catch(Exception ex)
		{
			throw new RemoteException(ex.getMessage());
		}
	}
	
    
    /**
     * Transfer the specified archive to the given project 
     * @param accessToken the access token required to make this call
     * @param archiveSignature the archive under consideration
     * @param target the target project
     */
	@Override
    public void transfer(String accessToken, String archiveSignature, String targetProjectName) 
    		throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		
		logger.logp(Level.INFO, "ImageSpaceManagementImpl", "transfer", "transferring experiment to "+targetProjectName);
		//TODO
	}

	
    /**
     * deletes the specified record
     * @param accessToken the access token required to make this call
     * @param guid the GUID of the record to delete
     * @throws RemoteException
     */
	@Override
    public void deleteRecord(String accessToken, long guid) throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		// TODO Auto-generated method stub
		
	}


    /**
     * deletes the specified archive
     * @param accessToken the access token required to make this call
     * @param archiveSignature the signature of the archive to delete
     * @throws RemoteException
     */
	@Override
    public void deleteArchive(String accessToken, String archiveSignature) throws RemoteException
	{
		String actorLogin = Service.MANAGEMENT.validateAccessToken(accessToken);
		// TODO Auto-generated method stub
		
	}
}
