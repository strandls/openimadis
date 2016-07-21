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
import java.util.logging.Level;

/**
 * Represents a dynamic or optional fragment of a SQL query.
 * this fragment will consist of a series of constant/tagged fragment and at LEAST one Variable fragment.
 * this fragment will take part in sql query building iff the variable part(s) has non null value(s)
 * 
 * @author arunabha
 */
public class OptionalFragment extends ComplexFragments {
	
	/**
	 * Creates an instance of an OptionalFragment with the specified list of QueryFragment
	 * @param fragmentSeq list of QueryFragment with at least one VariableFragment
	 */
    public OptionalFragment(List<QueryFragment> fragmentSeq)
    {
    	super(fragmentSeq);
    }

    @Override
    public Object clone()
    {
    	List<QueryFragment> temp = new ArrayList<QueryFragment>();
    	for(QueryFragment q : queryFragments)
    	{
    		temp.add( (QueryFragment) q.clone());
    	}
        return new OptionalFragment(temp);
    }
    
    public String toQueryString()
    {
        if(isFragmentsNotSet())
        {
            logger.logp(Level.FINEST, "OptionalFragment", "toQueryString", "ignoring fragment "+this);
            return ""; //ignore this fragment
        }
        else 
        {
        	return super.toQueryString();
        }
    }

	/**
     * Set the parameter to the PreparedStatement and Returns the next available parameter index
     */
    public int setParameterIndexAndValue(PreparedStatement pstmt, int parameterIndex) throws SQLException 
    {
        if(isFragmentsNotSet())
        {
            logger.logp(Level.FINEST, "OptionalFragment", "setParameterValue", "ignoring fragment "+this);
            return parameterIndex;
        }
        else
        {
            return super.setParameterIndexAndValue(pstmt, parameterIndex);
        }
    }
    
    private boolean isFragmentsNotSet()
    {
		for(QueryFragment fragment : queryFragments)
		{
			if(fragment instanceof VariableFragment)
			{
				VariableFragment vf = (VariableFragment) fragment;
				
				if(!vf.isValueSet())
				{
					if(!vf.isForceNullUse())
						return true;
				}
			}
			
			if(fragment instanceof TaggedFragment)
			{
				if(!((TaggedFragment)fragment).isValueSet())
					return true;
			}
		}
		return false;
	}
}