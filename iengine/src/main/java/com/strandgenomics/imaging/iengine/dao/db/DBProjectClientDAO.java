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
