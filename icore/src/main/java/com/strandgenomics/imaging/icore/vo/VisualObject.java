/*
 * VisualOverlay.java
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
import java.awt.geom.Rectangle2D;

import com.strandgenomics.imaging.icore.Storable;

/**
 * A visual object is either a ellipse/rectangle/line-segment/free hand stuffs.
 * free hand stuffs are geometric-path constructed from straight lines (can be open or closed).
 * @author arunabha
 */
public abstract class VisualObject implements Storable {
	
	private static final long serialVersionUID = 3271671962237935709L;
	private static volatile int ID_GENERATOR = (int)System.currentTimeMillis();
	
	public final int ID;
	protected Color penColor = Color.BLACK;
	protected float penWidth = 1.0f;
	protected float scaleX=1;
	protected float scaleY=1;
	protected double rotation=0;
	protected int zoomLevel=0;
    private VisualObjectType type;
	
	VisualObject(VisualObjectType type)
	{
		this.type = type;
        this.ID = ID_GENERATOR++;
	}
	
	VisualObject(VisualObjectType type, int id)
	{
	    this.type = type;
		this.ID = id;
	}
	
	/**
	 * Sets Zoom Level of the visual object
	 */
	public void setZoomLevel(int value)
	{
		zoomLevel = value;
	}

	/**
	 * Get the Zoom Level of the visual object
	 * @return zoom level of the object
	 */
	public int getZoomLevel() {
        return zoomLevel;
    }
	
	/**
	 * Returns the RGB value representing the color of the drawing pen
	 * @return color of the drawing pen
	 */
	public Color getPenColor()
	{
		return penColor;
	}
	
	public void setPenColor(Color value)
	{
		if(value != null)
		{
			penColor = value;
		}
	}
	
	/**
	 * Thickness of the drawing pen
	 * @return thickness of the drawing pen
	 */
	public float getPenWidth()
	{
		return penWidth;
	}
	
	/**
	 * Sets Thickness of the drawing pen
	 */
	public void setPenWidth(float value)
	{
		penWidth = value;
	}

	/**
	 * Get the type of the visual object
	 * @return type of the object
	 */
	public VisualObjectType getType() {
        return type;
    }
	
	/**
	 * Sets VisualObjectType of the visual object
	 */
	public void setType(VisualObjectType value)
	{
		this.type = value;
	}
	
	/** Set the scaling in X direction
	 * 
	 */
	public void setScaleX(float x){
		
		scaleX=x;
	}

	/** Set the scaling in Y direction
	 * 
	 */
	public void setScaleY(float y){
		
		scaleY=y;
	}

	/** Get the scaling in X direction
	 * 
	 */
	public float getScaleX(){
		
		return scaleX;
	}

	/** Get the scaling in Y direction
	 * 
	 */
	public float getScaleY(){
		
		return scaleY;
	}
	
	/**
	 * Get rotation angle
	 * @return
	 */
	public double getRotation(){
		return rotation;
	}
	
	/**
	 * Set rotation angle
	 * @param rotation
	 */
	public void setRotation(double rotation){
		this.rotation=rotation;
	}
	
	/**
	 * get angle of rotation in degrees
	 * @return  angle of rotation in degrees
	 */
	public double getRotationInDegrees()
	{
		return this.rotation * 180/Math.PI;
	}
	
	/**
	 * set angle of rotation specified in degrees
	 * @param angleInDegrees angle of rotation specified in degrees
	 */
	public void setRotationInDegrees(double angleInDegrees)
	{
		this.rotation = angleInDegrees*Math.PI/180;
	}
	
	/**
	 * Returns the framing rectangle of this visual object
	 * @return the framing rectangle of this visual object
	 */
	public abstract Rectangle2D.Double getBounds();
	

	public void dispose() 
	{}
	
	public boolean equals(VisualObject obj)
	{
		if(obj != null && obj instanceof VisualObject)
		{
			VisualObject that = (VisualObject) obj;
			return (this == that || this.ID == that.ID);
		}
		return false;
	}
}
