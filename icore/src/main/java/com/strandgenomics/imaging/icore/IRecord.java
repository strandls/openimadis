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

package com.strandgenomics.imaging.icore;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.vo.VisualObject;


/**
 * Record is a collection of images possibly specified in six dimensions (x,y,z,time,site,channel) 
 * and associated with a single acquisition instance
 * @author arunabha
 */
public interface IRecord extends Disposable {
	
	/**
	 * Returns the containing project for this record
	 * @return the containing project for this record
	 */
	public IProject getParentProject();
	
	/**
	 * Returns a description of the original source files generating the experiment
	 * @return a description of the original source files generating the experiment
	 */
	public List<ISourceReference> getSourceReference();
	
	/**
	 * Returns the containing experiment
	 * @return the containing experiment
	 */
	public IExperiment getExperiment();
	
	/**
	 * Each record has a unique signature or finger print. 
	 * This method returns the signature associated with this record. Signature is typically the md5 hash
	 * of the data represented by the fixed field plus one representative image within the record 
	 * (say the one with center coordinates)
	 * @return md5 sum of the record
	 * @throws IOException 
	 */
	public Signature getSignature();
	
    /**
     * Returns the MAC address - A Media Access Control address - (ethernet card) - network hardware address of the acquisition computer  
     * Note: more that one network interface may be associated with the computer, we may need to pick the first one after sorting
     * @return mac address of the machine from which record is uploaded
     */
    public String getOriginMachineAddress();
    
    /**
     * Returns the Internet Protocol (IP) address of the acquisition computer
     * @return ip address of the machine from which record is uploaded 
     */
    public String getOriginMachineIP();
    
    /**
     * Returns the record source file's last modification time
     * Note that this time may be same as the Acquired Date
     * @return source file's last modification time
     */
    public Date getSourceFileTime();
    
    /**
     * Returns the creation time of this record (
     * Note that this time may be same as the Acquired Date
     * @return time when record is created
     */
    public Date getCreationTime();
    
    /**
     * Returns the upload time or acquisition time of this record
     * @return time when record is uploaded
     */
    public Date getUploadTime();
    
    /**
     * Returns the acquired date of this record as extracted from the source files
     * it is string because, the date format is unknown
     * @return the acquired date of this record as extracted from the source files
     */
    public Date getAcquiredDate();
    
    /**
     * Returns the number of Z-axis slices (planes) supported by this record
     * @return the number of Z-axis slices (planes) supported by this record
     */
    public int getSliceCount();
    
    /**
     * Returns the maximum number of frames (time samples) associated with this record
     * @return the number of frames (time samples) supported by this record
     */
    public int getFrameCount();
    
    /**
     * Returns the number of different channels available with this record
     * @return the number of channels
     */
    public int getChannelCount();
    
    /**
     * Returns the channel for the corresponding channel number (zero based) 
     * @return the channel for the corresponding channel number (zero based) 
     */
    public IChannel getChannel(int channelNo);
    
    /**
     * Returns the channel number (index, zero based) if the specified channel is
     * there with the record
     * @param channel the channel under consideration
     * @return the channel index
     * @exception ArrayIndexOutOfBoundsException is the specified channel is not there
     */
    public int getChannelIndex(IChannel channel);
    
    /**
     * Returns the number of different sites available with this record
     */
    public int getSiteCount();
    
    /**
     * Returns the site for the corresponding site number (zero based) 
     * @return the site for the corresponding site number (zero based) 
     */
    public Site getSite(int siteNo);

    /**
     * Returns the width (in pixels) of the images represented by this record
     * Note that width is same for all images within the record
     */
    public int getImageWidth();
    
    /**
     * Returns the height (in pixels) of the images represented by this record
     * Note that height is same for all images within the record
     */
    public int getImageHeight();
    
    /**
     * Returns the total number of images represented by this record 
     */
    public int getImageCount();
    
    /**
     * Returns true iff the specified image coordinate is valid for this record
     * @param imageCoordinate the image coordinate to validate 
     * @return true iff the specified image coordinate is valid for this record, false otherwise
     */
    public boolean isValidCoordinate(Dimension imageCoordinate);
    
