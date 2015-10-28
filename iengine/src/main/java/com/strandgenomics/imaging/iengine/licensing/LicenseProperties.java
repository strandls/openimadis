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
