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
 * ConnectionPreferences.java
 *
 * AVADIS Image Management System
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

package com.strandgenomics.imaging.iclient.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import com.strandgenomics.imaging.icore.Constants;

/**
 * IMG Server connection preferences
 * @author arunabha
 *
 */
public class ConnectionPreferences implements Serializable {

	private static final long serialVersionUID = -739094926694920534L;
	
	public static final Integer DEFAULT_HTTPS_PORT = 443;
	public static final Integer DEFAULT_HTTP_PORT  = 80;
	
	private String serverAddress = null;
	/** non ssl server port */
	private Integer serverPort = null;
    /** check whether to use ssl */
    private boolean useSSL     = false;
    
	private String loginName = null;
	private transient String password = null;

	private String proxyHost = null;
	private Integer proxyPort = null;

	private String proxyUser = null;
	private String proxyPassword = null;
	
	private boolean useProxy = false;
	/**
	 * single instance of the connection information
	 */
	private static ConnectionPreferences conPreferences = null;
	private static final Object padLock = new Object();
	/**
	 * the file where the connection information will be saved
	 */
	private static File saveFile = new File(Constants.getConfigDirectory(),".imanageserver.conf");

	private ConnectionPreferences() {}
	
	/**
	 * Returns the singleton instance of the connection preferences
	 * @return the singleton instance of the connection preferences
	 */
	public static ConnectionPreferences getInstance()
	{
		if(conPreferences == null)
		{
			synchronized(padLock)
			{
				if(conPreferences == null)
				{
					//load it for serialized stuff
					conPreferences = load();
				}
			}
		}
		return conPreferences;
	}
	
    public boolean sslEnabled()
    {
        return useSSL;
    }
	
	public String getHostAddress()
	{
		return serverAddress;
	}

	public Integer getHostPort() 
	{
		return serverPort;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}
	
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	public String getLoginName() 
	{
		return loginName;
	}

	public String getPassword() 
	{
		return password;
	}

	public String setPassword(String value) 
	{
		return password = value;
	}
	
	
	public String getProxyAddress() 
	{
		return proxyHost;
	}

	public Integer getProxyPort() 
	{
		return proxyPort;
	}

	public String getProxyUser()
	{
		return proxyUser;
	}

	public String getProxyPassword() 
	{
		return proxyPassword;
	}

	public boolean toUseProxy()
	{
		return useProxy;
	}

	public boolean isConnectionPropertiesSet() 
	{
		return (serverAddress != null && loginName != null && password != null && serverPort != null);
	}

	public String toString() 
	{
		StringBuffer buffer = new StringBuffer();

		if (loginName != null) 
		{
			buffer.append(loginName);
		}

		if (serverAddress != null)
		{
			buffer.append('@');
			buffer.append(serverAddress);
		}

		buffer.append(":").append(serverPort);
		return buffer.toString();
	}

	public void setServerSettings(String hostAddress, int port, String userName, String password, boolean useSSL) 
	{
		if (hostAddress == null || userName == null || password == null) 
		{
			throw new NullPointerException("null host or user credentials");
		}

		this.serverAddress = hostAddress;
		this.loginName = userName;
		this.password = password;
		this.useSSL = useSSL;
        if(useSSL)
        {
        	serverPort = port <= 0 ? DEFAULT_HTTPS_PORT : new Integer(port);
        }
        else 
        {
            serverPort = port <= 0 ? DEFAULT_HTTP_PORT : new Integer(port);
        }
	}

	/**
	 * added to handle proxy according to Axis1.4 requirement
	 * 
	 * @see org.apache.axis.components.net.DefaultHTTPTransportClientProperties
	 * @see org.apache.axis.components.net.DefaultHTTPSTransportClientProperties
	 * @see org.apache.commons.discovery.tools.ManagedProperties
	 */
	public void setProxySettings(String proxyHost, Integer proxyPort,
			String proxyUser, String proxyPassword, boolean useProxy) {

		//System.out.println("setting proxy properties");
		// clear system properties
		clearSystemProxyProperties();
		// set appropriate member variables
		setProxyProperties(proxyHost, proxyPort, proxyUser, proxyPassword);
		// set whether to use proxy at all despite of the settings
		this.useProxy = useProxy;
		// if proxy is to be used, set appropriate system properties
		if (useProxy && proxyPort != null && proxyHost != null) 
		{
			System.out.println("setting  proxy properties");
			setProxySystemProperties();
		}
		else
		{
			clearSystemProxyProperties();
		}
	}

