package com.strandgenomics.imaging.iacquisition;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.UploadStatus;

/**
 * service to accept upload requests and execute them one by one
 * 
 * @author Anup Kulkarni
 */
public class UploaderService extends Thread {
	/**
	 * list of uploader tasks
	 */
	public List<UploadTask> uploaders;
	public ArrayList<UploadTask> curruploaders;
	
	public boolean uploadQueueEmpty = false; 

	public UploaderService() {
		uploaders = new ArrayList<UploadTask>();
		curruploaders = new ArrayList<UploadTask>();
	}

	public void run()
	{
		while(true)
		{
			curruploaders = new ArrayList<UploadTask>(uploaders);
			uploaders.clear();
			for (UploadTask upload : curruploaders) 
			{
				uploadQueueEmpty = false;
				
				if(upload.taskCancelled())
					continue;
				
				upload.execute();

				while (upload.getProgress() < 100) 
				{ // wait till the task is done
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			uploadQueueEmpty = true;
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//release CPU when there are no upload requests
		}
	}

	/**
	 * add the uploader task
	 * @param task
	 */
	public synchronized void addTask(UploadTask task) {
		uploaders.add(task);
	}

	/**
	 * returns the state of outstanding uploads
	 * @return the state of outstanding uploads
	 */
	public boolean isUploadQueueEmpty(){
		return uploadQueueEmpty;
	}
	
	/**
	 * attempts to cancel selected tasks; 
	 * tasks cannot be cancelled if transferring archive tar ball is done
	 * 
	 * @param rows selected row indices in upload table 
	 */
	public void cancelSelectedTasks(List<String> tasks) 
	{
		for (int i = 0; i < curruploaders.size(); i++) 
		{
			UploadTask upload = curruploaders.get(i);
			if (tasks.contains(upload.getExperimentName()) && upload.canCancel()) 
			{
				upload.cancelTask();
			}
			else{
				JOptionPane.showMessageDialog(null, "Cannot be cancelled now.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		for (int i = 0; i < uploaders.size(); i++) 
		{
			UploadTask upload = uploaders.get(i);
			if (tasks.contains(upload.getExperimentName()) && upload.canCancel()) 
			{
				upload.cancelTask();
			}
		}
	}

	public void shutdown() 
	{
		for (int i = 0; i < curruploaders.size(); i++) 
		{
			UploadTask upload = curruploaders.get(i);
			RawExperiment experiment = upload.getExperiment();
			if(experiment.getUploadStatus() == UploadStatus.Queued)
				experiment.setUploadStatus(UploadStatus.NotUploaded);
		}
		
		for (int i = 0; i < uploaders.size(); i++) 
		{
			UploadTask upload = uploaders.get(i);
			RawExperiment experiment = upload.getExperiment();
			if(experiment.getUploadStatus() == UploadStatus.Queued)
				experiment.setUploadStatus(UploadStatus.NotUploaded);
		}
	}
}
