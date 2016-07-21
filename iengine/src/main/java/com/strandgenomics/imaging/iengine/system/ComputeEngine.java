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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.ApplicationSpecification;
import com.strandgenomics.imaging.icore.app.Directive;
import com.strandgenomics.imaging.icore.app.JobReport;
import com.strandgenomics.imaging.icore.app.JobState;
import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.Request;
import com.strandgenomics.imaging.icore.app.Response;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.compute.ComputeException;
import com.strandgenomics.imaging.iengine.compute.ComputeWorker;
import com.strandgenomics.imaging.iengine.compute.Publisher;
import com.strandgenomics.imaging.iengine.compute.PublisherKey;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.ActiveTaskDAO;
import com.strandgenomics.imaging.iengine.dao.ArchivedTaskDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.PublisherDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.AuthCode;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.User;

/**
 * The compute service provided by the AVADIS iManage engine
 * @author arunabha
 *
 */
public class ComputeEngine extends SystemManager {
	
	/**
	 * worker is expected to ping atleast once in every 2 min
	 */
	private static final long WORKER_PING_TIMEOUT = 2 * 60 * 1000;
	
    ComputeEngine()
    {
    	initialize();
    }

    private void initialize()
    {
    	logger.logp(Level.INFO, "ComputeEngine", "initialize", "initializing the Compute Engine");
    	
    	try
		{
    		loadMonitoredTask();
		}
		catch (DataAccessException e)
		{ 
			logger.logp(Level.WARNING, "ComputeEngine", "initialize", "failed initializing the Compute Engine", e);
		}
    }
    
    private void insertPublisherInDB(Publisher r)
	{
    	PublisherDAO publisherDAO = DBImageSpaceDAOFactory.getDAOFactory().getPublisherDAO();
    	
    	try
		{
    		logger.logp(Level.FINE, "ComputeEngine", "insertPublisherInDB", "storing publisher in DB "+r.name);
    		
			publisherDAO.insertPublisher(r.name, r.description, r.publisherCode, r.ipFilter);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ComputeEngine", "insertPublisherInDB", "failed storing publisher in DB pub name = "+r.name, e);
		}
	}
    
    private void loadMonitoredTask() throws DataAccessException 
    {
    	ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
    	List<Task> allTasks = activeTaskDAO.getAllTasks();
    	if(allTasks!=null)
    	{
    		for(Task task:allTasks)
        	{
        		if(task.isMonitored()){
        			addMonitoredTask(task.getOwner(), task);
        		}
        	}
    	}
    	
		ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
		List<Task> archievedMonitoredTasks = archivedTaskDAO.getMonitoredTasks();
		if (archievedMonitoredTasks != null)
		{
			for (Task task : archievedMonitoredTasks)
			{
				if (task.isMonitored())
				{
					addMonitoredTask(task.getOwner(), task);
				}
			}
		}
    	
    	logger.logp(Level.FINE, "ComputeEngine", "initialize", "loaded the monitored task");
	}

    
	/**
	 * Returns the list of available publisher 
	 * @param actorLogin the login of the user making this call
	 * @return the registered publishers
	 * @throws DataAccessException 
	 */
	public Collection<Publisher> getPublisher(String actorLogin)
	{
		List<Publisher> publishers;
		try
		{
			publishers = ImageSpaceDAOFactory.getDAOFactory().getPublisherDAO().listPublishers();
		}
		catch (DataAccessException e)
		{
			return new ArrayList<Publisher>();
		}
		
		return publishers;
	}
	
	/**
	 * Returns the list of publisher for the specified application
	 * @param actorLogin the login of the user making this call
	 * @return the registered publishers
	 */
	public Set<Publisher> getPublisher(String actorLogin, Application app)
	{
		Set<Publisher> publishers = new HashSet<Publisher>();
		List<Publisher> workers;
		try
		{
			workers = ImageSpaceDAOFactory.getDAOFactory().getPublisherDAO().listPublishers();
		}
		catch (DataAccessException e)
		{
			return publishers;
		}
		
		for(Publisher worker : workers)
		{
			if(worker.isApplicationSupported(app))
			{
				publishers.add(worker);
			}
		}
		
		return publishers;
	}
	
	/**
	 * returns all applications
	 * @param actorLogin
	 * @throws DataAccessException 
	 */
	public List<ApplicationSpecification> listAllApplications(String actorLogin) throws DataAccessException
	{
		logger.logp(Level.INFO, "ComputeEngine", "listAllApplications", "listing all applications");
		List<ApplicationSpecification> apps = new ArrayList<ApplicationSpecification>();
		
		List<Publisher> publishers = ImageSpaceDAOFactory.getDAOFactory().getPublisherDAO().listPublishers();
		if(publishers!=null)
		{
			for(Publisher publisher:publishers)
			{
				Set<ApplicationSpecification> publisherApps = listApplications(actorLogin, publisher.name);
				if(publisherApps != null)
					apps.addAll(publisherApps);
			}
		}
		
		return apps;
	}
	
	/**
	 * Returns the list of available application with the given category and from the specified publisher 
	 * @param actorLogin the login of the user making this call
	 * @param publisher publisher name (null will mean a listing across all publisher)
	 * @param categoryName name of the category  (null will mean a listing across all categories)
	 * @return list of available applications
	 */
	public List<Application> listApplications(String actorLogin, String publisherName, String categoryName)
	{
		logger.logp(Level.INFO, "ComputeEngine", "listApplications", "listing applications for category "+categoryName +" for publisher "+publisherName);
		
		Set<ApplicationSpecification> publisherApps = listApplications(actorLogin, publisherName);
		
		if(publisherApps == null) return null;
			
		List<Application> apps = new ArrayList<Application>();
		for(ApplicationSpecification app:publisherApps)
		{
			if(app.categoryName.equals(categoryName))
				apps.add(app);
		}
		
		return apps;
	}
	
