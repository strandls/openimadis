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

import com.strandgenomics.imaging.icore.Storable;

/**
 * model representing Legend object. A legend has name and the type 
 * 
 * @author Anup Kulkarni
 */
public class LegendField implements Storable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7488909662076726119L;
	/**
	 * name of the legend field
	 */
	public final String legendName;
	/**
	 * type of the legend. legend can be from ImageMetadata, RecordMetaData and UserMetadata.
	 * this field is used to find appropriate value of legend from appropriate table.
	 */
	public final LegendType type;
	
	public LegendField(String name, LegendType type)
	{
		this.legendName = name;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return "Name="+legendName+",Type="+type;
	}
	
	@Override
    public int hashCode()
    {
    	return this.toString().hashCode();
    }   
    
    @Override
	public boolean equals(Object obj)
	{
    	if(obj != null && obj instanceof LegendField)
		{
    		LegendField that = (LegendField) obj;
			if(this == that) return true;
			
			boolean equals = this.type.equals(that.type) && this.legendName.equals(that.legendName);
			return equals;
		}
		return false;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

}
