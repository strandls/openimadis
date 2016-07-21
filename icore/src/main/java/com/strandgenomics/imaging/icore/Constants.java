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

package com.strandgenomics.imaging.icore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

/**
 * This class holds all constants for needed by iengine
 * @author arunabha
 *
 */
public class Constants {
	
	/**
	 * Name of the admin user 
	 */
	public static final String ADMIN_USER = "administrator";
	
    public static final long MAX_LOG_FILE_SIZE = 1024 * 1024 * 1024; //1 MB
    public static final int  LOG_INTERVALS     = 1; //1 day
    public static final int MAX_TILE_WIDTH = 10000;
    public static final int MAX_TILE_HEIGHT = 10000;
    public static final int MAX_THUMBNAIL_HEIGHT = 5000;
    public static final int MAX_THUMBNAIL_WIDTH = 5000;
    
    /**
	 * interval between two successive executions: 2 Hours
	 */
	public static final long REINDEX_INTERVAL = 2 * 60 * 60 * 1000;
    
	/**
	 * the temporary directory to write to
	 */
	public static final File TEMP_DIR = createTempDir();
    
    /**
     * Returns the value of the specified system property as an integer
     * @param propertyName name of the property
     * @param defaultValue default value of the property
     * @return the value of the specified system property as in integer
     */
    public static int getIntegerProperty(String propertyName, int defaultValue)
    {
    	int value = defaultValue;
    	try
    	{
    		value = Integer.parseInt(System.getProperty(propertyName));
    	}
    	catch(Exception ex)
    	{
    		value = defaultValue;
    	}
    	return value;
    }
    
    public static long getLongProperty(String propertyName, long defaultValue)
    {
    	long value = defaultValue;
    	try
    	{
    		value = Long.parseLong(System.getProperty(propertyName));
    	}
    	catch(Exception ex)
    	{
    		value = defaultValue;
    	}
    	return value;
    }
    
    public static String getStringProperty(String propertyName, String defaultValue)
    {
    	return System.getProperty(propertyName, defaultValue);
    }
    
	private static File createTempDir()
	{
		try
		{
			File tempDir = new File(new File(System.getProperty("java.io.tmpdir")), "avadis_img_cache");
			tempDir.mkdirs(); //create if needed
			return tempDir.getAbsoluteFile();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new File(System.getProperty("java.io.tmpdir")).getAbsoluteFile();
		}
	}
	
	/**
	 * system property accessed the application server context to access web-services
	 */
	public static String getWebApplicationContext() 
	{
		return getStringProperty(Property.APP_SERVER_CONTEXT, "iManage"); 
	}
	
	/**
	 * Returns the directory where LUTs are stored 
	 * @return the directory where LUTs are stored 
	 */
	public static File getLUTDirectory()
	{
		String folderPath = getStringProperty(Property.LUT_LOCATION, null);
		if(folderPath==null) return null;
		
		File storageRoot = new File(folderPath).getAbsoluteFile();
		if(storageRoot.isDirectory())
		{
			return storageRoot;
		}
		else
			throw new RuntimeException("named folder not found "+folderPath);
	}
	
	/**
	 * Returns the root directory where records and their archives will be stored
	 * @return the root directory where records and their archives will be stored
	 */
	public static File getStorageRoot()
	{
		String folderPath = getStringProperty(Property.STORAGE_LOCATION, null);
		try
		{
			File storageRoot = new File(folderPath).getCanonicalFile();
			if(storageRoot.isDirectory())
			{
				return storageRoot;
			}
			else
				throw new FileNotFoundException("named folder not found "+folderPath);
		}
		catch(Exception ex)
		{
			System.err.println("specified storage directory not found "+ex);
			
			File userDir = new File(System.getProperty("user.dir")).getAbsoluteFile();
			File storageRoot = new File(userDir, "avadis_img_storage_root");
			storageRoot.mkdir();
			
			System.err.println("setting storage directory as "+storageRoot);
			return storageRoot;
		}
	}
	
	/**
	 * location where tiles for zoom storage are stored
	 * @return
	 * @throws IOException
	 */
	public static File getZoomStorageLocation() throws IOException
	{
		String folderPath = getStringProperty(Property.ZOOM_STORAGE_LOCATION, "/home/navneet/imanage_storage/zoom_storage");
		System.out.println(folderPath);
		
		File storageRoot = new File(folderPath);
		if (storageRoot.isDirectory())
		{
			return storageRoot.getCanonicalFile();
		}
		else
			throw new FileNotFoundException("named folder not found " + folderPath);
	}
	
