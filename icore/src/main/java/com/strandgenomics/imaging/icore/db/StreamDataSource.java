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
 * QueryConfig.java
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * DataSource wrapper aound a stream
 * @author arunabha
 *
 */
public class StreamDataSource implements DataSource {

	private static final String DEFAULT_TYPE = "application/octet-stream";
	
	private InputStream m_src;
	
	public StreamDataSource(InputStream src){
		m_src = src;
	}
	
	public InputStream getInputStream() throws IOException 
	{
		if(m_src instanceof BufferedInputStream) return m_src;
		return new BufferedInputStream(m_src);
	}
	
	public String getContentType()
	{
        return DEFAULT_TYPE;
    }
	
	public String getName()
	{
        return null;
    }
	
	public OutputStream getOutputStream() throws IOException 
	{
        return null;
    }
}
