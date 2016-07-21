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

package com.strandgenomics.imaging.iengine.system;

import java.io.IOException;

import com.strandgenomics.imaging.iengine.licensing.LicenseManager;


/**
 * Provider of all System Managers. System Managers are a bunch of singleton objects
 * providing access to the system
 * 
 * @author arunabha
 *
 */
public class SysManagerFactory {
	
	/**
	 * used for synchronizing the creation data access objects
	 */
	private static Object padLock = new Object();
	/**
	 * singleton instance of the user manager
	 */
	private static UserManager userManager = null;
	/**
	 * singleton instance of the security manager
	 */
	private static ISSecurityManager securityManager = null;
	/**
	 * singleton instance of the record manager
	 */
	private static RecordManager recordManager = null;
	/**
	 * singleton instance of the storage manager
	 */
	private static StorageManager storageManager = null;
	/**
	 * singleton instance of the project manager
	 */
	private static ProjectManager projectManager = null;
	/**
	 * singleton instance of the ticketing manager
	 */
	private static TicketManager ticketManager = null;
	/**
	 * singleton instance of the extraction manager
	 */
	private static ExtractionManager extractionManager = null;
	/**
	 * manages images and caches
	 */
	private static ImageManager imageManager = null;
	/**
	 * manages images data download related urls etc
	 */
	private static ImageDataDownloadManager imageDataDownloadManager = null;
	/**
	 * singleton instance of the attachment manager
	 */
	private static AttachmentManager attachmentManager = null;
	/**
	 * singleton instance of the user preferences
	 */
	private static UserPreference userPreference = null;
	/**
	 * singleton instance of the search engine
	 */
    private static SearchEngine searchEngine;
	/**
	 * singleton instance of the search engine
	 */
    private static SolrSearchManager solrEngine;
    /**
     * singleton instance of the bookmark manager
     */
    private static BookmarkManager bookmarkManager;
    /**
     * singleton instance of the user permission manager
     */
    private static UserPermissionManager permissionManager = null;
    /**
     * singleton instance of the user permission manager
     */
	private static ThumbnailManager thumbnailManager;
	/**
	 * singleton instance of the user movie manager
	 */
	private static MovieManager movieManager;
	/**
     * singleton instance of the authorization manager
     */
    private static AuthorizationManager authManager;
    /**
     * there is only one instance of ComputeEngine in the application
     */
    private static ComputeEngine computeEngine = null;
    
    /**
     * singleton instance of TaskManager 
     * */
    private static TaskManager taskManager = null;
    /**
     * singleton instance of the history manager
     */
    private static HistoryManager historyManager = null;
    /**
     * singleton instance of the record creation manager
     */
    private static RecordCreationManager recordCreationManager = null;
    /**
     * singleton instance of the zoom manager
     */
    private static ZoomManager zoomManager = null;
    /**
     * singleton instance of the record export manager
     */
    private static ExportManager exportManager = null;
    /**
     * singleton instance of the unit manager
     */
    private static UnitManager unitManager = null;
    /**
     * singleton instance of the LDAP manager
     */
	private static LDAPManager ldapManager;
    /**
     * singleton instance of the log search manager
     */
	private static LogSearchManager logSearchManager;
	/**
	 * singleton instance of the image readers manager
	 */
	private static ImageReadersManager imageReadersManager;
	/**
	 * singleton instance of the shortcut manager
	 */
	private static ShortcutManager shortcutManager;
	/**
	 * singleton instance of the notification message manager
	 */
	private static NotificationMessageManager notificationMessageManager;
	/**
	 * singleton instance of the backup manager
	 */
	private static BackupManager backupManager;
	/**
	 * singleton instance of the microscope manager
	 */
	private static MicroscopeManager microscopeManager;
	/**
	 * singleton instance of cache manager
	 */
	private static CacheManager cacheManager;
	/**
	 * singleton instance of client manager
	 */
	private static ClientManager clientManager;
	/**
	 * singleton instance of import manager
	 */
	private static ImportManager importManager;
	/**
	 * singleton instance of License Manager
	 */
	private static LicenseManager licenseManager;
	
	/**
	 * singleton instance of largeimage manager
	 */
	private static LargeImageManager largeImageManager;
	
	/**
	 * singleton instance of worker manager
	 */
	private static WorkerManager workerManager;
	
	/**
	 * singleton instance of mosaic manager
	 */
	private static MosaicManager mosaicManager;
	
