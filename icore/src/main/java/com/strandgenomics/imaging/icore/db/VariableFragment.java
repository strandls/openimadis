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

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Set;
import java.util.logging.Level;

/**
 * A fragment with a SQL query specifying and handling a variable (prepared statement)
 * @author arunabha
 *
 */
public class VariableFragment extends QueryFragment implements Cloneable {

	/**
	 * symbol to use for prepared statement
	 */
    public static final String PARAMETER_SYMBOL = " ? ";
    /**
     * value associated with the variable, can be null as well.
     */
    protected Object value = null;
    /**
     * parameter values are one of values in class VariableFragment.Operator or empty string
     * the default value is empty string new String();
     */
    protected String operator = "";
    /**
     * setNull is valid or IS NULL is valid, default is false
     * "true" indicates that NULL values are valid with setNull(i, value)
     * the default is false, it means if the value is "null",
     * it will be written as Value IS NULL instead of Value=NULL
     * where Value is the name of the column whose value is specified by value
     */
    protected boolean setNullIsValid = false;
    /**
     * in case the value is not set i.e., is null, this variable override isValueSet call
     * Generally forceNullUse value is ignored except when it is used inside an OptionalFragment
     * where VariableFragment value can be null (generally meaning that the optional fragment is to be ignored)
     * but if forceNullUse is set to true, isValueSet methods returns true even when "value" is set to null.
     */
    protected boolean forceNullUse = false;
    /** 
     * variable name 
     */
    protected String name = null;
    /** 
     * java.sql.Types associated with the specified value 
     */
    protected int sqlType = Types.JAVA_OBJECT;
    
    /**
     * Create a fragment capturing a variable with values
     * @param name name of the variable
     * @param setNullIsValid indicates if setNull is valid (true) or IS NULL is valid (false), default is false
     * @param operator the operator to use, 
     */
    public VariableFragment(String name, boolean setNullIsValid, String operator)
    {
        if(name == null) {
            throw new NullPointerException("null name is illegal");
        }

        this.name  = name;
        this.value = null; //default value
        if(operator != null)
        {
            this.operator = operator;
        }
        this.setNullIsValid = setNullIsValid;
    }
    
    /**
     * Create a fragment capturing a variable with values
     * @param name name of the variable
     * @param operator the operator to use, 
     */
    public VariableFragment(String name, String operator)
    {
        this(name, false, operator);
    }
    
    protected VariableFragment()
    {}

    //make any empty copy of this VariableFragment
    public Object clone()
    {
    	VariableFragment vf = new VariableFragment(name, setNullIsValid, operator);
    	vf.forceNullUse = this.forceNullUse;
    	
    	return vf;
    }

    @Override
    public void dispose()
    {
        value     = null;
        name      = null;
        operator  = null;
    }

    /**
     * set the value of this parameter with its associated the java.sql.Types
     */
    public void setValue(Object value, int sqlType)
    {
        this.value = value;
        this.sqlType = sqlType;
    }

    public void setOperator(String paramValue)
    {
        operator = paramValue;
    }

    public String getOperator()
    {
        return operator;
    }

    public Object getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public String toQueryString()
    {
        if(value == null)
        {
            if(setNullIsValid)
            {
                return operator + PARAMETER_SYMBOL;
            }
            else 
            {
                return " IS NULL ";
            }
        }
        else { //value is not null
            
            if(value instanceof Set<?>)
            {
                return generateListString( (Set<?>) value );
            }
            else 
            {
                // ?
                // = ?
                // <= ?
                // >= ?
                // < ?
                // > ?
                return operator + PARAMETER_SYMBOL;
            }
        }
    }
    