    /**
     * Returns the Quality of the images (one of 32/16/8 bit images)
     */
    public PixelDepth getPixelDepth();
    
    /**
     * Pixels are small square dots on the computer screen/image
     * Returns the physical length represented by a pixel along the x-axis 
     */
    public double getPixelSizeAlongXAxis();
    
    /**
     * Returns the physical length represented by a pixel along the y-axis 
     */
    public double getPixelSizeAlongYAxis();
    
    /**
     * Returns the physical length represented by a pixel along the z-axis 
     */
    public double getPixelSizeAlongZAxis();
    
    /**
     * image type - a fixed set of values (Grayscale, RGB32, etc.) - default is Grayscale if no images are present, otherwise it is inferred from the data  
     */
    public ImageType getImageType();
    
    /**
     * the original storage file format of this record
     */
    public SourceFormat getSourceType();
    
    /**
     * Returns the name of one of the source file 
     * @return the name of one of the source file 
     */
	public String getSourceFilename();
	
	/**
	 * Returns the folder (absolute path in the acquisition machine) from which the record was created 
	 * @return the folder for which the record was created 
	 */
	public String getRootDirectory();

    /**
     * Returns additional machine generated (read-only) meta data associated with this record
     * The legal values are one of String and Numbers
     */
    public Map<String, Object> getDynamicMetaData();
    
    /**
     * Returns all relevant user annotations associated with this record
     * The legal values are one of String and Numbers
     */
    public Map<String, Object> getUserAnnotations();
    
    /**
     * Returns all relevant attachments associated with this record
     */
    public Collection<IAttachment> getAttachments();
    
    /**
     * Adds an attachment to the record
     * @param attachmentFile the file to attach
     * @param notes short description associated with this attachment
     * @throws IOException 
     */
    public void addAttachment(File attachmentFile, String notes) throws IOException;
    
    /**
     * Adds an attachment to the record
     * @param attachmentFile the file to attach
     * @param attachmentName attachment file name
     * @param notes short description associated with this attachment
     * @throws IOException 
     */
    public void addAttachment(File attachmentFile, String attachmentName, String notes) throws IOException;
    
    /**
     * Removes the named attachment from this record  (name is unique for a record)
     * @param name name of the attachment
     */
    public void removeAttachment(String name);
    
	/**
     * adds the specified user annotation with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
	 * @param name key of user annotation 
	 * @param value value of user annotation
	 */
    public void addUserAnnotation(String name, long value);
    
    /**
     * adds the specified user annotation with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
	 * @param name key of user annotation 
	 * @param value value of user annotation
	 */
    public void addUserAnnotation(String name, double value);
    
    /**
     * adds the specified user annotation with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
	 * @param name key of user annotation 
	 * @param value value of user annotation
	 */
    public void addUserAnnotation(String name, String value);
    
    /**
     * adds the specified user annotation with this record
     * Notes that user annotations are single valued and unique across for a given record irrespective of its type
	 * @param name key of user annotation 
	 * @param value value of user annotation
	 */
    public void addUserAnnotation(String name, Date value);
    
    /**
     * removes the specified named user annotations 
     * @param name name of the user annotation
     * @return the existing value of the annotation or null
     */
    public Object removeUserAnnotation(String name);
    
    /**
     * add the specified list of user annotations with this record. 
     * Note that only string, and numbers values are supported, rest will be ignored
     * @param annotations list of user annotations as key-value pairs
     */
    public void addUserAnnotation(Map<String, Object> annotations);
    
    /**
     * set the pixel data for the thumb-nail
     * @param image image file to be set as thumbnail on this record
     */
    public void setThumbnail(File imageFile);
    
    /**
     * Returns the thumbnail of the required dimension associated with this record
     * @return thumbnail as buffered image
     */
    public BufferedImage getThumbnail();
    
