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

/*
 * SQLQuery.java
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

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * This class represents a SQL query, can also play as a fragment of another SQL query
 * just to support nested queries
 * @author arunabha
 */
public class SQLQuery extends ComplexFragments  {

    public static final ConstantFragment UNION        = new ConstantFragment(" UNION ");
    public static final ConstantFragment UNION_ALL    = new ConstantFragment(" UNION ALL");
    public static final ConstantFragment INTERSECTION = new ConstantFragment(" INTERSECT ");

    public SQLQuery(List<QueryFragment> fragmentSeq)
    {
        super(fragmentSeq);
    }

    /**
     * Creates a new SQL query by combining the specified list of SQLQuery objects
     * either as a UNION or an INTERSECTION
     */
    public SQLQuery(SQLQuery[] queryList, boolean joinByAND)
    {
        this(queryList, joinByAND ? INTERSECTION : UNION);
    }

    /**
     * Creates a new SQL query by combining the specified list of SQLQuery objects
     */
    public SQLQuery(SQLQuery[] queryList, ConstantFragment joinFragment)
    {
    	super( toQueryFragment(queryList, joinFragment) );
    }
  

    @Override
    public Object clone()
    {
    	List<QueryFragment> temp = new ArrayList<QueryFragment>();
    	for(QueryFragment q : queryFragments)
    	{
    		temp.add( (QueryFragment) q.clone());
    	}
    	
    	return new SQLQuery(temp);
    }

    public void list(PrintStream out)
    {
        if(queryFragments != null)
        {
            for(QueryFragment fragment : queryFragments)
            {
                out.println(fragment.toString());
            }
        }
    }

    /**
     * make union of one or more other SQL queries with this SQLQuery object
     * @return a reference to this object
     */
    public SQLQuery union(SQLQuery ... queryList)
    {
        queryFragments = join(this, queryList, true);
        return this;
    }

    /**
     * make intersection with one or more other SQL queries with this SQLQuery object
     */
    public SQLQuery intersect(SQLQuery ... queryList)
    {
        queryFragments = join(this, queryList, false);
        return this;
    }

    public void addPrefix(SQLQuery prefix)
    {
        if(prefix == null) return;

        List<QueryFragment> fragments = new ArrayList<QueryFragment>();

        //first add the prefix fragments
        for(QueryFragment ithFragment : prefix.queryFragments)
        {
            fragments.add(ithFragment);
        }

        //then add this fragments
        for(QueryFragment ithFragment : this.queryFragments)
        {
            fragments.add(ithFragment);
        }

        this.queryFragments = fragments;
    }

    public void addSuffix(SQLQuery suffix) 
    {
        if(suffix == null) return;
        // just add the suffix fragments at the end
        for(QueryFragment ithFragment : suffix.queryFragments)
        {
            queryFragments.add(ithFragment);
        }
    }
    
