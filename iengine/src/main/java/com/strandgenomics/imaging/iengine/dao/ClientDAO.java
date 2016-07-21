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
import com.strandgenomics.imaging.iengine.models.Client;

/**
 * Data access methods for {@link Client}
 * 
 * @author santhosh
 */
public interface ClientDAO {

    /**
     * Get a client with the given client ID
     * 
     * @param clientID
     *            id of the client required
     * @return client instance if a valid client is present. <code>null</code>
     *         otherwise
     * @throws DataAccessException
     */
    public Client getClient(String clientID) throws DataAccessException;

    /**
     * Get client given the name and version
     * 
     * @param name
     *            name of the client required
     * @param version
     *            version
     * @return client instance if a valid client is present. <code>null</code>
     *         otherwise
     * @throws DataAccessException
     */
    public Client getClient(String name, String version) throws DataAccessException;

    /**
     * Get all clients present in the system
     * 
     * @return list of valid clients
     * @throws DataAccessException
     */
    public List<Client> getAllClients() throws DataAccessException;

    /**
     * Add a new client
     * 
     * @param clientID
     *            id of the new client
     * 
     * @param name
     *            name of the client
     * @param version
     *            version of the client
     * @param description
     *            description
     * @param user
     *            user who added this client
     * @param isWorkflow
     *            can the server run this client. The client is treated as a
     *            workflow if this is true.
     * @param url target url in case of web clients
     * @return instance of client just added
     * @throws DataAccessException
     */
    public Client addClient(String clientID, String name, String version, String description, String user,
            boolean isWorkflow, String url) throws DataAccessException;

    /**
     * Remove the client with the given id
     * 
     * @param clientID
     *            id of the client
     * @throws DataAccessException
     */
    public void removeClient(String clientID) throws DataAccessException;

    /**
     * updates the version of the existing client
     * 
     * @param acqClientId
     * @param clientVersion
     * @throws DataAccessException 
     */
	public void updateClientVersion(String acqClientId, String clientVersion) throws DataAccessException;
}
