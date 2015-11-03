/*
 * MembershipTest.java
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

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.icore.Permission;

public class MembershipTest implements TestData {
	
	public static void main(String ... args) throws Exception
	{
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"localhost", "8080", "salamero", "salamero123"};
    	}
    	
    	ImageSpace ispace = ImageSpaceObject.getConnectionManager();
    	String hostIP = args[0];
    	int hostPort = Integer.parseInt(args[1]);
    	String userName = args[2];
    	String password = args[3];
    	
    	ispace.login(false, hostIP, hostPort, userName, password);
    	
    	for(int i = 0; i < projects.length; i++)
    	{
	    	try
	    	{
		    	System.out.println("[MembershipTest]:\tfinding project "+projects[i]);
		    	Project project = ispace.findProject(projects[i]);
		    	
		    	if(project == null)
		    	{
		    		System.out.println("[MembershipTest]:\tproject "+projects[i] +" not found");
		    		continue;
		    	}
		    	
		    	System.out.println("[MembershipTest]:\tfound project "+project +" adding memberships");
		    	project.addProjectMembers(Permission.Upload, projectUsers[i]);
	    	
				Thread.sleep(100);
			}
			catch(Exception ex)
			{
				System.out.println("unable to find project "+projects[i] +", "+ex);
			}
    	}
    	
    	System.out.println("[MembershipTest]:\tDone");
	}
}
