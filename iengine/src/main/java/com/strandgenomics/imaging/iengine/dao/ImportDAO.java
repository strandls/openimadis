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

import java.util.List;

import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Import;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;

/**
 * imports
 * @author navneet
 *
 */
public interface ImportDAO {
	
	/**
	 * insert import
	 * @throws DataAccessException 
	 */
	public void insertImport(int projectId,long ticketID, long requestTime, Status jobStatus, RecordCreationRequest request) throws DataAccessException;
	
	/**
	 * get all imports with the given status
	 * @throws DataAccessException 
	 */
	public List<Import> getImportsForStatus(int projectId,Status jobStatus) throws DataAccessException;
	
	/**
	 * update status of a import
	 * @throws DataAccessException 
	 */
	public void updateImportStatus(int projectId, long ticketID, Status jobStatus, long lastModificationTime) throws DataAccessException;
}
