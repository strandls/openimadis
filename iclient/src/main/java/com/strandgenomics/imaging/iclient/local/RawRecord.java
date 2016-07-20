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
 * RawRecord.java
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
package com.strandgenomics.imaging.iclient.local;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.AcquisitionProfile;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IProject;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.bioformats.BioPixelData;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.bioformats.BioVisualOverlay;
import com.strandgenomics.imaging.icore.bioformats.FileAttachment;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * BioRecord with handles to make webservice calls
 * @author arunabha
 *
 */
public class RawRecord extends BioRecord {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5199056613529583345L;

	/**
	 * file storing the buffered image of this pixel data
	 */
	private File thumbnailCache = null;
	
	/**
	 * true when default thumbnail is changed; false otherwise
	 */
	private boolean hasCustomThumbnail = false;
	/**
	 * parent project associated with this raw record
	 */
	protected Project parentProject = null;
	/**
	 * parent project may not be accessible if not have sufficient permission, but name is accessible
	 */
	private String parentProjectName = null;
	/**
	 * name of the microscope
	 */
	private String microscopeName = null;
	/**
	 * after uploading, this is the GUID of this record
	 */
	protected long guid = -1;
	/**
	 * parameters used while performing the acquisition
	 */
	private AcquisitionProfile acquisitionProfile = null;
	
	public RawRecord(BioExperiment expt,
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
		
		Logger.getRootLogger().info("Created RawRecord record for experiment "+this);
	}
	
	
	/**
	 * @return true if default thumbnail is changed; false otherwise
	 */
	public boolean hasCustomThumbnail()
	{
		return hasCustomThumbnail;
	}
	
