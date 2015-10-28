/*
 * NavigationBin.java
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
import java.util.HashSet;
import java.util.Set;


/**
 * Represents a new FilterCondition on top of an existing list of FilterConditions 
 * @author arunabha
 */
public class NavigationBin extends SearchCondition {
	
	private static final long serialVersionUID = 6290841382726390110L;
	/**
	 * list of pre-existing filter conditions
	 */
	protected Set<SearchCondition> preExistingConditions = null;
	/**
	 * number of records satisfying the filters
	 */
	protected int noOfRecords = 0;
	
	/**
	 * Creates a bin with the specified filters and number of records satisfying the filters
	 * @param filters list of pre-existing filter conditions, can be null
	 * @param fieldName name of the field to search on
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public NavigationBin(Set<SearchCondition> filters, String fieldName, long lowerLimit, long upperLimit)
	{
		super(fieldName, lowerLimit, upperLimit);
		this.preExistingConditions = filters;
	}
	
	/**
	 * Creates a bin with the specified filters and number of records satisfying the filters
	 * @param filters list of pre-existing filter conditions, can be null
	 * @param fieldName name of the field to search on
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public NavigationBin(Set<SearchCondition> filters, String fieldName, double lowerLimit, double upperLimit)
	{
		super(fieldName, lowerLimit, upperLimit);
		this.preExistingConditions = filters;
	}
	
	/**
	 * Creates a bin with the specified filters and number of records satisfying the filters
	 * @param filters list of pre-existing filter conditions, can be null
	 * @param fieldName name of the field to search on
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public NavigationBin(Set<SearchCondition> filters, String fieldName, String lowerLimit, String upperLimit)
	{
		super(fieldName, lowerLimit, upperLimit);
		this.preExistingConditions = filters;
	}
	
	/**
	 * Creates a bin with the specified filters and number of records satisfying the filters
	 * @param filters list of pre-existing filter conditions, can be null
	 * @param fieldName name of the field to search on
	 * @param lowerLimit lower limit of the value
	 * @param upperLimit upper limit of the value
	 */
	public NavigationBin(Set<SearchCondition> filters, String fieldName, Timestamp lowerLimit, Timestamp upperLimit)
	{
		super(fieldName, lowerLimit, upperLimit);
		this.preExistingConditions = filters;
	}
	
	/**
	 * Creates an empty bin
	 * @param filters list of pre-existing filter conditions, can be null
	 * @param field the search field
	 */
	public NavigationBin(Set<SearchCondition> filters, SearchField field)
	{
		super(field.fieldName, field.fieldType);
		this.preExistingConditions = filters;
	}
	
	public Set<SearchCondition> getPreExitingFilters()
	{
		return preExistingConditions;
	}
	
	public void setRecordCount(int count)
	{
		noOfRecords = count;
	}
	
	public int getRecordCount()
	{
		return noOfRecords;
	}
	
	public Set<SearchCondition> toFilter()
	{
		Set<SearchCondition> filters = new HashSet<SearchCondition>();
		if(preExistingConditions != null) filters.addAll(preExistingConditions);
		filters.add( this );
		return filters;
	}
}