	/**
	 * location where projects are archived/backed-up
	 * @return
	 * @throws IOException
	 */
	public static File getArchiveStorageRoot() throws IOException
	{
		String folderPath = getStringProperty(Property.PROJECT_BACKUP_STORAGE_LOCATION, "C:\\iManage\\storage\\backup_storage");
		System.out.println(folderPath);
		
		File storageRoot = new File(folderPath);
		if (storageRoot.isDirectory())
		{
			return storageRoot.getCanonicalFile();
		}
		else
			throw new FileNotFoundException("named folder not found " + folderPath);
	}
	
	/**
	 * system property accessed the system log root directory
	 */
	public static File getLogDirectory()
	{
		String folderPath = getStringProperty(Property.SYSTEM_LOG_DIR, null);
		try
		{
			File logRoot = new File(folderPath).getCanonicalFile();
			if(logRoot.isDirectory())
			{
				return logRoot;
			}
			else
				throw new FileNotFoundException("named folder not found "+folderPath);
		}
		catch(Exception ex)
		{
			System.err.println("specified log directory not found "+ex);
			
			File userDir = new File(System.getProperty("user.dir"));
			File logRoot = new File(userDir, "logs");
			logRoot.mkdir();
			
			System.err.println("setting log directory as "+logRoot);
			return logRoot;
		}
	}
	
	/**
	 * system property specifying the maximum size of a log file after which
	 * a new file will be created in the log root folder
	 */
	public static long getMaximumLogSize()
	{
		return getLongProperty(Property.SYSTEM_LOG_SIZE, 1024*1024);
	}
	
	/**
	 * system property specifying when (days) a new log file will be created
	 * in the log root folder
	 */
	public static int getLogInterval()
	{
		return getIntegerProperty(Property.SYSTEM_LOG_INTERVAL, 1);
	}
	
	/**
	 * system property specifying the log level
	 */
	public static Level getLogLevel()
	{
		try
		{
			return Level.parse(getStringProperty(Property.SYSTEM_LOG_LEVEL, "INFO"));
		}
		catch(Exception ex)
		{
			return Level.INFO;
		}
	}
	
	/**
	 * system property specifying the log scope (class/package)
	 */
	public static String getLogScope()
	{
		return getStringProperty(Property.SYSTEM_LOG_SCOPE, "com.strandgenomics");
	}
	
	/**
	 * system property specifying the log filename prefix
	 */
	public static String getLogFilename()
	{
		return getStringProperty(Property.SYSTEM_LOG_FILENAME, "avadis_imaging_log.txt");
	}
	
    /**
     * system property accessed to create the data access object factory
     */
	public static String getDataAccessObjectFactory()
	{
		return getStringProperty(Property.DAO_FACTORY, null);
	}
	
    /**
     * system property accessed to create the connection provider
     */
	public static String getDatabaseConnectionProvider()
	{
		return getStringProperty(Property.CONNECTION_PROVIDER, null);
	}
	
    /**
     * system property accessed to a pool of threads for record extraction
     */
	public static int getRecordExtractorPoolSize()
	{
		return getIntegerProperty(Property.RECORD_EXTRACTOR_POOL_SIZE, 4);
	}
	
	/**
	 * system property accessed to a pool of threads for compute
	 */
	public static int getComputeThreadPoolSize()
	{
		return getIntegerProperty(Property.ICOMPUTE_THREADPOOL_SIZE, 4);
	}
	
	/**
	 * system property accesses to create cache manager
	 * @return
	 */
	public static String getCacheImplClassName()
	{
		return getStringProperty(Property.CACHE_HANDLER, null);
	}
	
    /**
     * system property accesses to create authentication for local users
     */
	public static String getLocalAuthHandler()
	{
		return getStringProperty(Property.AUTHENTICATION_LOCAL_HANDLER, null);
	}
	
    /**
     * system property accesses to create authentication for external users
     */
	public static String getExternalAuthHandler()
	{
		return getStringProperty(Property.AUTHENTICATION_EXTN_HANDLER, null);
	}
	
