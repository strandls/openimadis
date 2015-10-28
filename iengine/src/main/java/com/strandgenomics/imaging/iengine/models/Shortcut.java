package com.strandgenomics.imaging.iengine.models;

import java.math.BigInteger;

import com.strandgenomics.imaging.icore.Storable;

/**
 * Data structure to hold mapping between the shortcut and the original data
 * 
 * @author Anup Kulkarni
 */
public class Shortcut implements Storable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9183287789516680096L;
	/**
	 * sign of the shortcut which is in ArchiveRegistry
	 */
	private BigInteger shorcutSign;
	/**
	 * sign of the shortcut which is in ArchiveRegistry
	 */
	private BigInteger originalArchiveSign;
	
	public Shortcut(BigInteger sign, BigInteger originalArchiveSign) 
	{
		this.shorcutSign = sign;
		this.originalArchiveSign = originalArchiveSign;
	}
	
	public BigInteger getOriginalArchiveSign()
	{
		return this.originalArchiveSign;
	}
	
	public BigInteger getShortcutSign()
	{
		return shorcutSign;
	}
	
	@Override
	public void dispose() 
	{}

}
