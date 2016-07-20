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
 * DBImageSpaceDAOFactory.java
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

import java.util.HashMap;
import java.util.Map;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.AcquisitionProfileDAO;
import com.strandgenomics.imaging.iengine.dao.ActiveTaskDAO;
import com.strandgenomics.imaging.iengine.dao.AllTaskDAO;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ArchivedTaskDAO;
import com.strandgenomics.imaging.iengine.dao.AttachmentDAO;
import com.strandgenomics.imaging.iengine.dao.AuthCodeDAO;
import com.strandgenomics.imaging.iengine.dao.BackupDAO;
import com.strandgenomics.imaging.iengine.dao.BookmarkDAO;
import com.strandgenomics.imaging.iengine.dao.ClientDAO;
import com.strandgenomics.imaging.iengine.dao.ClientTagsDAO;
import com.strandgenomics.imaging.iengine.dao.CreationRequestDAO;
import com.strandgenomics.imaging.iengine.dao.HistoryDAO;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ImageTileCacheDAO;
import com.strandgenomics.imaging.iengine.dao.ImportDAO;
import com.strandgenomics.imaging.iengine.dao.LicenseDAO;
import com.strandgenomics.imaging.iengine.dao.LoginHistoryDAO;
import com.strandgenomics.imaging.iengine.dao.MetaDataDAO;
import com.strandgenomics.imaging.iengine.dao.MicroscopeDAO;
import com.strandgenomics.imaging.iengine.dao.MovieDAO;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectClientDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectCreationDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.dao.PublisherDAO;
import com.strandgenomics.imaging.iengine.dao.RecordCreationDAO;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.RecordExportDAO;
import com.strandgenomics.imaging.iengine.dao.ShortcutDAO;
import com.strandgenomics.imaging.iengine.dao.TaskLogDAO;
import com.strandgenomics.imaging.iengine.dao.ThumbnailDAO;
import com.strandgenomics.imaging.iengine.dao.TicketDAO;
import com.strandgenomics.imaging.iengine.dao.TileDAO;
import com.strandgenomics.imaging.iengine.dao.UnitAssociationDAO;
import com.strandgenomics.imaging.iengine.dao.UnitDAO;
import com.strandgenomics.imaging.iengine.dao.UserCommentDAO;
import com.strandgenomics.imaging.iengine.dao.UserDAO;
import com.strandgenomics.imaging.iengine.dao.UserPreferencesDAO;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysRevisionDAO;
import com.strandgenomics.imaging.iengine.dao.WorkerDAO;
import com.strandgenomics.imaging.iengine.dao.ZoomDAO;

/**
 * A provider of Databased based data access objects
 * @author arunabha
 *
 */
public class DBImageSpaceDAOFactory extends ImageSpaceDAOFactory {
	
	/**
	 * used for synchronizing the creation data access objects
	 */
	private Object padLock = new Object();
    /** 
     * database configuration provider 
     * */
    private final ConnectionProvider connectionProvider;
    /** 
     * singleton userdao instance 
     */
    private UserDAO userDAO = null;
    /** 
     * singleton project dao instance 
     */
    private ProjectDAO projectDAO = null;
    /** 
     * singleton project creation dao instance 
     */
    private ProjectCreationDAO creationDAO = null;
    /** 
     * singleton record dao instance 
     */
    private RecordDAO recordDAO = null;
    /** 
     * singleton archive dao instance 
     */
    private ArchiveDAO archiveDAO = null;
    /**
     * singleton instance of ticket dao
     */
    private TicketDAO ticketDAO = null;
    /**
     * metadata(user-annotations) dao for each type
     */
    private MetaDataDAO intMetaDataDAO = null;
    private MetaDataDAO realMetaDataDAO = null;
    private MetaDataDAO textMetaDataDAO = null;
    private MetaDataDAO timeMetaDataDAO = null;
    /**
     * singleton instance of pixel data dao
     */
    private ImagePixelDataDAO pixelDataDAO = null;
    /**
     * singleton instance of record attachment dao
     */
    private AttachmentDAO attachmentDAO = null;

