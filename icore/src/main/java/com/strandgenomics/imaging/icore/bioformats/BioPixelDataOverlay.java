/**
 * BioPixelDataOverlay.java
 *
 * Project imaging
 * Core Bioformat Component
 *
 * Copyright 2009-2010 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.bioformats;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.util.ImageUtil;

public class BioPixelDataOverlay implements IPixelDataOverlay, Serializable {

	private static final long serialVersionUID = 6080117897354829905L;
	/**
	 * owning record
	 */
	protected BioRecord parentRecord;
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

	/**
	 * Create a overlay of PixelDatas for the specified image coordinates (Dimension)
	 * @param parentRecord the owning record
	 * @param overlayDimensions the list of image coordinates
	 */
	public BioPixelDataOverlay(BioRecord parentRecord, int sliceNo, int frameNo, int siteNo, int[] channelNos)
	{
		this.parentRecord = parentRecord;
		this.sliceNo = sliceNo;
		this.frameNo = frameNo;
		this.siteNo  = siteNo;
		this.channelNos = channelNos;
	}

	public IRecord getContainingRecord() 
	{
		return parentRecord;
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
	public BufferedImage getImage(boolean zStacked, boolean mosaic, boolean useChannelColor, Rectangle roi) throws IOException 
	{
		System.out.println("[BioPixelDataOverlay]:\tcreating image for sliceNo="+sliceNo +", frameNo="+frameNo +", siteNo="+siteNo );
		List<Channel> channels = parentRecord.channels;
		
		VisualContrast[] contrasts = new VisualContrast[channelNos.length];
		
		String[] luts = new String[channelNos.length];
		for(int i = 0;i < channelNos.length; i++)
		{
			int channelNo = channelNos[i];
			Channel ithChannel = channels.get(channelNo);

			luts[i] = ithChannel.getLut() == null || !useChannelColor ? "grays" : ithChannel.getLut();
			contrasts[i] = ithChannel.getContrast(zStacked);
		}
		
		PixelArray[] pixelArrays = new PixelArray[channelNos.length];
		int noOfSlices = parentRecord.getSliceCount();
		
		for(int i = 0;i < channelNos.length; i++)
		{
			int channelNo = channelNos[i];
			PixelArray rawData = null;
			
			if(zStacked)
			{
				System.out.println("[BioPixelDataOverlay]:\toverlaying with channelNo="+channelNo);
				// XXX: Contrast is set to the slice passed in dimension 
				Dimension imageCoordinate = new Dimension(frameNo, sliceNo, channelNo, siteNo);
				
				rawData = parentRecord.getPixelData(imageCoordinate).getRawData(roi).clone();
				rawData.setColorModel(LutLoader.getInstance().getLUT(luts[i]));
				
				for(int slice = 0; slice < noOfSlices; slice++)
				{
					if(slice == sliceNo)
						continue;
					
					Dimension zCoordinate = new Dimension(frameNo, slice, channelNo, siteNo);
					PixelArray another = parentRecord.getPixelData(zCoordinate).getRawData(roi);
					rawData.overlay(another);
				}
				
				if(contrasts[i] != null) 
				{
					rawData.setContrast(contrasts[i].getMinIntensity(), contrasts[i].getMaxIntensity());
					rawData.setGamma(contrasts[i].getGamma());
				}
				else
				{
					rawData.setAutoContrast();
					rawData.setGamma(1.0);
				}
			}
			else
			{
				Dimension d = new Dimension(frameNo, sliceNo, channelNo, siteNo);
				rawData = parentRecord.getPixelData(d).getRawData(roi);
				rawData.setColorModel(LutLoader.getInstance().getLUT(luts[i]));
				
				if(contrasts[i] != null) 
				{
					rawData.setContrast(contrasts[i].getMinIntensity(), contrasts[i].getMaxIntensity());
					rawData.setGamma(contrasts[i].getGamma());
				}
			}
		
			
			pixelArrays[i] = rawData;
		}
		
		BufferedImage renderableImage = null;
		
		if(channelNos.length > 1)
		{
			if(mosaic)
			{
				List<BufferedImage> imageList = new ArrayList<BufferedImage>();
				for(int i = 0;i < pixelArrays.length; i++)
				{
					BufferedImage image = pixelArrays[i].createImage();
					imageList.add(image);
				}
				
				renderableImage = ImageUtil.createMosaicImage(imageList, getImageWidth(), getImageHeight(), 5);
			}
			else
			{
				renderableImage = PixelArray.getOverlayImage(pixelArrays);
			}
		}
		else
		{
			renderableImage = pixelArrays[0].createImage();
		}

		return renderableImage;
	}

	public void clear()
	{}

	@Override
	public void dispose() 
	{
		parentRecord = null;
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
}
