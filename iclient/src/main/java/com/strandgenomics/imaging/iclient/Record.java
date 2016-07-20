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

/*
 * Record.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iclient;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * Remote representation of a record stored in the server
 * @author arunabha
 */
public class Record extends ImageSpaceObject implements IRecord {
	
	private static final long serialVersionUID = -7967441664538415437L;
	/**
	 * actual date of the acquisition
	 */
	protected Date acquiredDate;
	/**
	 * record creation time in the acquisition machine 
	 */
	protected Date creationTime = null;
	/**
	 * last modification time on the source file
	 */
	protected Date sourceFileTime = null;
	/**
	 * checks if the login user has write permission on this record
	 */
	protected boolean isWritable = false;
	/**
	 * the unique signature is associated with this record
	 */
	protected Signature signature = null;
	/**
	 * MAC address of the machine from which the record was uploaded (acquisition machine )
	 */
	protected String macAddress = null;
	/**
	 * IP address of the machine from which the record was uploaded (acquisition machine )
	 */
	protected String ipAddress = null;
	/**
	 * pixel depth for pixel-data (image)
	 */
	protected final PixelDepth pixelDepth;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected double pixelSizeAlongXAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * value is in microns
	 */
	protected double pixelSizeAlongYAxis;
	/**
	 * pixel are dots of fixed size on a screen/image - 
	 * they are mapped to actual physical dimension based on the resolution of the microscope
	 * for z-axis movement, each slice move by this physical value
	 * value is in microns
	 */
	protected double pixelSizeAlongZAxis;
	/**
	 * type of the image, default is gray-scale
	 */
	protected ImageType imageType;
	/**
	 * file format of the original record files
	 */
	protected SourceFormat sourceFormat;
	/**
	 * List of available channels
	 */
	protected List<Channel> channels = null;
	/**
	 * List of available cites
	 */
	protected List<Site> sites = null;
	/**
	 * Root Directory
	 */
	protected String rootDirectory = null;
	/**
	 * name of one of the files (seed file while creating records) responsible for this record
	 */
	protected String sourceFilename;
	/**
	 * Every Record is associated with a specific Project
	 */
	protected Project parentProject = null;
	/**
	 * who uploaded this record
	 */
	protected String uploadedBy;
	/**
	 * when this record was uploaded
	 */
	protected Date uploadTime;
	/**
	 * The Containing experiment 
	 */
	protected Experiment experiment;
	/**
	 * The global unique identifier of this record
	 */
	private long guid = -1;
	
	/**
	 * Constructs a record from the data fetched from the underlying system
	 * @param parent owning project
	 * @param uploadedBy user who had uploaded this record
	 * @param uploadTime time of upload
	 * @param isWritable checks whether the current login user has write permission
	 * @param signature unique signature associated with this record
	 * @param macAddress MAC address of the acquisition machine
	 * @param ipAddress IP address of the acquisition machine
	 * @param creationTime acquisition time
	 * @param seriesNumber the series number of this record if any, otherwise zero
	 * @param noOfFrames number of frames within this record
	 * @param noOfSlices number of slices (z) within this record
	 * @param noOfChannels number of channels within this record
	 * @param noOfSites number of sites within this record
	 * @param imageWidth width of a pixel-data (image) within this record
	 * @param imageHeight height of a pixel-data (image) within this record
	 * @param pixelDepth depth of a pixel in the pixel-data
	 * @param pixelSizeX x-size (in micron) of the pixel
	 * @param pixelSizeY y-size (in micron) of the pixel
	 * @param imageType type of the image, by default it is Gray-scale
	 * @param sourceFormat file format of the original source files
	 */
	//package protected, to be used locally
	Record( String uploadedBy, Date uploadTime, Date creationTime, Date acquiredDate, Date sourceFileTime,
			boolean isWritable, String macAddress, String ipAddress, 
			Signature signature, PixelDepth pixelDepth,
			double pixelSizeX, double pixelSizeY, double pixelSizeZ,
			ImageType imageType, SourceFormat sourceFormat,
			String srcFolder, String srcFilename)
	{
		this.parentProject = null;
		this.experiment = new Experiment(signature.archiveHash);
		this.uploadedBy = uploadedBy;
		
		this.uploadTime = uploadTime;
		this.creationTime = creationTime;
		this.acquiredDate = acquiredDate;
		this.sourceFileTime = sourceFileTime;

		this.isWritable = isWritable;
		this.macAddress = macAddress;
		this.ipAddress = ipAddress;
		this.pixelDepth = pixelDepth;
		
		this.signature = signature;
		
		this.pixelSizeAlongXAxis = pixelSizeX;
		this.pixelSizeAlongYAxis = pixelSizeY;
		this.pixelSizeAlongZAxis = pixelSizeZ;
		
		this.imageType = imageType;
		this.sourceFormat = sourceFormat;
		
		this.rootDirectory  = srcFolder;
		this.sourceFilename = srcFilename;
	}
	
