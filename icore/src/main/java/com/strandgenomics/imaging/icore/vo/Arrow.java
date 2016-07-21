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

package com.strandgenomics.imaging.icore.vo;

/**
 * Arow class represents the arrow constructed from series of points
 * It extends geomerticPath 
 * @author navneet
 *
 */
public class Arrow extends GeometricPath {
	
	/**
	 * creates empty polygon with default capacity 20
	 */
	public Arrow(){
		this(GeometricPath.INIT_SIZE);
	}
	/**
	 * create polygon with defined intial capacity
	 * @param initialCapacity
	 */
	public Arrow(int initialCapacity){
		super(VisualObjectType.ARROW, initialCapacity);
	}
	
	/**
	 * creates polygon with ID and initial capacity
	 * @param ID
	 * @param initialCapacity
	 */
	public Arrow(int ID, int initialCapacity){
		super(VisualObjectType.ARROW, ID, initialCapacity);
	}	
}
