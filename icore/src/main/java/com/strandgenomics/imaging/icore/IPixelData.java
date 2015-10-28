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
 
package com.strandgenomics.imaging.icore;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.PixelArray;


/**
 * An pixel-data (raw pixel 2d image) within a record is uniquely identified by the 4 dimensions 
 * - (slices, frames, sites, channels) in addition to its x and y coordinates. 
 * All this 4 numbers starts with 1 and the last value is limited by the size in each corresponding dimensions
 * @author arunabha
 */
public interface IPixelData extends ImageSource, Disposable {
	
	/**
	 * Returns the dimension of this pixel-data
	 * @return the frame number for this image
	 */
	public Dimension getDimension();
	
	/**
	 * x coordinate in microns, default is 0 
	 */
	public double getX();
	
	/**
	 * y coordinate in microns, default is 0 
	 */
	public double getY();
	
	/**
	 * actual z coordinate in microns, default is 0 
	 */
	public double getZ();
	
	/**
	 * time in milliseconds from the beginning of the acquisition phase
	 * @return time in milliseconds
	 */
	public double getElapsedTime();

	/**
	 * Returns the exposure time (of this image) in milli-seconds
	 * @return the exposure time in milli-seconds
	 */
	public double getExposureTime();

	/**
	 * typically the creation time (when the image was captured)
	 * @return  the creation time
	 */
	public Date getTimeStamp();
	
    /**
     * Returns additional machine generated (read-only) meta data associated with this image
     * The values are expected to be either String/Long/Double
     * @return metada as key value pair
     */
    public Map<String, Object> getMetaData();
    
    /**
     * Returns the raw data associated with this image 
     * @return the raw data associated with this image 
     */
    public PixelArray getRawData() throws IOException;
    
    /**
     * Returns the raw data associated with this image for specified ROI
     * @param roi specified region of interest
     * @return the raw data associated with this image for specified ROI
     * @throws IOException
     */
    public PixelArray getRawData(Rectangle roi) throws IOException;
    
	/**
	 * Returns the input stream to read the specified rectangular block of pixel data or a Tile
	 * Note that it would be illegal to specify a rectangular area outside the bounds of this pixel-data
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
	 * @return the InputStream to read this block
	 */
	public ITile getTile(int x, int y, int width, int height);
	    
    /**
     * Returns the intensity distribution statistics for the raw pixel-data
     * @param zStacked z stacked or not
     * @return the intensity distribution statistics for the raw pixel-data
     * @throws IOException
     */
    public Histogram getIntensityDistibution(boolean zStacked) throws IOException;
}
