/*
 * Response.java
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
 * The worker-manager pings the server with its current state and receives response
 * containing relevant server directives or instructions
 * @author arunabha
 *
 */
public class Response extends Directive {
	/**
	 * The work associated with the directive (if any)
	 */
	private Work work = null ; 
	
	public Response()
	{}

	/**
	 * @return the work
	 */
	public Work getWork() {
		return work;
	}

	/**
	 * @param work the work to set
	 */
	public void setWork(Work work) {
		this.work = work;
	}
}
