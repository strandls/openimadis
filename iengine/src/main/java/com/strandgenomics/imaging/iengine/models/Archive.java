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

package com.strandgenomics.imaging.iengine.models;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.Storable;

public class Archive implements Storable {
	
	private static final long serialVersionUID = 303097238924475161L;
	/**
	 * list of source files (as found in the acquisition machine
	 */
	protected List<ISourceReference> sourceFiles;
	/**
	 * MD5 hash of all the constituent files 
	 */
	protected BigInteger signature;
	/**
	 * name of record specific folder - typically record file-name as the folder name
	 * this folder will contain the record source files 
	 * this folder will also contain an attachment folder containing the attachments 
	 * in case of an existing folder of the same name, will create folder by suffixing with number, 
	 * till a unique folder is created
	 */
	protected String archiveFolder;
	/**
	 * name of the folder containing the record-archive, typically project-name/user-name
	 */
	protected String rootFolder;
	
	public Archive(BigInteger signature, String rootFolder, String archiveFolder, List<ISourceReference> sourceFiles)
	{
		this.sourceFiles = sourceFiles;
		this.signature = signature;
		this.archiveFolder = archiveFolder;
		this.rootFolder = rootFolder;
	}

	@Override
	public void dispose() 
	{
		signature = null;
		sourceFiles = null;
	}
	
	public List<ISourceReference> getSourceFiles() {
		return sourceFiles;
	}

	public BigInteger getSignature() {
		return signature;
	}

	/**
	 * name of record specific folder - typically record file-name as the folder name
	 * this folder will contain the record source files 
	 * this folder will also contain an attachment folder containing the attachments 
	 * in case of an existing folder of the same name, will create folder by suffixing with number, 
	 * till a unique folder is created
	 */
	public String getArchiveFolder() 
	{
		return archiveFolder;
	}

	/**
	 * name of the folder containing the record-archive, typically project-name/user-name
	 * @return
	 */
	public String getRootFolder() 
	{
		return rootFolder;
	}
	
	public String getStorageLocation()
	{
		//by default the path will wave unix separator
		return rootFolder.replace('/', File.separatorChar) +File.separator + archiveFolder;
	}
	
	public String toString()
	{
		return getStorageLocation();
	}
}
