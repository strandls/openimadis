/*
 * ISourceReference.java
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

/**
 * Specification of a source file producing an experiment (muti-series records)
 * @author arunabha
 *
 */
public interface ISourceReference {
	
	/**
	 * Returns the actual name of the source file (fully qualified absolute path in the acquisition machine)
	 * @return name of the source file
	 */
	public String getSourceFile();
	
	/**
	 * Returns the size of the source file
	 * @return the size of the source file
	 */
	public long getSize();
	
	/**
	 * Returns the time when this source was last modified
	 * @return the time when this source was last modified
	 */
	public long getLastModified();
}
