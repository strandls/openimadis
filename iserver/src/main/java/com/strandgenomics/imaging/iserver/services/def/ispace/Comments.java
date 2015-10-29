/*
 * Comments.java
 *
 * AVADIS Image Management System
 * Web Service Definitions
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
package com.strandgenomics.imaging.iserver.services.def.ispace;

public class Comments {
	
	/**
	 * ID of the user
	 */
	private String userLogin;
	/** 
	 * the comment
	 */
	private String notes;
	/** 
	 * the date and time when the comment was posted
	 */
	private long creationDate;
	
	
	public Comments()
	{}

	/**
	 * @param userLogin the userLogin to set
	 */
	public void setUserLogin(String userLogin) 
	{
		this.userLogin = userLogin;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) 
	{
		this.notes = notes;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(long creationDate) 
	{
		this.creationDate = creationDate;
	}

	/**
	 * @return the userLogin
	 */
	public String getUserLogin() 
	{
		return userLogin;
	}

	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @return the creationDate
	 */
	public long getCreationDate() 
	{
		return creationDate;
	}
}
