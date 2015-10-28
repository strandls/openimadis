/*
 * AbstractStorageDAO.java
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

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;

/**
 * Maps the data access objects to the storage database. DAO classes needing access to the storage
 * database needs to extend this class
 * @author arunabha
 *
 */
public abstract class StorageDAO<T extends Storable> extends ImageSpaceDAO<T> {
	
    /**
     * Creates a DAO instance with the factory and a connection provider 
     * @param factory factory of all relevant DAOs
     * @param connectionProvider provider if database connection
     * @throws DataAccessException 
     */
	StorageDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider, String queryFile) 
	{
		super(factory, connectionProvider, queryFile);
	}
	
    /**
     * fetches database connection for the storage
     */
    public Connection getConnection() throws DataAccessException{

    	return connectionProvider.getStorageConnection();
    }
}
