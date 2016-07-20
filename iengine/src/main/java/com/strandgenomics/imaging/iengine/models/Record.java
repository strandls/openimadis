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
 * Record.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.iengine.models;

import java.math.BigInteger;
import java.util.List;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * A record object as stored in the database
 * @author arunabha
 *
 */
public class Record implements Storable {
	
	private static final long serialVersionUID = 5453007552635736694L;
	
	//references
	/** global unique identifier of this record */
	public final long guid; 
	/** id the of the project */
	public final int projectID;
	/** a record is uploaded by a specific user */
	public final String uploadedBy;
	
	//signature fields
	/** number of slices (Z-positions) */
	public final int numberOfSlices;
	/** number of frames (time samples)  */
	public final int numberOfFrames;
	/** numbers of channels (wavelengths) */
	public final int numberOfChannels;
	/** numbers of sites (location) */
	public final int numberOfSites;
	/** image width in pixels  */
	public final int imageWidth;
	/** image height in pixels */
	public final int imageHeight;
	/** the site reference, part of record signature */
	public final BigInteger siteSignature;
	/** the archive reference, part of record signature */
	public final BigInteger archiveSignature;
	
	//time information
	/** the time when the record was uploaded to the server */
	public final Long uploadTime;
	/** record's source files last modification time creation time  */
	public final Long sourceTime;
	/** record creation time  */
	public final Long creationTime;
	/** record acquisition time, from meta-data */
	public final Long acquiredTime;
	
	//image information
	/** image quality - pixel size in bytes - 32 BIT images (4 bytes), 16 BIT images (2 bytes), 8 BIT images (1 byte) */
	public final PixelDepth imageDepth;
	/** pixel size in microns in x dimension - float - default: 1.0 */
	private double xPixelSize;
	/** pixel size in microns in y dimension - float - default: 1.0  */
	private double yPixelSize;
	/** pixel size in microns in y dimension - float - default: 1.0  */
	private double zPixelSize;
	
	//source information
	/** source type (formats) */
	private SourceFormat sourceType;
	/** image type - a fixed set of values (Grayscale, RGB32, etc.) - default: Grayscale if no images are present, otherwise it must be inferred from the data  */
	public final ImageType imageType;
	
	//source machine information
	/** IP address of the acquisition machine */
	public final String machineIP;
	/** MAC address - A Media Access Control address - (ethernet card) - network hardware address of the acquisition computer  */
	public final String macAddress;
	
	//source archive information
	/** source directory */
	public final String sourceFolder;
	/** source file name */
	public final String sourceFilename;

	//others
	/** channels */
	public final List<Channel> channels;
	/** channels */
	public final List<Site> sites;
	
	// microscope/experiment information
	/** information about the acquisition this information,if present, will override */
	private AcquisitionProfile acquisitionProfile = null;
	
