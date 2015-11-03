package com.strandgenomics.imaging.iclient;

public class DuplicateRequestException extends ImageSpaceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8754813557553549761L;
	
	/**
	 * Constructs an IllegalAccessException with null as its error detail message.
	 */
	public DuplicateRequestException()
	{
		super();
	}
	
	/**
	 * Constructs an DuplicateRequestException with the specified detail message.
	 * @param message the message
	 */
	public DuplicateRequestException(String message)
	{
		super(message);
	}
	
	/**
	 * Constructs an DuplicateRequestException with the specified detail message and cause.
	 * @param message the message
	 * @param cause the cause
	 */
	public DuplicateRequestException(String message, Throwable cause)
	{
		super(message, cause);
	}


}
