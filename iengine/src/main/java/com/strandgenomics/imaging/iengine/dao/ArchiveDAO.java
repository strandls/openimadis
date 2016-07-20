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
 * ArchiveDAO.java
 *
 * AVADIS Image Management System
 * Data Access Components
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
package com.strandgenomics.imaging.iengine.dao;

import java.math.BigInteger;
import java.util.List;

import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Archive;

public interface ArchiveDAO {
	
	/**
	 * Register the specified archive
	 * @param archiveHash
	 * @param rootFolder
	 * @param archiveFolder
	 * @param sourceFiles
	 * @return
	 * @throws DataAccessException
	 */
	public Archive insertArchive(BigInteger archiveHash, String rootFolder, String archiveFolder, 
			List<ISourceReference> sourceFiles) throws DataAccessException;
	
	/**
	 * Find the archive with the specified signature
	 * @param archiveHash signature of the archive
	 * @return the archive object if it is there, otherwise null
	 * @throws DataAccessException
	 */
	public Archive findArchive(BigInteger archiveHash) throws DataAccessException;

	/**
	 * Delete archive from DB
	 * @param sign archive signature
	 * @throws DataAccessException 
	 */
	public void deleteArchive(BigInteger sign) throws DataAccessException;

	/**
	 * updates archive root folder name
	 * @param sign archive signature
	 * @param rootFolderName new name of root folder
	 * @throws DataAccessException 
	 */
	public void updateArchiveRootFolder(BigInteger sign, String rootFolderName) throws DataAccessException;

	/**
	 * updates archive source ref and archive signature
	 * @param oldSign old signature
	 * @param newSign new signature
	 * @param sourceFiles updates list of source references
	 * @throws DataAccessException 
	 */
	public void updateArchiveSignature(BigInteger oldSign, BigInteger newSign, List<ISourceReference> sourceFiles) throws DataAccessException;

	/**
	 * deletes dummy signature to computed signature mapping from signature mapping registry
	 * @param dummySignature old signature
	 * @throws DataAccessException 
	 */
	public void deleteDummySignatureMapping(BigInteger dummySignature) throws DataAccessException;
	
	/**
	 * returns computed signature for specified dummy signature
	 * @param dummySignature old signature
	 * @return new signature
	 * @throws DataAccessException 
	 */
	public BigInteger getComputedArchiveSignature(BigInteger dummySignature) throws DataAccessException;
	
	/**
	 * inserts dummy signature to computed signature mapping
	 * @param dummySignature old signature
	 * @param newSignature new signature
	 * @throws DataAccessException 
	 */
	public void insertSignatureMapping(BigInteger dummySignature, BigInteger newSignature) throws DataAccessException;
}
