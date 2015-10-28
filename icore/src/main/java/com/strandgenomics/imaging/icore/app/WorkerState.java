/**
 * WorkerState.java
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
 * The state of a compute-server system
 * @author arunabha
 *
 */
public enum WorkerState {

	/**
	 * the compute-server system is fully free and waiting to get a task; also
	 * compute-sever system has nothing to report anything about previsous
	 * requests
	 */
	FREE,
	/**
	 * the compute-server system is partially busy, serving one or more tasks,
	 * but can still pick up more, the compute-server system may be completely
	 * free in sense of not having any task scheduled to run, but still have
	 * some task reports to submit about previously allocated tasks (eg.
	 * SUCCESSFUL, TERMINATED, FALIED etc)
	 */
	ENGAGED,
	/**
	 * the compute-server system is fully busy, serving one or more tasks
	 */
	BUSY
}
