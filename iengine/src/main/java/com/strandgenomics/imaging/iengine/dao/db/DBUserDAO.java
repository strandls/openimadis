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
 * DBUserDAO.java
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
package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.UserDAO;
import com.strandgenomics.imaging.iengine.models.AuthenticationType;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserStatus;

public class DBUserDAO extends ImageSpaceDAO<User> implements UserDAO {

	DBUserDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "UserDAO.xml");
	}

	@Override
	public User findUser(String login) throws DataAccessException 
	{
		if(login == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.INFO, "DBUserDAO", "findUser", "login="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_USER_FOR_NAME");
        sqlQuery.setValue("LoginName", login, Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}

	@Override
	public List<User> findUser() throws DataAccessException 
	{
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_USERS");
        logger.logp(Level.INFO, "DBUserDAO", "findUser", "all user");

        RowSet<User> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	public List<User> listActiveUsers() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUserDAO", "findUser", "all user");

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ACTIVE_USERS");
		sqlQuery.setValue("Status", UserStatus.Active, Types.VARCHAR);

		RowSet<User> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public User insertUser(String login, String password, AuthenticationType authType, Rank rank,
			String emailID, String fullName) throws DataAccessException 
	{
		if(login == null || authType == null || rank == null) 
			throw new NullPointerException("unexpected null value");
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_USER");
        logger.logp(Level.INFO, "DBUserDAO", "insertUser", "login="+login);
        
        sqlQuery.setValue("Login", login, Types.VARCHAR);
        sqlQuery.setValue("Password",  password,  Types.VARCHAR);
        sqlQuery.setValue("AuthenticationType", authType.name(), Types.VARCHAR);
        sqlQuery.setValue("Rank",    rank.name(),  Types.VARCHAR);
        sqlQuery.setValue("EmailID", emailID, Types.VARCHAR);
        sqlQuery.setValue("FullName",  fullName,  Types.VARCHAR);

        updateDatabase(sqlQuery);

        return findUser(login);
	}

	@Override
	public boolean deleteUser(String login) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBUserDAO", "deleteUser", "user="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_USER_FOR_ID");
        sqlQuery.setValue("UserID", login, Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}

	@Override
	public boolean updateUser(User user) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBUserDAO", "updateUser", "user="+user);
        if(user == null) return false;

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_USER_FOR_ID");
        
        sqlQuery.setValue("UserID",   user.login,       Types.VARCHAR);
        sqlQuery.setValue("FullName", user.getName(),   Types.VARCHAR);
        sqlQuery.setValue("EmailID",  user.getEmail(),  Types.VARCHAR);
        sqlQuery.setValue("Status",  user.getStatus().name(),  Types.VARCHAR);
        sqlQuery.setValue("AuthType",  user.getAuthenticationType().name(),  Types.VARCHAR);
        sqlQuery.setValue("Rank",     user.getRank().name(),   Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}

	@Override
	public boolean updateUserPassword(String login, String password) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBUserDAO", "updateUserPassword", "userID="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PASSWORD_FOR_USERID");
        sqlQuery.setValue("Password",     password,  Types.VARCHAR);
        sqlQuery.setValue("UserID",       login,     Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}
	
	@Override
	public boolean updateUserStatus(String login, UserStatus status) throws DataAccessException 
	{
		if(status == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.INFO, "DBUserDAO", "updateUserStatus", "userID="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_STATUS_FOR_USERID");
        sqlQuery.setValue("Status",     status.name(),  Types.VARCHAR);
        sqlQuery.setValue("UserID",       login,     Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}
	
	@Override
	public boolean registerLogin(String login, Timestamp time, int loginCount) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBUserDAO", "registerLogin", "userID="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_LOGIN");
        sqlQuery.setValue("LastLogin",  time,        Types.TIMESTAMP);
        sqlQuery.setValue("LoginCount", loginCount,  Types.INTEGER);
        sqlQuery.setValue("UserID",     login,       Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}
	
	@Override
	public boolean registerLogout(String login, int loginCount) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBUserDAO", "registerLogout", "userID="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_LOGOUT");
        sqlQuery.setValue("LoginCount", loginCount,  Types.INTEGER);
        sqlQuery.setValue("UserID",     login,       Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}

	@Override
	public User createObject(Object[] columnValues) 
	{
		String login = (String)columnValues[0];
		String name  = (String)columnValues[1];
		
		String emailID  = (String)columnValues[2];
		String password = (String)columnValues[3];
		
		AuthenticationType authType = AuthenticationType.valueOf((String)columnValues[4]);
		Rank rank                   = Rank.valueOf((String)columnValues[5]);
		UserStatus status           = UserStatus.valueOf((String)columnValues[6]);
		
		Timestamp lastLogin = Util.getTimestamp(columnValues[7]);
		int loginCount      = Util.getInteger(columnValues[8]);

        return new User(login, name, emailID, password, authType, rank, status, lastLogin, loginCount);
	}

	@Override
	public List<User> getUsersForRank(Rank rank) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBUserDAO", "getUsersForRank", "all administrators");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_USERS_FOR_RANK");
        sqlQuery.setValue("Rank",     rank.name(),   Types.VARCHAR);

        RowSet<User> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
}
