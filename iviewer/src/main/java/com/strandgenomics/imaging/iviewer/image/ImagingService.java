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
 * ImagingService.java
 *
 * AVADIS Image Management System
 * GUI
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
package com.strandgenomics.imaging.iviewer.image;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.va.VAObject;

/**
 * To take advantage of multi-core CPU in ui application
 *  
 * Maintains a pool of threads to generate image of the requested record coordinates.
 * Once a request has been placed on this service, the request is serviced in the 
 * FIFO order and the generated image is provided back to the caller through a
 * call back method
 */
public class ImagingService {
	
	private static Object lock = new Object();
	private static ImagingService singleton = null;
	
	/**
	 * Returns the singleton instance of the ImagingService 
	 * @param <R>
	 * @return
	 */
	public static ImagingService getInstance()
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					//keep one for the UI thread
					int noOfCores = Runtime.getRuntime().availableProcessors() - 1;
					if(noOfCores == 0) noOfCores = 1;
					singleton = new ImagingService(noOfCores);
				}
			}
		}
		
		return singleton;
	}
	
    /**
     * thread pool to execute image generation concurrently
     */
    private ExecutorService m_executors = null;
    
    /**
     * creates an imaging service with the specified number of threads
     * @param noOfThreads 
     */
    private ImagingService(int noOfThreads)
    {
    	//Creates a thread pool that reuses a fixed number of threads operating off a 
    	//shared unbounded queue. At any point, at most nThreads threads will be active 
    	//processing tasks. If additional tasks are submitted when all threads are active,
    	//they will wait in the queue until a thread is available. 
    	//If any thread terminates due to a failure during execution prior to shutdown, 
    	//a new one will take its place if needed to execute subsequent tasks. 
    	//The threads in the pool will exist until it is explicitly shutdown. 
    	m_executors = Executors.newFixedThreadPool(noOfThreads);
    	System.out.println("[ImagingService]:\tcreated executer service with "+noOfThreads +" threads");
    }

    
    /**
     * shutd0own the service
     */
    public void shutdown()
    {
        if(m_executors != null)
        {
        	m_executors.shutdown();
        }
        m_executors = null;
    }
    
    /**
     * Adds a request to generate image with the given specifications
     * @param record the record whose image needs to be generated
     * @param imageCoordinates the set of image coordinates
     * @param vos list of visual annotation objects to overlay
     * @param channelState 
     * @param channelState 
     * @param consumer the object ready to consume the image once it is created
     */
    public synchronized void addRequest(IRecord record, int frame, int slice, int site, Set<Integer> channels, 
    		List<VAObject> vos, boolean useChannelColor, int channelState, int sliceState,  ImageConsumer consumer)
    {
    	ImagingTask task = ImagingTaskFactory.createTask(record, frame, slice, site, channels, vos, useChannelColor, channelState, sliceState,  consumer);
    	
    	//submit the task for execution
    	m_executors.submit(task);
    }
}
