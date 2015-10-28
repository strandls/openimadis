/*
 * ClientDAO.java
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
