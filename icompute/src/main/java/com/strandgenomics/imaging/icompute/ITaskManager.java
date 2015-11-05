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
