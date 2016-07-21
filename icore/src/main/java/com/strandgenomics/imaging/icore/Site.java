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

import java.io.Serializable;

/**
 * Represents a site of a record. By default, the records are single site records till they are merged
 * as a multi-site record
 * @author nimisha
 */
public class Site implements Serializable {

	private static final long serialVersionUID = 5116314082882541146L;
	
	/**
	 * name of the site
	 */
	protected String name;
	/**
	 * a site is mapped to the series number of a record
	 */
	protected final int seriesNo;

	/**
	 * creates a default site 
	 * @param name name of this site
	 */
	public Site(int seriesNo, String name)
	{
		this.seriesNo = seriesNo;
		this.name = name;
	}
	
    /**
     * A record may be part of a series (of records). 
     * This method returns the series number for this record if any. default is zero
     * @return the series number for this record if any zero otherwise
     */
    public int getSeriesNo()
    {
    	return seriesNo;
    }

    /**
     * series name
     * @return name of the series
     */
	public String getName() {
		return name;
	}

	
	public void setName(String name) 
	{
		this.name = name;
	}
	
    @Override
    public int hashCode()
    {
    	return seriesNo;
    }
    
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof Site)
    	{
    		Site that = (Site) obj;
    		if(this == that) return true;
    		
    		return this.seriesNo == that.seriesNo;
    	}
    	return false;
    }
}
