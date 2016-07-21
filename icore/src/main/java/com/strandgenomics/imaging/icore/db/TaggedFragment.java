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

/**
 * A special case of a constant fragment where the constant name rather acts as a tag or parameter that
 * can be replaced dynamically with custom values. 
 * Additionally, if the tag value is not set this fragment is ignored 
 * 
 * Parts of a SQL query acting as a parameter itself, can be replaced by another
 * fragment or a string value to dynamically generate a ConstantFragment and hence a SQL.
 * 
 * @author arunabha
 */
public class TaggedFragment extends ConstantFragment implements Cloneable {

	/**
	 * the actual value of this tag that is used in query generation instead of the name
	 */
    protected Object tagValue = null;

    /**
     * Creates a TaggedFragment instance with the specified name
     * @param tagName name of the tag
     */
    public TaggedFragment(String tagName)
    {
    	super(tagName);
    }

    //make any empty copy of this ParameterFragment
    @Override
    public Object clone()
    {
        return new TaggedFragment(name);
    }
   

    @Override
    public void dispose()
    {
        tagValue = null;
    }

    @Override
    public String toQueryString()
    {
    	if(tagValue == null)
    	{
            return ""; //ignore this fragment
    	}
    	else if(tagValue instanceof QueryFragment)
        {
            return ((QueryFragment)tagValue).toQueryString();
        }
        else 
        {
           return tagValue.toString();
        }
    }

    /**
     * if the parameter value is another QueryFragment, pass on the call to that guy,
     * otherwise nothing to set for this fragment & return the parameterIndex value as is
     */
    @Override
    public int setParameterIndexAndValue(PreparedStatement pstmt, int parameterIndex) throws SQLException 
    {
        if(tagValue != null && tagValue instanceof QueryFragment)
        {
            return ((QueryFragment)tagValue).setParameterIndexAndValue(pstmt, parameterIndex);
        }
        else 
        {
           return parameterIndex;
        }

    }

    /**
     * Returns the name of the tag
     * @return the name of the tag
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * set the value of this tag
     */
    public void setValue(String value)
    {
        tagValue = value;
    }
    
    /**
     * support for nested and/or parameterized queries
     */
    public void setValue(QueryFragment value)
    {
    	tagValue = value;
    }

    @Override
    public String toString()
    {
        return ("%"+name+"%");
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean status = false;
        if(obj != null && obj instanceof TaggedFragment)
        {
            TaggedFragment that = (TaggedFragment) obj;
            if(this == that) return true;
            
            if( that.name.equals(this.name))
            {
            	status = false;
            	
            	if(this.tagValue == null && that.tagValue == null)
            	{
            		status = true;
            	}
            	
            	if(this.tagValue != null && that.tagValue != null && 
                	this.tagValue.equals(that.tagValue))
	            {
	                status = true;
	            }
            }
        }
        return status;
    }
    
	@Override
	public void setParameter(String name, String value) 
	{
		if(this.name.equals(name))
        {
            setValue(value);
        }
	}

	@Override
	public void setParameter(String name, QueryFragment value) 
	{
		if(this.name.equals(name))
        {
            setValue(value);
        }
	}
	
	public boolean isValueSet()
	{
		return tagValue != null;
	}
}