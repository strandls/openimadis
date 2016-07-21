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
import java.io.IOException;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ITile;
import com.strandgenomics.imaging.icore.image.PixelArray;

/**
 * A tile or rectangular block within a pixel data
 * @author arunabha
 */
public class Tile extends ImageSpaceObject implements ITile  {
	
	private static final long serialVersionUID = 1700056176903051913L;
	/**
	 *  the X coordinate of the upper-left corner of the specified rectangular region
	 */
    public final int x;
    /**
     * the Y coordinate of the upper-left corner of the specified rectangular region
     */
    public final int y;
    /**
     * the width of the specified rectangular region
     */
    public final int width;
    /**
     * the height of the specified rectangular region
     */
    public final int height;
	/**
	 * the parent record
	 */
	protected Record parentRecord;
	/**
	 * record dimensions that uniquely identify the source pixel-data
	 */
	protected Dimension imageCoordinate;
	
	/**
	 * Creates a tile object representing a rectangular area within the pixel-data
	 * @param parentRecord parent record
	 * @param imageCoordinate image dimensions (t,z,c,site)
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
	 */
	Tile(Record parentRecord, Dimension imageCoordinate, int x,int y, int w, int h)
	{
		this.parentRecord = parentRecord;
		this.imageCoordinate = imageCoordinate;
		
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	@Override
	public PixelArray getRawData() throws IOException
	{
		return getImageSpace().getRawDataForTile(parentRecord.getGUID(), this);
	}

	@Override
	public BufferedImage getImage(boolean useChannelColor)  throws IOException
	{
		return getImageSpace().getTileImage(parentRecord.getGUID(), this, useChannelColor);
	}
	
	@Override
	public Dimension getDimension() 
	{
		return imageCoordinate;
	}

	@Override
	public int getX() 
	{
		return x;
	}

	@Override
	public int getY() 
	{
		return y;
	}

	@Override
	public int getImageWidth() 
	{
		return width;
	}

	@Override
	public int getImageHeight() 
	{
		return height;
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	}
}
