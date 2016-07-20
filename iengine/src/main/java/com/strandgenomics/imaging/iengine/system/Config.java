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

/*
 * Config.java
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

import java.io.File;
import java.util.Date;
import java.util.logging.Level;

import javax.naming.InitialContext;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.iengine.logging.LogManager;

/**
 * Server setups
 * @author arunabha
 *
 */
public class Config {
	
	public static final int  DEFAULT_SESSION_TIMEOUT = 8 * 3600; // time in seconds for 8 hours
    
    private static boolean initialized = false;
    private static Object padLock = new Object();
    
    /** single instance of ServerConfig */
    private static Config singletonInstance = null;
    
    /** session timeout in seconds */
    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;
    /**
     * the central location to store records
     */
    protected File storageRoot = null;
    
    /**
     * Creates a Config instance
     * Typically the server should be initialized by calling this method
     */
    Config()
    {
        System.out.println("[Config]:\tcreating config instance");
    }

    /**
     * A single instance of config is shared by all
     */
    public static final Config getInstance() 
    {
        if(!initialized)
        {
            synchronized(padLock)
            {
                if(!initialized)
                {
                	Config config = new Config();
                    try 
                    {
                    	config.init();
					} 
                    catch (Exception e) 
                    {
						e.printStackTrace();
					} 
                    
                    //start the logging system
                    LogManager.getInstance().initializeLogger();
                    
                    initialized = true;
                    singletonInstance = config;
                }
            }
        }
        
        return singletonInstance;
    }

    public int getSessionTimeout()
    {
        return sessionTimeout;
    }
    
    //called by the container to initialize the stuffs
    private void init() {

        //Expose system variables.
        initSystemProperties();

        //initialize other system variables
    	InitialContext ic;
		try 
		{
			ic = new InitialContext();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return;
		}

        //the session time out
        try {
			sessionTimeout  = ((Integer) ic.lookup(JNDINames.SESSION_TIMEOUT)).intValue();
		} 
        catch (Exception e)
        {
			e.printStackTrace();
			sessionTimeout = DEFAULT_SESSION_TIMEOUT;
		}
        
        System.out.println("successfully initialized server"+new Date());
    }
    
    private void initSystemProperties()  
    {
    	System.out.println("loading JNDI properties...");
    	InitialContext ic;
		try 
		{
			ic = new InitialContext();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return;
		}
		
		try 
        {
        	String mailConfigFile = (String) ic.lookup(JNDINames.MAIL_STORE);
	        System.setProperty(Constants.Property.MAIL_STORE, mailConfigFile);
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
        }
		
		try 
		{
			String solrURL = (String)ic.lookup(JNDINames.SOLR_URL);
	        System.setProperty(Constants.Property.SOLR_MAIN_URL, solrURL);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.SOLR_MAIN_URL);
		}
		
