/*
 * Constraints.java
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
package com.strandgenomics.imaging.iserver.services.def.compute;

public class StringListConstraints extends Constraints {
	
	private String[] validValues = null;
	
	public StringListConstraints()
	{}

	/**
	 * @return the validValues
	 */
	public String[] getValidValues() 
	{
		return validValues;
	}

	/**
	 * @param validValues the validValues to set
	 */
	public void setValidValues(String[] validValues)
	{
		this.validValues = validValues;
	}
}
