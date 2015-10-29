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
