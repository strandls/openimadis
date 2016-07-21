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
