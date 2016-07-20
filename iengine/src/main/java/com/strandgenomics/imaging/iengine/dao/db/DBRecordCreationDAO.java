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

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordCreationDAO;
import com.strandgenomics.imaging.iengine.models.RecordBuilder;


public class DBRecordCreationDAO extends ImageSpaceDAO<RecordBuilder> implements RecordCreationDAO {

	DBRecordCreationDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "RecordCreationDAO.xml");
	}

	@Override
	public void insertRecordBuilder(long guid, RecordBuilder rb) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordCreationDAO", "insertRecordBuilder", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_RECORD_BUILDER");
		
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		sqlQuery.setValue("StorageLocation", rb.sourceFileLocation, Types.VARCHAR);
		
		sqlQuery.setValue("FrameCount", rb.maxFrames, Types.INTEGER);
		sqlQuery.setValue("SliceCount", rb.maxSlices, Types.INTEGER);
		sqlQuery.setValue("ChannelCount", rb.maxChannels, Types.INTEGER);
		sqlQuery.setValue("SiteCount", rb.maxSites, Types.INTEGER);
		
		sqlQuery.setValue("Width", rb.imageWidth, Types.INTEGER);
		sqlQuery.setValue("Height", rb.imageHeight, Types.INTEGER);
		sqlQuery.setValue("Depth", rb.depth.getByteSize(), Types.INTEGER);
		
		sqlQuery.setValue("Dimensions",  toByteArray(rb.getReceivedDimensions()), Types.BLOB);
		sqlQuery.setValue("LastAccess",  new Timestamp(rb.getLastAccessTime()), Types.TIMESTAMP);
		
		sqlQuery.setValue("ParentGUID",  rb.parentGuid, Types.BIGINT);		
		
		updateDatabase(sqlQuery);
	}

	@Override
	public RecordBuilder getRecordBuilder(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordCreationDAO", "getRecordBuilder", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECORD_BUILDER");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);

        return fetchInstance(sqlQuery);
	}

	@Override
	public void updateReceivedDimensions(long guid, List<Dimension> dim) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordCreationDAO", "updateReceivedDimensions", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_RECEIVED_DIMENSIONS");
		sqlQuery.setValue("Dimensions",  toByteArray(dim), Types.BLOB);
		sqlQuery.setValue("LastAccess",  new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteRecordBuilder(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordCreationDAO", "deleteRecordBuilder", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_RECORD_BUILDER");
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public List<Dimension> getReceivedDimensions(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordCreationDAO", "getReceivedDimensions", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECEIVED_DIMENSIONS");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);

        DataSource ds = (DataSource) getObject(sqlQuery);
        return (List<Dimension>) toJavaObject( ds );
	}

	@Override
	public RecordBuilder createObject(Object[] columnValues)
	{
		long guid = Util.getLong(columnValues[0]);
		
		int maxFrames = Util.getInteger(columnValues[1]);
		int maxSlices = Util.getInteger(columnValues[2]);
		int maxChannels = Util.getInteger(columnValues[3]);
		int maxSites = Util.getInteger(columnValues[4]);
		
		int imageWidth = Util.getInteger(columnValues[5]);
		int imageHeight = Util.getInteger(columnValues[6]);
		
		int pDepth = Util.getInteger(columnValues[7]);
		PixelDepth pixelDepth = PixelDepth.toPixelDepth(pDepth);
		
		String sourceLocation = Util.getString(columnValues[8]);
		
		Timestamp laTime = Util.getTimestamp(columnValues[9]);
		long lastAccessTime = laTime.getTime();
		
		List<Dimension> receivedDimensions = (List<Dimension>) toJavaObject( (DataSource)columnValues[10]);
		
		Long parentGuid = Util.getLong(columnValues[11]);
		
		RecordBuilder rb = new RecordBuilder(guid, parentGuid, maxFrames, maxSlices,
				maxChannels, maxSites, imageWidth, imageHeight, pixelDepth,
				sourceLocation);
		
		rb.setLastAccessTime(lastAccessTime);
		rb.setReceivedDimensions(receivedDimensions);
		
		return rb;
	}

	@Override
	public List<RecordBuilder> getRecordBuilders() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordCreationDAO", "getRecordBuilders", "returning all the record creation objects in process");
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_RECORD_BUILDERS");

        RowSet<RecordBuilder> result = find(sqlQuery);
        
        return result == null ? null : result.getRows();
	}

}
