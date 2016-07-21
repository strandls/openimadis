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

/**
 * An pixel-data (raw bitmap image at a XY plane) within a record is uniquely identified by the 4 dimensions 
 * - (slices, frames, sites and channels). 
 * This are in addition to its x and y coordinates which is fixed for all pixel-data (images) within a record. 
 * All this 4 numbers starts with 0 and the last value is limited by the size in each corresponding dimensions
 * 
 * ImageCoordinate class represents the four dimension of a pixel-data that is used to identify it within a given record 
 * (excluding the x-y dimension)
 * 
 * @author arunabha
 */
public class ImageIndex extends VOIndex {
	/**
	 * the channel number
	 */
	protected int channelNo;

	public ImageIndex()
	{}
	
	/**
	 * Returns the channel number for a pixel-data with a record, default is 0
	 * @return the channel number for a pixel-data with a record
	 */
	public int getChannel()
	{
		return channelNo;
	}
	
	public void setChannel(int value)
	{
		channelNo = value;
	}
}
