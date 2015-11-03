/*
 * ImportFilter.java
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
package com.strandgenomics.imaging.iclient.local.genericimport;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import loci.formats.ChannelSeparator;
import loci.formats.FormatException;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;
import ome.xml.model.primitives.PositiveFloat;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.bioformats.custom.FieldType;
import com.strandgenomics.imaging.icore.bioformats.custom.ImageIdentifier;
import com.strandgenomics.imaging.icore.bioformats.custom.ImgFormatConstants;
import com.strandgenomics.imaging.icore.image.PixelDepth;

/**
 * encapsulates the notion of a filename based record import filter
 * @author arunabha
 *
 */
public class ImportFilter implements Serializable {
	
	private static final long serialVersionUID = 2653619031509496380L;
	/**
	 * name of the filter
	 */
	public final String name;
	/**
	 * the separator character to be used to tokenize the fields from the file name
	 */
	public final String fieldSeparator;
	/**
	 * the field token position versus field type table
	 */
	protected Map<Integer, FieldType> positionToFieldMap = null;
	/**
	 * the logger to track what is going on
	 */
	private Logger logger = Logger.getRootLogger();
	/**
	 * data validated from seed file
	 */
	private ImageIdentifier validatedData = null;
	/**
	 * count of images in seed file
	 */
	private int multiImageCount;
	/**
	 * Create an import filter instance with a specific position to field type table
	 * @param fieldSeparator the field separator char
	 * @param table  the field token position versus field type table
	 */
	public ImportFilter(String name, String fieldSeparator,  Map<Integer, FieldType> table)
	{
		this.name = name;
		this.fieldSeparator = fieldSeparator;
		this.positionToFieldMap = table;
	}
	
	/**
	 * Returns the position of the specified field
	 * @param field the field under consideration
	 * @return the position of the field if it is there, null otherwise
	 */
	public Integer getPosition(FieldType field)
	{
		for(Map.Entry<Integer, FieldType> entry : positionToFieldMap.entrySet())
		{
			if(entry.getValue().equals(field))
				return entry.getKey();
		}
		
		return null;
	}
	
	/**
	 * checks whether the specified file can be handled by this filter
	 * @param sourceFile the source file under consideration
	 * @return true if the specified file can be handled, false otherwise
	 */
	public boolean isValidFile(File sourceFile)
	{
		String[] fields = getTokens(sourceFile);
		return fields.length == positionToFieldMap.size();
	}
	
	/**
	 * extract the fields from the specified file (from its name only)
	 * @param sourceFile the source file under consideration
	 * @param doValidation if image data needs to be validated
	 * @return the extracted fields, null if the fields do not match
	 */
	public Set<ImageIdentifier> getFields(File sourceFile, boolean doValidation, FieldType multiImageDimension)
	{
		logger.info("[ImportFilter]:\tchecking "+sourceFile );
		
		String name = sourceFile.getName();
		if(name.contains(ImgFormatConstants.EXTENSION_SEPARATOR))// remove extension
			name = name.substring(0, name.lastIndexOf(ImgFormatConstants.EXTENSION_SEPARATOR));
		
		String fields[] = name.split(fieldSeparator);
		if(fields.length != positionToFieldMap.size())
		{
			logger.info("[ImportFilter]:\tinvalid file "+sourceFile );
			return null;
		}
		
		String frame = "0";
		String channel = "0";
		String slice = "0";
		String site = "0";
		String recordLabel = ImgFormatConstants.DEFAULT_RECORD_LABEL;
		
		for(int i=0; i < fields.length; i++)
		{
			FieldType type = positionToFieldMap.get(i);
			switch (type)
			{
				case FRAME:
					frame = fields[i];
					break;
				case SLICE:
					slice = fields[i];
					break;
				case CHANNEL:
					channel = fields[i];
					break;
				case SITE:
					site = fields[i];
					break;
				case RECORD_LABEL:
					recordLabel = fields[i];
					break;
				case IGNORE:
				default:
					break;
			}
		}
		
		return populateImageMetaData(sourceFile, recordLabel, frame, slice, channel, site, doValidation, multiImageDimension);
	}

