package com.strandgenomics.imaging.iclient.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;

/**
 * class to demonstrate getting image from overlaying image across different dimensions 
 * 
 * @author Anup Kulkarni
 */
public class ImageRetrievalTest {
	
	public static void main(String ... args) throws Exception
    {
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"banerghatta", "8080", "arunabha", "arunabha123", "14"};
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
    	
    	int[] channelNos = new int[noOfChannels];
    	for(int i = 0;i < noOfChannels; i++)
    		channelNos[i] = i;
    	
    	//Thread.sleep(5000);.
    	
    	long startTime = System.currentTimeMillis();
    	IPixelDataOverlay pixelData = r.getOverlayedPixelData(0, 0, 0, channelNos);
    	BufferedImage img = pixelData.getImage(false, false, true, null);
    	long endTime = System.currentTimeMillis();
    	System.out.println("Init time "+(endTime-startTime));
    	
    	int counter = 0;
    	long totalTime = 0;
    	
    	File root = new File(System.getProperty("user.dir"));
    	System.out.println(root);
    	
    	for(int site = 0;site < 1; site++)
    	{
    		for(int frame = 0;frame < 1; frame++)
    		{
    			for(int slice = 0;slice < 1; slice++)
    			{
					counter++;
			    	//get pixel data
					startTime = System.currentTimeMillis();
			    	pixelData = r.getOverlayedPixelData(slice, frame, site, channelNos);
			    	img = pixelData.getImage(false, false, true, null);
			    	endTime = System.currentTimeMillis();
			    	
			    	totalTime += (endTime-startTime);
			    	
//			    	img = ImageUtil.getScaledImage(img, 32,32);
			    	
			    	long ssTime = System.currentTimeMillis();
//			    	img = ImageUtil.getScaledImage(img, 128,128);
			    	long eeTime = System.currentTimeMillis();
			    	
			    	System.out.println("interpolation time = "+(eeTime-ssTime));
			    	
			    	ImageIO.write(img, "PNG", new File(root, guid+"_"+site+"_"+frame +"_"+slice +".png"));
    			}
    		}
    	}
    	
    	System.out.println("********** overlay channel *************");
    	System.out.println("Read "+counter +" images in "+totalTime +"ms, avg retreival time is "+((double)totalTime/counter) +" ms");
    	
    	counter = 0;
    	totalTime = 0;
    	channelNos = new int[1];
    	channelNos[0] = 0;
//    	for(int site = 0;site < noOfSites; site++)
//    	{
//    		for(int frame = 0;frame < frameCount; frame++)
//    		{
//    			for(int slice = 0;slice < noOfSlices; slice++)
//    			{
//					counter++;
//			    	//get pixel data
//					startTime = System.currentTimeMillis();
//			    	pixelData = r.getOverlayedPixelData(slice, frame, site, channelNos);
//			    	img = pixelData.getImage(false, false, true);
//			    	endTime = System.currentTimeMillis();
//			    	
//			    	totalTime += (endTime-startTime);
//			    	ImageIO.write(img, "PNG", new File(root, guid+"_"+site+"_"+frame +"_"+slice +"_0.png"));
//    			}
//    		}
//    	}
//    	
//    	System.out.println("********** single channel *************");
//    	System.out.println("Read "+counter +" images in "+totalTime +"ms, avg retreival time is "+((double)totalTime/counter) +" ms");
    }
}
