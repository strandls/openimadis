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
