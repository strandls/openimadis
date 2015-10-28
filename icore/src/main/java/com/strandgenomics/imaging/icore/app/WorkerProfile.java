/*
 * WorkerCV.java
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
package com.strandgenomics.imaging.icore.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The profile of a compute worker
 * @author arunabha
 *
 */
public class WorkerProfile {

    /**
     * List of supported applications
     */
    protected Set<Application> supportedApplications = null;
	/**
	 * architecture of the machine, one of 64 or 32 only
	 */
    protected Architecture architecture;
    /**
     * platform/os of the machine
     */
    protected Platform platform;
    /**
     * available RAM that can be allocated
     */
    protected long availableRAM = -1;
    /**
     * available disk-storage that can be used
     */
    protected long availableDiskSpace = -1;
    /**
     * number of available cores/processors
     */
    protected int noOfProcessors = -1;
    
    /**
     * creates an empty profile
     */
    public WorkerProfile()
    {
    	supportedApplications = new HashSet<Application>();
    	architecture = Architecture.NA;
    	platform = Platform.NA;
    }

    public WorkerProfile(Architecture arch, Platform p, long ram, long df, int noOfCores, Application ... apps)
    {
    	this();
    	
        architecture = arch == null ?  Architecture.NA : arch;
        platform = p == null ? Platform.NA : p;
        
        availableRAM = ram;
        availableDiskSpace = df;
        noOfProcessors = noOfCores;

        supportedApplications.addAll(Arrays.asList(apps));
    }
    
    public Application[] getSupportedContexts()
    {
        if(supportedApplications == null || supportedApplications.isEmpty())
        {
            return null;
        }
        else 
        {
            List<Application> appContexts = new ArrayList<Application>();
            for(Application a : supportedApplications)
            	supportedApplications.add(a);
            
            return appContexts.toArray(new Application[0]);
        }
    }
    
    public Platform getPlatform()
    {
        return platform;
    }
    
    public Architecture getArchitecture()
    {
    	return architecture;
    }
    
    public long getAvailableDiskSpace() 
    {
        return availableDiskSpace;
    }

    public long getAvailableRAM() 
    {
        return availableRAM;
    }
    
    public int getAvailableProcessors()
    {
    	return noOfProcessors;
    }
}