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

package com.strandgenomics.imaging.icore.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of two or more fragments
 * @author arunabha
 *
 */
public abstract class ComplexFragments extends QueryFragment {
	
    /**
     * List of fragments of this query
     */
    protected List<QueryFragment> queryFragments = null;
	
    /**
     * Create an instance of a complex fragment
     * @param fragmentSeq
     */
    public ComplexFragments(List<QueryFragment> fragmentSeq)
    {
        queryFragments = new ArrayList<QueryFragment>();
        queryFragments.addAll(fragmentSeq);
    }

	@Override
	public void dispose() 
	{
		queryFragments = null;
	}

	@Override
	public String toQueryString() 
	{
        StringBuffer buffer = new StringBuffer(128);
        for(QueryFragment fragment : queryFragments)
        {
            buffer.append(fragment.toQueryString());
        }

        return buffer.toString();
	}
	
	@Override
	public String toString() 
	{
        StringBuffer buffer = new StringBuffer(128);
        for(QueryFragment fragment : queryFragments)
        {
            buffer.append(fragment.toString());
        }

        return buffer.toString();
	}
	
    @Override
    public boolean equals(Object obj)
    {
    	if(obj instanceof ComplexFragments)
    	{
    		ComplexFragments that = (ComplexFragments) obj;
    		if(this == that) return true;
    		
    		return this.queryFragments.equals(that.queryFragments);
    	}
    	return false;
    }

    @Override
    public int setParameterIndexAndValue(PreparedStatement pstmt, int paramIndex) throws SQLException 
    {
    	for(QueryFragment fragment : queryFragments)
    	{
            //all this fragment to set relevant parameter values
            //set the parameter at the specified index if needed
            //and return the next available parameter index
            paramIndex = fragment.setParameterIndexAndValue(pstmt, paramIndex);
        }

        return paramIndex;
    }

	@Override
	public void setParameter(String name, String value)
	{
		for(QueryFragment fragment : queryFragments)
	    {
	        //set the value to the matching variable fragment
			fragment.setParameter(name, value);
	    }
	}

	@Override
	public void setParameter(String name, QueryFragment value){
	
		for(QueryFragment fragment : queryFragments)
	    {
	        //set the vale to the matching variable fragment
			fragment.setParameter(name, value);
	    }
	}

	@Override
	public void setValue(String name, Object value, int sqlType)
	{
	    for(QueryFragment fragment : queryFragments)
	    {
	    	fragment.setValue(name, value, sqlType);
	    }
	}

	@Override
	public void setValue(String name, Object value, int sqlType, Operator operator)
	{
	    for(QueryFragment fragment : queryFragments)
	    {
	    	fragment.setValue(name, value, sqlType, operator);
	    }
	}
	
	@Override
	public void setValue(String name, Object value, int sqlType, Operator operator, boolean forceNullUse)
	{
	    for(QueryFragment fragment : queryFragments)
	    {
	    	fragment.setValue(name, value, sqlType, operator, forceNullUse);
	    }
	}
}
