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

/**
 * BioExperiment.java
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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loci.formats.FormatTools;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.ome.OMEXMLMetadata;
import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Disposable;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.IRecordObserver;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.IValidator;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.bioformats.custom.ImgFormatConstants;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.ColorUtil;
import com.strandgenomics.imaging.icore.util.Util;

import ome.units.UNITS;
import ome.units.quantity.Length;

/**
 * Represents a set of related source files generated a bunch of records
 * @author arunabha
 *
 */
public abstract class BioExperiment implements IExperiment, Disposable, Serializable {
	
	/**
	 * file name constants used for identifying local and global attachments
	 */
	public static final String GlobalMetadata = "GlobalMetaData.tsv";// global attachment
	public static final String OMEXMLMetaData = "OMEXMLMetaData.xml";// global attachment
	public static final String SeriesMetadata = "SeriesMetadata_";// local attachment
	public static final String ImgFormatFile = "ImgFormatFile.txt";
	public static final String ACQUIRED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"; //format of the acquired as read using bioformts
	
	private static final long serialVersionUID = -8749958684585543335L;
	/**
	 * the list of records that are extracted
	 */
	protected Map<Signature, IRecord> members = null;
	/**
	 * List of source files
	 */
	protected Set<ISourceReference> sourceReferences = null;
	/**
	 * the seed file to initiate extraction
	 */
	protected File seedFile;
	/**
	 * md5 hash of the source files
	 */
	protected BigInteger md5Signature = null;
	/**
	 * checks whether the original experiment has been modified by deleting records and/or merging records
	 */
	private boolean modified = false;
	
	/**
	 * Creates a series with the specified source file 
	 * @param sourceFile the source file with possibly many companion files
	 */
	public BioExperiment(File sourceFile)
	{
		sourceReferences = new HashSet<ISourceReference>();
		
		members = new HashMap<Signature, IRecord>();
		seedFile = sourceFile.getAbsoluteFile();
	}
	
	/**
	 * Creates an experiment with existing references
	 * @param sourceReferences
	 */
	public BioExperiment(File seedFile, List<ISourceReference> sourceReferences)
	{
		this(seedFile);
		if(sourceReferences != null)
			this.sourceReferences.addAll(sourceReferences);
	}
	
	/**
	 * Returns true if the original experiment is modified by deleting records and/or merging records
	 * @return
	 */
	public boolean isModified()
	{
		return modified;
	}

	/**
	 * This method needs to implements by specific implementors
	 * @param usedFiles
	 */
	protected abstract void updateReference(String[] usedFiles);
	
	/**
	 * Returns the source file name of the source file
	 * @return the source file name of the source file
	 */
	public abstract String getSourceFilename();
	
	/**
	 * Returns the directory from which the files are extracted
	 * @return the directory from which the files are extracted
	 */
	public abstract String getRootDirectory();
	
    /**
     * Returns the MAC address - A Media Access Control address - (ethernet card) - network hardware address of the acquisition computer  
     * Note: more that one network interface may be associated with the computer, we may need to pick the first one after sorting
     */
    public abstract String getOriginMachineAddress();
    
    /**
     * Returns the Internet Protocol (IP) address of the acquisition computer 
     */
    public abstract String getOriginMachineIP();

    /**
     * Creates a record out of the information provided
     * @return the created record
     */ 
	public BioRecord createRecordObject(Date sourceFileTime, Date acquiredDate, 
			int noOfFrames, int noOfSlices, List<Channel> channels, List<Site> sites,
			int imageWidth, int imageHeight, PixelDepth pixelDepth, 
			double pixelSizeX, double pixelSizeY, double pixelSizeZ,
			ImageType imageType, SourceFormat sourceFormat)
	{
		return new BioRecord(this, sourceFileTime, acquiredDate, 
				noOfFrames, noOfSlices, channels, sites,
				imageWidth, imageHeight, pixelDepth, 
				pixelSizeX, pixelSizeY, pixelSizeZ,
				imageType, sourceFormat);
	}
	
	/**
	 * Returns the seed file generating the records
	 * @return the seed file generating the records
	 */
	public File getSeedFile()
	{
		return seedFile;
	}
	
	/**
	 * Returns number of records within this group
	 * @return number of records within this group
	 */
	public int size()
	{
		return members.size();
	}
	
	public int getSourceFileCount()
	{
		return sourceReferences.size();
	}
	
	@Override
	public Collection<Signature> getRecordSignatures()
	{
		return members.keySet();
	}
	
