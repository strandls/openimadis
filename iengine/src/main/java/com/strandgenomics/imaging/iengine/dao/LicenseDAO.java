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
import com.strandgenomics.imaging.iengine.models.LicenseIdentifier;

/**
 * DAO for handling acquisition licenses
 * 
 * @author Anup Kulkarni
 */
public interface LicenseDAO {
	
	/**
	 * lists all the active acquisition licenses
	 * @return list of all active acquisition licenses
	 * @throws DataAccessException 
	 */
	public List<LicenseIdentifier> listAllLicenses() throws DataAccessException;

	/**
	 * insert specified acquisition licenses
	 * @param license specified acquisition license
	 * @throws DataAccessException 
	 */
	public void insertLicense(LicenseIdentifier license) throws DataAccessException;
	
	/**
	 * deletes license asssociated with specified accessToken
	 * @param accessToken access token used for requesting the license
	 * @throws DataAccessException 
	 */
	public void deleteLicense(String accessToken) throws DataAccessException;

	/**
	 * return license for specified license identifier
	 * @param accessToken specified access token
	 * @return license for specified license identifier
	 * @throws DataAccessException 
	 */
	public LicenseIdentifier getLicense(String accessToken) throws DataAccessException;
}
