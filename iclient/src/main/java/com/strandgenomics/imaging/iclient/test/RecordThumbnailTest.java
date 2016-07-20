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
