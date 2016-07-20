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

package com.strandgenomics.imaging.iengine.system;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.TileDAO;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.zoom.ImageTilingTask;
import com.strandgenomics.imaging.iengine.zoom.TileNotReadyException;
import com.strandgenomics.imaging.iengine.zoom.TileParameters;
import com.strandgenomics.imaging.iengine.zoom.TilingRequest;
import com.strandgenomics.imaging.iengine.zoom.TilingService;
import com.strandgenomics.imaging.iengine.zoom.TilingServiceImpl;

/**
 * manages tiles of large images
 * @author navneet
 *
 */
public class LargeImageManager extends SystemManager {
	
	/**
	 * Dimension of tile on map
	 */
	int tileDimension = 256;
	
	/**
	 *  Object of tiling service
	 */
	private static TilingService service = null;
	
	/**
	 * checks if tile for given guid and zoom reverse level is ready
	 * @param guid
	 * @param zoomReverseLevel
	 * @return
	 * @throws DataAccessException
	 */
	public boolean isTileReady(long guid, int zoomReverseLevel) throws DataAccessException{
		
		TileDAO tileDAO = ImageSpaceDAOFactory.getDAOFactory().getTileDAO();
		
		return tileDAO.isTileReady(guid, zoomReverseLevel);
	}
	
	/**
	 * determines the reverse zoom level using required tile dimensions and image dimensions
	 * @param guid
	 * @param tilePosition
	 * @throws DataAccessException 
	 */
	public int getZoomReverseLevel(long guid, Rectangle roi) throws DataAccessException{
		
		Record record = ImageSpaceDAOFactory.getDAOFactory().getRecordDAO().findRecord(guid);
		
		int requestedImageDimension = Math.max(roi.width, roi.height);
		
		int imageDimension = requestedImageDimension == roi.width ? record.imageWidth : record.imageHeight;
		
		return (int)Math.ceil(Math.log((double)requestedImageDimension/tileDimension)/Math.log(2));
	}
	
	/**
	 * determine if the requested tile is large and has to fetched from cache
	 * @param guid
	 * @param tilePosition
	 * @return
	 * @throws DataAccessException 
	 */
	public boolean isLargeTile(Rectangle tilePosition) throws DataAccessException{
		
		return tilePosition.width > Constants.MAX_TILE_WIDTH || tilePosition.height > Constants.MAX_TILE_HEIGHT;
		
	}
	
	/**
	 * @throws IOException 
	 * @throws DataAccessException 
	 * returns the large tile which is ready
	 * if tile has not been created it submits a request for tile creation
	 * @param tileParameters
	 * @return
	 * @throws  
	 */
	public BufferedImage getLargeTile(String actor, long guid, Rectangle roi) throws TileNotReadyException, IOException{

		TileDAO tileDAO = ImageSpaceDAOFactory.getDAOFactory().getTileDAO();	
		
		int zoomReverseLevel = getZoomReverseLevel(guid,roi);
		
		logger.logp(Level.INFO, "LargeImageManager", "getLargeTile"," isTileAlreadyPresent="+tileDAO.isTileAlreadyPresent(guid , zoomReverseLevel));
		
		if(!tileDAO.isTileAlreadyPresent(guid , zoomReverseLevel)){
			
			TilingRequest request = toTilingRequest(actor, guid);
			startTiling(request);
			
			throw new TileNotReadyException("Tile not ready");
		}
		
		if(isTileReady(guid, zoomReverseLevel)){
			
			String storagePath = tileDAO.getTilePath(guid, zoomReverseLevel);
			
			try {
				
				String tileStorageRoot = Constants.getStringProperty(Property.ZOOM_STORAGE_LOCATION, null);

				File tileStorageDir = new File(tileStorageRoot, "RecordID_"+guid);
				
				//File file = new File("/home/navneet/imanage_storage/zoom_storage/RecordID_125/PixelArray/"+zoomReverseLevel);
				
				//BufferedImage img = ImageIO.read(new File(tileStorageDir,storagePath));
				
				logger.logp(Level.INFO, "LargeImageManager", "getLargeTile","zoom="+zoomReverseLevel+" x="+roi.getX()+" y="+roi.getY());
						
				Record r = SysManagerFactory.getRecordManager().findRecord(actor, guid);
				
				int imageWidth = r.imageWidth;
				int imageHeight = r.imageHeight;
				
				int scale = (int)Math.pow(2, zoomReverseLevel);
				int tileX = (int) Math.floor((double)roi.getX() / (tileDimension* scale));
				int tileY = (int) Math.floor((double)roi.getY() / (tileDimension* scale));
				
				imageWidth = imageWidth / scale;
				imageHeight = imageHeight / scale;
				
				int width = tileX * tileDimension+tileDimension < imageWidth ? tileDimension : imageWidth- tileX *tileDimension;
				int height = tileY * tileDimension+tileDimension < imageHeight ? tileDimension : imageHeight- tileY *tileDimension;
				
				logger.logp(Level.INFO, "LargeImageManager", "getLargeTile","tileX="+tileX+" tileY="+tileY+" width="+width+" height="+height);
				
//				PixelArray imageArray = PixelArray.read(new FileInputStream(new File(tileStorageRoot+"/RecordID_"+guid+"/PixelArray/"+tileX+"_"+tileY+"_"+zoomReverseLevel)));
//
//				BufferedImage img = convertToRGBImage(imageArray);
				
				PixelArray wholeImageArrray = PixelArray.read(new FileInputStream(new File(tileStorageRoot+"/RecordID_"+guid+"/PixelArray/"+zoomReverseLevel)));
				
//				BufferedImage wholeImage = convertToRGBImage(wholeImageArrray);
//				ImageIO.write(wholeImage, "png", new File("/home/navneet/125_test/"+zoomReverseLevel+".png"));
				
				BufferedImage img = convertToRGBImage(wholeImageArrray.getSubArray(tileX *tileDimension, tileY *tileDimension, width, height));
				
//				BufferedImage tileImage = img.getSubimage(tileX *tileDimension, tileY *tileDimension, width, height);	
//				
//				BufferedImage thisIsInsane = new BufferedImage(tileDimension, tileDimension, BufferedImage.TYPE_INT_ARGB);
//				Graphics gfx = thisIsInsane.getGraphics();
//				gfx.drawImage(img, 0, 0, width, height, null);
//				gfx.dispose();
				
				//PixelArray array = PixelArray.toPixelArray(thisIsInsane);
				
				//array.write(new FileOutputStream(new File(tileStorageRoot+"/RecordID_"+guid+"/PixelArray/"+tileX+"_"+tileY+"_"+zoomReverseLevel)));
				return img;
				
			} catch (IOException e) {
				
				throw new TileNotReadyException("Tile not found");
				
			}
		}
		
		throw new TileNotReadyException("Tile not ready");
	}

