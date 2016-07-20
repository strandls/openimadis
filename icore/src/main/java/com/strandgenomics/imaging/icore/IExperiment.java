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
 * IExperiment.java
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
package com.strandgenomics.imaging.icore;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * A list of records acquired under a given experimental condition
 * Typically a multi-site/multi-series collection of records 
 * @author arunabha
 *
 */
public interface IExperiment {
	
	/**
	 * Returns a description of the original source files generating the experiment
	 * @return a description of the original source files generating the experiment
	 */
	public List<ISourceReference> getReference();
	
	/**
	 * Returns the MD5 signature of all the source-files combined (after sorting them w.r.t size followed by name)
	 * @return  the MD5 signature of all the source-files combined
	 */
	public BigInteger getMD5Signature();
	
	/**
	 * Returns the list of records (as a signature) within this experiment
	 * @return the list of records (as a signature) within this experiment
	 */
	public Collection<Signature> getRecordSignatures();
		
	/**
	 * Returns the member record with the specified signature
	 * @param signature the signature of the record
	 * @return the corresponding Record
	 */
	public IRecord getRecord(Signature signature);
	
	/**
	 * Export this Experiment as a tar(.gz) file containing the source-files
	 * within the specified target folder
	 * @param dir the target folder to download to
	 * @param name the name of the file within this folder
	 * @param compress whether to compress
	 * @return a tar.gz file containing the source-files
	 */
	public File export(File dir, String name, boolean compress) throws IOException;
}
