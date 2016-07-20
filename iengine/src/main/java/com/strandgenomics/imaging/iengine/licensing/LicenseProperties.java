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

package com.strandgenomics.imaging.iengine.licensing;

import java.util.Properties;

/**
 * This will encapsulate all the properties related to the Licensing
 * 
 * @author Anup Kulkarni
 */
public class LicenseProperties {
	/**
	 * maximum number of active users allowed as per the license
	 */
	private int maxActiveUsers;
	/**
	 * maximum number of acquisition licenses allowed as per the license
	 */
	private int maxAcquisitionLicenses;
	/**
	 * maximum number of microscopes allowed as per the license
	 */
	private int maxMicroscopes;
	/**
	 * encrypted checksum of the license properties used for validation
	 */
	private String checksum;
	
	public LicenseProperties(int maxActiveUsers, int maxAcquisitionLicenses, int maxMicroscopes, String checksum)
	{
		this.maxActiveUsers = maxActiveUsers;
		this.maxAcquisitionLicenses = maxAcquisitionLicenses;
		this.maxMicroscopes = maxMicroscopes;
		this.checksum = checksum;
	}
	
	public LicenseProperties(Properties props)
	{
		this.maxAcquisitionLicenses = Integer.parseInt((String)props.get(LicenseConstants.MAX_ACQUISITION_LICENSES));
		this.maxActiveUsers = Integer.parseInt((String)props.get(LicenseConstants.MAX_USERS));
		this.maxMicroscopes = Integer.parseInt((String)props.get(LicenseConstants.MAX_MICROSCOPS));
		this.checksum = (String) props.get(LicenseConstants.CHECKSUM);
	}
	
	public String getChecksum()
	{
		return this.checksum;
	}
	
	/**
	 * enlist license related properties as java.util.Properties object 
	 * @return license related properties as java.util.Properties object
	 */
	public Properties getChecksumProperties()
	{
		Properties prop = new Properties();
		
		prop.put(LicenseConstants.MAX_USERS, maxActiveUsers);
		prop.put(LicenseConstants.MAX_ACQUISITION_LICENSES, maxAcquisitionLicenses);
		prop.put(LicenseConstants.MAX_MICROSCOPS, maxMicroscopes);
		
		return prop;
	}
	/**
	 * returns maximum number of active users allowed as per the license 
	 * @return maximum number of active users allowed as per the license
	 */
	public int getMaxActiveUsers()
	{
		return maxActiveUsers;
	}

	/**
	 * maximum number of acquisition licenses allowed as per the license
	 * @return maximum number of acquisition licenses allowed as per the license
	 */
	public int getMaxAcquisitionLicenses()
	{
		return maxAcquisitionLicenses;
	}

	/**
	 * maximum number of microscopes allowed as per the license
	 * @return maximum number of microscopes allowed as per the license
	 */
	public int getMaxMicroscopes()
	{
		return maxMicroscopes;
	}
}
