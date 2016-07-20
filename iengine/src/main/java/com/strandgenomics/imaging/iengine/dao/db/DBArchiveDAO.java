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
 * DBArchiveDAO.java
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
package com.strandgenomics.imaging.iengine.dao.db;

import java.math.BigInteger;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Archive;

public class DBArchiveDAO extends ImageSpaceDAO<Archive> implements ArchiveDAO {

	DBArchiveDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "ArchiveDAO.xml");
	}

	@Override
	public Archive insertArchive(BigInteger archiveHash, String rootFolder,
			String archiveFolder, List<ISourceReference> sourceFiles)
			throws DataAccessException 
	{
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_ARCHIVE");
        logger.logp(Level.INFO, "DBArchiveDAO", "insertArchive", "archiveHash="+archiveHash);
        
        sqlQuery.setValue("Signature",       Util.toHexString(archiveHash), Types.VARCHAR);
        sqlQuery.setValue("RootFolder",      rootFolder,      				Types.VARCHAR);
        sqlQuery.setValue("ArchiveFolder",   archiveFolder,  				Types.VARCHAR);
        sqlQuery.setValue("SourceFiles",     toByteArray(sourceFiles),      Types.BLOB);

        updateDatabase(sqlQuery);
        
        return findArchive(archiveHash);
	}

	@Override
	public Archive findArchive(BigInteger archiveHash) throws DataAccessException 
	{
		if(archiveHash == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.FINE, "DBArchiveDAO", "findArchive", "archiveHash="+archiveHash);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ARCHIVE_FOR_SIGNATURE");
        sqlQuery.setValue("Signature", Util.toHexString(archiveHash), Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}

	@Override
	public Archive createObject(Object[] columnValues) 
	{
		BigInteger signature = Util.toBigInteger( (String)columnValues[0] ) ;
		String rootFolder    = (String)columnValues[1];
		String archiveFolder = (String)columnValues[2];
		List<ISourceReference> sourceFiles = (List<ISourceReference>) toJavaObject((DataSource)columnValues[3]);
		
		return new Archive(signature, rootFolder, archiveFolder, sourceFiles);
	}

	@Override
	public void deleteArchive(BigInteger sign) throws DataAccessException
	{
		if(sign == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.INFO, "DBArchiveDAO", "findArchive", "archiveHash="+sign);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_ARCHIVE");
        sqlQuery.setValue("Signature", Util.toHexString(sign), Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public void updateArchiveRootFolder(BigInteger sign, String rootFolderName) throws DataAccessException
	{
		if(sign == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.INFO, "DBArchiveDAO", "updateArchiveRootFolder", "archiveHash="+sign+"root folder="+rootFolderName);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_ARCHIVE_ROOT_FOLDER");
        sqlQuery.setValue("Signature", Util.toHexString(sign), Types.VARCHAR);
        sqlQuery.setValue("RootFolder", rootFolderName, Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public void updateArchiveSignature(BigInteger oldSign, BigInteger newSign, List<ISourceReference> sourceFiles) throws DataAccessException
	{
		if(oldSign == null) throw new NullPointerException("unexpected null value");
		
        logger.logp(Level.INFO, "DBArchiveDAO", "updateArchiveSignature", "old archiveHash="+oldSign+"new hash="+newSign);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_SOURCE_REF");
        sqlQuery.setValue("OldSign", Util.toHexString(oldSign), Types.VARCHAR);
        sqlQuery.setValue("NewSign", Util.toHexString(newSign), Types.VARCHAR);
        sqlQuery.setValue("SourceFiles", toByteArray(sourceFiles), Types.BLOB);

        updateDatabase(sqlQuery);
	}

	@Override
	public void deleteDummySignatureMapping(BigInteger dummySignature) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBArchiveDAO", "deleteDummySignatureMapping", "old archiveHash="+dummySignature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_SIGNATURE_MAPPING");
        sqlQuery.setValue("OldSign", Util.toHexString(dummySignature), Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public BigInteger getComputedArchiveSignature(BigInteger dummySignature) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBArchiveDAO", "getComputedArchiveSignature", "old archiveHash="+dummySignature);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_NEW_SIGNATURE");
        sqlQuery.setValue("OldSign", Util.toHexString(dummySignature), Types.VARCHAR);
        
        String str = getString(sqlQuery);
		return Util.toBigInteger(str);
	}

	@Override
	public void insertSignatureMapping(BigInteger dummySignature, BigInteger newSignature) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBArchiveDAO", "insertSignatureMapping", "old archiveHash="+dummySignature+"new hash="+newSignature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_SIGNATURE_MAPPING");
        sqlQuery.setValue("OldSign", Util.toHexString(dummySignature), Types.VARCHAR);
        sqlQuery.setValue("NewSign", Util.toHexString(newSignature), Types.VARCHAR);

        updateDatabase(sqlQuery);
	}
}
