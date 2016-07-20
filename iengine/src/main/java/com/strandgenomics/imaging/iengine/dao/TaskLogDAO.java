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
