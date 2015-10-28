/*
 * AuthenticatorFactory.java
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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;

/**
 * Provides the relevant authentication handler
 * @author mantri,arunabha
 */
public class AuthenticatorFactory {


    // default authenticator
    public final static String DEFAULT_AUTHENTICATION_CLASS = "com.strandgenomics.imaging.iengine.auth.LocalUserAuthenticator";
    /**
     * the local authenticator
     */
    private static UserAuthenticator localAuthHandler = null;
    /**
     * the external authenticator
     */
    private static UserAuthenticator externalAuthHandler = null;
    /** just a lock */
    private static final Object padLoc = new Object();
    
    protected static Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.auth");

    /**
     * Returns the UserAuthenticator object for local authentication
     * @return the appropriate user authentication handler class object using the system property specified
     * in Constants.Property.AUTHENTICATION_LOCAL_HANDLER
     * @throws RuntimeException throws this exception when the authentication handler class cannot be found
     */
    public static UserAuthenticator getLocalAuthenticationHandler() {

        // create singleton instance
        if (localAuthHandler == null) {

            synchronized(padLoc) {

                if (localAuthHandler == null) 
                {
                	localAuthHandler = createAuthenticationHandler(Constants.getLocalAuthHandler());
                }
            }
        }
        
        return localAuthHandler;
    }
    
    /**
     * Returns the UserAuthenticator object for local authentication
     * @return the appropriate user authentication handler class object using the system property specified
     * in Constants.Property.AUTHENTICATION_LOCAL_HANDLER
     * @throws RuntimeException throws this exception when the authentication handler class cannot be found
     */
    public static UserAuthenticator getExternalAuthenticationHandler() {

        // create singleton instance
        if (externalAuthHandler == null) {

            synchronized(padLoc) {

                if (externalAuthHandler == null) 
                {
                	externalAuthHandler = createAuthenticationHandler(Constants.getExternalAuthHandler());
                }
            }
        }
        
        return externalAuthHandler;
    }
    
    private static UserAuthenticator createAuthenticationHandler(String className)
    {
        
    	UserAuthenticator authHandler = null;
        // get name of Authentication Handler implementing class which is to be used
//        String className = System.getProperty(property, DEFAULT_AUTHENTICATION_CLASS);

    	logger.logp(Level.INFO, "AuthenticatorFactory", "createAuthenticationHandler", "className="+className);
    	
        // check for null
        if (className == null || className.length() == 0) 
        {
            throw new RuntimeException("system property "+className +" not found");
        }

        try 
        {
            // load class dynamically
            Class<?> coreClass = Class.forName(className);
            //calls the default constructor
            authHandler = (UserAuthenticator) coreClass.newInstance();
        }
        catch(ClassNotFoundException ex)
        {
            throw new RuntimeException("cannot find authentication handler class "+className);
        }
        catch(Exception eex)
        {
            throw new RuntimeException("cannot instantiate authentication handler class "+className);
        }

        return authHandler;
    }
}