	/**
	 * map of project specific navigation meta data
	 */
	protected Map<Integer, NavigationDAO> navigationDAOs = null;

    /**
     * singleton instance of visual overlays dao
     */
	private VisualOverlaysDAO visualOverlaysDAO = null;
	
	/**
     * singleton instance of visual overlays dao
     */
	private VisualOverlaysRevisionDAO visualOverlaysRevisionDAO = null;
	
	/**
     * singleton instance of visual objects dao
     */
	private VisualObjectsDAO visualObjectsDAO = null;
	
	/**
     * singleton instance of comment objects dao
     */
	private UserCommentDAO userCommentDAO = null;

	/**
	 * singleton instance of user preference objects dao
	 */
	private UserPreferencesDAO userPreferencesDAO = null;
	
	/**
	 * singleton instance of user channel color choices dao
	 */
	private BookmarkDAO bookmarkDAO = null;
	/**
	 * singleton instance of record thumbnail dao
	 */
	private ThumbnailDAO thumbnailDAO;
	/**
	 * singleton instance of movie dao
	 */
	private MovieDAO movieDAO;
    /**
     * singleton instance of client dao
     */
    private ClientDAO clientDAO;
    /**
     * singleton instance of clienttags dao
     */
    private ClientTagsDAO clientTagsDAO;
    /**
     * singleton instance of authcode dao
     */
    private AuthCodeDAO authCodeDAO;
    /**
     * singleton instance of active task dao
     */
    private ActiveTaskDAO activeTaskDAO;
    /**
     * singleton instance of archived task dao
     */
    private ArchivedTaskDAO archivedTaskDAO;
    /**
     * singleton instance of all task dao
     */
    private AllTaskDAO allTaskDAO;
    /**
     * singleton instance of authorized publisher dao
     */
    private PublisherDAO publisherDAO;
    /**
     * singleton instance of record history dao
     */
    private HistoryDAO historyDAO;
    /**
     * singleton instance of record creation dao
     */
    private RecordCreationDAO recordCreationDAO;
    /**
     * singleton instance of login history dao
     */
    private LoginHistoryDAO loginHistoryDAO;
    /**
     * singleton instance of zoom dao
     */
    private ZoomDAO zoomDAO;
    /**
     * singleton instance of record export dao
     */
    private RecordExportDAO recordExportDAO;
    /**
     * singleton instance of unit dao
     */
    private UnitDAO unitDAO;
    /**
     * singleton instance of unit association dao
     */
    private UnitAssociationDAO unitAssociationDAO;    
    /**
     * singleton instance of shortcut dao
     */
    private ShortcutDAO shortcutDAO;
    /**
     * singleton instance of backup dao
     */
    private BackupDAO backupDAO;
    /**
     * singleton instance of microscope dao
     */
    private MicroscopeDAO microscopeDAO;
    /**
     * singleton instance of acq profile dao
     */
    private AcquisitionProfileDAO acquisitionProfileDAO;
    /**
     * singleton instance of ticket request dao
     */
    private CreationRequestDAO creationRequestDAO;
    /**
     * singleton instance of acquisition license dao
     */
    private LicenseDAO licenseDAO;
    /**
     * singleton instance of task log dao
     */
    private TaskLogDAO taskLogDAO;
    /**
     * singleton instance of image tile cache dao
     */
    private ImageTileCacheDAO imageTileCacheDAO;
    /**
     * singleton instance of user client dao
     */
    private ProjectClientDAO projectClientDAO;
    /**
     * singleton instance of import dao
     */
    private ImportDAO importDAO;
    /**
     * singleton instance of tile dao
     */
    private TileDAO tileDAO;
    /**
     * singleton instance of worker dao
     */
    private WorkerDAO workerDAO;
    
    /**
     * Uses system property "iengine.connection.provider" to load appropriate
     * ConnectionProvider implementation.
     */
    public DBImageSpaceDAOFactory() 
    {
    	navigationDAOs = new HashMap<Integer, NavigationDAO>();
        try 
        {
            String configClassName = Constants.getDatabaseConnectionProvider();
            connectionProvider = (ConnectionProvider) Class.forName(configClassName).newInstance();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace(System.err);
            throw new NullPointerException("config class not specified/found");
        }
    }
    
