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

/**
 * Job.java
 *
 * Project imaging
 * Core Compute Component
 *
 * Copyright 2009-2012 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.app;

import java.io.Serializable;
import java.util.Map;

/**
 * Encapsulate all the relevant information to execute an application
 * @author arunabha
 *
 */
public class Work implements Serializable{
	
	/**
	 * job ID
	 */
	public final long ID;
	/**
	 * The authorization code granted by the user to run this application on his/her behalf
	 */
	private String authCode;
	/**
	 * the application to execute
	 */
	public final Application application;
	/**
	 * The list of parameters needed by the application
	 */
	public final Map<String, String> parameters;
	/**
	 * the list of record guids as input to this application
	 */
	public final long[] inputGUIDs;
	/**
	 * internal id of the auth code; used to dereference authcode to access token 
	 */
	public final long authCodeInternalId;
	
	/**
	 * Creates a JOb to be executed by the compute servers
	 * @param authCode the authorization code granted by the user
	 * @param app the application to execute
	 * @param parameters the parameters needed to execute
	 * @param inputs the input records
	 */
	public Work(String authCode, long authCodeId, Application app, Map<String, String> parameters, long ... inputs)
	{
		if(authCode == null || app == null || inputs == null)
			throw new NullPointerException("null authorization code or application or inputs");
		
		this.authCode = authCode;
		this.application = app;
		this.inputGUIDs = inputs;
		this.parameters = parameters;
		this.authCodeInternalId = authCodeId;
		
		this.ID = generateID();
	}
	
	/**
	 * Creates a JOb to be executed by the compute servers
	 * @param workId the task id
	 * @param authCode the authorization code granted by the user
	 * @param app the application to execute
	 * @param parameters the parameters needed to execute
	 * @param inputs the input records
	 */
	public Work(long workId, String authCode, long authCodeId, Application app, Map<String, String> parameters, long ... inputs)
	{
		if(authCode == null || app == null || inputs == null)
			throw new NullPointerException("null authorization code or application or inputs");
		
		this.authCode = authCode;
		this.application = app;
		this.inputGUIDs = inputs;
		this.parameters = parameters;
		this.ID = workId;
		this.authCodeInternalId = authCodeId;
	}
	
	
	public String getAuthCode()
	{
		return this.authCode;
	}
	
	/**
	 * ID generator seed
	 */
	private static long ID_SEED = System.nanoTime();
	
	/**
	 * Generates an ID of a Task
	 * @return
	 */
    private static final synchronized long generateID()
    {
    	return ID_SEED++;
    }
}