	private void setProxyProperties(String proxyHost, Integer proxyPort, String proxyUser, String proxyPassword) 
	{
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort == null || proxyPort <= 0 ? null : proxyPort;

		if (proxyUser != null && proxyPassword != null)
		{
			this.proxyUser     = proxyUser;
			this.proxyPassword = proxyPassword;
		} 
		else 
		{
			this.proxyUser     = null;
			this.proxyPassword = null;
		}
	}

	private void setProxySystemProperties()
	{
		if (!useProxy || proxyHost == null || proxyPort == null)
		{
			clearSystemProxyProperties();
			return;
		}

		boolean proxyAuthorization = proxyUser != null && proxyPassword != null;

        if(useSSL)
        {
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort.toString());
            //axis1.4 properties
            if(proxyAuthorization)
            {
            	System.setProperty("https.proxyUser",     proxyUser);
            	System.setProperty("https.proxyPassword", proxyPassword);
            }
            else 
            {
                System.clearProperty("https.proxyUser");
                System.clearProperty("https.proxyPassword");
            }
        }
        else
        {
			// this two are typical java.net properties
			// see http://java.sun.com/j2se/1.5.0/docs/guide/net/properties.html
        	System.setProperty("http.proxyHost", proxyHost);
        	System.setProperty("http.proxyPort", proxyPort.toString());
			
			// axis1.4 properties
			if (proxyAuthorization) 
			{
				System.setProperty("http.proxyUser", proxyUser);
				System.setProperty("http.proxyPassword", proxyPassword);
			} 
			else 
			{
				System.clearProperty("http.proxyUser");
				System.clearProperty("http.proxyPassword");
			}
        }
	}
	
    private static void clearSystemProxyProperties()
    {
		// this two are typical java.net properties
		// see http://java.sun.com/j2se/1.5.0/docs/guide/net/properties.html
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("http.proxyUser");
        System.clearProperty("http.proxyPassword");

        System.clearProperty("https.proxyHost");
        System.clearProperty("https.proxyPort");
        System.clearProperty("https.proxyUser");
        System.clearProperty("https.proxyPassword");
    }

    public static void printSystemProperties(PrintStream out)
    {
        out.println("following are the relevant system properties used for proxy settings\n");
        
        out.println("http.proxyHost="+System.getProperty("http.proxyHost"));
        out.println("http.proxyPort="+System.getProperty("http.proxyPort"));
        out.println("http.proxyUser="+System.getProperty("http.proxyUser"));
        out.println("http.proxyPassword="+System.getProperty("http.proxyPassword"));
        
        out.println("https.proxyHost="+System.getProperty("https.proxyHost"));
        out.println("https.proxyPort="+System.getProperty("https.proxyPort"));
        out.println("https.proxyUser="+System.getProperty("https.proxyUser"));
        out.println("https.proxyPassword="+System.getProperty("https.proxyPassword"));
    }

	/**
	 * Saves this object
	 */
	public void save()
    {
        ObjectOutputStream oos = null;
        try
        {
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(saveFile));
            oos = new ObjectOutputStream(outStream);
            oos.writeObject(this);
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
        finally 
        {
            try {
                oos.close();
            }
            catch(Exception ex)
            {}
        }
    }
    
    private static ConnectionPreferences load()
    {
        ObjectInputStream ois = null;
        ConnectionPreferences spec = null;

        try 
        {
            BufferedInputStream outStream = new BufferedInputStream(new FileInputStream(saveFile));
            ois = new ObjectInputStream(outStream);
            spec = (ConnectionPreferences) ois.readObject();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
            spec = new ConnectionPreferences();
        }
        finally 
        {
            try 
            {
                ois.close();
            }
            catch(Exception ex)
            {}
        }

        return spec;
    }
}