	/**
	 * Creates a record instance from the various fixed fields fetched from the storage
	 * @param guid the global unique identifier of the record
	 * @param projectID id of the member project
	 * @param uploadedBy id of the user who have uploaded this record
	 * @param noOfSlices  number of slices (Z-positions) 
	 * @param noOfFrames number of frames
	 * @param imageWidth image width in pixels
	 * @param imageHeight image height in pixels
	 * @param siteSignature hash of the site - part of the signature
	 * @param archiveSignature hash of the archive - part of the signature
	 * @param uploadTime time when the record was uploaded
	 * @param sourceTime last modification time of the source files
	 * @param creationTime when the record was created with the acquisition software
	 * @param acquiredTime when the source files are generated from the microscopes
	 * @param imageDepth pixel depth (bytes per pixel)
	 * @param xPixelSize pixel size in microns in x dimension
	 * @param yPixelSize pixel size in microns in y dimension
	 * @param zPixelSize pixel size in microns in z dimension
	 * @param sourceType name of the format
	 * @param imageType image type
	 * @param machineIP IP address of the acquisition machine
	 * @param macAddress MAC address of the acquisition machine
	 * @param sourceFolder source directory
	 * @param sourceFilename seed file name
	 * @param channels channels of this record
	 * @param sites sites of this record
	 * @param profile acquisition profile
	 */
	public Record(long guid, int projectID, String uploadedBy, 
			int noOfSlices, int noOfFrames, int imageWidth, int imageHeight,
			BigInteger siteSignature, BigInteger archiveSignature,
			Long uploadTime, Long sourceTime, Long creationTime, Long acquiredTime,
			PixelDepth imageDepth, double xPixelSize, double yPixelSize, double zPixelSize,
			SourceFormat sourceType, ImageType imageType, 
			String machineIP, String macAddress,
			String sourceFolder, String sourceFilename, 
			List<Channel> channels, List<Site> sites, AcquisitionProfile profile)
	{
		this.guid = guid;
		this.projectID = projectID;
		this.uploadedBy = uploadedBy;
		
		this.numberOfSlices = noOfSlices;
		this.numberOfFrames = noOfFrames;
		this.numberOfChannels = channels.size();
		this.numberOfSites = sites.size();
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.siteSignature = siteSignature;
		this.archiveSignature = archiveSignature;
		
		this.uploadTime = uploadTime;
		this.sourceTime = sourceTime;
		this.creationTime = creationTime;
		this.acquiredTime = acquiredTime;
		
		this.imageDepth = imageDepth;
		this.xPixelSize = xPixelSize;
		this.yPixelSize = yPixelSize;
		this.zPixelSize = zPixelSize;
		
		this.sourceType = sourceType;
		this.imageType = imageType;
		
		this.machineIP = machineIP;
		this.macAddress = macAddress;
		
		this.sourceFolder = sourceFolder;
		this.sourceFilename = sourceFilename;

		this.channels = channels;
		this.sites    = sites;

		if(profile==null)
		{
			String microscopeName = null;
			try
			{
				 microscopeName = SysManagerFactory.getMicroscopeManager().getMicroscope(machineIP, macAddress);
			}
			catch (DataAccessException e)
			{}
			profile = new AcquisitionProfile(microscopeName, xPixelSize, yPixelSize, zPixelSize, sourceType, TimeUnit.MILISECONDS, TimeUnit.MILISECONDS, LengthUnit.MICROMETER);
		}
		this.acquisitionProfile = profile;
	}
	
	public Signature getSignature()
	{
		return new Signature(numberOfFrames, numberOfSlices, numberOfChannels,
				imageWidth, imageHeight, sites, archiveSignature);
	}
	
	public TimeUnit getElapsedTimeUnit()
	{
		return this.acquisitionProfile.getElapsedTimeUnit();
	}
	
	public TimeUnit getExposureTimeUnit()
	{
		return this.acquisitionProfile.getExposureTimeUnit();
	}
	
	public LengthUnit getLengthUnit()
	{
		return this.acquisitionProfile.getLengthUnit();
	}
	
	public List<Site> getSites()
	{
		return this.sites;
	}
	
	public List<Channel> getChannels()
	{
		return channels;
	}
	
	public Site getSite(int siteNo)
	{
		return sites.get(siteNo);
	}
	
	public Channel getChannel(int channelNo)
	{
		return channels.get(channelNo);
	}
	
	public int getChannelCount()
	{
		return channels.size();
	}
	
	public int getSiteCount()
	{
		return sites.size();
	}
	
	public double getXPixelSize()
	{
		return this.acquisitionProfile.getxPixelSize();
	}
	
	public double getYPixelSize()
	{
		return this.acquisitionProfile.getyPixelSize();
	}
	
	public double getZPixelSize()
	{
		return this.acquisitionProfile.getzPixelSize();
	}
	
	public SourceFormat getSourceFormat()
	{
		return this.acquisitionProfile.getSourceType();
	}
	
	public String getMicroscopeName()
	{
		return this.acquisitionProfile.getMicroscope();
	}
	
	@Override
	public void dispose() 
	{}
	
	public String toString()
	{
		return ""+guid;
	}
}
