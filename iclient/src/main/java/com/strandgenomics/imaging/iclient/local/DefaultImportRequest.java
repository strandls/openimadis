/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
