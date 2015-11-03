/*
 * ProjectListingTest.java
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

import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.User;

public class ProjectListingTest implements TestData
{
	public static void main(String ... args) throws Exception
	{
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"192.168.4.56", "8080", "perrine", "perrine123"};
    	}
    	
    	ImageSpace ispace = ImageSpaceObject.getConnectionManager();
    	String hostIP = args[0];
    	int hostPort = Integer.parseInt(args[1]);
    	String userName = args[2];
    	String password = args[3];
    	
    	ispace.login(false, hostIP, hostPort, userName, password);
    	
    	List<Project> projectList = null;
		try 
		{
			projectList = ispace.getActiveProjects();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    	
    	if(projectList != null)
    	{
	    	for(Project p : projectList)
	    	{
	    		System.out.println("[Project]:\tname="+p.getName()+", notes="+p.getNotes()+", Records="+p.getRecordCount()+", DiskUsage="+p.getSpaceUsage()+"GB, DiskQuota="+p.getDiskQuota()+"GB");
	    		List<User> members = p.getProjectMembers();
	    		for(User u : members)
	    		{
	    			System.out.println("\t[Member]:\tlogin="+u.getName() +", Email="+u.getEmailID() +", Rank="+u.getRank());
	    		}
	    		
	    		List<User> pm = p.getProjectManager();
	    		
	    		if(pm != null)
	    		for(User u : pm)
	    		{
	    			System.out.println("\t[Manager]:\tlogin="+u.getName() +", Email="+u.getEmailID() +", Rank="+u.getRank());
	    		}	    		
	    		Thread.sleep(100);
	    	}
    	}
    	else
    	{
    		System.out.println("[ProjectListingTest]:\tprojects not found...");
    	}
    	
    	System.out.println("[ProjectListingTest]:\tDone");
	}
}
