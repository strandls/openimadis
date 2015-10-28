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
