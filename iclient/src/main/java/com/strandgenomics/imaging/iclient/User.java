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

/*
 * User.java
 *
 * AVADIS Image Management System
 * Client APIs
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

import com.strandgenomics.imaging.icore.Rank;

/**
 * represents a user within the ImageSpace system
 * @author arunabha
 *
 */
public class User {
	
	/**
	 * login name of the user
	 */
	protected String name;
	/**
	 * email address of the user
	 */
	protected String emailID;
	/** 
	 * assigned rank of the user within the institution
	 */
	protected Rank rank;
	
	/**
	 * Creates a new User Object instance
	 * @param name user login
	 * @param emailID email of the user
	 * @param rank rank of the user
	 */
	User(String name, String emailID, Rank rank)
	{
		this.name = name;
		this.emailID = emailID;
		this.rank = rank;
	}
	
	/**
	 * Returns the name of the user
	 * @return the name of the user
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the email of the user
	 * @return the email of the user
	 */
	public String getEmailID()
	{
		return emailID;
	}
	
	/**
	 * Returns the rank associated with this user
	 * @return the rank associated with this user
	 */
	public Rank getRank()
	{
		return rank;
	}
}
