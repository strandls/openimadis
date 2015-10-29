/*
 * FreehandShape.java
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
