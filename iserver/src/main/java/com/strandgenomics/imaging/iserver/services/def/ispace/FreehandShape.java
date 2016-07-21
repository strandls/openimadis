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
 * The FreehandShape class represents a geometric path constructed from a series line segments. 
 * A general free-hand drawing can be considered as a series of connected line segments
 */
public class FreehandShape extends Shape {
	
    /**
     * a series of x followed by y - coordinates of points capturing the path
     */
	private Float[] coordinates;
	
	public FreehandShape()
	{}

	/**
	 * @return the coordinates
	 */
	public Float[] getCoordinates() 
	{
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Float[] value) 
	{
		this.coordinates = value;
	}
}
