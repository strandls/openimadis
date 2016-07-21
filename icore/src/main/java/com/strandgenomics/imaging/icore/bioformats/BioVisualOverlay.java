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
