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
 * MultiPartDataHandler.java
 *
 * Product:  faNGS
 * Next Generation Sequencing
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
package com.strandgenomics.imaging.iserver.services.impl.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


/**
 * This class implements the machanism to support Form-based File Upload from HTML supported browsers<br>
 * <a href="http://www.faqs.org/rfcs/rfc1867.html">rfc1867</a> <br>
 * <a href="http://www.faqs.org/rfcs/rfc2388.html">rfc2388</a> <br>
 * @author Arunabha Ghosh
 * @version Dec, 2005
 */
public class MultiPartDataHandler {

    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    
    protected String m_charEncoding  = null;

    protected byte[] m_boundary      = null;
    protected byte[] m_endBoundary   = null;
    protected File m_rootCacheFolder = null;
    protected InputStream m_reader   = null;


    /**
     * Creates a Form-based MultiPart Data Handler for reading the uploaded
     * data send through HTTP POST method from a HTML Form
     * @param reader     the request stream to read the content of the POST data
     * @param boundary   the multipart form-data boundary used to separate the data units, is obtained from the HTTP Request Header
     * @param rootFolder the root folder where the uploaded files are stored
     */
    public MultiPartDataHandler(InputStream reader, String boundary, String charEncoding, File rootFolder) throws UnsupportedEncodingException {

        m_charEncoding    = charEncoding == null ? DEFAULT_CHARSET : charEncoding;
        m_reader          = reader;
        m_boundary        = boundary.getBytes(m_charEncoding);
        //the end boundary has an extra "--" at the end
        m_endBoundary     = (boundary + "--").getBytes(m_charEncoding);
        m_rootCacheFolder = rootFolder;
    }

    /**
     * Downloads the various content of the HTTP POST data from the Request InputStream
     * and returns the name Vs Value Map. <br>
     * For File upload, the value is of type java.lang.File, otherwise it is just a String
     * All downloaded files are stored in the specified rootFolder
     */
    public Map extractMultipartData() throws IOException, ParseException {

        Logger logger = Logger.getLogger("com.strandgenomics.enterprise.dataserver.remotefilesystem.servlets");

        Map formDataMap = new HashMap();

        byte[] buffer = new byte[4096];
        int bytesRead = readLine(m_reader, buffer, 0, buffer.length);
        //System.out.println("first line length "+bytesRead);

        //extra two char for \r\n is also read into the buffer
        if(compareBytes(m_boundary, buffer)){
            logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "start boundary found");
        }
        else {
            logger.logp(Level.WARNING, "MultiPartDataHandler", "extractMultipartData", "ERROR, start boundary not found");
            throw new ParseException("start boundary not found", 0);
        }

