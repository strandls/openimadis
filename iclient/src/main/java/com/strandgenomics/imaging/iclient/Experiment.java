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
