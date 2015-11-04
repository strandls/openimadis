/*
 * IndexingUtils.java
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
package com.strandgenomics.imaging.iacquisition;

import javax.swing.event.EventListenerList;

import com.strandgenomics.imaging.iclient.local.ImportRequest;
import com.strandgenomics.imaging.iclient.local.IndexerListener;

/**
 * @author mayuresh
 *
 */
public class IndexingUtils {
	
	private static IndexingUtils instance = new IndexingUtils();
	
	private IndexingUtils()
	{}
	
	public static IndexingUtils getInstance()
	{
		return instance;
	}

	public void addIndexerListener(IndexerListener listener) 
	{
	    listenerList.add(IndexerListener.class, listener);
	}
	
	public void removeIndexerListener(IndexerListener listener) 
	{
	    listenerList.remove(IndexerListener.class, listener);
	}
	
	public synchronized void start(ImportRequest request)
	{
		IndexerListener[] listeners = listenerList.getListeners(IndexerListener.class);
		if(listeners != null)
		{
			for(IndexerListener l : listeners)
			{
				request.addIndexerListener(l);
			}
		}
		
		currentIndexer = request;
		request.start();
		System.out.println("[IndexingUtils]:\tstarting...");
	}
	
	public synchronized void stop()
	{
		if(currentIndexer != null)
		{
			currentIndexer.stop();
			currentIndexer = null;
		}
		
		System.out.println("[IndexingUtils]:\tstopping...");
	}
	
	/**
	 * current indexer
	 */
	protected ImportRequest currentIndexer = null;
    /**
     * List of listeners listening for record addition events
     */
	protected EventListenerList listenerList = new EventListenerList();
}
