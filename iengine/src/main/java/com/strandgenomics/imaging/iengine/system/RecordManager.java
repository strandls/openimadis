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
 * RecordManager.java
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

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.MetaDataDAO;
import com.strandgenomics.imaging.iengine.dao.NavigationDAO;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.UserCommentDAO;
import com.strandgenomics.imaging.iengine.dao.UserPreferencesDAO;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysRevisionDAO;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfileType;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;

/**
 * Manages records within a project
 * 
 * @author arunabha
 * 
 */
public class RecordManager extends SystemManager {
	/**
	 * map storing project id for every guid
	 */
	
	RecordManager() 
	{ 
		logger.logp(Level.INFO, "RecordManager", "RecordManager", "creating record manager");
		
		initialize();
	}

	private void initialize()
	{
		
		// populate the cache
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		CacheManager cacheManager = SysManagerFactory.getCacheManager();
		try
		{
			long allIds[] = factory.getRecordDAO().getAllProjectAndGuid();
			if (allIds != null)
			{
				for (int i = 0; i < allIds.length; i += 2)
				{
					cacheManager.set(new CacheKey(""+allIds[i+1], CacheKeyType.RecordId), (int)allIds[i]);
					logger.logp(Level.INFO, "RecordManager", "initialize", "setting cache: GUID_" + allIds[i+1] + " to " + allIds[i]);
				}
			}
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.INFO, "RecordManager", "initialize", "failed to initialize the cache");
		}
	}

	public List<Record> getRecords(String actorLogin, String projectName) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
			
		Project currentProject = SysManagerFactory.getProjectManager().getProject(projectName);
		logger.logp(Level.INFO, "RecordManager", "getRecords", "listing records for actor " + actorLogin +" within project "+currentProject);

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		logger.logp(Level.INFO, "RecordManager", "getRecords", "listing records for actor " + actorLogin +" within project "+currentProject);

		List<Record> records = recordDao.findRecord(currentProject.getID());
		logger.logp(Level.INFO, "RecordManager", "getRecords", "found records "+records);
		return records;
	}
	
	/**
	 * return first-n records
	 * @param actorLogin current user
	 * @param projectName specified project
	 * @param limit no. of records to be returned
	 * @return list of first n records
	 * @throws DataAccessException
	 */
	public List<Record> getRecords(String actorLogin, String projectName, int limit) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
			
		Project currentProject = SysManagerFactory.getProjectManager().getProject(projectName);
		logger.logp(Level.INFO, "RecordManager", "getRecords", "listing records for actor " + actorLogin +" within project "+currentProject+" with limit"+limit);

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		logger.logp(Level.INFO, "RecordManager", "getRecords", "listing records for actor " + actorLogin +" within project "+currentProject+" with limit"+limit);

		List<Record> records = recordDao.findRecord(currentProject.getID(), limit);
		logger.logp(Level.INFO, "RecordManager", "getRecords", "found records "+records);
		return records;
	}
	
	public long[] getGUIDs(String actorLogin, String projectName) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
			
		Project currentProject = SysManagerFactory.getProjectManager().getProject(projectName);
		logger.logp(Level.INFO, "RecordManager", "getGUIDs", "listing records for actor " + actorLogin +" within project "+currentProject);

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		logger.logp(Level.INFO, "RecordManager", "getGUIDs", "listing records for actor " + actorLogin +" within project "+currentProject);

		long[] guids = recordDao.getRecordGUIDs(currentProject.getID());
		logger.logp(Level.INFO, "RecordManager", "getGUIDs", "found "+(guids == null ? 0 : guids.length) +" records");
		return guids;
	}
	
	/**
	 * Returns the project id who is parent of specified guid
	 * @return the project id associated with guid
	 * @throws DataAccessException 
	 */
	public int getProjectID(long guid) throws DataAccessException
	{
		// check in cache
		CacheKey key = new CacheKey(guid, CacheKeyType.RecordId);
		if(SysManagerFactory.getCacheManager().isCached(key))
			return (Integer) SysManagerFactory.getCacheManager().get(key);

		// check in DB
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = factory.getRecordDAO().getProjectID(guid);

		// update the cache
		SysManagerFactory.getCacheManager().set(key, projectID);

		return projectID;
	}
	
	/**
	 * Returns the column values for the specified guids on the selected columns
	 * @param guids the relevant set of guids
	 * @param columns
	 * @return
	 * @throws DataAccessException
	 */
	public Map<Long, List<MetaData>> getRecordMetaData(String actorLogin, String projectName, Set<Long> guids, SearchColumn ... columns) 
			throws DataAccessException
	{
		if(guids == null || guids.isEmpty()) return null;
	    logger.logp(Level.INFO, "RecordManager", "getRecordMetaData", "get record with guid " + guids +" for project "+projectName);
	    
	    int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
	    
	    if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
    	{
    		throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
    	}
	    
	    ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
	    NavigationDAO nao = factory.getNavigationDAO(projectID);

        return nao.getRecordMetaData(guids, columns);
	}
	
	public List<Record> findRecords(String actorLogin, Set<Long> guids) throws DataAccessException 
	{
		if(guids == null || guids.isEmpty()) return null;
	    logger.logp(Level.INFO, "RecordManager", "getRecords", "get record with guid " + guids +" for user "+actorLogin);
	    
	    Set<Long> allowedList = new HashSet<Long>();
	    for(Long guid : guids)
	    {
	    	if(SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
	    	{
	    		allowedList.add(guid);
	    	}
	    }
	    ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        RecordDAO recordDao = factory.getRecordDAO();
        
        return recordDao.findRecords(allowedList);
	}
		
	public Record findRecord (String actorLogin, long guid) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName)) 
		{
			
            throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		}

	    logger.logp(Level.INFO, "RecordManager", "getRecord", "get record with guid " + guid +" for user "+actorLogin);

        return recordDao.findRecord(guid);
	}
	
	/**
	 * inserts or updates the specified annotation on the specified record
	 * @param app the application using which the method is called
	 * @param actorLogin the login ID of the login user
	 * @param guid the record GUID
	 * @param name name of the annotation
	 * @param value value of the annotation
	 * @throws DataAccessException
	 */
	public void setUserAnnotation(Application app, String actorLogin, String accessToken, long guid, String name, Object value) throws DataAccessException 
	{
		if(name == null || value == null) 
			return;
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		MetaData m = MetaData.createInstance(name, value);
		
		MetaDataDAO mao = factory.getMetaDataDAO(m.getType());
		MetaData existing = mao.find(projectID, guid, name);
		
		if(existing == null)
		{
			insertUserAnnotation(actorLogin, projectID, guid,  m);
			
			// add history
			addHistory(guid, app, actorLogin, accessToken, HistoryType.USER_ANNOTATION_ADDED, name, String.valueOf(value));
		}
		else
		{
			updateUserAnnotation(actorLogin, projectID, guid,  m);
			
			// add history
			addHistory(guid, app, actorLogin, accessToken, HistoryType.USER_ANNOTATION_MODIFIED, name, String.valueOf(value));
		}
	}
	
	private void addHistory(long guid, Application app, String user, String accessToken, HistoryType type, String... args) throws DataAccessException
	{
		SysManagerFactory.getHistoryManager().insertHistory(guid, app, user, accessToken, type, type.getDescription(guid, user, args), args);
	}
	
	public List<Site> getRecordSites(String actorLogin, long guid) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		return factory.getRecordDAO().getSites(guid);
	}
	
	/**
	 * updates the channel name of specified channel
	 * @param actorLogin
	 * @param guid
	 * @param channelNo
	 * @param newChannelName
	 */
	public void setRecordChannelName(String actorLogin, long guid, int channelNo, String newChannelName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		recordDao.updateChannelName(guid, channelNo,newChannelName);
		
		UserPreferencesDAO userPref = factory.getUserPreferencesDAO();
		userPref.updateChannels(guid, recordDao.getChannels(guid));
	}
	
	/**
	 * updates the acquisition profile for the record
	 * @param app application used for performing this operation
	 * @param actorLogin logged in user
	 * @param accessToken used in case of other applications than webclient, null in case of web client
	 * @param guid specified record
	 * @param acqProfile profile to be applied
	 * @throws DataAccessException 
	 */
	public void setAcquisitionProfile(Application app, String actorLogin, String accessToken, long guid, AcquisitionProfile acqProfile) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();

		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		if(acqProfile == null)
			return ;
		
		Record record = recordDao.findRecord(guid);
		
		SourceFormat sourceType = (acqProfile.getSourceType() == null || acqProfile.getSourceType().name==null || acqProfile.getSourceType().name.isEmpty()) ? record.getSourceFormat() : acqProfile.getSourceType();

		// modify the x,y,z pixel value
		Double xPixelSize = applyProfileOnField(record.getXPixelSize(), acqProfile.getxPixelSize(), acqProfile.getXType());
		Double yPixelSize = applyProfileOnField(record.getYPixelSize(), acqProfile.getyPixelSize(), acqProfile.getYType());
		Double zPixelSize = applyProfileOnField(record.getZPixelSize(), acqProfile.getzPixelSize(), acqProfile.getZType());
		
		// after modifying the value, store the new values computed in the
		// profile and the type of the profile is changed to VALUE
		AcquisitionProfile newProfile = new AcquisitionProfile(acqProfile.getProfileName(), acqProfile.getMicroscope(), xPixelSize, AcquisitionProfileType.VALUE, yPixelSize,
				AcquisitionProfileType.VALUE, zPixelSize, AcquisitionProfileType.VALUE, sourceType, acqProfile.getElapsedTimeUnit(), acqProfile.getExposureTimeUnit(), acqProfile.getLengthUnit());
		
		// change in record table
		recordDao.updateAcquisitionProfile(guid, newProfile);
		
		// change in nav table
		factory.getNavigationDAO(projectID).applyAcquisitionProfile(guid, newProfile);
		factory.getNavigationDAO(projectID).insertMicroscopeName(guid, acqProfile.getMicroscope());
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.PROFILE_APPLIED, newProfile.toString());
	}
	
	public Map<String,Object> getPixelProfile(List<Double> pixelSizes,List<AcquisitionProfileType> profileTypes){
		
		if(pixelSizes.size()!=profileTypes.size()||pixelSizes.size()==0)
			throw new IllegalArgumentException();
		
		double value=1;
		AcquisitionProfileType type=AcquisitionProfileType.FACTOR;
		
		for(int i=pixelSizes.size()-1;i>=0;i--){
			value*=pixelSizes.get(i);
			
			if(profileTypes.get(i)==AcquisitionProfileType.VALUE){
				type=AcquisitionProfileType.VALUE;
				break;
			}				
		}
		
		Map<String,Object> profileValues=new HashMap<String,Object>();
		profileValues.put("pixelSize",value);
		profileValues.put("type",type);
		
		return profileValues;
	}
	
	/**
	 * modify the record data using profile data and profile type
	 * @param recordData value of record field
	 * @param profileData value of the profile field
	 * @param fieldType type of the profile field
	 * @return modified record field
	 */
	private Double applyProfileOnField(Double recordData, Double profileData, AcquisitionProfileType fieldType)
	{
		// if any of the incoming arguments is null 
		// operation will not change the current state of record
		if(profileData == null)
			return recordData;
		
		if(recordData == null)
			return recordData;
		
		switch (fieldType)
		{
			case FACTOR:
				return (recordData * profileData);
			case VALUE:
			default:
				return profileData;
		}
	}

	/**
	 * adds list of user annotations on record
	 * @param app the application using which the method is called
	 * @param actorLogin the login ID of the login user
	 * @param guid the record GUID
	 * @param annotations
	 *            to be added (each metadata object contains recordId of the
	 *            record on which this annotation is added, userid of the user
	 *            who added this annotation within itself)
	 * @throws DataAccessException
	 * 
	 */
	public void addUserAnnotation(Application app, String actorLogin, String accessToken, long guid, List<MetaData> annotations) 
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "addUserAnnotation", "adding user annotations for record "+guid +" by "+actorLogin);
		if(annotations == null || annotations.isEmpty()) return;
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = getProjectID(guid);

		for(MetaData m : annotations)
		{
			try
			{
				insertUserAnnotation( actorLogin, projectID, guid, m );
				
				// add history
				addHistory(guid, app, actorLogin, accessToken, HistoryType.USER_ANNOTATION_ADDED, m.getName(), String.valueOf(m.getValue()));
			}
			catch(Exception ex)
			{
				logger.logp(Level.INFO, "RecordManager", "addUserAnnotation", "error adding user annotations for record "+guid +", "+m,ex);
			}
		}
	}
	
	/**
	 * updates the value of a particular user annotation to new value
	 * @param app the application which is used for invoking this method
	 * @param actorLogin logged in user
	 * @param guid of the specified record    
	 * @param name of the target user annotation    
	 * @param value new value of the user annotation          
	 * @return true if update successful, false otherwise
	 * @throws DataAccessException
	 */
	public boolean updateUserAnnotation(Application app, String actorLogin, String accessToken, long guid, String name, Object value)
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "updateUserAnnotation","updating user annotations for record ");
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		MetaData m = MetaData.createInstance(name, value);
		updateUserAnnotation(actorLogin, projectID, guid, m);

		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.USER_ANNOTATION_MODIFIED, m.getName(), String.valueOf(m.getValue()));
		
		return true;
	}

	/**
	 * adds comment on specified record
	 * @param app the application used for performing this operation
	 * @param actorLogin logged in user
	 * @param guid specified record 
	 * @param comment to be added
	 * @throws DataAccessException
	 */
	public void addUserComment(Application app, String actorLogin, String accessToken, long guid, String comment) throws DataAccessException 
	{
		comment = comment == null ? null : comment.trim();
		if(comment == null || comment.length() == 0) return;
		logger.logp(Level.INFO, "RecordManager", "addUserComment", "adding user comments for record ");
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserCommentDAO uao = factory.getUserCommentDAO();
		
		uao.insertComment(guid, actorLogin, comment);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.USER_COMMENT_ADDED, comment);
	}
	
	/**
	 * delete comment by its Id
	 * @param commentId
	 * @throws DataAccessException 
	 */
	public boolean deleteComment(Application app, long guid, long commentId , String projectName, String user) throws DataAccessException{
		
    	Permission permission=SysManagerFactory.getUserPermissionManager().getUserPermission(user, projectName); 
    	
    	UserComment userComment=ImageSpaceDAOFactory.getDAOFactory().getUserCommentDAO().findCommentById(commentId);    	   	    	   
    	
    	if(permission.ordinal()<=Permission.Manager.ordinal()||userComment.getUserLogin().equals(user)){  		
    		ImageSpaceDAOFactory.getDAOFactory().getUserCommentDAO().deleteComment(commentId);  
    		//add history
    		addHistory(guid, app, user, null, HistoryType.USER_COMMENT_DELETED, userComment.getNotes());
    		
    		return true;
    	}
    	
    	return false;
	}

	
	/**
	 * Returns the list of user comments on this record
	 * @param actorLogin the user making the request
	 * @param guid GUID of the specified record
	 * @return the list of user comments on this record
	 * @throws DataAccessException
	 */
	public List<UserComment> getUserComments(String actorLogin, long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getUserComments", "retriving user comments for record ");
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserCommentDAO uao = factory.getUserCommentDAO();
		return uao.findComments(guid);
	}
	
	/**
	 * Returns the lists the user annotations added on the specified record and of specified
	 * type
	 * @param actorLogin the user making the request        
	 * @param guid  of the specified record
	 * @return list of meta data of the specified type
	 * @throws DataAccessException
	 */
	public List<MetaData> getUserAnnotations(String actorLogin, long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getUserAnnotations", "retriving user annotations for record ");
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		List<MetaData> metaDataList = new ArrayList<MetaData>(); 
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		
		List<MetaData> intMetaDataList = factory.getMetaDataDAO(AnnotationType.Integer).find(projectID, guid);
		if(intMetaDataList != null) metaDataList.addAll(intMetaDataList);
		
		List<MetaData> realMetaDataList = factory.getMetaDataDAO(AnnotationType.Real).find(projectID, guid);
		if(realMetaDataList != null) metaDataList.addAll(realMetaDataList);
		
		List<MetaData> textMetaDataList = factory.getMetaDataDAO(AnnotationType.Text).find(projectID, guid);
		if(textMetaDataList != null) metaDataList.addAll(textMetaDataList);
		
		List<MetaData> dateMetaDataList = factory.getMetaDataDAO(AnnotationType.Time).find(projectID, guid);
		if(dateMetaDataList != null) metaDataList.addAll(dateMetaDataList);
		
		return metaDataList;
	}

	/**
	 * Returns the lists the user annotations added on the specified record and of specified
	 * type
	 * @param actorLogin the user making the request        
	 * @param guid  of the specified record
	 * @param type of the target user annotation
	 * @return list of meta data of the specified type
	 * @throws DataAccessException
	 */
	public List<MetaData> getUserAnnotations(String actorLogin, long guid, AnnotationType type) 
			throws DataAccessException 
	{
		
		logger.logp(Level.INFO, "RecordManager", "getUserAnnotations", "retriving user annotations for record "+guid +" by "+actorLogin);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 
		
		return factory.getMetaDataDAO(type).find(projectID, guid);
	}
	
	public List<VisualOverlay> getVisualOverlays(String actorLogin, long guid, VODimension d) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getVisualOverlays", "returning visual annotations for record "+guid);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = getProjectID(guid);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		return factory.getVisualOverlaysDAO().getVisualOverlays(projectID, guid, d);
	}

	/**
     * Returns the specific of visual overlay associated with the specified coordinate and name 
     * @param signature  the record signature under consideration 
     * @param name name of the visual annotation
     * @return the visual overlay associated with the specified dimension and name 
     */
	public VisualOverlay getVisualOverlay(String actorLogin, long guid, VODimension d, String name) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getVisualOverlay", "returning visual annotations for image ");
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = getProjectID(guid);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		return factory.getVisualOverlaysDAO().getVisualOverlay(projectID, guid,	d, name);
	}

	
	public  List<String> getAvailableVisualOverlays(String actorLogin, long guid, int siteNo) throws DataAccessException 
    {
		logger.logp(Level.INFO, "RecordManager", "getAvailableVisualOverlays", "returning visual annotations for image ");
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = getProjectID(guid);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		return  factory.getVisualOverlaysDAO().getAvailableVisualOverlays(projectID, guid, siteNo);
	}

	/**
     * Creates visual overlays for the specified record for all relevant frames and slices for the fixed site
     * @param app application used for performing this operation
     * @param actorLogin logged in user
     * @param guid the record signature under consideration
     * @param siteNo the site
     * @param name name of the visual annotation
     * @return a overlay that is created
     */
	public void createVisualOverlays(Application app, String actorLogin, String accessToken, long guid, int siteNo, String name) throws DataAccessException 
    {
		logger.logp(Level.INFO, "RecordManager", "createVisualOverlays", "creating visual annotations for record "+guid);
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int projectID = getProjectID(guid);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualOverlaysDAO vao = factory.getVisualOverlaysDAO();
		java.awt.Dimension d = factory.getRecordDAO().getImageSize(guid);
		
		boolean userGenerated = isUserGenerated(app);
		
		vao.createVisualOverlays(actorLogin, projectID, guid, d.width, d.height, siteNo, name, userGenerated);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.VISUAL_OVERLAY_ADDED, name, String.valueOf(siteNo));
	}

	private boolean isUserGenerated(Application app)
	{
		if(app.name.equals("Acquisition Client") || app.name.equals(com.strandgenomics.imaging.icore.Constants.getWebApplicationName()))
			return true;
		return false;
	}

	/**
	 * Deletes all visual overlays for the specified record for all relevant frames and slices for the fixed site
	 * @param app application used for performing this operation
	 * @param actorLogin logged in user
	 * @param guid specified record
	 * @param siteNo specified site
	 * @param name specified overlay
	 * @throws DataAccessException
	 */
	public void deleteVisualOverlays(Application app, String actorLogin, String accessToken, long guid, int siteNo, String name) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS)); 

		logger.logp(Level.INFO, "RecordManager", "deleteVisualOverlays", "deleting visual annotations for record " + guid);

		VisualOverlaysDAO vao = factory.getVisualOverlaysDAO();
		vao.deleteVisualOverlays(projectID, guid, siteNo, name);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.VISUAL_OVERLAY_DELETED, name, String.valueOf(siteNo));
	}

	/**
	 * Adds the specified visual objects to the visual-overlays of the specified records 
	 * @param signature the record signature under consideration 
	 * @param vObjects the shapes - visual objects
	 * @param name name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 * @throws IOException 
	 */
	public void addVisualObjects(Application app, String actorLogin, String accessToken, long guid,	List<VisualObject> vObjects, String overlayName, VODimension ... d) throws IOException 
	{
		logger.logp(Level.INFO, "RecordManager", "addVisualObjects", "adding visual objects for record "+guid);
		
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		if(vObjects == null || vObjects.isEmpty() || d == null || d.length == 0) return;

		int projectID = getProjectID(guid);
 
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualObjectsDAO vao = factory.getVisualObjectsDAO();
		
		vao.addVisualObjects(projectID, guid, overlayName, vObjects, d);
		updateRevisionVisualOverlay(actorLogin, projectID, guid, overlayName, d);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.VISUAL_ANNOTATION_ADDED, overlayName, String.valueOf(vObjects.size()));
	}
	
	private void updateRevisionVisualOverlay(String actor, int projectID, long guid, String overlayName, VODimension[] d) throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualOverlaysRevisionDAO vodao = factory.getVisualOverlaysRevisionDAO();

		for (VODimension vod : d)
		{
			vodao.incrementRevision(projectID, guid, vod.siteNo, overlayName, vod.sliceNo, vod.frameNo);
			try
			{
				SysManagerFactory.getMovieManager().refreshMovieImage(actor, guid, vod.sliceNo, vod.frameNo, vod.siteNo);
			}
			catch (IOException e)
			{
				logger.logp(Level.WARNING, "RecordManager", "updateRevisionVisualOverlay", "error submitting movie refresh request", e);
			}
		}
	}
	

	private void updateRevisionVisualOverlay(String actor, int projectID, long guid, String name, VODimension d) throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualOverlaysRevisionDAO vodao = factory.getVisualOverlaysRevisionDAO();
		vodao.incrementRevision(projectID, guid, d.siteNo, name, d.sliceNo, d.frameNo);
		try
		{
			SysManagerFactory.getMovieManager().refreshMovieImage(actor, guid, d.sliceNo, d.frameNo, d.siteNo);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "RecordManager", "updateRevisionVisualOverlay", "error submitting movie refresh request", e);
		}
	}
	
	/**
	 * sets the list of visual objects to specified visual overlay. 
	 * updates if objects already exists, creates new otherwise 
	 * @param actorLogin of logged in user
	 * @param guid of specified record
	 * @param vObjects new visual objects
	 * @param overlayName of specified overlay
	 * @param d list of visual dimentions to which this overlay has to be applied
	 * @throws IOException 
	 */
	public void setVisualObjects(Application app, String actorLogin, String accessToken, long guid,	List<VisualObject> vObjects, String overlayName, VODimension ... d) throws IOException
	{
		logger.logp(Level.INFO, "RecordManager", "setVisualObjects", "setting visual objects for record "+guid);
		deleteAllVisualObjects(app, actorLogin, accessToken, guid, overlayName, d);//delete all existing visual objects
		addVisualObjects(app, actorLogin, accessToken, guid, vObjects, overlayName, d);//add new visual objects
	}

	/**
	 * Removes the specified visual objects from the visual-overlays of the specified records 
	 * @param actorLogin logged in user
	 * @param guid specified record 
	 * @param vObjects specified objects
	 * @param name specified overlay
	 * @param d specified dimension
	 * @throws IOException 
	 */
    public void deleteVisualObjects(Application app, String actorLogin, String accessToken, long guid, List<VisualObject> vObjects, String name, VODimension ... d) throws IOException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		logger.logp(Level.INFO, "RecordManager", "deleteVisualObjects",	"deleting visual objects for record "+guid);
		
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualObjectsDAO vao = factory.getVisualObjectsDAO();
		
		vao.deleteVisualObjects(projectID, guid, vObjects, name, d);
		updateRevisionVisualOverlay(actorLogin, projectID, guid, name, d);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.VISUAL_ANNOTATION_DELETED, name, String.valueOf(vObjects.size()));
	}
    
    /**
     * Removes the specified visual objects from the visual-overlays of the specified records 
     * @param actorLogin logged in user
     * @param guid specified record
     * @param name specified overlay
     * @param d specified dimension
     * @param voID specified objects
     * @throws IOException 
     */
    public void deleteVisualObjects(Application app, String actorLogin, String accessToken, long guid, String name, VODimension d, int ... voID) throws IOException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		logger.logp(Level.INFO, "RecordManager", "deleteVisualObjects",	"deleting visual objects for record "+guid);
		
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualObjectsDAO vao = factory.getVisualObjectsDAO();
		
		vao.deleteVisualObjects(projectID, guid, name, d, voID);
		updateRevisionVisualOverlay(actorLogin, projectID, guid, name, d);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.VISUAL_ANNOTATION_DELETED, name, String.valueOf(voID.length));
	}
    
	public void deleteTextObjects(Application app, String actorLogin, String accessToken, long guid, String name, VODimension d, int ... voID) throws IOException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		logger.logp(Level.INFO, "RecordManager", "deleteVisualObjects",	"deleting visual objects for record "+guid);
		
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		VisualObjectsDAO vao = factory.getVisualObjectsDAO();
		
		vao.deleteTextObjects(projectID, guid, name, d, voID);
		updateRevisionVisualOverlay(actorLogin, projectID, guid, name, d);
		
		// add history
		addHistory(guid, app, actorLogin, accessToken, HistoryType.VISUAL_ANNOTATION_DELETED, name, String.valueOf(voID.length));
	}
    
    /**
     * deletes all visual objects on given overlay
     * @param actorLogin of logged in user
     * @param guid of specified record
     * @param name of specified overlay 
     * @param d list of dimentions
     * @throws IOException 
     */
    private void deleteAllVisualObjects(Application app, String actorLogin, String accessToken, long guid, String name, VODimension ... d) throws IOException
    {
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
    	
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));  
		
		logger.logp(Level.INFO, "RecordManager", "deleteAllVisualObjects",	"deleting all visual objects for record "+guid);
		
		VisualObjectsDAO vao = factory.getVisualObjectsDAO();
		
		vao.deleteVisualObjects(projectID, guid, name, d);
		updateRevisionVisualOverlay(actorLogin, projectID, guid, name, d); 
    }

	/**
     * Return all visual objects with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param name name of the overlay to delete (is unique for a record)
     * @return an array of visual objects
     */
	public List<VisualObject> getVisualObjects(String actorLogin, long guid, VODimension d, String overlayName) 
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getVisualObjects", "returning visual objects for overlay "+overlayName);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		return factory.getVisualObjectsDAO().getVisualObjects(projectID, guid, overlayName, d);
	}

	/**
	 * returns the list of elliptical objects associated with specified overlay on specified record
	 * @param actorLogin 
	 * @param projectID of parent project
	 * @param guid of specified record
	 * @param name of specified overlay
	 * @return 
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getEllipticalShapes(String actorLogin, long guid, VODimension d, String overlayName)
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getEllipticalShapes", "returning elliptical shapes for overlay "+overlayName);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		return factory.getVisualObjectsDAO().getEllipticalShapes(projectID, guid, overlayName, d);
	}
	
	/**
	 * returns the list of line segments associated with specified overlay on specified record
	 * @param actorLogin 
	 * @param projectID of parent project
	 * @param guid of specified record
	 * @param name of specified overlay
	 * @return 
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getLineSegments(String actorLogin, long guid, VODimension d, String overlayName) 
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getLineSegments", "returning line shapes for overlay "+overlayName);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		return factory.getVisualObjectsDAO().getLineSegments(projectID, guid, overlayName, d);
	}
	
	/**
	 * returns the list of rectangular objects associated with specified overlay on specified record
	 * @param actorLogin 
	 * @param projectID of parent project
	 * @param guid of specified record
	 * @param name of specified overlay
	 * @return 
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getRectangularShapes(String actorLogin, long guid, VODimension d, String overlayName) 
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getRectangularShapes", "returning rectangular shapes for overlay "+overlayName);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID =getProjectID(guid);

		return factory.getVisualObjectsDAO().getRectangularShapes(projectID, guid, overlayName, d);
	}
	

	/**
	 * returns the list of free hand objects associated with specified overlay on specified record
	 * @param actorLogin 
	 * @param projectID of parent project
	 * @param guid of specified record
	 * @param name of specified overlay
	 * @return 
	 * @throws DataAccessException 
	 */
	public List<VisualObject> getFreeHandShapes(String actorLogin, long guid, VODimension d, String overlayName) 
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getFreeHandShapes", "returning free hand shapes for overlay ");
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		return factory.getVisualObjectsDAO().getFreeHandShapes(projectID, guid, overlayName, d);
	}
	
	public List<VisualObject> getTextBoxes(String actorLogin, long guid, VODimension d, String overlayName) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "getFreeHandShapes", "returning free hand shapes for overlay "+overlayName);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		return factory.getVisualObjectsDAO().getTextBoxes(projectID, guid, overlayName, d);
	}

	/**
	 * returns the pixel data for specified image of specified record
	 * @param signature of record
	 * @param coordinate dimension - image-coordinate of the image
	 * @return the pixel data for specified image
	 * @throws DataAccessException
	 */
	public ImagePixelData getPixelDataForRecord(String actorLogin, long guid, Dimension coordinate) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));  
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		ImagePixelDataDAO pixelDataDao = factory.getImagePixelDataDAO();
		return pixelDataDao.find(guid, coordinate);
	}
	
	public Long findGuid(String actorLogin, Signature signature, boolean checkForDummy) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "findGuid", "finding guid login "+actorLogin+" signature "+signature);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		if(checkForDummy)
		{
			BigInteger archiveHash = factory.getArchiveDAO().getComputedArchiveSignature(signature.archiveHash);
			if(archiveHash!=null)
			{
				signature = new Signature(signature.noOfFrames, signature.noOfSlices, signature.noOfChannels, signature.noOfSites, signature.imageWidth, signature.imageHeight, signature.siteHash, archiveHash);
			}
		}
		RecordDAO recordDao = factory.getRecordDAO();
		logger.logp(Level.INFO, "RecordManager", "findGuid", "finding guid "+recordDao+" login "+actorLogin+" signature "+signature);
		return recordDao.findGUID(signature);
	}
	
	private void insertUserAnnotation(String actorLogin, int projectID, long guid,  MetaData m) 
			throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		MetaDataDAO   mao = factory.getMetaDataDAO(m.getType());
		NavigationDAO nao = factory.getNavigationDAO(projectID);
		
		mao.insertUserAnnotation(projectID, guid, actorLogin, m.getName(), m.getValue());
		nao.insertUserAnnotation(guid, m);
	}
	
	private void updateUserAnnotation(String actorLogin, int projectID, long guid, MetaData m)
			throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		MetaDataDAO   mao = factory.getMetaDataDAO(m.getType());
		NavigationDAO nao = factory.getNavigationDAO(projectID);
		
		mao.update(projectID, guid, actorLogin, m.getName(), m.getValue());
		nao.insertUserAnnotation(guid, m);
	}

	public boolean isExistingRecord(String actorLogin, Signature signature) throws DataAccessException 
	{
		logger.logp(Level.INFO, "RecordManager", "isExistingRecord", "checking if record exists for signature " + signature);
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		Long guid = recordDao.findGUID(signature);
		
		return guid != null;
	}

	/**
	 * find the dimensions where the specified visual object exists
	 * @param actorLogin logged in user
	 * @param signature of parent record
	 * @param siteNo on which overlay exists
	 * @param overlayName of specified overlay
	 * @param id of object to be searched 
	 * @return 
	 * @throws DataAccessException 
	 */
	public List<VODimension> findVisualObjectLocation(String actorLogin, long guid, int siteNo, String overlayName, int id) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		return factory.getVisualObjectsDAO().findVisualObjectLocation(projectID, guid, siteNo, overlayName, id);
	}

	/**
	 * find the dimensions where the specified overlay exists (non-empty visual objects)
	 * @param currentUser the logged in user
	 * @param signature the record under consideration
	 * @param siteNo of specified site on which overlay exists
	 * @param overlayName name of the overlay
	 * @return the dimensions where there are at least one visual objects
	 */
	public List<VODimension> findOverlayLocation(String actorLogin, long guid, int siteNo, String overlayName) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		
		return factory.getVisualObjectsDAO().findOverlayLocation(projectID, guid, siteNo, overlayName);
	}

	public List<VisualObject> findVisualObjects(String actorLogin, long guid, int frame, int slice, int site, String overlayName, int x, int y, int height, int width) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		int projectID = getProjectID(guid);
		
		return factory.getVisualObjectsDAO().findVisualObjects(projectID, guid, frame, slice, site, overlayName, x, y, height, width);
	}

	/**
	 * transfer for selected records to target project
	 * @param ids of records to be transfered
	 * @param targetProjectId id of target project
	 * @throws DataAccessException 
	 */
	void updateParentProject(long[] ids, int targetProjectId) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		factory.getRecordDAO().transferRecords(ids, targetProjectId);
		
		CacheManager cacheManager = SysManagerFactory.getCacheManager();
		for(long id:ids)
		{
			cacheManager.set(new CacheKey(id, CacheKeyType.RecordId), targetProjectId);
		}
	}
}
