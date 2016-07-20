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
 * ClientSourceFile.java
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

import java.io.Serializable;

import com.strandgenomics.imaging.icore.ISourceReference;

/**
 * A specified source file contributing to the record
 * @author arunabha
 *
 */
public class ClientSourceFile implements ISourceReference, Serializable  {

	private static final long serialVersionUID = -3619021436774577888L;
	/**
	 * name of the file (the absolute file path) in the acquisition machine
	 */
	protected String filename = null;
	/**
	 * size of the source file in bytes
	 */
	protected long size = 0;
	/**
	 * last modification time of the client source file
	 */
	protected long lastModified = 0;
	
	/**
	 * A Reference to a Source files used by the client to define a multi-series record
	 * @param filename name of the source file
	 * @param size size of the source file
	 * @param lastModified the time when this source file was last modified
	 */
	public ClientSourceFile(String filename, long size, long lastModified)
	{
		this.filename = filename;
		this.size = size;
		this.lastModified = lastModified;
	}
	
	@Override
	public String toString()
	{
		return filename;
	}
	
	@Override
	public int hashCode()
	{
		return filename.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof ClientSourceFile)
		{
			ClientSourceFile that = (ClientSourceFile) obj;
			if(this == that) return true;
			return this.filename.equals(that.filename);
		}
		return false;
	}
	
	@Override
	public long getSize()
	{
		return this.size;
	}

	@Override
	public String getSourceFile() 
	{
		return this.filename;
	}

	@Override
	public long getLastModified() 
	{
		return this.lastModified;
	}
	
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) 
	{
		this.filename = filename;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) 
	{
		this.size = size;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(long lastModified) 
	{
		this.lastModified = lastModified;
	}
}
