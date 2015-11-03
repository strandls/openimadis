/*
 * Job.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
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
