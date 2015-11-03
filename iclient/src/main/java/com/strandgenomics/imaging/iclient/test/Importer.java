/*
 * Importer.java
 *
 * AVADIS Image Management System
 * Web Service Test programs
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
package com.strandgenomics.imaging.iclient.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;

public class Importer {
	
	private String userName;
	private int port;
	private String host;
	private String password;
	
	public Importer(String host, int port, String userLogin, String password)
	{
		this.password = password;
		this.port = port;
		this.host = host;
		this.userName = userLogin;
	}
	
	public void login()
	{
		ImageSpaceObject.getImageSpace().login(false, host, port, userName, password);
	}
	
	
	public Project getProject(String projectName) 
	{
		List<Project> activeProjects = ImageSpaceObject.getConnectionManager().getActiveProjects();
		for(Project p : activeProjects)
		{
			if(p.getName().equals(projectName))
			{
				return p;
			}
		}
		return null;
	}

	public static void main(String ... args) throws IOException
	{
		if(args == null || args[0].equals("--help") || args[0].equals("?"))
		{
			System.out.println("Usage:java -Xmx1024m -cp <classpath> com.strandgenomics.imaging.iclient.test.Importer -h<server-ip> -p<server-port> -u<user-login> -P<user-password> -j<project-name> -f<index file or folder with quotes> [-r ( recursive indexing)] [ -m (for auto merge)]" );
		}
		
		String host = null;
		int port = 8080;
		
		String userLogin = null;
		String password = null;
		
		String indexFolder = null;
		String projectName = null;
		
		boolean recursive = false;
		boolean autoMerge = false; 
		
		for(String arg : args)
		{
			if(arg.startsWith("-h"))
			{
				host = arg.substring(2);
				continue;
			}
			
			if(arg.startsWith("-p"))
			{
				port = Integer.parseInt(arg.substring(2));
				continue;
			}
			
			if(arg.startsWith("-u"))
			{
				userLogin = arg.substring(2);
				continue;
			}
			
			if(arg.startsWith("-P"))
			{
				password = arg.substring(2);
				continue;
			}
			
			if(arg.startsWith("-j"))
			{
				projectName = arg.substring(2);
				continue;
			}
			
			if(arg.startsWith("-r"))
			{
				recursive = true;
				continue;
			}
			
			if(arg.startsWith("-m"))
			{
				autoMerge = true;
				continue;
			}
			
			if(arg.startsWith("-f")){
				indexFolder = arg.substring(2);
				continue;
			}
		}
		
		System.out.println("host="+host);
		System.out.println("port="+port);
		
		System.out.println("userLogin="+userLogin);
		System.out.println("password="+password);
		
		System.out.println("indexFolder="+indexFolder);
		System.out.println("projectName="+projectName);
		
		System.out.println("recursive="+recursive);
		System.out.println("autoMerge="+autoMerge);
		
		File f = new File(indexFolder).getCanonicalFile();
		if(!f.exists()) throw new FileNotFoundException(indexFolder);
		
		Importer importer = new Importer(host,port,userLogin,password);
		importer.login();
		try
		{
			Project p = importer.getProject(projectName);
			if(p == null)
			{
				System.out.println("Named project ("+projectName +") not found, stopping");
				return;
			}
			
//			p.createRecord(f, recursive, autoMerge);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
