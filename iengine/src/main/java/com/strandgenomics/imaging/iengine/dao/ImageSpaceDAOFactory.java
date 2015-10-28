/*
 * ImageSpaceDAOFactory.java
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
package com.strandgenomics.imaging.iengine.dao;

import java.lang.reflect.Constructor;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;

/**
 * Provider of all relevant data access objects needs by Avadis ImageSpace 
 * @author arunabha
 *
 */
public abstract class ImageSpaceDAOFactory {
	
    /**
     * singleton factory instance
     */
    private static ImageSpaceDAOFactory factoryInstance = null;
    private static Object padLock = new Object();
	
    /**
     * Uses system property "iengine.dao.factory" to load appropriate
     * DAO Factory implementation.
     */
    public static ImageSpaceDAOFactory getDAOFactory() 
    {
        if(factoryInstance == null) 
        {
            synchronized(padLock)
            {
                if(factoryInstance == null) 
                {
                    String factoryClassName = Constants.getDataAccessObjectFactory();
                    System.out.println("trying to load dao factory class "+factoryClassName);
                    try 
                    {
                    	ImageSpaceDAOFactory temp = (ImageSpaceDAOFactory) Class.forName(factoryClassName).newInstance();
                    	factoryInstance = temp;
                    }
                    catch (Exception se) 
                    {
                    	se.printStackTrace();
                        throw new RuntimeException("error while getting DAO factory : \n" + se.getMessage());
                    }
                }
            }
        }
        return factoryInstance;
    }
    
    /**
     * Uses the specified DBConfig Object to load appropriate
     * DAO Factory implementation.
     */
    public static ImageSpaceDAOFactory getDAOFactory(ConnectionProvider dbsource)
            throws DataAccessException {

        String factoryClassName = System.getProperty(Constants.getDataAccessObjectFactory());
        ImageSpaceDAOFactory factory = null;

        try {
            Class<?> factoryClazz = Class.forName(factoryClassName);

            Class<?>[] constructorSignature = {ConnectionProvider.class};
            //constructor parameters
            Object[] constructorParameters = {dbsource};
            // get the appropriate constructor
            Constructor<?> factoryConstructor = factoryClazz.getConstructor(constructorSignature);
            // call the constructor
            factory = (ImageSpaceDAOFactory)factoryConstructor.newInstance(constructorParameters);
        }
        catch (Exception se) 
        {
            throw new DataAccessException("error while getting DAO factory : \n" + se.getMessage());
        }
        return factory;
    }
    
    public ImageSpaceDAOFactory()
    {}
    
    /**
     * Returns the data access objects to handle user management
     * @return the data access objects to handle user management
     */
    public abstract UserDAO getUserDAO();
    
    /**
     * Returns the data access objects to handle project management
     * @return the data access objects to handle project management
     */
    public abstract ProjectDAO getProjectDAO();
    
    /**
     * Returns the data access objects to handle record related queries
     * @return the data access objects to handle record related queries
     */
    public abstract RecordDAO getRecordDAO();

    /**
     * Returns the data access objects to handle archive related queries
     * @return the data access objects to handle archive related queries
     */
    public abstract ArchiveDAO getArchiveDAO();

    /**
     * Returns the dao to create projects 
     * @return the dao to create projects 
     */
	public abstract ProjectCreationDAO getProjectCreationDAO();
	
    /**
     * Returns the dao to manage tickets 
     * @return the dao to manage tickets 
     */
	public abstract TicketDAO getTicketDAO();
	
    /**
     * Returns the dao to manage user annotation 
     * @return the dao to manage user annotation 
     */
	public abstract MetaDataDAO getMetaDataDAO(AnnotationType type);
	
	/**
	 * returns the dao to manage pixel data
	 * @return the dao to manage pixel data
	 */
	public abstract ImagePixelDataDAO getImagePixelDataDAO();

	/**
	 * returns the dao to manage record attachments
	 * @return the dao to manage record attachments
	 */
	public abstract AttachmentDAO getAttachmentDAO();
	
	/**
	 * returns the dao to manage the navigation table
	 * @return the navigation table handler for the specified project
	 * @throws DataAccessException 
	 */
	public abstract NavigationDAO getNavigationDAO(int projectID) 
			throws DataAccessException;
	
	/**
	 * returns the dao to manage record visual overlays
	 * @return the dao to manage record visual overlays
	 */
	public abstract VisualOverlaysDAO getVisualOverlaysDAO();

	/**
	 * returns the dao to manage revisions for record visual overlays
	 * @return dao to manage revisions for record visual overlays
	 */
	public abstract VisualOverlaysRevisionDAO getVisualOverlaysRevisionDAO();

	/**
	 * returns the dao to manage record visual objects
	 * @return the dao to manage record visual objects
	 */
	public abstract VisualObjectsDAO getVisualObjectsDAO();

