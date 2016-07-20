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
 * Engine Implementation
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
package com.strandgenomics.imaging.iengine.models;

import java.sql.Timestamp;

import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.Storable;

/**
 *  This class represents an User of Avadis IMG System
 * @author arunabha
 *
 */
public class User implements Storable {
	
	private static final long serialVersionUID = -4170666541019237407L;
	
	/** login ID of the user */
	public final String login;
	/** Christian name of the user */
	protected String name;
	/** email address of the user */
	protected String emailID;
	/** password of the user - needed for external user, internal user can be validated through LDAP etc */
	protected String password;
	/** checks whether the user is internal or external user */
	protected AuthenticationType authenticationType;
	/** assigned roles with decreasing responsibilities */
	protected Rank rank;
	/** status of the user, an user is either active, suspended or deleted */ 
	protected UserStatus status;
	/** last login time */
	protected Timestamp lastLogin;
	/** number of active logins */
	protected int loginCount;
	
	/**
	 * Create a user 
	 * @param userID db generated id for a user
	 * @param login login ID of the user
	 * @param name  Christian name of the user
	 * @param emailID email address of the user
	 * @param password password of the user - needed for external user
	 * @param authType  checks whether the user is internal or external user
	 * @param role assigned roles with the user
	 * @param status  status of the user, an user is either active, suspended or deleted
	 * @param lastLogin last login time
	 * @param loginCount number of active logins
	 */
	public User(String login, String name, String emailID, String password,
			AuthenticationType authType, Rank rank, UserStatus status, Timestamp lastLogin, int loginCount)
	{
		this.login = login;
		this.name = name;
		this.emailID = emailID;
		this.password = password;
		this.authenticationType = authType;
		this.rank = rank;
		this.status = status;
		this.lastLogin = lastLogin;
		this.loginCount = loginCount;
	}
	
	@Override
    public void dispose()
    {
        emailID = null;
        name = null;
        password  = null;
        lastLogin = null;
        authenticationType = null;
        rank = null;
        status = null;
    }

    public String getLogin() 
    {
        return login;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public String getEmail()
    {
    	return this.emailID;
    }

    public String getPassword() 
    {
        return password;
    }

    public Timestamp getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(Timestamp value)
    {
        lastLogin = value;
    }

    /** checks whether this user is logged in, no of different logins */
    public int getLoginCount()
    {
        return loginCount;
    }

    /** checks whether this user is logged in, no of different logins */
    public void setLoginCount(int value)
    {
        loginCount = value;
    }

    public AuthenticationType getAuthenticationType()
    {
        return authenticationType;
    }
    
    public UserStatus getStatus()
    {
    	return this.status;
    }

    public Rank getRank()
    {
        return rank;
    }
    
    @Override
    public String toString() 
    {
        return login;
    }

    @Override
    public int hashCode()
    {
        return login.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean status = false;
        if(obj instanceof User)
        {
            User that = (User)obj;
            if(this.login != null && that.login != null && this.login.equals(that.login))
            {
                status = true;
            }
        }
        return status;
    }
}
