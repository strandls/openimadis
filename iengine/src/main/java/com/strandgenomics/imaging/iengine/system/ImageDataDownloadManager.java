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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.Arrays;

import javax.imageio.ImageIO;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelArray.Integer;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.ImageUtil;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.zoom.TileNotReadyException;

/**
 * class to manage all image data related url generation methods
 * 
 * @author Anup Kulkarni
 */
public class ImageDataDownloadManager extends SystemManager {
	
	ImageDataDownloadManager() {}
	
	/**
	 * Sends the images required for 3D viewing
	 * @param outputStream
	 * @param actorLogin
	 * @param guid
	 * @param frameNo
	 * @param siteNo
	 * @param imageWidth
	 * @param useChannelColor
	 * @throws Exception 
	 */
	public void sendChannelOverlaidSlices(OutputStream outputStream, String actorLogin, long guid,
			int frameNo, int siteNo,int imageWidth, boolean useChannelColor) throws Exception
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		Record record = recordDao.findRecord(guid);
		
		int imageHeight = record.imageWidth == imageWidth ? record.imageHeight : 
			(int) Math.floor((imageWidth * ((double)record.imageHeight/(double)record.imageWidth)) + 0.5); 

		File archiveFile = null;
		File[] sliceImages = new File[record.numberOfSlices];
		
		try
		{
			for(int sliceNo = 0; sliceNo < record.numberOfSlices; sliceNo++)
			{
				Set<Dimension> overlayDimensions = getOverlayDimension(record, frameNo, siteNo, sliceNo);
				BufferedImage image = createOverlayImage(actorLogin, guid, overlayDimensions, useChannelColor);
				
				if(record.imageWidth != imageWidth)
				{
					image = Util.resizeImage(image, imageWidth, imageHeight);
				}
				
				sliceImages[sliceNo] = File.createTempFile(guid+"_slice"+sliceNo, ".png", Constants.TEMP_DIR);
				sliceImages[sliceNo].deleteOnExit();
	        	
	        	BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(sliceImages[sliceNo]));
				try
				{
					ImageIO.write(image, "PNG", output);
				}
				catch (IOException e)
				{
					output.close();
				}
				output.close();
			}
			
			archiveFile = File.createTempFile(guid+"_slices", ".tar.gz", Constants.TEMP_DIR);
			Archiver.createTarBall( archiveFile, true, sliceImages );
			