	/**
	 * Returns the singleton instance of UserManager 
	 * @return the singleton instance of UserManager 
	 */
	public static UserManager getUserManager()
	{
		if(userManager == null)
		{
			synchronized(padLock)
			{
				if(userManager == null)
				{
					UserManager um = new UserManager();
					//now check if the system is initialized
		            um.initialize();
					//finally set to the user manager
					userManager = um;
				}
			}
		}
		return userManager;
	}
	/**
	 * Returns the singleton instance of SecurityManager 
	 * @return the singleton instance of SecurityManager 
	 */
	public static ISSecurityManager getSecurityManager()
	{
		if(securityManager == null)
		{
			synchronized(padLock)
			{
				if(securityManager == null)
				{
					ISSecurityManager iss = new ISSecurityManager();
					securityManager = iss;
				}
			}
		}
		return securityManager;
	}
	/**
	 * Returns the singleton instance of ProjectManager 
	 * @return the singleton instance of ProjectManager 
	 */
	public static ProjectManager getProjectManager()
	{
		if(projectManager == null)
		{
			synchronized(padLock)
			{
				if(projectManager == null)
				{
					ProjectManager pm = new ProjectManager();
					//finally set the user manager
					projectManager = pm;     
				}
			}
		}
		return projectManager;
	}
	/**
	 * Returns the singleton instance of RecordManager 
	 * @return the singleton instance of RecordManager 
	 */
	public static RecordManager getRecordManager()
	{
		if(recordManager == null)
		{
			synchronized(padLock)
			{
				if(recordManager == null)
				{
					RecordManager rm = new RecordManager();
					recordManager = rm;
				}
			}
		}
		return recordManager;
	}
	/**
	 * Returns the singleton instance of StorageManager 
	 * @return the singleton instance of StorageManager 
	 */
	public static StorageManager getStorageManager()
	{
		if(storageManager == null)
		{
			synchronized(padLock)
			{
				if(storageManager == null)
				{
					StorageManager sm = new StorageManager();
					storageManager = sm;
				}
			}
		}
		return storageManager;
	}
	
	/**
	 * Returns the singleton instance of TicketManager 
	 * @return the singleton instance of TicketManager 
	 */
	public static TicketManager getTicketManager()
	{
		if(ticketManager == null)
		{
			synchronized(padLock)
			{
				if(ticketManager == null)
				{
					TicketManager tm = new TicketManager();
					ticketManager = tm;
				}
			}
		}
		return ticketManager;
	}
	
	/**
	 * Returns the singleton instance of TicketManager 
	 * @return the singleton instance of TicketManager 
	 */
	public static ExtractionManager getExtractionManager()
	{
		if(extractionManager == null)
		{
			synchronized(padLock)
			{
				if(extractionManager == null)
				{
					ExtractionManager em = new ExtractionManager();
					extractionManager = em;
				}
			}
		}
		return extractionManager;
	}
	
	public static ImageManager getImageManager()
	{
		if(imageManager == null)
		{
			synchronized(padLock)
			{
				if(imageManager == null)
				{
					ImageManager im = new ImageManager();
					imageManager = im;
				}
			}
		}
		return imageManager;
	}
	
	public static ImageDataDownloadManager getImageDataDownloadManager()
	{
		if(imageDataDownloadManager == null)
		{
			synchronized(padLock)
			{
				if(imageDataDownloadManager == null)
				{
					ImageDataDownloadManager im = new ImageDataDownloadManager();
					imageDataDownloadManager = im;
				}
			}
		}
		return imageDataDownloadManager;
	}
	
	public static AttachmentManager getAttachmentManager()
	{
		if(attachmentManager == null)
		{
			synchronized(padLock)
			{
				if(attachmentManager == null)
				{
					AttachmentManager am = new AttachmentManager();
					attachmentManager = am;
				}
			}
		}
		return attachmentManager;
	}
	
	public static UserPreference getUserPreference()
	{
		if(userPreference == null)
		{
			synchronized(padLock)
			{
				if(userPreference == null)
				{
					UserPreference am = new UserPreference();
					userPreference = am;
				}
			}
		}
		return userPreference;
	}
	
	/**
	 * Get instance of search engine
	 * @return instance of search engine
	 */
	public static SearchEngine getSearchEngine()
	{
	    if(searchEngine == null)
	        {
	            synchronized(padLock)
	            {
	                if(searchEngine == null)
	                {
	                    SearchEngine am = new SearchEngine();
	                    searchEngine = am;
	                }
	            }
	        }
	    return searchEngine;
	}
	
	/**
	 * Get instance of search engine
	 * @return instance of search engine
	 */
	public static SolrSearchManager getSolrSearchEngine()
	{
	    if(solrEngine == null)
	        {
	            synchronized(padLock)
	            {
	                if(solrEngine == null)
	                {
	                	SolrSearchManager am = new SolrSearchManager();
	                    solrEngine = am;
	                }
	            }
	        }
	    return solrEngine;
	}

