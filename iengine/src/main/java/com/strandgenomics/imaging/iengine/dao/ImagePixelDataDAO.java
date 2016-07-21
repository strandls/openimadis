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

import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;

/**
 * gives access to image pixel data
 * 
 * @author Anup Kulkarni
 */
public interface ImagePixelDataDAO {

	/**
	 * gets pixel data of image of specified dimension and specified record
	 * @param guid of parent record
	 * @param slice dimension of image
	 * @param frame dimension of image
	 * @param channel dimension of image
	 * @param site 
	 * @return PixelData of specified image
	 * @throws DataAccessException 
	 */
	public ImagePixelData find(long guid, Dimension coordinate) throws DataAccessException;
	
	/**
	 * gets pixel data of all images of specified record
	 * @param projectId of parent project 
	 * @param guid of parent record
	 * @return list of pixel data of all images 
	 */
	public List<ImagePixelData> find(long guid)
			throws DataAccessException;
	
	/**
	 * saves image pixel data
	 * @param projectId of parent project
	 * @param guid guid of parent record
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 * @param elapsedTime 
	 * @param exposureTime
	 * @param timeStamp 
	 * @param frame frame of specified image 
	 * @param slice slice of specified image
	 * @param channel channel of specified image
	 * @throws DataAccessException
	 */
	public void insertPixelDataForRecord(long guid, double x, double y, double z,
			double elapsedTime, double exposureTime, Date timeStamp, int frame,
			int slice, int channel, int site) throws DataAccessException;

	/**
	 * deletes the pixel data for the record 
	 * @param guid specified record
	 */
	public void deletePixelDataForRecord(long guid) throws DataAccessException;

	/**
	 * saves image pixel data
	 * @param projectId of parent project
	 * @param guid guid of parent record
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 * @param elapsedTime 
	 * @param exposureTime
	 * @param timeStamp 
	 * @param frame frame of specified image 
	 * @param slice slice of specified image
	 * @param channel channel of specified image
	 * @throws DataAccessException
	 */
	public void insertPixelDataForRecord(int targetProjectId, long guid,
			double x, double y, double z, double elapsed_time,
			double exposureTime, Date timestamp, int frameNo, int sliceNo,
			int channelNo, int siteNo) throws DataAccessException;
}
