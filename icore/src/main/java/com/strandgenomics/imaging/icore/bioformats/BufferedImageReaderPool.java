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
 * BufferedImageReaderPool.java
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

import java.io.IOException;
import java.util.Stack;

import loci.formats.gui.BufferedImageReader;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Disposable;

/**
 * Maintains a pool of BufferedImageReader
 * @author arunabha
 *
 */
public class BufferedImageReaderPool implements Disposable {
	
	/**
	 * handle to the image reader
	 */
	protected Stack<WrappedBufferedImageReader> imageReaders;
	/**
	 * the generator that will generate the BufferedImageReaders
	 */
	protected ImageReaderFactory generator = null;
	/**
	 * maximum number of readers to maintain
	 */
	protected int maxSize;
	/**
	 * number of readers created
	 */
	private int activeReaders = 0;
	/**
	 * the time when a reader in this pool was last used (asked for)
	 */
	private long lastUsageTime = 0;
	
	/**
	 * Creates a Pool of BufferedImageReader that is generated from the specified pool
	 * @param seedFile the relevant seed file from which the actual BufferedImageReader will be created
	 * @param maxSize maximum number of readers to maintain
	 */
	public BufferedImageReaderPool(ImageReaderFactory generator)
	{
		this.generator = generator;
		this.imageReaders = new Stack<WrappedBufferedImageReader>();
		
		this.maxSize = Constants.getBufferedReaderStackSize();
		this.activeReaders = 0;
	}
	
	/**
	 * Creates a Pool of BufferedImageReader that is generated from the specified pool
	 * @param seedFile the relevant seed file from which the actual BufferedImageReader will be created
	 * @param maxSize maximum number of readers to maintain
	 */
	public BufferedImageReaderPool(ImageReaderFactory generator, int maxSize)
	{
		this.generator = generator;
		this.imageReaders = new Stack<WrappedBufferedImageReader>();
		this.maxSize = maxSize <= 4 ? 4 : maxSize;
		this.activeReaders = 0;
	}
	
	/**
	 * update the max size of the pool
	 * @param maxSize
	 */
	public synchronized void updateMaxSize(int maxSize)
	{
		this.maxSize = maxSize;
	}
	
	/**
	 * returns max number of readers for this pool
	 * @return max number of readers for this pool
	 */
	public int getMaxSize()
	{
		return this.maxSize;
	}
	
	/**
	 * Returns the current idle size of the pool
	 * @return the current idle size of the pool
	 */
	public int idleSize()
	{
		return imageReaders.size();
	}
	
	/**
	 * Returns the number of active readers in the pool
	 * @return Number of active readers in the pool
	 */
	public int getCount()
	{
		return activeReaders;
	}
	
	/**
	 * Checks if all the readers created in this pool are back in the pool
	 * @return true if all the readers are back in the pool, false otherwise
	 */
	public synchronized boolean isIdle()
	{
		return activeReaders == imageReaders.size();
	}
	
	/**
	 * Returns the time when a reader in this pool was last used
	 * @return the time when a reader in this pool was last used
	 */
	public long getLastUsageTime()
	{
		return lastUsageTime;
	}
	
	/**
	 * closes this pool, closes all member BufferedImageReader instances
	 */
	@Override
	public synchronized void dispose()
	{
		for (WrappedBufferedImageReader reader : imageReaders)
		{
			try
			{
				reader.actualReader.close();
			}
			catch (Exception ex)
			{}
		}

		imageReaders.clear();
		imageReaders = null;
		//activeReaders = 0;
		
	}
	
	/**
	 * removes the last recently used image reader from the pool
	 * recomputes the lastUsageTime for the pool
	 * @throws IOException 
	 */
	public synchronized void removeLeastRecentlyUsedReader() throws IOException
	{
		if(imageReaders == null || imageReaders.empty()) 
			return ;
		
		long lastUsageTime = Long.MAX_VALUE;
		WrappedBufferedImageReader lastUsedReader = null;
		for(WrappedBufferedImageReader reader :imageReaders)
		{
			if(reader.getLastUsageTime()<lastUsageTime)
			{
				lastUsedReader = reader;
				lastUsageTime = reader.getLastUsageTime();
			}
		}
		
		if(lastUsedReader!=null)
		{
			// remove reader from the cache
			imageReaders.remove(lastUsedReader);
			
			// close the actual reader
			lastUsedReader.actualReader.close();
			
			// reduce the active reader count
			activeReaders--;
			
			// recompute the last usage time for the pool
			computeLastUsageTime();
		}
	}
	
	/**
	 * compute last usage time for the pool
	 */
	private void computeLastUsageTime()
	{
		if(imageReaders == null || imageReaders.empty())
		{
			this.lastUsageTime = Long.MIN_VALUE;
			return;
		}
		
		long lastUsageTime = Long.MAX_VALUE;
		for(WrappedBufferedImageReader reader :imageReaders)
		{
			if(reader.getLastUsageTime()<lastUsageTime)
			{
				lastUsageTime = reader.getLastUsageTime();
			}
		}
		
		this.lastUsageTime = lastUsageTime;
	}

	/**
	 * Returns the common image reader for underlying  records 
	 * @return  the common image reader for underlying  records 
	 */
	public synchronized BufferedImageReader getImageReader()
	{
		System.out.println("[BufferedImageReaderPool]:\tGetting ImageReader...");
		
		if(imageReaders == null) 
		{
			return null;
		}
		
		lastUsageTime = System.currentTimeMillis();

		if(imageReaders.empty())
		{
			if(activeReaders == maxSize)
			{
				throw new ImageReaderException("Image Reader Unavailable");
			}
			else
			{
				Logger.getRootLogger().info("Trying to create bioformat image reader from "+generator);
				activeReaders++;
				BufferedImageReader reader = generator.createBufferedImageReader();
				
				if(reader != null) //in case of exceptions, the reader will be null
				{
					WrappedBufferedImageReader wreader = new WrappedBufferedImageReader(this, reader);
					wreader.setLastUsageTime(lastUsageTime); // set the last usage time for the reader
					
					Logger.getRootLogger().info("Successfully created bioformat image reader "+wreader.ID +" from "+generator);
					return wreader;
				}
				else
				{
					activeReaders--;
					return null;
				}
			}
		}
		else
		{
			return imageReaders.pop();
		}
	}

	/**
	 * the close call on a WrappedBufferedImageReader will return to its parent pool
	 * @param wrappedBufferedImageReader
	 */
	synchronized void returnReader(WrappedBufferedImageReader reader) 
	{
		if(imageReaders == null) //if already disposed off
		{
			try 
			{
				reader.actualReader.close();
			} 
			catch (IOException e)
			{}
		}
		else
		{
			
			if(reader.actualReader.getUsedFiles().length > 5){			// hack to fix .nd file reading that keeps too many file handles open
				//dispose();
				try {
					reader.actualReader.close();
					BufferedImageReader tempreader = generator.createBufferedImageReader();
					
					if(tempreader != null) //in case of exceptions, the reader will be null
					{
						WrappedBufferedImageReader wreader = new WrappedBufferedImageReader(this, tempreader);
						wreader.setLastUsageTime(lastUsageTime); // set the last usage time for the reader
						
						Logger.getRootLogger().info("Successfully created bioformat image reader "+wreader.ID +" from "+generator);
						reader =  wreader;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			imageReaders.add(reader);
				
		}
	}
}
