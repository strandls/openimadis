/*
 * BioVisualOverlay.java
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
package com.strandgenomics.imaging.icore.bioformats;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

public class BioVisualOverlay implements IVisualOverlay, Serializable, Cloneable {

	private static final long serialVersionUID = -7986365054308113306L;
	/**
	 * ID of the visual annotation
	 */
	public final VODimension voID;
	/**
	 * name of this visual overlay
	 */
	public String name;
	/**
	 * Width of this visual annotation
	 */
	public final int imageWidth;
	/**
	 * Height of this visual annotation
	 */
	public final int imageHeight;
	/**
	 * list of visual object
	 */
	protected Set<VisualObject> vObjects = null;
	
	/**
	 * Create a local visual annotation
	 * @param parentRecord
	 * @param vObjects
	 * @param name
	 * @param width
	 * @param height
	 */
	public BioVisualOverlay(VODimension id, String name, int width, int height, Collection<VisualObject> vObjects)
	{
		this(id, name, width, height);
		this.vObjects.addAll(vObjects);
	}
	
	public BioVisualOverlay(VODimension id, String name, int width, int height)
	{
		this.voID = id;
		this.name = name;
		this.imageWidth = width;
		this.imageHeight = height;
		this.vObjects = new HashSet<VisualObject>();
	}	
	
	/**
	 * Checks if this overlay is empty
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return vObjects == null || vObjects.isEmpty();
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public VODimension getDimension()
	{
		return voID;
	}


	@Override
	public int getWidth() 
	{
		return imageWidth;
	}

	@Override
	public int getHeight() 
	{
		return imageHeight;
	}

	@Override
	public Collection<VisualObject> getVisualObjects() 
	{
		return vObjects;
	}

	@Override
	public Collection<Ellipse> getEllipticalShapes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<LineSegment> getLineSegments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Rectangle> getRectangularShapes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TextBox> getTextBoxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<GeometricPath> getFreeHandShapes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getOverlayImage() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose()
	{
		vObjects = null;
	}

	public void addVisualObjects(Collection<VisualObject> vObjects) 
	{
		this.vObjects.addAll(vObjects);
	}

	public void deleteVisualObjects(Collection<VisualObject> vObjects) 
	{
		for(VisualObject vo : vObjects)
		{
			this.vObjects.remove(vo);
		}
	}
}
