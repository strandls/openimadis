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

package com.strandgenomics.imaging.iserver.services.def.worker;

/**
 * Ping Request from a worker-manager
 * @author arunabha
 *
 */
public class Request {
	
    /** 
     * worker permit or license, do registration to get this
     * one of FREE, ENGAGED and BUSY
     */
    private String workerState;
    /** 
     * status if the work being done
     */
    private WorkStatus[] activeJobs = null;
    
    /**
     * Creates a request instance
     */
    public Request()
    {}

    
	/**
	 * @return the workerState
	 */
	public String getWorkerState() 
	{
		return workerState;
	}

	/**
	 * @param workerState the workerState to set
	 */
	public void setWorkerState(String workerState) 
	{
		this.workerState = workerState;
	}


	/**
	 * @return the activeJobs
	 */
	public WorkStatus[] getActiveJobs() 
	{
		return activeJobs;
	}

	/**
	 * @param activeJobs the activeJobs to set
	 */
	public void setActiveJobs(WorkStatus[] activeJobs) 
	{
		this.activeJobs = activeJobs;
	}
}
