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
