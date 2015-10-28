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