	/**
     * system property accesses to create compute manager for local publisher
     */
	public static String getComputeTaskManager()
	{
		return getStringProperty(Property.COMPUTE_TASK_MANAGER, "com.strandgenomics.imaging.icompute.ComputeTaskManager");
	}
	
	/**
     * system property accesses to create compute manager for torque
     */
	public static String getExternalTaskManager()
	{
		return getStringProperty(Property.EXTERNAL_TASK_MANAGER, null);
	}
	
	/**
	 * System property to get name of compute node which is used as job id suffix
	 */
	public static String getExternalComputeNodeName()
	{
		return getStringProperty(Property.EXTERNAL_COMPUTE_NODE_NAME , "");
	}
	
    /**
     * system property accessed to get the width of record thumb-nails
     */
	public static int getRecordThumbnailWidth()
	{
		return getIntegerProperty(Property.RECORD_THUMBNAIL_WIDTH, 120);
	}
	
    /**
     * system property accessed to get the height of record thumb-nails
     */
	public static int getRecordThumbnailHeight()
	{
		return getIntegerProperty(Property.RECORD_THUMBNAIL_HEIGHT, 120);
	}
	
	/**
     * property for the maximum number of active tickets
     */
	public static int getActiveTicketQSize()
	{
		return getIntegerProperty(Property.TICKET_QUEUE_LIMIT, 4);
	}
	
    /**
     * property for for download url prefix - download/query?id=
     */
	public static String getExchangeURLPrefix()
	{
		return getStringProperty(Property.EXCHANGE_URL_PREFIX, "exchange");
	}
	
	/**
     * property for the request to get timeout
     */
	public static int getRequestTimeout()
	{
		return getIntegerProperty(Property.REQUEST_TIMEOUT, 1000*300);
	}

    /**
     * property for for admin email
     */
	public static String getAdminEmail()
	{
		return getStringProperty(Property.ADMIN_EMAIL, "support@strandls.com");
	}
	
	/**
	 * property to access the rmi port for task creation
	 */
	public static int getRMIServicePort()
	{
		return getIntegerProperty(Property.RMI_SERVICE_PORT, 9999);
	}
	
	/**
	 * property to access the rmi port for movie creation
	 * @return
	 */
	public static int getMovieServicePort()
	{
		return getIntegerProperty(Property.MOVIE_SERVICE_PORT, 8888);
	}
	
	/**
	 * returns the port on which memcached server is running
	 * @return
	 */
	public static int getMemcachedPort()
	{
		return getIntegerProperty(Property.MEMCACHED_PORT, 11211);
	}
	
	/**
	 * returns the ip on which memcached server is running
	 * @return
	 */
	public static String getMemcachedIp()
	{
		return getStringProperty(Property.MEMCACHED_IP, "127.0.0.1");
	}
	
	/**
	 * property to access the rmi port for cache service
	 * @return
	 */
	public static int getCacheServicePort()
	{
		return getIntegerProperty(Property.CACHE_SERVICE_PORT, 9996);
	}
	
	/**
	 * property to access the rmi port for remote cache service
	 * @return
	 */
	public static int getRemoteCacheServicePort()
	{
		return getIntegerProperty(Property.REMOTE_CACHE_SERVICE_PORT, 9995);
	}
	
	/**
	 * property to access the rmi port for remote cache service
	 * @return
	 */
	public static String getRemoteCacheServiceHost()
	{
		return getStringProperty(Property.REMOTE_CACHE_SERVICE_HOST, "localhost");
	}
	
	/**
	 * property to access the rmi host for the cache service
	 * @return
	 */
	public static String getCacheServiceHost()
	{
		return getStringProperty(Property.CACHE_SERVICE_HOST, "localhost");
	}
	
	/**
	 * property to access the rmi port for project backup/restoration
	 * @return
	 */
	public static int getBackupServicePort()
	{
		return getIntegerProperty(Property.BACKUP_SERVICE_PORT, 9997);
	}
	
	public static int getUploadServicePort()
	{
		return getIntegerProperty(Property.UPLOAD_SERVICE_PORT, 5555);
	}
	
	/**
	 * property to access the rmi port for movie creation
	 * @return
	 */
	public static int getExportServicePort()
	{
		return getIntegerProperty(Property.EXPORT_SERVICE_PORT, 9998);
	}
	
