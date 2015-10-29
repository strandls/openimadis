/*
 * CreationRequest.java
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
 * Defines the context of the client when he upload a bunch of source files (as a tar-ball)
 * to upload a multi-series record (experiment)
 * @author arunabha
 *
 */
public class CreationRequest {
	
	/**
	 * List of client side source files that is actually transferred (as the tar ball)
	 */
	private Archive archive = null;
	/**
	 * MAC address of the client machine
	 */
	private String clientMacAddress = null;
	/**
	 * list of signatures (or record finger-prints) to extract, can be null
	 */
	private RecordSpecification[] specifications = null;
	
	public CreationRequest()
	{}
	
	public Archive getArchive() 
	{
		return archive;
	}

	public void setArchive(Archive archive) 
	{
		this.archive = archive;
	}


	public String getClientMacAddress()
	{
		return clientMacAddress;
	}

	public void setClientMacAddress(String clientMacAddress) 
	{
		this.clientMacAddress = clientMacAddress;
	}
	
	public RecordSpecification[] getValidSignatures()
	{
		return specifications;
	}
	
	public void setValidSignatures(RecordSpecification[] value)
	{
		specifications = value;
	}
}
