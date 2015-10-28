/*
 * DBProjectCreationDAO.java
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
