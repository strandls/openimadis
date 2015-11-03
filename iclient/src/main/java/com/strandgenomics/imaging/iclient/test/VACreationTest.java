package com.strandgenomics.imaging.iclient.test;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;

/**
 * test class to demonstrate creation of visual overlay on specified record 
 *  
 * @author Anup Kulkarni
 */
public class VACreationTest {
	public static void main(String... args) throws InterruptedException
	{
		if (args == null || args.length == 0) {
			args = new String[] { "banerghatta", "8080", "anup", "anup123" };
		}
		String hostIP = args[0];
		int hostPort = Integer.parseInt(args[1]);
		String userName = args[2];
		String password = args[3];

		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		ispace.login(false, hostIP, hostPort, userName, password);
		
    	Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(1);
    	r.createVisualOverlays(0, "test_overlay");
	}
}
