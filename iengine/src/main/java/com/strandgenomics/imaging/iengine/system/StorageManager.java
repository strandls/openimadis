/*
 * StorageManager.java
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.RecordMarker;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.Thumbnail;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.bioformats.ClientExperiment;
import com.strandgenomics.imaging.iengine.dao.ArchiveDAO;
import com.strandgenomics.imaging.iengine.dao.AttachmentDAO;
import com.strandgenomics.imaging.iengine.dao.HistoryDAO;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.ThumbnailDAO;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Archive;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.Shortcut;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserAction;
import com.strandgenomics.imaging.iengine.models.UserStatus;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;

/**
 * Archives - i.e., experiments are stored within a configured storage root folder
 * Within the storage-root, there will be project folder (project-name)
 * Within the project-folder, there will be user folder (login-name)
 * Within the user-folder, there will be the archive folder (source file name)
 * Within the archive-folder, there will be a folder - "original_files" - unzipped tar-ball
 * Within the "original_files" folder, there will be actual source files uploaded by the user for that experiment
 * Within the archive-folder for every series (record), there will be folder "series_i", where i is the series number
 * Within the series-folder, there will be an "attachments" folder for containing uploaded attachment for the record
 * @author arunabha
 *
 */
public class StorageManager extends SystemManager {
	
	/**
	 * name of the folder containing the original source files uploaded by the client
	 */
	public static final String FOLDER_NAME_SOURCE_FILES = "original_files";
	/**
	 * name of the thumb-nail file
	 */
	public static final String THUMBNAIL_FILENAME = "thumbnail";
	/**
	 * type of the thumb-nail image
	 */
	public static final String THUMBNAIL_TYPE = "PNG";
	/**
	 * name of the root folder for a record (with a specific series number) under the archive-root
	 */
	public static final String SERIES_FILENAME = "series_";
	/**
	 * name of root folder for attachments
	 */
	public static final String Attachment = "attachments";
	
	/**
	 * the root directory in where all records are saved
	 */
	protected File storageRoot = null;
	
	private static final Random rnd = new Random(System.nanoTime());
	
	StorageManager()
	{
		storageRoot = Constants.getStorageRoot();
		logger.logp(Level.INFO, "StorageManager", "StorageManager", "+++storageRoot: "+storageRoot);
	}
	
	public File getStorageRoot()
	{
		return storageRoot;
	}
	
	/**
	 * Returns the URL to download an archive
	 * @param actorLogin
	 * @param clientIP
	 * @param archiveSignature
	 * @return
	 * @throws DataAccessException
	 */
	public String getArchiveDownloadURL(String actorLogin, String clientIP, BigInteger archiveSignature) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		Set<Integer> projects = recordDao.getProjectForArchive(archiveSignature);
		
		if(projects == null || projects.isEmpty())
			return null;
		
		ProjectDAO projectDao = factory.getProjectDAO();
		List<Project> memberProjects = projectDao.findProject(actorLogin, ProjectStatus.Active);
		
		if(memberProjects == null || memberProjects.isEmpty())
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		boolean legalAccess = false;
		for(Project p : memberProjects)
		{
			if(projects.contains(p.getID()))
			{
				legalAccess = true;
			}
		}
		
