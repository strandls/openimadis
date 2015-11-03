/*
 * VOCoordinate.java
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
package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * Visual annotations (objects/shapes) are associated with image overlays linked to images specified by 
 * the image's frame number, slice number and the site number.
 * 
 * Note that, a pixel-data (raw pixel intensities) within a record is uniquely identified by the 4 dimensions 
 * - (slices, frames, sites and channels). 
 * 
 * This are in addition to its x and y coordinates which is fixed for all pixel-data (images) within a record. 
 * All this 4 numbers starts with 0 and the last value is limited by the size in each corresponding dimensions
 * 
 * Coordinate represents the three dimension of slices, frames, and sites. 
 * 
 * @author arunabha
 */
public class VOIndex {
	 
	/** 
	 * the frame number
	 */
	protected int frameNo;
	/**
	 * the slice (Z) number
	 */
	protected int sliceNo;
	/**
	 * the site number
	 */
	protected int siteNo;

	public VOIndex()
	{}
	
	/**
	 * Returns the frame number for a pixel-data with a record, default is 0
	 * @return the frame number for a pixel-data with a record
	 */
	public int getFrame()
	{
		return frameNo;
	}
	
	public void setFrame(int value)
	{
		frameNo = value;
	}
	
	/**
	 * Returns the slice (z-positions) number for a pixel-data with a record, default is 0
	 * @return the slice number for a pixel-data with a record
	 */
	public int getSlice()
	{
		return sliceNo;
	}
	
	public void setSlice(int value)
	{
		sliceNo = value;
	}

	/**
	 * Returns the site number for a pixel-data with a record, default is 0
	 * @return the site number for a pixel-data with a record
	 */
	public int getSite()
	{
		return siteNo;
	}
	
	public void setSite(int value)
	{
		siteNo = value;
	}
}
