/*
 * UserClientDAO.java
 *
 * Product:  AvadisIMG Server
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
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
import com.strandgenomics.imaging.iengine.models.ProjectClient;

/**
 * Data access methods for {@link ProjectClient}
 * 
 * @author navneet
 */
public interface ProjectClientDAO {

	/**
	 * get the top level folders for the project
	 * @param projectId
	 * @return
	 * @throws DataAccessException
	 */
	public List<Long> getRootFolders(int projectId) throws DataAccessException;

	/**
	 * get subfolders for given parentid
	 * @param projectId
	 * @param parentID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Long> getSubFolders(int projectId, long parentID) throws DataAccessException;
	
	/**
	 * get name of folder
	 * @param projectId
	 * @param folderID
	 * @return
	 */
	public String getFolderName(int projectId, long folderID)throws DataAccessException;

	/**
	 * create a new folder 
	 * @param projectId
	 * @param name
	 * @param parentID
	 * @throws DataAccessException
	 */
	public void createNewFolder(int projectId, String name, long parentID) throws DataAccessException;

	/**
	 * remove a folder
	 * @param projectId
	 * @param folderID
	 * @throws DataAccessException
	 */
	public void removeFolder(int projectId, long folderID) throws DataAccessException;
	
	/**
	 * add a client to a folder
	 * @param projectId
	 * @param parentID
	 * @throws DataAccessException
	 */
	public void addClient(int projectId, String clientID, long parentID) throws DataAccessException;
	
	
	/**
	 * remove client from folder
	 * @param projectId
	 * @param parentID
	 * @throws DataAccessException
	 */
	public void removeClient(int projectId, String clientID) throws DataAccessException;
	
	/**
	 * get all cleints in a folder
	 * @param projectId
	 * @param folderID
	 * @return
	 * @throws DataAccessException
	 */
	public String[] getClientIDsInFolder(int projectId, long folderID) throws DataAccessException;
	
	/**
	 * get all client in the project
	 * @param projectId
	 * @return
	 * @throws DataAccessException
	 */
	public String[] getAllClients(int projectId) throws DataAccessException;
}
