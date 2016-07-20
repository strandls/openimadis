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

/*
 * ImageSpaceService.java
 *
 * AVADIS Image Management System
 * Web-service definition
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park,
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iserver.services.def.ispace;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;


/**
 * The interface defining the public API available from Enterprise ImageSpace services (AvadisImageSpace)  <br>
 * Project (ProjectObject) is a collection of records <br>
 * A record (RecordObject) is a collection of pixel data (ImageObject) and identified by its unique signature (FingerPrint) <br>
 * ImageObject within a record is identified by its coordinates <br>
 * @author arunabha
 */
public interface ImageSpaceService {
	
	/**
	 * Returns the list of archives associated with the specified project
	 * @param the access token required to make this call
	 * @param projectName name of the project
	 * @return list of archive signatures within the specified project
	 * @throws RemoteException
	 */
	public String[] listArchives(String accessToken, String projectName) 
			throws RemoteException;
	
    
    /**
     * The list of active projects that the connected user have permission to read
     * @param the access token required to make this call
     * @return list of active projects that the connected user have permission to read
     */
    public String[] getActiveProjects(String accessToken) 
    		throws RemoteException;

    /**
     * The list of archived projects that the connected user have permission to browse, may be null otherwise
     * @param the access token required to make this call
     * @return list of active projects that the connected user have permission to read
     */
    public String[] getArchivedProjects(String accessToken) 
    		throws RemoteException;
	
	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////// Image APIs /////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * Returns the PixelData for the specified coordinate for the given record 
     * @param the access token required to make this call
     * @param guid the record under consideration 
     * @param imageIndex relevant image coordinate
     * @return the handle to the relevant PixelData
     */
    public Image getPixelDataForRecord(String accessToken, long guid, ImageIndex index) 
    		throws RemoteException;
	
	/**
     * Returns the intensity distribution statistics for the specified image within the record
     * @param the access token required to make this call
     * @param guid the record under consideration 
     * @param channel the channel
     * @return the intensity distribution statistics
     * @throws RemoteException
     */
	public Statistics getIntensityDistibution(String accessToken, long guid, ImageIndex index)
			throws RemoteException;
	
	/**
	 * returns the intensity distribution for the specified tile of the specified image within the record
	 * @param accessToken required to make this call 
	 * @param guid the record under consideration 
	 * @param index image index
	 * @param x x coordinate of top-left corner of the region of interest
	 * @param y y coordinate of top-left corner of the region of interest
 	 * @param width width of the region of interest
	 * @param height height of the region of interest
	 * @return the intensity distribution statistics
	 * @throws RemoteException
	 */
	public Statistics getIntensityDistibutionForTile(String accessToken, long guid, ImageIndex index, int x, int y, int width, int height) throws RemoteException;
	
    /**
     * Sets a custom contrast for all images of this record for the specified channel
     * @param the access token required to make this call
     * @param guid the record under consideration
     * @param channelNo the relevant channel number
     * @param contrast the custom contrast to set
     * @param lut the channel lut to set
     */
    public void setChannelColorAndContrast(String accessToken, long guid, int channelNo, 
    		Contrast contrast, String lut) throws RemoteException;

    /**
     * Returns the custom contrast, if any, for all images of this record for the specified channel
     * @param accessToken the access token required to make this call
     * @param guid the record under consideration
     * @return the channels with custom contrast settings
     */
    public Channel[] getRecordChannels(String accessToken, long guid)
    		throws RemoteException;
    
    /**
     * Returns the sites of this record 
     * @param accessToken the access token required to make this call
     * @param guid the GUID of  the record under consideration
     * @return the sites associated with the record
     * @throws RemoteException
     */
    public RecordSite[] getRecordSite(String accessToken, long guid)
    		throws RemoteException;
    
    /**
     * To add custom image as a thumb-nail
     * @param the access token required to make this call
     * @param guid the record under consideration
     * @return the URL to upload the image for thumb-nail
     */
    public String getThumbnailUploadURL(String accessToken, long guid) 
    		throws RemoteException;

    /**
     * Returns the URL to download the thumb-nail image for the specified record
     * @param the access token required to make this call
     * @param guid the record under consideration
     * @return the URL to download the thumb-nail image for the specified record
     */
    public String getThumbnailDownloadURL(String accessToken, long guid) 
    		throws RemoteException;
    