		try 
		{
			String defaultPassword = (String)ic.lookup(JNDINames.DEFAULT_ADMIN_PASSWORD);
	        System.setProperty(Constants.Property.DEFAULT_ADMIN_PASSWORD, defaultPassword);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LDAP_URL);
		}
		
		try 
		{
			String ldapURL = (String)ic.lookup(JNDINames.LDAP_URL);
	        System.setProperty(Constants.Property.LDAP_URL, ldapURL);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LDAP_URL);
		}
		
		try 
		{
			String ldapCN = (String)ic.lookup(JNDINames.LDAP_CN);
	        System.setProperty(Constants.Property.LDAP_CN, ldapCN);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LDAP_CN);
		}
		
		try 
		{
			String ldapCNPassword = (String)ic.lookup(JNDINames.LDAP_CN_PASSWORD);
	        System.setProperty(Constants.Property.LDAP_CN_PASSWORD, ldapCNPassword);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LDAP_CN_PASSWORD);
		}
		
		try 
		{
			String ldapGroupDN = (String)ic.lookup(JNDINames.LDAP_GROUP_DN);
	        System.setProperty(Constants.Property.LDAP_GROUP_DN, ldapGroupDN);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LDAP_GROUP_DN);
		}
		
		try 
		{
			String ldapBaseDN = (String)ic.lookup(JNDINames.LDAP_BASE_DN);
	        System.setProperty(Constants.Property.LDAP_BASE_DN, ldapBaseDN);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LDAP_GROUP_DN);
		}
		
		try 
		{
			String storageFolder = (String)ic.lookup(JNDINames.STORAGE_LOCATION);
	        System.setProperty(Constants.Property.STORAGE_LOCATION, storageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.STORAGE_LOCATION);
		}
		
		try 
		{
			String zoomStorageFolder = (String)ic.lookup(JNDINames.ZOOM_STORAGE_LOCATION);
	        System.setProperty(Constants.Property.ZOOM_STORAGE_LOCATION, zoomStorageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.ZOOM_STORAGE_LOCATION);
		}
		
		try 
		{
			String exportStorageFolder = (String)ic.lookup(JNDINames.EXPORT_STORAGE_LOCATION);
	        System.setProperty(Constants.Property.EXPORT_STORAGE_LOCATION, exportStorageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.ZOOM_STORAGE_LOCATION);
		}
		
		try 
		{
			String movieStorageFolder = (String)ic.lookup(JNDINames.MOVIE_STORAGE_LOCATION);
	        System.setProperty(Constants.Property.MOVIE_STORAGE_LOCATION, movieStorageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.MOVIE_STORAGE_LOCATION);
		}
		
		try 
		{
			String imageCacheStorageFolder = (String)ic.lookup(JNDINames.IMAGE_CACHE_STORAGE_LOCATION);
	        System.setProperty(Constants.Property.IMAGE_CACHE_STORAGE_LOCATION, imageCacheStorageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.IMAGE_CACHE_STORAGE_LOCATION);
		}
		
		try 
		{
			String taskLogStorageFolder = (String)ic.lookup(JNDINames.TASK_LOG_STORAGE_LOCATION);
	        System.setProperty(Constants.Property.TASK_LOG_STORAGE_LOCATION, taskLogStorageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.TASK_LOG_STORAGE_LOCATION);
		}
		
		try 
		{
			String backupStorageFolder = (String)ic.lookup(JNDINames.PROJECT_BACKUP_STORAGE_LOCATION);
	        System.setProperty(Constants.Property.PROJECT_BACKUP_STORAGE_LOCATION, backupStorageFolder);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.PROJECT_BACKUP_STORAGE_LOCATION);
		}
		
		try 
		{
			String acqClientLicenses = (String)ic.lookup(JNDINames.NUMBER_OF_ACQ_CLIENT_LICENSES);
	        System.setProperty(Constants.Property.NUMBER_OF_ACQ_CLIENT_LICENSES, acqClientLicenses);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.NUMBER_OF_ACQ_CLIENT_LICENSES);
		}
		
		try 
		{
			String movieCache = (String)ic.lookup(JNDINames.MOVIE_CACHE_SIZE);
	        System.setProperty(Constants.Property.MOVIE_CACHE_SIZE, movieCache);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.MOVIE_CACHE_SIZE);
		}
		
		try 
		{
			String zoomCache = (String)ic.lookup(JNDINames.ZOOM_CACHE_SIZE);
	        System.setProperty(Constants.Property.ZOOM_CACHE_SIZE, zoomCache);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.ZOOM_CACHE_SIZE);
		}
		
		try 
		{
			String exportCache = (String)ic.lookup(JNDINames.EXPORT_CACHE_SIZE);
	        System.setProperty(Constants.Property.EXPORT_CACHE_SIZE, exportCache);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.ZOOM_CACHE_SIZE);
		}
		
		try 
		{
			String uploadPort = (String)ic.lookup(JNDINames.EXTRACTION_SERVICE_PORT);
	        System.setProperty(Constants.Property.RMI_SERVICE_PORT, uploadPort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.UPLOAD_SERVICE_PORT);
		}
		
		try 
		{
			String cachePort = (String)ic.lookup(JNDINames.CACHE_SERVICE_PORT);
	        System.setProperty(Constants.Property.CACHE_SERVICE_PORT, cachePort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.CACHE_SERVICE_PORT);
		}
		
		try 
		{
			String cachePort = (String)ic.lookup(JNDINames.REMOTE_CACHE_SERVICE_PORT);
	        System.setProperty(Constants.Property.REMOTE_CACHE_SERVICE_PORT, cachePort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.REMOTE_CACHE_SERVICE_PORT);
		}
		
		try 
		{
			String cacheHost = (String)ic.lookup(JNDINames.CACHE_SERVICE_HOST);
	        System.setProperty(Constants.Property.CACHE_SERVICE_HOST, cacheHost);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.CACHE_SERVICE_HOST);
		}
		
		try 
		{
			String cacheHost = (String)ic.lookup(JNDINames.REMOTE_CACHE_SERVICE_HOST);
	        System.setProperty(Constants.Property.REMOTE_CACHE_SERVICE_HOST, cacheHost);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.REMOTE_CACHE_SERVICE_HOST);
		}
		
		try 
		{
			String licenseFile = (String)ic.lookup(JNDINames.LICENSE_FILE);
	        System.setProperty(Constants.Property.LICENSE_INFO, licenseFile);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LICENSE_INFO);
		}
		
		try 
		{
			String licenseKey = (String)ic.lookup(JNDINames.LICENSE_KEY);
	        System.setProperty(Constants.Property.LICENSE_KEY, licenseKey);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.LICENSE_KEY);
		}
		
		try 
		{
			String exportPort = (String)ic.lookup(JNDINames.EXPORT_SERVICE_PORT);
	        System.setProperty(Constants.Property.EXPORT_SERVICE_PORT, exportPort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.EXPORT_SERVICE_PORT);
		}
		
		try 
		{
			String zoomPort = (String)ic.lookup(JNDINames.ZOOM_SERVICE_PORT);
	        System.setProperty(Constants.Property.ZOOM_SERVICE_PORT, zoomPort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.ZOOM_SERVICE_PORT);
		}
		
		try 
		{
			String moviePort = (String)ic.lookup(JNDINames.MOVIE_SERVICE_PORT);
	        System.setProperty(Constants.Property.MOVIE_SERVICE_PORT, moviePort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.MOVIE_SERVICE_PORT);
		}
		
		try 
		{
			String backupPort = (String)ic.lookup(JNDINames.BACKUP_SERVICE_PORT);
	        System.setProperty(Constants.Property.BACKUP_SERVICE_PORT, backupPort);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.MOVIE_SERVICE_PORT);
		}
		
		try 
		{
			String readerPoolSize = (String)ic.lookup(JNDINames.BUFFERED_READER_POOL_SIZE);
	        System.setProperty(Constants.Property.BUFFERED_READER_POOL_SIZE, readerPoolSize);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.BUFFERED_READER_POOL_SIZE);
		}
		
		try 
		{
			String readerStackSize = (String)ic.lookup(JNDINames.BUFFERED_READER_STACK_SIZE);
	        System.setProperty(Constants.Property.BUFFERED_READER_STACK_SIZE, readerStackSize);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.BUFFERED_READER_STACK_SIZE);
		}
		
		try 
		{
			String maxImageReaders = (String)ic.lookup(JNDINames.NUMBER_OF_IMAGE_READERS);
	        System.setProperty(Constants.Property.NUMBER_OF_IMAGE_READERS, maxImageReaders);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.BUFFERED_READER_STACK_SIZE);
		}

		try 
		{
			String daoClassName = (String)ic.lookup(JNDINames.DAO_FACTORY_CLASS);
	        System.setProperty(Constants.Property.DAO_FACTORY, daoClassName);
		} 
		catch (Exception e)
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.DAO_FACTORY);
		}

		try
		{
			String connectionProviderClass = (String)ic.lookup(JNDINames.CONNECTION_PROVIDER_CLASS);
	        System.setProperty(Constants.Property.CONNECTION_PROVIDER, connectionProviderClass);
		} 
		catch (Exception e) 
		{
			System.out.println("unable to load from InitialContext "+Constants.Property.CONNECTION_PROVIDER);
		}

		try
        {
            String databaseType = (String)ic.lookup(JNDINames.DATABASE_TYPE);
            System.setProperty(Constants.Property.DATABASE_TYPE, databaseType);
        } 
        catch (Exception e) 
        {
            System.out.println("unable to load from InitialContext "+Constants.Property.DATABASE_TYPE);
        }
		
		try
		{
			String authClassLocal = (String)ic.lookup(JNDINames.USER_LOCAL_AUTHENTICATION_CLASS);
	        //set user authentication factory from USER_AUTHENTICATION_CLASS
	        System.setProperty(Constants.Property.AUTHENTICATION_LOCAL_HANDLER, authClassLocal);
		} 
		catch (Exception e)
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.AUTHENTICATION_LOCAL_HANDLER);
		}

		try 
		{
			String authClassExtn = (String)ic.lookup(JNDINames.USER_EXTN_AUTHENTICATION_CLASS);
	        //set user authentication factory from USER_AUTHENTICATION_CLASS
	        System.setProperty(Constants.Property.AUTHENTICATION_EXTN_HANDLER, authClassExtn);
		} 
		catch (Exception e) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.AUTHENTICATION_EXTN_HANDLER);
		}

        String rootLogFolder;
		try {
			rootLogFolder = (String) ic.lookup(JNDINames.ROOT_LOG_FOLDER);
			System.setProperty(Constants.Property.SYSTEM_LOG_DIR, rootLogFolder);
		}
		catch (Exception e1) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.SYSTEM_LOG_DIR);
		}


        // system property specifying the maximum size of a log file
        // after which a new file will be created in the log root folder

		try
		{
			long maxFileSize = (Long) ic.lookup(JNDINames.LOG_FILE_SIZE);
			 System.setProperty(Constants.Property.SYSTEM_LOG_SIZE, ""+maxFileSize);
		} 
		catch (Exception e1)
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.SYSTEM_LOG_SIZE);
		}
       

        //system property specifying when (days) a new log file 
        //will be created in the log root folder

		try {
			int dayIntervals = (Integer) ic.lookup(JNDINames.LOG_INTERVAL);
			System.setProperty(Constants.Property.SYSTEM_LOG_INTERVAL, ""+dayIntervals);
		} 
		catch (Exception e1) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.SYSTEM_LOG_INTERVAL);
		}
        

        //system property specifying the log level 
		try
		{
			Level logLevel = Level.parse((String) ic.lookup(JNDINames.LOG_LEVEL));
			System.setProperty(Constants.Property.SYSTEM_LOG_LEVEL, logLevel.toString());
		} 
		catch (Exception e1) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.SYSTEM_LOG_LEVEL);
		}
        

        //system property specifying the log scope (class/package) 
		try {
			String logScope = (String) ic.lookup(JNDINames.LOG_SCOPE);
			System.setProperty(Constants.Property.SYSTEM_LOG_SCOPE, logScope);
		} 
		catch (Exception e1) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.SYSTEM_LOG_SCOPE);
		}
        

        //system property specifying the log filename 
		try 
		{
			String filename = (String) ic.lookup(JNDINames.LOG_FILENAME);
			System.setProperty(Constants.Property.SYSTEM_LOG_FILENAME, filename);
		} 
		catch (Exception e) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.SYSTEM_LOG_FILENAME);
		}
		
        //system property specifying the LUT Directory 
		try 
		{
			String filename = (String) ic.lookup(JNDINames.LUT_LOCAION);
			File lutLocation = new File(filename).getCanonicalFile();
			
			if(lutLocation.isDirectory())
			{
				System.setProperty(Constants.Property.LUT_LOCATION, lutLocation.getAbsolutePath());
			}
		} 
		catch (Exception e) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.LUT_LOCATION);
		}
		
		// system property specifying web client name and version
		try 
		{
			String appName = (String) ic.lookup(JNDINames.WEB_APPLICATION_NAME);
			String appVersion = (String) ic.lookup(JNDINames.WEB_APPLICATION_VERSION);
			String adminAppVersion = (String) ic.lookup(JNDINames.WEB_ADMIN_APPLICATION_NAME);
			
			System.setProperty(Constants.Property.WEB_APPLICATION_NAME, appName);
			System.setProperty(Constants.Property.WEB_ADMIN_APPLICATION_NAME, adminAppVersion);
			System.setProperty(Constants.Property.WEB_APPLICATION_VERSION, appVersion);
		} 
		catch (Exception e) 
		{
			System.err.println("unable to load from InitialContext "+Constants.Property.LUT_LOCATION);
		}
		
        System.out.println("successfully initialized system properties from JNDI...");
        System.getProperties().list(System.out);
	}
}
