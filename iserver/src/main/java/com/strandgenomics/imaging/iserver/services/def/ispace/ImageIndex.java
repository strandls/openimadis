/*
 * ImageCoordinate.java
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
 * An pixel-data (raw bitmap image at a XY plane) within a record is uniquely identified by the 4 dimensions 
 * - (slices, frames, sites and channels). 
 * This are in addition to its x and y coordinates which is fixed for all pixel-data (images) within a record. 
 * All this 4 numbers starts with 0 and the last value is limited by the size in each corresponding dimensions
 * 
 * ImageCoordinate class represents the four dimension of a pixel-data that is used to identify it within a given record 
 * (excluding the x-y dimension)
 * 
 * @author arunabha
 */
public class ImageIndex extends VOIndex {
	/**
	 * the channel number
	 */
	protected int channelNo;

	public ImageIndex()
	{}
	
	/**
	 * Returns the channel number for a pixel-data with a record, default is 0
	 * @return the channel number for a pixel-data with a record
	 */
	public int getChannel()
	{
		return channelNo;
	}
	
	public void setChannel(int value)
	{
		channelNo = value;
	}
}
