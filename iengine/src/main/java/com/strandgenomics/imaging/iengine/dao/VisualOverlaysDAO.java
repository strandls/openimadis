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
