/*
 * Experiment.java
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.util.Util;

public class Experiment extends ImageSpaceObject implements IExperiment {
	
	private static final long serialVersionUID = -4629778273839122971L;
	/**
	 * MD5 signature of all the source-files combined (after sorting them w.r.t size followed by name)
	 */
	protected BigInteger experimentID = null;
	/**
	 * List of records within this experiment
	 */
	protected List<Signature> recordSignatures = null;
	/**
	 * List of source files
	 */
	protected List<ISourceReference> sourceReferences = null;
	
	Experiment(BigInteger experimentID)
	{
		this.experimentID = experimentID;
	}

	@Override
	public synchronized List<ISourceReference> getReference() 
	{
		if(sourceReferences == null)
		{
			//makes a system call to get it done
			sourceReferences = getImageSpace().getSourceReferences(this);
		}
		return sourceReferences;
	}

	@Override
	public BigInteger getMD5Signature() 
	{
		return experimentID;
	}

	@Override
	public synchronized Collection<Signature> getRecordSignatures() 
	{
		if(recordSignatures == null)
		{
			//makes a system call to get it done
			recordSignatures = getImageSpace().getSignaturesForExperiment(this);
		}
		return recordSignatures;
	}
	
	@Override
	public IRecord getRecord(Signature signature)
	{
		return getImageSpace().getRecordForExperiment(this, signature);
	}
	
	@Override
	public synchronized File export(File dir, String name, boolean compress) throws IOException
	{
		File target = new File(dir, name);
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		try
		{
			in = new BufferedInputStream(getImageSpace().exportExperiment(this.experimentID));
			out = new BufferedOutputStream(new FileOutputStream(target));
			Util.transferData(in, out);
		}
		finally
		{
			Util.closeStreams(in, out);
		}
		
		return target;
	}
	
	@Override
	public void dispose() 
	{
		recordSignatures = null;
		sourceReferences = null;
		experimentID = null;
	}

	public Uploader upload(Project project) throws IOException 
	{
		throw new RuntimeException("call is not valid");
	}
	
	/**
	 * Returns the list of records (their guids) associated with this experiment
	 * @return the list of records (their guids) associated with this experiment
	 */
	public long[] getGUIDs()
	{
		return getImageSpace().getGUIDsForArchive(this);
	}
}
