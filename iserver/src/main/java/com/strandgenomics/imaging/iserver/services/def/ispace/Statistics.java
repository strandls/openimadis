/*
 * Statistics.java
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
package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * The distribution of intensity values across the pixels within a PixelArray or Image
 * @author arunabha
 *
 */
public class Statistics {
	
	private int pixelDepth;
	/**
	 * Maximum intensity value
	 */
    private int maxValue;
    /**
     * Minimum intensity value
     */
    private int minValue;
    /**
     * list of all intensity values having non zero frequencies
     */
    private Integer[] intensities;
    /**
     * frequency distribution for the corresponding intensities
     */
    private Integer[] frequencies;
    /**
     * maximum frequency
     */
    private int maxFreq;
    
    public Statistics()
    {}

	/**
	 * @return the maxValue
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the minValue
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the intensities
	 */
	public Integer[] getIntensities() {
		return intensities;
	}

	/**
	 * @param intensities the intensities to set
	 */
	public void setIntensities(Integer[] intensities) {
		this.intensities = intensities;
	}

	/**
	 * @return the frequencies
	 */
	public Integer[] getFrequencies() {
		return frequencies;
	}

	/**
	 * @param frequencies the frequencies to set
	 */
	public void setFrequencies(Integer[] frequencies) {
		this.frequencies = frequencies;
	}

	/**
	 * @return the maxFreq
	 */
	public int getMaxFreq() {
		return maxFreq;
	}

	/**
	 * @param maxFreq the maxFreq to set
	 */
	public void setMaxFreq(int maxFreq) {
		this.maxFreq = maxFreq;
	}

	/**
	 * @return the pixelDepth
	 */
	public int getPixelDepth() {
		return pixelDepth;
	}

	/**
	 * @param pixelDepth the pixelDepth to set
	 */
	public void setPixelDepth(int pixelDepth) {
		this.pixelDepth = pixelDepth;
	}
}
