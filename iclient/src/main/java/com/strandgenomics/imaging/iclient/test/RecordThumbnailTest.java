package com.strandgenomics.imaging.iclient.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;

/**
 * test class to demonstrate setting user specified thumbnail on record
 * 
 * @author Anup Kulkarni
 */
public class RecordThumbnailTest {
    public static void main(String ... args) throws Exception
    {
    	if(args == null || args.length == 0)
    	{
    		args = new String[]{"anthurium", "8080", "anup", "anup123"};
    	}

    	ImageSpace ispace = ImageSpaceObject.getConnectionManager();
    	String hostIP = args[0];
    	int hostPort = Integer.parseInt(args[1]);
    	String userName = args[2];
    	String password = args[3];
    	
    	ispace.login(false, hostIP, hostPort, userName, password);
    	
    	Record r = ImageSpaceObject.getImageSpace().findRecordForGUID(4);
    	int frameNo = 0;
    	int sliceNo = 0;
    	int siteNo = 0;
    	int[] channelNos = {0};
    	BufferedImage img = r.getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos).getImage(false, false, false, null);
    	
    	File tempFile = File.createTempFile("thumbnail", "png");
    	ImageIO.write(img, "PNG", tempFile);
    	
		r.setThumbnail(tempFile);
    	System.out.println("Done");
    }
}
