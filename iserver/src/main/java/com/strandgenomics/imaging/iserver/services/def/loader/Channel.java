/*
 * ImageChannel.java
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
 * Represents a channel of a record
 * @author arunabha
 */
public class Channel {
	
	/**
	 * name of the channel
	 */
	private String name;
	/**
	 * lut associated with this channel
	 */
	private String lutName;
	/**
	 * user can set a custom contrast for a channel in a record
	 */
	private Contrast contrast = null;
	/**
	 * user can set a custom contrast for a channel in a record for z stacked mode
	 */
	private Contrast zStackedContrast = null;
	/**
	 * user can set a custom contrast for a channel in a record
	 */
	private int wavelength;
	
	/**
	 * Revision number for channel
	 * Any change to lut, contrast will increment the revision number 
	 */
	protected long revision=0;
	
	public Channel()
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
     * Sets a custom contrast for all images of this channel of the owning record
     * @param contrast the custom contrast to set
     */
    public void setContrast(Contrast contrast)
    {
    	this.contrast = contrast;
    }
    
    /**
     * Returns the custom contrast, if any, for all images of this channel of the owning record
     * @return the custom contrast, if any, for all images of this channel of the owning record, otherwise null
     */
    public Contrast getContrast()
    {
    	return contrast;
    }

    /**
     * sets the lut for this channel
     * @param lutName name of the lut
     */
	public void setLutName(String lutName)
	{
		this.lutName = lutName;
	}

	/**
	 * returns the name of lut for this channel
	 * @return the name of lut for this channel
	 */
	public String getLutName()
	{
		return lutName;
	}
	
	/**
	 * @return channel wavelength
	 */
	public int getWavelength()
	{
		return wavelength;
	}
	
	/**
	 * sets the channel wavelength
	 * @param wavelength
	 */
	public void setWavelength(int wavelength)
	{
		this.wavelength = wavelength;
	}
	
	/**
	 * @return z stacked contrast
	 */
	public Contrast getZStackedContrast()
	{
		return this.zStackedContrast;
	}
	
	/**
	 * sets the z stacked contrast for the channel
	 * @param zStackedContrast
	 */
	public void setZStackedContrast(Contrast zStackedContrast)
	{
		this.zStackedContrast = zStackedContrast;
	}
	
	public long getRevision() {
		return revision;
	}
	
	public void setRevision(long revision) {
		this.revision = revision;
	}
}