	/**
	 * get the record id
	 * @return the record id
	 */
	public synchronized long getGUID()
	{
		if(guid == -1)
		{
			guid = getImageSpace().findGUID(signature);
		}
		return guid;
	}
	
	/**
	 * get the channels
	 * @return
	 */
	synchronized List<Channel> getChannels()
	{
		if(channels == null)
		{
			channels = getImageSpace().getRecordChannels(getGUID());
		}
		
		return channels;
	}
	
	/**
	 * get the sites
	 * @return
	 */
	synchronized List<Site> getSites()
	{
		if(sites == null)
		{
			sites = getImageSpace().getRecordSites(getGUID());
		}
		
		return sites;
	}
	
	@Override
	public Signature getSignature()
	{
		return signature;
	}
	
	@Override
    public Date getUploadTime()
    {
    	return uploadTime;
    }
	
	@Override
    public Date getSourceFileTime()
    {
    	return sourceFileTime;
    }
	
	@Override
	public Date getCreationTime()
	{
		return creationTime;
	}
	
	@Override
	public Date getAcquiredDate()
	{
		return acquiredDate;
	}
	
	/**
     * Returns true iff the logged in user has write permission to this record
     * Write permissions controls ones ability to add/edit user annotations and add/remove attachments 
     */
    public boolean isWritable()
    {
    	return isWritable;
    }

	@Override
	public String getOriginMachineAddress() 
	{
		return macAddress;
	}

	@Override
	public String getOriginMachineIP() 
	{
		return ipAddress;
	}
	
	@Override
	public int getSliceCount()
	{
		return signature.noOfSlices;
	}

	@Override
	public int getFrameCount() 
	{
		return signature.noOfFrames;
	}

	@Override
	public int getChannelCount() 
	{
		return signature.noOfChannels;
	}
	
	@Override
	public IChannel getChannel(int channelNo) 
	{
		return getChannels().get(channelNo);
	}
	
	@Override
	public int getSiteCount() 
	{
		return signature.noOfSites;
	}

	@Override
	public Site getSite(int siteNo) 
	{
		return getSites().get(siteNo);
	}

	@Override
	public int getImageWidth() 
	{
		return signature.imageWidth;
	}

	@Override
	public int getImageHeight() 
	{
		return signature.imageHeight;
	}

	@Override
	public int getImageCount() 
	{
		return signature.noOfFrames * signature.noOfSlices * signature.noOfChannels * signature.getNoOfSites();
	}

	@Override
	public Project getParentProject()
	{
		if(parentProject == null)
		{
			//makes a system call to get it done
			parentProject = getImageSpace().findProject(getGUID());
		}
		return parentProject;
	}
	
	@Override
	public IExperiment getExperiment() 
	{
		return experiment;
	}
	
    /**
     * Returns the id of the user who have uploaded thos record
     */
    public String uploadedBy()
    {
    	return uploadedBy;
    }

	/**
	 * Transfers this record to another project.Note that relevant permissions are needed 
	 * for this operation to succeed
	 * @param another the target project to transfer to
	 */
	public void transfer(Project another) throws PermissionException
	{
		//makes a system call to the underlying image management system
		getImageSpace().transfer(this.signature, another);
	}

	@Override
	public Map<String, Object> getDynamicMetaData() 
	{
		//makes a system call to get it done
		return getImageSpace().getDynamicMetaData(this.getGUID());
	}

