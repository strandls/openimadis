/*
 * RecordDAO.java
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
import java.util.Set;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.RecordMarker;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.Record;

public interface RecordDAO {
	
	/**
	 * Returns the project id of the specified record
	 * @param guid record GUID
	 * @return the projectID
	 * @throws DataAccessException
	 */
	public int getProjectID(long guid) throws DataAccessException;
	
	/**
	 * Returns all the available project ids along with the guids 
	 * @return
	 * @throws DataAccessException
	 */
	public long[] getAllProjectAndGuid() throws DataAccessException;
	
	/**
	 * Returns projectID and GUID of the specified record signature
	 * @param signature the signature of the record
	 * @return zeroth element of this array is the project, 1th element is GUID
	 * @throws DataAccessException
	 */
	public long[] getProjectAndGuid(Signature signature) throws DataAccessException;
	
	/**
	 * finds the record GUID for the specified signature
	 * @param signature the signature of the record
	 * @return the record GUID for the specified signature
	 * @throws DataAccessException
	 */
	public Long findGUID(Signature signature) throws DataAccessException;
	
	/**
	 * Returns the channels associated with the specified record
	 * @param guid record GUID
	 * @return the channels associated with the specified record
	 * @throws DataAccessException
	 */
	public List<Channel> getChannels(long guid) throws DataAccessException;
	
	/**
	 * Returns the sites associated with the specified record
	 * @param guid record GUID
	 * @return the sites associated with the specified record
	 * @throws DataAccessException
	 */
	public List<Site> getSites(long guid) throws DataAccessException;
	
	/**
	 * Returns the archive signature associated with the specified record
	 * @param guid record GUID
	 * @return  the archive signature associated with the specified record
	 * @throws DataAccessException
	 */
	public BigInteger getArchiveSignature(long guid) throws DataAccessException;
	
	/**
	 * Returns the max dimensions of the record  
	 * @param guid record GUID
	 * @return dimension capturing the number of frames, slices, sites and channels
	 * @throws DataAccessException
	 */
	public Dimension getDimension(long guid) throws DataAccessException;
	
	/**
	 * Returns the dimension (width and height) of the images with the specified record
	 * @param guid record GUID
	 * @return Dimension of the image
	 * @throws DataAccessException
	 */
	public java.awt.Dimension getImageSize(long guid) throws DataAccessException;
	
	/**
	 * Returns the pixel depth of images associated with this record
	 * @param guid the GUID of the record
	 * @return the PixelDepth of the images
	 * @throws DataAccessException
	 */
	public PixelDepth getImageDepth(long guid) throws DataAccessException;
	
	/**
	 * find all records within the specified project
	 * @param projectName name of the project
	 * @return list of all records or null
	 * @throws DataAccessException
	 */
	public List<Record> findRecord(int projectID) throws DataAccessException;
	
	/**
	 * find all records within the specified project
	 * @param projectName name of the project
	 * @param limit to records to be returned
	 * @return list of top n records or null
	 * @throws DataAccessException
	 */
	public List<Record> findRecord(int projectID, int limit) throws DataAccessException;
	
	/**
	 * Fetches all record GUID for the specified project
	 * @return array of GUIDs
	 * @throws DataAccessException
	 */
	public long[] getRecordGUIDs(int projectID)  throws DataAccessException;
	
	/**
	 * Fetches all record GUID for the specified archiveSignature
	 * @param archiveSignature the archive signature under consideration
	 * @return array of GUIDs
	 * @throws DataAccessException
	 */
	public long[] getGUIDsForArchive(BigInteger archiveSignature)  throws DataAccessException;
	
	/**
	 * finds the record for the specified guid
	 * @param guid 
	 * @return
	 * @throws DataAccessException
	 */
	public Record findRecord(long guid) throws DataAccessException;
	
	/**
	 * find all records with the specified IDs
	 * @param guid set of record GUID
	 * @return list of all records or null
	 * @throws DataAccessException
	 */
	public List<Record> findRecords(Set<Long> guid) throws DataAccessException;
	
	/**
	 * finds the record for the specified signature
	 * @param signature the signature of the record
	 * @return the record for the specified signature
	 * @throws DataAccessException
	 */
	public Record findRecord(Signature signature) throws DataAccessException;
	
	/**
	 * Returns the list of projects (most likely one only) this archive has contributed records
	 * @param archiveSignature the archive signatire
	 * @return the distinct list of project IDs
	 * @throws DataAccessException
	 */
	public Set<Integer> getProjectForArchive(BigInteger archiveSignature) throws DataAccessException;
	
	/**
	 * Returns the list of archives associated with the specified project
	 * @param projectID project ID
	 * @return list of archive signatures
	 * @throws DataAccessException
	 */
	public Set<BigInteger> getArchivesForProject(int projectID) throws DataAccessException;
	
	/**
	 * Inserts a new record instance having the specified fixed fields 
	 * @param project the target project
	 * @param uploadedBy id of the user who have uploaded this record
	 * @param noOfSlices  number of slices (Z-positions) 
	 * @param noOfFrames number of frames
	 * @param imageWidth image width in pixels
	 * @param imageHeight image height in pixels
	 * @param siteSignature hash of the site - part of the signature
	 * @param archiveSignature hash of the archive - part of the signature
	 * @param uploadTime time when the record was uploaded
	 * @param sourceTime last modification time of the source files
	 * @param creationTime when the record was created with the acquisition software
	 * @param acquiredTime when the source files are generated from the microscopes
	 * @param imageDepth pixel depth (bytes per pixel)
	 * @param xPixelSize pixel size in microns in x dimension
	 * @param yPixelSize pixel size in microns in y dimension
	 * @param zPixelSize pixel size in microns in z dimension
	 * @param sourceType name of the format
	 * @param imageType image type
	 * @param machineIP IP address of the acquisition machine
	 * @param macAddress MAC address of the acquisition machine
	 * @param sourceFolder source directory
	 * @param sourceFilename seed file name
	 * @param channels channels of this record
	 * @param sites sites of this record
	 * @param notes free text description of this record
	 */
	public void insertRecord(int projectID, String uploadedBy, 
			int noOfSlices, int noOfFrames, int imageWidth, int imageHeight,
			BigInteger siteSignature, BigInteger archiveSignature,
			Long uploadTime, Long sourceTime, Long creationTime, Long acquiredTime,
			PixelDepth imageDepth, double xPixelSize, double yPixelSize, double zPixelSize,
			SourceFormat sourceType, ImageType imageType, 
			String machineIP, String macAddress,
			String sourceFolder, String sourceFilename, 
			List<Channel> channels, List<Site> sites, RecordMarker recordMarker, String microscopeName) throws DataAccessException;

	/**
	 * updates the channel name of specified channel to new value
	 * @param guid of specified record 
	 * @param channelNo of specified channel
	 * @param newChannelName new channel name
	 * @throws DataAccessException 
	 */
	public void updateChannelName(long guid, int channelNo,	String newChannelName) throws DataAccessException;

	/**
	 * marks record for deletion
	 * @param guid to be deleted
	 * @throws DataAccessException 
	 */
	public void markForDeletion(long guid) throws DataAccessException;

	/**
	 * returns true if record marked deleted
	 * @param id specified record
	 * @return true if record maked deleted; false otherwise
	 * @throws DataAccessException 
	 */
	public boolean isMarkedDeleted(long id) throws DataAccessException;

	/**
	 * returns all the deleted guids associated with archive
	 * @param archiveSignature
	 * @return list of deleted guids for archive
	 * @throws DataAccessException
	 */
	public long[] getDeletedGUIDsForArchive(BigInteger archiveSignature) throws DataAccessException;

	/**
	 * returns both the deleted and not deleted guids associated with archive
	 * @param sign archiveSignature
	 * @return list of both deleted and not deleted guids for archive
	 * @throws DataAccessException 
	 */
	public long[] getAllGUIDsForArchive(BigInteger sign) throws DataAccessException;

	/**
	 * changes project id of specified records
	 * @param ids specified records 
	 * @param targetProjectId target project
	 */
	public void transferRecords(long[] ids, int targetProjectId) throws DataAccessException;

	/**
	 * 
	 * @param signature
	 * @return
	 * @throws DataAccessException
	 */
	public Long findGUIDForRecordBuilder(Signature signature) throws DataAccessException;

	/**
	 * updates the record marker for specified record
	 * @param guid specified record
	 * @param marker specified record marker
	 * @throws DataAccessException 
	 */
	public void updateRecordMarker(long guid, RecordMarker active) throws DataAccessException;

	/**
	 * update archive signature for specified record
	 * @param guid specified record
	 * @param archiveSignature archive signature
	 * @throws DataAccessException 
	 */
	public void updateArchiveSignature(long guid, BigInteger archiveSignature) throws DataAccessException;

	/**
	 * Returns the archive signature associated with the specified record
	 * @param guid record GUID
	 * @param marker specified record marker
	 * @return  the archive signature associated with the specified record
	 * @throws DataAccessException
	 */
	public BigInteger getArchiveSignature(long guid, RecordMarker marker) throws DataAccessException;

	/**
	 * updates the acq profile for the record
	 * @param guid specified record
	 * @param acqProfile specified acq profile
	 * @throws DataAccessException 
	 */
	public void updateAcquisitionProfile(long guid, AcquisitionProfile acqProfile) throws DataAccessException;
}