	 /**
     * Returns the URL to download the raw pixel data (as a gzip-ed byte array) associated with the specified coordinate
     * @param the access token required to make this call
     * @param parentRecord the record under consideration 
     * @param imageIndex the image-coordinate under consideration 
     * @return the URL to download the raw pixel data associated with the specified coordinate
     */
    public String getRawIntensitiesDownloadURL(String accessToken, long guid, ImageIndex index) 
    		throws RemoteException;
	
	 /**
     * Returns the URL to download the pixel image (as a gzip-ed byte array) associated with the specified coordinate
     * @param the access token required to make this call
     * @param guid the record under consideration 
     * @param imageIndex the image-coordinate under consideration 
     * @return the URL to download the raw pixel data associated with the specified coordinate
     */
    public String getImageDownloadURL(String accessToken, long guid, boolean useChannelColor, 
    		ImageIndex index) throws RemoteException;
    
    /**
     * Returns the URL to download the pixel-data of the specified Tile/Block as a gzip-ed byte array
     * @param the access token required to make this call
     * @param guid the record under consideration 
	 * @param imageIndex the image-coordinate under consideration 
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     */
    public String getTileIntensitiesDownloadURL(String accessToken, long guid,
    		ImageIndex index, int x, int y, int width, int height) throws RemoteException;

    /**
     * Returns the URL to download the pixel-data of the specified Tile/Block as a gzip-ed byte array
     * @param the access token required to make this call
     * @param guid the record under consideration 
	 * @param imageIndex the image-coordinate under consideration 
	 * @param useChannelColor whether to use channel colors
	 * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     */
    public String getTileImageDownloadURL(String accessToken, long guid, boolean useChannelColor,
    		ImageIndex index, int x, int y, int width, int height) throws RemoteException;

    /**
     * Returns the URL to download the pixel-data overlay as a gzip-ed byte array
     * @param the access token required to make this call
     * @param guid the record under consideration 
     * @param channels list of channels to overlay
     * @param useChannelColor whether to use channel colors
     * @param zStacked whether to do z-stack projections (max intensity value) per channels
     * @param mosaic whether to stack the images or tile them (mosaic)
     * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
     * @return the URL to download the pixel-data overlay as a image
     */
    public String getOverlayImageDownloadURL(String accessToken, long guid, 
    		int frameNo, int sliceNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height) throws RemoteException;
    
    /**
     * 
     * @param accessToken
     * @param guid
     * @param frameNo
     * @param sliceNo
     * @param siteNo
     * @param channels
     * @param useChannelColor
     * @param zStacked
     * @param mosaic
     * @param x
     * @param y
     * @param width
     * @param height
     * @param destWidth image will be rescaled to destination width
     * @param destHeight image will be rescaled to destination height
     * @return
     * @throws RemoteException
     */
    public String getOverlayImageDownloadURL(String accessToken, long guid, 
    		int frameNo, int sliceNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height, int destWidth, int destHeight) throws RemoteException;
    
    /**
     * Returns the URL to download the pixel-data overlay as a gzip-ed byte array
     * @param the access token required to make this call
     * @param guid the record under consideration 
     * @param channels list of channels to overlay
     * @param useChannelColor whether to use channel colors
     * @param zStacked whether to do z-stack projections (max intensity value) per channels
     * @param mosaic whether to stack the images or tile them (mosaic)
     * @param x top left position in pixel coordinate
	 * @param y top left position in pixel coordinate
	 * @param width width in pixels
	 * @param height height in pixels
	 * @param contrasts list of contrasts per selected channel
     * @return the URL to download the pixel-data overlay as a image
     */
    public String getOverlayImageDownloadURL(String accessToken, long guid, 
    		int frameNo, int sliceNo, int siteNo, int[] channels, 
    		boolean useChannelColor, boolean zStacked, boolean mosaic, int x, int y, int width, int height, int contrasts[]) throws RemoteException;
    
