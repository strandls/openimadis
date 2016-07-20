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
import com.strandgenomics.imaging.iengine.models.BackupDataObject;

/**
 * gives access to information about backed up projects
 * @author anup
 *
 */
public interface BackupDAO {

	/**
	 * inserts the backup details for specified project
	 * @param projectId specified project
	 * @param location location where project data is backed up
	 * @param sign signature of the backed-up data
	 * @throws DataAccessException 
	 */
	public void insertBackupData(int projectId, String location, String sign) throws DataAccessException;
	
	/**
	 * get the backup detailes for specified project
	 * @param projectId specified project
	 * @return
	 * @throws DataAccessException 
	 */
	public BackupDataObject getBackupData(int projectId) throws DataAccessException;

	/**
	 * delete the backup details for specified project
	 * @param projectId specified project
	 * @throws DataAccessException 
	 */
	public void deleteBackupData(int projectId) throws DataAccessException;
}