	@Override
	public IRecord getRecord(Signature signature)
	{
		return members.get(signature);
	}
	
	/**
	 * Returns the extracted records
	 */
	public Collection<IRecord> getRecords()
	{
		return members.values();
	}
	
	@Override
	public List<ISourceReference> getReference()
	{
		List<ISourceReference> sources = new ArrayList<ISourceReference>();
		sources.addAll(sourceReferences);
		
		return sources;
	}
	
	public void dispose()
	{
		if(members != null)
		{
			for(Map.Entry<Signature, IRecord> entry : members.entrySet())
			{
				entry.getValue().dispose();
			}
			
			members.clear();
		}
		
		members = null;
	}

	/**
	 * Extract the records from the source files
	 * @throws IOException 
	 */
	public void extractRecords(IRecordObserver observer, IValidator validator) throws IOException
	{
		if(observer != null && observer.isStopped())
			return;
		
		if(!members.isEmpty()) return; //already called - done
		Logger.getRootLogger().info("[Indexer]:\tTrying to create bioformat reader from "+seedFile);
		//fetches an image reader to extract records 
		int numberOfSeries = ImageManager.getInstance().initializeExperiment(this);

		if(validator != null)
		{
			Logger.getRootLogger().info("[Indexer]:\t validator not null ");
			
			boolean duplicate = validator.isDuplicate(this);
			if(duplicate)
			{
				Logger.getRootLogger().info("[Indexer]:\tIgnoring duplicate import. " + getSeedFile());
				return;
			}
			
			boolean uploaded = validator.isUploaded(this);
			Logger.getRootLogger().info("[Indexer]:\t Checking for uploaded " + uploaded);
			validator.setUploaded(this, uploaded);
		}
		
		extractSeries(observer, numberOfSeries);
	}
	
	protected void extractSeries(IRecordObserver observer, int numberOfSeries)
	{
		if(observer != null && observer.isStopped())
			return;
		
		for(int seriesNo = 0; seriesNo < numberOfSeries; seriesNo++)
		{
			if(observer != null && observer.isStopped())
				break;
			
			try
			{
				//create the record for the ith series
				Logger.getRootLogger().info("[Indexer]:\tcreating record for series "+seriesNo);
				BioRecord ithRecord = createRecord(seriesNo);
				
				Logger.getRootLogger().info("[Indexer]:\tinspecting pixel-data for series "+seriesNo);
				ithRecord.populatePixelData(); //now populate with member pixel data
				
				Logger.getRootLogger().info(" Record Signature : " + ithRecord.getSignature());
				members.put(ithRecord.getSignature(), ithRecord);
				
				System.out.println("extracted record for series "+seriesNo);
				
				Logger.getRootLogger().info("[Indexer]:\tGenerating thumbnail for record of series "+seriesNo);
				ithRecord.getThumbnail();
				Logger.getRootLogger().info("[Indexer]:\tSuccessfully generated thumbnail for record of series "+seriesNo);
				
				if(observer!=null) observer.recordAdded(ithRecord);
			}
			catch(Exception ex)
			{
				Logger.getRootLogger().error("[Indxer]:\tCould not create record for series "+seriesNo, ex);
			}
        	catch(OutOfMemoryError error)
        	{
        		Logger.getRootLogger().error("[Indxer]:\tOutOfMemoryError, could not create record for series "+seriesNo, error);
        		break;
        	}
		}
		
		Logger.getRootLogger().info("Finished Processing : " + seedFile.getName() + " created " + numberOfSeries + " records");
	}

