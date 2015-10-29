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
