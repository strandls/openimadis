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
