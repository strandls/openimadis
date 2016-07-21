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

package com.strandgenomics.imaging.icore.vo;

import java.awt.geom.Rectangle2D;

/**
 * Represents a rectangular visual object
 * @author arunabha
 */
public class Rectangle extends VisualObject {
	
	private static final long serialVersionUID = 4171734812616041424L;
	/**
	 * the bounding rectangle
	 */
	protected Rectangle2D.Double boundingRect = new Rectangle2D.Double();
	
	/**
	 * Creates an rectangular visual objects  
	 * @param x X coordinate of the upper-left corner of this rectangle 
	 * @param y Y coordinate of the upper-left corner of this rectangle 
	 * @param width the width (in pixel coordinate) of this rectangle
	 * @param height the width (in pixel coordinate) of this rectangle
	 */
	public Rectangle(double x, double y, double width, double height)
	{
	    super(VisualObjectType.RECTANGLE);
		setBounds(x, y, width, height);
	}

	/**
	 * rectangle with object id
	 * @param ID object id
	 * @param x X coordinate of the upper-left corner of this rectangle 
	 * @param y Y coordinate of the upper-left corner of this rectangle 
	 * @param width the width (in pixel coordinate) of this rectangle
	 * @param height the width (in pixel coordinate) of this rectangle
	 */
	public Rectangle(int ID, double x, double y, double width, double height)
	{
		super(VisualObjectType.RECTANGLE, ID);
		setBounds(x, y, width, height);
	}
	
	/**
     * Creates an rectangular visual objects with a given type  
	 * @param type type of object
     * @param x X coordinate of the upper-left corner of this rectangle 
     * @param y Y coordinate of the upper-left corner of this rectangle 
     * @param width the width (in pixel coordinate) of this rectangle
     * @param height the width (in pixel coordinate) of this rectangle
     */
    public Rectangle(VisualObjectType type, double x, double y, double width, double height)
    {
        super(type);
        setBounds(x, y, width, height);
    }

    /**
     * Creates an rectangular visual objects with a given type  
     * @param type type of object
     * @param ID object id
     * @param x X coordinate of the upper-left corner of this rectangle 
     * @param y Y coordinate of the upper-left corner of this rectangle 
     * @param width the width (in pixel coordinate) of this rectangle
     * @param height the width (in pixel coordinate) of this rectangle
     */
    public Rectangle(VisualObjectType type, int ID, double x, double y, double width, double height)
    {
        super(type, ID);
        setBounds(x, y, width, height);
    }
    
	/**
	 * set the new bounds of this object
	 * @param x the X coordinate of the upper-left corner of the framing rectangle of this visual object
	 * @param y the Y coordinate of the upper-left corner of the framing rectangle of this visual object
	 * @param width the width (in pixel coordinate) of the bounding rectangle
	 * @param height the height (in pixel coordinate) of the bounding rectangle
	 */
	public void setBounds(double x, double y, double width, double height)
	{
		boundingRect.x = x;
		boundingRect.y = y;
		boundingRect.width = width;
		boundingRect.height = height;
	}
	
	@Override
	public Rectangle2D.Double getBounds()
	{
		return boundingRect;
	}
}
