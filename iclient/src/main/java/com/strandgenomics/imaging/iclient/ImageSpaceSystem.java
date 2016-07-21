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

package com.strandgenomics.imaging.iclient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strandgenomics.imaging.iclient.impl.ws.ispace.Area;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicParameters;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicRequest;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicResource;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.VOIndex;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.ImageType;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.SourceFormat;
import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.vo.Ellipse;
import com.strandgenomics.imaging.icore.vo.GeometricPath;
import com.strandgenomics.imaging.icore.vo.LineSegment;
import com.strandgenomics.imaging.icore.vo.Rectangle;
import com.strandgenomics.imaging.icore.vo.TextBox;
import com.strandgenomics.imaging.icore.vo.VisualObject;

/**
 * The underlying system class defining the APIs needed to support all the ImageSpace objects
 * @author arunabha
 */
public abstract class ImageSpaceSystem implements ImageSpace, ImageSpaceManagement, ImageSpaceWorkflow  {
	
    protected boolean connected = false;
    protected String  host      = null;
    protected int     port      = -1;
    protected String  user      = null;
    protected boolean useSSL = false;
    
    /**
     * returns the host address (of Enterprise ImageSpace Server) connected to,
     * null otherwise
     * @return m_host host name of the server
     */
    public String getHost()
    {
        return host;
    }
    
    /**
     * returns if the connection uses SSL or not
     * @return true if connection uses SSL, false otherwise
     */
    public boolean isSSL()
    {
    	return this.useSSL;
    }

    /**
     * return the server port number connected to, -1 otherwise
     * @return m_port server port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Enterprise ImageSpace Server needs authentication.
     * @return m_user the logged user name
     */
    public String getUser()
    {
        return user;
    }
    
	public abstract long findGUID(Signature signature);
	
	/**
	 * Returns the actual list of channels with custom contrast and all
	 * @param guid the record guid
	 * @return the list of channels with custom settings
	 */
	public abstract List<Channel> getRecordChannels(long guid);

	/**
	 * Returns the sites of the specified record
	 * @param guid the record guid
	 * @return the sites
	 */
	public abstract List<Site> getRecordSites(long guid);
	
    /**
     * find records for the project with search conditions
     * @param project parent project
     * @param conditions search conditions
     * @return record for specified search conditions under given project
     */
	public abstract long[] findRecords(Project project, Set<SearchCondition> conditions);

	/**
	 * find navigation bin for given project and given search conditions
	 * @param project parent project
	 * @param preConditions conditions
	 * @param current current set of conditions
	 * @return navigation node for the specified conditions and project
	 */
	public abstract List<NavigationBin> findNavigationBins(Project project, Set<SearchCondition> preConditions, SearchCondition current);

	/**
	 * name of the project
	 * @param projectName
	 * @return list of the users for the project
	 */
	public abstract List<User> getProjectMembers(String projectName);
	
	/**
	 * project manager for the project
	 * @param projectName
	 * @return list of managers for the project
	 */
	public abstract List<User> getProjectManager(String projectName);

	/**
	 * list of navigation fields for the project
	 * @param project name of the projects
	 * @return list of navigation fields for the project
	 */
	public abstract Collection<SearchField> getNavigableFields(Project project);
	
	/**
	 * list of user annotation fields for the project
	 * @param project name of the project
	 * @return list of user annotation fields for the project
	 */
	public abstract Collection<SearchField> getUserAnnotationFields(Project project);

	public Status getJobStatus(Task job)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * get attachment inputstream
	 * @param guid specified record
	 * @param attachmentName name of the attachment
	 * @return get attachment inputstream
	 */
	public abstract InputStream getAttachmentInputStream(long guid, String attachmentName);

	public void transfer(Signature record, Project another) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the non-standard (format specific) machine generated meta data associated with the specified record
	 * @param guid the record global unique identifier
	 * @return the dynamic meta data
	 */
	public abstract Map<String, Object> getDynamicMetaData(long guid);

	/**
	 * set given image file as thumbnail for record
	 * @param guid specified record
	 * @param imageFile thumbnail image
	 */
	public abstract void setRecordThumbnail(long guid, File imageFile);
	
	/**
	 * returns thumbnail image for the record
	 * @param guid specified record
	 * @return thumbnail image for the record
	 */
	public abstract BufferedImage getRecordThumbnail(long guid);

	/**
	 * pixel data for the record
	 * @param parent record
	 * @param imageCordinate dimension(t,z,c,site) 
	 * @return pixel data for the given image of the record
	 */
	public abstract IPixelData getPixelDataForRecord(Record parent, Dimension imageCordinate);

