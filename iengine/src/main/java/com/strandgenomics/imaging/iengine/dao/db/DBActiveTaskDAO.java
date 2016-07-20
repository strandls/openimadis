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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.compute.ComputeException;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.ActiveTaskDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;

public class DBActiveTaskDAO extends DBTaskDAO implements ActiveTaskDAO {

	DBActiveTaskDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "ActiveTaskDAO.xml");
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
		Integer progress = Util.getInteger(columnValues[12]);
		Long authCodeId = Util.getLong(columnValues[13]);
		
		authCodeId = authCodeId == null ? -1 : authCodeId;// null check
		
		Application app = new Application(appName, appVersion);
		
		Task t = new Task(workid, projectID, owner, app, authCode, authCodeId, priority, isMonitored, 
				scheduleTime, state, parameters, guids);
		if(progress!=null)
			t.setProgress(progress);
		return t;
	}
	
	@Override
	public void removeTask(long jobID) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBActiveTaskDAO", "removeTask", "deleting jobID=" + jobID);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_TASK");
		sqlQuery.setValue("JobID", jobID, Types.BIGINT);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void updateTaskState(long jobID, State state) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBActiveTaskDAO", "updateTaskState", "updating jobID=" + jobID +" to state="+state);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TASK_STATE");
		sqlQuery.setValue("JobID", jobID, Types.BIGINT);
		sqlQuery.setValue("State", state.name(), Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void clearIsMonitored(long taskId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBActiveTaskDAO", "clearIsMonitored", "clearing is_monitored for taskId=" + taskId);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("CLEAR_IS_MONITORED");
		sqlQuery.setValue("taskId", taskId, Types.BIGINT);

		updateDatabase(sqlQuery);

	}

	@Override
	public void setTaskProgress(long jobID, int progress) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBActiveTaskDAO", "setTaskProgress", "updating jobID=" + jobID +" to progress="+progress);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TASK_PROGRESS");
		sqlQuery.setValue("JobID", jobID, Types.BIGINT);
		sqlQuery.setValue("Progress", progress, Types.INTEGER);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void exchangeAuthCode(String authCode, String accessToken) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBActiveTaskDAO", "exchangeAuthCode", "exchanging authcode=" + authCode +" to accesstoken="+accessToken);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("EXCHANGE_AUTH_CODE");
		sqlQuery.setValue("AuthCode", authCode, Types.VARCHAR);
		sqlQuery.setValue("AccessToken", accessToken, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void updateScheduleTime(long jobID, long scheduleTime) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBActiveTaskDAO", "updateScheduleTime", "updating jobID=" + jobID +" to schedule time="+scheduleTime);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_SCHEDULE_TIME");
		sqlQuery.setValue("JobID", jobID, Types.BIGINT);
		sqlQuery.setValue("ScheduleTime", scheduleTime, Types.BIGINT);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public boolean rescheduleTaskInTransaction(long taskID, long waitTime) throws DataAccessException
	{
		Connection conn = null;
        boolean autoCommitStatus = true;
        
        try {
            conn = getConnection();
            autoCommitStatus = conn.getAutoCommit();
            //we will commit all in one shot, transaction
            conn.setAutoCommit(false);
            
            // perform operation
            boolean result = taskRescheduleTransaction(taskID, waitTime);
            
            //make the changes permanent
            conn.commit();
            
            return result;
        }
        catch(SQLException ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error executing transaction", ex);

            try 
            {
                if(conn != null) conn.rollback();
            }
            catch(Exception exx)
            {
                logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while doing a rollback", exx);
            }
            
            logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while creating tables", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            try 
            {
                conn.setAutoCommit(autoCommitStatus);
            }
            catch(Exception ex)
            {
                logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while setting autocommit status", ex);
            }
            //close the connection/set it free in the pool
            closeAll(null, null, conn);
        }
	}
	
	private boolean taskRescheduleTransaction(long taskID, long waitTime) throws DataAccessException
	{
		Task t = factory.getActiveTaskDAO().getTask(taskID);
		
		if(t==null)
			return false;// not active task
		
		if(t.isRunning())
		{
			throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_RUNNING));
		}
		
		if (t.getState() == State.WAITING || t.getState() == State.SCHEDULED)
		{
			t.reSchedule(waitTime);
			
			factory.getActiveTaskDAO().updateScheduleTime(t.getID(), t.getScheduleTime());
			updateDBState(t, State.SCHEDULED);
			return true;
		}
		
		return false;
	}

	private void updateDBState(Task task, State newState) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		Task dbtask = activeTaskDAO.getTask(task.getID());
		if(dbtask == null)
		{
			// insert
			activeTaskDAO.insertTask(task.getID(), task.getApplication().name, task.getApplication().version, 
					task.getProject().getID(), task.isMonitored(), task.getJobAuthCode(), task.work.authCodeInternalId, task.getOwner(), task.getPriority(), task.getScheduleTime(), task.getState(), task.getTaskParameters(), task.getInputs());
		}
		else
		{
			// update
			activeTaskDAO.updateTaskState(task.getID(), newState);
		}
	}
}
