/*
 * ImageSpaceComputeImpl.java
 *
 * AVADIS Image Management System
 * Web Service Implementation
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
package com.strandgenomics.imaging.iserver.services.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.axis.transport.http.AxisHttpSession;

import com.strandgenomics.imaging.icore.app.ApplicationSpecification;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.models.AuthCode;
import com.strandgenomics.imaging.iengine.system.AuthorizationManager;
import com.strandgenomics.imaging.iengine.system.ComputeEngine;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.ws.compute.Application;
import com.strandgenomics.imaging.iserver.services.ws.compute.DoubleListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.DoubleRangeConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.ImageSpaceCompute;
import com.strandgenomics.imaging.iserver.services.ws.compute.LongListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.LongRangeConstraints;
import com.strandgenomics.imaging.iserver.services.ws.compute.NVPair;
import com.strandgenomics.imaging.iserver.services.ws.compute.Parameter;
import com.strandgenomics.imaging.iserver.services.ws.compute.Publisher;

/**
 * Implementation of iCompute web-services
 * @author arunabha
 *
 */
public class ImageSpaceComputeImpl implements ImageSpaceCompute, Serializable {
	
	private static final long serialVersionUID = -1492126404259611647L;
	private transient Logger logger = null;
	
	public ImageSpaceComputeImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}

	/**
	 * Returns the list of publisher available with the server
	 * @param accessToken the access-token
	 * @return list of available publisher
	 * @throws RemoteException
	 */
	@Override
	public Publisher[] listPublishers(String accessToken) 
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "listPublishers", "listing registered publishers for "+accessToken);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		
		Collection<com.strandgenomics.imaging.iengine.compute.Publisher> localPublishers = engine.getPublisher(actorLogin);
		return CoercionHelper.toPublishers(localPublishers);
	}
	
	/**
	 * Returns the publisher for the specified application (app name is unique)
	 * @param accessToken the access-token
	 * @param appName name of the application
	 * @param appVersion version of the application
	 * @return the publisher of the specified application
	 * @throws RemoteException
	 */
	@Override
	public Publisher[] getPublisher(String accessToken, String appName, String appVersion)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getPublisher", "fetching publisher for application "+appName);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		
		com.strandgenomics.imaging.icore.app.Application app = new com.strandgenomics.imaging.icore.app.Application(appName, appVersion);
		Set<com.strandgenomics.imaging.iengine.compute.Publisher> localPublishers = engine.getPublisher(actorLogin, app);
		
		return CoercionHelper.toPublishers(localPublishers);
	}
	
	/**
	 * Returns the list of available application with the given category and from the specified publisher 
	 * @param accessToken the access-token
	 * @param publisher name of the publisher (is unique)
	 * @param categoryName name of the category 
	 * @return list of available applications
	 * @throws RemoteException
	 */
	@Override
	public Application[] listApplications(String accessToken, String publisher, String categoryName)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "listApplications", "listing application from publisher for "+publisher +" within category "+categoryName);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		
		List<com.strandgenomics.imaging.icore.app.Application> applications = engine.listApplications(actorLogin, publisher, categoryName);
		
		return CoercionHelper.toRemoteApplications(applications);
	}
	
	/**
	 * Returns the required application specific parameters for the specified application 
	 * @param accessToken access token
	 * @param appName name of the application (is unique)
	 * @return a list of relevant parameters or null, if nothing there
	 * @throws RemoteException
	 */
	@Override
	public Parameter[] getApplicationParameters(String accessToken, String appName, String appVersion)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getApplicationParameters", "fetching parameter definitions for application "+appName);
		
		com.strandgenomics.imaging.icore.app.Application app = new com.strandgenomics.imaging.icore.app.Application(appName, appVersion);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		Set<com.strandgenomics.imaging.icore.app.Parameter> parameters = engine.getParameters(actorLogin, app);
		
		return CoercionHelper.toRemoteParameters(parameters);
	}
	
	/**
	 * Execute the application specified by it name and requiring the specified parameters and input records (as GUID)
	 * @param accessToken the access token required to make this call
	 * @param appName name of the application
	 * @param appAuthCode the authorization grant obtained by the user for the specified application
	 * @param parameters the parameters needed for the application to run
	 * @param guids the list of GUID of Records, the input to the application
	 * @param taskPriority the priority of the task
	 * @return the jobID of the task that is submitted
	 * @throws RemoteException
	 */
	@Override
	public long executeApplication(String accessToken, String appName, String appVersion, String projectName, NVPair[] parameters, long[] guids, int taskPriority)
			throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "executeApplication", "executing application "+appName);
		
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "executeApplication", "executing application "+appName);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		
		com.strandgenomics.imaging.icore.app.Application app = new com.strandgenomics.imaging.icore.app.Application(appName, appVersion);
		Map<String, String> localParameters = CoercionHelper.toLocalParameters(parameters);
		try
		{
			AuthCode appAuthCode = getAuthCode(actorLogin, app);
			Task task = engine.executeApplication(actorLogin, app, appAuthCode, Priority.values()[taskPriority], projectName, localParameters, guids);
			
			return task.getID();
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "executeApplication", "error executing application "+appName, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	private AuthCode getAuthCode(String actorLogin, com.strandgenomics.imaging.icore.app.Application app) throws DataAccessException
	{
		logger.logp(Level.FINE, "ImageSpaceComputeImpl", "getAuthCode", "getting authcode for application "+app.name);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		
		ApplicationSpecification appSpecification = engine.getApplicationSpecification(app);
		if(appSpecification == null) return null;
		
		String clientID = appSpecification.getID();
		logger.logp(Level.FINE, "ImageSpaceComputeImpl", "getAuthCode", "getting authcode for client id "+clientID);
		long expiryTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // a validity for one week
		
		AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
		AuthCode appAuthcode = authManager.getAuthCode(actorLogin, clientID, Service.values(), expiryTime, null);
		
		logger.logp(Level.FINE, "ImageSpaceComputeImpl", "getAuthCode", "returning authcode for client id "+appAuthcode);
		return appAuthcode;
	}
	
	/**
	 * Schedules an application for execution specified by it name and requiring the specified parameters and input records (as GUID)
	 * @param accessToken the access token required to make this call
	 * @param appName name of the application
	 * @param appAuthCode the authorization grant obtained by the user for the specified application
	 * @param parameters the parameters needed for the application to run
	 * @param guids the list of GUID of Records, the input to the application
	 * @param taskPriority the priority of the task
	 * @param scheduleTime the delta time to wait
	 * @return the jobID of the task that is submitted
	 * @throws RemoteException
	 */
	@Override
	public long scheduleApplication(String accessToken, String appName, String appVersion, String projectName, NVPair[] parameters, long[] guids,  int taskPriority, long scheduleTime)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "scheduleApplication", "scheduling application "+appName);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		
		com.strandgenomics.imaging.icore.app.Application app = new com.strandgenomics.imaging.icore.app.Application(appName, appVersion);
		Map<String, String> localParameters = CoercionHelper.toLocalParameters(parameters);
		try
		{
			AuthCode appAuthCode = getAuthCode(actorLogin, app);
			Task task = engine.scheduleApplication(actorLogin, app, appAuthCode, 
					Priority.values()[taskPriority], projectName, false, scheduleTime, localParameters, guids);
			
			return task.getID();
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "scheduleApplication", "error scheduling application "+appName, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Returns the state of the job submitted for execution
	 * @param accessToken  the access token required to make this call
	 * @param jobID ID of the job submitted for execution
	 * @return the status code. one of SCHEDULED,WAITING,PAUSED,ALLOCATED,EXECUTING,DELETING, DELETED,ERROR,SUCCESS,TERMINATED
	 * @throws RemoteException
	 */
	@Override
	public String getJobState(String accessToken, long jobID) throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getJobState", "fetching job state "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		State jobState;
		try
		{
			jobState = engine.getJobState(actorLogin, jobID);
			return jobState.name();
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "getJobState", "error fetching job state "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Reschedule the specified job  which is still to be allocated (to a worker for execution)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @param rescheduleTime the new wait time interval
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	@Override
	public boolean rescheduleTask(String accessToken, long jobID, long rescheduleTime)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "rescheduleTask", "rescheduling application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		boolean value;
		try
		{
			value = engine.rescheduleTask(actorLogin, jobID, rescheduleTime);
			return value;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "rescheduleTask", "error in rescheduling application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
		
		
	}
	
	/**
	 * Pauses the specified job  which is still to be allocated (to a worker for execution)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	@Override
	public boolean pauseTask(String accessToken, long jobID)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "pauseTask", "request to pause scheduled application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		boolean value;
		try
		{
			value = engine.pauseTask(actorLogin, jobID);
			return value;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.INFO, "ImageSpaceComputeImpl", "pauseTask", "error pausing the scheduled application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Resumes the specified job  which is in a paused state
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	@Override
	public boolean resumeTask(String accessToken, long jobID)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "resumeTask", "request to resume paused application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		try
		{
			boolean value = engine.resumeTask(actorLogin, jobID);
			return value;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.INFO, "ImageSpaceComputeImpl", "resumeTask", "error resuming the scheduled application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Removes the specified job  which is still to be allocated (to a worker for execution)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	@Override
	public boolean removeTask(String accessToken, long jobID) throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "removeTask", "remove job "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		boolean value;
		try
		{
			value = engine.removeTask(actorLogin, jobID);
			return value;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "removeTask", "error removing job "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Terminate or kill the specified job which is being executed (by an worker)
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return true if successful, false otherwise
	 * @throws RemoteException
	 */
	@Override
	public boolean terminateTask(String accessToken, long jobID)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "terminateTask", "terminate running application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		try
		{
			boolean value = engine.terminateTask(actorLogin, jobID);
			return value;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "terminateTask", "error terminating job="+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Returns the progress measure associated the specified running task
	 * @param accessToken  the access token required to make this call
	 * @param jobID ID of the job under consideration
	 * @return the progress (0-100)
	 * @throws RemoteException
	 */
	@Override
	public int getTaskProgress(String accessToken, long jobID)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getTaskProgress", "fetch application progress "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		try
		{
			int progressValue = engine.getTaskProgress(actorLogin, jobID);
			return progressValue;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "getTaskProgress", "error getting application progress for job="+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void setTaskProgress(String accessToken, long jobID, int progress) throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "setTaskProgress", "set application progress for job="+jobID+" progress="+progress);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		try
		{
			engine.setTaskProgress(actorLogin, jobID, progress);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "setTaskProgress", "error setting application progress for job="+jobID+" progress="+progress, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Returns the List of parameters used while executing the specified Job
	 * @param accessToken  the access token required to make this call
	 * @param jobID ID of the job
	 * @return the list of parameters used when the task was submitted
	 * @throws RemoteException
	 */
	@Override
	public NVPair[] getTaskParameters(String accessToken, long jobID)
			throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getTaskParameters", "fetching assigned parameters for application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		Map<String, String> taskParameters;
		try
		{
			taskParameters = engine.getTaskParameters(actorLogin, jobID);
			return toNVPairParameters(taskParameters);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "getTaskParameters", "error fetching assigned parameters for application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
		
		
	}
	
	/**
	 * Returns the list of Record GUIDs which are used as inputs 
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job
	 * @return list of input Records (their GUID)
	 * @throws RemoteException
	 */
	@Override
	public long[] getTaskInputs(String accessToken, long jobID) throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getTaskInputs", "fetching input record guids assigned for application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		long[] taskInputs;
		try
		{
			taskInputs = engine.getTaskInputs(actorLogin, jobID);
			return taskInputs;
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "getTaskInputs", "error fetching input record guids assigned for application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Returns the list of Record GUIDs which are potentially created by this Job
	 * Note that is call is valid only after the said job is ended successfully
	 * @param accessToken the access token required to make this call
	 * @param jobID ID of the job
	 * @return list of Records (their GUID) that are create or null if there are none
	 * @throws RemoteException
	 */
	@Override
	public long[] getTaskOutputs(String accessToken, long jobID) throws RemoteException
	{
		String actorLogin = Service.COMPUTE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceComputeImpl", "getTaskOutputs", "fetching records that are created (if any) by application "+jobID);
		
		ComputeEngine engine = SysManagerFactory.getComputeEngine();
		long[] taskOutputs = null;
		try
		{
			taskOutputs = engine.getTaskOutputs(actorLogin, jobID);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "getTaskOutputs", "error fetching output record guids assigned for application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
		
		return taskOutputs;
	}
	
	//this is a Coercion Method put here to avoid confusion between NVPair defined in iserver.compute and iserver.worker
	private NVPair[] toNVPairParameters(Map<String, String> parameters)
	{
		if(parameters == null) return null;
		
		List<NVPair> nvPairParameters = new ArrayList<NVPair>();
		for(Entry<String, String> parameter: parameters.entrySet())
		{
			String name = parameter.getKey();
			String value = parameter.getValue();
			
			NVPair pair = new NVPair(name, value);
			nvPairParameters.add(pair);
		}
		return nvPairParameters.toArray(new NVPair[0]);
	}
	
	@Override
	public String getTaskLogUploadURL(String accessToken, long jobID, String filename) throws RemoteException
	{
		String actorLogin = Service.UPDATE.validateAccessToken(accessToken);
		
		try
		{
			return SysManagerFactory.getComputeEngine().getUploadURL(actorLogin, jobID, filename, getClientIPAddress());
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageSpaceComputeImpl", "getTaskLogUploadURL", "error fetching upload url for application "+jobID, e);
			throw new RemoteException(e.getMessage());
		}
	}
	
	private String getClientIPAddress(){

        MessageContext context = MessageContext.getCurrentContext();
        Session session = context.getSession();

        String clientIP = null;
        if(session instanceof AxisHttpSession)
        {
            clientIP = ((AxisHttpSession) session).getClientAddress();
        }

        if(clientIP == null)
            clientIP = "";
        
        return clientIP;
    }

	@Override
	public DoubleListConstraints testMethod1(String in0) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleRangeConstraints testMethod2(String in0) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LongListConstraints testMethod3(String in0) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LongRangeConstraints testMethod4(String in0) throws RemoteException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
