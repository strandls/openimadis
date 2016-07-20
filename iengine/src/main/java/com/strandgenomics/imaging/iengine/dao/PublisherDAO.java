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
