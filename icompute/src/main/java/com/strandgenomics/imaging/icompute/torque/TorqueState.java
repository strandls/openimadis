package com.strandgenomics.imaging.icompute.torque;

import com.strandgenomics.imaging.icore.app.JobState;

/**
 * enlists the states of the job in Torque/PBS
 * 
 * @author Anup Kulkarni
 */
public enum TorqueState {
	
	/**
	 * Queued
	 */
	Q,
	/**
	 * Running
	 */
	R,
	/**
	 * completed successfully
	 */
	C,
	/**
	 * exiting
	 */
	E,
	/**
	 * suspended by admin
	 */
	S;
	
	/**
	 * returns the job state given torque state
	 * @param torqueState
	 * @param exitStatus 
	 * @return
	 */
	public static JobState getJobState(TorqueState torqueState, int exitStatus)
	{
		JobState jobState;
		switch (torqueState)
		{
			case R:
				// job is still Running
				jobState = JobState.EXECUTING;
				break;

			case C:
				// job is completed
				if(exitStatus == 0)
				{
					jobState = JobState.SUCCESSFUL;
				}
				else if(exitStatus > 255)
				{
					jobState = JobState.TERMINATED;
				}
				else
				{
					jobState = JobState.FAILURE;
				}
				break;
				
			default:
				jobState = JobState.EXECUTING;
				break;
		}
		
		return jobState;
	}
}
