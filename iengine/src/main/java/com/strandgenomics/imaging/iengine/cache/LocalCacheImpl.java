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

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LRUMap;

public class LocalCacheImpl implements CacheService{

	/**
	 * LRU map implemetation of caching service
	 */
	private Map<CacheKey, Object> localCache; 
	
	public LocalCacheImpl()
	{
		localCache = new LRUMap(100000);
	}
	
	@Override
	public Object get(CacheKey key) throws RemoteException
	{
		return localCache.get(key);
	}

	@Override
	public void set(CacheKey key, Object value) throws RemoteException
	{
		localCache.put(key, value);
	}

	@Override
	public Object remove(CacheKey key) throws RemoteException
	{
		return localCache.remove(key);
	}

	@Override
	public boolean isCached(CacheKey key) throws RemoteException
	{
		return localCache.containsKey(key);
	}

	@Override
	public Map<CacheKey, Object> getAll() throws RemoteException
	{
		Map<CacheKey, Object> temp = new HashMap<CacheKey, Object>(localCache);
		return temp;
	}

	@Override
	public void loadAll(Map<CacheKey, Object> data) throws RemoteException
	{
		localCache.putAll(data);
	}

	@Override
	public void removeAll(CacheKeyType type) throws RemoteException {
		
		Set<CacheKey> cacheKeySet= localCache.keySet();
		
		Object[] keys = cacheKeySet.toArray();
		
		if(keys!=null){
			for(Object key : keys){
				
				CacheKey k = (CacheKey) key;
				if(k.keyType == type){
					localCache.remove(key);
				}
			}
		}
	}

	@Override
	public void removeAll() {
		localCache.clear();
	}

	@Override
	public Map<CacheKey, Object> getAll(CacheKeyType type)
			throws RemoteException {
		
		Map<CacheKey, Object> temp = new HashMap<CacheKey, Object>();

		Set<CacheKey> cacheKeySet= localCache.keySet();
		
		Object[] keys = cacheKeySet.toArray();
		
		for(Object key : keys)
		{
			CacheKey k = (CacheKey) key;
			if(k.keyType == type){
				temp.put(k, localCache.get(key));
			}
		}
		
		return temp;
	}

}
