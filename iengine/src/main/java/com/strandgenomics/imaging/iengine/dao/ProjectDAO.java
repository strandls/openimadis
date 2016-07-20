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
 * ProjectDAO.java
 *
 * AVADIS Image Management System
 * Data Access Components
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
package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.UserMembership;

/**
 * This is an interface which will be implemented by database specific
 * code. It list CRUD (create, retrieve, update, delete) APIs that all UserDAOs must support.
 *
 * Data Access Object (DAO) abstract and encapsulate all access to the data source.
 * The DAO manages the connection with the data source to obtain and store data.
 *
 * This layer maps the relational data stored in the database to
 * the objects needed by the application
 *
 * @author arunabha
 */
public interface ProjectDAO {
	
	/**
	 * Returns the role of the specified user within the specified project
	 */
	public Permission getRole(String login, int projectID) throws DataAccessException;
	
   /**
    * Returns the requested Project, or <code>null</code> if no such Project was found
    * in the repository
    */
    public Project findProject(int projectID) throws DataAccessException;

    /**
     * Returns the Project object associated with the specified Project name
     * from the repository, or null if it is not there
     */
    public Project findProject(String name) throws DataAccessException;
    
    /**
     * List all available Projects with the specified status
     */
    public List<Project> findProjectByStatus(ProjectStatus status) 
    		throws DataAccessException;
    
    /**
     * List all available Projects with the specified status the specified user is member of
     */
    public List<Project> findProject(String userID, ProjectStatus status) 
    		throws DataAccessException;

    /**
     * Returns a list of user-ids who are member of the specified project
     * @param projectID  the id of the project under consideration
     * @return a list of user-ids who are member of the specified project
     * @throws DataAccessException
     */
    public String[] getProjectMembers(int projectID, Permission permission) 
    		throws DataAccessException;
    
    public List<UserMembership> getMemberships(Integer projectID , String userLogin) throws DataAccessException;
    
    /**
     * removes the membership of the specified user from the specified project
     * Typically, a Team Leader or above can remove a PM
     * @param projectID the id of the project under consideration
     * @param userID the user whose membership is going to be revoked
     * @throws DataAccessException
     */
    public boolean removeProjectMembership(int projectID, String userID)
    		throws DataAccessException;
    
    /**
     * Adds the specified user to the specified project 
     * @param projectID the id of the project under consideration
     * @param userID the user whose membership is going to be added
     * @throws DataAccessException
     */
    public boolean addProjectMembership(int projectID, String userID, Permission role)
    		throws DataAccessException;
    
    public boolean updateProjectRecords(int projectID, int noOfRecords, double diskUsage)
    		throws DataAccessException;
    
    /**
     * Updates the status of the specified project
     * @param projectID the id of the project under consideration
     * @param status the new status
     * @return true if successful, false otherwise
     * @throws DataAccessException
     */
    public boolean updateProjectStatus(int projectID, ProjectStatus status)
    		throws DataAccessException;

    /**
     * deletes the specified project
     * @param projectID
     * @return
     * @throws DataAccessException
     */
	public boolean deleteProject(int projectID) throws DataAccessException;

	/**
	 * Insert into Project Table relevant information
	 * @param name name of the project
	 * @param notes notes associated with the project
	 * @param createdBy user creating the project
	 * @param quota quota in GB
	 * @param location name of the actual storage folder in the file-system
	 * @throws DataAccessException
	 */
	public Project insertProjectEntries(String name, String notes, String createdBy,
			double quota, String location) throws DataAccessException;

	/**
	 * Location of a project is unique across all projects (just like its name)
	 * @param newLocation the storage directory name of the project
	 * @return the relevant project
	 */
	public Project findByLocation(String newLocation) throws DataAccessException;
	
	/**
	 * lists all the projects managed by manger
	 * @param managerLogin
	 * @return
	 * @throws DataAccessException
	 */
	public List<Project> findByManager(String managerLogin) throws DataAccessException;

	/**
	 * updates project's notes, status and storage quota
	 * @param id specified project id
	 * @param notes new notes
	 * @param status new status
	 * @param storageQuota new storage quota
	 * @throws DataAccessException 
	 */
	public void updateProjectDetails(int id, String notes, ProjectStatus status, double storageQuota) throws DataAccessException;

	/**
	 * returns all the projects, users and their roles in respective projects
	 * @throws DataAccessException 
	 */
	public List<UserMembership> getAllUserMemberships() throws DataAccessException;

	/**
	 * adds new user permission
	 * @param projectID specified project 
	 * @param userLogin  specified user 
	 * @param permission specified permission
	 * @throws DataAccessException 
	 */
	public void addUserPermission(int projectID, String userLogin, Permission permission) throws DataAccessException;
	
	/**
	 * updates user permission
	 * @param projectID specified project 
	 * @param userLogin  specified user 
	 * @param newPermission specified permission
	 * @throws DataAccessException 
	 */
	public void updateUserPermission(int projectID, String userLogin, Permission newPermission) throws DataAccessException;

	/**
	 * returns list of all projects
	 * @return
	 * @throws DataAccessException
	 */
	public List<Project> findAllProjects() throws DataAccessException;

	
}
