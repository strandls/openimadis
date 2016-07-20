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
