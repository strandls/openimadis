package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.zoom.ZoomDimenstion;
import com.strandgenomics.imaging.iengine.zoom.ZoomIdentifier;

/**
 * manages images in the zoom
 * 
 * @author Anup Kulkarni
 */
public interface ZoomDAO {

	/**
	 * 
	 * @param zoomRequestID
	 * @param zoomLevel
	 * @param xTile
	 * @param yTile
	 * @return
	 * @throws DataAccessException
	 */
	public String getImagePath(long zoomRequestID, int zoomLevel, int xTile, int yTile) throws DataAccessException;

	/**
	 * inserts the image of specified movieID and specified coordinate
	 * @param zoomRequestID unique identifier of movie ticket
	 * @param coordinate the value of slice/frame
	 * @param path absolute path where the image is stored
	 * @throws DataAccessException 
	 */
	public void insertImage(long zoomRequestID, int zoomLevel, int xTile, int yTile, String path) throws DataAccessException;
	
	/**
	 * 
	 * @param zoomRequestID
	 * @param storagePath
	 * @param lastAccessTime
	 * @param zoomRequest
	 * @throws DataAccessException 
	 */
	public void insertZoomRequest(long zoomRequestID, long lastAccessTime, ZoomDimenstion zoomRequest) throws DataAccessException;

	/**
	 * loads all zoom request from DB
	 * @throws DataAccessException
	 */
	public List<ZoomIdentifier> loadAllRequests() throws DataAccessException;

	/**
	 * deletes all the zoom requests and related images from DB
	 * @param id zoom request id
	 * @throws DataAccessException
	 */
	public void deleteZoomRequest(long id) throws DataAccessException;

	/**
	 * updates last access time
	 * @param requestID
	 * @param lastAccessTime
	 * @throws DataAccessException
	 */
	public void updateLastAccessTime(long requestID, long lastAccessTime) throws DataAccessException;
}