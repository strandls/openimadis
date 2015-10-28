/*
 * UserComment.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.icore;

import java.sql.Timestamp;


/**
 * Comments on a record by users
 * @author arunabha
 *
 */
public class UserComment implements Storable {
	
	private static final long serialVersionUID = 7993193628396325246L;
	/**
	 * ID of the user
	 */
	protected String userLogin;
	/** 
	 * the comment
	 */
	protected String notes;
	/** 
	 * the date and time when the comment was posted
	 */
	protected Timestamp creationDate;
	/** 
	 * the id of the comment
	 */
	protected long commentid;
	
	/**
	 * Creates a comment
	 * @param userLogin
	 * @param notes
	 * @param creationDate
	 */
	public UserComment(String userLogin, String notes, Timestamp creationDate)
	{
		this.userLogin = userLogin;
		this.notes = notes;
		this.creationDate = creationDate;
	}
	
	/**
	 * Creates a comment
	 * @param userLogin
	 * @param notes
	 * @param creationDate
	 * @param commentid
	 */
	public UserComment(String userLogin, String notes, Timestamp creationDate,long commentid)
	{
		this.userLogin = userLogin;
		this.notes = notes;
		this.creationDate = creationDate;
		this.commentid=commentid;
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
	public Timestamp getCreationDate() 
	{
		return creationDate;
	}
	
	/**
	 * @return the commentid
	 */
	public long getCommentId() 
	{
		return commentid;
	}

	@Override
	public void dispose() {
		notes = null;
		userLogin = null;
		creationDate = null;
	}
}
