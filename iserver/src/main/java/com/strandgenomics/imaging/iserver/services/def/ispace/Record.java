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

/*
 * RecordObject.java
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
 * A record is a collection of images
 * @author arunabha
 *
 */
public class Record {

	/**
	 * the unique signature is associated with this record
	 */
	protected FingerPrint signature = null;
	/**
	 * who uploaded this record
	 */
	protected String uploadedBy;
	/**
	 * record creation time in the acquisition machine 
	 */
	protected Long creationTime ;
	/**
	 * when this record was uploaded
	 */
	protected Long uploadTime;
	/**
	 * actual date of the acquisition, may be null
	 */
	protected Long acquiredDate;
	/**
	 * last modification time on the source file
	 */
	protected Long sourceFileTime;
	/**
	 * checks if the login user has write permission on this record
	 */
	protected boolean isWritable = false;
	/**
	 * MAC address of the machine from which the record was uploaded (acquisition machine )
	 */
	protected String macAddress = null;
	/**
	 * IP address of the machine from which the record was uploaded (acquisition machine )
	 */
	protected String ipAddress = null;
	/**
	 * pixel depth for pixel-data (image), valid values are 1 (8 bits), 2 (16 bits) and 4 (32 bits)
	 */
	protected int pixelDepth;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected double pixelSizeAlongXAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected double pixelSizeAlongYAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * for z-axis movement, each slice move by this physical value
	 * value is in microns
	 */
	protected double pixelSizeAlongZAxis;
	/**
	 * type of the image, default is gray-scale, default is 0 - gray-scale, 1 for COLOR
	 */
	protected int imageType;
	/**
	 * file format of the original record files
	 */
	protected String sourceFormat;
	/** 
	 * source directory 
	 */
	protected String sourceFolder;
	/** 
	 * source file name (one of possibly many)
	 */
	protected String sourceFilename;
	/**
	 * physical size in bytes of the source files
	 */
	protected long sizeOnDisk;
	
	public Record()
	{}

	/**
	 * @return the sizeOnDisk
	 */
	public long getSizeOnDisk() 
	{
		return sizeOnDisk;
	}

	/**
	 * @param sizeOnDisk the sizeOnDisk to set
	 */
	public void setSizeOnDisk(long sizeOnDisk)
	{
		this.sizeOnDisk = sizeOnDisk;
	}

	/**
	 * @return the signature
	 */
	public FingerPrint getSignature() 
	{
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(FingerPrint signature) 
	{
		this.signature = signature;
	}

	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() 
	{
		return uploadedBy;
	}

	/**
	 * @param uploadedBy the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) 
	{
		this.uploadedBy = uploadedBy;
	}

	/**
	 * @return the uploadTime
	 */
	public Long getUploadTime() 
	{
		return uploadTime;
	}

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Long value) 
	{
		this.uploadTime = value;
	}
	
	/**
	 * @return the creationTime
	 */
	public Long getCreationTime() 
	{
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Long value) 
	{
		creationTime = value;
	}
	
	/**
	 * @return the creationTime
	 */
	public Long getAcquiredDate() 
	{
		return acquiredDate;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setAcquiredDate(Long value) 
	{
		acquiredDate = value;
	}
	
	/**
	 * @return the creationTime
	 */
	public Long getSourceFileTime() 
	{
		return sourceFileTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setSourceFileTime(Long value) 
	{
		sourceFileTime = value;
	}

	/**
	 * @return the isWritable
	 */
	public boolean isWritable() 
	{
		return isWritable;
	}

	/**
	 * @param isWritable the isWritable to set
	 */
	public void setWritable(boolean isWritable) 
	{
		this.isWritable = isWritable;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() 
	{
		return macAddress;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) 
	{
		this.macAddress = macAddress;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() 
	{
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the pixelDepth
	 */
	public int getPixelDepth() 
	{
		return pixelDepth;
	}

	/**
	 * @param pixelDepth the pixelDepth to set
	 */
	public void setPixelDepth(int pixelDepth) 
	{
		this.pixelDepth = pixelDepth;
	}

	/**
	 * @return the pixelSizeAlongXAxis
	 */
	public double getPixelSizeAlongXAxis() 
	{
		return pixelSizeAlongXAxis;
	}

	/**
	 * @param pixelSizeAlongXAxis the pixelSizeAlongXAxis to set
	 */
	public void setPixelSizeAlongXAxis(double pixelSizeAlongXAxis) 
	{
		this.pixelSizeAlongXAxis = pixelSizeAlongXAxis;
	}

	/**
	 * @return the pixelSizeAlongYAxis
	 */
	public double getPixelSizeAlongYAxis() 
	{
		return pixelSizeAlongYAxis;
	}

	/**
	 * @param pixelSizeAlongYAxis the pixelSizeAlongYAxis to set
	 */
	public void setPixelSizeAlongYAxis(double pixelSizeAlongYAxis) 
	{
		this.pixelSizeAlongYAxis = pixelSizeAlongYAxis;
	}

	/**
	 * @return the pixelSizeAlongZAxis
	 */
	public double getPixelSizeAlongZAxis() 
	{
		return pixelSizeAlongZAxis;
	}

	/**
	 * @param pixelSizeAlongZAxis the pixelSizeAlongZAxis to set
	 */
	public void setPixelSizeAlongZAxis(double pixelSizeAlongZAxis) 
	{
		this.pixelSizeAlongZAxis = pixelSizeAlongZAxis;
	}

	/**
	 * @return the imageType
	 */
	public int getImageType() {
		return imageType;
	}

	/**
	 * @param imageType the imageType to set
	 */
	public void setImageType(int imageType) {
		this.imageType = imageType;
	}

	/**
	 * @return the sourceFormat
	 */
	public String getSourceFormat() {
		return sourceFormat;
	}

	/**
	 * @param sourceFormat the sourceFormat to set
	 */
	public void setSourceFormat(String sourceFormat) {
		this.sourceFormat = sourceFormat;
	}

	/**
	 * @return the sourceFolder
	 */
	public String getSourceFolder()
	{
		return sourceFolder;
	}
	
	/**
	 * @param sourceFolder the sourceFolder to set
	 */
	public void setSourceFolder(String value) 
	{
		this.sourceFolder = value;
	}
	
	/**
	 * @return the sourceFilename
	 */
	public String getSourceFilename() 
	{
		return sourceFilename;
	}
	
	/**
	 * @param sourceFilename the sourceFilename to set
	 */
	public void setSourceFilename(String value) 
	{
		this.sourceFilename = value;
	}
}
