package com.strandgenomics.imaging.tileviewer.system;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import com.strandgenomics.imaging.iclient.ImageSpaceSystem;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Task for fetching tile from server, given parameters that uniquely identifies tile and
 * client side end point for image space service with relevant user session 
 * 
 * @author Anup Kulkarni
 */
public class TileFetchingTask implements Callable<Integer>{
	/**
	 * parameters describing the tile uniquely
	 */
	private TileParameters params;
	/**
	 * ispace service end point for relavent user session
	 */
	private ImageSpaceSystem ispace;

	public TileFetchingTask(ImageSpaceSystem ispace, TileParameters params)
	{
		this.params = params;
		this.ispace = ispace;
	}
	
	@Override
	public Integer call() throws Exception
	{
		// TODO 
		// fetch the tile from the server
		// store it in appropriate local storage(DB,FS or both), such that it is accessible given TileParameters
		long start= System.currentTimeMillis();
		System.out.println("task started for "+params.getX()+" "+params.getY()+" "+params.getZ());
		
		long recordid= params.getGuid();
		int tileX = params.getX();
		int tileY = params.getY();
		int tileZ = params.getZ();
		int tileDimension = params.getTileSize();
		RecordParameters recordParameters = params.getRecordParameters();
		
		int channelNumbers[] = new int[recordParameters.getChannelCount()];
		for (int i=0; i<recordParameters.getChannelCount(); i++) {
			channelNumbers[i] = i;
		}
		
		//get record for guid
		//Record r = ispace.findRecordForGUID(recordid);
		int imageWidth = recordParameters.getImageWidth();
		int imageHeight =  recordParameters.getImageHeight();

		// the scale of image to be fetched wrt to the map tile
		// tileZ is zoom reverse
		int scale= (int) Math.pow(2,tileZ);
		
		int scaledDimension = scale * tileDimension;
		
		// no of tiles along x direction is original image width divided by map tile width
		int numberOfTilesX= (int)Math.ceil((double) imageWidth/ scaledDimension);
		
		// no of tiles along x direction is original image width divided by map tile width
		int numberOfTilesY= (int)Math.ceil((double) imageHeight/ scaledDimension);
		
		//the dimension of image tile to be fetched
		//it will be map tile dimension multiply by the scale factor
		int imageTileDimension=tileDimension*scale;
		
		//the buffered image that will contain the image tile and will be returned to map		
		BufferedImage fetchedImg=null;
		long milestone2=0;
		
		if((tileX>=0 && tileY>=0) && (tileX<numberOfTilesX && tileY<numberOfTilesY)){
			int X= tileX*imageTileDimension;
			int Y= tileY*imageTileDimension;
			int W= imageTileDimension;
			int H= imageTileDimension;
			
			//W=(X+W)<imageWidth?W:(imageWidth-X);
			//H=(Y+H)<imageHeight?H:(imageHeight-Y);
			
			Rectangle reqdRect = new Rectangle(X,Y,W,H);
			
			//assuming zoom parameter will be passed to API
			boolean exp = false;
			do
			{
				exp=false;
				try
				{
					System.out.println("Fetching image x="+X+" y="+Y+" time="+System.currentTimeMillis());
					fetchedImg = ispace.getOverlayedImage(recordid, recordParameters.getSliceNumber(), recordParameters.getFrameNumber(), 0, channelNumbers, recordParameters.isZStacked(), false, !recordParameters.isGrayScale(), reqdRect.x, reqdRect.y, reqdRect.width, reqdRect.height, tileDimension, tileDimension);
					System.out.println("x="+X+" y="+Y+" time="+System.currentTimeMillis());
				}
				catch (Throwable e)
				{
					e.printStackTrace();
//					System.out.println("********** Retrying");
//					System.out.println(e.getMessage());
					exp = true;
				}
				if(fetchedImg == null){
					exp = true;
				}
			}while(exp);
			long milestone1= System.currentTimeMillis();
			System.out.println("fetching completed for "+params.getX()+" "+params.getY()+" "+params.getZ()+" in time="+(milestone1-start));			
			
			milestone2 = System.currentTimeMillis();
			System.out.println("resizing completed for "+params.getX()+" "+params.getY()+" "+params.getZ()+" in time="+(milestone2-milestone1));
		}
		
		StorageManager.getInstance().storeTile(params, fetchedImg);
		
		long milestone3= System.currentTimeMillis();
		System.out.println("storing completed for "+params.getX()+" "+params.getY()+" "+params.getZ()+" in time="+(milestone3-milestone2));
		
		long end= System.currentTimeMillis();
		System.out.println("task completed for "+params.getX()+" "+params.getY()+" "+params.getZ()+" in time="+(end-start)+" systime="+System.currentTimeMillis());
		
		return params.hashCode();
	}
}
