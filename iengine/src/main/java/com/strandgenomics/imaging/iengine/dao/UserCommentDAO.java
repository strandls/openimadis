/*
 * UserCommentDAO.java
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
package com.strandgenomics.imaging.iengine.dao;

import java.sql.Timestamp;
import java.util.List;

import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.db.DataAccessException;

public interface UserCommentDAO {
	
	/**
	 * fetches comments on the specified record
	 * @param guid the recoid GUID 
	 * @return list of available commnets
	 */
	public List<UserComment> findComments(long guid) throws DataAccessException;
	
	/**
	 * Insert the specified comment for the given GUID
	 * @param guid the record GUID 
	 * @param addedBy the user adding the comment
	 * @param comments the comment
	 */
	public void insertComment(long guid, String addedBy, String comments) throws DataAccessException;
	
	/**
	 * fetches comment by id
	 * @param commentId
	 * @return
	 * @throws DataAccessException
	 */
	public UserComment findCommentById(long commentId) throws DataAccessException;
	
	/**
	 * delete comment
	 * @param commentId
	 * @throws DataAccessException
	 */
	public void deleteComment(long commentId) throws DataAccessException;
	
	/**
	 * delete all comments for a guid
	 * @param guid
	 * @param commentId
	 * @throws DataAccessException
	 */
			
	public void deleteCommentByGUID(long guid)throws DataAccessException;
	
	/**
	 * insert a comment with a creation date
	 * @param guid
	 * @param addedBy
	 * @param comments
	 * @throws DataAccessException
	 */
	void insertCommentWithCreationDate(long guid, String addedBy,
			String comments, Timestamp creationDate) throws DataAccessException;
	
}
