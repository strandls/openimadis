/*
 * Publisher.java
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.ApplicationSpecification;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * The system that publishes applications to this compute-system
 * @author arunabha
 *
 */
public class Publisher implements Storable{
	
	/**
	 * name of the publisher
	 */
	public final String name;
	/**
	 * short note describing the publisher
	 */
	public final String description;
	/**
	 * system generated code to identify a publisher 
	 */
	public final String publisherCode;
	/**
	 * ip filters.TODO
	 */
	public final String ipFilter;
	/**
	 * the generated publisher key
	 */
	protected PublisherKey key = null;
    /**
     * List of supported applications
     */
    protected Set<ApplicationSpecification> supportedApplications = null;
    
    /**
     * Create a publisher instance
     * @param name name of the publisher
     * @param description description of the publisher
     */
	public Publisher(String name, String description)
	{
		this(name, description, "PUBLISHER_"+Util.generateRandomString(30), null);
	}
	
    /**
     * Create a publisher instance
     * @param name name of the publisher
     * @param description description of the publisher
     */
	public Publisher(String name, String description, String publisherCode, String ipFilter)
	{
		this.name          = name;
		this.description   = description;
		this.publisherCode = publisherCode;
		this.ipFilter      = ipFilter;
		this.supportedApplications = new HashSet<ApplicationSpecification>();
	}
	
	/**
	 * Validates the ipAddress and publisherCode combination and generates a relevant key
	 * @param ipAddress the ip address of the publisher
	 * @param publisherCode the publisher registration code
	 * @return an instance of a PublisherKey iff the ipAddress and code is valid, null otherwise
	 */
	public PublisherKey validate(String ipAddress, String registeredCode, String publisherCode)
	{
		if(ipAddress == null || publisherCode == null || registeredCode == null)
			return null;
		
		if(!registeredCode.equals(publisherCode))
			return null;
		
		if(key == null) 
			key = new PublisherKey(ipAddress);

		return key;
	}
    
    /**
     * Checks if this worker-system supports the specified application
     * @param app the application under consideration
     * @return true if the specified application is supported, false otherwise
     */
    public boolean isApplicationSupported(Application app)
    {
    	return supportedApplications.contains(app);
    }
    
    /**
     * Returns the IP address the publisher
     * @return  the IP address the publisher
     */
    public PublisherKey getID()
    {
    	return key;
    }
    
    /**
     * Returns the number of applications supported by this publisher
     * @return the number of applications supported by this publisher
     */
    public int capacity()
    {
    	return supportedApplications.size();
    }
    
    /**
     * Removes the specified applications
     * @param app
     */
    public ApplicationSpecification removeApplication(Application app)
    {
    	for(ApplicationSpecification a : supportedApplications)
    	{
    		if(app.equals(a))
    		{
    			supportedApplications.remove(a);
    			return a;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Adds the following applications to the profile of this compute worker
     * @param apps the supported applications
     */
    public void addApplication(ApplicationSpecification ... apps)
    {
		for(ApplicationSpecification a : apps)
		{
			supportedApplications.add(a);
		}
    }
    
    /**
     * Adds the following applications to the profile of this compute worker
     * @param apps the supported applications
     */
    public void addApplication(Collection<ApplicationSpecification> apps)
    {
    	supportedApplications.addAll(apps);
    }
    
    /**
     * returns set of all the supported applications
     * @return set of all the supported applications
     */
    public Set<ApplicationSpecification> getSupportedApplications()
	{
		return supportedApplications;
	}

	@Override
	public void dispose()
	{ }
}
