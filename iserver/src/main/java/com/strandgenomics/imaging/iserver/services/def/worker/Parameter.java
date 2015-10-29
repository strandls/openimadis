/*
 * Parameter.java
 *
 * AVADIS Image Management System
 * Web-service definition
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
package com.strandgenomics.imaging.iserver.services.def.worker;

/**
 * Definition of an application parameter
 * @author arunabha
 *
 */
public class Parameter {
	/**
	 * name of the parameter
	 */
	private String name;
	/**
	 * type of the parameter, one of integer, float, boolean and string only
	 */
	private String type;
	/**
	 * constraints on this parameter, can be null, meaning unbounded
	 */
	private Constraints constraints;
	/**
	 * default value associated with this parameter, null is not a valid value
	 */
	private Object defaultValue;
	/**
	 * description of the parameter
	 */
	private String description;
	
	
	public Parameter()
	{}
	
	

	/**
	 * @return the name
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() 
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) 
	{
		this.type = type;
	}

	/**
	 * @return the constraints
	 */
	public Constraints getConstraints() 
	{
		return constraints;
	}

	/**
	 * @param constraints the constraints to set
	 */
	public void setConstraints(Constraints constraints)
	{
		this.constraints = constraints;
	}

	/**
	 * @return the defaultValue
	 */
	public Object getDefaultValue() 
	{
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	/**
	 * returns the description of the parameter
	 * @return
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * sets the description of parameter
	 * @param description parameter description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}

