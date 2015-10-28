package com.strandgenomics.imaging.iengine.bioformats;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.image.PixelDepth;

public class ClientRecord extends BioRecord {

	private static final long serialVersionUID = 4646332586621142786L;
	
	/**
	 * the java logger
	 */
	protected transient Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.bioformats");

	public ClientRecord(BioExperiment expt,
			Date sourceFileTime, Date acquiredDate,
			int noOfFrames, int noOfSlices, List<Channel> channels, List<Site> sites,
			int imageWidth, int imageHeight, PixelDepth pixelDepth,
			double pixelSizeX, double pixelSizeY, double pixelSizeZ,
			ImageType imageType, SourceFormat sourceFormat)
	{
		super(expt, sourceFileTime, acquiredDate, 
				noOfFrames, noOfSlices, channels, sites,
				imageWidth, imageHeight, pixelDepth,
				pixelSizeX, pixelSizeY, pixelSizeZ,
				imageType, sourceFormat);
		
		logger.logp(Level.INFO, "ClientRecord", "init", "created record for experiment "+this);
	}
	
	@Override
	public synchronized BufferedImage getThumbnail() 
	{
		logger.logp(Level.INFO, "ClientRecord", "getThumbnail", "creating thunbnail for "+this);
		return super.getThumbnail();
	}
	
	public List<Channel> getChannels()
	{
		return channels;
	}
	
	public void setChannels(List<Channel> customChannels)
	{
		this.channels = customChannels;
	}
}
