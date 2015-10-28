package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.AllTaskDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;


/**
 * Queries accessing both active, archieved tasks should go here. 
 * @author devendra
 *
 */
public class DBAllTaskDAO extends ImageSpaceDAO<Task> implements AllTaskDAO {

	/**
	 * This constructor will be called from DBActiveTaskDAO, DBArchievedTaskDAO
	 * specifying their corresponding queryFile in the arguments
	 * @param factory
	 * @param connectionProvider
	 */
	DBAllTaskDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "AllTaskDAO.xml");
	}
	
	@Override
	final public Task createObject(Object[] columnValues)
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
		
		Task t = new Task(workid, projectID, owner, app, authCode, authCodeId, priority,isMonitored
				,scheduleTime, state, parameters, guids);
		return t;
	}

	@Override
	final public List<Task> searchTask(String owner, Priority priority,
			String appName, String appVersion, State taskState, Long fromTime,
			Long toTime, Integer projectID, long offset, long limit) throws DataAccessException {
		logger.logp(Level.INFO, "DBAllTaskDAO", "searchTask", 
				"owner="+owner + "priority="+priority + "appName="+appName 
				+ "appVersion="+appVersion+ "taskState="+taskState 
				+ "fromDate"+fromTime + "toDate="+toTime + "projectID="+projectID);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SEARCH_TASK");
		sqlQuery.setValue("owner",  owner,  Types.VARCHAR);
		sqlQuery.setValue("priority",  priority== null ? null : priority.name(),  Types.VARCHAR);
		sqlQuery.setValue("appName",  appName,  Types.VARCHAR);
		sqlQuery.setValue("appVersion",  appVersion,  Types.VARCHAR);
		sqlQuery.setValue("taskState",  taskState== null ? null : taskState.name(),  Types.VARCHAR);
		sqlQuery.setValue("fromTime",  fromTime,  Types.BIGINT);
		sqlQuery.setValue("toTime",  toTime,  Types.BIGINT);
		sqlQuery.setValue("projectID",  projectID,  Types.INTEGER);
		sqlQuery.setValue("Offset", offset, Types.INTEGER);
		sqlQuery.setValue("Limit", limit, Types.INTEGER);
        
		RowSet<Task> result = find(sqlQuery);
        return result == null ? new ArrayList<Task>() : result.getRows();
	}

	@Override
	public long getCountOf(String owner, Priority priority, String appName,
			String appVersion, State taskState, Long fromTime, Long toTime,
			Integer projectID) throws DataAccessException {
		logger.logp(Level.INFO, "DBAllTaskDAO", "getCountOf", 
				"owner="+owner + "priority="+priority + "appName="+appName 
				+ "appVersion="+appVersion+ "taskState="+taskState 
				+ "fromTime"+fromTime + "toTime="+toTime + "projectID="+projectID);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SEARCH_COUNT");
		sqlQuery.setValue("owner",  owner,  Types.VARCHAR);
		sqlQuery.setValue("priority",  priority== null ? null : priority.name(),  Types.VARCHAR);
		sqlQuery.setValue("appName",  appName,  Types.VARCHAR);
		sqlQuery.setValue("appVersion",  appVersion,  Types.VARCHAR);
		sqlQuery.setValue("taskState",  taskState== null ? null : taskState.name(),  Types.VARCHAR);
		sqlQuery.setValue("fromTime",  fromTime,  Types.BIGINT);
		sqlQuery.setValue("toTime",  toTime,  Types.BIGINT);
		sqlQuery.setValue("projectID",  projectID,  Types.INTEGER);
        
		Long count = getLong(sqlQuery);
        return count;
	}

}
