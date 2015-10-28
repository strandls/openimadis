package com.strandgenomics.imaging.iengine.zoom;

import java.io.File;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.Storable;

/**
 * identifies unique zoom request
 * @author Anup Kulkarni
 *
 */
public class ZoomRequest implements Storable{
	
	/**
	 * request identifier
	 */
	public final long requestID;
	/**
	 * dimension used for zoom
	 */
	private ZoomDimenstion zDim;
	/**
	 * zoom level
	 */
	public final int zoomLevel;
	/**
	 * 
	 */
	private boolean tileDone[][];
	private boolean originalFileRead = false;
	/**
	 * root for storing prefetched images
	 */
	private File zoomStorageRoot = null;
	
	public static final String ORIGINAL_FILE_NAME = "full_res.jpg";
	
	public ZoomRequest(long requestID, ZoomDimenstion zDim, int zoomLevel) 
	{
		this.requestID = requestID;
		this.zDim = zDim;
		this.zoomLevel = zoomLevel;
		
		tileDone = new boolean[getMaxTileX()][getMaxTileY()];
	}

	public ZoomDimenstion getZoomDimension()
	{
		return zDim;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(zDim.toString());
//		sb.append(";ZoomLevel=");
//		sb.append(zoomLevel);
		sb.append(requestID);
		
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof ZoomRequest)
		{
			ZoomRequest that = (ZoomRequest) obj;
			
//			return (this.zDim.equals(that.zDim) && this.zoomLevel == that.zoomLevel);
			return this.requestID == that.requestID;
		}
		
		return false;
	}

	public File getStorageRoot()
	{
		File f = new File(Constants.getStringProperty(Property.ZOOM_STORAGE_LOCATION, "/home/anup/Desktop/zoom_storage"));
		if(!f.isDirectory())
			f.mkdir();
		
		zoomStorageRoot = new File(f, String.valueOf(requestID));
		if(!zoomStorageRoot.exists())
			zoomStorageRoot.mkdirs();
		
		return zoomStorageRoot;
	}
	
	synchronized boolean isDone()
	{
		for(int i=0;i<getMaxTileX();i++)
		{
			for(int j=0;j<getMaxTileY();j++)
			{
				if(!isDone(i, j))
					return false;
			}
		}
		return true;
	}
	
	synchronized boolean isDone(int xTile,int yTile)
	{
		return tileDone[xTile][yTile];
	}
	
	synchronized void setDone(int xTile, int yTile)
	{
		tileDone[xTile][yTile] = true;
	}
	
	synchronized void resetDone(int xTile, int yTile)
	{
		tileDone[xTile][yTile] = false;
	}
	
	int getMaxTileX()
	{
		int imageWidth = zDim.imageWidth;
		
		int maxTileX = (int) Math.ceil((1.0 * imageWidth)/(ZoomDimenstion.TILE_WIDTH * (int)Math.pow(2, zoomLevel)));
		return maxTileX;
	}
	
	int getMaxTileY()
	{
		int imageHeight = zDim.imageHeight;
		
		int maxTileY = (int) Math.ceil((1.0 * imageHeight)/(ZoomDimenstion.TILE_HEIGHT * (int)Math.pow(2, zoomLevel)));
		return maxTileY;
	}

	@Override
	public void dispose()
	{ }

	synchronized void setOriginalFileDone(boolean flag)
	{
		this.originalFileRead = flag;
	}
	
	synchronized boolean getOriginalFileDone()
	{
		return this.originalFileRead;
	}
}
