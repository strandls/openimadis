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

package com.strandgenomics.imaging.icompute;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;

import com.strandgenomics.imaging.iclient.impl.ws.worker.Work;
import com.strandgenomics.imaging.icore.app.JobState;

public class ComputeTask implements Runnable {

	private Work work;
	private JobState jobState;
	private ComputeApplication computeApplication;
	private Logger logger;
	private int exitValue;
	private Executor executor;
	private ExecuteWatchdog watchdog;

	public ComputeTask(ComputeApplication computeApplication, Work work) {
		this.computeApplication = computeApplication;
		this.work = work;
		jobState=JobState.EXECUTING;
		logger = Logger.getLogger("com.strandgenomics.imaging.icompute");
	}
	
	private String writeArguments(String dirPath, long taskId, HashMap<String, List<Object>>arguments) throws FileNotFoundException
	{
		File file = new File(dirPath, taskId+".in");
		String filepath = file.getAbsolutePath();
		
		TaskArgumentsUtil.writeArguments(filepath, arguments);
		
		return filepath;
	}
	
	@Override
	public void run() {
		
		CommandLine commandLine= new CommandLine(computeApplication.getAppLauncherFile());
		
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
		
		Map<String,String> environment= new HashMap<String, String>();
		for (Entry<String, String> env : System.getenv().entrySet()) {
			environment.put(env.getKey(),env.getValue());
		}
		environment.put("InputFile", inputFilePath);
		
		try {
			
			watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
			executor = new DefaultExecutor();
			executor.setWorkingDirectory(computeApplication.getAppLauncherFile().getParentFile());
			
			DefaultExecuteResultHandler resultHandler=new DefaultExecuteResultHandler();
			executor.execute(commandLine, environment, resultHandler);
			
			resultHandler.waitFor();
			exitValue=resultHandler.getExitValue();
			if(jobState== JobState.EXECUTING){
				if(executor.isFailure(exitValue)){
					jobState=JobState.FAILURE;
				}
				else{
					jobState=JobState.SUCCESSFUL;
				}
			}
			
		} catch (IOException e) {
			logger.logp(Level.WARNING, "ComputeTask", "run",
					e.getMessage());
		} catch (InterruptedException e) {
			logger.logp(Level.WARNING, "ComputeTask", "run",
					e.getMessage());
		}
	}

	public void terminate() {
		watchdog.destroyProcess();
		jobState=JobState.TERMINATED;
	}
	
	public Work getWork() {
		return work;
	}
	
	public JobState getJobState() {
		return jobState;
	}

}
