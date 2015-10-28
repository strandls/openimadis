/**
 * ImgFormatReader.java
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

package com.strandgenomics.imaging.icore.bioformats.custom;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import loci.formats.CoreMetadata;
import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.FormatTools;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.meta.MetadataStore;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;

public class ImgFormatReader extends FormatReader {

	private File imgSourceFile;

	/** Custom parser to read dimension to filename mapping */
	private RecordMetaData parser;

	/** Reader to invoke the underlying format for each source file. */
	private ImageReader actualReader = null;
	/** the file being currently parsed */
	private String currentFile = null;
	/** list of used files */
	private Set<String> usedFiles;
	/**
	 * the current series being handled
	 */
	private int currentSeriesNo = 0;
	/**
	 * the OME meta data store
	 */
	private ImgMetaDataStore imgMetaData = null;

	public ImgFormatReader() 
	{
		// Registers this reader for .img filename extensions
		super("IMG", new String[] { "img" });
		suffixNecessary = true;
		suffixSufficient = true;
		domains = new String[] { FormatTools.UNKNOWN_DOMAIN };
		currentSeriesNo = 0;
	}
	
	public ImgFormatReader(RecordMetaData parser) throws IOException, FormatException
	{
		this();
		this.parser = parser;
		this.imgSourceFile = parser.getSeed();
		initialize(imgSourceFile.getAbsolutePath());
	}
	
	private void initialize(String id) throws FormatException, IOException
	{
		actualReader = new ImageReader();
		setCurrentFile(getSampleFilePath());
		
		super.initFile(id);
		usedFiles = listUsedFiles();
	}
	
	/**
	 * Initializes the given file (parsing header information, etc.).
	 * Most subclasses should override this method to perform
	 * initialization operations such as parsing metadata.
	 */
	@Override
	protected void initFile(String id) throws FormatException, IOException 
	{
		if(parser != null) return;
		
		imgSourceFile = new File(id).getAbsoluteFile();
		parser = RecordMetaData.load(imgSourceFile);
		initialize(id);
	}
	
	@Override
	public int getIndex(int z, int c, int t)
	{
		  String order = RecordMetaData.dimensionOrder;
		  int zSize = parser.getSliceCount();
		  int cSize = parser.getChannelCount();
		  int tSize = parser.getFrameCount();
		  int num = zSize * cSize * tSize;
		  return FormatTools.getIndex(order, zSize, cSize, tSize, num, z, c, t);
	}
	
	private Set<String> listUsedFiles()
	{
		Set<String> usedFilesList = new HashSet<String>();
		usedFilesList.add(imgSourceFile.getAbsolutePath());
		
		for(File sourceFile : parser.getFiles())
		{
			usedFilesList.add( sourceFile.getAbsolutePath() );
		}

		return usedFilesList;
	}

	private String getSampleFilePath() 
	{
		Dimension d = parser.getSampleDimension();
		return parser.getImageFile(d).getAbsolutePath();
	}

	@Override
	public int getSeriesCount()
	{
		return parser.noOfSites;  //series is mapped to a site
	}
	
	@Override
	public void setSeries(int no) 
	{
		if(no < getSeriesCount())
		{
			currentSeriesNo = no;
			setCurrentFile(getSampleFilePath());
		}
	}
	
	@Override
	public int getSeries()
	{
		return currentSeriesNo;
	}
	
	/**
	 * @return number of channels in img record
	 */
	@Override
	public int getSizeC() 
	{
		return parser.noOfChannels;
	}

	/**
	 * @return number of frames in img record
	 */
	@Override
	public int getSizeT() 
	{
		return parser.noOfFrames;
	}

	/**
	 * @return number of frames in img record
	 */
	@Override
	public int getSizeZ() 
	{
		return parser.noOfSlices;
	}
	
	private void setCurrentFile(String filePath) 
	{
		try {
			if(filePath != null && currentFile != null && !currentFile.equals(filePath))
			{
				actualReader.close();
			}
			
			currentFile = filePath;
			actualReader.setId(filePath);
		} 
		catch (FormatException e) 
		{
			Logger.getRootLogger().error("IMGFormat reader unable to read file"+filePath);
			return;
		} 
		catch (IOException e)
		{
			Logger.getRootLogger().error("IMGFormat reader unable to read file"+filePath);
			return;
		}

	}
	
	private int setCurrentIndex(int planeIndex)
	{
		Dimension d = parser.getDimension(currentSeriesNo, planeIndex);
		File f = parser.getImageFile(d);
		setCurrentFile(f.getAbsolutePath());
		
		FieldType multiImageField = parser.getMultiImageField();
		
		int coOrdinate = 0;
		if(multiImageField.equals(FieldType.FRAME))
			coOrdinate = d.frameNo;
		else if(multiImageField.equals(FieldType.CHANNEL))
			coOrdinate = d.channelNo;
		else if(multiImageField.equals(FieldType.SLICE))
			coOrdinate = d.sliceNo;
		else if(multiImageField.equals(FieldType.SITE))
			coOrdinate = d.siteNo;
		
		return coOrdinate;
	}

	@Override
	public String[] getUsedFiles() 
	{
		return this.usedFiles.toArray(new String[0]);
	}

	@Override
	public int getEffectiveSizeC() 
	{
		return super.getEffectiveSizeC();
	}
	
	
	/* ******************************************************
	 * Just invoke actualReader functions
	 ********************************************************/
	@Override
	public int getImageCount()
	{
		int ct = getSizeZ() * getSizeT() * getSizeC();
		return ct;
	}
	
	@Override
	public boolean isRGB()
	{
		return ImageType.RGB == parser.imageType;
	}
	
	@Override
	public int getSizeX() 
	{
		return parser.getImageWidth();
	}
	
	@Override
	public int getSizeY()
	{
		return parser.getImageHeight();
	}
	
	@Override
	public int getPixelType() 
	{
		return parser.getPixelDepth().getByteSize();
	}
	
	@Override
	public int getBitsPerPixel() 
	{
		return parser.getPixelDepth().getBitSize();
	}
	
	@Override
	public int getRGBChannelCount() 
	{
		return actualReader.getRGBChannelCount();
	}
	
	@Override
	public boolean isIndexed() 
	{
		return actualReader.isIndexed();
	}
	
	@Override
	public boolean isFalseColor() 
	{
		return actualReader.isFalseColor();
	}
	
	@Override
	public byte[][] get8BitLookupTable() throws FormatException, IOException
	{
		return actualReader.get8BitLookupTable();
	}
	
	@Override
	public short[][] get16BitLookupTable() throws FormatException, IOException
	{
		return actualReader.get16BitLookupTable();
	}
	
	@Override
	public int[] getChannelDimLengths()
	{
		return actualReader.getChannelDimLengths(); //TODO
	}
	
	@Override
	public String[] getChannelDimTypes() 
	{
		return actualReader.getChannelDimTypes();
	}
	
	@Override
	public int getThumbSizeX()
	{
		return actualReader.getThumbSizeX();
	}
	
	@Override
	public int getThumbSizeY()
	{
		return actualReader.getThumbSizeY();
	}
	
	@Override
	public boolean isLittleEndian() 
	{
		return parser.isLittleEndian();
	}
	
	@Override
	public String getDimensionOrder() 
	{
		return RecordMetaData.dimensionOrder;
	}
	
	@Override
	public boolean isOrderCertain() 
	{
		return actualReader.isOrderCertain();
	}
	
	@Override
	public boolean isThumbnailSeries()
	{
		return actualReader.isThumbnailSeries();
	}
	
	@Override
	public boolean isInterleaved() 
	{
		return actualReader.isInterleaved();
	}
	
	@Override
	public boolean isInterleaved(int subC)
	{
		return actualReader.isInterleaved(subC);
	}

	
	@Override
	public byte[] openBytes(int no) throws FormatException, IOException 
	{
		int index = setCurrentIndex(no);
		System.out.println("here");
		return actualReader.openBytes(index);
	}
	
	@Override
	public byte[] openBytes(int no, int x, int y, int w, int h) throws FormatException, IOException 
	{
		int index = setCurrentIndex(no);
		System.out.println("here");
		return actualReader.openBytes(index, x, y, w, h);
	}
	
	@Override
	public byte[] openBytes(int no, byte[] buf) throws FormatException, IOException 
	{
		int index = setCurrentIndex(no);
		System.out.println("here");
		return actualReader.openBytes(index, buf);
	}
	
	@Override
	public byte[] openBytes(int no, byte[] buf, int x, int y, int w, int h)
			throws FormatException, IOException {
		int index = setCurrentIndex(no);
		System.out.println("here");
		return actualReader.openBytes(index, buf, x, y, w, h);
	}
	
	@Override
	public Object openPlane(int no, int x, int y, int w, int h)
			throws FormatException, IOException {
		int index = setCurrentIndex(no);
		System.out.println("here");
		return actualReader.openPlane(index, x, y, w, h);
	}

	@Override
	public byte[] openThumbBytes(int no) throws FormatException, IOException {
		setCurrentIndex(no);
		return actualReader.openThumbBytes(no);
	}
	
	@Override
	public void close() throws IOException 
	{
		actualReader.close();
	}
	
	@Override
	public void setNormalized(boolean normalize)
	{
		actualReader.setNormalized(normalize);
	}
	
	@Override
	public boolean isNormalized()
	{
		return actualReader.isNormalized();
	}
	
	@Override
	public void setOriginalMetadataPopulated(boolean populate)
	{
		actualReader.setOriginalMetadataPopulated(populate);
	}
	
	@Override
	public boolean isOriginalMetadataPopulated()
	{
		return actualReader.isOriginalMetadataPopulated();
	}
	
	@Override
	public void setGroupFiles(boolean groupFiles) 
	{
		actualReader.setGroupFiles(groupFiles);
	}
	
	@Override
	public boolean isGroupFiles() 
	{
		return actualReader.isGroupFiles();
	}
	
	@Override
	public boolean isMetadataComplete() 
	{
		return actualReader.isMetadataComplete();
	}
	
	@Override
	public int fileGroupOption(String id) throws FormatException, IOException
	{
		return actualReader.fileGroupOption(id);
	}

	@Override
	public String getCurrentFile() 
	{
		return actualReader.getCurrentFile();
	}
	
	@Override
	public String[] getDomains() 
	{
		return actualReader.getDomains();
	}
	
	@Override
	public int[] getZCTCoords(int planeIndex)
	{
		Dimension d = parser.getDimension(currentSeriesNo, planeIndex);
		if(d == null) return null;
		return new int[] {d.sliceNo, d.channelNo, d.frameNo};
	}
	
	@Override
	public Object getMetadataValue(String field)
	{
		return actualReader.getMetadataValue(field);
	}
	
	@Override
	public Hashtable<String, Object> getGlobalMetadata() 
	{
		return actualReader.getGlobalMetadata();
	}
	
	@Override
	public Hashtable<String, Object> getSeriesMetadata()
	{
		return actualReader.getSeriesMetadata();
	}
	
	@Override
	public CoreMetadata[] getCoreMetadata() 
	{
		return actualReader.getCoreMetadata();
	}
	
	@Override
	public void setMetadataStore(MetadataStore store)
	{
		//does nothing
	}
	
	@Override
	public MetadataStore getMetadataStore() 
	{
		if(imgMetaData == null)
		{
			imgMetaData = new ImgMetaDataStore(parser);
		}
		
		return imgMetaData;
	}
	
	@Override
	public IFormatReader[] getUnderlyingReaders() {
		return actualReader.getUnderlyingReaders();
	}
	
	@Override
	public boolean isSingleFile(String id) throws FormatException, IOException {
		return actualReader.isSingleFile(id);
	}
	
	@Override
	public String[] getPossibleDomains(String id) throws FormatException,
			IOException {
		return actualReader.getPossibleDomains(id);
	}
	
	@Override
	public boolean hasCompanionFiles() {
		return actualReader.hasCompanionFiles();
	}
	
	@Override
	public void setMetadataCollected(boolean collect) {
		actualReader.setMetadataCollected(collect);
	}
	
	@Override
	public boolean isMetadataCollected() {
		return actualReader.isMetadataCollected();
	}
	
	@Override
	public Hashtable<String, Object> getMetadata() {
		return actualReader.getMetadata();
	}	
}