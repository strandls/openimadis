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
 * Application.java
 *
 * AVADIS Image Management System
 * Core Compute Engine Components
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
package com.strandgenomics.imaging.icore.app;

import java.io.Serializable;


/**
 * A registered application
 * @author arunabha
 *
 */
public class Application implements Serializable {

	private static final long serialVersionUID = -3376659566248360901L;
	/**
	 * name of the application
	 */
	public final String name;
	/**
	 * version of this application
	 */
	public final String version; 

	/**
	 * Defines an application by its name and version
	 * @param name name of the application 
	 * @param version version of the application
	 */
    public Application(String name, String version)
    {
    	if(name == null || version == null)
    		throw new NullPointerException("application name and/or version cannot be null");
    	
    	this.name         = name;
    	this.version      = version;
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
	 * Returns the version of the application
	 * @return the version of the application
	 */
	public String getVersion()
	{
		return version;
	}
    
    public int hashCode()
    {
        return name.hashCode();
    }
    
    public boolean equals(Object obj)
    {
        boolean status = false;
        
        if(obj != null && obj instanceof Application)
        {
            Application that = (Application)obj;
            if(this == that) return true;
            status = (this.name.equals(that.name) && this.version.equals(that.version));
        }
        
        return status;
    }
}
