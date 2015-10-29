/*
 * ImageSpaceManagement.java
 *
 * AVADIS Image Management System
 * Web-service definition
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

package com.strandgenomics.imaging.iserver.services.def.manage;

import java.rmi.RemoteException;


/**
 * Managing projects and users
 * @author arunabha
 *
 */
public interface ImageSpaceManagement {
	
	/**
	 * Creates a new project with the specified name and notes. 
	 * Note that the login user must be a TeamLeader and above to create a new project
	 * Note that by default, the creator is assigned as the project manager
	 * @param accessToken the access token required to make this call
	 * @param projectName name of the project (must be unique across all projects)
	 * @param notes project description
	 * @param quota quota in GB
	 * @return newly created project
	 */
	public boolean createNewProject(String accessToken, String projectName, String notes, double quota)
			throws RemoteException;
	
    /**
     * Adds the specified users as the member of the this project.
     * Note that the login user must be the project Manager of this project for this call to be successful
     * @param accessToken the access token required to make this call
     * @param users list of users to add to the project
     * @param permission a valid permission
     * @see com.strandgenomics.imaging.icore.Permission
     * @return the list of users who are successfully added
     */
    public void addProjectMembers(String accessToken, String projectName, String[] userlogins, String permission) 
    		throws RemoteException;
    
	/**
	 * List all legal members of the specified project, 
	 * may return null if the concerned user do not have permission to get this list
	 * @param accessToken the access token required to make this call
	 * @return all legal members of this projects
	 */
    public User[] getProjectMembers(String accessToken, String projectName) throws RemoteException;
    
	/**
	 * Renames the specified project - note that project names are unique
	 * @param accessToken the access token required to make this call 
	 * @param oldName existing name of the project
	 * @param newName new name of the project
	 * @throws RemoteException
	 */
    public boolean renameProject(String accessToken, String oldName, String newName)
    		throws RemoteException;
	
	/**
	 * Returns the project Manager(s) for the specified project
	 * @param accessToken the access token required to make this call
	 * @param projectName name of the project
	 * @return the project manager of this project
	 */
	public User[] getProjectManager(String accessToken, String projectName)
			throws RemoteException;
	
    /**
     * Deletes this project - place a request to delete this project. Projects are not deleted right away
     * Note that the login user may not have permission to delete, in that case an exception will be thrown
     * @param accessToken the access token required to make this call
     */
    public Task deleteProject(String accessToken, String projectName) 
    		throws RemoteException;
    
    /**
     * deletes the specified record
     * @param accessToken the access token required to make this call
     * @param guid the GUID of the record to delete
     * @throws RemoteException
     */
    public void deleteRecord(String accessToken, long guid)
    		throws RemoteException;
    
    /**
     * deletes the specified archive
     * @param accessToken the access token required to make this call
     * @param archiveSignature the signature of the archive to delete
     * @throws RemoteException
     */
    public void deleteArchive(String accessToken, String archiveSignature)
    		throws RemoteException;

    /**
     * Archives this project - place a request to archive this project
     * Note that the login user may not have permission to archive, in that case an exception will be thrown
     * @param accessToken the access token required to make this call
     */
    public Task archiveProject(String accessToken, String projectName) 
    		throws RemoteException;
    
    /**
     * Restores this project from archive - place a request to restore this project
     * Note that the login user may not have permission to restore, in that case an exception will be thrown
     * @param accessToken the access token required to make this call
     */
    public Task restoreProject(String accessToken, String projectName) 
    		throws RemoteException;
    
    /**
     * Returns the status of the specified job
     * @param accessToken the access token required to make this call
     * @param job the job under consideration 
     * @return the current status of the job
     */
    public String getJobStatus(String accessToken, Task job) throws RemoteException;
	
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
	public boolean allowExternalUser(String accessToken, String login, String email, String rank)
			throws RemoteException;
	
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
	public boolean createInternalUser(String accessToken, String login, String password, String email, String fullName, String rank)
			throws RemoteException;
	
    
    /**
     * Transfer the specified archive to the given project 
     * @param accessToken the access token required to make this call
     * @param archiveSignature the archive under consideration
     * @param target the target project
     */
    public void transfer(String accessToken, String archiveSignature, String targetProjectName) 
    		throws RemoteException;
}
