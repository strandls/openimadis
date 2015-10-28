package com.strandgenomics.imaging.iengine.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.MovieDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.movie.MovieService;
import com.strandgenomics.imaging.iengine.movie.MovieTicket;
import com.strandgenomics.imaging.iengine.movie.MovieType;

/**
 * Manager class to manage movie creation
 * 
 * @author Anup Kulkarni
 */
public class MovieManager extends SystemManager{
	
	Map<Long, ArrayList<Integer>> idToImageRequestMap;
	/**
	 * memory used for caching zoom requests
	 */
	private long usedMemory = -1L;
	
	MovieManager()
	{ 
		idToImageRequestMap = new HashMap<Long, ArrayList<Integer>>();
		
		usedMemory = Util.calculateSize(getMovieStorageRoot());
	}
	
	private static synchronized final long generateMovieID()
	{
		return System.nanoTime();
	}
	
	/**
	 * creates new movie request. on this request the video will be created.
	 * @param actor logged in user
	 * @param guid specified record
	 * @param site specified site
	 * @param onFrame true if movie is to be played on frames; false if movie is played on slices
	 * @param channels selected channels 
	 * @param overlays selected overlays
	 * @param expiryTime time till when the video will be available
	 * @param movieName name of movie
	 * @return ticket associated with movie
	 * @throws IOException 
	 */
	public MovieTicket createVideoMovie(String actor, long guid, int site, boolean onFrame, int fixedCoorinate, boolean useChannelColor, boolean useZStack, List<Integer>channels, List<String>overlays, long expiryTime, Double fps, String movieName) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		return createMovie(actor, guid, site, onFrame, fixedCoorinate, useChannelColor, useZStack, channels, overlays, true, expiryTime, fps, movieName);
	}
	
	/**
	 * creates new movie request. on this request the images will be prefetched in order to serve subsequent requests
	 * @param actor logged in user
	 * @param guid specified record
	 * @param site specified site
	 * @param onFrame true if movie is to be played on frames; false if movie is played on slices
	 * @param channels selected channels 
	 * @param overlays selected overlays
	 * @return ticket associated with movie
	 * @throws IOException 
	 */
	public MovieTicket createMovieImages(String actor, long guid, int site, boolean onFrame, int fixedCoorinate, boolean useChannelColor, boolean useZStack, List<Integer>channels, List<String>overlays) throws IOException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		long expiryTime = System.currentTimeMillis() + MovieTicket.DEFAULT_EXPIRY_TIME;
		return createMovie(actor, guid, site, onFrame, fixedCoorinate, useChannelColor, useZStack, channels, overlays, false, expiryTime, null, String.valueOf(guid));
	}
	
	/**
	 * creates new movie request. on this request the images will be prefetched in order to serve subsequent requests
	 * @param actor logged in user
	 * @param guid specified record
	 * @param site specified site
	 * @param onFrame true if movie is to be played on frames; false if movie is played on slices
	 * @param channels selected channels 
	 * @param overlays selected overlays
	 * @param isVideo true if video is to be created, false otherwise
	 * @param movieName null if movie has no name
	 * @return ticket associated with movie
	 * @throws IOException 
	 */
	private MovieTicket createMovie(String actor, long guid, int site, boolean onFrame, int fixedCoorinate, boolean useChannelColor, boolean useZStack, List<Integer>channels, List<String>overlays, boolean isVideo, long expiryTime, Double fps, String movieName) throws IOException
	{
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectId);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		return createMovie(actor, guid, site, onFrame, fixedCoorinate, useChannelColor, useZStack, channels, overlays, 0, isVideo, expiryTime, fps, movieName);
	}
	
	/**
	 * delete the movie specified by movie id all the entries related to this
	 * movie are removed from DB as well as all the prefetched images are
	 * deleted
	 * 
	 * @param movieid
	 * @throws DataAccessException 
	 */
	private void deleteMovie(long movieid) throws DataAccessException
	{
		deleteMovieFromDB(movieid);
		deleteMovieFromStorage(movieid);
	}

	private void deleteMovieFromStorage(long movieid)
	{
		File movieStorage = new File(Constants.getStringProperty(Property.MOVIE_STORAGE_LOCATION, null), String.valueOf(movieid));

		long releasedMemory = Util.calculateSize(movieStorage);
		Util.deleteTree(movieStorage);
		
		usedMemory -= releasedMemory;
	}
	
	private File getMovieStorageRoot()
	{
		File movieStorageRoot = new File(Constants.getStringProperty(Property.MOVIE_STORAGE_LOCATION, null));
		return movieStorageRoot;
	}

	private void deleteMovieFromDB(long movieid) throws DataAccessException
	{
		MovieDAO movieDAO = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		movieDAO.deleteMovie(movieid);
	}
	
	/**
	 * returns list of video movies for specified user
	 * @param user specified user
	 * @return list of video movies for specified user
	 * @throws DataAccessException 
	 */
	public List<MovieTicket> getVideoMovies(String user) throws DataAccessException
	{
		List<MovieTicket> userVideos = new ArrayList<MovieTicket>();
		
		List<MovieTicket> movies = DBImageSpaceDAOFactory.getDAOFactory().getMovieDAO().loadMovies();
		if(movies!=null)
		{
			long currTime = System.currentTimeMillis();
			
			for(MovieTicket movie: movies)
			{
				logger.logp(Level.INFO, "MovieManager", "getVideoMovies", "movie");
				
				if(movie.actorLogin.equals(user) && MovieType.VIDEO.equals(movie.type) && movie.getExpiryTime() > currTime)
				{					
					userVideos.add(movie);
				}
			}
		}
		
		return userVideos;
	}

	/**
	 * creates new movie request. on this request the images will be prefetched in order to serve subsequent requests
	 * @param actor logged in user
	 * @param guid specified record
	 * @param site specified site
	 * @param onFrame true if movie is to be played on frames; false if movie is played on slices
	 * @param channels selected channels 
	 * @param overlays selected overlays
	 * @param start the starting co-ordinate of movie
	 * @param movieName name of movie
	 * @return ticket associated with movie
	 * @throws DataAccessException 
	 * @throws IOException 
	 */
	private synchronized MovieTicket createMovie(String actorLogin, long guid, int site, boolean onFrame, int fixedCoorinate, boolean useChannelColor, boolean useZStack, List<Integer> channels, List<String> overlays, int start, boolean isVideo, long expiryTime, Double fps, String movieName) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		if(!isValidMovie(actorLogin, guid, site, onFrame, fixedCoorinate))
		{
			throw new IllegalArgumentException("illegal arguments exception for creating movie");
		}
		
		// check for available space
		if(!sufficientMemory(actorLogin, guid))
		{
			cleanupExpiredRequests(actorLogin, guid);

			if (!sufficientMemory(actorLogin, guid))
				throw new IllegalArgumentException("Server Busy");
		}
		
		MovieType type = isVideo ? MovieType.VIDEO : MovieType.PREFETCHED_IMAGES;
		
		// create new movie object
		MovieTicket newMovie = new MovieTicket(generateMovieID(), actorLogin, guid, site, onFrame, fixedCoorinate, useChannelColor, useZStack, channels, overlays, type);
		newMovie.setExpiryTime(expiryTime);
		newMovie.setFPS(fps);
		newMovie.setMovieName(movieName);
		
		if(type==MovieType.PREFETCHED_IMAGES){
	        List<MovieTicket> movies = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO().loadMovies();
	        if (movies != null)
	        {
	                for(MovieTicket movie : movies)
	                {
	                        if(movie.equals(newMovie))
	                        {
	                                //movie exists
	                                logger.logp(Level.INFO, "MovieManager", "createMovie", "movie exists");
	
	                                return movie;
	                        }
	                }
	        }
		}
		
		// update DB
		MovieDAO movieDAO = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		movieDAO.registerMovie(newMovie);
		
		try
		{
			if(isVideo)
			{
				// start created the video
				submitVideoRequest(actorLogin, newMovie);
			}
			else
			{
				// start prefetching the images
				submitImagePrefetchRequest(actorLogin, newMovie, start);
			}
		}
		catch(Exception e)
		{
			movieDAO.deleteMovie(newMovie.ID);
		}
		
		
		// update memory usage
		usedMemory += getMemoryRequired(actorLogin, guid);
		
		return newMovie;
	}
	
	private boolean sufficientMemory(String actorLogin, long guid) throws DataAccessException
	{
		if(getMemoryRequired(actorLogin, guid) <= getAvailableMemory())
			return true;
		return false;
	}
	
	private long getMemoryRequired(String actorLogin, long guid) throws DataAccessException
	{
		Record r = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
		
		int imageWidth = r.imageWidth;
		int imageHeight = r.imageHeight;
		
		int frames = r.numberOfFrames;
		int slices = r.numberOfSlices;
		
		int max = frames > slices ? frames : slices;
		
		long requiredBytes = (long) (imageWidth * imageHeight * max * 1.0 / 8.0);
		
		return requiredBytes;
	}
	
	private long getAvailableMemory()
	{
		long allocatedMemory = Constants.getLongProperty(Property.MOVIE_CACHE_SIZE, 100 * 1024 * 1024 * 1024);
		allocatedMemory = allocatedMemory * 1024 * 1024;
		
		File movieStorage = new File(Constants.getStringProperty(Property.MOVIE_STORAGE_LOCATION, ""));
		if((allocatedMemory - usedMemory) < 0)
		{
			// over the period of time used memory is updated as per estimation of required memory
			// if allocated memory becomes less than zoom memory
			// calculate actual used memory
			usedMemory = Util.calculateSize(movieStorage);
		}
		
		return (allocatedMemory - usedMemory);
	}

	private void cleanupExpiredRequests(String actorLogin, long guid) throws DataAccessException
	{
		List<MovieTicket> movies = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO().loadMovies();
		Map<Long, MovieTicket> idToMoviesMap = new HashMap<Long, MovieTicket>();
		if(movies!=null)
		{
			for(MovieTicket movie:movies)
			{
				idToMoviesMap.put(movie.ID, movie);
			}
		}
		
		MovieExpiryComparator comparator = new MovieExpiryComparator(idToMoviesMap);

		TreeMap<Long, MovieTicket> treeMap = new TreeMap<Long, MovieTicket>(comparator);
		treeMap.putAll(idToMoviesMap);
		
		long currentTime = System.currentTimeMillis();
		
		Iterator<Long> it = treeMap.keySet().iterator();
		while(!sufficientMemory(actorLogin, guid))
		{
			if(!it.hasNext())
				return;
			
			long requestId = treeMap.firstKey();
			MovieTicket movieRequest = idToMoviesMap.get(requestId);
			
			if(movieRequest.getExpiryTime() <= currentTime)
			{
				removeMovieRequest(actorLogin, requestId);
				treeMap.remove(requestId);
			}
		}
	}
	
	/**
	 * deletes the movie and all the images
	 * @param actorLogin logged in user
	 * @param requestId specified movie id
	 * @throws DataAccessException
	 */
	public void removeMovieRequest(String actorLogin, long requestId) throws DataAccessException
	{
		deleteMovie(requestId);
		
		// update cache
		idToImageRequestMap.remove(requestId);
	}
	
	private boolean isValidMovie(String actorLogin, long guid, int site, boolean onFrame, int fixedCoorinate) throws DataAccessException
	{
		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
		
		if(site < record.numberOfSites)
		{
			boolean value = onFrame ?  (fixedCoorinate < record.numberOfSlices) : (fixedCoorinate < record.numberOfFrames);
			return value;
		}
		
		return false;
	}
	
	/**
	 * returns etag of the image
	 * @param actor current logged in user
	 * @param movieId specified movie
	 * @param ithImage ith image(frame/slice) of the movie
	 * @return etag of the specified image of the specified movie
	 * @throws IOException
	 */
	public String getEtag(String actor, long movieId, int ithImage) throws IOException
	{
		MovieTicket movieTicket = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO().getMovie(movieId);
		if(movieTicket == null || !movieTicket.isValid(ithImage))
		{
			throw new IllegalArgumentException("This request "+ithImage+" is not valid for this movie "+movieId);	
		}
		
		if (movieTicket.onFrames)
		{
			return SysManagerFactory.getImageManager().getEtag(actor, movieTicket.guid, movieTicket.otherCoordinate, ithImage, movieTicket.site, movieTicket.getOverlays(), false);
		}
		else
		{
			return SysManagerFactory.getImageManager().getEtag(actor, movieTicket.guid, ithImage, movieTicket.otherCoordinate, movieTicket.site, movieTicket.getOverlays(), false);
		}
	}
	
	/**
	 * returns the video for the movie, null if not present
	 * @param movieId specified movie
	 * @return video file for the movie, null if not present
	 * @throws IOException
	 */
	public MovieTicket getVideoMovie(long movieId) throws IOException{
		
		MovieDAO movieDAO = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		MovieTicket movie = movieDAO.getMovie(movieId);
		
		if(movie==null)
		{
			throw new IllegalArgumentException("Invalid Movie id="+movieId);
		}
		
		return movie;
		
	}
	
	/**
	 * returns the video for the movie, null if not present
	 * @param actor logged in user
	 * @param movieId specified movie
	 * @return video file for the movie, null if not present
	 * @throws IOException
	 */
	public File getVideo(String actor, long movieId) throws IOException
	{
		MovieDAO movieDAO = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		MovieTicket movie = movieDAO.getMovie(movieId);

		if(movie==null)
		{
			throw new IllegalArgumentException("Invalid Movie id="+movieId);
		}
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, movie.guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		String path = movieDAO.getImagePath(movieId, 0);
		
		if(path == null || path.isEmpty())
		{
			logger.logp(Level.INFO, "MovieManager", "getVideo", "video request for "+movieId+" is not ready");
			// submit new request to movie service

			// video is not ready in server
			// request is expected to come back
			return null;
		}
		
		return fetchFile(path);
	}

	/**
	 * returns the ith image from the movie
	 * @param actor logged in user
	 * @param movieId specified movie id
	 * @param ithImage absolute value frame/slice to be returned
	 * @return requested image
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	public File getNextImage(String actor, long movieId, int ithImage) throws IOException
	{	
		MovieDAO movieDAO = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		MovieTicket movie = movieDAO.getMovie(movieId);
		if(movie==null || !movie.isValid(ithImage))
		{
			throw new IllegalArgumentException("This request "+ithImage+" is not valid for this movie id"+movieId+" movie = "+movie.maxCoordinate+" isValid="+movie.isValid(ithImage));	
		}
		
		String path = movieDAO.getImagePath(movieId, ithImage);
		
		if(path == null || path.isEmpty())
		{
			logger.logp(Level.INFO, "MovieManager", "getNextImage", "submitting request for "+movieId+" for "+ithImage);
			
			// submit new request to movie service
			// if already not submitted
			if(!requestAlreadySubmitted(movieId, ithImage))
				submitImagePrefetchRequest(actor, movie, ithImage);
			
			// image is not ready in server
			// request is expected to come back
			return null;
		}
		
		return fetchFile(path);
	}
	
	/**
	 * returns true if image creation request is already submitted to worker
	 * @param movieId
	 * @param ithImage
	 * @return true if image creation request is already submitted to worker, false otherwise
	 */
	private synchronized boolean requestAlreadySubmitted(long movieId, int ithImage)
	{
		if(idToImageRequestMap.containsKey(movieId))
		{
			return true;
		}
		
		return false;
	}

	/**
	 * Refresh some image which is part of the movie.
	 * 
	 * @param actor logged in user
	 * @param guid specified record
	 * @param sliceNo specified slice no
	 * @param frameNo specified frame
	 * @param siteNo specified site
	 * @throws IOException
	 */
	public void refreshMovieImage(String actor, long guid, int sliceNo, int frameNo, int siteNo) throws IOException
	{
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectId);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		MovieDAO moviedao = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		List<MovieTicket> movies = moviedao.loadMovies();
		
		if(movies!=null){
			for (MovieTicket movieTicket : movies)
			{
				if (movieTicket.guid == guid && movieTicket.site == siteNo)
				{
					if (movieTicket.onFrames == true)
					{
						moviedao.deleteImagePath(movieTicket.ID, frameNo);
						submitImagePrefetchRequest(actor, movieTicket, frameNo);
					}
					else
					{
						moviedao.deleteImagePath(movieTicket.ID, sliceNo);
						submitImagePrefetchRequest(actor, movieTicket, sliceNo);
					}
				}
			}
		}
	}
	
	/**
	 * inserts the image for specified movie in DB
	 * @param movieID specified movie
	 * @param coordinate specified coordinate
	 * @param path to image location
	 * @throws DataAccessException
	 */
	public void insertImage(long movieID, int coordinate, String path) throws DataAccessException
	{
		MovieDAO moviedao = ImageSpaceDAOFactory.getDAOFactory().getMovieDAO();
		moviedao.insertImage(movieID, coordinate, path);
	}
	
	/**
	 * submits request for video creation
	 * @param actorLogin logged in user
	 * @param movieTicket specified movie
	 * @throws IOException 
	 */
	private void submitVideoRequest(String actorLogin, MovieTicket movieTicket) throws IOException
	{
		logger.logp(Level.INFO, "MovieManager", "submitVideoRequest", "submitting request for video for movie ("+movieTicket +")");
		
        Registry registry = LocateRegistry.getRegistry(Constants.getMovieServicePort());
        MovieService serviceStub = null;
		try 
		{
			serviceStub = (MovieService) registry.lookup(MovieService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "MovieManager", "submitVideoRequest", "error submitting request for video for movie ("+movieTicket +")");
			throw new IOException(e);
		}
		
        serviceStub.submitImageRequest(actorLogin, movieTicket, 0);
	}
	
	/**
	 * submits request for unavailable image to movie service
	 * @param actorLogin logged in user
	 * @param movieTicket specified movie
	 * @param ithImage requested image
	 * @throws IOException 
	 */
	private synchronized void submitImagePrefetchRequest(String actorLogin, MovieTicket movieTicket, int ithImage) throws IOException
	{
		logger.logp(Level.INFO, "MovieManager", "submitRequest", "submitting request for movie ("+movieTicket +") for image "+ithImage);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getMovieServicePort());
        MovieService serviceStub = null;
		try 
		{
			serviceStub = (MovieService) registry.lookup(MovieService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "MovieManager", "submitRequest", "error submitting request for movie ("+movieTicket +") for image "+ithImage);
			throw new IOException(e);
		}
		
		if(idToImageRequestMap.containsKey(movieTicket.getMovieid()))
		{
			if(idToImageRequestMap.get(movieTicket.getMovieid()).contains(ithImage))
			{
				// request already exists
				return;
			}
			idToImageRequestMap.get(movieTicket.getMovieid()).add(ithImage);
		}
		else
		{
			ArrayList<Integer> requestedImages = new ArrayList<Integer>();
			requestedImages.add(ithImage);
			idToImageRequestMap.put(movieTicket.getMovieid(), requestedImages);
		}
                    
        serviceStub.submitImageRequest(actorLogin, movieTicket, ithImage);
	}

	private File fetchFile(String filePath) throws FileNotFoundException
	{
		if(filePath==null || filePath.isEmpty()) return null;
		
		return new File(filePath);
	}
	
	/**
	 * compares MovieTicket based on last access time
	 * 
	 * @author Anup Kulkarni
	 */
	private class MovieExpiryComparator implements Comparator<Long>
	{
		private Map<Long, MovieTicket> baseMap;

		public MovieExpiryComparator(Map<Long, MovieTicket> idToDimMap) 
		{
			this.baseMap = idToDimMap;
		}

		@Override
		public int compare(Long o1, Long o2)
		{
			MovieTicket id1 = baseMap.get(o1);
			MovieTicket id2 = baseMap.get(o2);
			
			if(id1 == null) return -1;
			
			if(id2 == null) return 1;
			
			if(id1.getExpiryTime() == id2.getExpiryTime())
				return 0;
			else if(id1.getExpiryTime() > id2.getExpiryTime())
				return 1;
			
			return -1;
		}
	}
	
	public static void main(String args[]) throws Exception
	{}
}
