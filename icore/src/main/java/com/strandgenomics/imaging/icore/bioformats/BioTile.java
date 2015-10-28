package com.strandgenomics.imaging.icore.bioformats;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ITile;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelArray;

public class BioTile implements ITile,Serializable {
	
	private static final long serialVersionUID = 8658005485045345347L;
	
	/**
	 * owning record
	 */
	protected BioRecord parentRecord;
	/**
	 * record dimensions that uniquely identify the parent pixel-data
	 */
	protected Dimension imageID;
	/**
	 * the rectangular bounds of this tile
	 */
	protected Rectangle bounds;
	
	/**
	 * Creates a tile with the specified bounds for the given image-coordinate of the given record
	 * @param parentRecord the record under consideration
	 * @param imageID the coordinate of  the specified image
	 * @param bounds the bounds within the image
	 */
	public BioTile(BioRecord parentRecord, Dimension imageID, Rectangle bounds)
	{
		this.parentRecord = parentRecord;
		this.imageID = imageID;
		this.bounds = bounds;
	}

	public IRecord getContainingRecord() 
	{
		return parentRecord;
	}

	@Override
	public int getImageWidth() 
	{
		return bounds.width;
	}

	@Override
	public int getImageHeight() 
	{
		return bounds.height;
	}
	
	@Override
	public int getX() 
	{
		return bounds.x;
	}

	@Override
	public int getY()
	{
		return bounds.y;
	}

	@Override
	public PixelArray getRawData() throws IOException 
	{
		IPixelData pData = parentRecord.getPixelData(imageID);
		PixelArray fullData = pData.getRawData();
		
		return fullData.getSubArray(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public BufferedImage getImage(boolean useChannelColor) throws IOException 
	{
		PixelArray rawData = getRawData();
		
		String channelLUT = parentRecord.getChannel(imageID.channelNo).getLut();
		rawData.setColorModel(LutLoader.getInstance().getLUT(channelLUT));
		
		VisualContrast contrast = parentRecord.getChannel(imageID.channelNo).getContrast(false);
		if(contrast != null) 
		{
			rawData.setContrast(contrast.getMinIntensity(), contrast.getMaxIntensity());
			rawData.setGamma(contrast.getGamma());
		}
		
		return rawData.createImage();	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension getDimension()
	{
		return imageID;
	}
}
