package com.strandgenomics.imaging.iengine.zoom;

import com.strandgenomics.imaging.icore.Storable;

/**
 * server side object for identifying zoom request
 * 
 * @author Anup Kulkarni
 */
public class ZoomIdentifier implements Storable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7319210827636624736L;
	/**
	 * unqiue zoom identifier
	 */
	public final long zoomID;
	/**
	 * the zoom dimensions
	 */
	private ZoomDimenstion zoomDim;
	/**
	 * the last access time for this zoom request 
	 */
	private long lastAccessTime;
	
	public ZoomIdentifier(long zoomID, ZoomDimenstion zoomDim)
	{
		this.zoomDim = zoomDim;
		this.zoomID = zoomID;
	}
	
	public ZoomDimenstion getZoomDimension()
	{
		return this.zoomDim;
	}
	
	public void setLastAccessTime(long lastAccessTime)
	{
		this.lastAccessTime = lastAccessTime;
	}

	public long getLastAccessTime()
	{
		return lastAccessTime;
	}
	
	@Override
	public void dispose()
	{ }
}
