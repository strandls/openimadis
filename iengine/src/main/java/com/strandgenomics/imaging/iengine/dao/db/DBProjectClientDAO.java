/*
 * DBUserClientDAO.java
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
package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ProjectClientDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectClient;

/**
 * DB implementation of {@link ProjectClientDAO}
 * 
 * @author navneet
 */
public class DBProjectClientDAO extends StorageDAO<ProjectClient> implements ProjectClientDAO {

    DBProjectClientDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) {
        super(factory, connectionProvider, "ProjectClientDAO.xml");
    }   

	@Override
	public List<Long> getRootFolders(int projectId) throws DataAccessException {
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_FOLDERS");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("ParentID", 0 , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 1 , Types.SMALLINT);
		
		long[] result=getRowsWithLongValues(sqlQuery);
		
		if(result==null)
			return null;
		
		return Arrays.asList(ArrayUtils.toObject(result));
	}

	@Override
	public List<Long> getSubFolders(int projectId, long parentID) throws DataAccessException {
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SUB_FOLDERS");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("ParentID", parentID , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 1 , Types.SMALLINT);
	
		long[] result=getRowsWithLongValues(sqlQuery);
		
		if(result==null)
			return null;
		
		return Arrays.asList(ArrayUtils.toObject(result));
	}

	@Override
	public void createNewFolder(int projectId, String name, long parentID) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("CREATE_NEW_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("Name", name , Types.VARCHAR);
		sqlQuery.setValue("ParentID", parentID , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 1 , Types.SMALLINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void removeFolder(int projectId, long folderID) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("folderID", folderID , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 1 , Types.SMALLINT);
		
		updateDatabase(sqlQuery);			
	}

	@Override
	public void addClient(int projectId, String clientID, long parentID) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_CLIENT");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("Name", clientID , Types.VARCHAR);
		sqlQuery.setValue("ParentID", parentID , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 0 , Types.SMALLINT);
		
		updateDatabase(sqlQuery);		
		
	}

	@Override
	public void removeClient(int projectId, String clientID) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_CLIENT");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("ClientID", clientID , Types.VARCHAR);
		
		updateDatabase(sqlQuery);					
	}

	@Override
	public String[] getClientIDsInFolder(int projectId, long folderID) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_CLIENTS_IN_FOLDER");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("folderID", folderID , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 0 , Types.SMALLINT);
		
		return getRowsWithStringValues(sqlQuery);
	}

	@Override
	public String getFolderName(int projectId, long folderID) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_FOLDER_NAME");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("folderID", folderID , Types.BIGINT);
		sqlQuery.setValue("isDirectory", 1 , Types.SMALLINT);
		
		return getString(sqlQuery);
	}
	
	public String[] getAllClients(int projectId) throws DataAccessException {
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_CLIENTS");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectId));
		
		sqlQuery.setValue("isDirectory", 0 , Types.SMALLINT);
		
		return getRowsWithStringValues(sqlQuery);
	}

	@Override
	public ProjectClient createObject(Object[] columnValues) {
		return null;
	}

}
