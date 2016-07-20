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

package com.strandgenomics.imaging.iengine.system;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.bioformats.BufferedImageReaderPool;
import com.strandgenomics.imaging.icore.bioformats.DefaultImageReaderFactory;
import com.strandgenomics.imaging.icore.bioformats.ImageReaderException;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;

/**
 * manager class for managing ImageReaders
 * 
 * @author Anup Kulkarni
 */
public class ImageReadersManager extends SystemManager{

	/**
	 * a pool of image readers per Archive
	 */
	protected Map<BigInteger, BufferedImageReaderPool> cache = null;
	/**
	 * movie creator service
	 */
	private ExecutorService imageReaderInitializers = null;
	/**
	 * maximum number of archives to serve concurrently
	 */
	protected int maxReaders = 100;
	
	private Map<Long, ImagerReaderInitializer> idToInitMap;
	
	ImageReadersManager() 
	{
		cache = new ConcurrentHashMap<BigInteger, BufferedImageReaderPool>();
		
		maxReaders = Constants.getMaxReaders();
		
		int nThread = Runtime.getRuntime().availableProcessors() - 1;
//		int nThread = 500;
		
		idToInitMap = new HashMap<Long, ImagerReaderInitializer>();
		
		imageReaderInitializers = Executors.newFixedThreadPool(nThread);
	}
	
