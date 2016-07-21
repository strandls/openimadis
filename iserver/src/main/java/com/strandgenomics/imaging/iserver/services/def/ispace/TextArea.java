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