    /**
     * Creates a data access object factory with the specified connection provider 
     * @param dbsource the connection provider instance
     */
    public DBImageSpaceDAOFactory(ConnectionProvider dbsource) 
    {
    	connectionProvider = dbsource;
    }

	@Override
    public UserDAO getUserDAO()
    {
        if(userDAO == null) 
        {
            synchronized(padLock)
            {
                if(userDAO == null) 
                {
                    userDAO = new DBUserDAO(this, connectionProvider);
                }
            }
        }
        return userDAO;
    }
	
	@Override
    public ProjectDAO getProjectDAO()
    {
        if(projectDAO == null) 
        {
            synchronized(padLock)
            {
                if(projectDAO == null) 
                {
                	projectDAO = new DBProjectDAO(this, connectionProvider);
                }
            }
        }
        return projectDAO;
    }
	
    @Override
	public ProjectCreationDAO getProjectCreationDAO()
    {
        if(creationDAO == null) 
        {
            synchronized(padLock)
            {
                if(creationDAO == null) 
                {
                	creationDAO = new DBProjectCreationDAO(this, connectionProvider);
                }
            }
        }
        return creationDAO;
    }
	
	@Override
    public RecordDAO getRecordDAO()
    {
        if(recordDAO == null) 
        {
            synchronized(padLock)
            {
                if(recordDAO == null) 
                {
                	recordDAO = new DBRecordDAO(this, connectionProvider);
                }
            }
        }
        return recordDAO;
    }
	
	@Override
	public ArchiveDAO getArchiveDAO()
	{
        if(archiveDAO == null) 
        {
            synchronized(padLock)
            {
                if(archiveDAO == null) 
                {
                	archiveDAO = new DBArchiveDAO(this, connectionProvider);
                }
            }
        }
        return archiveDAO;
	}
	
	@Override
	public TicketDAO getTicketDAO()
	{
        if(ticketDAO == null) 
        {
            synchronized(padLock)
            {
                if(ticketDAO == null) 
                {
                	ticketDAO = new DBTicketDAO(this, connectionProvider);
                }
            }
        }
        return ticketDAO;
	}
	
    /**
     * Returns the dao to manage tickets 
     * @return the dao to manage tickets 
     */
	public MetaDataDAO getMetaDataDAO(AnnotationType type)
	{
		switch(type)
		{
		case Integer:
		        if(intMetaDataDAO == null) 
		        {
		            synchronized(padLock)
		            {
		                if(intMetaDataDAO == null) 
		                {
		                	intMetaDataDAO = new DBMetaDataDAO(this, connectionProvider, type);
		                }
		            }
		        }
		        return intMetaDataDAO;
		        
		case Real:
		        if(realMetaDataDAO == null) 
		        {
		            synchronized(padLock)
		            {
		                if(realMetaDataDAO == null) 
		                {
		                	realMetaDataDAO = new DBMetaDataDAO(this, connectionProvider, type);
		                }
		            }
		        }
		        return realMetaDataDAO;
		        
		case Text:
		        if(textMetaDataDAO == null) 
		        {
		            synchronized(padLock)
		            {
		                if(textMetaDataDAO == null) 
		                {
		                	textMetaDataDAO = new DBMetaDataDAO(this, connectionProvider, type);
		                }
		            }
		        }
		        return textMetaDataDAO;
		        
		case Time:
	        if(timeMetaDataDAO == null) 
	        {
	            synchronized(padLock)
	            {
	                if(timeMetaDataDAO == null) 
	                {
	                	timeMetaDataDAO = new DBMetaDataDAO(this, connectionProvider, type);
	                }
	            }
	        }
	        return timeMetaDataDAO;
		}
		
		return null;
		
	}

	@Override
	public ImagePixelDataDAO getImagePixelDataDAO() {
		if(pixelDataDAO == null) 
        {
            synchronized(padLock)
            {
                if(pixelDataDAO == null) 
                {
                	pixelDataDAO = new DBImagePixelDataDAO(this, connectionProvider);
                }
            }
        }
        return pixelDataDAO;
	}

