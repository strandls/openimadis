package com.strandgenomics.imaging.iengine.cache;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


/**
 * definitions of the methods that must be implemented by caching service provider
 * 
 * @author Anup Kulkarni
 */
public interface CacheService extends Remote{
	/**
	 * returns the object in the cache mapped to requested key
	 * @param key specified key
	 * @return object mapped to specified key
	 */
	public Object get(CacheKey key) throws RemoteException;
	
	/**
	 * sets the object in the cache to specified key
	 * @param key specified key
	 * @param value specified object
	 */
	public void set(CacheKey key, Object value) throws RemoteException;
	
	/**
	 * removes the object from the cache mapped to requested key
	 * @param key specified key
	 * @return object mapped to specified key
	 */
	public Object remove(CacheKey key) throws RemoteException;
	
	/**
	 * returns true if key is already cached; false otherwise
	 * @param key specified key
	 * @return true if key is already cached; false otherwise
	 */
	public boolean isCached(CacheKey key) throws RemoteException;
	
	public Map<CacheKey, Object> getAll() throws RemoteException;
	
	public void loadAll(Map<CacheKey, Object> data) throws RemoteException;
	
	/**
	 * remove all objects with specified cachekey type
	 * @param type
	 * @throws RemoteException 
	 */
	public void removeAll(CacheKeyType type) throws RemoteException;
	
	/**
	 * remove all objects from the cache
	 */
	public void removeAll() throws RemoteException;
	
	/**
	 * get all objects of specified cachekey type
	 * @param type
	 * @return
	 * @throws RemoteException
	 */
	public Map<CacheKey, Object> getAll(CacheKeyType type) throws RemoteException;
}
