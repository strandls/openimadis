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
import com.strandgenomics.imaging.iengine.dao.MicroscopeDAO;
import com.strandgenomics.imaging.iengine.models.MicroscopeObject;

public class DBMicroscopeDAO extends ImageSpaceDAO<MicroscopeObject> implements MicroscopeDAO {

	DBMicroscopeDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "MicroscopeDAO.xml");
	}

	@Override
	public void registerMicroscope(MicroscopeObject microscope) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMicroscopeDAO", "registerMicroscope", "registering microscope = "+microscope);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_MICROSCOPE");
		
		sqlQuery.setValue("Name",  microscope.microscope_name,        Types.VARCHAR);
		sqlQuery.setValue("IpAddress", microscope.ip_address,       Types.VARCHAR);
		sqlQuery.setValue("MacAddress",   microscope.mac_address,      Types.VARCHAR);
		sqlQuery.setValue("Licenses",   microscope.getAcquisitionLicenses(),      Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteMicroscope(String name) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMicroscopeDAO", "deleteMicroscope", "deleting microscope = "+name);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_MICROSCOPE");
		sqlQuery.setValue("Name",  name,        Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public MicroscopeObject getMicroscope(String name) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMicroscopeDAO", "getMicroscope", "deleting microscope = "+name);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_MICROSCOPE");
		sqlQuery.setValue("Name",  name,        Types.VARCHAR);

		return fetchInstance(sqlQuery);
	}

	@Override
	public void updateMicroscope(String name, MicroscopeObject newObject) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMicroscopeDAO", "updateMicroscope", "deleting microscope = "+name);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_MICROSCOPE");
		sqlQuery.setValue("Name",  name,        Types.VARCHAR);
		
		sqlQuery.setValue("NewName",  newObject.microscope_name,        Types.VARCHAR);
		sqlQuery.setValue("NewIpAddress",  newObject.ip_address,        Types.VARCHAR);
		sqlQuery.setValue("NewMacAddress",  newObject.mac_address,        Types.VARCHAR);
		sqlQuery.setValue("NewLicenses",  newObject.getAcquisitionLicenses(),        Types.INTEGER);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public List<MicroscopeObject> list() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBMicroscopeDAO", "list", "listing all the microscopes");
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_MICROSCOPES");
		
		
		RowSet<MicroscopeObject> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public MicroscopeObject createObject(Object[] columnValues)
	{
		String name = Util.getString(columnValues[0]);
		String mac_address = Util.getString(columnValues[2]);
		String ip_address = Util.getString(columnValues[1]);
		int licenses = Util.getInteger(columnValues[3]);

		MicroscopeObject microscope = new MicroscopeObject(name, mac_address, ip_address, licenses);
		return microscope;
	}
}
