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

package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Permission;

/**
 * encapsulation of users membership inside project
 * 
 * @author Anup Kulkarni
 */
public class UserMembership {
	
	/**
	 * db generated project id
	 */
	public int project_id;
	
	/**
	 * user login
	 */
	public String user_login;
	
	/**
	 * access permission for the user for this project
	 */
	public Permission permission;
	
	public UserMembership(int project_id, String user_login, Permission permission)
	{
		this.project_id = project_id;
		this.user_login = user_login;
		this.permission = permission;
	}

	public int getProjectId()
	{
		return project_id;
	}

	public String getUserLogin()
	{
		return user_login;
	}

	public Permission getPermission()
	{
		return permission;
	}
}