	private Set<ApplicationSpecification> listApplications(String actorLogin, String publisherName)
	{
		logger.logp(Level.FINE, "ComputeEngine", "listApplications", "listing applications for publisher "+publisherName);
		
		Publisher publisher = null;
		try
		{
			publisher = ImageSpaceDAOFactory.getDAOFactory().getPublisherDAO().getPublisher(publisherName);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ComputeEngine", "listApplications", "error in getting the publisher "+ publisherName);
		}
		
		if(publisher == null) return null;
		
		logger.logp(Level.FINEST, "ComputeEngine", "listApplications", "found publisher "+publisher.name);
		
		if(!SysManagerFactory.getCacheManager().isCached(new CacheKey(publisherName, CacheKeyType.PublisherPing))) return null;
		
		long lastPingTime = (Long) SysManagerFactory.getCacheManager().get(new CacheKey(publisherName, CacheKeyType.PublisherPing));
		
		logger.logp(Level.FINEST, "ComputeEngine", "listApplications", "last ping time for publisher "+lastPingTime);
		
		if(System.currentTimeMillis() - lastPingTime >= WORKER_PING_TIMEOUT)
		{
			return null;
		}

//		Set<ApplicationSpecification> publisherApps = publisher.getSupportedApplications();
		@SuppressWarnings("unchecked")
		Set<ApplicationSpecification> publisherApps = (Set<ApplicationSpecification>) SysManagerFactory.getCacheManager().get(new CacheKey(publisher.name, CacheKeyType.AuthorizedPublisher));
		
		logger.logp(Level.FINEST, "ComputeEngine", "listApplications", "supported applications from publisher "+publisherApps.size());
		
		return publisherApps;
	}
	
	public ApplicationSpecification getApplicationSpecification(Application app)
	{
		if(SysManagerFactory.getCacheManager().isCached(new CacheKey(app, CacheKeyType.ApplicationSpecification)))
			return (ApplicationSpecification) SysManagerFactory.getCacheManager().get(new CacheKey(app, CacheKeyType.ApplicationSpecification));
		return null;
	}


	/**
	 * Executes the specified registered-application with the specified parameters (if any) 
	 * on the specified list of records as input
	 * @param actorLogin the login of the user making this call
	 * @param app the application pre-registered with the server (through plugins to compute-server)
	 * @param authCode the authorization code granted by the user to execute the specified application
	 * @param priority priority of the task (running application) under consideration 
	 * @param parameters the parameters to run the application
	 * @param guids the input records
	 * @return an handle to the remotely executing task
	 * @throws DataAccessException 
	 */
	public Task executeApplication(String actorLogin, Application app, AuthCode authCode, Priority priority, String projectName,
			Map<String, String> parameters, long ... guids) throws DataAccessException
	{
		logger.logp(Level.INFO, "ComputeEngine", "executeApplication", "trying to execute application "+app +" with records "+guids);
		
		if(actorLogin == null || app == null || authCode == null || guids == null)
			throw new NullPointerException("null user/application/authorization/input records ");
		
		//check permissions to read the input guids
		checkPermission(actorLogin, guids);
		//creates the task handling 
		Task t = new Task(actorLogin, app, authCode.getId(), authCode.getInternalID(), priority, projectName, parameters, guids);
		//update in db
		updateDBState(t, State.SCHEDULED);
		
		// add history
		addHistory(t);
		
		return t;
	}
	
	private void addHistory(Task t) throws DataAccessException
	{
		long guids[] = t.getInputs();
		
		if(guids == null) return;
		
		// history type
		HistoryType type = HistoryType.TASK_EXECUTED;
		switch(t.getState())
		{
			case TERMINATED:
			case DELETED:
				type = HistoryType.TASK_TERMINATED;
				break;
			case ERROR:
				type = HistoryType.TASK_FAILED;
				break;
			case SUCCESS:
				type = HistoryType.TASK_SUCCESSFUL;
				break;
			default:
					break;
		}
		
		// parameters to string
		StringBuffer paramString = new StringBuffer();
		Map<String, String> parameters = t.getTaskParameters();
		if(parameters!=null)
		{
			for(Entry<String,String>param:parameters.entrySet())
			{
				paramString.append(" Param1: ("+param.getKey()+","+param.getValue()+") ");
			}
		}
		
		// application
		Application app = t.getApplication();
		
		for(long guid:guids)
		{
			addHistory(guid, app, t.getOwner(), t.work.getAuthCode(), type, app.name, app.version, String.valueOf(t.getID()), paramString.toString());
		}
	}

	private void addHistory(long guid, Application app, String user, String accessToken, HistoryType type, String... args) throws DataAccessException
	{
		SysManagerFactory.getHistoryManager().insertHistory(guid, app, user, accessToken, type, type.getDescription(guid, user, args), args);
	}
	
	/**
	 * Schedules the specified registered-application for execution with the specified parameters (if any) 
	 * on the specified list of records as input
	 * @param actorLogin the login of the user making this call
	 * @param app the application pre-registered with the server (through plugins to compute-server)
	 * @param authCode the authorization code granted by the user to execute the specified application
	 * @param priority priority of the task (running application) under consideration 
	 * @param waitTime the wait time (in milliseconds) after which this task is scheduled for execution 
	 * @param parameters the parameters to run the application
	 * @param guids the input records
	 * @return an handle to the remotely executing task
	 * @throws DataAccessException 
	 */
	public Task scheduleApplication(String actorLogin, Application app, AuthCode authCode, Priority priority, String projectName,
			boolean isMonitored, long waitTime, Map<String, String> parameters, long ... guids) throws DataAccessException
	{
		logger.logp(Level.INFO, "ComputeEngine", "scheduleApplication", "trying to execute application "+app +" with records "+guids);
		
		if(actorLogin == null || app == null || authCode == null || guids == null)
			throw new NullPointerException("null user/application/authorization/input records ");
		
		//check permissions to read the input guids
		checkPermission(actorLogin, guids);
		//creates the task handling 
		Task t = new Task(actorLogin, app, authCode.getId(), authCode.getInternalID(), priority, projectName, isMonitored, waitTime, parameters, guids);
		// Insert the specified task to monitoredTasks
		if(isMonitored == true){
			addMonitoredTask(actorLogin,t);
		}
		//update in db
		updateDBState(t, State.SCHEDULED);
		// add history
		addHistory(t);
		
		return t;
	}
	
	