	@Override
	public AttachmentDAO getAttachmentDAO() {
		if(attachmentDAO == null) 
        {
            synchronized(padLock)
            {
                if(attachmentDAO == null) 
                {
                	attachmentDAO = new DBAttachmentDAO(this, connectionProvider);
                }
            }
        }
        return attachmentDAO;
	}
	
	@Override
	public synchronized NavigationDAO getNavigationDAO(int projectID) throws DataAccessException
	{
		if(navigationDAOs.containsKey(projectID))
			return navigationDAOs.get(projectID);
		
		NavigationDAO dao = new DBNavigationDAO(this, connectionProvider, projectID);
		navigationDAOs.put(projectID, dao);
		
		return dao;
	}

	@Override
	public VisualOverlaysDAO getVisualOverlaysDAO() 
	{
		if(visualOverlaysDAO == null) 
        {
            synchronized(padLock)
            {
                if(visualOverlaysDAO == null) 
                {
                	visualOverlaysDAO = new DBVisualOverlaysDAO(this, connectionProvider);
                }
            }
        }
        return visualOverlaysDAO;
	}

	@Override
	public VisualOverlaysRevisionDAO getVisualOverlaysRevisionDAO() 
	{
		if(visualOverlaysRevisionDAO == null) 
        {
            synchronized(padLock)
            {
                if(visualOverlaysRevisionDAO == null) 
                {
                	visualOverlaysRevisionDAO = new DBVisualOverlaysRevisionDAO(this, connectionProvider);
                }
            }
        }
        return visualOverlaysRevisionDAO;
	}

	@Override
	public VisualObjectsDAO getVisualObjectsDAO() 
	{
		if(visualObjectsDAO == null) 
        {
            synchronized(padLock)
            {
                if(visualObjectsDAO == null) 
                {
                	visualObjectsDAO = new DBVisualObjectsDAO(this, connectionProvider);
                }
            }
        }
        return visualObjectsDAO;
	}
	
	@Override
	public UserCommentDAO getUserCommentDAO()
	{
		if(userCommentDAO == null) 
        {
            synchronized(padLock)
            {
                if(userCommentDAO == null) 
                {
                	userCommentDAO = new DBUserCommentDAO(this, connectionProvider);
                }
            }
        }
        return userCommentDAO;
	}

	@Override
	public UserPreferencesDAO getUserPreferencesDAO() {
		if(userPreferencesDAO == null) 
        {
            synchronized(padLock)
            {
                if(userPreferencesDAO == null) 
                {
                	userPreferencesDAO = new DBUserPreferencesDAO(this, connectionProvider);
                }
            }
        }
        return userPreferencesDAO;
	}

	@Override
	public BookmarkDAO getBookmarkDAO()
	{
		if(bookmarkDAO == null) 
        {
            synchronized(padLock)
            {
                if(bookmarkDAO == null) 
                {
                	bookmarkDAO = new DBBookmarkDAO(this, connectionProvider);
                }
            }
        }
        return bookmarkDAO;
	}

	@Override
	public ThumbnailDAO getThumbnailDAO()
	{
		if(thumbnailDAO == null) 
        {
            synchronized(padLock)
            {
                if(thumbnailDAO == null) 
                {
                	thumbnailDAO = new DBThumbnailDAO(this, connectionProvider);
                }
            }
        }
        return thumbnailDAO;
	}

	@Override
	public MovieDAO getMovieDAO()
	{
		if(movieDAO == null) 
        {
            synchronized(padLock)
            {
                if(movieDAO == null) 
                {
                	movieDAO = new DBMovieDAO(this, connectionProvider);
                }
            }
        }
        return movieDAO;
	}
	
	@Override
	public ClientDAO getClientDAO() 
	{
	    if (clientDAO == null)
	    {
	        synchronized(padLock)
            {
            if(clientDAO == null) 
            {
                clientDAO = new DBClientDAO(this, connectionProvider);
            }
        }

	    }
	    return clientDAO;
	}
	
