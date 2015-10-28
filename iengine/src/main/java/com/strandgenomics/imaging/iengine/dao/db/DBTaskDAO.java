package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TaskDAO;


/**
 * Implementation of common queries for active, archieved tasks.
 * This class assumes that subclass will provide valid queryfile.  
 * @author devendra
 *
 */
public abstract class DBTaskDAO extends ImageSpaceDAO<Task> implements TaskDAO {

	/**
	 * This constructor will be called from DBActiveTaskDAO, DBArchievedTaskDAO
	 * specifying their corresponding queryFile in the arguments
	 * @param factory
	 * @param connectionProvider
	 * @param queryFile This should provide definitions for all queries supported by TaskDAO
	 */
	DBTaskDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider, String queryFile) 
	{
		super(factory, connectionProvider, queryFile);
	}

	/* Map columnValues to task fields*/
	@Override
	final public void insertTask(long jobID, String appName, String appVersion, int projectID,
			boolean isMonitored, String authcode, long authID, String owner, Priority priority, long scheduleTime,
			State state, Map<String, String> parameters, long[] inputs) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskDAO", "insertTask", "inserting task = "+jobID+" for app = "+appName+" owned by = "+owner);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TASK");

		sqlQuery.setValue("JobID",  jobID,        Types.BIGINT);
		sqlQuery.setValue("Owner", owner,       Types.VARCHAR);
		sqlQuery.setValue("AppName",   appName,      Types.VARCHAR);
		sqlQuery.setValue("AppVersion",   appVersion,      Types.VARCHAR);
		sqlQuery.setValue("AuthCode", authcode, Types.VARCHAR);
		sqlQuery.setValue("Priority",    priority.name(),    Types.VARCHAR);
		sqlQuery.setValue("State",   state.name(),      Types.VARCHAR);
		sqlQuery.setValue("ScheduleTime",   scheduleTime,      Types.BIGINT);
		sqlQuery.setValue("Parameters",  toByteArray(parameters),     Types.BLOB);
		sqlQuery.setValue("InputGuids",  toByteArray(inputs),     Types.BLOB);
		sqlQuery.setValue("isMonitored", isMonitored ,     Types.BOOLEAN);
		sqlQuery.setValue("projectID", projectID ,     Types.INTEGER);
		sqlQuery.setValue("AuthID", authID ,     Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	final public void setMonitored(String user, long taskId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_MONITORED");

		sqlQuery.setValue("JobID",  taskId,        Types.BIGINT);
		sqlQuery.setValue("Owner", user,       Types.VARCHAR);
		sqlQuery.setValue("isMonitored", true ,     Types.BOOLEAN);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	final public Task getTask(long jobID) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskDAO", "getTask", "jobID=" + jobID);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TASK");
		sqlQuery.setValue("JobID", jobID, Types.BIGINT);

		return fetchInstance(sqlQuery);
	}
	
	@Override
	final public List<Task> getAllTasks() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskDAO", "getAllTasks", "returning all tasks");

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_TASKS");

		RowSet<Task> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	final public List<Task> getMonitoredTasks(String user) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBTaskDAO", "getMonitoredTasks", "returning monitored tasks for user "+user);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_MONITORED_TASKS");
		sqlQuery.setValue("User", user, Types.VARCHAR);
		sqlQuery.setValue("Monitored", true, Types.BOOLEAN);

		RowSet<Task> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	final public List<Task> getTasks(State state) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordDAO", "getTasks", "state ="+state);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TASK_FOR_STATE");
        sqlQuery.setValue("State",  state.name(),  Types.VARCHAR);
        
		RowSet<Task> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	abstract public void clearIsMonitored(long taskId) throws DataAccessException;

}
