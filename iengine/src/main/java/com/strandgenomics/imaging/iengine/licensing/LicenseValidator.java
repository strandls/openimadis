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

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * This will take care of the validation of the License related properties
 * 
 * @author Anup Kulkarni
 */
public class LicenseValidator {

	/**
	 * authentication keys required for validation
	 */
	private LicenseKeys keys;

	public LicenseValidator(LicenseKeys keys)
	{
		this.keys = keys;
	}
	
	/**
	 * returns if License properties for this validator are valid as per the
	 * LicenseKeys
	 * @param properties license properties to be validated
	 * @return if true if License properties for this validator are valid as per the
	 * LicenseKeys, false otherwise
	 * @return
	 * @throws IOException 
	 */
	public boolean isValid(LicenseProperties properties) throws IOException
	{
		String computedChecksum = getComputedChecksum(properties);
		String storedChecksum = getStoredChecksum(properties);
		
		return computedChecksum.equals(storedChecksum);
	}
	
	private String getStoredChecksum(LicenseProperties properties)
	{
		String encryptedChecksum = properties.getChecksum();
		return decrypt(encryptedChecksum);
	}
	
	private String decrypt(String encryptedChecksum)
	{
		try
		{
			Cipher cipher = Cipher.getInstance(LicenseConstants.ENCRYPTION_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, this.keys.getKey());
			
			byte[] decoded = cipher.doFinal(Hex.decodeHex(encryptedChecksum.toCharArray()));
			char[] toCharArray = new char[decoded.length];
			
			for(int i=0;i<decoded.length;i++)
			{
				toCharArray[i] = (char)decoded[i];
			}
			
			return new String(toCharArray);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	private String getComputedChecksum(LicenseProperties properties) throws IOException
	{
		Properties prop = properties.getChecksumProperties();
		Set<Entry<Object, Object>> values = prop.entrySet();
		
		StringBuffer sb = new StringBuffer();
		for (Entry<Object, Object> value : values)
		{
			sb.append(value.getKey());
			sb.append("_");
			sb.append(value.getValue());
		}
		String propertyString = sb.toString();
		BigInteger computedHash = Util.computeMD5Hash(propertyString);
		
		return computedHash.toString();
	}
}
