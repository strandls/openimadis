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
 * ImageSpaceCompute.java
 *
 * AVADIS Image Management System
 * Web-service definition
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

package com.strandgenomics.imaging.iserver.services.def.compute;

import java.rmi.RemoteException;

/**
 * Compute APIs
 * @author arunabha
 *
 */
public interface ImageSpaceCompute {
	
	/**
	 * Returns the list of publisher available with the server
	 * @param accessToken the access-token
	 * @return list of available publisher
	 * @throws RemoteException
	 */
	public Publisher[] listPublishers(String accessToken) 
			throws RemoteException;
	
	/**
	 * Returns the list of available application with the given category and from the specified publisher 
	 * @param accessToken the access-token
	 * @param publisher name of the publisher (is unique)
	 * @param categoryName name of the category 
	 * @return list of available applications
	 * @throws RemoteException
	 */
	public Application[] listApplications(String accessToken, String publisher, String categoryName)
			throws RemoteException;
	
	/**
	 * Returns the list of publishers for the specified application (app name is unique)
	 * @param accessToken the access-token
	 * @param appName name of the application
	 * @param appVersion version of the application
	 * @return the publisher of the specified application
	 * @throws RemoteException
	 */
	public Publisher[] getPublisher(String accessToken, String appName, String appVersion)
			throws RemoteException;
	
	/**
	 * Returns the required application specific parameters for the specified application 
	 * @param accessToken access token
	 * @param appName name of the application (is unique)
	 * @param appVersion version of the application
	 * @return a list of relevant parameters or null, if nothing there
	 * @throws RemoteException
	 */
	public Parameter[] getApplicationParameters(String accessToken, String appName, String appVersion)
			throws RemoteException;
	
	/**
	 * Execute the application specified by it name and requiring the specified parameters and input records (as GUID)
	 * @param accessToken the access token required to make this call
	 * @param appName name of the application
	 * @param appVersion version of the application
	 * @param parameters the parameters needed for the application to run
	 * @param guids the list of GUID of Records, the input to the application
	 * @param taskPriority the priority of the task
	 * @return the jobID of the task that is submitted
	 * @throws RemoteException
	 */
	public long executeApplication(String accessToken, String appName, String appVersion, String projectName, NVPair[] parameters, long[] guids, int taskPriority)
			throws RemoteException;
	
	/**
	 * Schedules an application for execution specified by it name and requiring the specified parameters and input records (as GUID)
	 * @param accessToken the access token required to make this call
	 * @param appName name of the application
	 * @param appVersion version of the application
	 * @param parameters the parameters needed for the application to run
	 * @param guids the list of GUID of Records, the input to the application
	 * @param taskPriority the priority of the task
	 * @param scheduleTime the delta time to wait
	 * @return the jobID of the task that is submitted
	 * @throws RemoteException
	 */
	public long scheduleApplication(String accessToken, String appName, String appVersion, String projectName, NVPair[] parameters, long[] guids,  int taskPriority, long scheduleTime)
			throws RemoteException;
	
	/**
	 * Returns the state of the job submitted for execution
	 * @param accessToken  the access token required to make this call
	 * @param jobID ID of the job submitted for execution
	 * @return the status code. one of SCHEDULED,WAITING,PAUSED,ALLOCATED,EXECUTING,DELETING, DELETED,ERROR,SUCCESS,TERMINATED
	 * @throws RemoteException
	 */
	public String getJobState(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Reschedule the specified job  which is still to be allocated (to a worker for execution)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @param rescheduleTime the new wait time interval
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean rescheduleTask(String accessToken, long jobID, long rescheduleTime)
			throws RemoteException;
	
	/**
	 * Pauses the specified job  which is still to be allocated (to a worker for execution)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean pauseTask(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Resumes the specified job  which is in a paused state
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean resumeTask(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Removes the specified job  which is still to be allocated (to a worker for execution)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean removeTask(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Terminate or kill the specified job which is being executed (by an worker)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean terminateTask(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Returns the progress measure associated the specified running task
	 * @param accessToken  the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return the progress (0-100)
	 * @throws RemoteException
	 */
	public int getTaskProgress(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * sets the tasks input (0-100)
	 * @param accessToken the access token required to make this call
	 * @param jobID of the job under consideration
	 * @param progress the progress(0-100)
	 * @throws RemoteException
	 */
	public void setTaskProgress(String accessToken, long jobID, int progress)
			throws RemoteException;
	
	/**
	 * Returns the List of parameters used while executing the specified Job
	 * @param accessToken  the access token required to make this call
	 * @param jobID ID of the job
	 * @return the list of parameters used when the task was submitted
	 * @throws RemoteException
	 */
	public NVPair[] getTaskParameters(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Returns the list of Record GUIDs which are used as inputs 
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job
	 * @return list of input Records (their GUID)
	 * @throws RemoteException
	 */
	public long[] getTaskInputs(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * Returns the list of Record GUIDs which are potentially created by this Job
	 * Note that is call is valid only after the said job is ended successfully
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job
	 * @return list of Records (their GUID) that are create or null if there are none
	 * @throws RemoteException
	 */
	public long[] getTaskOutputs(String accessToken, long jobID)
			throws RemoteException;
	
	/**
	 * returns the url for uploading the job log
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job
	 * @param filename of specified log file
	 * @return url for uploading the job logs
	 */
	public String getTaskLogUploadURL(String accessToken, long jobID, String filename);
	
	//dummy api for exposing the constraints subclasses, remove if not needed anymore
		public DoubleListConstraints testMethod1(String appName) throws RemoteException;
		public DoubleRangeConstraints testMethod2(String appName) throws RemoteException;
		public LongListConstraints testMethod3(String appName) throws RemoteException;
		public LongRangeConstraints testMethod4(String appName) throws RemoteException;
}
