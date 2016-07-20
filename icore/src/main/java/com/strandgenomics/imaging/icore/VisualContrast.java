/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * VisualContrast.java
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
 * Contrast of a pixel data
 * @author arunabha
 *
 */
public class VisualContrast implements Serializable {
	
	private static final long serialVersionUID = 820389232394312288L;
	/**
	 * minimum intensity value
	 */
	protected int minIntensity;
	/**
	 * maximum intensity value
	 */
    protected int maxIntensity;
	/**
	 * gamma, default is 1.0
	 */
    protected double gamma;
    
    public VisualContrast(int minIntensity, int maxIntensity)
    {
    	this(minIntensity, maxIntensity, 1.0);
    }
    
    public VisualContrast(int minIntensity, int maxIntensity, double gamma)
    {
    	this.gamma = gamma;
    	setIntensityRange(minIntensity, maxIntensity);
    }
    
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof VisualContrast)
    	{
    		VisualContrast that = (VisualContrast)obj;
    		return (this.maxIntensity == that.maxIntensity &&
    				this.minIntensity == that.minIntensity &&
    				Math.abs(this.gamma - that.gamma) < 0.01);
    	}
    	return false;
    }
    
    /**
     * @return gamma value
     */
    public double getGamma()
    {
    	return gamma;
    }
    
    /**
     * 
     * @param value gamma value
     */
    public void setGamma(double value)
    {
    	gamma = value;
    }
    
    /**
     * 
     * @return min intensity value
     */
    public int getMinIntensity()
    {
    	return this.minIntensity;
    }
    
    /**
     * 
     * @return max intensity value
     */
    public int getMaxIntensity()
    {
    	return this.maxIntensity;
    }
   
    
    public void setIntensityRange(int minIntensity, int maxIntensity)
    {
    	if(minIntensity >= maxIntensity)
    		throw new IllegalArgumentException("minIntensity("+minIntensity+") >= maxIntensity("+maxIntensity+") is not acceptable ");
    	
    	this.minIntensity = minIntensity;
		this.maxIntensity = maxIntensity;
    }
}
