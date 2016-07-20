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
package com.strandgenomics.imaging.icore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Attachment (to a record) can be any file one wishes to associated with a record
 * @author arunabha
 */
public interface IAttachment extends Disposable {
	
	/**
	 * Returns the name of the attachment file
	 * @return the name of the attachment file
	 */
	public String getName();
	
	/**
	 * Returns the notes associated with this attachment
	 * @return the notes associated with this attachment
	 */
	public String getNotes();
	
	/**
	 * add or replace the notes
	 * @param notes
	 */
	public void updateNotes(String notes);
	
	/**
	 * Returns the input stream to read this attachment
	 * @return the input stream to read this attachment
	 */
	public InputStream getInputStream() throws IOException;
	
	/**
	 * Returns the attachment as a file
	 * @return the attachment as a file
	 */
	public File getFile();
	
	/**
	 * Deletes the attachment, login user needs relevant permission to execute this call
	 */
	public void delete();
}
