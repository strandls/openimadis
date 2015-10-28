package com.strandgenomics.imaging.iengine.dao;

import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;

/**
 * methods for handling record history
 * 
 * @author Anup Kulkarni
 */
public interface HistoryDAO {
	/**
	 * inserts history item
	 * @param guid record whos history item is to be added
	 * @param clientName of the client application who performed modification
	 * @param clientVersion of the client application who performed modification
	 * @param modifiedBy user who performed modification
	 * @param modificationTime time of modification
	 * @param accessToken access token used for performing this modification
	 * @param type of modification
	 * @param description of modification
	 * @param list of arguments used 
	 * @throws DataAccessException 
	 */
	public void insertHistory(int projectId, long guid, String clientName, String clientVersion, String modifiedBy, long modificationTime, String accessToken, HistoryType type, String description, List<String> list) throws DataAccessException;
	
	/**
	 * list of history associated with the record
	 * @param projectId of specified project
	 * @param guid specified record
	 * @return list of history associated with the record
	 * @throws DataAccessException 
	 */
	public List<HistoryObject> getRecordHistory(int projectId, long guid) throws DataAccessException;
	
	/**
	 * list of modification performed by specified client application
	 * @param projectId of specified project 
	 * @param clientName of specified client application
	 * @param clientVersion of specified client application
	 * @return list of modification performed by specified client application 
	 */
	public List<HistoryObject> getClientHistory(int projectId, String clientName, String clientVersion) throws DataAccessException;
	
	/**
	 * list of modification performed by specified access token
	 * @param projectID of specified project 
	 * @param accessToken specified access token
	 * @return list of modification performed by specified access token
	 * @throws DataAccessException
	 */
	public List<HistoryObject> getAccessTokenHistory(int projectID, String accessToken) throws DataAccessException;
	
	/**
	 * list of modification performed by specified user
	 * @param projectId of specified project
	 * @param userLogin of specified user
	 * @return list of modification performed by specified user
	 * @throws DataAccessException 
	 */
	public List<HistoryObject> getUserHistory(int projectId, String userLogin) throws DataAccessException;

	/**
	 * returns list of history items on specified search criterion, (some criterions can be null in which case they are ignored)
	 * @param projectID specified project
	 * @param guid specified record
	 * @param userLogin specified user, can be null
	 * @param type specified history type, can be null
	 * @param fromDate specified from date, can be null
	 * @param toDate specified end date, can be null
	 * @return list of history items on specified search criterion,
	 * @throws DataAccessException 
	 */
	public List<HistoryObject> getFilteredHistory(int projectID, long guid, String userLogin, HistoryType type, Date fromDate, Date toDate) throws DataAccessException;

	/**
	 * delete record history
	 * @param projectId specified project
	 * @param guid specified record
	 * @throws DataAccessException 
	 */
	public void deleteHistory(int projectId, long guid) throws DataAccessException;
}
