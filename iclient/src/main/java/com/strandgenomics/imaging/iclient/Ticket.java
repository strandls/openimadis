/*
 * Ticket.java
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

import java.math.BigInteger;
import java.net.URL;

import com.strandgenomics.imaging.icore.ITicket;
import com.strandgenomics.imaging.icore.Status;

/**
 * ticket used for uploading data on server. it will be valid only till the time
 * of validity associated with it
 * 
 * @author Anup Kulkarni
 */
public class Ticket extends ImageSpaceObject implements ITicket {

	private static final long serialVersionUID = -2081556936758031948L;

	/**
	 * ticket identifier
	 */
	public final long ticketId;
	/**
	 * md5 hash of the archive requested to upload/download
	 */
	public final BigInteger archiveID;
	/**
	 * url to upload the experiment archive file
	 */
	private URL uploadURL = null;
	/**
	 * url to download the experiment archive file
	 */
	private URL downloadURL = null;

	/**
	 * ticket object used for uploading experiment
	 * @param ticketId id 
	 * @param archiveID md5 checksum of the source files of the experiment to be uploaded
	 * @param uploadURL the upload url
	 * @param downloadURL the download url 
	 */
	Ticket(long ticketId, BigInteger archiveID, URL uploadURL, URL downloadURL) 
	{
		this.ticketId    = ticketId;
		this.archiveID   = archiveID;
		this.uploadURL   = uploadURL;
		this.downloadURL = downloadURL;
	}
	
	/**
	 * get the ticket status
	 * @return the ticket status
	 */
	public Status getStatus()
	{
		//makes a system call
		return getImageSpace().getTicketStatus(this);
	}
	
	@Override
	public long getID()
	{
		return ticketId;
	}
	
	/**
	 * md5 hash of the archive requested to upload/download
	 * @return
	 */
	public BigInteger getArchiveID()
	{
		return archiveID;
	}

	@Override
	public URL getUploadURL() 
	{
		return uploadURL;
	}
	
	/**
	 * get the download url
	 * @return the download url
	 */
	public URL getDownloadURL() 
	{
		return downloadURL;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
	}
}
