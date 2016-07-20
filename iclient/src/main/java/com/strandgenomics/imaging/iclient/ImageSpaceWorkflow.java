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

/*
 * ImageSpaceWorkflow.java
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;

/**
 * Service to enable workflow - executing of task/pipeline by the server
 * @author arunabha
 *
 */
public interface ImageSpaceWorkflow {

	/**
	 * Returns the publisher of the specified application
	 * @param application the application 
	 * @return the publisher for the specified application
	 */
	public Publisher getPublisher(Application application);

	/**
	 * Executes the specified registered-application with the specified parameters (if any) 
	 * on the specified list of records as input
	 * @param application the application pre-registered with the server (through plugins to compute-server)
	 * @param parameters the parameters to run the application
	 * @param guids the input records
	 * @param priority priority of the task (running application) under consideration 
	 * @return an handle to the remotely executing task
	 */
	public Job executeApplication(Application application, Map<String, Object> parameters, Priority priority, String projectName, long ... guids);

	/**
	 * Schedules the specified registered-application for execution with the specified parameters (if any) 
	 * on the specified list of records as input
	 * @param application the application pre-registered with the server (through plugins to compute-server)
	 * @param parameters the parameters to run the application
	 * @param records the input records
	 * @param priority priority of the task (running application) under consideration 
	 * @param scheduleTime time in milliseconds 
	 * @return an handle to the remotely executing task
	 */
	public Job scheduleApplication(Application application, Map<String, Object> parameters, Priority priority, String projectName, long scheduleTime, long ... guids);

	/**
	 * Returns the list of available application with the given category and from the specified publisher 
	 * @param publisher publisher (null will mean a listing across all publisher)
	 * @param categoryName name of the category  (null will mean a listing across all categories)
	 * @return list of available applications
	 */
	public List<Application> listApplications(Publisher publisher, String categoryName);

	/**
	 * Returns the required application specific parameters for the specified application 
	 * @param application application under consideration
	 * @return a list of relevant parameters or null, if nothing there
	 */
	public Set<Parameter> getParameters(Application application) ;

	/**
	 * Returns the state of the task submitted for execution
	 * @param job handle to the remotely executing task
	 * @return the status code. one of SCHEDULED,WAITING,PAUSED,ALLOCATED,EXECUTING,DELETING, DELETED,ERROR,SUCCESS,TERMINATED
	 */
	public State getJobState(Job job) ;

	/**
	 * Reschedule the specified job  which is still to be allocated (to a worker for execution)
	 * @param job handle to the remotely executing task
	 * @param rescheduleTime the new wait time interval
	 * @return true if successful, false otherwise
	 */
	public boolean rescheduleTask(Job job, long rescheduleTime);

	/**
	 * Pauses the specified job  which is still to be allocated (to a worker for execution)
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 */
	public boolean pauseTask(Job job);

	/**
	 * Resumes the specified job  which is in a paused state
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 */
	public boolean resumeTask(Job job);
	
	/**
	 * Removes the specified job  which is still to be allocated (to a worker for execution)
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 */
	public boolean removeTask(Job job);

	/**
	 * Terminate or kill the specified job which is being executed (by an worker)
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 */
	public boolean terminateTask(Job job);

	/**
	 * Returns the progress measure (0-100) associated the specified running task
	 * @param job handle to the remotely executing task
	 * @return the progress (0-100)
	 */
	public int getTaskProgress(Job job);
	
	/**
	 * sets the progress measure (0-100) associated the specified running task
	 * @param jobID handle to the remotely executing task
	 * @param progress the progress (0-100)
	 */
	public void setTaskProgress(long jobID, int progress);

	/**
	 * Returns the List of parameters used while executing the specified Job
	 * @param job handle to the remotely executing task
	 * @return the list of parameters used when the task was submitted
	 */
	public Map<String, Object> getTaskParameters(Job job);

	/**
	 * Returns the list of Record GUIDs which are used as inputs for the specified task
	 * @param job handle to the remotely executing task
	 * @return list of input Records (their GUID)
	 */
	public long[] getTaskInputs(Job job);
	
	/**
	 * Returns the list of Record GUIDs which are potentially created by this Job
	 * Note that is call is valid only after the said job is ended successfully
	 * @param job handle to the remotely executing task
	 * @return list of Records (their GUID) that are create or null if there are none
	 */
	public long[] getTaskOutputs(String accessToken, long jobID);
	
	/**
	 * upload the task log file for the specified job
	 * @param jobID specified job id
	 * @param logFile log file to be attached with the log
	 */
	public void uploadTaskLog(long jobID, File logFile);
}
