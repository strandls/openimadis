/*
 * ListConstraints.java
 *
 * AVADIS Image Management System
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
