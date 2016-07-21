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

package com.strandgenomics.imaging.iworker.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.backup.BackupService;
import com.strandgenomics.imaging.iengine.backup.BackupServiceImpl;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TicketDAO;
import com.strandgenomics.imaging.iengine.export.ExportService;
import com.strandgenomics.imaging.iengine.export.ExportServiceImpl;
import com.strandgenomics.imaging.iengine.movie.MovieService;
import com.strandgenomics.imaging.iengine.movie.MovieServiceImpl;
import com.strandgenomics.imaging.iengine.search.ReindexService;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.ExtractionService;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.worker.ExecutorServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ExtractionServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;
import com.strandgenomics.imaging.iengine.zoom.TilingService;
import com.strandgenomics.imaging.iengine.zoom.TilingServiceImpl;
import com.strandgenomics.imaging.iengine.zoom.ZoomService;
import com.strandgenomics.imaging.iengine.zoom.ZoomServiceImpl;
import com.strandgenomics.imaging.iworker.services.monitor.CacheCleaningService;

/**
 * Maintains a pool of threads to generate records from its format file(s)
 * Once a request has been placed on this service, the request is serviced in the 
 * FIFO order and once served, the caller is notified
 */
public class ExtractionServiceImpl extends ServiceMonitor implements ExtractionService, ExtractionObserver {
	
	private static Object lock = new Object();
	private static ExtractionServiceImpl singleton = null;
	
	/**
	 * Returns the singleton instance of the ImagingService 
	 * @param <R>
	 * @return
	 */
	public static ExtractionServiceImpl getInstance()
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					Config.getInstance();
					int noOfCores = Constants.getRecordExtractorPoolSize();
					singleton = new ExtractionServiceImpl(noOfCores);
					
