/*
 * WorkResponse.java
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

/**
 * The response to a ping request received by the compute server (worker-system) 
 * @author arunabha
 *
 */
public class Response {
	
	/**
	 * the iManage server command to the compute server (worker-system)
	 */
	public final Directive command; 
	/**
	 * the job to execute, if any, null otherwise
	 */
	public final Work job;
	
	/**
	 * Construct a iManage server response with only a directive
	 * @param command the directive to the compute-server
	 */
	public Response(Directive command)
	{
		this.command = command;
		this.job = null;
	}
	
	/**
	 * Construct a iManage server response with the job and the associated directive
	 * @param command the directive to the compute-server
	 * @param task the task to execute/terminate/etc by the compute server 
	 */
	public Response(Directive command, Work task)
	{
		this.command = command;
		this.job = task;
	}
}
