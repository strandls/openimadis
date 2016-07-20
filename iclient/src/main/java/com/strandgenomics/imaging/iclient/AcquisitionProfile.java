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

package com.strandgenomics.imaging.iclient;

import java.io.Serializable;

/**
 * client side representation of acquisition profile object
 * 
 * @author Anup Kulkarni
 */
public class AcquisitionProfile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -711775848689730041L;
	/**
	 * name of the profile
	 */
	protected String profileName;
	/**
	 * name of the microscope
	 */
	protected String name;
	/**
	 * pixel size along x dimension
	 */
	protected Double xPixelSize;
	/**
	 * type of x field: VALUE, FACTOR, CORRECTION
	 */
	protected String xType;
	/**
	 * pixel size along y dimension
	 */
	protected Double yPixelSize;
	/**
	 * type of y field: VALUE, FACTOR, CORRECTION
	 */
	protected String yType;
	/**
	 * pixel size along z dimension
	 */
	protected Double zPixelSize;
	/**
	 * type of z field: VALUE, FACTOR, CORRECTION
	 */
	protected String zType;
	/**
	 * source format
	 */
	protected String sourceFormat;
	/**
	 * unit of time used in the acquisition
	 */
	protected String timeUnit;
	/**
	 * unit of length used in the acquisition
	 */
	protected String lengthUnit;
	
	/**
	 * creates acquisition profile
	 * @param profileName name of the acquisition profile
	 * @param name name of the microscope
	 * @param xPixelSize x pixel size parameter
	 * @param xType type of x pixel parameter
	 * @param yPixelSize y pixel size parameter
	 * @param yType type of y pixel parameter
	 * @param zPixelSize y pixel size parameter
	 * @param zType type of z pixel parameter
	 * @param sourceFormat source format
	 * @param timeUnit time unit 
	 * @param lengthUnit length unit
	 */
	public AcquisitionProfile(String profileName, String name, Double xPixelSize, String xType, Double yPixelSize, String yType, Double zPixelSize, String zType, String sourceFormat, String timeUnit, String lengthUnit)
	{
		this.profileName = profileName;
		this.name = name;
		
		this.xPixelSize = xPixelSize;
		this.xType = xType;
		
		this.yPixelSize = yPixelSize;
		this.yType = yType;
		
		this.zPixelSize = zPixelSize;
		this.zType = zType;
		
		this.sourceFormat = sourceFormat;
		
		this.timeUnit = timeUnit;
		this.lengthUnit = lengthUnit;
	}

	/**
	 * return name of the microscope
	 * @return name of the microscope
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public Double getxPixelSize()
	{
		return xPixelSize;
	}

	public Double getyPixelSize()
	{
		return yPixelSize;
	}

	public Double getzPixelSize()
	{
		return zPixelSize;
	}

	public String getSourceFormat()
	{
		return sourceFormat;
	}

	public String getTimeUnit()
	{
		return timeUnit;
	}

	public String getLengthUnit()
	{
		return lengthUnit;
	}
	
	public String getProfileName()
	{
		return this.profileName;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getxType()
	{
		return xType;
	}

	public String getyType()
	{
		return yType;
	}

	public String getzType()
	{
		return zType;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("MicroscopeName=");
		sb.append(this.name);
		sb.append(",");
		
		sb.append("ProfileName=");
		sb.append(this.profileName);
		
		return sb.toString();
	}
}