					//initialize the log4j logger
					try 
					{
						File log4jFile = new File(Constants.getLogDirectory(), "bio-formats.log"); 
						DailyRollingFileAppender rfa = new DailyRollingFileAppender(new PatternLayout(), log4jFile.getAbsolutePath(), "'.'yyyy-MM-dd");
						rfa.setAppend(true);
						
						org.apache.log4j.Logger.getRootLogger().addAppender(rfa);
						
						org.apache.log4j.Level logLevel = org.apache.log4j.Level.INFO;
						org.apache.log4j.Logger.getRootLogger().setLevel(logLevel);
					} 
					catch (IOException e) 
					{
						System.out.println("unable to initialize log4j logger used by bio-format library "+e);
					} 
				}
			}
		}
		
		return singleton;
	}
	
	/**
	 * the logger
	 */
	protected Logger logger;

    /**
     * thread pool to execute image generation concurrently
     */
    private ExecutorService[] executors = null;
    
    /**
     * to monitor the submitted tasks
     */
    private ExecutorServiceMonitor executorServiceMonitor = null;
    
    /**
     * status of extraction service
     */
    private ExtractionServiceStatus serviceStatus = null;
    
    private ServiceType serviceType = ServiceType.EXTRACTION_SERVICE;
    
    /**
     * creates an imaging service with the specified number of threads
     * @param noOfThreads 
     */
    private ExtractionServiceImpl(int noOfThreads)
    {
    	logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
    	
    	executors = new ExecutorService[noOfThreads];
    	//Creates a thread pool that reuses a fixed number of threads operating off a 
    	//shared unbounded queue. At any point, at most nThreads threads will be active 
    	//processing tasks. If additional tasks are submitted when all threads are active,
    	//they will wait in the queue until a thread is available. 
    	//If any thread terminates due to a failure during execution prior to shutdown, 
    	//a new one will take its place if needed to execute subsequent tasks. 
    	//The threads in the pool will exist until it is explicitly shutdown. 
    	for(int i = 0; i < noOfThreads; i++)
    	{
    		executors[i] = Executors.newFixedThreadPool(1);
    	}
    	
    	serviceStatus = new ExtractionServiceStatus();
    	
    	executorServiceMonitor = new ExecutorServiceMonitor();
    	
    	startServiceMonitor();
    }

    /**
     * shutd0own the service
     * @throws DataAccessException 
     */
    public void shutdown() throws DataAccessException
    {
    	stopServiceMonitor();
    	
    	executorServiceMonitor.stopExecutorServiceMonitor();
    	
        if(executors != null)
        {
        	for(int i = 0; i < executors.length; i++)
        	{
        		executors[i].shutdown();
        		executors[i] = null;
        	}
        }
        executors = null;
    }
    
	@Override
	public void update(ExtractionEvent e) 
	{
		logger.logp(Level.INFO, "ExtractionServiceImpl", "update", "updating with "+e);
		
		try
		{
			SysManagerFactory.getTicketManager().updateTicketStatus(e.ticketID, e.status);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ExtractionServiceImpl", "update", "error updating ticket "+e +", "+ex);
		}
	}
	
	@Override
	public boolean submitTask(long ticketID, RecordCreationRequest request) throws IOException 
	{
		System.out.println("[ExtractionServiceImpl]:\treceived task "+request +" for ticket "+ticketID);
		logger.logp(Level.INFO, "ExtractionServiceImpl", "submitTask", "submitting ticket("+ticketID +") for execuion "+request);
		submitRequest(ticketID, this, request);
		return true;
	}
    
    /**
     * Adds a request process the given archive tar-ball (containing the format files a multi-series records
     * @param observer the observer interested in the progress of such events
     * @param actor the user on whose behalf this is being done
     * @param projectName the project under which the records are to be added
     * @param archiveSignature MD5 hash of the source files (inside the tar)
     * @param tarBall the gzipped tar-ball containing the format files
     */
    public synchronized void submitRequest(long ticketID, ExtractionObserver observer, RecordCreationRequest context)
    {
    	logger.logp(Level.INFO, "ExtractionServiceImpl", "submitRequest", "submitting ticket("+ticketID +") for execuion "+context);
    	
    	ExtractionTask task = new ExtractionTask(ticketID, observer, context);
    	//report submission
		observer.update(new ExtractionEvent(ticketID, context.getArchiveHash(), Status.QUEUED));
    	//submit the task for execution
    	int executorNo = (int) Math.floor( Math.random() * executors.length ); 
    	executorNo = executorNo == executors.length ? executors.length - 1 : executorNo;
    	//actually we do not need this result, since task will communicate to the 
    	//observer directly the state of the processing as and when required
    	Future future = executors[executorNo].submit(task);
    	
    	executorServiceMonitor.addToExecutorServiceMonitor(future);
    	logger.logp(Level.INFO, "ExtractionServiceImpl", "submitRequest", "successfully submited ticket("+ticketID +") for execuion "+context);
    }
    

	@Override
	public ServiceStatus getServiceStatus() {
		
		serviceStatus.setRecordExtractionQueueSize(executorServiceMonitor.getExecutorServiceQueueSize());
		return serviceStatus;
	}

	@Override
	public ServiceType getServiceType() {
		return serviceType;
	}

	@Override
	public void restart() {
		
    	executorServiceMonitor.cleanExecutorServiceMonitor();
    	
        if(executors != null)
        {
        	for(int i = 0; i < executors.length; i++)
        	{
        		executors[i].shutdownNow();
        		executors[i] = Executors.newFixedThreadPool(1);
        	}
        }
	}
    
    /**
     * record extraction service
     */
    private static void recordExtractionService()
    {
    	try 
        {
        	//create the registry
            LocateRegistry.createRegistry(Constants.getRMIServicePort());  
            ExtractionServiceImpl serverObj = ExtractionServiceImpl.getInstance();
            
            ExtractionService stub = (ExtractionService) UnicastRemoteObject.exportObject(serverObj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(Constants.getRMIServicePort());
            registry.bind(ExtractionService.class.getCanonicalName(), stub);

            System.out.println("ExtractionService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * solr reindexing service
     */
    private static void solrReindexingService()
    {
    	try 
        {
    		ReindexService service = new ReindexService();
    		service.schedule();

            System.out.println("ReindexingService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("ReindexingService exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static void cacheCleaningService()
	{
    	try 
        {
    		CacheCleaningService service = new CacheCleaningService();
    		service.schedule();

            System.out.println("CacheCleaning initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("CacheCleaning exception: " + e.toString());
            e.printStackTrace();
        }
	}
    
    /**
     * movie service
     */
    private static void recordMovieService()
    {
    	try 
        {
        	//create the registry
            LocateRegistry.createRegistry(Constants.getMovieServicePort());  
            MovieServiceImpl serverObj = MovieServiceImpl.getInstance();
            
            MovieService stub = (MovieService)UnicastRemoteObject.exportObject(serverObj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(Constants.getMovieServicePort());
            registry.bind(MovieService.class.getCanonicalName(), stub);

            System.out.println("MovieService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static void recordExportService()
    {
    	try 
        {
        	//create the registry
            LocateRegistry.createRegistry(Constants.getExportServicePort());  
            ExportServiceImpl serverObj = ExportServiceImpl.getInstance();
            
            ExportService stub = (ExportService)UnicastRemoteObject.exportObject(serverObj, Constants.getExportServicePort());

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(Constants.getExportServicePort());
            registry.bind(ExportService.class.getCanonicalName(), stub);

            System.out.println("ExportService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private static void projectBackupService()
	{
    	try 
        {
        	//create the registry
            LocateRegistry.createRegistry(Constants.getBackupServicePort());  
            BackupServiceImpl serverObj = BackupServiceImpl.getInstance();
            
            BackupService stub = (BackupService)UnicastRemoteObject.exportObject(serverObj, Constants.getBackupServicePort());

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(Constants.getBackupServicePort());
            registry.bind(BackupService.class.getCanonicalName(), stub);

            System.out.println("BackupService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}
    
    /**
     * tiling service
     */
    private static void imageTilingService()
    {
    	try 
        {
        	//create the registry
            LocateRegistry.createRegistry(Constants.getZoomServicePort());  
            TilingServiceImpl serverObj = TilingServiceImpl.getInstance();
            
            TilingService stub = (TilingService)UnicastRemoteObject.exportObject(serverObj, Constants.getZoomServicePort());

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(Constants.getZoomServicePort());
            registry.bind(TilingService.class.getCanonicalName(), stub);

            System.out.println("TilingService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) throws IOException {

    	if(args != null && args.length > 0)
    	{
    		File f = new File(args[0]);
    		if(f.isFile())
    		{
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.load(inStream);
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
    	
    	System.out.println("\n\n............\n\n");
    	
    	// start record extraction service
    	recordExtractionService();
    	
    	// start solr reindexing service
    	solrReindexingService();
    	
    	// start movie service
    	recordMovieService();
    	
    	// start zoom service
    	imageTilingService();
    	
    	// start export service
    	recordExportService();
    	
    	// start project backup service
    	projectBackupService();
    	
    	// start cache cleaning service
    	cacheCleaningService();
    }
}
