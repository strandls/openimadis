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

package com.strandgenomics.imaging.iengine.zoom;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.iengine.system.Config;

/**
 * service for prefetching zoom related images
 * 
 * @author Anup Kulkarni
 */
public class ZoomServiceImpl implements ZoomService {
	
	private static Object lock = new Object();
	
	private static ZoomServiceImpl singleton = null;
	
	private Map<ZoomRequest, ZoomRequest> zoomRequestMap = null;
	
	private Logger logger;
	
	/**
	 * movie creator service
	 */
	private ExecutorService zoomPrefetchers = null;
	
	private ZoomServiceImpl(int noOfThreads)
	{ 
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		zoomRequestMap = new HashMap<ZoomRequest, ZoomRequest>();

		zoomPrefetchers = Executors.newFixedThreadPool(noOfThreads);
	}
	
	/**
	 * returns the singleton instance of movie service
	 * @return the singleton instance of movie service
	 */
	public static ZoomServiceImpl getInstance()
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					Config.getInstance();
					int noOfCores = Constants.getRecordExtractorPoolSize();
					singleton = new ZoomServiceImpl(noOfCores);
				}
			}
		}
		
		return singleton;
	}
	
	/**
     * shutd0own the service
     */
    public void shutdown()
    {
    	if(zoomPrefetchers != null)
        {
    		zoomPrefetchers.shutdown();
    		zoomPrefetchers = null;
        }
    	zoomPrefetchers = null;
    }

	@Override
	public synchronized void submitZoomRequest(String actor, long requestID, ZoomDimenstion zDim, int zoomLevel, int xTile, int yTile) throws RemoteException
	{
		if(zDim == null)
			throw new IllegalArgumentException("Zoom Dimension cannot be null");
		
		ZoomRequest zr = new ZoomRequest(requestID, zDim, zoomLevel);
		
		if(!zoomRequestMap.containsKey(zr))
		{
			logger.logp(Level.INFO, "ZoomServiceImpl", "submitZoomRequest", "new zoom request "+zr);
			System.out.println("new request "+zoomRequestMap.size());
			zoomRequestMap.put(zr, zr);
		
			ZoomRequest zoomWorker = zoomRequestMap.get(zr);
			
			ZoomPrefetcherTask zoomTask = new ZoomPrefetcherTask(actor, zoomWorker, xTile, yTile);
			zoomPrefetchers.submit(zoomTask);
		}
		
		logger.logp(Level.INFO, "ZoomServiceImpl", "submitZoomRequest", " submitted request "+zr);
	}
	
	@Override
	public void removeZoomRequest(long requestID) throws RemoteException
	{
		Set<ZoomRequest> toDelete = new HashSet<ZoomRequest>();
		for(ZoomRequest zr:zoomRequestMap.keySet())
		{
			if(zr.requestID == requestID)
				toDelete.add(zr);
		}
		
		for(ZoomRequest zr:toDelete)
		{
			// remove from cache
			zoomRequestMap.remove(zr);
		}
	}
	
	public static void main(String... args) throws IOException, InterruptedException 
	{}
}
