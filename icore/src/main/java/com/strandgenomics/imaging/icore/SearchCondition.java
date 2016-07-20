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
 * SearchCondition.java
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

import java.sql.Timestamp;

import com.strandgenomics.imaging.icore.util.Util;


/**
 * A column (field) with its lower and upper limits defines a search condition 
 * @author arunabha
 *
 */
public class SearchCondition extends SearchField {

	private static final long serialVersionUID = -1381492585289546726L;
	/**
	 * lower limit of the range, inclusive
	 */
	private Object lowerLimit = null;
	/**
	 * upper limit of the range, exclusive
	 */
	private Object upperLimit = null;
	
	/**
	 * Creates a filter condition
	 * @param fieldName name of the field
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public SearchCondition(String fieldName, Long lowerLimit, Long upperLimit) 
	{
		super(fieldName, AnnotationType.Integer);
		setRange(lowerLimit, upperLimit);
	}
	
	/**
	 * Creates a filter condition
	 * @param fieldName name of the field
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public SearchCondition(String fieldName, Double lowerLimit, Double upperLimit) 
	{
		super(fieldName, AnnotationType.Real);
		setRange(lowerLimit, upperLimit);
	}
	
	/**
	 * Creates a filter condition
	 * @param fieldName name of the field
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public SearchCondition(String fieldName, String lowerLimit, String upperLimit) 
	{
		super(fieldName, AnnotationType.Text);
		setRange(lowerLimit, upperLimit);
	}
	
	/**
	 * Creates a filter condition
	 * @param fieldName name of the field
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public SearchCondition(String fieldName, Timestamp lowerLimit, Timestamp upperLimit) 
	{
		super(fieldName, AnnotationType.Time);
		setRange(lowerLimit, upperLimit);
	}
	
	/**
	 * Creates an empty filter condition
	 * @param fieldName name of the field
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public SearchCondition(String fieldName, AnnotationType fieldType) 
	{
		super(fieldName, AnnotationType.Time);
		this.lowerLimit = null;
		this.upperLimit = null;
	}
	
	/**
	 * Returns true iff the lower and upper limits are identical
	 * @return true iff the lower and upper limits are identical, false otherwise
	 */
	public boolean isIdentity()
	{
		return Util.safeEquals(lowerLimit, upperLimit);
	}
	
	/**
	 * lower limit on search condition
	 * @return lower limit on search condition
	 */
	public Object getLowerLimit()
	{
		return lowerLimit;
	}
	
	/**
	 * upper limit on search condition
	 * @return upper limit on search condition
	 */
	public Object getUpperLimit()
	{
		return upperLimit;
	}
	
	@Override
	public int hashCode()
	{   
		return fieldName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof SearchCondition)
		{
			SearchCondition that = (SearchCondition) obj;
			if(this == that) return true;
			return (this.fieldName.equals(that.fieldName) && this.fieldType.equals(that.fieldType)
					&& Util.safeEquals(this.lowerLimit, that.lowerLimit) && 
					Util.safeEquals(this.upperLimit, that.upperLimit));
		}
		return false;
	}
	
	@Override
	public String toString() 
	{
	    return fieldName + " (" + lowerLimit + "-" + upperLimit + ")";
	}
	
	private <T extends Comparable> void setRange(T lowerLimit, T upperLimit)
	{
		if(lowerLimit != null && upperLimit != null && lowerLimit.compareTo(upperLimit) > 0)
			throw new IllegalArgumentException("lower limit is greater than upper limit");
		
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}
}
