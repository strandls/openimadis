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

package com.strandgenomics.imaging.icompute.torque;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strandgenomics.imaging.iclient.impl.ws.worker.Work;
import com.strandgenomics.imaging.iclient.impl.ws.worker.WorkStatus;
import com.strandgenomics.imaging.icompute.ComputeApplication;
import com.strandgenomics.imaging.icompute.ITaskManager;
import com.strandgenomics.imaging.icompute.TaskArgumentsUtil;
import com.strandgenomics.imaging.icore.app.JobState;
import com.strandgenomics.imaging.icore.app.WorkerState;
/**
 * TaskManager for Torque/PBS
 * 
 * @author Anup Kulkarni
 */
public class TorqueTaskManager implements ITaskManager{
	/**
	 * map for task id to torque id
	 */
	private Map<String, Long> torqueIDTaskIDMap;
	
	/**
	 * reverse map for task id to torque id
	 */
	private Map<Long, String> taskIDTorqueIDMap;
	
	private WorkerState state;
	
	public TorqueTaskManager()
	{
		torqueIDTaskIDMap = new HashMap<String, Long>();
		taskIDTorqueIDMap = new HashMap<Long, String>();
		
		state = WorkerState.FREE;
	}
	
	@Override
	public WorkStatus getStatus(Work work)
	{
		JobState jobState = JobState.TERMINATED;
		
		String torqueId = taskIDTorqueIDMap.get(work.getTaskID());
		try
		{
			String torqueStatus = TorqueHandler.getJobStatus(torqueId);
			TorqueState torqueState = TorqueState.valueOf(torqueStatus);
			
			jobState = TorqueState.getJobState(torqueState, TorqueHandler.getExitStatus(torqueId));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		WorkStatus status = new WorkStatus(work.getTaskID(), jobState.ordinal());
		return status;
	}

	@Override
	public void terminateTask(Work work) throws Exception
	{
		// get torque id
		String jobId = taskIDTorqueIDMap.get(work.getTaskID());
		
		// delete from torque
		TorqueHandler.terminateJob(jobId);
	}
	
	private String writeArguments(String dirPath, long taskId, HashMap<String, List<Object>>arguments) throws FileNotFoundException
	{
		File file = new File(dirPath, taskId+".in");
		String filepath = file.getAbsolutePath();
		
		TaskArgumentsUtil.writeArguments(filepath, arguments);
		
		return filepath;
	}
	
	@Override
	public void execute(ComputeApplication computeApplication, Work work) throws Exception
	{
		// get job arguments
		HashMap<String, List<Object>> arguments = TaskArgumentsUtil.getWorkArguments(work);
		String inputFilePath = "";
		try
		{
			inputFilePath = writeArguments(computeApplication.getAppLauncherFile().getParent(), work.getTaskID(), arguments);
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("InputFile", inputFilePath);
		String id = TorqueHandler.submitJob(work.getAppName()+work.getTaskID(), computeApplication.getAppLauncherFile().getAbsolutePath(), params);

		// update the cache
		torqueIDTaskIDMap.put(id, work.getTaskID());
		taskIDTorqueIDMap.put(work.getTaskID(), id);
	}

	@Override
	public WorkerState getWorkerState()
	{
		return state;
	}
	
	@Override
	public void terminate()
	{ }
	
	@Override
	public WorkStatus[] getActiveJobsWorkStatus()
	{
		List<WorkStatus> report= new ArrayList<WorkStatus>();
		state = WorkerState.FREE;
		try
		{
			for(String torqueId:torqueIDTaskIDMap.keySet())
			{
				String jobStatus = TorqueHandler.getJobStatus(torqueId);
				TorqueState torqueState = TorqueState.valueOf(jobStatus);
				JobState jobState = TorqueState.getJobState(torqueState, TorqueHandler.getExitStatus(torqueId));
				WorkStatus status = new WorkStatus(torqueIDTaskIDMap.get(torqueId), jobState.ordinal());
				
				report.add(status);

				if( jobState !=JobState.EXECUTING)
				{
					Long taskId = torqueIDTaskIDMap.remove(torqueId);
					taskIDTorqueIDMap.remove(taskId);
				}
				
				state = WorkerState.ENGAGED;
			}
		}
		catch (NegativeArraySizeException e)
		{ }
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return report.toArray(new WorkStatus[0]);
	}
}
