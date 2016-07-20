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
