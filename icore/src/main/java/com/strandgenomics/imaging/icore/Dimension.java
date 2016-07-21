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

package com.strandgenomics.imaging.icore;

/**
 * An pixel-data (raw bitmap image) within a record is uniquely identified by the 4 dimensions 
 * - (slices, frames, sites and channels). 
 * This are in addition to its x and y coordinates which is fixed for all pixel-data (images) within a record. 
 * All this 4 numbers starts with 0 and the last value is limited by the size in each corresponding dimensions
 * 
 * Dimension class represents the four dimension of a pixel-data that is used to identify it within a given record 
 * (excluding the x-y dimension)
 * 
 * @author arunabha
 */
public class Dimension extends VODimension implements Comparable<Dimension> {
	
	private static final long serialVersionUID = -5219011958689941797L;

	/**
	 * the channel number
	 */
	public final int channelNo;

	/**
	 * Creates a Dimension instance with the specified frame,slice,site,channel numbers
	 * @param frameNumber the frame number for a pixel-data with a record
	 * @param sliceNumber the slice number for a pixel-data with a record
	 * @param channelNumber the channel number for a pixel-data with a record
	 */
	public Dimension(int frameNumber, int sliceNumber, int channelNumber, int siteNumber)
	{
		super(frameNumber, sliceNumber, siteNumber);
		this.channelNo = channelNumber;
	}

	@Override
	public int hashCode()
	{
		return (((frameNo & 0xFF) << 24) + ((sliceNo & 0xFF) << 16) + ((siteNo & 0xFF) << 8) + ((channelNo & 0xFF) << 0));
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof Dimension)
		{
			Dimension that = (Dimension) obj;
			return (this.frameNo == that.frameNo &&
					this.sliceNo == that.sliceNo &&
					this.channelNo == that.channelNo &&
					this.siteNo == that.siteNo);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		builder.append(this.frameNo).append(',');
		builder.append(this.sliceNo).append(',');
		builder.append(this.channelNo).append(',');
		builder.append(this.siteNo);
		builder.append('}');
		
		return builder.toString();
	}

	/**
	 * the order is frame slice,channel and then site
	 */
	@Override
	public int compareTo(Dimension that)
	{
		//returns a negative integer, zero, or a positive integer as this object is less than, 
		//equal to, or greater than the specified object. 
		if(this.frameNo < that.frameNo)
			return -1;
		else if(this.frameNo > that.frameNo)
			return 1;
		else 
		{
			if(this.sliceNo < that.sliceNo)
				return -1;
			else if(this.sliceNo > that.sliceNo)
				return 1;
			else
			{
				if(this.channelNo < that.channelNo)
					return -1;
				else if(this.channelNo > that.channelNo)
					return 1;
				else
				{
					if(this.siteNo < that.siteNo)
						return -1;
					else if(this.siteNo > that.siteNo)
						return 1;
					else
						return 0;
				}
			}
		}
	}
}
