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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.Record;

public class RecordUserAnnotationTest {
	public static void main(String ... args) throws Exception
	{
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"banerghatta", "8080", "salamero", "salamero123"};
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
	    		long[] guids = p.getRecords();
	    		if (guids != null) {
					for (long guid : guids) 
					{
						Record r = ispace.findRecordForGUID(guid);
						
						Map<String, Object> testMap = new HashMap<String, Object>();
						testMap.put("test_3_string", "string_value");
						testMap.put("test_3_real", 3.14);
						testMap.put("test_3_int", 2012);
						testMap.put("test_3_date", new Date(System.currentTimeMillis()));
						r.addUserAnnotation(testMap);
						
						System.out.println("\t[Record]:\tchannel count="
								+ r.getChannelCount() + ", Frame Count="
								+ r.getFrameCount() + ", Slice Count="
								+ r.getSignature());
						Thread.sleep(1000);
						Map<String, Object> annotations = r.getUserAnnotations();
						Iterator<String> it = annotations.keySet().iterator();
						while(it.hasNext()){
							String key = it.next();
							Object value = annotations.get(key);
							System.out.println(key+" "+value);
						}
						System.exit(0);
					}
				}
				Thread.sleep(100);
	    	}
    	}
    	else
    	{
    		System.out.println("[RecordListingTest]:\trecords not found...");
    	}
    	
    	System.out.println("[RecordListingTest]:\tDone");
	}
}
