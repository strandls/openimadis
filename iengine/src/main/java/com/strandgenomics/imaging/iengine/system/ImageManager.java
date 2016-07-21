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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

import loci.formats.gui.BufferedImageReader;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.bioformats.BufferedImageReaderPool;
import com.strandgenomics.imaging.icore.bioformats.ImageReaderException;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.icore.util.ImageUtil;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.icore.vo.VisualObjectType;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.dao.ImagePixelDataDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.RecordDAO;
import com.strandgenomics.imaging.iengine.dao.VisualOverlaysRevisionDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.LegendField;
import com.strandgenomics.imaging.iengine.models.PixelArrayTile;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.movie.CacheRequest;
import com.strandgenomics.imaging.iengine.movie.MovieService;
import com.strandgenomics.imaging.iengine.zoom.TileNotReadyException;
import com.strandgenomics.imaging.iengine.zoom.TileParameters;

/**
 * maintains a cache of BufferedImageReaders for most recently used archives One
 * can use ImageIO.write(image, "PNG", outStream); to dump the BufferedImage as
 * a PNG image to a stream
 * 
 * @author arunabha
 * 
 */
public class ImageManager extends SystemManager {

	/**
	 * size of the image cache
	 */
	private static final long imageCacheSize = 10 * 1024 * 1024 * 1024L;

	ImageManager()
	{
	}

	/**
	 * Gets image pixel data for the given record and record attributes
	 * 
	 * @param actorLogin
	 *            login user
	 * @param guid
	 *            record id
	 * @param coordinate
	 *            image coordinate - slice, frame,channel and site
	 * @return image pixel data if valid record is available. Else
	 *         <code>null</code>
	 * @throws IOException
	 */
	public ImagePixelData getImageMetaData(String actorLogin, long guid, Dimension coordinate) throws IOException
	{
		logger.logp(Level.FINEST, "ImageManager", "getImageMetaData", "guid=" + guid + " for " + actorLogin);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();

		RecordDAO recordDao = factory.getRecordDAO();
		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		ImagePixelDataDAO imagePixelDataDAO = factory.getImagePixelDataDAO();

		return imagePixelDataDAO.find(guid, coordinate);
	}

	// /**
	// * Returns the image statistics for the specified image within the
	// specified record
	// * @param actorLogin the user making the request
	// * @param guid the guid of the record under consideration
	// * @param coordinate the coordinate of the image under consideration
	// * @param zStackOn true if z-stack is on; false otherwise
	// * @return the image statistics stuff
	// * @throws IOException
	// */
	// public Histogram getIntensityDistibution(String actorLogin, long guid,
	// Dimension coordinate, boolean zStackOn) throws IOException
	// {
	// if(!SysManagerFactory.getUserPermissionManager().canRead(actorLogin,
	// guid))
	// throw new ImagingEngineException(new
	// ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
	//
	// PixelArray rawData = null;
	// if(zStackOn)
	// {
	// ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
	// RecordDAO recordDao = factory.getRecordDAO();
	// Dimension maxDimension = recordDao.getDimension(guid);
	//
	// rawData = getRawData(actorLogin, guid, coordinate, null);
	//
	// for(int slice = 0; slice < maxDimension.sliceNo; slice++)
	// {
	// if(slice == coordinate.sliceNo) continue;
	// PixelArray another = getRawData(actorLogin, guid, new
	// Dimension(coordinate.frameNo, slice, coordinate.channelNo,
	// coordinate.siteNo), null);
	// rawData.overlay(another);
	// }
	// }
	// else
	// {
	// rawData = getRawData(actorLogin, guid, coordinate, null);
	// }
	// return rawData.computeIntensityDistribution();
	// }

