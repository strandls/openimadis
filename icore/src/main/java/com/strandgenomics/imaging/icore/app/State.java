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