	public Map<String, Object> getImageMetaData(long guid, PixelData pixelData) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * list of available source formats
	 * @return list of available source formats
	 */
	public abstract Collection<SourceFormat> listAvailableFormats();
	
	/**
	 * returns raw pixel data for the image of the record
	 * @param guid record
	 * @param d dimension(t,z,c,site) 
	 * @return raw data for the given image of the record
	 */
	public abstract PixelArray getRawPixelData(long guid, Dimension d);
	
	/**
	 * returns image for specified dimension and record
	 * @param guid specified record
	 * @param d dimension(t,z,c,site)
	 * @param useChannelColor true if channel color is used
	 * @return image for specified dimension and record
	 */
	public abstract BufferedImage getPixelDataImage(long guid, Dimension d, boolean useChannelColor);
	
	/**
	 * get raw data for the tile
	 * @param guid specified record
	 * @param tile specified tile
	 * @return raw data for the tile
	 */
	public abstract PixelArray getRawDataForTile(long guid, Tile tile) ;
	
	/**
	 * image for the tile
	 * @param guid speicified record
	 * @param tile speicifed tile
	 * @param useChannelColor true if channel color is used
	 * @return image for the tile
	 */
	public abstract BufferedImage getTileImage(long guid, Tile tile, boolean useChannelColor);

	/**
	 * Returns the intensity distribution histogram of the specified image of the specified record 
	 * @param guid the record global unique identifier
	 * @param index the image index
	 * @return the intensity distribution statistics
	 */
	public abstract Histogram getIntensityDistibutionForImage(long guid, Dimension index);
	
	/**
	 * Returns the intensity distribution histogram of the specified tile of the specified record 
	 * @param guid the record global unique identifier
	 * @param index the image index
	 * @param tile image tile
	 * @return the intensity distribution statistics
	 */
	public abstract Histogram getIntensityDistibutionForTile(long guid, Dimension index, java.awt.Rectangle tile);
	
	/**
	 * Returns the RGB BufferedImage for the
	 * @param guid
	 * @param sliceNo
	 * @param frameNo
	 * @param siteNo
	 * @param channelNos
	 * @param zStacked
	 * @param mosaic
	 * @param useChannelColor
	 * @param height 
	 * @param width 
	 * @param y 
	 * @param x 
	 * @return
	 */
	public abstract BufferedImage getOverlayedImage(long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos, boolean zStacked, boolean mosaic, boolean useChannelColor, int x, int y, int width, int height);
	
	/**
	 * 
	 * @param guid
	 * @param sliceNo
	 * @param frameNo
	 * @param siteNo
	 * @param channelNos
	 * @param zStacked
	 * @param mosaic
	 * @param useChannelColor
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public abstract BufferedImage getOverlayedImage(long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos, boolean zStacked, boolean mosaic, boolean useChannelColor, int x, int y, int width, int height, int targetWidth, int targetHeight);
	
	/**
	 * returns the RGB buffered image for the specified settings and contrast
	 * @param guid record
	 * @param sliceNo slice
	 * @param frameNo frame 
	 * @param siteNo site
	 * @param channelNos list of channels
	 * @param zStacked true if zstacked
	 * @param mosaic true if channel mosaic
	 * @param useChannelColor true if channel color is to be set
	 * @param x x of upper left corner of tile
	 * @param y y of upper left corner of tile
	 * @param width width of the tile
	 * @param height height of the tile
	 * @param contrasts respective channel contrasts
	 * @return image with specified settings
	 */
	public abstract BufferedImage getOverlayedImage(long guid, int sliceNo, int frameNo, int siteNo, int[] channelNos, boolean zStacked, boolean mosaic, boolean useChannelColor, int x, int y, int width, int height, int[] contrasts);
	
	public void changeProjectManager(Project project, String userlogin) {
		// TODO Auto-generated method stub
	}

    /**
     * Adds the specified users as the member of the this project.
     * Note that the login user must be the project Manager of this project for this call to be successful
     * @param users list of users to add to the project
     * @return the list of users who are successfully added
     */
	public abstract void addProjectMembers(String projectName, Permission p, String ... userlogin);
	
	public List<User> removeProjectMembers(Project project,
			List<String> users) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ISourceReference> getSourceReferences(Experiment experiment) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Signature> getSignaturesForExperiment(Experiment experiment) {
		// TODO Auto-generated method stub
		return null;
	}

