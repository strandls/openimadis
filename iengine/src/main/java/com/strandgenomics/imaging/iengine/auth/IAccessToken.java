/*
 * IAccessToken.java
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
package com.strandgenomics.imaging.iengine.auth;

import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.iengine.Service;

/**
 * An access token is used to perform authorization on behalf of a user. It can
 * act as a proxy for the user or provide a more fine-grained access to the
 * resources of the user. It is uniquely identified by a id
 * 
 * @author santhosh
 */
public interface IAccessToken extends IPFilter {

    /**
     * Get unique id associated with this token
     * 
     * @return unique id
     */
    public String getId();

    /**
     * Can the access token access the service given.
     * 
     * @param service
     *            service which needs to be checked.
     * @return <code>true</code> if access is allowed. <code>false</code>
     *         otherwise.
     */
    public boolean canAccess(Service service);

    /**
     * Is the token still valid
     * 
     * @return <code>true</code> if the token is still valid, <code>false</code>
     *         otherwise
     */
    public boolean isValid();

    /**
     * Has the token expired or not
     * 
     * @return <code>true</code> if the token has expired. <code>false</code>
     *         otherwise
     */
    public boolean hasExpired();

    /**
     * Get user associated with token
     * 
     * @return user associated with token
     */
    public String getUser();

    /**
     * Get id of client asssociated with token
     * 
     * @return id of client asssociated with token
     */
    public String getClientID();

    /**
     * Returns the time instance when the access token expires
     * 
     * @param accessToken
     *            the access token under consideration
     * @return milliseconds left for the specified access to expire
     */
    public Date getExpiryTime();

    /**
     * Get the list of filters associated with this token.
     * 
     * @return list of filters. can be <code>null</code>.
     */
    public List<IPFilter> getFilters();

    /**
     * Returns the time instance when the access token was last accessed.
     * 
     * @return the time instance when the access token was last accessed.
     */
    public Date getLastAccessTime();

    /**
     * Returns the time instance when the access token was created
     * 
     * @return the time instance when the access token was created
     */
    public Date getCreationTime();

    /**
     * Internal id to reference the access token.
     * 
     * @return internal id of the access token.
     */
    public long getInternalID();
    
    /**
     * Get list of services the token has access to.
     * @return list of services the token has access to.
     */
    public Service[] getAllowedServices();
}
