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

import java.util.Collection;
import java.util.List;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.icore.SearchField;

public class NavigableFieldsTest {
    public static void main(String ... args) throws Exception
    {
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"localhost", "8080", "anup", "anup123"};
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
	    		//get navigable fields
	    		System.out.println("Navigable Fields");
	    		Collection<SearchField> fields = p.getNavigableFields();
	    		if(fields!=null && fields.size()>0)
	    		{
	    			for(SearchField field:fields)
	    			{
	    				System.out.println(field);
	    			}
	    		}
	    		
	    		// get user annotation fields
	    		System.out.println("User Annotation Fields");
	    		fields = p.getUserAnnotationFields();
	    		if(fields!=null && fields.size()>0)
	    		{
	    			for(SearchField field:fields)
	    			{
	    				System.out.println(field);
	    			}
	    		}
	    	}
    	}
    	else
    	{
    		System.out.println("[NavigableFieldsTest]:\t projects not found...");
    	}
    	
    	System.out.println("[RecordListingTest]:\tDone");
    }
}