	@Override
    public AuthCodeDAO getAuthCodeDAO()
    {
        if (authCodeDAO == null)
        {
            synchronized(padLock)
            {
            if(authCodeDAO == null) 
            {
                authCodeDAO = new DBAuthCodeDAO(this, connectionProvider);
            }
        }

        }
        return authCodeDAO;
    }
	
	@Override
    public ActiveTaskDAO getActiveTaskDAO()
    {
		if (activeTaskDAO == null)
		{
			synchronized (padLock)
			{
				if (activeTaskDAO == null)
				{
					activeTaskDAO = new DBActiveTaskDAO(this,
							connectionProvider);
				}
			}

		}
		return activeTaskDAO;
    }
	
	@Override
    public ArchivedTaskDAO getArchivedTaskDAO()
    {
        if (archivedTaskDAO == null)
        {
            synchronized(padLock)
            {
            if(archivedTaskDAO == null) 
            {
            	archivedTaskDAO = new DBArchivedTaskDAO(this, connectionProvider);
            }
        }

        }
        return archivedTaskDAO;
    }
	
	@Override
    public AllTaskDAO getAllTaskDAO()
    {
        if (allTaskDAO == null)
        {
            synchronized(padLock)
            {
            if(allTaskDAO == null) 
            {
            	allTaskDAO = new DBAllTaskDAO(this, connectionProvider);
            }
        }

        }
        return allTaskDAO;
    }

	@Override
	public PublisherDAO getPublisherDAO()
	{
		if (publisherDAO == null)
		{
			synchronized (padLock)
			{
				if (publisherDAO == null)
				{
					publisherDAO = new DBPublisherDAO(this, connectionProvider);
				}
			}

		}
		return publisherDAO;
	}
	
	@Override
	public HistoryDAO getHistoryDAO()
	{
		if (historyDAO == null)
		{
			synchronized (padLock)
			{
				if (historyDAO == null)
				{
					historyDAO = new DBHistoryDAO(this, connectionProvider);
				}
			}

		}
		return historyDAO;
	}

	@Override
	public RecordCreationDAO getRecordCreationDAO()
	{
		if (recordCreationDAO == null)
		{
			synchronized (padLock)
			{
				if (recordCreationDAO == null)
				{
					recordCreationDAO = new DBRecordCreationDAO(this, connectionProvider);
				}
			}

		}
		return recordCreationDAO;
	}
	
	@Override
	public LoginHistoryDAO getLoginHistoryDAO()
	{
		if (loginHistoryDAO == null)
		{
			synchronized (padLock)
			{
				if (loginHistoryDAO == null)
				{
					loginHistoryDAO = new DBLoginHistoryDAO(this, connectionProvider);
				}
			}

		}
		return loginHistoryDAO;
	}

	@Override
	public ZoomDAO getZoomDAO()
	{
		if (zoomDAO == null)
		{
			synchronized (padLock)
			{
				if (zoomDAO == null)
				{
					zoomDAO = new DBZoomDAO(this, connectionProvider);
				}
			}

		}
		return zoomDAO;
	}

	@Override
	public RecordExportDAO getRecordExportDAO()
	{
		if (recordExportDAO == null)
		{
			synchronized (padLock)
			{
				if (recordExportDAO == null)
				{
					recordExportDAO = new DBRecordExportDAO(this, connectionProvider);
				}
			}

		}
		return recordExportDAO;
	}
	
	@Override
	public UnitDAO getUnitDAO()
	{
		if (unitDAO == null)
		{
			synchronized (padLock)
			{
				if (unitDAO == null)
				{
					unitDAO = new DBUnitDAO(this, connectionProvider);
				}
			}

		}
		return unitDAO;
	}
	
	@Override
	public UnitAssociationDAO getUnitAssociationDAO()
	{
		if (unitAssociationDAO == null)
		{
			synchronized (padLock)
			{
				if (unitAssociationDAO == null)
				{
					unitAssociationDAO = new DBUnitAssociationDAO(this, connectionProvider);
				}
			}

		}
		return unitAssociationDAO;
	}
	
