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

package com.strandgenomics.imaging.iengine.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.compute.ComputeException;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfileType;
import com.strandgenomics.imaging.iengine.models.LengthUnit;
import com.strandgenomics.imaging.iengine.models.LicenseIdentifier;
import com.strandgenomics.imaging.iengine.models.MicroscopeObject;
import com.strandgenomics.imaging.iengine.models.TimeUnit;

/**
 * Manager class to manage properties of Microscope and acquisition profiles 
 * 
 * @author Anup Kulkarni
 */
public class MicroscopeManager extends SystemManager
{
	/**
	 * number of acq client licenses: signifies # of acq clients that can be launched simultaneously
	 */
	public static final int TOTAL_ACQ_LICENSES = Constants.getAcqClientLicenses();
	private static final Object ACTIVE_ACQ_LICENSES = "Active_Licenses";
	
	public MicroscopeManager()
	{
		SysManagerFactory.getCacheManager().set(new CacheKey(ACTIVE_ACQ_LICENSES, CacheKeyType.Misc), 0);
	}
	
	/**
	 * register new microscope
	 * @param actorName name of the logged in user
	 * @param name name of the microscope
	 * @param ipAddress of the microscope machine
	 * @param macAddress of the microscope machine
	 * @param licenses number of acquisition client licenses to be reserved for this microscope
	 * @throws DataAccessException
	 */
	public void registerMicroscope(String actorName, String name, String ipAddress, String macAddress, int licenses) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		if(name == null || name.isEmpty())
			throw new IllegalArgumentException("microscope name cannot be empty");
		
		List<MicroscopeObject> existingMicroscopes = listMicroscopes();
		int nExistingMicroscopes = existingMicroscopes == null ? 0 : existingMicroscopes.size();
//		try
//		{
//			if(nExistingMicroscopes>=SysManagerFactory.getLicenseManager().getLicenseProperties().getMaxMicroscopes())
//				throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.MAX_MICROSCOPE_LIMIT_EXCEEDED));
//		}
//		catch (IOException e)
//		{
//			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.LICENSE_EXCEPTION));
//		}
		
		MicroscopeObject microscope = new MicroscopeObject(name, macAddress, ipAddress, licenses);
		
