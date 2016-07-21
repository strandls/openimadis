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