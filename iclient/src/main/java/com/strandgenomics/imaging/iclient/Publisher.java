/*
 * Publisher.java
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

import java.util.List;



/**
 * Publisher of applications
 * @author arunabha
 *
 */
public class Publisher extends ImageSpaceObject {
	
	private static final long serialVersionUID = -7804187387651016078L;
	/**
	 * name of the publisher (unique)
	 */
	protected String name;
	
	Publisher(String name)
	{
		this.name = name;
	}
	
	/**
	 * Returns the name of the publisher publishing a application
	 * @return the name of the publisher
	 */
	public String getName()
	{
		return name;
	}

    /**
     * List applications published by this publisher
     * @param categoryName name of the category or null
     * @return a list of applications published by this publisher
     */
    public List<Application> listApplications(String categoryName)
    {
    	return getImageSpace().listApplications(this, categoryName);
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
