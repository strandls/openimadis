/*
 * DBBookmarkDAO.java
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

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.BookmarkDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.SearchColumn;

/**
 * Handles bookmarks related stuff
 * @author arunabha
 *
 */
public class DBBookmarkDAO extends StorageDAO<SearchColumn> implements BookmarkDAO {

	public DBBookmarkDAO(DBImageSpaceDAOFactory dbImageSpaceDAOFactory, ConnectionProvider connectionProvider)
	{
		super(dbImageSpaceDAOFactory, connectionProvider, "BookmarkDAO.xml");
	}

	@Override
	public Long getFolderID(String userlogin, int projectID, Long parentID, String name) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_FOLDER_ID");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", parentID, Types.BIGINT);
		sqlQuery.setValue("FolderName", name, Types.VARCHAR);
		
		return getLong(sqlQuery);
	}

	@Override
	public String[] getSubFolderNames(int projectID, long parentID) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SUBFOLDER_NAMES_IN_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", parentID, Types.BIGINT);
		return getRowsWithStringValues(sqlQuery);
	}

	@Override
	public List<Object> getBookmarks(int projectID, long folderID) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_BOOKMARKS_IN_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", folderID, Types.BIGINT);
		List<Object> objects = new ArrayList<Object>();
		String[] bookmarkNames ;
		
		RowSet<Object[]> result = executeQuery(sqlQuery);
		bookmarkNames=getBookmarkNames(result);
		
		String[] IDs = getRowsWithStringValues(sqlQuery);
		if(IDs == null || IDs.length == 0) return null;
		
		long[] bookmarks = new long[IDs.length];
		for(int i = 0;i < IDs.length; i++)
			bookmarks[i] = Long.parseLong(IDs[i]);
		objects.add(bookmarkNames);
		objects.add(bookmarks);
		
		return objects;
	}

	@Override
	public void createNewFolder(String userlogin, int projectID, Long parentID, String name) 
			throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_BOOKMARK_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("UserID", userlogin, Types.VARCHAR);
		sqlQuery.setValue("ParentID", parentID, Types.BIGINT);
		sqlQuery.setValue("FolderName", name, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void addBookmarks(String userlogin, int projectID, long folderID, long guid) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_BOOKMARK");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("UserID", userlogin, Types.VARCHAR);
		sqlQuery.setValue("ParentID", folderID, Types.BIGINT);
		sqlQuery.setValue("GUID", ""+guid, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void removeGuidFromBookmark(String userlogin, int projectID, long guid) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_GUID_FROM_BOOKMARK");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", ""+guid, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void removeBookmark(int projectID, long folderID, long guid) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_BOOKMARK");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", folderID, Types.BIGINT);
		sqlQuery.setValue("GUID", ""+guid, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteBookmarkFolder(int projectID, long folderID) throws DataAccessException
	{
		Stack<Long> folders = new Stack<Long>();
		folders.push(folderID);
		
		while(!folders.isEmpty())
		{
			long parentID = folders.peek();
			long[] children = getSubFolderIDs(projectID, parentID);
			
			if(children == null || children.length == 0)
			{
				deleteFolder(projectID, folders.pop());
			}
			else
			{
				for(long child : children)
					folders.push(child);
			}
		}
	}
	
	private void deleteFolder(int projectID, long folderID) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_BOOKMARK_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", folderID, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}
	
	private long[] getSubFolderIDs(int projectID, long parentID) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SUBFOLDER_IDS_IN_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", parentID, Types.BIGINT);
		return getRowsWithLongValues(sqlQuery);
	}
	public boolean isBookmarkPresent(){
		return true;
	}
	
	@Override
	public SearchColumn createObject(Object[] columnValues) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Object> getBookmark(int projectID, long folderID, long guid) throws DataAccessException{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_BOOKMARK");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("ParentID", folderID, Types.BIGINT);
		sqlQuery.setValue("GUID", guid, Types.BIGINT);
		List<Object> objects = new ArrayList<Object>();
		String[] bookmarkNames ;
		
		RowSet<Object[]> result = executeQuery(sqlQuery);
		bookmarkNames=getBookmarkNames(result);
		
		
		String[] IDs = getRowsWithStringValues(sqlQuery);
		if(IDs == null || IDs.length == 0) return null;
		
		long[] bookmarks = new long[IDs.length];
		for(int i = 0;i < IDs.length; i++)
			bookmarks[i] = Long.parseLong(IDs[i]);
		objects.add(bookmarkNames);
		objects.add(bookmarks);
		
		return objects;
		
	}
	private String[] getBookmarkNames(RowSet<Object[]> result){
		String[] bookmarkNames =null;

		if(result == null || result.isEmpty()){
			return null;
		}

		List<Object[]> rowList = result.getRows();
		if(rowList == null || rowList.isEmpty()) {
			;
		}
		else {
			bookmarkNames = new String[rowList.size()];
			for(int i = 0;i < bookmarkNames.length; i++){
				bookmarkNames[i] = (String) rowList.get(i)[1];
			}
		}
		rowList = null;



		return bookmarkNames;
	}
	
}
