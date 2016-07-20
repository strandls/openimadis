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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.local.DefaultImportRequest;
import com.strandgenomics.imaging.iclient.local.DirectDirectoryImportRequest;
import com.strandgenomics.imaging.iclient.local.FilterImportRequest;
import com.strandgenomics.imaging.iclient.local.ImportRequest;
import com.strandgenomics.imaging.iclient.local.IndexerListener;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.SignaturelessImportRequest;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;

/**
 * 
 * Service to accept import request and execute them one by one
 * 
 * @author Anup Kulkarni
 */
public class IndexingService extends Thread implements IndexerListener {
	
	private Logger logger = Logger.getRootLogger();
	/**
	 * list of uploader tasks
	 */
	public List<ImportRequest> importQueue  = null;
	
	/**
	 * required by IndexerEngine
	 */
	private Context context;
	
	/**
	 * true if there are no outstanding import request; false otherwise
	 */
	private boolean importQueueEmpty = false;
	
	/**
	 * flag to see if current import job is over(true when either complete or failed; false otherwise)
	 */
	private boolean currentJobComplete = false;
	/**
	 * source file name of the import task that is currently executing; null if nothing
	 */
	private File currentTask;
	
	private int currQueueSize = 0;
	/**
	 * holds the list of files to be cancelled;
	 */
	private Set<File> toCancel;
	
	public IndexingService(Context context)
	{
		importQueue = new ArrayList<ImportRequest>();
		toCancel = new HashSet<File>();
		
		this.context = context; 
	}

	public void run()
	{
		while(true)
		{
			List<ImportRequest> currImportQueue = new ArrayList<ImportRequest>();
			currImportQueue.addAll(importQueue);
			
			currQueueSize = currImportQueue.size();
			importQueue.clear();
			currentTask = null;
			
			for (final ImportRequest request : currImportQueue) 
			{
				currentJobComplete = false;
				importQueueEmpty = false;
				currQueueSize--;
				
				currentTask = request.getRoot().getAbsoluteFile();
				
				if(toCancel.contains(currentTask))
				{
					toCancel.remove(currentTask);
					continue;
				}
				
				//actual file import
				IndexingUtils.getInstance().start(request);
				
				while (!currentJobComplete) //wait till the previous job is not done 
				{ 
					// wait till the task is done
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
			importQueueEmpty = true;
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
	 * Adds file/directory for import 
	 * @param file to import
	 */
	public synchronized void addFilesToIndex(List<File> files, boolean computeSignature, boolean createRecords)
	{
		for(File f : files)
		{
			f = f.getAbsoluteFile();
			
			DefaultImportRequest request = null;
			if(!createRecords)
			{
				request = new DirectDirectoryImportRequest(f, false, context);
			}
			else if(computeSignature)
			{
				request = new DefaultImportRequest(f, true, context);
			}
			else
			{
				request = new SignaturelessImportRequest(f, true, context);
			}
			importQueue.add( request );
			toCancel.remove(f);
		}
		context.setImportQueueSize(importQueue.size()+currQueueSize);
	}
	
	/**
	 * Adds the specified RecordMetaData for import
	 * @param userSelection
	 */
	public synchronized void addGenericImports(File rootFile, List<RecordMetaData> userSelection)
	{
		rootFile = rootFile.getAbsoluteFile();
		importQueue.add( new FilterImportRequest(rootFile, userSelection, context) );
		toCancel.remove(rootFile);
	}

	/**
	 * returns status of importing service
	 * @return true if there are no outstanding import request; false otherwise
	 */
	public boolean isImportQueueEmpty()
	{
		return this.importQueueEmpty;
	}

	@Override
	public void indexed(IRecord record)
	{
		final IRecord indexedRecord = record;
		
		logger.info("*********** indexed record "+record);
		
		if (!context.getRecords().contains(indexedRecord))
			context.addRecord(indexedRecord);
		else
			logger.info("Ignored duplicate record ");
	}

	@Override
	public void indexingStarted()
	{
		currentJobComplete = false;

		context.setImportQueueSize(importQueue.size()+currQueueSize);
	}

	@Override
	public void indexingComplete()
	{
		logger.info("*********** indexing complete ");
		
		currentJobComplete = true;
		//context.setImportQueueSize(currQueueSize);
	}
	
	@Override
	public void ignoredDuplicate(IExperiment experiment)
	{
		currentJobComplete = true;
	}

	@Override
	public void indexingFailed(boolean serverError)
	{
		currentJobComplete = true;
	//	context.setImportQueueSize(currQueueSize);
	}

	public File getCurrentTask()
	{
		return currentTask;
	}

	public synchronized void stopSelected(List<File> filesToCancel)
	{
		if (filesToCancel == null || filesToCancel.size() == 0)
			return;
		
		File fileToCancel = null;
		
		for(File f : filesToCancel)
		{
			f = f.getAbsoluteFile();
			
			if(f.equals(currentTask))
			{
				IndexingUtils.getInstance().stop();
				//currentTask = null;
				fileToCancel = f;
		//		filesToCancel.remove(f);//?
			}
			else
				toCancel.add(f);
		}
		
		if(fileToCancel != null){
			filesToCancel.remove(fileToCancel);//?
		}
	}

	@Override
	public void experimentFound(List<RawExperiment> experimentList)
	{
		if (experimentList == null || experimentList.size() < 1)
			return;
		
		context.addDirectImportExperiments(experimentList);
		
		logger.info("experiment found "+experimentList.size());
		logger.info("experiment found "+experimentList.get(0).getClass());
	}
	
}
