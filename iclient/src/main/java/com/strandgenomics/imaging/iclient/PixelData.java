/*
 * PixelData.java
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
import java.net.URL;
import java.util.Date;
import java.util.Map;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.ITile;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.PixelArray;

/**
 * An pixel-data (raw bitmap image) within a record is uniquely identified by the 4 dimensions 
 * - (slices, frames, sites, channels) in addition to its x and y coordinates. 
 * All this 4 numbers starts with 1 and the last value is limited by the size in each corresponding dimensions
 * @author arunabha
 */
public class PixelData extends ImageSpaceObject implements IPixelData {
	
	private static final long serialVersionUID = -8381694499986482730L;
	/**
	 * the parent record
	 */
	protected Record parentRecord;
	/**
	 * record dimensions that uniquely identify this pixel-data
	 */
	protected Dimension imageCoordinate;
	/**
	 * x coordinate in micron, default is 0 
	 */
	protected double x;
	/**
	 * y coordinate in micron, default is 0 
	 */
	protected double y;
	/**
	 * actual z coordinate in micron, default is 0 
	 */
	protected double z;
	/**
	 * time in milliseconds from the beginning of the acquisition phase
	 */
	protected double elapsedTime;
	/**
	 * the exposure time (of this image) in milli-seconds
	 */
	protected double exposureTime;
	/**
	 * the creation time (when the image was captured)
	 */
	protected Date timeStamp;
	
	/**
	 * Construct a PixelData Object instance from the information made available from the
	 * underlying system
	 * @param parentRecord the parent record
	 * @param imageID record dimensions that uniquely identify this pixel-data
	 * @param x X coordinate in micron, default is 0 
	 * @param y Y coordinate in micron, default is 0 
	 * @param z Z coordinate in micron, default is 0 
	 * @param elapsedTime time in milliseconds from the beginning of the acquisition phase
	 * @param exposureTime the exposure time (of this image) in milli-seconds
	 * @param timeStamp the creation time (when the image was captured)
	 * @param gridRow the grid row coordinate
	 * @param gridColumn the grid column coordinate
	 * @param blackContrast Contrast level for black
	 * @param whiteContrast Contrast level for white
	 * @param gammaContrast Contrast level for gamma
	 * @param baseRGB the RGB base color of the image - default: Gray (128, 128, 128) 
	 */
	//package protected - to be called locally within the package
	PixelData(Record parent, Dimension imageID,
			double x, double y, double z,
			double elapsedTime, double exposureTime, Date timeStamp)
	{
		this.parentRecord = parent;
		this.imageCoordinate = imageID;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.elapsedTime = elapsedTime;
		this.exposureTime = exposureTime;
		this.timeStamp = timeStamp;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Returns the http url that can be used to download this pixel-data 
     * @return the http url that can be used to download this pixel-data 
     */
    public URL getURL()
    {
    	return null; //TBD
    }
    
    /**
     * Returns the http url that can be used to download the png image
     * @return the http url that can be used to download the png image
     */
    public URL getImageURL()
    {
    	return null; //TBD
    }

	@Override
	public Dimension getDimension() 
	{
		return imageCoordinate;
	}

	@Override
	public double getX() 
	{
		return x;
	}

	@Override
	public double getY() 
	{
		return y;
	}

	@Override
	public double getZ() 
	{
		return z;
	}

	@Override
	public double getElapsedTime() 
	{
		return elapsedTime;
	}

	@Override
	public double getExposureTime() 
	{
		return exposureTime;
	}

	@Override
	public Date getTimeStamp() 
	{
		return timeStamp;
	}

	@Override
	public Map<String, Object> getMetaData() 
	{
		//makes a system call to get it done
		return getImageSpace().getImageMetaData(parentRecord.getGUID(), this);
	}
	
	@Override
	public ITile getTile(int x, int y, int width, int height) 
	{
		int imageWidth = parentRecord.getImageWidth();
		int imageHeight = parentRecord.getImageHeight();
		
		if(x < 0 || x >= imageWidth)
			throw new IllegalArgumentException("illegal x value "+x);
		
		if(y < 0 || y >= imageHeight)
			throw new IllegalArgumentException("illegal y value "+y);
		
		if(width > imageWidth)
			throw new IllegalArgumentException("illegal width value "+width);
		
		if(height > imageHeight)
			throw new IllegalArgumentException("illegal height value "+height);
		
		if((x + width) > imageWidth)
			throw new IllegalArgumentException("illegal tile "+width);
		
		if((y + height) > imageHeight)
			throw new IllegalArgumentException("illegal tile "+height);
		
		return new Tile(parentRecord, imageCoordinate, x, y,  width, height);
	}

	@Override
	public PixelArray getRawData() throws IOException
	{
		//makes a system call to get it done
		return getImageSpace().getRawPixelData(parentRecord.getGUID(), imageCoordinate);
	}
	
	@Override
	public PixelArray getRawData(Rectangle roi) throws IOException
	{
		if(roi == null)
			return getRawData();
		
		//makes a system call to get it done
		Tile tile = new Tile(parentRecord, imageCoordinate, roi.x, roi.y, roi.width, roi.height);
		return getImageSpace().getRawDataForTile(parentRecord.getGUID(), tile);
	}
	
	@Override
    public Histogram getIntensityDistibution(boolean zStacked) throws IOException
    {
		//makes a system call to get it done
		return getImageSpace().getIntensityDistibutionForImage(parentRecord.getGUID(), imageCoordinate);
    }
	
	@Override
	public BufferedImage getImage(boolean useChannelColor) throws IOException
	{
		//makes a system call to get it done
		return getImageSpace().getPixelDataImage(parentRecord.getGUID(), imageCoordinate, useChannelColor);
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
}
