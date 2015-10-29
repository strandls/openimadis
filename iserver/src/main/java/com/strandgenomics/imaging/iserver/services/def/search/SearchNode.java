/*
 * SearchNode.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.search;

/**
 * Represents a new Filter Condition on top of an existing list of SearchCondition 
 * @author arunabha
 */ 
public class SearchNode {
	
	/**
	 * list of pre-existing filter conditions
	 */
	private SearchCondition[] preExistingConditions = null;
	/**
	 * number of records satisfying the filters
	 */
	private int noOfRecords = 0;
	
	public SearchNode()
	{}
	
	/**
	 * Returns the list of filter conditions
	 * @return the list of filter conditions
	 */
	public SearchCondition[] getFilters()
	{
		return preExistingConditions;
	}
	
	public void setFilters(SearchCondition[] value)
	{
		preExistingConditions = value;
	}	
	/**
	 * Sets the number of records satisfying the search conditions
	 * @param count the number of records satisfying the search conditions
	 */
	public void setRecordCount(int count)
	{
		noOfRecords = count;
	}
	
	/**
	 * Returns the number of records satisfying the search conditions
	 * @return the number of records satisfying the search conditions
	 */
	public int getRecordCount()
	{
		return noOfRecords;
	}
}