        for(int counter = 0; true; counter++){

            //realine also returns the \r\n pair
            bytesRead = readLine(m_reader, buffer, 0, buffer.length);
            String contentDisposition = new String(buffer,0, bytesRead-2, m_charEncoding);

            //Content-Disposition: form-data; name="author"
            if(!contentDisposition.startsWith("Content-Disposition:")){
                logger.logp(Level.WARNING, "MultiPartDataHandler", "extractMultipartData", "ERROR, expected a contentDisposition ");
                throw new ParseException("expected a contentDisposition", counter);
            }

            String value = contentDisposition.substring("Content-Disposition:".length());
            //System.out.println(value);

            String[] data = getTokensFromEntry(value, ";");

            if(data.length == 2){
                String name = trimQuotes(getTokensFromEntry(data[1], "=")[1]);
                //System.out.println("name = "+name);

                readLine(m_reader, buffer, 0, buffer.length); //empty line delimiter
                //read the value
                bytesRead = readLine(m_reader, buffer, 0, buffer.length);

                String nameValue = new String(buffer,0, bytesRead-2, m_charEncoding);
                //System.out.println("value = "+nameValue);

                formDataMap.put(name, nameValue);

                bytesRead = readLine(m_reader, buffer, 0, buffer.length);

                if(compareBytes(m_endBoundary, buffer)) {
                    logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "end of multipart data");
                    break;
                }
                else if(compareBytes(m_boundary, buffer)){
                    logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "found another boundary");
                }
                else {
                    throw new ParseException("expected a boundary", counter);
                }
            }
            else if(data.length == 3){

                String name     = trimQuotes(getTokensFromEntry(data[1], "=")[1]);
                String filename = trimQuotes(getTokensFromEntry(data[2], "=")[1]);

                logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "name     = "+name);
                logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "filename = "+filename);

                bytesRead = readLine(m_reader, buffer, 0, buffer.length);
                String contentType = new String(buffer,0, bytesRead-2, m_charEncoding);
                logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "contentType="+contentType);

                readLine(m_reader, buffer, 0, buffer.length); //empty line delimiter

                logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "found file "+filename);

                //take only the name of the file, instead of the path (IE sends the path also)

                int lastIndex = filename.lastIndexOf('\\');
                if(lastIndex != -1){
                    filename = filename.substring(lastIndex+1);
                }

                lastIndex = filename.lastIndexOf('/');
                if(lastIndex != -1){
                    filename = filename.substring(lastIndex+1);
                }

                File uploadedFile = null;
                filename = filename == null ? null : filename.trim();

                BufferedOutputStream fileWriter = null;
                if(filename != null && filename.length() > 0){
                    uploadedFile = new File(m_rootCacheFolder, filename);
                    formDataMap.put(name, uploadedFile);
                    fileWriter = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                    logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "saving uploaded file into "+uploadedFile);
                }

                boolean endBoundaryFound = saveUploadedFile(fileWriter);
                logger.logp(Level.INFO, "MultiPartDataHandler", "extractMultipartData", "successfuly saved uploaded file ");
                try {
                    fileWriter.close();
                }
                catch(Exception ex)
                {}

                if(endBoundaryFound) break;
            }
        }

        return formDataMap;
    }

    private boolean saveUploadedFile(OutputStream fileWriter) throws IOException {

        boolean endBoundaryFound = false;
        byte[] buffer = new byte[1024*8];

        byte[] CRLF = {(byte)'\r',(byte)'\n'};
        boolean crlfNotWritten = false;

        Logger logger = Logger.getLogger("com.strandgenomics.enterprise.dataserver.remotefilesystem.servlets");

        for(int i = 0; true; ++i){

            int bytesRead = readLine(m_reader, buffer, 0, buffer.length);
            if(bytesRead == -1) throw new IOException("unexpected end of stream");

            if(bytesRead-2 == m_boundary.length){ //extra 2 char are read for the \r\n
                if(compareBytes(m_boundary, buffer)){
                    logger.logp(Level.INFO, "MultiPartDataHandler", "saveUploadedFile", "boundary found,...");
                    break; //boundary found
                }
            }

            if(bytesRead-2 == m_endBoundary.length){

                if(compareBytes(m_endBoundary, buffer)){
                    endBoundaryFound = true;
                    logger.logp(Level.INFO, "MultiPartDataHandler", "saveUploadedFile", "end boundary found,...");
                    break; //end boundary found
                }
            }

            //this chunk is not a boundary, so write it in the file
            //the file is terminated by a \r\n pair followed by a boundary
            //so don't write the \r\n pair the first time you get it but in the next loop

            if(fileWriter != null) {
                if(crlfNotWritten){
                    //System.out.println("writing crlf"+i);
                    //write the previous unwritten CRLF
                    fileWriter.write(CRLF, 0, 2);
                    crlfNotWritten = false;
                }

                if(buffer[bytesRead-2] == CRLF[0] && buffer[bytesRead-1] == CRLF[1]){
                    //this chunk contains a terminal CRLF, so delay writing it till the next loop
                    fileWriter.write(buffer, 0, bytesRead-2);
                    crlfNotWritten = true;
                    //System.out.println("delaying writing crlf"+i);
                }
                else {
                    //this chunk do not contain any CRLF, so write all
                    fileWriter.write(buffer, 0, bytesRead);
                    crlfNotWritten = false;
                }
            }
        }

        buffer = null;
        return endBoundaryFound;
    }
    
    /////////////////////////////////////////////////////
    /////////////////// HELPER METHODS //////////////////
    /////////////////////////////////////////////////////
    
    public static boolean isMulipartFormData(HttpServletRequest req){
        String cType = req.getContentType();

        if(cType == null)
            return false;
        if(cType.toLowerCase().startsWith(MULTIPART_FORM_DATA))
            return true;

        return false;
    }
    
    public static String getMultipartFormBoundary(HttpServletRequest req){

        String boundaryStr = null;
        Logger logger = Logger.getLogger("com.strandgenomics.enterprise.dataserver.remotefilesystem.servlets");

        try {
            //typically, the header field looks like
            //multipart/form-data; boundary=---------------------------2406270818230
            String cType = req.getHeader("Content-Type");
            String ccType = cType.toLowerCase();

            if(ccType.startsWith("multipart/form-data")){
                int startIndex = ccType.indexOf("boundary");

                String boundryValue = cType.substring(startIndex);
                int equalsToIndex = boundryValue.indexOf('=');
                boundaryStr = boundryValue.substring(equalsToIndex+1).trim();

                // The real boundary is always preceeded by an extra "--"
                boundaryStr = "--"+boundaryStr;
            }
        }
        catch(Exception ex){
            logger.logp(Level.WARNING, "Helper", "getMultipartFormBoundary", "expecting multipart/form-data only", ex);
        }

        logger.logp(Level.INFO, "Helper", "getMultipartFormBoundary", boundaryStr);
        return boundaryStr;
   }
    
    /**
     * Reads the input stream, one line (terminated by a \r\n combination) at a time.
     * Starting at an offset, reads bytes into an array,
     * until it reads a certain number of bytes or reaches a newline character,
     * which it reads into the array as well.
     * This method returns -1 if it reaches the end of the input stream
     * before reading the maximum number of bytes.
     * @return an integer specifying the actual number of bytes read,
     *         or -1 if the end of the stream is reached
     */
    public static final int readLine(InputStream reader, byte[] buffer, int offset, int length) throws IOException {

        boolean crFound = false;
        int counter = 0;

        for(int i = offset;i < length; ++i){

            int data = reader.read();
            if(data == -1) {
                if(counter == 0) counter = -1;
                break; //error
            }

            buffer[i] = (byte) data;
            ++counter;

            char c = (char) data;
            if(c == '\r'){
                crFound = true;
            }
            else {
                if(c == '\n'){
                    if(crFound){
                        break; //found a /r/n combination
                    }
                }
                crFound = false;
            }
        }

        return counter;
    }
    
    /**
     * compares boundaryBytes.length of bytes in the buffer bytes
     * If identical retun true, false otherwise
     */
    public static final boolean compareBytes(byte[] boundaryBytes, byte[] buffer){
        boolean status = true;
        try {
            for(int i = 0; i < boundaryBytes.length; ++i){
                if(boundaryBytes[i] != buffer[i]){
                    status = false;
                    break;
                }
            }
        }
        catch(Exception ex){
            status = false;
        }
        return status;
    }
    
    public static final String[] getTokensFromEntry(String entry, String delim) {

        if(entry == null) return null;

        Vector tokenList = new Vector();
        try {
            StringTokenizer tokenizer = new StringTokenizer(entry, delim);

            while(tokenizer.hasMoreTokens()){
                String value = tokenizer.nextToken().trim();
                if(value.length() != 0) {
                    tokenList.add(value);
                }
            }
            tokenizer = null;
        }
        catch(Exception e) {
            System.err.println("Error while tokenizing string: " +e);
        }

        String[] tokens = null;
        if(!tokenList.isEmpty()){
            tokens = new String[tokenList.size()];
            tokenList.copyInto(tokens);
        }
        return tokens;
    }
    
    public static final String trimQuotes(String name){
        if(name.startsWith("\"") && name.endsWith("\"")){
            name = name.substring(1,name.length()-1);
        }
        return name;
    }
}