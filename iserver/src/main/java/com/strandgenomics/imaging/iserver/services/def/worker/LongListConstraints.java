/*
 * LongListConstraints.java
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
 * A constraint on a long parameter and specified by a list of valid values
 * @author arunabha
 *
 */
public class LongListConstraints extends Constraints {

	private Long[] validValues = null;
	
	public LongListConstraints()
	{}

	/**
	 * @return the validValues
	 */
	public Long[] getValidValues() 
	{
		return validValues;
	}

	/**
	 * @param validValues the validValues to set
	 */
	public void setValidValues(Long[] validValues)
	{
		this.validValues = validValues;
	}
}
