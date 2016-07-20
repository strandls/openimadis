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

package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TileDAO;
import com.strandgenomics.imaging.iengine.zoom.TileParameters;

/**
 * operation on tiles
 * @author navneet
 *
 */
public class DBTileDAO extends ImageSpaceDAO<TileParameters> implements TileDAO{

	DBTileDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) {
		
		super(factory, connectionProvider, "TileDAO.xml");

	}

	@Override
	public String getTilePath(long guid, int zoomReverseLevel )throws DataAccessException {

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TILE_PATH");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("zoomReverse", zoomReverseLevel, Types.INTEGER);
        
		return getString(sqlQuery);
	}

	@Override
	public void insertTile(long guid, int zoomReverseLevel, String storagePath) throws DataAccessException {

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TILE");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("zoomReverse", zoomReverseLevel, Types.INTEGER);
        sqlQuery.setValue("storagePath", storagePath, Types.VARCHAR);
        
		updateDatabase(sqlQuery);		
	}

	@Override
	public void removeTile(long guid, int zoomReverseLevel) throws DataAccessException {

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_TILES");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("zoomReverse", zoomReverseLevel, Types.INTEGER);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public boolean isTileReady(long guid, int zoomReverseLevel) throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("IS_TILE_READY");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("zoomReverse", zoomReverseLevel, Types.INTEGER);	
		
        return getBoolean(sqlQuery);
	}

	@Override
	public TileParameters createObject(Object[] columnValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIsReady(long guid, int zoomReverseLevel, boolean status) throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_IS_TILE_READY");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("zoomReverse", zoomReverseLevel, Types.INTEGER);
        sqlQuery.setValue("isReady", status, Types.BOOLEAN);
        
		updateDatabase(sqlQuery);
	}

	@Override
	public boolean isTileAlreadyPresent(long guid, int zoomReverseLevel) throws DataAccessException {
		
		if(getTilePath(guid, zoomReverseLevel)==null)
			return false;
		
		return true;
        
	}
    
	/**
	 * manages EstimatedTime and Progress
	 * @author Asif
	 *
	 */
	
	@Override
	public void insertEstimatedTime(long guid
			, double estimatedTime, double elapsedTime, long size) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TILE_EXECUTION_STATUS");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("estimatedTime", estimatedTime, Types.DOUBLE);
        sqlQuery.setValue("elapsedTime", elapsedTime, Types.DOUBLE);
        sqlQuery.setValue("size", size, Types.BIGINT);
        
		updateDatabase(sqlQuery);
		
	}

	@Override
	public void setEstimatedTime(long guid,
			double estimatedTime, double elapsedTime, long size)
			throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TILE_EXECUTION_STATUS");
        sqlQuery.setValue("guid", guid, Types.BIGINT);
        sqlQuery.setValue("estimatedTime", estimatedTime, Types.DOUBLE);
        sqlQuery.setValue("elapsedTime", elapsedTime, Types.DOUBLE);
        sqlQuery.setValue("size", size, Types.BIGINT);
        
		updateDatabase(sqlQuery);
		
	}

	@Override
	public RowSet<Object[]> getEstimatedTime() throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TILE_EXECUTION_STATUS");
		return executeQuery(sqlQuery);
	}

}
