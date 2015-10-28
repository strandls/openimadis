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
