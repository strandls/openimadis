/*
 * BookmarkManager.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.iengine.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.BookmarkDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;

/**
 * manages bookmarks for users
 * @author arunabha
 *
 */
public class BookmarkManager extends SystemManager {
	
    public static final char   SEPARATOR_CHAR           = '/';
    public static final String SEPARATOR                = ""+SEPARATOR_CHAR;
	
	BookmarkManager()
	{}
	
	public synchronized void registerNewUser(String userlogin, int projectID) throws DataAccessException
	{
		logger.logp(Level.INFO, "BookmarkManager", "registerNewUser", "registering bookmark root for user="+ userlogin);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        BookmarkDAO dao  = factory.getBookmarkDAO();
        
        String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
        
        dao.createNewFolder(userlogin, projectID, null, projectName);
	}
	
	public String getBookmarkRoot(String actorLogin, int projectID)
	{
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		return SEPARATOR+projectName;
	}
	
	public synchronized Long getBookmarkRootFolderID(String userlogin, int projectID) throws DataAccessException
	{
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        BookmarkDAO dao  = factory.getBookmarkDAO();
        
        String bookmarkRoot = SysManagerFactory.getProjectManager().getProject(projectID).getName();
        
        return dao.getFolderID(userlogin, projectID, null, bookmarkRoot);
	}
	
