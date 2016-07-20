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

/*
 * RecordSources.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.loader;


/**
 * Specification of the source files generating a record
 * @author arunabha
 *
 */
public class Archive {
	
	/**
	 * md5 hash of the source file(s) - if there are multiple source files
	 * the files are sorted by name followed by size and MD5 hash is computed using all these
	 * files in this order
	 */
	private String signature;
	/**
	 * list of all source files generating the record
	 */
	private SourceFile[] sourceFiles;
	/**
	 * root source directory
	 */
	private String rootDirectory;
	/**
	 * typically, the name of one of the source file
	 */
	private String name;
	
	public Archive()
	{}

	/**
	 * @return the rootDirectory
	 */
	public String getRootDirectory() {
		return rootDirectory;
	}

	/**
	 * @param rootDirectory the rootDirectory to set
	 */
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() 
	{
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) 
	{
		this.signature = signature;
	}

	/**
	 * @return the sourceFiles
	 */
	public SourceFile[] getSourceFiles()
	{
		return sourceFiles;
	}

	/**
	 * @param sourceFiles the sourceFiles to set
	 */
	public void setSourceFiles(SourceFile[] sourceFiles) 
	{
		this.sourceFiles = sourceFiles;
	}
}
