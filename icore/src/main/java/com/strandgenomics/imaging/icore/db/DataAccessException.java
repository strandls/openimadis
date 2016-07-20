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
 * DataAccessException.java
 *
 * AVADIS Image Management System
 * Data Access Components
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

import java.io.IOException;

/**
 * Many persistence mechanisms potentially generate exceptions when we perform
 * persistence operations. For example, creating a JDBC connection can generate
 * an java.sql.SQLException, and parsing XML data can generate a SAXException <br>
 * An elegant solution to this problem is to create a new generic data access
 * exception class, say, DataAccessException and have the abstract persistence
 * methods declare that they throw this generic exception instead of the special
 * exceptions. When a DAO method catches a special exception, it wraps the
 * special exception and throws it. <br>
 * DataAccessException is an exception that extends the standard Exception
 * Exception. This is thrown by the DAOs of component when there is some
 * irrecoverable error (like SQLException)
 * @author arunabha
 */
public class DataAccessException extends IOException {

	private static final long serialVersionUID = -7101155032303140429L;

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable rootCause) 
	{
		super(rootCause);
	}

	public DataAccessException(String message, Throwable rootCause) 
	{
		super(message, rootCause);
	}
}