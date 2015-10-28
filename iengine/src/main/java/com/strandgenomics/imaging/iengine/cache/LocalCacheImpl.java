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
