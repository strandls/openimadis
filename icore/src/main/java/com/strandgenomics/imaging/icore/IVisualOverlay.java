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
package com.strandgenomics.imaging.icore;

import java.awt.image.BufferedImage;
import java.util.Collection;

import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;


/**
 * Visual Object are a collection of visual objects (shapes/graphic-paths)
 * @author arunabha
 */
public interface IVisualOverlay extends Disposable {
	
	/**
	 * Name of this overlay
	 * @return name of this overlay
	 */
	public String getName();
	
	/**
	 * Returns the image coordinate associated with this visual overlay
	 * @return the image coordinate associated with this visual overlay
	 */
	public VODimension getDimension();
	
	/**
	 * Returns the width of the visual Overlay
	 * @return the width of the visual Overlay
	 */
	public int getWidth();
	
	/**
	 * Returns the height of the visual Overlay
	 * @return the height of the visual Overlay
	 */
	public int getHeight();
	
    /**
     * Returns the list of visual Objects associated with this visual overlay
     * @return list of visual Objects associated with this visual overlay
     */
    public Collection<VisualObject> getVisualObjects();
    
    /**
     * Returns the list of elliptical shapes associated with this visual overlay
     * @return list of visual Objects associated with this visual overlay
     */
    public Collection<Ellipse> getEllipticalShapes();

    /**
     * Returns the list of line segments associated with this visual overlay
     * @return list of visual Objects associated with this visual overlay
     */
    public Collection<LineSegment> getLineSegments();

    /**
     * Returns the list of rectangular shapes associated with this visual overlay
     * @return list of visual Objects associated with this visual overlay
     */
    public Collection<Rectangle> getRectangularShapes();

    /**
     * Returns the list of TextBoxes associated with this visual overlay
     * @return list of visual Objects associated with this visual overlay
     */
    public Collection<TextBox> getTextBoxes();

    /**
     * Returns the list of free-hand-drawn objects associated with this visual overlay
     * @return list of visual Objects associated with this visual overlay
     */
    public Collection<GeometricPath> getFreeHandShapes();
    
    /**
     * Returns a transparent image with visual objects drawn (opaque) on it
     * @return a transparent image with visual objects drawn on it
     */
    public BufferedImage getOverlayImage();
}
