package com.strandgenomics.imaging.iengine.dao;

import com.strandgenomics.imaging.icore.db.DataAccessException;

/**
 * dao to handle the task logs
 * 
 * @author Anup Kulkarni
 */
public interface TaskLogDAO {

	/**
	 * add log to task 
	 * @param taskID specified task
	 * @param logFileName name of the log file
	 * @throws DataAccessException 
	 */
	public void addTaskLog(long taskID, String logFileName) throws DataAccessException;
	
	/**
	 * get location where the logs for specified task are stored
	 * @param taskID specified task
	 * @return location where the logs for specified task are stored
	 * @throws DataAccessException 
	 */
	public String getTaskLogLocation(long taskID) throws DataAccessException;
}
