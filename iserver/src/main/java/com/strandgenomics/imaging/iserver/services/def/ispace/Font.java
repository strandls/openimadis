/*
 * FontObject.java
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
 * Represents a font
 * @author arunabha
 *
 */
public class Font {
	
	/**
	 * name of the font
	 */
	private String name;
	/**
	 * style associated with the font 
	 * @see java.awt.Font
	 */
	private int style;
	/**
	 * size of the font
	 */
	private int size;
	
	public Font()
	{}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the style
	 */
	public int getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(int style) {
		this.style = style;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

}
