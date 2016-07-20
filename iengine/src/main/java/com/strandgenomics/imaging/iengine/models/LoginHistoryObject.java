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

import java.util.Date;

import com.strandgenomics.imaging.icore.Storable;

/**
 * Object capturing login history item 
 * 
 * @author Anup Kulkarni
 */
public class LoginHistoryObject implements Storable{
	public final String user;
	
	public final String appName;
	
	public final String appVersion;
	
	public final Date loginTime;
	
	public LoginHistoryObject(String user, String appName, String appVersion, Date loginTime)
	{
		this.user = user;
		this.appName = appName;
		this.appVersion = appVersion;
		this.loginTime = loginTime;
	}
	
	public LoginHistoryObject(String user, String appName, String appVersion, long loginTime){
		this(user, appName, appVersion, new Date(loginTime));
	}
	
	public String getUser()
	{
		return user;
	}

	public String getAppName()
	{
		return appName;
	}

	public String getAppVersion()
	{
		return appVersion;
	}

	public Date getLoginTime()
	{
		return loginTime;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
}
