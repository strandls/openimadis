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
