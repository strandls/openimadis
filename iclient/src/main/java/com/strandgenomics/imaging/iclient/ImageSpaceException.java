/*
 * ImageSpaceException.java
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

public class ImageSpaceException extends RuntimeException {
	
	//generated serial version id
	private static final long serialVersionUID = -1333973808730819091L;

	/**
	 * Constructs an ImageSpaceException with null as its error detail message.
	 */
	public ImageSpaceException()
	{
		super();
	}
	
	/**
	 * Constructs an ImageSpaceException with the specified detail message.
	 * @param message the message
	 */
	public ImageSpaceException(String message)
	{
		super(message);
	}
	
	/**
	 * Constructs an IOException with the specified detail message and cause.
	 * @param message the message
	 * @param cause the cause
	 */
	public ImageSpaceException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
