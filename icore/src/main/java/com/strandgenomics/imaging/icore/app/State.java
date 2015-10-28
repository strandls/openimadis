/*
 * State.java
 *
 * AVADIS Image Management System
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
package com.strandgenomics.imaging.icore.app;

/**
 * State of an application submitted to the server for execution
 * @author arunabha
 *
 */
public enum State {
	
	/**
	 * scheduled for execution
	 */
	SCHEDULED,
	/**
	 * available for execution
	 */
    WAITING,
    /**
     * is not available for allocation
     */
    PAUSED,
    /**
     * picked up by a compute-server
     */
    ALLOCATED,
    /**
     * is being executed by a compute server
     */
    EXECUTING,
    /**
     * has been deleted
     */
    DELETED,
    /**
     * failed to complete
     */
    ERROR,
    /**
     * successfully completed
     */
    SUCCESS,
    /**
     * is on its way to be terminated
     */
    TERMINATING,
    /**
     * force termination
     */
    TERMINATED
}
