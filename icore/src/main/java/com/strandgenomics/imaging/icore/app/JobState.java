/**
 * JobState.java
 *
 * Project imaging
 * Core Compute Component
 *
 * Copyright 2009-2012 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
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
 * state of a job under execution by the compute server
 * @author arunabha
 *
 */
public enum JobState {
	
	/**
	 * still under execution
	 */
	EXECUTING,
	/**
	 * completed successfully
	 */
	SUCCESSFUL,
	/**
	 * completed with failure
	 */
	FAILURE,
	/**
	 * terminated forcefully
	 */
	TERMINATED
}
