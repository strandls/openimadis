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
