package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Unit;
import com.strandgenomics.imaging.iengine.models.UnitType;

/**
 * encapsulaged DAO methods required by Unit
 * 
 * @author Anup Kulkarni
 */
public interface UnitDAO {
	/**
	 * adds unit to db
	 * @param unit specified unit
	 * @throws DataAccessException 
	 */
	public void insertUnit(Unit unit) throws DataAccessException;
	/**
	 * returns list of all available units
	 * @return
	 * @throws DataAccessException 
	 */
	public List<Unit> listAllUnits() throws DataAccessException;
	/**
	 * return specified unit
	 * @param unitName name of specified unit
	 * @return
	 * @throws DataAccessException 
	 */
	public Unit getUnit(String unitName) throws DataAccessException;
	/**
	 * update details of the specified unit
	 * @param unitName
	 * @param newStorageSpace
	 * @param newType
	 * @param newContact
	 * @throws DataAccessException 
	 */
	public void updateUnitDetails(String unitName, double newStorageSpace, UnitType newType, String newContact) throws DataAccessException;
	/**
	 * removed specified unit from db
	 * @param unitName
	 * @throws DataAccessException 
	 */
	public void removeUnit(String unitName) throws DataAccessException;
}
