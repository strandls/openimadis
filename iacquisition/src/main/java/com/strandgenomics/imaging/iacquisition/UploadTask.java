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

package com.strandgenomics.imaging.iacquisition;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.DuplicateRequestException;
import com.strandgenomics.imaging.iclient.IllegalRequestException;
import com.strandgenomics.imaging.iclient.InsufficientPermissionException;
import com.strandgenomics.imaging.iclient.QuotaExceededException;
import com.strandgenomics.imaging.iclient.ServerIsBusyException;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.UploadStatus;
import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.util.UploadObserver;

/**
 * Task to manage experiments upload to server
 * 
 * @author Anup Kulkarni
 */
public class UploadTask extends SwingWorker implements UploadObserver{
	
	/**
	 * uploader associated with experiment
	 */
	private Uploader uploader;
	
	/**
	 * progress message
	 */
	private String message;

	/**
	 * progress of tar file upload
	 */
	private int uploadProgress;
	
	/**
	 * success of task
	 */
	private boolean status = false;
	
	private UploadStatus state = UploadStatus.Queued;
	/**
	 * logger
	 */
	private Logger logger = Logger.getRootLogger();
	
	private boolean taskcancelled = false;
	
	private boolean archiveFileTransfered = false;

	public UploadTask(Uploader uploader)
	{
		this.uploader = uploader;
	}

	protected Object doInBackground() throws Exception 
	{
		File tarFile = null;
		try 
		{
			if(uploader.getExperiment().isExistOnServer()){
				message = "Record already available on server";
				state = UploadStatus.Duplicate;
				return false;
			}
			else{
				message = "Record is not available on server";
				state = UploadStatus.NotUploaded;
			}
			System.out.println("Uploading record");
			message = "creating tar with the source files";
			logger.info("[UploadTask]: execute "+message);
			updateProgress(1);
			
			tarFile = uploader.packSourceFiles();
			if(tarFile!=null)
				tarFile.deleteOnExit();
			message = "waiting for server response";
			if(isCancelled())
			{
				message = "cancelled";
				state = UploadStatus.NotUploaded;
				return false;
			}
				
			logger.info("[UploadTask]: execute "+message);
			updateProgress(2);

			uploader.fetchTicket();// doesnt return till valid ticket is fetched, but can throw exceptions
			message = "uploading tar of the source files...";
			if(isCancelled()){
				message = "cancelled";
				state = UploadStatus.NotUploaded;
				return false;
			}
			
			logger.info("[UploadTask]: execute "+message);
			updateProgress(3);
			
			uploader.uploadSourceFiles(tarFile, this);
			message = "server processing request (this may take some time)";
			updateProgress(getProgress()+1);
			archiveFileTransfered = true; // after this the upload task cannot be cancelled
			logger.info("[UploadTask]: execute "+message);
			
			uploader.monitorTicketStatus();
			message = "uploading user added data";
			updateProgress(getProgress()+1);
			logger.info("[UploadTask]: execute "+message);

			uploader.uploadRecordFields(this);
			message = "upload successfully completed";
			logger.info("[UploadTask]: execute "+message);
			state = UploadStatus.Uploaded;
			status = true;
			updateProgress(100);
		} 
		catch (AuthenticationException e)
		{
			handleException("Authentication failed", e);
			logger.error("[UploadTask]: Authentication failed "+e.getMessage());
			state = UploadStatus.NotUploaded;
		}
		catch(InsufficientPermissionException e)
		{
			handleException("Insufficient permission to upload", e);
			logger.error("[UploadTask]: Insufficient permission to upload "+e.getMessage());
			state = UploadStatus.NotUploaded;
		}
		catch(IllegalRequestException e)
		{
			handleException("Another upload of the same record is in progress", e);
			logger.error("[UploadTask]: Another upload of the same record is in progress "+e.getMessage());
			state = UploadStatus.Duplicate;
		}
		catch(DuplicateRequestException e)
		{
			handleException("Record already available on server", e);
			logger.error("[UploadTask]: Record already available on server "+e.getMessage());
			state = UploadStatus.Duplicate;
		}
		catch(ServerIsBusyException e)
		{
			handleException("Server cannot handle the request",e);
			logger.error("[UploadTask]: Server cannot handle the request "+e.getMessage());
			state = UploadStatus.NotUploaded;
		}catch(QuotaExceededException e){
			handleException("Project disk quota exceeded",e);
			logger.error("[UploadTask]: project quota exceeded "+e.getMessage());
			state = UploadStatus.NotUploaded;
		}
		catch (Exception e) 
		{
			handleException("Upload failed",e);
			logger.error("[UploadTask]: Upload failed "+e.getMessage());
			state = UploadStatus.NotUploaded;
		}
		finally
		{
			tarFile.delete();
			logger.info("[UploadTask]: deleted temp tar ");
			updateProgress(100);
		}
			
		return null;
	}
	
	public void handleException(String message, Exception e){
		this.message = message;
		status = false;
		updateProgress(100);
		e.printStackTrace();
	}
	
	public String getMessage()
	{
		return message;
	}

	public boolean getStatus(){
		return status;
	}
	
	private void updateProgress(final int newValue){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(newValue >100)
					setProgress(100);
				else
					setProgress(newValue);
			}
		});
	}
	
	@Override
	public void reportProgress(File source, final long totalBytes, final long bytesUploaded) {
//		message = "uploaded "+bytesUploaded+" out of "+totalBytes;
		message = "uploaded "+bytesUploaded*100/totalBytes+" %";
		// uploading progress is scaled because task has still to upload other stuff and delete the tar
		uploadProgress = (int) (bytesUploaded*0.8/totalBytes*100);
		updateProgress(uploadProgress);
		
	}
	
	@Override
	public void reportProgress(int progress, String message) {
		this.message = message;
		int prevProgress = getProgress();
		uploadProgress = prevProgress + progress/25;
		updateProgress(uploadProgress);
	}
	
	public UploadStatus getUploadState(){
		return state;
	}

	public boolean taskCancelled(){
		return taskcancelled;
	}
	
	public void cancelTask()
	{
		taskcancelled = true;
		message = "cancelled";
		updateProgress(100);
		state = UploadStatus.NotUploaded;
		status = false;
		cancel(true);
	}
	/**
	 * returns true if the current upload task can be cancelled
	 * task cannot be cancelled if the uploading of archive file is done
	 * @return
	 */
	public boolean canCancel() 
	{
		return !archiveFileTransfered;
	}
	
	public String getExperimentName(){
		RawExperiment expr = uploader.getExperiment();
		String fullSourceFileName = expr.getReference().get(0).getSourceFile();
		String separator = File.separator;
		String name = fullSourceFileName.substring(fullSourceFileName
				.lastIndexOf(separator) + 1);
		return name;
	}
	
	public RawExperiment getExperiment(){
		return uploader.getExperiment();
	}
}
