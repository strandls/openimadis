/*
 * UserStatus.java
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
 * status of a user within the system
 * @author arunabha
 *
 */
public enum UserStatus {
	
	/**
	 * user is fully active and allowed to use the infrastructure
	 */
	 Active,
	 /**
	  * the user is suspended for some reason to login to the system
	  */
	 Suspended,
	 /**
	  * The user was deleted from the system
	  */
	 Deleted

}
