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

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.worker.ExecutorServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;
import com.strandgenomics.imaging.iengine.worker.TilingServiceStatus;

/**
 * executor service to create tiles
 * @author navneet
 *
 */
public class TilingServiceImpl extends ServiceMonitor implements TilingService {
	
	private static Object lock = new Object();
	
	/**
	 * executor service which will execute tiling service
	 */
	private ExecutorService tilingService;
	
	/**
	 * monitor for tiling service
	 */
	private ExecutorServiceMonitor tilingServiceMonitor;
	
	private TilingServiceStatus serviceStatus;
	
	private ServiceType serviceType = ServiceType.TILING_SERVICE;
	/**
	 * 
	 */
	private static TilingServiceImpl service;
	
	/**
	 * no of threads in executor service
	 */
	int nThreads = 1;
	
	public TilingServiceImpl() {
		
		tilingService = Executors.newFixedThreadPool(nThreads);
		
		serviceStatus = new TilingServiceStatus();
		
		tilingServiceMonitor = new ExecutorServiceMonitor();
		
		startServiceMonitor();
	}
	
	/**
	 * returns singleton instance of TileFetchingService
	 * @param nThreads no of tasks that can be executed parallely 
	 * @return singleton instance of TileFetchingService
	 */
	public static TilingServiceImpl getInstance()
	{
		if(service == null)
		{
			synchronized (lock) {
				
				if(service == null){
					
					service = new TilingServiceImpl();
				}
			}
			
		}
		return service;
	}
	
	@Override
	public void submitTilingTask(ImageTilingTask task) throws RemoteException{
		
		Future future = tilingService.submit(task);
		tilingServiceMonitor.addToExecutorServiceMonitor(future);
	}

	@Override
	public ServiceStatus getServiceStatus() {
		serviceStatus.setTilingServiceQueueSize(tilingServiceMonitor.getExecutorServiceQueueSize());
		return serviceStatus;
	}

	@Override
	public ServiceType getServiceType() {
		return serviceType;
	}

	@Override
	public void restart() {
		
		tilingServiceMonitor.cleanExecutorServiceMonitor();
		
		if(tilingService!=null){
			tilingService.shutdownNow();
			tilingService = Executors.newFixedThreadPool(nThreads);
		}
		
	}	
	
	/**
	 * shutdown tiling service
	 * @throws DataAccessException
	 */
	public void shutdown() throws DataAccessException{
		
		stopServiceMonitor();
		
		tilingServiceMonitor.cleanExecutorServiceMonitor();
		
		if(tilingService!=null){
			tilingService.shutdownNow();
			tilingService = null;
		}
	}
}
