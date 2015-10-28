package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.compute.Task;

/**
 * 
 * Manages the archived tasks of the compute engine
 * 
 * @author Anup Kulkarni
 */
public interface ArchivedTaskDAO extends TaskDAO
{
	public List<Task> getMonitoredTasks() throws DataAccessException;

	public void setTaskOutputs(long id, long[] outputs) throws DataAccessException;

	public long[] getTaskOutputs(long id) throws DataAccessException;
}
