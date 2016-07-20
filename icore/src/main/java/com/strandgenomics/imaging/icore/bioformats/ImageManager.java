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
 * ImageManager.java
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
package com.strandgenomics.imaging.icore.bioformats;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import loci.formats.gui.BufferedImageReader;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.PixelArray;

public class ImageManager {
	
	/**
	 * used for synchronizing the creation data access objects
	 */
	private static Object padLock = new Object();
	/**
	 * singleton instance of the cache
	 */
	private static ImageManager instance = null;
	
	/**
	 * Returns the singleton instance of Cache 
	 * @return the singleton instance of Cache 
	 */
	public static ImageManager getInstance()
	{
		if(instance == null)
		{
			synchronized(padLock)
			{
				if(instance == null)
				{
					ImageManager c = new ImageManager();
					//do initialization is needed
		            c.initialize();
					instance = c;
				}
			}
		}
		return instance;
	}
	
	/**
	 * a pool of image readers per Archive
	 */
	protected Map<BigInteger, BufferedImageReaderPool> cache = null;
	/**
	 * maximum number of archives to serve concurrently
	 */
	protected int maxArchives = 20;
	/**
	 * Logger
	 */
	private Logger logger = Logger.getRootLogger();
	
	private ImageManager()
	{
		cache = new ConcurrentHashMap<BigInteger, BufferedImageReaderPool>();
		
		maxArchives = Constants.getMaxArchives();
	}

	private void initialize() 
	{}

	/**
	 * Returns the pool of image readers ,maintained for the specified experiment
	 * @param expt the experiment under consideration
	 * @return the existing or a newly create pool
	 * @throws DataAccessException
	 */
	private synchronized BufferedImageReaderPool getPool(BioExperiment expt) throws DataAccessException
	{
		System.out.println("[ImageManager]:\tFetching Reader-Pool for "+expt.getMD5Signature());
		
		BufferedImageReaderPool pool = null;
		
		if(cache.containsKey(expt.getMD5Signature()))
		{
			pool = cache.get(expt.getMD5Signature());
		}
		else
		{	
			makeRoom(); //make sure there is room from this new entry
			pool = createPool(expt);
			cache.put(expt.getMD5Signature(), pool);
		}
		
		return pool;
	}
	
	/**
	 * make room in the pool
	 */
	private synchronized void makeRoom()
	{
		//make room if there are too many
		logger.info("[ImageManager]:\t cache size "+cache.size() +", maxArchives="+maxArchives);
		if(cache.size() < maxArchives) return; //nothing needs to be done
		
		logger.warn("[ImageManager]:\t cache is full...");
		
		BigInteger leastBusy = null;
		long lastUsage = Long.MAX_VALUE;
		
		//see if there is an idle guy, delete that last used idle pool
		for(Map.Entry<BigInteger, BufferedImageReaderPool> entry : cache.entrySet())
		{
			BufferedImageReaderPool pool = entry.getValue();
			BigInteger archiveID = entry.getKey();
			
			if(pool.getLastUsageTime() < lastUsage && pool.isIdle())
			{
				leastBusy = archiveID;
				lastUsage = pool.getLastUsageTime();
			}
		}
		
		if(leastBusy != null)
		{
			logger.warn("[ImageManager]:\t making room by removing the oldest idle "+leastBusy);
			BufferedImageReaderPool pool = cache.remove(leastBusy);
			pool.dispose();
			return;
		}
			
		//nobody is idle, delete the least busy guy
		logger.warn("[ImageManager]:\t overload, making room for new experiment");
		leastBusy = null;
		lastUsage = Long.MAX_VALUE;

		for(Map.Entry<BigInteger, BufferedImageReaderPool> entry : cache.entrySet())
		{
			BufferedImageReaderPool pool = entry.getValue();
			BigInteger archiveID = entry.getKey();
			
			if(pool.getLastUsageTime() < lastUsage)
			{
				lastUsage = pool.getLastUsageTime();
				leastBusy = archiveID;
			}
		}
		
		logger.warn("[ImageManager]:\t making room by removing least Busy "+leastBusy);
		BufferedImageReaderPool pool = cache.remove(leastBusy);
		pool.dispose();
	}
	
