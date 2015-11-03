/*
 * Task.java
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
