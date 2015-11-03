/*
 * IllegalAccessException.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.iclient;

/**
 * This exception is thrown when one tries to access an API without sufficient access rights
 * @author arunabha
 *
 */
public class InsufficientPermissionException extends ImageSpaceException {

	private static final long serialVersionUID = -2839693965815122613L;

	/**
	 * Constructs an IllegalAccessException with null as its error detail message.
	 */
	public InsufficientPermissionException()
	{
		super();
	}
	
	/**
	 * Constructs an IllegalAccessException with the specified detail message.
	 * @param message the message
	 */
	public InsufficientPermissionException(String message)
	{
		super(message);
	}
	
	/**
	 * Constructs an IllegalAccessException with the specified detail message and cause.
	 * @param message the message
	 * @param cause the cause
	 */
	public InsufficientPermissionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
