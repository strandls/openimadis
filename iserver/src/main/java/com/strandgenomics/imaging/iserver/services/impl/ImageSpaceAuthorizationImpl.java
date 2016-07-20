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
 * ImageSpaceAuthorizationImpl.java
 *
 * AVADIS Image Management System
 * Web Service Implementation
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */

package com.strandgenomics.imaging.iserver.services.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.ws.auth.ImageSpaceAuthorization;

/**
 * Web-service implementation of the iAuth service
 * @author arunabha
 *
 */
public class ImageSpaceAuthorizationImpl implements ImageSpaceAuthorization, Serializable {
	
	private static final long serialVersionUID = -2286231045675202186L;
	private transient Logger logger = null;
	
	public ImageSpaceAuthorizationImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}

    /**
     * fetches access token for the specified client/Application (clientID) using the specified auth code
     * @param clientID Client/Application Identifier - obtained after registering the client/Application with iManage (web client) 
     * @param authCode the authorization grant obtained by the user for the above mentioned clientID (again using iManage)
     * @throws RemoteException
     */
	@Override
    public String getAccessToken(String clientID, String authCode) throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceAuthorizationImpl", "getAccessToken", "fetching access token for clientID"+clientID +" for authorization code "+authCode);
		try
		{
			String accessToken =  SysManagerFactory.getAuthorizationManager().getAccessToken(authCode, clientID);
			
			return accessToken;
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceAuthorizationImpl", "getAccessToken", "error fetching access token for clientID"+clientID +" for authorization code "+authCode, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns the user who has granted the authorisation for the specified access token
     * @param accessToken the access token under consideration
     * @return the login id of the user who has created this grant
     * @throws RemoteException
     */
	@Override
    public String getUser(String accessToken) throws RemoteException
    {
		logger.logp(Level.INFO, "ImageSpaceAuthorizationImpl", "getUser", "finding user for access token "+accessToken);
		try
		{
			if(!SysManagerFactory.getAuthorizationManager().canAccess(accessToken, Service.AUTHENTICATION))
				return null;
			
			String user = SysManagerFactory.getAuthorizationManager().getUser(accessToken);
			
			if(user != null)
			{
				String clientID = SysManagerFactory.getAuthorizationManager().getClientID(accessToken);
				Client client = SysManagerFactory.getAuthorizationManager().getClient(clientID);
				
				SysManagerFactory.getUserManager().addLoginHistory(new Application(client.getName(), client.getVersion()), user);
			}
			return user;
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceAuthorizationImpl", "getUser", "error finding user for access token "+accessToken, ex);
			throw new RemoteException(ex.getMessage());
		}
    }
	
	/**
	 * Returns the time in milliseconds for the access token to expire
	 * @param accessToken the access token under consideration
	 * @return the time in milliseconds for the access token to expire
	 * @throws RemoteException
	 */
	@Override
	public long getExpiryTime(String accessToken) throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceAuthorizationImpl", "getExpiryTime", "finding expiry time for access token "+accessToken);
		try
		{
			if(SysManagerFactory.getAuthorizationManager().canAccess(accessToken, Service.AUTHENTICATION))
				return SysManagerFactory.getAuthorizationManager().getExpiryTime(accessToken);
			else
				return 0;
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceAuthorizationImpl", "getExpiryTime", "error finding expiry time for access token "+accessToken, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * Surrenders the specified access token to prevents any more of its usage
	 * @param clientID the application/client Identifier 
	 * @param accessToken the access token to surrender
	 * @throws RemoteException 
	 */
	@Override
	public void surrenderAccessToken(String clientID, String accessToken)
			throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceAuthorizationImpl", "surrenderAccessToken", clientID+" surrending access token "+accessToken);
		try
		{
			SysManagerFactory.getAuthorizationManager().surrender(clientID, accessToken);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceAuthorizationImpl", "surrenderAccessToken", "error surrending access token "+accessToken, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
}
