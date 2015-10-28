/*
 * VisualOverlaysDAO.java
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

import com.strandgenomics.imaging.icore.db.DataAccessException;

/**
 * gives access to revisions for record visual overlays
 */
public interface VisualOverlaysRevisionDAO {
	
	public Long getRevision(int projectID, long guid, int siteNo, String overlayName, 
			int sliceNo, int frameNo)
			throws DataAccessException;
	
	public void incrementRevision(int projectID, long guid, int siteNo, String overlayName, 
			int sliceNo, int frameNo)
			throws DataAccessException;
}