	/**
	 * Returns the image statistics for the specified image within the specified
	 * record
	 * 
	 * @param actorLogin
	 *            the user making the request
	 * @param guid
	 *            the guid of the record under consideration
	 * @param coordinate
	 *            the coordinate of the image under consideration
	 * @param zStackOn
	 *            true if z-stack is on; false otherwise
	 * @param tile
	 *            region of interest
	 * @return the image statistics stuff
	 * @throws IOException
	 */
	public Histogram getIntensityDistibution(String actorLogin, long guid, Dimension coordinate, boolean zStackOn, Rectangle tile) throws IOException
	{
		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, guid))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		PixelArray rawData = null;
		if (zStackOn)
		{
			ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
			RecordDAO recordDao = factory.getRecordDAO();
			Dimension maxDimension = recordDao.getDimension(guid);

			rawData = getRawData(actorLogin, guid, coordinate, tile);

			for (int slice = 0; slice < maxDimension.sliceNo; slice++)
			{
				if (slice == coordinate.sliceNo)
					continue;
				PixelArray another = getRawData(actorLogin, guid, new Dimension(coordinate.frameNo, slice, coordinate.channelNo, coordinate.siteNo),
						tile);
				rawData.overlay(another);
			}
		}
		else
		{
			rawData = getRawData(actorLogin, guid, coordinate, tile);
		}
		return rawData.computeIntensityDistribution();
	}

	/**
	 * Get raw data for the given record and image dimensions. Optionally a
	 * tilePosition can also be specified for fetching data only for a tile.
	 * 
	 * @param actorLogin
	 *            current logged in user
	 * @param guid
	 *            guid of the record
	 * @param imageCoordinate
	 *            dimensions of the image required in the record
	 * @param tilePosition
	 *            position of tile. <code>null</code> for the whole image
	 * @return raw data for the specified params
	 * @throws IOException
	 */
	public PixelArray getRawData(String actorLogin, long guid, Dimension imageCoordinate, Rectangle tilePosition) throws IOException
	{
		logger.logp(Level.FINEST, "ImageManager", "getRawData", "guid=" + guid + " for " + imageCoordinate);

		int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (project.getStatus() != ProjectStatus.Active)
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.PROJECT_NOT_ACTIVE));
		}

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		return getRawData(guid, imageCoordinate, tilePosition);
	}

	/**
	 * submits the rawdata prefetching request
	 * 
	 * @param actorLogin
	 *            current logged in user
	 * @param guid
	 *            guid of the record
	 * @param startingCoordinate
	 *            dimension from which the data is to be prefetched
	 * @throws DataAccessException
	 */
	public void submitRawDataPrefetchRequest(String actorLogin, long guid, Dimension startingCoordinate) throws DataAccessException
	{
		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);

		try
		{
			Registry registry = LocateRegistry.getRegistry(Constants.getMovieServicePort());
			MovieService serviceStub = null;

			serviceStub = (MovieService) registry.lookup(MovieService.class.getCanonicalName());

			boolean onFrames = record.numberOfSlices < record.numberOfFrames;
			int otherCoorinate = onFrames ? startingCoordinate.sliceNo : startingCoordinate.frameNo;
			int ithImage = onFrames ? startingCoordinate.frameNo : startingCoordinate.sliceNo;

			List<Integer> channels = new ArrayList<Integer>();
			for (int i = 0; i < record.numberOfChannels; i++)
			{
				channels.add(i);
			}

			serviceStub.submitCacheRequest(actorLogin, new CacheRequest(guid, actorLogin, guid, startingCoordinate.siteNo, onFrames, otherCoorinate,
					channels), ithImage);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "MovieManager", "submitVideoRequest", "error submitting caching request for images for record " + guid + ")");
		}
	}

	private PixelArray getRawData(long guid, Dimension imageCoordinate, Rectangle tilePosition) throws IOException
	{
		logger.logp(Level.INFO, "ImageManager", "getRawData", "get raw data for " + guid + " dim=" + imageCoordinate);

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();
		Record record = recordDao.findRecord(guid);
		if (tilePosition == null && (record.imageHeight > Constants.MAX_TILE_HEIGHT || record.imageWidth > Constants.MAX_TILE_WIDTH))
		{
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.IMAGE_SIZE_EXCEPTION));
		}

		Rectangle tileCache = tilePosition;
		if (tileCache == null)
		{
			java.awt.Dimension size = recordDao.getImageSize(guid);
			tileCache = new Rectangle(0, 0, size.width, size.height);
		}
		if (isCached(guid, imageCoordinate, tileCache))
		{
			PixelArray cachedData = getCachedRawData(guid, imageCoordinate, tileCache);

			if (cachedData != null)
			{
				return cachedData;
			}
		}

		BigInteger sign = recordDao.getArchiveSignature(guid);
		logger.logp(Level.INFO, "ImageManager", "getRawData", "getting image reader pool for guid=" + guid + " sign=" + sign);
		BufferedImageReaderPool pool;
		try
		{
			pool = SysManagerFactory.getImageReadersManager().getPool(sign);
		}
		catch (ImageReaderException e1)
		{
			logger.logp(Level.WARNING, "ImageManager", "getRawData", "image reader pool unavailable for guid=" + guid);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.IMAGE_READER_UNAVAILABLE));
		}
		logger.logp(Level.INFO, "ImageManager", "getRawData", "got image reader pool for guid=" + guid);
		List<Site> sites = recordDao.getSites(guid);

		logger.logp(Level.INFO, "ImageManager", "getRawData", "getting image reader for guid=" + guid);
		BufferedImageReader imageReader;
		try
		{
			imageReader = pool.getImageReader();
		}
		catch (ImageReaderException e1)
		{
			logger.logp(Level.WARNING, "ImageManager", "getRawData", "image reader unavailable for guid=" + guid);
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.IMAGE_READER_UNAVAILABLE));
		}
		imageReader.setSeries(sites.get(imageCoordinate.siteNo).getSeriesNo());

		logger.logp(Level.INFO, "ImageManager", "getRawData", "got image reader for guid=" + guid);
		int index = imageReader.getIndex(imageCoordinate.sliceNo, imageCoordinate.channelNo, imageCoordinate.frameNo);
		BufferedImage image = null;
		try
		{
			long startTime = System.currentTimeMillis();
			// the buffered imaged returned here if of unknown type
			if (tilePosition == null)
				image = imageReader.openImage(index);
			else
				image = imageReader.openImage(index, tilePosition.x, tilePosition.y, tilePosition.width, tilePosition.height);

			long endTime = System.currentTimeMillis();

			logger.logp(Level.INFO, "ImageManager", "getRawData", "Read RawData of guid=" + guid + " @ " + imageCoordinate + " in "
					+ (endTime - startTime) + " ms");
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "getRawData", "guid=" + guid + " for " + imageCoordinate, e);
			throw new IOException("format exception", e);
		}
		finally
		{
			try
			{
				logger.logp(Level.INFO, "ImageManager", "getRawData", "closing the image reader");
				imageReader.close(); // return it back to the pool
			}
			catch (Exception ex)
			{
				logger.logp(Level.WARNING, "ImageManager", "getRawData", "error in closing the image reader", ex);
			}
		}

		PixelArray pixelArray = PixelArray.toPixelArray(image);
		logger.logp(Level.INFO, "ImageManager", "getRawData", "writting image cache");
		writeToCache(guid, imageCoordinate, tileCache, pixelArray);

		return pixelArray;
	}

	private PixelArray getCachedRawData(long guid, Dimension imageCoordinate, Rectangle tilePosition)
	{
		try
		{
			PixelArrayTile tile = ImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().getTile(guid, imageCoordinate, tilePosition);

			logger.logp(Level.INFO, "ImageManager", "getCachedRawData", "reading image tile from cache");

			DBImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().updateTimestamp(guid);

			if (tile.getImageHeight() != tilePosition.height || tile.getImageWidth() != tilePosition.width)
				return tile.getRawData().getSubArray(tilePosition.x - tile.getX(), tilePosition.y - tile.getY(), tilePosition.width,
						tilePosition.height);

			return tile.getRawData();
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ImageManager", "getCachedRawData", "unable to read image tile cache", e);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "getCachedRawData", "unable to read image tile cache", e);
		}
		return null;
	}

	/**
	 * record is deleted or transfered. delete all the cached data for this
	 * record
	 * 
	 * @param guid
	 */
	void deleteCachedData(long guid)
	{
		try
		{
			ImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().deleteAllTiles(guid);

			String cacheStoreDir = Constants.getStringProperty(Property.IMAGE_CACHE_STORAGE_LOCATION, null);

			File recordCache = new File(cacheStoreDir, "RecordID_" + guid);
			Util.deleteTree(recordCache);
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ImageManager", "deleteCachedData", "unable to delete cache", e);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "deleteCachedData", "unable to delete cache", e);
		}
	}

	private boolean isCached(long guid, Dimension imageCoordinate, Rectangle tilePosition) throws DataAccessException
	{
		try
		{
			PixelArrayTile tile = ImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().getTile(guid, imageCoordinate, tilePosition);

			logger.logp(Level.WARNING, "ImageManager", "isCached", "image tile is cached " + (tile != null));

			return (tile != null);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ImageManager", "isCached", "unable to read image tile cache", e);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "isCached", "unable to read image tile cache", e);
		}
		return false;
	}

	private void writeToCache(long guid, Dimension imageCoordinate, Rectangle tilePosition, PixelArray rawData) throws IOException
	{
		if (!isMemoryAvailable(rawData))
		{
			logger.logp(Level.INFO, "ImageManager", "writeToCache", "not enough memory");
			return;
		}

		String cacheStoreDir = Constants.getStringProperty(Property.IMAGE_CACHE_STORAGE_LOCATION, null);

		File recordCache = new File(cacheStoreDir, "RecordID_" + guid);
		recordCache.mkdirs();

		String filepath = imageCoordinate.frameNo + "_" + imageCoordinate.sliceNo + "_" + imageCoordinate.channelNo + "_" + imageCoordinate.siteNo
				+ "_" + tilePosition.x + "_" + tilePosition.y + "_" + tilePosition.width + "_" + tilePosition.height + ".bin";

		logger.logp(Level.INFO, "ImageManager", "writeToCache", "writting cache to file " + filepath);

		ObjectOutputStream oos = null;
		try
		{
			File f = new File(recordCache, filepath);
			oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
			oos.writeObject(rawData);

			PixelArrayTile tile = new PixelArrayTile(guid, imageCoordinate, tilePosition, filepath);
			ImageSpaceDAOFactory.getDAOFactory().getImageTileCacheDAO().setTile(tile);

			Long currentSize = (Long) SysManagerFactory.getCacheManager().get(new CacheKey("RawDataMonitoring", CacheKeyType.Misc));
			if (currentSize != null)
				SysManagerFactory.getCacheManager().set(new CacheKey("RawDataMonitoring", CacheKeyType.Misc),
						(currentSize + rawData.getBytes().length));
		}
		catch (FileNotFoundException e)
		{
			logger.logp(Level.WARNING, "ImageManager", "writeToCache", "unable to write image tile cache", e);
		}
		catch (IOException e)
		{
			logger.logp(Level.WARNING, "ImageManager", "writeToCache", "unable to write image tile cache", e);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ImageManager", "writeToCache", "unable to write image tile cache", e);
		}
		finally
		{
			if (oos != null)
				oos.close();
		}
	}

	private boolean isMemoryAvailable(PixelArray rawData)
	{
		long byteLength = rawData.getBytes().length;

		// initialize to max value
		long currentSize = imageCacheSize;
		try
		{
			currentSize = (Long) SysManagerFactory.getCacheManager().get(new CacheKey("RawDataMonitoring", CacheKeyType.Misc));
		}
		catch (Exception e)
		{
			return false;
		}

		logger.logp(Level.INFO, "ImageManager", "isMemoryAvailable", "current size=" + currentSize + " byteLength=" + byteLength
				+ " image cache size=" + imageCacheSize);

		if (currentSize + byteLength <= imageCacheSize)
		{
			logger.logp(Level.INFO, "ImageManager", "isMemoryAvailable", "memory is available");
			return true;
		}

		return false;
	}

	/**
	 * Tile based access analogous to
	 * {@link #getPixelDataOverlay(String, long, int, int, int, List, boolean, boolean, boolean)}
	 * . Get a particular tile based on the tilePosition.
	 * 
	 * @param actorLogin
	 * @param guid
	 * @param sliceNo
	 * @param frameNo
	 * @param siteNo
	 * @param channelList
	 * @param useChannelColor
	 * @param zStacked
	 * @param mosaic
	 * @param tilePosition
	 * @return
	 * @throws IOException
	 */
	public BufferedImage getTileImage(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, List<Integer> channelList,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle tilePosition) throws IOException
	{
		logger.logp(Level.FINEST, "ImageManager", "getTileImage", "guid=" + guid + " for " + channelList);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int[] channels = new int[channelList.size()];
		for (int i = 0; i < channels.length; i++)
			channels[i] = channelList.get(i);

		return createOverlay(actorLogin, guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, tilePosition, null);
	}

	/**
	 * Returns the BufferedImage
	 * 
	 * @param actorLogin
	 *            the user making the call
	 * @param the
	 *            record guid
	 * @param channels
	 *            list of channels to overlay
	 * @param useChannelColor
	 *            whether to use channel colors
	 * @param zStacked
	 *            whether to do z-stack projections (max intensity value) per
	 *            channels
	 * @param mosaic
	 *            whether to stack the images or tile them (mosaic)
	 * @return the create BufferedImage
	 */
	public BufferedImage getPixelDataOverlay(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, List<Integer> channelList,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle tilePosition) throws IOException
	{
		logger.logp(Level.FINEST, "ImageManager", "getPixelDataOverlay", "guid=" + guid + " for " + channelList);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int[] channels = new int[channelList.size()];
		for (int i = 0; i < channels.length; i++)
			channels[i] = channelList.get(i);

		return createOverlay(actorLogin, guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, tilePosition, null);
	}

	/**
	 * Returns the BufferedImage
	 * 
	 * @param actorLogin
	 *            the user making the call
	 * @param the
	 *            record guid
	 * @param channels
	 *            list of channels to overlay
	 * @param useChannelColor
	 *            whether to use channel colors
	 * @param zStacked
	 *            whether to do z-stack projections (max intensity value) per
	 *            channels
	 * @param mosaic
	 *            whether to stack the images or tile them (mosaic)
	 * @param visualOverlays
	 *            list of selected visual overlays
	 * @return the create BufferedImage
	 */
	public BufferedImage getPixelDataOverlay(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, List<Integer> channelList,
			boolean useChannelColor, boolean zStacked, boolean mosaic, List<String> visualOverlays, Rectangle tilePosition) throws IOException
	{
		long start = System.currentTimeMillis();

		logger.logp(Level.FINEST, "ImageManager", "getPixelDataOverlay", "guid=" + guid + " for " + channelList);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();

		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int[] channels = new int[channelList.size()];
		for (int i = 0; i < channels.length; i++)
			channels[i] = channelList.get(i);

		BufferedImage img = createOverlay(actorLogin, guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, tilePosition, null);
		if (visualOverlays != null && visualOverlays.size() > 0)
		{
			List<VisualObject> objects = new ArrayList<VisualObject>();
			for (String voName : visualOverlays)
			{
				List<VisualObject> objs = SysManagerFactory.getRecordManager().getVisualObjects(actorLogin, guid,
						new VODimension(frameNo, sliceNo, siteNo), voName);
				objects.addAll(objs);
			}

			BufferedImage transperancy = getOverlayTransperancy(actorLogin, guid, objects, record.imageHeight);

			if (tilePosition == null)
				tilePosition = new Rectangle(0, 0, img.getWidth(), img.getHeight());

			BufferedImage subTransperancy = transperancy.getSubimage(tilePosition.x, tilePosition.y, tilePosition.width, tilePosition.height);

			return renderOverlaysOnImage(img, subTransperancy);
		}

		long end = System.currentTimeMillis();

		logger.logp(Level.INFO, "ImageManager", "getPixelDataOverlay", "PixelDataOverlay of guid=" + guid + " in " + (end - start) + " ms");

		return img;
	}

	/**
	 * Returns the BufferedImage
	 * 
	 * @param actorLogin
	 *            the user making the call
	 * @param the
	 *            record guid
	 * @param channels
	 *            list of channels to overlay
	 * @param useChannelColor
	 *            whether to use channel colors
	 * @param zStacked
	 *            whether to do z-stack projections (max intensity value) per
	 *            channels
	 * @param mosaic
	 *            whether to stack the images or tile them (mosaic)
	 * @param visualOverlays
	 *            list of selected visual overlays
	 * @param
	 * @return the create BufferedImage
	 */
	public BufferedImage getPixelDataOverlay(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, List<Integer> channelList,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle tilePosition, Map<Integer, Channel> channelDetailsMap)
			throws IOException
	{
		logger.logp(Level.INFO, "ImageManager", "getPixelDataOverlay", "guid=" + guid + " for " + channelList);
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		String projectName = SysManagerFactory.getProjectManager().getProject(projectID).getName();

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, projectName))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		int[] channels = new int[channelList.size()];
		for (int i = 0; i < channels.length; i++)
			channels[i] = channelList.get(i);

		BufferedImage img = createOverlay(actorLogin, guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, tilePosition,
				channelDetailsMap);

		return img;
	}

	/**
	 * generate E tag for specified configuration
	 * 
	 * @param actorLogin
	 *            logged in user
	 * @param guid
	 *            specified record id
	 * @param sliceNo
	 *            specified slice
	 * @param frameNo
	 *            specified frame
	 * @param siteNo
	 *            specified site
	 * @param visualOverlays
	 *            specified visual overlays
	 * @param scalebar
	 *            true if scalebar is tobe rendered, false otherwise
	 * @return E tag string
	 * @throws IOException
	 */
	public String getEtag(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, List<String> visualOverlays, boolean scalebar)
			throws IOException
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

		// ETags can ignore channel revision since channel details are encoded
		// in the url or movieId
		VisualOverlaysRevisionDAO visualOverlaysRevisionDAO = ImageSpaceDAOFactory.getDAOFactory().getVisualOverlaysRevisionDAO();
		RecordManager recordManager = SysManagerFactory.getRecordManager();

		HashMap<String, Long> overlayRevisions = new HashMap<String, Long>();
		for (String overlayName : visualOverlays)
		{
			Long revision = visualOverlaysRevisionDAO.getRevision(recordManager.getProjectID(guid), guid, siteNo, overlayName, sliceNo, frameNo);
			overlayRevisions.put(overlayName, revision);
		}

		StringBuffer sb = new StringBuffer();
		// sb.append(channelRevisions.toString());
		sb.append(overlayRevisions.toString());

		return Util.computeMD5Hash(sb.toString()).toString();
	}

	/**
	 * get overlay transperancy for specified configuration
	 * 
	 * @param actorLogin
	 *            current logged in user
	 * @param guid
	 *            specified record
	 * @param frameNo
	 *            specified frame
	 * @param sliceNo
	 *            spcified slice
	 * @param siteNo
	 *            specified site
	 * @param visualOverlays
	 *            selected visual overlays
	 * @param scalebar
	 *            if scalebar is to be displayed
	 * @return buffered image representing transperancy of the specified
	 *         configuration
	 * @throws DataAccessException
	 */
	public BufferedImage getOverlayTransperancy(String actorLogin, long guid, int frameNo, int sliceNo, int siteNo, List<String> visualOverlays,
			int requiredHeight, boolean scalebar) throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		List<VisualObject> objects = new ArrayList<VisualObject>();
		if (visualOverlays != null && visualOverlays.size() > 0)
		{
			for (String voName : visualOverlays)
			{
				List<VisualObject> objs = SysManagerFactory.getRecordManager().getVisualObjects(actorLogin, guid,
						new VODimension(frameNo, sliceNo, siteNo), voName);
				objects.addAll(objs);
			}
		}

		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);

		boolean psuedoScale = false;
		double xPixelSize = record.getXPixelSize();
		if (record.getXPixelSize() <= 0)
		{
			xPixelSize = 1.0;
			psuedoScale = true;
		}

		String unit = "";
		String scalebarTextValue = "XPixelSize Missing";
		if (scalebar)
		{
			// scalebar
			// float width = 3.0f/512f * record.imageHeight;
			double xstart = 20.0 / 512.0 * record.imageWidth;
			double ystart = 50 / 512.0 * record.imageHeight;

			double length = record.imageWidth / 5.0;
			double value = length * xPixelSize;
			double floorValue = Math.floor(value);

			length = length / value * floorValue;// re-adjust the length
													// according to rounded of
													// value

			if (!psuedoScale)
			{
				LineSegment scaleBar = new LineSegment(xstart, record.imageHeight - ystart, xstart + length, record.imageHeight - ystart);
				scaleBar.setPenWidth(3.0f);
				scaleBar.setPenColor(Color.RED);
				objects.add(scaleBar);

				scalebarTextValue = String.valueOf((int) floorValue);
				if (floorValue == 0)
				{
					scalebarTextValue = new DecimalFormat("##.#").format(value);
					floorValue = Double.parseDouble(scalebarTextValue);
				}

				switch (record.getLengthUnit())
				{
					case MILIMETER:
						unit = "mm";
						break;
					case MICROMETER:
						unit = "\u03bcm";
						break;
					case NANOMETER:
						unit = "nm";
						break;
					default:
						break;
				}
			}

			double textYStart = 30 / 512.0 * record.imageHeight;

			TextBox scalebarText = new TextBox(xstart, record.imageHeight - textYStart, (xstart + length) / 3.0, record.imageHeight - textYStart);
			scalebarText.setText(scalebarTextValue + " " + unit);
			scalebarText.setPenColor(Color.RED);

			int fontSize = scalebarText.getFont().getSize();
			fontSize = (int) (fontSize * 2);
			scalebarText.setFont(new Font(scalebarText.getFont().getName(), Font.BOLD, fontSize));

			objects.add(scalebarText);
		}

		BufferedImage transperancy = getOverlayTransperancy(actorLogin, guid, objects, requiredHeight);

		return transperancy;
	}

	private BufferedImage renderOverlaysOnImage(BufferedImage img, BufferedImage transperancy)
	{
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.drawImage(transperancy, 0, 0, img.getWidth(), img.getHeight(), null);

		g2d.dispose();

		return img;
	}

	/**
	 * returns a transparent image on which overlays are drawn {MUST use png
	 * format to write BufferedImage}
	 * 
	 * @param actorLogin
	 * @param guid
	 * @param objects
	 * @return transparent buffered image on which overlays are drawn
	 * @throws DataAccessException
	 */
	public BufferedImage getOverlayTransperancy(String actorLogin, long guid, List<VisualObject> objects, int requiredHeight)
			throws DataAccessException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
		int imageWidth = record.imageWidth;
		int imageHeight = record.imageHeight;

		// TODO: HACK for handling huge images
		if (requiredHeight > Constants.MAX_TILE_HEIGHT)
		{
			requiredHeight = Constants.MAX_TILE_HEIGHT;
		}

		double aspectRatio = imageWidth * 1.0 / imageHeight * 1.0;
		int requiredWidth = (int) (aspectRatio * requiredHeight);

		double widthRatio = requiredWidth * 1.0 / imageWidth * 1.0;
		double heightRatio = requiredHeight * 1.0 / imageHeight * 1.0;

		BufferedImage img = new BufferedImage(requiredWidth, requiredHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = (Graphics2D) img.getGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2D.setBackground(new Color(0, 0, 0, 0));
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.0f));

		img = renderVisualOverlays(actorLogin, guid, img, objects, widthRatio, heightRatio);

		return img;
	}

	private BufferedImage renderVisualOverlays(String actorLogin, long guid, BufferedImage img, List<VisualObject> objects, double widthRatio,
			double heightRatio) throws DataAccessException
	{
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		for (VisualObject obj : objects)
		{
			g.setColor(obj.getPenColor());
			g.setStroke(new BasicStroke(obj.getPenWidth()));

			logger.logp(Level.INFO, "ImageManager", "renderVisualOverlays", "angle=" + obj.getRotation());

			if (obj.getType() == VisualObjectType.ELLIPSE)
			{
				Point2D origin = new Point2D.Double(obj.getBounds().x * widthRatio, obj.getBounds().y * heightRatio);
				Point2D bounds = new Point2D.Double(obj.getBounds().width * widthRatio, obj.getBounds().height * heightRatio);

				AffineTransform transform = new AffineTransform();
				transform.rotate(obj.getRotation(), origin.getX() + bounds.getX() / 2, origin.getY() + bounds.getY() / 2);
				AffineTransform old = g.getTransform();
				g.transform(transform);

				g.drawOval((int) origin.getX(), (int) origin.getY(), (int) bounds.getX(), (int) bounds.getY());

				g.setTransform(old);

			}
			else if (obj.getType() == VisualObjectType.CIRCLE)
			{
				Point2D origin = new Point2D.Double(obj.getBounds().x * widthRatio, obj.getBounds().y * heightRatio);
				Point2D bounds = new Point2D.Double(obj.getBounds().width * widthRatio, obj.getBounds().height * heightRatio);
				g.drawOval((int) origin.getX(), (int) origin.getY(), (int) bounds.getX(), (int) bounds.getY());
				g.fillOval((int) origin.getX(), (int) origin.getY(), (int) bounds.getX(), (int) bounds.getY());
			}
			else if (obj.getType() == VisualObjectType.LINE)
			{
				int x1 = (int) ((LineSegment) obj).startX;
				int y1 = (int) ((LineSegment) obj).startY;

				int x2 = (int) ((LineSegment) obj).endX;
				int y2 = (int) ((LineSegment) obj).endY;

				Point2D start = new Point2D.Double(x1 * widthRatio, y1 * heightRatio);
				Point2D end = new Point2D.Double(x2 * widthRatio, y2 * heightRatio);
				g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
			}
			else if (obj.getType() == VisualObjectType.TEXT)
			{
				String text = ((TextBox) obj).getText();
				Font f = ((TextBox) obj).getFont();
				g.setFont(f);
				g.drawString(text, (int) (obj.getBounds().x * widthRatio), (int) (obj.getBounds().y * heightRatio));
			}
			else if (obj.getType() == VisualObjectType.RECTANGLE)
			{
				Point2D origin = new Point2D.Double(obj.getBounds().x * widthRatio, obj.getBounds().y * heightRatio);
				Point2D bounds = new Point2D.Double(obj.getBounds().width * widthRatio, obj.getBounds().height * heightRatio);

				AffineTransform transform = new AffineTransform();
				transform.rotate(obj.getRotation(), origin.getX() + bounds.getX() / 2, origin.getY() + bounds.getY() / 2);
				AffineTransform old = g.getTransform();
				g.transform(transform);

				g.drawRect((int) origin.getX(), (int) origin.getY(), (int) bounds.getX(), (int) bounds.getY());

				g.setTransform(old);

			}
			else if (obj.getType() == VisualObjectType.PATH || obj.getType() == VisualObjectType.POLYGON || obj.getType() == VisualObjectType.ARROW)
			{
				List<Point2D> points = ((GeometricPath) obj).getPathPoints();
				int nPoints = points.size();
				int xPoints[] = new int[nPoints];
				int yPoints[] = new int[nPoints];
				int i = 0;
				for (Point2D point : points)
				{
					xPoints[i] = (int) (point.getX() * widthRatio);
					yPoints[i] = (int) (point.getY() * heightRatio);
					i++;
				}

				g.drawPolyline(xPoints, yPoints, nPoints);
			}

		}

		return img;
	}

	public PixelArray[] getPixelArrays(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos,
			boolean useChannelColor, boolean zStacked, Rectangle tilePosition) throws IOException
	{
		logger.logp(Level.INFO, "ImageManager", "getPixelArrays", "guid=" + guid + ", channels=" + channelNos.length + ", useChannelColor="
				+ useChannelColor + ",zStacked=" + zStacked);
		List<Channel> allChannels = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);

		Map<Integer, Channel> channelDetailsMap = new LinkedHashMap<Integer, Channel>(channelNos.length);
		for (int i = 0; i < channelNos.length; i++)
		{
			channelDetailsMap.put(channelNos[i], allChannels.get(channelNos[i]));
		}
		return getPixelArrays(actorLogin, guid, sliceNo, frameNo, siteNo, useChannelColor, zStacked, tilePosition, channelDetailsMap);
	}

	private PixelArray[] getPixelArrays(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, boolean useChannelColor,
			boolean zStacked, Rectangle tilePosition, Map<Integer, Channel> channelDetailsMap) throws IOException
	{
		logger.logp(Level.INFO, "ImageManager", "getPixelArrays", "guid=" + guid + ", channels=" + channelDetailsMap + ", useChannelColor="
				+ useChannelColor + ",zStacked=" + zStacked);

		PixelArray[] pixelArrays = new PixelArray[channelDetailsMap.size()];
		long startTime = System.currentTimeMillis();

		int i = 0;
		for (Integer channelNo : channelDetailsMap.keySet())
		{

			Channel ithChannel = channelDetailsMap.get(channelNo);

			PixelArray rawData = getRawData(actorLogin, guid, new Dimension(frameNo, sliceNo, channelNo, siteNo), tilePosition);
			String lut = ithChannel.getLut() == null || !useChannelColor ? "grays" : ithChannel.getLut();
			VisualContrast constrast = ithChannel.getContrast(zStacked);

			rawData.setColorModel(LutLoader.getInstance().getLUT(lut));

			if (zStacked)
			{
				ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
				RecordDAO recordDao = factory.getRecordDAO();
				Dimension maxDimension = recordDao.getDimension(guid);

				for (int slice = 0; slice < maxDimension.sliceNo; slice++)
				{
					if (slice == sliceNo)
						continue;
					PixelArray another = getRawData(actorLogin, guid, new Dimension(frameNo, slice, channelNo, siteNo), tilePosition);
					rawData.overlay(another);
				}
			}

			if (constrast != null)
			{
				rawData.setContrast(constrast.getMinIntensity(), constrast.getMaxIntensity());
				rawData.setGamma(constrast.getGamma());
			}
			else
			{
				// Use image contrast if no contrast is set ..
				rawData.setAutoContrast();
				rawData.setGamma(1.0);
				// save for future reference as well
				UserPreference pref = SysManagerFactory.getUserPreference();
				try
				{
					pref.setCustomContrast(actorLogin, guid, channelNo, zStacked, rawData.getMinContrastSetting(), rawData.getMaxContrastSetting(),
							rawData.getGamma());
				}
				catch (Exception e)
				{
					logger.logp(Level.WARNING, "ImageManager", "getPixelArrays", "error setting custom constrast ", e);
				}
			}

			pixelArrays[i++] = rawData;
		}

		long endTime = System.currentTimeMillis();
		logger.logp(Level.INFO, "ImageManager", "getPixelArrays", "Read all RawData of guid=" + guid + " for " + channelDetailsMap + " channels in "
				+ (endTime - startTime) + " ms");
		return pixelArrays;
	}

