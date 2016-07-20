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
 * RecordUploadManager.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.iengine.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ImportDAO;
import com.strandgenomics.imaging.iengine.dao.TicketDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Job;

public class TicketManager extends SystemManager {
	
	/**
	 * time to live
	 */
	protected int expiryInterval;

	/**
	 * maximum number of tickets that are accepted
	 */
	protected int maxQueueSize;
	
	TicketManager()
	{
		maxQueueSize = Constants.getActiveTicketQSize();
		expiryInterval = Constants.getRequestTimeout();
	}
	
	public final synchronized Ticket requestUploadTicket(RecordCreationRequest request) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canUpload(request.getActor(), request.getProject()))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		if(SysManagerFactory.getStorageManager().isArchiveExists(request.getArchiveHash()))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.ARCHIVE_ALREADY_EXIST, request.getProject(), request.getActor()));
		}
		
		logger.logp(Level.FINEST, "TicketManager", "requestUploadTicket", "checking if storage quota is exceeded");
		if(SysManagerFactory.getProjectManager().isQuotaExceeded(request.getProject(), request.getClientFiles()))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.QUOTA_EXCEEDED));
		}
		
		Ticket ticket = ImageSpaceDAOFactory.getDAOFactory().getTicketDAO().createTicketTransaction(maxQueueSize, request);
		
		SysManagerFactory.getImportManager().insertImport(ticket.ID, ticket.getRequestTime(), ticket.getStatus(), request);

		return ticket;
	}
	
	/**
	 * check whether the ticket has expired
	 * @param ticketID
	 * @return
	 * @throws DataAccessException 
	 */
	public boolean isValid(long ticketID) throws DataAccessException 
	{
		long currentTime = System.currentTimeMillis();
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		TicketDAO ticketDao = factory.getTicketDAO();
		Job job = ticketDao.findTicket(ticketID);
		
		if(job == null || job.hasCompleted() || job.hasExpired()) 
		{
			return false;
		}
		else if((currentTime - job.getRequestTime()) > expiryInterval)
		{
			updateTicketStatus(ticketID, Status.EXPIRED);
			return false;
		}
		else
			return true;
	}

	/**
	 * This method is called by the UploadServlet to create records
	 * @param ticketID the ticket ID
	 * @param tarBall the uploaded (downloaded by the server) tar-ball
	 * @throws DataAccessException 
	 */
	public void update(long ticketID, File tarBall) throws DataAccessException
	{
		logger.logp(Level.INFO, "TicketManager", "update", "received client tar-ball "+tarBall);
		if(tarBall == null || !tarBall.isFile()) return;
		
		RecordCreationRequest request = ImageSpaceDAOFactory.getDAOFactory().getTicketDAO().getRequest(ticketID);
		logger.logp(Level.INFO, "TicketManager", "update", "received client tar-ball for ticket "+ticketID);
		
		if(request == null)
		{
			throw new RuntimeException("ticket expired for "+ticketID);
		}
		//set the tar ball to the request
		request.setTarBall(tarBall);
		//submit the task for execution
		try 
		{
			boolean status = SysManagerFactory.getExtractionManager().submitTask(ticketID, request);
			logger.logp(Level.INFO, "TicketManager", "update", "submitted request for extracting records from "+tarBall +" for ticket "+ticketID +", status="+status);
		} 
		catch (IOException e) 
		{
			logger.logp(Level.WARNING, "TicketManager", "update", "error submitting ticket("+ticketID +") for execuion "+request, e);
			updateTicketStatus(ticketID, Status.FAILURE);
		}
		
		ImageSpaceDAOFactory.getDAOFactory().getTicketDAO().updateTicketRequest(ticketID, request);
	}

	/**
	 * update the ticket status
	 * @param ticketID specified ticket
	 * @param status new status
	 */
	public void updateTicketStatus(long ticketID, Status status)
	{
		logger.logp(Level.INFO, "TicketManager", "updateTicketStatus", "updating "+ticketID +" with "+status);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		TicketDAO ticketDao = factory.getTicketDAO();
		ImportDAO importDAO = factory.getImportDAO();
		
		try
		{
			RecordCreationRequest request= ticketDao.getRequest(ticketID);
			int projectId= DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findProject(request.getProject()).getID();
			
			Job job = ticketDao.updateJobStatus(ticketID, status);
			importDAO.updateImportStatus(projectId, ticketID, status, job.getLastModificationTime());
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "updateTicketStatus", "update", "error updating ticket "+ticketID +", "+ex);
		}
	}
	
	/**
	 * returns the status of the specified ticket
	 * @param ticketID specified ticket
	 * @return status of the specified ticket
	 * @throws DataAccessException
	 */
	public Status getTicketStatus(long ticketID) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		TicketDAO ticketDao = factory.getTicketDAO();
		Job job = ticketDao.findTicket(ticketID);
		return job == null ? null : job.getJobStatus();
	}
	
	/**
	 * returns all the tickets
	 * @return all the tickets
	 * @throws DataAccessException
	 */
	public List<Job> getTickets() throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		TicketDAO ticketDao = factory.getTicketDAO();
		
		List<Job> tickets = ticketDao.listTickets();
		return tickets;
	}
	
	/**
     * Save the uploaded archive
     * @param request the request stream
     * @param ticketID the ticket
     * @throws IOException 
     */
    public void acceptRequest(InputStream request, long ticketID) throws Exception 
    {
    	logger.logp(Level.INFO, "TicketManager", "acceptRequest", "accepting upload request for ticket "+ticketID);
    	updateTicketStatus(ticketID, Status.UPLOADING);
    
    	BufferedInputStream iStream = null;
    	
    	FileOutputStream fStream = null;
    	BufferedOutputStream oStream = null;
    	
    	File tarBall = null;
    	try
    	{
        	iStream = new BufferedInputStream(request);
        	
        	tarBall = File.createTempFile("ticket_"+ticketID, ".tar.gz", Constants.TEMP_DIR);
        	tarBall.deleteOnExit();
        	
        	logger.logp(Level.INFO, "TicketManager", "acceptRequest", "storing tar ball in "+tarBall);
        	
        	fStream = new FileOutputStream(tarBall);
        	oStream = new BufferedOutputStream(fStream);
    		
        	long dataLength = Util.transferData(iStream, oStream);
        	logger.logp(Level.INFO, "TicketManager", "acceptRequest", "successfully stored tar ball of size "+dataLength +" at "+tarBall);
    	}
    	catch(Exception ex)
    	{
    		updateTicketStatus(ticketID, Status.FAILURE);
    		throw ex;
    	}
    	finally
    	{
    		Util.closeStream(oStream);
    		Util.closeStream(fStream);
    		Util.closeStream(iStream);
    	}
    	
    	//send the tar-ball for further processing
    	update(ticketID, tarBall);
	}

    /**
     * returns record creation request for ticket id
     * @param ticketID specified ticket
     * @return record creation request for ticket id
     * @throws DataAccessException 
     */
	public RecordCreationRequest getRequest(long ticketID) throws DataAccessException
	{
		return ImageSpaceDAOFactory.getDAOFactory().getTicketDAO().getRequest(ticketID);
	}

	/**
	 * cancells active upload
	 * @param user 
	 * @param ticketId
	 * @throws DataAccessException 
	 */
	public void cancelUpload(String user, long ticketId) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(user))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		Status status = getTicketStatus(ticketId);
		if(status != Status.SUCCESS || status != Status.FAILURE || status!=Status.EXPIRED)
		{
			SysManagerFactory.getTicketManager().updateTicketStatus(ticketId, Status.FAILURE);
			ImageSpaceDAOFactory.getDAOFactory().getTicketDAO().removeCreationRequest(ticketId);
		}
	}    
}
