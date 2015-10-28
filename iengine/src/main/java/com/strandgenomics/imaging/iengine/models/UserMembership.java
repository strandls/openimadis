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
