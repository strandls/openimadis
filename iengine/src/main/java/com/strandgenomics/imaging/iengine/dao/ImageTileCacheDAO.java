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

import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.PixelArrayTile;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public interface ImageTileCacheDAO {
	/**
	 * puts the raw data tile in cache
	 * @param tile specified raw data tile
	 * @throws DataAccessException
	 */
	public void setTile(PixelArrayTile tile) throws DataAccessException;
	/**
	 * deletes the specified raw data tile from the cache
	 * @param guid specified record id
	 * @param dim specified dimension
	 * @param tile specified tile
	 * @throws DataAccessException
	 */
	public void deleteTile(long guid, Dimension dim, Rectangle tile) throws DataAccessException;
	/**
	 * returns the specified tile from the cache
	 * @param guid specified record id
	 * @param dim specified dimension
	 * @param tile specified tile
	 * @return the specified raw data tile
	 * @throws DataAccessException
	 */
	public PixelArrayTile getTile(long guid, Dimension dim, Rectangle tile) throws DataAccessException;
	
	/**
	 * delete all the tiles of the specified record
	 * @param guid specified record
	 * @throws DataAccessException
	 */
	public void deleteAllTiles(long guid) throws DataAccessException;

	/**
	 * updates the timestamp of the current guid to current time
	 * @param guid specified recordid
	 * @throws DataAccessException 
	 */
	public void updateTimestamp(long guid) throws DataAccessException;
	
	/**
	 * returns records and respective timestamp when they were used
	 * @return records and respective timestamp when they were used
	 * @throws DataAccessException 
	 */
	public Map<Long, Long> getLRURecords() throws DataAccessException;
	
	/**
	 * deletes all the user cached data(all except for t=0,z=0,site=0,ch=all)
	 * @param guid specified record
	 */
	public void deleteUserCachedData(long guid) throws DataAccessException;
}
