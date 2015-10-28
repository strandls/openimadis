package com.strandgenomics.imaging.iengine.zoom;

import java.io.IOException;

/**
 * exception thrown where tile is not available 
 * 
 * @author Navneet
 */
public class TileNotReadyException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8959050150405806752L;
	
	public TileNotReadyException(String message) {
		super(message);
	}

	public TileNotReadyException(Throwable rootCause) 
	{
		super(rootCause);
	}

	public TileNotReadyException(String message, Throwable rootCause) 
	{
		super(message, rootCause);
	}

}
