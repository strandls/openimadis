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
