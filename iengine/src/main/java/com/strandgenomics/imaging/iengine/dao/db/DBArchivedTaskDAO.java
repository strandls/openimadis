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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.ArchivedTaskDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;

public class DBArchivedTaskDAO extends DBTaskDAO implements ArchivedTaskDAO {

	DBArchivedTaskDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "ArchivedTaskDAO.xml");
	}
	
	@Override
	public Task createObject(Object[] columnValues)
	{
		long workid = Util.getLong(columnValues[0]);
		String owner = Util.getString(columnValues[1]);
		String authCode = Util.getString(columnValues[2]);
		Priority priority = Priority.valueOf(Util.getString(columnValues[3]));
		String appName = Util.getString(columnValues[4]);
		String appVersion = Util.getString(columnValues[5]);
		long scheduleTime = Util.getLong(columnValues[6]);
		State state = State.valueOf(Util.getString(columnValues[7]));
		Map<String, String> parameters = (Map<String, String>) toJavaObject( (DataSource)columnValues[8]);
		long[] guids = (long[]) toJavaObject( (DataSource)columnValues[9]);
		int projectID = Util.getInteger(columnValues[10]);
		boolean isMonitored = Util.getBoolean(columnValues[11]);
		Long authCodeId = Util.getLong(columnValues[12]);
		
		authCodeId = authCodeId == null ? -1 : authCodeId;// null check
		Application app = new Application(appName, appVersion);
		
		Task t = new Task(workid, projectID, owner, app, authCode, authCodeId, priority, isMonitored, 
				scheduleTime, state, parameters, guids);
		return t;
	}
	
	@Override
	public long[] getTaskOutputs(long id) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_OUTPUTS");
		sqlQuery.setValue("JobID", id, Types.BIGINT);
		
		DataSource ds = (DataSource) getObject(sqlQuery);
		Object obj = toJavaObject( ds );
		if(obj == null) return null;
		
		return (long[])obj;
	}
	
	@Override
	public List<Task> getMonitoredTasks() throws DataAccessException {

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_MONITORED_TASK");
		RowSet<Task> result = find(sqlQuery);
        return result == null ? new ArrayList<Task>() : result.getRows();
	}
	
	@Override
	public void clearIsMonitored(long taskId) throws DataAccessException {
		logger.logp(Level.INFO, "DBArchivedTaskDAO", "clearIsMonitored", "clearing is_monitored for taskId=" + taskId);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("CLEAR_IS_MONITORED");
		sqlQuery.setValue("taskId", taskId, Types.BIGINT);
        
        updateDatabase(sqlQuery);
		
	}

	@Override
	public void setTaskOutputs(long id, long[] outputs) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBArchivedTaskDAO", "setTaskOutputs", "setting outputs for =" + id);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_OUTPUTS");
		sqlQuery.setValue("JobID", id, Types.BIGINT);
		sqlQuery.setValue("Outputs", toByteArray(outputs), Types.BLOB);
        
		updateDatabase(sqlQuery);
	}
}
