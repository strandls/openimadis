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

package com.strandgenomics.imaging.iclient;

import com.strandgenomics.imaging.icore.Status;

/**
 * Handle to long running Task like archiving projects, deleting projects, restoring archived projects etc
 * @author arunabha
 *
 */
public class Task extends ImageSpaceObject {
	
	/**
	 * id of the task
	 */
	private long id;
	/**
	 * name of the task
	 */
	private String name;
	
	/**
	 * 
	 * @param id id of the task
	 * @param name name of the task
	 */
	Task(long id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	/**
	 * returns the task status
	 * @return the task status
	 */
	public Status getState()
	{
    	return getImageSpace().getJobStatus(this);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	}
}
