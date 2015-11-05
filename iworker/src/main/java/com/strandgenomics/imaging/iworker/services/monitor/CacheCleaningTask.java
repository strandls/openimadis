package com.strandgenomics.imaging.iworker.services.monitor;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Task to monitor raw image cache data directory and clean the LRU cached data
 * 
 * @author Anup Kulkarni
 */
public class CacheCleaningTask implements Runnable {
	
	/**
	 * size of the image cache
	 */
	private static final long imageCacheSize = 10 * 1024 * 1024 * 1024L;
	
	/**
	 * time out of the cache
	 */
	private static final long CACHE_TIMEOUT = 24 * 60 * 60 * 1000;
	
	/**
	 * hitting upper threashold will trigger cleaning task
	 */
	private static final double UPPER_THRESHOLD = 0.8;
	
    private static Object padLock = new Object();
    
    /**
     * to keep track of start and end of cleaning up process 
     */
    private boolean isCleaning = false;
	
	/**
	 * logger
	 */
	private Logger logger;
	
	public CacheCleaningTask()
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.iworker.services");
	}

	@Override
	public void run()
	{
		synchronized (padLock) {
			isCleaning = true;
		}
		
		System.out.println("cleaning task started");
		String cacheStoreDir = Constants.getStringProperty(Property.IMAGE_CACHE_STORAGE_LOCATION, null);
		
		File cacheStorageLocation = new File(cacheStoreDir);
		cacheStorageLocation.mkdirs();
		
		long currentSize = Util.calculateSize(cacheStorageLocation);

		if (currentSize >= (imageCacheSize * UPPER_THRESHOLD))
		{
			// current size of the image cache directory is close to finishing
			// so remove LRU requests
			removeLRURequests();
			
			//recalculate the size of the directory
			currentSize = Util.calculateSize(cacheStorageLocation);
		}
		
		// make this computed value available to rest of the nodes
		SysManagerFactory.getCacheManager().set(new CacheKey("RawDataMonitoring", CacheKeyType.Misc), currentSize);
		System.out.println("cleaning task finished "+currentSize);
	
		synchronized (padLock) {
			isCleaning = false;
		}
	}
	
	private void removeLRURequests()
	{
		Map<Long, Long> records;
		try
		{
			records = DBImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().getLRURecords();
			long currentTime = System.currentTimeMillis();
			for(Entry<Long,Long> record:records.entrySet())
			{
				long guid = record.getKey();
				long lastAccessTime = record.getValue();
				
				if(lastAccessTime + CACHE_TIMEOUT < currentTime)
					deleteUserCachedData(guid);
			}
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "deleteCachedData", "unable to delete cache", e);
		}
	}
	
	/**
	 * delete only user cached data. keep first frame/slice as representative image of the record.
	 * @param guid
	 */
	private void deleteUserCachedData(long guid)
	{
		try
		{
			ImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().deleteUserCachedData(guid);
			
			String cacheStoreDir = Constants.getStringProperty(Property.IMAGE_CACHE_STORAGE_LOCATION, null);

			File recordCache = new File(cacheStoreDir, "RecordID_"+guid);
			if(recordCache.isDirectory())
			{
				File[] files = recordCache.listFiles();
				for(File file:files)
				{
					String[] values = file.getName().split("_");
					if(!(Integer.parseInt(values[0])==0 && Integer.parseInt(values[1])==0 && Integer.parseInt(values[3])==0))
					{
						file.delete();
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "deleteCachedData", "unable to delete cache", e);
		}
	}
	
	/**
	 * check if cleaning up
	 * @return
	 */
	public boolean isCleaning(){
		synchronized (padLock) {
			return isCleaning;
		}
	}

}
