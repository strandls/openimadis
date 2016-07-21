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

package com.strandgenomics.imaging.iserver.services.def.worker;

/**
 * status of a work being done
 * @author arunabha
 *
 */
public class WorkStatus {
	
    /** 
     * state of task being executed, one of EXECUTING, SUCCESSFUL, FAILURE and TERMINATED
     */
    private int taskState;
    /** 
     * ID of the task which is being executed
     */
    private long taskID;
    
    public WorkStatus()
    {}

	/**
	 * @return the taskState
	 */
	public int getTaskState() {
		return taskState;
	}

	/**
	 * @param taskState the taskState to set
	 */
	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	/**
	 * @return the taskID
	 */
	public long getTaskID() {
		return taskID;
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}
}
