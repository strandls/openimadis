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
 * The response to a ping request received by the compute server (worker-system) 
 * @author arunabha
 *
 */
public class Response {
	
	/**
	 * the iManage server command to the compute server (worker-system)
	 */
	public final Directive command; 
	/**
	 * the job to execute, if any, null otherwise
	 */
	public final Work job;
	
	/**
	 * Construct a iManage server response with only a directive
	 * @param command the directive to the compute-server
	 */
	public Response(Directive command)
	{
		this.command = command;
		this.job = null;
	}
	
	/**
	 * Construct a iManage server response with the job and the associated directive
	 * @param command the directive to the compute-server
	 * @param task the task to execute/terminate/etc by the compute server 
	 */
	public Response(Directive command, Work task)
	{
		this.command = command;
		this.job = task;
	}
}
