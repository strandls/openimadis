package com.strandgenomics.imaging.iclient.daemon;

import java.io.File;
import java.util.concurrent.Callable;

import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.DuplicateRequestException;
import com.strandgenomics.imaging.iclient.IllegalRequestException;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.InsufficientPermissionException;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.QuotaExceededException;
import com.strandgenomics.imaging.iclient.ServerIsBusyException;
import com.strandgenomics.imaging.iclient.local.UploadStatus;
import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.util.UploadObserver;

public class UploadDaemonTask  implements Callable <Void>, UploadObserver {

	/**
	 * specification of record to upload
	 */
	private UploadSpecification spec;
	
	private File tarFile;
	
	public UploadDaemonTask(UploadSpecification spec)
	{
		this.spec = spec;
	}
	
	@Override
	public Void call()
	{
		try
		{
			if(spec.isCanceled())
			{
				spec.setMessage("Canceled");
				spec.setStatus(UploadStatus.NotUploaded);
				
				updateSession();
				return null;
			}
			
			ImageSpaceObject.getConnectionManager().setAccessKey(spec.isSSL(), spec.getHost(), spec.getPort(),spec.getAccessKey());
			
			Project project = ImageSpaceObject.getConnectionManager().findProject(spec.getProjectName());
			
			Uploader uploader = new Uploader(project, spec.getExperiment());

			System.out.println("creating tar ball");
			// create tar ball of original files
			tarFile = uploader.packSourceFiles();
			
			if(spec.isCanceled())
			{
				spec.setMessage("Canceled");
				spec.setStatus(UploadStatus.NotUploaded);
				
				updateSession();
				
				tarFile.delete();
				return null;
			}
			
			System.out.println("requesting ticket");
			uploader.fetchTicket();
			
			System.out.println("uploading source files");
			uploader.uploadSourceFiles(tarFile, this);
			
			boolean success = uploader.monitorTicketStatus();
			if(success)
			{
				// update the status
				spec.setMessage("Upload Successful");
				spec.setStatus(UploadStatus.Uploaded);
				
				System.out.println("uploading user data");
				uploader.uploadRecordFields(this);
			}
		}
		catch(DuplicateRequestException e)
		{
			spec.setMessage("Record already available on server new status");
			spec.setStatus(UploadStatus.Duplicate);
		}
		catch (AuthenticationException e)
		{
			System.out.println("Authentication failed");
			spec.setMessage("Authentication failed");
			spec.setStatus(UploadStatus.NotUploaded);
		}
		catch(InsufficientPermissionException e)
		{
			spec.setMessage("Insufficient permission to upload");
			spec.setStatus(UploadStatus.NotUploaded);
		}
		catch(IllegalRequestException e)
		{
			spec.setMessage("Another upload of the same record is in progress");
			spec.setStatus(UploadStatus.Duplicate);
		}
		catch(ServerIsBusyException e)
		{
			spec.setMessage("Server cannot handle the request");
			spec.setStatus(UploadStatus.NotUploaded);
		}
		catch(QuotaExceededException e)
		{
			spec.setMessage("Project disk quota exceeded");
			spec.setStatus(UploadStatus.NotUploaded);
		}
		catch (Exception e) 
		{
			spec.setMessage("Upload failed");
			spec.setStatus(UploadStatus.NotUploaded);
		}
		finally
		{
			updateSession();
			
			if(tarFile!=null)
				tarFile.delete();
			
			System.out.println("completed");
		}
		
		return null;
	}
	
	private void updateSession()
	{
		// store the session
		UploadDaemonUtil.storeObject(spec);
	}
	
	@Override
	public void reportProgress(File source, long totalBytes, long bytesUploaded)
	{}

	@Override
	public boolean isCancelled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reportProgress(int progress, String message)
	{
		// TODO Auto-generated method stub
		
	}
}
