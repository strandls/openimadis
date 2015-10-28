package com.strandgenomics.imaging.iengine.dao.db;

import java.math.BigInteger;
import java.sql.Types;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ShortcutDAO;
import com.strandgenomics.imaging.iengine.models.Shortcut;

public class DBShortcutDAO extends ImageSpaceDAO<Shortcut> implements ShortcutDAO {

	DBShortcutDAO(ImageSpaceDAOFactory factory,	ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "ShortcutDAO.xml");
	}

	@Override
	public void insertShortcut(BigInteger shortcutSign, BigInteger originalArchiveSign) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_SHORTCUT");
        logger.logp(Level.INFO, "DBShortcutDAO", "insertShortcut", "shortcut_guid="+shortcutSign);
        
        sqlQuery.setValue("Signature",       Util.toHexString(shortcutSign), Types.VARCHAR);
        sqlQuery.setValue("OriginalSignature",       Util.toHexString(originalArchiveSign), Types.VARCHAR);

        updateDatabase(sqlQuery);
	}

	@Override
	public void deleteShortcut(BigInteger shortcutSign) throws DataAccessException 
	{
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_SHORTCUT");
        logger.logp(Level.INFO, "DBShortcutDAO", "insertShortcut", "shortcut_guid="+shortcutSign);
        
        sqlQuery.setValue("Signature",       Util.toHexString(shortcutSign), Types.VARCHAR);
        updateDatabase(sqlQuery);
	}

	@Override
	public Shortcut createObject(Object[] columnValues) 
	{
		BigInteger signature = Util.toBigInteger( (String)columnValues[0] ) ;
		BigInteger originalSignature    = Util.toBigInteger( (String)columnValues[1] ) ;
		return new Shortcut(signature, originalSignature);
	}

	@Override
	public Shortcut getShortcut(BigInteger shortcutSign) throws DataAccessException 
	{
		if(shortcutSign == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.FINE, "DBShortcutDAO", "getShortcut", "shortcutSign="+shortcutSign);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SHORTCUT_FOR_ID");
        sqlQuery.setValue("Signature", Util.toHexString(shortcutSign), Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}

}
