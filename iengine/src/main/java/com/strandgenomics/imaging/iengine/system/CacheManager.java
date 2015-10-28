package com.strandgenomics.imaging.iengine.system;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.cache.CacheService;

/**
 * class to manage System cache
 * 
 * @author Anup Kulkarni
 */
public class CacheManager extends SystemManager{

	private CacheService cacheInstance = null;
	
	public CacheManager()
	{
		try
		{
			Registry registry = LocateRegistry.getRegistry(Constants.getCacheServiceHost(), Constants.getCacheServicePort());
			cacheInstance = (CacheService) registry.lookup(CacheService.class.getCanonicalName());
		}
		catch (NotBoundException e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error creating cache service instance", e);
		}
		catch (AccessException e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error creating cache service instance", e);
		}
		catch (RemoteException e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error creating cache service instance", e);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error creating cache service instance", e);
		}
	}
	
	public Object get(CacheKey key)
	{
		try
		{
			return cacheInstance.get(key);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
		return null;
	}
	
	public void set(CacheKey key, Object value)
	{
		try
		{
			cacheInstance.set(key, value);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
	}
	
	public Object remove(CacheKey key)
	{
		try
		{
			return cacheInstance.remove(key);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
		return null;
	}
	
	public boolean isCached(CacheKey key)
	{
		try
		{
			return cacheInstance.isCached(key);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
		return false;
	}
	
	public void removeAll(CacheKeyType type)
	{
		try
		{
			cacheInstance.removeAll(type);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
	}
	
	public void removeAll()
	{
		try
		{
			cacheInstance.removeAll();
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
	}
	
	public int getCount(CacheKeyType type)
	{
		Map<CacheKey, Object> map = null;
		
		try
		{
			map = cacheInstance.getAll(type);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "CacheManager", "CacheManager", "error getting cache instance", e);
		}
		
		return map.size();
	}
}
