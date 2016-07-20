/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.strandgenomics.imaging.iclient;

import java.util.Date;

import com.strandgenomics.imaging.icore.Dimension;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class PixelMetaData {
	/**
	 * the dimension of this pixel-data
	 */
	protected Dimension dimension;
	/**
	 * x coordinate in microns, default is 0 
	 */
	protected double x;
	/**
	 * y coordinate in microns, default is 0 
	 */
	protected double y;
	/**
	 * z coordinate in microns, default is 0 
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
	 * typically the creation time (when the image was captured)
	 */
	protected Date timestamp;

	/**
	 * 
	 * @param dimension the dimension of this pixel-data
	 * @param x coordinate in microns, default is 0 
	 * @param y coordinate in microns, default is 0 
	 * @param z coordinate in microns, default is 0 
	 * @param elapsedTime time in milliseconds from the beginning of the acquisition phase
	 * @param exposureTime the exposure time (of this image) in milli-seconds
	 * @param time typically the creation time (when the image was captured)
	 */
	public PixelMetaData(Dimension dimension, double x, double y, double z, double elapsedTime, double exposureTime, Date time) 
	{
		this.dimension = dimension;
		this.x = x;
		this.y = y;
		this.z = z;
		this.elapsedTime = elapsedTime;
		this.exposureTime = exposureTime;
		this.timestamp = time;
	}
	
	
	/**
	 * Returns the dimension of this pixel-data
	 * @return the frame number for this image
	 */
	public Dimension getDimension()
	{
		return this.dimension;
	}
	
	/**
	 * x coordinate in microns, default is 0 
	 */
	public double getX()
	{
		return this.x;
	}
	
	/**
	 * y coordinate in microns, default is 0 
	 */
	public double getY()
	{
		return this.y;
	}
	
	/**
	 * actual z coordinate in microns, default is 0 
	 */
	public double getZ()
	{
		return this.z;
	}
	
	/**
	 * time in milliseconds from the beginning of the acquisition phase
	 * @return time in milliseconds
	 */
	public double getElapsedTime()
	{
		return this.elapsedTime;
	}

	/**
	 * Returns the exposure time (of this image) in milli-seconds
	 * @return the exposure time in milli-seconds
	 */
	public double getExposureTime()
	{
		return this.exposureTime;
	}

	/**
	 * typically the creation time (when the image was captured)
	 * @return  the creation time
	 */
	public Date getTimeStamp()
	{
		return this.timestamp;
	}
}
