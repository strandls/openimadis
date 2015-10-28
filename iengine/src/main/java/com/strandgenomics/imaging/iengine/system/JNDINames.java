/*
 * JNDINames.java
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
package com.strandgenomics.imaging.iengine.system;

/**
 * The InitialContext is configured as a web application is initially deployed,
 * and is made available to web application components (for read-only access).
 * 
 * All configured entries and resources are placed in the java:comp/env portion
 * of the JNDI namespace.
 * 
 * Configure names and values for scalar environment entries that will be exposed
 * to the web application through the JNDI InitialContext
 * <Environment name="maxExemptions" value="10" type="java.lang.Integer" override="false"/>
 * 
 * Configure the name and data type of a resource made available to the application
 * <Resource name="jdbc/EmployeeDB" auth="Container" type="javax.sql.DataSource" description="Employees Database for HR Applications"/>
 * 
 * @author arunabha
 *
 */
public class JNDINames {
	
	public static final String DEFAULT_ADMIN_PASSWORD    = "java:comp/env/param/DefaultAdminPassword";
	
    public static final String ROOT_LOG_FOLDER           = "java:comp/env/param/LogRoot";
    
    public static final String LOG_SCOPE                 = "java:comp/env/param/LogScope";

    public static final String LOG_FILE_SIZE             = "java:comp/env/param/MaxLogSize";

    public static final String LOG_INTERVAL              = "java:comp/env/param/LogInterval";

    public static final String LOG_LEVEL                 = "java:comp/env/param/LogLevel";
    
    public static final String LOG_FILENAME              = "java:comp/env/param/LogFileName";
	
    public static final String SESSION_TIMEOUT           = "java:comp/env/param/SessionTimeout";
    
    public static final String MAIL_STORE                = "java:comp/env/param/MailStore";
    
    public static final String KEY_STORE                 = "java:comp/env/param/KeyStore";
    
    public static final String USER_LOCAL_AUTHENTICATION_CLASS = "java:comp/env/param/LocalAuthenticationClass";
    
    public static final String USER_EXTN_AUTHENTICATION_CLASS = "java:comp/env/param/ExtnAuthenticationClass";
    
    public static final String DAO_FACTORY_CLASS         = "java:comp/env/param/DAOFactoryClass";

	public static final String CONNECTION_PROVIDER_CLASS = "java:comp/env/param/ConnectionProviderClass";

	public static final String STORAGE_LOCATION          = "java:comp/env/param/StorageLocation";
	
	public static final String MOVIE_STORAGE_LOCATION    = "java:comp/env/param/MovieStorageLocation";
	
	public static final String IMAGE_CACHE_STORAGE_LOCATION = "java:comp/env/param/ImageCacheStorageLocation";
	
	public static final String TASK_LOG_STORAGE_LOCATION = "java:comp/env/param/TaskLogStorageLocation";
	
	public static final String PROJECT_BACKUP_STORAGE_LOCATION    = "java:comp/env/param/ProjectBackupStorageLocation";
	
	public static final String EXPORT_STORAGE_LOCATION    = "java:comp/env/param/ExportStorageLocation";
	
	public static final String ZOOM_STORAGE_LOCATION     = "java:comp/env/param/ZoomStorageLocation";
	/**
	 * zoom cache size in MB
	 */
	public static final String ZOOM_CACHE_SIZE           = "java:comp/env/param/ZoomCacheSize";
	/**
	 * movie cache size in MB
	 */
	public static final String MOVIE_CACHE_SIZE          = "java:comp/env/param/MovieCacheSize";
	/**
	 * export cache size in MB
	 */
	public static final String EXPORT_CACHE_SIZE          = "java:comp/env/param/ExportCacheSize";
	
	public static final String EXTRACTION_SERVICE_PORT 	      = "java:comp/env/param/ExtractionServicePort";
	
	public static final String EXPORT_SERVICE_PORT        = "java:comp/env/param/ExportServicePort";
	
	public static final String MOVIE_SERVICE_PORT        = "java:comp/env/param/MovieServicePort";
	
	public static final String BACKUP_SERVICE_PORT = "java:comp/env/param/ProjectBackupServicePort";
	
	public static final String ZOOM_SERVICE_PORT        = "java:comp/env/param/ZoomServicePort";
	
    public static final String DEFAULT_DATASOURCE_REFERENCE  = "java:comp/env/jdbc/imagedb";
    
    public static final String STORAGE_DATASOURCE_REFERENCE  = "java:comp/env/jdbc/image_storagedb";
    
    public static final String DATABASE_TYPE             = "java:comp/env/param/DatabaseType";
    
    public static final String SOLR_URL                  = "java:comp/env/param/solrURL";

	public static final String LUT_LOCAION               = "java:comp/env/param/LutLocation";
	
	public static final String WEB_APPLICATION_NAME               = "java:comp/env/param/WebClientName";
	
	public static final String WEB_ADMIN_APPLICATION_NAME               = "java:comp/env/param/WebAdminClientName";
	
	public static final String WEB_APPLICATION_VERSION               = "java:comp/env/param/WebClientVersion";

	public static final String LDAP_URL = "java:comp/env/param/ldapURL";
	
	public static final String LDAP_CN = "java:comp/env/param/ldapCN";
	
	public static final String LDAP_CN_PASSWORD = "java:comp/env/param/ldapCNPassword";
	
	public static final String LDAP_GROUP_DN = "java:comp/env/param/ldapGroupDN";

	public static final String LDAP_BASE_DN = "java:comp/env/param/ldapBaseDN";

	public static final String BUFFERED_READER_POOL_SIZE = "java:comp/env/param/BufferedReaderPoolSize";
	
	public static final String BUFFERED_READER_STACK_SIZE = "java:comp/env/param/BufferedReaderStackSize";

	public static final String NUMBER_OF_IMAGE_READERS = "java:comp/env/param/MaxImageReaders";

	public static final String NUMBER_OF_ACQ_CLIENT_LICENSES = "java:comp/env/param/AcqClientLicenses";

	public static final String CACHE_SERVICE_PORT = "java:comp/env/param/CacheServicePort";
	
	public static final String CACHE_SERVICE_HOST = "java:comp/env/param/CacheServiceHost";
	
	public static final String REMOTE_CACHE_SERVICE_PORT = "java:comp/env/param/RemoteCacheServicePort";

	public static final String REMOTE_CACHE_SERVICE_HOST = "java:comp/env/param/RemoteCacheServiceHost";
	
	public static final String LICENSE_FILE = "java:comp/env/param/LicenseFile";
	
	public static final String LICENSE_KEY = "java:comp/env/param/LicenseKey";

    private JNDINames(){} // prevent instantiation

}