	/**
	 * Returns the name of the sub folders
	 * @param userlogin
	 * @param path
	 * @return
	 * @throws DataAccessException
	 */
	public String[] getSubFolderNames(String userlogin, int projectID, String path) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "getSubFolderNames", "sub folders for user="+ userlogin +" at "+path);

		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);

        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        BookmarkDAO dao  = factory.getBookmarkDAO();
        return dao.getSubFolderNames(projectID, folderID);
	}
	
	/**
	 * Returns a list of sub-folder under the specified path (fully qualified path)
	 * The list is ordered alphabetically
	 * @param userlogin the user making the query
	 * @param path fully qualified path of the bookmark folder
	 * @return list of sub folders (as fully qualified paths)
	 */
	public List<String> getSubFolders(String userlogin, int projectID, String path) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "getSubFolders", "sub folders for user="+ userlogin +" at "+path);
        String[] subFolderNames = getSubFolderNames(userlogin, projectID, path);
        if(subFolderNames == null) return null;

        path = path.trim();
        boolean endWithSeparator = path.endsWith(SEPARATOR);
        
        List<String> subFolders = new ArrayList<String>();
        for(String name : subFolderNames)
        {
        	String subFolder = endWithSeparator ? path + name :path +SEPARATOR+ name; 
        	subFolders.add(subFolder);
        }
        return subFolders;
	}
	
	private Set<String> getAllSubFolders(String userLogin, int projectID, String path) throws DataAccessException
	{
		Set<String> allSubFolders = new HashSet<String>();
		allSubFolders.add(path);
		boolean unchanged = true;
		
		while(unchanged)
		{
			int size = allSubFolders.size();
			Set<String> localList = new HashSet<String>();
			
			for(String folder:allSubFolders)
			{
				List<String> subFolders = getSubFolders(userLogin, projectID, folder);
				if(subFolders!=null)
				{
					localList.addAll(subFolders);
				}
			}
			allSubFolders.addAll(localList);
			
			if(allSubFolders.size() == size)
				unchanged = false;
		}
		return allSubFolders;
	}
	
	/**
	 * returns all the bookmarked records under given bookmark forlder recursively
	 * @param userLogin logged in user
	 * @param projectID specified project
	 * @param path specified folder path
	 * @return
	 * @throws DataAccessException
	 */
	public List<Object> getAllBookmarksRecursively(String userLogin, int projectID, String path) throws DataAccessException
	{
		Set<String> allSubFolders = getAllSubFolders(userLogin, projectID, path);
		
		List<String> bookmarkNames = new ArrayList<String>();
		List<Long> bookmarkIds = new ArrayList<Long>();
		for(String folderPath:allSubFolders)
		{
			List<Object> bs = getBookmarks(userLogin, projectID, folderPath);
			if(bs!=null)
			{
				String names[] = (String[]) bs.get(0);
				for(String name:names)
				{
					bookmarkNames.add(name);
				}
				long[] ids = (long[]) bs.get(1);
				for(long id:ids)
				{
					bookmarkIds.add(id);
				}
			}
		}
		
		String finalNames[] = new String[bookmarkNames.size()];
		long finalIds[] = new long[bookmarkIds.size()];
		
		int cnt = 0;
		for(String name:bookmarkNames)
		{
			finalNames[cnt] = name;
			cnt++;
		}
		cnt = 0;
		for(long id:bookmarkIds)
		{
			finalIds[cnt] = id;
			cnt++;
		}
		
		List<Object> bookmarks = new ArrayList<Object>();
		bookmarks.add(finalNames);
		bookmarks.add(finalIds);
		
		return bookmarks;
	}
	
	/**
	 * Returns a list of book-marked records (their guid) under the specified book-mark folder
	 * @param userlogin userlogin the user making the query
	 * @param path  path fully qualified path of the bookmark folder
	 * @return a list of guids, if any, and sorted by their creation time
	 */
	public List<Object> getBookmarks(String userlogin, int projectID, String path) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "getBookmarks", "bookmarks for user="+ userlogin +" at "+path);
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);
		
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        BookmarkDAO dao = factory.getBookmarkDAO();
        return dao.getBookmarks(projectID, folderID);
	}
	
	/**
	 * Returns number of book-marked records (their guid) under the specified book-mark folder
	 * @param userlogin userlogin the user making the query
	 * @param path  path fully qualified path of the bookmark folder
	 * @return number of guids, if any, and sorted by their creation time
	 */
	public int getBookmarksCount(String userlogin, int projectID, String path) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "getBookmarks", "bookmarks for user="+ userlogin +" at "+path);
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			return 0;
		
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        BookmarkDAO dao = factory.getBookmarkDAO();
        List<Object> bookmarks = dao.getBookmarks(projectID, folderID);
        
        if(bookmarks == null)
        	return 0;
        
        long[] ids = (long[]) bookmarks.get(1);
        
        if(ids == null) return 0;

        return ids.length;
	}
	
	/**
	 * Adds the specified book-mark under the specified book-mark folder
	 * @param userlogin userlogin the user making the query
	 * @param path path fully qualified path of the bookmark folder
	 * @param guid the guid of the record to book-mark
	 */
	public void addBookmarks(String userlogin, int projectID, String path, long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "addBookmarks", "adding bookmarks for user="+ userlogin +" at "+path);
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);
		
		 ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		 BookmarkDAO dao = factory.getBookmarkDAO();
		 
		 dao.addBookmarks(userlogin, projectID, folderID, guid);
	}
	
	/**
	 * Removes the specified book-mark from the specified folder
	 * @param userlogin userlogin the user making the query
	 * @param path path fully qualified path of the bookmark folder
	 * @param guid the guid
	 * @throws DataAccessException
	 */
	public void removeBookmark(String userlogin, int projectID, String path, long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "removeBookmark", "removing bookmarks for user="+ userlogin +" at "+path);
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);
		
		 ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		 BookmarkDAO dao = factory.getBookmarkDAO();
		 
		 dao.removeBookmark(projectID, folderID, guid);
	}
	
	public void addBookmarkFolder(String userlogin, int projectID, String path, String name) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "addBookmarkFolder", "removing bookmark folder "+name +" for user="+ userlogin +" at "+path);
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);
		
		 ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		 BookmarkDAO dao = factory.getBookmarkDAO();
		 
		 dao.createNewFolder(userlogin, projectID, folderID, name);
	}
	
	public void deleteBookmarkFolder(String userlogin, int projectID, String path) throws DataAccessException 
	{
		logger.logp(Level.INFO, "BookmarkManager", "deleteBookmarkFolder", "removing bookmark folder for user="+ userlogin +" at "+path);
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);
		
		 ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		 BookmarkDAO dao = factory.getBookmarkDAO();
		 
		 dao.deleteBookmarkFolder(projectID, folderID);
	}
	public boolean isBookmarkPresent(String userlogin, int projectID, String path,long guid) throws DataAccessException {
		
		Long folderID = getFolderIDForPath(userlogin, projectID, path);
		if(folderID == null)
			throw new DataAccessException("specified path do not exits "+path);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		 BookmarkDAO dao = factory.getBookmarkDAO();
		 List<Object>object = dao.getBookmark(projectID,folderID,guid);
		 if(object==null || object.size()==0){
			 return false;
		 }
		 else{
			 return true;
		 }
		
		
	}
	
	
    private Long getFolderIDForPath(String userlogin, int projectID, String absolutePath) throws DataAccessException 
    {
        logger.logp(Level.INFO, "BookmarkManager", "getFolderIDForPath", "absolutePath="+ absolutePath);
        
        List<String> pathComponents = getPathComponent(absolutePath);
        if(pathComponents == null || pathComponents.isEmpty())
        {
            return null;
        }

        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        BookmarkDAO dao    = factory.getBookmarkDAO();

        Long folderID = null;

        for(String name : pathComponents)
        {
            Long subFolderID = dao.getFolderID(userlogin, projectID, folderID, name);
            if(subFolderID == null)
            {
            	return null;
            }
            else
            {
            	folderID = subFolderID;
            }
        }

        return folderID;
    }

    /**
     * Returns the components from the specified normalized & absolute path
     */
    private List<String> getPathComponent(String absolutePath){

        logger.logp(Level.INFO, "BookmarkManager", "getPathComponent", "absolutePath="+ absolutePath);

        if(absolutePath == null || absolutePath.length() == 0 || !absolutePath.startsWith(SEPARATOR))
        {
            return null;
        }

        List<String> componentList = new ArrayList<String>();

        StringTokenizer tokenizer = new StringTokenizer(absolutePath,SEPARATOR);
        while(tokenizer.hasMoreTokens())
        {
            String component = tokenizer.nextToken().trim();
            if(component.length() > 0)
            	componentList.add(component);
        }

        return componentList;
    }
}
