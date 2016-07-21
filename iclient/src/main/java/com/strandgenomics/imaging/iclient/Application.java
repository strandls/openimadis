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

package com.strandgenomics.imaging.iclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Represents an application to be executed (scheduled for execution) by the server
 * @author arunabha
 *
 */
public class Application implements Serializable  {
	
	private static final long serialVersionUID = 5283925749816303859L;
	/**
	 * name of the application
	 */
	protected String name;
	/**
	 * version of this application
	 */
	public final String version; 
	/**
	 * general description of the application
	 */
	protected String description;
	/**
	 * broad area (or domain) where this application is relevant - for example Quality Control, Statistics 
	 */
	protected String categoryName;
	/**
	 * name of the publisher
	 */
	protected Publisher publisher = null;
	/**
	 * list of parameters
	 */
	protected Set<Parameter> parameters = null;
	
    Application(String name, String version, String category, String notes)
	{
		this.name = name;
		this.version = version;
		this.categoryName = category;
		this.description = notes;
	}

	public void dispose() 
	{
		publisher = null;
		name = null;
		description = null;
		categoryName = null;
	}
	
	/**
	 * Returns the publisher of this application
	 * @return
	 */
	public synchronized Publisher getPublisher()
	{
		if(publisher != null)
		{
			publisher = ImageSpaceObject.getImageSpace().getPublisher(this);
		}
		return publisher;
	}
	
	/**
	 * Returns the name of the application
	 * @return the name of the application
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the version of this application
	 * @return the version of this application
	 */
	public String getVersion()
	{
		return version;
	}
	
	/**
	 * Returns a short description of this application
	 * @return a short description of this application
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns the category name 
	 * @return
	 */
	public String getCategory()
	{
		return this.categoryName;
	}
	
	/**
	 * Returns a general description of each parameter needed for application execution
	 * @return a general description of each parameter needed for application execution
	 */
	public synchronized Set<Parameter> getParameters()
	{
		if(parameters == null)
		{
			parameters = ImageSpaceObject.getImageSpace().getParameters(this);
		}
		
		return parameters;
	}
	
	/**
	 * execute the application on the specified records using the given parameters
	 * @param records the list of records to process
	 * @param parameters the parameter for the application execution
	 * @return the handle to the application-process for status etc
	 */
	public Job execute(String projectName, List<Record> records, Map<String, Object> parameters, Priority priority)
	{
		return ImageSpaceObject.getImageSpace().executeApplication(this, parameters, priority, projectName, getRecordGUIDs(records));
	}
	
	/**
	 * execute the application on the specified records using the given parameters
	 * @param records the list of records to process
	 * @param parameters the parameter for the application execution
	 * @param priority the task priority
	 * @param scheduleTime the scheduled execution time
	 * @return the handle to the application-process for status etc
	 */
	public Job schedule(String projectName, List<Record> records, Map<String, Object> parameters, Priority priority, long scheduleTime)
	{
		return ImageSpaceObject.getImageSpace().scheduleApplication(this, parameters, priority, projectName, scheduleTime, getRecordGUIDs(records));
	}
	
	/**
	 * Returns the list of available applications registered within the system
	 * @param categoryName name of the category or null
	 * @return the list of available applications registered within the system
	 */
	public static List<Application> listApplications(String categoryName)
	{
		return ImageSpaceObject.getImageSpace().listApplications(null, categoryName);
	}
	
	private long[] getRecordGUIDs(List<Record> records)
	{
		List<Long> guids = new ArrayList<Long>();
		for(Record r : records)
			guids.add( r.getGUID() );
		
		return Util.toLongArray(guids);
	}
}
