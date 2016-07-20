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

package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.HistoryDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.Project;

public class DBHistoryDAO extends StorageDAO<HistoryObject> implements HistoryDAO {

	public DBHistoryDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "HistoryDAO.xml");
	}

	@Override
	public void insertHistory(int projectID, long guid, String clientName, String clientVersion, String modifiedBy, long modificationTime, String accessToken, HistoryType type, String description, List<String>args) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "insertHistory", "inserting history item for record = "+guid+" for app = "+clientName+" modified by = "+modifiedBy+" history type = "+ type+" access token = "+accessToken);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_HISTORY_ITEM");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",  guid,        Types.BIGINT);
		sqlQuery.setValue("ClientName", clientName,       Types.VARCHAR);
		sqlQuery.setValue("ClientVersion",   clientVersion,      Types.VARCHAR);
		sqlQuery.setValue("ModifiedBy",   modifiedBy,      Types.VARCHAR);
		sqlQuery.setValue("AccessToken",   accessToken,      Types.VARCHAR);
		sqlQuery.setValue("ModificationTime", new Timestamp(modificationTime),   Types.TIMESTAMP);
		sqlQuery.setValue("ModificationType",    type.name(),    Types.VARCHAR);
		sqlQuery.setValue("Desc",   description,      Types.VARCHAR);
		sqlQuery.setValue("Args",   toByteArray(args),      Types.BLOB);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public List<HistoryObject> getRecordHistory(int projectID, long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "getRecordHistory", "project id "+projectID+"guid +"+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_HISTORY_FOR_RECORD");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		
		RowSet<HistoryObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public List<HistoryObject> getClientHistory(int projectID, String clientName, String clientVersion) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "getClientHistory", "project id "+projectID+"clientName +"+clientName);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_HISTORY_FOR_APPLICATION");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ClientName",    clientName,         Types.VARCHAR);
		sqlQuery.setValue("ClientVersion",    clientVersion,         Types.VARCHAR);
		
		RowSet<HistoryObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}
	
	@Override
	public List<HistoryObject> getAccessTokenHistory(int projectID, String accessToken) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "getAccessTokenHistory", "project id "+projectID+"accessToken +"+accessToken);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_HISTORY_FOR_ACCESS_TOKEN");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("AccessToken",    accessToken,         Types.VARCHAR);
		
		RowSet<HistoryObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}
	
	@Override
	public List<HistoryObject> getFilteredHistory(int projectID, long guid, String userLogin, HistoryType type, Date fromDate, Date toDate) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "getUserHistory", "project id "+projectID+"userLogin +"+userLogin+" type="+type+" fromDate="+fromDate+" toDate="+toDate);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_HISTORY_FOR_FILTER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",    guid,         Types.BIGINT);
		sqlQuery.setValue("ModifiedBy",    userLogin,         Types.VARCHAR);
		sqlQuery.setValue("Type",    type == null ? null : type.name(),         Types.VARCHAR);
		sqlQuery.setValue("FromDate",    fromDate == null ? null : new java.sql.Date(fromDate.getTime()),         Types.TIMESTAMP);
		sqlQuery.setValue("ToDate",    toDate == null ? null : new java.sql.Date(toDate.getTime()),         Types.TIMESTAMP);
		
		RowSet<HistoryObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}
	
	@Override
	public List<HistoryObject> getUserHistory(int projectID, String userLogin) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "getUserHistory", "project id "+projectID+"userLogin +"+userLogin);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_HISTORY_FOR_USER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ModifiedBy",    userLogin,         Types.VARCHAR);
		
		RowSet<HistoryObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public HistoryObject createObject(Object[] columnValues)
	{
		long historyID = Util.getLong(columnValues[0]);
		long guid = Util.getLong(columnValues[1]);
		String clientName = Util.getString(columnValues[2]);
		String clientVersion = Util.getString(columnValues[3]);
		String modifiedBy = Util.getString(columnValues[4]);
		String accessToken = Util.getString(columnValues[5]);
		
		Timestamp modificationTimestamp = Util.getTimestamp(columnValues[6]);
		Long modificationTime = modificationTimestamp == null ? null : modificationTimestamp.getTime();
		
		HistoryType type = HistoryType.valueOf(Util.getString(columnValues[7]));
		String desc = Util.getString(columnValues[8]);
		
		List<String>args = (List<String>) toJavaObject( (DataSource)columnValues[9]);
		
		desc = type.getDescription(guid, modifiedBy, args.toArray(new String[0]));
		HistoryObject historyItem = new HistoryObject(historyID, guid, new Application(clientName, clientVersion), modifiedBy, accessToken, modificationTime, type, desc, args);
		return historyItem;
	}

	@Override
	public void deleteHistory(int projectId, long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "deleteHistory", "deleting history item for record = "+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_RECORD_HISTORY");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("GUID",  guid,        Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

}
