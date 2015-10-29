/*
 * PolygonShape.java
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
 * Polygon connecting points
 * @author navneet
 *
 */

public class PolygonShape extends Shape {

	/**
	 * array storing the coordinates of point which form polygon
	 */
	private Double points[];
	
	public PolygonShape(){}
	
	/**
	 * get points of polygon
	 * @return
	 */
	public Double[] getPoints(){
		
		return this.points;
	}
	
	/**
	 * set points of a polygon
	 * @param points
	 */
	public void setPoints(Double points[]){
		this.points=points;
	}
}
