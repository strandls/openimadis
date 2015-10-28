package com.strandgenomics.imaging.iengine.system;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.collections.map.LRUMap;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Thumbnail;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.ThumbnailDAO;

/**
 * handles record thumbnails caching and thumbnail DAO
 * 
 * @author Anup Kulkarni
 */
public class ThumbnailManager extends SystemManager {
	
	private static final int MAX_CAPACITY = 100000;
	/**
	 * thumbnail cache: map of guid and thumbnail
	 */
	public LRUMap thumbnailCache;
	
	ThumbnailManager()
	{
		thumbnailCache = new LRUMap(MAX_CAPACITY);
	}

	/**
	 * returns the thumbnail InputStream given guid
	 * @param actorLogin logged in user
	 * @param guid specified record
	 * @return thumbnail InputStream
	 * @throws IOException 
	 * @throws DataAccessException 
	 */
	public InputStream getThumbnail(String actorLogin, long guid) throws DataAccessException, IOException
	{
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		CacheKey key = new CacheKey(guid,CacheKeyType.Thumbnail);
		if(SysManagerFactory.getCacheManager().isCached(key))
		{
			try
			{
				Thumbnail thumbnail = (Thumbnail) thumbnailCache.get(guid);
				long cacheRevision = (Long) SysManagerFactory.getCacheManager().get(key);
				if(thumbnail.getRevision()==cacheRevision)
					return thumbnail.getInputStream();	
			}
			catch (Exception e)
			{}
		}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ThumbnailDAO thumbnailDao = factory.getThumbnailDAO();
		Thumbnail thumbnail = thumbnailDao.getThumbnail(guid);
		thumbnailCache.put(guid, thumbnail);
		
		// set in cache
		SysManagerFactory.getCacheManager().set(key, thumbnail.getRevision());
		
		return thumbnail.getInputStream();
	}
	
	/**
	 * returns the thumbnail revision given guid
	 * @param actorLogin logged in user
	 * @param guid specified record
	 * @return thumbnail revision
	 * @throws IOException 
	 * @throws DataAccessException 
	 */
	public long getThumbnailRevision(String actorLogin, long guid) throws DataAccessException, IOException
	{
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		CacheKey key = new CacheKey(guid,CacheKeyType.Thumbnail);
//		if(thumbnailCache.containsKey(guid))
		if(SysManagerFactory.getCacheManager().isCached(key))
		{
			Thumbnail thumbnail = (Thumbnail) thumbnailCache.get(guid);
			if(thumbnail!=null)
			{
				try
				{
					long revision = thumbnail.getRevision();
					long cacheRevision = (Long) SysManagerFactory.getCacheManager().get(key);
					if(cacheRevision==revision)
						return revision;
				}
				catch(Exception e)
				{}
			}
		}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ThumbnailDAO thumbnailDao = factory.getThumbnailDAO();
		Thumbnail thumbnail = thumbnailDao.getThumbnail(guid);
		thumbnailCache.put(guid, thumbnail);
		
		// set in cache
		SysManagerFactory.getCacheManager().set(key, thumbnail.getRevision());
				
		return thumbnail.getRevision();
	}
	
	/**
	 * 
	 * @param actorLogin
	 * @param guid
	 * @return etag based on revision, guid
	 * @throws IOException 
	 * @throws DataAccessException 
	 */
	public String getETag(String actorLogin, long guid) throws DataAccessException, IOException
	{
		return String.valueOf(getThumbnailRevision(actorLogin, guid));
	}
	
	/**
	 * sets the image specified by given coordinates as record thumbnail
	 * @param actorLogin logged in user
	 * @param guid id of specified record
	 * @param frameNo selected frame number
	 * @param sliceNo selected frame number
	 * @param siteNo selected site number
	 * @param channelList list of channels to use
	 * @param channelNos selected frame number
	 * @param useChannelColor true if channel color is to be used; false in case of gray scale
	 * @param zStacked true if overlayed across slices; false in case of single slice
	 * @param mosaic true if channels are tiled; false in case of single/overlayed channels
	 * @throws Exception 
	 */
	public void setThumbnail(String actorLogin, long guid, int frameNo, int sliceNo, int siteNo, List<Integer> channelList, boolean useChannelColor, boolean zStacked, boolean mosaic) throws IOException
	{
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		BufferedImage img = SysManagerFactory.getImageManager().getPixelDataOverlay(actorLogin, guid, sliceNo, frameNo, siteNo, channelList, useChannelColor, zStacked, mosaic,null);
		setThumbnail(actorLogin, guid, img);
	}
	
	/**
	 * Sets a thumbnail to the specified record
	 * @param actorLogin the user making the request
	 * @param guid the record GUID
	 * @param thumbnail the thumbnail image
	 * @throws IOException 
	 */
	public synchronized void setThumbnail(String actorLogin, long guid, BufferedImage thumbnail) throws IOException 
	{
		if(thumbnail == null) return;
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		logger.logp(Level.INFO, "StorageManager", "setThumbnail", "setting thumbnail for "+guid);
		
		//resize thumbnail image
		thumbnail = Util.resizeImage(thumbnail, Constants.getRecordThumbnailWidth());
		
		//store in DB
		ThumbnailDAO thumbnailDao = factory.getThumbnailDAO();
		thumbnailDao.setThumbnail(guid, thumbnail);
		
		//clear from cache
//		thumbnailCache.remove(guid);
		SysManagerFactory.getCacheManager().remove(new CacheKey(guid, CacheKeyType.Thumbnail));
	}
}
