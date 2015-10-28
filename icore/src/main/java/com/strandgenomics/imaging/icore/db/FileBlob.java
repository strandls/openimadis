
/* @(#)FileBlob.java
 *
 * Project Enterprise File System
 * Module Data Access Layer
 *
 * Copyright 2005-2006 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * A mapping in the Java programming language of an SQL 
 * <code>BLOB</code> value. 
 * @author arunabha
 */
public class FileBlob implements Blob {

    private File m_srcFile = null;

    public FileBlob(File source) {
        m_srcFile = source;
    }
       
    /**
     * Copies the specified number of bytes, starting at the given
     * position, from this <code>FileBlob</code> object to 
     * another array of bytes.
     * <P>
     * Note that if the given number of bytes to be copied is larger than
     * the length of this <code>FileBlob</code> object's array of
     * bytes, the given number will be shortened to the array's length.
     *
     * @param pos the ordinal position of the first byte in this
     *            <code>FileBlob</code> object to be copied;
     *            numbering starts at <code>1</code>; must not be less 
     *            than <code>1</code> and must be less than or equal
     *            to the length of this <code>FileBlob</code> object
     * @param length the number of bytes to be copied 
     * @return an array of bytes that is a copy of a region of this
     *         <code>FileBlob</code> object, starting at the given
     *         position and containing the given number of consecutive bytes
     * @throws SQLException if the given starting position is out of bounds
     */
    public byte[] getBytes(long pos, int length) throws SQLException {
        
        long fileLength = m_srcFile.length();
        
        if (length > fileLength) {
            length = (int)fileLength;                
        }

        if (pos < 1 || length - pos < 0 ) {
            throw new SQLException("Invalid arguments: position cannot be less that 1");
        }      
        
        pos--; // correct pos to file pointer index
        
        byte[] data = null;
        RandomAccessFile reader = null;
        
        try {
            reader = new RandomAccessFile(m_srcFile, "r");
            reader.seek(pos);
            
            data = new byte[length];

            int bufferSize = 1024*16;
            if(length < bufferSize){
                bufferSize = length;
            }

            byte[] buffer = new byte[bufferSize];
            int counter = 0;

            while(counter < length){
                int noOfBytesRead = reader.read(buffer, 0, buffer.length);
                int noOfNeededBytes = length - counter;

                if(noOfBytesRead > noOfNeededBytes){
                    noOfBytesRead = noOfNeededBytes;
                }

                System.arraycopy(buffer, 0, data, counter, noOfBytesRead);
                counter += noOfBytesRead; 
            }
        }
        catch(IOException ex){
            throw new SQLException(ex.getMessage());
        }
        finally {
            try {
                reader.close();
            }
            catch(Exception exx)
            {}
        }
      
        return data;
    }
    
    public void free(){
        try {
            m_srcFile.delete();
            m_srcFile = null;
        }
        catch(Exception ex)
        {}
    }
    
    /**
     * Retrieves the number of bytes in this <code>FileBlob</code>
     * object's internal file.
     *
     * @return a <code>long</code> indicating the length in bytes of this
     *         <code>FileBlob</code> object's internal file
     * @throws SQLException if an error occurs
     */
    public long length() throws SQLException {
        return m_srcFile.length();
    }

    /**
     * Returns an InputStream object that contains a partial Blob value, 
     * starting with the byte specified by pos, which is length bytes in length.
     */
    public InputStream getBinaryStream(long pos,long length) throws SQLException {
        throw new SQLException("Unsupported operation");
    }

    /**
     * Returns this <code>FileBlob</code> object as an input stream.
     * @return a <code>java.io.InputStream</code> object that contains
     *         this <code>FileBlob</code> object's array of bytes
     * @throws SQLException if an error occurs     
     * @see #setBinaryStream
     */
    public InputStream getBinaryStream() throws SQLException {            
        InputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(m_srcFile));
        }
        catch(Exception ex)
        {}
        return inStream;
    }
    
    public long position(byte[] pattern, long start) throws SQLException {                
        throw new SQLException("Unsupported operation");
    }
        
    public long position(Blob pattern, long start) throws SQLException {
        throw new SQLException("Unsupported operation");
    }
    
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new SQLException("Unsupported operation, FileBlob is not writable");
    }
    
    public int setBytes(long pos, byte[] bytes, int offset, int length) throws SQLException {
        throw new SQLException("Unsupported operation, FileBlob is not writable");     
    }
    
    public OutputStream setBinaryStream(long pos)  throws SQLException {       
        throw new SQLException("Unsupported operation, FileBlob is not writable");     
    }
    
    public void truncate(long length) throws SQLException {
         throw new SQLException("Unsupported operation, FileBlob is not writable");
    }
}
