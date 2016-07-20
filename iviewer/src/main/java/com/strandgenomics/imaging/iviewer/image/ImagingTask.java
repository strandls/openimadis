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

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.ImageViewerState;
import com.strandgenomics.imaging.iviewer.va.VAObject;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PImage;

/**
 * the class that actually generates the image
 * @author arunabha
 *
 */
public class ImagingTask implements Callable <ImageResult>
{
	/**
	 * the record whose image needs to be generated
	 */
	protected IRecord record = null;

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
	public final int[] channelNos;
	
	/**
	 * visual annotations
	 */
	protected List<VAObject> vos;
	/**
	 * checks whether to use channel colors
	 */
	protected boolean useChannelColor = false;
	/**
	 * state of channel (overlay/tile)
	 */
	protected int channelState = ImageViewerState.ChannelState.OVERLAY; 
	/**
	 * state of slice
	 */
	protected int sliceState = ImageViewerState.SliceState.SINGLE_SLICE;
	/**
	 * the object which is interested
	 */
	protected ImageConsumer consumer;
    /**
     * the requestID at the time of submitting this task
     */
    private long requestID;
	
	public ImagingTask(IRecord record, int frame, int slice, int site, Set<Integer> channels, 
    		List<VAObject> vos, boolean useChannelColor, int channelState, int sliceState, ImageConsumer consumer)
	{
		this.record = record;
		
		this.sliceNo = slice;
		this.frameNo = frame;
		this.siteNo  = site;
		this.channelNos = new int[channels.size()];
		
		int i = 0;
		for(Integer channelNo : channels)
		{
			channelNos[i++] = channelNo;
		}

		this.vos = vos;
		this.useChannelColor = useChannelColor;
		this.channelState = channelState;
		this.sliceState = sliceState;
		this.consumer = consumer;
		//the current request 
		requestID = consumer.getCurrentRequestID();
	}
	
	public ImageResult call() throws Exception 
	{
		System.out.println("serving request on record "+record.getSignature() +",sliceNo="+sliceNo +",frameNo="+frameNo);
		if(requestID != consumer.getCurrentRequestID()) 
		{
			//abort the image creation
			return new ImageResult(consumer, new ImageEvent(null, record.getSignature(), sliceNo, frameNo, siteNo, channelNos)); 
		}
		
	    ImageResult result = createImage();

	    if(result != null)
	    {
			ImageEvent theImage = result.getImage();
			if(theImage != null && theImage.isValid())
			{
				result.getConsumer().consumeImage(theImage);
			}
	    }
	    
	    return result;
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

			bimage = overlay.getImage(zStack, mosaic, useChannelColor, null);
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
