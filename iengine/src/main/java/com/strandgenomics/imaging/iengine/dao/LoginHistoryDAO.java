package com.strandgenomics.imaging.iengine.dao;

import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.LoginHistoryObject;

/**
 * methods for handling login history of user
 * 
 * @author Anup Kulkarni
 */
public interface LoginHistoryDAO {

	/**
	 * inserts login history for user
	 * @param app application used by user for login
	 * @param userLogin logged in user
	 * @throws DataAccessException 
	 */
	public void insertLoginHistory(Application app, String userLogin) throws DataAccessException;
	
	/**
	 * returns list of LoginHistoryObject for specified filter details
	 * @param app used for login whos login history is required, can be null
	 * @param user whos login history is required, can be null 
	 * @param fromDate start date, can be null
	 * @param toDate end date, can be null
	 * @param limit , can be null
	 * @param offset, can be null
	 * @return requested login history
	 * @throws DataAccessException 
	 */
	public List<LoginHistoryObject> getLoginHistory(Application app, String user, Date fromDate, Date toDate, Long limit, Long offset) throws DataAccessException;
	
	/**
	 * returns count of LoginHistoryObject for specified filter details
	 * @param app used for login whos login history is required, can be null
	 * @param user whos login history is required, can be null 
	 * @param fromDate start date, can be null
	 * @param toDate end date, can be null
	 * @param limit , can be null
	 * @param offset, can be null
	 * @return count requested login history
	 * @throws DataAccessException 
	 */
	public long countLoginHistory(Application app, String user, Date fromDate, Date toDate) throws DataAccessException;
}
