/*
 * AuthCodeDAO.java
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

import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.auth.IAccessToken;
import com.strandgenomics.imaging.iengine.auth.IPFilter;
import com.strandgenomics.imaging.iengine.models.AuthCode;

/**
 * Data access methods for {@link AuthCode}
 * 
 * @author santhosh
 */
public interface AuthCodeDAO {

    /**
     * Get auth code with the given id
     * 
     * @param id
     *            id of the auth code required
     * @return {@link AuthCode} instance if id is valid. <code>null</code>
     *         otherwise.
     * @throws DataAccessException
     */
    public AuthCode getAuthCode(String id) throws DataAccessException;

    /**
     * Get auth code from the internal id
     * 
     * @param authID
     *            internal id of the auth code
     * @return auth code instance if id is valid. <code>null</code> otherwise
     * @throws DataAccessException
     */
    public AuthCode getAuthCode(long authID) throws DataAccessException;

    /**
     * Add a new authcode
     * 
     * @param id
     *            id of the authcode
     * @param user
     *            user associated with the code
     * @param clientID
     *            client associated with the code
     * @param services
     *            services which are allowed for this auth code.
     * @param expiry
     *            when the code expires
     * @param filters
     *            filters for the code
     * @return instance of authcode added
     * @throws DataAccessException
     */
    public AuthCode addAuthCode(String id, String user, String clientID, int services, Date expiry,
            List<IPFilter> filters) throws DataAccessException;

    /**
     * Update the auth code with given id to a new id. Has the side-effect of
     * flipping the delivered status to true.
     * 
     * @param oldID
     *            old id of the auth code
     * @param newID
     *            new id required
     * @return instance with new id. <code>null</code> if no auth code with
     *         oldID is present.
     * @throws DataAccessException
     */
    public AuthCode exchangeAuthCode(String oldID, String newID) throws DataAccessException;

    /**
     * List all active auth codes and access tokens present in the system.
     * 
     * @return list of all active auth codes
     * @throws DataAccessException
     */
    public List<AuthCode> listTokens() throws DataAccessException;

    /**
     * List all active auth codes and access tokens present in the system for an
     * user
     * 
     * @param user
     *            user whose codes are required
     * 
     * @return list of all active auth codes
     * @throws DataAccessException
     */
    public List<AuthCode> listUserTokens(String user) throws DataAccessException;

    /**
     * Update details of an existing token (auth code/access token)
     * 
     * @param authID
     *            internal id of the token to update
     * @param services
     *            new list of services to access
     * @param newExpiry
     *            new expiry time
     * @param newFilters
     *            new set of filters
     * @return updated token instance
     * @throws DataAccessException
     */
    public IAccessToken updateToken(long authID, int services, Date newExpiry, List<IPFilter> newFilters)
            throws DataAccessException;

    /**
     * Remove a particular auth code with its internal ID.
     * 
     * @param authID
     *            internal id of the auth that should be disabled.
     * @throws DataAccessException
     */
    public void removeAuthCode(long authID) throws DataAccessException;

    /**
     * Remove a particular auth code with its id.
     * 
     * @param id
     *            id of the auth that should be disabled.
     * @throws DataAccessException
     */
    public void removeAuthCode(String id) throws DataAccessException;

    /**
     * Clean expired tokens.
     * 
     * @throws DataAccessException
     */
    public void clean() throws DataAccessException;

    /**
     * Update access time for the token with given id
     * 
     * @param id
     *            id of token to update access time
     * @param accessTime
     *            new access time.
     * @throws DataAccessException
     */
    public void updateAccessTime(String id, Date accessTime) throws DataAccessException;

    /**
     * 
     * @param id
     * @param isValid
     * @throws DataAccessException5:12:13 PM
     */
	public void disableAuthCode(String id ) throws DataAccessException;
}