	public Record getRecordForExperiment(Experiment experiment, Signature signature)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * request the ticket for uploading experiment to a project
	 * @param project parent project
	 * @param experiment experiment to be uploaded
	 * @return ticket 
	 */
	public abstract Ticket requestTicket(Project project, RawExperiment experiment);

	/**
	 * status of the ticket
	 * @param ticket specified ticket
	 * @return
	 */
	public abstract Status getTicketStatus(Ticket ticket);
	
    /**
     * Adds user comment on the specified record 
     * @param signature the record under consideration
     * @param comments the comment to add
     */
    public abstract void addUserComment(long guid, String comments);
    
    /**
     * Returns all comments on the specified record 
     * @param signature the record under consideration
     * @return all comments on the specified record 
     * @throws RemoteException
     */
    public abstract List<UserComment> fetchUserComment(long guid);
    
    /**
     * set acquisition profile for record
     * @param guid specified record
     * @param profile specified profile
     */
    public abstract void setAcquisitionProfile(long guid, AcquisitionProfile profile);
    
    /**
     * surrenders acquisition license for current acq client
     */
    public abstract void surrenderAcquisitionLicense();
 
    /**
     * request license for acq client
     * @param user current user
     * @return true if license available, false otherwise
     */
    public abstract boolean requestAcquisitionLicense(String user);
    
	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////// record history methods ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
    public abstract List<HistoryItem> getRecordHistory(long guid);