	/**
	 * returns the dao to manager comments
	 * @return the dao to manager comments
	 */
	public abstract UserCommentDAO getUserCommentDAO();
	
	/**
	 * returnts the dao to manage user preferences
	 * @return the dao to manage user preferences
	 */
	public abstract UserPreferencesDAO getUserPreferencesDAO();
	
	/**
	 * returns the dao to manage user bookmarks
	 * @return the dao to manage user bookmarks
	 */
	public abstract BookmarkDAO getBookmarkDAO();

	/**
	 * returns the dao to manage record thumbnails
	 * @return the dao to manage user bookmarks
	 */
	public abstract ThumbnailDAO getThumbnailDAO();

	/**
	 * returns the dao to manage movie images
	 * @return the dao to manage user bookmarks
	 */
	public abstract MovieDAO getMovieDAO();
	
	/**
	 * get the dao to manage clients
	 * @return dao to manage clients
	 */
	public abstract ClientDAO getClientDAO();
	
	/**
	 * get the dao to manage clienttags
	 * @return dao to manage clienttags
	 */
	public abstract ClientTagsDAO getClientTagsDAO();
	
	/**
	 * get the dao to manage authcodes
	 * @return dao to manage authcodes
	 */
	public abstract AuthCodeDAO getAuthCodeDAO();

	/**
	 * get the dao to active tasks
	 * @return dao to active tasks
	 */
	public abstract ActiveTaskDAO getActiveTaskDAO();
	
	/**
	 * get the dao to archived tasks
	 * @return dao to archived tasks
	 */
	public abstract ArchivedTaskDAO getArchivedTaskDAO();
	
	
	/**
	 * get the dao to all tasks
	 * @return dao to all tasks
	 */
	public abstract AllTaskDAO getAllTaskDAO();

	/**
	 * get the dao to authorized publishers
	 * @return dao to authorized publishers
	 */
	public abstract PublisherDAO getPublisherDAO();
	
	/**
	 * get the dao to handle record history
	 * @return dao to handle record history
	 */
	public abstract HistoryDAO getHistoryDAO();

	/**
	 * get the dao to record builder
	 * @return dao to record builder
	 */
	public abstract RecordCreationDAO getRecordCreationDAO();

	/**
	 * get the dao to login history
	 * @return dao to login history
	 */
	public abstract LoginHistoryDAO getLoginHistoryDAO();

	/**
	 * get the dao to manage zoom
	 * @return the dao to manage zoom
	 */
	public abstract ZoomDAO getZoomDAO();

	/**
	 * get the dao to manage record export
	 * @return the dao to manage record export
	 */
	public abstract RecordExportDAO getRecordExportDAO();

	/**
	 * get the dao to manage units
	 * @return the dao to manage units
	 */
	public abstract UnitDAO getUnitDAO();
	
	/**
	 * get the dao to manage units associations
	 * @return the dao to manage units associations
	 */
	public abstract UnitAssociationDAO getUnitAssociationDAO();

	/**
	 * get the dao to manage shortcuts
	 * @return the dao to manage shortcuts
	 */
	public abstract ShortcutDAO getShortcutDAO();

	/**
	 * get the dao to manage the project backup
	 * @return the dao to manage the project backup
	 */
	public abstract BackupDAO getBackupDAO();

	/**
	 * get the dao to manage microscopes
	 * @return the dao to manage the microscope
	 */
	public abstract MicroscopeDAO getMicroscopeDAO();

	/**
	 * get the dao to manage acq profile
	 * @return the dao to manage the microscope
	 */
	public abstract AcquisitionProfileDAO getAcquisitionProfileDAO();
	
	/**
	 * get the dao to manage ticket requests
	 * @return the dao to manage ticket requests
	 */
	public abstract CreationRequestDAO getCreationRequestDAO();

	/**
	 * get the dao to manage acquisition licenses
	 * @return the dao to manage acquisition licenses
	 */
	public abstract LicenseDAO getLicenseDAO();

	/**
	 * get the dao to manage the task logs
	 * @return the dao to manage task logs
	 */
	public abstract TaskLogDAO getTaskLogDAO();

	/**
	 * get the dao to manage the image tile cache
	 * @return the dao to manage the image tile cache
	 */
	public abstract ImageTileCacheDAO getImageTileCacheDAO();
	
	/**
	 * get the dao to manage the user client registry
	 * @retrun the dao to manage the user client registry
	 */
	public abstract ProjectClientDAO getProjectClientDAO();

	/**
	 * get the dao to manage the user import registry
	 * @retrun the dao to manage the user import registry
	 */
	public abstract ImportDAO getImportDAO();
	
	/**
	 * get the dao to manage the tile registry
	 * @return
	 */
	public abstract TileDAO getTileDAO();
	
	/**
	 * get dao to manage worker status registry
	 * @return
	 */
	public abstract WorkerDAO getWorkerDAO();
}
