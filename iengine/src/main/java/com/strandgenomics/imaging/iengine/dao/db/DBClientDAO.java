/*
 * DBClientDAO.java
 *
 * Product:  AvadisIMG Server
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
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
import com.strandgenomics.imaging.iengine.dao.ClientDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Client;

/**
 * DB implementation of {@link ClientDAO}
 * 
 * @author santhosh
 */
public class DBClientDAO extends ImageSpaceDAO<Client> implements ClientDAO {

    DBClientDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) {
        super(factory, connectionProvider, "ClientDAO.xml");
    }

    @Override
    public Client getClient(String clientID) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientDAO", "getClient", "Get client with id : " + clientID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_CLIENT_BY_ID");
        sqlQuery.setValue("ClientID", clientID, Types.VARCHAR);
        return fetchInstance(sqlQuery);
    }

    @Override
    public List<Client> getAllClients() throws DataAccessException {
        logger.logp(Level.INFO, "DBClientDAO", "getAllClients", "Get all clients");
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_CLIENTS");
        RowSet<Client> result = find(sqlQuery);
        return result == null ? null : result.getRows();
    }

    @Override
    public Client addClient(String clientID, String name, String version, String description, String user,
            boolean isWorkflow, String url) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientDAO", "addClient", "Add client: " + name + " " + version + " " + description
                + " " + user);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_CLIENT");
        sqlQuery.setValue("ClientID", clientID, Types.VARCHAR);
        sqlQuery.setValue("Name", name, Types.VARCHAR);
        sqlQuery.setValue("Version", version, Types.VARCHAR);
        sqlQuery.setValue("Description", description, Types.VARCHAR);
        sqlQuery.setValue("User", user, Types.VARCHAR);
        sqlQuery.setValue("isWorkflow", isWorkflow, Types.BOOLEAN);
        sqlQuery.setValue("Url", url, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
        return getClient(clientID);
    }

    @Override
    public void removeClient(String clientID) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientDAO", "removeClient", "Remove client: " + clientID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_CLIENT");
        sqlQuery.setValue("ClientID", clientID, Types.VARCHAR);
        updateDatabase(sqlQuery);
    }

    @Override
    public Client createObject(Object[] columnValues) {
        String clientID = (String) columnValues[0];
        String name = (String) columnValues[1];
        String version = (String) columnValues[2];
        String description = (String) columnValues[3];
        String user = (String) columnValues[4];
        boolean isWorkflow = Util.getBoolean(columnValues[5]);
        String url = (String) columnValues[6];
        
        return new Client(clientID, name, version, description, user, isWorkflow, url);
    }

    @Override
    public Client getClient(String name, String version) throws DataAccessException {
        logger.logp(Level.INFO, "DBClientDAO", "getClient", "Get client with name : " + name + " versioN: " + version);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_CLIENT_BY_NAME");
        sqlQuery.setValue("Name", name, Types.VARCHAR);
        sqlQuery.setValue("Version", version, Types.VARCHAR);
        return fetchInstance(sqlQuery);
    }

	@Override
	public void updateClientVersion(String acqClientId, String clientVersion) throws DataAccessException
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_CLIENT_VERSION");
        sqlQuery.setValue("ClientID", acqClientId, Types.VARCHAR);
        sqlQuery.setValue("Version", clientVersion, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}

}
