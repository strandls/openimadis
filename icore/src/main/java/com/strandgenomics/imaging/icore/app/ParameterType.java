/*
 * ParameterType.java
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

public enum ParameterType {
	
	BOOLEAN(Boolean.class, "boolean"),
	INTEGER(Integer.class, "int"),
	DECIMAL(Double.class, "double"),
	STRING(String.class, "String");
	
	private final String description;
	private final Class<?> clazz;
	
	private ParameterType(Class<?> clazz, String description)
	{
		this.description = description;
		this.clazz = clazz;
	}
	
	@Override
	public String toString()
	{
		return description;
	}
	
	public boolean isValidValue(Object value)
	{
		return value == null || clazz.isInstance(value);
	}
}