	@Override
	public Map<String, Object> getUserAnnotations() 
	{
		//makes a system call to get it done
		return getImageSpace().getUserAnnotations(this.getGUID());
	}

	@Override
	public Collection<IAttachment> getAttachments() 
	{
		//makes a system call to get it done
		return getImageSpace().getRecordAttachments(this.getGUID());
	}

	@Override
	public void addAttachment(File attachmentFile, String notes)
	{
		//makes a system call to get it done
		getImageSpace().addRecordAttachments(this.getGUID(), attachmentFile, null, notes);
	}
	
	@Override
	public void addAttachment(File attachmentFile, String name, String notes)
	{
		//makes a system call to get it done
		getImageSpace().addRecordAttachments(this.getGUID(), attachmentFile, name, notes);
	}

	@Override
	public void addUserAnnotation(String name, long value)
	{
		//makes a system call to get it done
		getImageSpace().addRecordUserAnnotation(this.getGUID(), name, value);
	}
	
	@Override
	public void removeAttachment(String name)
	{
		 //TODO
	}

	@Override
	public void addUserAnnotation(String name, double value) 
	{
		//makes a system call to get it done
		getImageSpace().addRecordUserAnnotation(this.getGUID(), name, value);
	}

	@Override
	public void addUserAnnotation(String name, String value) 
	{
		//makes a system call to get it done
		getImageSpace().addRecordUserAnnotation(this.getGUID(), name, value);
	}
	
	@Override
	public void addUserAnnotation(String name, Date value) 
	{
		//makes a system call to get it done
		getImageSpace().addRecordUserAnnotation(this.getGUID(), name, value);
	}

	@Override
	public void addUserAnnotation(Map<String, Object> annotations) {
		//makes a system call to get it done
		getImageSpace().addRecordUserAnnotation(this.getGUID(), annotations);
	}
	
	@Override
    public Object removeUserAnnotation(String name)
    {
		return null;
    	//TODO
    }

	@Override
	public void setThumbnail(File customImage) 
	{
		//makes a system call to get it done
		getImageSpace().setRecordThumbnail(this.getGUID(), customImage);
	}

	@Override
	public BufferedImage getThumbnail() 
	{
		//makes a system call to get it done
		return getImageSpace().getRecordThumbnail(this.getGUID());
	}

	@Override
	public IPixelData getPixelData(Dimension imageCordinate)
	{
		return getImageSpace().getPixelDataForRecord(this, imageCordinate);
	}

	@Override
	public IPixelDataOverlay getOverlayedPixelData(int sliceNo, int frameNo, int siteNo, int[] channelNos) 
	{
		return new PixelDataOverlay(this, sliceNo, frameNo, siteNo, channelNos);
	}
	
	@Override
    public void setCustomContrast(boolean zStacked, int channelNo, VisualContrast contrast)
	{
		getImageSpace().setChannelContrast(this, channelNo, contrast);
	}
	
	@Override
	public void setChannelLUT(int channelNo, String lut)
	{
		getImageSpace().setChannelLUT(this, channelNo, lut);
	}
    
	@Override
    public VisualContrast getCustomContrast(boolean zStacked, int channelNo)
    {
		// TODO Auto-generated method stub
    	return null;
    }

	@Override
	public void dispose() 
	{}

	@Override
	public int getChannelIndex(IChannel channel) 
	{
		int noOfChannels = channels.size();
		
		for(int i = 0;i < noOfChannels; i++)
		{
			if(channels.get(i).equals(channel))
				return i;
		}
		 
		throw new ArrayIndexOutOfBoundsException("channel not found");
	}

	@Override
	public boolean isValidCoordinate(Dimension imageCoordinate) 
	{
		return imageCoordinate.frameNo < signature.noOfFrames &&
				imageCoordinate.sliceNo < signature.noOfSlices &&
				imageCoordinate.siteNo < signature.getNoOfSites() &&
				imageCoordinate.channelNo < signature.noOfChannels;
	}

	@Override
	public PixelDepth getPixelDepth() 
	{
		return pixelDepth;
	}

	@Override
	public double getPixelSizeAlongXAxis() 
	{
		return pixelSizeAlongXAxis;
	}

	@Override
	public double getPixelSizeAlongYAxis() 
	{
		return pixelSizeAlongYAxis;
	}

