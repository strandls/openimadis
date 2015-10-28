/*
 * UserPreference.java
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.UserPreferencesDAO;
import com.strandgenomics.imaging.iengine.models.LegendField;
import com.strandgenomics.imaging.iengine.models.LegendLocation;
import com.strandgenomics.imaging.iengine.models.LegendType;
import com.strandgenomics.imaging.iengine.models.SearchColumn;

/**
 * Manages the user preferences about the navigator and channel color 
 */
public class UserPreference extends SystemManager {
	
	UserPreference()
	{}
	
	public void markProjectUse(String userLogin, String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		
		dao.getLastAccessTime(userLogin, projectID);// create the entry if not there for this project & user
		dao.updateProjectUsage(userLogin, projectID);
	}
	
	public int getNumberOfNavigatorBins(String userLogin, String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		return dao.getNumberOfNavigatorBins(userLogin, projectID);
	}
	
	public boolean isBinsAreInAscendingOrder(String userLogin, String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		return dao.isBinsAreInAscendingOrder(userLogin, projectID);
	}
	
	public List<SearchColumn> getSpreadSheetColumns(String userLogin, String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		
		List<SearchColumn> spreadSheetColumns=null;
		try{
			spreadSheetColumns=dao.getSpreadSheetColumns(userLogin, projectID);
		}
		catch(DataAccessException e){
			logger.logp(Level.INFO, "UserPreference", "getSpreadSheetColumns","spreadSheetColumns are not present",e);
		}
		return spreadSheetColumns;
	}
	
