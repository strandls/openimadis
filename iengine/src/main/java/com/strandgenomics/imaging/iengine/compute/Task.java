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

package com.strandgenomics.imaging.iengine.compute;

import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.Directive;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.Response;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.app.Work;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

public class Task implements Delayed, Storable {
	/**
	 * the work to be done by a compute-server (worker system)
	 */
	public final Work work;
	/**
	 * the priority of this work
	 */
	protected Priority priority;
	/**
	 * the time after which this task is available for execution
	 */
	protected long scheduleTime;
	/**
	 * state of this task
	 */
	protected State taskState;
	/**
	 * the user who owns this task
	 */
	protected String owner;
	/**
	 * Project under which task is submited.
	 * */
	protected Project project;
	/**
	 * 
	 */
	protected boolean isMonitored=false;
	/**
	 * progress of the running task
	 */
	protected Integer progress = 0;
	
	
	public Task(long workid, int projectId, String owner, Application app, String authCode, long authCodeId, Priority priority, boolean isMonitored, long scheduleTime, State taskState, Map<String, String> parameters, long[] guids)
	{
		this.owner = owner;
		this.project=SysManagerFactory.getProjectManager().getProject(projectId);
		work = new Work(workid, authCode, authCodeId, app, parameters, guids);
		this.priority = priority == null ? Priority.MEDIUM : priority;
		this.scheduleTime = scheduleTime;
		this.taskState = taskState;
		this.isMonitored = isMonitored;
	}
	
	/**
	 * Creates a Task (work) to be executed by a compute-server (or a worker system)
	 * @param app the application to execute
	 * @param authCode the authorization code granted by the user
	 * @param priority the priority of the task
	 * @param parameters the parameters needed by the application
	 * @param guids the input record GUID
	 */
	public Task(String owner, Application app, String authCode, long authCodeId, Priority priority, String projectName, Map<String, String> parameters, long ... guids)
	{
		this.owner = owner;
		this.project=SysManagerFactory.getProjectManager().getProject(projectName);
		work = new Work(authCode, authCodeId, app, parameters, guids);
		this.priority = priority == null ? Priority.MEDIUM : priority;
		this.scheduleTime = System.currentTimeMillis();
		this.taskState = State.SCHEDULED;
	}
	
	/**
	 * Creates a Task (work) to be executed by a compute-server (or a worker system)
	 * @param app the application to execute
	 * @param authCode the authorization code granted by the user
	 * @param priority the priority of the task
	 * @param waitTime the wait time (in milliseconds) after which this task is scheduled for execution 
	 * @param parameters the parameters needed by the application
	 * @param guids the input record GUID
	 */
	public Task(String owner, Application app, String authCode, long authCodeId, Priority priority, String projectName, boolean isMonitored, long waitTime, Map<String, String> parameters, long ... guids)
	{
		this.owner = owner;
		work = new Work(authCode, authCodeId, app, parameters, guids);
		this.project=SysManagerFactory.getProjectManager().getProject(projectName);
		this.priority = priority == null ? Priority.MEDIUM : priority;
		this.scheduleTime = waitTime <= 0 ? System.currentTimeMillis() : System.currentTimeMillis() + waitTime;
		this.taskState = State.SCHEDULED;
		this.isMonitored=isMonitored;
	}
	
	public Map<String, String> getTaskParameters()
	{
		return work.parameters;
	}

	/**
	 * Returns the owner of this task
	 * @return the owner of this task
	 */
	public String getOwner()
	{
		return owner;
	}
	
	/**
	 * Returns the underlying application 
	 * @return the underlying application 
	 */
	public Application getApplication()
	{
		return work.application;
	}
	
	/**
	 * Re-schedule this task
	 * @param waitTime the wait time (in milliseconds) after which this task is scheduled for execution 
	 */
	public void reSchedule(long waitTime)
	{
		if(taskState == State.WAITING || taskState == State.SCHEDULED)
		{
			this.scheduleTime = System.currentTimeMillis() + waitTime;
			this.taskState = State.SCHEDULED;
		}
	}
	
