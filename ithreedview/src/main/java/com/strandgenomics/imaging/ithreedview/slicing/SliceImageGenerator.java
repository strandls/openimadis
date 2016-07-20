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

package com.strandgenomics.imaging.ithreedview.slicing;
import java.awt.image.BufferedImage;

import com.strandgenomics.imaging.ithreedview.dataobjects.ImageArray;
import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;
import com.strandgenomics.imaging.ithreedview.utils.ImageUtils;

public class SliceImageGenerator {
	private int xDim;
	private int yDim;
	private int zDim;
	private Volume3DData vol3d;
	private ImageArray slicedImage;
	private int currentBrightness=0;
	private int currentThreshold=0;
	public static enum Mode {CONFIGURE, SLICE};


	public SliceImageGenerator(Volume3DData vol3d){

		zDim = vol3d.scaledZDimension;
		xDim = vol3d.xDim;
		yDim = vol3d.yDim;

		this.vol3d = vol3d;
	}

	public void setBrightness(int brightness)
	{
		currentBrightness = brightness*25;
	}

	public void setThreshold(int threshold)
	{
		currentThreshold = threshold;
	}

	///{{{ generateSliceImage	
	public void generateSliceImage(SliceConfigurer.SlicePlane slicePlane, float percent, Mode mode)
	{
		if(mode == Mode.SLICE)
		{
		if(slicePlane==SliceConfigurer.SlicePlane.YZPLANE)
		{
			slicedImage = new ImageArray(zDim,xDim,3);	
			sliceYZ(percent);
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XZPLANE)
		{
			slicedImage = new ImageArray(zDim,xDim,3);
			sliceXZ(percent);
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XYPLANE)
		{
			slicedImage = new ImageArray(xDim,yDim,3);
			sliceXY(percent); // no need to alloc image
		}
		}
		if(mode == Mode.CONFIGURE)
		{
		if(slicePlane==SliceConfigurer.SlicePlane.YZPLANE || slicePlane == SliceConfigurer.SlicePlane.XZPLANE)
		{
			slicedImage = new ImageArray(xDim,yDim,3);	
			generateFrontalView();
		}
		if(slicePlane==SliceConfigurer.SlicePlane.XYPLANE)
		{
			slicedImage = new ImageArray(zDim,yDim,3);
			generateProfileView(); 
		}

		
		}
	}
	///}}}

	///{{{sliceYZ
	public void sliceYZ(float percent)
	{
		float xMax = percent*(xDim-1);
		int xLower = (int)Math.max(0,Math.floor(xMax));
		int xUpper = (int)Math.min(xDim-1,Math.ceil(xMax));
		float frac1 = 0.5f;
		float frac2 = 0.5f;
		if(xUpper!=xLower)
		{
			frac1 = (float)(xUpper-xMax)/(float)(xUpper-xLower);
			frac2 = (float)(xMax-xLower)/(float)(xUpper-xLower);
		}

		int max = 0;

		int[]data = new int[3];
		for(int z=0;z<zDim;z++)
		{
			for(int y=0;y<yDim;y++)
			{

				double[]lDataArray = vol3d.getVolumeData(xLower,y,z);
				double[]uDataArray = vol3d.getVolumeData(xUpper,y,z);

				data[0] =  (int)(frac1*lDataArray[0]+frac2*uDataArray[0]);
				data[1] =  (int)(frac1*lDataArray[1]+frac2*uDataArray[1]);
				data[2] =  (int)(frac1*lDataArray[2]+frac2*uDataArray[2]);
				for(int i=0;i<3;i++)
				{
					data[i] =  Math.min(254,data[i]+ currentBrightness);
					if(data[i]<currentThreshold){data[i]=0;}
				}
				slicedImage.setData(z,y,data);
			}
		}

	}
	///}}}

	///{{{sliceXZ
	public void sliceXZ(float percent)
	{
		float  yMax = (percent*(yDim-1));
		int yLower = (int)Math.max(0,Math.floor(yMax));
		int yUpper = (int)Math.min(yDim-1,Math.ceil(yMax));
		float frac1 = 0.5f;
		float frac2 = 0.5f;
		if(yUpper!=yLower)
		{
			frac1 = (float)(yUpper-yMax)/(float)(yUpper-yLower);
			frac2 = (float)(yMax-yLower)/(float)(yUpper-yLower);
		}

		int[]data= new int[3];
		for(int z=0;z<zDim;z++)
		{

			for(int x=0;x<xDim;x++)
			{

				double[]lDataArray = vol3d.getVolumeData(x,yLower,z);
				double[]uDataArray = vol3d.getVolumeData(x,yUpper,z);

				data[0] =(int)(frac1* lDataArray[0] + frac2*uDataArray[0]);
				data[1] =(int)(frac1* lDataArray[1] + frac2*uDataArray[1]);
				data[2] =(int)(frac1* lDataArray[2] + frac2*uDataArray[2]);
				for(int i=0;i<3;i++)
				{
					data[i] =  Math.min(254,data[i]+ currentBrightness);
					if(data[i]<currentThreshold){data[i]=0;}
				}

				slicedImage.setData(z,x,data);
			}
		}
	}
	///}}}

