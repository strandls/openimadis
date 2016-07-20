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
 * Job.java
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

/**
 * Status of a job as fetched from the storage
 * @author arunabha
 *
 */
public class Job implements Storable {
	
	private static final long serialVersionUID = 5214623384397861906L;
	/**
	 * ID of the ticket
	 */
	protected long ticketID;
	/**
	 * time in milliseconds when the ticket was formed, will be used to invalidate a ticket
	 */
	protected long requestTime;
	/**
	 * time in milliseconds when the ticket was was last modified
	 */
	protected long lastModificationTime;
	/**
	 * state the ticket is currently in
	 */
	protected Status jobStatus = Status.WAITING;
	
	public Job(long ticketID, long requestTime, long lastModificationTime, Status jobStatus)
	{
		this.ticketID = ticketID;
		this.requestTime = requestTime;
		this.lastModificationTime = lastModificationTime;
		this.jobStatus = jobStatus;
	}
	
	public String toString()
	{
		return ""+ticketID;
	}
	
	@Override
	public int hashCode()
	{
		return (int)(ticketID ^ (ticketID >>> 32));
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof Job)
		{
			Job that = (Job) obj;
			if(this == that) return true;
			
			return (this.ticketID == that.ticketID);
		}
		
		return false;
	}
	
	/**
	 * @return the ticketID
	 */
	public long getTicketID() 
	{
		return ticketID;
	}

	/**
	 * @param ticketID the ticketID to set
	 */
	public void setTicketID(long ticketID) {
		this.ticketID = ticketID;
	}

	/**
	 * @return the requestTime
	 */
	public long getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * @return the lastModificationTime
	 */
	public long getLastModificationTime() {
		return lastModificationTime;
	}

	/**
	 * @param lastModificationTime the lastModificationTime to set
	 */
	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	/**
	 * @return the jobStatus
	 */
	public Status getJobStatus() {
		return jobStatus;
	}

	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(Status jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	public boolean hasCompleted()
	{
		return jobStatus.equals(Status.SUCCESS) || jobStatus.equals(Status.FAILURE) || jobStatus.equals(Status.DUPLICATE) ;
	}
	
	public boolean isBusy()
	{
		return jobStatus.equals(Status.EXECUTING) || 
				jobStatus.equals(Status.QUEUED) || 
				jobStatus.equals(Status.WAITING);
	}
	
	public boolean isExecuting()
	{
		return jobStatus.equals(Status.EXECUTING);
	}
	
	public boolean isWaiting()
	{
		return jobStatus.equals(Status.WAITING) || jobStatus.equals(Status.QUEUED);
	}
	
	public boolean isSuccessful()
	{
		return jobStatus.equals(Status.SUCCESS);
	}
	
	public boolean hasFailed()
	{
		return jobStatus.equals(Status.FAILURE);
	}
	
	public boolean hasExpired()
	{
		return jobStatus.equals(Status.EXPIRED);
	}

	@Override
	public void dispose()
	{}
}
