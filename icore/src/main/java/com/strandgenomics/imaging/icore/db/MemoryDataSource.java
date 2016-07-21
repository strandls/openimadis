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

package com.strandgenomics.imaging.icore.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

 /**
  * The DataSource interface provides the JavaBeans Activation Framework
  * with an abstraction of an arbitrary collection of data.
  * It provides a type for that data as well as access to it in the form of
  * InputStreams and OutputStreams where appropriate. <br>
  *
  * MemoryDataSource is a memory based data source
  */
public class MemoryDataSource implements DataSource {

    private String m_name = null;
    private ByteArrayOutputStream m_sink = null;

    public MemoryDataSource(String name, ByteArrayOutputStream data) 
    {
    	m_name = name;
        m_sink = data;
    }
    
    /**
     * This method returns the MIME type of the data in the form of a string.
     */
    public String getContentType()
    {
        return "application/octet-stream";
    }
    
    /**
     * additional method to determine the size of the data source
     */
    public int getContentLength(){
        return m_sink.size();
    }

    /**
     * This method returns an InputStream representing the data and
     * throws the appropriate exception if it can not do so.
     */
    public InputStream getInputStream() throws IOException 
    {
        return new ByteArrayInputStream(m_sink.toByteArray());
    }

    /**
     * Return the name of this object where the name of the object is
     * dependant on the nature of the underlying objects.
     * in our case, it is the name of the remote file
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * This method returns an OutputStream where the data can be written and
     * throws the appropriate exception if it can not do so.
     */
    public OutputStream getOutputStream() throws IOException 
    {
        return m_sink;
    }
}