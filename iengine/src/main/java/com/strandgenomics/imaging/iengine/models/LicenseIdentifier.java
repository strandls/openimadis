package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Storable;

/**
 * class to represent acquisition client license
 * 
 * @author Anup Kulkarni
 */
public class LicenseIdentifier implements Storable{
	
	/**
	 * unique identifier for the license
	 */
	public final long id;
	/**
	 * access token used for launching the acquisition client
	 */
	public final String accessToken;
	/**
	 * the time when the license was issued
	 */
	public final long timeOfIssue;
	/**
	 * ipAddress of the machine
	 */
	public final String ipAddress;
	/**
	 * mac address of the machine
	 */
	public final String macAddress;
	
	public LicenseIdentifier(long id, String token, long issueTime, String ip, String mac)
	{
		this.id = id;
		this.accessToken = token;
		this.timeOfIssue = issueTime;
		this.ipAddress = ip;
		this.macAddress = mac;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
}
