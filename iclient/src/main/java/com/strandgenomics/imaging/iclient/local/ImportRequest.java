/*
 * ImportRequest.java
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

/**
 * Handles import - e.g., file/folder based imports, or a generic pattern based imports
 * @author arunabha
 *
 */
public interface ImportRequest {
	
	/**
	 * Returns the root directory or a specific file chosen for import 
	 * @return  the root directory or a specific file chosen for import 
	 */
	public File getRoot();
	
	/**
	 * Starts the import
	 */
	public void start();
	
	/**
	 * Stops the import
	 */
	public void stop();
	
	/**
	 * add the specified index listener to this importer
	 * @param listener the listener to add
	 */
	public void addIndexerListener(IndexerListener listener) ;
	
	/**
	 * removes the specified listener from this importer
	 * @param listener the listener to remove
	 */
	public void removeIndexerListener(IndexerListener listener); 
}
