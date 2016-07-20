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

package com.strandgenomics.imaging.iserver.services.def.ispace;

/**
 * it represents an entity contains parameters to identify a subimage of a mosaic image
 * @author navneet
 *
 */
public class MosaicParameters {

	public MosaicParameters() {
	}
	
	/**
	 * x coordinate wrt resultant mosaic image
	 */
	private int X;
	
	/**
	 * y coordinate wrt resultant mosaic image
	 */
	private int Y;
	
	/**
	 * width of required sub image of resultant mosaic image
	 */
	private int width;
	
	/**
	 * height of required sub image of resultant mosaic image
	 */
	private int height;
	
	/**
	 * width to which subimage has to be resized
	 */
	private int tileWidth;
	
	/**
	 * height to which subimage has to be resized
	 */
	private int tileHeight;

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
	
}
