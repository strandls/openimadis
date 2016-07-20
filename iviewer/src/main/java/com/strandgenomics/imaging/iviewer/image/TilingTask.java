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

package com.strandgenomics.imaging.iviewer.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.ImageViewerState;
import com.strandgenomics.imaging.iviewer.va.VAObject;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PImage;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class TilingTask extends ImagingTask{
	
	private final int TILE_HEIGHT = 10000;
	
	private final int TILE_WIDTH = 10000;
	
	public TilingTask(IRecord record, int frame, int slice, int site, Set<Integer> channels, List<VAObject> vos, boolean useChannelColor, int channelState, int sliceState, ImageConsumer consumer)
	{
		super(record, frame, slice, site, channels, vos, useChannelColor, channelState, sliceState, consumer);
	}

	public ImageResult createImage()
	{
		PLayer piccoloLayer = new PLayer();
		
		int xStart = 10;
		int yStart = 10;
		
		BufferedImage bimage = null;
		
		try
		{
			IPixelDataOverlay overlay = record.getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos);
			
			boolean mosaic = channelState != ImageViewerState.ChannelState.OVERLAY;
			boolean zStack = sliceState == ImageViewerState.SliceState.Z_STACK;

			long stime = System.currentTimeMillis();
			bimage = overlay.getImage(zStack, mosaic, useChannelColor, new Rectangle(0, 0, TILE_WIDTH, TILE_HEIGHT));
			
			System.out.println("[TilingTask] image read in "+(System.currentTimeMillis()-stime));
			Logger.getRootLogger().info("[TilingTask] Only partial image being displayed.");
			System.out.println("successfully created image with mosaic="+mosaic +", zStack="+zStack +", useChannelColor="+useChannelColor);
		} 
		catch(Exception e) 
		{
			e.printStackTrace(); //TODO
			return null;
		}
		
		PImage image = new PImage(bimage);
		image.setX(xStart);
		image.setY(yStart);
		
		for (VAObject overlay : vos)
		{
			image.addChild(overlay);
			overlay.setOffset(xStart, yStart);
		}
		
		PLayer imagesLayer = new PLayer();
		imagesLayer.addChild(image);
		piccoloLayer.addChild(imagesLayer);

		return new ImageResult(consumer, new ImageEvent(piccoloLayer, record.getSignature(), sliceNo, frameNo, siteNo, channelNos));
	}
}
