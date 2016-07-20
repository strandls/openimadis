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
 * RangeConstraints.java
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
package com.strandgenomics.imaging.icore.app;

/**
 * parameter validation by range
 * @author arunabha
 *
 */
public class RangeConstraints extends ParameterConstraints {
	/**
	 * lower limit (inclusive)
	 */
	public final Number lowerLimit;
	/**
	 * upper limit (inclusive)
	 */
	public final Number upperLimit;
	
	/**
	 * type of the constrained parameter, one of integer, float only
	 */
	public final ParameterType type;
	
	/**
	 * Creates a integral range constraints 
	 * @param lowerLimit the lower limit of this range
	 * @param upperLimit the upper limit of this range
	 */
	public RangeConstraints(long lowerLimit, long upperLimit)
	{
		this.lowerLimit = Long.valueOf(lowerLimit);
		this.upperLimit = Long.valueOf(upperLimit);
		type = ParameterType.INTEGER;		
	}
	
	/**
	 * Creates a integral range constraints 
	 * @param lowerLimit the lower limit of this range
	 * @param upperLimit the upper limit of this range
	 */
	public RangeConstraints(double lowerLimit, double upperLimit)
	{
		this.lowerLimit = Double.valueOf(lowerLimit);
		this.upperLimit = Double.valueOf(upperLimit);
		type = ParameterType.DECIMAL;		
	}

	@Override
	public boolean validate(Object value) 
	{
		Number v = (Number) value;
		switch(type)
		{
			case INTEGER:
				return v.longValue() >= lowerLimit.longValue() && v.longValue() <= upperLimit.longValue();
			case DECIMAL:
				return v.doubleValue() >= lowerLimit.doubleValue() && v.doubleValue() <= upperLimit.doubleValue();
			default:
				throw new IllegalArgumentException("illegal operation"); 
		}
	}

	@Override
	public ParameterType getType() 
	{
		return type;
	}
}
