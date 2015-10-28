/*
 * SearchField.java
 *
 * AVADIS Image Management System
 * Core Engine
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


/**
 * represents a field that is searchable
 */
public class SearchField implements Storable {
	
	private static final long serialVersionUID = 3703997754758782349L;
	/**
	 * name of the navigable field
	 */
	public final String fieldName;
	/**
	 * type of the field
	 */
	public final AnnotationType fieldType;
	
	/**
	 * Create a record field with a name and type 
	 * @param fieldName name of the field
	 * @param fieldType type of the field
	 */
	public SearchField(String fieldName, AnnotationType fieldType)
	{
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
	
	/**
	 * name of the search field
	 * @return name of the search field
	 */
	public String getField()
	{
		return this.fieldName;
	}
	
	/**
	 * type of the search field
	 * @return type of the search field
	 */
	public AnnotationType getType()
	{
		return this.fieldType;
	}
	
	@Override
	public String toString()
	{
		return this.fieldName;
	}
	
	@Override
	public int hashCode()
	{
		return this.fieldName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof SearchField)
		{
			SearchField that = (SearchField)obj;
			if(this == that) return true;
			return (this.fieldName.equals(that.fieldName) && this.fieldType.equals(that.fieldType));
		}
		return false;
	}

	@Override
	public void dispose() 
	{
	}
	
	/**
	 * Returns true iff the field represents a text value
	 * @return true iff the field represents a text value
	 */
	public boolean isText()
	{
		return fieldType.equals(AnnotationType.Text);
	}
	
	/**
	 * Returns true iff the field represents a real value
	 * @return true iff the field represents a real value
	 */
	public boolean isReal()
	{
		return fieldType.equals(AnnotationType.Real);
	}
	
	/**
	 * Returns true iff the field represents a integral value
	 * @return true iff the field represents a integral value
	 */
	public boolean isIntegral()
	{
		return fieldType.equals(AnnotationType.Integer);
	}
	
	/**
	 * Returns true iff the field represents a date value
	 * @return true iff the field represents a date value
	 */
	public boolean isDateTime()
	{
		return fieldType.equals(AnnotationType.Time);
	}
}
