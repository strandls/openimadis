/*
 * ImageSpaceDAO.java
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.DatabaseHandler;
import com.strandgenomics.imaging.icore.db.QueryDictionary;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;


/**
 * base class of image space data access object class
 * @author arunabha
 *
 */
public abstract class ImageSpaceDAO<T extends Storable> extends DatabaseHandler<T> {
	
    /** 
     * database configuration provider 
     * */
	protected final ConnectionProvider connectionProvider;
    /** 
     * the specific dao factory 
     */
    protected final ImageSpaceDAOFactory factory;
    
    /**
     * Creates a DAO instance with the factory and a connection provider 
     * @param factory factory of all relevant DAOs
     * @param connectionProvider provider if database connection
     * @throws DataAccessException 
     */
    ImageSpaceDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider, String queryFile) 
    {
    	this.factory = factory;
    	this.connectionProvider = connectionProvider;
    	try 
    	{
			this.queryDictionary = loadQueryConfig(queryFile);
			logger.logp(Level.INFO, "ImageSpaceDAO", "ImageSpaceDAO", "query file loaded successfully "+queryFile);
		} 
    	catch (DataAccessException e) 
    	{
    		logger.logp(Level.INFO, "ImageSpaceDAO", "ImageSpaceDAO", "failed loading query file "+queryFile);
    		throw new RuntimeException(e);
		}
    }
	
    /**
     * fetches database connection
     */
    public Connection getConnection() throws DataAccessException{

    	return connectionProvider.getConnection();
    }
    
	/**
	 * Returns a list of query names that are responsible for table creation.
	 * All keys having the term CREATE and not having the terms INDEX,FUNCTION and TRIGGER are returned
	 * @return a list of query names that are responsible for table creation
	 */
	protected TreeSet<String> getTableCreationQueries() 
	{
		Set<String> queryKeys = queryDictionary.getQueryKeys();
		if (queryKeys == null || queryKeys.isEmpty()) 
		{
			return null;
		}

		TreeSet<String> tableCreationKeys = new TreeSet<String>();
		// fire all queries that is there
		for (String queryKey : queryKeys) 
		{
			// get the SQLQuery for this key
			// just a little convention to avoid more code
			if (queryKey.indexOf("CREATE") != -1
					&& queryKey.indexOf("INDEX") == -1
					&& queryKey.indexOf("FUNCTION") == -1
					&& queryKey.indexOf("TRIGGER") == -1)
			{
				tableCreationKeys.add(queryKey);
			}
		}

		return tableCreationKeys;
	}

	/**
	 * Returns a list of query names that are responsible for table creation.
	 * All keys having the term CREATE and INDEX and not having the terms FUNCTION and TRIGGER are returned
	 * @return a list of query names that are responsible for table creation
	 */
	protected TreeSet<String> getIndexCreationQueries() {

		Set<String> queryKeys = queryDictionary.getQueryKeys();
		if (queryKeys == null || queryKeys.isEmpty()) 
		{
			return null;
		}

		TreeSet<String> indexCreationKeys = new TreeSet<String>();
		// fire all queries that is there
		for (String queryKey : queryKeys) 
		{
			// get the SQLQuery for this key
			if (queryKey.indexOf("CREATE") != -1
					&& queryKey.indexOf("INDEX") != -1
					&& queryKey.indexOf("FUNCTION") == -1
					&& queryKey.indexOf("TRIGGER") == -1) 
			{
				indexCreationKeys.add(queryKey);
			}
		}

		return indexCreationKeys;
	}

	/**
	 * Returns a list of query names that are responsible for table creation.
	 * All keys having the term CREATE and FUNCTION and not having the terms INDEX and TRIGGER are returned
	 * @return a list of query names that are responsible for table creation
	 */
	protected TreeSet<String> getFunctionCreationQueries() {

		Set<String> queryKeys = queryDictionary.getQueryKeys();
		if (queryKeys == null || queryKeys.isEmpty()) 
		{
			return null;
		}

		TreeSet<String> functionCreationKeys = new TreeSet<String>();
		// fire all queries that is there
		for (String queryKey : queryKeys) 
		{
			// get the SQLQuery for this key
			if (queryKey.indexOf("CREATE") != -1
					&& queryKey.indexOf("FUNCTION") != -1) 
			{
				functionCreationKeys.add(queryKey);
			}
		}

		return functionCreationKeys;
	}

	/**
	 * Returns a list of query names that are responsible for table creation.
	 * All keys having the term CREATE and TRIGGER and not having the terms INDEX and FUNCTION are returned
	 * @return a list of query names that are responsible for table creation
	 */
	protected TreeSet<String> getTriggerCreationQueries() {

		Set<String> queryKeys = queryDictionary.getQueryKeys();
		if (queryKeys == null || queryKeys.isEmpty())
		{
			return null;
		}

		TreeSet<String> triggerCreationKeys = new TreeSet<String>();
		// fire all queries that is there
		for (String queryKey : queryKeys) {
			// get the SQLQuery for this key
			if (queryKey.indexOf("CREATE") != -1
					&& queryKey.indexOf("TRIGGER") != -1) {
				triggerCreationKeys.add(queryKey);
			}
		}

		return triggerCreationKeys;
	}
    
    /**
     * Load the query dictionary from the specified file in the classpath
     * The path it looks for is "imaging/db-name/DBXYZ.xml", where db-name is the name of the database
     * as obtained from the connection provider, and filename is the query configuration xml 
     * specific for specific DAO classes
     * 
     * @param configFile name of the file
     * @return the Query Dictionary
     * @throws DataAccessException
     */
    protected QueryDictionary loadQueryConfig(String configFile) throws DataAccessException {

        QueryDictionary dictionary = null;
        BufferedInputStream inStream = null;

        String resourceName = "imagingdao/"+ connectionProvider.getDatabaseName() +"/"+configFile;

        try {

            ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
            inStream = new BufferedInputStream(contextLoader.getResourceAsStream(resourceName));

            dictionary = (QueryDictionary)Util.unmarshallFromXML(inStream, "UTF-8");
            logger.logp(Level.INFO, "ImageSpaceDAO", "loadQueryConfig", "loaded query config from "+resourceName);
        }
        catch (Exception se) 
        {
            logger.logp(Level.WARNING, "ImageSpaceDAO", "loadQueryConfig", "error while loading query config from "+resourceName, se);
            throw new DataAccessException(se.getMessage());
        }
        finally 
        {
        	Util.closeStream(inStream);
        }
        return dictionary;
    }
    
    protected OutputStream validateOutputStream(OutputStream out) throws IOException 
    {
        if(out == null) return null;
 
        BufferedOutputStream outStream = null;

        if(out instanceof BufferedOutputStream){
            outStream = (BufferedOutputStream) out;
        }
        else {
            outStream = new BufferedOutputStream(out, 1024*8);
        }

        //anonymous constructor to set the Deflater level in GZIPOutputStream
	    return new GZIPOutputStream(outStream){{
	                    def.setLevel(Deflater.BEST_SPEED);
	                 }};
	}

    protected final InputStream validateInputStream(InputStream in) throws IOException {

        if(in == null) return null;

        BufferedInputStream inStream = null;

        if(in instanceof BufferedInputStream){
            inStream = (BufferedInputStream) in;
        }
        else {
            inStream = new BufferedInputStream(in, 1024*8);
        }

        return new GZIPInputStream(inStream);
    }
    
	protected Object toJavaObject(DataSource ds)
	{
        if(ds == null) return null;
        
        InputStream inStream = null;
        Object pojo = null;
        
        try 
        {
        	inStream = ds.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(inStream);
            pojo = ois.readObject();
            
            ois.close();
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DBRecordDAO", "toJavaObject", "error re-serializing object", ex);
            throw new RuntimeException(ex);
        }
        finally
        {
        	Util.closeStream(inStream);
        }

        return pojo;
	}

	protected byte[] toByteArray(Object obj)  
	{
		try
		{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream(8096);
			ObjectOutputStream oos = new ObjectOutputStream(outStream);
			oos.writeObject(obj);
			oos.close();
			
			return outStream.toByteArray();
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "DBRecordDAO", "toByteArray", "error serializing object", ex);
			throw new RuntimeException(ex);
		}
	}
}
