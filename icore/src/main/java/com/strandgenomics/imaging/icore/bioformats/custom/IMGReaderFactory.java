/**
 * IMGReaderFactory.java
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

import loci.formats.gui.BufferedImageReader;

import org.apache.log4j.Logger;

import com.strandgenomics.imaging.icore.bioformats.ImageReaderFactory;

public class IMGReaderFactory extends ImageReaderFactory {

	protected RecordMetaData specs = null;

	public IMGReaderFactory(RecordMetaData metaData) 
	{
		specs = metaData;
	}
	
	@Override
	public BufferedImageReader createBufferedImageReader() 
	{
		try
		{
			ImgFormatReader reader = new ImgFormatReader(specs);
			return new BufferedImageReader(reader); 
		}
		catch(Exception e)
		{
			Logger.getRootLogger().error("Cannot create Format Handler ..",e);
		}
		
		return null;
	}
}