	/**
	 * all the modifications (eg. addition/deletion of attachment, user
	 * annotation, visual annotation, comments, record creation) are implicitly added as history
	 * to every record by the server. THIS METHOD NEED NOT BE USED FOR SUCH MODIFICATIONS.
	 * 
	 * The purpose of this method is to add any special custom history message to the record.
	 * 
	 * @param guid
	 * @param historyMessage
	 */
    public abstract void addCustomHistory(long guid, String historyMessage);
    
	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////// record builder methods ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public abstract long registerRecordBuilder(String recordLabel, String projectName, Long parentGuid, int noOfFrames, int noOfSlices,
			int imageWidth, int imageHeight, List<Channel> channels, List<Site> sites, PixelDepth imageDepth, double xPixelSize,
			double yPixelSize, double zPixelSize, String sourceType, ImageType imageType, String machineIP, String macAddress,
			String sourceFolder, String sourceFilename, Long sourceTime, Long creationTime, Long acquiredTime);
    
	/**
	 * adds image raw and metadata for the builder
	 * @param builderId speicifed builder
	 * @param dim image dimension
	 * @param pixelArr raw data
	 * @param pixelData meta data
	 * @return true if successfully added, false otherwise
	 */
    public abstract boolean addImageData(long builderId, Dimension dim, PixelArray pixelArr, PixelMetaData pixelData);
    
    /**
     * commits the current record builder
     * @param builderId 
     */
    public abstract void commitRecordBuilder(long builderId);
    
    /**
     * aborts the current record builder
     * @param builderId
     */
    public abstract void abortRecordBuilder(long builderId);
    
	/////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////// user annotation methods ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

    /**
     * returns the user annotations for specified record
     * @param guid specified record
     * @return the user annotations for specified record
     */
	public abstract Map<String, Object> getUserAnnotations(long guid);

	/**
	 * list of record attachments
	 * @param guid specified record
	 * @return list of record attachment
	 */
	public abstract Collection<IAttachment> getRecordAttachments(long guid);

	/**
	 * Updates the notes associated with the specified attachment of the specified record (specified by its guid)
	 * @param guid the global unique identifier of the record under consideration
	 * @param attachmentName name of the attachment (is unique for a record)
	 * @param notes the notes to associate it with
	 */
	public abstract void updateAttachmentNotes(long guid, String attachmentName, String notes);

	public abstract void deleteAttachment(long guid, String attachmentName);

	public abstract void addRecordAttachments(long guid, File attachmentFile, String name, String notes);
	
	public abstract void addRecordUserAnnotation(long guid, String name, long value);

	public abstract void addRecordUserAnnotation(long guid, String name, double value);

	public abstract void addRecordUserAnnotation(long guid, String name, String value);
	
	public abstract void addRecordUserAnnotation(long guid, String name, Date value);
	
	public abstract void updateRecordUserAnnotation(long guid, String name, long value);

	public abstract void updateRecordUserAnnotation(long guid, String name, double value);
	
	public abstract void updateRecordUserAnnotation(long guid, String name, Date value);

	public abstract void updateRecordUserAnnotation(long guid, String name, String value);
	
	public abstract void addRecordUserAnnotation(long guid, Map<String, Object> annotations);
	
	/////////////////////////////////////////////////////////////////////////////////////
	////////////////////////// visual annotation methods ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////

	public abstract Collection<Ellipse> getEllipticalShapes(long guid, VisualOverlay visualOverlay);

	public abstract Collection<LineSegment> getLineSegments(long guid, VisualOverlay visualOverlay);

	public abstract Collection<Rectangle> getRectangularShapes(long guid, VisualOverlay visualOverlay);

	public abstract Collection<TextBox> getTextBoxes(long guid, VisualOverlay visualOverlay);

	public abstract Collection<GeometricPath> getFreeHandShapes(long guid, VisualOverlay visualOverlay);

	public abstract Collection<VisualObject> getVisualObjects(long guid, VisualOverlay visualOverlay);
	
	public abstract Collection<IVisualOverlay> getVisualOverlays(long guid, VODimension dimension);

	public abstract IVisualOverlay getVisualOverlay(long guid, VODimension coordinate, String name);
	
	public abstract Collection<VisualObject> findVisualObjects(long guid, VODimension coordinate, String overlayName, Area a);

	/**
	 * returns all the overlays for specified record and site
	 * @param guid specified record
	 * @param siteNo specified site
	 * @return all the visual overlays
	 */
	public abstract Set<String> getAvailableVisualOverlays(long guid, int siteNo);

	/**
	 * create visual overlay for specied record with given name
	 * @param guid specified record 
	 * @param siteNo speicifed site
	 * @param name specified name
	 */
	public abstract void createVisualOverlays(long guid, int siteNo, String name);

	/**
	 * delete the visual overlay, all the objects will be deleted
	 * @param guid specified record
	 * @param siteNo specified site
	 * @param name name of the overlay to be deleted
	 */
	public abstract void deleteVisualOverlays(long guid, int siteNo, String name);

	public abstract void addVisualObjects(long guid, Collection<VisualObject> vObjects, String name, VODimension ... imageCoordinates);

	/**
	 * delete visual objects for the specified overlay on specified record
	 * @param guid record
	 * @param vObjects objects to be deleted
	 * @param name name of the overlay
	 * @param imageCoordinates dimensions form which the objects to be deleted
	 */
	public abstract void deleteVisualObjects(long guid, Collection<VisualObject> vObjects, String name, VODimension ... imageCoordinates);
	
	/**
	 * Returns the project associated with the specified record
	 * @param guid the record global unique identifier
	 * @return  project associated with the specified record
	 */
	public abstract Project findProject(long guid);
	
	/**
	 * return name of the parent project for the record
	 * @param guid specified record
	 * @return name of the parent project
	 */
	public abstract String findProjectName(long guid);
	
	public abstract boolean isArchiveExist(BigInteger archiveSignature);
	
	public abstract void setChannelLUT(Record record, int channelNo, String lut);
	
	public abstract void setChannelContrast(Record record, int channelNo, VisualContrast contrast);

	/**
	 * Returns the InputStream to read the images for all slices from the specified frame and site
     * and overlaid on all channels (and possibly scaled downed to a low value)
     * This is needed for 3D viewing
	 * @param guid the global unique identifier of the record under consideration
	 * @param frameNo the frame number
	 * @param siteNo the site number
	 * @param imageWidth the image width (used for scaling, if needed)
	 * @param useChannelColor checks whether to use channel colors
	 * @return
	 */
	public abstract InputStream getChannelOverlaidImagesForSlices(long guid, int frameNo, int siteNo, 
			int imageWidth, boolean useChannelColor);

	public abstract InputStream exportExperiment(BigInteger experimentID);

	public abstract List<BigInteger> listArchives(Project project);

	public abstract long[] getGUIDsForArchive(Experiment experiment);
	
	/////////////////////////////////////////////////////////////////////////////////////
	////////////////////////// bookmark related methods ////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	public abstract BookmarkFolder getBookmarkRoot(Project project);
	
	public abstract BookmarkFolder[] getSubfolders(BookmarkFolder folder);
	
	public abstract long[] getBookmarkedRecords(BookmarkFolder folder);
	
	public abstract void createBookmarkFolder(BookmarkFolder parentFolder, String newFolder);
	
	public abstract void addBookmark(BookmarkFolder parentFolder, long guid);
	
	public abstract MosaicResource getMosaicResource(MosaicRequest request);
	
	public abstract BufferedImage getMosaicElementDownloadUrl(MosaicResource resource, MosaicParameters params);
}
