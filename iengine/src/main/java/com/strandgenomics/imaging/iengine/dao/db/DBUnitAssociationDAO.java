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
import com.strandgenomics.imaging.iengine.dao.UnitAssociationDAO;
import com.strandgenomics.imaging.iengine.models.UnitAssociation;

public class DBUnitAssociationDAO extends ImageSpaceDAO<UnitAssociation> implements UnitAssociationDAO {

	DBUnitAssociationDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "UnitDAO.xml");
	}

	@Override
	public void associateProject(String unitName, int projectId, double spaceContribution) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "associateProject", "associateProject="+unitName+" "+projectId+" "+spaceContribution);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ASSOCIATE_PROJECT");
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        sqlQuery.setValue("StorageSpace", spaceContribution, Types.DOUBLE);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void removeProject(String unitName, int projectId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "removeProject", "removeProject="+unitName+" "+projectId);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_PROJECT");
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void updateProjectSpace(String unitName, int projectId, double spaceContribution) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "updateProjectSpace", "updateProjectSpace="+unitName+" "+projectId+" "+spaceContribution);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PROJECT_SPACE");
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        sqlQuery.setValue("StorageSpace", spaceContribution, Types.DOUBLE);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public List<UnitAssociation> getAssociationsForUnit(String unitName) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "getAssociationsForUnit", "getAssociationsForUnit="+unitName);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ASSOCIATION_FOR_UNIT");
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        
        RowSet<UnitAssociation> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public List<UnitAssociation> getAssociationsForProject(int projectId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "getAssociationsForProject", "getAssociationsForProject="+projectId);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ASSOCIATION_FOR_PROJECT");
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        
        RowSet<UnitAssociation> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	public UnitAssociation getAssociation(String unitName, int projectId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "getAssociation", "getAssociation="+projectId+" "+unitName);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ASSOCIATION");
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        
        return fetchInstance(sqlQuery);
	}
	
	@Override
	public List<UnitAssociation> getAssociations(String unitName, Integer projectId) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBUnitAssociationDAO", "getAssociations", "getAssociations project="+projectId+" unit="+unitName);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ASSOCIATIONS");
        sqlQuery.setValue("ProjectID", projectId, Types.INTEGER);
        sqlQuery.setValue("UnitName", unitName, Types.VARCHAR);
        
        RowSet<UnitAssociation> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}

	@Override
	public UnitAssociation createObject(Object[] columnValues)
	{
		String unitName = Util.getString(columnValues[0]);
		
		int projectId = Util.getInteger(columnValues[1]);
		
		Double storageSpace = Util.getDouble(columnValues[2]);
		
		UnitAssociation u = new UnitAssociation(unitName, projectId, storageSpace);
		return u;
	}
}
