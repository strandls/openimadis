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

package com.strandgenomics.imaging.iserver.services.impl;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;
import org.apache.axis.transport.http.AxisHttpSession;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;
import com.strandgenomics.imaging.iengine.system.AttachmentManager;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.RecordManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.def.ispace.MosaicResource;
import com.strandgenomics.imaging.iserver.services.ws.ispace.AcquisitionProfile;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Area;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Channel;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Comments;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Contrast;
import com.strandgenomics.imaging.iserver.services.ws.ispace.EllipticalShape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.FingerPrint;
import com.strandgenomics.imaging.iserver.services.ws.ispace.FreehandShape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.HistoryItem;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Image;
import com.strandgenomics.imaging.iserver.services.ws.ispace.ImageIndex;
import com.strandgenomics.imaging.iserver.services.ws.ispace.ImageSpaceService;
import com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicParameters;
import com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicRequest;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Overlay;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Project;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Property;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Record;
import com.strandgenomics.imaging.iserver.services.ws.ispace.RecordAttachment;
import com.strandgenomics.imaging.iserver.services.ws.ispace.RecordSite;
import com.strandgenomics.imaging.iserver.services.ws.ispace.RectangularShape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Shape;
import com.strandgenomics.imaging.iserver.services.ws.ispace.Statistics;
import com.strandgenomics.imaging.iserver.services.ws.ispace.StraightLine;
import com.strandgenomics.imaging.iserver.services.ws.ispace.TextArea;
import com.strandgenomics.imaging.iserver.services.ws.ispace.VOIndex;

/**
 * Implementation of the iSpace webservice
 * @author arunabha
 *
 */
public class ImageSpaceImpl implements ImageSpaceService, Serializable {

	private static final long serialVersionUID = 9165660663414842314L;
	/**
	 * the logger
	 */
	private transient Logger logger = null;
	
