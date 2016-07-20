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

package com.strandgenomics.imaging.iengine.system;

import java.util.List;

import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.ActiveTaskDAO;
import com.strandgenomics.imaging.iengine.dao.AllTaskDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;

/**
 * APIs related to active tasks, archieved tasks 
 * @author devendra
 *
 */
public class TaskManager extends SystemManager{
	
	TaskManager() {}
	
	/**
	 * Search for task matching given criteria
	 * @param owner
	 * @param priority
	 * @param appName
	 * @param appVersion
	 * @param taskState
	 * @param fromTime closed interval timestamp>=fromTime
	 * @param toTime open interval  timestamp<toTime
	 * @param projectID
	 * @param offset
	 * @param limit
	 * @return list of tasks matching given criteria within given offset,limit
	 * @throws DataAccessException
	 */
	public List<Task> searchTask(String owner, Priority priority, String appName, String appVersion, 
			State taskState, Long fromTime, Long toTime, Integer projectID,long offset,long limit) throws DataAccessException
	{
		AllTaskDAO allTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getAllTaskDAO();
		return allTaskDAO.searchTask(owner, priority, appName, appVersion, taskState, 
				fromTime, toTime, projectID, offset, limit);		
	}
	
	/**
	 * 
	 * @param owner
	 * @param priority
	 * @param appName
	 * @param appVersion
	 * @param taskState
	 * @param fromTime closed interval timestamp>=fromTime
	 * @param toTime open interval  timestamp<toTime
	 * @param projectID
	 * @return
	 * @throws DataAccessException
	 */
	public long getCountOf(String owner, Priority priority, String appName, String appVersion, State taskState, Long fromTime, Long toTime, Integer projectID) throws DataAccessException
	{
		AllTaskDAO allTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getAllTaskDAO();
		return allTaskDAO.getCountOf(owner, priority, appName, appVersion, taskState, fromTime, toTime, projectID);
	}

	/**
	 * exchanges authcode with accesstoken if exists in task table
	 * @param authCode auth code given for task
	 * @param accessToken granted for task
	 * @throws DataAccessException 
	 */
	public void exchangeAuthCode(String authCode, String accessToken) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		activeTaskDAO.exchangeAuthCode(authCode, accessToken);
	}
}
