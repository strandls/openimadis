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
 * VODimension.java
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

import java.io.Serializable;

/**
 * Dimensions required for VisualOverlay
 * it contains frame number, slice number and overlay name
 * overlays will be shallow copied across the channels within THIS dimension
 * @author Anup Kulkarni
 */
public class VODimension implements Serializable {

	private static final long serialVersionUID = 5564561119720829242L;
	
	/**
	 * the frame number
	 */
	public final int frameNo;
	/**
	 * the slice (Z) number
	 */
	public final int sliceNo;
	/**
	 * the site number
	 */
	public final int siteNo;
	
	/**
	 * Creates a Dimension instance with the specified frame,slice
	 * @param frameNumber the frame number for the overlay
	 * @param sliceNumber the slice number for the overlay
	 * @param name the name for the overlay
	 */
	public VODimension(int frameNumber, int sliceNumber, int siteNo)
	{
		this.frameNo = frameNumber;
		this.sliceNo = sliceNumber;
		this.siteNo  = siteNo;
	}

	@Override
	public int hashCode()
	{
		return (((frameNo & 0xFF) << 24) + ((sliceNo & 0xFF) << 16) + this.siteNo <<8);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof VODimension)
		{
			VODimension that = (VODimension) obj;
			if(this == that) return true;
			
			return (this.frameNo == that.frameNo &&
					this.sliceNo == that.sliceNo &&
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
		builder.append(this.siteNo);
		builder.append('}');
		
		return builder.toString();
	}
}