    /**
     * Returns the URL to download the images for all slices from the specified frame and site
     * and overlaid on all channels (and possibly scaled downed to a low value)
     * This is needed for 3D viewing
     * @param the access token required to make this call
     * @param guid the record under consideration 
     * @param frameNo the frame number
     * @param siteNo the site number
     * @param imageWidth image width of the requested images (for scaling)
     * @param useChannelColor specifies whether to use channel colors
     * @param imageFormat format of the image - e.g., PNG, TIFF, JPEG etc
     * @return the URL to download the pixel-data overlay as an image
     */
	public String getChannelOverlaidSliceImagesURL(String accessToken, long guid, 
			int frameNo, int siteNo, int imageWidth, boolean useChannelColor) throws RemoteException;
	
	
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
    public String createRecordAttachment(String accessToken, long guid, 
    		String name, String notes) throws RemoteException;

    /**
     * Returns a URL to download the specified attachment
     * @param parentRecord the record (it's signature) containing the attachment
     * @param name name of the attachment
     * @return a URL to download the specified attachment
     */
    public String getAttachmentDownloadURL(String accessToken, long guid, String name) 
    		throws RemoteException;
    
    /**
     * Returns available attachments associated with this record
     * @param record the record under consideration
     * @return list user added attachments (files) associated with this record
     */
    public RecordAttachment[] getRecordAttachments(String accessToken, long guid) 
    		throws RemoteException;
    
    /////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Meta Data APIs ////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Adds user comment on the specified record 
     * @param signature the record under consideration
     * @param comments the comment to add
     */
    public void addUserComment(String accessToken, long guid, String comments) 
    		throws RemoteException;
    
    /**
     * Returns all comments on the specified record 
     * @param signature the record under consideration
     * @return all comments on the specified record 
     * @throws RemoteException
     */
    public Comments[] fetchUserComment(String accessToken, long guid) 
    		throws RemoteException;

    /**
     * Returns additional machine generated (read-only) meta data associated with this record
     * The legal values are one of String and Numbers
     * @param record the record under consideration
     * @return list of machine generated meta data associated with this record
     */
    public Property[] getDynamicMetaData(String accessToken, long guid) 
    		throws RemoteException;

    /**
     * Returns all relevant user annotations associated with this record
     * The legal values are one of String and Numbers
     * @param record the record under consideration
     * @return list user added meta data associated with this record
     */
    public Property[] getRecordUserAnnotations(String accessToken, long guid) 
    		throws RemoteException;

    /**
     * Adds the specified user annotations with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
     * @param record the record under consideration
     * @param annotations list of annotations or meta data to add to the record
     */
    public void addRecordUserAnnotation(String accessToken, long guid, Property[] annotations) 
    		throws RemoteException;

    /**
     * Returns the list of additional meta data associated with the specified image
     * @param parentRecord the record under consideration 
     * @param imageIndex the image under consideration
     * @return the list of additional meta data associated with the specified image
     */
    public Property[] getImageMetaData(String accessToken, long guid, ImageIndex imageIndex)
    		throws RemoteException;
    
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Record History APIs /////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * returns list of history items associated with specified record
     * @param guid
     * @return the list of history items associated with specified record
     * @throws RemoteException
     */
    public HistoryItem[] getRecordHistory(String accessToken, long guid) throws RemoteException;
    
    /**
     * adds specified history message as custom history item on specified record
     * @param accessToken
     * @param guid
     * @param historyMessage
     * @throws RemoteException
     */
    public void addRecordHistory(String accessToken, long guid, String historyMessage) throws RemoteException;
    
	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Visual Overlays & Objects APIs //////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Returns the relevant visual overlays associated the specified dimension of the given record  
     * @param signature the record signature under consideration 
     * @param dimension the image coordinate
     * @return all relevant visual overlays associated the specified dimension of the given record  
     */
	public Overlay[] getVisualOverlays(String accessToken, long guid, 
			VOIndex dimension) throws RemoteException;

    /**
     * Returns the specific of visual overlay associated with the specified coordinate and name 
     * @param signature  the record signature under consideration 
     * @param overlayName name of the visual overlay
     * @return the visual overlay associated with the specified dimension and name 
     */
	public Overlay getVisualOverlay(String accessToken, long guid, 
			VOIndex coordinate, String overlayName) throws RemoteException;

	/**
	 * Returns the list of visual annotation names associated with the specified record at the specified site
	 * @param signature the record signature under consideration 
	 * @param siteNo the site
	 * @return the list of visual annotation names associated with the specified record at the specified site
	 */
	public String[] getAvailableVisualOverlays(String accessToken, long guid, int siteNo)
			throws RemoteException;

