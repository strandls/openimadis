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
 * DBUserPreferencesDAO.java
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

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.UserPreferencesDAO;
import com.strandgenomics.imaging.iengine.models.LegendField;
import com.strandgenomics.imaging.iengine.models.LegendLocation;
import com.strandgenomics.imaging.iengine.models.SearchColumn;

/**
 * gives access to user preferences about navigator fields and projects
 * 
 * @author Anup Kulkarni
 */
public class DBUserPreferencesDAO extends ImageSpaceDAO<SearchColumn> implements UserPreferencesDAO {
	
	public static final int     DEFAULT_BIN_COUNT    = 10;
	public static final boolean DEFAULT_BIN_ORDERING = true;

	public DBUserPreferencesDAO(DBImageSpaceDAOFactory factory,	ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "UserPreferencesDAO.xml");
	}
	
	@Override
	public Timestamp getLastAccessTime(String userLogin, int projectID) throws DataAccessException
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getLastAccessTime", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LAST_ACCESS_TIME");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
		
		Timestamp accessTime = getTimestamp(sqlQuery);
        if(accessTime == null)
        {
        	createDefaultPreference(userLogin, projectID);
        	accessTime = new Timestamp(System.currentTimeMillis());
        }
        
        return accessTime;
	}

	@Override
	public int getNumberOfNavigatorBins(String userLogin, int projectID) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getNumberOfNavigatorBins", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_BIN_COUNT");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
        
        Integer binCount = getInteger(sqlQuery);
        if(binCount == null)
        {
        	createDefaultPreference(userLogin, projectID);
        }
        return DEFAULT_BIN_COUNT;
	}

	@Override
	public boolean isBinsAreInAscendingOrder(String userLogin, int projectID) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "isBinsAreInAscendingOrder", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_BIN_ORDER");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
        
        Boolean binCount = getBoolean(sqlQuery);
        if(binCount == null)
        {
        	createDefaultPreference(userLogin, projectID);
        }
        return DEFAULT_BIN_ORDERING;
	}

	@Override
	public List<SearchColumn> getSpreadSheetColumns(String userLogin, int projectID) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getSpreadSheetColumns", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SPREADSHEET_COLUMNS");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
		
		return getColumns(userLogin, projectID, sqlQuery);
	}

	@Override
	public List<SearchColumn> getNavigationColumns(String userLogin, int projectID) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getNavigationColumns", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_NAVIGATION_COLUMNS");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
		
		return getColumns(userLogin, projectID, sqlQuery);
	}
	
	@Override
	public List<SearchColumn> getOverlayColumns(String userLogin, int projectID)
			throws DataAccessException {
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getOverlayColumns", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_NAVIGATION_COLUMNS");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
		
		return getColumns(userLogin, projectID, sqlQuery);
	}

	@Override
	public int[] getRecentProjects(String userLogin, int maxProjects) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getRecentProjects", "userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECENT_PROJECTS");
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
		sqlQuery.setValue("Limit", maxProjects, Types.INTEGER);
        
		return getRowsWithIntValues(sqlQuery);
	}

	@Override
	public void setSpreadSheetColumns(String userLogin, int projectID, List<SearchColumn> columns) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "setSpreadSheetColumns", "projectID="+projectID +",userLogin="+userLogin);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_SPREADSHEET_COLUMNS");
		sqlQuery.setValue("ProjectID", projectID,            Types.BIGINT);
		sqlQuery.setValue("User",      userLogin,            Types.VARCHAR);
		sqlQuery.setValue("Columns",   toByteArray(columns), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void setNavigationColumns(String userLogin, int projectID, List<SearchColumn> columns) throws DataAccessException
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "setNavigationColumns", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_NAVIGATION_COLUMNS");
		sqlQuery.setValue("ProjectID", projectID,            Types.BIGINT);
		sqlQuery.setValue("User",      userLogin,            Types.VARCHAR);
		sqlQuery.setValue("Columns",   toByteArray(columns), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void setOverlayColumns(String userLogin, int projectID,
			List<SearchColumn> columns) throws DataAccessException {
		logger.logp(Level.INFO, "DBUserPreferencesDAO", "setOverlayColumns", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_OVERLAY_COLUMNS");
		sqlQuery.setValue("ProjectID", projectID,            Types.BIGINT);
		sqlQuery.setValue("User",      userLogin,            Types.VARCHAR);
		sqlQuery.setValue("Columns",   toByteArray(columns), Types.BLOB);
		
		updateDatabase(sqlQuery);
	
	}

	@Override
	public void createDefaultPreference(String userLogin, int projectID) 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "createDefaultPreference", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("CREATE_DEFAULT_PREFERENCES");
		sqlQuery.setValue("ProjectID", projectID, Types.BIGINT);
		sqlQuery.setValue("User",      userLogin, Types.VARCHAR);
		
		try
		{
			updateDatabase(sqlQuery);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "DBUserPreferencesDAO", "createDefaultPreference", "projectID="+projectID +",userLogin="+userLogin, ex);
		}
	}
	
	@Override
	public void updateProjectUsage(String userLogin, int projectID) throws DataAccessException 
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "createDefaultPreference", "projectID="+projectID +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PROJECT_USE");
		sqlQuery.setValue("ProjectID",  projectID, Types.BIGINT);
		sqlQuery.setValue("User",       userLogin, Types.VARCHAR);
		sqlQuery.setValue("AccessTime", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public SearchColumn createObject(Object[] columnValues) 
	{
		return null; //not used
	}
	
	private List<SearchColumn> getColumns(String userLogin, int projectID, SQLQuery sqlQuery) throws DataAccessException
	{
		List<SearchColumn> columns = null;
		
		Object value = getObject(sqlQuery);
		
		try 
		{		 
		    columns = (List<SearchColumn> ) toJavaObject( (DataSource) value);
		} 
		catch (Exception e) 
		{
			logger.logp(Level.WARNING, "DBUserPreferencesDAO", "getColumns", "projectID="+projectID +",userLogin="+userLogin,e);
		}
		
		if(columns == null)
		{
			try 
			{
				getLastAccessTime(userLogin, projectID);
			} 
			catch (DataAccessException e) 
			{
				logger.logp(Level.WARNING, "DBUserPreferencesDAO", "getColumns", "projectID="+projectID +",userLogin="+userLogin,e);
			}
		}

        return columns;
	}

	@Override
	public List<Channel> getChannels(String userLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "getChannelColors", "guid="+guid +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_CUSTOM_CHANNELS");
		sqlQuery.setValue("GUID", guid,      Types.BIGINT);
		sqlQuery.setValue("User", userLogin, Types.VARCHAR);
		
	    Object value = getObject(sqlQuery);
	    return value == null ? null : ( List<Channel> ) toJavaObject( (DataSource) value);
	}

	@Override
	public void addChannels(String userLogin, long guid, List<Channel> channels) throws DataAccessException 
	{
		if(channels == null || channels.isEmpty())
			return;
		
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "addChannelColors", "guid="+guid +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_CUSTOM_CHANNELS");
		sqlQuery.setValue("GUID",   guid,                  Types.BIGINT);
		sqlQuery.setValue("User",   userLogin,             Types.VARCHAR);
		sqlQuery.setValue("Channels", toByteArray(channels), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void updateChannels(String userLogin, long guid, List<Channel> channels) throws DataAccessException
	{
		if(channels == null || channels.isEmpty())
			return;
		
		logger.logp(Level.FINE, "DBUserPreferencesDAO", "updateChannelColors", "guid="+guid +",userLogin="+userLogin);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_CUSTOM_CHANNELS");
		sqlQuery.setValue("GUID",   guid,                  Types.BIGINT);
		sqlQuery.setValue("User",   userLogin,             Types.VARCHAR);
		sqlQuery.setValue("Channels", toByteArray(channels), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void updateChannels(long guid, List<Channel> channels) throws DataAccessException
	{
		if(channels == null || channels.isEmpty())
			return;
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_CUSTOM_CHANNELS_FOR_ALL_USERS");
		sqlQuery.setValue("GUID",   guid,                  Types.BIGINT);
		sqlQuery.setValue("Channels", toByteArray(channels), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
	
	
	
	@Override
	public void deleteUserPreferences(int projectID , String userLogin) throws DataAccessException 
	{
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_USER_PROJECT_ENTRY");
        sqlQuery.setValue("ProjectID", projectID, Types.INTEGER);
        sqlQuery.setValue("User", userLogin, Types.VARCHAR);

        
        updateDatabase(sqlQuery);
	}

	@Override
	public void setLegendFields(String actorLogin, List<LegendField> chosenFields) throws DataAccessException
	{
		if(chosenFields == null || chosenFields.isEmpty())
			return;
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_LEGENDS");
		sqlQuery.setValue("User",   actorLogin,                  Types.VARCHAR);
		sqlQuery.setValue("Legends", toByteArray(chosenFields), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void updateLegendFields(String actorLogin, List<LegendField> chosenFields) throws DataAccessException
	{
		if(chosenFields == null || chosenFields.isEmpty())
			return;
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_LEGENDS");
		sqlQuery.setValue("User",   actorLogin,                  Types.VARCHAR);
		sqlQuery.setValue("Legends", toByteArray(chosenFields), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public List<LegendField> getLegends(String actorLogin) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LEGENDS");
		sqlQuery.setValue("User",   actorLogin,                  Types.VARCHAR);
		
		Object value = getObject(sqlQuery);
	    return value == null ? null : ( List<LegendField> ) toJavaObject( (DataSource) value);
	}
	
	@Override
	public LegendLocation getLegendLocation(String actorLogin) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LEGEND_LOCATION");
		sqlQuery.setValue("User",   actorLogin,                  Types.VARCHAR);
		
		String value = getString(sqlQuery);
		if(value == null || value.isEmpty())
		{
			setLegendLocation(actorLogin, LegendLocation.TOPLEFT);
			return LegendLocation.TOPLEFT;
		}
	    return LegendLocation.valueOf(value);
	}
	
	@Override
	public void setLegendLocation(String actorLogin, LegendLocation location) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_LEGEND_LOCATION");
		sqlQuery.setValue("User",   actorLogin,                  Types.VARCHAR);
		sqlQuery.setValue("Location",   location.name(),                  Types.VARCHAR);

		updateDatabase(sqlQuery);
	}
}
