package com.strandgenomics.imaging.icore.bioformats;
/**
 * Exceptin occured while invoking image reader
 * 
 * @author Anup Kulkarni
 */
public class ImageReaderException extends RuntimeException {

	private static final long serialVersionUID = -5614019846168541232L;

	public ImageReaderException(String message) {
		super(message);
	}
}
