package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.RecordBuilder;

/**
 * data access methods for Record Builder
 * 
 * @author Anup Kulkarni
 */
public interface RecordCreationDAO {
	
	/**
	 * inserts record builder object in DB
	 * @param guid specified guid associated with record builder
	 * @param rb record builder object
	 * @throws DataAccessException 
	 */
	public void insertRecordBuilder(long guid, RecordBuilder rb) throws DataAccessException;

	/**
	 * returns requested record builder
	 * @param guid specified record builder id
	 * @return record builder object
	 * @throws DataAccessException 
	 */
	public RecordBuilder getRecordBuilder(long guid) throws DataAccessException;
	
	/**
	 * updates the list of dimensions for which pixel data is received
	 * @param guid specified record builder
	 * @param dim list of dimensions for which pixel data is received
	 * @throws DataAccessException 
	 */
	public void updateReceivedDimensions(long guid, List<Dimension> dim) throws DataAccessException;
	
	/**
	 * removes the specified record builder 
	 * @param guid specified record builder
	 * @throws DataAccessException 
	 */
	public void deleteRecordBuilder(long guid) throws DataAccessException;
	
	/**
	 * returns the list of dimensions for which pixel data is received
	 * @param guid specified record builder
	 * @return the list of dimensions for which pixel data is received
	 * @throws DataAccessException 
	 */
	public List<Dimension> getReceivedDimensions(long guid) throws DataAccessException;

	/**
	 * returns all the record builders
	 * @return all the record builders
	 */
	public List<RecordBuilder> getRecordBuilders() throws DataAccessException;
}
