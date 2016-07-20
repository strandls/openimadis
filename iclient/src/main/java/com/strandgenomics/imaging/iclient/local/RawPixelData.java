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
 * RawPixelData.java
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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.SoftReference;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.bioformats.BioPixelData;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.image.PixelArray;

public class RawPixelData extends BioPixelData {

	private static final long serialVersionUID = -8233076643703701620L;
	
	/**
	 * soft cache for BufferedImage
	 */
	private transient SoftReference<BufferedImage> memoryCache = null;
	
	/**
	 * file storing the buffered image of this pixel data
	 */
	private File imageCache = null;
	private File pixelCache = null; 

	public RawPixelData(BioRecord parentRecord, Dimension imageCoordinate, double elapsedTime, double exposureTime, Double x, Double y, Double z)
	{
		super(parentRecord, imageCoordinate, elapsedTime, exposureTime, x, y, z);
	}
	
	@Override
	public void dispose() 
	{
		super.dispose();
		
		if(pixelCache != null && pixelCache.isFile())
			pixelCache.delete();
		
		if(imageCache != null && imageCache.isFile()) 
			imageCache.delete();
		
		pixelCache = null;
		imageCache = null;
		memoryCache = null;
	}
	
	public void clear()
	{
		memoryCache = null;
		if(imageCache != null) imageCache.delete();
		imageCache = null;
	}
	
    /**
     * Returns the raw data associated with this image 
     * @return the raw data associated with this image 
     */
	public synchronized PixelArray getRawData() throws IOException
	{
		if(pixelCache != null && pixelCache.isFile())
		{
			PixelArray pixeldata = readCachedPixelData();
			if(pixeldata != null) return pixeldata; 
		}
		
		//generate the image
		PixelArray pixelData = super.getRawData();
		//write to cache for later access
		writeToCache(pixelData);
		
		return pixelData;
	}
	
//	private Color previousColor = null;
	private String previousLUT = null;
	
	@Override
	public BufferedImage getImage(boolean useChannelColor) throws IOException 
	{
//		Color currentColor = useChannelColor ? new Color(parentRecord.getChannel(imageCoordinate.channelNo).getColor()) : null;
//
//		if((previousColor != null && currentColor == null) || (previousColor == null && currentColor != null))
//		{
//			memoryCache = null;
//			if(imageCache != null && imageCache.isFile()) imageCache.delete();
//			imageCache = null;
//		}
//		else if(previousColor != null && currentColor != null && !previousColor.equals(currentColor))
//		{
//			memoryCache = null;
//			if(imageCache != null && imageCache.isFile()) imageCache.delete();
//			imageCache = null;
//		}
//		
//		//for the next call
//		previousColor = currentColor;
		
		String currentLUT = useChannelColor ? parentRecord.getChannel(imageCoordinate.channelNo).getLut() : null;

		if ((previousLUT != null && currentLUT == null)
				|| (previousLUT == null && currentLUT != null))
		{
			memoryCache = null;
			if (imageCache != null && imageCache.isFile())
				imageCache.delete();
			imageCache = null;
		}
		else
			if (previousLUT != null && currentLUT != null
					&& !previousLUT.equals(currentLUT))
			{
				memoryCache = null;
				if (imageCache != null && imageCache.isFile())
					imageCache.delete();
				imageCache = null;
			}

		// for the next call
		previousLUT = currentLUT;
		
		if(memoryCache == null)
		{
			BufferedImage img = createBufferedImage(useChannelColor);
			memoryCache = new SoftReference<BufferedImage>(img);
			return img;
		}
		else
		{
			BufferedImage cachedImg =  memoryCache.get();
			if(cachedImg == null)
			{
				cachedImg = createBufferedImage(useChannelColor);
				memoryCache = new SoftReference<BufferedImage>(cachedImg);
			}
			return cachedImg;
		}
	}
	
	private BufferedImage createBufferedImage(boolean useChannelColor) throws IOException 
	{
		if(imageCache != null && imageCache.isFile())
		{
			BufferedImage image = readCachedImage();
			if(image != null) return image; 
		}
		
		//get the image
		BufferedImage renderableImage = super.getImage(useChannelColor);
		//write to cache for later access
		writeToCache(renderableImage);
		
		return renderableImage;
	}
	
	private BufferedImage readCachedImage()
	{
		if(imageCache == null && !imageCache.isFile())
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
	
	private void writeToCache(BufferedImage renderableImage)
	{
		if(imageCache != null && imageCache.isFile())
			imageCache.delete();
		
		BufferedOutputStream out = null;
		try
		{
			imageCache = File.createTempFile("pixeldata", ".png", Constants.TEMP_DIR);
			
			out = new BufferedOutputStream(new FileOutputStream(imageCache));
			ImageIO.write(renderableImage, "PNG", out);
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
		}
	}
	
	private volatile boolean writingToCache = false;

	private synchronized void writeToCache(final PixelArray pixelData) 
	{
		if(writingToCache) 
			return;
		
		if(pixelCache != null && pixelCache.isFile())
			pixelCache.delete();
		
		pixelCache = null;
		writingToCache = true;

		Runnable runner = new Runnable()
		{
			public void run()
			{
				ObjectOutputStream out = null;
				try
				{
					File temp = File.createTempFile("pixeldata", ".raw", Constants.TEMP_DIR);

					out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(temp)));
					out.writeObject(pixelData);
					pixelCache = temp;
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
		
		//write the cache in a separate thread
		new Thread(runner).start();
	}

	private PixelArray readCachedPixelData() 
	{
		if(pixelCache == null && !pixelCache.isFile())
			return null;

		ObjectInputStream in = null;
		PixelArray pixelData = null;
		try
		{
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(pixelCache)));
			pixelData = (PixelArray) in.readObject();
			System.out.println("[RawPixelData]:\tsuccessfully read raw pixels from cache "+getDimension());
		}
		catch(IOException ex)
		{
			System.out.println("unable to read cache "+ex);
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println("error reading cache "+ex);
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
		
		return pixelData;
	}
}
