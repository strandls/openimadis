/*
 * UserAuthenticator.java
 *
 * AVADIS Image Management System
 * Server Implementation
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
package com.strandgenomics.imaging.iengine.auth;

import com.strandgenomics.imaging.icore.db.DataAccessException;

/**
 * interface defining the basic APIs needed to authenticate an enterprise user
 * @author mantri
 */
public interface UserAuthenticator {
    
    /**
     * method to authenticate user
     * @param login     login id of the user
     * @param password  password of the user
     * @return          true iff user is authenticated else false
     */
    public boolean authenticateUser(String login, String password) 
        throws DataAccessException;
    
    /**
     * changes user password
     * @param login       the login of the user concerned
     * @param oldPassword the existing password for the user (encrypted)
     * @param newPassword the new password for this user (encrypted)
     */
    public boolean changePassword(String login, String oldPassword, String newPassword)
        throws DataAccessException;
}
