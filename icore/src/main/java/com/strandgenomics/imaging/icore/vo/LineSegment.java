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
 * LineSegment.java
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

import java.awt.geom.Rectangle2D;

/**
 * A straight line between two points
 * @author arunabha
 */
public class LineSegment extends VisualObject {
	
	private static final long serialVersionUID = 4118724027056402979L;

	/**
     * The X coordinate of the start point of the line segment.
     */
    public double startX;

    /**
     * The Y coordinate of the start point of the line segment.
     */
    public double startY;

    /**
     * The X coordinate of the end point of the line segment.
     */
    public double endX;

    /**
     * The Y coordinate of the end point of the line segment.
     */
    public double endY;
	
    /**
     * Creates a line segment between two points
     * @param startX the X coordinate of the start point
     * @param startY the Y coordinate of the start point
     * @param endX the X coordinate of the end point
     * @param endY the Y coordinate of the end point
     */
	public LineSegment(double startX, double startY, double endX, double endY)
	{
	    super(VisualObjectType.LINE);
		setLine(startX,startY,endX,endY);
	}
	
    /**
     * Creates a line segment between two points
     * @param startX the X coordinate of the start point
     * @param startY the Y coordinate of the start point
     * @param endX the X coordinate of the end point
     * @param endY the Y coordinate of the end point
     */
	public LineSegment(int ID, double startX, double startY, double endX, double endY)
	{
		super(VisualObjectType.LINE, ID);
		setLine(startX,startY,endX,endY);
	}
	
	/**
     * Returns the X coordinate of the start point 
     * @return the X coordinate of the start point of this LineSegment
     */
	public double getStartX()
	{
		return startX;
	}

    /**
     * Returns the Y coordinate of the start point 
     * @return the Y coordinate of the start point of this LineSegment
     */
	public double getStartY()
	{
		return startY;
	}
	
   /**
    * Returns the X coordinate of the end point 
    * @return the X coordinate of the end point of this LineSegment
    */
	public double getEndX()
	{
		return endX;
	}

   /**
    * Returns the Y coordinate of the end point 
    * @return the Y coordinate of the end point of this Lie Segment
    */
	public double getEndY()
	{
		return endY;
	}
	
    /**
     * Sets the location of the end points of this <code>Line2D</code>
     * to the specified float coordinates.
     * @param startX the X coordinate of the start point
     * @param startY the Y coordinate of the start point
     * @param endX the X coordinate of the end point
     * @param endY the Y coordinate of the end point
     */
    public void setLine(double startX, double startY, double endX, double endY) 
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    @Override
	public Rectangle2D.Double getBounds()
	{
		double x = startX < endX ? startX : endX;
		double y = startY < endY ? startY : endY;
		double w = startX < endX ? endX - startX : startX - endX;
		double h = startY < endY ? endY - startY : startY - endY;
		
		return new Rectangle2D.Double(x,y,w,h);
	}
}
