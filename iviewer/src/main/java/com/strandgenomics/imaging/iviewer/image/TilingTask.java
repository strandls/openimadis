package com.strandgenomics.imaging.iviewer.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

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
