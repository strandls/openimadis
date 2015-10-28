/*
 * RecordArchive.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.iengine.bioformats;

import java.util.List;

public class RecordArchive {

	/**
	 * list of source files (as found in the acquisition machine
	 */
	protected List<ClientSourceFile> sourceFiles;
	/**
	 * MD5 hash of all the constituent files 
	 */
	protected String signature;
	
	public RecordArchive(List<ClientSourceFile> sourceFiles, String signature)
	{
		this.sourceFiles = sourceFiles;
		this.signature = signature;
	}
}
