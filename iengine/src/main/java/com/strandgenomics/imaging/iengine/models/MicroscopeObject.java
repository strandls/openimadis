package com.strandgenomics.imaging.iengine.models;

import com.strandgenomics.imaging.icore.Storable;

/**
 * class to encapsulate the microsope information used for acquisition
 * 
 * @author Anup Kulkarni
 */
public class MicroscopeObject implements Storable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 120359896878574283L;
	/**
	 * mac address of the microscope
	 */
	public final String mac_address;
	/**
	 * ip address of the microscope
	 */
	public final String ip_address;
	/**
	 * name of the microscope(this uniquely identify microscope)
	 */
	public final String microscope_name;
	/**
	 * number of acquisition licenses reserved for this microscope(default=1)
	 */
	private int acquisitionLicenses = 1;
	
	public MicroscopeObject(String name, String mac_address, String ip_address)
	{
		this.ip_address = ip_address;
		this.mac_address = mac_address;
		this.microscope_name = name;
	}
	
	public MicroscopeObject(String name, String mac_address, String ip_address, int licenses)
	{
		this.ip_address = ip_address;
		this.mac_address = mac_address;
		this.microscope_name = name;
		this.setAcquisitionLicenses(licenses);
	}

	/**
	 * returns number of acquisition licenses reserved for this microscope
	 * @return number of acquisition licenses reserved for this microscope
	 */
	public int getAcquisitionLicenses()
	{
		return acquisitionLicenses;
	}

	/**
	 * sets number of acquisition licenses reserved for this microscope
	 * @param acquisitionLicenses
	 */
	public void setAcquisitionLicenses(int acquisitionLicenses)
	{
		this.acquisitionLicenses = acquisitionLicenses;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("name=");
		sb.append(this.microscope_name);
		sb.append(",");
		sb.append("ip=");
		sb.append(this.ip_address);
		sb.append(",");
		sb.append("mac=");
		sb.append(this.mac_address);
		sb.append(",");
		sb.append("licenses=");
		sb.append(this.acquisitionLicenses);
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof MicroscopeObject)
		{
			MicroscopeObject that = (MicroscopeObject) obj;
			if(this == that) return true;
			
			boolean equals = this.microscope_name.equals(that.microscope_name);
			return equals;
		}
		return false;
	}

	@Override
	public void dispose()
	{}
}
