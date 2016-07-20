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
import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Project;

/**
 * gives access to image pixel data of image within a record
 * 
 * @author Anup Kulkarni
 */
public class DBImagePixelDataDAO extends StorageDAO<ImagePixelData> implements ImagePixelDataDAO{

	DBImagePixelDataDAO(ImageSpaceDAOFactory factory,
			ConnectionProvider connectionProvider) {
		super(factory, connectionProvider, "ImagePixelDataDAO.xml");
	}

	@Override
	public ImagePixelData find(long guid, Dimension d) throws DataAccessException {
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_IMAGEDATA_FOR_IMAGE");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		sqlQuery.setValue("SliceNo", d.sliceNo, Types.SMALLINT);
		sqlQuery.setValue("FrameNo", d.frameNo, Types.SMALLINT);
		sqlQuery.setValue("ChannelNo", d.channelNo, Types.SMALLINT);
		sqlQuery.setValue("SiteNo", d.siteNo, Types.SMALLINT);
		
		return fetchInstance(sqlQuery);
	}

	@Override
	public List<ImagePixelData> find(long guid) throws DataAccessException 
	{
	    RecordDAO recordDao = factory.getRecordDAO();
	    int projectID = recordDao.getProjectID(guid);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_IMAGEDATA_FOR_RECORD");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		
		RowSet<ImagePixelData> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public void insertPixelDataForRecord(long guid, double x, double y, double z,
			double elapsedTime, double exposureTime, Date timeStamp, int frame,
			int slice, int channel, int site) throws DataAccessException {
	    RecordDAO recordDao = factory.getRecordDAO();
	    int projectID = recordDao.getProjectID(guid);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_IMAGEDATA");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		sqlQuery.setValue("X", x, Types.REAL);
		sqlQuery.setValue("Y", y, Types.REAL);
		sqlQuery.setValue("Z", z, Types.REAL);
		sqlQuery.setValue("ElapsedTime", elapsedTime, Types.REAL);
		sqlQuery.setValue("ExposureTime", exposureTime, Types.REAL);
		sqlQuery.setValue("Timestamp", timeStamp, Types.TIMESTAMP);
		
		sqlQuery.setValue("SliceNo", slice, Types.SMALLINT);
		sqlQuery.setValue("FrameNo", frame, Types.SMALLINT);
		sqlQuery.setValue("ChannelNo", channel, Types.SMALLINT);
		sqlQuery.setValue("SiteNo", site, Types.SMALLINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public ImagePixelData createObject(Object[] columnValues)
	{
		double x = Util.getDouble(columnValues[0]);
		double y = Util.getDouble(columnValues[1]);
		double z = Util.getDouble(columnValues[2]);
		
		double elapsedTime = Util.getDouble(columnValues[3]);
		double exposureTime = Util.getDouble(columnValues[4]);
		Date timestamp = Util.getTimestamp(columnValues[5]);
		
		int slice = Util.getInteger(columnValues[6]);
		int frame = Util.getInteger(columnValues[7]);
		int channel = Util.getInteger(columnValues[8]);
		int site = Util.getInteger(columnValues[9]);
		
		return new ImagePixelData(x, y, z, elapsedTime, exposureTime,
				timestamp, slice, frame, channel,site);
	}

	@Override
	public void deletePixelDataForRecord(long guid) throws DataAccessException
	{
		RecordDAO recordDao = factory.getRecordDAO();
	    int projectID = recordDao.getProjectID(guid);
	    
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_IMAGEDATA_FOR_RECORD");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void insertPixelDataForRecord(int projectID, long guid,
			double x, double y, double z, double elapsedTime,
			double exposureTime, Date timeStamp, int frame, int slice,
			int channel, int site) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_IMAGEDATA");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		sqlQuery.setValue("X", x, Types.REAL);
		sqlQuery.setValue("Y", y, Types.REAL);
		sqlQuery.setValue("Z", z, Types.REAL);
		sqlQuery.setValue("ElapsedTime", elapsedTime, Types.REAL);
		sqlQuery.setValue("ExposureTime", exposureTime, Types.REAL);
		sqlQuery.setValue("Timestamp", timeStamp, Types.TIMESTAMP);
		
		sqlQuery.setValue("SliceNo", slice, Types.SMALLINT);
		sqlQuery.setValue("FrameNo", frame, Types.SMALLINT);
		sqlQuery.setValue("ChannelNo", channel, Types.SMALLINT);
		sqlQuery.setValue("SiteNo", site, Types.SMALLINT);
		
		updateDatabase(sqlQuery);
	}

}