	/**
	 * returns server side unique client id for specified application
	 * @param appName
	 * @param appVersion
	 * @return server side unique client id for specified application
	 */
	public String getClientID(String appName, String appVersion)
	{
		Application app = new Application(appName, appVersion);
		
		if(!SysManagerFactory.getCacheManager().isCached(new CacheKey(app, CacheKeyType.ApplicationSpecification))) return null;
		
		ApplicationSpecification appSpec = (ApplicationSpecification) SysManagerFactory.getCacheManager().get(new CacheKey(app, CacheKeyType.ApplicationSpecification));
		return appSpec.getID();
	}

	/**
	 * Returns the required application specific parameters for the specified application 
	 * @param actorLogin the login of the user making this call
	 * @param application application under consideration
	 * @return a list of relevant parameters or null, if nothing there
	 */
	public Set<Parameter> getParameters(String actorLogin, Application application) 
	{
		if(!SysManagerFactory.getCacheManager().isCached(new CacheKey(application, CacheKeyType.ApplicationSpecification))) return null;
		
		ApplicationSpecification appSpec = (ApplicationSpecification) SysManagerFactory.getCacheManager().get(new CacheKey(application, CacheKeyType.ApplicationSpecification));
		return appSpec.parameters;
	}
	
	/**
	 * Returns the required application specific parameters for the specified application 
	 * @param actorLogin the login of the user making this call
	 * @param appName application under consideration
	 * @param appVersion application under consideration
	 * @return a list of relevant parameters or null, if nothing there
	 */
	public Set<Parameter> getParameters(String actorLogin, String appName, String appVersion)
	{
		Application application = new Application(appName, appVersion);
		return getParameters(actorLogin, application);
	}

	/**
	 * Returns the state of the task submitted for execution
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return the status code. one of SCHEDULED,WAITING,PAUSED,ALLOCATED,EXECUTING,DELETING, DELETED,ERROR,SUCCESS,TERMINATED
	 * @throws DataAccessException 
	 */
	public State getJobState(String actorLogin, long taskID) throws DataAccessException 
	{
		synchronized(this)
		{
			State activeState = findStateFromActive(taskID);
			if(activeState!=null)
				return activeState;
			
			return findStateFromArchives(taskID);
		}
	}

	/**
	 * Reschedule the specified job which is still to be allocated (to a worker for execution)
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @param waitTime the new wait time interval
	 * @return true if successful, false otherwise
	 * @throws DataAccessException 
	 */
	public boolean rescheduleTask(String actorLogin, long taskID, long waitTime) throws DataAccessException
	{
		synchronized(this)
		{
			ActiveTaskDAO activeTaskDao = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
			Task t = activeTaskDao.getTask(taskID);
			
			if(t==null)
				return false;// not active task
			
			if(t.isRunning())
			{
				throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_RUNNING));
			}
			
			if (t.getState() == State.WAITING || t.getState() == State.SCHEDULED)
			{
				t.reSchedule(waitTime);
				
				activeTaskDao.updateScheduleTime(t.getID(), t.getScheduleTime());
				updateDBState(t, State.SCHEDULED);
				return true;
			}
			
