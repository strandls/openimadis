package com.strandgenomics.imaging.iengine.licensing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.util.Properties;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.SystemManager;

/**
 * this will be exposed to rest of the world and will take care of all the
 * licensing related functionalities
 * 
 * @author Anup Kulkarni
 */
public class LicenseManager extends SystemManager{

	/**
	 * validator used for validating the license properties
	 */
	private LicenseValidator validator;
	/**
	 * license related properties
	 */
	private LicenseProperties properties;
	
	public LicenseManager() throws IOException
	{
		this.properties = loadLicenseProperties();
		
		LicenseKeys keys = loadLicenseKeys();
		this.validator = new LicenseValidator(keys);
	}
	
	/**
	 * returns all the license related properties
	 * @return
	 */
	public LicenseProperties getLicenseProperties()
	{
		return this.properties;
	}
	
	/**
	 * authorizes this license 
	 * @param prop specified license
	 * @return true if specified license is valid as per the license keys
	 * @throws IOException 
	 */
	public boolean isValidLicense(LicenseProperties prop) throws IOException
	{
		return validator.isValid(prop);
	}
	
	private LicenseProperties loadLicenseProperties() throws IOException
	{
		String licenseLocation = Constants.getLicenseFileLocation();
		
		Properties prop = new Properties();
		prop.load(new FileInputStream(licenseLocation));
		
		return new LicenseProperties(prop);
	}
	
	private LicenseKeys loadLicenseKeys() throws IOException
	{
		String licenseLocation = Constants.getLicenseKeysFileLocation();
		
		ObjectInputStream ois = null;
		try
		{
			ois = new ObjectInputStream(new FileInputStream(licenseLocation));
			PublicKey key = (PublicKey)ois.readObject();
			
			return new LicenseKeys(key);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			ois.close();
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		File f = new File("C:\\repository\\rep\\imaging\\worker-deploy\\iworker.properties");//iworker.properties.
    	System.out.println(f.getName());
    	if(f.isFile())
    	{
    		System.out.println("loading system properties from "+f);
			BufferedReader inStream = new BufferedReader(new FileReader(f));
			Properties props = new Properties();
			props.load(inStream);

			props.putAll(System.getProperties());
			props.list(System.out);

			System.setProperties(props);
			inStream.close();
    	}
		
		LicenseManager lm = SysManagerFactory.getLicenseManager();
		
		LicenseProperties properties = lm.getLicenseProperties();
		System.out.println(properties.getChecksum());
		boolean isValid = lm.isValidLicense(properties);
		System.out.println(isValid);
	}
}
