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
