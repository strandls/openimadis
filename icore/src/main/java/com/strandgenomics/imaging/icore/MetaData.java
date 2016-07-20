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
 * MetaData.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.icore;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * 
 * metadata represented as Name-Value pair
 * @author arunabha
 */
public class MetaData implements Storable {

	private static final long serialVersionUID = 465002750937072912L;
	
	/**
     * Formatter to use for date
     */
    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
    
	/**
	 * name of the meta data
	 */
	protected String name;
	/**
	 * the user annotations value
	 */
	protected Object value;
	/**
	 * type of this annotation
	 */
	protected AnnotationType type;
	
	public MetaData(String name, Long value)
	{
		this.name = name;
		this.value = value;
		this.type = AnnotationType.Integer;
	}
	
	public MetaData(String name, String value) 
	{
		this.name = name;
		this.value = value;
		this.type = AnnotationType.Text;
	}
	
	public MetaData(String name, Double value) 
	{
		this.name = name;
		this.value = value;
		this.type = AnnotationType.Real;
	}
	
	public MetaData(String name, Timestamp value) 
	{
		this.name = name;
		this.value = value;
		this.type = AnnotationType.Time;
	}
	
	public static MetaData createInstance(String name, Object value)
	{
        if(value == null)
            return null;

        if(value instanceof String)
        {
            return new MetaData(name, (String)value);
        }
        else if(value instanceof Double || value instanceof Float || value instanceof BigDecimal)
        {
        	return new MetaData(name, Util.getDouble(value));      
        }
        else if(value instanceof Long || value instanceof Integer || 
        		value instanceof BigInteger || value instanceof Short || value instanceof Byte){
                    
        	return new MetaData(name, Util.getLong(value));      
        }
        else if(value instanceof Calendar)
        {
        	return new MetaData(name, new Timestamp(((Calendar) value).getTimeInMillis()));      
        }
        else if(value instanceof Date)
        {
        	return new MetaData(name, new Timestamp(((Date)value).getTime()));      
        }
        else if(value instanceof java.sql.Date)
        {
        	return new MetaData(name, new Timestamp(((java.sql.Date)value).getTime()));      
        }
        else if(value instanceof Timestamp)
        {
        	return new MetaData(name, (Timestamp)value);      
        }
        else
        {
        	throw new IllegalArgumentException("unknown annotation value type");
        }
	}
	
	public AnnotationType getType()
	{
		return this.type;
	}

	/**
	 * @return the name
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public Object getValue()
	{
		return value;
	}

	/**
	 * @return get string value of the metadata
	 */
	public String stringValue() {
	    switch (type) {
	    case Integer:
	    case Real:
	    case Text:
	        return value+"";
	    case Time:
	        return dateFormat.format(value);
	    }
	    return super.toString();
	}

    @Override
    public String toString() {
        return "[" + this.name + "," + this.type + "," + this.value + "]";
    }

	@Override
	public void dispose() 
	{}
}
