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

package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * A place holder of many records - may be an administrative unit or biological
 * @author arunabha
 */
public class Project {
	
	/**
	 * name of the project
	 */
	private String name;
	/**
	 * notes/comments/description associated with this project
	 */
	private String notes;
	/**
	 * creation date, the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 */
	private long creationDate;
	/** 
	 * disk quota in gb 
	 */
	private double storageQuota;
	/** 
	 * disk usage in gb 
	 */
	private double spaceUsage;
	/**
	 * Number of records within a project
	 */
	private int recordCount = 0;
	
	//default constructor, as a requirement of java beans
	public Project()
	{}
	
	/**
	 * @return the storageQuota
	 */
	public double getStorageQuota() {
		return storageQuota;
	}

	/**
	 * @param storageQuota the storageQuota to set
	 */
	public void setStorageQuota(double storageQuota) {
		this.storageQuota = storageQuota;
	}

	/**
	 * @return the spaceUsage
	 */
	public double getSpaceUsage() {
		return spaceUsage;
	}

	/**
	 * @param spaceUsage the spaceUsage to set
	 */
	public void setSpaceUsage(double spaceUsage) {
		this.spaceUsage = spaceUsage;
	}
	
	/**
	 * Returns the name of the project
	 * Project names are unique across the Enterprise IMG Server
	 * @return the name of the project
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Notes or comments associated with the project 
	 * @return Notes or comments associated with the project 
	 */
	public String getNotes()
	{
		return notes;
	}
	
	/**
	 * Returns the creation time
	 * @return the creation time
	 */
	public long getCreationDate()
	{
		return creationDate;
	}
    
    
	/**
	 * Sets the name of the project
	 * Project names are unique across the Enterprise IMG Server
	 */
	public void setName(String value)
	{
		name = value;
	}
	
	/**
	 * Sets Notes or comments associated with the project 
	 */
	public void setNotes(String value)
	{
		notes = value;
	}
	
	/**
	 * Sets the creation time
	 */
	public void setCreationDate(long value)
	{
		creationDate = value;
	}

	/**
	 * @return the recordCount
	 */
	public int getRecordCount() 
	{
		return recordCount;
	}

	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(int recordCount) 
	{
		this.recordCount = recordCount;
	}
}
