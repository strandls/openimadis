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