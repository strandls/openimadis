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