		if(!legalAccess) throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		return DataExchange.DOWNLOAD_ARCHIVE.generateURL(actorLogin, clientIP, System.currentTimeMillis(), Util.toHexString(archiveSignature));
	}
	
	/**
	 * Returns the tar-ball of the source files (of an experiment)
	 * @param actorLogin the login id of the user making the request
	 * @param projectName name of the project
	 * @param guid the GUID of the record
	 * @param compress whether to compress
	 * @return the tar-ball
	 * @throws DataAccessException
	 * @throws IOException
	 */
	public File getArchive(String actorLogin, long guid, boolean compress) 
			throws DataAccessException, IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
    	logger.logp(Level.INFO, "StorageManager", "getArchive", "finding archive for "+guid);
		
		return getArchive(guid, compress);
	}
	
	/**
	 * Returns the folder where the original files of the archive are stored
	 * @param archiveSignature the archive signature
	 * @return the folder where the original files of the archive are stored
	 * @throws DataAccessException
	 */
	public File getStorageRoot(BigInteger archiveSignature) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		Archive archive = archiveDao.findArchive(archiveSignature);
		
		//location of the archive root
		String location = archive.getStorageLocation();
		Shortcut s = SysManagerFactory.getShortcutManager().getShortcut(archiveSignature);
		if(s!=null)
		{
			BigInteger originalArchiveSign = s.getOriginalArchiveSign();
			Archive originalArchive = archiveDao.findArchive(originalArchiveSign);
			location = originalArchive.getStorageLocation();
		}
		
		File archiveRoot = new File(storageRoot, location);
		logger.logp(Level.FINE, "StorageManager", "getOriginalFiles", "archiveRoot="+archiveRoot);
		
		if(!archiveRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		File srcRoot = new File(archiveRoot, FOLDER_NAME_SOURCE_FILES).getAbsoluteFile();
		logger.logp(Level.FINE, "StorageManager", "getOriginalFiles", "srcRoot="+srcRoot);
		
		if(!srcRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		return srcRoot;
	}
	
	public File getArchive(long guid, boolean compress) throws DataAccessException, IOException
	{	
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		BigInteger archiveSignature = recordDao.getArchiveSignature(guid);
		
		return getArchive(archiveSignature, compress);
	}
	
	public File getArchive(BigInteger archiveSignature, boolean compress) throws DataAccessException, IOException	
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		Archive archive = archiveDao.findArchive(archiveSignature);
		//location of the archive root
		String location = archive.getStorageLocation();
		
		File archiveRoot = new File(storageRoot, location);
		logger.logp(Level.FINE, "StorageManager", "getArchive", "archiveRoot="+archiveRoot);
		
		if(!archiveRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		File srcRoot = new File(archiveRoot, FOLDER_NAME_SOURCE_FILES);
		logger.logp(Level.FINE, "StorageManager", "getArchive", "srcRoot="+srcRoot);
		
		if(!srcRoot.isDirectory())
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.INTERNAL_SERVER_ERROR)); 
		}
		
		String filename = archiveRoot.getName() + (compress ? ".tar.gz" : ".tar");
		File tarBall = Archiver.createTarBall(Constants.TEMP_DIR, filename, compress, srcRoot.listFiles()); 
		tarBall.deleteOnExit();
		
		logger.logp(Level.FINE, "StorageManager", "getArchive", "tarBall="+tarBall);
		return tarBall;
	}
	
	
	/**
	 * Returns the storage folder for the specified project
	 * @param projectName name of the concerned project
	 * @return the storage folder for the specified project
	 * @throws DataAccessException 
	 */
	public File getProjectStorageDirectory(String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ProjectDAO projectDao = factory.getProjectDAO();
		
		Project existingProject = projectDao.findProject(projectName);
		File location = new File(storageRoot, existingProject.getLocation());
		location.mkdir();
		
		return location;
	}
	
	public boolean isArchiveExists(BigInteger archiveSignature) throws DataAccessException
	{
		if(archiveSignature == null) 
			throw new NullPointerException("unexpected null value");
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO projectDao = factory.getArchiveDAO();
		
		return projectDao.findArchive(archiveSignature) != null;
	}

	/**
	 * Register the newly extracted experiment
	 * @param app the application used
	 * @param actorLogin  the login name of the user
	 * @param projectName name of the concerned project
	 * @param expt the experiment to add
	 * @throws Exception 
	 */
	public void registerExperiment(Application app, String actorLogin, String accessToken, String projectName, ClientExperiment expt) throws Exception
	{
		logger.logp(Level.INFO, "StorageManager", "registerExperiment", "Registering new experiment for project "+projectName +" for actor "+actorLogin +" with "+expt);
		
		User actor = SysManagerFactory.getUserManager().getUser(actorLogin);
		if(!SysManagerFactory.getUserPermissionManager().canUpload(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		Project existingProject = SysManagerFactory.getProjectManager().getProject(actor.login, projectName);
		
		// check if project quota is exceeded
		long targetSize = Util.calculateSize(expt.getContext().extractionFolder);
		double targetSizeInGB = (double)targetSize / (double)(1024* 1024 * 1024);
		if(existingProject.getStorageQuota()< existingProject.getSpaceUsage() + targetSizeInGB)
		{
			logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "quota exceeded");
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.QUOTA_EXCEEDED));
		}
		
		//step 1: create the folder structure for storage
		File projectRoot = new File(storageRoot, existingProject.getLocation());
		projectRoot.mkdir(); //project root directory
		
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "project root "+projectRoot);
		
		File actorRoot = new File(projectRoot, Util.asciiText(actorLogin, '_'));
		//storage folder for the actor within the project root 
		actorRoot.mkdir();
		
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "actor root "+actorRoot);
		
		//the archive storage folder
		File archiveRoot = null;
		
		synchronized (this) //this lock is important
		{
			archiveRoot = findArchiveRoot(actorRoot, expt);
			if (archiveRoot == null){
				logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "archive root is not found");
			}
			if ( (!archiveRoot.mkdir()) && !archiveRoot.exists() ){
				logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "Failed to create archive root directory");
				throw new Exception("Failed to create archive root directory");
			}
		}
		
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "archive root "+archiveRoot);

		//now register the archive
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		
		//relative path w.r.t the storage for this archive, use unix file separator
		String rootFolder    = existingProject.getLocation() + "/" + Util.asciiText(actorLogin, '_');
		String archiveFolder = archiveRoot.getName();
		
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "storing archive information ");
		//make entries for the archive in storage
		Archive archive = archiveDao.insertArchive(expt.getContext().archiveHash, rootFolder, archiveFolder, expt.getContext().getClientFiles());
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "successfully stored archive information "+archive);
		
		//now copy the original source files in 
		File srcRoot = new File(archiveRoot, FOLDER_NAME_SOURCE_FILES);
		if ( (!srcRoot.mkdir()) && !srcRoot.exists() ){
			System.out.println("source dir not created");
			// delete archiveRoot entry
			archiveDao.deleteArchive(expt.getContext().archiveHash);
			// delete archiveRoot directory
			archiveRoot.delete();
			logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "Failed to create source root directory");
			throw new Exception("Failed to create source root directory");
		}
		
		File attachmentRoot = new File(archiveRoot, Attachment);
		attachmentRoot.mkdir();
		if ( (!attachmentRoot.mkdir()) && !attachmentRoot.exists() ){
			System.out.println("attachment dir not created");
			// delete srcRoot directory
			srcRoot.delete();
			// delete archiveRoot entry
			archiveDao.deleteArchive(expt.getContext().archiveHash);
			// delete archiveRoot directory
			archiveRoot.delete();
			logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "Failed to create attachment root directory");
			throw new Exception("Failed to create attachment root directory");
		}
		
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "src root "+srcRoot);		
		//now insert the individual records
		int noOfRecords = registerRecords(existingProject, actor, expt, archiveRoot);
		if(noOfRecords == 0 && expt.getRecords().size() !=0){
			System.out.println("record upload failed");
			// delete attachmentRoot directory
			attachmentRoot.delete();
			// delete srcRoot directory
			srcRoot.delete();
			// delete archiveRoot entry
			archiveDao.deleteArchive(expt.getContext().archiveHash);
			// delete archiveRoot directory
			archiveRoot.delete();
			logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "Failed to upload records");
			throw new Exception("Failed to upload records");
		}
		
		//finally move the source files from the temporary location
		//move from the temp folder the extracted source files (extracted from the tar-ball)
		Util.move(expt.getContext().extractionFolder, srcRoot);
		logger.logp(Level.FINE, "StorageManager", "registerExperiment", "successfully copied to src root from "+expt.getContext().extractionFolder);

		//update the project status
		long size = Util.calculateSize(archiveRoot);
		double increaseInGB = (double)size/ ((double)1024*1024*1024);
		
		//report it back to the project manager
		SysManagerFactory.getProjectManager().updateProjectRecords(existingProject.getID(), noOfRecords, increaseInGB);
		
		// add history
		for(Signature signature : expt.getRecordSignatures())
		{
			Long guid = SysManagerFactory.getRecordManager().findGuid(actorLogin, signature, false);
			if(guid != null){
				logger.logp(Level.FINE, "StorageManager", "registerExperiment", "adding history for access token="+accessToken);
				addHistory((long)guid, app, actorLogin, accessToken, HistoryType.RECORD_CREATED, (String)null);
			}
		}
		
		if(existingProject.getSpaceUsage()> existingProject.getStorageQuota() * 0.8){
			
			Set<User> receivers = SysManagerFactory.getProjectManager().getProjectManager(actorLogin, projectName);
			receivers.addAll(SysManagerFactory.getUserManager().getUsersForRank(Rank.FacilityManager));
			
			try{
				
				SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, null, NotificationMessageType.PROJECT_QUOTA_REACHING_LIMIT, projectName);
			}
			catch(Exception e){
				
				logger.logp(Level.WARNING, "StorageManager", "registerExperiment", "failed sending email notification to "+ receivers, e);
			}
			
		}
		
		if((noOfRecords < expt.getRecords().size()) && expt.getRecords().size() !=0){
			throw new Exception("Failed to upload some records");
		}
		
	}
	
	/**
	 * share the records among the projects, the raw data will not be copied only the metadata will be copied
	 * @param actorLogin logged in user
	 * @param ids list of guids to be shared
	 * @param targetProjectName name of the target project
	 * @throws IOException 
	 */
	public void shareRecords(Application app, String actorLogin, String accessToken, List<Long>ids, String targetProjectName) throws IOException
	{
		logger.logp(Level.INFO, "ProjectManager", "shareRecords", "sharing starts "+targetProjectName+" ids="+ids);
		
		// sharing is considered to be upload in target project 
		if(!SysManagerFactory.getUserPermissionManager().canUpload(actorLogin, targetProjectName))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		int targetProjectId = SysManagerFactory.getProjectManager().getProject(targetProjectName).getID();
		for(long guid:ids)
		{
			Record ithRecord = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
			int sourceProjectId = ithRecord.projectID;
			Project targetProject = SysManagerFactory.getProjectManager().getProject(targetProjectName);
			
			BigInteger dummyArchiveSignature = new BigInteger(127, rnd);
			
			// create dummy profile
			String microscopeName = SysManagerFactory.getMicroscopeManager().getMicroscope(ithRecord.machineIP, ithRecord.macAddress);
			
			// insert dummy record in record registry
			factory.getRecordDAO().insertRecord(
					targetProjectId,
					actorLogin,
					ithRecord.numberOfSlices, 
					ithRecord.numberOfFrames,
					ithRecord.imageWidth, 
					ithRecord.imageHeight,
					ithRecord.getSignature().getSiteHash(), 
					dummyArchiveSignature,
					ithRecord.uploadTime, 
					ithRecord.sourceTime,
					ithRecord.creationTime,
					ithRecord.acquiredTime,		
					ithRecord.imageDepth, 
					ithRecord.getXPixelSize(), 
					ithRecord.getYPixelSize(), 
					ithRecord.getZPixelSize(), 
					ithRecord.getSourceFormat(), 
					ithRecord.imageType,
					ithRecord.machineIP, 
					ithRecord.macAddress, 
					ithRecord.sourceFolder, 
					ithRecord.sourceFilename, 
					ithRecord.getChannels(),
					ithRecord.getSites(), RecordMarker.Active, microscopeName);// create a new shortcut
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "added in record registry");
			
			// create storage space 
			// for the sake of attachments only (the original files will be read
			// from original record)
			// the mapping of shortcut and the original storage root will be in
			// shortcut_registry
			File projectRoot = new File(storageRoot, targetProject.getLocation());
			projectRoot.mkdir(); // project root directory

			File actorRoot = new File(projectRoot, Util.asciiText(actorLogin,'_'));
			// storage folder for the actor within the project root
			actorRoot.mkdir();

			String originalRootDir = ithRecord.sourceFolder + System.nanoTime();// nano time added for considering multiple shortcuts of same record
			String originalSourceFilename = ithRecord.sourceFilename;
			File archiveRoot = findArchiveRoot(actorRoot, originalRootDir, originalSourceFilename);
			archiveRoot.mkdir();
			
			File srcRoot = new File(archiveRoot, FOLDER_NAME_SOURCE_FILES);
			srcRoot.mkdir();
			
			File attachmentRoot = new File(archiveRoot, Attachment);
			attachmentRoot.mkdir();
			
			File recordRoot = new File(archiveRoot, getSeriesFilename(ithRecord.getSite(0).getSeriesNo()));
			recordRoot.mkdir();
			
			File localAttachmentRoot = new File(recordRoot, Attachment);
			localAttachmentRoot.mkdir();
			
			String rootFolder    = targetProject.getLocation() + "/" + Util.asciiText(actorLogin, '_');
			String archiveFolder = archiveRoot.getName();
			
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "created directory structure");
			
			// copy the original archive information with the dummy archive data
			Archive originalData = factory.getArchiveDAO().findArchive(ithRecord.archiveSignature);
			factory.getArchiveDAO().insertArchive(dummyArchiveSignature, rootFolder, archiveFolder, originalData.getSourceFiles());
			
			// get new guid
			long shortcutGuid = factory.getRecordDAO().findGUID(
					new Signature(ithRecord.numberOfFrames,
							ithRecord.numberOfSlices,
							ithRecord.numberOfChannels, ithRecord.imageWidth,
							ithRecord.imageHeight, ithRecord.getSites(),
							dummyArchiveSignature));
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "got guid="+shortcutGuid);
			
			// create mapping in shortcut registry
			if(SysManagerFactory.getShortcutManager().getShortcut(ithRecord.archiveSignature)!=null)
			{
				// this is a shortcut of shortcut
				Shortcut shortcut = SysManagerFactory.getShortcutManager().getShortcut(ithRecord.archiveSignature);
				factory.getShortcutDAO().insertShortcut(dummyArchiveSignature, shortcut.getOriginalArchiveSign());
			}
			else
			{
				factory.getShortcutDAO().insertShortcut(dummyArchiveSignature, originalData.getSignature());
			}
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "added shortcut");
			
			// register pixel data
			ImagePixelDataDAO pixelDataDao = factory.getImagePixelDataDAO();
			
			List<ImagePixelData> pixelData = pixelDataDao.find(guid);
			for(ImagePixelData pData:pixelData)
			{
				Dimension dim = pData.getDimension();
				pixelDataDao.insertPixelDataForRecord(shortcutGuid, pData.getX(),
						pData.getY(), pData.getZ(), pData.getElapsed_time(),
						pData.getExposureTime(), pData.getTimestamp(),
						dim.frameNo, dim.sliceNo, dim.channelNo, dim.siteNo);
			}
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "added pixel data");
			
			// populate navigation table
			Record shortcutRecord = factory.getRecordDAO().findRecord(shortcutGuid);
			SysManagerFactory.getStorageManager().populateNavigator(shortcutRecord, originalData.getStorageLocation());
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "populated navigator");
			
			// insert thumbnail
			Thumbnail thumbnail = factory.getThumbnailDAO().getThumbnail(guid);
			factory.getThumbnailDAO().setThumbnail(shortcutGuid, thumbnail.getBufferedImage());
			
			// populate user data
			
			// user annotations
			List<MetaData> intMetadata = factory.getMetaDataDAO(AnnotationType.Integer).find(sourceProjectId, guid);
			List<MetaData> realMetadata = factory.getMetaDataDAO(AnnotationType.Real).find(sourceProjectId, guid);
			List<MetaData> textMetadata = factory.getMetaDataDAO(AnnotationType.Text).find(sourceProjectId, guid);
			List<MetaData> timeMetadata = factory.getMetaDataDAO(AnnotationType.Time).find(sourceProjectId, guid);
			
			List<MetaData> allMetadata = new ArrayList<MetaData>();
			if(intMetadata!=null)
				allMetadata.addAll(intMetadata);
			if(realMetadata!=null)
				allMetadata.addAll(realMetadata);
			if(textMetadata!=null)
				allMetadata.addAll(textMetadata);
			if(timeMetadata!=null)
				allMetadata.addAll(timeMetadata);
			for(MetaData metadata:allMetadata)
			{
				// insert in target tables
				factory.getMetaDataDAO(metadata.getType()).insertUserAnnotation(targetProjectId, shortcutGuid, actorLogin, metadata.getName(), metadata.getValue());
				factory.getNavigationDAO(targetProjectId).insertUserAnnotation(shortcutGuid, metadata);
			}
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "populated navigator");
			
			// share attachments
			AttachmentDAO attachmentDao = factory.getAttachmentDAO();
			List<Attachment> attachments = attachmentDao.getRecordAttachments(guid);
			if(attachments!=null)
			{
				for(Attachment attachment:attachments)
				{
					File attachmentFile = SysManagerFactory.getAttachmentManager().findAttachment(actorLogin, guid, attachment.getName());
					File newAttachmentFile = null;
					if(SysManagerFactory.getAttachmentManager().isSystemAttachment(attachment.getName()))
					{
						newAttachmentFile = new File(attachmentRoot, attachment.getName());
					}
					else
					{
						newAttachmentFile = new File(localAttachmentRoot, attachment.getName());
					}
					Util.copy(attachmentFile, newAttachmentFile);
					
					attachmentDao.insertAttachmentNotes(shortcutGuid, attachment.getName(), attachment.getNotes(), attachment.getUser());
				}
			}
			
			// visual annotations
			VisualOverlaysDAO visualOverlaysDao = factory.getVisualOverlaysDAO();
			
			VisualObjectsDAO visualObjectsDao = factory.getVisualObjectsDAO();
			
			for(int site=0;site<ithRecord.numberOfSites;site++)
			{
				List<String> names = visualOverlaysDao.getAvailableVisualOverlays(sourceProjectId, guid, site);
				if(names!=null)
				{
					// create overlays
					for(String name:names)
					{
						VisualOverlay vo = visualOverlaysDao.getVisualOverlay(sourceProjectId, guid, new Dimension(0,0,0,site), name);
						visualOverlaysDao.createVisualOverlays(actorLogin, targetProjectId, shortcutGuid, ithRecord.imageWidth, ithRecord.imageHeight, site, name, vo.handCreated);
					}
					
					for(int slice=0;slice<ithRecord.numberOfSlices;slice++)
					{
						for(int frame=0;frame<ithRecord.numberOfFrames;frame++) 
						{
							for(String name:names)
							{
								// read objects from the overlays
								VODimension imageCoordinate = new VODimension(frame, slice, site);
								List<VisualObject> objects = visualObjectsDao.getVisualObjects(sourceProjectId, guid, name, imageCoordinate);
								
								//put in target project
								visualObjectsDao.addVisualObjects(targetProjectId, shortcutGuid, name, objects, imageCoordinate);
							}
						}
					}
				}
			}
			logger.logp(Level.INFO, "ProjectManager", "shareRecords", "copied visual annotations");
			
			// comments
			List<UserComment> userComments= ImageSpaceDAOFactory.getDAOFactory().getUserCommentDAO().findComments(guid);
			if(userComments!=null){
				for(UserComment comment: userComments){
					 ImageSpaceDAOFactory.getDAOFactory().getUserCommentDAO().insertCommentWithCreationDate(shortcutGuid, comment.getUserLogin(), comment.getNotes(), comment.getCreationDate());
				}
			}
			
			// update history
			String sourceProjectName = SysManagerFactory.getProjectManager().getProject(sourceProjectId).getName();
			addHistory(shortcutGuid, app, actorLogin, accessToken, HistoryType.RECORD_CREATED, (String)null);
			addHistory(shortcutGuid, app, actorLogin, accessToken, HistoryType.SHORTCUT_ADDED, String.valueOf(guid), sourceProjectName, targetProjectName);
			
			List<HistoryObject> history = SysManagerFactory.getHistoryManager().getRecordHistory(actorLogin, guid);
			HistoryDAO historyDao = DBImageSpaceDAOFactory.getDAOFactory().getHistoryDAO();
			for (HistoryObject item : history)
			{
				historyDao.insertHistory(targetProjectId, shortcutGuid, item.getClient().name, item.getClient().version, item.getModifiedBy(),
						item.getModificationTime(), item.getAccessToken(), item.getType(), item.getDescription(), item.getArguments());
			}
			
			addHistory(guid, app, actorLogin, accessToken, HistoryType.HAS_SHORTCUT, String.valueOf(shortcutGuid),sourceProjectName, targetProjectName);
		}
		
		// update number of records in project
		SysManagerFactory.getProjectManager().updateProjectRecords(targetProjectId, ids.size(), 0);
	}
	
	private File findArchiveRoot(File actorRoot, ClientExperiment expt) 
	{
		return findArchiveRoot(actorRoot, expt.getRootDirectory(), expt.getSourceFilename());
	}
	
	private File findArchiveRoot(File actorRoot, String clientRootDir, String sourceFilename) 
	{
		String clientFolderName = "";
		
		if(clientRootDir.indexOf('/') != -1) //UNIX CLIENT MACHINE
		{
			clientFolderName = clientRootDir.substring(clientRootDir.lastIndexOf('/')+1);
		}
		else if(clientRootDir.indexOf('\\') != -1) //windows client machine
		{
			clientFolderName = clientRootDir.substring(clientRootDir.lastIndexOf('\\')+1);
		}
		
		String archiveRoot = clientFolderName +"_"+ sourceFilename;
		//remove special chars
		return Util.findUnique(actorRoot, Util.asciiText(archiveRoot,'_'));
	}
	
	private int registerRecords(Project existingProject, User actor, ClientExperiment expt, File archiveRoot) throws DataAccessException 
	{
		int noOfRecords = 0;
		logger.logp(Level.INFO, "StorageManager", "registerRecords", "trying to register "+expt.size() +" records for "+expt);
		ArrayList <IRecord> failedRecords = new ArrayList <IRecord>();
		for(Signature signature : expt.getRecordSignatures())
		{
			logger.logp(Level.FINE, "StorageManager", "registerRecords", "registering record for "+signature);
			try
			{				
				noOfRecords++;
				IRecord ithRecord = registerRecord(existingProject, actor, expt, signature, archiveRoot);
				logger.logp(Level.FINE, "StorageManager", "registerRecords", "successfully registered record "+ithRecord);
				
				//create storage folder	using the first site
				File recordRoot = new File(archiveRoot, getSeriesFilename(ithRecord.getSite(0).getSeriesNo()));
				recordRoot.mkdir();
				
				File attachmentRoot = new File(recordRoot, Attachment);
				attachmentRoot.mkdir();
				
				//saves the thumb-nail of this record
				setThumbnail(ithRecord);
				//register the pixel data with the image
				registerPixelDataForRecord(ithRecord);
				//save attachments
				saveAttachment(actor, ithRecord, attachmentRoot, new File(archiveRoot, Attachment));
				// generate first image
				generateFirstImage(actor,ithRecord);
			}
			catch(Exception ex)
			{
				System.out.println("record upload exception");
				// roll back
				Long guid = SysManagerFactory.getRecordManager().findGuid(actor.getLogin(), signature, false);
				if(guid != null){
					Application app = new Application("Roll Back","1.0");
					deleteFailedRecord(app,Constants.ADMIN_USER,null,(long)guid);
				}
				noOfRecords--;
				logger.logp(Level.WARNING, "StorageManager", "registerRecords", "unable to register record "+signature, ex);
			}
		}
		
		logger.logp(Level.INFO, "StorageManager", "registerRecords", "successfully registered "+noOfRecords +" records of "+expt.size());
		return noOfRecords;
	}
	
	/**
	 * generate first image for the record to improve user experience in loading the image
	 * @param actor
	 * @param ithRecord
	 */
	private void generateFirstImage(User actor, IRecord ithRecord)
	{
		try
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			RecordDAO recordDao = factory.getRecordDAO();
			long guid = recordDao.findGUID(ithRecord.getSignature());
			
			int chCount = ithRecord.getChannelCount();
			List<Integer> channelList = new ArrayList<Integer>();
			for(int i=0;i<chCount;i++)
			{
				channelList.add(i);
			}
			
			SysManagerFactory.getImageManager().getPixelDataOverlay(actor.login, guid, 0, 0, 0, channelList , true, false, false, null);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "StorageManger", "generateFirstImage", "error generating first image", e);
		}
	}

	/**
	 * used while record creation to create default thumbnail
	 * @param actor current logged in user
	 * @param ithRecord specified record
	 * @throws IOException
	 */
	private void setThumbnail(IRecord ithRecord) throws IOException
	{
		BufferedImage image = ithRecord.getThumbnail();
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		long guid = recordDao.findGUID(ithRecord.getSignature());
		
		//resize thumbnail image
		image = Util.resizeImage(image, Constants.getRecordThumbnailWidth());
		
		//store in DB
		ThumbnailDAO thumbnailDao = factory.getThumbnailDAO();
		thumbnailDao.setThumbnail(guid, image);
	}
	
	/**
	 * deletes failed record while uploading specified by guid
	 * @param app application used to perform the operation
	 * @param actorLogin currently logged in user
	 * @param projectId parent project
	 * @param guid specified guid
	 * @throws DataAccessException
	 */
	public void deleteFailedRecord(Application app, String actorLogin, String accessToken, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "StorageManager", "deleteFailedRecord", "deleting record "+guid);
		
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = permissionManager.getProject(projectId).getName();
		if(!permissionManager.canDelete(actorLogin, projectName))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		RecordDAO recordDao = factory.getRecordDAO();
		
		//get all the guids for the archive before marking the record as deleted
		BigInteger sign = recordDao.getArchiveSignature(guid);
		long[] ids = recordDao.getAllGUIDsForArchive(sign);
		
		// remove record from navigation table
		NavigationDAO navDao = factory.getNavigationDAO(projectId);
		navDao.deleteRecord(guid);
		
		// delete record attachments
		AttachmentManager attachmentManager = SysManagerFactory.getAttachmentManager();
		List<Attachment> attachments = attachmentManager.getRecordAttachments(actorLogin, guid);
		if(attachments!=null)
		{
			for(Attachment attachment:attachments)
			{
				// do not delete global attachments
				if(BioExperiment.GlobalMetadata.equals(attachment.getName())||BioExperiment.OMEXMLMetaData.equals(attachment.getName()))
					continue;
				
				attachmentManager.deleteRecordAttachments(app , actorLogin, accessToken, guid, attachment.getName());
			}
		}
		
		//remove user data for the record
		
		//remove all comments for record
		try
		{
			factory.getUserCommentDAO().deleteCommentByGUID(guid);
		}
		catch (Exception e)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteFailedRecord", "deleting record "+guid, e);
		}
		
		//remove visual annotations
		try
		{
			factory.getVisualOverlaysDAO().deleteVisualOverlaysByGUID(projectId, guid);
		}
		catch (Exception e)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteFailedRecord", "deleting record "+guid, e);
		}
		
		
		// remove bookmarks if the record is bookmarked by any user
		try
		{
			factory.getBookmarkDAO().removeGuidFromBookmark(actorLogin, projectId, guid);
		}
		catch (Exception e)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteFailedRecord", "deleting record "+guid, e);
		}
		
		// mark record for deletion in record_registry
		recordDao.markForDeletion(guid);
		
		// check for other records from the same source file
		boolean allMarked = true;
		logger.logp(Level.FINE, "StorageManager", "deleteFailedRecord", "deleting record "+ids);
		for (int index = 0; index<ids.length; index++)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteFailedRecord", "deleting archive "+ids[index]);
			allMarked = allMarked && recordDao.isMarkedDeleted(ids[index]);
			if(!allMarked)
				break;
		}
		
		// delete storage root if all the records of corresponding source file
		// are marked deleted
		if(allMarked)
		{
			deleteArchive(sign, projectId);
		}
		
		// delete from shortcut registry
		SysManagerFactory.getShortcutManager().deleteShortcut(sign);
		
		// delete the cache
		SysManagerFactory.getImageManager().deleteCachedData(guid);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.RECORD_DELETED);
	}
	
	/**
	 * deletes record specified by guid
	 * @param app application used to perform the operation
	 * @param actorLogin currently logged in user
	 * @param projectId parent project
	 * @param guid specified guid
	 * @throws DataAccessException
	 */
	public void deleteRecord(Application app, String actorLogin, String accessToken, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+guid);
		
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
		String projectName = permissionManager.getProject(projectId).getName();
		if(!permissionManager.canDelete(actorLogin, projectName))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		RecordDAO recordDao = factory.getRecordDAO();
		
		//get all the guids for the archive before marking the record as deleted
		BigInteger sign = recordDao.getArchiveSignature(guid);
		long[] ids = recordDao.getAllGUIDsForArchive(sign);
		
		// remove record from navigation table
		NavigationDAO navDao = factory.getNavigationDAO(projectId);
		navDao.deleteRecord(guid);
		
		// update the record count for project
		SysManagerFactory.getProjectManager().updateProjectRecords(projectId, -1, 0);
		
		// delete record attachments
		AttachmentManager attachmentManager = SysManagerFactory.getAttachmentManager();
		List<Attachment> attachments = attachmentManager.getRecordAttachments(actorLogin, guid);
		if(attachments!=null)
		{
			for(Attachment attachment:attachments)
			{
				// do not delete global attachments
				if(BioExperiment.GlobalMetadata.equals(attachment.getName())||BioExperiment.OMEXMLMetaData.equals(attachment.getName()))
					continue;
				
				attachmentManager.deleteRecordAttachments(app , actorLogin, accessToken, guid, attachment.getName());
			}
		}
		
		//remove user data for the record
		
		//remove all comments for record
		try
		{
			factory.getUserCommentDAO().deleteCommentByGUID(guid);
		}
		catch (Exception e)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+guid, e);
		}
		
		//remove visual annotations
		try
		{
			factory.getVisualOverlaysDAO().deleteVisualOverlaysByGUID(projectId, guid);
		}
		catch (Exception e)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+guid, e);
		}
		
		
		// remove bookmarks if the record is bookmarked by any user
		try
		{
			factory.getBookmarkDAO().removeGuidFromBookmark(actorLogin, projectId, guid);
		}
		catch (Exception e)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+guid, e);
		}
		
		// mark record for deletion in record_registry
		recordDao.markForDeletion(guid);
		
		// check for other records from the same source file
		boolean allMarked = true;
		logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+ids);
		for (int index = 0; index<ids.length; index++)
		{
			logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting archive "+ids[index]);
			allMarked = allMarked && recordDao.isMarkedDeleted(ids[index]);
			if(!allMarked)
				break;
		}
		
		// delete storage root if all the records of corresponding source file
		// are marked deleted
		if(allMarked)
		{
			deleteArchive(sign, projectId);
		}
		
		// delete from shortcut registry
		SysManagerFactory.getShortcutManager().deleteShortcut(sign);
		
		// delete the cache
		SysManagerFactory.getImageManager().deleteCachedData(guid);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.RECORD_DELETED);
	}
	
	private void addHistory(long guid, Application app, String user, String accessToken, HistoryType type, String... args) throws DataAccessException
	{
		SysManagerFactory.getHistoryManager().insertHistory(guid, app, user, accessToken, type, type.getDescription(guid, user, args), args);
	}
	
	void deleteArchive(BigInteger sign, int projectID) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		Archive archive = archiveDao.findArchive(sign);
		
		// location of the archive root
		String location = archive.getStorageLocation();
		
		//get the archive root
		File archiveRoot = new File(storageRoot, location);
		long size = Util.calculateSize(archiveRoot);
		double decreaseInGB = (double)size/ ((double)1024*1024*1024);
		
		// delete archive physical from storage root
		Util.deleteTree(archiveRoot);
		
		// delete archive entry from DB
		archiveDao.deleteArchive(sign);
		
		// change project quota
		SysManagerFactory.getProjectManager().updateProjectRecords(projectID, 0, -1*decreaseInGB);
	}
	
	/**
	 * transfer archive from specified source project to specified target project
	 * archive is denoted by a seed record id generated from that archive.
	 * all the records generated by the archive will be moved to target project
	 * @param actorLogin logged in user
	 * @param seedGuid seed record id representing archive
	 * @param sourceProjectId id of current parent project of the archive
	 * @param targetProjectId id of target project where archive is to be moved
	 * @throws IOException 
	 */
	public void transferArchive(Application app, String actorLogin, String accessToken, long seedGuid, int sourceProjectId, int targetProjectId) throws IOException
	{
		UserPermissionManager permissionManager = SysManagerFactory.getUserPermissionManager();
		String sourceProjectName = permissionManager.getProject(sourceProjectId).getName();
		String targetProjectName = permissionManager.getProject(targetProjectId).getName();
		
		// transfer is considered to be delete from source project and upload in target project 
		if(!permissionManager.canDelete(actorLogin, sourceProjectName) || !permissionManager.canUpload(actorLogin, targetProjectName))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		
		// get the archive to be transfered
		BigInteger sign = recordDao.getArchiveSignature(seedGuid);
		Archive archive = archiveDao.findArchive(sign);
		
		// get name of uploader
		String uploaded_by = recordDao.findRecord(seedGuid).uploadedBy;
		
		String location = archive.getStorageLocation();
		
		File archiveRoot = new File(storageRoot, location);
		long size = Util.calculateSize(archiveRoot);
		double sizeInGB = (double)size/ ((double)1024*1024*1024);
		
		// check storage quota in target project
		Project targetProject = permissionManager.getProject(targetProjectName);
		if(targetProject.getStorageQuota()< targetProject.getSpaceUsage() + sizeInGB)
		{
			logger.logp(Level.WARNING, "StorageManager", "transferArchive", "quota exceeded for target project "+targetProjectName);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.QUOTA_EXCEEDED));
		}
		
		// update db
		long[] ids = recordDao.getAllGUIDsForArchive(sign);
		SysManagerFactory.getProjectManager().transferRecords(actorLogin, ids, sourceProjectId, targetProjectId, sizeInGB);
		
		// update storage
		File projectRoot = new File(storageRoot, targetProject.getLocation());
		projectRoot.mkdir();
		File actorRoot = new File(projectRoot, Util.asciiText(uploaded_by, '_'));
		actorRoot.mkdir();
		File targetArchiveRoot = new File(actorRoot, archiveRoot.getName());
		targetArchiveRoot.mkdir();
		Util.move(archiveRoot, targetArchiveRoot);
		archiveRoot.delete();
		
		// update registry tables
		SysManagerFactory.getRecordManager().updateParentProject(ids, targetProjectId);
		updateStorageLocation(sign, Util.asciiText(targetProjectName, '_')+File.separator+Util.asciiText(uploaded_by, '_'));
		
		// clear image readers if opened
		SysManagerFactory.getImageReadersManager().removeFromCache(sign);
		
		// delete image cache
		for(long id:ids)
		{
			try
			{
				SysManagerFactory.getImageManager().deleteCachedData(id);
			}
			catch (Exception e)
			{
				logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+id, e);
			}
		}
		
		
		// remove bookmarks if the record is bookmarked by any user
		for(long id:ids)
		{
			try
			{
				factory.getBookmarkDAO().removeGuidFromBookmark(actorLogin, sourceProjectId, id);
			}
			catch (Exception e)
			{
				logger.logp(Level.FINE, "StorageManager", "deleteRecord", "deleting record "+id, e);
			}
		}
				
		// add history
		for(long id:ids)
		{
			addHistory(id, app, actorLogin, accessToken, HistoryType.RECORD_TRANSFERED, sourceProjectName, targetProjectName);
		}
	}
	
	private void updateStorageLocation(BigInteger sign, String rootFolderName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		archiveDao.updateArchiveRootFolder(sign, rootFolderName);
	}
	
	private IRecord registerRecord(Project existingProject, User actor, ClientExperiment expt, Signature signature, File archiveRoot) 
			throws DataAccessException 
	{
		IRecord ithRecord = expt.getRecord(signature);
		logger.logp(Level.FINE, "StorageManager", "registerRecord", "found series "+ithRecord +" for signature "+signature);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		Long uploadTime = expt.getContext().uploadTime; 
		Long sourceTime = expt.getContext().getSourceTime() <= 0 ? System.currentTimeMillis() : expt.getContext().getSourceTime();
		Long creationTime = ithRecord.getCreationTime() == null ? null : ithRecord.getCreationTime().getTime();
		Long acquiredTime = ithRecord.getAcquiredDate() == null ? null : ithRecord.getAcquiredDate().getTime();
		
		logger.logp(Level.INFO, "StorageManager", "registerRecord", "found acquired Time "+acquiredTime +" for signature "+signature);
		logger.logp(Level.INFO, "StorageManager", "registerRecord", "found source Time "+sourceTime +" for signature "+signature);
		
		// create dummy profile
		String microscopeName = SysManagerFactory.getMicroscopeManager().getMicroscope(ithRecord.getOriginMachineIP(), ithRecord.getOriginMachineAddress());
		
		//register the record
		recordDao.insertRecord(
				existingProject.getID(),
				actor.login,
				ithRecord.getSliceCount(), 
				ithRecord.getFrameCount(),
				ithRecord.getImageWidth(), 
				ithRecord.getImageHeight(),
				ithRecord.getSignature().getSiteHash(), 
				ithRecord.getSignature().getArchiveHash(),
				uploadTime, 
				sourceTime,
				creationTime,
				acquiredTime,		
				ithRecord.getPixelDepth(), 
				ithRecord.getPixelSizeAlongXAxis(), 
				ithRecord.getPixelSizeAlongYAxis(), 
				ithRecord.getPixelSizeAlongZAxis(), 
				ithRecord.getSourceType(), 
				ithRecord.getImageType(),
				expt.getContext().clientIPAddress, 
				expt.getContext().clientMacAddress, 
				expt.getContext().getSourceDirectory(), 
				expt.getContext().getSourceFilename(), 
				getChannels(ithRecord),
				getSites(ithRecord), RecordMarker.Active, microscopeName);// default deletion status of record is false
		
		//the GUID of the inserted record
		Record rObject = recordDao.findRecord(signature);
		logger.logp(Level.FINE, "StorageManager", "registerRecord", "inserted record "+signature +" with GUID "+rObject.guid);
		
		//populate the navigator
		String storageLocation = existingProject.getLocation()+ File.separator + actor.login + File.separator + archiveRoot.getName();
		populateNavigator(rObject, storageLocation);
		
		return ithRecord;
	}

	void populateNavigator(Record r, String storageLocation) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		NavigationDAO nao = factory.getNavigationDAO(r.projectID);
		
		//populate the navigation table
		nao.registerRecord(r.guid, r.uploadedBy,
				r.numberOfSlices, r.numberOfFrames, r.numberOfChannels, r.numberOfSites, 
				r.imageWidth, r.imageHeight,
				r.uploadTime, r.sourceTime, r.creationTime, r.acquiredTime,	
				r.imageDepth, 
				r.getXPixelSize(), 
				r.getYPixelSize(), 
				r.getZPixelSize(), 
				r.getSourceFormat(), 
				r.imageType,
				r.machineIP, 
				r.macAddress,  
				r.sourceFolder, 
				r.sourceFilename);
		
		nao.insertStorageLocation(r.guid, storageLocation);
		nao.insertMicroscopeName(r.guid, r.getMicroscopeName());
	}

	/**
	 * saves record attachments at appropriate locations in storage root
	 * @param record specified record
	 * @param localAttachmentRoot location where local attachments are stored
	 * @param globalAttachmentRoot locations where global attachments are stored
	 * @throws IOException 
	 */
	private void saveAttachment(User actor, IRecord record, File localAttachmentRoot, File globalAttachmentRoot) throws IOException 
	{
		logger.logp(Level.FINEST, "StorageManager", "saveAttachment", "saving record attachments ");
		Collection<IAttachment> attachments = record.getAttachments();
		
		if(attachments == null || attachments.isEmpty())
			return;
		
		for(IAttachment attachment : attachments)
		{
			logger.logp(Level.FINEST, "StorageManager", "saveAttachment", "saving record attachment "+attachment.getName());
			if(BioExperiment.GlobalMetadata.equals(attachment.getName())||BioExperiment.OMEXMLMetaData.equals(attachment.getName()))
			{
				// store in global attachment root
				File f = attachment.getFile();
				File newFile = new File(globalAttachmentRoot, attachment.getName());
				if(newFile.exists())
					continue;
				
				Util.copy(f, newFile);
				
				f.delete();
			}
			else
			{
				// store in local attachment root
				File f = attachment.getFile();
				File newFile = new File(localAttachmentRoot, attachment.getName());
				Util.copy(f, newFile);
				
				f.delete();
			}
			
			SysManagerFactory.getAttachmentManager().registerAttachment(record.getSignature(), attachment.getName(), attachment.getNotes(), actor.login);
		}
	}
	
	/**
	 * deletes the attachment from the file system specified by name from specified record
	 * @param projectID specified project
	 * @param guid specified record
	 * @param name of attachment
	 * @throws DataAccessException 
	 */
	void deleteRecordAttachment(int projectID, long guid, String name) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		Record record = recordDao.findRecord(guid);
		BigInteger archiveSignature = recordDao.getArchiveSignature(guid);
		
		ArchiveDAO archiveDao = factory.getArchiveDAO();
		Archive archive = archiveDao.findArchive(archiveSignature);
		//location of the archive root
		String location = archive.getStorageLocation();
		
		File archiveRoot = new File(storageRoot, location);
		
		File recordRoot = new File(archiveRoot, getSeriesFilename(record.getSite(0).getSeriesNo()));
		
		File attachmentRoot = new File(recordRoot, Attachment);
		
		File localAttachmentFile = new File(attachmentRoot, name);
		if(localAttachmentFile.isFile())
		{
			localAttachmentFile.delete();
			
			double sizeInGB = (double)Util.calculateSize(localAttachmentFile)/(double)(1024 * 1024 * 1024);
			SysManagerFactory.getProjectManager().updateProjectRecords(projectID, 0, -1*sizeInGB);
		}
	}

	private List<Channel> getChannels(IRecord ithRecord) 
	{
		int noOfChannels = ithRecord.getChannelCount();
		List<Channel> channels = new ArrayList<Channel>(noOfChannels);
		for(int channelNo = 0;channelNo < noOfChannels; channelNo++)
		{
			channels.add( (Channel) ithRecord.getChannel(channelNo) );
		}
		return channels;
	}

	private List<Site> getSites(IRecord ithRecord) 
	{
		int noOfSites = ithRecord.getSiteCount();
		List<Site> sites = new ArrayList<Site>(noOfSites);
		for(int siteNo = 0;siteNo < noOfSites; siteNo++)
		{
			sites.add( ithRecord.getSite(siteNo) );
		}
		return sites;
	}
	
