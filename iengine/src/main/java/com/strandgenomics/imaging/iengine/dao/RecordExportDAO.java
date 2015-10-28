package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.export.ExportRequest;
import com.strandgenomics.imaging.iengine.export.ExportStatus;

/**
 * Manages the record export
 * 
 * @author Anup Kulkarni
 */
public interface RecordExportDAO {
	/**
	 * inserts record export request
	 * @param request specified record export request
	 * @throws DataAccessException 
	 */
	public void insertRecordExportRequest(ExportRequest request) throws DataAccessException;
	
	/**
	 * get the location where record is exported
	 * @param requestId specified record export request
	 * @return the location where record is exported
	 */
	public String getExportLocation(long requestId) throws DataAccessException;

	/**
	 * removes the record export request
	 * @param requestId record export request
	 */
	public void removeRecordExportRequest(long requestId) throws DataAccessException;
	
	/**
	 * sets the location where specified record is exported
	 * @param requestId record export request
	 * @param exportLocation location where record is exported
	 */
	public void setExportLocation(long requestId, String exportLocation) throws DataAccessException;
	
	/**
	 * loads all existing export requests from DB (typically used by init method)
	 * @return
	 * @throws DataAccessException
	 */
	public List<ExportRequest> loadAllRequests() throws DataAccessException;

	/**
	 * returns request for specific user
	 * @param loginUser
	 * @return
	 * @throws DataAccessException 
	 */
	public List<ExportRequest> getExportRequests(String loginUser) throws DataAccessException;

	
	/**
	 * returns requests for all users
	 * @return
	 * @throws DataAccessException12:25:27 PM
	
	public List<ExportRequest> getAllExportRequests() throws DataAccessException;
*/
	/**
	 * sets the export status of specified export request
	 * @param requestId request identifier
	 * @param status of the request
	 * @throws DataAccessException
	 */
	public void setExportStatus(long requestId, ExportStatus status) throws DataAccessException;

	/**
	 * returns the export status of specified export request
	 * @param requestId
	 * @return
	 * @throws DataAccessException 
	 */
	public ExportStatus getExportStatus(long requestId) throws DataAccessException;

	/**
	 * get export request for specified export request
	 * @param requestId
	 * @return
	 * @throws DataAccessException 
	 */
	public ExportRequest getExportRequest(long requestId) throws DataAccessException;
}
