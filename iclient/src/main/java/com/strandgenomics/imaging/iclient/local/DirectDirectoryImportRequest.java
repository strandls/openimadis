package com.strandgenomics.imaging.iclient.local;

import java.io.File;

import com.strandgenomics.imaging.icore.IValidator;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class DirectDirectoryImportRequest extends SignaturelessImportRequest{

	public DirectDirectoryImportRequest(File file, boolean recursive, IValidator validator)
	{
		super(file, recursive, validator);
		
		actualIndexer = new DirectUploadIndexer();
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
