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
import com.strandgenomics.imaging.iengine.models.MicroscopeObject;

/**
 * access layer for microscope DAO 
 *
 * @author Anup Kulkarni
 */
public interface MicroscopeDAO {
	/**
	 * register new microscope
	 * @param microscope new microscope
	 * @throws DataAccessException 
	 */
	public void registerMicroscope(MicroscopeObject microscope) throws DataAccessException;;
	/**
	 * delete specified microscope
	 * @param name specified microscope
	 * @throws DataAccessException 
	 */
	public void deleteMicroscope(String name) throws DataAccessException;;
	/**
	 * returns specified microscope object
	 * @param name specified microscope
	 * @return specified microscope object
	 * @throws DataAccessException 
	 */
	public MicroscopeObject getMicroscope(String name) throws DataAccessException;
	/**
	 * update the specified microscope
	 * @param name spcified microscope
	 * @param newObject new microscope
	 */
	public void updateMicroscope(String name, MicroscopeObject newObject) throws DataAccessException;;
	/**
	 * enlists all the registered microscopes
	 * @return list of all the registered microscopes
	 */
	public List<MicroscopeObject> list() throws DataAccessException;
}