	/**
	 * property to access the rmi port for movie creation
	 * @return
	 */
	public static int getZoomServicePort()
	{
		return getIntegerProperty(Property.ZOOM_SERVICE_PORT, 7777);
	}
	
	/**
	 * optional system property to access the database driver to local access
	 */
	public static String getDatabaseDriver()
	{
		return getStringProperty(Property.DATABASE_DRIVER, null);
	}
	
	/**
	 * system property to access the database server type (mysql, oracle, postgres etc) for enabling local access
	 */		
	public static String getDatabaseType()
	{
		return getStringProperty(Property.DATABASE_TYPE, "mysql");
	}
	
	/**
	 * system property to access the database locally with connection url 
	 */
	public static String getDatabaseURL()
	{
		return getStringProperty(Property.DATABASE_URL, null);
	}
	
	/**
	 * system property to access the database locally with the database user
	 */
	public static String getDatabaseUser()
	{
		return getStringProperty(Property.DATABASE_USER, null);
	}
	
	/**
	 * system property to access the database locally with the database user's password
	 */
	public static String getDatabasePassword()
	{
		return getStringProperty(Property.DATABASE_PASSWORD, null);
	}
	
	/**
	 * system property to access the database locally with connection url 
	 */
	public static String getStorageDatabaseURL()
	{
		return getStringProperty(Property.STORAGE_DATABASE_URL, null);
	}
	
	/**
	 * system property to access the database locally with the database user
	 */
	public static String getStorageDatabaseUser()
	{
		return getStringProperty(Property.STORAGE_DATABASE_USER, null);
	}
	
	/**
	 * system property to access the database locally with the database user's password
	 */
	public static String getStorageDatabasePassword()
	{
		return getStringProperty(Property.STORAGE_DATABASE_PASSWORD, null);
	}
	
	/**
	 * Returns the url for solr searches
	 * @return the url for solr searches
	 */
	public static String getSolrMainURL()
	{
		return getStringProperty(Property.SOLR_MAIN_URL, "http://localhost:8080/solr/main");
	}
	
	/**
	 * Returns the url for solr reindexing
	 * @return the url for solr reindexing
	 */
	public static String getSolrReindexURL()
	{
		return getStringProperty(Property.SOLR_REINDEX_URL, "http://localhost:8080/solr/reindex");
	}
	
	/**
	 * returns the ldap url
	 * @return
	 */
	public static String getLDAP_URL()
	{
		return getStringProperty(Property.LDAP_URL, null);
	}
	
	/**
	 * returns base DN for searching ldap users
	 * @return
	 */
	public static String getLDAPBaseDN()
	{
		return getStringProperty(Property.LDAP_BASE_DN, null);
	}
	
	/**
	 * returns the ldap url
	 * @return
	 */
	public static String getLDAPGroupDN()
	{
		return getStringProperty(Property.LDAP_GROUP_DN, null);
	}
	
	/**
	 * returns the ldap url
	 * @return
	 */
	public static String getLDAP_CN()
	{
		return getStringProperty(Property.LDAP_CN, null);
	}
	
	/**
	 * returns the ldap url
	 * @return
	 */
	public static String getLDAP_CN_Password()
	{
		return getStringProperty(Property.LDAP_CN_PASSWORD, null);
	}
	
	/**
	 * Returns the reindexing time interval
	 * @return the url for solr searches
	 */
	public static long getSolrReindexingInterval()
	{
		return getLongProperty(Property.SOLR_REINDEX_INTERVAL, REINDEX_INTERVAL);
//		return "http://localhost:8080/solr/core0";
	}
	
	/**
	 * Returns the name of web application
	 * @return the name of web application
	 */
	public static String getWebApplicationName()
	{
		return getStringProperty(Property.WEB_APPLICATION_NAME, "iManage - Image Management System");
	}
	
	/**
	 * Returns the name of web admin application
	 * @return the name of web application
	 */
	public static String getWebAdminApplicationName()
	{
		return getStringProperty(Property.WEB_ADMIN_APPLICATION_NAME, "iManage - Admin");
	}
	
	/**
	 * Returns the web application version
	 * @return the web application version
	 */
	public static String getWebApplicationVersion()
	{
		return getStringProperty(Property.WEB_APPLICATION_VERSION, "1.12");
	}
	
	public static String getLicenseFileLocation()
	{
		return getStringProperty(Property.LICENSE_INFO, "C:\\Users\\anup\\Desktop\\License.info");
	}
	
