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
 * Object capturing history item for a record 
 * 
 * @author Anup Kulkarni
 */
public class HistoryItem {
	/**
	 * guid of the record
	 */
	private long guid;
	/**
	 * the name of client used for modification of the record
	 */
	private String appName;
	/**
	 * the version of client used for modification of the record
	 */
	private String appVersion;
	/**
	 * authorized user who modified the record
	 */
	private String modifiedBy;
	/**
	 * the time of modification
	 */
	private long modificationTime;
	/**
	 * type of the modification
	 */
	private String type;
	/**
	 * description of the modification
	 */
	private String description;
	
	public HistoryItem() 
	{ }
	
	public void setGuid(long guid)
	{
		this.guid = guid;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public void setAppVersion(String appVersion)
	{
		this.appVersion = appVersion;
	}

	public void setModifiedBy(String modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	}

	public void setModificationTime(long modificationTime)
	{
		this.modificationTime = modificationTime;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public long getGuid()
	{
		return guid;
	}

	public String getAppName()
	{
		return appName;
	}

	public String getAppVersion()
	{
		return appVersion;
	}

	public String getModifiedBy()
	{
		return modifiedBy;
	}

	public long getModificationTime()
	{
		return modificationTime;
	}

	public String getType()
	{
		return type;
	}

	public String getDescription()
	{
		return description;
	}
}
