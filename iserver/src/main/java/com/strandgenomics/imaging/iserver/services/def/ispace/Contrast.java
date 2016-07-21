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

package com.strandgenomics.imaging.iserver.services.def.ispace;

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