	/**
	 * Creates a pool with the image reader factory of the specified experiment
	 * @param expt the experiment under consideration
	 * @return the pool
	 * @throws DataAccessException
	 */
	public BufferedImageReaderPool createPool(BioExperiment expt) throws DataAccessException
	{
		//keep one for the UI thread
		int noOfCores = Runtime.getRuntime().availableProcessors() - 1;
		if(noOfCores == 0) noOfCores = 1;
		//maximum number of concurrent request on a single archive
		return new BufferedImageReaderPool(expt.getImageReaderFactory(), noOfCores);
	}
	
	/**
	 * Removes image readers for the specified experiment
	 * @param expt the experiment under consideration
	 */
	public synchronized void removeCache(BioExperiment expt)
	{
		if(cache.containsKey(expt.getMD5Signature()))
		{
			BufferedImageReaderPool pool = cache.remove(expt.getMD5Signature());
			pool.dispose();
		}
	}
	
	/**
	 * destroy the cache
	 */
	public synchronized void dispose()
	{
		for(Map.Entry<BigInteger, BufferedImageReaderPool> entry : cache.entrySet())
		{
			entry.getValue().dispose();
		}
		cache.clear();
		cache = null;
	}
	
	/**
	 * initialize the specified experiment within this cache and returns the number of series within the source files
	 * Called only during indexing
	 * @param expt
	 * @return
	 * @throws IOException
	 */
	public int initializeExperiment(BioExperiment expt) throws IOException
	{
		//create a pool of reads with the seed, do not add to the cache as of yet
		BufferedImageReaderPool pool = createPool( expt );
		//get one image reader from the pool, expensive & blocking call
		BufferedImageReader imageReader = pool.getImageReader();
		//figure out all companion source files
		expt.updateReference( imageReader.getUsedFiles() );
		//once the companion files are identified, the signature can be computed
		BigInteger archiveID = expt.getMD5Signature();
		
		synchronized(this)
		{
			makeRoom(); //make sure there is room from this new entry
			//add the pool to the cache of pools
			cache.put(archiveID, pool);
		}
		
		try
		{
			//find the number of series
			int numberOfSeries = imageReader.getSeriesCount();
			Logger.getRootLogger().info("[ImageManager]:\tTotal number of series is "+numberOfSeries +" in "+expt.getSeedFile());
			return numberOfSeries;
		}
		finally
		{
			//close the reader
			imageReader.close();
		}
	}
	
	/**
	 * Returns the image reader for readering image and/or data for the specified record
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	public BufferedImageReader getImageReader(BioExperiment expt) throws IOException
	{
		BufferedImageReaderPool pool = getPool( expt );
		return pool.getImageReader();
	}

	/**
	 * Returns the image reader for readering image and/or data for the specified record
	 * @param record
	 * @return
	 * @throws DataAccessException
	 */
	public BufferedImageReader getImageReader(BioRecord record) throws IOException
	{
		BufferedImageReaderPool pool = getPool( (BioExperiment) record.getExperiment() );
		return pool.getImageReader();
	}
	
	/**
	 * Returns the Pixel Data associated with the specified dimension of the specified record
	 * @param record the record under consideration
	 * @param imageCoordinate the dimension
	 * @return the raw pixel data
	 * @throws IOException
	 */
	public synchronized PixelArray getPixelArray(BioRecord record, Dimension imageCoordinate)  throws IOException
	{	
		BufferedImageReaderPool pool = getPool( (BioExperiment) record.getExperiment() );
		BufferedImageReader imageReader = pool.getImageReader();
		
		int seriesNo = record.getSite(imageCoordinate.siteNo).getSeriesNo();
		imageReader.setSeries( seriesNo );

		int index = imageReader.getIndex(imageCoordinate.sliceNo, imageCoordinate.channelNo, imageCoordinate.frameNo);
		BufferedImage image = null;
		try 
		{
			//the buffered imaged returned here if of unknown type
			image = imageReader.openImage(index);
		} 
		catch (Exception e) 
		{
			Logger.getRootLogger().warn("error while reading pixel data for "+imageCoordinate, e);
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
}
