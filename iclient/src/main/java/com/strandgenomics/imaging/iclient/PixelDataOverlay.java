/*
 * PixelDataOverlay.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.iclient;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.strandgenomics.imaging.icore.IPixelDataOverlay;

/**
 * Represents the overlay state of many pixel-data within a record
 * @author arunabha
 */
public class PixelDataOverlay extends ImageSpaceObject implements IPixelDataOverlay {
	
	private static final long serialVersionUID = -5825704111427039161L;
	/**
	 * the parent record
	 */
	protected Record parentRecord;
	/**
	 *  The slice number (Z) of the image to overlay with channels
	 */
	public final int sliceNo;
	/**
	 * The frame number of the image to overlay with channels
	 */
	public final int frameNo;
	/**
	 *  The site number of the image to overlay with channels
	 */
	public final int siteNo; 
	/**
	 *  The list of channel numbers (dimensions) that are combined together to create this PixelData overlay  
	 */
	protected final int[] channelNos;
	
	//package projected - to be used locally
	PixelDataOverlay(Record parent, int sliceNo, int frameNo, int siteNo, int[] channelNos)
	{
		this.parentRecord = parent;
		this.sliceNo = sliceNo;
		this.frameNo = frameNo;
		this.siteNo  = siteNo;
		this.channelNos = channelNos;
	}


	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	}


	@Override
    public int getImageWidth()
    {
    	return parentRecord.getImageWidth();
    }
    
	@Override
    public int getImageHeight()
    {
    	return parentRecord.getImageHeight();
    }


	@Override
	public int[] getOverlaidChannels()
	{
		return channelNos;
	}


	@Override
	public int getSliceNumber()
	{
		return sliceNo;
	}


	@Override
	public int getFrameNumber() 
	{
		return frameNo;
	}


	@Override
	public int getSiteNumber() 
	{
		return siteNo;
	}


	@Override
	public BufferedImage getImage(boolean zStacked, boolean mosaic, boolean useChannelColor, Rectangle roi) throws IOException
	{
		if(roi == null)
			roi = new Rectangle(0, 0, parentRecord.getImageWidth(), parentRecord.getImageHeight());
		return getImageSpace().getOverlayedImage(parentRecord.getGUID(), sliceNo, frameNo, siteNo, channelNos, zStacked, mosaic, useChannelColor, roi.x, roi.y, roi.width, roi.height);
	}
}
