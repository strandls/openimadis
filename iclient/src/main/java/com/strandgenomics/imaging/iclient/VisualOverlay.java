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
package com.strandgenomics.imaging.iclient;

import java.awt.image.BufferedImage;
import java.util.Collection;

import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * Remote visual overlays
 * @author arunabha
 */
public class VisualOverlay extends ImageSpaceObject implements IVisualOverlay {

	private static final long serialVersionUID = 4425076711374824778L;
	/**
	 * ID of the visual annotation
	 */
	public final VODimension voID;
	/**
	 * name of this visual overlay
	 */
	public final String name ;
	/**
	 * Width of this visual annotation
	 */
	public final int imageWidth;
	/**
	 * Height of this visual annotation
	 */
	public final int imageHeight;
	/**
	 * the parent record's guid
	 */
	protected long guid;
	
	VisualOverlay(long parentRecord, VODimension id, String name, int width, int height)
	{
		this.guid = parentRecord;
		this.voID = id;
		this.name = name;
		this.imageWidth = width;
		this.imageHeight = height;
	}
	
	/**
	 * every visual overlays has an associated name
	 * @return name of this overlay
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the width of the visual Overlay
	 * @return the width of the visual Overlay
	 */
	public int getWidth()
	{
		return imageWidth;
	}
	
	/**
	 * Returns the height of the visual Overlay
	 * @return the height of the visual Overlay
	 */
	public int getHeight()
	{
		return imageHeight;
	}

	@Override
	public Collection<VisualObject> getVisualObjects() 
	{
		//makes a system call to get it done
		return getImageSpace().getVisualObjects(guid, this);
	}
	
	@Override
	public Collection<Ellipse> getEllipticalShapes() 
	{
		//makes a system call to get it done
		return getImageSpace().getEllipticalShapes(guid, this);
	}
	
	@Override
	public Collection<LineSegment> getLineSegments() 
	{
		//makes a system call to get it done
		return getImageSpace().getLineSegments(guid, this);
	}
	
	@Override
	public Collection<Rectangle> getRectangularShapes() 
	{
		//makes a system call to get it done
		return getImageSpace().getRectangularShapes(guid, this);
	}
	
	@Override
	public Collection<TextBox> getTextBoxes() 
	{
		//makes a system call to get it done
		return getImageSpace().getTextBoxes(guid, this);
	}
	
	@Override
	public Collection<GeometricPath> getFreeHandShapes() 
	{
		//makes a system call to get it done
		return getImageSpace().getFreeHandShapes(guid, this);
	}
	
	@Override
	public BufferedImage getOverlayImage() 
	{
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public VODimension getDimension() 
	{
		return voID;
	}
}
