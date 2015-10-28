package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.iengine.zoom.TileParameters;

/**
 * manages tiles
 * 
 * @author Navneet
 */
public interface TileDAO {

	/**
	 * 
	 * @param guid
	 * @param zoomReverseLevel
	 * @return
	 * @throws DataAccessException
	 */
	public String getTilePath(long guid, int zoomReverseLevel) throws DataAccessException;

	/**
	 * 
	 * @param guid
	 * @param zoomReverseLevel
	 * @param storagePath
	 * @throws DataAccessException
	 */
	public void insertTile(long guid, int zoomReverseLevel, String storagePath) throws DataAccessException;
	
	/**
	 * delete tile
	 * @param guid
	 * @param zoomReverseLevel
	 * @throws DataAccessException
	 */
	public void removeTile(long guid, int zoomReverseLevel) throws DataAccessException;
	
	/**
	 * is image for specified guid and zoom level ready
	 * @param guid
	 * @param zoomReverseLevel
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isTileReady(long guid, int zoomReverseLevel) throws DataAccessException;
	
	/**
	 * change status of isReady
	 * @param guid
	 * @param zoomReverseLevel
	 * @param status
	 * @throws DataAccessException
	 */
	public void setIsReady(long guid, int zoomReverseLevel, boolean status) throws DataAccessException;
	
	/**
	 * check if already there is an entry for given guid and zoom level
	 * @param guid
	 * @param zoomReverseLevel
	 * @return
	 * @throws DataAccessException 
	 */
	public boolean isTileAlreadyPresent(long guid, int zoomReverseLevel) throws DataAccessException;
	
	/**
	 * manages Estimated time and Progress (timeElapsed) of tiling a record
	 * @author Asif
	 */
	
	/**
	 * @param guid
	 * @param estimatedTime
	 * @param elapsedTime
	 * @throws DataAccessException 
	 */
	public void insertEstimatedTime(long guid, double estimatedTime, double elapsedTime, long size) throws DataAccessException;
	/**
	 * update estimatedTime and elapsedTime
	 * @param guid
	 * @param estimatedTime
	 * @param elapsedTime
	 * @throws DataAccessException 
	 */
	public void setEstimatedTime(long guid, double estimatedTime, double elapsedTime, long size) throws DataAccessException;
	/**
	 * get estimatedTime and elapsedTime
	 * @throws DataAccessException 
	 */
	public RowSet<Object[]> getEstimatedTime() throws DataAccessException;
}