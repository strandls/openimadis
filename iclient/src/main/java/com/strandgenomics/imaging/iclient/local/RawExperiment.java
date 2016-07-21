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

package com.strandgenomics.imaging.iclient.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Group of records (series) extracted from a set of source files
 * @author arunabha
 */
public class RawExperiment extends BioExperiment {
	
	private static final long serialVersionUID = -7681001080592974735L;
	
	/**
	 * MAC address of the machine
	 */
	protected String macAddress = null;
	
	/**
	 * IP address of the machine
	 */
	protected String ipAddress = null;
	
	/**
	 * status of upload
	 */
	public UploadStatus status = UploadStatus.NotUploaded;

	/**
	 * Creates a series with the specified source file 
	 * @param sourceFile the source file with possibly many companion files
	 */
	public RawExperiment(File sourceFile)
	{
		super(sourceFile);
	}
	
    @Override
	public BioRecord createRecordObject(Date sourceFileTime, Date acquiredDate, 
			int noOfFrames, int noOfSlices, List<Channel> channels, List<Site> sites,
			int imageWidth, int imageHeight, PixelDepth pixelDepth, 
			double pixelSizeX, double pixelSizeY, double pixelSizeZ,
			ImageType imageType, SourceFormat sourceFormat)
	{
		return new RawRecord(this, sourceFileTime, acquiredDate, 
				noOfFrames, noOfSlices, channels, sites,
				imageWidth, imageHeight, pixelDepth, 
				pixelSizeX, pixelSizeY, pixelSizeZ,
				imageType, sourceFormat);
	}
    
    /**
     * Checks if the archive already exists on the server
     * @return true if the archive already exists on the server, false otherwise
     */
    public boolean isExistOnServer()
    {
    	return ImageSpaceObject.getImageSpace().isArchiveExist(getMD5Signature());
    }

	@Override
	protected void updateReference(String[] usedFiles) 
	{
		for(String name : usedFiles)
		{
			try
			{
				String lowerCaseName = name.toLowerCase();
				if(!lowerCaseName.endsWith("tif") && !lowerCaseName.endsWith("tiff"))
				{
					seedFile = new File(name).getAbsoluteFile();
				}
			}
			catch(Exception e)
			{}
			
			sourceReferences.add( new RawSourceReference(new File(name).getAbsoluteFile()));
		}
	}
	
	@Override
	public String getSourceFilename()
	{
		return getSeedFile().getName();
	}
	
	@Override
	public String getRootDirectory(){
		
		return getSeedFile().getParent().toString();
	}
	
	@Override
	public String getOriginMachineAddress() 
	{
		if(macAddress == null)
		{
			String address = Util.getMachineAddress();
			macAddress = address == null ? "NA" : address;
		}
		return macAddress;
	}

	@Override
	public String getOriginMachineIP() 
	{
		if(ipAddress == null)
		{
			String address = Util.getMachineIP();
			ipAddress = address == null ? "NA" : address;
		}
		return ipAddress;
	}
	
	public UploadStatus getUploadStatus()
	{
		return status;
	}
	
	public void setUploadStatus(UploadStatus status)
	{
		this.status = status;
	}
}
