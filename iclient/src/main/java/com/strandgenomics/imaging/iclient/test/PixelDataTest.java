package com.strandgenomics.imaging.iclient.test;

import java.awt.image.BufferedImage;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;

/**
 * test class to demonstrate IPixelData and its apis
 * 
 * @author Anup Kulkarni
 */
public class PixelDataTest 
{
	public static void main(String ... args) throws Exception
    {
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"localhost", "8080", "arunabha", "arunabha123", "10"};
    	}

    	ImageSpace ispace = ImageSpaceObject.getConnectionManager();
    	String hostIP = args[0];
    	int hostPort = Integer.parseInt(args[1]);
    	String userName = args[2];
    	String password = args[3];
    	long guid = Long.parseLong(args[4]);		
    	
    	ispace.login(false, hostIP, hostPort, userName, password);
    	
    	//find record
    	Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
    	
    	int noOfChannels = r.getChannelCount(); 
    	int noOfSites = r.getSiteCount();
    	int noOfSlices = r.getSliceCount();
    	int frameCount = r.getFrameCount();
    	
    	System.out.println(r.getSignature());
    	
    	Thread.sleep(10000);
    	
    	int counter = 0;
    	
    	long startTime = System.currentTimeMillis();
    	
    	for(int site = 0;site < noOfSites; site++)
    	{
    		for(int frame = 0;frame < frameCount; frame++)
    		{
    			for(int slice = 0;slice < noOfSlices; slice++)
    			{
    				for(int channel = 0; channel < noOfChannels; channel++)
    				{
    					counter++;
    					
    					Dimension d = new Dimension(frame,slice,channel, site);
    			    	//get pixel data
    			    	IPixelData pixelData = r.getPixelData(d);
    			    	BufferedImage img = pixelData.getImage(true);
    			    	System.out.println(d);
    				}
    			}
    		}
    	}
    	
    	long endTime = System.currentTimeMillis();
    	System.out.println("Read "+counter +" images in "+(endTime-startTime) +"ms, avg retreival time is "+((double)(endTime-startTime)/counter) +" ms");

    	System.out.println("Done");
    }
}
