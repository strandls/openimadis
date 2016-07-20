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
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;

/**
 * access layer for acquisition profile
 * 
 * @author Anup Kulkarni
 */
public interface AcquisitionProfileDAO {
	
	/**
	 * create new acquisition profile
	 * @param profile new acquisition profile
	 * @throws DataAccessException
	 */
	public void createAcquisitionProfile(AcquisitionProfile profile) throws DataAccessException;
	
	/**
	 * list all the available acquisition profiles
	 * @return list of all the available acquisition profiles
	 * @throws DataAccessException
	 */
	public List<AcquisitionProfile> listAcquisitionProfiles() throws DataAccessException;
	
	/**
	 * update acquisition profile
	 * @param microscope
	 * @param profileName
	 * @param updatedProfile
	 */
	public void updateAcquisitionProfile(String microscope, String profileName, AcquisitionProfile updatedProfile) throws DataAccessException;
	
	/**
	 * delete acquisition profile
	 * @param microscope
	 * @param profileName
	 * @throws DataAccessException
	 */
	public void deleteAcquisitionProfile(String microscope, String profileName) throws DataAccessException;
}
