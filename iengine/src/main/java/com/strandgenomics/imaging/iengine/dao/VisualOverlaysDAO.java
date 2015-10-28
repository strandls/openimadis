/*
 * VisualOverlaysDAO.java
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

import java.util.List;

import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;

/**
 * gives access to record visual overlays
 */
public interface VisualOverlaysDAO {
	
	/**
	 * Returns the list of visual annotation names associated with the specified record at the specified site
	 * @param projectID of parent project 
	 * @param guid of specified record
	 * @param siteNo the site number, may be null (implying all overlay names for the record)
	 * @return the list of visual annotation names associated with the specified record at the specified site
	 */
	public List<String> getAvailableVisualOverlays(int projectID, long guid, Integer siteNo)
			throws DataAccessException;
	
    /**
     * Returns the specific of visual overlay associated with the specified coordinate and name 
	 * @param projectID of parent project 
	 * @param guid of specified record
     * @param dimension the image coordinate
     * @param name name of the visual annotation
     * @return the visual overlay associated with the specified dimension and name 
     */
	public VisualOverlay getVisualOverlay(int projectID, long guid, VODimension dimension, String name)
			throws DataAccessException;
	
	
    /**
     * Returns the relevant visual overlays associated the specified dimension of the given record  
	 * @param projectID of parent project 
	 * @param guid of specified record
     * @param dimension the image coordinate
     * @return all relevant visual overlays associated the specified dimension of the given record  
     */
	public List<VisualOverlay> getVisualOverlays(int projectID, long guid, VODimension dimension)
			throws DataAccessException;
	
    /**
     * Creates visual overlays for the specified record for all relevant frames and slices for the fixed site
	 * @param projectID of parent project 
	 * @param guid of specified record
     * @param siteNo the site
     * @param name name of the visual annotation
     * @return a overlay that is created
     */
	public void createVisualOverlays(String actorLogin, int projectID, long guid, int width, int height, int siteNo, String name, boolean isUserGenerated) throws DataAccessException;
	
    /**
     * Deletes all visual overlays for the specified record for all relevant frames and slices for the fixed site
	 * @param projectID of parent project 
	 * @param guid of specified record
     * @param siteNo the site
     * @param name name of the visual annotation
     * @return a overlay that is created
     */
	public void deleteVisualOverlays(int projectID, long guid, int siteNo, String name)
			throws DataAccessException;
	
	/**
	 * Delete all overlay for a record
	 * @param guid
	 * @throws DataAccessException
	 */
	public void deleteVisualOverlaysByGUID(int projectID, long guid) throws DataAccessException;
}
