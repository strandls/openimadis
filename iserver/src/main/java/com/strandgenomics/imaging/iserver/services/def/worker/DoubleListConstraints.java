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

package com.strandgenomics.imaging.iserver.services.def.worker;

/**
 * A constraint on a double parameter and specified by a list of valid values
 * @author arunabha
 *
 */
public class DoubleListConstraints extends Constraints {

	private Double[] validValues = null;
	
	public DoubleListConstraints()
	{}

	/**
	 * @return the validValues
	 */
	public Double[] getValidValues() 
	{
		return validValues;
	}

	/**
	 * @param validValues the validValues to set
	 */
	public void setValidValues(Double[] validValues)
	{
		this.validValues = validValues;
	}
}
