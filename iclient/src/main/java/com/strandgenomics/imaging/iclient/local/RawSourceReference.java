/*
 * RawSourceReference.java
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
package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.io.Serializable;

import com.strandgenomics.imaging.icore.ISourceReference;

/**
 * This class encapsulate the format files responsible for an experiment (multi-series records)
 * @author arunabha
 *
 */
public class RawSourceReference implements ISourceReference, Serializable {
	
	private static final long serialVersionUID = 7178336811927649901L;
	/**
	 * source format file 
	 */
	protected File sourceFile = null;
	
	public RawSourceReference(File sourceFile)
	{
		this.sourceFile = sourceFile.getAbsoluteFile();
	}

	@Override
	public String getSourceFile() 
	{
		return sourceFile.getAbsolutePath();
	}
	
	@Override
	public String toString()
	{
		return sourceFile.toString();
	}

	@Override
	public long getSize() 
	{
		return sourceFile.length();
	}
	
	@Override
	public int hashCode()
	{
		return sourceFile.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof RawSourceReference)
		{
			RawSourceReference that = (RawSourceReference) obj;
			if(this == that) return true;
			return this.sourceFile.equals(that.sourceFile);
		}
		return false;
	}

	@Override
	public long getLastModified() 
	{
		return sourceFile.lastModified();
	}
}
