/*
 * TicketDAO.java
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

import java.math.BigInteger;
import java.util.List;

import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Job;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.Ticket;

public interface TicketDAO {

	/**
	 * Returns the requested Ticket, or <code>null</code> if no such Ticket was
	 * found in the repository
	 */
	public Job findTicket(long ticketID) throws DataAccessException;
	
	/**
	 * Returns the requested Tickets, or <code>null</code> if no such Tickets was
	 * found in the repository
	 */
	public List<Job> findTicket(Status jobStatus) throws DataAccessException;

	/**
	 * Update the status of the specified ticket with the given status
	 * @param ticketID id of the ticket
	 * @param jobStatus status of the job
	 * @return the updated job
	 * @throws DataAccessException
	 */
    public Job updateJobStatus(long ticketID, Status jobStatus)
    		throws DataAccessException;
    
	/**
	 * Insert the specified Ticket into the repository
	 * @return the Ticket object 
	 */
	public Job insertTicket(long ticketID, long requestTime, Status jobStatus) 
			throws DataAccessException;

	/**
	 * list all the tickets
	 * @return
	 * @throws DataAccessException 
	 */
	public List<Job> listTickets() throws DataAccessException;

	/**
	 * insert the record creation requset
	 * @param ticketId ticket on which record creation request is made
	 * @param request specified request object
	 * @throws DataAccessException
	 */
	public void insertRequest(long ticketId, RecordCreationRequest request) throws DataAccessException;
	
	/**
	 * returns the ticket id for request, null if not exists
	 * @param requestHash hash of record creation requests
	 * @return the ticket id for request, null if not exists
	 * @throws DataAccessException
	 */
	public Long getTicket(BigInteger requestHash) throws DataAccessException;
	
	/**
	 * get the record creation request for specified ticket
	 * @param ticketId specified ticket
	 * @return record creation request
	 * @throws DataAccessException
	 */
	public RecordCreationRequest getRequest(long ticketId) throws DataAccessException;
	
	/**
	 * get the record creation request for specified hash
	 * @param requestHash specified hash
	 * @return record creation request
	 * @throws DataAccessException
	 */
	public RecordCreationRequest getRequest(BigInteger requestHash) throws DataAccessException;

	/**
	 * update the ticket request
	 * @param ticketID specified ticket
	 * @param request new request object
	 * @throws DataAccessException 
	 */
	public void updateTicketRequest(long ticketID, RecordCreationRequest request) throws DataAccessException;

	/**
	 * remove the specified ticket
	 * @param ticketId
	 * @throws DataAccessException
	 */
	public void removeCreationRequest(long ticketId) throws DataAccessException;
	
	/**
	 * remove the specified ticket
	 * @param ticketId
	 * @throws DataAccessException
	 */
	public void removeTicket(long ticketId) throws DataAccessException;
	
	/**
	 * 
	 * @param maxQueueSize
	 * @param request
	 * @return
	 * @throws DataAccessException
	 */
	public Ticket createTicketTransaction(int maxQueueSize, RecordCreationRequest request) throws DataAccessException;
}
