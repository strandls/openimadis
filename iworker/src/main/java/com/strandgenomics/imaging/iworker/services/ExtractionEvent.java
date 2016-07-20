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
 * ExtractionEvent.java
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
package com.strandgenomics.imaging.iworker.services;

import java.math.BigInteger;

import com.strandgenomics.imaging.icore.Status;

/**
 * This event is generated when records are successfully extracted out of the source files
 * @author arunabha
 *
 */
public class ExtractionEvent {
	
	/**
	 * job ID
	 */
	protected final long ticketID;
	/**
	 * signature of an experiment (multi-series records)
	 */
	protected final BigInteger experimentID;
	/**
	 * status of the task
	 */
	protected final Status status;
	
	public ExtractionEvent(long ticketID, BigInteger experimentSignature, Status status)
	{
		this.ticketID = ticketID;
		this.experimentID = experimentSignature;
		this.status = status;
	}
	
	public String toString()
	{
		return "("+ticketID+","+status+")";
	}
	
	public long getTicketID()
	{
		return ticketID;
	}
	
	public BigInteger getSignature()
	{
		return experimentID;
	}
	
	public Status getStatus()
	{
		return status;
	}
	
	public boolean isExecuting()
	{
		return status.equals(Status.EXECUTING);
	}
	
	public boolean isWaiting()
	{
		return status.equals(Status.QUEUED);
	}
	
	public boolean isSuccessful()
	{
		return status.equals(Status.SUCCESS);
	}
	
	public boolean isFailed()
	{
		return status.equals(Status.FAILURE);
	}
	
	public boolean isCompleted()
	{
		return status.equals(Status.SUCCESS) || status.equals(Status.FAILURE);
	}
}
