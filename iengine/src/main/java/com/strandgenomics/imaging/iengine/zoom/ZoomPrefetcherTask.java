package com.strandgenomics.imaging.iengine.zoom;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ZoomDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
/**
 * Zoom images prefetcher task 
 * 
 * @author Anup Kulkarni
 */
public class ZoomPrefetcherTask  implements Callable <Void>
{
	/**
	 * zoom request
	 */
	private ZoomRequest zoomRequest;
	/**
	 * requesting user login
	 */
	public final String actor;
	
	private Logger logger;
	
	public ZoomPrefetcherTask(String actor, ZoomRequest zoomRequest, int xTile, int yTile) 
	{
		this.actor = actor;
		this.zoomRequest = zoomRequest;
		
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
	}
	
	@Override
	public Void call()
	{
		logger.logp(Level.INFO, "ZoomPrefetcherTask", "run", "prefetching images for zoom");

		if(zoomRequest.isDone()) 
		{
			System.out.println("already exist");
			return null;
		}
		
		int xTile = 0;
		int yTile = 0;

		BufferedImage originalImage; 
		try
		{
			originalImage = readOriginalImage();
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ZoomPrefetcherTask", "run", "error prefetching full resolution image for zoom", e);
			return null;
		}
		
		try
		{
			generateThumbnail(originalImage);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ZoomPrefetcherTask", "run", "error generating thumbnail", e);
			return null;
		}
		
		ZoomDimenstion zDim = zoomRequest.getZoomDimension();
		int max = zDim.imageHeight >= zDim.imageWidth ? zDim.imageHeight : zDim.imageWidth;
		
		int maxZoomLevel = (int) Math.ceil(Math.log( (max*1.0)/ZoomDimenstion.TILE_HEIGHT ) /Math.log(2));
		
		for(int zoomLevel=maxZoomLevel; zoomLevel>=0;zoomLevel--)
		{
			ZoomRequest zr = new ZoomRequest(zoomRequest.requestID, zDim, zoomLevel);

			System.out.println("starting for zoomlevel "+zoomLevel+" max zoom level "+maxZoomLevel);
			
			xTile = 0;
			yTile = 0;
			
			while (xTile < zr.getMaxTileX())
			{
				yTile = 0;
				while (yTile < zr.getMaxTileY())
				{
					if (!zr.isDone(xTile, yTile))
					{
						try
						{
							// set flag
							zr.setDone(xTile, yTile);
							
							// generate xTile, yTile
							int rightTopX = xTile * ZoomDimenstion.TILE_WIDTH * ((int)Math.pow(2, zr.zoomLevel));
							int rightTopY = yTile * ZoomDimenstion.TILE_HEIGHT * ((int)Math.pow(2, zr.zoomLevel));
							Rectangle tilePosition = new Rectangle(rightTopX, rightTopY, ZoomDimenstion.TILE_WIDTH, ZoomDimenstion.TILE_HEIGHT);
							
							// fetch image
							BufferedImage img = getTiledImage(originalImage, tilePosition, zr.zoomLevel);
							
							// store image
							storeImage(img, xTile, yTile, zr.zoomLevel);
							
							logger.logp(Level.INFO, "ZoomPrefetcherTask", "run", "done prefetching image for xTile="+xTile+" yTile="+yTile);
							System.out.println("Done "+xTile+" "+yTile);
							
						}
						catch (IOException e)
						{
							// set flag
							zr.resetDone(xTile, yTile);
							
							e.printStackTrace();
							logger.logp(Level.WARNING, "ZoomPrefetcherTask", "run", "error tiling image for zoom", e);
						}
					}
					else
					{
						logger.logp(Level.INFO, "ZoomPrefetcherTask", "run", "image already exists for xTile="+xTile+" yTile="+yTile);
						System.out.println("already exists "+xTile+" "+yTile);
					}
					yTile++;
				}
				xTile++;
			}
		}
		
		System.out.println("done");
		return null;
	}
	
