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
