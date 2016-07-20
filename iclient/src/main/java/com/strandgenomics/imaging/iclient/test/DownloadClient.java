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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.strandgenomics.imaging.iclient.ImageSpace;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.image.PixelArray;

class Test implements Runnable{
	Record record;
	
	Test(Record record){
		this.record=record;
	}
	
	@Override
	public void run() {
		int site = 0;
		
		for(int frame = 0;frame<record.getFrameCount();frame++)
		{
			for(int slice=0;slice<record.getSliceCount();slice++)
			{
				for(int channel=0;channel<record.getChannelCount();channel++)
				{
					Dimension dim = new Dimension(frame,slice,channel,site);
					System.out.println("Thread="+Thread.currentThread()+" Downloading raw data "+dim);
					
					IPixelData data = record.getPixelData(dim);
					try {
						PixelArray rawData = data.getRawData();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Thread="+Thread.currentThread()+" Done downloading raw data for "+dim);
				}
			}
		}		
	}
	
}


public class DownloadClient {
	public static void main(String[] args) throws IOException
	{
		ImageSpace ispace = ImageSpaceObject.getConnectionManager();
		boolean connected = ispace.login(false, "banerghatta", 80, "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI", "ucoVFkvFR02tHxtILLlH8zhUFX7Zpdg9OAVXvOLV");
		System.out.println(connected);
		
		Record record = ispace.findRecordForGUID(1306);	
		
		int guids[]={1251,1040,1041,1043,1044,569,571,1291};
		
		List<Record> recordArray=new ArrayList<Record>();
		
		for(int i=0;i<guids.length;i++){	
			recordArray.add(ispace.findRecordForGUID(guids[i]));
		}
		
		
		List<Thread> test=new ArrayList<Thread>();
		
	    Random rand = new Random();

	    int max=guids.length-1;
	    int min=0;
	    int randomNum;
		
		for(int i=0;i<100;i++){
			randomNum = rand.nextInt((max - min) + 1) + min;
			test.add(new Thread(new Test(recordArray.get(randomNum))));
		}
		
		for(int i=0;i<test.size();i++){
			System.out.println("Strating thread "+i);
			test.get(i).start();
		}
		
	}
}
