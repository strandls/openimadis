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
