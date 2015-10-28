package com.strandgenomics.imaging.iengine.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.worker.MovieServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * class handles movie requests. this is the implementation where movie is
 * assumed to be played as series of images. This will not create actual video
 * but only prefetches all the images of the record. Every movie request is supposed to have a
 * request id. this service prefetches all the images required to create movie
 * for every request. every request have some timeout after which all prefetched
 * images are discared.
 * 
 * @author Anup Kulkarni
 */
public class MovieServiceImpl extends ServiceMonitor implements MovieService{
	
	private static Object lock = new Object();
	
	private static MovieServiceImpl singleton = null;
	
	private Map<Long, MovieTicket> currentMovies; 
	
	private Map<CacheRequestIdentifier, CacheRequest> currentCacheRequests;
	
	/**
	 * movie creator service
	 */
	private ExecutorService movieCreators = null;
	/**
	 * image prefetching service
	 */
	private ExecutorService imagePrefetchers = null;
	
	/**
	 * status of movie service
	 */
	private MovieServiceStatus serviceStatus = null;
	
	/**
	 * type of service
	 */
	private ServiceType serviceType = ServiceType.MOVIE_SERVICE;

	private Logger logger;
	
	private MovieServiceImpl(int noOfThreads)
	{ 
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		currentMovies = new HashMap<Long, MovieTicket>();
		currentCacheRequests = new HashMap<CacheRequestIdentifier, CacheRequest>();
		
		initMovieCreators();
		initImagePrefetchers();
		
		serviceStatus = new MovieServiceStatus();
		
		startServiceMonitor();
		logger.logp(Level.INFO, "MovieServiceImpl", "MovieServiceImpl", "movie service started");
	}
	
	/**
	 * Initialize movie creator executor service
	 */
	private void initMovieCreators(){
		movieCreators = Executors.newFixedThreadPool(4);
	}
	
	/**
	 * Initialize image prefetchers executor service
	 */
	private void initImagePrefetchers(){
		
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		availableProcessors = availableProcessors >= 1 ? availableProcessors / 2 : availableProcessors;
		imagePrefetchers = Executors.newFixedThreadPool(availableProcessors);
		
	}
	
	/**
     * shutd0own the service
	 * @throws DataAccessException 
     */
    public void shutdown() throws DataAccessException
    {
    	stopServiceMonitor();
    	
    	if(movieCreators != null)
        {
    		movieCreators.shutdown();
    		movieCreators = null;
        }
    	if(imagePrefetchers != null)
    	{
    		imagePrefetchers.shutdown();
    		imagePrefetchers = null;
    	}
    	
    }
    
    @Override
	public void submitCacheRequest(String actor, CacheRequest cacheRequest, int ithImage) throws RemoteException
	{
		logger.logp(Level.INFO, "MovieServiceImpl", "submitCacheRequest", "submitted request for cache request="+cacheRequest.getMovieid()+" img="+ithImage+" type="+cacheRequest.type);
		
		CacheRequest myRequest = currentCacheRequests.get(new CacheRequestIdentifier(cacheRequest.guid, cacheRequest.onFrames, cacheRequest.otherCoordinate,
				cacheRequest.site));
		if(myRequest == null)
		{
			currentCacheRequests.put(new CacheRequestIdentifier(cacheRequest.guid, cacheRequest.onFrames, cacheRequest.otherCoordinate,
					cacheRequest.site), cacheRequest);
			myRequest = cacheRequest;
		}
		
		Callable<Void> task = MovieTaskFactory.createTask(actor, myRequest, ithImage);
		imagePrefetchers.submit(task);
		
		logger.logp(Level.FINE, "MovieServiceImpl", "submitCacheRequest", "created new cache request");	
	}
   	
	@Override
	public void submitImageRequest(String actor, MovieTicket movieTicket, int ithImage)
	{
		logger.logp(Level.INFO, "MovieServiceImpl", "submitImageRequest", "submitted request for movie="+movieTicket.getMovieid()+" img="+ithImage+" type="+movieTicket.type);
		
		MovieTicket myMovie = currentMovies.get(movieTicket.ID);
		if(myMovie == null)
		{
			currentMovies.put(movieTicket.ID, movieTicket);
			myMovie = movieTicket;
		}
		
		File movieRoot = myMovie.createStorageDirectory();
		logger.logp(Level.FINEST, "MovieServiceImpl", "createStorageDirectory", "created movie storage root "+movieRoot);
		
		Callable<Void> task = MovieTaskFactory.createTask(actor, myMovie, ithImage);
		movieCreators.submit(task);
		
		logger.logp(Level.FINE, "MovieServiceImpl", "submitImageRequest", "created new movie");	
	}
	
	/**
	 * returns the singleton instance of movie service
	 * @return the singleton instance of movie service
	 */
	public static MovieServiceImpl getInstance()
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					Config.getInstance();
					singleton = new MovieServiceImpl(1);
				}
			}
		}
		
		return singleton;
	}

	@Override
	public void deleteMovie(String actor, long movieID) throws RemoteException
	{
		currentMovies.remove(movieID);
	}
	
	public static void main(String... args) throws IOException 
	{
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
    	
    	// start movie extraction service
//    	recordMovieService();
	}

	@Override
	public ServiceStatus getServiceStatus() {
		
		int movieCreatorQueueSize = currentMovies.size();
		int imagePrefetchersQueueSize = currentCacheRequests.size();
		
		logger.logp(Level.INFO,"","getServiceStatus","curentMovies="+currentMovies.size());
				
		serviceStatus.setMovieCreatorQueueSize(movieCreatorQueueSize);
		serviceStatus.setImagePrefetchersQueueSize(imagePrefetchersQueueSize);
		
		return serviceStatus;
	}

	@Override
	public ServiceType getServiceType() {

		return serviceType;
	}

	@Override
	public void restart() {
		
		logger.logp(Level.INFO,"","restart","");
		
    	if(movieCreators != null)
        {
    		movieCreators.shutdownNow();
    		initMovieCreators();
        }
    	if(imagePrefetchers != null)
    	{
    		imagePrefetchers.shutdownNow();
    		initImagePrefetchers();;
    	}
    	
    	currentMovies.clear();
    	currentCacheRequests.clear();
    	
	}
}
