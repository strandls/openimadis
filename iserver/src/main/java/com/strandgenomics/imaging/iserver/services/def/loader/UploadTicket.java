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
 * TicketObject.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.loader;

/**
 * A ticket for uploading and download record archives
 * @author arunabha
 *
 */
public class UploadTicket {

	/**
	 * ID of the ticket
	 */
	private long ID;
	/**
	 * the URL to use for uploading archives etc
	 */
	private String uploadURL = null;
	/**
	 * the URL to use for downloading archives etc
	 */
	private String downloadURL = null;
	/**
	 * Signature - md5 hash of the source files as computed by the client
	 */
	private String archiveSignature = null;
	
	public UploadTicket()
	{}

	/**
	 * @return the iD
	 */
	public long getID()
	{
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(long iD)
	{
		ID = iD;
	}

	/**
	 * @return the uploadURL
	 */
	public String getUploadURL()
	{
		return uploadURL;
	}

	/**
	 * @param uploadURL the uploadURL to set
	 */
	public void setUploadURL(String uploadURL)
	{
		this.uploadURL = uploadURL;
	}

	/**
	 * @return the downloadURL
	 */
	public String getDownloadURL() 
	{
		return downloadURL;
	}

	/**
	 * @param downloadURL the downloadURL to set
	 */
	public void setDownloadURL(String downloadURL) 
	{
		this.downloadURL = downloadURL;
	}

	public String getArchiveSignature()
	{
		return archiveSignature;
	}

	public void setArchiveSignature(String archiveSignature)
	{
		this.archiveSignature = archiveSignature;
	}
}
