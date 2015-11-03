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
