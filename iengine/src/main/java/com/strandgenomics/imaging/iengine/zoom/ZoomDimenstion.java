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

import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Storable;

/**
 * identifies every zoom request uniquely
 * @author Anup Kulkarni
 *
 */
public class ZoomDimenstion implements Storable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8593213127007149767L;
	/**
	 * record id
	 */
	public final long guid;
	/**
	 * selected frame (T)
	 */
	public final int frame;
	/**
	 * selected slice (Z)
	 */
	public final int slice;
	/**
	 * selected site
	 */
	public final int site;
	/**
	 * selected channels
	 */
	private List<Integer> channels;
	/**
	 * channel objects
	 */
	private List<Channel> actualChannels;
	/**
	 * selected overlays
	 */
	private List<String> visualOverlays;
	/**
	 * original image width
	 */
	public final int imageWidth;
	/**
	 * original image height
	 */
	public final int imageHeight;
	/**
	 * use channel color
	 */
	public final boolean useChannelColor;
	/**
	 * zoom tile width
	 */
	public static final int TILE_WIDTH = 512;
	/**
	 * zoom tile height
	 */
	public static final int TILE_HEIGHT = 512;
	
	public ZoomDimenstion(long guid, int frame, int slice, int site, List<Integer> channels, List<Channel>actualChannels, List<String> overlays, boolean useChannelColor, int imageWidth, int imageHeight)
	{
		this.guid = guid;
		this.frame = frame;
		this.slice = slice;
		this.channels = channels;
		this.actualChannels = actualChannels;
		this.site = site;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		this.useChannelColor = useChannelColor;
		this.visualOverlays = overlays;
		
		if(visualOverlays == null)
		{
			visualOverlays = new ArrayList<String>();
		}
	}
	
	public List<String> getVisualOverlays()
	{
		return this.visualOverlays;
	}
	
	public List<Integer> getChannels()
	{
		return this.channels;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof ZoomDimenstion)
		{
			ZoomDimenstion that = (ZoomDimenstion) obj;
			
			return (this.frame == that.frame &&
					this.guid == that.guid &&
					this.slice == that.slice &&
					this.channels.size() == that.channels.size() && this.channels.containsAll(that.channels) &&
					this.visualOverlays.size() == that.visualOverlays.size() && this.visualOverlays.containsAll(that.visualOverlays) &&
					this.site == that.site &&
					this.useChannelColor == that.useChannelColor);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("guid=");
		sb.append(this.guid);
		sb.append(";frame=");
		sb.append(this.frame);
		sb.append(";slice=");
		sb.append(this.slice);
		sb.append(";channels=");
		for(Channel channel:this.actualChannels)
		{
			sb.append(channel.getName());
			sb.append(",");
			sb.append(channel.getLut());
			sb.append(",");
			sb.append(channel.getContrast(false).getGamma());
			sb.append(",");
			sb.append(channel.getContrast(false).getMaxIntensity());
			sb.append(",");
			sb.append(channel.getContrast(false).getMinIntensity());
			sb.append(",");
		}
		sb.append(";overlays=");
		for(String ovName: this.visualOverlays)
		{
			sb.append(ovName);
			sb.append(",");
		}
		sb.append(";useChannelColor");
		sb.append(this.useChannelColor);
		sb.append(";site=");
		sb.append(this.site);
		
		return sb.toString();
	}

	@Override
	public void dispose()
	{ }
}
