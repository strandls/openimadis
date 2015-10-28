package com.strandgenomics.imaging.iengine.dao;

import java.util.List;
import java.util.Set;

import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.db.DataAccessException;

/**
 * give access to user annotations
 * @author Anup Kulkarni
 *
 * @param <T> can be integer, double, string
 */
public interface MetaDataDAO {

	/**
	 * @param signature of record
	 * @return user annotations
	 */
	public List<MetaData> find(int projectID, long guid)
			throws DataAccessException;
	
	/**
	 * Returns the meta with the specified name within the given project of the specified record
	 * @param signature of record
	 * @return user annotations
	 */
	public MetaData find(int projectID, long guid, String name)
			throws DataAccessException;
	
	/**
	 * deletes the user annotation with the specified name within the given record and project
	 * @param projectID of corresponding project
	 * @param guid of corresponding record
	 * @param name of annotation to be deleted
	 * @throws DataAccessException
	 */
	public void delete(int projectID, long guid, String name) 
		throws DataAccessException;
	
	/**
	 * updates the user annotation of specified name to specified value given the record and project id
	 * @param projectID of corresponding project
	 * @param guid of corresponding record
	 * @param name of annotation to be updated
	 * @param value new value of the annotation
	 * @return true if successful, false otherwise
	 * @throws DataAccessException
	 */
	public boolean update(int projectID, long guid, String actorLogin, String name, Object value)
		throws DataAccessException;
	
	/**
	 * Registers the specified name with the annotation registry
	 * @param name relevant project 
	 * @param type the record
	 * @param projectID
	 * @throws DataAccessException
	 */
	public void registerAnnotationName(String name, String type, int projectID) 
			throws DataAccessException;


	/**
	 * Inserts the specified named meta data to the specified record
	 * @param projectID relevant project 
	 * @param guid the record
	 * @param userId the user doing the insertion
	 * @param name
	 * @param value
	 * @throws DataAccessException
	 */
	public void insertUserAnnotation(int projectID, long guid, String login, String name, Object value)
			throws DataAccessException;
	
	/**
	 * returns the list of annotations(name,type) associated with project
	 * @param projectID
	 * @return list of search columns
	 * @throws DataAccessException
	 */
	public List<SearchField> getAnnotationsFromRegistry(int projectID)
			throws DataAccessException;
	
	/**
	 * Returns the annotation type for the specified  annotation name
	 * @param projectID
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public AnnotationType getAnnotationType(int projectID, String name)
			throws DataAccessException;
	
	/**
	 * Returns unique annotation values for specified annotation key on specified project
	 * @param projectID specified project id
	 * @param name specified annotation key
	 * @return set of unique annotation values
	 * @throws DataAccessException
	 */
	public Set<MetaData> getUniqueAnnotationValues(int projectID, String name, AnnotationType type, int limit)
			throws DataAccessException;
	
	/**
	 * delete all the user annotation for given name
	 * @param projectID specified project id
	 * @param name specified user annotation
	 * @throws DataAccessException
	 */
	public void deleteUserAnnotation(int projectID, String name) throws DataAccessException;
	
	/**
	 * delete user annotation from registry
	 * @param projectID specified project id
	 * @param name specified user annotation
	 * @throws DataAccessException
	 */
	public void deleteUserAnnotationFromRegistry(int projectId, String name) throws DataAccessException;

	/**
	 * returns the list of guids on which the annotation exists
	 * @param projectID specified project id
	 * @param annotationName specified user annotation name
	 * @return
	 * @throws DataAccessException 
	 */
	public long[] getRecordsForUserAnnotation(int projectID, String annotationName) throws DataAccessException;
}
