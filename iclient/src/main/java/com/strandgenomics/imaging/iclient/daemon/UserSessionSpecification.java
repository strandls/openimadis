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

package com.strandgenomics.imaging.iclient.daemon;

import java.io.Serializable;

/**
 * The details of the user session
 * 
 * @author Anup Kulkarni
 */
public class UserSessionSpecification implements Serializable{
	/**
	 * hostname of server
	 */
	public final String host;
	/**
	 * port of the server
	 */
	public final int port;
	/**
	 * protocol
	 */
	public final boolean useSSL;
	/**
	 * access key 
	 */
	public final String accessKey;
	/**
	 * logged in user
	 */
	public final String userLogin;
	/**
	 * name of the project to which user is logged in
	 */
	public final String projectName;
	
	public UserSessionSpecification(String userLogin, String accessKey, String projectName, String host, int port, boolean useSSL)
	{
		this.userLogin = userLogin;
		this.accessKey = accessKey;
		this.projectName = projectName;
		this.host = host;
		this.port = port;
		this.useSSL = useSSL;
	}
}
