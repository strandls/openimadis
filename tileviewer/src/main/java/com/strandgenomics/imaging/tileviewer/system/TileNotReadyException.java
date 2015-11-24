package com.strandgenomics.imaging.tileviewer.system;

import java.io.IOException;

/**
 * exception thrown where tile is not available 
 * 
 * @author Anup Kulkarni
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