	private Set<ImageIdentifier> populateImageMetaData(File sourceFile, String recordLabel, String frame, String slice, String channel, String site, boolean doValidation, FieldType multiImageDimension)
	{
		if (doValidation || this.validatedData == null)
		{
			return readImageFile(sourceFile, recordLabel, frame, slice, channel, site, multiImageDimension);
		}
		else
		{
			
			Set<ImageIdentifier> images = new HashSet<ImageIdentifier>();
			
			int imageCount = this.multiImageCount;
			for (int dim = 0; dim < imageCount; dim++)
			{
				if(FieldType.SLICE.equals(multiImageDimension))
					slice = dim+"";
				else if(FieldType.FRAME.equals(multiImageDimension))
					frame = dim+"";
				else if(FieldType.CHANNEL.equals(multiImageDimension))
					channel = dim+"";
				else if(FieldType.SITE.equals(multiImageDimension))
					site = dim+"";
				
				ImageIdentifier id = new ImageIdentifier(sourceFile.getName(), recordLabel, frame, slice, channel, site, this.validatedData.recordID.imageWidth, this.validatedData.recordID.imageHeight,
						this.validatedData.recordID.depth, this.validatedData.recordID.imageType, this.validatedData.recordID.isLittleEndian, this.validatedData.recordID.pixelSizeAlongXAxis,
						this.validatedData.recordID.pixelSizeAlongYAxis, this.validatedData.recordID.pixelSizeAlongYAxis, this.validatedData.positionX, this.validatedData.positionY,
						this.validatedData.positionZ, this.validatedData.deltaTime, this.validatedData.exposureTime, this.validatedData.recordID.acquiredTime, multiImageDimension);
				
				images.add(id);
			}
			
			return images;
		}
	}
	
	private Set<ImageIdentifier> readImageFile(File sourceFile, String recordLabel, String frame, String slice, String channel, String site, FieldType multiImage)
	{
		int seriesNo = 0;
		int planeIndex = 0;
		
		BufferedImageReader imageReader = null;
		try
		{
			imageReader = createBufferedImageReader(sourceFile);
			imageReader.setSeries(seriesNo); 
			
			int imageWidth = imageReader.getSizeX();
			int imageHeight = imageReader.getSizeY();
			
			PixelDepth pixelDepth = extractPixelDepth(imageReader.getPixelType());
			ImageType imageType = imageReader.isRGB() ? ImageType.RGB : ImageType.GRAYSCALE;
			boolean isLittleEndian = imageReader.isLittleEndian();
			
			//Adding record meta-data fields ..
			IMetadata metaData = (IMetadata)imageReader.getMetadataStore();
			
			PositiveFloat pixelX = metaData.getPixelsPhysicalSizeX(seriesNo);
			PositiveFloat pixelY = metaData.getPixelsPhysicalSizeY(seriesNo);
			PositiveFloat pixelZ = metaData.getPixelsPhysicalSizeZ(seriesNo);
			
			double sizeX = pixelX == null ? 0.0 : pixelX.getValue();
			double sizeY = pixelY == null ? 0.0 : pixelY.getValue(); 
			double sizeZ = pixelZ == null ? 0.0 : pixelZ.getValue(); 
			
			
			Double deltaT = null;
			try
			{
				deltaT    = metaData.getPlaneDeltaT(seriesNo, planeIndex);
			}
			catch (Exception e) 
			{ }
			
			Double exposureT = null;
			try
			{
				exposureT = metaData.getPlaneExposureTime(seriesNo, planeIndex);
			}
			catch (Exception e) 
			{ }
			
			Double positionX = null;
			try
			{
				positionX = metaData.getPlanePositionX(seriesNo, planeIndex);
			}
			catch (Exception e) 
			{ }
			
			Double positionY = null;
			try
			{
				positionY = metaData.getPlanePositionY(seriesNo, planeIndex);
			}
			catch (Exception e) 
			{ }
			
			Double positionZ = null;
			try
			{
				positionZ = metaData.getPlanePositionZ(seriesNo, planeIndex);
			}
			catch (Exception e) 
			{ }
			
			Date acquiredDate = null;
			if(metaData.getImageAcquisitionDate(seriesNo)!=null)
				acquiredDate = metaData.getImageAcquisitionDate(seriesNo).asDate();

			Set<ImageIdentifier> images = new HashSet<ImageIdentifier>();
			int imageCount = imageReader.getImageCount();
			
			for (int dim = 0; dim < imageCount; dim++)
			{
				if(FieldType.SLICE.equals(multiImage))
					slice = dim+"";
				else if(FieldType.FRAME.equals(multiImage))
					frame = dim+"";
				else if(FieldType.CHANNEL.equals(multiImage))
					channel = dim+"";
				else if(FieldType.SITE.equals(multiImage))
					site = dim+"";
				
				ImageIdentifier id = new ImageIdentifier(sourceFile.getName(), recordLabel, frame, slice, channel, site, imageWidth, imageHeight, pixelDepth, imageType, isLittleEndian, sizeX, sizeY,
						sizeZ, positionX == null ? 0.0 : positionX.doubleValue(), positionY == null ? 0.0 : positionY.doubleValue(), positionZ == null ? 0.0 : positionZ.doubleValue(),
						deltaT == null ? 0.0 : deltaT.doubleValue(), exposureT == null ? 0.0 : exposureT.doubleValue(), acquiredDate, multiImage);
				
				if(this.validatedData == null)
				{
					this.validatedData = id;
					this.multiImageCount = imageCount;
				}
				
				images.add(id);
			}
			
			
			return images;
		}
		catch(Exception ex)
		{
			logger.info("[ImportFilter]:\terror while extracting image meta data from "+sourceFile, ex);
			return null;
		}
		finally
		{
			try 
			{
				imageReader.close();
			} 
			catch (Exception e) {}
		}
	}
	