	/**
     * Get instance of bookmark manager
     * @return instance of bookmark manager
     */
    public static BookmarkManager getBookmarkManager()
    {
        if(bookmarkManager == null)
            {
                synchronized(padLock)
                {
                    if(bookmarkManager == null)
                    {
                        BookmarkManager bm = new BookmarkManager();
                        bookmarkManager = bm;
                    }
                }
            }
        return bookmarkManager;
    }
    
    /**
     * Get instance of user permission manager
     * @return instance of user permission manager
     */
    public static UserPermissionManager getUserPermissionManager()
    {
        if(permissionManager == null)
            {
                synchronized(padLock)
                {
                    if(permissionManager == null)
                    {
                    	UserPermissionManager bm = new UserPermissionManager();
                        permissionManager = bm;
                    }
                }
            }
        return permissionManager;
    }
    
    /**
     * Get instance of thumbnail manager
     * @return instance of thumbnail manager
     */
	public static ThumbnailManager getThumbnailManager()
	{
		if(thumbnailManager == null)
        {
            synchronized(padLock)
            {
                if(thumbnailManager == null)
                {
                	ThumbnailManager bm = new ThumbnailManager();
                	thumbnailManager = bm;
                }
            }
        }
		return thumbnailManager;
	}
	
	/**
     * Get instance of movie manager
     * @return instance of movie manager
     */
	public static MovieManager getMovieManager()
	{
		if(movieManager == null)
        {
            synchronized(padLock)
            {
                if(movieManager == null)
                {
                	MovieManager bm = new MovieManager();
                	movieManager = bm;
                }
            }
        }
		return movieManager;
	}
	
	/**
     * Get instance of authorization manager
     * @return instance of authorization manager
     */
    public static AuthorizationManager getAuthorizationManager()
    {
        if(authManager == null)
        {
            synchronized(padLock)
            {
                if(authManager == null)
                {
                    AuthorizationManager am = new AuthorizationManager();
                    am.registerAcqClient();
                    authManager = am;
                }
            }
        }
        return authManager;
    }

    public static ComputeEngine getComputeEngine()
    {
        if(computeEngine == null)
        {
            synchronized(padLock)
            {
                if(computeEngine == null)
                {
                	ComputeEngine ce = new ComputeEngine();
                    computeEngine = ce;
                }
            }
        }
        return computeEngine;
    }
    
    public static TaskManager getTaskManager()
    {
        if(taskManager == null)
        {
            synchronized(padLock)
            {
                if(taskManager == null)
                {
                	TaskManager tm = new TaskManager();
                    taskManager = tm;
                }
            }
        }
        return taskManager;
    }
    
    /**
     * Get instance of history manager
     * @return instance of history manager
     */
    public static HistoryManager getHistoryManager()
    {
        if(historyManager == null)
        {
            synchronized(padLock)
            {
                if(historyManager == null)
                {
                    HistoryManager hm = new HistoryManager();
                    historyManager = hm;
                }
            }
        }
        return historyManager;
    }
    
    /**
     * Get instance of record creation manager
     * @return instance of record creation manager
     */
    public static RecordCreationManager getRecordCreationManager()
    {
        if(recordCreationManager == null)
        {
            synchronized(padLock)
            {
                if(recordCreationManager == null)
                {
                	RecordCreationManager rm = new RecordCreationManager();
                	recordCreationManager = rm;
                }
            }
        }
        return recordCreationManager;
    }
    
    /**
     * Get instance of zoom manager
     * @return instance of zoom manager
     */
    public static ZoomManager getZoomManager()
    {
        if(zoomManager == null)
        {
            synchronized(padLock)
            {
                if(zoomManager == null)
                {
                	ZoomManager zm = new ZoomManager();
                	zoomManager = zm;
                }
            }
        }
        return zoomManager;
    }
    
    /**
     * Get instance of export manager
     * @return instance of export manager
     */
    public static ExportManager getExportManager()
    {
        if(exportManager == null)
        {
            synchronized(padLock)
            {
                if(exportManager == null)
                {
                	ExportManager em = new ExportManager();
                	exportManager = em;
                }
            }
        }
        return exportManager;
    }
    
    /**
     * Get instance of unit manager
     * @return instance of unit manager
     */
    public static UnitManager getUnitManager()
    {
        if(unitManager == null)
        {
            synchronized(padLock)
            {
                if(unitManager == null)
                {
                	UnitManager em = new UnitManager();
                	unitManager = em;
                }
            }
        }
        return unitManager;
    }
    
