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
 * The <code>Circle</code> class describes an circle that is defined by a framing rectangle.
 * @author navneet
 */
public class Circle extends VisualObject {

	private static final long serialVersionUID = 5399819711012539649L;
	/**
	 * the bounding rectangle
	 */
	protected Rectangle2D.Double boundingRect = new Rectangle2D.Double();
    
    /**
     * Creates an Circle instance from the specified coordinates.
     *
     * @param x the X coordinate of the upper-left corner of the framing rectangle
     * @param y the Y coordinate of the upper-left corner of the framing rectangle
     * @param w the width of the framing rectangle
     * @param h the height of the framing rectangle
     */
    public Circle(double x, double y, double width, double height) 
    {
        super(VisualObjectType.CIRCLE);
        setFrame(x, y, width, height);
    }
    
    /**
     * Creates an Circle instance from the specified coordinates.
     *
     * @param x the X coordinate of the upper-left corner of the framing rectangle
     * @param y the Y coordinate of the upper-left corner of the framing rectangle
     * @param w the width of the framing rectangle
     * @param h the height of the framing rectangle
     */
    public Circle(int ID, double x, double y, double width, double height) 
    {
    	super(VisualObjectType.CIRCLE, ID);
        setFrame(x, y, width, height);
    }
    
    /**
     * updates this Circle with the specified coordinates.
     *
     * @param x the X coordinate of the upper-left corner of the framing rectangle
     * @param y the Y coordinate of the upper-left corner of the framing rectangle
     * @param w the width of the framing rectangle
     * @param h the height of the framing rectangle
     */
    public void setFrame(double x, double y, double width, double height) 
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
