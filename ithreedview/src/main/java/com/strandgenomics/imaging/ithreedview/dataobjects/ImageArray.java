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

package com.strandgenomics.imaging.ithreedview.dataobjects;

public class ImageArray{

int[][][]imageData;
int xDim;
int yDim;
int depth;

	public ImageArray(int[][][]imageData, int xDim, int yDim, int depth)
	{
		this.imageData = imageData;
		this.xDim = xDim;
		this.yDim = yDim;
		this.depth = depth;
	}

	public ImageArray(int xDim, int yDim, int depth){
		this.xDim = xDim;
		this.yDim = yDim;
		this.depth = depth;
		imageData = new int[xDim][yDim][depth];
	}

	public void setData(int x,int y, int[] data)
	{
		for(int i=0;i<depth;i++)
		{
			imageData[x][y][i] = data[i];
		}
	}

	public int[] getData(int x, int y)
	{
		if(x>=0 && x<xDim && y>=0 && y<yDim)
		{
			return imageData[x][y];	
		}
		else
		{
			System.err.println("ImageArray: indices out of bounds::"+x+"\t"+y+"\t"+xDim+"\t"+yDim);
			return null;
		}
	}

	public int getXDimension()
	{
		return xDim;
	}

	public int getYDimension()
	{
		return yDim;
	}
	public int[][][]getImageArray()
	{
		return imageData;
	}



}
