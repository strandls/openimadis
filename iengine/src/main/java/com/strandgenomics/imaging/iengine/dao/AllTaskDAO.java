package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.compute.Task;

/**
 * Queries accessing both active, archieved tasks should go here.
 * @author Devendra
 */
public interface AllTaskDAO 
{	
	/**
	 * find task matching search criteria
	 * @param owner
	 * @param priority
	 * @param appName
	 * @param appVersion
	 * @param taskState
	 * @param fromTime closed interval timestamp>=fromTime
	 * @param toTime open interval  timestamp<toTime
	 * @param projectID
	 * @return tasks matching given search criteria
	 * @throws DataAccessException
	 */
	public List<Task> searchTask(String owner, Priority priority, String appName, String appVersion, 
			State taskState, Long fromTime, Long toTime, Integer projectID, long offset, long limit) throws DataAccessException;
	
	/**
	 * get count of tasks matching given criteria 
	 * @param owner
	 * @param priority
	 * @param appName
	 * @param appVersion
	 * @param taskState
	 * @param fromTime closed interval timestamp>=fromTime
	 * @param toTime open interval  timestamp<toTime
	 * @param projectID
	 * @return count of tasks matching given criteria
	 * @throws DataAccessException
	 */
	
	public long getCountOf(String owner, Priority priority, String appName, String appVersion, 
			State taskState, Long fromTime, Long toTime, Integer projectID) throws DataAccessException;
	
	
}
