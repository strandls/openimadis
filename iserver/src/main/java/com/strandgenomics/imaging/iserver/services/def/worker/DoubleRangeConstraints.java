/*
 * DoubleRangeConstraints.java
 *
 * AVADIS Image Management System
 * Compute Web-service definition
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
 * A constraint on a double parameter and defined by a by range of valid values
 * @author arunabha
 *
 */
public class DoubleRangeConstraints  extends Constraints {
	
	/**
	 * lower limit (inclusive)
	 */
	private double lowerLimit;
	/**
	 * upper limit (inclusive)
	 */
	private double upperLimit;
	
	public DoubleRangeConstraints()
	{}
	
	/**
	 * @return the lowerLimit
	 */
	public double getLowerLimit()
	{
		return lowerLimit;
	}

	/**
	 * @param lowerLimit the lowerLimit to set
	 */
	public void setLowerLimit(double lowerLimit) 
	{
		this.lowerLimit = lowerLimit;
	}

	/**
	 * @return the upperLimit
	 */
	public double getUpperLimit() 
	{
		return upperLimit;
	}

	/**
	 * @param upperLimit the upperLimit to set
	 */
	public void setUpperLimit(double upperLimit) 
	{
		this.upperLimit = upperLimit;
	}
}
