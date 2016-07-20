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
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysRevisionDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;

/**
 * Gives access to visual overlays
 */
public class DBVisualOverlaysRevisionDAO extends StorageDAO<VisualOverlay> implements VisualOverlaysRevisionDAO {
	
	DBVisualOverlaysRevisionDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "VisualOverlaysRevisionDAO.xml");
	}
	
	@Override
	public Long getRevision(int projectID, long guid, int siteNo,
			String overlayName, int sliceNo, int frameNo)
			throws DataAccessException {
		logger.logp(Level.INFO, "DBVisualOverlaysRevisionDAO", "getRevision", "fetching visual overlay revision for " +
				"projectId="+projectID +" guid="+guid +" site="+siteNo + "ovName"+overlayName + "slice="+sliceNo + "frame="+frameNo);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_REVISION");
		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));

		sqlQuery.setValue("GUID", guid,    Types.BIGINT);
		sqlQuery.setValue("Site", siteNo,  Types.INTEGER);
		sqlQuery.setValue("Slice", sliceNo,    Types.INTEGER);
		sqlQuery.setValue("Frame", frameNo,  Types.INTEGER);
		sqlQuery.setValue("Name", overlayName,  Types.VARCHAR);

		return getLong(sqlQuery);
	}
	
	@Override
	public void incrementRevision(int projectID, long guid, int siteNo,
			String overlayName, int sliceNo, int frameNo)
			throws DataAccessException {
		
		logger.logp(Level.INFO, "DBVisualOverlaysRevisionDAO", "incrementRevision", "fetching visual overlay revision for " +
				"projectId="+projectID +" guid="+guid +" site="+siteNo + "ovName"+overlayName + "slice="+sliceNo + "frame="+frameNo);
		
		Long revision = getRevision(projectID, guid, siteNo, overlayName, sliceNo, frameNo);
		SQLQuery sqlQuery;
		if(revision == null){
			//insert
			sqlQuery = queryDictionary.createQueryGenerator("INSERT_VISUAL_OVERLAY_REVISION");
		}
		else{
			//update (increment revision)
			sqlQuery = queryDictionary.createQueryGenerator("INCREMENT_REVISION");
		}

		sqlQuery.setParameter("Project", Project.getTablePrefix(projectID));

		sqlQuery.setValue("GUID", guid,    Types.BIGINT);
		sqlQuery.setValue("Site", siteNo,  Types.INTEGER);
		sqlQuery.setValue("Slice", sliceNo,    Types.INTEGER);
		sqlQuery.setValue("Frame", frameNo,  Types.INTEGER);
		sqlQuery.setValue("Name", overlayName,  Types.VARCHAR);
		updateDatabase(sqlQuery);
	}

	//Dummy implementation: should never be called
	@Override
	public VisualOverlay createObject(Object[] columnValues) {
		// TODO Auto-generated method stub
		return null;
	}
}