	/**
	 * Create a Single Raw Record using a configured reader and series  
	 * @param formatHandler the format handler
	 * @param series the series number
	 * @return the raw records that is created
	 * @throws IOException 
	 */
	private BioRecord createRecord(int seriesNo) throws IOException
	{
		BufferedImageReader formatHandler = ImageManager.getInstance().getImageReader(this);
		try
		{
			Logger.getRootLogger().info("[Indexer]:\tCreating record for series "+seriesNo);
			formatHandler.setSeries(seriesNo);
			
			//Adding record meta-data fields ..
			IMetadata metaData = (IMetadata)formatHandler.getMetadataStore();
			String seriesName = Util.trim( metaData.getImageName(seriesNo) );
			seriesName = seriesName == null || seriesName.length() == 0 ? "Series "+seriesNo : seriesName;
			
			// Create the site
			List<Site> sites = new ArrayList<Site>(); 
			sites.add(new Site(seriesNo, seriesName));
			
			//the files generating this record
			String [] usedFiles = formatHandler.getSeriesUsedFiles();
			Logger.getRootLogger().info("[Indexer]:\tNumber of used files: " + usedFiles.length);
			
			Date sourceFileTime = new Date( new File(usedFiles[0]).getAbsoluteFile().lastModified() );
//			Date acquiredDate = extractAcquiredDate( metaData.getImageAcquiredDate(seriesNo) );
			Date acquiredDate = null;
			if(metaData.getImageAcquisitionDate(seriesNo)!=null)
				acquiredDate =  metaData.getImageAcquisitionDate(seriesNo).asDateTime(null).toDate(); //.asDate(); 
			
			int noOfFrames = formatHandler.getSizeT();
			int noOfSlices = formatHandler.getSizeZ();
			int noOfChannels = formatHandler.getSizeC();
			
			String[] primaryColorLUTs = LutLoader.getInstance().getPrimaryColorLUTs();
			int noOfColors = primaryColorLUTs.length;
			
			// Create dummy channel names
			List<Channel> channels = new ArrayList<Channel>(); 
			List<Integer> colorsLeft = new ArrayList<Integer>();// keeps indexes of colors that are not predicted in 
			for(int i = 0 ; i < 6 ; i++ ){
				colorsLeft.add(i);
			}
			boolean[] isColorFound = new boolean[noOfChannels]; //keep a track of all channels whose wavelength has been identified correctly. 
			//note: channel with gray colors are by default considered as not found in this array 
			for(int channel = 0; channel < noOfChannels; channel++){
				
				String channelName = null;
				
				//Reading channel name from metadata for effective C 
				if(channel < metaData.getChannelCount(seriesNo))
				{
					channelName = Util.trim( metaData.getChannelName(seriesNo, channel) );
				}
				
				if(channelName == null) channelName = "Channel " + channel;
				
				Channel ch = new Channel(channelName);
				Length emissionWavelength = null;
				try
				{
					 emissionWavelength = metaData.getChannelEmissionWavelength(seriesNo, channel);
				}
				catch (Exception e)
				{
					Logger.getRootLogger().info("[BioExperiment]:\tEmission Wavelenght not fount for channel= "+channelName);
				}
				
				if(emissionWavelength != null)
				{
					
					// Get the color from wavelength of the channel
					Color rgbColorObj = ColorUtil.getColor(emissionWavelength.value(UNITS.NM).longValue());
				
					// get the LUT name corresponding to the color identidfied
					String colorName = ColorUtil.getColorName(rgbColorObj) ;
					
					//if the color identified corresponds to any of RGBCMY , set it as channel's lut
					if (colorName != null)
					{
						int colorIndex = LutLoader.getInstance().getPrimaryLutIndex(colorName);
						if ((colorIndex >= 0) && (colorIndex < colorsLeft.size()))
						{
							ch.setLut(primaryColorLUTs[colorIndex]);
							colorsLeft.remove(colorIndex);// remove used color
															// from the list
							isColorFound[channel] = true; // toggle the boolean
															// for identified
															// channel
						}
					}
					ch.setWavelength(emissionWavelength.value(UNITS.NM).intValue());
				}
				
				channels.add(ch);
			}
	
			//assigning lut to channels (randomly) whose colours were not identified by their wavelength
			for (int channel = 0; channel < noOfChannels; channel++)
			{
				if ((!isColorFound[channel]))
				{
					// first use the colors which are not yet set to any channel
					if (!colorsLeft.isEmpty())
					{
						channels.get(channel).setLut(primaryColorLUTs[colorsLeft.get(0)]);
						colorsLeft.remove(0);
					}
					// then assign the colors as per the index of the channel
					else
					{
						channels.get(channel).setLut(primaryColorLUTs[channel % 6]);
						colorsLeft.remove(0);

					}

				}
			}
			
			if(noOfChannels==1)
			{
				channels.get(0).setLut("grays");
			}

			int imageWidth = formatHandler.getSizeX();
			int imageHeight = formatHandler.getSizeY();
			PixelDepth pixelDepth = extractPixelDepth(formatHandler.getPixelType());
	
			Length pixelX = metaData.getPixelsPhysicalSizeX(seriesNo);
			Length pixelY = metaData.getPixelsPhysicalSizeY(seriesNo);
			Length pixelZ = metaData.getPixelsPhysicalSizeZ(seriesNo);
			
			double pixelSizeX = pixelX == null ? 0 : pixelX.value(UNITS.MICROM).doubleValue();
			double pixelSizeY = pixelY == null ? 0 : pixelY.value(UNITS.MICROM).doubleValue(); 
			double pixelSizeZ = pixelZ == null ? 0 : pixelZ.value(UNITS.MICROM).doubleValue();
			
			// Check the image type
			ImageType imageType = formatHandler.isRGB() ? ImageType.RGB : ImageType.GRAYSCALE;
			// Check .. this should be string .. 
			SourceFormat sourceFormat = new SourceFormat(Util.trim(formatHandler.getFormat()));
	
			BioRecord record = createRecordObject(sourceFileTime, acquiredDate, 
					noOfFrames, noOfSlices, channels, sites,
					imageWidth, imageHeight, pixelDepth, 
					pixelSizeX, pixelSizeY, pixelSizeZ,
					imageType, sourceFormat);
			
			Logger.getRootLogger().info("[Indexer]:\tSuccessfully created record for series "+seriesNo +" with series name "+seriesName);
			
			try
			{
				File dumpFile;
				File dynamicFields;
				
				//Adding IMG file as attachment
				for(ISourceReference reference:record.getExperiment().getReference())
				{
					if(reference.getSourceFile().endsWith(ImgFormatConstants.IMG_FORMAT_EXTENSION))
					{
						record.addAttachment(new File(reference.getSourceFile()), ImgFormatFile, "Description of all the companion tiff", true);
						break;
					}
				}
				
		        //This is the global data for attachment 1: a tsv file
		        Map<String, Object> globalMetaData = formatHandler.getGlobalMetadata();
		        if(globalMetaData != null && globalMetaData.size()>0)
		        {
		        	dumpFile = File.createTempFile("global_meta_data", ".tsv", Constants.TEMP_DIR);
					dynamicFields = Util.dumpToFile(globalMetaData, dumpFile, "\t");
					record.addAttachment(dynamicFields, GlobalMetadata, "Global Meta Data", true);
		        }
				
		        //This is series meta data for attachment 2: a tsv file
		        Map<String, Object> seriesMetaData = formatHandler.getSeriesMetadata();
		        if(seriesMetaData != null && seriesMetaData.size()>0)
		        {
		        	dumpFile = File.createTempFile("series_meta_data", ".tsv", Constants.TEMP_DIR);
		        	dynamicFields = Util.dumpToFile(seriesMetaData, dumpFile, "\t");
					record.addAttachment(dynamicFields, SeriesMetadata + seriesNo + ".tsv" , "Series Meta Data", true);
		        }
				
				//This is ome xml meta data for attachment 2: a tsv file
				String xml = ((OMEXMLMetadata)(metaData)).dumpXML();
				dumpFile = File.createTempFile("ome_meta_data", ".xml", Constants.TEMP_DIR);
				dynamicFields = Util.dumpToFile(xml, dumpFile);
				record.addAttachment(dynamicFields, OMEXMLMetaData, "OME XML Meta Data", true);
				
				Logger.getRootLogger().info("[Indexer]:\tFound series meta data for series "+seriesNo);
			}
			catch(Exception ioex)
			{
				Logger.getRootLogger().error("[Indexer]:\tUnable to add original meta data as attachment for series "+seriesNo, ioex);
			}
			catch(AbstractMethodError what)
			{
				Logger.getRootLogger().error("[Indexer]:\tBioformat reader do not support extraction of original meta data "+seriesNo);
			}
			
			return record;
		}
		finally
		{
			try {
				formatHandler.close();
			} 
			catch (IOException e)
			{}
		}
	}
	