	public static String getLicenseKeysFileLocation()
	{
		return getStringProperty(Property.LICENSE_KEY, "C:\\Users\\anup\\Desktop\\License.key");
	}
	
	/**
	 * Returns the directory for user configurations
	 * @return the directory for user configurations
	 */
	
	public static String getConfigDirectory()
	{
		
		try
		{
			String userHome = System.getProperty("user.home");
			File confDir = new File(userHome, ".imanage");
			confDir.mkdirs(); //create if needed
			return getStringProperty(Property.SYSTEM_CONF_HOME, confDir.getAbsolutePath());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return getStringProperty(Property.SYSTEM_CONF_HOME, System.getProperty("user.home"));
		}
			
	}
	
	/**
	 * returns number of buffered image reader pools
	 * @return
	 */
	public static int getMaxArchives()
	{
		return getIntegerProperty(Property.BUFFERED_READER_POOL_SIZE, 20);
	}
	
	/**
	 * returns number of buffered image reader pools
	 * @return
	 */
	public static int getBufferedReaderStackSize()
	{
		return getIntegerProperty(Property.BUFFERED_READER_STACK_SIZE, 4);
	}
	
	/**
	 * return max number of image readers
	 * @return
	 */
	public static int getMaxReaders()
	{
		return getIntegerProperty(Property.NUMBER_OF_IMAGE_READERS, 200);
	}
	
	public static int getAcqClientLicenses()
	{
		return getIntegerProperty(Property.NUMBER_OF_ACQ_CLIENT_LICENSES, 50);
	}

	public static int getAcqClientAuthCodeValidity()
	{
	    return getIntegerProperty(Property.ACQ_CLIENT_AUTHCODE_VALID, 7);
	}
	/**
	 * return identifier for the worker
	 */
	public static int getWorkerIdentifier()
	{
		return getIntegerProperty(Property.WORKER_IDENTIFIER, 1);
	}
	
    /**
     * Bunch of system properties to access global system variables
     * Generally global variables are not a good idea, and therefore
     * direct access to these system properties must be avoided
     * @author arunabha
     *
     */
	public static final class Property {
		/**
		 * license properties file for iManage server
		 */
		public static final String LICENSE_INFO = "iengine.license.info";
		
		/**
		 * license encryption key file for iManage
		 */
		public static final String LICENSE_KEY = "iengine.license.key";

		/**
		 * ip on which memcached server is running
		 */
		public static final String MEMCACHED_IP = "iengine.cache.memcached.ip";
		
		/**
		 * port on which memcached server is running
		 */
		public static final String MEMCACHED_PORT = "iengine.cache.memcached.port";

		/**
		 * class handling the cache
		 */
		public static final String CACHE_HANDLER = "iengine.cache.handler";

		/**
		 * total number of acquisition client licenses
		 */
		public static final String NUMBER_OF_ACQ_CLIENT_LICENSES = "iengine.acq.client.licenses";

		/**
		 * storage root where the projects will be archived and backed up from
		 */
		public static final String PROJECT_BACKUP_STORAGE_LOCATION = "iengine.backup.storage.root";

		/**
		 * total number of image readers
		 */
		public static final String NUMBER_OF_IMAGE_READERS = "iengine.max.image.readers";

		/**
		 * number of buffered image readers per archive
		 */
		public static final String BUFFERED_READER_STACK_SIZE = "icore.max.buffered.reader.stack";
		
		/**
		 * number of archives for which buffered reader pools will be active concurrently
		 */
		public static final String BUFFERED_READER_POOL_SIZE = "icore.max.buffered.reader.pool";

		public static final String EXTERNAL_COMPUTE_NODE_NAME = "icompute.external.node.name";

		public static final String ICOMPUTE_THREADPOOL_SIZE = "icompute.threadpool.size";

		/**
		 * host and ports for rmi services
		 */
		public static final String CACHE_SERVICE_HOST = "iengine.cache.host";
		
		public static final String REMOTE_CACHE_SERVICE_HOST = "iengine.remote.cache.host";
		
		public static final String UPLOAD_SERVICE_PORT = "iengine.upload.port";

		public static final String EXPORT_SERVICE_PORT = "iengine.export.port";
		
		public static final String CACHE_SERVICE_PORT = "iengine.cache.port";
		
