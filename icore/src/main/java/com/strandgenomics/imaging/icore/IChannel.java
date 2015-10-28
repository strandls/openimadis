/*
 * IChannel.java
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
package com.strandgenomics.imaging.icore;

/**
 * Represents a channel of a record
 * @author arunabha
 */
public interface IChannel {
	
	/**
	 * Returns the name of this channel
	 * @return the name of this channel
	 */
	public String getName();
	
	/**
	 * Returns the wavelength of the channel light source
	 * @return wavelength in micrometre if set, else -1
	 */
	public int getWavelength();
	
	/**
	 * Returns the lut name associated with this channel
	 * @return the lut name associated with this channel
	 */
	public String getLut();
	
    /**
     * Returns the custom contrast, if any, for all images of this channel of the owning record
     * @param zStacked true if zStacked contrast, false otherwise
     * @return the custom contrast, if any, for all images of this channel of the owning record, otherwise null
     */
    public VisualContrast getContrast(boolean zStacked);
}