			return false;
		}
	}

	/**
	 * Pauses the specified job  which is still to be allocated (to a worker for execution)
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 * @throws DataAccessException 
	 */
	public boolean pauseTask(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			ActiveTaskDAO activeTaskDao = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
			Task task = activeTaskDao.getTask(taskID);
			if(task == null)
				return false;// task is not active
			
			if(task.isRunning())
			{
				throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_RUNNING));
			}
			
			if (task.getState() == State.SCHEDULED || task.getState() == State.WAITING)
			{
				task.setPause();
				updateDBState(task, task.getState());
				return true;
			}
			
			return false;
		}
	}

	/**
	 * Resumes the specified job  which is in a paused state
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 * @throws DataAccessException 
	 */
	public boolean resumeTask(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			ActiveTaskDAO activeTaskDao = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
			Task t = activeTaskDao.getTask(taskID);
			if(t==null)
				return false;
			
			if(t.isRunning())
			{
				throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_RUNNING));
			} 
			
			else if (t.getState() == State.PAUSED)
			{
				t.setResume();
				updateDBState(t, t.getState());
				return true;
			}
			
			return false;
		}
	}
	
	/**
	 * cancels the specified job: removes if job is not allocated, requests to terminate the job if job is running
	 * @param actorLogin the login of the user making this call
	 * @param taskID handle to the remotely executing task
	 * @return true if successful, false otherwise
	 * @throws DataAccessException
	 */
	public boolean cancelTask(String actorLogin, long taskID) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDao = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		
		Task task = activeTaskDao.getTask(taskID);
		if(task==null)
			return false;
		
		logger.logp(Level.INFO, "ComputeServlet", "cancelTask","here");
		
		if(task.isRunning())
		{
			logger.logp(Level.INFO, "ComputeServlet", "cancel task","terminating");
			return terminateTask(actorLogin, taskID);
		}
		
		return removeTask(actorLogin, taskID);
	}
	
	/**
	 * Removes the specified job  which is still to be allocated (to a worker for execution)
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 * @throws DataAccessException 
	 */
	public boolean removeTask(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			ActiveTaskDAO activeTaskDao = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
			Task task = activeTaskDao.getTask(taskID);

			if(task == null)
				return false;// task is not active
			
			if(task.isRunning())
			{
				throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_RUNNING));
			}
			
			if (task.getState()==State.SCHEDULED || task.getState()==State.WAITING)
			{
				task.setDeleted();
				
				archiveTask(task);
				return true;
			}
			
			return false;
		}
	}

	/**
	 * Terminate or kill the specified job which is being executed (by an worker)
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return true if successful, false otherwise
	 * @throws DataAccessException 
	 */
	public boolean terminateTask(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			ActiveTaskDAO activeTaskDao = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
			Task task = activeTaskDao.getTask(taskID);
			if(task!=null && task.isRunning())
			{
				task.setTerminate();
				updateDBState(task, task.getState());
				return true;
			}
			else
			{
				throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_NOT_RUNNING));
			}
		}
	}

	/**
	 * Returns the progress measure (0-100) associated the specified running task
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return the progress (0-100)
	 * @throws DataAccessException 
	 */
	public int getTaskProgress(String actorLogin, long taskID) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDao = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		Task task = activeTaskDao.getTask(taskID);
		if(task==null || !task.isRunning())
			throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_NOT_RUNNING));
		
		return task.getProgress();
	}
	
	/**
	 * sets the progress measure (0-100) associated the specified running task
	 * @param actorLogin the login of the user making this call
	 * @param jobID handle to the remotely executing task
	 * @param progress the progress (0-100)
	 * @throws DataAccessException 
	 */
	public void setTaskProgress(String actorLogin, long jobID, int progress) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDao = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		Task task = activeTaskDao.getTask(jobID);
		if(task == null || !task.isRunning())
			throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.TASK_IS_NOT_RUNNING));
		
		task.setProgress(progress);
		
		// update DB
		activeTaskDao.setTaskProgress(jobID, progress);
	}
	
	/**
	 * Returns the List of parameters used while executing the specified Job
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return the list of parameters used when the task was submitted
	 * @throws DataAccessException 
	 */
	public Map<String, String> getTaskParameters(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			Map<String, String> activeParameters = findParametersFromActive(taskID);
			if(activeParameters!=null)
				return activeParameters;
			
			return findParametersFromArchive(taskID);
		}
	}
	
	/**
	 * Returns the projectName for the specified Job
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return tthe projectName for the specified Job
	 * @throws DataAccessException 
	 */
	public String getTaskProject(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			String activeTaskProject = findProjectFromActive(taskID);
			if(activeTaskProject!=null && !activeTaskProject.isEmpty())
				return activeTaskProject;
			
			return findProjectFromArchive(taskID);
		}
	}
	
	public Map<String, Object> getTaskExecutionDetails(String actorLogin, long taskID) throws DataAccessException
	{
		Task task = getTask(taskID);
		if (task != null)
		{
			Map<String, Object> taskDetails = new HashMap<String, Object>();
			Application application = task.work.application;
			taskDetails.put("appName", application.name);
			taskDetails.put("appVersion", application.version);
			
			String desc = "";
			ApplicationSpecification appSpec = getApplicationSpecification(application);
			if(appSpec != null)
				desc = appSpec.getDescription();
			taskDetails.put("description", desc);
			taskDetails.put("nvpairs", task.work.parameters);
			taskDetails.put("priority", task.getPriority().name());
			taskDetails.put("scheduledtime", task.getScheduleTime());
			taskDetails.put("guids", task.work.inputGUIDs);
			return taskDetails;
		}
		return null;
	}
		
	private Task getTask(long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
			logger.logp(Level.INFO, "ComputeEngine", "getTask", "finding task = "+taskID);
			
			Task task = activeTaskDAO.getTask(taskID);
			if(task!=null)
				return task;
			
			ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
			logger.logp(Level.INFO, "ComputeEngine", "getTask", "finding task = "+taskID);
			
			return  archivedTaskDAO.getTask(taskID);
		}
	}


	/**
	 * Returns the list of Record GUIDs which are used as inputs for the specified task
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return list of input Records (their GUID)
	 * @throws DataAccessException 
	 */
	public long[] getTaskInputs(String actorLogin, long taskID) throws DataAccessException
	{
		synchronized(this)
		{
			long[] inputs = findActiveTaskInputs(taskID);
			if(inputs!=null)
				return inputs;
			
			return findTaskInputsFromArchive(taskID);
		}
	}
	
	/**
	 * Returns the list of Record GUIDs which are potentially created by this Job
	 * Note that is call is valid only after the said job is ended successfully
	 * @param actorLogin the login of the user making this call
	 * @param job handle to the remotely executing task
	 * @return list of Records (their GUID) that are create or null if there are none
	 * @throws DataAccessException 
	 */
	public long[] getTaskOutputs(String actorLogin, long taskID) throws DataAccessException
	{
		Task t = getTask(taskID);
		switch(t.getState())
		{
			case ALLOCATED:
				return getActiveTaskOutputs(t);
			case DELETED:
				return getArchivedTaskOutputs(t);
			case ERROR:
				return getArchivedTaskOutputs(t);
			case EXECUTING:
				return getActiveTaskOutputs(t);
			case PAUSED:
				return getActiveTaskOutputs(t);
			case SCHEDULED:
				return getActiveTaskOutputs(t);
			case SUCCESS:
				return getArchivedTaskOutputs(t);
			case TERMINATED:
				return getArchivedTaskOutputs(t);
			case TERMINATING:
				return getActiveTaskOutputs(t);
			case WAITING:
				return getActiveTaskOutputs(t);
			default:
				return getArchivedTaskOutputs(t);
		}
	}
	
	private long[] getActiveTaskOutputs(Task t) throws DataAccessException
	{
		Long accessTokenID = t.work.authCodeInternalId;
		String accessToken = SysManagerFactory.getAuthorizationManager().getAccessToken(accessTokenID);
		
		int projectID = t.getProject().getID();
		
		List<HistoryObject> taskHistory = SysManagerFactory.getHistoryManager().getAccessTokenHistory(projectID, accessToken);
		
		List<Long> outputs = new ArrayList<Long>();
		if(taskHistory!=null)
		{
			for(HistoryObject historyItem: taskHistory)
			{
				if(historyItem.getType() == HistoryType.RECORD_CREATED)
				{
					long guid = historyItem.getGuid();
					outputs.add(guid);
				}
			}
		}
		
		long outputGuids[] = new long[outputs.size()];
		int i=0;
		for(Long id:outputs)
		{
			outputGuids[i] = id;
			i++;
		}
		
		return outputGuids;
	}
	
	private long[] getArchivedTaskOutputs(Task t) throws DataAccessException
	{
		ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
		
		return archivedTaskDAO.getTaskOutputs(t.getID());
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// worker APIs /////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * Returns the publisher associated with the given access token
     * @param accessToken the access token granted by this engine to a publisher
     * @return the publisher associated with this access-token if any, null otherwise
     */
    public Publisher getPublisherForAccessToken(String accessToken)
    {
    	return (ComputeWorker) SysManagerFactory.getCacheManager().get(new CacheKey(accessToken, CacheKeyType.ComputeWorkers));
    }
    
    /**
     * Registers a publisher with its name and description and returns the publisher code
     * Typically will be done through the master application 
     * @param actorLogin the user making the call
     * @param name name of the publisher
     * @param description the description of the publisher
     * @return the publisher authorization code
     */
    public String registerPublisher(String actorLogin, String name, String description)
    {
    	logger.logp(Level.INFO, "ComputeEngine", "registerPublisher", "trying to register publisher "+name +" by "+actorLogin);
    	if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorLogin))
    		throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	