	private void generateThumbnail(BufferedImage originalImage) throws IOException
	{
		// generate thumbnail
		// thumbnail is scaled version of full resolution image
		BufferedImage thumbnail = new BufferedImage(ZoomDimenstion.TILE_WIDTH, ZoomDimenstion.TILE_HEIGHT, originalImage.getType());
		Graphics2D g = thumbnail.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(originalImage, 0, 0, ZoomDimenstion.TILE_WIDTH, ZoomDimenstion.TILE_HEIGHT, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
		g.dispose();
		
		// store in physical location
		File storageLocation = zoomRequest.getStorageRoot();
		
		String tileName = "thumbnail.jpg";
		File tileLocation = new File(storageLocation, tileName);
		
		ImageIO.write(thumbnail, "jpg", tileLocation);
		
		// update DB
		// thumbnail will be stored as zoomLevel -1, xTile=0, yTile=0 in DB
		updateDB(zoomRequest.requestID, -1, 0, 0, tileLocation.getAbsolutePath());
	}
	
	private BufferedImage getTiledImage(BufferedImage originalImage, Rectangle tile, int zoomLevel)
	{
		int effectiveWidth = tile.width * (int)Math.pow(2, zoomLevel);
		int effectiveHeight = tile.height * (int)Math.pow(2, zoomLevel);
		BufferedImage tiledImage = new BufferedImage(effectiveWidth, effectiveHeight, BufferedImage.TYPE_INT_ARGB);
		
		int renderableWidth = effectiveWidth > (originalImage.getWidth() - tile.x) ? (originalImage.getWidth() - tile.x) :effectiveWidth;
		int renderableHeight = effectiveHeight > (originalImage.getHeight() - tile.y) ? (originalImage.getHeight() - tile.y) :effectiveHeight;
		
		Graphics2D g = tiledImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, renderableWidth, renderableHeight, tile.x, tile.y, tile.x+renderableWidth, tile.y+renderableHeight, null);
	    g.dispose();
	    
		BufferedImage resized = new BufferedImage(tile.width, tile.height, tiledImage.getType());
	    g = resized.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(tiledImage, 0, 0, tile.width, tile.height, 0, 0, tiledImage.getWidth(), tiledImage.getHeight(), null);
	    g.dispose();
		
		return resized;
	}

	/**
	 * fetch full resolution image from server once and store in the storage location.
	 * This image will be used for generating tiles
	 * @return
	 * @throws IOException
	 */
	private synchronized BufferedImage readOriginalImage() throws IOException
	{
		File storageLocation = zoomRequest.getStorageRoot();
		
		File originalImage = new File(storageLocation, ZoomRequest.ORIGINAL_FILE_NAME);
		if(originalImage.exists())
			zoomRequest.setOriginalFileDone(true);
		
		if(!zoomRequest.getOriginalFileDone())
		{
			BufferedImage img = null;
			zoomRequest.setOriginalFileDone(true);
			
			ZoomDimenstion zDim = zoomRequest.getZoomDimension();
			try
			{
				img = SysManagerFactory.getImageManager().getPixelDataOverlay(actor, zDim.guid, zDim.slice, zDim.frame, zDim.site, zDim.getChannels(), zDim.useChannelColor, false, false, zDim.getVisualOverlays(),null);
				ImageIO.write(img, "jpg", originalImage);
			}
			catch (IOException e)
			{
				zoomRequest.setOriginalFileDone(false);
			}
			return img;
		}
		
		return ImageIO.read(originalImage);
	}

	/**
	 * store image tiles in physical storage location and update DB accordingly
	 * @param img image to be stored
	 * @param xTile x tile
	 * @param yTile y tile
	 * @throws IOException
	 */
	private void storeImage(BufferedImage img, int xTile, int yTile, int zoomLevel) throws IOException
	{
		// store in physical location
		File storageLocation = zoomRequest.getStorageRoot();
		
		String tileName = zoomLevel+"-"+xTile+"-"+yTile+".jpg";
		File tileLocation = new File(storageLocation, tileName);
		
		ImageIO.write(img, "jpg", tileLocation);
		
		// update DB
		updateDB(zoomRequest.requestID, zoomLevel, xTile, yTile, tileLocation.getAbsolutePath());
	}

	private void updateDB(long requestID, int zoomLevel, int xTile, int yTile, String absolutePath) throws DataAccessException
	{
		ZoomDAO zoomDao = DBImageSpaceDAOFactory.getDAOFactory().getZoomDAO();
		zoomDao.insertImage(requestID, zoomLevel, xTile, yTile, absolutePath);
	}

}
