/*
 * Attachment.java
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
package com.strandgenomics.imaging.iclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Handle to an attachment of a record
 * @author arunabha
 *
 */
public class Attachment extends ImageSpaceObject implements IAttachment {
	
	private static final long serialVersionUID = 2136253970971540409L;
	/**
	 * name of this attachment
	 */
	protected String name = null;
	/**
	 * notes associated with this attachment
	 */
	protected String notes = null;
	/**
	 * owner of this attachment
	 */
	protected long parentRecord;
	
	Attachment(long guid, String name, String notes) {
		this.parentRecord = guid;
		this.name = name;
		this.notes = notes;
	}

	@Override
	public String getNotes() 
	{
		return notes;
	}

	@Override
	public void updateNotes(String notes) 
	{
		//makes a system call to get it done
		getImageSpace().updateAttachmentNotes(parentRecord, name, notes);
	}

	@Override
	public InputStream getInputStream() 
	{
		//makes a system call to the server 
		return getImageSpace().getAttachmentInputStream(parentRecord, name);
	}
	
	@Override
	public File getFile()
	{
		InputStream is = getInputStream();
		File tempFile = null;
		try {
			tempFile = File.createTempFile("Temp", name);
			FileOutputStream fs = new FileOutputStream(tempFile);
			Util.transferData(is, fs, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tempFile;
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public void delete()
	{
		//makes a system call to the server 
		getImageSpace().deleteAttachment(parentRecord, name);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
