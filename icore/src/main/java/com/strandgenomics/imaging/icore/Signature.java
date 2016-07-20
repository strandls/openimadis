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
 * Signature.java
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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;


/**
 * This class represents the Unique signature of a record
 * @author arunabha
 */
public class Signature implements Disposable, Serializable {
	
	private static final long serialVersionUID = 7110539096881137453L;
	/**
	 * Total number of frames, default is 1 
	 */
	public final int noOfFrames;
	/**
	 * total number of slices (z-size), default is 1 
	 */
	public final int noOfSlices;
	/**
	 * total number of channels, default is 1 
	 */
	public final int noOfChannels;
	/**
	 * List of available sites
	 */
	public final int noOfSites;
	/**
	 * Width of a pixel-data (image) within this record
	 */
	public final int imageWidth;
	/**
	 * Height of a pixel-data (image) within this record
	 */
	public final int imageHeight;
	/**
	 * the md5 hash (128 bit, 16 bytes) long number - archive md5 hash
	 */
	public final BigInteger archiveHash;
	/**
	 * the md5 hash (128 bit, 16 bytes) long number - site md5 hash
	 */
	public final BigInteger siteHash;
	
	/**
	 * Constructs a signature/finger print of a record
	 * @param noOfFrames number of frames within this record
	 * @param noOfSlices number of slices (z) within this record
	 * @param noOfChannels number of channels within this record
	 * @param imageWidth width of a pixel-data (image) within this record
	 * @param imageHeight height of a pixel-data (image) within this record
	 * @param sites list of sites
	 * @param archiveHash the md5 hash (128 bit, 16 bytes) long number of the pixel data in the middle coordinates
	 */
	public Signature(int noOfFrames, int noOfSlices, int noOfChannels,
			int imageWidth, int imageHeight, List<Site> sites, BigInteger archiveHash)
	{
		this(noOfFrames, noOfSlices, noOfChannels, sites.size(), imageWidth, imageHeight, createSiteHash(sites), archiveHash);
	}
	
	public Signature(int noOfFrames, int noOfSlices, int noOfChannels, int noOfSites,
			int imageWidth, int imageHeight, BigInteger siteHash, BigInteger archiveHash)
	{
		this.noOfFrames   = noOfFrames;
		this.noOfSlices   = noOfSlices;
		this.noOfChannels = noOfChannels;
		this.noOfSites    = noOfSites;
		
		this.imageWidth  = imageWidth;
		this.imageHeight = imageHeight;
		
		this.archiveHash = archiveHash;
		this.siteHash    = siteHash;
	}
	
	public int getNoOfFrames()
	{
		return noOfFrames;
	}

	public int getNoOfSlices() 
	{
		return noOfSlices;
	}

	public int getNoOfChannels()
	{
		return noOfChannels;
	}
	
	public int getNoOfSites() 
	{
		return noOfSites;
	}
	
	public int getImageWidth() 
	{
		return imageWidth;
	}

	public int getImageHeight() 
	{
		return imageHeight;
	}

	public BigInteger getArchiveHash() 
	{
		return archiveHash;
	}
	
	public BigInteger getSiteHash() 
	{
		return siteHash;
	}
	
	@Override
	public int hashCode()
	{
		return this.archiveHash.intValue();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof Signature)
		{
			Signature that = (Signature) obj;
			if(this == that) return true;
			
			return (this.noOfFrames == that.noOfFrames &&
					this.noOfSlices == that.noOfSlices &&
					this.noOfChannels == that.noOfChannels &&
					this.imageWidth == that.imageWidth && 
					this.imageHeight == that.imageHeight &&
					this.noOfSites == that.noOfSites &&
					this.siteHash.equals( that.siteHash ) &&
					this.archiveHash.equals(that.archiveHash));
		}
		return false;
	}

	@Override
	public void dispose() 
	{}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		builder.append(this.noOfFrames).append(',');
		builder.append(this.noOfSlices).append(',');
		builder.append(this.noOfChannels).append(',');
		builder.append(this.noOfSites).append(',');
		builder.append(this.imageWidth).append(',');
		builder.append(this.imageHeight).append(',');
		builder.append(this.archiveHash);
		builder.append('}');
		
		return builder.toString();
	}
	
	/**
	 * a way to create the site hash from the sites
	 * @param siteList list of sites
	 * @return the MD5 hash as a 128 bit BigInteger
	 */
	public static final BigInteger createSiteHash(List<Site> siteList)
	{
		try
		{
			int[] seriesList = new int[siteList.size()];
			for(int i = 0;i < seriesList.length; i++)
				seriesList[i] = siteList.get(i).getSeriesNo();
			
			//sort w.r.t the series number
			Arrays.sort(seriesList);
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream(seriesList.length * 4);
			DataOutputStream out = new DataOutputStream(buffer);
			
			for(int i = 0;i < seriesList.length; i++)
				out.writeInt(seriesList[i]);
			
			out.flush();
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(buffer.toByteArray());

			return new BigInteger(md.digest());
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	    catch (NoSuchAlgorithmException e) 
	    {
	    	throw new RuntimeException(e);
	    }
	}
}