	private BufferedImage convertToRGBImage(PixelArray rawData) 
	{
		logger.logp(Level.INFO, "LargeImageManager", "getLargeTile","image type="+rawData.getType());
		switch (rawData.getType()) {
		case BYTE:
			byte[] byteData = (byte[]) rawData.getPixelArray();
			
			int[] convertedByteData = new int[byteData.length];
			
			logger.logp(Level.INFO, "LargeImageManager", "getLargeTile","data length="+byteData.length/4+" "+rawData.getWidth()+" "+rawData.getHeight());			
			for(int i=0;i<byteData.length;i++)
			{
				convertedByteData[i] = byteData[i];
			}
			
			return PixelArray.getRGBAImage(rawData.getWidth(), rawData.getHeight(), convertedByteData);
			
		case SHORT:
			short[] shortData = (short[]) rawData.getPixelArray();
			
			int[] convertedShortData = new int[shortData.length];
			for(int i=0;i<shortData.length;i++)
			{
				convertedShortData[i] = shortData[i];
			}
			
			return PixelArray.getRGBAImage(rawData.getWidth(), rawData.getHeight(), convertedShortData);
		case INT:
			return PixelArray.getRGBAImage(rawData.getWidth(), rawData.getHeight(), (int[]) rawData.getPixelArray());
		}
		
		return null;
	}
	
	/**
	 * start tiling process for specified guid and zoom level
	 * @param guid
	 * @param zoomReverseLevel
	 * @throws RemoteException 
	 */
	private void startTiling(TilingRequest request, int zoomReverseLevel) throws RemoteException{
		
		ImageTilingTask imageTilingTask = new ImageTilingTask(request);
		
		submitTilingTask(imageTilingTask);
	}
	
	/**
	 * start tiling process for specified guid for all necessary zoom levels
	 * @param guid
	 * @throws RemoteException 
	 */
	private void startTiling(TilingRequest request) throws RemoteException{
		
		ImageTilingTask imageTilingTask = new ImageTilingTask(request);
		
		submitTilingTask(imageTilingTask);
	}
	
	/**
	 * submit the tiling task to executor service
	 * @param task
	 * @throws RemoteException 
	 */
	private void submitTilingTask(ImageTilingTask task) throws RemoteException{
		logger.logp(Level.INFO, "LargeImageManager", "submitTilingTask","here");
		service = TilingServiceImpl.getInstance();
		
		service.submitTilingTask(task);
	}
	
	/**
	 * create object of tilingRequest
	 * @return
	 * @throws IOException 
	 */
	private TilingRequest toTilingRequest(String actor, long guid) throws IOException{
		
		Dimension dim = new Dimension(0, 0, 0, 0);
		
		TilingRequest request = new TilingRequest(actor, guid, dim);
		
		return request;
	}

}
