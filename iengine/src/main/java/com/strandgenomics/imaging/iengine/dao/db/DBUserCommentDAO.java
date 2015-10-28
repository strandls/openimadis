/*
 * DBUserCommentDAO.java
 *
 * AVADIS Image Management System
 * Data Access Components
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.UserCommentDAO;

public class DBUserCommentDAO extends ImageSpaceDAO<UserComment> implements UserCommentDAO {

	DBUserCommentDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "UserCommentDAO.xml");
	}

	@Override
	public List<UserComment> findComments(long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBUserCommentDAO", "findComments", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_COMMENTS_FOR_RECORD");
		sqlQuery.setValue("GUID", guid, Types.BIGINT);

		RowSet<UserComment> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public void insertComment(long guid, String addedBy, String comments) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBUserCommentDAO", "insertComment", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_COMMENT");

		sqlQuery.setValue("GUID",    guid,     Types.BIGINT);
		sqlQuery.setValue("UserID",  addedBy,  Types.VARCHAR);
		sqlQuery.setValue("Comment", comments, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void insertCommentWithCreationDate(long guid, String addedBy, String comments, Timestamp creationDate) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBUserCommentDAO", "insertComment", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_COMMENT_WITH_CREATION_DATE");

		sqlQuery.setValue("GUID",    guid,     Types.BIGINT);
		sqlQuery.setValue("UserID",  addedBy,  Types.VARCHAR);
		sqlQuery.setValue("Comment", comments, Types.VARCHAR);
		sqlQuery.setValue("creationDate", creationDate, Types.TIMESTAMP);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public UserComment findCommentById(long commentId)
			throws DataAccessException {
		logger.logp(Level.INFO, "DBUserCommentDAO", "findCommentById", "commentid="+commentId);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_COMMENT_BY_ID");
		sqlQuery.setValue("commentID", commentId, Types.BIGINT);

		RowSet<UserComment> result = find(sqlQuery);
		return result == null ? null : result.getRows().get(0);
	}

	@Override
	public void deleteComment(long commentId) throws DataAccessException {
		logger.logp(Level.INFO, "DBUserCommentDAO", "deleteComment", "commentid="+commentId);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_COMMENT");

		sqlQuery.setValue("commentID", commentId, Types.BIGINT);
		
		updateDatabase(sqlQuery);		
	}
	
	@Override
	public void deleteCommentByGUID(long guid) throws DataAccessException {
		logger.logp(Level.INFO, "DBUserCommentDAO", "deleteCommentByGUID", "guid="+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_COMMENT_BY_GUID");

		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);		
	}

	@Override
	public UserComment createObject(Object[] columnValues) 
	{
		String userLogin       = (String)columnValues[0];
		Timestamp creationDate = Util.getTimestamp(columnValues[1]);
		String notes           = (String)columnValues[2];
		long commentid		   = (Long)columnValues[3];
		
        return new UserComment(userLogin, notes, creationDate, commentid);
	}

}
