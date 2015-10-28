/*
 * ComputeWorker.java
 *
 * AVADIS Image Management System
 * Core Compute Engine Components
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
package com.strandgenomics.imaging.iengine.compute;

/**
 * ComputeWorkers is a Publisher and represents an instance of a worker-system managed by the server
 * @author arunabha
 *
 */
public class ComputeWorker extends Publisher
{
    /** 
     * current state of this worker 
     */
    protected boolean isWorking = false;
    /**
     *  the time when the last ping call was received 
     */
    protected long lastPingTime = 0;
    /** 
     * flag to indicate if this worker should be terminated 
     */
    protected boolean terminateWorker = false;
    
    /**
     * Creates an empty worker
     */
    public ComputeWorker(Publisher p)
    {
    	super(p.name, p.description, p.publisherCode, p.ipFilter);
    	this.key = p.key;
    }
    
    public void setTerminateWorker(boolean terminate) 
    {
        terminateWorker = terminate;
    }
    
    public boolean isTerminateWorker()
    {
        return terminateWorker;
    }
    
    public void setWorking(boolean status)
    {
        isWorking = status;
    }
    
    public boolean isWorking()
    {
        return isWorking;
    }
}