//	private String getThumbnailFilename()
//	{
//		return THUMBNAIL_FILENAME+"."+THUMBNAIL_TYPE.toLowerCase();
//	}
	
	private String getSeriesFilename(int seriesNo)
	{
		return SERIES_FILENAME+seriesNo;
	}
	
	void registerPixelDataForRecord(IRecord ithRecord) throws DataAccessException
	{
		logger.logp(Level.INFO, "StorageManager", "registerPixelDataForRecord", "Registering pixel data for record ");
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		long guid = recordDao.findGUID(ithRecord.getSignature());

		ImagePixelDataDAO pixelDataDao = factory.getImagePixelDataDAO();
		
		int maxFrame = ithRecord.getFrameCount();
		int maxChannel = ithRecord.getChannelCount();
		int maxSlice = ithRecord.getSliceCount();
		int maxSite = ithRecord.getSiteCount();
		
		for (int site = 0; site < maxSite; site++) 
		{
			for (int frame = 0; frame < maxFrame; frame++) 
			{
				for (int slice = 0; slice < maxSlice; slice++) 
				{
					for (int channel = 0; channel < maxChannel; channel++)
					{
						Dimension d = new Dimension(frame, slice, channel, site);
						try
						{
							IPixelData pixelData = ithRecord.getPixelData(d);

							pixelDataDao.insertPixelDataForRecord(
									guid,
									pixelData.getX(), 
									pixelData.getY(),
									pixelData.getZ(),
									pixelData.getElapsedTime(),
									pixelData.getExposureTime(),
									pixelData.getTimeStamp(), 
									frame, 
									slice, 
									channel, 
									site);
						}
						catch (Exception e)
						{
							logger.logp(Level.INFO, "StorageManager", "registerPixelDataForRecord", "error Registering pixel data for record at "+d,e);
						}
					}
				}
			}
		}
	}
}