    /**
     * Creates visual overlays for the specified record for all relevant frames and slices for the fixed site
     * @param signature the record signature under consideration 
     * @param siteNo the site
     * @param name name of the visual annotation
     * @return a overlay that is created
     */
	public void createVisualOverlays(String accessToken, long guid, int siteNo, String name)
			throws RemoteException;
	
	/**
	 * Adds the specified visual objects to the visual-overlays of the specified records 
	 * @param signature the record signature under consideration 
	 * @param vObjects the shapes - visual objects
	 * @param overlayName name of the overlay
	 * @param imageCoordinates the coordinates of the overlay
	 */
	public void addVisualObjects(String accessToken, long guid, 
			Shape[] vObjects, String overlayName, VOIndex[] imageCoordinates) throws RemoteException;

	   /**
     * Return all visual objects with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of visual objects
     */
    public Shape[] getVisualObjects(String accessToken, long guid, VOIndex index, 
    		String overlayName) throws RemoteException;

    /**
     * Return all elliptical shapes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
	public EllipticalShape[] getEllipticalShapes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException;

    /**
     * Return all straight lines with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
	public StraightLine[] getLineSegments(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException;

    /**
     * Return all rectangular shapes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
	public RectangularShape[] getRectangularShapes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException;

    /**
     * Return all text boxes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of elliptical shapes if any, null otherwise
     */
	public TextArea[] getTextBoxes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException;

    /**
     * Return all free-hand shapes with the specified named visual overlay
     * @param parentRecord the record under consideration 
     * @param overlayName name of the overlay
     * @return an array of free-hand shapes if any, null otherwise
     */
	public FreehandShape[] getFreeHandShapes(String accessToken, long guid, 
			VOIndex index, String overlayName) throws RemoteException;
	
	/**
	 * find the dimensions where the specified overlay exists (non-empty visual objects)
	 * @param parentRecord the record under consideration
	 * @param overlayName name of the overlay
	 * @return the dimensions where there are at least one visual objects
	 */
	public VOIndex[] findOverlayLocation(String accessToken, long guid, 
			int siteNo, String overlayName) throws RemoteException;
	
	/**
	 * find the dimensions where the specified visual object exists
	 * @param parentRecord the record under consideration
	 * @param siteNo overlays are specific to sites, so the relevant site no
	 * @param overlayName name of the overlay
	 * @param the visual object to search
	 * @return the dimensions where the overlay contains the specified object
	 */
	public VOIndex[] findVisualObjectLocation(String accessToken, long guid, 
			int siteNo, String overlayName, Shape Object) throws RemoteException;
	
	/**
	 * find the visual objects within the specified area
	 * @param parentRecord the record under consideration
	 * @param index the image index
	 * @param overlayName name of the overlay
	 * @param the area under consideration
	 * @return the dimensions where the overlay contains the specified object
	 */
	public Shape[] findVisualObjects(String accessToken, long guid, 
			VOIndex index, String overlayName, Area a) throws RemoteException;

	/////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// Microscope/Acquisition Methods //////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * lists all the acquisition profiles available
	 * @param accessToken
	 * @return
	 * @throws RemoteException
	 */
	public AcquisitionProfile[] listAvailableProfiles(String accessToken) throws RemoteException;
	
	/**
	 * get the name of the microscope for specified mac and ip address
	 * @param accessToken
	 * @param ipAddress
	 * @param macAddress
	 * @return the name of the microscope for specified mac and ip address, null if not registered
	 * @throws RemoteException
	 */
	public String getMicroscopeName(String accessToken, String ipAddress, String macAddress) throws RemoteException;
	
	/**
	 * set specified acquisition profile on specified record
	 * @param accessToken 
	 * @param guid specified record
	 * @param profile specified profile
	 * @throws RemoteException
	 */
	public void setAcquisitionProfile(String accessToken, long guid, AcquisitionProfile profile) throws RemoteException;
	
