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
 * TextBox.java
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

import java.awt.Color;
import java.awt.Font;

/**
 * Represents a rectangular visual object containing a text
 * @author arunabha
 */
public class TextBox extends Rectangle {
	
	private static final long serialVersionUID = 1886131430525827712L;
	public static final Font DEFAULT_FONT = new Font ("Arial", Font.PLAIN, 10);
	public static final Color DEFAULT_BKG_COLOR = Color.WHITE;
	
	/**
	 * the associated text
	 */
	protected String text = null;
	/**
	 * font used for this text box
	 */
	protected Font font = DEFAULT_FONT;
	/**
	 * background color for text box
	 */
	protected Color backgroundColor = DEFAULT_BKG_COLOR;
	
	/**
	 * Creates an rectangular visual objects with text inside
	 * @param x X coordinate of the upper-left corner of this rectangle 
	 * @param y Y coordinate of the upper-left corner of this rectangle 
	 * @param width the width (in pixel coordinate) of this rectangle
	 * @param height the width (in pixel coordinate) of this rectangle
	 */
	public TextBox(double x, double y, double width, double height)
	{
		super(VisualObjectType.TEXT, x, y, width, height);
	}
	
	/**
	 * Creates an rectangular visual objects with text inside
	 * @param x X coordinate of the upper-left corner of this rectangle 
	 * @param y Y coordinate of the upper-left corner of this rectangle 
	 * @param width the width (in pixel coordinate) of this rectangle
	 * @param height the width (in pixel coordinate) of this rectangle
	 * @param text text to be placed in the text box
	 */
	public TextBox(double x, double y, double width, double height, String text)
	{
		super(VisualObjectType.TEXT, x, y, width, height);
		setText(text);
	}
	
	/**
	 * Creates an rectangular visual objects with text inside
	 * @param ID object id
	 * @param x X coordinate of the upper-left corner of this rectangle 
	 * @param y Y coordinate of the upper-left corner of this rectangle 
	 * @param width the width (in pixel coordinate) of this rectangle
	 * @param height the width (in pixel coordinate) of this rectangle
	 * @param text text to be placed in the text box
	 */
	public TextBox(int ID, double x, double y, double width, double height, String text)
	{
		super(VisualObjectType.TEXT, ID, x, y, width, height);
		setText(text);
	}
	
	/**
	 * Returns the text associated with this text box
	 * @return the text associated with this text box
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Sets the text associated with this TextBox
	 * @param text
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 * returns the font of the text
	 * @return the font of the text
	 */
	public Font getFont()
	{
		return font;
	}
	
	/**
	 * set the font for the text in the textbox
	 * @param f specified font
	 */
	public void setFont(Font f)
	{
		if(f != null)
		{
			this.font = f;
		}
	}
	
	/**
	 * set the background color for the text of the textbox
	 * @param color specified color
	 */
	public void setBkgColor(Color color)
	{
		this.backgroundColor = color;
	}
	
	/**
	 * returns the background color of the text box
	 * @return returns the background color of the textbox
	 */
	public Color getBkgColor()
	{
		return this.backgroundColor;
	}
}

