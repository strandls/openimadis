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

package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.UnitDAO;
import com.strandgenomics.imaging.iengine.models.Unit;
import com.strandgenomics.imaging.iengine.models.UnitType;


public class DBUnitDAO extends ImageSpaceDAO<Unit> implements UnitDAO{

	DBUnitDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "UnitDAO.xml");
	}

	@Override
	public void insertUnit(Unit unit) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_UNIT");
        logger.logp(Level.INFO, "DBUnitDAO", "insertUnit", "unit="+unit);
        
        sqlQuery.setValue("UnitName", unit.unitName,       				Types.VARCHAR);
        sqlQuery.setValue("UnitType",       unit.getType().name(),      				Types.VARCHAR);
        sqlQuery.setValue("StorageSpace",   unit.getGlobalStorage(),  				Types.DOUBLE);
        sqlQuery.setValue("Contact",       unit.getPointOfContact(),      				Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public List<Unit> listAllUnits() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitDAO", "listAllUnits", "all units");
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_ALL");

        RowSet<Unit> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public Unit getUnit(String unitName) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitDAO", "getUnit", "getUnit for name="+unitName);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_UNIT_FOR_NAME");
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}

	@Override
	public void updateUnitDetails(String unitName, double newStorageSpace, UnitType newType, String newContact) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitDAO", "updateUnitDetails", "updateUnitDetails="+unitName+" "+newStorageSpace+" "+newType+" "+newContact);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_UNIT_DETAILS");
        sqlQuery.setValue("UnitType",   newType.name(),   Types.VARCHAR);
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        sqlQuery.setValue("StorageSpace", newStorageSpace, Types.DOUBLE);
        sqlQuery.setValue("Contact",   newContact,   Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public void removeUnit(String unitName) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitDAO", "removeUnit", "removeUnit="+unitName);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_UNIT");
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        updateDatabase(sqlQuery);
	}

	@Override
	public Unit createObject(Object[] columnValues)
	{
		String unitName = Util.getString(columnValues[0]);
		
		String unitType = Util.getString(columnValues[1]);
		UnitType type = UnitType.valueOf(unitType);
		
		String contact = Util.getString(columnValues[3]);
		
		Double storageSpace = Util.getDouble(columnValues[2]);
		
		Unit u = new Unit(unitName, storageSpace, type, contact);
		return u;
	}

}
