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

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TaskLogDAO;

public class DBTaskLogDAO extends ImageSpaceDAO<Storable> implements TaskLogDAO {

	DBTaskLogDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "TaskLogDAO.xml");
	}

	@Override
	public void addTaskLog(long taskID, String logFileName) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskLogDAO", "addTaskLog", "adding logfile="+logFileName+" to task="+taskID);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TASK_LOG");

		sqlQuery.setValue("JobID",  taskID,        Types.BIGINT);
		sqlQuery.setValue("TaskLocation",  String.valueOf(taskID),        Types.VARCHAR);
		sqlQuery.setValue("Filename",  logFileName,        Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public String getTaskLogLocation(long taskID) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskLogDAO", "addTaskLog", "getting logfile for task="+taskID);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TASK_LOG");

		sqlQuery.setValue("JobID",  taskID,        Types.BIGINT);
		
		return getString(sqlQuery);
	}

	@Override
	public Storable createObject(Object[] columnValues)
	{
		return null;
	}

}
