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