		public static final String REMOTE_CACHE_SERVICE_PORT = "iengine.remote.cache.port";

		public static final String ZOOM_SERVICE_PORT = "iengine.zoom.port";

		public static final String MOVIE_SERVICE_PORT = "iengine.movie.port";
		
		public static final String BACKUP_SERVICE_PORT = "iengine.project.archival.port";

		/**
		 * system property accessed the application server context to access web-services
		 */
		public static final String APP_SERVER_CONTEXT = "avadis.iserver.context";

		/**
		 * system property accessed the system log root directory
		 */
		public static final String SYSTEM_LOG_DIR = "iengine.log.dir";

		/**
		 * system property specifying the maximum size of a log file after which
		 * a new file will be created in the log root folder
		 */
		public static final String SYSTEM_LOG_SIZE = "iengine.log.size";

		/**
		 * system property specifying when (days) a new log file will be created
		 * in the log root folder
		 */
		public static final String SYSTEM_LOG_INTERVAL = "iengine.log.interval";

		/**
		 * system property specifying the log level
		 */
		public static final String SYSTEM_LOG_LEVEL = "iengine.log.level";

		/**
		 * system property specifying the log scope (class/package)
		 */
		public static final String SYSTEM_LOG_SCOPE = "iengine.log.scope";

		/**
		 * system property specifying the log filename prefix
		 */
		public static final String SYSTEM_LOG_FILENAME = "iengine.log.filename";
		
        /**
         * system property accessed to create the data access object factory
         */
        public static final String DAO_FACTORY = "iengine.dao.factory";
        
        /**
         * system property accessed to create the connection provider
         */
        public static final String CONNECTION_PROVIDER = "iengine.connection.provider";
        
        /**
         * system property accessed to a pool of threads for record extraction
         */
        public static final String RECORD_EXTRACTOR_POOL_SIZE = "iengine.record.extractor.pool.size";
        
        /**
         * system property accesses to create authentication for local users
         */
        public final static String AUTHENTICATION_LOCAL_HANDLER = "iengine.authentication.local.handler";
        /**
         * system property accesses to create authentication for external users
         */
        public final static String AUTHENTICATION_EXTN_HANDLER = "iengine.authentication.extn.handler";
        /**
         * system property accesses to create compute task manager
         */
        public final static String COMPUTE_TASK_MANAGER = "iengine.compute.task.manager";
        /**
         * system property accesses to create external task manager
         */
        public final static String EXTERNAL_TASK_MANAGER = "iengine.external.task.manager";
        /**
         * system property accesses to create external task manager
         */
        public final static String COMPUTE_TASK_TYPE = "iengine.compute.task.type";
        /**
         * system property accessed to get the width of record thumb-nails
         */
        public static final String RECORD_THUMBNAIL_WIDTH = "iengine.record.thumbnail.width";
        /**
         * system property accessed to get the height of record thumb-nails
         */
        public static final String RECORD_THUMBNAIL_HEIGHT = "iengine.record.thumbnail.height";
        /**
         * system property for the maximum number of active tickets
         */
        public static final String TICKET_QUEUE_LIMIT = "iengine.ticket.limit";
        /**
         * system property for for download url prefix - download/query?id=
         */
        public static final String EXCHANGE_URL_PREFIX = "iengine.exchange.url";
        /**
         * system property for for user time out - upload/query?id=
         */
		public static final String REQUEST_TIMEOUT = "iengine.upload.timeout";
        /**
         * system property for for admin email
         */
		public static final String ADMIN_EMAIL = "iengine.admin.email";
		/**
		 * system property to access the rmi port for task creation
		 */
		public static final String RMI_SERVICE_PORT = "iengine.rmi.port";
		/**
		 * optional system property to access the database driver to local access
		 */
		public static final String DATABASE_DRIVER = "iengine.db.driver";
		/**
		 * system property to access the database server type (mysql, oracle, postgres etc) for enabling local access
		 */		
		public static final String DATABASE_TYPE = "iengine.db.type";
		
		
		/**
		 * system property to access the database locally with connection url 
		 */
		public static final String DATABASE_URL = "iengine.db.url";
		/**
		 * system property to access the database locally with the database user
		 */
		public static final String DATABASE_USER = "iengine.db.user";;
		/**
		 * system property to access the database locally with the database user's password
		 */
		public static final String DATABASE_PASSWORD = "iengine.db.password";
		
