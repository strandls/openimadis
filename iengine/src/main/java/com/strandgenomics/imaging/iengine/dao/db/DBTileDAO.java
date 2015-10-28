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
