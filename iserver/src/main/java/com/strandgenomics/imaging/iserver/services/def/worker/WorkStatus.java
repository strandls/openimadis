/*
 * WorkStatus.java
 *
 * AVADIS Image Management System
 * Web-service definition
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
package com.strandgenomics.imaging.iserver.services.def.worker;

/**
 * status of a work being done
 * @author arunabha
 *
 */
public class WorkStatus {
	
    /** 
     * state of task being executed, one of EXECUTING, SUCCESSFUL, FAILURE and TERMINATED
     */
    private int taskState;
    /** 
     * ID of the task which is being executed
     */
    private long taskID;
    
    public WorkStatus()
    {}

	/**
	 * @return the taskState
	 */
	public int getTaskState() {
		return taskState;
	}

	/**
	 * @param taskState the taskState to set
	 */
	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	/**
	 * @return the taskID
	 */
	public long getTaskID() {
		return taskID;
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(long taskID) {
		this.taskID = taskID;
	}
}
