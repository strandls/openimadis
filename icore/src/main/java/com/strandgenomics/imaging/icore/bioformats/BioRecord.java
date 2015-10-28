/**
 * BioRecord.java
 *
 * Project imaging
 * Core Bioformat Component
 *
 * Copyright 2009-2010 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.icore.bioformats;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loci.formats.ChannelSeparator;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IProject;
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
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * Record as extracted using Bio-formats Libraries
 * @author arunabha
 *
 */
public class BioRecord implements IRecord, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2686791451448126995L;
	/**
	 * the member pixel data of this record
	 */
	protected Map<Dimension, IPixelData> members = new HashMap<Dimension, IPixelData>();
	/**
	 * the unique signature is associated with this record
	 */
	protected Signature signature = null;
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
	 * record upload time to the server
	 */
	protected Date uploadTime = null;
	/**
	 * checks if the login user has write permission on this record
	 */
	protected boolean isWritable = false;
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
	 * List of available sites
	 */
	protected List<Site> sites = null;
	/**
	 * comments added on this record
	 */
	protected String comments = null;
	/**
	 * user annotations associated with this record
	 */
	protected Map<String, Object> userAnnotations;
	/**
	 * non std meta data associated with this record
	 */
	protected Map<String, Object> dynamicMetaData;
	/**
	 * list of attachments associated with this record
	 */
	protected Map<String, IAttachment> attachments;
	/**
	 * list of attachments associated with this record
	 */
	protected Map<Integer, Map<String, Map<VODimension, BioVisualOverlay>>> visualAnnotations;
	/**
	 * A raw record is typically sourced through a record group 
	 */
	protected BioExperiment parentExpt;
	/**
	 * the thumb-nail
	 */
	protected transient BufferedImage thumbnail = null;
	/**
	 * cache of pixel-data overlays created
	 */
	private Map<OverlayDimension, IPixelDataOverlay> overlaydataCache = new HashMap<OverlayDimension, IPixelDataOverlay>();
	
	/**
	 * Constructs a record from the data fetched from the underlying system
	 * @param expt a record can be part of a group of records
	 * @param creationTime acquisition time
	 * @param noOfFrames number of frames within this record
	 * @param noOfSlices number of slices (z) within this record
	 * @param channelNames channels within this record
	 * @param imageWidth width of a pixel-data (image) within this record
	 * @param imageHeight height of a pixel-data (image) within this record
	 * @param pixelDepth depth of a pixel in the pixel-data
	 * @param pixelSizeX x-size (in micron) of the pixel
	 * @param pixelSizeY y-size (in micron) of the pixel
	 * @param imageType type of the image, by default it is Gray-scale
	 * @param sourceFormat file format of the original source files
	 */
	public BioRecord(BioExperiment expt, Date sourceFileTime, Date acquiredDate,
			int noOfFrames, int noOfSlices, List<Channel> channels, List<Site> sites,
			int imageWidth, int imageHeight, PixelDepth pixelDepth,
			double pixelSizeX, double pixelSizeY, double pixelSizeZ,
			ImageType imageType, SourceFormat sourceFormat)
	{
		this.signature = new Signature(noOfFrames, noOfSlices, channels.size(), 
				imageWidth, imageHeight, sites, expt.getMD5Signature());
		
		this.sites = sites;
		this.pixelDepth = pixelDepth;
		this.parentExpt = expt;
		this.channels   = channels;
		
		this.creationTime   = new Date();
		this.acquiredDate   = acquiredDate;
		this.sourceFileTime = sourceFileTime;

		this.pixelSizeAlongXAxis = pixelSizeX;
		this.pixelSizeAlongYAxis = pixelSizeY;
		this.pixelSizeAlongZAxis = pixelSizeZ;
		
		this.imageType    = imageType;
		this.sourceFormat = sourceFormat;
		
		this.userAnnotations   = new HashMap<String, Object>();
		this.dynamicMetaData   = new HashMap<String, Object>();
		this.attachments       = new HashMap<String, IAttachment>();
		
		this.visualAnnotations = new HashMap<Integer, Map<String, Map<VODimension, BioVisualOverlay>>>();
	}
	
	@Override
	public void setChannelLUT(int channelNo, String lut)
    {
		if(channels.get(channelNo).getLut().equals(lut))
			return; //noting to change
		
		//set the change
		channels.get(channelNo).setLut(lut);
		
		//propagate the information to all cached images of this channel
		for(Map.Entry<Dimension, IPixelData> entry : members.entrySet())
		{
			if(entry.getKey().channelNo == channelNo)
			{
				((BioPixelData)entry.getValue()).clear(); //clear all held caches
			}
		}
		
		//clear all caches
		for(Map.Entry<OverlayDimension, IPixelDataOverlay> entry : overlaydataCache.entrySet())
		{
			OverlayDimension dims = entry.getKey();
			IPixelDataOverlay overlay = entry.getValue();
			
			if(dims.containsChannel(channelNo))
			{
				((BioPixelDataOverlay)overlay).clear();//clear all caches
			}
		}
    }
	
	@Override
    public void setCustomContrast(boolean zStacked, int channelNo, VisualContrast newContrast)
	{
		VisualContrast oldContrast = getCustomContrast(zStacked, channelNo);
		
		if(oldContrast == null && newContrast == null || 
				newContrast != null && oldContrast != null && newContrast.equals(oldContrast))
			return; //no change
		
		//set the change
		channels.get(channelNo).setContrast(zStacked, newContrast);
		
		//propagate the information to all cached images of this channel
		for(Map.Entry<Dimension, IPixelData> entry : members.entrySet())
		{
			if(entry.getKey().channelNo == channelNo)
			{
				((BioPixelData)entry.getValue()).clear(); //clear all held caches
			}
		}
		
		//clear all caches
		for(Map.Entry<OverlayDimension, IPixelDataOverlay> entry : overlaydataCache.entrySet())
		{
			OverlayDimension dims = entry.getKey();
			IPixelDataOverlay overlay = entry.getValue();
			
			if(dims.containsChannel(channelNo))
			{
				((BioPixelDataOverlay)overlay).clear();//clear all caches
			}
		}
	}
    
	@Override
    public VisualContrast getCustomContrast(boolean zStacked, int channelNo)
    {
    	return channels.get(channelNo).getContrast(zStacked);
    }

	@Override
	public Signature getSignature() 
	{
		return signature;
	}
	
	@Override
	public Map<String, Object> getDynamicMetaData() 
	{
		return dynamicMetaData;
	}

	@Override
	public Map<String, Object> getUserAnnotations() 
	{
		return userAnnotations;
	}

	@Override
	public Collection<IAttachment> getAttachments()
	{
		return attachments.values();
	}

	@Override
	public void addAttachment(File attachmentFile, String notes) throws IOException
	{
		//user generated attachment
		addAttachment(attachmentFile, attachmentFile.getName(), notes, false);
	}
	
	@Override
	public void addAttachment(File attachmentFile, String name, String notes) throws IOException
	{
		//user generated attachment
		addAttachment(attachmentFile, name, notes, false);
	}
	
	public void addAttachment(File attachmentFile, String name, String notes, boolean systemGenerated) throws IOException
	{
		if(!attachmentFile.isFile())
			throw new IOException("attachment file not found "+attachmentFile);
		
		FileAttachment att = new FileAttachment(this, attachmentFile, name, notes, systemGenerated);
		attachments.put(name, att);
	}
	
	@Override
	public void removeAttachment(String name)
	{
    	if(attachments.containsKey(name))
    	{
    		attachments.remove(name);
    	}
	}

	@Override
	public void addUserAnnotation(String name, long value) 
	{
		userAnnotations.put(name, value);
	}

	@Override
	public void addUserAnnotation(String name, double value) 
	{
		userAnnotations.put(name, value);
	}

	@Override
	public void addUserAnnotation(String name, String value) 
	{
		userAnnotations.put(name, value);
	}
	
	@Override
	public void addUserAnnotation(String name, Date value) 
	{
		userAnnotations.put(name, value);
	}
	
	@Override
    public Object removeUserAnnotation(String name)
    {
    	if(userAnnotations.containsKey(name))
    	{
    		return userAnnotations.remove(name);
    	}
    	return null;
    }
	
	@Override
	public void setThumbnail(File customImage) 
	{
	}
	
	@Override
	public synchronized BufferedImage getThumbnail() 
	{
		if(thumbnail != null)
			return thumbnail;
		
		int sliceNo = signature.noOfSlices/2;
		int frameNo = signature.noOfFrames/2;
		int siteNo = signature.getNoOfSites()/2;
		
		int[] channelNos = new int[getChannelCount()]; //all channels
		for(int i = 0;i < channelNos.length; i++)
		{
			channelNos[i] = i;
		}
    	
    	IPixelDataOverlay overlay = getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos);
    	BufferedImage originalImage = null;
		
    	try 
    	{
    		if(overlay.getImageHeight() > Constants.MAX_THUMBNAIL_HEIGHT || overlay.getImageWidth() > Constants.MAX_THUMBNAIL_WIDTH)
    		{
    			originalImage = overlay.getImage(false, false, true, new Rectangle(0, 0, Constants.MAX_THUMBNAIL_WIDTH, Constants.MAX_THUMBNAIL_HEIGHT));
    		}
    		else
    			originalImage = overlay.getImage(false, false, true, null);
		} 
		catch (IOException e) 
		{
			Logger.getRootLogger().error("Cannot get overlay: ",e);
		}
		
		thumbnail = Util.resizeImage(originalImage, Constants.getRecordThumbnailWidth());
		return thumbnail;
	}
	
	/**
	 * pre-populate the cache
	 * @throws IOException 
	 */
	public void populatePixelData() throws IOException
	{
		Logger.getRootLogger().info("[Indexer]:\tinspecting individual pixel data of the record for "+signature);
		BufferedImageReader imageReader = ImageManager.getInstance().getImageReader(this);
		
		try
		{
			for (int siteNo = 0; siteNo < signature.getNoOfSites(); siteNo++) 
			{
				int seriesNo  = getSite(siteNo).getSeriesNo();
				imageReader.setSeries( seriesNo );
				IMetadata metaData = (IMetadata)imageReader.getMetadataStore();
				
				//populate for all dimensions for the given site (series)
				for (int frameNo = 0; frameNo < signature.noOfFrames; frameNo++) 
				{
					for (int sliceNo = 0; sliceNo < signature.noOfSlices; sliceNo++) 
					{
						for (int channelNo = 0; channelNo < signature.noOfChannels; channelNo++) 
						{
							Dimension imageCoordinate = new Dimension(frameNo, sliceNo, channelNo, siteNo);
							IPixelData pixelData = createPixelDataFromSeries(imageReader, metaData, seriesNo, imageCoordinate);
							members.put(imageCoordinate, pixelData);
						}
					}
				}
			}
		}
		finally
		{
			try 
			{
				imageReader.close();  //return to the pool
			} 
			catch (IOException e)
			{}
		}
		
		Logger.getRootLogger().info("[Indexer]:\tsuccessfully inspected individual pixel data of the record for "+signature);
	}
	
	protected IPixelData createPixelDataFromSeries(BufferedImageReader imageReader, IMetadata metaData, int seriesNo, Dimension imageCoordinate)
	{
		int planeIndex = 0;
		double deltaTime = 0;
		double exposureTime = 0;
		Double x = null,y = null,z = null;
		
		try
		{
			if(imageReader.isRGB())
			{
				ChannelSeparator separator = new ChannelSeparator(imageReader);
				planeIndex = separator.getOriginalIndex(separator.getIndex(imageCoordinate.sliceNo, imageCoordinate.channelNo, imageCoordinate.frameNo));	
			}
			else
			{	
				planeIndex = imageReader.getIndex(imageCoordinate.sliceNo, imageCoordinate.channelNo, imageCoordinate.frameNo);
			}
			
			if(planeIndex >= metaData.getPlaneCount(seriesNo))
			{
				Logger.getRootLogger().error("Error: planeIndex >= metaData.getPlaneCount(seriesNo)");
				Logger.getRootLogger().warn("unable to obtain deltaTime & exposureTime...");
			}
			else
			{
				Double deltaT = metaData.getPlaneDeltaT(seriesNo, planeIndex);
				Double exposureT = metaData.getPlaneExposureTime(seriesNo, planeIndex);
				
				x = metaData.getPlanePositionX(seriesNo, planeIndex);
				y = metaData.getPlanePositionY(seriesNo, planeIndex);
				z = metaData.getPlanePositionZ(seriesNo, planeIndex);

				deltaTime = deltaT == null ? 0 : deltaT.doubleValue();
				exposureTime = exposureT == null ? 0 : exposureT.doubleValue();
			}
		}
		catch(Exception e)
		{
			Logger.getRootLogger().error("Cannot read meta-data: ",e);
		}
		catch(AbstractMethodError what)
		{
			Logger.getRootLogger().error("Cannot read meta-data: ",what);
		}
		
		return createPixelData(imageCoordinate, deltaTime, exposureTime, x,y,z);
	}

	@Override
	public IPixelData getPixelData(Dimension imageCoordinate) throws IOException 
	{
		if(members.containsKey(imageCoordinate))
		{
			return members.get(imageCoordinate);
		}
			
		IPixelData pixeldata = null;
		BufferedImageReader imageReader = null;
		
		try
		{
			imageReader = ImageManager.getInstance().getImageReader(this);
			
			int seriesNo  = getSite(imageCoordinate.siteNo).getSeriesNo();
			imageReader.setSeries( seriesNo );
			
			IMetadata metaData = (IMetadata)imageReader.getMetadataStore();
			
			pixeldata = createPixelDataFromSeries(imageReader, metaData, seriesNo, imageCoordinate);
			members.put(imageCoordinate, pixeldata);
		}
		finally
		{
			try 
			{
				imageReader.close(); //return to the pool
			} 
			catch (IOException e) 
			{} 
		}
		
		return pixeldata;
	}
	
	protected BioPixelData createPixelData(Dimension imageCoordinate, double deltaTime, double exposureTime, Double x, Double y, Double z)
	{
		return new BioPixelData(this, imageCoordinate, deltaTime, exposureTime, x, y, z);
	}

	@Override
	public IPixelDataOverlay getOverlayedPixelData(int sliceNo, int frameNo, int siteNo, int[] channelNos) 
	{
		OverlayDimension dims = new OverlayDimension(sliceNo, frameNo, siteNo, channelNos);
		if(overlaydataCache.containsKey(dims))
		{
			System.out.println("reading pixel overlay from cache...");
			return overlaydataCache.get(dims);
		}
		
		IPixelDataOverlay pixelData = createPixelDataOverlay(sliceNo, frameNo, siteNo, channelNos);
		overlaydataCache.put(dims, pixelData);
		
		return pixelData;
	}
	
	protected IPixelDataOverlay createPixelDataOverlay(int sliceNo, int frameNo, int siteNo,int[]  channelNos)
	{
		return new BioPixelDataOverlay(this, sliceNo, frameNo, siteNo, channelNos);
	}
	
	public boolean containsVisualOverlay(VODimension key)
	{
		return visualAnnotations.containsKey(key);
	}

	@Override
	public IProject getParentProject() 
	{
		return null;
	}

	@Override
	public IExperiment getExperiment() 
	{
		return parentExpt;
	}
	
    /**
     * add the specified list of meta-data to this record. 
     * Note that only string, and numbers values are supported, rest will be ignored
     */
	public void addDynamicMetaData(Map<String, Object> annotations) 
	{
		if(annotations == null) return;
		
		for(Map.Entry<String, Object> entry : annotations.entrySet())
		{
			String key   = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof String || value instanceof Number)
			{
				dynamicMetaData.put(key, value);
			}
		}
	}
	
	@Override
	public int getChannelIndex(IChannel channel)
	{
		for(int i = channels.size()-1; i >= 0; i--)
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
	public Date getUploadTime()
	{
		return uploadTime;
	}
	
	public void setUploadTime(Date uploadTime)
	{
		this.uploadTime = uploadTime;
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
	public Channel getChannel(int channelNo) 
	{
		return channels.get(channelNo);
	}
	
	public List<Channel> getChannels()
	{
		return channels;
	}
	
	@Override
	public int getSiteCount() 
	{
		return signature.getNoOfSites();
	}

	@Override
	public Site getSite(int siteNo) 
	{
		return sites.get(siteNo);
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
	public String toString()
	{
		return getSignature().toString();
	}
	
	@Override
	public int hashCode()
	{
		return getSignature().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof IRecord)
		{
			return ((IRecord)obj).getSignature().equals(this.getSignature());
		}
		return false;
	}
	
	@Override
	public void addUserAnnotation(Map<String, Object> annotations) 
	{
		if(annotations == null) return;
		
		for(Map.Entry<String, Object> entry : annotations.entrySet())
		{
			String key   = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof String || value instanceof Number)
			{
				userAnnotations.put(key, value);
			}
		}
	}
	
	@Override
	public void dispose() 
	{
		if(members != null)
		{
			for(Map.Entry<Dimension, IPixelData> entry : members.entrySet())
			{
				entry.getValue().dispose();
			}
			
			members.clear();
		}
		
		if(overlaydataCache != null)
		{
			for(Map.Entry<OverlayDimension, IPixelDataOverlay> entry : overlaydataCache.entrySet())
			{
				entry.getValue().dispose();
			}
			
			overlaydataCache.clear();
		}

		overlaydataCache = null;
		members = null;
		thumbnail = null;
	}
	
	@Override
	public String getOriginMachineAddress() 
	{
		return parentExpt.getOriginMachineAddress();
	}

	@Override
	public String getOriginMachineIP() 
	{
		return parentExpt.getOriginMachineIP();
	}

	@Override
	public String getSourceFilename() 
	{
		return parentExpt.getSourceFilename();
	}

	@Override
	public String getRootDirectory() 
	{
		return parentExpt.getRootDirectory();
	}
	
	@Override
	public List<ISourceReference> getSourceReference()
	{
		return parentExpt.getReference();
	}

	@Override
	public synchronized void createVisualOverlays(int siteNo, String name) 
	{
		if(siteNo >= signature.noOfSites)
			throw new IllegalArgumentException("illegal site number "+siteNo);
			
		//Map<Integer, Map<String, Map<VODimension, IVisualOverlay>>>
		if(!visualAnnotations.containsKey(siteNo))
		{
			Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = new HashMap<String, Map<VODimension, BioVisualOverlay>>();
			visualAnnotations.put(siteNo, siteOverlays);
		}
		
		Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = visualAnnotations.get(siteNo);
		if(siteOverlays.containsKey(name))
		{
			throw new IllegalArgumentException("named overlays already exist on site "+siteNo);
		}
		
		Map<VODimension, BioVisualOverlay> namedOverlay = new HashMap<VODimension, BioVisualOverlay>();
		
		Signature s = getSignature();
		for(int vframe=0; vframe < s.noOfFrames; vframe++)
		{
			for(int vslice=0; vslice < s.noOfSlices;vslice++)
			{
				VODimension id = new VODimension(vframe, vslice, siteNo);
				BioVisualOverlay overlay = new BioVisualOverlay(id, name, s.imageWidth, s.imageHeight);
				namedOverlay.put(id, overlay);
			}
		}
		
		siteOverlays.put(name, namedOverlay);
	}

	@Override
	public Collection<IVisualOverlay> getVisualOverlays(VODimension dimension) 
	{
		if(!visualAnnotations.containsKey(dimension.siteNo)) return null;
		
		List<IVisualOverlay> list = new ArrayList<IVisualOverlay>();
		
		Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = visualAnnotations.get(dimension.siteNo);
		
		for(Map.Entry<String, Map<VODimension, BioVisualOverlay>> entry : siteOverlays.entrySet())
		{
			Map<VODimension, BioVisualOverlay> overlays = entry.getValue();
			
			if(overlays.containsKey(dimension))
			{
				list.add( overlays.get(dimension) );
			}
		}
		
		return list.isEmpty() ? null : list;
	}

	@Override
	public Set<String> getAvailableVisualOverlays(int siteNo) 
	{
		return visualAnnotations.containsKey(siteNo) ? visualAnnotations.get(siteNo).keySet() : null;
	}

	@Override
	public void deleteVisualOverlays(int siteNo, String name) 
	{
		if(visualAnnotations.containsKey(siteNo))
		{
			Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = visualAnnotations.get(siteNo);
			if(siteOverlays.containsKey(name))
			{
				Map<VODimension, BioVisualOverlay> overlays = siteOverlays.remove(name);
				overlays.clear();
				overlays = null;
			}
		}
	}

	@Override
	public void addVisualObjects(List<VisualObject> vObjects, String name, VODimension ... imageCoordinates)
	{
		for(VODimension d : imageCoordinates)
		{
			Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = visualAnnotations.get(d.siteNo);
			Map<VODimension, BioVisualOverlay> namedOverlays = siteOverlays.get(name);
			
			if(namedOverlays.containsKey(d))
			{
				namedOverlays.get(d).addVisualObjects(vObjects);
			}
		}
	}

	@Override
	public void deleteVisualObjects(List<VisualObject> vObjects, String name, VODimension... imageCoordinates) 
	{
		for(VODimension d : imageCoordinates)
		{
			Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = visualAnnotations.get(d.siteNo);
			Map<VODimension, BioVisualOverlay> namedOverlays = siteOverlays.get(name);
			
			if(namedOverlays.containsKey(d))
			{
				namedOverlays.get(d).deleteVisualObjects(vObjects);
			}
		}
	}

	@Override
	public IVisualOverlay getVisualOverlay(VODimension d, String name) 
	{
		Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = visualAnnotations.get(d.siteNo);
		if(siteOverlays.containsKey(name))
			return siteOverlays.get(name).get(d);
		return null;
	}

	@Override
	public List<UserComment> getUserComments() 
	{
		return null;
	}

	@Override
	public void addUserComments(String value) 
	{
		this.comments = value;
	}
	
	/**
	 * Returns the user comment on this record
	 * @return user (the uploader user) comment on this record
	 */
	public String getComments()
	{
		return comments;
	}
	
	@Override
	public List<BufferedImage> getChannelOverlayImagesForSlices(int frameNo, int siteNo, int imageWidth, 
			boolean useChannelColor) throws IOException
	{
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		int[] channelNos = new int[signature.noOfChannels]; //all channels
		for(int i = 0;i < channelNos.length; i++)
			channelNos[i] = i;
		
		for(int sliceNo = 0; sliceNo < signature.noOfSlices; sliceNo++)
		{
			IPixelDataOverlay overlay = getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos);
			BufferedImage image = overlay.getImage(false, false, useChannelColor, null);
			
			if(signature.imageWidth != imageWidth)
			{
				int imageHeight = (int) Math.floor((imageWidth * ((double)signature.imageHeight/(double)signature.imageWidth)) + 0.5); 
				image = Util.resizeImage(image, imageWidth, imageHeight);
			}
			images.add( image );
		}
		return images;
	}
	
	static class OverlayDimension implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 *  The slice number (Z) of the image to overlay with channels
		 */
		public final int sliceNo;
		/**
		 * The frame number of the image to overlay with channels
		 */
		public final int frameNo;
		/**
		 *  The site number of the image to overlay with channels
		 */
		public final int siteNo; 
		/**
		 *  The list of channel numbers (dimensions) that are combined together to create this PixelData overlay  
		 */
		public final Set<Integer> channelNos;
		
		public OverlayDimension(int sliceNo, int frameNo, int siteNo, int[] channelList)
		{
			this.sliceNo = sliceNo;
			this.frameNo = frameNo;
			this.siteNo  = siteNo;
			this.channelNos = new HashSet<Integer>();
			
			for(int channelNo : channelList)
				channelNos.add(channelNo);
		}
		
		public boolean containsChannel(int channelNo)
		{
			return channelNos.contains(channelNo);
		}
		
		@Override
		public int hashCode()
		{
			return (((frameNo & 0xFF) << 24) + ((sliceNo & 0xFF) << 16) + ((siteNo & 0xFF) << 8));
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj != null && obj instanceof OverlayDimension)
			{
				OverlayDimension that = (OverlayDimension) obj;
				return (this.frameNo == that.frameNo &&
						this.sliceNo == that.sliceNo &&
						this.siteNo == that.siteNo &&
						 this.channelNos.equals(that.channelNos));
			}
			return false;
		}
		
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append('{');
			builder.append(this.frameNo).append(',');
			builder.append(this.sliceNo).append(',');
			builder.append(this.siteNo);
			builder.append('}');
			
			return builder.toString();
		}
	}
}