	///{{{sliceXY
	public void sliceXY(float percent)
	{
		float zMax = percent*(zDim-1);
		int zLower = (int) Math.max(0,Math.floor(zMax));
		int zUpper = (int) Math.min(zDim-1,Math.ceil(zMax));
		float frac1 = 0.5f;
		float frac2 = 0.5f;

		if(zUpper!=zLower)
		{
			frac1 = (float)(zUpper-zMax)/(float)(zUpper-zLower);
			frac2 = (float)(zMax-zLower)/(float)(zUpper-zLower);
		}

		int[]data = new int[3];
		for(int x = 0;x<xDim;x++)
		{
			for(int y=0;y<yDim;y++)
			{
				double[]lDataArray = vol3d.getVolumeData(x,y,zLower);
				double[]uDataArray = vol3d.getVolumeData(x,y,zUpper);
				data[0] = (int)(frac1*lDataArray[0] + frac2*uDataArray[0]);
				data[1] = (int)(frac1*lDataArray[1] + frac2*uDataArray[1]);
				data[2] = (int)(frac1*lDataArray[2] + frac2*uDataArray[2]);
				for(int i=0;i<3;i++)
				{
					data[i] =  Math.min(254,data[i]+ currentBrightness);
					if(data[i]<currentThreshold){data[i]=0;}
				}

				slicedImage.setData(x,y,data);
			}
		}
	}
	///}}}

	///{{{gettors
	public ImageArray getSliceImage()
	{
		return slicedImage;
	}

	///}}}

	///{{{
	public BufferedImage getSliceAsBufferedImage()
	{
		return ImageUtils.getBufferedImage(slicedImage.getXDimension(),slicedImage.getYDimension(), slicedImage.getImageArray());
	}
	///}}}

	///{{{generateFrontalView
	public void generateFrontalView()
	{
		for(int i=0;i<xDim;i++)
		{
			for(int j=0;j<yDim;j++)
			{
				int[]volData = new int[3];
				double alpha = 0;
				double alphaSum = 0;
				for(int k=0;k<zDim;k++)
				{

				double[]dataArray = vol3d.getVolumeData(i,j,k);
				if(k>0)
				{

				int[]currData = slicedImage.getData(i,j);
				alpha = (currData[0]+currData[1]+currData[2])/3.0;
				alphaSum = alphaSum+(1-alpha);
				}
				for(int l=0;l<3;l++)
				{
					volData[l] =(int)( volData[l]+(1-alpha)*dataArray[l]); 
					//if(volData[l]>254) volData[l]=254;
				}

				}
				for(int l=0;l<3;l++)
				{
					volData[l] = (int)((double)volData[l]/alphaSum);

					if(volData[l]>255) volData[l] =255;
				}
				slicedImage.setData(i,j,volData);
			}
		}

	}
	///}}}

	///{{{ generate Profile view
	public void generateProfileView()
	{
		for(int i=0;i<zDim;i++)
		{
			for(int j=0;j<yDim;j++)
			{
				int[]volData = new int[3];
				double alpha =0;
				double alphaSum = 0;
				for(int k=0;k<xDim;k++)
				{

				double[]dataArray = vol3d.getVolumeData(k,j,i);
				if(k>0)
				{
				int[]currData = slicedImage.getData(i,j);
				alpha = (currData[0]+currData[1]+currData[2])/3.0;
				alphaSum = alphaSum+(1-alpha);
				}

				for(int l=0;l<3;l++)
				{
					volData[l] = (int)(volData[l]+(1-alpha)*dataArray[l]); 
					//if(volData[l]>254) volData[l]=254;
				}
				}
				for(int l=0;l<3;l++)
				{
					volData[l] = (int)(volData[l]/alphaSum);
					//volData[l] = volData[l]+50;
					if(volData[l]>255) volData[l] =255;
				}

				slicedImage.setData(i,j,volData);
			}
		}

	}
	///}}}

	public void cleanup()
	{
		//do nothing	
	}

}
