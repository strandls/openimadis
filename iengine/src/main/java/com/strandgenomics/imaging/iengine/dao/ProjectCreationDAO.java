/*
 * ProjectCreationDAO.java
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
import com.strandgenomics.imaging.iengine.models.Project;

public interface ProjectCreationDAO {
	
    /**
     * Create a new project in the repository
     * @param name name of the project
     * @param notes names associated with the project
     * @param createdBy the team-leader (or above) creating it, the default project manager
     * @param quota disk space quota in gb
     * @return the created project
     * @throws DataAccessException
     */
    public Project createProject(String name, String notes, String createdBy, double quota, String location) 
    		throws DataAccessException;
    
}
