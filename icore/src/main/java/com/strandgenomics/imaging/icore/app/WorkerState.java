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
