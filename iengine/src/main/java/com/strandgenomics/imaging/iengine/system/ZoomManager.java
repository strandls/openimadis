package com.strandgenomics.imaging.iengine.system;

import java.io.File;
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

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ZoomDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.zoom.ZoomDimenstion;
import com.strandgenomics.imaging.iengine.zoom.ZoomIdentifier;
import com.strandgenomics.imaging.iengine.zoom.ZoomService;

/**
 * Manages zoom related requests and image prefetching
 * 
 * @author Anup Kulkarni
 */
public class ZoomManager extends SystemManager {
	/**
	 * time out interval (half hour)
	 */
	private static final long TIMEOUT_INTERVAL = 30 * 60 * 1000;

	public static long ID_GENERATOR = System.nanoTime();
	
	/**
	 * map for zoom dimension and zoom request id
	 */
	private Map<ZoomDimenstion, Long> dimToIdMap;
	/**
	 * map for zoom dimension and zoom request id
	 */
	private Map<Long, ZoomIdentifier> idToDimMap;
	/**
	 * memory used for caching zoom requests
	 */
	private long usedMemory = -1L;
	
	public ZoomManager() 
	{ 
		dimToIdMap = new HashMap<ZoomDimenstion, Long>();
		idToDimMap = new HashMap<Long, ZoomIdentifier>();
		
		initialize();
	}
	
	private void initialize()
	{
		ZoomDAO zoomDao = DBImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
		try
		{
			List<ZoomIdentifier> zoomRequests = zoomDao.loadAllRequests();
			if(zoomRequests!=null)
			{
				for(ZoomIdentifier zr:zoomRequests)
				{
					dimToIdMap.put(zr.getZoomDimension(), zr.zoomID);
					idToDimMap.put(zr.zoomID, zr);
				}
			}
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "initialize", "error loading zoom from DB");
		}
		
