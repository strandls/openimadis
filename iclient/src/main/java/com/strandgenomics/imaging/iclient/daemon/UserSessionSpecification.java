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