	/**
	 * The work associated with this Task
	 * @return
	 */
	public Response allocateWork(ComputeWorker worker)
	{
		if(taskState == State.WAITING)
		{
			this.taskState = State.ALLOCATED;
			return new Response(Directive.EXECUTE_TASK, work);
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public boolean isAllocated()
	{
		return taskState == State.ALLOCATED;
	}
	
	public void setAvailable()
	{
		if(taskState == State.SCHEDULED)
		{
			taskState =  State.WAITING;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public void setPause()
	{
		if(taskState == State.SCHEDULED || taskState == State.WAITING)
		{
			taskState = State.PAUSED;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public void setResume()
	{
		if(taskState == State.PAUSED)
		{
			taskState = State.SCHEDULED;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public boolean isRunning()
	{
		if(taskState == State.ALLOCATED || taskState == State.EXECUTING)
			return true;
		return false;
	}

	public void setExecuting()
	{
		if(taskState == State.ALLOCATED || taskState == State.EXECUTING)
		{
			taskState = State.EXECUTING;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public void setDeleted()
	{
		if(taskState == State.SCHEDULED || taskState == State.WAITING)
		{
			taskState = State.DELETED;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public boolean needsTermination()
	{
		return taskState == State.TERMINATING;
	}
	
	
	public void setTerminate()
	{
		if(taskState == State.ALLOCATED || taskState == State.EXECUTING)
		{
			taskState = State.TERMINATING;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public void setTerminated()
	{
		if(taskState == State.TERMINATING)
		{
			taskState = State.TERMINATED;
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	public void setSuccessFul()
	{
		if(taskState == State.ALLOCATED || taskState == State.EXECUTING)
			taskState = State.SUCCESS;
		else
			throw new IllegalStateException();
	}
	
	public void setFailure()
	{
		if(taskState == State.ALLOCATED || taskState == State.EXECUTING)
			taskState = State.ERROR;
		else
			throw new IllegalStateException();
	}
	
	/**
	 * Returns the priority of this task
	 * @return the priority of this task
	 */
	public Priority getPriority()
	{
		return priority;
	}
	
	/**
	 * Returns the ID of this Task
	 * @return the ID of this Task
	 */
	public final long getID()
	{
		return work.ID;
	}
	
	/**
	 * Returns the current state of this Task
	 * @return the current state of this Task
	 */
	public State getState()
	{
		return taskState;
	}
	
	/**
	 * returns the progress of the running task
	 * @return
	 */
	public Integer getProgress()
	{
		return this.progress;
	}
	
	/**
	 * sets progress of running task
	 * @param progress
	 * @return
	 */
	public void setProgress(int progress)
	{
		if(progress<0 || progress>100) throw new IllegalArgumentException("progress can be only [0-100]");
		
		this.progress = progress;
	}

	@Override
	public int compareTo(Delayed o) 
	{
		//provides an ordering consistent with <tt>getDelay</tt> method.
		//a negative integer, zero, or a positive integer as this object
	    //is less than, equal to, or greater than the specified object.
		
		long thisDelay = getDelay(TimeUnit.MILLISECONDS);
		long thatDelay = o.getDelay(TimeUnit.MILLISECONDS);
		
		if(thisDelay == thatDelay)
			return 0;
		else if(thisDelay < thatDelay)
			return -1;
		else
			return 1;
	}

	@Override
	public long getDelay(TimeUnit unit) 
	{
		if(taskState == State.PAUSED) //if paused infinite delay
			return unit.convert(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		
    	//Task delay expiration occurs when a Task's getDelay(TimeUnit.NANOSECONDS) 
    	//method returns a value less than or equal to zero
		
		// Returns the remaining delay associated with this task
        //delay is used to check if the task is scheduled for a future time or the scheduled time has already passed
        long delayTime = scheduleTime - System.currentTimeMillis();
        return unit.convert(delayTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	public String getJobAuthCode()
	{
		return work.getAuthCode();
	}
	
	public long[] getInputs()
	{
		return work.inputGUIDs;
	}
	
	public long getScheduleTime()
	{
		return scheduleTime;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
	public boolean isMonitored() {
		return isMonitored;
	}
}
