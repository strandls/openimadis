/*
 * DefaultImportRequest.java
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

import com.strandgenomics.imaging.icore.IValidator;

/**
 * Default importer, indexes a file/folder 
 * @author arunabha
 *
 */
public class DefaultImportRequest implements ImportRequest {
	
	/**
	 * the root file/folder ti index
	 */
	protected File indexFile;
	/**
	 * the validator
	 */
	protected IValidator validator = null;
	/**
	 * checks whether to index sub-directories also
	 */
	protected boolean recursive = false;
	/**
	 * the actual indexer
	 */
	protected Indexer actualIndexer = null;
	
	protected boolean computeSignature=false;
		
	
	/**
	 * Creates an instance of the default indexer
	 * @param file the file to index
	 * @param recursive whether to do recursive indexing
	 * @param validator the validator of generated records
	 */
	public DefaultImportRequest(File file, boolean recursive, IValidator validator)
	{
		indexFile = file.getAbsoluteFile();
		if(!indexFile.exists()) 
			throw new IllegalArgumentException("file or folder not found "+file);
		
		actualIndexer = new Indexer();
		this.recursive = recursive;
		this.validator = validator;
	}
	
	@Override
	public File getRoot() 
	{
		return indexFile;
	}
	
	@Override
	public String toString()
	{
		return indexFile.toString();
	}
	
	@Override
	public int hashCode()
	{
		return indexFile.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof DefaultImportRequest)
		{
			DefaultImportRequest that = (DefaultImportRequest) obj;
			if(this == that) return true;
			return this.indexFile.equals(that.indexFile);
		}
		
		return false;
	}

	@Override
	public void start() 
	{
		actualIndexer.startIndexing(indexFile,  recursive, validator);
		System.out.println("[IndexingUtils]:\tstarting..."+indexFile);
	}

	@Override
	public void stop()
	{
		if(actualIndexer != null)
			actualIndexer.stopIndexing();
		
		actualIndexer = null;
	}

	@Override
	public void addIndexerListener(IndexerListener listener) 
	{
		actualIndexer.addIndexerListener(listener);
	}
	
	@Override
	public void removeIndexerListener(IndexerListener listener) 
	{
		actualIndexer.removeIndexerListener(listener);
	}
}
