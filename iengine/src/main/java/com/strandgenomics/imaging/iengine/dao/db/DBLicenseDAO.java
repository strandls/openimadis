package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.LicenseDAO;
import com.strandgenomics.imaging.iengine.models.LicenseIdentifier;

public class DBLicenseDAO extends ImageSpaceDAO<LicenseIdentifier> implements LicenseDAO {

	DBLicenseDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "LicenseDAO.xml");
	}

	@Override
	public List<LicenseIdentifier> listAllLicenses() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBLicenseDAO", "list", "listing all the active licenses");
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_LICENSES");
		
		
		RowSet<LicenseIdentifier> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public void insertLicense(LicenseIdentifier license) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBLicenseDAO", "insertLicense", "registering license = "+license);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_LICENSE");
		
		sqlQuery.setValue("Id",  license.id,        Types.BIGINT);
		sqlQuery.setValue("IpAddress", license.ipAddress,       Types.VARCHAR);
		sqlQuery.setValue("MacAddress",   license.macAddress,      Types.VARCHAR);
		sqlQuery.setValue("IssueTime",   new Timestamp(license.timeOfIssue),      Types.TIMESTAMP);
		sqlQuery.setValue("AccessToken",   license.accessToken,      Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteLicense(String accessToken) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBLicenseDAO", "deleteLicense", "deleting license for token = "+accessToken);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_LICENSE");
		sqlQuery.setValue("AccessToken",  accessToken,        Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public LicenseIdentifier createObject(Object[] columnValues)
	{
		Long id = Util.getLong(columnValues[0]);
		String ip_address = Util.getString(columnValues[1]);
		String mac_address = Util.getString(columnValues[2]);
		Timestamp issueTime = Util.getTimestamp(columnValues[3]);
		String token = Util.getString(columnValues[4]);

		LicenseIdentifier license = new LicenseIdentifier(id, token, issueTime.getTime(), ip_address, mac_address);
		return license;
	}

	@Override
	public LicenseIdentifier getLicense(String accessToken) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBLicenseDAO", "getLicense", "returning license for token = "+accessToken);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LICENSE");
		sqlQuery.setValue("AccessToken",  accessToken,        Types.VARCHAR);
		
		return fetchInstance(sqlQuery);
	}
}
