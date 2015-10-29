/*
 * Shape.java
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
 * A Shape is either a ellipse/rectangle/line-segment/free hand stuffs.
 * free hand stuffs are geometric-path constructed from straight lines (can be open or closed).
 * @author arunabha
 */
public abstract class Shape {
	
	private int ID;
	private int penColor;
	private float penWidth;
	private int zoomLevel;
	private String type;
	
	public Shape()
	{}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * @return the zoomLevel
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}
	
	/**
	 * @param zoomLevel the zoomLevel to set
	 */
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @return the penColor
	 */
	public int getPenColor() {
		return penColor;
	}

	/**
	 * @param penColor the penColor to set
	 */
	public void setPenColor(int penColor) {
		this.penColor = penColor;
	}

	/**
	 * @return the penWidth
	 */
	public float getPenWidth() {
		return penWidth;
	}

	/**
	 * @param penWidth the penWidth to set
	 */
	public void setPenWidth(float penWidth) {
		this.penWidth = penWidth;
	}

}
