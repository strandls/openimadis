/*
 * QueryDictionary.java
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

import java.util.Map;
import java.util.Set;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * A dictionary of SQL queries
 * @author arunabha
 *
 */
public class QueryDictionary {

    protected Map<String, QueryFormat> m_queryTable = null;

    /**
     * default constractor, needed for XML Bean Serializer
     */
    public QueryDictionary() {}

    public QueryDictionary(Map<String, QueryFormat> queryTable){
        setQueryTable(queryTable);
    }
    
    /**
     * returns all registered query keys within this dictionary
     */
    public Set<String> getQueryKeys()
    {
        return m_queryTable.keySet();
    }

    /**
     * Get method for XML Bean Serializer
     */
    public Map<String, QueryFormat> getQueryTable()
    {
        return m_queryTable;
    }

    /**
    * Set Method for XML Bean Serializer
    */
    public void setQueryTable(Map<String, QueryFormat> table)
    {
        m_queryTable = table;
    }

    public QueryFormat getQueryFormat(String methodName)
    {
        return m_queryTable.get(methodName);
    }

    /**
     * SQLQuery is basically a clone of QueryFormat as far as the encapsulated data
     * is concerned
     */
    public SQLQuery createQueryGenerator(String methodName)
    {
        QueryFormat queryFormat = m_queryTable.get(methodName);
        return queryFormat.createQueryGenerator();
    }

    public static void main(String[] args) throws Exception 
    {
        QueryDictionary config = (QueryDictionary) Util.unmarshallFromXML(new java.io.File(args[0]), "UTF-8");
        System.out.println(config);
    }
}