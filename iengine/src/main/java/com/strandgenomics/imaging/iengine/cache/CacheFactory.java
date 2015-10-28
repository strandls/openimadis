package com.strandgenomics.imaging.iengine.cache;

/**
 * Factory class for creating appropriate cache manager
 * 
 * @author Anup Kulkarni
 */
public class CacheFactory {

	/**
	 * creates cache manager for given class name
	 * @param className
	 * @return
	 */
	public static CacheService createCacheManager(String className)
    {
        
		CacheService cacheManager = null;

        // check for null
        if (className == null || className.length() == 0) 
        {
            throw new RuntimeException("system property "+className +" not found");
        }

        try 
        {
            // load class dynamically
            Class<?> coreClass = Class.forName(className);
            //calls the default constructor
            cacheManager = (CacheService) coreClass.newInstance();
        }
        catch(ClassNotFoundException ex)
        {
            throw new RuntimeException("cannot find cache service class "+className);
        }
        catch(Exception eex)
        {
            throw new RuntimeException("cannot instantiate cache service class "+className);
        }

        return cacheManager;
    }
}
