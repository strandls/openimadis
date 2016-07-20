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
 * VisualObjectsDAO.java
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
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * gives access to record visual overlays
 */
public interface VisualObjectsDAO {
	
	/**
	 * Adds the specified visual objects to the visual-overlays of the specified records 
	 * @param signature the record signature under consideration 
	 * @param vObjects the shapes - visual objects
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	public void addVisualObjects(int projectID, long guid, String overlayName, List<VisualObject> vObjects, 
			VODimension ... imageCoordinates) throws DataAccessException;

	/**
	 * Removes the specified visual objects from the visual-overlays of the specified records 
	 * @param signature the record signature under consideration 
	 * @param vObjects the shapes - visual objects
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	public void deleteVisualObjects(int projectID, long guid, List<VisualObject> vObjects, String overlayName, 
			VODimension ... imageCoordinates) throws DataAccessException;
	
	/**
	 * delete all visual objects for the specified overlays of the specified record
	 * @param projectId of parent project
	 * @param guid the record
	 * @param siteNo the site number of the overlay
	 * @param overlayName name of the overlay
	 * @throws DataAccessException
	 */
	public void deleteVisualObjects(int projectID, long guid, int siteNo, String overlayName)	
			throws DataAccessException;

	/**
	 * delete all visual objects for the specified overlays of the specified record
	 * @param projectId of parent project
	 * @param guid the record
	 * @param d list of dimensions
	 * @param overlayName name of the overlay
	 * @throws DataAccessException
	 */
	public void deleteVisualObjects(int projectID, long guid, String name, VODimension ... d) 
			throws DataAccessException;
	
	/**
	 * delete the specified visual objects (non-text) for the specified overlays of the specified record
	 * @param projectId of parent project
	 * @param guid the record
	 * @param d list of dimensions
	 * @param overlayName name of the overlay
	 * @throws DataAccessException
	 */
	public void deleteVisualObjects(int projectID, long guid, String name, VODimension d, int ... voID)
			throws DataAccessException;
	
	/**
	 * delete the specified text objects for the specified overlays of the specified record
	 * @param projectId of parent project
	 * @param guid the record
	 * @param d list of dimensions
	 * @param overlayName name of the overlay
	 * @throws DataAccessException
	 */
	public void deleteTextObjects(int projectID, long guid, String name, VODimension d, int ... voID)
			throws DataAccessException;
	
	/**
	 * returns the list of all visual objects associated with specified overlay on specified record
	 * @param projectID of parent project
	 * @param overlayId of specified overlay
	 * @return 
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getVisualObjects(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException;

	/**
	 * returns the list of free hand objects associated with specified overlay on specified record
	 * @param projectID of parent project
	 * @param overlayId of specified overlay
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getFreeHandShapes(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException;

	/**
	 * returns the list of rectangular objects associated with specified overlay on specified record
	 * @param projectID of parent project
	 * @param overlayId of specified overlay
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getRectangularShapes(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException;

	/**
	 * returns the list of line segments associated with specified overlay on specified record
	 * @param projectID of parent project
	 * @param overlayId of specified overlay
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getLineSegments(int projectID, long guid, String overlayName, VODimension d)
			throws DataAccessException;

	/**
	 * returns the list of elliptical objects associated with specified overlay on specified record
	 * @param projectID of parent project
	 * @param overlayId of specified overlay
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getEllipticalShapes(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException;
	
	/**
	 * returns the list of text area objects associated with specified overlay on specified record
	 * @param projectID of parent project
	 * @param overlayId of specified overlay
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getTextBoxes(int projectID, long guid, String overlayName, VODimension d) 
			throws DataAccessException;
	
	/**
	 * Returns the messages of all TextBoxes for the specified record 
	 * @param projectID the containing project
	 * @param guid the recordID
	 * @return the list of messages associated with the text-boxes
	 * @throws DataAccessException
	 */
	public String[] getTextBoxeMessages(int projectID, long guid)
			throws DataAccessException;

	/**
	 * inserts list of visual objects into the specified record
	 * @param projectId of parent project
	 * @param overlayID of parent visual overlay
	 * @param shapes to be added
	 * @throws DataAccessException 
	 */
	public void insertVisualObjects(int projectId, long guid, String overlayName, VODimension d, List<VisualObject> shapes) 
			throws DataAccessException;

	/**
	 * update list of visual objects from specified overlay of specified record
	 * @param projectId of parent project
	 * @param overlayID of parent visual overlay
	 * @param shapes to be added
	 * @throws DataAccessException 
	 */
	public void updateVisualOverlay(int projectID, long guid, String overlayName, VODimension d, List<VisualObject> shapes)
			throws DataAccessException;

	/**
	 * find the dimensions where the specified visual object exists
	 * @param projectID of parent project
	 * @param guid of parent record 
	 * @param siteNo of site on which the overlay exists
	 * @param overlayName name of specified overlay
	 * @return the list of locations where the visual object exists
	 * @param id of target visual object 
	 * @throws DataAccessException 
	 */
	public List<VODimension> findVisualObjectLocation(int projectID, long guid, int siteNo, String overlayName, int id) throws DataAccessException;

	/**
	 * find the dimensions where the specified overlay exists (non-empty visual objects)
	 * @param projectID the parent project
	 * @param guid the record under consideration
	 * @param siteNo of specified site on which overlay exists
	 * @param overlayName name of the overlay
	 * @return the dimensions where there are at least one visual objects
	 */
	public List<VODimension> findOverlayLocation(int projectID, long guid, int siteNo, String overlayName) throws DataAccessException;

	/**
	 * find the visual objects within the specified area
	 * @param projectID of the parent project
	 * @param guid of the record under consideration
	 * @param frame
	 * @param slice 
	 * @param site  
	 * @param overlayName name of the overlay
	 * @param x of area under consideration
	 * @param y of area under consideration
	 * @param height of area under consideration
	 * @param width of area under consideration
	 * @return the dimensions where the overlay contains the specified object
	 */
	public List<VisualObject> findVisualObjects(int projectID, long guid, int frame, int slice, int site, String overlayName, int x, int y, int height, int width) throws DataAccessException;

	/**
	 * delete all visual objects for a record
	 * @param projectID
	 * @param guid
	 */
	public void deleteVisualObjectsByGUID(int projectID, long guid) throws DataAccessException;
}