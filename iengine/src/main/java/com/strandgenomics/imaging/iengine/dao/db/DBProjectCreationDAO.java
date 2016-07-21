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

import java.sql.Connection;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ProjectCreationDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.models.Project;

/**
 * Creates necessary tables for a specific project
 * @author arunabha
 *
 */
public class DBProjectCreationDAO extends DBCreationDAO<Project> implements ProjectCreationDAO {
	
	DBProjectCreationDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "ProjectCreationDAO.xml");
	}
	
	@Override
	public synchronized Project createProject(String name, String notes, String createdBy, double quota, String location) 
			throws DataAccessException
	{
		if(name == null) 
			throw new NullPointerException("unexpected null value");
		
		ProjectDAO projectDao = factory.getProjectDAO();
		
		//make entries in the project table
		Project p = projectDao.insertProjectEntries(name, notes, createdBy, quota, location);
		try
		{
			//make project specific tables
			createStorageTables(p);
			//make relevant indexes, triggers and functions
			createPerformanceIndexes(p);
		}
		catch(DataAccessException ex)
		{
			projectDao.deleteProject(p.getID());
			throw ex;
		}
        return p;
	}
    
	@Override
	protected void executeCreationQuery(Connection conn, String queryKey, Project project, Object... args) 
			throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator(queryKey);
        sqlQuery.setParameter("Project", project.getTablePrefix());
        executeUpdate(sqlQuery, conn); 
	}
	
	@Override
	public Project createObject(Object[] columnValues) 
	{
		return null;
	}
}
