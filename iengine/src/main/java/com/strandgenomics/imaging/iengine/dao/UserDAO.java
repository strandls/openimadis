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
 * UserDAO.java
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

import java.sql.Timestamp;
import java.util.List;

import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.AuthenticationType;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserStatus;

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
public interface UserDAO {

    /**
     * Returns the user object associated with the specified user login
     * from the repository
     */
    public User findUser(String login) throws DataAccessException;

    /**
     * List all available Users
     */
    public List<User> findUser() throws DataAccessException;
    
    /**
     * Insert the specified User into the repository
     * @return the User object associated with the inserted User
     */
	public User insertUser(String login, String password, AuthenticationType authType, Rank rank,
			String emailID, String fullName) throws DataAccessException;

    /**
     * Delete the User Object from the repository  for the specified userID
     * @return deletion status, true if successful, false otherwise
     */
    public boolean deleteUser(String userID) throws DataAccessException;

    /**
     * Update the User Entry in the repository with the specified user object
     * updates all field except the password field
     * @return updation status, true if successful, false otherwise
     */
    public boolean updateUser(User data) throws DataAccessException;

    /**
     * Update the User Password in the repository with the specified user object
     * @return update status, true if successful, false otherwise
     */
    public boolean updateUserPassword(String userID, String password) throws DataAccessException;

    /**
     * Update users status
     * @param userID relevant user id
     * @param status the new status
     * @return true if successful, false otherwise
     * @throws DataAccessException
     */
	public boolean updateUserStatus(String userID, UserStatus status) throws DataAccessException;

	/**
	 * Call this method after a user logins into the system 
	 * @param userID relevant user id
	 * @param time current time
	 * @param number of active logins
	 * @return true if successful, false otherwise
	 * @throws DataAccessException
	 */
	public boolean registerLogin(String userID, Timestamp time, int loginCount) throws DataAccessException;

	/**
	 * Call this method after a user logs out of the system
	 * @param userID relevant user id
	 * @param loginCount the login count
	 * @return true if successful, false otherwise
	 * @throws DataAccessException
	 */
	public boolean registerLogout(String userID, int loginCount) throws DataAccessException;

	/**
	 * returns all the users who are administrators
	 * @return the users who are administrators
	 * @throws DataAccessException 
	 */
	public List<User> getUsersForRank(Rank rank) throws DataAccessException;

	/**
	 * list all active users
	 * @return list all active users
	 * @throws DataAccessException 
	 */
	public List<User> listActiveUsers() throws DataAccessException;
}