//	private BufferedImage createOverlay(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos,
//			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle tilePosition) throws IOException
//	{
//		return createOverlay(actorLogin, guid, sliceNo, frameNo, siteNo, channelNos, useChannelColor, zStacked, mosaic, tilePosition, null);
//	}

	private BufferedImage createOverlay(String actorLogin, long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos,
			boolean useChannelColor, boolean zStacked, boolean mosaic, Rectangle tilePosition, Map<Integer, Channel> channelDetailsMap)
			throws IOException
	{
		long start = System.currentTimeMillis();
		PixelArray[] pixelArrays;

		if (channelDetailsMap != null)
		{
			pixelArrays = getPixelArrays(actorLogin, guid, sliceNo, frameNo, siteNo, useChannelColor, zStacked, tilePosition, channelDetailsMap);
		}
		else
		{
			pixelArrays = getPixelArrays(actorLogin, guid, sliceNo, frameNo, siteNo, channelNos, useChannelColor, zStacked, tilePosition);
		}

		BufferedImage renderableImage = null;

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

				renderableImage = ImageUtil.createMosaicImage(imageList, pixelArrays[0].getWidth(), pixelArrays[0].getHeight(), 5);
			}
			else
			{
				renderableImage = PixelArray.getOverlayImage(pixelArrays);
			}
		}
		else
		{
			renderableImage = PixelArray.getOverlayImage(pixelArrays);
		}
		long end = System.currentTimeMillis();

		logger.logp(Level.INFO, "ImageManager", "createOverlay", "create overlay image for guid=" + guid + " in " + (end - start) + " ms");

		return renderableImage;
	}

	/**
	 * return text form of legends for specified image
	 * 
	 * @param actorLogin
	 *            logged in user
	 * @param recordId
	 *            specified record
	 * @param frameNumber
	 *            specified frame
	 * @param sliceNumber
	 *            specified slice
	 * @param siteNumber
	 *            specified site
	 * @return text form of legends for specified image
	 * @throws IOException
	 */
	public String getLegendText(String actorLogin, long recordId, int frameNumber, int sliceNumber, int siteNumber) throws IOException
	{
		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(recordId);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		ImagePixelData metadata = getImageMetaData(actorLogin, recordId, new Dimension(frameNumber, sliceNumber, 0, siteNumber));

		List<LegendField> legendFields = SysManagerFactory.getUserPreference().getLegendFields(actorLogin);

		StringBuffer legendText = new StringBuffer();

		if (legendFields != null)
		{
			for (LegendField field : legendFields)
			{
				if ("Elapsed Time".equals(field.legendName))
				{
					legendText.append("Elapsed Time:");
					legendText.append(metadata.getElapsed_time());
				}
				else if ("Exposure Time".equals(field.legendName))
				{
					legendText.append("Exposure Time:");
					legendText.append(metadata.getExposureTime());
				}
				else if ("X Coordinate".equals(field.legendName))
				{
					legendText.append("X Coordinate:");
					legendText.append(metadata.getX());
				}
				else if ("Y Coordinate".equals(field.legendName))
				{
					legendText.append("Y Coordinate:");
					legendText.append(metadata.getY());
				}
				else if ("Z Coordinate".equals(field.legendName))
				{
					legendText.append("Z Coordinate:");
					legendText.append(metadata.getZ());
				}

				legendText.append("\n");
			}
		}
		return legendText.toString();
	}

	/**
	 * return xPixelSize for the record
	 * 
	 * @param actorLogin
	 * @param guid
	 * @return
	 * @throws DataAccessException
	 */
	public double getXPixelSize(String actorLogin, long guid) throws DataAccessException
	{

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);

		return record.getXPixelSize();
	}

	/**
	 * return length unit for pixelsize of record
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	public String getLengthUnit(String actorLogin, long guid) throws DataAccessException
	{

		ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
		RecordDAO recordDao = factory.getRecordDAO();

		int projectID = recordDao.getProjectID(guid);
		Project project = SysManagerFactory.getProjectManager().getProject(projectID);

		if (!SysManagerFactory.getUserPermissionManager().canRead(actorLogin, project.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));

		Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);

		String unit = "";

		switch (record.getLengthUnit())
		{
			case MILIMETER:
				unit = "mm";
				break;
			case MICROMETER:
				unit = "micron";
				break;
			case NANOMETER:
				unit = "nm";
				break;
			default:
				break;
		}

		return unit;

	}
}