		if(licenses>getAvailableLicenses())
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.NOT_ENOUGH_ACQ_LICENSES));
		
		DBImageSpaceDAOFactory.getDAOFactory().getMicroscopeDAO().registerMicroscope(microscope);
	}
	
	/**
	 * string number of licenses available
	 * @return
	 * @throws DataAccessException 
	 */
	private int getAvailableLicenses() throws DataAccessException
	{
		List<MicroscopeObject> microscopes = listMicroscopes();
		
		int reserved = 0;
		if(microscopes!=null)
		{
			for(MicroscopeObject microscope : microscopes)
			{
				reserved += microscope.getAcquisitionLicenses();
			}
		}
		
		return TOTAL_ACQ_LICENSES - reserved;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public MicroscopeObject getMicroscope(String name) throws DataAccessException
	{
		return DBImageSpaceDAOFactory.getDAOFactory().getMicroscopeDAO().getMicroscope(name);
	}
	
	/**
	 * 
	 * @param name
	 * @throws DataAccessException
	 */
	public void deleteMicroscope(String actorName, String name) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		DBImageSpaceDAOFactory.getDAOFactory().getMicroscopeDAO().deleteMicroscope(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param newDetail
	 * @throws DataAccessException
	 */
	public void updateMicroscope(String actorName, String name, MicroscopeObject newDetail) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(actorName))
			throw new ComputeException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		MicroscopeObject microscope = getMicroscope(name);
		int licenseChange = newDetail.getAcquisitionLicenses() - microscope.getAcquisitionLicenses();
		
		if(licenseChange>getAvailableLicenses())
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.NOT_ENOUGH_ACQ_LICENSES));
		
		DBImageSpaceDAOFactory.getDAOFactory().getMicroscopeDAO().updateMicroscope(name, newDetail);
	}
	
	/**
	 * enlist all the registered microscopes
	 * @return list of all the registered microscopes
	 * @throws DataAccessException
	 */
	public List<MicroscopeObject> listMicroscopes(String actorLogin) throws DataAccessException
	{
		return listMicroscopes();
	}
	
	private List<MicroscopeObject> listMicroscopes() throws DataAccessException
	{
		return DBImageSpaceDAOFactory.getDAOFactory().getMicroscopeDAO().list();
	}
	
	/**
	 * register new profile for acquisition
	 * @param profileName name of the profile
	 * @param microscope_name name of the microscope
	 * @param xPixelSize pixel size along x axis
	 * @param yPixelSize pixel size along y axis
	 * @param zPixelSize pixel size along z axis
	 * @param sourceType format of the source file
	 * @param timeUnit time unit
	 * @param lengthUnit length unit
	 * @param type type of acquisition profile
	 * @throws DataAccessException 
	 */
	public void createAcquisitionProfile(String profileName, String microscope_name, Double xPixelSize, AcquisitionProfileType xType, Double yPixelSize, AcquisitionProfileType yType, Double zPixelSize, AcquisitionProfileType zType, SourceFormat sourceType, TimeUnit elapsedTimeUnit, TimeUnit exposureTimeUnit, LengthUnit lengthUnit) throws DataAccessException
	{
		AcquisitionProfile acqProfile = new AcquisitionProfile(profileName, microscope_name, xPixelSize, xType, yPixelSize, yType, zPixelSize, zType, sourceType, elapsedTimeUnit, exposureTimeUnit, lengthUnit);
		
		List<AcquisitionProfile> profiles = listAcquisitionProfiles();
		if(profiles!=null)
		{
			for(AcquisitionProfile profile:profiles)
			{
				if(profile.getMicroscope().equals(microscope_name) && profile.getProfileName().equals(profileName))
				{
					throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.ACQ_PROFILE_NAME_EXISTS));
				}
			}
		}
		
		DBImageSpaceDAOFactory.getDAOFactory().getAcquisitionProfileDAO().createAcquisitionProfile(acqProfile);
	}
	
	/**
	 * delete the acquisition specified profile associated with specified microscope
	 * @param user logged in user
	 * @param profileName name of the profile
	 * @param microscopeName name of the associated microscope
	 * @throws DataAccessException
	 */
	public void deleteAcquisitionProfile(String user, String profileName, String microscopeName) throws DataAccessException
	{
		if(SysManagerFactory.getUserPermissionManager().isFacilityManager(user))
			DBImageSpaceDAOFactory.getDAOFactory().getAcquisitionProfileDAO().deleteAcquisitionProfile(microscopeName, profileName);
	}
	
	/**
	 * list all the acquisition profiles
	 * @return list of all the acquisition profiles
	 * @throws DataAccessException 
	 */
	public List<AcquisitionProfile> listAcquisitionProfiles() throws DataAccessException
	{
		return DBImageSpaceDAOFactory.getDAOFactory().getAcquisitionProfileDAO().listAcquisitionProfiles();
	}

	/**
	 * request license for acquisition client
	 * @param actorLogin user
	 * @param accessToken the access token used for acquisition client
	 * @param ipAddress the ip address of the machine on which acquisition client is to be launched
	 * @param macAddress the mac address of the machine on which acquisition client is to be launched
	 * @return true if license is granted, false otherwise
	 * @throws DataAccessException 
	 */
	public synchronized boolean requestAcquisitionLicense(String actorLogin, String accessToken, String ipAddress, String macAddress) throws DataAccessException
	{
		logger.logp(Level.INFO, "MicroscopeManager", "requestAcquisitionLicense", "requesting acq licenses for user="+actorLogin+" from ip="+ipAddress+" and mac="+macAddress);
		int activeGeneralLicenses = (Integer) SysManagerFactory.getCacheManager().get(new CacheKey(ACTIVE_ACQ_LICENSES, CacheKeyType.Misc));
		if(activeGeneralLicenses  >= TOTAL_ACQ_LICENSES)// no license is available
			return false;
		
		// check if the requesting machine is registered microscope
		List<MicroscopeObject> microscopes = listMicroscopes();
		
		MicroscopeObject requestingMicroscope = null;
		int reservedForMicroscopes = 0;
		if(microscopes!=null)
		{
			for(MicroscopeObject microscope:microscopes)
			{
				if(microscope.mac_address.equals(macAddress))// considering only mac address
				{
					requestingMicroscope = microscope;
				}
				
				reservedForMicroscopes += microscope.getAcquisitionLicenses();
			}
		}
		
		if(requestingMicroscope==null)// this is not registered machine, licenses will be given from general quota
		{
			logger.logp(Level.FINEST, "MicroscopeManager", "requestAcquisitionLicense", "not a registered machine");
			if(activeGeneralLicenses >= (TOTAL_ACQ_LICENSES - reservedForMicroscopes))
			{
				logger.logp(Level.FINEST, "MicroscopeManager", "requestAcquisitionLicense", "acq license rejected: active licenses="+activeGeneralLicenses+" total licenses="+TOTAL_ACQ_LICENSES+" reserved for microscopes="+reservedForMicroscopes);
				return false;
			}
			
			activeGeneralLicenses++;// increament the count for general quota
			SysManagerFactory.getCacheManager().set(new CacheKey(ACTIVE_ACQ_LICENSES, CacheKeyType.Misc),  activeGeneralLicenses);
		}
		else// this is a registered machine
		{
			logger.logp(Level.FINEST, "MicroscopeManager", "requestAcquisitionLicense", "registered machine");
			int alreadyActive = 0;
			
			CacheKey key = new CacheKey(requestingMicroscope, CacheKeyType.Microscope);
			if(SysManagerFactory.getCacheManager().isCached(key))
			{
				if(activeGeneralLicenses >= (TOTAL_ACQ_LICENSES - reservedForMicroscopes))
				{
					logger.logp(Level.FINEST, "MicroscopeManager", "requestAcquisitionLicense", "acq license rejected: active licenses="+activeGeneralLicenses+" total licenses="+TOTAL_ACQ_LICENSES+" reserved for microscopes="+reservedForMicroscopes);
					return false;
				}
				
				alreadyActive = (Integer) SysManagerFactory.getCacheManager().get(key);
				if(alreadyActive>=requestingMicroscope.getAcquisitionLicenses())
				{
					logger.logp(Level.FINEST, "MicroscopeManager", "requestAcquisitionLicense", "num licenses for current microscope exeeded the reservations: already active"+alreadyActive+" reservations="+requestingMicroscope.getAcquisitionLicenses());
					return false;
				}
			}
			
			SysManagerFactory.getCacheManager().set(key, alreadyActive+1);
		}
		
		LicenseIdentifier licenseIdentifier = new LicenseIdentifier(System.currentTimeMillis(), accessToken, System.currentTimeMillis(), ipAddress, macAddress); 
		DBImageSpaceDAOFactory.getDAOFactory().getLicenseDAO().insertLicense(licenseIdentifier);
		
		logger.logp(Level.FINEST, "MicroscopeManager", "requestAcquisitionLicense", "acq license granted");
		return true;
	}
	
	/**
	 * surrender the acquisition client license used by accessToken
	 * @param accessToken specified accessToken
	 * @throws DataAccessException 
	 */
	public synchronized void surrenderAcquisitionLicense(String accessToken) throws DataAccessException
	{
		LicenseIdentifier identifier = DBImageSpaceDAOFactory.getDAOFactory().getLicenseDAO().getLicense(accessToken);
		
		if(identifier == null)// already surrendered
			return;
		
		DBImageSpaceDAOFactory.getDAOFactory().getLicenseDAO().deleteLicense(accessToken);
		
		List<MicroscopeObject> microscopes = listMicroscopes();
		
		if(microscopes!=null)
		{
			for(MicroscopeObject microscope:microscopes)
			{
				if(microscope.mac_address.equals(identifier.macAddress))// considering only mac address
				{
					int value = (Integer) SysManagerFactory.getCacheManager().remove(new CacheKey(microscope, CacheKeyType.Microscope));
					if(value>microscope.getAcquisitionLicenses())
					{
						SysManagerFactory.getCacheManager().set(new CacheKey(microscope, CacheKeyType.Microscope), value-1);
					}
					return;
				}
			}
		}
		
		logger.logp(Level.INFO, "MicroscopeManager", "surrenderAcquisitionLicense", "surrendered linces for ="+accessToken);
		CacheKey key = new CacheKey(ACTIVE_ACQ_LICENSES, CacheKeyType.Misc);
		int activeGeneralLicenses = (Integer) SysManagerFactory.getCacheManager().get(key);
		activeGeneralLicenses--;
		SysManagerFactory.getCacheManager().set(key, activeGeneralLicenses);
	}

	/**
	 * returns microscope name for ip and mac
	 * @param ipAddress 
	 * @param macAddress
	 * @return microscope name
	 * @throws DataAccessException 
	 */
	public String getMicroscope(String ipAddress, String macAddress) throws DataAccessException
	{
		List<MicroscopeObject> microscopes = listMicroscopes();
		if(microscopes!=null)
		{
			for(MicroscopeObject microscope:microscopes)
			{
				if(microscope.mac_address.equals(macAddress)) // consider only the mac address
					return microscope.microscope_name;
			}
		}
		return null;
	}

	/**
	 * returns list of all active lisenses
	 * @param user
	 * @throws DataAccessException 
	 */
	public List<LicenseIdentifier> listActiveLicenses(String user) throws DataAccessException
	{
		if(!SysManagerFactory.getUserPermissionManager().isFacilityManager(user))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		List<LicenseIdentifier> activeLicenses = new ArrayList<LicenseIdentifier>();
		List<LicenseIdentifier> licenses = DBImageSpaceDAOFactory.getDAOFactory().getLicenseDAO().listAllLicenses();
		if(licenses!=null)
		{
			for(LicenseIdentifier license:licenses)
			{
				try
				{
					long expTime = SysManagerFactory.getAuthorizationManager().getExpiryTime(license.accessToken);
					if(expTime>0)
					{
						activeLicenses.add(license);
					}
				}
				catch (Exception e)
				{}
			}
		}
		
		return activeLicenses;
	}

	/**
	 * returns text form of number of licenses currently active
	 * @return
	 * @throws DataAccessException 
	 */
	public Map<String, Integer> getLicenseCounts() throws DataAccessException
	{
		int activeGeneralLicenses = (Integer) SysManagerFactory.getCacheManager().get(new CacheKey(ACTIVE_ACQ_LICENSES, CacheKeyType.Misc));
		
		List<MicroscopeObject> microscopes = listMicroscopes();
		
		int count = 0;
		if(microscopes!=null)
		{
			for(MicroscopeObject microscope:microscopes)
			{
				Object value = SysManagerFactory.getCacheManager().get(new CacheKey(microscope, CacheKeyType.Microscope));
				if(value!=null && ((Integer)value)>0)
				{
					count+=((Integer)value);
				}
			}
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("floating", activeGeneralLicenses);
		map.put("fixed", count);
		map.put("total", TOTAL_ACQ_LICENSES);
		
		return map;
	}
}