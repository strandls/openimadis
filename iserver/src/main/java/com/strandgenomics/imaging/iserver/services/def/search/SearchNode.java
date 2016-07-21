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
