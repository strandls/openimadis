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

package com.strandgenomics.imaging.icore;

import java.util.Date;

public interface IProject {
	
	/**
	 * Disk quota in GBs
	 * @return Disk quota in GBs
	 */
	public double getDiskQuota();
	
	/**
	 * Disk space consumption in GBs
	 * @return Disk space consumption in GBs
	 */
	public double getSpaceUsage();
	
	/**
	 * Returns the name of the project
	 * Project names are unique across the Enterprise IMG Server
	 * @return the name of the project
	 */
	public String getName();
	
	/**
	 * Notes or comments associated with the project 
	 * @return Notes or comments associated with the project 
	 */
	public String getNotes();
	
	/**
	 * Returns the creation time
	 * @return the creation time
	 */
	public Date getCreationDate();
	    
    /**
     * Returns the list of available records for the login user to read 
     * @return the list of available records for the login user to read 
     */
    public int getRecordCount();
}
