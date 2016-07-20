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
 * RectangularShape.java
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
package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * Represents a rectangular visual object
 * @author arunabha
 */
public class RectangularShape extends Shape {
	
	/**
	 *  the X coordinate of the upper-left corner of the framing rectangle of this visual object
	 */
	private double x;
	/**
	 * the Y coordinate of the upper-left corner of the framing rectangle of this visual object
	 */
	private double y;
	/**
	 * the width (in pixel coordinate) of the bounding rectangle
	 */
	private double width;
	/**
	 * the height (in pixel coordinate) of the bounding rectangle
	 */
	private double height;
	/**
	 * clockwise rotation(in radians) around axis parallel to Y passing through centre of this rectangle; default 0 
	 */
	private double rotation = 0.0;
	
	public RectangularShape()
	{}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * returns angle in radians 
	 * @return angle in radians
	 */
	public double getRotation()
	{
		return rotation;
	}

	/**
	 * sets angle in radians
	 * @param rotation angle in radians
	 */
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}

}
