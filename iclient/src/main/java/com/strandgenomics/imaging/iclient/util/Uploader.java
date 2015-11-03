/*
 * Uploader.java
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
package com.strandgenomics.imaging.iclient.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.iclient.DuplicateRequestException;
import com.strandgenomics.imaging.iclient.IllegalRequestException;
import com.strandgenomics.imaging.iclient.ImageSpaceException;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.ServerIsBusyException;
import com.strandgenomics.imaging.iclient.Ticket;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.RawRecord;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.util.HttpUtil;
import com.strandgenomics.imaging.icore.util.UploadObserver;

/**
 * uploader is associated with experiment to handle the logistics of uploading
 * experiment and user added information to server
 * 
 * @author Anup Kulkarni
 */
public class Uploader {
	
	/**
	 * wait for say one minite before trying your luck with another request
	 */
	public static final long WAIT_TIME = 1000 * 10;

	/**
	 * ticket associated with uploader.
	 */
	private Ticket ticket;
	/**
	 * experiment associated with this uploader
	 */
	protected RawExperiment experiment;
	/**
	 * the project to upload
	 */
	protected Project project;
	/**
	 * whether to continue with the request (before a ticket is acquired, it can be cancelled)
	 */
	private boolean terminate = false;
	/**
	 * checks whether the records tar-ball was successfully uploaded to the server
	 */
	private boolean successfullyUploaded = false;

	/**
	 * Creates an uploader to upload the specified raw-experiment within the specified project 
	 * @param project the project to upload the experiment to
	 * @param experiment the experiment to upload
	 */
	public Uploader(Project project, RawExperiment experiment) 
	{
		this.project = project;
		this.experiment = experiment;
	}

	/**
	 * @param project
	 *            the project to which the experiment has to be uploaded
	 */
	public Ticket fetchTicket() 
	{
		while (!terminate) 
		{
			try 
			{
				ticket = project.requestTicket(experiment);
				break;
			} 
			catch (ServerIsBusyException e) 
			{
				sleep(WAIT_TIME);
			}
			catch (IllegalRequestException e) 
			{
				throw e;
			}
		}
		
		return ticket;
	}

	private void sleep(long waitTime) 
	{
		try 
		{
			Thread.sleep(waitTime);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * uploads source files for experiment
	 * @return 
	 * @throws IOException 
	 */
	public boolean uploadSourceFiles(final File tarFile,final UploadObserver uploaderTask) throws IOException
	{
		URL uploadUrl = ticket.getUploadURL();
		HttpUtil httpUtil = new HttpUtil(uploadUrl);
		
		try 
		{
			httpUtil.upload(tarFile, uploaderTask);
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean monitorTicketStatus()
	{
		while (!terminate) 
		{
			switch(ticket.getStatus())
			{
				case WAITING:
				case QUEUED:
				case UPLOADING:
				case EXECUTING:
					sleep(WAIT_TIME);
					break; //break the switch
				case SUCCESS:
					terminate = true;
					successfullyUploaded = true;
					break;
				case EXPIRED:
				case FAILURE:
					terminate = true;
					throw new ImageSpaceException("failed");
				case DUPLICATE:
					terminate = true;
					throw new DuplicateRequestException();
				default:
					sleep(WAIT_TIME);
			}
		}
		terminate = false;// for future calls to this method
		
		return successfullyUploaded;
	}
	
	/**
	 * uploads record fields 
	 * @param record
	 * @return status of task: true if completed successfully, false otherwise
	 * @throws InterruptedException 
	 */
	public boolean uploadRecordFields(final UploadObserver uploaderTask)
	{
		if(!successfullyUploaded)
			return false;
		
		if(experiment.size() == 0)
			return true;
		
		for(Signature sign : experiment.getRecordSignatures())
		{
			RawRecord record = (RawRecord) experiment.getRecord(sign);
			//upload the comments
			uploaderTask.reportProgress(25, "Uploading Comments");
			record.uploadComments();
			
			// upload acq profile if exists
			record.uploadAcqProfile();
			
			//upload the attachments
			uploaderTask.reportProgress(50, "Uploading Attachments");
			record.uploadAttachments();
			
			//upload the annotations
			uploaderTask.reportProgress(75, "Uploading User Annotations");
			Map<String, Object> userAnnotations = record.getUserAnnotations();
			Set<Entry<String, Object>> entry = userAnnotations.entrySet();
			
			for(Entry<String, Object> annotation :entry)
			{
				String name = annotation.getKey();
				Object value = annotation.getValue();
				if(value instanceof Long)
					record.uploadUserAnnotation(name, (Long)value);
				else if(value instanceof Double)
					record.uploadUserAnnotation(name, (Double)value);
				else if(value instanceof String)
					record.uploadUserAnnotation(name, (String)value);
				else if(value instanceof Date)
					record.uploadUserAnnotation(name, (Date)value);
			}
			
			//upload visual overlays
			uploaderTask.reportProgress(90, "Uploading Visual Annotation");
			record.uploadVisualOverlays();
			
			//upload thumbnail
			if(record.hasCustomThumbnail())
			{
				uploaderTask.reportProgress(100, "Uploading Thumbnail");
				BufferedImage img = record.getThumbnail();
				File tempFile = null;
				try
				{
					tempFile = File.createTempFile("thumbnail", "png");
					tempFile.deleteOnExit();
					ImageIO.write(img, "PNG", tempFile);
			    	
					record.uploadThumbnails(tempFile);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if(tempFile!=null)
						tempFile.delete();
				}
			}
			uploaderTask.reportProgress(100, "Uploading User Data Done");
		}
		return true;
	}

	/**
	 * all the source files are tarred together for uploading
	 * 
	 * @return tar files
	 */
	public File packSourceFiles() 
	{
		try 
		{
			return experiment.export(new File(System.getProperty("user.home")), experiment.getSeedFile().getName()+".tar", false);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * returns experiment associated with this uploader
	 * @return RawExperiment associated with this uploader
	 */
	public RawExperiment getExperiment()
	{
		return this.experiment;
	}
}
