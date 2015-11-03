/*
 * GenericExperiment.java
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

import loci.formats.gui.BufferedImageReader;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.RawSourceReference;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.bioformats.ImageManager;
import com.strandgenomics.imaging.icore.bioformats.ImageReaderFactory;
import com.strandgenomics.imaging.icore.bioformats.custom.IMGReaderFactory;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;

/**
 * A Record data source backed up by RecordMetaData 
 * @author arunabha
 *
 */
public class GenericExperiment extends RawExperiment {
	
	private static final long serialVersionUID = 6580898524859269591L;
	/**
	 * The meta data that powers this experiment
	 */
	protected RecordMetaData metaData = null;

	public GenericExperiment(RecordMetaData metaData) throws IOException 
	{
		super(metaData.getSeed());
		this.metaData = metaData;
		//add all the source references
		for(File sourceFile : metaData.getFiles())
		{
			sourceReferences.add( new RawSourceReference(sourceFile) );
		}
		
		sourceReferences.add( new RawSourceReference(metaData.getSeed()) );
	}
	
	public BioRecord extractRecord()
	{
		BufferedImageReader formatHandler = null;
		BioRecord record = null;
		try
		{
			formatHandler = ImageManager.getInstance().getImageReader(this);
			//create the record for the ith series
			Logger.getRootLogger().info("[GenericExperiment]:\tcreating record ...");
			record = createRecord();
			
			Logger.getRootLogger().info("[GenericExperiment]:\tinspecting pixel-data for record "+metaData);
			record.populatePixelData(); //now populate with member pixel data
			
			Logger.getRootLogger().info(" Record Signature : " + record.getSignature());
			members.put(record.getSignature(), record);
		
			Logger.getRootLogger().info("[GenericExperiment]:\tGenerating thumbnail for record "+metaData);
			record.getThumbnail();
			Logger.getRootLogger().info("[GenericExperiment]:\tSuccessfully generated thumbnail for record  "+metaData);
		}
		catch(Exception ex)
		{
			Logger.getRootLogger().error("[GenericExperiment]:\tCould not create record "+metaData, ex);
		}
    	catch(OutOfMemoryError error)
    	{
    		Logger.getRootLogger().error("[GenericExperiment]:\tOutOfMemoryError, could not create record for "+metaData, error);
    	}
		finally
		{
			try {
				formatHandler.close();
			} 
			catch (IOException e)
			{}
		}
		
		return record;
	}
	
	private BioRecord createRecord() throws IOException
	{
		return createRecordObject(metaData.getSourceTime(), null, 
				metaData.getFrameCount(), metaData.getSliceCount(), 
				metaData.getChannels(), metaData.getSites(),
				metaData.getImageWidth(), metaData.getImageHeight(), 
				metaData.getPixelDepth(), 
				metaData.getPixelSizeX(), metaData.getPixelSizeY(), metaData.getPixelSizeZ(),
				metaData.getImageType(), metaData.getSourceFormat());
	}
	
	@Override
	public String getSourceFilename()
	{
		return metaData.getImageFile(new Dimension(0,0,0,0)).getName();
	}
	
	@Override
	public String getRootDirectory(){
		
		return metaData.getRootDirectory().getAbsolutePath();
	}
	
	/**
	 * Returns the Image reader factory 
	 * @return
	 */
	public ImageReaderFactory getImageReaderFactory()
	{
		return new IMGReaderFactory(metaData);
	}
}