    /**
     * Returns the image handler associated with the specified coordinates 
     * Note that slice (and frame) numbers are 1 based numbering and it is illegal to 
     * provide a number which greater than what getSliceCount (getFrameCount) returns
     * @param specified dimension(t,z,c,site)
     * @return corresponding pixel data
     * @throws IOException 
     */
    public IPixelData getPixelData(Dimension d) throws IOException;
    
    /**
     * Returns the combined pixel data of the specified dimensions
     * @param sliceNo the slice number
     * @param frameNo the frame number
     * @param siteNo the site number
     * @param channelNos the channels to overlay
     * @return the overlaid image
     */
    public IPixelDataOverlay getOverlayedPixelData(int sliceNo, int frameNo, int siteNo, int[] channelNos) throws IOException;
    
    /**
     * Sets a custom contrast for all images of this record
     * @param channelNo specified channel no
     * @param contrast the custom contrast to set
     * @param zStacked true if this contrast is to be set on zStacked image; false otherwise
     */
    public void setCustomContrast(boolean zStacked, int channelNo, VisualContrast contrast);
    
    /**
     * sets the channel color
     * @param channelNo the relevant channel number
     * @param rgb the channel color to set
     */
    public void setChannelLUT(int channelNo, String rgb);
    
    /**
     * Returns the custom contrast, if any, for all images of this record
     * @param zStacked true if contrast of Z Stacked image is desired
     * @param channelNo specified channel
     * @return the custom contrast, if any, for all images of this record, otherwise null 
     */
    public VisualContrast getCustomContrast(boolean zStacked, int channelNo);
    
    /**
     * Returns the list of visual annotations associated with this dimension if any
     * @param specified overlay dimension
     * @return the visual annotations associated with this image if any or null
     */
    public Collection<IVisualOverlay> getVisualOverlays(VODimension dimension);
    
    /**
     * List of names visual annotations for this site
     * @param siteNo the site under consideration
     * @return  List of names visual annotations for this site (across all frames and slices)
     */
    public Set<String> getAvailableVisualOverlays(int siteNo);
    
    /**
     * Creates names (and empty) visual annotations associated with all frames & all slices for the specified site
     * @param siteNo the site under consideration
     * @param name name of the visual overlay
     */
    public void createVisualOverlays(int siteNo, String name);
    
    /**
     * Deletes the specified named visual-overlays associated with all frames & all slices for the specified site
     * @param siteNo the site under consideration
     * @param name name of the visual overlay
     */
    public void deleteVisualOverlays(int siteNo, String name);
    
    /**
     * Add the specified visual objects to the named visual-overlays on the specified dimensions
     * @param vObjects the list of visual objects
     * @param name the name of the overlay
     * @param imageCoordinates the coordinates under consideration
     */
    public void addVisualObjects(List<VisualObject> vObjects, String name, VODimension ... imageCoordinates);
    
    /**
     * Deletes the specified visual objects to the named visual-overlays on the specified dimensions
     * @param vObjects the list of visual objects
     * @param name the name of the overlay
     * @param imageCoordinates the coordinates under consideration
     */
    public void deleteVisualObjects(List<VisualObject> vObjects, String name, VODimension ... imageCoordinates);
    
    /**
     * Return the specified named visual overlay
     * @param id dimension of visual overlay
     * @param name of visual overlay
     * @return the named visual annotation associated with this image if any or null
     */
    public IVisualOverlay getVisualOverlay(VODimension id, String name);

    /**
     * Returns the existing comments on this record
     * @return list of comments if exists, null otherwise
     */
	public List<UserComment> getUserComments();

	/**
	 * Adds a comment to this record
	 * @param comment the comment to add
	 */
	public void addUserComments(String comment);
	
	/**
	 * fetches the overlaid on all channels (and scaled) images for all slices from the specified frame and site
	 * @param frameNo the frame number under consideration
	 * @param siteNo the site number under consideration
	 * @param imageWidth the required image height (for scaling purpose)
	 * @param useChannelColor checks whether to use channel colors
	 * @return list of image all the site in order
	 */
	public List<BufferedImage> getChannelOverlayImagesForSlices(int frameNo, int siteNo, int imageWidth, 
			boolean useChannelColor) throws IOException;
}
