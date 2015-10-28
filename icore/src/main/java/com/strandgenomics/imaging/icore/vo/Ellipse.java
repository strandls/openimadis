/*
 * Ellipse.java
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
 * The <code>Ellipse</code> class describes an ellipse that is defined by a framing rectangle.
 * @author arunabha
 */
public class Ellipse extends VisualObject {

	private static final long serialVersionUID = 5399819711012539649L;
	/**
	 * the bounding rectangle
	 */
	protected Rectangle2D.Double boundingRect = new Rectangle2D.Double();
    
    /**
     * Creates an Ellipse instance from the specified coordinates.
     *
     * @param x the X coordinate of the upper-left corner of the framing rectangle
     * @param y the Y coordinate of the upper-left corner of the framing rectangle
     * @param width the width of the framing rectangle
     * @param height the height of the framing rectangle
     */
    public Ellipse(double x, double y, double width, double height) 
    {
        super(VisualObjectType.ELLIPSE);
        setFrame(x, y, width, height);
    }
    
    /**
     * Creates an Ellipse instance from the specified coordinates.
     *
     * @param ID internal id of this object
     * @param x the X coordinate of the upper-left corner of the framing rectangle
     * @param y the Y coordinate of the upper-left corner of the framing rectangle
     * @param w the width of the framing rectangle
     * @param h the height of the framing rectangle
     */
    public Ellipse(int ID, double x, double y, double width, double height) 
    {
    	super(VisualObjectType.ELLIPSE, ID);
        setFrame(x, y, width, height);
    }
    
    /**
     * updates this Ellipse with the specified coordinates.
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
