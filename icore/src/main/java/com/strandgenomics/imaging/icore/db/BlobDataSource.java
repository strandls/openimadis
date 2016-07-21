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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * a file based datasource that can acts as a blob also
 * @author arunabha
 *
 */
public class BlobDataSource implements DataSource {

    public static final String DEFAULT_TYPE = "application/octet-stream";
    /**
     * the source file
     */
    protected File m_file;
    /**
     * the content type
     */
    protected String mimeType;

    /**
     *  Creates a BlobDataSource from a File object
     */
    public BlobDataSource(File file) throws IOException 
    {
        this(file, DEFAULT_TYPE);
    }
    
    /**
     *  Creates a BlobDataSource from a File object
     */
    public BlobDataSource(File file, String mimeType) throws IOException 
    {
        m_file = file.getCanonicalFile();
        if(!m_file.isFile() || !m_file.canRead())
        {
            throw new IOException("cannot access cache file "+file);
        }
        this.mimeType = mimeType;
    }
    
    public Blob getBlob()
    {
        return new FileBlob(m_file);
    }

    public InputStream getInputStream() throws IOException 
    {
        return new BufferedInputStream(new FileInputStream(m_file));
    }

    public OutputStream getOutputStream() throws IOException 
    {
        return null;
    }

    public String getContentType()
    {
        return mimeType;
    }

    public String getName()
    {
        return m_file.getPath();
    }
    
    public void destroy()
    {
        if(m_file != null)
        {
            try 
            {
                m_file.delete();
            }
            catch(Exception ex)
            {}
        }
        m_file = null;
    }
    
    /**
     * Called by the garbage collector on an object when garbage collection 
     * determines that there are no more references to the object. 
     * this subclass overrides the finalize method to dispose of system resources 
     * i.e., to delete the temporary cached file
     */
    protected void finalize()
    {
        destroy();
    }
    
    public static BlobDataSource createDataSource(InputStream inStream) throws IOException {
    
        //step1: first cache the data in a local temp cache file
        File cachedFile = File.createTempFile("avadis",".cache");
        cachedFile.deleteOnExit();
        
        BufferedOutputStream writer = null;
        BufferedInputStream  reader = null;
        
        try 
        {
            reader = inStream instanceof BufferedInputStream ? (BufferedInputStream)inStream : new BufferedInputStream(inStream);
            writer = new BufferedOutputStream(new FileOutputStream(cachedFile));
            Util.transferData(reader, writer, false);
        }
        finally {
            Util.closeStreams(reader, writer);
        }

        return new BlobDataSource(cachedFile);
    }
}
