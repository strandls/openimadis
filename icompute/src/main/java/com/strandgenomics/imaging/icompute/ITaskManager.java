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

import com.strandgenomics.imaging.iclient.impl.ws.worker.Work;
import com.strandgenomics.imaging.iclient.impl.ws.worker.WorkStatus;
import com.strandgenomics.imaging.icore.app.WorkerState;

/**
 * ITaskManager enlists the methods to be implemented by TaskManager. The job of
 * task manager is to schedule a task, get job status, and terminate the job
 * 
 * @author Anup Kulkarni
 */
public interface ITaskManager {
	/**
	 * returns status of the specified task
	 * @param work the specified task handle
	 * @return status of the specified task
	 */
	public WorkStatus getStatus(Work work);

	/**
	 * terminates specified task
	 * @param work the specified task
	 * @throws Exception 
	 */
	public void terminateTask(Work work) throws Exception;
	
	/**
	 * executes the specified task
	 * @param work the specified task
	 */
	public void execute(ComputeApplication computeApplication, Work work) throws Exception;
	
	/**
	 * returns state of task executor
	 * @return
	 */
	public WorkerState getWorkerState();
	
	/**
	 * returns the status of all active jobs
	 * @return the status of all active jobs
	 */
	public WorkStatus[] getActiveJobsWorkStatus();

	/**
	 * request to terminate worker
	 */
	public void terminate();
}
