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
 * ClientExperiment.java
 *
 * AVADIS Image Management System
 * Core Engine Bio-format Components
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
package com.strandgenomics.imaging.iengine.bioformats;

import java.math.BigInteger;
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
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;

/**
 * Multi-series records extracted from files uploaded by the client
 * @author arunabha
 *
 */
public class ClientExperiment extends BioExperiment {
	
	private static final long serialVersionUID = 2507015670793525619L;
	/**
	 * The specification of the client request
	 */
	protected RecordCreationRequest context;
	/**
	 * the java logger
	 */
	protected transient Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.bioformats");
	
	/**
	 * Creates a multi-series record with the specified client context
	 * @param context the client context
	 */
	public ClientExperiment(RecordCreationRequest context)
	{
		super(context.getSeedFile(), context.getClientFiles());
		this.context = context;
		
		logger.logp(Level.INFO, "ClientExperiment", "createRecordObject", "created experiment for context "+context);
	}
	
	@Override
	public BioRecord createRecordObject(Date sourceFileTime, Date acquiredDate, 
			int noOfFrames, int noOfSlices, List<Channel> channels, List<Site> sites,
			int imageWidth, int imageHeight, PixelDepth pixelDepth, 
			double pixelSizeX, double pixelSizeY, double pixelSizeZ,
			ImageType imageType, SourceFormat sourceFormat)
	{
		logger.logp(Level.INFO, "ClientExperiment", "createRecordObject", "creating record for series "+this +" for context "+context);
		
		return new ClientRecord(this, sourceFileTime, acquiredDate, 
				noOfFrames, noOfSlices, channels, sites,
				imageWidth, imageHeight, pixelDepth, 
				pixelSizeX, pixelSizeY, pixelSizeZ,
				imageType, sourceFormat);
	}
	
	public RecordCreationRequest getContext()
	{
		return context;
	}
	
	@Override
	public BigInteger getMD5Signature() 
	{
		return context.getArchiveHash();
	}

	@Override
	protected void updateReference(String[] usedFiles) 
	{
		//do nothing here
	}
	
	@Override
	public String getSourceFilename()
	{
		return context.getSourceFilename();
	}

	@Override
	public String getRootDirectory() 
	{
		return context.getSourceDirectory();
	}

	@Override
	public String getOriginMachineAddress() 
	{
		return context.getClientMacAddress();
	}

	@Override
	public String getOriginMachineIP()
	{
		return context.getClientIPAddress();
	}
}
