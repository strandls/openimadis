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
 * DBRecordDAO.java
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
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.RecordMarker;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.LengthUnit;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.TimeUnit;

/**
 * gives access to a record within a project
 * @author arunabha
 *
 */
public class DBRecordDAO extends ImageSpaceDAO<Record> implements RecordDAO {
	
	DBRecordDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "RecordDAO.xml");
	}
	
	@Override
	public int getProjectID(long guid) throws DataAccessException
	{
        logger.logp(Level.FINE, "DBRecordDAO", "getProjectID", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);

        return getInteger(sqlQuery);
	}
	
	@Override
	public List<Channel> getChannels(long guid) throws DataAccessException
	{
        logger.logp(Level.FINE, "DBRecordDAO", "getChannels", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_CHANNELS_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        DataSource ds = (DataSource) getObject(sqlQuery);
        return (List<Channel>) toJavaObject( ds );
	}
	
	@Override
	public List<Site> getSites(long guid) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getSites", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SITES_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        DataSource ds = (DataSource) getObject(sqlQuery);
        return (List<Site>) toJavaObject( ds );
	}
	
	@Override
	public BigInteger getArchiveSignature(long guid) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getSites", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ARCHIVE_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        return Util.toBigInteger( getString(sqlQuery) );
	}
	
	@Override
	public BigInteger getArchiveSignature(long guid, RecordMarker marker) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getArchiveSignature", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ARCHIVE_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", marker.name(), Types.VARCHAR);

        return Util.toBigInteger( getString(sqlQuery) );
	}
	
	@Override
	public Dimension getDimension(long guid) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getImageSize", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_DIMENSION_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        
        int[] d = getColumnsWithIntValues(sqlQuery);
        //int frameNumber, int sliceNumber, int channelNumber, int siteNumber
        return new Dimension(d[0], d[1], d[2], d[3]);
	}
	
	@Override
	public java.awt.Dimension getImageSize(long guid) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getImageSize", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_IMAGE_DIMENSION_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        
        int[] sizes = getColumnsWithIntValues(sqlQuery);
        return new java.awt.Dimension(sizes[0], sizes[1]);
	}
	
	@Override
	public PixelDepth getImageDepth(long guid) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getImageDepth", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_IMAGE_DEPTH_FOR_GUID");
        sqlQuery.setValue("GUID", guid, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        
        int byteSize = getInteger(sqlQuery);
        return toPixelDepth(byteSize);
	}
	
	@Override
	public long[] getProjectAndGuid(Signature signature) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getProjectAndGuid", "for signature "+signature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_N_GUID_FOR_SIGNATURE");

        sqlQuery.setValue("SliceCount",   signature.noOfSlices,    Types.INTEGER);
        sqlQuery.setValue("FrameCount",   signature.noOfFrames,    Types.INTEGER);
        sqlQuery.setValue("ChannelCount", signature.noOfChannels,  Types.INTEGER);
        sqlQuery.setValue("SiteCount",    signature.noOfSites,     Types.INTEGER);
        
        sqlQuery.setValue("ImageWidth",   signature.imageWidth,    Types.INTEGER);
        sqlQuery.setValue("ImageHeight",  signature.imageHeight,   Types.INTEGER);

		sqlQuery.setValue("SiteSignature",    Util.toHexString(signature.siteHash),    Types.VARCHAR);
		sqlQuery.setValue("ArchiveSignature", Util.toHexString(signature.archiveHash), Types.VARCHAR);
		sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        return this.getColumnsWithLongValues(sqlQuery);
	}
	
	@Override
	public long[] getAllProjectAndGuid() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordDAO", "getAllProjectAndGuid", "All Project And Guid");

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_PROJECT_N_GUID");
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        return this.getColumnsWithLongValues(sqlQuery);
	}
	
	@Override
	public Long findGUID(Signature signature) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "findGUID", "for signature "+signature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_GUID_FOR_SIGNATURE");

        sqlQuery.setValue("SliceCount",   signature.noOfSlices,    Types.INTEGER);
        sqlQuery.setValue("FrameCount",   signature.noOfFrames,    Types.INTEGER);
        sqlQuery.setValue("ChannelCount", signature.noOfChannels,  Types.INTEGER);
        sqlQuery.setValue("SiteCount",    signature.noOfSites,     Types.INTEGER);
        
        sqlQuery.setValue("ImageWidth",   signature.imageWidth,    Types.INTEGER);
        sqlQuery.setValue("ImageHeight",  signature.imageHeight,   Types.INTEGER);

		sqlQuery.setValue("SiteSignature",    Util.toHexString(signature.siteHash),    Types.VARCHAR);
		sqlQuery.setValue("ArchiveSignature", Util.toHexString(signature.archiveHash), Types.VARCHAR);
		sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        Long value = getLong(sqlQuery);
        logger.logp(Level.FINEST, "DBRecordDAO", "findGUID", "found value "+value);
        return value;
	}

	@Override
	public Long findGUIDForRecordBuilder(Signature signature) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "findGUID", "for signature "+signature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_GUID_FOR_SIGNATURE");

        sqlQuery.setValue("SliceCount",   signature.noOfSlices,    Types.INTEGER);
        sqlQuery.setValue("FrameCount",   signature.noOfFrames,    Types.INTEGER);
        sqlQuery.setValue("ChannelCount", signature.noOfChannels,  Types.INTEGER);
        sqlQuery.setValue("SiteCount",    signature.noOfSites,     Types.INTEGER);
        
        sqlQuery.setValue("ImageWidth",   signature.imageWidth,    Types.INTEGER);
        sqlQuery.setValue("ImageHeight",  signature.imageHeight,   Types.INTEGER);

		sqlQuery.setValue("SiteSignature",    Util.toHexString(signature.siteHash),    Types.VARCHAR);
		sqlQuery.setValue("ArchiveSignature", Util.toHexString(signature.archiveHash), Types.VARCHAR);
		sqlQuery.setValue("RecordMarker", RecordMarker.UNDER_CONSTRUCTION.name(), Types.VARCHAR);

        Long value = getLong(sqlQuery);
        logger.logp(Level.FINEST, "DBRecordDAO", "findGUID", "found value "+value);
        return value;
	}
	
	@Override
	public void insertRecord(int projectID, String uploadedBy, 
			int noOfSlices, int noOfFrames, int imageWidth, int imageHeight,
			BigInteger siteSignature, BigInteger archiveSignature,
			Long uploadTime, Long sourceTime, Long creationTime, Long acquiredTime,
			PixelDepth imageDepth, double xPixelSize, double yPixelSize, double zPixelSize,
			SourceFormat sourceType, ImageType imageType, 
			String machineIP, String macAddress,
			String sourceFolder, String sourceFilename, 
			List<Channel> channels, List<Site> sites, RecordMarker recordMarker, String microscopeName) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBRecordDAO", "insertRecord", "inserting record for project "+projectID);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_RECORD");

		sqlQuery.setValue("ProjectID",  projectID,        Types.INTEGER);
		sqlQuery.setValue("UploadedBy", uploadedBy,       Types.VARCHAR);
		
		sqlQuery.setValue("SliceCount",   noOfSlices,      Types.INTEGER);
		sqlQuery.setValue("FrameCount",   noOfFrames,      Types.INTEGER);
		sqlQuery.setValue("ChannelCount", channels.size(), Types.INTEGER);
		sqlQuery.setValue("SiteCount",    sites.size(),    Types.INTEGER);
		sqlQuery.setValue("ImageWidth",   imageWidth,      Types.INTEGER);
		sqlQuery.setValue("ImageHeight",  imageHeight,     Types.INTEGER);
		
		sqlQuery.setValue("SiteSignature",    Util.toHexString(siteSignature),    Types.VARCHAR);
		sqlQuery.setValue("ArchiveSignature", Util.toHexString(archiveSignature), Types.VARCHAR);
		
		sqlQuery.setValue("UploadTime",   new Timestamp(uploadTime.longValue()),   Types.TIMESTAMP);
		sqlQuery.setValue("SourceTime",   new Timestamp(sourceTime.longValue()),   Types.TIMESTAMP);
		sqlQuery.setValue("CreationTime", new Timestamp(creationTime.longValue()), Types.TIMESTAMP);
		
		Timestamp t = null;
		if(acquiredTime != null)
			t = new Timestamp(acquiredTime.longValue());
		
		logger.logp(Level.INFO, "DBRecordDAO", "insertRecord", "acquired time for the record "+t);
		sqlQuery.setValue("AcquiredTime", t, Types.TIMESTAMP);

		sqlQuery.setValue("ImageDepth", imageDepth.getByteSize(), Types.INTEGER);
		sqlQuery.setValue("PixelSizeX", xPixelSize, Types.DOUBLE);
		sqlQuery.setValue("PixelSizeY", yPixelSize, Types.DOUBLE);
		sqlQuery.setValue("PixelSizeZ", zPixelSize, Types.DOUBLE);
		
		sqlQuery.setValue("SourceType", sourceType.name,  Types.VARCHAR);
		sqlQuery.setValue("ImageType",  imageType.name(), Types.VARCHAR);
		
		sqlQuery.setValue("MachineIP",  machineIP,        Types.VARCHAR);
		sqlQuery.setValue("MacAddress", macAddress,       Types.VARCHAR);

		sqlQuery.setValue("SourceFolder",   sourceFolder, Types.VARCHAR);
		sqlQuery.setValue("SourceFilename", sourceFilename, Types.VARCHAR);

		sqlQuery.setValue("Channels",  toByteArray(channels), Types.BLOB);
		sqlQuery.setValue("Sites",     toByteArray(sites),    Types.BLOB);
		
		sqlQuery.setValue("RecordMarker", recordMarker.name(), Types.VARCHAR);
		
		AcquisitionProfile profile = new AcquisitionProfile(microscopeName, xPixelSize, yPixelSize, zPixelSize, sourceType, TimeUnit.MILISECONDS, TimeUnit.MILISECONDS, LengthUnit.MICROMETER);
		sqlQuery.setValue("AcqProfile", toByteArray(profile), Types.BLOB);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public Record findRecord(long guid) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBRecordDAO", "findRecord", "guid="+guid);
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECORD_FOR_GUID");
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        sqlQuery.setValue("GUID", guid, Types.BIGINT);

        return fetchInstance(sqlQuery);
	}
	
	@Override
	public List<Record> findRecords(Set<Long> guids) throws DataAccessException 
	{
		if(guids == null || guids.isEmpty()) return null;
        logger.logp(Level.INFO, "DBRecordDAO", "findRecords", "no of records requested="+guids.size());
        
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECORD_FOR_GUID");
        sqlQuery.setValue("GUID", guids, Types.BIGINT);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        RowSet<Record> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	public List<Record> findRecord(int projectID) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBRecordDAO", "findRecord", "project="+projectID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECORDS_FOR_PROJECT");
        sqlQuery.setValue("ProjectID",  projectID,  Types.INTEGER);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        sqlQuery.setValue("Limit",  null,  Types.INTEGER);// no limit specified

        RowSet<Record> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	public List<Record> findRecord(int projectID, int limit)
			throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordDAO", "findRecord", "project="+projectID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECORDS_FOR_PROJECT");
        sqlQuery.setValue("ProjectID",  projectID,  Types.INTEGER);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        sqlQuery.setValue("Limit",  new Integer(limit),  Types.INTEGER);

        RowSet<Record> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	public long[] getRecordGUIDs(int projectID)  throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getRecordGUIDs", "project="+projectID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_GUIDS_FOR_PROJECT");
        sqlQuery.setValue("ProjectID",  projectID,  Types.INTEGER);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        return getRowsWithLongValues(sqlQuery);
	}
	
	@Override
	public long[] getGUIDsForArchive(BigInteger archiveSignature)  throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getGUIDsForArchive", "archiveSignature="+archiveSignature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_GUIDS_FOR_ARCHIVE");
        sqlQuery.setValue("Archive",  Util.toHexString(archiveSignature),  Types.VARCHAR);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        return getRowsWithLongValues(sqlQuery);
	}
	
	@Override
	public long[] getDeletedGUIDsForArchive(BigInteger archiveSignature)  throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getGUIDsForArchive", "archiveSignature="+archiveSignature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_DELETED_GUIDS_FOR_ARCHIVE");
        sqlQuery.setValue("Archive",  Util.toHexString(archiveSignature),  Types.VARCHAR);
        sqlQuery.setValue("RecordMarker", RecordMarker.Deleted.name(), Types.VARCHAR);

        return getRowsWithLongValues(sqlQuery);
	}
	
	@Override
	public long[] getAllGUIDsForArchive(BigInteger archiveSignature) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordDAO", "getGUIDsForArchive", "archiveSignature="+archiveSignature);

		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_GUIDS_FOR_ARCHIVE");
		sqlQuery.setValue("Archive", Util.toHexString(archiveSignature),Types.VARCHAR);

		return getRowsWithLongValues(sqlQuery);
	}
	
	@Override
	public Record findRecord(Signature signature) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBRecordDAO", "findRecord", "for signature "+signature);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_RECORD_FOR_SIGNATURE");

        sqlQuery.setValue("SliceCount",   signature.noOfSlices,    Types.INTEGER);
        sqlQuery.setValue("FrameCount",   signature.noOfFrames,    Types.INTEGER);
        sqlQuery.setValue("ChannelCount", signature.noOfChannels,  Types.INTEGER);
        sqlQuery.setValue("SiteCount",    signature.getNoOfSites(),Types.INTEGER);
        
        sqlQuery.setValue("ImageWidth",   signature.imageWidth,    Types.INTEGER);
        sqlQuery.setValue("ImageHeight",  signature.imageHeight,   Types.INTEGER);

		sqlQuery.setValue("SiteSignature",    Util.toHexString(signature.siteHash),    Types.VARCHAR);
		sqlQuery.setValue("ArchiveSignature", Util.toHexString(signature.archiveHash), Types.VARCHAR);
		sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}
	
	@Override
	public Set<Integer> getProjectForArchive(BigInteger archiveSignature) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getProjectForArchive", "for archive "+archiveSignature);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECTS_FOR_ARCHIVE");
        sqlQuery.setValue("Archive",   Util.toHexString(archiveSignature),    Types.VARCHAR);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        
        int[] projectIDs = this.getRowsWithIntValues(sqlQuery);
        if(projectIDs == null || projectIDs.length == 0) return null;
        
        Set<Integer> projects = new HashSet<Integer>();
        for(int projectID : projectIDs)
        {
        	projects.add(projectID);
        }
        
        return projects;
	}
	
	@Override
	public Set<BigInteger> getArchivesForProject(int projectID) throws DataAccessException
	{
        logger.logp(Level.INFO, "DBRecordDAO", "getArchivesForProject", "for projectID = "+projectID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ARCHIVES_FOR_PROJECT");
        sqlQuery.setValue("ProjectID",   projectID,    Types.INTEGER);
        sqlQuery.setValue("RecordMarker", RecordMarker.Active.name(), Types.VARCHAR);
        
        String[] archiveSignatures = this.getRowsWithStringValues(sqlQuery);
        if(archiveSignatures == null || archiveSignatures.length == 0) return null;
        
        Set<BigInteger> signatures = new HashSet<BigInteger>();
        for(String hexString : archiveSignatures)
        {
        	signatures.add( Util.toBigInteger(hexString) );
        }
        
        return signatures;
	}
	
	@Override
	public Record createObject(Object[] columnValues) 
	{	
		long guid      = Util.getLong(columnValues[0]);
		int projectID  = Util.getInteger(columnValues[1]);
		String uploadedBy = (String) columnValues[2];
		
		int sliceCount   = Util.getInteger(columnValues[3]);
		int frameCount   = Util.getInteger(columnValues[4]);
		int channelCount = Util.getInteger(columnValues[5]);
		int siteCount    = Util.getInteger(columnValues[6]);
		int imageWidth   = Util.getInteger(columnValues[7]);
		int imageHeight  = Util.getInteger(columnValues[8]);
		
		BigInteger siteHash    = Util.toBigInteger( (String)columnValues[9] );
		BigInteger archiveHash = Util.toBigInteger( (String)columnValues[10] );
		
		Timestamp ut = Util.getTimestamp(columnValues[11]);
		Long uploadTime = ut == null ? null : ut.getTime();
		
		Timestamp st = Util.getTimestamp(columnValues[12]);
		Long sourceTime = st == null ? null : st.getTime();
		
		Timestamp ct = Util.getTimestamp(columnValues[13]);
		Long creationTime = ct == null ? null : ct.getTime();
		
		Timestamp at = Util.getTimestamp(columnValues[14]);
		Long acquiredTime = at == null ? null : at.getTime();
		
		PixelDepth imageDepth = toPixelDepth( Util.getInteger(columnValues[15]) );
		
		double xPixelSize = Util.getDouble(columnValues[16]);
		double yPixelSize = Util.getDouble(columnValues[17]);
		double zPixelSize = Util.getDouble(columnValues[18]);
		
		SourceFormat sourceType = new SourceFormat( (String)columnValues[19]  );
		ImageType imageType = ImageType.valueOf( (String)columnValues[20] );
		
		String machineIP  = (String)columnValues[21];
		String macAddress = (String)columnValues[22];
		
		String sourceFolder   = (String)columnValues[23];
		String sourceFilename = (String)columnValues[24];
		
		List<Channel> channels = (List<Channel>) toJavaObject( (DataSource)columnValues[25]);
		List<Site> sites = (List<Site>) toJavaObject( (DataSource)columnValues[26]);
		
		AcquisitionProfile profile = (AcquisitionProfile) toJavaObject( (DataSource)columnValues[27]);

		return new Record(guid, projectID, uploadedBy, 
				sliceCount, frameCount, imageWidth, imageHeight,
				siteHash, archiveHash,
				uploadTime, sourceTime, creationTime, acquiredTime,
				imageDepth, xPixelSize, yPixelSize, zPixelSize,
				sourceType, imageType, 
				machineIP, macAddress,
				sourceFolder, sourceFilename, 
				channels, sites, profile);
	}
	
	private PixelDepth toPixelDepth(int noOfBytes)
	{
		switch(noOfBytes)
		{
			case 1: return PixelDepth.BYTE;
			case 2: return PixelDepth.SHORT;
			case 4: return PixelDepth.INT;
		}
		
		return null;
	}

	@Override
	public void updateChannelName(long guid, int channelNo, String newChannelName) throws DataAccessException
	{
		List<Channel> channels = getChannels(guid);
		if(channels==null || channels.size()<=channelNo) return;
		
		channels.get(channelNo).setName(newChannelName);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_CHANNELS");
		sqlQuery.setValue("Channels",  toByteArray(channels), Types.BLOB);
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void updateAcquisitionProfile(long guid, AcquisitionProfile acqProfile) throws DataAccessException
	{
		if(acqProfile == null)
			return;
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_ACQ_PROFILE");
		sqlQuery.setValue("AcqProfile",  toByteArray(acqProfile), Types.BLOB);
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void markForDeletion(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordDAO", "markForDeletion", "deleting record = "+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("MARK_DELETED");
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public boolean isMarkedDeleted(long guid) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBRecordDAO", "isMarkedDeleted", "for guid = "+guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("IS_PRESENT");
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		RowSet<Object[]> result = executeQuery(sqlQuery);
		if(result != null)
		{
			List<Object[]> rows = result.getRows();
			if(rows !=null)
			{
				for(int i=0;i<rows.size();i++)
				{
					if(rows.get(i)!=null && rows.get(i)[0]!=null && (Long)(rows.get(i)[0]) == guid)
					{
						logger.logp(Level.INFO, "DBRecordDAO", "isMarkedDeleted", "found "+rows.get(i)[0]);
						return false;
					}
				}
			}
		}
		logger.logp(Level.INFO, "DBRecordDAO", "isMarkedDeleted", "for guid = "+guid+" "+true);
		
	    return true;
	}

	@Override
	public void transferRecords(long[] ids, int projectID) throws DataAccessException
	{
		if(ids == null || ids.length < 1) return;
		
		for(long id:ids)
		{
			transferRecord(id, projectID);
		}
	}
	
	private void transferRecord(long guid, int projectID) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PROJECT");
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		sqlQuery.setValue("ProjectID", projectID, Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void updateRecordMarker(long guid, RecordMarker marker) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_MARKER");
		sqlQuery.setValue("Marker",  marker.name(), Types.VARCHAR);
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void updateArchiveSignature(long guid, BigInteger archiveSignature) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_SIGN");
        sqlQuery.setValue("Archive",  Util.toHexString(archiveSignature),  Types.VARCHAR);
		sqlQuery.setValue("GUID",  guid, Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}
}
