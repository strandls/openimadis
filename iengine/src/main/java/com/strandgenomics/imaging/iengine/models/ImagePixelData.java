package com.strandgenomics.imaging.iengine.models;

import java.util.Date;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.Storable;

/**
 * Image Pixel Data Object for containing information about image of specified
 * dimension
 * 
 * @author Anup Kulkarni
 */
public class ImagePixelData implements Storable {
	/**
	 * x coordinate in microns
	 */
	double x_coordinate;
	
	/**
	 * y coordinate in microns
	 */
	double y_coordinate;
	
	/**
	 * z coordinate in microns
	 */
	double z_coordinate;
	
	/**
	 * elapsed  time in milliseconds from the beginning of the acquisition phase
	 */
	double elapsed_time;
	
	/**
	 * exposure time (of this image) in milli-seconds
	 */
	double exposure_time;
	
	/**
	 * typically the creation time (when the image was captured)
	 */
	Date timestamp;
	/**
	 * Image coordinate
	 */
	Dimension imageCoordinate = null;
	
	/**
	 * constructs pixel data object for specified image
	 * @param guid of parent record
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 * @param elapsedtime time in milliseconds from the beginning of the acquisition phase
	 * @param exposuretime exposure time (of this image) in milli-seconds
	 * @param timestamp time of creation of this image 
	 * @param slice slice no of this image (constitutes image dimension) 
	 * @param frame frame no of this image (constitutes image dimension)
	 * @param channel channel no of this image (constitutes image dimension)
	 * @param site site no of this image (constitutes image dimension)
	 */
	public ImagePixelData(double x, double y,
			double z, double elapsedtime, double exposuretime, Date timestamp,
			int slice, int frame, int channel, int site) {
		
		this.x_coordinate = x;
		this.y_coordinate = y;
		this.z_coordinate = z;
		this.elapsed_time = elapsedtime;
		this.exposure_time = exposuretime;
		this.timestamp = timestamp;
		
		this.imageCoordinate = new Dimension(frame, slice, channel, site);
	}
	
	public double getX() {
		return x_coordinate;
	}

	public void setX(double x_coordinate) {
		this.x_coordinate = x_coordinate;
	}

	public double getY() {
		return y_coordinate;
	}

	public void setY(double y_coordinate) {
		this.y_coordinate = y_coordinate;
	}

	public double getZ() {
		return z_coordinate;
	}

	public void setZ(double z_coordinate) {
		this.z_coordinate = z_coordinate;
	}

	public double getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsedTime(double elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public double getExposureTime() {
		return exposure_time;
	}

	public void setExposureTime(double exposure_time) {
		this.exposure_time = exposure_time;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Dimension getDimension()
	{
		return imageCoordinate;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
