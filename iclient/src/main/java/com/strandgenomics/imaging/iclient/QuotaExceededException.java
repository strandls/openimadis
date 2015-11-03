package com.strandgenomics.imaging.iclient;

public class QuotaExceededException extends ImageSpaceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6347807972462861639L;

	/**
	 * Constructs an QuotaExceededException with null as its error detail message.
	 */
	public QuotaExceededException()
	{
		super();
	}
	
	/**
	 * Constructs an QuotaExceededException with the specified detail message.
	 * @param message the message
	 */
	public QuotaExceededException(String message)
	{
		super(message);
	}
	
	/**
	 * Constructs an QuotaExceededException with the specified detail message and cause.
	 * @param message the message
	 * @param cause the cause
	 */
	public QuotaExceededException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
