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
