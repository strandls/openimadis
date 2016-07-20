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

package com.strandgenomics.imaging.iengine.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.HistoryDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;

/**
 * History related API
 * 
 * @author Anup Kulkarni
 */
public class HistoryManager extends SystemManager {
	HistoryManager() {}
	
	/**
	 * inserts history item
	 * @param history history item
	 * @throws DataAccessException
	 */
	private void insertHistory(HistoryObject history) throws DataAccessException
	{
		logger.logp(Level.INFO, "HistoryManager", "insertHistory", "inserting history for "+history.getGuid());
		
		HistoryDAO historyDao = DBImageSpaceDAOFactory.getDAOFactory().getHistoryDAO();
		
		int projectId = SysManagerFactory.getRecordManager().getProjectID(history.getGuid());
		historyDao.insertHistory(projectId, history.getGuid(), history.getClient().name, history.getClient().version, history.getModifiedBy(), System.currentTimeMillis(), history.getAccessToken(), history.getType(), history.getDescription(), history.getArguments());
	}
	
	/**
	 * inserts history item
	 * @param guid specified record id
	 * @param app the application which performed the modification
	 * @param modifiedBy authorised user on behalf of whom the modification performed
	 * @param accessToken used for performing the operation
	 * @param type type of the modification 
	 * @param description of the modification
	 * @throws DataAccessException 
	 */
	public void insertHistory(long guid, Application app, String modifiedBy, String accessToken, HistoryType type, String description, String... args)
	{
		List<String> historyArgs = new ArrayList<String>();
		if(args !=null)
		{
			for(String arg:args)
				historyArgs.add(arg);
		}
		
		HistoryObject history = new HistoryObject(guid, app, modifiedBy, accessToken, System.currentTimeMillis(), type, description, historyArgs);
		
		try
		{
			insertHistory(history);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "HistoryManager", "insertHistory", "failed inserting history = "+description);
		}
	}
	
	/**
	 * returns list of history items on specified search criterion, (some criterions can be null in which case they are ignored)
	 * @param projectID specified project
	 * @param guid specified record
	 * @param actorLogin current logged in user
	 * @param targetLogin specified user, can be null
	 * @param type specified history type, can be null
	 * @param fromDate specified from date, can be null
	 * @param toDate specified end date, can be null
	 * @return list of history items on specified search criterion,
	 * @throws DataAccessException 
	 */
	public List<HistoryObject> getRecordHistory(String actorLogin, long guid, String targetLogin, HistoryType type, Date fromDate, Date toDate) throws DataAccessException
	{
		logger.logp(Level.INFO, "HistoryManager", "getRecordHistory", "getting history for "+guid+" type="+type);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		HistoryDAO historyDao = DBImageSpaceDAOFactory.getDAOFactory().getHistoryDAO();
		
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		return historyDao.getFilteredHistory(projectId, guid, targetLogin, type, fromDate, toDate);
	}
	
	/**
	 * returns history object for given access token
	 * @param projectID specified project id
	 * @param accessToken specified access token
	 * @return history object for given access token
	 * @throws DataAccessException 
	 */
	public List<HistoryObject> getAccessTokenHistory(int projectID, String accessToken) throws DataAccessException
	{
		HistoryDAO historyDao = DBImageSpaceDAOFactory.getDAOFactory().getHistoryDAO();
		return historyDao.getAccessTokenHistory(projectID, accessToken);
	}
	
	/**
	 * returns list of history items associated with specified record
	 * @param actorLogin current logged in user
	 * @param guid specified record
	 * @return list of history items associated with specified record
	 * @throws DataAccessException
	 */
	public List<HistoryObject> getRecordHistory(String actorLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "HistoryManager", "getHistory", "getting history for "+guid);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		HistoryDAO historyDao = DBImageSpaceDAOFactory.getDAOFactory().getHistoryDAO();
		
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		return historyDao.getRecordHistory(projectId, guid);
	}

	/**
	 * returns history types
	 * @return history types
	 */
	public HistoryType[] getHistoryTypes()
	{
		return HistoryType.values();
	}
}
