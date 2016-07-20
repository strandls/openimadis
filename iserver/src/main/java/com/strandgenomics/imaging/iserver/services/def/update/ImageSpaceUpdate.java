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
 * ImageSpaceUpdateService.java
 *
 * AVADIS Image Management System
 * Web-service definition
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
package com.strandgenomics.imaging.iserver.services.def.update;

import java.rmi.RemoteException;

/**
 * service to update/delete data available in ispace
 * @author arunabha
 *
 */
public interface ImageSpaceUpdate {
	
    /**
     * Returns a URL to upload the specified attachment
     * @param guid the record (it's GUID) under consideration
     * @param name name of the attachment
     * @return a URL to download the specified attachment
     */
    public String getAttachmentUploadURL(String accessToken, long guid, String name) 
    		throws RemoteException;
	
    /**
     * Deletes the specified named attachment
     * @param guid the record (it's signature) containing the attachment
     * @param name name of the attachment
     */
    public void deleteAttachment(String accessToken, long guid, String name) 
    		throws RemoteException;

	/**
	 * Adds or replace the notes associated the specified attachment 
	 * @param guid the record under consideration
	 * @param name name of an existing attachment of the record under consideration
	 * @param notes the note to update
	 */
    public void updateAttachmentNotes(String accessToken, long guid, String name, String notes) 
    		throws RemoteException;
    
    /**
     * updates the specified user integer annotation with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
     * @param guid the record under consideration
     * @param name of the meta-data
     * @param value of the annotation
     */
    public void updateRecordUserAnnotation(String accessToken, long guid, String name, Object value) 
    		throws RemoteException;
    
    /**
     * Deletes all visual overlays for the specified record for all relevant frames and slices for the fixed site
     * @param guid the record signature under consideration 
     * @param siteNo the site
     * @param overlayName name of the visual overlay to delete
     * @return a overlay that is created
     */
	public void deleteVisualOverlays(String accessToken, long guid, int siteNo, String overlayName)
			throws RemoteException;

	/**
	 * Removes the specified visual objects from the visual-overlays of the specified records 
	 * @param guid the record signature under consideration 
	 * @param objectIDs the list of visual objects IDs to delete
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	public void deleteVisualObjects(String accessToken, long guid, 
			int[] objectIDs, String overlayName, VOIndex[] indexes) 
					throws RemoteException;
	
	/**
	 * Removes the specified text labels from the visual-overlays of the specified records 
	 * @param guid the record signature under consideration 
	 * @param objectIDs the list of visual objects IDs to delete
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	public void deleteTextObjects(String accessToken, long guid, 
			int[] objectIDs, String overlayName, VOIndex[] indexes) 
					throws RemoteException;
	
}
