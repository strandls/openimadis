/*
 * Arrow.java
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
