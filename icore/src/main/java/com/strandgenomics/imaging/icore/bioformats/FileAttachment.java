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
 * FileAttachment.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.icore.bioformats;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IRecord;

/**
 * attachments associated with a raw record
 * @author arunabha
 */
public class FileAttachment implements IAttachment, Serializable {
	
	private static final long serialVersionUID = 7567481594660088018L;
	/**
	 * name of the attachment
	 */
	protected String name = null;
	/**
	 * the actual file attachment
	 */
	protected File attachment = null;
	/**
	 * notes associated with this attachment
	 */
	protected String notes = null;
	/**
	 * owner of this attachment
	 */
	protected IRecord parentRecord = null;
	/**
	 * true if this file attachment is system generated
	 * false if user added
	 */
	protected boolean systemGenerated;
	
	/**
	 * Creates an instance of file attachment with the given file
	 */
	public FileAttachment(IRecord owner, File localFile, String notes, boolean systemGenerated)
	{
		this(owner, localFile, localFile.getName(), notes, systemGenerated);
	}
	
	/**
	 * Creates an instance of file attachment with the given file
	 */
	public FileAttachment(IRecord owner, File localFile, String name, String notes, boolean systemGenerated)
	{
		this.parentRecord = owner;
		this.attachment = localFile;
		this.notes = notes;
		this.name = name;
		this.systemGenerated = systemGenerated;
	}
	
	/**
	 * tells if this file attachment is system generated or user added
	 * @return true if attachment is system generated; false otherwise
	 */
	public boolean isSystemGenerated()
	{
		return this.systemGenerated;
	}

	public IRecord getParentRecord()
	{
		return parentRecord;
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public String getNotes() 
	{
		return notes;
	}

	@Override
	public void updateNotes(String notes)
	{
		this.notes = notes;
	}

	@Override
	public InputStream getInputStream() throws IOException 
	{
		return new FileInputStream(attachment);
	}
	
	@Override
	public File getFile()
	{
		return attachment;
	}

	@Override
	public void delete()
	{
		parentRecord.removeAttachment(getName());
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	}
}