		/**
		 * system property to access the database locally with connection url 
		 */
		public static final String STORAGE_DATABASE_URL = "iengine.storagedb.url";
		/**
		 * system property to access the database locally with the database user
		 */
		public static final String STORAGE_DATABASE_USER = "iengine.storagedb.user";;
		/**
		 * system property to access the database locally with the database user's password
		 */
		public static final String STORAGE_DATABASE_PASSWORD = "iengine.storagedb.password";
		
		/**
		 * system property to access the LUT storage folder
		 */
		public static final String LUT_LOCATION = "iengine.lut.location";
		/**
		 * system property to access the root storage folder
		 */
		public static final String STORAGE_LOCATION = "iengine.storage.location";
		/**
		 * system property to access the root storage folder
		 */
		public static final String MOVIE_STORAGE_LOCATION = "iengine.movie.location";
		/**
		 * system property to access the export storage folder
		 */
		public static final String EXPORT_STORAGE_LOCATION = "iengine.export.location";
		/**
		 * system property to access the zoom root storage folder
		 */
		public static final String ZOOM_STORAGE_LOCATION = "iengine.zoom.location";
		/**
		 * system property to access the solr main core url for populating and searching
		 */
		public static final String SOLR_MAIN_URL = "iengine.solr.main.url";
		/**
		 * system property to access the solr reindex core url for populating and searching
		 */
		public static final String SOLR_REINDEX_URL = "iengine.solr.reindex.url";
		/**
		 * system property to denote reindexing interval for solr 
		 */
		public static final String SOLR_REINDEX_INTERVAL = "iengine.solr.reindex.interval";
		/**
		 * ldap url
		 */
		public static final String LDAP_URL = "iengine.ldap.url";
		/**
		 * ldap cn
		 */
		public static final String LDAP_CN = "iengine.ldap.cn";
		/**
		 * ldap cn password
		 */
		public static final String LDAP_CN_PASSWORD = "iengine.ldap.cn.password";
		/**
		 * ldap group dn
		 */
		public static final String LDAP_GROUP_DN = "iengine.ldap.group.dn";
		/**
		 * ldap base dn
		 */
		public static final String LDAP_BASE_DN = "iengine.ldap.base.dn";
		
		public static final String SYSTEM_CONF_HOME = "acquisition.conf.home";
		/**
		 * system property to denote movies cleanup interval for timed out movies
		 */
		public static final String MOVIE_CLEANUP_INTERVAL = "iengine.movie.cleanup.interval";
		/**
		 * name of the web client
		 */
		public static final String WEB_APPLICATION_NAME = "iengine.webclient.name";
		/**
		 * name of the web admin client
		 */
		public static final String WEB_ADMIN_APPLICATION_NAME = "iengine.webadmin.client.name";
		/**
		 * version of the web client
		 */
		public static final String WEB_APPLICATION_VERSION = "iengine.webclient.version";
		/**
		 * how long should an authcode generated for acquisition client be valid (in days)
		 */
		public static final String ACQ_CLIENT_AUTHCODE_VALID = "acquisition.authcode.validdays";
		/**
		 * cache allocated for zoom prefetching in MB
		 */
		public static final String ZOOM_CACHE_SIZE = "iengine.zoom.allocated.cache";
		/**
		 * cache allocated for movie prefetching in MB
		 */
		public static final String MOVIE_CACHE_SIZE = "iengine.movie.allocated.cache";
		/**
		 * cache allocated for zoom prefetching in MB
		 */
		public static final String EXPORT_CACHE_SIZE = "iengine.export.allocated.cache";
		/**
		 * location where task logs will be stored
		 */
		public static final String TASK_LOG_STORAGE_LOCATION = "iengine.compute.log.storage.location";
		/**
		 * location where mail.conf file will be stored
		 */
		public static final String MAIL_STORE = "iengine.mail.store.location";
		/**
		 * default admin password
		 */
		public static final String DEFAULT_ADMIN_PASSWORD = "iengine.default.admin.password";
		/**
		 * storage root for image tile cache
		 */
		public static final String IMAGE_CACHE_STORAGE_LOCATION = "iengine.image.cache.storage";
		/**
		 * identifier for the worker
		 */
		public static final String WORKER_IDENTIFIER = "iengine.worker.identifier";
	}
}
