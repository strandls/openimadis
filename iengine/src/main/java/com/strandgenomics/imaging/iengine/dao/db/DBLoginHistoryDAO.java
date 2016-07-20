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

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.LoginHistoryDAO;
import com.strandgenomics.imaging.iengine.models.LoginHistoryObject;

public class DBLoginHistoryDAO  extends ImageSpaceDAO<LoginHistoryObject> implements LoginHistoryDAO {

	DBLoginHistoryDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "LoginHistoryDAO.xml");
	}

	@Override
	public LoginHistoryObject createObject(Object[] columnValues)
	{
		String user = Util.getString(columnValues[0]);
		String appName = Util.getString(columnValues[1]);
		String appVersion = Util.getString(columnValues[2]);
		Long loginTime = Util.getTimestamp(columnValues[3]).getTime();
		
		LoginHistoryObject login = new LoginHistoryObject(user , appName, appVersion, loginTime);
		return login;
	}

	@Override
	public void insertLoginHistory(Application app, String userLogin) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "insertLoginHistory", "application "+app+" user +"+userLogin);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_LOGIN_HISTORY");
		sqlQuery.setValue("User",    userLogin,         Types.VARCHAR);
		sqlQuery.setValue("AppName",    app == null ? null : app.name,         Types.VARCHAR);
		sqlQuery.setValue("AppVersion",    app == null ? null : app.version,         Types.VARCHAR);
		sqlQuery.setValue("LoginTime",    new java.sql.Date(System.currentTimeMillis()),         Types.TIMESTAMP);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public List<LoginHistoryObject> getLoginHistory(Application app, String user, Date fromDate, Date toDate, Long limit, Long offset) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBHistoryDAO", "getLoginHistory", "application "+app+" user +"+user+" fromDate="+fromDate+" toDate="+toDate);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LOGIN_HISTORY");
		sqlQuery.setValue("User",    user,         Types.VARCHAR);
		sqlQuery.setValue("AppName",    app == null ? null : app.name,         Types.VARCHAR);
		sqlQuery.setValue("AppVersion",    app == null ? null : app.version,         Types.VARCHAR);
		sqlQuery.setValue("FromDate",    fromDate == null ? null : new java.sql.Date(fromDate.getTime()),         Types.TIMESTAMP);
		sqlQuery.setValue("ToDate",    toDate == null ? null : new java.sql.Date(toDate.getTime()),         Types.TIMESTAMP);
		sqlQuery.setValue("Limit",    limit,         Types.BIGINT);
		sqlQuery.setValue("Offset",    offset,         Types.BIGINT);
		
		RowSet<LoginHistoryObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}
	
	@Override
	public long countLoginHistory(Application app,
			String user, Date fromDate, Date toDate)
			throws DataAccessException {
		logger.logp(Level.INFO, "DBHistoryDAO", "countLoginHistory", "application "+app+" user +"+user+" fromDate="+fromDate+" toDate="+toDate);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("COUNT_LOGIN_HISTORY");
		sqlQuery.setValue("User",    user,         Types.VARCHAR);
		sqlQuery.setValue("AppName",    app == null ? null : app.name,         Types.VARCHAR);
		sqlQuery.setValue("AppVersion",    app == null ? null : app.version,         Types.VARCHAR);
		sqlQuery.setValue("FromDate",    fromDate == null ? null : new java.sql.Date(fromDate.getTime()),         Types.TIMESTAMP);
		sqlQuery.setValue("ToDate",    toDate == null ? null : new java.sql.Date(toDate.getTime()),         Types.TIMESTAMP);
		
		Long count = getLong(sqlQuery);
        return count;
	}

}
