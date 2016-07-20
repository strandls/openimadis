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

package com.strandgenomics.imaging.iengine.dao;

import java.math.BigInteger;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Shortcut;

public interface ShortcutDAO {
	/**
	 * insert the shorcut reference
	 * @param shortcutSign sign of the shortcut record
	 * @param originalArchiveSign sign of the original record archive
	 * @throws DataAccessException 
	 */
	public void insertShortcut(BigInteger shortcutSign, BigInteger originalArchiveSign) throws DataAccessException;
	
	/**
	 * delete the shortcut 
	 * @param shortcutSign sign of the shortcut record
	 * @throws DataAccessException 
	 */
	public void deleteShortcut(BigInteger shortcutSign) throws DataAccessException;
	
	/**
	 * get the specified shortcut
	 * @param shortcutSign sign of the shortcut record
	 * @return specified shortcut record
	 * @throws DataAccessException
	 */
	public Shortcut getShortcut(BigInteger shortcutSign) throws DataAccessException;
}
