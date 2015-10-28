/*
 * QueryFragment.java
 *
 * AVADIS Image Management System
 * Core Engine Database Module
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */

package com.strandgenomics.imaging.icore.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Disposable;

/**
 * Root class of all query fragments
 * @author arunabha
 *
 */
public abstract class QueryFragment implements Disposable, Cloneable {
	
	/**
	 * the logger to logs
	 */
	protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.icore.db");

    /**
     * Returns the string representation of the sql query fragment
     */
    public abstract String toQueryString();
    
    /**
     * Returns the cloned instance of this query-fragment
     */
    public abstract Object clone();

    /**
     * Set the parameter index and the corresponding value to the PreparedStatement and returns 
     * the next available parameter index. Note that the index starts with 1.
     */
    public abstract int setParameterIndexAndValue(PreparedStatement pstmt, int parameterIndex)
        throws SQLException;
    
    /**
     * support for altering queries, generally to be used for prepared statement non-parameter stuff
     */
    public abstract void setParameter(String name, String value);
    
    /**
     * support for nested and/or parameterized queries
     */
    public abstract void setParameter(String name, QueryFragment value);
    
    /**
     * set value to parameter of the specified name with the specified value and java.sql.Types
     */
    public abstract void setValue(String name, Object value, int sqlType);
    
    /**
     * set value to parameter of the specified name with the specified value, types and operator
     */
    public abstract void setValue(String name, Object value, int sqlType, Operator operator);

    /**
     * set value to parameter of the specified name with the specified value, types, operator and the forceNullUse flag
     */
	public abstract void setValue(String name, Object value, int sqlType, Operator operator, boolean forceNullUse);
}