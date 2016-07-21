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

import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.strandgenomics.imaging.icore.util.EncryptionUtil;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;

public class ISSecurityManager extends SystemManager {
	
	private RSAPublicKey  publicKey = null;
    private RSAPrivateKey privateKey = null;
	
	ISSecurityManager()
	{
		privateKey = (RSAPrivateKey) SysManagerFactory.getCacheManager().get(new CacheKey("Private Key", CacheKeyType.PrivateKey));
		publicKey = (RSAPublicKey) SysManagerFactory.getCacheManager().get(new CacheKey("Public Key", CacheKeyType.PublicKey));
		
		if(privateKey == null || publicKey == null)
		{
			//create a new private/public key pair
	        Key[] publicPrivateKeys = EncryptionUtil.generateAsymmetricKeys();
	        
	        publicKey  = (RSAPublicKey) publicPrivateKeys[0];
	        privateKey = (RSAPrivateKey) publicPrivateKeys[1];
	        
	        SysManagerFactory.getCacheManager().set(new CacheKey("Private Key", CacheKeyType.PrivateKey), privateKey);
	        SysManagerFactory.getCacheManager().set(new CacheKey("Public Key", CacheKeyType.PublicKey), publicKey);
		}
        
	}
	
	public RSAPublicKey getPublicKey()
	{
		return publicKey;
	}
	
	public RSAPrivateKey getPrivateKey()
	{
		return privateKey;
	}
}
