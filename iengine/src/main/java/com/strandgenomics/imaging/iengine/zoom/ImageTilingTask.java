package com.strandgenomics.imaging.iengine.zoom;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TileDAO;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * 
 * 
 * @author Anup Kulkarni
 */
public class ImageTilingTask implements Callable<Void>{

	/**
	 * tiling request parameters
	 */
	private TilingRequest request;
	
	private final int MAX_ZOOM_LEVEL_FOR_PREFETCHING;
	
	private final int MIN_ZOOM_LEVEL_FOR_PREFETCHING;
	
	public ImageTilingTask(TilingRequest req)
	{
		this.request = req;
		
		MAX_ZOOM_LEVEL_FOR_PREFETCHING = (int) Math.floor(Math.log(Math.max(req.getRecordWidth(), req.getRecordHeight()))/Math.log(2) - 8);
		
		MIN_ZOOM_LEVEL_FOR_PREFETCHING = (int) Math.ceil(Math.log(Math.max(Constants.MAX_TILE_WIDTH, Constants.MAX_TILE_HEIGHT))/Math.log(2) - 8);
	}
	
	@Override
	public Void call()
	{
		try {
			if(!registerTask()){
				return null;
			}
			doTiling();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void doTiling() throws IOException
	{	
		double estimatedTime = 0, elapsedTime = 0;
		long sTime = System.currentTimeMillis();
		
		Dimension dim = this.request.getDim();
		
		List<Integer> channelList = new ArrayList<Integer>();
		for (int i = 0; i < this.request.getChannelCount(); i++)
		{
			channelList.add(Integer.valueOf(i));
		}
		
		prepareImages();
		
		long allImagesSize = 0;
		long guid = this.request.getGuid();
		TileDAO tileDAO = ImageSpaceDAOFactory.getDAOFactory().getTileDAO();
		long nTiles = 0; 
		elapsedTime = System.currentTimeMillis()-sTime;
		estimatedTime = elapsedTime;
		double prev = estimatedTime;
		int count = 0;
		long totalTime = 0;
	
		for(int x=0;x<this.request.getRecordWidth();x+=Constants.MAX_TILE_WIDTH)
		{
			for(int y=0;y<this.request.getRecordHeight();y+=Constants.MAX_TILE_HEIGHT)
			{
				nTiles++;
			}
		}
		
		for(int x=0;x<this.request.getRecordWidth();x+=Constants.MAX_TILE_WIDTH)
		{
			for(int y=0;y<this.request.getRecordHeight();y+=Constants.MAX_TILE_HEIGHT)
			{
				System.out.println("Doing for startx="+x+" starty"+y);
				// find out the bounds
				int tileWidth = (x + Constants.MAX_TILE_WIDTH) < this.request.getRecordWidth() ? Constants.MAX_TILE_WIDTH : (this.request
						.getRecordWidth() - x);
				int tileHeight = (y + Constants.MAX_TILE_HEIGHT) < this.request.getRecordHeight() ? Constants.MAX_TILE_HEIGHT : (this.request
						.getRecordHeight() - y);
				System.out.println("tile dimension "+tileWidth+" "+tileHeight);
				
				// get the image in original scale
				BufferedImage image = SysManagerFactory.getImageManager().getPixelDataOverlay(this.request.getActor(), this.request.getGuid(),
						dim.sliceNo, dim.frameNo, dim.siteNo, channelList, true, false, false,
						new Rectangle(x, y, tileWidth, tileHeight));
				
				System.out.println("fetched tile for x="+x+" y="+y);
				
				count++;
				allImagesSize = 0;
				int zoomCount = 0;
				totalTime = 0;
				for (int zoom = MAX_ZOOM_LEVEL_FOR_PREFETCHING; zoom >= MIN_ZOOM_LEVEL_FOR_PREFETCHING; zoom--)
				{
					zoomCount++;
					long startTime = System.currentTimeMillis();
					
					System.out.println("for zoom="+zoom);
					int scalingFactor = (int)Math.pow(2, zoom);
					
					int scaledTileWidth = (int)Math.ceil((double)tileWidth / scalingFactor);
					int scaledTileHeight = (int) Math.ceil((double)tileHeight / scalingFactor);
					
					int scaledTopX = x / scalingFactor;
					int scaledTopY = y / scalingFactor;
					
					//BufferedImage scaledImage = Util.resizeImage(image, scaledTileWidth, scaledTileHeight);
					//writeImage(scaledImage, scaledTopX, scaledTopY, zoom);
					
					BufferedImage imageForZoom = getScaledImageForZoom(zoom);
					Graphics gfx = imageForZoom.getGraphics();
					gfx.drawImage(image, scaledTopX, scaledTopY, scaledTopX+scaledTileWidth, scaledTopY+scaledTileHeight, 0, 0, tileWidth, tileHeight, null);
					gfx.dispose();

					writeImage(imageForZoom, zoom);
					
					elapsedTime += (System.currentTimeMillis() - startTime);
					long endTime   = System.currentTimeMillis();
					totalTime += (endTime - startTime);
					File imageFile = new File(this.request.getStorageRoot(),zoom+".png");
					allImagesSize += imageFile.length();
	
					long estimatedTimeForAllLevels  = totalTime + ((totalTime/zoomCount)*(MAX_ZOOM_LEVEL_FOR_PREFETCHING - MIN_ZOOM_LEVEL_FOR_PREFETCHING + 1 - zoomCount));
					estimatedTime = prev + estimatedTimeForAllLevels + ((estimatedTimeForAllLevels/count)*(nTiles - count));
					tileDAO.setEstimatedTime(guid, estimatedTime, elapsedTime, allImagesSize/1024);
					
				}
				prev = prev + totalTime;
			}
		}
		File pixelArrayDir = new File(this.request.getStorageRoot(),"PixelArray"); 
		pixelArrayDir.mkdir();
		
		count = 0;
		totalTime =0;
		prev = estimatedTime;
		for (int zoom = MAX_ZOOM_LEVEL_FOR_PREFETCHING; zoom >= MIN_ZOOM_LEVEL_FOR_PREFETCHING; zoom--)
		{
			sTime = System.currentTimeMillis();
			count++;
			
			BufferedImage img = ImageIO.read(new File(this.request.getStorageRoot(),zoom+".png"));
			
			BufferedImage thisIsInsane = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics gfx = thisIsInsane.getGraphics();
			gfx.drawImage(img, 0, 0, null);
			gfx.dispose();
			
			PixelArray array = PixelArray.toPixelArray(thisIsInsane);
			
			OutputStream out= new FileOutputStream(new File(pixelArrayDir,String.valueOf(zoom)));
			array.write(out);
			out.close();
			
			elapsedTime += (System.currentTimeMillis() - sTime);
			totalTime += System.currentTimeMillis() - sTime;
			estimatedTime = prev;
			estimatedTime += totalTime + ((totalTime/count) * (MAX_ZOOM_LEVEL_FOR_PREFETCHING - MIN_ZOOM_LEVEL_FOR_PREFETCHING + 1 - count));
			tileDAO.setEstimatedTime(guid, estimatedTime, elapsedTime, allImagesSize/1024);
			
		}
		tileDAO.setEstimatedTime(guid, elapsedTime, elapsedTime, allImagesSize/1024);
		setTileStatus();
		
		Set<User> receivers = new HashSet<User>();
        receivers.add(SysManagerFactory.getUserManager().getUser(request.getActor()));
        SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, null, NotificationMessageType.TILING_TASK_COMPLETED, SysManagerFactory.getProjectManager().getProject(request.getProjectId()).getName(), String.valueOf(request.getRecordId()), request.getRecordFileName(), "READY");
		
	}
	
	private BufferedImage getScaledImageForZoom(int zoom) throws IOException
	{
		File outputFile = new File(this.request.getStorageRoot(), String.valueOf(zoom)+".png");
		return ImageIO.read(outputFile);
	}
	
	private void writeImage(BufferedImage image, int zoom) throws IOException
	{
		File outputFile = new File(this.request.getStorageRoot(), String.valueOf(zoom)+".png");
		ImageIO.write(image, "png", outputFile);
	}
	
	private void prepareImages() throws IOException
	{
		for (int zoom = MAX_ZOOM_LEVEL_FOR_PREFETCHING; zoom >= MIN_ZOOM_LEVEL_FOR_PREFETCHING; zoom--)
		{
			int scalingFactor = (int)Math.pow(2, zoom);
			
			int scaledRecordWidth = this.request.getRecordWidth() / scalingFactor;
			int scaledRecordHeight = this.request.getRecordHeight() / scalingFactor;
			
			BufferedImage scaledImage = new BufferedImage(scaledRecordWidth, scaledRecordHeight, BufferedImage.TYPE_INT_RGB);
			File outputFile = new File(this.request.getStorageRoot(), String.valueOf(zoom)+".png");
			ImageIO.write(scaledImage, "png", outputFile);
		}
	}
	
	/**
	 * register tiles which will be generated by given task
	 * if duplicate abort task
	 * @throws DataAccessException 
	 */
	private boolean registerTask() throws DataAccessException{
			
		long guid = this.request.getGuid();
		
		TileDAO tileDAO = ImageSpaceDAOFactory.getDAOFactory().getTileDAO();
			
		for (int zoom = MAX_ZOOM_LEVEL_FOR_PREFETCHING; zoom >= MIN_ZOOM_LEVEL_FOR_PREFETCHING; zoom--)
		{
			if(tileDAO.isTileAlreadyPresent(guid, zoom))
				return false;
			
			String storagePath= zoom+".png";
			tileDAO.insertTile(guid, zoom, storagePath);	
		}
		tileDAO.insertEstimatedTime(guid, 100, 1, 0);
		return true;
	}
	
	/**
	 * set the status of tile to ready
	 * @param zoomReverseLevel
	 * @throws DataAccessException
	 */
	private void setTileStatus() throws DataAccessException{
		
		for(int zoom = MAX_ZOOM_LEVEL_FOR_PREFETCHING; zoom>= MIN_ZOOM_LEVEL_FOR_PREFETCHING; zoom--){
			long guid = this.request.getGuid();
			
			TileDAO tileDAO = ImageSpaceDAOFactory.getDAOFactory().getTileDAO();
			
			tileDAO.setIsReady(guid, zoom, true);
		}
	}
	
}
