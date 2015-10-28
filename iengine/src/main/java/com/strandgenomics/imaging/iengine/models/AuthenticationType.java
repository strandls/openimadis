/*
 * AuthenticationType.java
 *
 * AVADIS Image Management System
 * Engine Implementation
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
package com.strandgenomics.imaging.iengine.models;

/**
 * Authentication can happen locally or externally (say through LDAP)
 * @author arunabha
 *
 */
public enum AuthenticationType
{
	/**
	 * for external authentication type, the authentication is delegated to outside the system
	 */
	External, 
	/**
	 * for internal authentication type, the authentication is done locally
	 */
	Internal
};
