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
