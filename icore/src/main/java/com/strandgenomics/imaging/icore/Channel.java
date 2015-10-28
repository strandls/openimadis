/*
 * Channel.java
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

import java.io.Serializable;

/**
 * Represents a channel of a record
 * @author arunabha
 */
public class Channel implements IChannel, Serializable {
	
	private static final long serialVersionUID = -2187489346863510931L;
	/**
	 * name of the channel
	 */
	protected String name;
	/**
	 * wavelength of the channel light source (in micrometre)
	 */
	protected int wavelength = -1;
	/**
	 * color associated with this channel, default is green
	 */
	protected int rgbColor = 0xFF00FF00;
	
	/**
	 * lut associated with channel
	 */
	protected String lut = "grays";
	/**
	 * user can set a custom contrast for a channel in a record
	 */
	protected VisualContrast customContrast = null;
	/**
	 * contrast for the z-stacked image
	 */
	protected VisualContrast zstackContrast = null;
	
	/**
	 * Revision number for channel
	 * Any change to lut, contrast will increment the revision number 
	 */
	protected long revision=0;
	
	/**
	 * creates a default channel 
	 * @param name name of this channel
	 */
	public Channel(String name)
	{
		this(name, 0xFF00FF00);
	}
	
	/**
	 * Creates a channel with the specified name, color and contrast
	 * @param name name of the channel
	 * @param color color associated with this channel
	 */
	public Channel(String name, int color)
	{
		this.name = name;
		this.rgbColor = color;
		
	}
	
	/**
	 * Creates a channel with the specified name and lut
	 * @param name name name of the channel
	 * @param lut name of the lut
	 */
	public Channel(String name, String lut)
	{
		this.name = name;
		this.lut = lut;
	}
	
	/**
	 * Returns the name of this channel
	 * @return the name of this channel
	 */
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String value)
	{
		this.name = value;
	}
	
	/**
	 * Returns the color associated with this channel
	 * @return the color associated with this channel
	 */
	public int getColor()
	{
		return this.rgbColor;
	}
	
	/**
	 * Returns the lut associated with this channel
	 * @return the lut associated with this channel
	 */
	public String getLut()
	{
		return this.lut == null ? "grays" : this.lut;
	}
	
	/**
	 * Sets the lut associated with this channel
	 * @param lut the name of lut to be associated with this channel
	 */
	public void setLut(String lut)
	{
		this.lut = lut;
	}
	
    /**
     * Sets a custom contrast for all images of this channel of the owning record
     * @param zStacked true if zStacked contrast, false otherwise
     * @param contrast the custom contrast to set
     */
    public void setContrast(boolean zStacked, VisualContrast contrast)
    {
    	if(zStacked)
    		this.zstackContrast = contrast;
    	else
    		this.customContrast = contrast;
    }
    
    /**
     * Returns the custom contrast, if any, for all images of this channel of the owning record
     * @param zStacked true if zStacked contrast, false otherwise
     * @return the custom contrast, if any, for all images of this channel of the owning record, otherwise null
     */
    public VisualContrast getContrast(boolean zStacked)
    {
    	if(zStacked)
    		return this.zstackContrast;
    	
    	return customContrast;
    }
    
    public int getWavelength()
    {
    	return wavelength;
    }
    
    
    public void setWavelength(int waveLength)
    {
    	this.wavelength = waveLength;
    }
    
    
    public long getRevision() {
		return revision;
	}
    
    
    public void setRevision(long revision) {
		this.revision = revision;
	}
    
    
    public String toString()
    {
    	if(wavelength > 0)
    	{
    		return name +"("+wavelength+")";
    	}
    	else
    	{
    		return name;
    	}
    }
    
    @Override
    public int hashCode()
    {
    	return this.name.hashCode();
    }   
    
    @Override
	public boolean equals(Object obj)
	{
    	if(obj != null && obj instanceof Channel)
		{
    		Channel that = (Channel) obj;
			if(this == that) return true;
			
			boolean equals = this.lut.equals(that.lut) && this.name.equals(that.name);
			if (this.customContrast != null)
			    equals = equals && this.customContrast.equals(that.customContrast);
			if (this.zstackContrast != null)
			    equals = equals && this.zstackContrast.equals(that.zstackContrast);
			return equals;
		}
		return false;
	}
}
