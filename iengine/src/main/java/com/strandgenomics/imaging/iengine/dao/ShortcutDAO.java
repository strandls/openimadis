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
