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