	@Override
	public ShortcutDAO getShortcutDAO()
	{
		if (shortcutDAO == null)
		{
			synchronized (padLock)
			{
				if (shortcutDAO == null)
				{
					shortcutDAO = new DBShortcutDAO(this, connectionProvider);
				}
			}

		}
		return shortcutDAO;
	}

	@Override
	public BackupDAO getBackupDAO()
	{
		if (backupDAO == null)
		{
			synchronized (padLock)
			{
				if (backupDAO == null)
				{
					backupDAO = new DBBackupDAO(this, connectionProvider);
				}
			}

		}
		return backupDAO;
	}
	
	@Override
	public MicroscopeDAO getMicroscopeDAO()
	{
		if (microscopeDAO == null)
		{
			synchronized (padLock)
			{
				if (microscopeDAO == null)
				{
					microscopeDAO = new DBMicroscopeDAO(this, connectionProvider);
				}
			}

		}
		return microscopeDAO;
	}
	
	@Override
	public AcquisitionProfileDAO getAcquisitionProfileDAO()
	{
		if (acquisitionProfileDAO == null)
		{
			synchronized (padLock)
			{
				if (acquisitionProfileDAO == null)
				{
					acquisitionProfileDAO = new DBAcquisitionProfileDAO(this, connectionProvider);
				}
			}

		}
		return acquisitionProfileDAO;
	}

	@Override
	public CreationRequestDAO getCreationRequestDAO()
	{
		if (creationRequestDAO == null)
		{
			synchronized (padLock)
			{
				if (creationRequestDAO == null)
				{
					creationRequestDAO = new DBCreationRequestDAO(this, connectionProvider);
				}
			}

		}
		return creationRequestDAO;
	}
	
	@Override
	public LicenseDAO getLicenseDAO()
	{
		if (licenseDAO == null)
		{
			synchronized (padLock)
			{
				if (licenseDAO == null)
				{
					licenseDAO = new DBLicenseDAO(this, connectionProvider);
				}
			}

		}
		return licenseDAO;
	}

	@Override
	public TaskLogDAO getTaskLogDAO()
	{
		if (taskLogDAO == null)
		{
			synchronized (padLock)
			{
				if (taskLogDAO == null)
				{
					taskLogDAO = new DBTaskLogDAO(this, connectionProvider);
				}
			}

		}
		return taskLogDAO;
	}

	@Override
	public ImageTileCacheDAO getImageTileCacheDAO()
	{
		if (imageTileCacheDAO == null)
		{
			synchronized (padLock)
			{
				if (imageTileCacheDAO == null)
				{
					imageTileCacheDAO = new DBImageTileCacheDAO(this, connectionProvider);
				}
			}

		}
		return imageTileCacheDAO;
	}

	@Override
	public ProjectClientDAO getProjectClientDAO() {
		if (projectClientDAO == null)
		{
			synchronized (padLock)
			{
				if (projectClientDAO == null)
				{
					projectClientDAO = new DBProjectClientDAO(this, connectionProvider);
				}
			}

		}
		return projectClientDAO;
	}
	
	@Override
	public ClientTagsDAO getClientTagsDAO() {
		if (clientTagsDAO == null)
		{
			synchronized (padLock)
			{
				if (clientTagsDAO == null)
				{
					clientTagsDAO = new DBClientTagsDAO(this, connectionProvider);
				}
			}

		}
		return clientTagsDAO;
	}
	
	@Override
	public ImportDAO getImportDAO() {
		if (importDAO == null)
		{
			synchronized (padLock)
			{
				if (importDAO == null)
				{
					importDAO = new DBImportDAO(this, connectionProvider);
				}
			}

		}
		return importDAO;
	}

	@Override
	public TileDAO getTileDAO() {
		if (tileDAO == null)
		{
			synchronized (padLock)
			{
				if (tileDAO == null)
				{
					tileDAO = new DBTileDAO(this, connectionProvider);
				}
			}

		}
		return tileDAO;
	}

	@Override
	public WorkerDAO getWorkerDAO() {
		if (workerDAO == null)
		{
			synchronized (padLock)
			{
				if (workerDAO == null)
				{
					workerDAO = new DBWorkerDAO(this, connectionProvider);
				}
			}

		}
		return workerDAO;
	}
}
