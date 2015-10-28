/*
 * DBVisualOverlaysDAO.java
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

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.VisualObjectsDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;

/**
 * Gives access to visual overlays
 */
public class DBVisualOverlaysDAO extends StorageDAO<VisualOverlay> implements VisualOverlaysDAO {
	
	DBVisualOverlaysDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "VisualOverlaysDAO.xml");
	}
	
	@Override
	public List<String> getAvailableVisualOverlays(int projectID, long guid, Integer siteNo) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getAvailableVisualOverlays", "fetching visual overlay names for siteNo="+siteNo);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("AVAILABLE_OVERLAY_NAMES");
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getAvailableVisualOverlays", "2fetching visual overlay names for siteNo="+siteNo);
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getAvailableVisualOverlays", "3fetching visual overlay names for siteNo="+siteNo);
		
		sqlQuery.setValue("GUID", guid,    Types.BIGINT);
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getAvailableVisualOverlays", "3fetching visual overlay names for siteNo="+siteNo);
		sqlQuery.setValue("Site", siteNo,  Types.INTEGER);
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getAvailableVisualOverlays", "4fetching visual overlay names for siteNo="+siteNo);

		String[] names = getRowsWithStringValues(sqlQuery);
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getAvailableVisualOverlays", "5fetching visual overlay names for siteNo="+siteNo);
		return names == null || names.length == 0 ? null : Arrays.asList(names);
	}
	
	
	@Override
	public VisualOverlay getVisualOverlay(int projectID, long guid, VODimension d, String name) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getVisualOverlay", "fetching visual overlay for "+d +",name="+name);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OVERLAY");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));

		sqlQuery.setValue("GUID",     guid,        Types.BIGINT);
		sqlQuery.setValue("Site",     d.siteNo,    Types.INTEGER);
		sqlQuery.setValue("Name",     name,        Types.VARCHAR);

		VisualOverlay overlay = fetchInstance(sqlQuery);
		if(overlay != null)
		{
			overlay.setFrame(d.frameNo);
			overlay.setSlice(d.sliceNo);
		}
		
		return overlay;
	}
	
	@Override
	public List<VisualOverlay> getVisualOverlays(int projectID, long guid, VODimension d) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "getVisualOverlaysForRecord", "list visual overlays for record "+guid +",vod="+d);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_VISUAL_OVERLAY_FOR_RECORD");
		
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));

		sqlQuery.setValue("GUID", guid,       Types.BIGINT);
		sqlQuery.setValue("Site",    d.siteNo,    Types.INTEGER);

		RowSet<VisualOverlay> result = find(sqlQuery);
		List<VisualOverlay> overlays = result == null ? null : result.getRows();
		
		if(overlays != null)
		{
			for(VisualOverlay o : overlays)
			{
				o.setFrame(d.frameNo);
				o.setSlice(d.sliceNo);
			}
		}
		
		return overlays;
	}

	@Override
	public void createVisualOverlays(String actorLogin, int projectID, long guid, int width, int height, int siteNo, String name, boolean isUserGenerated) 
			throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "createVisualOverlays", "adding visual overlay siteNo="+siteNo);
		Dimension all = factory.getRecordDAO().getDimension(guid);
		if(siteNo >= all.siteNo) throw new IllegalArgumentException("illegal site number "+siteNo);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_VISUAL_OVERLAY");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID",     guid,       Types.BIGINT);
		sqlQuery.setValue("Width",    width,      Types.INTEGER);
		sqlQuery.setValue("Height",   height,     Types.INTEGER);
		sqlQuery.setValue("Site",     siteNo,     Types.INTEGER);
		sqlQuery.setValue("Name",     name,       Types.VARCHAR);
		sqlQuery.setValue("UserID",   actorLogin, Types.VARCHAR);
		sqlQuery.setValue("HandCreated",   isUserGenerated, Types.BOOLEAN);

		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteVisualOverlays(int projectID, long guid, int siteNo, String overlayName) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "deleteVisualOverlays", "deleting visual overlay for siteNo="+siteNo +"name="+overlayName);
		Dimension all = factory.getRecordDAO().getDimension(guid);
		if(siteNo >= all.siteNo) throw new IllegalArgumentException("illegal site number "+siteNo);
		
		//delete all relevant visual objects
		VisualObjectsDAO voDao = factory.getVisualObjectsDAO();
		voDao.deleteVisualObjects(projectID, guid, siteNo, overlayName);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_VISUAL_OVERLAY_FOR_RECORD");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid,        Types.BIGINT);
		sqlQuery.setValue("Site", siteNo,      Types.INTEGER);
		sqlQuery.setValue("Name", overlayName, Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	@Override
	public void deleteVisualOverlaysByGUID(int projectID, long guid) throws DataAccessException 
	{
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "deleteVisualOverlays", "deleting visual overlay for guid="+guid);
		
		//delete all relevant visual objects
		VisualObjectsDAO voDao = factory.getVisualObjectsDAO();
		voDao.deleteVisualObjectsByGUID(projectID, guid);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_VISUAL_OVERLAY_BY_GUID");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));
		
		sqlQuery.setValue("GUID", guid,        Types.BIGINT);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public VisualOverlay createObject(Object[] columnValues) 
	{
		int width = Util.getInteger(columnValues[0]);
		int height = Util.getInteger(columnValues[1]);
		int site = Util.getInteger(columnValues[2]);
		String name = (String) columnValues[3];
		boolean handCreated = Util.getBoolean(columnValues[4]);
		
		logger.logp(Level.INFO, "DBVisualOverlaysDAO", "createObject", "creating VisualOverlay object "+name);
		return new VisualOverlay(site, name, width, height, handCreated);
	}
}
