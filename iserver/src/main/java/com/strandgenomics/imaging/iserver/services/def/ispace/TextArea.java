/*
 * TextArea.java
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
 * Represents a rectangular visual object containing a text
 * @author arunabha
 */
public class TextArea extends RectangularShape {
	
	/**
	 * the associated text
	 */
	private String text;
	
	private Font font;
	
	private int backgroundColor;
	
	/**
	 * @return the font
	 */
	public Font getFont() 
	{
		return font;
	}

	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) 
	{
		this.font = font;
	}

	/**
	 * @return the bgColor
	 */
	public int getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * @param bgColor the Background Color to set
	 */
	public void setBackgroundColor(int bgColor) 
	{
		this.backgroundColor = bgColor;
	}

	public TextArea(){}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) 
	{
		this.text = text;
	}
}
