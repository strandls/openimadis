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

/**
 * BioPixelData.java
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
import java.util.Date;
import java.util.Map;

import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ITile;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelArray;

public class BioPixelData implements IPixelData,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8637141551538687783L;
	/**
	 * owning record
	 */
	protected BioRecord parentRecord;
	/**
	 * record dimensions that uniquely identify this pixel-data
	 */
	protected Dimension imageCoordinate;
	/**
	 * x coordinate in micron, default is 0 
	 */
	protected double x;
	/**
	 * y coordinate in micron, default is 0 
	 */
	protected double y;
	/**
	 * actual z coordinate in micron, default is 0 
	 */
	protected double z;
	/**
	 * time in milliseconds from the beginning of the acquisition phase
	 */
	protected double elapsedTime;
	/**
	 * the exposure time (of this image) in milli-seconds
	 */
	protected double exposureTime;
	/**
	 * the creation time (when the image was captured)
	 */
	protected Date timeStamp;
	
	public BioPixelData(BioRecord parentRecord, Dimension imageCoordinate)
	{
		this(parentRecord, imageCoordinate, 0, 0, 0.0, 0.0, 0.0);
	}
	
	public BioPixelData(BioRecord parentRecord, Dimension imageCoordinate, double elapsedTime, double exposureTime)
	{
		this(parentRecord, imageCoordinate, elapsedTime, exposureTime, 0.0, 0.0, 0.0);
	}
	
	public BioPixelData(BioRecord parentRecord, Dimension imageCoordinate, double elapsedTime, double exposureTime, Double x, Double y, Double z)
	{
		this.parentRecord = parentRecord;
		this.imageCoordinate = imageCoordinate;
		
		this.elapsedTime = elapsedTime;
		this.exposureTime = exposureTime;
		
		this.x = x == null ? 0 : x.doubleValue();
		this.y = y == null ? 0 : y.doubleValue();
		this.z = z == null ? 0 : z.doubleValue();
	}
	
	public BioRecord getParent()
	{
		return this.parentRecord;
	}
	
	public IRecord getContainingRecord() 
	{
		return parentRecord;
	}

	@Override
	public Dimension getDimension() 
	{
		return imageCoordinate;
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
	public double getX() 
	{
		return x;
	}

	@Override
	public double getY() 
	{
		return y;
	}

	@Override
	public double getZ() 
	{
		return z;
	}

	@Override
	public double getElapsedTime() 
	{
		return elapsedTime;
	}

	@Override
	public double getExposureTime()
	{
		return exposureTime;
	}

	@Override
	public Date getTimeStamp() 
	{
		return timeStamp;
	}

	@Override
	public Map<String, Object> getMetaData() 
	{
		return null;
	}

    /**
     * Returns the raw data associated with this image 
     * @return the raw data associated with this image 
     */
	public synchronized PixelArray getRawData() throws IOException
	{
		System.out.println("[BioPixelData]:\tGetting Raw Data for "+imageCoordinate);
		
		BufferedImageReader imageReader = ImageManager.getInstance().getImageReader(parentRecord);
		int seriesNo = parentRecord.getSite(imageCoordinate.siteNo).getSeriesNo();
		
		imageReader.setSeries(seriesNo);

		int index = imageReader.getIndex(imageCoordinate.sliceNo, imageCoordinate.channelNo, imageCoordinate.frameNo);
		BufferedImage image = null;
		try 
		{
			//the buffered imaged returned here if of unknown type
			image = imageReader.openImage(index);
		} 
		catch (FormatException e) 
		{
			throw new IOException("format exception", e);
		}
		finally
		{
			try
			{
				imageReader.close(); //return it back to the pool
			}
			catch(Exception ex)
			{}
		}
		
		return PixelArray.toPixelArray(image);
	}
	
	@Override
	public PixelArray getRawData(Rectangle roi) throws IOException
	{
		if(roi == null)
			return getRawData();
		
		System.out.println("[BioPixelData]:\tGetting Raw Data for "+imageCoordinate);
		
		BufferedImageReader imageReader = ImageManager.getInstance().getImageReader(parentRecord);
		int seriesNo = parentRecord.getSite(imageCoordinate.siteNo).getSeriesNo();
		
		imageReader.setSeries(seriesNo);

		int index = imageReader.getIndex(imageCoordinate.sliceNo, imageCoordinate.channelNo, imageCoordinate.frameNo);
		BufferedImage image = null;
		try 
		{
			//the buffered imaged returned here if of unknown type
			image = imageReader.openImage(index, roi.x, roi.y, roi.width, roi.height);
		} 
		catch (FormatException e) 
		{
			throw new IOException("format exception", e);
		}
		finally
		{
			try
			{
				imageReader.close(); //return it back to the pool
			}
			catch(Exception ex)
			{}
		}
		
		return PixelArray.toPixelArray(image);
	}

	@Override
	public BufferedImage getImage(boolean useChannelColor) throws IOException 
	{
		String currentLUT = useChannelColor ? parentRecord.getChannel(imageCoordinate.channelNo).getLut() : null;

		PixelArray rawData = getRawData();
		rawData.setColorModel(LutLoader.getInstance().getLUT(currentLUT));
		
		VisualContrast contrast = parentRecord.getChannel(imageCoordinate.channelNo).getContrast(false);
		if(contrast != null) 
		{
			rawData.setContrast(contrast.getMinIntensity(), contrast.getMaxIntensity());
			rawData.setGamma(contrast.getGamma());
		}
		
		return rawData.createImage();
	}

    @Override
	public ITile getTile(int x, int y, int width, int height) 
	{
		if(x < 0 || y < 0 || width <= 0 || height <= 0 ||
				(x+width) > getImageWidth() || (y+height) > getImageHeight())
			throw new IllegalArgumentException("specified area is not contained within this PixelData");
		
		return new BioTile(parentRecord, this.imageCoordinate, new Rectangle(x,y,width,height));
	}
    
	@Override
	public void dispose() 
	{
		parentRecord = null;
		imageCoordinate = null;
	}
	
	public void clear()
	{
	}

	@Override
	public Histogram getIntensityDistibution(boolean zStacked) throws IOException
	{
		if(zStacked)
		{
			PixelArray rawData = getRawData();
			
			for(int slice = 0; slice < parentRecord.getSliceCount(); slice++)
			{
				if(slice == imageCoordinate.sliceNo) continue;
				PixelArray another = parentRecord.getPixelData(new Dimension(imageCoordinate.frameNo, slice, imageCoordinate.channelNo, imageCoordinate.siteNo)).getRawData();
				rawData.overlay(another);
			}
			
			return rawData.computeIntensityDistribution();
		}
		else
		{
			PixelArray rawData = getRawData();
			return rawData.computeIntensityDistribution();
		}
	}
}
