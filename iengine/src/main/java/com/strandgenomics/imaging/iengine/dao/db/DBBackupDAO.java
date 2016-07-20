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
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.BackupDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.BackupDataObject;

public class DBBackupDAO extends ImageSpaceDAO<BackupDataObject> implements BackupDAO {

	DBBackupDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "BackupDAO.xml");
	}

	@Override
	public void insertBackupData(int projectId, String location, String sign) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBBackupDAO", "insertBackupData", "project "+projectId+" location +"+location);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_BACKUP_DATA");
		sqlQuery.setValue("Project",    projectId,         Types.INTEGER);
		sqlQuery.setValue("Location",    location,         Types.VARCHAR);
		sqlQuery.setValue("Sign",    sign,         Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public BackupDataObject getBackupData(int projectId) throws DataAccessException
	{
		logger.logp(Level.FINE, "DBBackupDAO", "getBackupData", "projectID="+projectId);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_BACKUP_DATA");
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);

        return fetchInstance(sqlQuery);
	}

	@Override
	public BackupDataObject createObject(Object[] columnValues)
	{
		int projectId = Util.getInteger(columnValues[0]);
		String location = Util.getString(columnValues[1]);
		String sign = Util.getString(columnValues[2]);
	
		BackupDataObject obj = new BackupDataObject(projectId, location, sign);
		return obj;
	}

	@Override
	public void deleteBackupData(int projectId) throws DataAccessException
	{
		logger.logp(Level.FINE, "DBBackupDAO", "getBackupData", "projectID="+projectId);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_BACKUP_DATA");
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        
        updateDatabase(sqlQuery);
	}

}
