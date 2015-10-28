/*
 * DBAuthCodeDAO.java
 *
 * Product:  AvadisIMG Server
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.auth.IAccessToken;
import com.strandgenomics.imaging.iengine.auth.IPFilter;
import com.strandgenomics.imaging.iengine.dao.AuthCodeDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.AuthCode;

/**
 * DB implementation of {@link AuthCodeDAO}
 * 
 * @author santhosh
 * 
 */
public class DBAuthCodeDAO extends ImageSpaceDAO<AuthCode> implements AuthCodeDAO {

    DBAuthCodeDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) {
        super(factory, connectionProvider, "AuthCodeDAO.xml");
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iengine.dao.AuthCodeDAO#getAuthCode(java.lang.String)
     */
    @Override
    public AuthCode getAuthCode(String id) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "getAuthCode", "Get authcode with id : " + id);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_AUTHCODE_BY_ID");
        sqlQuery.setValue("id", id, Types.VARCHAR);
        return fetchInstance(sqlQuery);
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iengine.dao.AuthCodeDAO#getAuthCode(java.lang.Long)
     */
    @Override
    public AuthCode getAuthCode(long authID) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "getAuthCode", "Get authcode with internal id : " + authID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_AUTHCODE_BY_INTERNAL_ID");
        sqlQuery.setValue("authID", authID, Types.BIGINT);
        return fetchInstance(sqlQuery);
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iengine.dao.AuthCodeDAO#updateAuthCode(java.lang.String, java.lang.String)
     */
    @Override
    public AuthCode exchangeAuthCode(String oldID, String newID) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "exchangeAuthCode", "exchange auth code : " + oldID + " new: " + newID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("EXCHANGE_AUTHCODE");
        sqlQuery.setValue("id", oldID, Types.VARCHAR);
        sqlQuery.setValue("newid", newID, Types.VARCHAR);
        updateDatabase(sqlQuery);
        return getAuthCode(newID);
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iengine.dao.AuthCodeDAO#listAll()
     */
    @Override
    public List<AuthCode> listTokens() throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "listAuthCodes", "Get all authcodes");
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_TOKENS");
        RowSet<AuthCode> result = find(sqlQuery);
        return result == null ? null : result.getRows();
    }

    /* (non-Javadoc)
     * @see com.strandgenomics.imaging.iengine.dao.AuthCodeDAO#disableAuthCode(java.lang.String)
     */
    @Override
    public void removeAuthCode(long authID) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "removeAuthCode", "remove authcode with authID : " + authID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_AUTHCODE_BY_AUTHID");
        sqlQuery.setValue("authID", authID, Types.BIGINT);
        updateDatabase(sqlQuery);
    }

    @Override
    public AuthCode createObject(Object[] columnValues) {
        String id = (String) columnValues[0];
        long authID = Util.getLong(columnValues[1]);
        String user = (String) columnValues[2];
        String clientID = (String) columnValues[3];
        int services = Util.getInteger(columnValues[4]);
        Date creationDate = Util.getTimestamp(columnValues[5]);
        Date expiryDate = Util.getTimestamp(columnValues[6]);
        Date accessTime = Util.getTimestamp(columnValues[7]);
        boolean valid = Util.getBoolean(columnValues[8]);
        @SuppressWarnings("unchecked")
        List<IPFilter> filters = (List<IPFilter>) toJavaObject((DataSource) columnValues[9]);
        boolean delivered = Util.getBoolean(columnValues[10]);
        return new AuthCode(id, authID, user, clientID, services, creationDate, expiryDate, accessTime, valid, filters,
                delivered);
    }

    @Override
    public AuthCode addAuthCode(String id, String user, String clientID, int services, Date expiry,
            List<IPFilter> filters) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "addAuthCode", "add auth code: " + id + " " + user + " " + clientID
                + " " + services + " " + expiry + " " + filters);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_AUTHCODE");
        sqlQuery.setValue("id", id, Types.VARCHAR);
        sqlQuery.setValue("User", user, Types.VARCHAR);
        sqlQuery.setValue("ClientID", clientID, Types.VARCHAR);
        sqlQuery.setValue("Services", services, Types.INTEGER);
        sqlQuery.setValue("Creation", new Timestamp((new Date()).getTime()), Types.TIMESTAMP);
        sqlQuery.setValue("Expiry", new Timestamp(expiry.getTime()), Types.TIMESTAMP);
        sqlQuery.setValue("LastAccess", new Timestamp((new Date()).getTime()), Types.TIMESTAMP);
        sqlQuery.setValue("Filters", toByteArray(filters), Types.BLOB);
        updateDatabase(sqlQuery);
        return getAuthCode(id);
    }

    @Override
    public IAccessToken updateToken(long authID, int services, Date newExpiry, List<IPFilter> newFilters)
            throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "updateToken", "update auth code: " + authID + " " + services + " "
                + newExpiry + " " + newFilters);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_TOKEN");
        sqlQuery.setValue("authID", authID, Types.BIGINT);
        sqlQuery.setValue("Services", services, Types.INTEGER);
        sqlQuery.setValue("Expiry", new Timestamp(newExpiry.getTime()), Types.TIMESTAMP);
        sqlQuery.setValue("LastAccess", new Timestamp((new Date()).getTime()), Types.TIMESTAMP);
        sqlQuery.setValue("Filters", toByteArray(newFilters), Types.BLOB);
        updateDatabase(sqlQuery);
        return getAuthCode(authID);
    }

    @Override
    public List<AuthCode> listUserTokens(String user) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "listUserTokens", "Get all authcodes: " + user);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_USER_TOKENS");
        sqlQuery.setValue("User", user, Types.VARCHAR);
        RowSet<AuthCode> result = find(sqlQuery);
        return result == null ? null : result.getRows();
    }

    @Override
    public void removeAuthCode(String id) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "removeAuthCode", "remove authcode with ID : " + id);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_AUTHCODE_BY_ID");
        sqlQuery.setValue("ID", id, Types.VARCHAR);
        updateDatabase(sqlQuery);
    }

    @Override
    public void clean() throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "clean", "clean database");
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_EXPIRED");
        updateDatabase(sqlQuery);
    }

    @Override
    public void updateAccessTime(String id, Date accessTime) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "updateAccessTime", "updateAccessTime: " + id + " time: " + accessTime);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_ACCESS_TIME");
        sqlQuery.setValue("ID", id, Types.VARCHAR);
        sqlQuery.setValue("LastAccess", new Timestamp(accessTime.getTime()), Types.TIMESTAMP);
        updateDatabase(sqlQuery);
    }
    

    @Override
    public void disableAuthCode(String id) throws DataAccessException {
        logger.logp(Level.INFO, "DBAuthCodeDAO", "disableAuthCode", "disableAuthCode: " + id );
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_VALIDITY");
        sqlQuery.setValue("ID", id, Types.VARCHAR);
       // sqlQuery.setValue("valid", 0 , Types.TINYINT);

        updateDatabase(sqlQuery);
    }

}
