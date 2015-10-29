/*
 * Directive.java
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
 * Defines the command received by the worker-system
 * 	
 * One of UNKNOWN_REQUEST, WAIT, CONTINUE, REGISTRATION_REQUIRED, PUBLISH_APPLICATIONS,
 * EXECUTE_TASK,TERMINATE_TASK, TERMINATE
 * 
 * @author arunabha
 */
public class Directive {
	
	/**
	 * One of UNKNOWN_REQUEST, WAIT, CONTINUE, REGISTRATION_REQUIRED, PUBLISH_APPLICATIONS,
	 * EXECUTE_TASK,TERMINATE_TASK, TERMINATE
	 */
	private String directive;
	
	public Directive()
	{}

	/**
	 * @return the directive
	 */
	public String getDirective() 
	{
		return directive;
	}

	/**
	 * @param directive the directive to set
	 */
	public void setDirective(String directive) 
	{
		this.directive = directive;
	}
}
