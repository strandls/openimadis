/*
 * SourceFormat.java
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
 * Represents a source format
 * @author arunabha
 *
 */
public class SourceFormat implements Serializable, Disposable {
	
	private static final long serialVersionUID = -4259390596330036980L;
	/**
	 * name of the format
	 */
	public final String name;
	
	/**
	 * Creates a source format with then given name
	 * @param name name of the format
	 */
	public SourceFormat(String name)
	{
		this.name = name.toUpperCase();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof SourceFormat)
		{
			SourceFormat that = (SourceFormat) obj;
			return this.name.equals(that.name);
		}
		return false;
	}


	@Override
	public void dispose() 
	{
	}
}
