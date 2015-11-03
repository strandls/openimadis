package com.strandgenomics.imaging.iclient.test;

import java.util.Collection;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * Test class to demonstrate retrieval of visual objects from specified overlay of specified record
 * 
 * @author Anup Kulkarni
 */
public class VAObjectRetrievalTest {
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
    	IVisualOverlay overlay = r.getVisualOverlay(new VODimension(0, 0, 0), "overlay");
    	
    	if(overlay!=null)
    	{
    		Collection<VisualObject> vobjects = overlay.getVisualObjects();
    		if(vobjects!=null)
    		{
    			for(VisualObject vobject:vobjects)
    			{
    				System.out.println("Found visual object "+vobject.getBounds()+" "+vobject.getType());
    			}
    		}
    	}
	}
}
