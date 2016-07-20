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
