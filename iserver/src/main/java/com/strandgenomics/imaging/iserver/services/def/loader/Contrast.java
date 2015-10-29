/*
 * Contrast.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.loader;

/**
 * Visual Contrast for images of a specific channel
 * @author arunabha
 *
 */
public class Contrast {
	
	/**
	 * minimum intensity value
	 */
	private int minIntensity;
	/**
	 * maximum intensity value
	 */
	private int maxIntensity;
	/**
	 * gamma, default is 1.0
	 */
	private double gamma;
	
	public Contrast()
    {}
    
    /**
	 * @return the minIntensity
	 */
	public int getMinIntensity()
	{
		return minIntensity;
	}

	/**
	 * @param minIntensity the minIntensity to set
	 */
	public void setMinIntensity(int minIntensity) 
	{
		this.minIntensity = minIntensity;
	}

	/**
	 * @return the maxIntensity
	 */
	public int getMaxIntensity()
	{
		return maxIntensity;
	}

	/**
	 * @param maxIntensity the maxIntensity to set
	 */
	public void setMaxIntensity(int maxIntensity)
	{
		this.maxIntensity = maxIntensity;
	}

	/**
	 * @return the gamma
	 */
	public double getGamma()
	{
		return gamma;
	}

	/**
	 * @param gamma the gamma to set
	 */
	public void setGamma(double gamma)
	{
		this.gamma = gamma;
	}
}
