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

package com.strandgenomics.imaging.iserver.services.def.ispace;

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