	/**
	 * parse the acquired date field from the given string value 
	 * (the format is assumed to be yyyy-MM-ddTHH:mm:ss)
	 * @param acquiredDate
	 * @return the date
	 */
	public static Date extractAcquiredDate(String acquiredDate) 
	{
		try
		{
			//format is yyyy-MM-ddTHH:mm:ss
			DateFormat formatter = new SimpleDateFormat(ACQUIRED_DATE_FORMAT);
		    return (Date)formatter.parse(acquiredDate);
		}
		catch(Exception ex)
		{
			return null;
		}
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
	
	/**
	 * Returns the md5 signature of this experiment
	 * for single file stuff, return the md5 hash of tht file.
	 * for multi-file experiment, calculate the md5 hash of each,
	 * then sort the files w.r.t their md5 hash (to get a unique order)
	 * and then compute the combined md5 hash f this files in that sorted order
	 */
	@Override
	public synchronized BigInteger getMD5Signature() 
	{
		if (md5Signature == null) 
		{
			Logger.getRootLogger().info(" [Indexing] : Computing signature");
			
			List<File> sourceFiles = new ArrayList<File>();
			for (ISourceReference ref : sourceReferences) 
			{
				sourceFiles.add( new File(ref.getSourceFile()) );
			}

			try 
			{
				md5Signature = Util.computeMD5Hash(sourceFiles.toArray(new File[0]));
			} 
			catch (IOException e) 
			{
				throw new RuntimeException(e);
			}
			Logger.getRootLogger().info(" [Indexing] : Done computing signature");
		}
		return md5Signature;
	}

	@Override
	public File export(File dir, String name, boolean compress) throws IOException 
	{
		List<File> sourceFiles = new ArrayList<File>();
		for (ISourceReference ref : sourceReferences) 
		{
			sourceFiles.add( new File(ref.getSourceFile()) );
		}
				
		return Archiver.createTarBall(dir, name, compress, sourceFiles.toArray(new File[0]));
	}
	
	/**
	 * Returns the list of signatures (their site list only
	 * @return
	 */
	public List<List<Site>> getSelectedSignatures()
	{
		if(!isModified()) return null;
		
		List<List<Site>> selectedSignatures = new ArrayList<List<Site>>();
		for(IRecord r : members.values())
		{
			List<Site> sList = new ArrayList<Site>();
			for(int i = 0;i < r.getSiteCount(); i++)
			{
				sList.add( r.getSite(i));
			}
			
			selectedSignatures.add(sList);
		}
		
		return selectedSignatures;
	}
	
	/**
	 * Merged all records within this experiment that match the dimensions of selected records
	 * as different sites of a single record
	 * Once the records are merged as a multi-site record, those individual records are 
	 * no longer available as records within this experiment.
	 * Note that merging of  records will be permitted if and only if the following fields 
	 * are identical
	 * 1. number of frames
	 * 2. number of slices (z)
	 * 3. number of channels
	 * 4. pixel depth
	 * 5. image width 
	 * 6. image height
	 * 7. pixel size along x,y, and z axis
	 * 8. image type (GRAYSCALE, RGB etc)
	 * @param record whose siblings(records with matching dimensions) need to be merged 
	 * @return the list of sibling records
	 */
	
	public List<IRecord> getAllSiblings(IRecord record)
	{
		Signature signature = record.getSignature();
		List <IRecord> siblings = new ArrayList<IRecord>();
		
		for(Signature otherSignature: members.keySet())
		{
			try
			{
				validateDimension(signature, otherSignature);
				siblings.add(members.get(otherSignature));
			}
			catch(Exception e)
			{
				System.err.println(e);
			}
		}
		return siblings;
	}
	
	/**
	 * Merged the specified records (two or more) within this experiment as different sites of a single record
	 * Once the specified records are merged as a multi-site record, those individual records are 
	 * no longer available as records within this experiment.
	 * Note that merging of  records will be permitted if and only if the following fields 
	 * are identical
	 * 1. number of frames
	 * 2. number of slices (z)
	 * 3. number of channels
	 * 4. pixel depth
	 * 5. image width 
	 * 6. image height
	 * 7. pixel size along x,y, and z axis
	 * 8. image type (GRAYSCALE, RGB etc)
	 * @param sites the specified list of signatures (of records of this experiment)
	 * @return the signature of the newly created merged record
	 */
	public Signature mergeRecordsAsSites(Signature ... records)
	{
		modified = true;

		//remove the signatures from the members
		List<Site> allSites = new ArrayList<Site>();
		List<IRecord> dumped = new ArrayList<IRecord>();
		
		BioRecord zerothRecord = (BioRecord) getRecord(records[0]);
		
		for(int i = 0; i < records.length; i++)
		{
			IRecord ithrecord = members.remove(records[i]);
			for(int site =  0; site < ithrecord.getSiteCount(); site++)
				allSites.add( ithrecord.getSite(site) );
			dumped.add(ithrecord);
		}

		BioRecord mergedRecord = createRecordObject(
				zerothRecord.getSourceFileTime(), 
				zerothRecord.getAcquiredDate(), 
				zerothRecord.getFrameCount(), 
				zerothRecord.getSliceCount(), 
				zerothRecord.channels, 
				allSites,
				zerothRecord.getImageWidth(), 
				zerothRecord.getImageHeight(), 
				zerothRecord.getPixelDepth(), 
				zerothRecord.getPixelSizeAlongXAxis(), 
				zerothRecord.getPixelSizeAlongYAxis(), 
				zerothRecord.getPixelSizeAlongZAxis(),
				zerothRecord.getImageType(), 
				zerothRecord.getSourceType());
		
		// Add global attachments once ..
		for(IAttachment attachment: zerothRecord.getAttachments())
		{
			if(attachment.getName().equals(BioExperiment.GlobalMetadata) || attachment.getName().equals(BioExperiment.OMEXMLMetaData)){
				try 
				{
					mergedRecord.addAttachment(attachment.getFile(), attachment.getName(), attachment.getNotes(), true);
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		
		for(IRecord r  : dumped)
		{
			// Add non-global attachments only
		    for(IAttachment attachment: r.getAttachments())
		    {
		    	if(attachment.getName().equals(BioExperiment.GlobalMetadata) || attachment.getName().equals(BioExperiment.OMEXMLMetaData))
		    		continue;
				try {
					mergedRecord.addAttachment(attachment.getFile(), attachment.getName(), attachment.getNotes(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		  //dispose existing stuff
			r.dispose();
		}
		
		//now add this merged record with this experiment
		members.put(mergedRecord.getSignature(), mergedRecord);
		//returns the signature
		return mergedRecord.getSignature();
	}
	
	public void removeRecord(Signature signature)
	{
		IRecord r = members.remove(signature);
		r.dispose();
		modified = true;
	}

	/**
	 * Checks if the following fields are identical in two records
	 * 1. number of frames
	 * 2. number of slices (z)
	 * 3. number of channels
	 * 4. pixel depth
	 * 5. image width 
	 * 6. image height
	 * 7. pixel size along x,y, and z axis
	 * 8. image type (GRAYSCALE, RGB etc)
	 * @param sites
	 * @exception IllegalArgumentException if the dimensions do not match
	 */
	public void validateDimension(Signature zeroth, Signature first) 
	{
		IRecord zerothRecord = getRecord(zeroth);
		
			if(zeroth.noOfFrames != first.noOfFrames)
				throw new IllegalArgumentException("frame count do not match");
			
			if(zeroth.noOfSlices != first.noOfSlices)
				throw new IllegalArgumentException("slice count do not match");
			
			if(zeroth.imageWidth != first.imageWidth)
				throw new IllegalArgumentException("image width count do not match");
			
			if(zeroth.imageHeight != first.imageHeight)
				throw new IllegalArgumentException("image height do not match");
			
			if(zeroth.getNoOfChannels() != first.getNoOfChannels())
				throw new IllegalArgumentException("channel count do not match");
			
			IRecord ithRecord = getRecord(first);
			
			if(!zerothRecord.getPixelDepth().equals( ithRecord.getPixelDepth() ) )
				throw new IllegalArgumentException("pixel depth do not match");
			
			if(!zerothRecord.getImageType().equals( ithRecord.getImageType() ) )
				throw new IllegalArgumentException("image type do not match");
			
			//TBD - doing floating point equality checks, 
			if( zerothRecord.getPixelSizeAlongXAxis() != ithRecord.getPixelSizeAlongXAxis() )
				throw new IllegalArgumentException("pixel size along x-axis do not match");
			
			if( zerothRecord.getPixelSizeAlongYAxis() != ithRecord.getPixelSizeAlongYAxis() )
				throw new IllegalArgumentException("pixel size along y-axis do not match");
			
			if( zerothRecord.getPixelSizeAlongZAxis() != ithRecord.getPixelSizeAlongZAxis() )
				throw new IllegalArgumentException("pixel size along z-axis do not match");
	}
	
	/**
	 * Checks if the following fields are identical in all records
	 * 1. number of frames
	 * 2. number of slices (z)
	 * 3. number of channels
	 * 4. pixel depth
	 * 5. image width 
	 * 6. image height
	 * 7. pixel size along x,y, and z axis
	 * 8. image type (GRAYSCALE, RGB etc)
	 * @param sites
	 * @exception IllegalArgumentException if the dimensions do not match
	 */
	public void validateDimensions(Signature ... sites) 
	{
		for(int i = 1; i < sites.length; i++)
		{
			validateDimension(sites[0],sites[i]);
		}
	}

	/**
	 * Returns the Image reader factory 
	 * @return
	 */
	public ImageReaderFactory getImageReaderFactory()
	{
		return new DefaultImageReaderFactory(this.seedFile);
	}
}
