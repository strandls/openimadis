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
