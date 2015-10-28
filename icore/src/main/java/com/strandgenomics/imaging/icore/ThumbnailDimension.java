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
