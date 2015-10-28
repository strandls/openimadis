package com.strandgenomics.imaging.iengine.licensing;

import java.security.PublicKey;

/**
 * Keys required for authentication of the license
 * 
 * @author Anup Kulkarni
 */
public class LicenseKeys {
	/**
	 * public key required for validation of the license
	 */
	private PublicKey publicKey;
	
	public LicenseKeys(PublicKey key)
	{
		this.publicKey = key;
	}
	
	/**
	 * return the key required for the validation of the license
	 * @return the key required for the validation of the license
	 */
	public PublicKey getKey()
	{
		return this.publicKey;
	}
}
