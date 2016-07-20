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

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordExportDAO;
import com.strandgenomics.imaging.iengine.export.ExportFormat;
import com.strandgenomics.imaging.iengine.export.ExportRequest;
import com.strandgenomics.imaging.iengine.export.ExportStatus;

public class DBRecordExportDAO extends ImageSpaceDAO<ExportRequest> implements RecordExportDAO {

	DBRecordExportDAO(ImageSpaceDAOFactory factory,ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "RecordExportDAO.xml");
	}

	@Override
	public void insertRecordExportRequest(ExportRequest request) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "insertRecordExportRequest", "ExportRequest="+request);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_EXPORT_REQUEST");
		
        sqlQuery.setValue("ExportID", request.requestId, Types.BIGINT);
        sqlQuery.setValue("GUIDS", toByteArray(request.getRecordIds()),     Types.BLOB);
        sqlQuery.setValue("ExportFormat", request.format.name(), Types.VARCHAR);
        sqlQuery.setValue("SubmittedBy", request.submittedBy, Types.VARCHAR);
        sqlQuery.setValue("SubmittedOn", new Timestamp(request.submittedOn), Types.TIMESTAMP);
        sqlQuery.setValue("Validity", new Timestamp(request.validTill), Types.TIMESTAMP);
        sqlQuery.setValue("Name", request.name, Types.VARCHAR);
        sqlQuery.setValue("ExportStatus", ExportStatus.SUBMITTED.name(), Types.VARCHAR);
        sqlQuery.setValue("Size", request.size, Types.BIGINT);

        updateDatabase(sqlQuery);
	}

	@Override
	public String getExportLocation(long requestId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "getExportLocation", "requestId="+requestId);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_EXPORT_LOCATION");
		
        sqlQuery.setValue("ExportID", requestId, Types.BIGINT);

        return getString(sqlQuery);
	}

	@Override
	public void removeRecordExportRequest(long requestId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "removeRecordExportRequest", "ExportRequest="+requestId);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_EXPORT_REQUEST");
		
		sqlQuery.setValue("ExportID", requestId, Types.BIGINT);

        updateDatabase(sqlQuery);
	}
	
	@Override
	public void setExportLocation(long requestId, String exportLocation) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "setExportLocation", "requestId="+requestId+" exportLocation"+exportLocation);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_EXPORT_LOCATION");
		
        sqlQuery.setValue("ExportID", requestId, Types.BIGINT);
        sqlQuery.setValue("ExportLocation", exportLocation, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public List<ExportRequest> loadAllRequests() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "loadAllRequests", "");
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LOAD_ALL");
		RowSet<ExportRequest> result = find(sqlQuery);
        
		return result == null ? null : result.getRows();
	}
	
	@Override
	public List<ExportRequest> getExportRequests(String user) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "getExportRequests", "user="+user);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_EXPORT_REQUESTS");
		sqlQuery.setValue("SubmittedBy", user, Types.VARCHAR);
		RowSet<ExportRequest> result = find(sqlQuery);
        
		return result == null ? null : result.getRows();
	}
	/*
	@Override
	public List<ExportRequest> getAllExportRequests() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "getExportRequests", "all users");
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_EXPORT_REQUESTS");
		RowSet<ExportRequest> result = find(sqlQuery);
        
		return result == null ? null : result.getRows();
	}
	
	*/
	@Override
	public void setExportStatus(long requestId, ExportStatus status) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "setExportStatus", "requestId="+requestId+" status="+status);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_EXPORT_STATUS");
		
        sqlQuery.setValue("ExportID", requestId, Types.BIGINT);
        sqlQuery.setValue("ExportStatus", status.name(), Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}
	
	@Override
	public ExportStatus getExportStatus(long requestId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordExportDAO", "getExportStatus", "requestId="+requestId);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_EXPORT_STATUS");
		
        sqlQuery.setValue("ExportID", requestId, Types.BIGINT);
        
        String statusString = getString(sqlQuery);
        return ExportStatus.valueOf(statusString);
	}

	@Override
	public ExportRequest createObject(Object[] columnValues)
	{
		long requestId = Util.getLong(columnValues[0]);

		List<Long> guids = (List<Long>) toJavaObject( (DataSource)columnValues[1]);
		
		String formatName = Util.getString(columnValues[2]);
		ExportFormat format = ExportFormat.valueOf(formatName);
		
		String submittedBy = Util.getString(columnValues[3]);
		
		Timestamp validity = Util.getTimestamp(columnValues[4]);
		
		String name = Util.getString(columnValues[5]);
		
		String url = Util.getString(columnValues[6]);
		
		Timestamp submittedOn = Util.getTimestamp(columnValues[7]);
		
		String statusName = Util.getString(columnValues[8]);
		ExportStatus status = ExportStatus.valueOf(statusName);
		
		long size = Util.getLong(columnValues[9]);
		ExportRequest request = new ExportRequest(requestId, submittedBy, guids, format, validity.getTime(), submittedOn.getTime(), size, name, status);
		request.setURL(url);
		
		return request;
	}

	@Override
	public ExportRequest getExportRequest(long requestId) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_EXPORT_REQUEST_FOR_ID");
		sqlQuery.setValue("RequestId", requestId, Types.BIGINT);
		
		return fetchInstance(sqlQuery);
	}
}
