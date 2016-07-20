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
 * DBAttachmentDAO.java
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

import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.AttachmentDAO;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.AttachmentType;

/**
 * Gives access to record attachments
 * 
 * @author Anup Kulkarni
 * 
 */
public class DBAttachmentDAO extends ImageSpaceDAO<Attachment> implements AttachmentDAO {
	
	DBAttachmentDAO(DBImageSpaceDAOFactory dbImageSpaceDAOFactory, ConnectionProvider connectionProvider) 
	{
		super(dbImageSpaceDAOFactory, connectionProvider, "AttachmentDAO.xml");
	}
	
	@Override
	public void updateAttachmentNotes(int projectId, long guid, String name, String notes) 
			throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_ATTACHMENT_NOTES");
		
		sqlQuery.setValue("RecordID", guid,     Types.BIGINT);
		sqlQuery.setValue("Name",   name,       Types.VARCHAR);
		sqlQuery.setValue("Notes",  notes,      Types.VARCHAR);

		updateDatabase(sqlQuery);
	}

	@Override
	public Attachment createObject(Object[] columnValues) 
	{
		if(columnValues == null)
			return null;
		
		String uploadedBy = (String) columnValues[0];
		String name       = (String) columnValues[1];
		String notes      = (String) columnValues[2];
		
		logger.logp(Level.FINE, "DBAttachmentDAO", "createObject", "got record attachments "+name+" "+notes);
		
		return new Attachment(name, notes, uploadedBy);
	}

	@Override
	public List<Attachment> getRecordAttachments(long guid)
			throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ATTACHMENT_FOR_RECORD");
		
		sqlQuery.setValue("RecordID", guid, Types.BIGINT);

		RowSet<Attachment> result = find(sqlQuery);
		logger.logp(Level.FINE, "DBAttachmentDAO", "getRecordAttachments", "got record attachments "+result);
		return result == null ? null : result.getRows();
	}

	@Override
	public Attachment getRecordAttachment(int projectID, long guid,
			String attachmentName) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ATTACHMENT_FOR_NAME");
		
		sqlQuery.setValue("RecordID", guid, Types.BIGINT);
		sqlQuery.setValue("Name", attachmentName, Types.VARCHAR);

		return fetchInstance(sqlQuery);
	}

	@Override
	public void deleteRecordAttachment(int projectID, long guid, String name)
			throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_ATTACHMENT_OF_NAME");
		
		sqlQuery.setValue("RecordID", guid, Types.BIGINT);
		sqlQuery.setValue("Name", name, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void insertAttachmentNotes(long guid, String name,
			String notes, String uploadedBy) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_ATTACHMENT_NOTES");
		
		sqlQuery.setValue("RecordID", guid,       Types.BIGINT);
		sqlQuery.setValue("Name",     name,       Types.VARCHAR);
		sqlQuery.setValue("Type",     getTypeFromName(name).name(), Types.VARCHAR);
		sqlQuery.setValue("Notes",    notes,      Types.VARCHAR);
		sqlQuery.setValue("UserID",   uploadedBy, Types.VARCHAR);

		updateDatabase(sqlQuery);
	}
	
	private AttachmentType getTypeFromName(String name)
	{
		if(BioExperiment.GlobalMetadata.equals(name))
			return AttachmentType.Global;
		if(BioExperiment.OMEXMLMetaData.equals(name))
			return AttachmentType.Global;
		
		else 
			return AttachmentType.Local;
	}
}
