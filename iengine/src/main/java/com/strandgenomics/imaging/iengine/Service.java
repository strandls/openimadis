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
 * Service.java
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
package com.strandgenomics.imaging.iengine;

import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.auth.AuthorizationException;
import com.strandgenomics.imaging.iengine.models.AuthCode;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * {@link Service} is a collection of related methods which are published by the
 * server. User decides which all services a particular {@link AuthCode} is
 * allowed to access.
 */
public enum Service {

    AUTHENTICATION("Authentication service"),
    
    ISPACE("ImageSpace Basic service"),
    
    SEARCH("ImageSpace Search service"), 
    
    LOADER("ImageSpace Loader service"),
    
    UPDATE("ImageSpace Update service"),

    MANAGEMENT("ImageSpace Management service"),

    COMPUTE("ImageSpace Compute service");
    
    /**
     * description of the service
     */
    private String description;
    
    private Service(String description)
    {
    	this.description = description;
    }
    
    @Override
    public String toString()
    {
    	return description;
    }
    
    /**
     * returns the client associated with the access token
     * @param accessToken specified access token
     * @return Client associated with the access token
     * @throws AuthorizationException
     */
    public Client getClient(String accessToken) throws AuthorizationException
    {
    	if(!SysManagerFactory.getAuthorizationManager().canAccess(accessToken, this))
			throw new AuthorizationException(new ErrorCode(ErrorCode.ImageSpace.INVALID_CREDENTIALS));
    	
    	String clientID = SysManagerFactory.getAuthorizationManager().getClientID(accessToken);
    	return SysManagerFactory.getAuthorizationManager().getClient(clientID);
    }
    
    /**
     * checks whether the specified access token can access this service and if successful
     * returns the user login associated with this access token
     * @param accessToken the access token under consideration
     * @return the user login associated with the access token
     * @throws AuthorizationException iff the access token is illegal
     */
    public String validateAccessToken(String accessToken) throws AuthorizationException
	{
		if(!SysManagerFactory.getAuthorizationManager().canAccess(accessToken, this))
			throw new AuthorizationException(new ErrorCode(ErrorCode.ImageSpace.INVALID_CREDENTIALS));
	
		return SysManagerFactory.getAuthorizationManager().getUser(accessToken);
	}
}
