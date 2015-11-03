package com.strandgenomics.imaging.iclient;

import java.util.List;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * RecordBuilder is notional record under construction. Record can be built by
 * providing initial record metadata and adding image data one by one.
 * 
 * @author Anup Kulkarni
 */
public class RecordBuilder extends ImageSpaceObject {
	/**
	 * unique identifier for record builder
	 */
	protected long builderId;
	/**
	 * parent project
	 */
	protected Project parentProject;
	/**
	 * Total number of frames, default is 1 
	 */
	protected final int noOfFrames;
	/**
	 * total number of slices (z-size), default is 1 
	 */
	protected final int noOfSlices;
	/**
	 * total number of channels, default is 1 
	 */
	protected final int noOfChannels;
	/**
	 * List of available sites
	 */
	protected final int noOfSites;
	/**
	 * Width of a pixel-data (image) within this record
	 */
	protected final int imageWidth;
	/**
	 * Height of a pixel-data (image) within this record
	 */
	protected final int imageHeight;
	/**
	 * List of available channels
	 */
	protected List<Channel> channels;
	/**
	 * List of available sites
	 */
	protected List<Site> sites;
	/**
	 * pixel depth for pixel-data (image)
	 */
	protected final PixelDepth pixelDepth;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected final double pixelSizeAlongXAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected final double pixelSizeAlongYAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * for z-axis movement, each slice move by this physical value
	 * value is in microns
	 */
	protected final double pixelSizeAlongZAxis;
	/**
	 * type of the image, default is gray-scale
	 */
	protected final ImageType imageType;
	/**
	 * file format of the original record files
	 */
	protected SourceFormat sourceFormat;

	/**
	 * record under construction
	 * @param parentProject parent project
	 * @param noOfFrames max number of frames (T)
	 * @param noOfSlices max number of slices (Z)
	 * @param channels channels
	 * @param sites sites 
	 * @param imageWidth width of every image in record under construction
	 * @param imageHeight height of every image in record under construction
	 * @param pixelDepth depth of pixel 
	 * @param pixelSizeX x-size (in micron) of the pixel
	 * @param pixelSizeY y-size (in micron) of the pixel
	 * @param pixelSizeZ z-size (in micron) of the pixel
	 * @param imageType type of the image, by default it is Gray-scale
	 * @param sourceFormat file format of the original source files
	 */
	RecordBuilder(long builderId, Project parentProject, int noOfFrames, int noOfSlices, List<Channel> channels,
			List<Site> sites, int imageWidth, int imageHeight,
			PixelDepth pixelDepth, double pixelSizeX, double pixelSizeY,
			double pixelSizeZ, ImageType imageType, SourceFormat sourceFormat) 
	{
		this.builderId = builderId;
		this.parentProject = parentProject;
		this.noOfFrames = noOfFrames;
		this.noOfSlices = noOfSlices;
		this.noOfChannels = channels.size();
		this.noOfSites = sites.size();
		
		this.channels = channels;
		this.sites = sites;
		
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		
		this.pixelDepth = pixelDepth;
		
		this.pixelSizeAlongXAxis = pixelSizeX;
		this.pixelSizeAlongYAxis = pixelSizeY;
		this.pixelSizeAlongZAxis = pixelSizeZ;
		
		this.sourceFormat = sourceFormat;
		this.imageType = imageType;
	}
	
	/**
	 * terminates the record building process; removes all the data uploaded so far.
	 * method has no effect if commit is called @see #commit() 
	 */
	public void abort()
	{
		getImageSpace().abortRecordBuilder(builderId);
	}
	
	/**
	 * Commits the record builder to produce Record object.
	 * This call has to made to finish record building process. 
	 * @return the record 
	 */
	public Record commit()
	{
		getImageSpace().commitRecordBuilder(builderId);
		return getImageSpace().findRecordForGUID(builderId);
	}
	
	/**
	 * adds specified pixeldata to specified dimension of the record under construction along with specified meta data
	 * @param dim specified image dimension
	 * @param pixelData specified image data
	 * @param imageMetadata specified meta data
	 * @return true if successful; false otherwise 
	 */
	public boolean addImageData(Dimension dim, PixelArray rawData, PixelMetaData pixelData)
	{
		return getImageSpace().addImageData(builderId, dim, rawData, pixelData);
	}
	
	/**
	 * returns the list of dimensions which does not have image data
	 * @return the list of dimensions which does not have image data
	 */
	public List<Dimension> getRemainingImages()
	{
		// TODO:
		return null;
	}

	@Override
	public void dispose()
	{ }

}
