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
 * Publisher.java
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

package com.strandgenomics.imaging.iserver.services.def.compute;

/**
 * Publisher of applications
 * @author arunabha
 *
 */
public class Publisher {
	
	/**
	 * name of the publisher (unique)
	 */
	protected String name;
	/**
	 * IP address of the server hosting applications
	 */
	protected String hostIP;
	/**
	 * IP port of the server hosting  applications
	 */
	protected int hostPort;
	
	public Publisher()
	{}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * @param hostIP the hostIP to set
	 */
	public void setHostIP(String hostIP)
	{
		this.hostIP = hostIP;
	}

	/**
	 * @param hostPort the hostPort to set
	 */
	public void setHostPort(int hostPort) 
	{
		this.hostPort = hostPort;
	}

	/**
	 * Returns the name of the publisher publishing a application
	 * @return the name of the publisher
	 */
	public String getName()
	{
		return name;
	}
	
	/**
     * Returns the host address (of Enterprise IMG Server) publishing an application
     */
    public String getHost()
    {
    	return hostIP;
    }

    /**
     * return the server port number of Enterprise IMG Server publishing an application
     */
    public int getPort()
    {
    	return hostPort;
    }

}
