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

import java.util.List;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.app.Application;

/**
 * Object capturing history item for a record 
 * 
 * @author Anup Kulkarni
 */
public class HistoryObject implements Storable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4419815919159903318L;
	/**
	 * history id
	 */
	private long historyID;
	/**
	 * guid of the record
	 */
	private long guid;
	/**
	 * the client used for modification of the record
	 */
	private Application client;
	/**
	 * access token used for modification
	 */
	private String accessToken;
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
	private HistoryType type;
	/**
	 * description of the modification
	 */
	private String description;
	/**
	 * list of arguments
	 */
	private List<String> arguments;
	
	public HistoryObject(long guid, Application app, String modifiedBy, String accessToken, long modificationTime, HistoryType type, String description, List<String>args)
	{
		this.guid = guid;
		this.description = description;
		this.modifiedBy = modifiedBy;
		this.modificationTime = modificationTime;
		this.client = app;
		this.type = type;
		this.accessToken = accessToken;
		this.arguments = args;
	}
	
	public HistoryObject(long historyID, long guid, Application app, String modifiedBy, String accessToken, long modificationTime, HistoryType type, String description, List<String>args)
	{
		this.historyID = historyID;
		this.guid = guid;
		this.description = description;
		this.modifiedBy = modifiedBy;
		this.accessToken = accessToken;
		this.modificationTime = modificationTime;
		this.client = app;
		this.type = type;
		this.arguments = args;
	}

	/**
	 * returns task id if history item is of relavent type
	 * @return task id if history item is of relavent type
	 */
	public Long getTaskId()
	{
		if(this.type == HistoryType.TASK_EXECUTED || this.type == HistoryType.TASK_FAILED || this.type == HistoryType.TASK_SUCCESSFUL || this.type == HistoryType.TASK_TERMINATED)
		{
			String[] fields = description.split("=");
			String taskID = fields[fields.length-1].split("\\)")[0].trim();
			return Long.parseLong(taskID);
		}
		return null;
	}
	
	/**
	 * the record id
	 * @return record id
	 */
	public long getGuid()
	{
		return this.guid;
	}

	/**
	 * the client used for modification of the record
	 * @return the client used for modification of the record
	 */
	public Application getClient()
	{
		return this.client;
	}

	/**
	 * authorized user who modified the record
	 * @return authorized user who modified the record
	 */
	public String getModifiedBy()
	{
		return this.modifiedBy;
	}

	/**
	 * the time of modification
	 * @return the time of modification
	 */
	public long getModificationTime()
	{
		return this.modificationTime;
	}

	/**
	 * type of the modification
	 * @return type of the modification
	 */
	public HistoryType getType()
	{
		return this.type;
	}

	/**
	 * description of the modification
	 * @return description of the modification
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * returns history id 
	 * @return history id 
	 */
	public long getHistoryID()
	{
		return this.historyID;
	}
	
	/**
	 * returns access token used to perform the modification
	 * @return access token used to perform the modification
	 */
	public String getAccessToken()
	{
		return this.accessToken;
	}
	
	/**
	 * returns the arguments(if any) associated with the history object
	 * @return the arguments(if any) associated with the history object
	 */
	public List<String> getArguments()
	{
		return this.arguments;
	}

	@Override
	public void dispose()
	{ }
}
