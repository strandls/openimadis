package com.strandgenomics.imaging.iclient.test;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;

/**
 * test class to demonstrate free text search on record data
 * 
 * @author Anup Kulkarni
 */
public class SearchTest {
	public static void main(String... args)
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
    	
    	// search unconditionally on all projects
    	long[] guids = ImageSpaceObject.getImageSpace().search("anup", null, null, 10);
    	print(guids);
    	
    	// search on specific project
//    	Set<String> projectNames = new HashSet<String>();
//    	projectNames.add("StrandSample");
//    	long[] projectSpecGuids = ImageSpaceObject.getImageSpace().search("anup", projectNames, null, 10);
//    	print(projectSpecGuids);
    	
    	//seach on condition
//    	SearchCondition cond = new SearchCondition("Record ID", 1L, 3L);
//    	Set<SearchCondition> searchCond = new HashSet<SearchCondition>();
//    	searchCond.add(cond);
//    	long[] condSpecGuids = ImageSpaceObject.getImageSpace().search("anup", null, searchCond, 10);
//    	print(condSpecGuids);

    	System.out.println("Done");
	}
	
	private static void print(long[] guids){
		if(guids !=null && guids.length > 0)
    	{
    		System.out.println("Query string found in following records ");
    		for(long guid:guids)
    		{
    			System.out.println(guid);
    		}
    	}
    	else
    	{
    		System.out.println("Query string not found");
    	}
	}
}