    /**
     * Set the parameter index to the PreparedStatement and Returns the next available parameter index
     */
    public int setParameterIndexAndValue(PreparedStatement pstmt, int parameterIndex) throws SQLException 
    {
        int nextParameterIndex = parameterIndex;

        if(value == null)
        {
            if(setNullIsValid)
            {
                logger.logp(Level.FINEST, "VariableFragment", "setParameterValue", "setting null at "+parameterIndex +" of type "+sqlType);
                pstmt.setNull(parameterIndex, sqlType);
                nextParameterIndex = parameterIndex + 1;
            }
            else 
            {
                //not needed here
                logger.logp(Level.FINEST, "VariableFragment", "setParameterValue", "ignoring null value");
                nextParameterIndex = parameterIndex;
            }
        }
        else 
        {
            if(value instanceof Set<?>)
            {
                nextParameterIndex = setListParameterIndex(pstmt, parameterIndex, (Set<?>)value);
            }
            else 
            {
                nextParameterIndex = setNonNullParameterIndex(pstmt, parameterIndex, value);
            }
        }

        return nextParameterIndex;
    }
    
    private int setNonNullParameterIndex(PreparedStatement pstmt, int parameterIndex, Object value) 
    		throws SQLException 
    {
        logger.logp(Level.FINEST, "VariableFragment", "setNonNullParameterValue", "setting "+ value +" at "+parameterIndex +" of type "+sqlType);
        
        if(sqlType == Types.BLOB)
        {
            if(value instanceof Blob)
            {
                pstmt.setBlob(parameterIndex, (Blob)value);
            }
            else if(value instanceof byte[])
            {
                byte[] data = (byte[])value;
                pstmt.setBinaryStream(parameterIndex, new ByteArrayInputStream(data), data.length); 
            }
            else 
            {
                throw new SQLException("unknown value for BLOB types "+value.getClass().getName());
            }
        }
        else 
        {
            pstmt.setObject(parameterIndex, value, sqlType);
        }

        return (parameterIndex + 1);
    }
    
    private int setListParameterIndex(PreparedStatement pstmt, int parameterIndex, Set<?> valueList) 
    		throws SQLException 
    {
        int noOfValues = valueList.size();
         
        logger.logp(Level.FINEST, "VariableFragment", "setListParameterValue", "setting "+ noOfValues +" values from "+parameterIndex +" of type "+sqlType);
        for(Object value : valueList)
        {
            parameterIndex = setNonNullParameterIndex(pstmt, parameterIndex, value);
        }
        
        return parameterIndex;
    }

    private String generateListString(Set<?> valueList)
    {
        int noOfValues = valueList.size();
        
        if(noOfValues == 1)
        {
            return operator + PARAMETER_SYMBOL;
        }
        else 
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(" IN (");
            
            for(int i = 0;i < noOfValues; i++)
            {
                if(i > 0)
                {
                    buffer.append(", ");
                }
                buffer.append(PARAMETER_SYMBOL);
            }
            
            buffer.append(") ");
            return buffer.toString();
        }
    }

    public String toString()
    {
        return ("{"+name +","+ setNullIsValid +","+ operator+"}");
    }

    public int hashCode()
    {
        return name.hashCode();
    }

    public boolean equals(Object obj)
    {
        boolean status = false;
        if(obj != null && obj instanceof VariableFragment)
        {
            VariableFragment vf = (VariableFragment) obj;
            if(vf == this) return true;
            
            if(vf.setNullIsValid == this.setNullIsValid &&
                vf.operator.equals(this.operator) &&
                vf.name.equals(this.name))
            {
                status = true;
            }
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
        if(this.name.equals(name))
        {
            setValue(value, sqlType);
        }
	}

	@Override
	public void setValue(String name, Object value, int sqlType, Operator operator) 
	{
        if(this.name.equals(name))
        {
            setValue(value, sqlType);
            setOperator(operator.toString());
        }
	}
	
	@Override
	public void setValue(String name, Object value, int sqlType, Operator operator, boolean forceNullUse) 
	{
        if(this.name.equals(name))
        {
            setValue(value, sqlType);
            setOperator(operator.toString());
            this.forceNullUse = forceNullUse;
        }
	}
	
	public boolean isValueSet()
	{
		return value != null;
	}
	
	public boolean isForceNullUse()
	{
		return forceNullUse;
	}
}