	/**
	 * request access token for specified access token and specified machine
	 * @param accessToken access token
	 * @param clientIpAddress specified client ip address
	 * @param clientMacAddress specified mac address
	 * @return true if license is granted, false otherwise
	 * @throws RemoteException
	 */
	public boolean requestAcquisitionLicense(String accessToken, String clientIpAddress, String clientMacAddress) throws RemoteException;
	
	/**
	 * surrender acquisition license used for access token
	 * @param accessToken specified access token
	 * @throws RemoteException
	 */
	public void surrenderAcquisitionLicense(String accessToken) throws RemoteException;
	
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
    public Project findProject(String accessToken, String projectName) 
    		throws RemoteException;
	
    /**
     * Returns the available source types 
     * @param the access token required to make this call
     * @return the available source types 
     */
    public String[] listAvailableFormats(String accessToken) 
    		throws RemoteException;	
    
	/**
	 * Returns the list of record GUID for the specified archive
	 * @param the access token required to make this call
	 * @param archiveSignature the archive signature (unique for an archive)
	 * @return the list of record GUIDs
	 * @throws RemoteException
	 */
	public long[] listGUIDsForArchive(String accessToken, String archiveSignature) 
			throws RemoteException;
	
    /**
     * Returns the project the specified record (signature) is associated with 
     * @param the access token required to make this call
     * @param signature the signature of a record
     * @return the project the specified record (signature) is associated with, null otherwise
     */
    public String findProjectForRecord(String accessToken, long guid) 
    		throws RemoteException;

    /**
     * Returns the project the specified record (signature) is associated with 
     * @param the access token required to make this call
     * @param signature the signature of a record
     * @return the project the specified record (signature) is associated with, null otherwise
     */
    public String[] findProjectForArchive(String accessToken, String archiveSignature) 
    		throws RemoteException;

    /**
     * Returns true if the specified signature exist in the system
     * @param signature the signature of a record
     * @return the guid of the record, if the specified signature exist in the system, -1 otherwise
     */
    public long findGUID(String accessToken, FingerPrint signature) 
    		throws RemoteException;
    
    /**
     * find the records for the specified GUID
     * @param guid the universal identifier for records
     * @return the records for the specified GUID
     */
    public Record[] findRecordForGUIDs(String accessToken, long[] guid)
    		throws RemoteException;
    
    /**
     * returns bookmakr root for this project
     * @param accessToken 
     * @param project specified project
     * @return bookmark root
     * @throws RemoteException
     */
    public String getBookmarkRoot(String accessToken, String project) throws RemoteException;
    
    /**
     * returns subfolders under given bookmark hierarchy 
     * @param accessToken
     * @param projectId specified project id
     * @param path given bookmark hierarchy 
     * @return subfolder names
     * @throws RemoteException
     */
	public String[] getBookmarkSubFolders(String accessToken, String project, String path) throws RemoteException;
	
	/**
	 * returns guids under given bookmark hierarchy (not recursive) 
	 * @param accessToken
	 * @param projectId
	 * @param path given bookmark hierarchy 
	 * @return guids under given bookmark hierarchy (not recursive) 
	 * @throws RemoteException
	 */
	public Long[] getBookmarkGuids(String accessToken, String project, String path) throws RemoteException;
	
	/**
	 * creates subfolder under given bookmark hierarchy 
	 * @param accessToken
	 * @param projectID project id
	 * @param parentPath bookmark hierarchy 
	 * @param folderName name of new folder to be created
	 * @throws RemoteException
	 */
	public void createBookmarkFolder(String accessToken, String project, String parentPath, String folderName) throws RemoteException;
	
	/**
	 * adds new bookmark under given bookmark hierarchy 
	 * @param accessToken
	 * @param projectID parent project
	 * @param path under which record will be bookmarked
	 * @param guid record to be bookmarked
	 * @throws RemoteException
	 */
	public void addBookmark(String accessToken, String project, String path, long guid) throws RemoteException;

	/**
	 * gives the mosaic resource for the provided request
	 *  @param accessToken
	 * @param request
	 * @throws RemoteException
	 */
	public MosaicResource getMosaicResource(String accessToken, MosaicRequest request) throws RemoteException;

	/**
	 * gives the requested element from specified resource
	 * @param resource
	 * @return
	 * @throws IOException 
	 */
	public String getMosaicElementDownloadUrl(String actorLogin, MosaicResource resource, MosaicParameters element) throws RemoteException;
}
