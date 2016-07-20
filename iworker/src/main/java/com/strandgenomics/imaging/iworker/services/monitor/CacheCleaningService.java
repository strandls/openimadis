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

package com.strandgenomics.imaging.iworker.services.monitor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.iengine.worker.CacheCleaningServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * Monitor thread to monitor size of the cached/pre-fetched raw data.
 * 
 * 
 * @author Anup Kulkarni
 */
public class CacheCleaningService extends ServiceMonitor{

	/**
	 * monitor after every 30min
	 */
	public static final long MONITOR_INTERVAL = 30 * 60 * 1000;
	
	private CacheCleaningServiceStatus serviceStatus = null;
	
	private ServiceType serviceType = ServiceType.CACHE_CLEANING_SERVICE;
	
    /**
     * cache cleaning task
     */
    private CacheCleaningTask task = null;
	
	/**
	 * logger
	 */
	private Logger logger;
	
	/**
	 * monitor thread which will run after a fixed interval and monitor size of the cached/pre-fetched raw data
	 */
	public ScheduledThreadPoolExecutor monitor = null;
	
	public CacheCleaningService()
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.iworker.services");
		
		monitor = new ScheduledThreadPoolExecutor(1);
		
		serviceStatus = new CacheCleaningServiceStatus();
		
		task = new CacheCleaningTask();
		
		startServiceMonitor();
	}
	
	public void schedule()
	{
		logger.logp(Level.INFO, "ReindexService", "schedule", "Starting service");
		
		try
		{
			monitor.scheduleWithFixedDelay(task, 0, MONITOR_INTERVAL, TimeUnit.MILLISECONDS);
		}
		catch(Exception e)
		{
			logger.logp(Level.INFO, "ReindexService", "schedule", e.getMessage());
		}
	}

	@Override
	public ServiceStatus getServiceStatus() {
		
		serviceStatus.setCleaning(task.isCleaning());
		
		return serviceStatus;
	}

	@Override
	public ServiceType getServiceType() {
		return serviceType;
	}

	@Override
	public void restart() {
		
		if(monitor!=null){
			monitor.shutdownNow();
			monitor = new ScheduledThreadPoolExecutor(1);
			schedule();
		}
		
	}
}
