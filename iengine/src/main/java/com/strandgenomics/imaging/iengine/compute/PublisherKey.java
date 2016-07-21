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

package com.strandgenomics.imaging.iengine.compute;

import java.io.Serializable;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * Identifies a publisher (worker-system)
 * @author arunabha
 *
 */
public class PublisherKey implements Serializable {
	
	private static final long serialVersionUID = 6711573320018149546L;
	/**
	 * the access token associated with this PublisherKey
	 */
	public final String accessToken;
	/**
	 * The address this key is bound with
	 */
	public final String ipAddress;
	
	/**
	 * Creates a publisher key bound with the specified IP address
	 * @param ipAddress
	 */
	public PublisherKey(String ipAddress)
	{
		this.ipAddress = ipAddress;
		this.accessToken = "PKEY_"+Util.generateRandomString(35);
	}

	/**
	 * Returns the access token associated with this publisher key
	 * @return the access token used by the publisher
	 */
	public String getAccessToken()
	{
		return accessToken;
	}
	
	@Override
	public int hashCode()
	{
		return accessToken.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof PublisherKey)
		{
			PublisherKey that = (PublisherKey) obj;
			if(this == that) return true;
			return this.accessToken.equals(that.accessToken);
		}
		
		return false;
	}
}
