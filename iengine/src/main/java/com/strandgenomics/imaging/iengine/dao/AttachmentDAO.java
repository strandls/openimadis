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
