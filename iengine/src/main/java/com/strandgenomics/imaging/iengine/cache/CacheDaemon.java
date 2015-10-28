package com.strandgenomics.imaging.iengine.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.strandgenomics.imaging.icore.Constants;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class CacheDaemon implements CacheService{

	/**
	 * frequency in which remote node will be pinged, <IS_ALIVE> message
	 */
	private static final long POLLING_FREQENCY = 15;
	/**
	 * local cache node instantiated by this class
	 */
	private CacheService localServiceObject=null;
	/**
	 * remote cache stub
	 */
	private CacheService remoteServiceObject = null;
	
	public CacheDaemon()
	{
		localServiceObject = CacheFactory.createCacheManager(Constants.getCacheImplClassName());
		
		initRemoteCacheService();
		initCacheService();
		
		syncData();
		
		ScheduledThreadPoolExecutor cachePolling = new ScheduledThreadPoolExecutor(1);
		cachePolling.scheduleWithFixedDelay(new CachePoller(), POLLING_FREQENCY, POLLING_FREQENCY, TimeUnit.SECONDS);
	}
	
	private void syncData()
	{
		if(remoteServiceObject!=null)
		{
			System.out.println("Syncing data from remote");
			Map<CacheKey, Object> data;
			try
			{
				data = remoteServiceObject.getAll();
				localServiceObject.loadAll(data);
				System.out.println("Syncing done");
			}
			catch (RemoteException e)
			{
				System.out.println("Remote node not ready");
			}
		}
	}
	
	@Override
	public Object get(CacheKey key) throws RemoteException
	{
		return localServiceObject.get(key);
	}

	@Override
	public void set(final CacheKey key, final Object value) throws RemoteException
	{
		try
		{
			localServiceObject.set(key, value);
		}
		catch (Exception e)
		{
			System.out.println("error writting cache");
		}

		if (remoteServiceObject != null)
		{
			try
			{
				remoteServiceObject.set(key, value);
			}
			catch (Exception e)
			{
				System.out.println("error writting cache to remote node... trying after some time");
				remoteServiceObject = null;
			}
		}
	}

	@Override
	public Object remove(final CacheKey key) throws RemoteException
	{
		if(remoteServiceObject!=null)
		{
			Thread t2 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						remoteServiceObject.remove(key);
					}
					catch (Exception e)
					{
						System.out.println("error writting cache to remote node... trying after some time");
						remoteServiceObject = null;
					}
				}
			});
			t2.start();
		}
		
		try
		{
			Object value = localServiceObject.remove(key);
			return value;
		}
		catch (Exception e)
		{
			System.out.println("error writting cache");
		}
		return null;
	}

	@Override
	public boolean isCached(CacheKey key) throws RemoteException
	{
		return localServiceObject.isCached(key);
	}

	@Override
	public Map<CacheKey, Object> getAll() throws RemoteException
	{
		return localServiceObject.getAll();
	}

	@Override
	public void loadAll(Map<CacheKey, Object> data) throws RemoteException
	{
		localServiceObject.loadAll(data);
	}
	
	/**
     * register cache service
     */
    private void initCacheService()
    {
    	try 
        {
        	//create the registry
    		System.out.println(Constants.getCacheServicePort());
    		System.out.println(Constants.getCacheImplClassName());
    		
            LocateRegistry.createRegistry(Constants.getCacheServicePort());  
            
            CacheService stub = (CacheService)UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(Constants.getCacheServicePort());
            registry.bind(CacheService.class.getCanonicalName(), stub);

            System.out.println("CacheService initialized...");
        } 
        catch (Exception e)
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
    /**
     * locate remote cache node, if present
     */
    private void initRemoteCacheService()
    {
    	System.out.println(Constants.getRemoteCacheServicePort());
		System.out.println(Constants.getRemoteCacheServiceHost());
		
		try
		{
			Registry registryRemote = LocateRegistry.getRegistry(Constants.getRemoteCacheServiceHost(), Constants.getRemoteCacheServicePort());
			remoteServiceObject = (CacheService) registryRemote.lookup(CacheService.class.getCanonicalName());
		}
		catch (Exception e)
		{
			System.out.println("Remote service not yet ready... Trying after 15s");
			remoteServiceObject = null;
		}
    }
    
    /**
     * class for polling remote cache node
     * 
     * @author Anup Kulkarni
     */
    private class CachePoller implements Runnable
	{

		@Override
		public void run()
		{
			if(remoteServiceObject == null)
			{
				initRemoteCacheService();
			}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		System.out.println(args[0]);
		if(args != null && args.length > 0)
    	{
			System.out.println("here");
    		File f = new File(args[0]);
    		System.out.println(f.getAbsolutePath());
    		if(f.isFile())
    		{
    			System.out.println("also here");
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.load(inStream);
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
		
		// initialize cache service
		CacheDaemon cd = new CacheDaemon();
	}

	@Override
	public void removeAll(final CacheKeyType type) throws RemoteException {
	
		if(remoteServiceObject!=null)
		{
			Thread t2 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
							remoteServiceObject.removeAll(type);
					}
					catch (Exception e)
					{
						System.out.println("error writting cache to remote node... trying after some time");
						remoteServiceObject = null;
					}
				}
			});
			t2.start();
		}
		
		try
		{
			localServiceObject.removeAll(type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("error writting cache");
		}
	}

	@Override
	public void removeAll() {
		
		if(remoteServiceObject!=null)
		{
			Thread t2 = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						remoteServiceObject.removeAll();
					}
					catch (Exception e)
					{
						System.out.println("error writting cache to remote node... trying after some time");
						remoteServiceObject = null;
					}
				}
			});
			t2.start();
		}
		
		try
		{
			localServiceObject.removeAll();
		}
		catch (Exception e)
		{
			System.out.println("error writting cache");
		}
		
	}

	@Override
	public Map<CacheKey, Object> getAll(CacheKeyType type)
			throws RemoteException {
		
		return localServiceObject.getAll(type);
	}
}
