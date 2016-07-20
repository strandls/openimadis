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

package com.strandgenomics.imaging.icore;

/**
 * Dimensions that identify a configuration of image(s) and used to generate thumbnail
 * 
 * @author Anup Kulkarni
 */
public class ThumbnailDimension extends VODimension {

	/**
	 * list of channels
	 */
	private int[] channelNos;
	/**
	 * true if channels are tiled; false if overlayed
	 */
	private boolean mosaic;
	/**
	 * true if images are overlayed across slices; false if single slice
	 */
	private boolean zStack;
	/**
	 * true if channel colors are used; false in case of gray scale
	 */
	private boolean useChannelColor;

	public ThumbnailDimension(int frameNumber, int sliceNumber, int siteNo, int[] channelNos, boolean mosaic, boolean zStack, boolean useChannelColor) {
		super(frameNumber, sliceNumber, siteNo);
		
		this.channelNos = channelNos;
		this.mosaic = mosaic;
		this.zStack = zStack;
		this.useChannelColor = useChannelColor;
	}

	public int[] getChannelNos()
	{
		return channelNos;
	}

	public boolean isMosaic()
	{
		return mosaic;
	}

	public boolean isZStack()
	{
		return zStack;
	}

	public boolean isUseChannelColor()
	{
		return useChannelColor;
	}

}
