/*
 * ImageSpaceObject.java
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
package com.strandgenomics.imaging.iclient;

import java.io.Serializable;

import com.strandgenomics.imaging.icore.Disposable;

/**
 * Base class for all objects interested in making system call to the 
 * imaging system
 */
public abstract class ImageSpaceObject implements Disposable, Serializable  {

	private static final long serialVersionUID = -4350286324029229034L;
	
	//singleton instance
    private static ImageSpaceSystem imageSystem = null;
    private static Object lock = new Object();

    /**
     * Returns the handle to the ImageSpaceSystem associated with the 
     * Enterprise IMG Server for login, logout and other useful methods
     */
    public static ImageSpace getConnectionManager()
    {
        return getImageSpace();
    }
    
    /**
     * Returns the handle to the ImageSpaceSystem associated with the 
     * Enterprise IMG Server for login, logout and other useful methods
     */
    public static ImageSpaceManagement getSystemManager()
    {
        return getImageSpace();
    }
    
    public static ImageSpaceSystem getImageSpace()
    {
    	if(imageSystem == null)
        {
            synchronized(lock)
            {
                if(imageSystem == null)
                {
                	imageSystem = ImageSpaceFactory.createImageSpaceInstance();
                }
            }
        }
        return imageSystem;
    }

    public ImageSpaceObject(){}
    
    public static void main(String ... args) throws Exception
    {
    	String appID = "TestPrograms";
    	
    	ImageSpace ispace = ImageSpaceObject.getConnectionManager();
    	String hostIP = args[0];
    	int hostPort = Integer.parseInt(args[1]);
    	String authCode = args[2];
    	
    	boolean successful = ispace.login(false, hostIP, hostPort, appID, authCode);
    	    	
    	System.out.println("successful="+successful);
    }
}
