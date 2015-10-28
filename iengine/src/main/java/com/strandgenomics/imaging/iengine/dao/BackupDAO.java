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
