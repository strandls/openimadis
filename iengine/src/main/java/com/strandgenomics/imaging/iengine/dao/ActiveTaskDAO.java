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

package com.strandgenomics.imaging.iengine.dao;

import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.DataAccessException;

/**
 * Manages the active tasks of the compute engine
 * 
 * @author Anup Kulkarni
 */
public interface ActiveTaskDAO extends TaskDAO
{	
	
	/**
	 * removes the task from active task table
	 * @param jobID specified task
	 * @throws DataAccessException
	 */
	public void removeTask(long jobID) throws DataAccessException;
	
	/**
	 * updates the state of the task in DB
	 * @param jobID
	 * @param state
	 * @throws DataAccessException
	 */
	public void updateTaskState(long jobID, State state) throws DataAccessException;

	/**
	 * sets the progress of the running task
	 * @param jobID
	 * @param progress
	 * @throws DataAccessException 
	 */
	public void setTaskProgress(long jobID, int progress) throws DataAccessException;

	/**
	 * exchanges authcode to accesstoken whenever task is logged in
	 * @param authCode
	 * @param accessToken
	 * @throws DataAccessException 
	 */
	public void exchangeAuthCode(String authCode, String accessToken) throws DataAccessException;

	/**
	 * update the schedule time of the task
	 * @param id specified task id
	 * @param scheduleTime new schedule time
	 * @throws DataAccessException 
	 */
	public void updateScheduleTime(long id, long scheduleTime) throws DataAccessException;
	
	/**
	 * reschedule the task in single transaction
	 * @param taskID task id
	 * @param waitTime wait time
	 * @throws DataAccessException
	 */
	public boolean rescheduleTaskInTransaction(long taskID, long waitTime) throws DataAccessException;
}
