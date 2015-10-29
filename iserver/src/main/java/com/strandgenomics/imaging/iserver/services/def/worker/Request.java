/*
 * Request.java
 *
 * AVADIS Image Management System
 * Web-service definition
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
