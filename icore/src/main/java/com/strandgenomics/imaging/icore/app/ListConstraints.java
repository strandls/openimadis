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

import java.util.HashSet;
import java.util.Set;

/**
 * validation by bounded range
 * @author arunabha
 *
 */
public class ListConstraints extends ParameterConstraints {
	
	/**
	 * list of valid values
	 */
	private Set<Object> validValues = null;
	/**
	 * type of the constrained parameter, one of integer, float, string only
	 */
	public final ParameterType type;
	
	/**
	 * Creates a ListConstraints with an unique list of long values
	 * @param values an unique list of long values
	 */
	public ListConstraints(long ... values)
	{
		validValues = new HashSet<Object>();
		for(long v : values)
			validValues.add(v);
		
		type = ParameterType.INTEGER;		
	}
	
	/**
	 * Creates a ListConstraints with an unique list of double values
	 * @param values an unique list of double values
	 */
	public ListConstraints(double ... values)
	{
		validValues = new HashSet<Object>();
		for(double v : values)
			validValues.add(v);
		
		type = ParameterType.DECIMAL;		
	}
	
	/**
	 * Creates a ListConstraints with an unique list of String values
	 * @param values an unique list of String values
	 */
	public ListConstraints(String ... values)
	{
		validValues = new HashSet<Object>();
		for(String v : values)
			validValues.add(v);
		
		type = ParameterType.STRING;		
	}

	@Override
	public boolean validate(Object value) 
	{
		if(value instanceof Integer)
			value = ((Integer) value).longValue();
		if(value instanceof Float)
			value = ((Float) value).doubleValue();
		return validValues.contains(value);
	}

	@Override
	public ParameterType getType() 
	{
		return type;
	}
	
	/**
	 * returns all the valid values for this constraint
	 * @return all the valid values for this constraint
	 */
	public Set<Object> getValidValues()
	{
		return validValues;
	}
}
