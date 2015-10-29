/*
 * Site.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.ispace;

public class RecordSite {
	
	/**
	 * name of the site
	 */
	protected String name;
	/**
	 * a site is mapped to the series number of a record
	 */
	protected int seriesNo;

	public RecordSite()
	{}
	
    /**
     * A record may be part of a series (of records). 
     * This method returns the series number for this record if any. default is zero
     * @return the series number for this record if any zero otherwise
     */
    public int getSeriesNo()
    {
    	return seriesNo;
    }
    
    public void setSeriesNo(int value)
    {
    	seriesNo = value;
    }

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
}