//    	synchronized(authorizedPublishers)
    	{
    		Publisher publisher = null;
			try
			{
				publisher = ImageSpaceDAOFactory.getDAOFactory().getPublisherDAO().getPublisher(name);
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "ComputeEngine", "registerPublisher", "error retrieving publisher name",e);
			}
			
	    	if(publisher!=null)
	    	{
	    		throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.PUBLISHER_ALREADY_EXIT, name));
	    	}
	    	
	    	Publisher r = new Publisher(name, description);
	    	
	    	// put publisher in DB
	    	insertPublisherInDB(r);
	    	
	    	return r.publisherCode;
    	}
    }
    
	/**
     * Registers a worker-system with the specified access-code 
     * @param name name of the publisher
     * @param publisherCode the authorization code granted by the Administrator
     * @param ipAddress the IP address of the machine generating this request
     * @return the one time access-token that enables the workers to make other calls
     */
    public String registerWorkerSystem(String ipAddress, String name, String publisherCode)
    {
    	Publisher publisher = null;
		try
		{
			publisher = ImageSpaceDAOFactory.getDAOFactory().getPublisherDAO().getPublisher(name);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ComputeEngine", "registerWorkerSystem", "publisher not found "+name, e);
		}
    	
    	if(publisher == null)
    	{
    		throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.PUBLISHER_DOESNT_EXIST));
    	}
    	PublisherKey key = publisher.validate(ipAddress, publisher.publisherCode, publisherCode);
    	
    	if(key == null) return null;
    	
		SysManagerFactory.getCacheManager().set(new CacheKey(key.accessToken, CacheKeyType.ComputeWorkers), new ComputeWorker(publisher));
		return key.accessToken;
    }
    
    /**
     * The worker-system publishes a bunch of applications
     * @param accessToken the access token needed to make this call
     * @param apps a list of application supported by this worker-system
     * @return the directive from the compute-server to the worker
     */
    public Directive publishApplications(String accessToken, Set<ApplicationSpecification> apps)
    {
    	ComputeWorker publisher = (ComputeWorker) SysManagerFactory.getCacheManager().get(new CacheKey(accessToken, CacheKeyType.ComputeWorkers));
    	
    	if(publisher == null)
    	{
    		logger.logp(Level.WARNING, "ComputeEngine", "registerWorkerSystem", "publisher not found "+publisher);
    		return Directive.REGISTRATION_REQUIRED;
    	}
    	
    	if(apps != null)
    	{
    		for(ApplicationSpecification app : apps)
        	{
    			logger.logp(Level.INFO, "ComputeEngine", "publishApplications", "publishing application "+app.getName()+" version = "+app.getVersion());
            	//if the application name, version is pre defined, get that entry
    			ApplicationSpecification existingSpec = (ApplicationSpecification) SysManagerFactory.getCacheManager().get(new CacheKey(app, CacheKeyType.ApplicationSpecification));
            	if(existingSpec == null)
            	{
            		existingSpec = app;
            		SysManagerFactory.getCacheManager().set(new CacheKey(new Application(app.name, app.version), CacheKeyType.ApplicationSpecification), app);
            	}
            	// update cache
            	existingSpec.setDescription(app.getDescription());
            	publisher.addApplication(existingSpec);
            	SysManagerFactory.getCacheManager().set(new CacheKey(new Application(app.name, app.version), CacheKeyType.ApplicationSpecification), existingSpec);
            	
            	SysManagerFactory.getCacheManager().set(new CacheKey(accessToken, CacheKeyType.ComputeWorkers), publisher);
            	SysManagerFactory.getCacheManager().set(new CacheKey(publisher.name, CacheKeyType.AuthorizedPublisher), publisher.getSupportedApplications());
        	}
    	}

    	return Directive.WAIT;
    }

    /**
     * The worker-system removes a bunch of published applications
     * @param accessToken the access token needed to make this call
     * @param apps a list of application no longer supported by this worker-system
     * @return the directive from the compute-server to the worker
     */
    public Directive removeApplications(String accessToken, Set<Application> apps)
    {
    	ComputeWorker publisher = (ComputeWorker) SysManagerFactory.getCacheManager().get(new CacheKey(accessToken, CacheKeyType.ComputeWorkers));
    	if(publisher == null)
    	{
    		return Directive.REGISTRATION_REQUIRED;
    	}
    	
    	for(Application a : apps)
    	{
    		publisher.removeApplication(a);
    	}
    	
    	// update cache
    	SysManagerFactory.getCacheManager().set(new CacheKey(publisher.name, CacheKeyType.AuthorizedPublisher), publisher.getSupportedApplications());
    	
    	if(publisher.capacity() == 0)
    		return Directive.PUBLISH_APPLICATIONS;
    	else
    		return Directive.WAIT;
    }
    
    /**
     * The worker-system ping the server periodically with its current status and 
     * picks up jobs and relevant directives from the server
     * @param accessToken the access token needed to make this call
     * @param request the worker request
     * @return the response of the server on such a request 
     * @throws DataAccessException 
     */
    public Response ping(String accessToken, Request request) throws DataAccessException
    {
    	ComputeWorker publisher = (ComputeWorker) SysManagerFactory.getCacheManager().get(new CacheKey(accessToken, CacheKeyType.ComputeWorkers));
    	if(publisher == null)
    	{
    		logger.logp(Level.INFO, "ComputeEngine", "ping", "ping from "+publisher+" for access token "+accessToken);
    		return new Response( Directive.REGISTRATION_REQUIRED );
    	}
    	
    	// update last ping time for publisher
    	logger.logp(Level.FINEST, "ComputeEngine", "ping", "ping from "+publisher.name);
    	SysManagerFactory.getCacheManager().set(new CacheKey(publisher.name, CacheKeyType.PublisherPing), System.currentTimeMillis());
    	
    	if(publisher.capacity() == 0)
    		return new Response( Directive.PUBLISH_APPLICATIONS );
    	
    	validateQ(); //validate the Queues
    	
    	Response r = checkTerminations(request.getJobs());
    	if(r != null) return r;
    	
    	Task t = null;
    	Directive command = Directive.WAIT;
    	
    	switch(request.state)
    	{
	    	case FREE:
	    		t = findCompatibleTask(publisher);
	    		break;
	    	case ENGAGED:
	    		updateTaskStatus(request.getJobs());
	    		//find a unallocated task, remove from the Q as well
	    		t = findCompatibleTask(publisher);
	    		break;
	    	case BUSY:
	    		updateTaskStatus(request.getJobs());
	    		command = Directive.CONTINUE;
	    		break;
    	}
    	
    	if(t != null)
    	{
    		updateDBState(t, State.ALLOCATED);
    		
    		Response response = t.allocateWork(publisher);
    		return response;
    	}
    	else
    	{
    		return new Response( command );
    	}
    }
    
    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// helper methods ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    
    private Response checkTerminations(Collection<JobReport> executingJobs) throws DataAccessException 
    {
    	for(JobReport report : executingJobs)
    	{
    		logger.logp(Level.FINEST, "ComputeEngine", "checkTerminations", "checking termination for task("+report.taskID+") status = "+report.state);
    		Task t = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO().getTask(report.taskID);
    		if(t!=null && t.needsTermination() && report.state == JobState.EXECUTING) 
    		{   
    			// job needs termination and is still running on the compute-worker side
    			updateDBState(t, State.TERMINATING);
    			return new Response(Directive.TERMINATE_TASK, t.work);
    		}
    	}
    	
    	return null;
	}

	private void updateTaskStatus(Collection<JobReport> executingJobs) throws DataAccessException 
    {
		if(executingJobs == null || executingJobs.isEmpty())
			return;
		
		for(JobReport report : executingJobs)
		{
			updateTaskStatus(report);
		}
	}

	private void updateTaskStatus(JobReport report) throws DataAccessException
	{
		Task t = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO().getTask(report.taskID);
		if(t == null)
			return;
		
		logger.logp(Level.INFO, "ComputeEngine", "updateTaskStatus", "updating task("+report.taskID+") status = "+report.state);
		
		switch(report.state)
		{
			case EXECUTING:
				t.setExecuting();
				try
				{
					updateDBState(t, State.EXECUTING);
				}
				catch (DataAccessException e)
				{ }
				break;
			case SUCCESSFUL:
				t.setSuccessFul();
				archiveTask(t);
				break;
			case FAILURE:
				t.setFailure();
				archiveTask(t);
				break;
			case TERMINATED:
				t.setTerminated();
				archiveTask(t);
				break;
		}
	}
	
	private String findProjectFromActive(long taskID) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findProjectFromActive", "finding task = "+taskID);
		
		Task task = activeTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getProject().getName();
	}
	
	private String findProjectFromArchive(long taskID) throws DataAccessException
	{
		ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findParametersFromArchive", "finding task = "+taskID);
		
		Task task = archivedTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getProject().getName();
	}
	
	private Map<String, String> findParametersFromActive(long taskID) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findParametersFromActive", "finding task = "+taskID);
		
		Task task = activeTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getTaskParameters();
	}
	
	private Map<String, String> findParametersFromArchive(long taskID) throws DataAccessException
	{
		ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findParametersFromArchive", "finding task = "+taskID);
		
		Task task = archivedTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getTaskParameters();
	}
	
	private State findStateFromActive(long taskID) throws DataAccessException 
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findStateFromActive", "finding task = "+taskID);
		
		Task task = activeTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getState();
	}
	
	private State findStateFromArchives(long taskID) throws DataAccessException 
	{
		ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findParametersFromArchive", "finding task = "+taskID);
		
		Task task = archivedTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getState();
	}
	
	private long[] findActiveTaskInputs(long taskID) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findParametersFromArchive", "finding task = "+taskID);
		
		Task task = activeTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getInputs();
	}
	
	private long[] findTaskInputsFromArchive(long taskID) throws DataAccessException
	{
		ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
		logger.logp(Level.INFO, "ComputeEngine", "findParametersFromArchive", "finding task = "+taskID);
		
		Task task = archivedTaskDAO.getTask(taskID);
		
		if(task == null) return null;
		
		return task.getInputs();
	}

	
	private void archiveTask(Task t) 
	{
		logger.logp(Level.INFO, "ComputeEngine", "archiveTask", "archiving task = "+t.getID());
		try
		{
			// get task outputs, if any
			long[] outputs = getActiveTaskOutputs(t);
			
			removeFromDB(t.getID());
			
			// insert into archived tasks
			ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
			archivedTaskDAO.insertTask(t.getID(), t.getApplication().name, t.getApplication().version, 
					t.getProject().getID() ,t.isMonitored(), t.getJobAuthCode(), t.work.authCodeInternalId, t.getOwner(), t.getPriority(), t.getScheduleTime(), t.getState(), t.getTaskParameters(), t.getInputs());
			archivedTaskDAO.setTaskOutputs(t.getID(), outputs);
			
			// add history
			addHistory(t);
			
			// send notification
			sendNotification(t);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ComputeEngine", "archiveTask", "Error archiving task = "+t.getID(),e);
		}
	}
	
	private void sendNotification(Task t) throws DataAccessException
	{
		Set<User> receivers = new HashSet<User>();
        receivers.add(SysManagerFactory.getUserManager().getUser(t.getOwner()));
        
        StringBuffer sb = null;
        
        // task inputs
        long[] inputs = t.getInputs();
        if(inputs!=null)
        {
        	sb = new StringBuffer();
        	for(long input:inputs)
        	{
        		sb.append(input);
        		sb.append(",");
        	}
        	sb.deleteCharAt(sb.lastIndexOf(","));
        }
        String inputIdsString = "{"+sb.toString()+"}";
        
        // task parameters
        Map<String, String> parameters = t.work.parameters;
        if(parameters!=null)
        {
        	sb = new StringBuffer();
        	for(Entry<String, String>  entry: parameters.entrySet())
        	{
        		sb.append("[");
        		String key = entry.getKey();
        		String value = entry.getValue();
        		sb.append(key);
        		sb.append("=");
        		sb.append(value);
        		sb.append("]");
        		
        		sb.append(",");
        	}
        	sb.deleteCharAt(sb.lastIndexOf(","));
        }
        String parameterString = "{"+sb.toString()+"}";
     
        // task attachments
        List<File> attachments = null;
        
        String taskLogArchive = getTaskLogArchive(t.getID());
        File taskLogLocation = new File(taskLogArchive);
    	
        if(taskLogLocation.isDirectory())
        {
        	File[] files = taskLogLocation.listFiles();
        	if(files!=null && files.length>0)
        	{
        		attachments = Arrays.asList(files);
        	}
        }
        
		SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, attachments, NotificationMessageType.TASK_COMPLETED, t.work.application.name, t.getState().name(), inputIdsString, parameterString);
	}

	/**
	 * Finds a Task that this publisher can handle
	 * @param publisher the publisher under consideration
	 * @return the task if there is any, null otherwise
	 */
	private Task findCompatibleTask(ComputeWorker publisher)
	{
		synchronized(this)
		{
			//the available (to be allocated) list of task are ordered by priority
	    	//if priority is the same, returns the order based on the delay
			PriorityQueue<Task> waitingQ   = new PriorityQueue<Task>(10, new Comparator<Task>(){
				@Override
				public int compare(Task t1, Task t2)
				{
					//a negative integer, zero, or a positive integer as the 
					//first argument is less than, equal to, or greater than the second. 
					int value = t1.getPriority().ordinal() - t2.getPriority().ordinal();
					//same priority, check the delay
					//the task that is waiting the longest is ahead
					if(value == 0) 
					{
						value = (int) (t1.getDelay(TimeUnit.NANOSECONDS) - t2.getDelay(TimeUnit.NANOSECONDS));
					}
					return value;
				}}
	    	);
			
			try
			{
				List<Task> waitingTasks = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO().getTasks(State.WAITING);
				if(waitingTasks!=null)
					waitingQ.addAll(waitingTasks);
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "ComputeEngine", "findCompatibleTask", "error finding waiting tasks", e);
			}
			
			for(Task t : waitingQ) //browse through the task in their priority order
			{
				if(publisher.isApplicationSupported(t.getApplication()))
				{
					return t;
				}
			}
			
			return null;
		}
	}
	
	/**
	 * updates the state information in DB
	 * @param id
	 * @param newState
	 * @throws DataAccessException 
	 */
	private void updateDBState(Task task, State newState) throws DataAccessException
	{
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		Task dbtask = activeTaskDAO.getTask(task.getID());
		if(dbtask == null)
		{
			// insert
			activeTaskDAO.insertTask(task.getID(), task.getApplication().name, task.getApplication().version, 
					task.getProject().getID(), task.isMonitored(), task.getJobAuthCode(), task.work.authCodeInternalId, task.getOwner(), task.getPriority(), task.getScheduleTime(), task.getState(), task.getTaskParameters(), task.getInputs());
		}
		else
		{
			// update
			activeTaskDAO.updateTaskState(task.getID(), newState);
		}
	}
	
	private void removeFromDB(long id) throws DataAccessException
	{
		// remove from active tasks
		ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
		activeTaskDAO.removeTask(id);
	}

	/**
     * check for entry in the waitingQ and move all ready to be allocated entries to the readyQ
	 * @throws DataAccessException 
     */
	private void validateQ() throws DataAccessException 
	{
		synchronized(this)
		{
			Set<Task> availableTasks = new HashSet<Task>();
//			scheduledQ.drainTo(availableTasks);
			List<Task> scheduledQ = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO().getTasks(State.SCHEDULED);
			if(scheduledQ!=null)
				availableTasks.addAll(scheduledQ);
			
			for(Task t : availableTasks)
			{
				t.setAvailable();
				// update in DB
				updateDBState(t, State.WAITING);
			}
		}
	}

	private void checkPermission(String actorLogin, long ...guids) 
	{
		UserPermissionManager pm = SysManagerFactory.getUserPermissionManager();
		for(long guid : guids)
		{
			try 
			{
				if(!pm.canWrite(actorLogin, guid))
					throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
			}
			catch (Exception e) 
			{
				logger.logp(Level.WARNING, "ComputeEngine", "checkPermission", "error varifying permission on "+guid +" by "+actorLogin, e);
				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
			}
		}
	}
	
	private void addMonitoredTask(String userName, Task task)
	{
		synchronized (this)
		{
			logger.logp(Level.FINEST, "ComputeEngine", "addMonitoredTask", "userName=" + userName + "taskId" + task.getID());
			try
			{
				ImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO().setMonitored(userName, task.getID());
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "ComputeEngine", "addMonitoredTask", "error adding as monitored task");
			}
		}
	}
	
	public void clearTaskMonitor(String userName, long taskId) throws DataAccessException
	{
		synchronized (this)
		{
			logger.logp(Level.FINEST, "ComputeEngine", "clearTaskMonitor", "userName=" + userName + "taskId" + taskId);
			List<Task> taskList = getMonitoredTasks(userName);
			if (taskList != null)
			{
				Iterator<Task> taskSetIterator = taskList.iterator();
				while (taskSetIterator.hasNext())
				{
					Task task = taskSetIterator.next();
					if (task.getID() == taskId)
					{
						taskSetIterator.remove();

						// We don't know whether given task is in active task or
						// archived task.
						// Hence call update on both. Only one of them will be
						// successful
						ActiveTaskDAO activeTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO();
						activeTaskDAO.clearIsMonitored(taskId);

						ArchivedTaskDAO archivedTaskDAO = DBImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO();
						archivedTaskDAO.clearIsMonitored(taskId);

						break;
					}
				}
			}
		}
	}
	
	public List<Task> getMonitoredTasks(String owner) throws DataAccessException
	{
		synchronized (this)
		{
			List<Task> taskList = new ArrayList<Task>();

			List<Task> archivedTaskList = ImageSpaceDAOFactory.getDAOFactory().getArchivedTaskDAO().getMonitoredTasks(owner);
			if(archivedTaskList!=null)
				taskList.addAll(archivedTaskList);
			
			List<Task> activeTaskList = ImageSpaceDAOFactory.getDAOFactory().getActiveTaskDAO().getMonitoredTasks(owner);
			if(activeTaskList!=null)
				taskList.addAll(activeTaskList);
			
			Collections.sort(taskList, new Comparator<Task>() {
				public int compare(Task t1, Task t2)
				{
					return (int) (t2.getScheduleTime() - t1.getScheduleTime());
				};
			});
			
			return taskList;
		}
	}
	
	/**
	 * returns the upload url where the task log will be uploaded
	 * @param actorLogin logged in user
	 * @param taskId specified task
	 * @param name name of the attachment
	 * @param clientIP ip of the requesting machine
	 * @return the upload url where the task log will be uploaded
	 * @throws DataAccessException
	 */
	public String getUploadURL(String actorLogin, long taskId, String name, String clientIP) throws DataAccessException 
	{
		return DataExchange.UPLOAD_TASK_LOG.generateURL(actorLogin, clientIP, System.currentTimeMillis(), taskId, name);
	}

	/**
	 * 
	 * @param taskID
	 * @param logFileName 
	 * @param inputStream
	 * @throws Exception 
	 */
	public void addTaskLog(long taskID, String logFileName, InputStream request) throws Exception
	{
		logger.logp(Level.INFO, "ComputeEngine", "addTaskLog", "accepting upload request for task log ");
		BufferedInputStream iStream = null;

		FileOutputStream fStream = null;
		BufferedOutputStream oStream = null;

		File tarBall = null;
		try 
		{
			iStream = new BufferedInputStream(request);
			
			String suffix = "log";
			tarBall = File.createTempFile("tasklog_" + logFileName, suffix, Constants.TEMP_DIR);
			tarBall.deleteOnExit();
			
			logger.logp(Level.INFO, "ComputeEngine", "addTaskLog", "storing tar ball in " + tarBall);

			fStream = new FileOutputStream(tarBall);
			oStream = new BufferedOutputStream(fStream);
			
			long dataLength = Util.transferData(iStream, oStream);
			
			storeLogFile(taskID, logFileName, tarBall);
			logger.logp(Level.INFO, "ComputeEngine", "addTaskLog", "successfully stored log file " + dataLength+ " at " + tarBall);
		} 
		catch (Exception ex) 
		{
			throw ex;
		}
		finally
		{
			Util.closeStream(oStream);
			Util.closeStream(fStream);

			Util.closeStream(iStream);
		}
	}
	
	/**
	 * returns the directory where all the task logs are stored
	 * @param taskID specified task
	 * @return path of directory where all the task logs are stored
	 * @throws DataAccessException 
	 */
	public String getTaskLogArchive(long taskID) throws DataAccessException
	{
		String taskLocation = DBImageSpaceDAOFactory.getDAOFactory().getTaskLogDAO().getTaskLogLocation(taskID);

		if(taskLocation == null)
		{
			File taskRoot = new File(Constants.getStringProperty(Property.TASK_LOG_STORAGE_LOCATION, null), String.valueOf(taskID));
			taskRoot.mkdirs();
			
			taskLocation = String.valueOf(taskID);
		}
		logger.logp(Level.INFO, "ComputeEngine", "getTaskLogArchive", "teskLocation="+taskLocation);
		File location = new File(Constants.getStringProperty(Property.TASK_LOG_STORAGE_LOCATION, null), taskLocation);
		return location.getAbsolutePath();
	}
	
	/**
	 * stores log file to correct location in storate_root
	 * @param taskID of specified task
	 * @param logFileName of specified log file
	 * @param tarBall temp file 
	 * @throws IOException
	 */
	private void storeLogFile(long taskID, String logFileName, File tarBall) throws IOException 
	{
		//location of the task root
		File taskRoot = new File(Constants.getStringProperty(Property.TASK_LOG_STORAGE_LOCATION, null), String.valueOf(taskID));
		taskRoot.mkdirs();
		
		logger.logp(Level.FINE, "ComputeEngine", "storeLogFile", "taskLogRoot="+taskRoot.getAbsolutePath());
		
		if(!taskRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		// physical file copy
		File logFile = new File(taskRoot, logFileName);
		Util.copy(tarBall, logFile);
		tarBall.delete();
				
		// put in DB
		DBImageSpaceDAOFactory.getDAOFactory().getTaskLogDAO().addTaskLog(taskID, logFileName);
	}
}