		usedMemory = Util.calculateSize(getZoomStorageRoot());
	}

	/**
	 * registers zoom request for given image co-ordinates; and returns unique
	 * identifier for the request. all the subsequent calls for requesting image must 
	 * contain this id as parameter
	 
	 * @param actorLogin logged in user
	 * @param guid specified record
	 * @param frame specified frame
	 * @param slice
	 * @param channel
	 * @param site
	 * @return unique request id
	 * @throws DataAccessException 
	 */
	public long registerZoomRequest(String actorLogin, long guid, int frame, int slice, List<Integer> channels, int site, boolean useChannelColor, List<String> overlays) throws DataAccessException
	{
		if(channels == null || channels.size() == 0)
			throw new IllegalArgumentException("at least one channel has to be selected");
		
		Record r = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
		
		List<Channel> ch = new ArrayList<Channel>();
		List<Channel> custChannels = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);
		for(Integer channelNo:channels)
		{
			ch.add(custChannels.get(channelNo));
		}
		
		if(r == null)
			throw new IllegalArgumentException("record doesnt exist for guid="+guid);
		
		ZoomDimenstion zDim = new ZoomDimenstion(guid, frame, slice, site, channels, ch, overlays, useChannelColor, r.imageWidth, r.imageHeight);
		
		if(dimToIdMap.containsKey(zDim))
		{
			// ZOOM REQUEST ALREADY EXISTS
			long requestID = dimToIdMap.get(zDim);

			// update DB
			updateLastAccessTime(requestID, System.currentTimeMillis());
			
			return requestID;
		}
		
		// check memory requirements
		if(!sufficientMemory(actorLogin, guid))
		{
			// cleanup records
			cleanupLRURecords(actorLogin, guid);
			
			if(!sufficientMemory(actorLogin, guid))
				throw new IllegalArgumentException("Server busy");
		}
		
		// NEW ZOOM REQUEST
		long requestID = generateZoomID();

		ZoomIdentifier zr = new ZoomIdentifier(requestID, zDim);
		dimToIdMap.put(zDim, requestID);
		idToDimMap.put(requestID, zr);
		
		// update last access time
		idToDimMap.get(requestID).setLastAccessTime(System.currentTimeMillis());
		
		try
		{
			// start prefetching image for zoom level 1(original image size) from tile (0,0)
			submitRequest(actorLogin, requestID, zDim, 1, 0, 0);
			
			// update DB
			updateDB(requestID);
			
			// update memory usage
			usedMemory += getMemoryRequired(actorLogin, guid);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "submitRequest", "error submitting request for zoom "+zDim);
		}
		
		return dimToIdMap.get(zDim);
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
		
		int max = imageHeight >= imageWidth ? imageHeight : imageWidth;
		
		int maxZoomLevel = (int) Math.ceil(max*1.0/ZoomDimenstion.TILE_HEIGHT);
		
		long requiredBytes = (long) (imageWidth * imageHeight * maxZoomLevel * 1.0 / 8.0);
		
		return requiredBytes;
	}
	
	private long getAvailableMemory()
	{
		long allocatedMemory = Constants.getLongProperty(Property.ZOOM_CACHE_SIZE, 100 * 1024 * 1024 * 1024);
		allocatedMemory = allocatedMemory * 1024 * 1024;
		
		logger.logp(Level.INFO, "ZoomManager", "getAvailableMemory", "available memory "+allocatedMemory);
		
		File zoomStorage = new File(Constants.getStringProperty(Property.ZOOM_STORAGE_LOCATION, "/home/anup/Desktop/zoom_storage"));
		if((allocatedMemory - usedMemory) < 0)
		{
			// over the period of time used memory is updated as per estimation of required memory
			// if allocated memory becomes less than zoom memory
			// calculate actual used memory
			usedMemory = Util.calculateSize(zoomStorage);
		}
		
		return (allocatedMemory - usedMemory);
	}

	private void cleanupLRURecords(String actorLogin, long guid) throws DataAccessException
	{
		if(idToDimMap == null || idToDimMap.size()<1) return;
		
		ZoomLRUComparator comparator = new ZoomLRUComparator(idToDimMap);

		TreeMap<Long, ZoomIdentifier> treeMap = new TreeMap<Long, ZoomIdentifier>(comparator);
		treeMap.putAll(idToDimMap);
		
		long currentTime = System.currentTimeMillis();
		
		Iterator<Long> it = treeMap.keySet().iterator();
		while(!sufficientMemory(actorLogin, guid))
		{
			if(!it.hasNext())
				return;
			
			long requestId = it.next();
			ZoomIdentifier zoomRequest = idToDimMap.get(requestId);
			
			if(zoomRequest.getLastAccessTime() + TIMEOUT_INTERVAL > currentTime)
			{
				// requests after this last access time cannot be removed
				break;
			}
			else 
			{
				removeZoomRequest(actorLogin, requestId);
			}
		}
	}
	
	private void removeZoomRequest(String actorLogin, long requestId)
	{
		// remove from cache
		ZoomIdentifier zr = idToDimMap.remove(requestId);
		dimToIdMap.remove(zr);
		
		// remove from DB
		try
		{
			ZoomDAO zoomDao = DBImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
			zoomDao.deleteZoomRequest(requestId);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "clearTimedoutRequests", "error deleting from DB zoom request="+requestId);
		}
		
		// remove from storage
		try
		{
			File storageRoot = getZoomStorageRoot();
			File requestStorageRoot = new File(storageRoot, String.valueOf(requestId));
			
			// get amount of memory released
			long releasedMemory = Util.calculateSize(requestStorageRoot);
			
			if(requestStorageRoot.exists())
			{
				Util.deleteTree(requestStorageRoot);
			}
			
			// update memory usage
			usedMemory -= releasedMemory;
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "clearTimedoutRequests", "error deleting from storage zoom request="+requestId);
		}
		
		// remove from worker cache
		try
		{
			removeFromWorker(requestId);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "clearTimedoutRequests", "error deleting from worker cache zoom request="+requestId);
		}
	}

	private void submitRequest(String actorLogin, long requestID, ZoomDimenstion zDim, int zoomLevel, int xTile, int yTile) throws IOException
	{
		logger.logp(Level.INFO, "ZoomManager", "submitRequest", "submitting request for zoom "+zDim);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getZoomServicePort());
        ZoomService serviceStub = null;
		try 
		{
			serviceStub = (ZoomService) registry.lookup(ZoomService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "submitRequest", "error submitting request for zoom "+zDim);
			throw new IOException(e);
		}
                    
        serviceStub.submitZoomRequest(actorLogin, requestID, zDim, zoomLevel, xTile, yTile);
	}
	
	private void updateLastAccessTime(long zoomRequestID, long lastAccessTime) throws DataAccessException
	{
		// update cache
		idToDimMap.get(zoomRequestID).setLastAccessTime(lastAccessTime);
		
		// update db
		ZoomDAO zoomDAO = ImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
		zoomDAO.updateLastAccessTime(zoomRequestID, lastAccessTime);
	}
	
	private void updateDB(long zoomRequestID) throws DataAccessException
	{
		ZoomDAO zoomDAO = ImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
		zoomDAO.insertZoomRequest(zoomRequestID, System.currentTimeMillis(), idToDimMap.get(zoomRequestID).getZoomDimension());
	}

	/**
	 * returns buffered image for specified request and specified sub-image for specified zoom level
	 * @param actorLogin
	 * @param requestId
	 * @param tile specified sub-image
	 * @param zoomLevel specified zoom level
	 * @return
	 * @throws IOException 
	 */
	public File getImage(String actorLogin, long requestId, int zoomLevel, int xTile, int yTile) throws IOException
	{
		if(!idToDimMap.containsKey(requestId))
			throw new IllegalArgumentException("zoom request does not exist "+requestId);

		// update last access time
		updateLastAccessTime(requestId, System.currentTimeMillis());
		
		logger.logp(Level.INFO, "ZoomManager", "getImage", "looking for image "+zoomLevel+" xTile="+xTile+" yTile="+yTile);
		
		ZoomDAO zoomDAO = ImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
		String path = zoomDAO.getImagePath(requestId, zoomLevel, xTile, yTile);
		
		if(path == null)
		{
			// requested image does not exist
			submitRequest(actorLogin, requestId, idToDimMap.get(requestId).getZoomDimension(), zoomLevel, xTile, yTile);
			return null;
		}
		
		return fetchImage(path);
	}
	
	/**
	 * returns thumbnail of image
	 * @param actorLogin
	 * @param requestId
	 * @return
	 * @throws IOException
	 */
	public File getThumbnail(String actorLogin, long requestId) throws IOException
	{
		// thumbnail will be stored as zoomLevel -1, xTile=0, yTile=0 in DB
		
		if(!idToDimMap.containsKey(requestId))
			throw new IllegalArgumentException("zoom request does not exist "+requestId);

		// update last access time
		updateLastAccessTime(requestId, System.currentTimeMillis());
		
		logger.logp(Level.INFO, "ZoomManager", "getThumbnail", "looking for thumbnail ");
		
		ZoomDAO zoomDAO = ImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
		String path = zoomDAO.getImagePath(requestId, -1, 0, 0);
		
		if(path == null)
		{
			// requested image does not exist
			return null;
		}
		
		return fetchImage(path);
	}
	
	private File fetchImage(String filePath) throws IOException
	{
		if(filePath==null || filePath.isEmpty()) return null;
		
		return  new File(filePath);
	}
	
	private void removeFromWorker(long requestID) throws IOException
	{
		logger.logp(Level.INFO, "ZoomManager", "removeFromWorker", "removing from worker cache zoom="+requestID);
		
        Registry registry = LocateRegistry.getRegistry(Constants.getZoomServicePort());
        ZoomService serviceStub = null;
		try 
		{
			serviceStub = (ZoomService) registry.lookup(ZoomService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "ZoomManager", "submitRequest", "error removing from worker cache zoom="+requestID);
			throw new IOException(e);
		}
                    
        serviceStub.removeZoomRequest(requestID);
	}

	/**
	 * returns unique id for zoom request
	 * @return
	 */
	private static synchronized final long generateZoomID()
	{
		return ID_GENERATOR++;
	}
	
	private File getZoomStorageRoot()
	{
		File f = new File(Constants.getStringProperty(Property.ZOOM_STORAGE_LOCATION, "/home/anup/Desktop/zoom_storage"));
		if(!f.isDirectory())
			f.mkdir();
		return f;
	}
	
	/**
	 * compares ZoomIdentifier based on last access time
	 * 
	 * @author Anup Kulkarni
	 */
	private class ZoomLRUComparator implements Comparator
	{
		private Map<Long, ZoomIdentifier> baseMap;

		public ZoomLRUComparator(Map<Long, ZoomIdentifier> idToDimMap) 
		{
			this.baseMap = idToDimMap;
		}

		@Override
		public int compare(Object o1, Object o2)
		{
			ZoomIdentifier id1 = baseMap.get(o1);
			ZoomIdentifier id2 = baseMap.get(o2);
			
			if(id1 == null) return -1;
			
			if(id2 == null) return 1;
			
			if(id1.getLastAccessTime() == id2.getLastAccessTime())
				return 0;
			else if(id1.getLastAccessTime() > id2.getLastAccessTime())
				return 1;
			
			return -1;
		}
	}
}
