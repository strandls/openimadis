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

/*
 * AuthCode.java
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
package com.strandgenomics.imaging.iengine.models;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.auth.IAccessToken;
import com.strandgenomics.imaging.iengine.auth.IPFilter;

/**
 * Authorization code controls the access to the system. It allows users to
 * provide a proxy access of themselves or provide fine-grained access to
 * specific resources to a third party software. It is uniquely identified by an
 * id.
 * 
 * @author santhosh
 */
public class AuthCode implements Storable, IAccessToken {

    /**
     * 
     */
    private static final long serialVersionUID = -3901671954852009534L;
    /**
     * unique id
     */
    private final String id;
    /**
     * user associated with token
     */
    private final String user;
    /**
     * client associated with token
     */
    private final String clientID;
    /**
     * Services which are allowed to access using this code.
     */
    private final int services;
    /**
     * time when the token expires
     */
    private final Date expiryDate;
    /**
     * is the token still valid
     */
    private final boolean valid;
    /**
     * list of {@link IPFilter} controlling access of token
     */
    private final List<IPFilter> filters;
    /**
     * differentiates between a auth code and access token. if delivered is
     * false, the instance is auth code, else it is access token
     */
    private final boolean delivered;
    /**
     * Time when auth code was created
     */
    private final Date creationDate;
    /**
     * Time when auth code was last accessed.
     */
    private Date lastAccess;

    /**
     * Internal auto-increment id to reference the authcode
     */
    private final long authID;

    /**
     * Create new {@link AuthCode} instance.
     * 
     * @param id
     *            unique id
     * @param authID
     *            auto increment id to reference the authcode
     * @param user
     *            user associated with auth code
     * @param clientID
     *            client associated with auth code
     * @param services
     *            services allowed access
     * @param creationDate
     *            time when the auth code was created
     * @param expiryDate
     *            time when auth code expires
     * @param lastAccess
     *            time when the auth code was last accessed
     * @param valid
     *            is the token still valid
     * @param filters
     *            list of {@link IPFilter} controlling access of token
     * @param delivered
     *            differentiates between a auth code and access token. if
     *            delivered is false, the instance is auth code, else it is
     *            access token
     */
    public AuthCode(String id, long authID, String user, String clientID, int services, Date creationDate,
            Date expiryDate, Date lastAccess, boolean valid, List<IPFilter> filters, boolean delivered) {
        this.id = id;
        this.authID = authID;
        this.user = user;
        this.clientID = clientID;
        this.services = services;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.lastAccess = lastAccess;
        this.valid = valid;
        this.filters = filters;
        this.delivered = delivered;
    }

    @Override
    public void dispose() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean hasExpired() {
        Date now = new Date();
        if (now.before(expiryDate))
            return false;
        else
            return true;
    }

    @Override
    public boolean isAllowed(InetAddress address) {
        // If no filters are present, allow access for all.
        if (filters == null || filters.size() == 0) {
            return true;
        }
        for (IPFilter filter : filters)
            if (filter.isAllowed(address))
                return true;
        return false;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getClientID() {
        return clientID;
    }

    /**
     * Differentiates between a auth code and access token. if delivered is
     * false, the instance is auth code, else it is access token
     * 
     * @return is the token delivered or not.
     */
    public boolean isDelivered() {
        return delivered;
    }

    @Override
    public boolean canAccess(Service service) {
        int ordinal = service.ordinal();
        int value = (this.services >> ordinal) & 1;
        lastAccess = new Date();
        return (value == 1);
    }

    @Override
    public Date getExpiryTime() {
        return expiryDate;
    }

    @Override
    public List<IPFilter> getFilters() {
        return filters;
    }

    @Override
    public Date getLastAccessTime() {
        return lastAccess;
    }

    @Override
    public Date getCreationTime() {
        return creationDate;
    }

    @Override
    public long getInternalID() {
        return authID;
    }

    @Override
    public Service[] getAllowedServices() {
        List<Service> services = new ArrayList<Service>();
        for (Service service : Service.values()) {
            int ordinal = service.ordinal();
            int value = (this.services >> ordinal) & 1;
            if (value == 1)
                services.add(service);
        }
        return services.toArray(new Service[0]);
    }
}