    /**
     *  Get instance of LDAP manager
     * @return
     */
	public static LDAPManager getLDAPManager()
	{
		if(ldapManager == null)
        {
            synchronized(padLock)
            {
                if(ldapManager == null)
                {
                	LDAPManager em = new LDAPManager();
                	ldapManager = em;
                }
            }
        }
        return ldapManager;
	}
	
	/**
     *  Get instance of log search manager
     * @return
     */
	public static LogSearchManager getLogSearchManager()
	{
		if(logSearchManager == null)
        {
            synchronized(padLock)
            {
                if(logSearchManager == null)
                {
                	LogSearchManager em = new LogSearchManager();
                	logSearchManager = em;
                }
            }
        }
        return logSearchManager;
	}
	
	public static ImageReadersManager getImageReadersManager()
	{
		if(imageReadersManager == null)
        {
            synchronized(padLock)
            {
                if(imageReadersManager == null)
                {
                	ImageReadersManager em = new ImageReadersManager();
                	imageReadersManager = em;
                }
            }
        }
        return imageReadersManager;
	}
	
	public static ShortcutManager getShortcutManager()
	{
		if(shortcutManager == null)
        {
            synchronized(padLock)
            {
                if(shortcutManager == null)
                {
                	ShortcutManager em = new ShortcutManager();
                	shortcutManager = em;
                }
            }
        }
        return shortcutManager;
	}
	
	public static BackupManager getBackupManager()
	{
		if(backupManager == null)
        {
            synchronized(padLock)
            {
                if(backupManager == null)
                {
                	BackupManager em = new BackupManager();
                	backupManager = em;
                }
            }
        }
        return backupManager;
	}
	
	/**
	 * @return the notificationMessageManager
	 */
	public static NotificationMessageManager getNotificationMessageManager() {
		if(notificationMessageManager == null)
        {
            synchronized(padLock)
            {
                if(notificationMessageManager == null)
                {
                	NotificationMessageManager em = new NotificationMessageManager();
                	notificationMessageManager = em;
                }
            }
        }
		return notificationMessageManager;
	}
	
	/**
	 * returns the microscope manager
	 * @return the microscope manager
	 */
	public static MicroscopeManager getMicroscopeManager()
	{
		if(microscopeManager == null)
        {
            synchronized(padLock)
            {
                if(microscopeManager == null)
                {
                	MicroscopeManager em = new MicroscopeManager();
                	microscopeManager = em;
                }
            }
        }
		return microscopeManager;
	}
	
	public static CacheManager getCacheManager()
	{
		if(cacheManager == null)
        {
            synchronized(padLock)
            {
                if(cacheManager == null)
                {
                	CacheManager em = new CacheManager();
                	cacheManager = em;
                }
            }
        }
		return cacheManager;
	}
	
	public static ClientManager getClientManager()
	{
		if(clientManager == null)
        {
            synchronized(padLock)
            {
                if(clientManager == null)
                {
                	ClientManager em = new ClientManager();
                	clientManager = em;
                }
            }
        }
		return clientManager;
	}
	
	public static ImportManager getImportManager()
	{
		if(importManager == null)
        {
            synchronized(padLock)
            {
                if(importManager == null)
                {
                	ImportManager em = new ImportManager();
                	importManager = em;
                }
            }
        }
		return importManager;
	}
	
	public static LargeImageManager getLargeImageManager() throws IOException
	{
		if(largeImageManager == null)
        {
            synchronized(padLock)
            {
                if(largeImageManager == null)
                {
                	LargeImageManager em = new LargeImageManager();
                	largeImageManager = em;
                }
            }
        }
		return largeImageManager;
	}
	
	public static LicenseManager getLicenseManager() throws IOException
	{
		if(licenseManager == null)
        {
            synchronized(padLock)
            {
                if(licenseManager == null)
                {
                	LicenseManager em = new LicenseManager();
                	licenseManager = em;
                }
            }
        }
		return licenseManager;
	}
	
	public static WorkerManager getWorkerManager() throws IOException
	{
		if(workerManager == null)
        {
            synchronized(padLock)
            {
                if(workerManager == null)
                {
                	WorkerManager em = new WorkerManager();
                	workerManager = em;
                }
            }
        }
		return workerManager;
	}
	
	public static MosaicManager getMosaicManager() throws IOException
	{
		if(mosaicManager == null)
        {
            synchronized(padLock)
            {
                if(workerManager == null)
                {
                	MosaicManager em = new MosaicManager();
                	mosaicManager = em;
                }
            }
        }
		return mosaicManager;
	}	
}

