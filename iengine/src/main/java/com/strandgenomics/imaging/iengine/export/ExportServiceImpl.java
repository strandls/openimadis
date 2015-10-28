package com.strandgenomics.imaging.iengine.export;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.worker.ExecutorServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ExportServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;
/**
 * Implementation of export service
 * 
 * @author Anup Kulkarni
 */
public class ExportServiceImpl extends ServiceMonitor implements ExportService 
{
	private static Object lock = new Object();
	
	private static ExportServiceImpl singleton = null;
	
	private Logger logger;
	
	/**
	 * The threshold value of Record size is used to allocate Export task to specific Executor Service
	 */
	private long threshold;
	
	/**
	 * record exporter service for handling heavier size file requests
	 */
	private ExecutorService recordExporterHeavy = null;
	/**
	 * monitor for heavier record exporter service
	 */
	private ExecutorServiceMonitor recordExporterMonitorHeavy = null;
	/**
	 * record exporter service for handling small size file requests
	 */
	private ExecutorService recordExporterLight = null;
	/**
	 * monitor for lighter record exporter service
	 */
	private ExecutorServiceMonitor recordExporterMonitorLight = null;
	
	private ExportServiceStatus serviceStatus = null;
	
	private ServiceType serviceType = ServiceType.EXPORT_SERVICE;
	
	private ExportServiceImpl()
	{
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		recordExporterHeavy = Executors.newFixedThreadPool(1);
		
		recordExporterLight = Executors.newFixedThreadPool(1);
		
		recordExporterMonitorHeavy = new ExecutorServiceMonitor();
		
		recordExporterMonitorLight = new ExecutorServiceMonitor();
		
		threshold = 100 * 1024 * 1024;
		
		serviceStatus = new ExportServiceStatus();
		
		startServiceMonitor();
	}
	
	/**
	 * returns the singleton instance of export service
	 * @return the singleton instance of export service
	 */
	public static ExportServiceImpl getInstance()
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					Config.getInstance();
					singleton = new ExportServiceImpl();
				}
			}
		}
		
		return singleton;
	}
	
	@Override
	public synchronized void submitExportRequest(ExportRequest request) throws IOException
	{
		logger.logp(Level.INFO, "ExportServiceImpl", "submitExtractionRequest", "new export request "+request);
		logger.logp(Level.INFO, "ExportServiceImpl", "submitExtractionRequest", "new export request "+request.size);
		
		RecordExportTask exportTask = new RecordExportTask(request.requestId, request.submittedBy, request.getRecordIds(), request.format, request.validTill, request.name);
		
		//  If request size is greater than threshold, then Heavy Executor Service handles export task.
		if(request.size > threshold){
			logger.logp(Level.INFO, "ExportServiceImpl", "submitExtractionRequest", "new export request size > threshold:"+threshold);
			submitTasktoHeavy(exportTask);
		}
		//
		//  If request size is less than or equal to threshold, then one of the following actions is performed.
		//  1. If Light Executor Service is currently not handling any tasks, 
		//            then export task is submitted to Light Executor Service
		//  2. If Heavy Executor Service is currently not handling any tasks,
		//            then export task is submitted to Heavy Executor Service
		//  3. If conditions 1 and 2 doesn't apply,
		//            then export task is randomly submitted to Heavy or Light Executor Service
		//
		else{
			logger.logp(Level.INFO, "ExportServiceImpl", "submitExtractionRequest", "new export request size <= threshold:"+threshold);
			
			if(recordExporterMonitorLight.getExecutorServiceQueueSize() == 0)
				submitTasktoLight(exportTask);
			
			else if(recordExporterMonitorHeavy.getExecutorServiceQueueSize() == 0)
				submitTasktoHeavy(exportTask);
			
			else{
				submitTasktoLight(exportTask);
			}
		}
	}
	
	/*
	 * This Method Submits RecordExportTask to Executor Service (heavier) handling records of size > threshold
	 */
	private void submitTasktoHeavy(RecordExportTask exportTask){
		logger.logp(Level.INFO, "ExportServiceImpl", "submitTasktoHeavy", "new export request: Heavy Executor Service Called");
		Future future = recordExporterHeavy.submit(exportTask);
		recordExporterMonitorHeavy.addToExecutorServiceMonitor(future);
	}
	
	/*
	 * This Method Submits RecordExportTask to Executor Service (lighter) handling records of size <= threshold
	 */
	private void submitTasktoLight(RecordExportTask exportTask){
		logger.logp(Level.INFO, "ExportServiceImpl", "submitTasktoLight", "new export request: Light Executor Service Called");
		Future future = recordExporterLight.submit(exportTask);
		recordExporterMonitorLight.addToExecutorServiceMonitor(future);
	}

	public void shutdown() throws DataAccessException{
		
		stopServiceMonitor();
		
		recordExporterMonitorHeavy.stopExecutorServiceMonitor();
		
		recordExporterMonitorLight.stopExecutorServiceMonitor();
		
		if(recordExporterHeavy!=null){
			recordExporterHeavy.shutdownNow();
		}
		
		if(recordExporterLight!=null){
			recordExporterLight.shutdownNow();
		}
	}

	@Override
	public ServiceStatus getServiceStatus() {
		
		serviceStatus.setExportQueueSize(recordExporterMonitorHeavy.getExecutorServiceQueueSize()+recordExporterMonitorLight.getExecutorServiceQueueSize());
		return serviceStatus;
	}
	
	@Override
	public ServiceType getServiceType() {
		return serviceType;
	}

	@Override
	public void restart() {
		
		if(recordExporterHeavy!=null){
			recordExporterHeavy.shutdownNow();
			recordExporterHeavy = Executors.newFixedThreadPool(1);
		}
		
		if(recordExporterLight!=null){
			recordExporterLight.shutdownNow();
			recordExporterLight = Executors.newFixedThreadPool(1);
		}
		
		recordExporterMonitorHeavy.cleanExecutorServiceMonitor();
		recordExporterMonitorLight.cleanExecutorServiceMonitor();
	}
}
