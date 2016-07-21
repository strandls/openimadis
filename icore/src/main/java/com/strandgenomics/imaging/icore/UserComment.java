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
