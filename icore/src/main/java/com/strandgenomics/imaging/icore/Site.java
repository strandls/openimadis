/*
 * Site.java
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

import java.io.Serializable;

/**
 * Represents a site of a record. By default, the records are single site records till they are merged
 * as a multi-site record
 * @author nimisha
 */
public class Site implements Serializable {

	private static final long serialVersionUID = 5116314082882541146L;
	
	/**
	 * name of the site
	 */
	protected String name;
	/**
	 * a site is mapped to the series number of a record
	 */
	protected final int seriesNo;

	/**
	 * creates a default site 
	 * @param name name of this site
	 */
	public Site(int seriesNo, String name)
	{
		this.seriesNo = seriesNo;
		this.name = name;
	}
	
    /**
     * A record may be part of a series (of records). 
     * This method returns the series number for this record if any. default is zero
     * @return the series number for this record if any zero otherwise
     */
    public int getSeriesNo()
    {
    	return seriesNo;
    }

    /**
     * series name
     * @return name of the series
     */
	public String getName() {
		return name;
	}

	
	public void setName(String name) 
	{
		this.name = name;
	}
	
    @Override
    public int hashCode()
    {
    	return seriesNo;
    }
    
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof Site)
    	{
    		Site that = (Site) obj;
    		if(this == that) return true;
    		
    		return this.seriesNo == that.seriesNo;
    	}
    	return false;
    }
}
