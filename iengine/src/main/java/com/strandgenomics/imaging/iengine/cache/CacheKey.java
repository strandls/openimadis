package com.strandgenomics.imaging.iengine.cache;

import java.io.Serializable;

/**
 * Encapsulated key used for caching
 * 
 * @author Anup Kulkarni
 */
public class CacheKey implements Serializable{
	
	/**
	 * name of the key by which value is cached
	 */
	public final Object keyName;
	/**
	 * type of the key
	 */
	public final CacheKeyType keyType;
	
	public CacheKey(Object name, CacheKeyType type)
	{
		this.keyName = name;
		this.keyType = type;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof CacheKey)
		{
			CacheKey that = (CacheKey) obj;
			if(this == that) return true;
			
			boolean equals = this.keyName.equals(that.keyName) && this.keyType.equals(that.keyType);
			return equals;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("KeyName");
		sb.append(keyName);
		sb.append("KeyType");
		sb.append(keyType.name());
		
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}
}