	/**
	 * Initialize a Bioformats reader by setting a filepath
	 * @param file the file source of records
	 * @return the image reader instance
	 * @throws IOException 
	 * @throws FormatException 
	 */
	private BufferedImageReader createBufferedImageReader(File seedFile) throws FormatException, IOException
	{
		BufferedImageReader imageReader = new BufferedImageReader(); 
		IMetadata omexmlMetadata = MetadataTools.createOMEXMLMetadata();
		imageReader.setMetadataStore(omexmlMetadata);
		MetadataTools.setDefaultDateEnabled(false);
		
		imageReader.setId(seedFile.getAbsolutePath());
		//use channel separator ignoring the image types (RGB or GRAYSCALE)
		ChannelSeparator separator = new ChannelSeparator(imageReader);
		//the BufferedImageReader
		return new BufferedImageReader(separator); 
	}
	
	/**
	 * Returns the relevant pixel depth
	 * @param pixelType 
	 * @return the relevant pixel depth
	 */
	private PixelDepth extractPixelDepth(int pixelType)
	{
		PixelDepth pixelDepth = PixelDepth.BYTE;
		
		switch(pixelType)
		{
			case FormatTools.UINT32:
			case FormatTools.INT32:
				pixelDepth = PixelDepth.INT;
				break;
			case FormatTools.UINT16:
			case FormatTools.INT16:
				pixelDepth = PixelDepth.SHORT;
				break;
			case FormatTools.UINT8:
			case FormatTools.INT8:
				pixelDepth = PixelDepth.BYTE;
				break;
		}
		return pixelDepth;
	}
	
	private String[] getTokens(File sourceFile)
	{
		String name = sourceFile.getName();
		if(name.contains(ImgFormatConstants.EXTENSION_SEPARATOR))// remove extension
			name = name.substring(0, name.lastIndexOf(ImgFormatConstants.EXTENSION_SEPARATOR));
		
		return name.split(fieldSeparator);
	}
}
