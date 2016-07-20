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
 * DatabaseHandler.java
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Base class for Data Access Objects implemented over Database <br>
 * Each DataAccessObjects acts as accessor to specific TableModel objects <br>
 * TableModel objects can be actual or dynamic database tables <br>
 *
 * @author arunabha@strandls.com
 */
public abstract class DatabaseHandler <T extends Storable> {

    protected QueryDictionary queryDictionary = null;
    protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.db");

    /**
     * construct this DatabaseHandler with the specific factory of DataAccessObjects
     */
    public DatabaseHandler(QueryDictionary queryDictionary) throws DataAccessException 
    {
        this.queryDictionary = queryDictionary;
    }
    
    protected DatabaseHandler(){}

    /**
     * constructs appropriate data model object that represent the specified column values
     * the columns of a specific database table (real or dynamic result of a query)
     * Implementing classes is expected to define this
     * @param columnValues the column values
     * @return an instance of an appropriate row object
     */
    public abstract T createObject(Object[] columnValues);

    /**
     * fetches database connection
     */
    public abstract Connection getConnection() throws DataAccessException;
    
    /**
     * Executes the specified queries as a transaction
     * @param sqlQuery the list of queries to execute as a transaction
     * @throws DataAccessException if there is a database error
     */
    public void executeTransaction(SQLQuery ... sqlQuery) throws DataAccessException
    {
        if(sqlQuery == null || sqlQuery.length == 0)
            return;
        
        Connection conn = null;
        boolean autoCommitStatus = true;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            autoCommitStatus = conn.getAutoCommit();
            //we will commit all in one shot, transaction
            conn.setAutoCommit(false);
            
            int counter = 0;
            //fire all queries that is there to create tables
            for(SQLQuery query : sqlQuery)
            {
                //execute the SQLQuery for this key
                logger.logp(Level.INFO, "DatabaseHandler", "executeTransaction", "executing query#" +(++counter));
                pstmt = query.getPreparedStatement(conn);
                //execute the statement that returns nothingh
                pstmt.executeUpdate();
                //close the statement
                pstmt.close();
            }
            
            //make the changes permanent
            conn.commit();
        }
        catch(SQLException ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error executing transaction", ex);

            try 
            {
                if(conn != null) conn.rollback();
            }
            catch(Exception exx)
            {
                logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while doing a rollback", exx);
            }
            
            logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while creating tables", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            try 
            {
                conn.setAutoCommit(autoCommitStatus);
            }
            catch(Exception ex)
            {
                logger.logp(Level.WARNING, "DatabaseHandler", "executeTransaction", "error while setting autocommit status", ex);
            }
            //close the connection/set it free in the pool
            closeAll(null, null, conn);
        }
    }

    /**
     * fires the specified SQLQuery for ONLY updating the database
     * @param sqlQuery the sql query to fire
     * @return the status of the update
     */
    public boolean updateDatabase(SQLQuery sqlQuery) throws DataAccessException 
    {
        boolean status = false;
        try
        {
            //execute the statement
            executeUpdate(sqlQuery);
            status = true;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "fireUpdateQuery", "error while updating", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }
        return status;
    }

    /**
     * fires the specified SQLQuery for fetching appropriate TableModel from the database
     * @param sqlQuery the sql query to fire
     * @return an instance of appropriate TableModel
     */
    public T fetchInstance(SQLQuery sqlQuery) throws DataAccessException 
    {
        T row = null;
        try 
        {
            RowSet<Object[]> queryResult = executeQuery(sqlQuery);
            if(queryResult == null || queryResult.isEmpty())
            {
                return null;
            }

            Object[] zerothRow = queryResult.getRows().get(0);

            if(zerothRow != null && zerothRow.length != 0)
            {
                row = createObject(zerothRow); //though it is apparently unsafe, T is always child of TableModel
            }

            zerothRow = null;
            queryResult.destroy();
            queryResult = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "find", "error while retrieving rows", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return row;
    }
 
    /**
     * fires the specified SQLQuery for searching appropriate TableModels from the database
     * @param sqlQuery the sql query to fire
     * @return a list of appropriate TableModels
     */
    public RowSet<T> find(SQLQuery sqlQuery) throws DataAccessException 
    {
        return find(sqlQuery, 0, 0);
    }

    /**
     * fires the specified SQLQuery for searching appropriate TableModels from the database
     * @param sqlQuery the sql query to fire
     * @param offset offset for the resultset
     * @param count the number of rows to be retrieved
     * @return a list of appropriate TableModels
     */
    public RowSet<T> find(SQLQuery sqlQuery, int offset, int count) throws DataAccessException 
    {
        RowSet<T> searchResult = null;

        try 
        {
            RowSet<Object[]> result = executeQuery(sqlQuery, offset, count);
            if(result == null || result.isEmpty()){
                return null;
            }

            List<T> rowList = new ArrayList<T>();
            for(Object[] rowData : result.getRows())
            {
                T row = createObject(rowData);
                rowList.add(row);
            }

            searchResult = new RowSet<T>(result.getOffset(), result.getTotalHits(), rowList);

            result.destroy();
            result = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "find", "error while retrieving rows", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return searchResult;
    }

    /**
     * executes an already configured SQLQuery that returns data returned by the query
     * @see java.sql.PreparedStatement#executeQuery
     */
    public RowSet<Object[]> executeQuery(SQLQuery sqlQuery) throws DataAccessException 
    {
        return executeQuery(sqlQuery, 0, 0);
    }

    /**
     * executes an already configured SQLQuery that returns data returned by the query
     * @see java.sql.PreparedStatement#executeQuery
     */
    public RowSet<Object[]> executeQuery(SQLQuery sqlQuery, int offset, int count) throws DataAccessException 
    {
        Connection conn         = null;
        PreparedStatement pstmt = null;
        ResultSet rset          = null;

        try
        {
            conn  = getConnection();
            pstmt = sqlQuery.getPreparedStatement(conn);
            rset  = pstmt.executeQuery();
            //extract the data out of the result set
            return getData(rset, offset, count);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            logger.logp(Level.WARNING, "DatabaseHandler", "executeQuery", "error executing query", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally {
            closeAll(rset, pstmt, conn);
        }
    }

    /**
     * executes an already configured SQLQuery that returns nothing
     * @see java.sql.PreparedStatement#executeUpdate
     */
    public boolean executeUpdate(SQLQuery sqlQuery) throws DataAccessException 
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean status = false;

        try 
        {
            conn  = getConnection();
            pstmt = sqlQuery.getPreparedStatement(conn);
            //execute the statement that returns nothingh
            pstmt.executeUpdate();
            status = true;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "executeUpdate", "error executing query", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally {
            closeAll(null, pstmt, conn);
        }

        return status;
    }
    
    /**
     * executes an already configured SQLQuery that returns nothing
     * @see java.sql.PreparedStatement#executeUpdate
     */
    public void executeUpdate(SQLQuery sqlQuery, Connection conn) throws DataAccessException 
    {
        PreparedStatement pstmt = null;

        try {
            pstmt = sqlQuery.getPreparedStatement(conn);
            //execute the statement that returns nothingh
            pstmt.executeUpdate();
        }
        catch(Exception ex){
            logger.logp(Level.WARNING, "DatabaseHandler", "executeUpdate", "error executing query", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            closeAll(null, pstmt, null);
        }
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate value from the database
     * @param sqlQuery the sql query to fire
     * @return a long value
     */
    public Object getObject(SQLQuery sqlQuery) throws DataAccessException {

        Object value = null;
        try {
            RowSet<Object[]> result = executeQuery(sqlQuery);

            if(result == null || result.isEmpty()){
                return null;
            }

            Object[] answer = result.getRows().get(0);
            if(answer == null || answer.length == 0 || answer[0] == null) {
                value = null;
            }
            else 
            {
                value = answer[0];
            }
            answer = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "getObject", "error while retrieving rows", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return value;
    }

    /**
     * fires the specified SQLQuery for fetching appropriate
     * integer value from the database
     * @param sqlQuery the sql query to fire
     * @return a integer value
     */
    public Integer getInteger(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return Util.getInteger(getObject(sqlQuery));
    }

    /**
     * fires the specified SQLQuery for fetching appropriate
     * long value from the database
     * @param sqlQuery the sql query to fire
     * @return a long value
     */
    public Long getLong(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return Util.getLong(getObject(sqlQuery));
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * float value from the database
     * @param sqlQuery the sql query to fire
     * @return a float value
     */
    public Float getFloat(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return Util.getFloat(getObject(sqlQuery));
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * float value from the database
     * @param sqlQuery the sql query to fire
     * @return a float value
     */
    public Boolean getBoolean(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return Util.getBoolean(getObject(sqlQuery));
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * double value from the database
     * @param sqlQuery the sql query to fire
     * @return a double value
     */
    public Double getDouble(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return Util.getDouble(getObject(sqlQuery));
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * string value from the database
     * @param sqlQuery the sql query to fire
     * @return a String value
     */
    public String getString(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return (String) getObject(sqlQuery);
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * time-stamp value from the database
     * @param sqlQuery the sql query to fire
     * @return a timestamp value
     */
    public Timestamp getTimestamp(SQLQuery sqlQuery) throws DataAccessException 
    {
    	return Util.getTimestamp(getObject(sqlQuery));
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * array of long value from the database
     * @param sqlQuery the sql query to fire
     * @return an array of Long values
     */
    public long[] getRowsWithLongValues(SQLQuery sqlQuery) throws DataAccessException {

        long[] valueList = null;
        try {
            RowSet<Object[]> result = executeQuery(sqlQuery);

            if(result == null || result.isEmpty()){
                return null;
            }

            List<Object[]> rowList = result.getRows();
            if(rowList == null || rowList.isEmpty()) {
                ;
            }
            else 
            {
                valueList = new long[rowList.size()];
                for(int i = 0;i < valueList.length; i++)
                {
                    valueList[i] = Util.getLong(rowList.get(i)[0]);
                }
            }
            rowList = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "getRowsWithLongValues", "error while retrieving rows", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return valueList;
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * array of long value from the database
     * @param sqlQuery the sql query to fire
     * @return an array of Long values
     */
    public int[] getRowsWithIntValues(SQLQuery sqlQuery) throws DataAccessException {

        int[] valueList = null;
        try {
            RowSet<Object[]> result = executeQuery(sqlQuery);

            if(result == null || result.isEmpty()){
                return null;
            }

            List<Object[]> rowList = result.getRows();
            if(rowList == null || rowList.isEmpty()) {
                ;
            }
            else 
            {
                valueList = new int[rowList.size()];
                for(int i = 0;i < valueList.length; i++)
                {
                    valueList[i] = Util.getInteger(rowList.get(i)[0]);
                }
            }
            rowList = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "getRowsWithLongValues", "error while retrieving rows", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return valueList;
    }

    /**
     * fires the specified SQLQuery for fetching appropriate
     * result. assumes all the returned columns are ints and converts the same to an int array.
     * Also only the first result is converted and returned
     * 
     * @param sqlQuery the sql query to fire
     * @return an array of Long values
     */
    public int[] getColumnsWithIntValues(SQLQuery sqlQuery) throws DataAccessException {

        int[] valueList = null;
        try {
            RowSet<Object[]> result = executeQuery(sqlQuery);

            if(result == null || result.isEmpty()){
                return null;
            }

            List<Object[]> rowList = result.getRows();
            if(rowList == null || rowList.isEmpty()) {
                ;
            }
            else 
            {
                valueList = new int[rowList.get(0).length];
                for(int i = 0;i < rowList.get(0).length; i++)
                {
                    valueList[i] = Util.getInteger(rowList.get(0)[i]);
                }
            }
            rowList = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "getColumnsWithIntValues", "error while retrieving columns", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return valueList;
    }
    
    /**
     * fires the specified SQLQuery for fetching appropriate
     * result. assumes all the returned columns are longs and converts the same to an long array.
     * Also only the first result is converted and returned
     * 
     * @param sqlQuery the sql query to fire
     * @return an array of Long values
     */
    public long[] getColumnsWithLongValues(SQLQuery sqlQuery) throws DataAccessException {

        long[] valueList = null;
        try {
            RowSet<Object[]> result = executeQuery(sqlQuery);

            if(result == null || result.isEmpty()){
                return null;
            }

            List<Object[]> rowList = result.getRows();
            if(rowList == null || rowList.isEmpty()) {
                ;
            }
            else 
            {
                valueList = new long[rowList.get(0).length * rowList.size()];
                int cnt = 0;
                for(int j=0;j<rowList.size();j++)
                {
                	for(int i = 0;i < rowList.get(j).length; i++)
                    {
                        valueList[cnt] = Util.getLong(rowList.get(j)[i]);
                        cnt++;
                    }
                }
            }
            rowList = null;
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "getColumnsWithLongValues", "error while retrieving columns", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return valueList;
    }

    /**
     * fires the specified SQLQuery for fetching appropriate
     * array of long value from the database
     * @param sqlQuery the sql query to fire
     * @return an array of Long values
     */
    public String[] getRowsWithStringValues(SQLQuery sqlQuery) throws DataAccessException {

        String[] valueList = null;
        try {
            RowSet<Object[]> result = executeQuery(sqlQuery);

            if(result == null || result.isEmpty()){
                return null;
            }

            List<Object[]> rowList = result.getRows();
            if(rowList == null || rowList.isEmpty()) {
                ;
            }
            else {
                valueList = new String[rowList.size()];
                for(int i = 0;i < valueList.length; i++){
                    valueList[i] = (String) rowList.get(i)[0];
                }
            }
            rowList = null;
        }
        catch(Exception ex){
            logger.logp(Level.WARNING, "DatabaseHandler", "getRowsWithStringValues", "error while retrieving rows", ex);
            throw new DataAccessException(ex.getMessage());
        }
        finally 
        {
            sqlQuery.dispose();
        }

        return valueList;
    }

    /**
     * Returns the spreadsheet data represented by the specified ResultSet
     * as an array of two dimensional objects
     */
    public RowSet<Object[]> getData(ResultSet rs) throws SQLException, IOException 
    {
        return getData(rs, 0, 0);
    }

    /**
     * Returns the spreadsheet data represented by the specified ResultSet
     * as an array of two dimensional objects
     */
    public RowSet<Object[]> getData(ResultSet rs, int offset, int count) throws SQLException, IOException {

        // Get the ResultSetMetaData.  This will be used for the column headings
        ResultSetMetaData rsmd = rs.getMetaData();
        // Get the number of columns in the result set
        int numCols = rsmd.getColumnCount();

        // Get the dataTypes
        int[] dataTypes = new int[numCols];
        for(int i = 0;i < numCols; i++) 
        { // the first column is 1, the second is 2, ...
            dataTypes[i] = rsmd.getColumnType(i+1);
        }

        boolean more = false;
        int totalHits = 0;

        // Display data, fetching until end of the result set
        // in case offset and count are not 0, fetch data accordingly
        for(int i = 0; i <= offset; i++)
        {
            more = rs.next();

            if(!more) {
                return null;
            }
            else{
                totalHits++;
            }
        }

        List<Object[]> rowList = new ArrayList<Object[]>();
        int noOfRowsSelected = 0;

        while(more) { // another row is found in the result

            // column data for this row
            Object[] columns = new Object[numCols];

            // Loop through each column for this row, getting the appropriate Data
            for(int i = 0;i < numCols; i++) {

                switch(dataTypes[i]){

                    case Types.CHAR :
                        columns[i] = rs.getString(i+1);
                        break;

                    case Types.VARCHAR :
                        columns[i] = rs.getString(i+1);
                        break;

                    case Types.LONGVARCHAR : // for MEMO field
                        columns[i] = getStringValue(rs.getCharacterStream(i+1));
                        break;

                    case Types.LONGVARBINARY :
                        columns[i] = createDataSource(rsmd.getColumnName(i+1), rs.getBinaryStream(i+1));
                        break;

                    case Types.BLOB :
                        columns[i] = createDataSource(rsmd.getColumnName(i+1), rs.getBinaryStream(i+1));
                        break;

                    case Types.CLOB :        //Supported in JDBC 2.0
                        columns[i] = getStringValue(rs.getCharacterStream(i+1));
                        break;

                    case Types.TIMESTAMP : //generic SQL type TIMESTAMP = 93
                        columns[i] = rs.getTimestamp(i+1);
                        break;

                    case Types.TIME : //generic SQL type TIME = 92
                        columns[i] = rs.getTime(i+1);
                        break;

                    case Types.DATE : //generic SQL type DATE= 91
                        columns[i] = rs.getDate(i+1);
                        break;

                    case Types.FLOAT : //generic SQL type FLOAT = 6
                    {
                        float value = rs.getFloat(i+1); //will return 0 in case of NULL
                        if(rs.wasNull()){
                            columns[i] = null;
                        }
                        else {
                            columns[i] = new Float(value);
                        }
                        break;
                    }
                    case Types.DOUBLE : //generic SQL type DOUBLE = 8
                    {
                        double value = rs.getDouble(i+1); //will return 0 in case of NULL
                        if(rs.wasNull()){
                            columns[i] = null;
                        }
                        else {
                            columns[i] = new Double(value);
                        }
                        break;
                    }
                    case Types.INTEGER : //generic SQL type INTEGER = 4
                    {
                        int value = rs.getInt(i+1); //will return 0 in case of NULL
                        if(rs.wasNull()){
                            columns[i] = null;
                        }
                        else {
                            columns[i] = new Integer(value);
                        }
                        break;
                    }
                    // otherwise for each supported data Types, get the Java field value
                    default:
                        columns[i] = rs.getObject(i+1);
                        break;
                }
            }

            rowList.add(columns);
            noOfRowsSelected++;

             // Fetch the next result set row
            more = rs.next();
            if(more){
                totalHits++;
            }

            //check if limit is specified
            if(count > 0){
                //check if the number of rows of data exceeds the limit
                if(noOfRowsSelected == count){
                    break;
                }
            }

        }

        //post data read operation beyond the window,
        //to calculate the toal number of hits
        while(rs.next ()){
            totalHits++;
        }

        return new RowSet<Object[]>(offset, totalHits, rowList);
    }
    
    /**
     * The default implementation is a memory based datasource
     * @param inStream the source Stream
     * @return the data source
     */
    protected DataSource createDataSource(String name, InputStream inStream) throws IOException
    {
    	BufferedInputStream reader = null;
    	ByteArrayOutputStream sink = null;
    	long dataLength = 0;
    	
    	try 
        {
            reader = inStream instanceof BufferedInputStream ? (BufferedInputStream)inStream : new BufferedInputStream(inStream);
            sink = new ByteArrayOutputStream();
            dataLength = Util.transferData(reader, sink, false);
        }
        finally 
        {
            Util.closeStreams(reader, sink);
        }
    	
    	return dataLength <= 0 ? null : new MemoryDataSource(name, sink);
    }

    public void closeAll(ResultSet result, Statement stmt, Connection conn) 
    {
        try 
        {
            if(result != null) result.close();
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "closeAll", "resultset closing error", ex);
        }

        try 
        {
            if(stmt != null) stmt.close();
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "closeAll", "statement closing error", ex);
        }

        try 
        {
            if(conn != null) conn.close();
        }
        catch(Exception ex)
        {
            logger.logp(Level.WARNING, "DatabaseHandler", "closeAll", "connection closing error", ex);
        }
    }

    protected byte[] getBinaryValue(InputStream inStream){

        if(inStream == null) return null;

        byte[] value = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(8192); //8k
        BufferedInputStream   reader     = new BufferedInputStream(inStream);

        byte[] buffer = new byte[256];
        try {
            while(true){
                int length = reader.read(buffer, 0, buffer.length);
                if(length == -1) break;
                byteBuffer.write(buffer, 0, length);
            }

            value = byteBuffer.toByteArray();
        }
        catch(Exception ex){
            logger.logp(Level.WARNING, "DatabaseHandler", "getBinaryValue", ex.getMessage());
        }
        finally {
            buffer = null;
            try {
                if(reader != null) reader.close();
                reader = null;
            }
            catch(Exception ex){
            }
            try {
                if(byteBuffer != null) byteBuffer.close();
                byteBuffer = null;
            }
            catch(Exception ex){
            }
        }

        return value;
    }

    protected byte[] getBinaryValue(InputStream inStream, int size){

        if(inStream == null) return null;

        byte[] value = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(8192); //8k

        byte[] buffer = new byte[256];
        int remaining = size;
        try {
            while(remaining > 0) {
                int lengthToRead = remaining < buffer.length ? remaining : buffer.length;
                int length = inStream.read(buffer, 0, lengthToRead);
                if(length == -1) break;
                remaining -= length;
                byteBuffer.write(buffer, 0, length);
            }

            value = byteBuffer.toByteArray();
        }
        catch(Exception ex){
            logger.logp(Level.WARNING, "DatabaseHandler", "getBinaryValue", ex.getMessage());
        }
        finally {
            buffer = null;
            try {
                if(byteBuffer != null) byteBuffer.close();
                byteBuffer = null;
            }
            catch(Exception ex){
            }
        }

        return value;
    }

    protected String getStringValue(Reader inStream) {

        if(inStream == null) return null;

        StringBuffer writerBuffer = new StringBuffer(256);
        BufferedReader     reader = null;

        char[] buffer = new char[256];
        try {
            reader = new BufferedReader(inStream);

            while(true){
                int length = reader.read(buffer, 0, buffer.length);
                if(length == -1) break;
                writerBuffer.append(buffer, 0, length);
            }
        }
        catch(Exception ex){
            logger.logp(Level.WARNING, "DatabaseHandler", "getStringValue", ex.getMessage());
        }
        finally {
            buffer = null;
            try {
                if(reader != null) reader.close();
                reader = null;
            }
            catch(Exception ex){
            }
        }

        String value = writerBuffer.toString();
        writerBuffer = null;
        return value;
    }
}
