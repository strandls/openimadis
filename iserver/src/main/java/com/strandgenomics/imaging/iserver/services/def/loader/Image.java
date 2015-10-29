/*
 * ImageObject.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.loader;

public class Image {
	
	/** 
	 * the image index
	 */
	private ImageIndex index;
	/**
	 * x coordinate in micron, default is 0 
	 */
	private double x;
	/**
	 * y coordinate in micron, default is 0 
	 */
	private double y;
	/**
	 * actual z coordinate in micron, default is 0 
	 */
	private double z;
	/**
	 * time in milliseconds from the beginning of the acquisition phase
	 */
	private double elapsedTime;
	/**
	 * the exposure time (of this image) in milli-seconds
	 */
	private double exposureTime;
	/**
	 * the creation time (when the image was captured)
	 */
	protected long timeStamp;
	
	public Image(){}

	/**
	 * Returns the frame number for a pixel-data with a record, default is 0
	 * @return the frame number for a pixel-data with a record
	 */
	public ImageIndex getIndex()
	{
		return index;
	}
	
	public void setIndex(ImageIndex value)
	{
		index = value;
	}
	

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the exposureTime
	 */
	public double getExposureTime() {
		return exposureTime;
	}

	/**
	 * @param exposureTime the exposureTime to set
	 */
	public void setExposureTime(double exposureTime) {
		this.exposureTime = exposureTime;
	}

	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
