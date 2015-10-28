/**
 * Directive.java
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
 * The directive received by a compute-worker-system from the iManage server
 * in response to a hand-shake established between the worker and the server
 * @author arunabha
 *
 */
public enum Directive {

    /**
     * an unknown worker-request has been received
     */
    UNKNOWN_REQUEST,
    /**
     * the worker is free & no pending task for him
     */
    WAIT,
    /**
     * the worker is busy with some task, let him do that
     */
    CONTINUE,
    /**
     * unknown worker, he must register himself first
     */
    REGISTRATION_REQUIRED,
    /**
     * request to workers to publish its applications
     */
    PUBLISH_APPLICATIONS,
    /**
     * the worker is free, he gets a task to execute
     */
    EXECUTE_TASK,
    /**
     * request to kill the task, the worker is executing
     */
    TERMINATE_TASK,
    /**
     * terminate the worker
     */
    TERMINATE;
}
