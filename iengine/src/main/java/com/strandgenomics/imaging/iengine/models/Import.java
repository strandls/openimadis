/*
 * Import.java
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
package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;


/**
 * Status of import
 * @author navneet
 *
 */
public class Import implements Storable{
	
	/**
	 * ID of the ticket
	 */
	protected long ticketID;
	/**
	 * time in milliseconds when the ticket was formed, will be used to invalidate a ticket
	 */
	protected long requestTime;
	/**
	 * state the ticket is currently in
	 */
	protected Status jobStatus = Status.WAITING;
	/**
	 * import request
	 */
	RecordCreationRequest request;

	public Import(long ticketID, long requestTime, Status jobStatus, RecordCreationRequest request){
		this.ticketID = ticketID;
		this.requestTime = requestTime;
		this.jobStatus = jobStatus;
		this.request=request;
	}
	
	/**
	 * @return the ticketID
	 */
	public long getTicketID() 
	{
		return ticketID;
	}

	/**
	 * @return the requestTime
	 */
	public long getRequestTime() {
		return requestTime;
	}
	
	/**
	 * @return the jobStatus
	 */
	public Status getJobStatus() {
		return jobStatus;
	}
	
	/**
	 * @return the record creation request
	 */
	public RecordCreationRequest getRequest(){
		return request;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
