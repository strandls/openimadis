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

package com.strandgenomics.imaging.icore.app;

import java.io.Serializable;

/**
 * Description of a parameter of an application
 * @author arunabha
 *
 */
public class Parameter implements Serializable {

	private static final long serialVersionUID = -4442491119645663893L;
	
	/**
	 * name of the parameter
	 */
	public final String name;
	/**
	 * type of the parameter, one of integer, float, boolean and string only
	 */
	public final ParameterType type;
	/**
	 * constraints on this parameter, can be null, meaning unbounded
	 */
	public final ParameterConstraints constraints;
	/**
	 * default value associated with this parameter, null is not a valid value
	 */
	public final Object defaultValue;
	/**
	 * description of the parameter
	 */
	public final String description;
	
	/**
	 * Creates a Parameter having the specified name, type and constraints
	 * @param name name of the parameter
	 * @param type type of the parameter, one of integer, float boolean and string only
	 * @param constraints constraints on this parameter
	 * @param defaultValue default value of the parameter
	 * @param description parameter description
	 */
	public Parameter(String name, ParameterType type, ParameterConstraints constraints, Object defaultValue, String description)
	{
		if(name == null || type == null)
			throw new IllegalArgumentException("null parameter name or type");
		
		if(constraints != null && constraints.getType() != type)
			throw new IllegalArgumentException("parameter and its constraint type do not match");
		
		this.name = name;
		this.type = type;
		this.constraints = constraints;
		
		if(!validate(defaultValue))
			throw new IllegalArgumentException("illegal default value="+defaultValue+" for parameter "+name);
		
		this.defaultValue = defaultValue;
		
		this.description = description;
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
		if(obj != null && obj instanceof Parameter)
		{
			Parameter that = (Parameter) obj;
			if(this == that) 
				return true;
			else 
				return this.name.equals(that.name);
		}
		return false;
	}
	
	/**
	 * check if the specified value is valid for this parameter
	 * @param value the value to verify
	 * @return true if valid, false otherwise
	 */
	public boolean validate(Object value)
	{
		if(isValidType(value))
		{
			return constraints == null ? true : constraints.validate(value); 
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns true if the specified value is consistent with this parameter's type
	 * @param value the value to verify
	 * @return  true if valid, false otherwise
	 */
	public boolean isValidType(Object value)
	{
		boolean validType = false;
		switch(type)
		{
			case INTEGER:
				validType = (value instanceof Long || value instanceof Integer || value instanceof Short);
				break;
			case DECIMAL:
				validType = (value instanceof Float || value instanceof Double);
				break;
			case STRING:
				validType = (value instanceof String);
				break;
			case BOOLEAN:
				validType = (value instanceof Boolean);
				break;
		}
		return validType;
	}
}
