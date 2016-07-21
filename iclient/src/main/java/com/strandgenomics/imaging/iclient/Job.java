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

package com.strandgenomics.imaging.iclient;

import java.io.File;
import java.util.Map;

import com.strandgenomics.imaging.icore.app.State;

/**
 * Represents a handle to a Task submitted to the server
 * @author arunabha
 *
 */
public class Job extends ImageSpaceObject {
	
	private static final long serialVersionUID = 6750093791862505372L;
	protected long id;
	
	Job(long id)
	{
		this.id = id;
	}
	
	public State getState()
	{
    	//makes a system call
    	return getImageSpace().getJobState(this);
	}
	
	/**
	 * returns the job identifier to identify the job at the server side
	 * @return
	 */
	public long getJobID()
	{
		return id;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * reschedules a task that has not yet commenced execution
     */
	public boolean reschedule(long rescheduleTime)
	{
		return getImageSpace().rescheduleTask(this, rescheduleTime);
	}

    /**
     * pauses a task to be executed by the server
     */
	public boolean pause()
	{
		return getImageSpace().pauseTask(this);
	}
	
    /**
     * resumes a task that has been paused
     */
	public boolean resume()
	{
		return getImageSpace().resumeTask(this);
	}
	
    /**
     * deletes a task from the server task execution queue
     */
	public boolean remove()
	{
		return getImageSpace().removeTask(this);
	}
	
    /**
     * Terminates an executing task
     */
	public boolean terminate()
	{
		return getImageSpace().terminateTask(this);
	}
	
	/**
	 * Returns the current progress of the task under execution
	 * @return the current progress of the task under execution
	 */
	public int getProgress()
	{
		return getImageSpace().getTaskProgress(this);
	}
	
	/**
	 * sets the progress of the task under execution
	 * @param progress the current progress of the task under execution
	 */
	public void setProgress(int progress)
	{
		getImageSpace().setTaskProgress(id, progress);
	}
	
	/**
	 * Returns the application parameters of this task
	 * @return  the application parameters of this task
	 */
	public Map<String, Object> getParameters()
	{
		return getImageSpace().getTaskParameters(this);
	}
	
	/**
	 * Returns the guids of the input records of this task
	 * @return the guids of the input records of this task
	 */
	public long[] getInputRecords()
	{
		return getImageSpace().getTaskInputs(this);
	}
	
	/**
	 * uploads the specified file as log of this task
	 * @param logFile
	 */
	public void uploadLog(File logFile)
	{
		getImageSpace().uploadTaskLog(this.id, logFile);
	}
}
