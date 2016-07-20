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
 * microscope specific settings used for acquisition
 * 
 * @author Anup Kulkarni
 */
public class AcquisitionProfile {
	/**
	 * name of the profile
	 */
	private String profileName;
	/**
	 * name of the microscope
	 */
	private String name;
	/**
	 * pixel size along x dimension
	 */
	private Double xPixelSize;
	/**
	 * type of x pixel size field
	 */
	private String xType;
	/**
	 * pixel size along y dimension
	 */
	private Double yPixelSize;
	/**
	 * type of y pixel size field
	 */
	private String yType;
	/**
	 * pixel size along z dimension
	 */
	private Double zPixelSize;
	/**
	 * type of z pixel size field
	 */
	private String zType;
	/**
	 * source format
	 */
	private String sourceFormat;
	/**
	 * unit of time used in the acquisition
	 */
	private String timeUnit;
	/**
	 * unit of length used in the acquisition
	 */
	private String lengthUnit;
	
	public AcquisitionProfile()
	{}
	
	public String getProfileName()
	{
		return this.profileName;
	}

	public void setProfileName(String profileName)
	{
		this.profileName = profileName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Double getxPixelSize()
	{
		return xPixelSize;
	}
	
	public void setxPixelSize(Double xPixelSize)
	{
		this.xPixelSize = xPixelSize;
	}

	public Double getyPixelSize()
	{
		return yPixelSize;
	}

	public void setyPixelSize(Double yPixelSize)
	{
		this.yPixelSize = yPixelSize;
	}

	public Double getzPixelSize()
	{
		return zPixelSize;
	}

	public void setzPixelSize(Double zPixelSize)
	{
		this.zPixelSize = zPixelSize;
	}

	
	public String getxType()
	{
		return this.xType;
	}

	public void setxType(String xType)
	{
		this.xType = xType;
	}

	public String getyType()
	{
		return this.yType;
	}

	public void setyType(String yType)
	{
		this.yType = yType;
	}

	public String getzType()
	{
		return this.zType;
	}

	public void setzType(String zType)
	{
		this.zType = zType;
	}

	public String getSourceFormat()
	{
		return sourceFormat;
	}

	public void setSourceFormat(String sourceFormat)
	{
		this.sourceFormat = sourceFormat;
	}

	public String getTimeUnit()
	{
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit)
	{
		this.timeUnit = timeUnit;
	}

	public String getLengthUnit()
	{
		return lengthUnit;
	}

	public void setLengthUnit(String lengthUnit)
	{
		this.lengthUnit = lengthUnit;
	}
}
