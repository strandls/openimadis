package com.strandgenomics.imaging.tileviewer.system;

import java.io.IOException;


/**
 * exception thrown when request for fetching tile is cancelled
 * @author navneet
 *
 */
public class TileRequestCancelledException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8959050150405806752L;
	
	public TileRequestCancelledException(String message) {
		super(message);
	}

	public TileRequestCancelledException(Throwable rootCause) 
	{
		super(rootCause);
	}

	public TileRequestCancelledException(String message, Throwable rootCause) 
	{
		super(message, rootCause);
	}

}