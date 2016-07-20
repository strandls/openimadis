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
 * AttachmentManager.java
 *
 * AVADIS Image Management System
 * Core Engine
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

package com.strandgenomics.imaging.iengine.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.models.Archive;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.HistoryType;

/**
 * handles record attachment upload/download and exposes attachment notes DAO
 * 
 * @author Anup Kulkarni
 */
public class AttachmentManager extends SystemManager {
	
	/**
	 * name of the folder containing the actual attachments uploaded by the client
	 */
	public static final String FOLDER_NAME_ATTACHMENTS = "attachments";

	AttachmentManager() {
	}

	/**
	 * creates the upload URL for uploading specified attachment
	 * 
	 * @param projectId  of parent project
	 * @param guid  of parent project       
	 * @param name  of specified attachment         
	 * @param clientIP  of client machine
	 * @return upload URL
	 */
	public String getUploadURL(String actorLogin, long guid, String name, String clientIP) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		return DataExchange.UPLOAD_ATTACHMENT.generateURL(actorLogin, clientIP, System.currentTimeMillis(), projectID, guid, name);
	}
	
	/**
	 * creates the download URL for downloading specified attachment
	 * 
	 * @param signature of the parent project
	 * @param name of attachment to be deleted
	 * @param clientIPAddress of client machine
	 * @return download URL
	 * @throws DataAccessException 
	 */
	public String getDownloadURL(String actorLogin, long guid, String name, String clientIPAddress) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		return DataExchange.DOWNLOAD_ATTACHMENT.generateURL(actorLogin, clientIPAddress, System.currentTimeMillis(), projectID, guid, name);
	}

	/**
	 * updates the notes associated with specified attachment
	 * @param signature of parent record
	 * @param name of attachment
	 * @param notes new notes to be updated
	 * @throws DataAccessException
	 */
	public void updateAttachmentNotes(String actorLogin, long guid, String name, String notes) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		logger.logp(Level.INFO, "RecordManager", "updateAttachmentNotes", "updating attachment notes for record ");
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		factory.getAttachmentDAO().updateAttachmentNotes(projectID, guid, name, notes);
	}
	
	/**
	 * returns the list of attachments with specified record
	 * @param signature of parent record
	 * @return the list of attachments with specified record
	 * @throws DataAccessException
	 */
	public List<Attachment> getRecordAttachments(String actorLogin, Signature signature) throws DataAccessException {
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();

		RecordDAO recordDao = factory.getRecordDAO();
		long guid = recordDao.findGUID(signature);
		
		return getRecordAttachments(actorLogin, guid);
	}

	/**
	 * returns the list of attachments with specified record
	 * 
	 * @param projectID
	 *            of parent project
	 * @param guid
	 *            of parent record
	 * @return list of record attachment objects
	 * @throws DataAccessException
	 */
	public List<Attachment> getRecordAttachments(String actorLogin, long guid)
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getRecordAttachments", "retrieving attachment notes for record "+guid);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		return factory.getAttachmentDAO().getRecordAttachments(guid);
	}
	

	/**
	 * deletes specified attachment of specified record
	 * 
	 * @param projectID
	 *            of parent project
	 * @param guid
	 *            of parent record
	 * @param name
	 *            of particular attachment
	 * @throws DataAccessException
	 */
	public void deleteRecordAttachments(Application app, String actorLogin, String accessToken, long guid, String name) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canDelete(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "RecordManager", "deleteRecordAttachments", "deleting attachment notes for record ");
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		// delete from attachment table
		factory.getAttachmentDAO().deleteRecordAttachment(projectID, guid, name);
		
		// delete from storage location
		SysManagerFactory.getStorageManager().deleteRecordAttachment(projectID, guid, name);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.ATTACHMENT_DELETED, name);
	}
	
	private void storeInDB(long guid, String name, String notes, String uploadedBy) throws DataAccessException
	{
		logger.logp(Level.INFO, "AttachmentManager", "storeInDB", "registering attachment for "+guid);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		factory.getAttachmentDAO().insertAttachmentNotes(guid, name,	notes, uploadedBy);
	}

	/**
	 * this method inserts attachment name and attachment notes on server DB and
	 * returns URL for uploading actual attachment
	 * @param guid record GUID
	 * @param name of attachment
	 * @param notes with the attachment
	 * @param clientIPAddress IP address of client used for URL creation
	 * @return URL to upload the attachment
	 * @throws DataAccessException
	 */
	public String insertAttachment(Application app, String actorLogin, String accessToken, long guid, String name,
			String notes, String clientIPAddress) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "insertAttachment","adding attachment notes for record ");
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		storeInDB(guid, name, notes, actorLogin);
		String uploadURL =  getUploadURL(actorLogin, guid, name, clientIPAddress);

		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.ATTACHMENT_ADDED, name);
		
		return uploadURL;
	}
	
	/**
	 * finds the path on server where the actual attachment with specified parameters is stored 
	 * @param projectId of parent project
	 * @param guid of parent record
	 * @param attachmentName of specified attachment
	 * @return actual attachment
	 * @throws IOException 
	 */
	public File findAttachment(String actorLogin, long guid, String attachmentName) throws IOException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();

		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

	    return findAttachment(guid, attachmentName);
	}

	/**
	 * Unauthorized api to get the attachment file.
	 * TODO: cleanup this and the previous {@link #findAttachment(String, long, String)} api.
	 * 
	 * @param guid
	 * @param attachmentName
	 * @return
	 * @throws IOException 
	 */
	private File findAttachment(long guid, String attachmentName) throws IOException 
	{
	    ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ArchiveDAO archiveDao = factory.getArchiveDAO();
        RecordDAO recordDao = factory.getRecordDAO();
        
        Archive archive = archiveDao.findArchive(recordDao.getArchiveSignature(guid));
        //location of the archive root
        String location = archive.getStorageLocation();
        
        File archiveRoot = new File(Constants.getStorageRoot(), location);
        logger.logp(Level.INFO, "AttachmentManager", "findAttachment", "archiveRoot="+archiveRoot);
        
        if(!archiveRoot.isDirectory())
        {
            throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
        }
        
        File attachmentRoot = null;
        if(isSystemAttachment(attachmentName))
        {   //global attachment
            attachmentRoot = new File(archiveRoot, "attachments");
        }
        else
        {   //local attachment
            int seriesNo = recordDao.getSites(guid).get(0).getSeriesNo();
            attachmentRoot = new File(new File(archiveRoot, getSeriesFilename(seriesNo)), "attachments");
        }
        
        logger.logp(Level.INFO, "AttachmentManager", "findAttachment", "attachment location ="+attachmentRoot);
        
        if(!attachmentRoot.isDirectory())
        {
            throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
        }
        
        File attachment = new File(attachmentRoot, attachmentName);// the attachment
        
        if(!attachment.getParentFile().equals(attachmentRoot))
        	throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.NOT_VALID_FILEPATH));
        	
        return attachment;
	}

	
	private String getSeriesFilename(int seriesNo)
	{
		return StorageManager.SERIES_FILENAME+seriesNo;
	}
	
	/**
	 * adds attachment to specified record
	 * this is used by web client who does not requier URL to upload the actual file
	 * @param guid of specified record
	 * @param attachment actual file
	 * @param notes attachment notes
	 * @param user who added this file
	 * @throws IOException
	 */
	public void addAttachmentForRecord(Application app, String accessToken, long guid, File attachment, String notes, String user) throws IOException
	{
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		long bytesToAdd = Util.calculateSize(attachment);
		if(SysManagerFactory.getProjectManager().isQuotaExceeded(projectID, bytesToAdd))
		{
			logger.logp(Level.WARNING, "AttachmentManager", "addAttachmentForRecord", "project disk quota exceeded");
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.QUOTA_EXCEEDED));
		}
		
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
		if(!SysManagerFactory.getUserPermissionManager().canWrite(user, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		storeInDB(guid, attachment.getName(), notes, user);
		storeAttachment(guid, attachment.getName(), attachment);
		
		double gbToAdd = (double)bytesToAdd/ (double)(1024 * 1024 * 1024);
		SysManagerFactory.getProjectManager().updateProjectRecords(projectID, 0, gbToAdd);
	
		// add history
		addHistory(guid, app, user, accessToken, HistoryType.ATTACHMENT_ADDED, attachment.getName());
	}
	
	private void addHistory(long guid, Application app, String user, String accessToken, HistoryType type, String... args) throws DataAccessException
	{
		SysManagerFactory.getHistoryManager().insertHistory(guid, app, user, accessToken, type, type.getDescription(guid, user, args), args);
	}
	
	/**
	 * returns true if attachment is generated from source files; false otherwise
	 * @param attachmentName
	 * @return true if attachment is generated from source files; false otherwise
	 */
	public boolean isSystemAttachment(String attachmentName)
	{
		if(BioExperiment.GlobalMetadata.equals(attachmentName)||BioExperiment.OMEXMLMetaData.equals(attachmentName))
		{
			return true;
		}
		return false;
	}

	/**
	 * stores attachment file to correct location in storate_root
	 * @param projectId of parent project
	 * @param guid of parent record
	 * @param attachmentName of specified attachment
	 * @param tarBall temp file 
	 * @throws IOException
	 */
	private void storeAttachment(long guid, String attachmentName, File tarBall) throws IOException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		RecordDAO recordDao = factory.getRecordDAO();
		
		Archive archive = archiveDao.findArchive(recordDao.getArchiveSignature(guid));
		//location of the archive root
		String location = archive.getStorageLocation();
		
		File archiveRoot = new File(Constants.getStorageRoot(), location);
		logger.logp(Level.FINE, "AttachmentManager", "storeAttachment", "archiveRoot="+archiveRoot);
		
		if(!archiveRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		File attachmentRoot = null;
		if(isSystemAttachment(attachmentName))
		{   //global attachment
			attachmentRoot = new File(archiveRoot, "attachments");
		}
		else
		{   //local attachment
			int seriesNo = recordDao.getSites(guid).get(0).getSeriesNo();
			attachmentRoot = new File(new File(archiveRoot, getSeriesFilename(seriesNo)), "attachments");
		}
		
		logger.logp(Level.FINE, "AttachmentManager", "storeAttachment", "recordRoot="+attachmentRoot);
		
		if(!attachmentRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		File attachmentFile = new File(attachmentRoot, attachmentName);// the attachment
		Util.copy(tarBall, attachmentFile); //try move first
		tarBall.delete();
	}

	/**
	 * used while registering record
	 * @param signature of the record 
	 * @param name of attachment
	 * @param notes associated with attachment
	 * @throws DataAccessException
	 */
	void registerAttachment(Signature signature, String name,
			String notes, String uploadedBy) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		RecordDAO recordDao = factory.getRecordDAO();
		long guid = recordDao.findGUID(signature);
		
		storeInDB(guid, name, notes, uploadedBy);
	}

	public void acceptAttachmentUploadRequest(String actorLogin, InputStream request, int projectID, long guid, String attachmentName) throws Exception 
	{
		logger.logp(Level.INFO, "AttachmentManager", "acceptAttachmentRequest", "accepting upload request for attachment ");
		BufferedInputStream iStream = null;

		FileOutputStream fStream = null;
		BufferedOutputStream oStream = null;
		
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		File tarBall = null;
		try 
		{
			iStream = new BufferedInputStream(request);
			
			String suffix = attachmentName.substring(attachmentName.lastIndexOf("."));
			tarBall = File.createTempFile("attachment_" + attachmentName, suffix, Constants.TEMP_DIR);
			tarBall.deleteOnExit();
			
			logger.logp(Level.INFO, "AttachmentManager", "acceptAttachmentRequest", "storing tar ball in " + tarBall);

			fStream = new FileOutputStream(tarBall);
			oStream = new BufferedOutputStream(fStream);
			
			long dataLength = Util.transferData(iStream, oStream);
			
			storeAttachment(guid, attachmentName, tarBall);
			logger.logp(Level.INFO, "AttachmentManager", "acceptAttachmentRequest", "successfully stored attachment " + dataLength+ " at " + tarBall);
		} 
		catch (Exception ex) 
		{
			throw ex;
		}
		finally
		{
			Util.closeStream(oStream);
			Util.closeStream(fStream);

			Util.closeStream(iStream);
		}
	}

	public void acceptAttachmentDownloadRequest(String actorLogin, OutputStream outputStream, String attachmentName, int projectID, long guid) throws Exception 
	{
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
		if(!SysManagerFactory.getUserPermissionManager().canExport(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		File attachment = findAttachment(guid, attachmentName);
		sendFile(outputStream, attachment);
		logger.logp(Level.INFO, "AttachmentManager", "acceptAttachmentDownloadRequest", "accepting attachment download request "+attachment);
	}
	
	private void sendFile(OutputStream response, File archiveFile) throws IOException
	{
		BufferedOutputStream oStream = null;
		
		FileInputStream fStream = null;
		BufferedInputStream iStream = null;

		try
		{
			oStream = new BufferedOutputStream(response);
			logger.logp(Level.INFO, "DataExchangeServlet", "sendFile", "sending data "+archiveFile +" of length "+archiveFile.length());
			
			fStream = new FileInputStream(archiveFile);
			iStream = new BufferedInputStream(fStream);

			long dataLength = Util.transferData(iStream, oStream);
			logger.logp(Level.INFO, "DataExchangeServlet", "sendFile", "successfully send file " + dataLength);
		} 
		catch (IOException ex) 
		{
			throw ex;
		} 
		finally
		{
			Util.closeStream(oStream);
			Util.closeStream(fStream);
			Util.closeStream(iStream);
		}
	}
}