	public void setSpreadSheetColumns(String userLogin, String projectName, List<SearchColumn> columns) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		
		dao.getLastAccessTime(userLogin, projectID);// create the entry if not there for this project & user
		dao.setSpreadSheetColumns(userLogin, projectID, columns);
	}
	
	public List<SearchColumn> getNavigationColumns(String userLogin, String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		List<SearchColumn> colms = null;
		
		try{
			colms=dao.getNavigationColumns(userLogin, projectID);
		}
		catch(DataAccessException e){
			logger.logp(Level.INFO, "UserPreference", "getNavigationColumns","navigationColumns are not present",e);
		}
		
		if(colms==null || colms.size()==0)
		{
			colms = getDefaultNavigationColumns();
		}
		return colms;
	}
	
	private List<SearchColumn> getDefaultNavigationColumns() {
        List<SearchColumn> fixedColumns = new ArrayList<SearchColumn>();

        fixedColumns.add(new SearchColumn("Uploaded By", AnnotationType.Text, "uploaded_by"));
        
        fixedColumns.add(new SearchColumn("Microscope Name", AnnotationType.Text, "microscope_name"));

        fixedColumns.add(new SearchColumn("Slice Count", AnnotationType.Integer, "number_of_slices"));
        fixedColumns.add(new SearchColumn("Frame Count", AnnotationType.Integer, "number_of_frames"));
        fixedColumns.add(new SearchColumn("Channel Count", AnnotationType.Integer, "number_of_channels"));
        fixedColumns.add(new SearchColumn("Site Count", AnnotationType.Integer, "number_of_sites"));

        fixedColumns.add(new SearchColumn("Image Width", AnnotationType.Integer, "image_width"));
        fixedColumns.add(new SearchColumn("Image Height", AnnotationType.Integer, "image_height"));

        fixedColumns.add(new SearchColumn("Source Folder", AnnotationType.Text, "source_folder"));
        fixedColumns.add(new SearchColumn("Source File", AnnotationType.Text, "source_filename"));

        return fixedColumns;
    }
	
	public List<SearchColumn> getOverlayColumns(String userLogin, String projectName) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		
		List<SearchColumn> overlayColumns=null;
		try{
			overlayColumns=dao.getSpreadSheetColumns(userLogin, projectID);
		}
		catch(DataAccessException e){
			logger.logp(Level.INFO, "UserPreference", "getOverlayColumns","overlayColumns are not present",e);
		}
		return overlayColumns;
	}
	
	public List<String> getRecentProjects(String userLogin, int maxProjects) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int[] projectIDs = dao.getRecentProjects(userLogin, maxProjects);
		if(projectIDs == null) return null;
		
		ProjectManager pm = SysManagerFactory.getProjectManager();
		List<String> projectList = new ArrayList<String>();
		
		logger.logp(Level.INFO, "UserPreference", "getRecentProjects","projectIDs="+projectIDs.length);
				
		for(int projectID : projectIDs)
		{
			projectList.add( pm.getProject(projectID).getName() );
		}
		
		return projectList;
	}
	
	public void setNavigationColumns(String userLogin, String projectName, List<SearchColumn> columns) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		
		dao.getLastAccessTime(userLogin, projectID);// create the entry if not there for this project & user
		dao.setNavigationColumns(userLogin, projectID, columns);
	}
	
	public void setOverlayColumns(String userLogin, String projectName, List<SearchColumn> columns) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
		
		dao.getLastAccessTime(userLogin, projectID);// create the entry if not there for this project & user
		dao.setOverlayColumns(userLogin, projectID, columns);
	}
	
	public VisualContrast getChannelContrast(String actorLogin, long guid, Dimension coordinate, boolean zStacked) throws IOException
	{
		 List<Channel> channels = getChannels(actorLogin, guid);
		 VisualContrast contrast = channels.get(coordinate.channelNo).getContrast(zStacked);
		 
		 if(contrast == null)
		 {
			 contrast = computeContrast(actorLogin, guid, coordinate, zStacked);
			 setCustomContrast(actorLogin, guid, coordinate.channelNo, zStacked, contrast.getMinIntensity(), contrast.getMaxIntensity(), 1.0);
		 }
		 
		 return contrast;
	}
	
	/**
	 * This function does not compute default contrast but simply returns if contrast is set
	 * */
	
	public VisualContrast getChannelContrast(String actorLogin, long guid, int channelNumber, boolean zStacked) throws IOException
	{
		 List<Channel> channels = getChannels(actorLogin, guid);
		 VisualContrast contrast = channels.get(channelNumber).getContrast(zStacked);
		 return contrast;
	}
	
	
	/**
	 * computes the auto contrast for the specified record
	 * @param guid
	 * @param coordinate
	 * @param zStacked
	 * @return
	 * @throws IOException
	 */
	private VisualContrast computeContrast(String actorLogin, long guid, Dimension coordinate, boolean zStacked) throws IOException
	{
		ImageManager im = SysManagerFactory.getImageManager();
		Histogram stat = im.getIntensityDistibution(actorLogin, guid, coordinate, zStacked, null);
		
		return new VisualContrast(stat.getMin(), stat.getMax(), 1.0);
	}
	
	public void setCustomContrast(String currentUser, Signature signature, int channelNo, boolean zStacked, int minIntensity, int maxIntensity, double gamma) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		long guid = recordDao.findGUID(signature);
		
		setCustomContrast(currentUser, guid, channelNo, zStacked, minIntensity, maxIntensity, gamma);
	}
	
	/**
	 * Sets the contrast to the specified channel of given record for the login user
	 * @param actorLogin login id of the user under consideration
	 * @param guid the guid of the record
	 * @param channelNo the channel number
	 * @param minIntensity the minimum intensity value (contrast)
	 * @param maxIntensity the maximum intensity value (contrast)
	 * @param gamma the gamma value
	 */
    public synchronized void setCustomContrast(String actorLogin, long guid, int channelNo, boolean zStacked, 
    		int minIntensity, int maxIntensity, double gamma)  throws DataAccessException
    {
       	if(minIntensity >= maxIntensity)
    		throw new IllegalArgumentException("minIntensity("+minIntensity+") >= maxIntensity("+maxIntensity+") is not acceptable ");

       	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
       	
		List<Channel> channels = getCustomChannels(actorLogin, guid);
		if(channels == null) //insert custom
		{
			channels = getDefaultChannels(actorLogin, guid);
			
			VisualContrast contrast = new VisualContrast(minIntensity, maxIntensity, gamma);
			channels.get(channelNo).setContrast(zStacked, contrast);
			
			UserPreferencesDAO dao = factory.getUserPreferencesDAO();
			dao.addChannels(actorLogin, guid, channels);
		}
		else //update
		{
			VisualContrast contrast = new VisualContrast(minIntensity, maxIntensity, gamma);
			channels.get(channelNo).setContrast(zStacked, contrast);
			
			//update revision number
			long revision=channels.get(channelNo).getRevision()+1;
			channels.get(channelNo).setRevision(revision);
			
			UserPreferencesDAO dao = factory.getUserPreferencesDAO();
			dao.updateChannels(actorLogin, guid, channels);
		}
    }
	
	public void setChannelLUT(String currentUser, Signature signature, int channelNo, String lutName) throws DataAccessException 
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		long guid = recordDao.findGUID(signature);
		
		setChannelLUT(currentUser, guid, channelNo, lutName);
	}
	
	/**
	 * Sets the channel color for the specified channel number
	 * @param userLogin the relevant user
	 * @param guid the GUID of the record
	 * @param channelNo the channel number
	 * @param channelColor the corresponding channel color
	 * @throws DataAccessException 
	 */
	public void setChannelLUT(String userLogin, long guid, int channelNo, String lutName) throws DataAccessException
	{
		if(lutName == null)
			throw new NullPointerException("channel lut cannot be null");
		
		List<Channel> channels = getCustomChannels(userLogin, guid);
		if(channels == null) //insert custom
		{
			channels = getDefaultChannels(userLogin, guid);
			channels.get(channelNo).setLut(lutName);
			
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			UserPreferencesDAO dao = factory.getUserPreferencesDAO();
			dao.addChannels(userLogin, guid, channels);
		}
		else //update
		{
			channels.get(channelNo).setLut(lutName);
			
			//update revision number
			long revision=channels.get(channelNo).getRevision()+1;
			channels.get(channelNo).setRevision(revision);
			
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			UserPreferencesDAO dao = factory.getUserPreferencesDAO();
			dao.updateChannels(userLogin, guid, channels);
		}
	}
	
	public List<Channel> getChannels(String userLogin, long guid) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(userLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));  
		
		List<Channel> channels = getCustomChannels(userLogin, guid);
		if(channels == null)
		{
			channels = getDefaultChannels(userLogin, guid);
		}
		return channels;
	}

	public List<Channel> getDefaultChannels(String userLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "UserPreference", "getDefaultChannels","reading default channel settings for record "+guid);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO rao = factory.getRecordDAO();
		return rao.getChannels(guid);
	}
	
	private List<Channel> getCustomChannels(String userLogin, long guid) throws DataAccessException
	{
		logger.logp(Level.FINE, "UserPreference", "getCustomChannels","reading custom channel settings for user "+userLogin);
		List<Channel> channels = null;
		try
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			UserPreferencesDAO dao = factory.getUserPreferencesDAO();
			channels = dao.getChannels(userLogin, guid);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "UserPreference", "getCustomChannels","error reading custom channel settings for user "+userLogin,ex);
		}
		return channels;
	}

	/**
	 * get the legend fields for user
	 * @param actorLogin specified user
	 * @return legend fields for user
	 */
	public List<LegendField> getLegendFields(String actorLogin)
	{
		logger.logp(Level.FINE, "UserPreference", "getLegendFields","reading legends for user "+actorLogin);
		List<LegendField> legends = null;
		try
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			UserPreferencesDAO dao = factory.getUserPreferencesDAO();
			legends = dao.getLegends(actorLogin);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "UserPreference", "getLegendFields","error reading custom legend fields for user "+actorLogin,ex);
		}
		return legends;
	}

	/**
	 * sets legend columns for specified user
	 * @param actorLogin logged in user
	 * @param chosenFields chosen legend fields
	 * @throws DataAccessException 
	 */
	public void setLegendFields(String actorLogin, List<LegendField> chosenFields) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		if(dao.getLegends(actorLogin)==null)
			dao.setLegendFields(actorLogin, chosenFields);
		else
			dao.updateLegendFields(actorLogin, chosenFields);
	}
	
	/**
	 * 
	 * @param actorLogin
	 * @return
	 * @throws DataAccessException
	 */
	public LegendLocation getLegendLocation(String actorLogin) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		return dao.getLegendLocation(actorLogin);
	}
	
	/**
	 * 
	 * @param actorLogin
	 * @param location
	 * @throws DataAccessException
	 */
	public void setLegendLocation(String actorLogin, LegendLocation location) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		UserPreferencesDAO dao = factory.getUserPreferencesDAO();
		
		dao.setLegendLocation(actorLogin, location);
	}

	public List<LegendField> getAllLegendFields()
	{
		List<LegendField> legendField = new ArrayList<LegendField>();
		legendField.add(new LegendField("Elapsed Time", LegendType.IMAGE_METADATA));
		legendField.add(new LegendField("Exposure Time", LegendType.IMAGE_METADATA));
		legendField.add(new LegendField("X Coordinate", LegendType.IMAGE_METADATA));
		legendField.add(new LegendField("Y Coordinate", LegendType.IMAGE_METADATA));
		legendField.add(new LegendField("Z Coordinate", LegendType.IMAGE_METADATA));
		return legendField;
	}
}
