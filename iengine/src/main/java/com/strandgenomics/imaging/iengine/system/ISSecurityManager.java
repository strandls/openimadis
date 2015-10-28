/*
 * ISSecurityManager.java
 *
 * AVADIS Image Management System
 * Core Engine
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
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
