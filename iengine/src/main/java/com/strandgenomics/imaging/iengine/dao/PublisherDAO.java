package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.compute.Publisher;

/**
 * Manages the authorized publishers of the compute engine
 * 
 * @author Anup Kulkarni
 */
public interface PublisherDAO 
{

	/**
	 * returns publisher for given publisher code
	 * @param publisherName specified publisher
	 * @return
	 * @throws DataAccessException 
	 */
	public Publisher getPublisher(String publisherName) throws DataAccessException;
	
	/**
	 * returns all the publishers
	 * @return all the publishers
	 * @throws DataAccessException 
	 */
	public List<Publisher> listPublishers() throws DataAccessException;
	
	/**
	 * inserts authorized publisher in the DB
	 * @param name of the publisher
	 * @param description of the publisher
	 * @param publisherCode of the publisher
	 * @param ipfilter used by the publisher
	 * @throws DataAccessException 
	 */
	public void insertPublisher(String name, String description, String publisherCode, String ipfilter) throws DataAccessException;
	
	/**
	 * removed the specified publisher from DB
	 * @param name of specified publisher
	 * @throws DataAccessException
	 */
	public void removePublisher(String name) throws DataAccessException;
}
