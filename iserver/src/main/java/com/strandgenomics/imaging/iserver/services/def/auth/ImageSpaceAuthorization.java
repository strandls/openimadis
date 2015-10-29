/*
 * ImageSpaceAuthorization.java
 *
 * AVADIS Image Management System
 * Web-service definition
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

package com.strandgenomics.imaging.iserver.services.def.auth;

import java.rmi.RemoteException;


/**
 * Service for getting the access tokens etc
 * @author arunabha
 *
 */
public interface ImageSpaceAuthorization {
	
    /**
     * fetches access token for the specified client/Application (clientID) using the specified auth code
     * @param appID Client/Application Identifier - obtained after registering the client/Application with iManage (web client) 
     * @param authCode the authorization grant obtained by the user for the above mentioned clientID (again using iManage)
     * @throws RemoteException
     */
    public String getAccessToken(String appID, String authCode) 
    		throws RemoteException;
    
    /**
     * Returns the user who has granted the authorization for the specified access token
     * @param accessToken the access token user consideration
     * @return the login id of the user who has created this grant
     * @throws RemoteException
     */
    public String getUser(String accessToken)
    		throws RemoteException;
    
	/**
	 * Returns the expiry time of the specified access token
	 * @param accessToken the access token under consideration
	 * @return the time in milliseconds for the access token to expire
	 * @throws RemoteException
	 */
	public long getExpiryTime(String accessToken) 
			throws RemoteException;
	
	/**
	 * Surrenders the specified access token to prevents any more of its usage
	 * @param clientID the application/client Identifier 
	 * @param accessToken the access token to surrender
	 * @throws RemoteException 
	 */
	public void surrenderAccessToken(String clientID, String accessToken)
			throws RemoteException;
}
