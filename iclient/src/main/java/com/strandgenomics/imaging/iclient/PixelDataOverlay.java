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

package com.strandgenomics.imaging.iclient;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.strandgenomics.imaging.icore.IPixelDataOverlay;

/**
 * Represents the overlay state of many pixel-data within a record
 * @author arunabha
 */
public class PixelDataOverlay extends ImageSpaceObject implements IPixelDataOverlay {
	
	private static final long serialVersionUID = -5825704111427039161L;
	/**
	 * the parent record
	 */
	protected Record parentRecord;
	/**
	 *  The slice number (Z) of the image to overlay with channels
	 */
	public final int sliceNo;
	/**
	 * The frame number of the image to overlay with channels
	 */
	public final int frameNo;
	/**
	 *  The site number of the image to overlay with channels
	 */
	public final int siteNo; 
	/**
	 *  The list of channel numbers (dimensions) that are combined together to create this PixelData overlay  
	 */
	protected final int[] channelNos;
	
	//package projected - to be used locally
	PixelDataOverlay(Record parent, int sliceNo, int frameNo, int siteNo, int[] channelNos)
	{
		this.parentRecord = parent;
		this.sliceNo = sliceNo;
		this.frameNo = frameNo;
		this.siteNo  = siteNo;
		this.channelNos = channelNos;
	}


	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	}


	@Override
    public int getImageWidth()
    {
    	return parentRecord.getImageWidth();
    }
    
	@Override
    public int getImageHeight()
    {
    	return parentRecord.getImageHeight();
    }


	@Override
	public int[] getOverlaidChannels()
	{
		return channelNos;
	}


	@Override
	public int getSliceNumber()
	{
		return sliceNo;
	}


	@Override
	public int getFrameNumber() 
	{
		return frameNo;
	}


	@Override
	public int getSiteNumber() 
	{
		return siteNo;
	}


	@Override
	public BufferedImage getImage(boolean zStacked, boolean mosaic, boolean useChannelColor, Rectangle roi) throws IOException
	{
		if(roi == null)
			roi = new Rectangle(0, 0, parentRecord.getImageWidth(), parentRecord.getImageHeight());
		return getImageSpace().getOverlayedImage(parentRecord.getGUID(), sliceNo, frameNo, siteNo, channelNos, zStacked, mosaic, useChannelColor, roi.x, roi.y, roi.width, roi.height);
	}
}
