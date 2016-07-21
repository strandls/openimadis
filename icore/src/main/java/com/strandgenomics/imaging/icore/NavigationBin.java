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