	/**
	 * Initialises image reader for specified record and dimension
	 * @param archiveSignature specified archive
	 * @param dim specified dimension
	 * @throws ImageReaderException 
	 * @throws IOException 
	 */
	public long initializeReader(String actorLogin, long guid, Dimension dim) throws ImageReaderException, IOException
	{
		logger.logp(Level.INFO, "ImageReadersManager", "initializeReader", "initializing reader for record="+guid);
		
		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
		Project project = SysManagerFactory.getProjectManager().getProjectForRecord(actorLogin, guid);
		
		if (project.getStatus() != ProjectStatus.Active)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_IS_ARCHIVED));
		}
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		BufferedImageReaderPool pool = getPool(record.archiveSignature);
		
		long id = System.nanoTime();
		
		// start initialising the server in separate thread
		ImagerReaderInitializer init = new ImagerReaderInitializer(pool, dim, id, record.imageWidth, record.imageWidth);
		
		idToInitMap.put(id, init);
		imageReaderInitializers.submit(init);
		
		return id;
	}
	
	/**
	 * cancel the reader
	 * @param requestId
	 */
	public void cancelRequest(long requestId)
	{
		logger.logp(Level.INFO, "ImageReadersManager", "cancelling request", "initializing reader for record="+requestId);
		
		if(idToInitMap.containsKey(requestId))
		{
			ImagerReaderInitializer init = idToInitMap.get(requestId);
			init.interrupt();
		}
	}
	
	/**
	 * returns true if image reader for specified archive is available
	 * @param archiveSignature specified archive signature
	 * @return true if image reader for specified archive is available; false otherwise
	 */
	public boolean isReaderAvailable(BigInteger archiveSignature)
	{
		synchronized (cache)
		{
			if(cache.containsKey(archiveSignature))
			{
				BufferedImageReaderPool pool = cache.get(archiveSignature);
				
				if(pool.idleSize()>0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the pool of image readers ,maintained for the specified archive
	 * @param archiveSignature the archive under consideration
	 * @return the existing or a newly create pool
	 * @throws ImageReaderException 
	 * @throws IOException 
	 */
	public BufferedImageReaderPool getPool(BigInteger archiveSignature) throws ImageReaderException, IOException
	{
		BufferedImageReaderPool pool = null;
		
		synchronized (cache)
		{
			if(cache.containsKey(archiveSignature))
			{
				pool = cache.get(archiveSignature);
				if(pool.idleSize()<=0)// if there is no idle see if this can be updated
				{
					if(getTotalReaderCount() < maxReaders)
					{
						// make room in the reader pool
						pool.updateMaxSize(pool.getMaxSize()+1);
					}
					else
					{
						throw new ImageReaderException("Image Reader Unavailable");
					}
				}
				
				return pool;
			}
		}
		
		logger.logp(Level.INFO, "ImageManager", "getPool", "making room... sign="+archiveSignature);
		makeRoom(); //make sure there is room for this new entry
		logger.logp(Level.INFO, "ImageManager", "getPool", "creating pool... sign="+archiveSignature);
		pool = createPool(archiveSignature);
		logger.logp(Level.INFO, "ImageManager", "getPool", "done creating pool... sign="+archiveSignature);
		
		synchronized (cache)
		{
			cache.put(archiveSignature, pool);
		}
		
		return pool;
	}
	
	private int getTotalReaderCount()
	{
		int cnt = 0;
		for(Entry<BigInteger, BufferedImageReaderPool> pools:cache.entrySet())
		{
			BufferedImageReaderPool pool = pools.getValue();
			cnt += pool.getCount();
		}		
		
		logger.logp(Level.INFO, "ImageManager", "getTotalReaderCount", " reader count="+cnt);	
		
		return cnt;
	}
	
	/**
	 * removes specified signature from buffered readers cache
	 * @param sign
	 */
	public void removeFromCache(BigInteger sign)
	{
		synchronized (cache)
		{
			BufferedImageReaderPool pool = cache.remove(sign);
			if(pool != null)
				pool.dispose();
		}
	}
	
	/**
	 * make room in the pool
	 * @throws IOException 
	 */
	private void makeRoom() throws ImageReaderException, IOException
	{
		//make room if there are too many
		synchronized (cache)
		{
			if(getTotalReaderCount() < maxReaders)
			{
				return;
			}
		}
		logger.logp(Level.INFO, "ImageManager", "makeRoom", "cache is full...");
		
		BigInteger leastBusy = null;
		long lastUsage = Long.MAX_VALUE;
		
		//see if there is an idle guy, delete that last used idle pool
		Set<Entry<BigInteger, BufferedImageReaderPool>> entrySet = new HashSet<Map.Entry<BigInteger,BufferedImageReaderPool>>();
		synchronized (cache)
		{
			entrySet = cache.entrySet();
		}
		
		// get least recently used pool
		for(Map.Entry<BigInteger, BufferedImageReaderPool> entry : entrySet)
		{
			BufferedImageReaderPool pool = entry.getValue();
			BigInteger archiveID = entry.getKey();
			
			logger.logp(Level.INFO, "ImageManager", "makeRoom", "checking candidature of "+archiveID);
			if(pool.getLastUsageTime() < lastUsage && pool.idleSize()>0)
			{// last usage time of the pool is current least and pool has atleast one idle reader which can be removed
				leastBusy = archiveID;
				lastUsage = pool.getLastUsageTime();
			}
		}
		
		if(leastBusy != null)
		{
			logger.logp(Level.INFO, "ImageManager", "makeRoom", "making room by removing the oldest idle "+leastBusy);
			
			long stime = System.currentTimeMillis();
			BufferedImageReaderPool pool = null;
			synchronized (cache)
			{
				pool = cache.get(leastBusy);
			}
			// remove least recently used reader
			pool.removeLeastRecentlyUsedReader();
			if(pool.isIdle())
			{
				pool.dispose();
				cache.remove(leastBusy);
			}
			logger.logp(Level.INFO, "ImageManager", "makeRoom", "removed least busy in "+(System.currentTimeMillis()-stime)+"ms");
			
			return;
		}
			
		//nobody is idle, throw server busy exception
		throw new ImageReaderException("Image Reader Unavailable");
	}

	private BufferedImageReaderPool createPool(BigInteger archiveSignature) throws DataAccessException
	{
		logger.logp(Level.INFO, "ImageManager", "createPool", "getting file root ");
		File root = SysManagerFactory.getStorageManager().getStorageRoot(archiveSignature);
		logger.logp(Level.INFO, "ImageManager", "createPool", "got file root ");
		//maximum number of concurrent request on a single archive

		// get seed file
		File seedFile = root.listFiles()[0];
		for(File file:root.listFiles())
		{
			String filename = file.getName().toLowerCase();
			if (!(filename.endsWith(".tif") || filename.endsWith(".tiff") || filename.endsWith(".xml")))
			{
				seedFile = file;
				break;
			}
		}
		
		logger.logp(Level.INFO, "ImageManager", "getPool", "creating BufferedImageReader pool...");
		return new BufferedImageReaderPool(new DefaultImageReaderFactory(seedFile));
	}
	
	/**
	 * Initialises new image reader in separate thread
	 * 
	 * @author Anup Kulkarni
	 */
	private class ImagerReaderInitializer extends Thread
	{
		/**
		 * the buffered image reader
		 */
		private BufferedImageReaderPool pool;
		/**
		 * dimension of the image
		 */
		private Dimension dim;
		/**
		 * id for this initializer
		 */
		private long id;
		/**
		 * 
		 */
		private int width;
		/**
		 * 
		 */
		private int height; 
		
		public ImagerReaderInitializer(BufferedImageReaderPool pool, Dimension dim, long id, int w, int h)
		{
			this.pool = pool;
			this.dim = dim;
			this.id = id;
			this.width = w;
			this.height = h;
		}
		
		@Override
		public void run()
		{
			try
			{
				BufferedImageReader imageReader = pool.getImageReader();
				imageReader.setSeries(0);
				int index = imageReader.getIndex(dim.sliceNo, dim.channelNo, dim.frameNo);
				
				if(width>Constants.MAX_TILE_WIDTH || height > Constants.MAX_TILE_HEIGHT)
					imageReader.openImage(index, 0, 0, Constants.MAX_TILE_WIDTH, Constants.MAX_TILE_HEIGHT);
				else
					imageReader.openImage(index);

				imageReader.close();
				
				idToInitMap.remove(id);
				
				return;
			}
			catch (IOException e)
			{
				logger.logp(Level.WARNING, "ImagerReaderInitializer", "run", "error initializing image reader ",e);
				return;
			}
			catch (FormatException e)
			{
				logger.logp(Level.WARNING, "ImagerReaderInitializer", "run", "error initializing image reader ",e);
				return;
			}
			catch (ImageReaderException e)
			{
				logger.logp(Level.WARNING, "ImagerReaderInitializer", "run", "error initializing image reader ",e);
				return;
			}
			catch (Exception e)
			{
				logger.logp(Level.WARNING, "ImagerReaderInitializer", "run", "thread interupted "+id,e);
				return;
			}
		}
	}
}
