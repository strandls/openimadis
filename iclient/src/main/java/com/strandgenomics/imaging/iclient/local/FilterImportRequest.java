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

package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.local.genericimport.GenericExperiment;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.IValidator;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;

/**
 * Do a importing based on file-name based identification of specific images of records
 * @author arunabha
 *
 */
public class FilterImportRequest implements ImportRequest {
	
	/**
	 * Logger
	 */
	private Logger logger = Logger.getRootLogger();
    /**
     * List of listeners listening for record addition events
     */
	protected EventListenerList listenerList = new EventListenerList();
	/**
	 * the list of records to extracts
	 */
	protected List<RecordMetaData> extractedMeta;
	/**
	 * root import folder
	 */
	protected File rootFolder;
	/**
	 * the validator
	 */
	protected IValidator validator = null;
	
	protected Worker worker = null;
	
	protected boolean stop = false;
	
	/**
	 * Creates a import request with the specified filter and a folder
	 * @param extractedMeta the list of extracted meta data
	 */
	public FilterImportRequest(File folder, List<RecordMetaData> extractedMeta, IValidator validator)
	{
		this.rootFolder = folder;
		this.extractedMeta = extractedMeta;
		this.validator = validator;
	}
	
	@Override
	public File getRoot() 
	{
		return rootFolder;
	}
	
	@Override
	public String toString()
	{
		return rootFolder.toString();
	}
	
	@Override
	public int hashCode()
	{
		return rootFolder.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof FilterImportRequest)
		{
			FilterImportRequest that = (FilterImportRequest) obj;
			if(this == that) return true;
			return this.rootFolder.equals(that.rootFolder);
		}
		
		return false;
	}

	@Override
	public void start() 
	{
		stop = false;
		worker = new Worker();
		worker.start();
	}

	@Override
	public void stop()
	{
		stop = true;
		worker.interrupt();
		worker = null;
	}

	public void addIndexerListener(IndexerListener listener) 
	{
		listenerList.add(IndexerListener.class, listener);
	}
	
	public void removeIndexerListener(IndexerListener listener) 
	{
		listenerList.remove(IndexerListener.class, listener);
	}
	
    public void fireStartEvent()
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexingStarted();
	    	}
	    }
    }
    
    public void fireCompletionEvent()
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexingComplete();
	    	}
	    }
    }
    
    public void fireFailedEvent(boolean serverError)
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexingFailed(serverError);
	    	}
	    }
    }
    
    public void fireRecordFound(IRecord record) 
    {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).indexed(record);
	    	}
	    }
    }
    
    public void fireFoundDuplicate(IExperiment experiment)
	{
    	Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) 
	    {
	    	if (listeners[i] == IndexerListener.class) 
	    	{
	    		((IndexerListener) listeners[i+1]).ignoredDuplicate(experiment);
	    	}
	    }
	}
    
    private class Worker extends Thread
    {
    	public void run()
    	{
    		fireStartEvent();
    		
			for(RecordMetaData metaData : extractedMeta)
			{
				if(stop) break;
				try 
				{
					GenericExperiment expt = new GenericExperiment(metaData);
					boolean duplicate = validator.isDuplicate(expt);
					
					if(duplicate)
					{
						Logger.getRootLogger().info("[Indexer]:\tIgnoring duplicate import. " + metaData.name);
						fireFoundDuplicate(expt);
						continue;
					}
					
					boolean uploaded = validator.isUploaded(expt);
					Logger.getRootLogger().info("[Indexer]:\t Checking for uploaded " + uploaded);
					validator.setUploaded(expt, uploaded);
					
					BioRecord r = expt.extractRecord(); //extract the record
					if(r != null) fireRecordFound(r);
						
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					logger.error("[Indexer]:\t Error Processing Import: " + metaData.name+" "+e.getMessage());
                	SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run()
						{
							fireFailedEvent(false);
						}
					});
				}
			}
			
			fireCompletionEvent();
    	}
    }
}
