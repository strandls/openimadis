/*
 * Rectangle.java
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
