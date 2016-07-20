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
 * RawPixelDataOverlay.java
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
package com.strandgenomics.imaging.iclient.local;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.bioformats.BioPixelDataOverlay;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;

public class RawPixelDataOverlay extends BioPixelDataOverlay {

	private static final long serialVersionUID = 3635381964305957152L;
	/**
	 * soft cache for BufferedImage
	 */
	protected transient SoftReference<BufferedImage> memoryCache = null;
	/**
	 * file storing the buffered image of this pixel data
	 */
	private File imageCache = null;
	/**
	 *  Checks if the overlay is to be created by first stacking it for all Z (slices)
	 */
	private boolean zStackEnabled = false;
	/**
	 * Checks if the resultant image is a mosaic of channels or overlaid on top
	 */
	private boolean mosaicEnabled = false;
	/**
	 * Checks if the resultant cache (image) has used channel colors
	 */
	private boolean useChannelColor = false;
	
	
	public RawPixelDataOverlay(BioRecord parentRecord, int sliceNo, int frameNo, int siteNo, int[] channelNos) 
	{
		super(parentRecord, sliceNo, frameNo, siteNo, channelNos);
	}
	
	public void clear()
	{
		memoryCache = null;
		if(imageCache != null)
		{
			imageCache.delete();
		}
		imageCache = null;
	}
	
	@Override
	public void dispose() 
	{
		super.dispose();
		memoryCache = null;
		if(imageCache != null)
		{
			imageCache.delete();
		}
		imageCache = null;
	}
	
	@Override
	public BufferedImage getImage(boolean zStacked, boolean mosaic, boolean useChannelColor, Rectangle roi) throws IOException 
	{
		if(isDifferentFromCache(zStacked, mosaic, useChannelColor))
		{
			clear();
			return createImage(zStacked, mosaic, useChannelColor, roi);
		}
		else //same as the previous stuff
		{
			//check soft refernce
			BufferedImage cachedImg = memoryCache == null ? null : memoryCache.get();
			//check file cache
			cachedImg = cachedImg == null ? readCachedImage() : cachedImg;
			
			if(cachedImg == null)
			{
				cachedImg = createImage(zStacked, mosaic, useChannelColor, roi);
			}
			
			return cachedImg;
		}
	}
	
	private boolean isDifferentFromCache(boolean zStacked, boolean mosaic, boolean useChannelColor)
	{
		return (zStackEnabled != zStacked || mosaicEnabled != mosaic || this.useChannelColor != useChannelColor);
	}
	
	private BufferedImage createImage(boolean zStacked, boolean mosaic, boolean useChannelColor, Rectangle roi) throws IOException 
	{
		BufferedImage img = super.getImage(zStacked, mosaic, useChannelColor, roi);
		this.useChannelColor = useChannelColor;
		this.zStackEnabled = zStacked;
		this.mosaicEnabled = mosaic;
		
		//write to cache for later access
		writeToCache(img);
		memoryCache = new SoftReference<BufferedImage>(img);
		return img;
	}
	
	private BufferedImage readCachedImage()
	{
		if(imageCache == null || !imageCache.isFile())
			return null;
		
		BufferedInputStream in = null;
		BufferedImage renderableImage = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(imageCache));
			renderableImage = ImageIO.read(in);
		}
		catch(IOException ex)
		{
			System.out.println("unable to read cache "+ex);
		}
		finally
		{
			try 
			{
				in.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return renderableImage;
	}
	
	protected volatile boolean writingToCache = false;
	
	protected void writeToCache(final BufferedImage renderableImage)
	{
		if(writingToCache) 
			return;
		
		if(imageCache != null && imageCache.isFile())
			imageCache.delete();
		
		imageCache = null;
		writingToCache = true;
		
		Runnable runner = new Runnable()
		{
			public void run()
			{
				BufferedOutputStream out = null;
				try
				{
					File temp = File.createTempFile("pixeloverlay", ".png", Constants.TEMP_DIR);
					
					out = new BufferedOutputStream(new FileOutputStream(temp));
					ImageIO.write(renderableImage, "PNG", out);
					
					imageCache = temp;
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					try 
					{
						out.close();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					writingToCache = false;
				}
			}
		};
		
		new Thread(runner).start();
	}
}
