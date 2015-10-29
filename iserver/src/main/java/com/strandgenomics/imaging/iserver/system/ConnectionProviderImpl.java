/*
 * ConnectionProviderImpl.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.system;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.system.JNDINames;

public class ConnectionProviderImpl implements ConnectionProvider {
	
	/**
	 * default data source
	 */
	protected DataSource defaultDataSource = null;
	/**
	 * storage data source
	 */
	protected DataSource storageDataSource = null;
	
	public ConnectionProviderImpl()
	{}
	
	@Override
	public Connection getStorageConnection() throws DataAccessException 
	{
        if(storageDataSource == null)
        {
        	synchronized(this)
        	{
        		if(storageDataSource == null)
        		{
			        try {
			            InitialContext ctx = new InitialContext();
			            DataSource ds = (DataSource)ctx.lookup(JNDINames.STORAGE_DATASOURCE_REFERENCE);
			            storageDataSource = ds;
			        }
			        catch(NamingException nex)
			        {
			            throw new DataAccessException(nex.getMessage());
			        }
        		}
        	}
        }
        
        try 
        {
			return storageDataSource.getConnection();
		} 
        catch (SQLException e)
        {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	@Override
	public Connection getConnection() throws DataAccessException 
	{
        if(defaultDataSource == null)
        {
        	synchronized(this)
        	{
        		if(defaultDataSource == null)
        		{
			        try {
			            InitialContext ctx = new InitialContext();
			            DataSource ds = (DataSource)ctx.lookup(JNDINames.DEFAULT_DATASOURCE_REFERENCE);
			            defaultDataSource = ds;
			        }
			        catch(NamingException nex)
			        {
			            throw new DataAccessException(nex.getMessage());
			        }
        		}
        	}
        }
        
        try 
        {
			return defaultDataSource.getConnection();
		} 
        catch (SQLException e)
        {
			e.printStackTrace();
			throw new DataAccessException(e);
		}
	}

	@Override
	public String getDatabaseName()
	{
		return Constants.getDatabaseType();
	}
}
