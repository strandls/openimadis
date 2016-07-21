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

package com.strandgenomics.imaging.iclient;

import com.strandgenomics.imaging.icore.Rank;

/**
 * Administrative APIs - to manager projects and users
 * @author arunabha
 *
 */
public interface ImageSpaceManagement {
	
	/**
	 * Creates a new project with the specified name and notes. 
	 * Note that the login user must be a TeamLeader and above to create a new project
	 * Note that by default, the creator is assigned as the project manager
	 * @param projectName name of the project (must be unique across all projects)
	 * @param notes project description
	 * @param quota quota in GB
	 * @return newly created project
	 */
	public Project createNewProject(String projectName, String notes, double quota);
	
	/**
	 * Note that the login user needs to an Administrator for this API will be successfully 
	 * Allows a new user with the specified login and other details to access the system with
	 * external authentication - say LDAP
	 * @param login login name of the user, must be unique
	 * @param email email of the user
	 * @param rank rank of the user
	 * @return true is successful, false otherwise
	 */
	public boolean allowExternalUser(String login, String email, Rank rank);
	
	/**
	 * Note that the login user needs to an Administrator for this API will be successfully 
	 * Creates a new user with the specified login and other details
	 * @param login login name of the user, must be unique
	 * @param password password of the user
	 * @param email email of the user
	 * @param fullName full name of the user
	 * @param rank rank of the user
	 * @return true is successful, false otherwise
	 */
	public boolean createInternalUser(String login, String password, String email, String fullName, Rank rank);
	
	/**
	 * Deletes the specified project
	 * @param project the project to delete
	 * @return the handle to the long running task
	 */
	public Task deleteProject(String projectName);
	
	/**
	 * Archives the specified project
	 * @param project the project to delete
	 * @return the handle to the long running task
	 */
	public Task archiveProject(String projectName);

	/**
	 * Restore the specified archived project
	 * @param project the project to delete
	 * @return the handle to the long running task
	 */
	public Task restoreProject(String projectName);
}
