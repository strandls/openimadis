package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.AcquisitionProfileDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfileType;
import com.strandgenomics.imaging.iengine.models.LengthUnit;
import com.strandgenomics.imaging.iengine.models.TimeUnit;

public class DBAcquisitionProfileDAO extends ImageSpaceDAO<AcquisitionProfile> implements AcquisitionProfileDAO {

	DBAcquisitionProfileDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider)
	{
		super(factory, connectionProvider, "AcquisitionProfileDAO.xml");
	}

	@Override
	public void createAcquisitionProfile(AcquisitionProfile profile) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBAcquisitionProfileDAO", "createAcquisitionProfile", "creating profile = "+profile);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REGISTER_PROFILE");
		
		sqlQuery.setValue("ProfileName",  profile.getProfileName(),        Types.VARCHAR);
		
		sqlQuery.setValue("Name",  profile.getMicroscope(),        Types.VARCHAR);
		
		sqlQuery.setValue("XSize", profile.getxPixelSize(),       Types.DOUBLE);
		sqlQuery.setValue("YSize",   profile.getyPixelSize(),      Types.DOUBLE);
		sqlQuery.setValue("ZSize",   profile.getzPixelSize(),      Types.DOUBLE);
		
		sqlQuery.setValue("SourceType",   profile.getSourceType(),      Types.VARCHAR);
		
		sqlQuery.setValue("ElapsedTimeUnit",   profile.getElapsedTimeUnit().name(),      Types.VARCHAR);
		sqlQuery.setValue("ExposureTimeUnit",   profile.getExposureTimeUnit().name(),      Types.VARCHAR);
		
		sqlQuery.setValue("LengthUnit",   profile.getLengthUnit().name(),      Types.VARCHAR);
		
		sqlQuery.setValue("XType",   profile.getXType().name(),      Types.VARCHAR);
		sqlQuery.setValue("YType",   profile.getYType().name(),      Types.VARCHAR);
		sqlQuery.setValue("ZType",   profile.getZType().name(),      Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public List<AcquisitionProfile> listAcquisitionProfiles() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBAcquisitionProfileDAO", "listAcquisitionProfiles", "listing all the acq profiles");
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("LIST_PROFILES");
		
		
		RowSet<AcquisitionProfile> result = find(sqlQuery);
		return result == null ? null : result.getRows();
	}

	@Override
	public AcquisitionProfile createObject(Object[] columnValues)
	{
		String name = Util.getString(columnValues[0]);
		String microscopeName = Util.getString(columnValues[1]);
		
		Double xSize = Util.getDouble(columnValues[2]);
		Double ySize = Util.getDouble(columnValues[3]);
		Double zSize = Util.getDouble(columnValues[4]);
		
		SourceFormat sourceType = null;
		if(Util.getString(columnValues[5])!=null)
			sourceType = new SourceFormat(Util.getString(columnValues[5]));
		
		TimeUnit elapsedTimeUnit = TimeUnit.valueOf(Util.getString(columnValues[6]));
		TimeUnit exposureTimeUnit = TimeUnit.valueOf(Util.getString(columnValues[7]));
		
		LengthUnit lengthUnit = LengthUnit.valueOf(Util.getString(columnValues[8]));
		
		AcquisitionProfileType xType = AcquisitionProfileType.valueOf(Util.getString(columnValues[9]));
		AcquisitionProfileType yType = AcquisitionProfileType.valueOf(Util.getString(columnValues[10]));
		AcquisitionProfileType zType = AcquisitionProfileType.valueOf(Util.getString(columnValues[11]));
		
		return new AcquisitionProfile(name, microscopeName, xSize, xType, ySize, yType, zSize, zType, sourceType, elapsedTimeUnit, exposureTimeUnit, lengthUnit);
	}

	@Override
	public void updateAcquisitionProfile(String microscope, String profileName, AcquisitionProfile updatedProfile) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBAcquisitionProfileDAO", "updateAcquisitionProfile", "updating profile = "+updatedProfile);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PROFILE");
		
		sqlQuery.setValue("ProfileName",  updatedProfile.getProfileName(),        Types.VARCHAR);
		
		sqlQuery.setValue("Name",  updatedProfile.getMicroscope(),        Types.VARCHAR);
		
		sqlQuery.setValue("XSize", updatedProfile.getxPixelSize(),       Types.DOUBLE);
		sqlQuery.setValue("YSize",   updatedProfile.getyPixelSize(),      Types.DOUBLE);
		sqlQuery.setValue("ZSize",   updatedProfile.getzPixelSize(),      Types.DOUBLE);
		
		sqlQuery.setValue("SourceType",   updatedProfile.getSourceType(),      Types.VARCHAR);
		
		sqlQuery.setValue("ElpasedTimeUnit",   updatedProfile.getElapsedTimeUnit().name(),      Types.VARCHAR);	
		sqlQuery.setValue("ExposureTimeUnit",   updatedProfile.getExposureTimeUnit().name(),      Types.VARCHAR);
		
		sqlQuery.setValue("LengthUnit",   updatedProfile.getLengthUnit().name(),      Types.VARCHAR);
		
		sqlQuery.setValue("XType",   updatedProfile.getXType().name(),      Types.VARCHAR);
		sqlQuery.setValue("YType",   updatedProfile.getYType().name(),      Types.VARCHAR);
		sqlQuery.setValue("ZType",   updatedProfile.getZType().name(),      Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void deleteAcquisitionProfile(String microscope, String profileName) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBAcquisitionProfileDAO", "deleteAcquisitionProfile", "deleting profile = "+profileName+" for microscope="+microscope);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_PROFILE");
		
		sqlQuery.setValue("ProfileName",  profileName,        Types.VARCHAR);
		sqlQuery.setValue("Name",  microscope,        Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

}
