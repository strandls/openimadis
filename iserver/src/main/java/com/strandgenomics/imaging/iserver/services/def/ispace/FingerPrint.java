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
 * FingerPrint.java
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
 * This class represents the unique signature or finger print of a record
 * @author arunabha
 */
public class FingerPrint {

	/**
	 * Total number of frames, default is 1 
	 */
	private int noOfFrames;
	/**
	 * total number of slices (z-size), default is 1 
	 */
	private int noOfSlices;
	/**
	 * total number of channels
	 */
	private int noOfChannels;
	/**
	 * total number of sites
	 */
	private int noOfSites;
	/**
	 * Width of a pixel-data (image) within this record
	 */
	private int imageWidth;
	/**
	 * Height of a pixel-data (image) within this record
	 */
	private int imageHeight;
	/**
	 * the md5 hash (128 bit, 16 bytes) of the archive
	 * we send it a hex string, big-integers are not supported in soap
	 */
	private String archiveHash;
	/**
	 * the md5 hash (128 bit, 16 bytes) of the sites
	 * we send it a hex string, big-integers are not supported in soap
	 */
	private String siteHash;
	
	
	public FingerPrint()
	{}

	/**
	 * @return the noOfFrames
	 */
	public int getNoOfFrames() 
	{
		return noOfFrames;
	}

	/**
	 * @param noOfFrames the noOfFrames to set
	 */
	public void setNoOfFrames(int noOfFrames) 
	{
		this.noOfFrames = noOfFrames;
	}

	/**
	 * @return the noOfSlices
	 */
	public int getNoOfSlices() 
	{
		return noOfSlices;
	}

	/**
	 * @param noOfSlices the noOfSlices to set
	 */
	public void setNoOfSlices(int noOfSlices) 
	{
		this.noOfSlices = noOfSlices;
	}
	
	/**
	 * @return the number of channels
	 */
	public int getNoOfChannels() 
	{
		return noOfChannels;
	}

	/**
	 * @param value the number of channels to set
	 */
	public void setNoOfChannels(int value) 
	{
		this.noOfChannels = value;
	}
	
	/**
	 * @return the number of sites
	 */
	public int getNoOfSites() 
	{
		return noOfSites;
	}

	/**
	 * @param value the number of sites to set
	 */
	public void setNoOfSites(int value) 
	{
		this.noOfSites = value;
	}

	/**
	 * @return the imageWidth
	 */
	public int getImageWidth() 
	{
		return imageWidth;
	}

	/**
	 * @param imageWidth the imageWidth to set
	 */
	public void setImageWidth(int imageWidth) 
	{
		this.imageWidth = imageWidth;
	}

	/**
	 * @return the imageHeight
	 */
	public int getImageHeight() 
	{
		return imageHeight;
	}

	/**
	 * @param imageHeight the imageHeight to set
	 */
	public void setImageHeight(int imageHeight) 
	{
		this.imageHeight = imageHeight;
	}

	/**
	 * @return the md5Hash
	 */
	public String getArchiveHash() 
	{
		return archiveHash;
	}

	/**
	 * @param md5Hash the md5Hash to set
	 */
	public void setArchiveHash(String md5Hash) 
	{
		this.archiveHash = md5Hash;
	}
	
	/**
	 * @return the md5Hash
	 */
	public String getSiteHash() 
	{
		return siteHash;
	}

	/**
	 * @param md5Hash the md5Hash to set
	 */
	public void setSiteHash(String md5Hash) 
	{
		this.siteHash = md5Hash;
	}
}
