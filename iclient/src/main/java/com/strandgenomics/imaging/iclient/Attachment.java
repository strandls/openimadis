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