	public ImageSpaceImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}
	
	/**
	 * Returns the list of archives associated with the specified project
	 * @param the access token required to make this call
	 * @param projectName name of the project
	 * @return list of archive signatures within the specified project
	 * @throws RemoteException
	 */
	@Override
	public String[] listArchives(String accessToken, String projectName) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceImpl", "listArchives", "finding archives for projects "+ projectName);
		
		try
		{

			Set<BigInteger> archiveSignatures = SysManagerFactory.getProjectManager().getArchivesForProject(actorLogin, projectName);
			if(archiveSignatures == null || archiveSignatures.isEmpty()) return null;
			
			List<String> signatures = new ArrayList<String>();
			for(BigInteger i : archiveSignatures)
			{
				signatures.add( Util.toHexString(i) );
			}
			
			return signatures.toArray(new String[0]);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "listArchives", "error finding archives for project "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    
    /**
     * The list of active projects that the connected user have permission to read
     * @param the access token required to make this call
     * @return list of active projects that the connected user have permission to read
     */
	@Override
    public String[] getActiveProjects(String accessToken) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getActiveProjects", "finding active project for user "+ actorLogin);
			
			List<String> projList = SysManagerFactory.getProjectManager().getActiveProjects(actorLogin);
			return projList == null || projList.isEmpty() ? null : projList.toArray(new String[0]);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getActiveProjects", "error finding active project", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * The list of archived projects that the connected user have permission to browse, may be null otherwise
     * @param the access token required to make this call
     * @return list of active projects that the connected user have permission to read
     */
    @Override
    public String[] getArchivedProjects(String accessToken) 
    		throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getActiveProjects", "finding archived project for user "+ actorLogin);
			
			List<String> projList = SysManagerFactory.getProjectManager().getArchivedProjects(actorLogin);
			return projList == null || projList.isEmpty() ? null : projList.toArray(new String[0]);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getActiveProjects", "error finding archived project", ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Image APIs /////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	/**
     * Returns the PixelData for the specified coordinate for the given record 
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
     * @param imageIndex relevant image coordinate
     * @return the handle to the relevant PixelData
     */
	@Override
    public Image getPixelDataForRecord(String accessToken, long guid, ImageIndex index) throws RemoteException 
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			
			logger.logp(Level.INFO, "ImageSpaceImpl", "getPixelDataForRecord", "finding PixelData at "+d +" for "+actorLogin);
			
			ImagePixelData pixelData = SysManagerFactory.getRecordManager().getPixelDataForRecord(actorLogin, guid, d);
			return CoercionHelper.toRemotePixelData(pixelData);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getPixelDataForRecord", "error finding pixeldata for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
     * Returns the intensity distribution statistics for the specified image within the record
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
     * @param channel the channel
     * @return the intensity distribution statistics
     * @throws RemoteException
     */
	@Override
	public Statistics getIntensityDistibution(String accessToken, long guid, ImageIndex index) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getIntensityDistibution", "finding image histogram at "+d +" for "+actorLogin);
			
			Histogram histo = SysManagerFactory.getImageManager().getIntensityDistibution(actorLogin, guid, d, false, null);
			return CoercionHelper.toImageStatistics(histo);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getIntensityDistibution", "error finding pixeldata for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	@Override
	public Statistics getIntensityDistibutionForTile(String accessToken, long guid, ImageIndex index,int x, int y, int width, int height) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getIntensityDistibution", "finding image histogram at "+d +" for "+actorLogin);
			
			Rectangle tile = new Rectangle(x, y, width, height);
			
			Histogram histo = SysManagerFactory.getImageManager().getIntensityDistibution(actorLogin, guid, d, false, tile);
			return CoercionHelper.toImageStatistics(histo);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getIntensityDistibution", "error finding pixeldata for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Sets a custom contrast for all images of this record for the specified channel
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration
     * @param channelNo the relevant channel number
     * @param contrast the custom contrast to set
     * @param lutName the channel lut to set
     */
	@Override
    public void setChannelColorAndContrast(String accessToken, long guid, int channelNo, Contrast contrast, String lutName)
    		throws RemoteException 
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "setChannelColorAndContrast", "setting channel preferences for channel "+channelNo +" for "+actorLogin);
			if(lutName!=null && !lutName.isEmpty())
			{
				SysManagerFactory.getUserPreference().setChannelLUT(actorLogin, guid, channelNo, lutName);
			}
 			if(contrast!=null)
 			{
 				SysManagerFactory.getUserPreference().setCustomContrast(actorLogin, guid, channelNo, false, contrast.getMinIntensity(), contrast.getMaxIntensity(), contrast.getGamma());
 			}
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "setChannelColorAndContrast", "error setting channel preferences guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the custom contrast, if any, for all images of this record for the specified channel
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration
     * @return the custom contrast, if any, for all images of this record, otherwise null
     */
	@Override
    public Channel[] getRecordChannels(String accessToken, long guid)
    		throws RemoteException 
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRecordChannel", "fetching channels for "+guid +" by "+actorLogin);

			List<com.strandgenomics.imaging.icore.Channel> cList = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);
			return CoercionHelper.toRemoteChannel(cList);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getRecordChannel", "error fetching channels for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Returns the sites of this record 
     * @param accessToken the access token required to make this call
     * @param guid the GUID of  the record under consideration
     * @return the sites associated with the record
     * @throws RemoteException
     */
	@Override
	public RecordSite[] getRecordSite(String accessToken, long guid) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRecordSite", "fetching sites for "+guid +" by "+actorLogin);

			List<com.strandgenomics.imaging.icore.Site> cList = SysManagerFactory.getRecordManager().getRecordSites(actorLogin, guid);
			return CoercionHelper.toRemoteSite(cList);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getRecordSite", "error fetching sites for record "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * To add custom image as a thumb-nail
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration
     * @return the URL to upload the image for thumb-nail
     */
	@Override
    public String getThumbnailUploadURL(String accessToken, long guid) 
    		throws RemoteException 
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getThumbnailUploadURL", "fetching thumbnail upload URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getThumbnailUploadURL(actorLogin, getClientIPAddress(), guid);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getThumbnailUploadURL", "error fetching thumbnail upload URL for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the URL to download the thumb-nail image for the specified record
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration
     * @return the URL to download the thumb-nail image for the specified record
     */
	@Override
    public String getThumbnailDownloadURL(String accessToken, long guid) 
    		throws RemoteException
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getThumbnailDownloadURL", "fetching thumbnail download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getThumbnailDownloadURL(actorLogin, getClientIPAddress(), guid);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getThumbnailDownloadURL", "error fetching thumbnail download URL for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
	 /**
     * Returns the URL to download the raw pixel data (as a gzip-ed byte array) associated with the specified coordinate
     * @param accessToken the access token required to make this call
     * @param parentRecord the record under consideration 
     * @param imageIndex the image-coordinate under consideration 
     * @return the URL to download the raw pixel data associated with the specified coordinate
     */
	@Override
    public String getRawIntensitiesDownloadURL(String accessToken, long guid, ImageIndex index) throws RemoteException 
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRawIntensitiesDownloadURL", "fetching pixel array@"+d +" download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getRawPixelArrayDownloadURL(actorLogin, getClientIPAddress(), guid, d);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getRawIntensitiesDownloadURL", "error fetching pixel array download URL for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	 /**
     * Returns the URL to download the pixel image (as a gzip-ed byte array) associated with the specified coordinate
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
     * @param imageIndex the image-coordinate under consideration 
     * @return the URL to download the raw pixel data associated with the specified coordinate
     */
	@Override
    public String getImageDownloadURL(String accessToken, long guid, boolean useChannelColor, 
    		ImageIndex index) throws RemoteException 
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getImageDownloadURL", "fetching pixel image @"+d +" download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getPixelDataDownloadURL(actorLogin, getClientIPAddress(), guid, d, useChannelColor);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getImageDownloadURL", "error finding pixel image for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns the URL to download the pixel-data of the specified Tile/Block as a gzip-ed byte array
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
	 * @param imageIndex the image-coordinate under consideration 
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     */
	@Override
    public String getTileIntensitiesDownloadURL(String accessToken, long guid,
    		ImageIndex index, int x, int y, int width, int height) throws RemoteException
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);

		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getTileIntensitiesDownloadURL", "fetching ROI pixel intensities @"+d +" download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getTilePixelArrayDownloadURL(actorLogin, getClientIPAddress(), guid, d, x, y, width, height);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getTileIntensitiesDownloadURL", "error finding pixeldata for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the URL to download the pixel-data of the specified Tile/Block as a gzip-ed byte array
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
	 * @param imageIndex the image-coordinate under consideration 
	 * @param useChannelColor whether to use channel colors
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     */
	@Override
    public String getTileImageDownloadURL(String accessToken, long guid, boolean useChannelColor,
    		ImageIndex index, int x, int y, int width, int height) throws RemoteException
    {
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getTileImageDownloadURL", "fetching ROI pixel image @"+d +" download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getTileDownloadURL(actorLogin, getClientIPAddress(), guid, d, useChannelColor, x, y, width, height);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getTileImageDownloadURL", "error roi finding image for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the URL to download the pixel-data overlay as a gzip-ed byte array
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
     * @param channels list of channels to overlay
     * @param useChannelColor whether to use channel colors
     * @param zStacked whether to do z-stack projections (max intensity value) per channels
     * @param mosaic whether to stack the images or tile them (mosaic)
     * @return the URL to download the pixel-data overlay as a image
     */
    @Override
    public String getOverlayImageDownloadURL(String accessToken, long guid, 
    		int frameNo, int sliceNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height) throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getOverlayImageDownloadURL", "fetching channel overlay image  download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getPixelDataOverlayDownloadURL(actorLogin,
					getClientIPAddress(), guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, x, y, width, height);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getOverlayImageDownloadURL", "error finding channel overlay image for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    @Override
	public String getOverlayImageDownloadURL(String accessToken, long guid, 
    		int frameNo, int sliceNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height, int targetWidth, int targetHeight) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getOverlayImageDownloadURL", "fetching channel overlay image  download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getPixelDataOverlayDownloadURL(actorLogin,
					getClientIPAddress(), guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, x, y, width, height, targetWidth, targetHeight);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getOverlayImageDownloadURL", "error finding channel overlay image for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns the URL to download the pixel-data overlay as a gzip-ed byte array
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
     * @param channels list of channels to overlay
     * @param useChannelColor whether to use channel colors
     * @param zStacked whether to do z-stack projections (max intensity value) per channels
     * @param mosaic whether to stack the images or tile them (mosaic)
     * @return the URL to download the pixel-data overlay as a image
     */
    @Override
    public String getOverlayImageDownloadURL(String accessToken, long guid, 
    		int frameNo, int sliceNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height, int[] contrasts) throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getOverlayImageDownloadURL", "fetching channel overlay image  download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getPixelDataOverlayWithContrastDownloadURL(actorLogin,
					getClientIPAddress(), guid, sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, x, y, width, height, contrasts);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getOverlayImageDownloadURL", "error finding channel overlay image for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns the URL to download the images for all slices from the specified frame and site
     * and overlaid on all channels (and possibly scaled downed to a low value)
     * This is needed for 3D viewing
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration 
     * @param frameNo the frame number
     * @param siteNo the site number
     * @param imageWidth image width of the requested images (for scaling)
     * @param useChannelColor specifies whether to use channel colors
     * @param imageFormat format of the image - e.g., PNG, TIFF, JPEG etc
     * @return the URL to download the pixel-data overlay as an image
     */
    @Override
	public String getChannelOverlaidSliceImagesURL(String accessToken, long guid, 
			int frameNo, int siteNo, int imageWidth, boolean useChannelColor) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getChannelOverlayForSlicesURL", "fetching zstacked channel overlay image download URL for "+guid +" by "+actorLogin);

			return SysManagerFactory.getImageDataDownloadManager().getChannelOverlaidSliceImagesURL(actorLogin, getClientIPAddress(),
					guid, frameNo, siteNo, imageWidth, useChannelColor);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getChannelOverlayForSlicesURL", "error finding zstacked channel overlay for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////// Attachment APIs ///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	 /**
     * Create an attachment associated with the specified record
     * @param parentRecord the record (it's signature) owning the attachment
     * @param name name of the attachment
     * @param notes shot text notes associated with the attachment
     * @return a URL to upload the specified attachment
     */
    @Override
    public String createRecordAttachment(String accessToken, long guid, 
    		String name, String notes) throws RemoteException 
    {
    	Client client = Service.ISPACE.getClient(accessToken);
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "createRecordAttachment", "creating attachment "+name +" for "+guid +" by "+actorLogin);

			Application app = new Application(client.getName(), client.getVersion());
			return SysManagerFactory.getAttachmentManager().insertAttachment(app, actorLogin, accessToken, guid, name,
					notes, getClientIPAddress());
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "createRecordAttachment", "error creating attachment for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns a URL to download the specified attachment
     * @param parentRecord the record (it's signature) containing the attachment
     * @param name name of the attachment
     * @return a URL to download the specified attachment
     */
    @Override
    public String getAttachmentDownloadURL(String accessToken, long guid, String name) 
    		throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getAttachmentDownloadURL", "fetching attachment URL for "+name +" for "+guid +" by "+actorLogin);
			return SysManagerFactory.getAttachmentManager().getDownloadURL(actorLogin, guid, name, getClientIPAddress());
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getAttachmentDownloadURL", "error finding download url for arratchment for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns available attachments associated with this record
     * @param record the record under consideration
     * @return list user added attachments (files) associated with this record
     */
    @Override
    public RecordAttachment[] getRecordAttachments(String accessToken, long guid) 
    		throws RemoteException 
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRecordAttachments", "fetching attachments for "+guid +" by "+actorLogin);

			AttachmentManager am = SysManagerFactory.getAttachmentManager();
			List<Attachment> localAttachment = am.getRecordAttachments(actorLogin, guid);
			return CoercionHelper.toRemoteAttachments(localAttachment);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getRecordAttachments", "error finding attachments for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Meta Data APIs ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds user comment on the specified record 
     * @param signature the record under consideration
     * @param comments the comment to add
     */
    @Override
    public void addUserComment(String accessToken, long guid, String comments) 
    		throws RemoteException
    {
    	Client client = Service.ISPACE.getClient(accessToken);
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addUserComment", "adding comments for "+guid +" by "+actorLogin);
			SysManagerFactory.getRecordManager().addUserComment(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, comments);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "addUserComment", "error adding comments for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns all comments on the specified record 
     * @param signature the record under consideration
     * @return all comments on the specified record 
     * @throws RemoteException
     */
    @Override
    public Comments[] fetchUserComment(String accessToken, long guid) 
    		throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "fetchUserComment", "fetching comments for "+guid +" by "+actorLogin);
			
			RecordManager rm = SysManagerFactory.getRecordManager();
			List<UserComment> comments = rm.getUserComments(actorLogin, guid);
			
			return CoercionHelper.toComments(comments);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "fetchUserComment", "error finding comments for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns additional machine generated (read-only) meta data associated with this record
     * The legal values are one of String and Numbers
     * @param record the record under consideration
     * @return list of machine generated meta data associated with this record
     */
    @Override
    public Property[] getDynamicMetaData(String accessToken, long guid) 
    		throws RemoteException 
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
    	
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getDynamicMetaData", "fetching dynamic metadata for "+guid +" by "+actorLogin);
			
			return null; //TODO
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getDynamicMetaData", "error finding dynamic meta data for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns all relevant user annotations associated with this record
     * The legal values are one of String and Numbers
     * @param record the record under consideration
     * @return list user added meta data associated with this record
     */
    @Override
    public Property[] getRecordUserAnnotations(String accessToken, long guid) 
    		throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRecordUserAnnotations", "fetching user metadata for "+guid +" by "+actorLogin);
			
			List<MetaData> metadata = SysManagerFactory.getRecordManager().getUserAnnotations(actorLogin, guid);
			return CoercionHelper.toRemoteMetadataList(metadata);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getRecordUserAnnotations", "error finding user annotations for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Adds the specified user annotations with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
     * @param record the record under consideration
     * @param annotations list of annotations or meta data to add to the record
     */
    @Override
    public void addRecordUserAnnotation(String accessToken, long guid, Property[] annotations) 
    		throws RemoteException 
    {
    	Client client = Service.ISPACE.getClient(accessToken);
    	Application app = new Application(client.getName(), client.getVersion());
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addRecordUserAnnotation", "fetching user metadata for "+guid +" by "+actorLogin);
			
			List<MetaData> metadataList = CoercionHelper.toMetaDataList(annotations);
			SysManagerFactory.getRecordManager().addUserAnnotation(app, actorLogin, accessToken, guid, metadataList);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "addRecordUserAnnotation", "error adding user annotations for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the list of additional meta data associated with the specified image
     * @param parentRecord the record under consideration 
     * @param index the image under consideration
     * @return the list of additional meta data associated with the specified image
     */
    @Override
    public Property[] getImageMetaData(String accessToken, long guid, ImageIndex index)
    		throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			Dimension d = CoercionHelper.toDimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getImageMetaData", "fetching image metadata for "+d +"@"+guid+" by "+actorLogin);
			
			return null; //TODO
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getImageMetaData", "error finding image meta-data for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Visual Overlays & Objects APIs //////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Returns the relevant visual overlays associated the specified dimension of the given record  
     * @param guid the record under consideration 
     * @param index the image coordinate
     * @return all relevant visual overlays associated the specified dimension of the given record  
     */
    @Override
	public Overlay[] getVisualOverlays(String accessToken, long guid, VOIndex index) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getVisualOverlays", "fetching visual overlays for "+d +"@"+guid+" by "+actorLogin);
			
			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualOverlay> overlays = rm.getVisualOverlays(actorLogin, guid, d);
			return CoercionHelper.toRemoteVisualAnnotations(overlays);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getVisualOverlays", "error finding overlays for "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the specific of visual overlay associated with the specified coordinate and name 
     * @param guid  the record under consideration 
     * @param index the image coordinate
     * @param overlayName name of the visual overlay
     * @return the visual overlay associated with the specified dimension and name 
     */
    @Override
	public Overlay getVisualOverlay(String accessToken, long guid, VOIndex index, String overlayName) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getVisualOverlay", "fetching visual overlays for "+d +"@"+guid+" name= "+overlayName);
			
			RecordManager rm = SysManagerFactory.getRecordManager();
			VisualOverlay overLay = rm.getVisualOverlay(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteVisualAnnotation(overLay);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getVisualOverlay", "error finding overlay for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * Returns the list of visual annotation names associated with the specified record at the specified site
	 * @param signature the record signature under consideration 
	 * @param siteNo the site
	 * @return the list of visual annotation names associated with the specified record at the specified site
	 */
    @Override
	public String[] getAvailableVisualOverlays(String accessToken, long guid, int siteNo)
			throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getAvailableVisualOverlays", "fetching visual overlays for site "+siteNo +"@"+guid+" actorLogin "+actorLogin);
			
			RecordManager rm = SysManagerFactory.getRecordManager();
			List<String> names = rm.getAvailableVisualOverlays(actorLogin, guid, siteNo);
			return names == null || names.isEmpty() ? null : names.toArray(new String[0]);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getAvailableVisualOverlays", "error finding pixeldata for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Creates visual overlays for the specified record for all relevant frames and slices for the fixed site
     * @param signature the record signature under consideration 
     * @param siteNo the site
     * @param name name of the visual annotation
     * @return a overlay that is created
     */
    @Override
	public void createVisualOverlays(String accessToken, long guid, int siteNo, String name)
			throws RemoteException 
	{
    	Client client = Service.UPDATE.getClient(accessToken);
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "createVisualOverlays", "creating visual overlays for site "+siteNo +"@"+guid+" name "+name);

			RecordManager rm = SysManagerFactory.getRecordManager();
			rm.createVisualOverlays(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, siteNo, name);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "createVisualOverlays", "error finding pixeldata for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * Adds the specified visual objects to the visual-overlays of the specified records 
	 * @param signature the record signature under consideration 
	 * @param vObjects the shapes - visual objects
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
    @Override
	public void addVisualObjects(String accessToken, long guid, 
			Shape[] vObjects, String overlayName, VOIndex[] indexes) throws RemoteException 
	{
    	Client client = Service.ISPACE.getClient(accessToken);
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addVisualObjects", "adding visual objects for overlay "+overlayName +"@"+guid+" actorLogin "+actorLogin);

			List<VisualObject> voList = CoercionHelper.toVisualObjects(vObjects);
			VODimension[] dList = CoercionHelper.toVODimension(indexes);
			
			RecordManager rm = SysManagerFactory.getRecordManager();
			rm.addVisualObjects(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, voList, overlayName, dList);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "addVisualObjects", "error adding visual objects for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	   /**
     * Return all visual objects with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of visual objects
     */
    @Override
    public Shape[] getVisualObjects(String accessToken, long guid, VOIndex index, String overlayName) throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "addVisualObjects", "fetching visual objects for overlay "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> vobjects = rm.getVisualObjects(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteShapes(vobjects);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getVisualObjects", "error finding visual objects for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Return all elliptical shapes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
    @Override
	public EllipticalShape[] getEllipticalShapes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getEllipticalShapes", "fetching visual objects for overlay "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> vobjects = rm.getEllipticalShapes(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteEllipticalShapes(vobjects);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getEllipticalShapes", "error finding visual objects for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Return all straight lines with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
    @Override
	public StraightLine[] getLineSegments(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException 
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getLineSegments", "fetching visual objects for overlay "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> vobjects = rm.getLineSegments(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteStraightLines(vobjects);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getLineSegments", "error finding visual objects for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Return all rectangular shapes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
    @Override
	public RectangularShape[] getRectangularShapes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRectangularShapes", "fetching visual objects for overlay "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> vobjects = rm.getRectangularShapes(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteRectangularShapes(vobjects);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getRectangularShapes", "error finding visual objects for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Return all text boxes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
    @Override
	public TextArea[] getTextBoxes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getTextBoxes", "fetching visual objects for overlay "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> vobjects = rm.getTextBoxes(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteTextArea(vobjects);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getTextBoxes", "error finding visual objects for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Return all free-hand shapes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of free-hand shapes if any, null otherwise
     */
    @Override
	public FreehandShape[] getFreeHandShapes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "getFreeHandShapes", "fetching visual objects for overlay "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> vobjects = rm.getFreeHandShapes(actorLogin, guid, d, overlayName);
			return CoercionHelper.toRemoteFreeHandShapes(vobjects);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "getFreeHandShapes", "error finding visual objects for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * find the dimensions where the specified overlay exists (non-empty visual objects)
	 * @param parentRecord the record under consideration
	 * @param overlayName name of the overlay
	 * @return the dimensions where there are at least one visual objects
	 */
    @Override
	public VOIndex[] findOverlayLocation(String accessToken, long guid, int siteNo, String overlayName) throws RemoteException 
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "findOverlayLocation", "fetching overlay locations for "+overlayName +"@"+guid+" siteNo "+siteNo);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VODimension> locations = rm.findOverlayLocation(actorLogin, guid, siteNo, overlayName);
			
			return CoercionHelper.toVOCoordinates(locations);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findOverlayLocation", "error finding overlay locations for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * find the dimensions where the specified visual object exists
	 * @param parentRecord the record under consideration
	 * @param siteNo overlays are specific to sites, so the relevant site no
	 * @param overlayName name of the overlay
	 * @param the visual object to search
	 * @return the dimensions where the overlay contains the specified object
	 */
    @Override
	public VOIndex[] findVisualObjectLocation(String accessToken, long guid, 
			int siteNo, String overlayName, Shape object) throws RemoteException 
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "findVisualObjectLocation", "fetching overlay locations for "+overlayName +"@"+guid+" siteNo "+siteNo);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VODimension> locations = rm.findVisualObjectLocation(actorLogin, guid, siteNo, overlayName, object.getID());
			
			return CoercionHelper.toVOCoordinates(locations);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findVisualObjectLocation", "error finding vo location for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/**
	 * find the visual objects within the specified area
	 * @param parentRecord the record under consideration
	 * @param index the image index
	 * @param overlayName name of the overlay
	 * @param the area under consideration
	 * @return the dimensions where the overlay contains the specified object
	 */
    @Override
	public Shape[] findVisualObjects(String accessToken, long guid, 
			VOIndex index, String overlayName, Area a) throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			VODimension d = CoercionHelper.toVODimension(index);
			logger.logp(Level.INFO, "ImageSpaceImpl", "findVisualObjects", "fetching visual objects in "+overlayName +"@"+guid+" index "+d);

			RecordManager rm = SysManagerFactory.getRecordManager();
			List<VisualObject> shapes = rm.findVisualObjects(actorLogin, guid, index.getFrame(), index.getSlice(), index.getSite(), overlayName, a.getX(), a.getY(), a.getHeight(), a.getWidth());
			
			return CoercionHelper.toRemoteShapes(shapes);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findVisualObjects", "error finding pixeldata for index "+index, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Utility Methods /////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * Finds the project with the given name 
     * @param the access token required to make this call
     * @param projectName
     * @return the project
     * @throws RemoteException
     */
    @Override
    public Project findProject(String accessToken, String projectName) 
    		throws RemoteException 
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "findProject", "getting Project for "+projectName +" actorLogin "+actorLogin);
			return CoercionHelper.toRemoteProject( SysManagerFactory.getProjectManager().getProject(actorLogin, projectName));
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findProject", "error finding project for name "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Returns the available source types 
     * @param the access token required to make this call
     * @return the available source types 
     */
    @Override
    public String[] listAvailableFormats(String accessToken) 
    		throws RemoteException 
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "listAvailableFormats", "Listing formats for "+ actorLogin);
			return null;//TODO
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "listAvailableFormats", "error finding formats", ex);
			throw new RemoteException(ex.getMessage());
		}
	}	
    
	/**
	 * Returns the list of record GUID for the specified archive
	 * @param the access token required to make this call
	 * @param archiveSignature the archive signature (unique for an archive)
	 * @return the list of record GUIDs
	 * @throws RemoteException
	 */
    @Override
	public long[] listGUIDsForArchive(String accessToken, String archiveSignature) 
			throws RemoteException
	{
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "listGUIDsForArchive", "Listing guids for archive "+ archiveSignature);
			return SysManagerFactory.getProjectManager().getGUIDsForArchive(actorLogin, Util.toBigInteger(archiveSignature));
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "listGUIDsForArchive", "error finding guids for archive "+archiveSignature, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
    /**
     * Returns the project the specified record (signature) is associated with 
     * @param the access token required to make this call
     * @param signature the signature of a record
     * @return the project the specified record (signature) is associated with, null otherwise
     */
    @Override
    public String findProjectForRecord(String accessToken, long guid) 
    		throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);

		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "findProjectForRecord", actorLogin +" finding project for record "+ guid);
			int projectID = SysManagerFactory.getRecordManager().getProjectID(guid);
			com.strandgenomics.imaging.iengine.models.Project p = SysManagerFactory.getProjectManager().getProject(projectID);
			
			return p == null ? null : p.getName();
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findProjectForRecord", "error finding project for guid "+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns the project the specified record (signature) is associated with 
     * @param the access token required to make this call
     * @param signature the signature of a record
     * @return the project the specified record (signature) is associated with, null otherwise
     */
    @Override
    public String[] findProjectForArchive(String accessToken, String archiveSignature) 
    		throws RemoteException 
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
			
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "findProjectForArchive", "finding project for archive "+ archiveSignature);
			
			List<String> projects = SysManagerFactory.getProjectManager().findProjectForArchive(actorLogin, Util.toBigInteger(archiveSignature));
			return projects == null || projects.isEmpty() ? null : projects.toArray(new String[0]);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findProjectForArchive", "error finding project for archive "+archiveSignature, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * Returns true if the specified signature exist in the system
     * @param fingerPrint the signature of a record
     * @return the guid of the record, if the specified signature exist in the system, -1 otherwise
     */
    @Override
    public long findGUID(String accessToken, FingerPrint fingerPrint) 
    		throws RemoteException 
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		Signature signature = CoercionHelper.toSignature(fingerPrint);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "findProjectForArchive", "finding guid for signature "+ signature);
			Long guid = SysManagerFactory.getRecordManager().findGuid(actorLogin, signature, true);
			logger.logp(Level.INFO, "ImageSpaceImpl", "findProjectForArchive", "found guid for signature "+ guid);
			return guid == null ? -1 : guid;
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findGUID", "error finding guid for signature "+signature, ex);
			throw new RemoteException(ex.getMessage());
		}
    }
   
    /**
     * find the records for the specified GUID
     * @param guid the universal identifier for records
     * @return the records for the specified GUID
     */
    @Override
    public Record[] findRecordForGUIDs(String accessToken, long[] guidList)
    		throws RemoteException
    {
    	String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		
		try
		{
			Set<Long> guids = new HashSet<Long>();
			for(long g : guidList) guids.add(g);
			
			logger.logp(Level.INFO, "ImageSpaceImpl", "findRecordForGUIDs", "finding Records for guids "+ guids.size());

			List<com.strandgenomics.imaging.iengine.models.Record> rList = SysManagerFactory.getRecordManager().findRecords(actorLogin, guids);
			return CoercionHelper.toRemoteRecords(actorLogin, rList);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "findRecordForGUIDs", "error finding Record for guids ", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    
    private String getClientIPAddress(){

        MessageContext context = MessageContext.getCurrentContext();
        Session session = context.getSession();

        String clientIP = null;
        if(session instanceof AxisHttpSession)
        {
            //clientIP = ((AxisHttpSession) session).getClientAddress();
        }

        if(clientIP == null)
            clientIP = "";
        
        return clientIP;
    }

	@Override
	public HistoryItem[] getRecordHistory(String accessToken, long guid) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRecordHistory", "getting history of record for guid "+ guid);

			List<HistoryObject> history = SysManagerFactory.getHistoryManager().getRecordHistory(actorLogin, guid);
			return CoercionHelper.toRemoteHistory(history);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getRecordHistory", "failed while history of record for guid "+ guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public void addRecordHistory(String accessToken, long guid, String historyMessage) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		Client client = Service.ISPACE.getClient(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addRecordHistory", "adding history of record="+guid+" message="+historyMessage);
			
			SysManagerFactory.getHistoryManager().insertHistory(guid, new Application(client.getName(), client.getVersion()), actorLogin, accessToken, HistoryType.CUSTOM_HISTORY ,historyMessage, historyMessage);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addRecordHistory", "failed while adding history", ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////// Bookmark APIs ///////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String[] getBookmarkSubFolders(String accessToken, String projectName, String path) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			int projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
			
			logger.logp(Level.INFO, "ImageSpaceImpl", "getBookmarkSubFolders", "returning subfolders under project "+projectId +" path="+path);
			
			return SysManagerFactory.getBookmarkManager().getSubFolderNames(actorLogin, projectId, path);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getBookmarkSubFolders", "failed returning subfolders under project "+projectName +" path="+path, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	@Override
	public Long[] getBookmarkGuids(String accessToken, String projectName, String path) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			int projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
			
			logger.logp(Level.INFO, "ImageSpaceImpl", "getBookmarkGuids", "returning guids under project "+projectId +" path="+path);
			
			List<Object> object = SysManagerFactory.getBookmarkManager().getBookmarks(actorLogin, projectId, path);
			long[] guids = (long[]) object.get(1);
			List<Long> ids = new ArrayList<Long>();
			for(long guid:guids)
			{
				ids.add(guid);
			}
			
			return ids.toArray(new Long[0]);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "getBookmarkGuids", "failed returning guids under project "+projectName +" path="+path, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	@Override
	public void createBookmarkFolder(String accessToken, String projectName, String parentPath, String folderName) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "createBookmarkFolder", "creating new folder "+folderName +" parentPath="+parentPath);
			
			int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
			
			SysManagerFactory.getBookmarkManager().addBookmarkFolder(actorLogin, projectID, parentPath, folderName);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "createBookmarkFolder", "failed creating new folder "+folderName +" parentPath="+parentPath, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	@Override
	public void addBookmark(String accessToken, String projectName, String path, long guid) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addBookmark", "adding bookmark to record "+guid +" path="+path);
			
			int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
			
			SysManagerFactory.getBookmarkManager().addBookmarks(actorLogin, projectID, path, guid);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addBookmark", "failed adding bookmark to record "+guid +" path="+path, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public String getBookmarkRoot(String accessToken, String projectName) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addBookmark", "returning bookmark root for project "+projectName);
			
			int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
			
			return SysManagerFactory.getBookmarkManager().getBookmarkRoot(actorLogin, projectID);
		}
		catch(Exception ex)
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "addBookmark", "returning bookmark root for project "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public AcquisitionProfile[] listAvailableProfiles(String accessToken) throws RemoteException
	{
		try
		{
			logger.logp(Level.INFO, "ImageSpaceImpl", "listAvailableProfiles", "listing available acq profiles");
			List<com.strandgenomics.imaging.iengine.models.AcquisitionProfile> acqProfiles = SysManagerFactory.getMicroscopeManager().listAcquisitionProfiles();
			return CoercionHelper.toRemoteAcqProfiles(acqProfiles);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "listAvailableProfiles", "error in listing available acq profiles", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public void setAcquisitionProfile(String accessToken, long guid, AcquisitionProfile remoteProfile) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		Client client = Service.ISPACE.getClient(accessToken);
		try
		{
			com.strandgenomics.imaging.iengine.models.AcquisitionProfile acqProfile = CoercionHelper.toLocalAcqProfile(remoteProfile);
			SysManagerFactory.getRecordManager().setAcquisitionProfile(new Application(client.getName(), client.getVersion()), actorLogin, accessToken, guid, acqProfile);
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "setAcquisitionProfile", "error in setting profile for guid="+guid, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public boolean requestAcquisitionLicense(String accessToken, String ipAddress, String macAddress) throws RemoteException
	{
		logger.logp(Level.INFO, "ImageSpaceImpl", "requestAcquisitionLicense", "requesting acquisition license");
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		logger.logp(Level.INFO, "ImageSpaceImpl", "requestAcquisitionLicense", "authenticated user "+actorLogin);
		try
		{
			return SysManagerFactory.getMicroscopeManager().requestAcquisitionLicense(actorLogin, accessToken, ipAddress, macAddress);
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "requestAcquisitionLicense", "error in requesting acquisition license", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public void surrenderAcquisitionLicense(String accessToken) throws RemoteException
	{
		try
		{
			SysManagerFactory.getMicroscopeManager().surrenderAcquisitionLicense(accessToken);
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "surrenderAcquisitionLicense", "error in surrendering the acquisition license", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public String getMicroscopeName(String accessToken, String ipAddress, String macAddress) throws RemoteException
	{
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			return SysManagerFactory.getMicroscopeManager().getMicroscope(ipAddress,macAddress);
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "surrenderAcquisitionLicense", "error in surrendering the acquisition license", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource getMosaicResource(
			String accessToken, MosaicRequest in1) throws RemoteException {
		
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		try
		{
			com.strandgenomics.imaging.iengine.models.MosaicResource resource = SysManagerFactory.getMosaicManager().getMosaicResource(actorLogin, CoercionHelper.toLocalMosaicRequest(in1));
			return CoercionHelper.toRemoteMosaicResource(resource);
		}
		catch (Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceImpl", "surrenderAcquisitionLicense", "error in surrendering the acquisition license", ex);
			throw new RemoteException(ex.getMessage());
		}
	}

	@Override
	public String getMosaicElementDownloadUrl(
			String accessToken,
			com.strandgenomics.imaging.iserver.services.ws.ispace.MosaicResource in1,
			MosaicParameters in2) throws RemoteException {
		
		String actorLogin = Service.ISPACE.validateAccessToken(accessToken);
		
		try {
			return SysManagerFactory.getMosaicManager().getMosaicElementDownloadUrl(actorLogin, getClientIPAddress(), CoercionHelper.toLocalMosaicResource(in1), CoercionHelper.toLocalMosaicParameters(in2));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