	@Override
	public void setThumbnail(File customImage) 
	{
		try
		{
			BufferedImage originalImage = ImageIO.read(customImage);
			
			thumbnail = Util.resizeImage(originalImage, Constants.getRecordThumbnailWidth());
			writeToCache(thumbnail);
			
			hasCustomThumbnail = true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public synchronized BufferedImage getThumbnail() 
	{
		if(thumbnail != null)
		{
			thumbnail = readCachedImage();
		}
		
		if(thumbnail == null)
		{
			thumbnail = super.getThumbnail();
			writeToCache(thumbnail);
		}
		
		return thumbnail;
	}
	
	@Override
	protected BioPixelData createPixelData(Dimension imageCoordinate, double deltaTime, double exposureTime, Double x, Double y, Double z)
	{
		return new RawPixelData(this, imageCoordinate, deltaTime, exposureTime, x, y, z);
	}
	
	@Override
	protected IPixelDataOverlay createPixelDataOverlay(int sliceNo, int frameNo, int siteNo,int[] channelNos)
	{
		return new RawPixelDataOverlay(this, sliceNo, frameNo, siteNo, channelNos);
	}
	
	long getGUID()
	{
		if(guid == -1)
		{
			guid = ImageSpaceObject.getImageSpace().findGUID(signature);
		}
		return guid;
	}
	
	
	@Override
	public IProject getParentProject() 
	{
		if(parentProject == null)
		{
			parentProject = ImageSpaceObject.getImageSpace().findProject(getGUID());
			if(parentProject!=null)
				parentProjectName = parentProject.getName();
		}
		return parentProject;
	}
	
	public String getParentProjectName()
	{
		if(parentProjectName == null)
		{
			parentProjectName = ImageSpaceObject.getImageSpace().findProjectName(getGUID()); 
		}
		return parentProjectName;
	}
	
	public String getMicroscopeName()
	{
		if(microscopeName == null)
		{
			String mName = ImageSpaceObject.getImageSpace().getMicroscope();
			microscopeName = mName == null ? "NA": mName; 
		}
		return microscopeName;
	}
	
	public void uploadComments() 
	{
		try
		{
			guid = -1;
			ImageSpaceObject.getImageSpace().addUserComment(getGUID(), comments);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().warn("Error while uploading comments ", ex);
		}
	}
	
	public void uploadAttachments()
	{
		Collection<IAttachment> attachments = getAttachments();
		if(attachments == null) return;
		
		Iterator<IAttachment> it = attachments.iterator();
		
		while (it.hasNext()) 
		{
			IAttachment a = it.next();
			if(a instanceof FileAttachment)
			{
				FileAttachment fa = (FileAttachment) a;
				try
				{
					if(!fa.isSystemGenerated())//upload only user added attachments
					{
						Logger.getRootLogger().info("uploading attachments "+fa.getFile());
						ImageSpaceObject.getImageSpace().addRecordAttachments(getGUID(), fa.getFile(), fa.getName(), fa.getNotes());
					}
				}
				catch(Exception ex)
				{
					Logger.getRootLogger().warn("Error while uploading attachment "+fa.getFile(), ex);
				}
			}
		}
	}
	
	public void uploadUserAnnotations()
	{
		Map<String, Object> userAnnotations = getUserAnnotations();
		if(userAnnotations == null || userAnnotations.isEmpty())
			return;
		
		try
		{
			Logger.getRootLogger().info("uploading "+userAnnotations.size() +" user annotations");
			ImageSpaceObject.getImageSpace().addRecordUserAnnotation(getGUID(), userAnnotations);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().info("Error while uploading user annotations", ex);
		}
	}
	
	public void uploadUserAnnotation(String name, long value)
	{
		if(name == null || name.isEmpty())
			return;
		
		try
		{
			Logger.getRootLogger().info("uploading user annotation "+name+" "+value);
			ImageSpaceObject.getImageSpace().addRecordUserAnnotation(getGUID(), name, value);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().info("Error while uploading user annotation "+name+" "+value, ex);
		}
	}
	
	public void uploadUserAnnotation(String name, double value)
	{
		if(name == null || name.isEmpty())
			return;
		
		try
		{
			Logger.getRootLogger().info("uploading user annotation "+name+" "+value);
			ImageSpaceObject.getImageSpace().addRecordUserAnnotation(getGUID(), name, value);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().info("Error while uploading user annotation "+name+" "+value, ex);
		}
	}
	
	public void uploadUserAnnotation(String name, Date value)
	{
		if(name == null || name.isEmpty())
			return;
		
		try
		{
			Logger.getRootLogger().info("uploading user annotation "+name+" "+value);
			ImageSpaceObject.getImageSpace().addRecordUserAnnotation(getGUID(), name, value);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().info("Error while uploading user annotation "+name+" "+value, ex);
		}
	}
	
	public void uploadUserAnnotation(String name, String value)
	{
		if(name == null || name.isEmpty())
			return;
		
		try
		{
			Logger.getRootLogger().info("uploading user annotation "+name+" "+value);
			ImageSpaceObject.getImageSpace().addRecordUserAnnotation(getGUID(), name, value);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().info("Error while uploading user annotation "+name+" "+value, ex);
		}
	}
	
	public void uploadVisualOverlays()
	{
		if(visualAnnotations == null || visualAnnotations.isEmpty())
			return;

		for(Map.Entry<Integer, Map<String, Map<VODimension, BioVisualOverlay>>> entry : visualAnnotations.entrySet())
		{
			int siteNo = entry.getKey();
			Map<String, Map<VODimension, BioVisualOverlay>> siteOverlays = entry.getValue();

			for(Map.Entry<String, Map<VODimension, BioVisualOverlay>> namedOverlay : siteOverlays.entrySet())
			{
				String overlayName = namedOverlay.getKey();
				Map<VODimension, BioVisualOverlay> overlays = namedOverlay.getValue();
				try
				{
					Logger.getRootLogger().info("uploading overlays for siteNo="+siteNo +" having name "+overlayName);
					ImageSpaceObject.getImageSpace().createVisualOverlays(getGUID(), siteNo, overlayName);
					
					for(Map.Entry<VODimension, BioVisualOverlay> overlayEntry :  overlays.entrySet())
					{
						VODimension index = overlayEntry.getKey();
						BioVisualOverlay overlay  = overlayEntry.getValue();
						
						if(overlay.isEmpty()) continue;
						ImageSpaceObject.getImageSpace().addVisualObjects(getGUID(), overlay.getVisualObjects(), overlayName, index);
					}
				}
				catch(Exception ex)
				{
					Logger.getRootLogger().info("Error while uploading visual annotations", ex);
				}
			}
		}
	}
	
	public void uploadThumbnails(File thumbnailFile)
	{
		try
		{
			Logger.getRootLogger().info("uploading thumbnails ");
			ImageSpaceObject.getImageSpace().setRecordThumbnail(getGUID(), thumbnailFile);
		}
		catch (Exception ex)
		{
			Logger.getRootLogger().warn("Error while uploading thumbnail ", ex);
		}
	}
	
	protected BufferedImage readCachedImage()
	{
		if(thumbnailCache == null || !thumbnailCache.isFile())
			return null;
		
		BufferedInputStream in = null;
		BufferedImage renderableImage = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(thumbnailCache));
			renderableImage = ImageIO.read(in);
		}
		catch(IOException ex)
		{
			System.out.println("unable to read thumbnail cache "+ex);
		}
		finally
		{
			try 
			{
				in.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return renderableImage;
	}
	
	@Override
	public void dispose() 
	{
		super.dispose();
		
		if(thumbnailCache != null && thumbnailCache.exists())
			thumbnailCache.delete();
		
		Collection<IAttachment> attachments = getAttachments();
		if(attachments == null) return;
		
		Iterator<IAttachment> it = attachments.iterator();
		
		while (it.hasNext()) 
		{
			IAttachment a = it.next();
			if(a instanceof FileAttachment)
			{
				FileAttachment fa = (FileAttachment) a;
				if (fa.isSystemGenerated())// delete only system generated attachments
				{
					File temp = fa.getFile();
					if(temp != null && temp.exists())
						temp.delete();
				}
			}
		}
	}
	
	protected void writeToCache(BufferedImage renderableImage)
	{
		if(thumbnailCache != null && thumbnailCache.isFile())
			thumbnailCache.delete();
		
		thumbnailCache = null;
	
		BufferedOutputStream out = null;
		try
		{
			File temp = File.createTempFile("thumbnail", ".png", Constants.TEMP_DIR);
			
			out = new BufferedOutputStream(new FileOutputStream(temp));
			ImageIO.write(renderableImage, "PNG", out);
			
			thumbnailCache = temp;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try 
			{
				out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

		}
	}

	public void setAcquisitionProfile(AcquisitionProfile profile)
	{
		this.acquisitionProfile = profile;
	}


	public void uploadAcqProfile()
	{
		if(this.acquisitionProfile!=null)
		{
			try
			{
				ImageSpaceObject.getImageSpace().setAcquisitionProfile(getGUID(), this.acquisitionProfile);
			}
			catch(Exception ex)
			{
				Logger.getRootLogger().warn("Error while uploading comments ", ex);
			}
		}
	}
}

