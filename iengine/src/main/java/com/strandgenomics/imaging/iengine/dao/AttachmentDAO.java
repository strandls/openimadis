/*
 * AttachmentDAO.java
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

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Attachment;

/**
 * gives access to record attachments
 * 
 * @author Anup Kulkarni
 */
public interface AttachmentDAO {
	/**
	 * updates the notes associated with specified attachment
	 * 
	 * @param projectID
	 *            of parent project
	 * @param guid
	 *            of parent record
	 * @param name
	 *            of specified attachment
	 * @param notes
	 *            to be added
	 * @throws DataAccessException
	 */
	public void updateAttachmentNotes(int projectId, long guid, String name, String notes)
			throws DataAccessException;
	
	/**
	 * returns the list of attchments available with specified record
	 * @param projectID of parent project
	 * @param guid of parent record
	 * @return list of attchments available with specified record
	 * @throws DataAccessException
	 */
	public List<Attachment> getRecordAttachments(long guid) 
			throws DataAccessException;
	
	/**
	 * returns the list of attchments available with specified record
	 * @param projectID of parent project
	 * @param guid of parent record
	 * @param attachmentName of particular attachment
	 * @return specified attachment object
	 * @throws DataAccessException
	 */
	public Attachment getRecordAttachment(int projectID, long guid, String attachmentName) 
			throws DataAccessException;

	/**
	 * deletes specified attachment of specified record
	 * @param projectID of parent project
	 * @param guid of parent record
	 * @param name of particular attachment
	 * @throws DataAccessException 
	 */
	public void deleteRecordAttachment(int projectID, long guid, String name)
			throws DataAccessException;

	/**
	 * inserts attachment notes for specified record
	 * @param projectID of parent project
	 * @param guid of parent record
	 * @param name of attachment
	 * @param notes for attachment
	 * @throws DataAccessException 
	 */
	public void insertAttachmentNotes(long guid, String name,
			String notes, String uploadedBy) throws DataAccessException;
}
