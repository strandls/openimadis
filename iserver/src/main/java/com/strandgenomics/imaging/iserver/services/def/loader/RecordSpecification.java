/*
 * RecordSpecification.java
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
package com.strandgenomics.imaging.iserver.services.def.loader;


/**
 * Specification of the record to be created by the server
 * Specifies the sites that needs to be merged, additionally specifies the
 * custom channel colors and contrast
 * @author arunabha
 *
 */
public class RecordSpecification {
	
	/**
	 * List of sites to merge into a single record
	 */
	private RecordSite[] siteToMerge = null;
	/**
	 * custom channel specifications for the record
	 */
	private Channel[] customChannels = null;
	
	public RecordSpecification()
	{}

	/**
	 * @return the siteToMerge
	 */
	public RecordSite[] getSiteToMerge()
	{
		return siteToMerge;
	}

	/**
	 * @param siteToMerge the siteToMerge to set
	 */
	public void setSiteToMerge(RecordSite[] siteToMerge)
	{
		this.siteToMerge = siteToMerge;
	}

	/**
	 * @return the customColors
	 */
	public Channel[] getCustomChannels() 
	{
		return customChannels;
	}

	/**
	 * @param customColors the customColors to set
	 */
	public void setCustomChannels(Channel[] customColors)
	{
		this.customChannels = customColors;
	}
}
