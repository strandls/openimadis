package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ZoomDAO;
import com.strandgenomics.imaging.iengine.zoom.ZoomDimenstion;
import com.strandgenomics.imaging.iengine.zoom.ZoomIdentifier;

public class DBZoomDAO extends ImageSpaceDAO<ZoomIdentifier> implements ZoomDAO{

	DBZoomDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "ZoomDAO.xml");
	}

	@Override
	public String getImagePath(long zoomRequestID, int zoomLevel, int xTile, int yTile) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "getImagePath", "zoomRequestID="+zoomRequestID+" zoomLevel="+zoomLevel+" xTile="+xTile+" yTile="+yTile);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TILE");
        sqlQuery.setValue("ZoomID", zoomRequestID, Types.BIGINT);
        sqlQuery.setValue("XTile", xTile, Types.INTEGER);
        sqlQuery.setValue("YTile", yTile, Types.INTEGER);
        sqlQuery.setValue("ZoomLevel", zoomLevel, Types.INTEGER);

        return getString(sqlQuery);
	}

	@Override
	public void insertImage(long zoomRequestID, int zoomLevel, int xTile, int yTile, String path) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "insertImage", "zoomRequestID="+zoomRequestID+" zoomLevel="+zoomLevel+" xTile="+xTile+" yTile="+yTile+" path="+path);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_TILE");
        sqlQuery.setValue("ZoomID", zoomRequestID, Types.BIGINT);
        sqlQuery.setValue("XTile", xTile, Types.INTEGER);
        sqlQuery.setValue("YTile", yTile, Types.INTEGER);
        sqlQuery.setValue("ZoomLevel", zoomLevel, Types.INTEGER);
        sqlQuery.setValue("StoragePath", path, Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public void insertZoomRequest(long zoomRequestID, long lastAccessTime, ZoomDimenstion zoomRequest) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "insertZoomRequest", "zoomRequestID="+zoomRequestID+" lastAccessTime="+lastAccessTime+" zoomRequest="+zoomRequest);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_ZOOM_REQUEST");
        sqlQuery.setValue("ZoomID", zoomRequestID, Types.BIGINT);
        sqlQuery.setValue("LastAccess", new Timestamp(lastAccessTime), Types.TIMESTAMP);
        sqlQuery.setValue("ZoomRequest", toByteArray(zoomRequest), Types.BLOB);

        updateDatabase(sqlQuery);
	}

	@Override
	public ZoomIdentifier createObject(Object[] columnValues)
	{
		Long zoomID = Util.getLong(columnValues[0]);

		Timestamp lastAccessTimestamp = Util.getTimestamp(columnValues[1]);
		long lastAccessTime = lastAccessTimestamp.getTime();
		
		ZoomDimenstion zoomDim = (ZoomDimenstion) toJavaObject( (DataSource)columnValues[2]);
		
		ZoomIdentifier zi = new ZoomIdentifier(zoomID, zoomDim);
		zi.setLastAccessTime(lastAccessTime);
		
		return zi;
	}

	@Override
	public List<ZoomIdentifier> loadAllRequests() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "loadAllRequests", "loading all zoom requests from DB");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LOAD_ALL");

        RowSet<ZoomIdentifier> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public void deleteZoomRequest(long zoomRequestID) throws DataAccessException
	{
		deleteZoom(zoomRequestID);
		
		deleteZoomTiles(zoomRequestID);
	}

	private void deleteZoom(long zoomRequestID) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "deleteZoom", "zoomRequestID="+zoomRequestID);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_ZOOM_REQUEST");
        sqlQuery.setValue("ZoomID", zoomRequestID, Types.BIGINT);

        updateDatabase(sqlQuery);
	}

	private void deleteZoomTiles(long zoomRequestID) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "deleteZoomTiles", "zoomRequestID="+zoomRequestID);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_ZOOM_TILES");
        sqlQuery.setValue("ZoomID", zoomRequestID, Types.BIGINT);

        updateDatabase(sqlQuery);
	}

	@Override
	public void updateLastAccessTime(long requestID, long lastAccessTime) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBZoomDAO", "updateLastAccessTime", "zoomRequestID="+requestID);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_LAST_ACCESS");
        sqlQuery.setValue("ZoomID", requestID, Types.BIGINT);
        sqlQuery.setValue("LastAccess", new Timestamp(lastAccessTime), Types.TIMESTAMP);

        updateDatabase(sqlQuery);
	}
	
}