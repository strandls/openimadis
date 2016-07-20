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
 * QueryConfig.java
 *
 * AVADIS Image Management System
 * Core Engine Database Module
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

package com.strandgenomics.imaging.icore.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Parts of an SQL query that do not contain any parameter
 * @author arunabha
 *
 */
public class ConstantFragment extends QueryFragment {

	/**
	 * the name of the fragment
	 */
    public final String name;

    public ConstantFragment(String value)
    {
        if(value == null)
        {
            throw new NullPointerException("null constant is illegal");
        }
        name = value;
    }
    
    @Override
    public Object clone()
    {
    	return new ConstantFragment(name);
    }

    @Override
    public void dispose(){}
   
    @Override
    public String toQueryString()
    {
        return name;
    }

    /**
     * nothing to set for constant fragment, so return the parameterIndex value as is
     */
    @Override
    public int setParameterIndexAndValue(PreparedStatement pstmt, int parameterIndex) throws SQLException 
    {
        return parameterIndex;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean status = false;
        if(obj != null && obj instanceof ConstantFragment)
        {
            status = this.name.equals(((ConstantFragment)obj).name);
        }
        return status;
    }

	@Override
	public void setParameter(String name, String value) 
	{
		;//does nothing
	}

	@Override
	public void setParameter(String name, QueryFragment value) 
	{
		;//does nothing
	}

	@Override
	public void setValue(String name, Object value, int sqlType) 
	{
		;//does nothing
	}

	@Override
	public void setValue(String name, Object value, int sqlType, Operator operator)
	{
		;//does nothing
	}
	
	@Override
	public void setValue(String name, Object value, int sqlType, Operator operator, boolean forceNullUse)
	{
		;//does nothing
	}
}