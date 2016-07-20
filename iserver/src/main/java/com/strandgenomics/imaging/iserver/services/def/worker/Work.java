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
 * Work.java
 *
 * AVADIS Image Management System
 * Web-service definition
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
package com.strandgenomics.imaging.iserver.services.def.worker;


/**
 * The definition of a work for an worker
 * @author arunabha
 *
 */
public class Work {
	
	/**
	 * ID of this work
	 */
	private long taskID;
	/**
	 * name of the application to run
	 */
	private String appName = null;
	/**
	 * version of the application
	 */
	private String version = null;
	/**
	 * the authorization code granted by the user for this application, the worker will need
	 */
	private String appAuthCode  = null;
	/**
	 * the list of parameters needed by this application
	 */
	private NVPair[] parameters = null;
	/**
	 * list of record GUIDs as input
	 */
	private Long[] inputRecords = null;
	
	public Work()
	{}
	

	/**
	 * @return the taskID
	 */
	public long getTaskID() {
		return taskID;
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}

	/**
	 * @return the appName
	 */
	public String getAppName()
	{
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the appAuthCode
	 */
	public String getAppAuthCode()
	{
		return appAuthCode;
	}

	/**
	 * @param appAuthCode the appAuthCode to set
	 */
	public void setAppAuthCode(String appAuthCode)
	{
		this.appAuthCode = appAuthCode;
	}

	/**
	 * @return the parameters
	 */
	public NVPair[] getParameters()
	{
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(NVPair[] parameters) 
	{
		this.parameters = parameters;
	}

	/**
	 * @return the inputRecords
	 */
	public Long[] getInputRecords() 
	{
		return inputRecords;
	}

	/**
	 * @param inputRecords the inputRecords to set
	 */
	public void setInputRecords(Long[] inputRecords)
	{
		this.inputRecords = inputRecords;
	}
}
