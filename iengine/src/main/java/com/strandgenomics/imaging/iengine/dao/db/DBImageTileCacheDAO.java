package com.strandgenomics.imaging.iengine.dao.db;

import java.awt.Rectangle;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ImageTileCacheDAO;
import com.strandgenomics.imaging.iengine.models.PixelArrayTile;

public class DBImageTileCacheDAO extends ImageSpaceDAO<PixelArrayTile> implements ImageTileCacheDAO {

	DBImageTileCacheDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "ImageTileCacheDAO.xml");
	}

	@Override
	public void setTile(PixelArrayTile tile) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBImageTileCacheDAO", "setTile", "tile="+tile);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_TILE");
        sqlQuery.setValue("RecordID", tile.guid, Types.BIGINT);
        
        sqlQuery.setValue("Frame", tile.getDimension().frameNo, Types.SMALLINT);
        sqlQuery.setValue("Slice", tile.getDimension().sliceNo, Types.SMALLINT);
        sqlQuery.setValue("Channel", tile.getDimension().channelNo, Types.SMALLINT);
        sqlQuery.setValue("Site", tile.getDimension().siteNo, Types.SMALLINT);
        
        sqlQuery.setValue("StartX", tile.getX(), Types.INTEGER);
        sqlQuery.setValue("StartY", tile.getY(), Types.INTEGER);
        sqlQuery.setValue("EndX", tile.getEndX(), Types.INTEGER);
        sqlQuery.setValue("EndY", tile.getEndY(), Types.INTEGER);
        
        sqlQuery.setValue("Path", tile.getPath(), Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public void deleteTile(long guid, Dimension dim, Rectangle tile) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBImageTileCacheDAO", "deleteTile", "guid="+guid+" dimension="+dim+" tile"+tile);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_TILE");
        
        sqlQuery.setValue("RecordID", guid, Types.BIGINT);
        
        sqlQuery.setValue("Frame", dim.frameNo, Types.SMALLINT);
        sqlQuery.setValue("Slice", dim.sliceNo, Types.SMALLINT);
        sqlQuery.setValue("Channel", dim.channelNo, Types.SMALLINT);
        sqlQuery.setValue("Site", dim.siteNo, Types.SMALLINT);
        
        sqlQuery.setValue("StartX", tile.getX(), Types.INTEGER);
        sqlQuery.setValue("StartY", tile.getY(), Types.INTEGER);
        sqlQuery.setValue("EndX", tile.getX() + tile.getWidth(), Types.INTEGER);
        sqlQuery.setValue("EndY", tile.getY() + tile.getHeight(), Types.INTEGER);
        
        updateDatabase(sqlQuery);
	}
	
	@Override
	public void deleteAllTiles(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBImageTileCacheDAO", "deleteAllTiles", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_ALL_TILES");
        
        sqlQuery.setValue("RecordID", guid, Types.BIGINT);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public PixelArrayTile getTile(long guid, Dimension dim, Rectangle tile) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TILE");
        
        sqlQuery.setValue("RecordID", guid, Types.BIGINT);
        
        sqlQuery.setValue("Frame", dim.frameNo, Types.SMALLINT);
        sqlQuery.setValue("Slice", dim.sliceNo, Types.SMALLINT);
        sqlQuery.setValue("Channel", dim.channelNo, Types.SMALLINT);
        sqlQuery.setValue("Site", dim.siteNo, Types.SMALLINT);
        
        sqlQuery.setValue("StartX", tile.getX(), Types.INTEGER);
        sqlQuery.setValue("StartY", tile.getY(), Types.INTEGER);
        sqlQuery.setValue("EndX", tile.getX() + tile.getWidth(), Types.INTEGER);
        sqlQuery.setValue("EndY", tile.getY() + tile.getHeight(), Types.INTEGER);
        
		return fetchInstance(sqlQuery);
	}

	@Override
	public PixelArrayTile createObject(Object[] columnValues)
	{
		Long guid = (Long)columnValues[0];
		
		Integer frame = (Integer)columnValues[1];
		Integer slice = (Integer)columnValues[2];
		Integer channel = (Integer)columnValues[3];
		Integer site = (Integer)columnValues[4];
		Dimension dim = new Dimension(frame, slice, channel, site);
		
		Integer x = (Integer)columnValues[5];
		Integer y = (Integer)columnValues[6];
		Integer width = (Integer)columnValues[7];
		Integer height = (Integer)columnValues[8];
		Rectangle tile = new Rectangle(x, y, width, height);
		
		String path = (String)columnValues[9];
		
		PixelArrayTile pTile = new PixelArrayTile(guid, dim, tile, path);
		return pTile;
	}

	@Override
	public void updateTimestamp(long guid) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TIMESTAMP");
        
        sqlQuery.setValue("RecordID", guid, Types.BIGINT);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public Map<Long, Long> getLRURecords() throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LRU_RECORDS");
        
		List<Object[]> qResult = executeQuery(sqlQuery).getRows();
		
		if(qResult == null || qResult.isEmpty()) return null;
		
		Map<Long, Long> lruRecords = new HashMap<Long, Long>();
		for(Object[] values : qResult)
		{
			long guid = Util.getLong(values[0]);
			Timestamp accessTime = Util.getTimestamp(values[1]); 
			
			lruRecords.put(guid, accessTime.getTime());
		}
		return lruRecords;
	}

	@Override
	public void deleteUserCachedData(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBImageTileCacheDAO", "deleteUserCachedData", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_ALL_TILES_EXCEPT_FIRST");
        
        sqlQuery.setValue("RecordID", guid, Types.BIGINT);
        sqlQuery.setValue("Frame", 0, Types.SMALLINT);
        sqlQuery.setValue("Slice", 0, Types.SMALLINT);
        sqlQuery.setValue("Site", 0, Types.SMALLINT);
        
        updateDatabase(sqlQuery);
	}
}