	@Override
	public double getPixelSizeAlongZAxis() 
	{
		return pixelSizeAlongZAxis;
	}

	@Override
	public ImageType getImageType() 
	{
		return imageType;
	}

	@Override
	public SourceFormat getSourceType() 
	{
		return sourceFormat;
	}

	@Override
	public String getSourceFilename() 
	{
		return sourceFilename;
	}

	@Override
	public String getRootDirectory() 
	{
		return rootDirectory;
	}
	
	@Override
	public List<ISourceReference> getSourceReference() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IVisualOverlay> getVisualOverlays(VODimension coordinate) 
	{
		return getImageSpace().getVisualOverlays(getGUID(), coordinate);
	}
	
	@Override
	public IVisualOverlay getVisualOverlay(VODimension coordinate, String name) 
	{
		return getImageSpace().getVisualOverlay(getGUID(), coordinate, name);
	}

	@Override
	public Set<String> getAvailableVisualOverlays(int siteNo)
	{
		return getImageSpace().getAvailableVisualOverlays(getGUID(), siteNo);
	}

	@Override
	public void createVisualOverlays(int siteNo, String name) 
	{
		getImageSpace().createVisualOverlays(getGUID(), siteNo, name);
	}

	@Override
	public void deleteVisualOverlays(int siteNo, String name) 
	{
		getImageSpace().deleteVisualOverlays(getGUID(), siteNo, name);
	}

	@Override
	public void addVisualObjects(List<VisualObject> vObjects, String name, VODimension... imageCoordinates) 
	{
		getImageSpace().addVisualObjects(getGUID(), vObjects, name, imageCoordinates);
	}

	@Override
	public void deleteVisualObjects(List<VisualObject> vObjects, String name, VODimension... imageCoordinates) 
	{
		getImageSpace().deleteVisualObjects(getGUID(), vObjects, name, imageCoordinates);
	}
	
	@Override
	public List<UserComment> getUserComments()
	{
		return getImageSpace().fetchUserComment(getGUID());
	}
	
	@Override
	public void addUserComments(String comment)
	{
		getImageSpace().addUserComment(getGUID(), comment);
	}
	
	public void addCustomHistory(String historyMessage)
	{
		getImageSpace().addCustomHistory(getGUID(), historyMessage);
	}
	
	public void applyAcquisitionProfile(AcquisitionProfile profile)
	{
		getImageSpace().setAcquisitionProfile(getGUID(), profile);
	}
	
	@Override
	public synchronized List<BufferedImage> getChannelOverlayImagesForSlices(int frameNo, int siteNo, int imageWidth, 
			boolean useChannelColor) throws IOException
	{
		//makes a system call to get it done
		InputStream iStream = getImageSpace().getChannelOverlaidImagesForSlices(this.getGUID(), 
				frameNo, siteNo, imageWidth, useChannelColor);
		
		File gzTarball = File.createTempFile(frameNo+"_slice_"+siteNo, ".tar.gz", Constants.TEMP_DIR);
		gzTarball.deleteOnExit();
		
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		
		File destFolder = null;
		File[] sliceFiles = null;
		List<BufferedImage> images = null;
				
		try
		{
			input = new BufferedInputStream(iStream);
			output = new BufferedOutputStream(new FileOutputStream(gzTarball));
			long dataLength = Util.transferData(input, output);
			
			Util.closeStreams(input, output);
			
			System.out.println("successfully downloaded "+dataLength);
			
			destFolder = new File( Constants.TEMP_DIR, "slices_"+System.currentTimeMillis());
			destFolder.mkdir();
			
			Archiver.unTar(destFolder, gzTarball, true);
			sliceFiles = destFolder.listFiles();
			
			images = readBufferedImages(sliceFiles);
		}
		finally
		{
			gzTarball.delete();
			for(File f : sliceFiles)
			{
				if(f != null) f.delete();
			}
			destFolder.delete();
		}
		
		return images;
	}

	private List<BufferedImage> readBufferedImages(File[] sliceFiles) throws IOException 
	{
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		for(File f : sliceFiles)
		{
			images.add( ImageIO.read(f) );
		}
		
		return images;
	}
}
