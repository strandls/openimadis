/*
 * WorkRequest.java
 *
 * AVADIS Image Management System
 * Core Compute Engine Components
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

package com.strandgenomics.imaging.icore.app;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The compute-server (worker-system) updates its status with the iManage server
 * periodically with a request specified by this class
 * @author arunabha
 *
 */
public class Request {
	
	/**
	 * state of the compute server (workers)
	 */
	public final WorkerState state;
	/**
	 * status of the list of jobs being executed
	 */
	private final Set<JobReport> executingJobs;
	
	/**
	 * 
	 * @param state state of the compute worker
	 * @param jobs status of the list of the jobs
	 */
	public Request(WorkerState state, Collection<JobReport> jobs)
	{
		this.state = state;
		executingJobs = new HashSet<JobReport>();
		executingJobs.addAll(jobs);
	}
	
	/**
	 * returns the jobs that are being reported by this request
	 * @return the jobs that are being reported by this request
	 */
	public Collection<JobReport> getJobs()
	{
		return executingJobs;
	}
}