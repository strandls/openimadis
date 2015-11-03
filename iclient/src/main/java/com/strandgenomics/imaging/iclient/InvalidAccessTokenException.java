package com.strandgenomics.imaging.iclient;

public class InvalidAccessTokenException extends ImageSpaceException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2431469396671496733L;

	/**
	 * Constructs an QuotaExceededException with null as its error detail message.
	 */
	public InvalidAccessTokenException()
	{
		super();
	}
	
	/**
	 * Constructs an QuotaExceededException with the specified detail message.
	 * @param message the message
	 */
	public InvalidAccessTokenException(String message)
	{
		super(message);
	}
	
	/**
	 * Constructs an QuotaExceededException with the specified detail message and cause.
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidAccessTokenException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
