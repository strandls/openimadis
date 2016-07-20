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
