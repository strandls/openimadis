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
 * StraightLine.java
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
 * A straight line between two points
 * @author arunabha
 */
public class StraightLine extends Shape {

	 /**
     * The X coordinate of the start point of the line segment.
     */
    private double startX;

    /**
     * The Y coordinate of the start point of the line segment.
     */
    private double startY;

    /**
     * The X coordinate of the end point of the line segment.
     */
    private double endX;

    /**
     * The Y coordinate of the end point of the line segment.
     */
    private double endY;
    
    public StraightLine(){}

	/**
	 * @return the startX
	 */
	public double getStartX() {
		return startX;
	}

	/**
	 * @param startX the startX to set
	 */
	public void setStartX(double startX) {
		this.startX = startX;
	}

	/**
	 * @return the startY
	 */
	public double getStartY() {
		return startY;
	}

	/**
	 * @param startY the startY to set
	 */
	public void setStartY(double startY) {
		this.startY = startY;
	}

	/**
	 * @return the endX
	 */
	public double getEndX() {
		return endX;
	}

	/**
	 * @param endX the endX to set
	 */
	public void setEndX(double endX) {
		this.endX = endX;
	}

	/**
	 * @return the endY
	 */
	public double getEndY() {
		return endY;
	}

	/**
	 * @param endY the endY to set
	 */
	public void setEndY(double endY) {
		this.endY = endY;
	}
}
