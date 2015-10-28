package com.strandgenomics.imaging.iengine.cache;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import com.strandgenomics.imaging.icore.Constants;

public class MemcachedImpl extends LocalCacheImpl{
	
	private MemcachedClient memcachedClient;
	
	public MemcachedImpl() throws IOException
	{
		String memcachedIp = Constants.getMemcachedIp();
		int memcachedPort = Constants.getMemcachedPort();
		
		String addr = memcachedIp + ":" + memcachedPort;
		memcachedClient = new MemcachedClient(AddrUtil.getAddresses(addr));
	}
	
	/**
	 * memcached cannot accept keys with spaces init
	 * @param key cache key
	 * @return new key with spaces replaced with _
	 */
	private CacheKey sanitizeKey(CacheKey key)
	{
		return new CacheKey(key.keyName.toString().replaceAll(" ", "_"), key.keyType);
	}

	@Override
	public Object get(CacheKey key) throws RemoteException
	{
		key = sanitizeKey(key);
		return memcachedClient.get(key.toString());
	}

	@Override
	public void set(CacheKey key, Object value) throws RemoteException
	{
		key = sanitizeKey(key);
		super.set(key, value);
		Future<Boolean> result = memcachedClient.set(key.toString(), 0, value);
	}

	@Override
	public Object remove(CacheKey key) throws RemoteException
	{
		key = sanitizeKey(key);
		
		super.remove(key);
		
		Object value = memcachedClient.get(key.toString());
		memcachedClient.delete(key.toString());
		return value;
	}

	@Override
	public boolean isCached(CacheKey key) throws RemoteException
	{
		key = sanitizeKey(key);
		Object value = memcachedClient.get(key.toString());
		return value!=null;
	}
	
	@Override
	public void loadAll(Map<CacheKey, Object> data) throws RemoteException
	{
		for(Entry<CacheKey, Object> entry:data.entrySet())
		{
			CacheKey key = sanitizeKey(entry.getKey());
			Object value = entry.getValue();
			
			super.set(key, value);
			set(key, value);
		}
	}
	
	@Override
	public void removeAll(CacheKeyType type) throws RemoteException {
		
		Map<CacheKey, Object> keysForType= super.getAll(type);
		
		super.removeAll(type);
		
		if(keysForType!=null){
			
			for(Entry<CacheKey, Object> entry : keysForType.entrySet()){
				
				if(entry.getKey().keyType == type){
					memcachedClient.delete(sanitizeKey(entry.getKey()).toString());
				}
			}
		}
	}

	@Override
	public void removeAll() {
		
		super.removeAll();
		memcachedClient.flush();
	}

	@Override
	public Map<CacheKey, Object> getAll(CacheKeyType type) throws RemoteException {
		
		Map<CacheKey, Object> temp = new HashMap<CacheKey, Object>();

		Map<CacheKey, Object> keysForType= super.getAll(type);
		
		if(keysForType!=null){
			
			for(Entry<CacheKey, Object> entry : keysForType.entrySet()){
				
				if(entry.getKey().keyType == type){
					temp.put(entry.getKey(), memcachedClient.get(sanitizeKey(entry.getKey()).toString()));
				}
			}
		}
		
		return temp;
	}

}