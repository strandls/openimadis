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
 * ImageSpaceWorkerManagerImpl.java
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
import java.util.HashSet;
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
import com.strandgenomics.imaging.icore.app.JobReport;
import com.strandgenomics.imaging.icore.app.JobState;
import com.strandgenomics.imaging.icore.app.ListConstraints;
import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.ParameterConstraints;
import com.strandgenomics.imaging.icore.app.ParameterType;
import com.strandgenomics.imaging.icore.app.RangeConstraints;
import com.strandgenomics.imaging.icore.app.Work;
import com.strandgenomics.imaging.icore.app.WorkerState;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.auth.AuthorizationException;
import com.strandgenomics.imaging.iengine.compute.Publisher;
import com.strandgenomics.imaging.iengine.system.ComputeEngine;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.ws.worker.Application;
import com.strandgenomics.imaging.iserver.services.ws.worker.Constraints;
import com.strandgenomics.imaging.iserver.services.ws.worker.Directive;
import com.strandgenomics.imaging.iserver.services.ws.worker.DoubleListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.worker.DoubleRangeConstraints;
import com.strandgenomics.imaging.iserver.services.ws.worker.ImageSpaceWorkers;
import com.strandgenomics.imaging.iserver.services.ws.worker.LongListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.worker.LongRangeConstraints;
import com.strandgenomics.imaging.iserver.services.ws.worker.NVPair;
import com.strandgenomics.imaging.iserver.services.ws.worker.Request;
import com.strandgenomics.imaging.iserver.services.ws.worker.Response;
import com.strandgenomics.imaging.iserver.services.ws.worker.StringListConstraints;
import com.strandgenomics.imaging.iserver.services.ws.worker.WorkStatus;

/**
 * Implementation of iWorkers webservices
 * @author arunabha
 *
 */
public class ImageSpaceWorkerManagerImpl implements ImageSpaceWorkers, Serializable {

	private static final long serialVersionUID = -6033135758738998995L;
	private transient Logger logger = null;
	
	public ImageSpaceWorkerManagerImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}
	
	   /**
     * the first thing a worker-manager is expected to do is to register itself
     * with the compute server as a trusted worker-manager with its name and
     * and the publisher-code obtained (using the web-client, the master application)
     * 
     * If the name and publisher-code matches, the server returns a valid access-token or permit
     *  
     * Once the worker-manager receives a permit or access-token, it is expected that
     * the worker-manager will continuously ping the server to maintain a heart-beat.
     * 
     * A valid WorkPermit is required to pull Task from the compute server 
     * (if available).
     * 
     * @param name name of the publisher
     * @param publisherCode the authorization code granted by the Administrator (from web-client)
     * @return the access token required to make calls if valid, null otherwise
     */
	@Override
    public String register(String name, String publisherCode) throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceWorkerManagerImpl", "register", "registering a publisher, name="+name +", publisherCode="+publisherCode);
		try
		{
			ComputeEngine engine = SysManagerFactory.getComputeEngine();
			
			return engine.registerWorkerSystem(getClientIPAddress(), name, publisherCode);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "register", "error registing worker-systems "+name, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * The worker-system publishes a bunch of applications
     * @param accessToken the access token needed to make this call
     * @param apps a list of application supported by this worker-system
     * @return the directive from the compute-server to the worker
     */
	@Override
    public Directive publishApplications(String accessToken, Application[] apps) throws RemoteException
    {
		String publisher = validate(accessToken);
		logger.logp(Level.INFO, "ImageSpaceWorkerManagerImpl", "publishApplications", publisher+" publishing applications "+apps);
		try
		{
			ComputeEngine engine = SysManagerFactory.getComputeEngine();
			com.strandgenomics.imaging.icore.app.Directive d = engine.publishApplications(accessToken, toApplicationSpecification(apps));
			return d == null ? null : new Directive(d.name());
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "publishApplications", "error publishing applications by worker-systems ", ex);
			throw new RemoteException(ex.getMessage());
		}
    }
  

	/**
     * The worker-system removes a bunch of published applications
     * @param accessToken the access token needed to make this call
     * @param apps a list of application no longer supported by this worker-system
     * @return the directive from the compute-server to the worker
     */
	@Override
    public Directive removeApplications(String accessToken, Application[] apps) throws RemoteException
    {
		String publisher = validate(accessToken);
		logger.logp(Level.INFO, "ImageSpaceWorkerManagerImpl", "removeApplications", publisher+" publishing applications "+apps);
		try
		{
			ComputeEngine engine = SysManagerFactory.getComputeEngine();
			com.strandgenomics.imaging.icore.app.Directive d = engine.removeApplications(accessToken, toApplication(apps));
			return d == null ? null : new Directive(d.name());
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceLoaderImpl", "removeApplications", "error removing applications by worker-systems ", ex);
			throw new RemoteException(ex.getMessage());
		}
    }
    
    /**
     * The worker-system ping the server periodically with its current status and 
     * picks up jobs and relevant directives from the server
     * @param accessToken the access token needed to make this call
     * @param request the worker request
     * @return the response of the server on such a request 
     */
	@Override
    public Response ping(String accessToken, Request request) throws RemoteException
    {
		try
		{
			String publisher = validate(accessToken);
			logger.logp(Level.FINEST, "ImageSpaceWorkerManagerImpl", "ping", publisher+" ping with "+request);
			
			ComputeEngine engine = SysManagerFactory.getComputeEngine();
			com.strandgenomics.imaging.icore.app.Request localRequest = toRequest(request);
			
			com.strandgenomics.imaging.icore.app.Response localResponse = engine.ping(accessToken, localRequest);
			Response response = toResponse(localResponse);
			
			logger.logp(Level.FINEST, "ImageSpaceWorkerManagerImpl", "ping", "converted succussfully "+response);
			return response;
		}
		catch (AuthorizationException e) 
		{
			logger.logp(Level.FINEST, "ImageSpaceWorkerManagerImpl", "ping", "error code "+e.getErrorCode().getCode());
			
			if(e.getErrorCode().getCode() == ErrorCode.ImageSpace.INVALID_CREDENTIALS)
			{
				logger.logp(Level.FINEST, "ImageSpaceWorkerManagerImpl", "ping", "returning registration required");
				
				com.strandgenomics.imaging.icore.app.Response localResponse = new com.strandgenomics.imaging.icore.app.Response( com.strandgenomics.imaging.icore.app.Directive.REGISTRATION_REQUIRED );
				return toResponse(localResponse);
			}
			throw e;
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceWorkerManagerImpl", "ping", "error ", ex);
			throw new RemoteException(ex.getMessage());
		}
    }
    
	private Response toResponse(com.strandgenomics.imaging.icore.app.Response localResponse)
	{
		if(localResponse == null) return null;
		
		com.strandgenomics.imaging.icore.app.Directive directive = localResponse.command;
		
		Work job = localResponse.job;
		com.strandgenomics.imaging.iserver.services.ws.worker.Work work = toWork(job);

		Response response = new Response(directive.name(), work);
		return response;
	}

	private com.strandgenomics.imaging.iserver.services.ws.worker.Work toWork(Work job)
	{
		if(job == null) return null;
		
		long[] inputs = job.inputGUIDs;
		List<Long> inputList = new ArrayList<Long>();
		if(inputs!=null)
		{
			for(long input:inputs)
			{
				inputList.add(input);
			}
		}

		com.strandgenomics.imaging.iserver.services.ws.worker.Work work = new com.strandgenomics.imaging.iserver.services.ws.worker.Work();
		work.setAppAuthCode(job.getAuthCode());
		work.setAppName(job.application.getName());
		work.setInputRecords(inputList.toArray(new Long[0]));
		work.setTaskID(job.ID);
		work.setVersion(job.application.getVersion());
		NVPair[] nvPairParameters = toNVPairParameters(job.parameters);
		work.setParameters(nvPairParameters);
		
		return work;
	}

	//this is a Coercion Method put here to avoid confusion between NVPair defined in iserver.compute and iserver.worker
	private NVPair[] toNVPairParameters(Map<String, String> parameters)
	{
		if(parameters == null) return null;
		
		List<NVPair> nvPairParameters = new ArrayList<NVPair>();
		for(Entry<String, String> parameter: parameters.entrySet())
		{
			logger.logp(Level.FINEST, "ImageSpaceWorkerManagerImpl", "toNVPairParameters", "parameter name="+parameter.getKey()+"="+parameter.getValue());
			
			String name = parameter.getKey();
			String value = parameter.getValue();
			
			NVPair pair = new NVPair(name, value);
			nvPairParameters.add(pair);
		}
		return nvPairParameters.toArray(new NVPair[0]);
	}

	//dummy api for exposing the constraints subclasses, remove if not needed anymore
	@Override
	public StringListConstraints testMethod0(String appName) throws RemoteException
	{
		return null;
	}
	
	@Override
	public DoubleListConstraints testMethod1(String appName) throws RemoteException
	{
		return null;
	}
	
	@Override
	public DoubleRangeConstraints testMethod2(String appName) throws RemoteException
	{
		return null;
	}
	@Override
	public LongListConstraints testMethod3(String appName) throws RemoteException
	{
		return null;
	}
	
	@Override
	public LongRangeConstraints testMethod4(String appName) throws RemoteException
	{
		return null;
	}
	
	/**
	 * Returns the publisher name for the specified access token
	 * @param accessToken the access token under consideration
	 * @return the publisher name
	 */
	private String validate(String accessToken)
	{
		Publisher p = SysManagerFactory.getComputeEngine().getPublisherForAccessToken(accessToken);
		if(p == null)
			throw new AuthorizationException(new ErrorCode(ErrorCode.ImageSpace.INVALID_CREDENTIALS));
		
		return p.name;
	}
	
    private String getClientIPAddress(){

        MessageContext context = MessageContext.getCurrentContext();
        Session session = context.getSession();

        String clientIP = null;
        if(session instanceof AxisHttpSession)
        {
            //clientIP = ((AxisHttpSession) session).getClientAddress();
        }

        if(clientIP == null)
            clientIP = "";
        
        return clientIP;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// coercion helpers //////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    
    private Set<com.strandgenomics.imaging.icore.app.Application> toApplication(Application[] apps) 
    {
    	if(apps == null || apps.length == 0)
    		return null;
    	
    	Set<com.strandgenomics.imaging.icore.app.Application> specs = new HashSet<com.strandgenomics.imaging.icore.app.Application>();
    	for(Application a : apps)
    	{
    		com.strandgenomics.imaging.icore.app.Application s = toApplicationSpecification(a);
    		if(s != null) specs.add(s);
    	}
    	
    	return specs;
	}
    
    private Set<ApplicationSpecification> toApplicationSpecification(Application[] apps) 
    {
    	if(apps == null || apps.length == 0)
    		return null;
    	
    	Set<ApplicationSpecification> specs = new HashSet<ApplicationSpecification>();
    	for(Application a : apps)
    	{
    		ApplicationSpecification s = toApplicationSpecification(a);
    		if(s != null) specs.add(s);
    	}
    	
    	return specs;
	}

	private ApplicationSpecification toApplicationSpecification(Application a) 
	{
		if(a == null) return null;
		
		com.strandgenomics.imaging.iserver.services.ws.worker.Parameter[] parameters = a.getParameters();
		Set<Parameter> remoteParameters = toParameters(parameters);
		
		return new ApplicationSpecification(a.getClientID(), a.getName(), a.getVersion(),a.getCategoryName(),a.getDescription(), remoteParameters);
	}

	private Set<Parameter> toParameters(com.strandgenomics.imaging.iserver.services.ws.worker.Parameter[] parameters)
	{
		if(parameters == null || parameters.length == 0)
			return null;
		
		Set<Parameter> pList = new HashSet<Parameter>();
		for(com.strandgenomics.imaging.iserver.services.ws.worker.Parameter p : parameters)
		{
			Parameter pp = toParameter(p);
			if(pp != null)
			{
				pList.add(pp);
			}
		}
		
		return pList;
	}

	private Parameter toParameter(com.strandgenomics.imaging.iserver.services.ws.worker.Parameter p) 
	{
		if(p == null) return null;
		ParameterType type = ParameterType.valueOf(p.getType());
		
		Object deserializedValue = convertDefaultValue(p.getDefaultValue(), type); 
		Object defaultValue = type.isValidValue( deserializedValue) ? deserializedValue : null;
		
		ParameterConstraints constraints = toParameterConstraints(p.getConstraints());
		
		if(constraints !=null && constraints.getType() != type)
			throw new IllegalArgumentException("type mismatch in constraints, expecting "+type +" found "+constraints.getType() );
		
		return new Parameter(p.getName(), type, constraints, defaultValue, p.getDescription());
	}
	
	private Object convertDefaultValue(Object value, ParameterType type)
	{
		Object convertedValue = null;
		switch (type)
		{
		case BOOLEAN:
			convertedValue = Util.getBoolean(value);
			break;
		case INTEGER:
			convertedValue = Util.getInteger(value);
			break;
		case DECIMAL:
			convertedValue = Util.getDouble(value);
			break;
		case STRING:
			convertedValue = Util.getString(value);
			break;
		}
		
		return convertedValue;
	}
	
	private com.strandgenomics.imaging.icore.app.Request toRequest(Request request)
	{
		if(request == null) return null;
		
		String state = request.getWorkerState();
		WorkerState workerState = WorkerState.valueOf(state);
		
		WorkStatus[] workStatus = request.getActiveJobs();
		List<JobReport> jobs = new ArrayList<JobReport>();
		for(int i=0;i<workStatus.length;i++)
		{
			JobReport job = toJobReport(workStatus[i]);
			jobs.add(job);
		}
		
		com.strandgenomics.imaging.icore.app.Request localRequest = new com.strandgenomics.imaging.icore.app.Request(workerState, jobs);
		return localRequest;
	}

	private JobReport toJobReport(WorkStatus workStatus)
	{
		if(workStatus == null) return null;
		
		int state = workStatus.getTaskState();
		JobState jobState = JobState.values()[state];
		
		JobReport job = new JobReport(workStatus.getTaskID(), jobState);
		return job;
	}

	private ParameterConstraints toParameterConstraints(Constraints constraints) 
	{
		if(constraints == null) return null;

		if(constraints instanceof LongListConstraints)
		{
			Long[] validValues = ((LongListConstraints)constraints).getValidValues();
			long[] values = null;
			if(validValues!=null)
			{
				values = new long[validValues.length];
				int i=0;
				for(Long value:validValues)
				{
					values[i] = value;
					i++;
				}
			}
			return new ListConstraints(values);
		}
		else if(constraints instanceof DoubleListConstraints)
		{
			Double[] validValues = ((DoubleListConstraints)constraints).getValidValues();
			double[] values = null;
			if(validValues!=null)
			{
				values = new double[validValues.length];
				int i=0;
				for(Double value:validValues)
				{
					values[i] = value;
					i++;
				}
			}
			return new ListConstraints(values);
		}
		else if(constraints instanceof StringListConstraints)
		{
			return new ListConstraints(((StringListConstraints)constraints).getValidValues());
		}
		else if(constraints instanceof LongRangeConstraints)
		{
			LongRangeConstraints c = (LongRangeConstraints) constraints;
			return new RangeConstraints(c.getLowerLimit(), c.getUpperLimit());
		}
		else if(constraints instanceof DoubleRangeConstraints)
		{
			DoubleRangeConstraints c = (DoubleRangeConstraints) constraints;
			return new RangeConstraints(c.getLowerLimit(), c.getUpperLimit());
		}
		else
		{
			throw new IllegalArgumentException("unknown Constraints type "+constraints.getClass());
		}
	}
}
