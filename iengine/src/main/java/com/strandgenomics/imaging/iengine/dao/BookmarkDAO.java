/*
 * BookmarkDAO.java
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

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;

public interface BookmarkDAO {

	public Long getFolderID(String userlogin, int projectId, Long folderID, String name) throws DataAccessException;

	public String[] getSubFolderNames(int projectId, long folderID) throws DataAccessException;

	public List<Object> getBookmarks(int projectId, long folderID)  throws DataAccessException;

	public void createNewFolder(String userlogin, int projectId, Long folderID, String name) throws DataAccessException;

	public void addBookmarks(String userlogin, int projectId, long folderID, long guid) throws DataAccessException;

	public void removeBookmark(int projectId, long folderID, long guid) throws DataAccessException;
	
	public void removeGuidFromBookmark(String userlogin, int projectId, long guid) throws DataAccessException;
	
	public List<Object>  getBookmark(int projectId, long folderId,long guid) throws DataAccessException;

	public void deleteBookmarkFolder(int projectId, long folderID) throws DataAccessException;
}
