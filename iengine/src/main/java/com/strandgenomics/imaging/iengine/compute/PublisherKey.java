/*
 * PublisherKey.java
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

import java.io.Serializable;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * Identifies a publisher (worker-system)
 * @author arunabha
 *
 */
public class PublisherKey implements Serializable {
	
	private static final long serialVersionUID = 6711573320018149546L;
	/**
	 * the access token associated with this PublisherKey
	 */
	public final String accessToken;
	/**
	 * The address this key is bound with
	 */
	public final String ipAddress;
	
	/**
	 * Creates a publisher key bound with the specified IP address
	 * @param ipAddress
	 */
	public PublisherKey(String ipAddress)
	{
		this.ipAddress = ipAddress;
		this.accessToken = "PKEY_"+Util.generateRandomString(35);
	}

	/**
	 * Returns the access token associated with this publisher key
	 * @return the access token used by the publisher
	 */
	public String getAccessToken()
	{
		return accessToken;
	}
	
	@Override
	public int hashCode()
	{
		return accessToken.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof PublisherKey)
		{
			PublisherKey that = (PublisherKey) obj;
			if(this == that) return true;
			return this.accessToken.equals(that.accessToken);
		}
		
		return false;
	}
}
