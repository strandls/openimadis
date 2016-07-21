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

package com.strandgenomics.imaging.iworker.services;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.bioformats.RecordExtractor;
import com.strandgenomics.imaging.iengine.bioformats.RecordExtractorFactory;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * A task that is executed in a separate thread to extract records
 * @author arunabha
 *
 */
public class ExtractionTask implements Callable <Void> {
	
	/**
	 * job ID
	 */
	protected final long ticketID;
	/**
	 * The specification of the client request
	 */
	protected final RecordCreationRequest context;
	/**
	 * The Observer interested in the progress of the task
	 */
	protected final ExtractionObserver observer;
	/**
	 * The logger
	 */
	protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.worker");

	/**
	 * Creates a record extraction task for the specified tar-ball of source files
     * @param actor the user on whose behalf this is being done
     * @param projectName the project under which the records are to be added
	 * @param archiveSignature  MD5 hash of the uploaded source files
	 * @param tarBall gzipped tar ball of the source file
	 * @param observer The Observer interested in the progress of the task
	 */
	public ExtractionTask(long ticketID, ExtractionObserver observer, RecordCreationRequest context) 
	{
		this.ticketID = ticketID;
		this.observer = observer;
		this.context  = context;
	}

	@Override
	public Void call() throws Exception 
	{
		//report starting
		observer.update(new ExtractionEvent(ticketID, context.getArchiveHash(), Status.EXECUTING));
		
		try
		{
			extractRecords();
			//report success
			observer.update(new ExtractionEvent(ticketID, context.getArchiveHash(), Status.SUCCESS));
		}
		catch(ImagingEngineException e){
			observer.update(new ExtractionEvent(ticketID, context.getArchiveHash(), Status.DUPLICATE));			
			logger.logp(Level.INFO, "ExtractionTask", "call", "duplicate archive");
		}
		catch(Exception e)
		{
			//report failure
			observer.update(new ExtractionEvent(ticketID, context.getArchiveHash(), Status.FAILURE));
			logger.logp(Level.INFO, "ExtractionTask", "call", "error while extracting records "+context, e);
		}
		finally{
			
			try
			{
				// send notification
				Set<User> receivers = new HashSet<User>();
		        receivers.add(SysManagerFactory.getUserManager().getUser(context.getActor()));
		        
				SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, null, NotificationMessageType.RECORD_CREATED, context.getProject(), context.getSourceFilename(), SysManagerFactory.getTicketManager().getTicketStatus(ticketID).toString().toLowerCase());
			}
			catch(Exception e)
			{
				logger.logp(Level.WARNING, "ExtractionTask", "call", "failed sending email notification to "+ context.getActor(), e);
			}
		}
		
		return null;
	}
	
	public void extractRecords() throws ImagingEngineException,Exception
	{
		RecordExtractor extractor = RecordExtractorFactory.getExtractor(context);
		extractor.extractRecords();
	}
}
