/*
 * DefaultConnectionProvider.java
 *
 * AVADIS Image Management System
 * Core Engine Database Module
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
package com.strandgenomics.imaging.icore.db;

import java.sql.Connection;

import javax.sql.DataSource;

//apache-dbcp specific classes.
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
//apache-commons-pool specific classes.
import org.apache.commons.pool.impl.GenericObjectPool;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Disposable;

/**
 * maintains a list of two database connection pools
 * @author arunabha
 *
 */
public class DefaultConnectionProvider implements ConnectionProvider, Disposable {
	
	/**
	 * data-source for default connection
	 */
    private DataSource defaultDataSource = null;
    /**
     * ObjectPool that serves as the actual pool of default connections. 
     */
    private GenericObjectPool defaultConnectionPool = null;
	/**
	 * data-source for storage connection
	 */
    private DataSource storageDataSource = null;
    /**
     * ObjectPool that serves as the actual pool of storage connections. 
     */
    private GenericObjectPool storageConnectionPool = null;
    /**
     * type of the database server, one of mysql, oracle, postgres etc
     */
    private String databaseType = null;

    public DefaultConnectionProvider()
    {
        try
        {
        	System.out.println("[DefaultConnectionProvider]:\ttrying to load database driver "+Constants.getDatabaseDriver());
            Class.forName(Constants.getDatabaseDriver());
            System.out.println("[DefaultConnectionProvider]:\tsuccessfully loaded database driver "+Constants.getDatabaseDriver());
        } 
        catch (ClassNotFoundException e) 
        {
            e.printStackTrace(System.err);
        }
        
        printVariables();
        
        databaseType =  Constants.getDatabaseType();
        
        defaultConnectionPool = new GenericObjectPool(null, getConnectionPoolConfig());
        defaultDataSource = setupDataSource(defaultConnectionPool, Constants.getDatabaseURL(), 
        		Constants.getDatabaseUser(), Constants.getDatabasePassword());
        
        System.out.println("[DefaultConnectionProvider]:\tsetting up pooled default data sources..."+defaultDataSource);
        
        storageConnectionPool = new GenericObjectPool(null, getConnectionPoolConfig());
        storageDataSource = setupDataSource(storageConnectionPool, Constants.getStorageDatabaseURL(), 
        		Constants.getStorageDatabaseUser(), Constants.getStorageDatabasePassword());
        
        System.out.println("[DefaultConnectionProvider]:\tsetting up pooled storage data sources..."+storageDataSource);
    }
 
    @Override
    public void dispose()
    {
    	try
    	{
	    	if(defaultConnectionPool != null)
	    		defaultConnectionPool.close();
	    	
	    	defaultConnectionPool = null;
    	}
    	catch(Exception ex)
    	{}
    	
    	try
    	{
	    	if(storageConnectionPool != null)
	    		storageConnectionPool.close();
	    	
	    	storageConnectionPool = null;
    	}
    	catch(Exception ex)
    	{}
    }

	@Override
	public Connection getConnection() throws DataAccessException 
	{
        Connection connection = null;
        try
        {
            connection = defaultDataSource.getConnection();
        }
        catch(Exception ex)
        {
            throw new DataAccessException(ex.getMessage());
        }
        return connection;
	}
	
	@Override
    public Connection getStorageConnection() throws DataAccessException
    {
        Connection connection = null;
        try
        {
            connection = storageDataSource.getConnection();
        }
        catch(Exception ex)
        {
            throw new DataAccessException(ex.getMessage());
        }
        return connection;
    }

	@Override
	public String getDatabaseName() 
	{
		return databaseType;
	}
	
    private void printVariables() {
    }
    
    private DataSource setupDataSource(GenericObjectPool connectionPool, String url, String user, String pass) 
    {
    	System.out.println("[DefaultConnectionProvider]:\tcreating datasource for "+url);
    	
        //  create a ConnectionFactory that the pool will use to create Connections.
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, user, pass);
        // Next, create a pool for prepared statement pooling
        // KeyedObjectPoolFactory is used to create KeyedObjectPools 
        // for pooling PreparedStatements
	
	    // removed prepared statement pooling - results in large memory usage when there are
	    // several temporary tables used and without serving any purpose..
        // GenericKeyedObjectPool.Config stmtPoolConfig = getPreparedStatementPoolConfig();
        // KeyedObjectPoolFactory stmtPoolFactory = new GenericKeyedObjectPoolFactory(null, stmtPoolConfig);

        // Now create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        new PoolableConnectionFactory(connectionFactory, connectionPool, null, 
        		"SELECT 1",  //set the validation query to validate Connections.
                false, //the default "read only" setting for borrowed Connections
                true); // true for auto commit
        
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
        dataSource.setAccessToUnderlyingConnectionAllowed(true);

        return dataSource;
    }
    
    private static GenericObjectPool.Config getConnectionPoolConfig(){
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        
        //timeBetweenEvictionRunsMillis  indicates how long the eviction thread 
        //should sleep before "runs" of examining idle objects. 
        //When non-positive, no eviction thread will be launched. 
        //The default setting for this parameter is -1 
        //(i.e., idle object eviction is disabled by default). 
       // config.timeBetweenEvictionRunsMillis =  10 * 60 * 1000L; //10 minutes
        //testWhileIdle indicates whether or not idle objects should be validated 
        //using the factory's PoolableObjectFactory.validateObject(java.lang.Object) 
        //method. Objects that fail to validate will be dropped from the pool. 
        //This setting has no effect unless timeBetweenEvictionRunsMillis > 0. 
        //The default setting for this parameter is false.  
      //  config.testWhileIdle = true;
        //softMinEvictableIdleTimeMillis specifies the minimum amount of time an object 
        //may sit idle in the pool before it is eligible for eviction by the idle 
        //object evictor (if any), with the extra condition that at least "minIdle" 
        //amount of object remain in the pool. 
        //When non-positive, no objects will be evicted from the pool due to idle time 
        //alone. This setting has no effect unless timeBetweenEvictionRunsMillis > 0. 
        //The default setting for this parameter is -1 (disabled). 
      //  config.softMinEvictableIdleTimeMillis =  5 * 60000L;// 30 minutes
        //minEvictableIdleTimeMillis  specifies the minimum amount of time that an 
        //object may sit idle in the pool before it is eligible for eviction due to 
        //idle time. When non-positive, no object will be dropped from the pool due 
        //to idle time alone. 
        //This setting has no effect unless timeBetweenEvictionRunsMillis > 0. 
        //The default setting for this parameter is 30 minutes. 
     //   config.minEvictableIdleTimeMillis = 5 * 60L * 1000L;// 30 minutes
        
        config.maxActive = 40;
        config.maxIdle   = 10;
      //  config.maxWait
        config.minIdle   = 4;
      //  config.numTestsPerEvictionRun
        config.testOnBorrow = true;
       // config.testOnReturn
      //  config.whenExhaustedAction 
 
        return config;
    }
}
