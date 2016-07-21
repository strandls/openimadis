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

public class RecordSite {
	
	/**
	 * name of the site
	 */
	protected String name;
	/**
	 * a site is mapped to the series number of a record
	 */
	protected int seriesNo;

	public RecordSite()
	{}
	
    /**
     * A record may be part of a series (of records). 
     * This method returns the series number for this record if any. default is zero
     * @return the series number for this record if any zero otherwise
     */
    public int getSeriesNo()
    {
    	return seriesNo;
    }
    
    public void setSeriesNo(int value)
    {
    	seriesNo = value;
    }

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
}
