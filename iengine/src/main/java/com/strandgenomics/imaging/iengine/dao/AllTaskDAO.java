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
