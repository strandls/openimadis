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

package com.strandgenomics.imaging.iengine.models;

import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * Record Builder object
 * 
 * @author Anup Kulkarni
 */
public class RecordBuilder implements Storable{

	/**
	 * unique identifier for record builder
	 */
	public final long guid;
	/**
	 * last access time 
	 */
	private long lastAccessTime;
	/**
	 * width of every image of the record
	 */
	public final int imageWidth;
	/**
	 * height of every image of the record
	 */
	public final int imageHeight;
	/**
	 * depth of every image of the record
	 */
	public final PixelDepth depth;
	/**
	 * number of frames (T)
	 */
	public final int maxFrames;
	/**
	 * number of slices (Z)
	 */
	public final int maxSlices;
	/**
	 * number of sites
	 */
	public final int maxSites;
	/**
	 * number of channels
	 */
	public final int maxChannels;
	/**
	 * location where actual pixel data is stored physically
	 */
	public final String sourceFileLocation;
	/**
	 * this list of dimensions for which pixel data is recevied
	 */
	private List<Dimension> receivedDimensions;
	/**
	 * parent Guid
	 */
	public final Long parentGuid;
	
	public RecordBuilder(long guid, Long parentGuid, int maxFrames, int maxSlices, int maxChannels, int maxSites, int imageWidth, int imageHeight, PixelDepth pixelDepth, String sourceLocation) 
	{
		this.guid = guid;
		this.maxFrames = maxFrames;
		this.maxChannels = maxChannels;
		this.maxSlices = maxSlices;
		this.maxSites = maxSites;
		this.sourceFileLocation = sourceLocation;
		this.depth = pixelDepth;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;

		this.setLastAccessTime(System.currentTimeMillis());
		setReceivedDimensions(new ArrayList<Dimension>());

		this.parentGuid = parentGuid;
	}

	/**
	 * adds dimension to the list of dimension for which pixel data is received
	 * @param dim
	 */
	public void addReceivedDimension(Dimension dim)
	{
		this.receivedDimensions.add(dim);
	}

	public void setReceivedDimensions(List<Dimension> receivedDimensions)
	{
		this.receivedDimensions = receivedDimensions;
	}

	public List<Dimension> getReceivedDimensions()
	{
		return receivedDimensions;
	}

	/**
	 * returns the list of dimensions for which pixel data is not received
	 * @return the list of dimensions for which pixel data is not received
	 */
	public List<Dimension> getUnreceivedDimensions()
	{
		List<Dimension> unreceivedDimensions = new ArrayList<Dimension>();
		
		for(int frame=0;frame<maxFrames;frame++)
		{
			for(int slice=0;slice<maxSlices;slice++)
			{
				for(int channel=0;channel<maxChannels;channel++)
				{
					for(int site=0;site<maxSites;site++)
					{
						Dimension dim = new Dimension(frame, slice, channel, site);
						if(!receivedDimensions.contains(dim))
						{
							unreceivedDimensions.add(dim);
						}
					}
				}
			}
		}
		
		return unreceivedDimensions;
	}
	
	public void setLastAccessTime(long lastAccessTime)
	{
		this.lastAccessTime = lastAccessTime;
	}

	public long getLastAccessTime()
	{
		return lastAccessTime;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
}
