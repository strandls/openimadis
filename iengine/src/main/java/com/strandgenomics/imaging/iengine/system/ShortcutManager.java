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

package com.strandgenomics.imaging.iengine.system;

import java.math.BigInteger;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Shortcut;

/**
 * manager class handling shorcuts
 * 
 * @author Anup Kulkarni
 *
 */
public class ShortcutManager extends SystemManager {
	
	public ShortcutManager() 
	{}
	
	/**
	 * returns shortcut for specified sign
	 * @param shortcutSign archive signature of the shortcut
	 * @return shortcut for specified sign
	 * @throws DataAccessException 
	 */
	public Shortcut getShortcut(BigInteger shortcutSign) throws DataAccessException
	{
		CacheKey key = new CacheKey(shortcutSign, CacheKeyType.ShortcutSign);
		if(SysManagerFactory.getCacheManager().isCached(key)) return (Shortcut) SysManagerFactory.getCacheManager().get(key);
		
		Shortcut shortcut = ImageSpaceDAOFactory.getDAOFactory().getShortcutDAO().getShortcut(shortcutSign);
		if(shortcut != null)
		{
			SysManagerFactory.getCacheManager().set(key, shortcut);
		}
		return shortcut;
	}
	
	/**
	 * 
	 * @param shortcutSign
	 * @throws DataAccessException
	 */
	void deleteShortcut(BigInteger shortcutSign)
	{
		try 
		{
			if(getShortcut(shortcutSign)!=null)
			{
				ImageSpaceDAOFactory.getDAOFactory().getShortcutDAO().deleteShortcut(shortcutSign);
				CacheKey key = new CacheKey(shortcutSign, CacheKeyType.ShortcutSign);
				SysManagerFactory.getCacheManager().remove(key);
			}
		} 
		catch (DataAccessException e) 
		{}
	}
}
