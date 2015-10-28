package com.strandgenomics.imaging.iengine.models;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ITile;
import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.image.PixelArray;

/**
 * Representation of ImageTile on server side. This will be used for caching the raw data on server. 
 * 
 * @author Anup Kulkarni
 */
public class PixelArrayTile implements ITile, Storable{
	/**
	 * record id of the parent record
	 */
	public final long guid;
	/**
	 * dimension of the image this tile is part of
	 */
	private Dimension imageCordinate;
	/**
	 * dimension of the tile
	 */
	private Rectangle tile;
	/**
	 * location of the pixel array for the tile
	 */
	private String pixelArrayPath;
	/**
	 * raw data for the pixel array
	 */
	private PixelArray rawData = null;
	
	public PixelArrayTile(long guid, Dimension dim, Rectangle tile, String path)
	{
		this.guid = guid;
		this.imageCordinate = dim;
		this.tile = tile;
		this.pixelArrayPath = path;
	}
	
	public String getPath()
	{
		return this.pixelArrayPath;
	}
	
	@Override
	public Dimension getDimension()
	{
		return this.imageCordinate;
	}

	@Override
	public int getX()
	{
		return tile.x;
	}

	@Override
	public int getY()
	{
		return tile.y;
	}

	@Override
	public int getImageWidth()
	{
		return tile.width;
	}

	@Override
	public int getImageHeight()
	{
		return tile.height;
	}
	
	public int getEndX()
	{
		return tile.x + tile.width;
	}
	
	public int getEndY()
	{
		return tile.y + tile.height;
	}

	@Override
	public BufferedImage getImage(boolean useChannelColor) throws IOException
	{
		PixelArray data = getRawData();
		return data.createImage(); // this does not take into account user preference of LUTs and contrast
	}
	
	@Override
	public PixelArray getRawData() throws IOException
	{
		if(this.rawData != null)
			return this.rawData;
		
		String cacheStoreDir = Constants.getStringProperty(Property.IMAGE_CACHE_STORAGE_LOCATION, null);

		File recordCache = new File(cacheStoreDir, "RecordID_"+guid);
		File data = new File(recordCache, this.pixelArrayPath);
		
		ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(data)));
		try
		{
			this.rawData = (PixelArray) ois.readObject();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally {
			ois.close();
		}
		
		return this.rawData;
	}

	@Override
	public void dispose()
	{
		rawData = null;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("guid=");
		sb.append(guid);
		sb.append(",");
		sb.append("dim=");
		sb.append(imageCordinate.toString());
		sb.append(",");
		sb.append("tile=");
		sb.append(tile.toString());
		sb.append(",");
		sb.append("path=");
		sb.append(pixelArrayPath);
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof PixelArrayTile)
		{
    		PixelArrayTile that = (PixelArrayTile) obj;
			if(this == that) return true;
			
			boolean equals = (this.guid == that.guid);
			equals = equals && (this.imageCordinate == that.imageCordinate);
			equals = equals && (this.tile.x == that.tile.x && this.tile.y == that.tile.y && this.tile.width == that.tile.width && this.tile.height == that.tile.height);
			
			return equals;
		}
		return false;
	}
}