    /**
     * Creates a raw PreparedStatement with-out filling the values
     * @param conn the connection
     * @return the PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException 
    {
    	return createPreparedStatement(conn, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }
    
    /**
     * Creates a raw PreparedStatement with-out filling the value
     * @param conn the connection
     * @return the PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement createPreparedStatement(Connection conn, int resultSetType, int resultSetConcurrency) throws SQLException 
    {
        String sqlQuery = Util.trim(toQueryString()); //trim excess white spaces
        logger.logp(Level.FINEST, "SQLQuery", "createPreparedStatement", sqlQuery);
        return conn.prepareStatement(sqlQuery, resultSetType, resultSetConcurrency);
    }

    /**
     * Creates a PreparedStatement object for sending parameterized SQL statements to
     * the database.
     * Result sets created using the returned PreparedStatement  object will by default
     * be of type ResultSet.TYPE_FORWARD_ONLY and have
     * a concurrency level of ResultSet.CONCUR_READ_ONLY.
     */
    public PreparedStatement getPreparedStatement(Connection conn) throws SQLException 
    {
        return getPreparedStatement(conn, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * Creates a PreparedStatement object that will generate ResultSet objects with the given
     * type and concurrency.
     * This method is the same as the prepareStatement method above,
     * but it allows the default result set type and concurrency to be overridden.
     * @param conn          the database connection from which the prepared statement is be be created
     * @param resultSetType          a result set type; one of ResultSet.TYPE_FORWARD_ONLY,
     *                                                         ResultSet.TYPE_SCROLL_INSENSITIVE,
     *                                                      or ResultSet.TYPE_SCROLL_SENSITIVE
     * @param resultSetConcurrency   a concurrency type; one of ResultSet.CONCUR_READ_ONLY
     *                                                       or ResultSet.CONCUR_UPDATABLE
     */
    public PreparedStatement getPreparedStatement(Connection conn, int resultSetType, int resultSetConcurrency) 
    		throws SQLException 
    {
        logger.logp(Level.FINEST, "SQLQuery", "getPreparedStatement", "connection="+conn);
        PreparedStatement pstmt = createPreparedStatement(conn, resultSetType, resultSetConcurrency);
        //populate with values
        populatePreparedStatement(pstmt);
        //return the statement
        return pstmt;
    }
    
    /**
     * Populate the PreparedStatement with values
     * @param pstmt the PreparedStatement
     * @throws SQLException
     */
    public void populatePreparedStatement(PreparedStatement pstmt) throws SQLException
    {
        //PreparedStatement parameter index start from 1
        setParameterIndexAndValue(pstmt, 1);
        //return the PreparedStatement after setting all parameter values
        
        //set the fetch size only for mysql to handle bulk data select.
        String dbType = System.getProperty("iengine.db.type");
        if(dbType != null && dbType.toLowerCase().equals("mysql"))
        {
            pstmt.setFetchSize(Integer.MIN_VALUE);
        }
        
        if(dbType != null && dbType.toLowerCase().equals("postgres"))
        {
        	String fetchSize = System.getProperty("avadis.resultset.fetchsize", "2");
            pstmt.setFetchSize(Integer.parseInt(fetchSize));
        }
    }

    /**
     * joins one or more other SQL queries with this SQLQuery object
     */
    protected List<QueryFragment> join(SQLQuery query, SQLQuery[] queryList, boolean union){

        if(queryList == null || queryList.length == 0){
            return query.queryFragments;
        }

        List<QueryFragment> combinedFragments = new ArrayList<QueryFragment>();

        combinedFragments.add(new ConstantFragment("("));
        for(QueryFragment ithFragment : query.queryFragments){
            combinedFragments.add(ithFragment);
        }
        combinedFragments.add(new ConstantFragment(")"));

        for(SQLQuery ithQuery : queryList){

            if(union)
            {
                combinedFragments.add(UNION);
            }
            else 
            {
                combinedFragments.add(INTERSECTION);
            }

            combinedFragments.add(new ConstantFragment("("));
            for(QueryFragment ithFragment : ithQuery.queryFragments)
            {
                combinedFragments.add(ithFragment);
            }
            combinedFragments.add(new ConstantFragment(")"));
        }

        return combinedFragments;
    }
    
    private static List<QueryFragment> toQueryFragment(SQLQuery[] queryList, ConstantFragment joinFragment)
    {
    	List<QueryFragment> queryFragments = new ArrayList<QueryFragment>();

        for(int i = 0;i < queryList.length; i++){

            SQLQuery ithQuery = queryList[i];

            if(i > 0)
            {
                queryFragments.add(joinFragment);
            }

            queryFragments.add(new ConstantFragment("("));
            for(QueryFragment ithFragment : ithQuery.queryFragments)
            {
                queryFragments.add(ithFragment);
            }
            queryFragments.add(new ConstantFragment(")"));
        }
        return queryFragments;
    }
}