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
