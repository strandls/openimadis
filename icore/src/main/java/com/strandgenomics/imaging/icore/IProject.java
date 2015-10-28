/*
 * IProject.java
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
package com.strandgenomics.imaging.icore;

import java.util.Date;

public interface IProject {
	
	/**
	 * Disk quota in GBs
	 * @return Disk quota in GBs
	 */
	public double getDiskQuota();
	
	/**
	 * Disk space consumption in GBs
	 * @return Disk space consumption in GBs
	 */
	public double getSpaceUsage();
	
	/**
	 * Returns the name of the project
	 * Project names are unique across the Enterprise IMG Server
	 * @return the name of the project
	 */
	public String getName();
	
	/**
	 * Notes or comments associated with the project 
	 * @return Notes or comments associated with the project 
	 */
	public String getNotes();
	
	/**
	 * Returns the creation time
	 * @return the creation time
	 */
	public Date getCreationDate();
	    
    /**
     * Returns the list of available records for the login user to read 
     * @return the list of available records for the login user to read 
     */
    public int getRecordCount();
}