			sendFile(outputStream, archiveFile);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageManager", "sendChannelOverlaidSlices", "error serving request", ex);
			throw ex;
		}
		finally
		{
			if(archiveFile != null) archiveFile.delete();
			for(File f : sliceImages)
			{
				if(f != null) f.delete();
			}
		}
	}
	
	private BufferedImage createOverlayImage(String actorLogin, long guid, Set<Dimension> overlayDimensions, boolean useChannelColor) 
			throws IOException
	{
		//user preferences
		List<Channel> channels = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);
	
		String[] luts = new String[channels.size()];
		VisualContrast[] contrasts = new VisualContrast[channels.size()];
		
		for(int channelNo = 0;channelNo < channels.size(); channelNo++)
		{
			Channel ithChannel = channels.get(channelNo);
			
			luts[channelNo] = !useChannelColor ? "grays" : ithChannel.getLut();
			contrasts[channelNo] = ithChannel.getContrast(false);
		}
		
		List<PixelArray> rawDataList = new ArrayList<PixelArray>();

		for(Dimension imageCoordinate : overlayDimensions)
		{
			PixelArray rawData = SysManagerFactory.getImageManager().getRawData(actorLogin, guid, imageCoordinate, null);  
			
			rawData.setColorModel(LutLoader.getInstance().getLUT(luts[imageCoordinate.channelNo]));
			if(contrasts[imageCoordinate.channelNo] != null) 
			{
				rawData.setContrast(contrasts[imageCoordinate.channelNo].getMinIntensity(), contrasts[imageCoordinate.channelNo].getMaxIntensity());
				rawData.setGamma(contrasts[imageCoordinate.channelNo].getGamma());
			}

			rawDataList.add(rawData);
		}
		
		PixelArray[] pixelArrays = rawDataList.toArray(new PixelArray[0]);
		return PixelArray.getOverlayImage(pixelArrays);
	}
	
	private Set<Dimension> getOverlayDimension(Record record, int frameNo, int siteNo, int sliceNo) 
	{
		Set<Dimension> d = new HashSet<Dimension>();
		for(int channelNo = 0; channelNo < record.numberOfChannels; channelNo++)
		{
			d.add( new Dimension(frameNo,sliceNo,channelNo,siteNo) );
		}
		return d;
	}
	
	private void sendFile(OutputStream sStream, File archiveFile) throws IOException
	{
		BufferedOutputStream oStream = null;
		
		FileInputStream fStream = null;
		BufferedInputStream iStream = null;

		try {
			oStream = new BufferedOutputStream(sStream);
			
			logger.logp(Level.INFO, "ImageManager", "sendFile", "sending data "+archiveFile +" of length "+archiveFile.length());
			
			fStream = new FileInputStream(archiveFile);
			iStream = new BufferedInputStream(fStream);

			long dataLength = Util.transferData(iStream, oStream);
			logger.logp(Level.INFO, "ImageManager", "sendFile", "successfully send file " + dataLength);
		} 
		catch (IOException ex) 
		{
			throw ex;
		} 
		finally
		{
			Util.closeStream(oStream);
			Util.closeStream(fStream);

			Util.closeStream(iStream);
			Util.closeStream(sStream);
		}
	}
	
	public PixelArray createOverlayPixelArray(String actorLogin, long guid,
			int sliceNo, int frameNo, int siteNo, int[] channelNos,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle roi)  throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		
		if(project.getStatus() != ProjectStatus.Active)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_NOT_ACTIVE));
		}
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		LargeImageManager largeImageManager = SysManagerFactory.getLargeImageManager();
		
		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
		
		int recordWidth = record.imageWidth;
		int recordHeight = record.imageHeight;
		
		int requiredWidth = roi.x+roi.width < recordWidth ? roi.width : recordWidth-roi.x; 
		int requiredHeight = roi.y+roi.height < recordHeight ? roi.height : recordHeight-roi.y; 
		
		Rectangle requiredROI = new Rectangle(roi.x, roi.y, requiredWidth, requiredHeight);
		
		PixelArray[] pixelArrays;
		
		if (largeImageManager.isLargeTile(roi)) {
			BufferedImage img = null;

			try {
				img = largeImageManager.getLargeTile(actorLogin, guid, roi);

			} catch (TileNotReadyException e) {
				throw e;
			}
		
			pixelArrays = new PixelArray[1];
			pixelArrays[0] = PixelArray.toPixelArray(img);
			
			return pixelArrays[0];
		}
		else{
		 pixelArrays = SysManagerFactory.getImageManager().getPixelArrays(actorLogin, guid, sliceNo, frameNo, siteNo, channelNos, useChannelColor, zStacked, requiredROI);
		}
		
		PixelArray renderableImage = null;
		
		if(channelNos.length > 1)
		{
			logger.logp(Level.INFO, "ImageDataDownloadManager", "createOverlayPixelArray","no of channels="+channelNos.length);
			if(mosaic)
			{
				logger.logp(Level.INFO, "ImageDataDownloadManager", "createOverlayPixelArray","mosaic");
						
				List<BufferedImage> imageList = new ArrayList<BufferedImage>();
				for(int i = 0;i < pixelArrays.length; i++)
				{
					BufferedImage image = pixelArrays[i].createImage();
					imageList.add(image);
				}
				
				BufferedImage mosaicImage = ImageUtil.createMosaicImage(imageList, pixelArrays[0].getWidth(), pixelArrays[0].getHeight(), 5);
				int[] pixels = ((DataBufferInt)mosaicImage.getData().getDataBuffer()).getData();
				//image with new DirectColorModel
				renderableImage = new PixelArray.Integer(pixels, pixelArrays[0].getWidth(), pixelArrays[0].getHeight());
			}
			else
			{
				renderableImage = PixelArray.getRGBPixels(pixelArrays);
			}
		}
		else
		{
			logger.logp(Level.INFO, "ImageDataDownloadManager", "createOverlayPixelArray","no of channels="+1);
//			renderableImage = PixelArray.getRGBPixels(pixelArrays);
			return pixelArrays[0];
		}

		return renderableImage;
	}
	
	public PixelArray createOverlayPixelArray(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle roi, int[] contrasts) throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (project.getStatus() != ProjectStatus.Active)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_NOT_ACTIVE));
		}

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		PixelArray[] pixelArrays = SysManagerFactory.getImageManager().getPixelArrays(actorLogin, guid, sliceNo, frameNo, siteNo, channelNos, useChannelColor, zStacked, roi);
		for (int i = 0; i < channelNos.length; i++)
		{
			pixelArrays[i].setContrast(contrasts[i * 2], contrasts[i * 2 + 1]);
		}

		PixelArray renderableImage = null;

		if (channelNos.length > 1)
		{
			if (mosaic)
			{
				List<BufferedImage> imageList = new ArrayList<BufferedImage>();
				for (int i = 0; i < pixelArrays.length; i++)
				{
					BufferedImage image = pixelArrays[i].createImage();
					imageList.add(image);
				}

				BufferedImage mosaicImage = ImageUtil.createMosaicImage(imageList, pixelArrays[0].getWidth(), pixelArrays[0].getHeight(), 5);
				int[] pixels = ((DataBufferInt) mosaicImage.getData().getDataBuffer()).getData();
				// image with new DirectColorModel
				renderableImage = new PixelArray.Integer(pixels, pixelArrays[0].getWidth(), pixelArrays[0].getHeight());
			}
			else
			{
				renderableImage = PixelArray.getRGBPixels(pixelArrays);
			}
		}
		else
		{
			renderableImage = PixelArray.getRGBPixels(pixelArrays);
		}

		return renderableImage;
	}
	
	/**
	 * 
	 * @param actorLogin
	 * @param guid
	 * @param sliceNo
	 * @param frameNo
	 * @param siteNo
	 * @param channels
	 * @param useChannelColor
	 * @param zStacked
	 * @param mosaic
	 * @param roi
	 * @param rectangle
	 * @return
	 * @throws IOException
	 */
	public PixelArray createOverlayPixelArray(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, int[] channels,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle roi, Rectangle rectangle) throws IOException
	{
		
		PixelArray fullData = createOverlayPixelArray(actorLogin, guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, roi);

		// scale down to fit to required rectangle
		BufferedImage img = PixelArray.getRGBImage(fullData.getWidth(), fullData.getHeight(), convertToIntegerArray(fullData));
		
        BufferedImage resizedImage = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        
        LargeImageManager largeImageManager = SysManagerFactory.getLargeImageManager();
        
        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();
        
        //scale the image if not a large tile
        //large tiles are returned with scaled dimension
        if(!largeImageManager.isLargeTile(roi)){
        	  double scale = (double) roi.getWidth()/rectangle.width;
             
              imageWidth = (int)Math.floor((double)img.getWidth()/scale);
              imageHeight = (int)Math.floor((double)img.getHeight()/scale);
        }
             
        g.drawImage(img, 0, 0, imageWidth, imageHeight, null);
        g.dispose();
        
		PixelArray scaledData = PixelArray.toPixelArray(resizedImage);
		scaledData.setColorModel(fullData.getColorModel());
		return scaledData;
	}
	
	/**
	 * convert given pixel array to an integer array
	 * @param pixelArray
	 * @return
	 */
	private int[] convertToIntegerArray(PixelArray pixelArray){
		switch(pixelArray.getType()){
		
			case BYTE : 
						Integer pixelArrayIntFormat = pixelArray.getRGBPixels(pixelArray);
						return (int []) pixelArrayIntFormat.getPixelArray();
						
			case SHORT: 
						pixelArrayIntFormat = pixelArray.getRGBPixels(pixelArray);
						return (int []) pixelArrayIntFormat.getPixelArray();
						
			case INT: return (int []) pixelArray.getPixelArray();
		}
		
		return null;
	}
	
	public PixelArray getRawTileImage(String actorLogin, long guid, Dimension imageCoordinate, 
			boolean useChannelColor, int x, int y, int width, int height) throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		
		if(project.getStatus() != ProjectStatus.Active)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_NOT_ACTIVE));
		}
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		//user preferences
		List<Channel> channels = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);
		//preferred contrast
		VisualContrast contrast = channels.get(imageCoordinate.channelNo).getContrast(false);
		
		PixelArray rawData = SysManagerFactory.getImageManager().getRawData(actorLogin, guid, imageCoordinate, new Rectangle(x, y, width, height));
		String channelLUT = channels.get(imageCoordinate.channelNo).getLut();
		rawData.setColorModel(LutLoader.getInstance().getLUT(channelLUT));
		
		if(contrast != null) 
		{
			rawData.setContrast(contrast.getMinIntensity(), contrast.getMaxIntensity());
			rawData.setGamma(contrast.getGamma());
		}
		
		return rawData;
	}
	
	public PixelArray getRawImage(String actorLogin, long guid, Dimension imageCoordinate, boolean useChannelColor) 
			throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		
		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);
		
		if(project.getStatus() != ProjectStatus.Active)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_NOT_ACTIVE));
		}
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		//user preferences
		List<Channel> channels = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);
		//preferred contrast
		VisualContrast contrast = channels.get(imageCoordinate.channelNo).getContrast(false);
		
		PixelArray rawData = SysManagerFactory.getImageManager().getRawData(actorLogin, guid, imageCoordinate, null);
		String channelLUT = channels.get(imageCoordinate.channelNo).getLut();
		rawData.setColorModel(LutLoader.getInstance().getLUT(channelLUT));
		
		if(contrast != null) 
		{
			rawData.setContrast(contrast.getMinIntensity(), contrast.getMaxIntensity());
			rawData.setGamma(contrast.getGamma());
		}
		
		return rawData;
	}
	
	/**
	 * Returns a URL to upload custom thumbnail to a record
	 * @param actorLogin  the user making the request
	 * @param guid  of parent project        
	 * @param clientIP  of client machine
	 * @return upload URL
	 * @throws DataAccessException 
	 */
	public String getThumbnailUploadURL(String actorLogin, String clientIP, long guid) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

    	logger.logp(Level.INFO, "ImageManager", "getThumbnailUploadURL", "finding thumbnail for "+guid);
    	return DataExchange.THUMBNAIL_UPLOAD.generateURL(actorLogin, clientIP, System.currentTimeMillis(), guid);
	}
	
	/**
	 * Returns a URL to download thumb-nail to a record
	 * @param actorLogin  the user making the request
	 * @param guid  of parent project        
	 * @param clientIP  of client machine
	 * @return upload URL
	 * @throws DataAccessException 
	 */
	public String getThumbnailDownloadURL(String actorLogin, String clientIP, long guid) throws DataAccessException 
	{
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

    	logger.logp(Level.INFO, "ImageManager", "getThumbnailDownloadURL", "finding thumbnail for "+guid);
    	return DataExchange.THUMBNAIL_DOWNLOAD.generateURL(actorLogin, clientIP, System.currentTimeMillis(), guid);
	}
	
	 /**
     * Returns the URL to download the raw pixel data (as a gzip-ed byte array) associated with the specified coordinate
     * @param parentRecord the record under consideration 
     * @param imageIndex the image-coordinate under consideration 
     * @return the URL to download the raw pixel data associated with the specified coordinate
	 * @throws DataAccessException 
     */
    public String getRawPixelArrayDownloadURL(String actorLogin, String clientIP, long guid, Dimension d)
    		throws DataAccessException
    {
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
    	logger.logp(Level.INFO, "ImageManager", "getRawPixelArrayDownloadURL", "finding PixelArray URL for "+guid +", image="+d);
    	return DataExchange.RAW_PIXEL_ARRAY.generateURL(actorLogin, clientIP, System.currentTimeMillis(), 
    			guid, d.frameNo, d.sliceNo, d.channelNo, d.siteNo);
    }
	
	 /**
     * Returns the URL to download the raw pixel data (as a gzip-ed byte array) associated with the specified coordinate
     * @param parentRecord the record under consideration 
     * @param imageIndex the image-coordinate under consideration 
     * @return the URL to download the raw pixel data associated with the specified coordinate
	 * @throws DataAccessException 
     */
    public String getPixelDataDownloadURL(String actorLogin, String clientIP, long guid, 
    		Dimension d, boolean useChannelColor) throws DataAccessException
    {	
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
    	logger.logp(Level.INFO, "ImageManager", "getPixelDataDownloadURL", "finding PixelData URL for "+guid+", image="+d);
    	return DataExchange.PIXEL_DATA.generateURL(actorLogin, clientIP, System.currentTimeMillis(), 
    			guid, d.frameNo, d.sliceNo, d.channelNo, d.siteNo, useChannelColor);
    }
    
    /**
     * Returns the URL to download the pixel-data of the specified Tile/Block as a gzip-ed byte array
     * @param parentRecord the record under consideration 
	 * @param imageIndex the image-coordinate under consideration 
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     * @throws DataAccessException 
     */
    public String getTilePixelArrayDownloadURL(String actorLogin, String clientIP, long guid, Dimension d, 
    		int x, int y, int width, int height) throws DataAccessException
    {
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
    	logger.logp(Level.INFO, "ImageManager", "getTilePixelArrayDownloadURL", "finding Raw Tile URL for "+guid+", image="+d);
    	return DataExchange.RAW_TILE_ARRAY.generateURL(actorLogin, clientIP, System.currentTimeMillis(), 
    			guid, d.frameNo, d.sliceNo, d.channelNo, d.siteNo, x, y, width, height);
    }

    /**
     * Returns the URL to download the pixel-data of the specified Tile/Block as a gzip-ed byte array
     * @param parentRecord the record under consideration 
	 * @param imageIndex the image-coordinate under consideration 
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     * @throws DataAccessException 
     */
    public String getTileDownloadURL(String actorLogin, String clientIP, long guid,
    		Dimension d, boolean useChannelColor,
    		int x, int y, int width, int height) throws DataAccessException 
    {
    	ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();
		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
    	logger.logp(Level.INFO, "ImageManager", "getTileDownloadURL", "finding Tile URL for "+guid+", image="+d);
    	
    	return DataExchange.TILE.generateURL(actorLogin, clientIP, System.currentTimeMillis(), 
    			guid, d.frameNo, d.sliceNo, d.channelNo, d.siteNo, useChannelColor, x, y, width, height);
    }

    /**
     * Returns the URL to download the pixel-data overlay as a gzip-ed byte array
     * @param parentRecord the record under consideration 
     * @param channels list of channels to overlay
     * @param useChannelColor whether to use channel colors
     * @param zStacked whether to do z-stack projections (max intensity value) per channels
     * @param mosaic whether to stack the images or tile them (mosaic)
     * @return the URL to download the pixel-data overlay as a image
     * @throws DataAccessException 
     */
    public String getPixelDataOverlayDownloadURL(String actorLogin, String clientIP, long guid, 
    		int sliceNo, int frameNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height) throws DataAccessException 
    {
    	if(channels == null || channels.length == 0) return null;

		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int i = 0;
		java.lang.Integer[] channelParameters = new java.lang.Integer[channels.length];
      	for(int channel:channels)
      		channelParameters[i++] = channel;
      	
      	Object[] defaultParameters = {guid, frameNo, sliceNo, siteNo, useChannelColor, zStacked, mosaic, x, y, width, height};
    	Object[] parameters = new Object[defaultParameters.length + channels.length];
    	
    	System.arraycopy(defaultParameters, 0, parameters, 0, defaultParameters.length);
    	System.arraycopy(channelParameters, 0, parameters, defaultParameters.length, channelParameters.length);
    	
      	logger.logp(Level.INFO, "ImageManager", "getPixelDataOverlayDownloadURL", "finding overlay URL for "+parameters);

    	return DataExchange.OVERLAY.generateURL(actorLogin, clientIP, System.currentTimeMillis(), parameters);
    }
    
    /**
     * 
     * @param actorLogin
     * @param clientIP
     * @param guid
     * @param sliceNo
     * @param frameNo
     * @param siteNo
     * @param channels
     * @param useChannelColor
     * @param zStacked
     * @param mosaic
     * @param x
     * @param y
     * @param width
     * @param height
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws DataAccessException
     */
    public String getPixelDataOverlayDownloadURL(String actorLogin, String clientIP, long guid, 
    		int sliceNo, int frameNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height, int reqWidth, int reqHeight) throws DataAccessException 
    {
    	if(channels == null || channels.length == 0) return null;

		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int i = 0;
		java.lang.Integer[] channelParameters = new java.lang.Integer[channels.length];
      	for(int channel:channels)
      		channelParameters[i++] = channel;
      	
      	Object[] defaultParameters = {guid, frameNo, sliceNo, siteNo, useChannelColor, zStacked, mosaic, x, y, width, height, reqWidth, reqHeight};
    	Object[] parameters = new Object[defaultParameters.length + channels.length];
    	
    	System.arraycopy(defaultParameters, 0, parameters, 0, defaultParameters.length);
    	System.arraycopy(channelParameters, 0, parameters, defaultParameters.length, channelParameters.length);
    	
      	logger.logp(Level.INFO, "ImageManager", "getPixelDataOverlayDownloadURL", "finding overlay URL for "+parameters);

    	return DataExchange.OVERLAY_RESCALE.generateURL(actorLogin, clientIP, System.currentTimeMillis(), parameters);
    }
    
    public String getPixelDataOverlayWithContrastDownloadURL(String actorLogin, String clientIPAddress, long guid, int sliceNo, int frameNo, int siteNo, int[] channels, boolean useChannelColor,
			boolean zStacked, boolean mosaic, int x, int y, int width, int height, int[] contrasts) throws DataAccessException
	{
    	if(channels == null || channels.length == 0) return null;

		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int i = 0;
		java.lang.Integer[] channelParameters = new java.lang.Integer[channels.length];
      	for(int channel:channels)
      		channelParameters[i++] = channel;
      	
      	java.lang.Integer[] contrastParameters = new java.lang.Integer[contrasts.length];
      	i=0;
      	for(int contrast:contrasts)
      		contrastParameters[i++] = contrast;
      	
      	Object[] defaultParameters = {guid, frameNo, sliceNo, siteNo, useChannelColor, zStacked, mosaic, x, y, width, height};
    	Object[] parameters = new Object[defaultParameters.length + channels.length + contrasts.length];
    	
    	System.arraycopy(defaultParameters, 0, parameters, 0, defaultParameters.length);
    	System.arraycopy(channelParameters, 0, parameters, defaultParameters.length, channelParameters.length);
    	System.arraycopy(contrastParameters, 0, parameters, defaultParameters.length + channelParameters.length, contrastParameters.length);
    	
      	logger.logp(Level.INFO, "ImageManager", "getPixelDataOverlayDownloadURL", "finding overlay URL for "+parameters);

    	return DataExchange.OVERLAYWITHCONTRAST.generateURL(actorLogin, clientIPAddress, System.currentTimeMillis(), parameters);
	}

    /**
     * Returns the URL to download the images for all slices from the specified frame and site
     * and overlaid on all channels (and possibly scaled downed to a low value)
     * This is needed for 3D viewing
     * @param actorLogin the user making the call
     * @param guid the record under consideration 
     * @param frameNo the frame number
     * @param siteNo the site number
     * @param imageWidth image width of the requested images (for scaling)
     * @param useChannelColor specifies whether to use channel colors
     * @return the URL to download the pixel-data overlay as an image
     */
	public String getChannelOverlaidSliceImagesURL(String actorLogin, String clientIP, long guid,
			int frameNo, int siteNo, int imageWidth, boolean useChannelColor)  throws DataAccessException
	{		
		if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		return DataExchange.CHANNEL_OVERLAID_SLICES.generateURL(actorLogin, clientIP, System.currentTimeMillis(), 
				guid, frameNo, siteNo, imageWidth, useChannelColor);
	}

}
