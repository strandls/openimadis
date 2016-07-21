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

package com.strandgenomics.imaging.iserver.services.impl.web;

/**
 * Constants for the requests. Keys to be used while making requests.
 * 
 * @author santhosh
 * 
 */
public class RequestConstants {

    /**
     * Max number of records to fetch on any request
     */
    public static final int MAX_RECORDS = 1000;

    /**
     * Project name key
     */
    public static final String PROJECT_NAME_KEY = "projectName";
    
    /**
     * Movie name key
     */
    public static final String MOVIE_NAME_KEY = "movieName";
        

    /**
     * Frame number key
     */
    public static final String FRAME_NUMBER_KEY = "frameNumber";

    /**
     * Frame numbers key
     */
    public static final String FRAME_NUMBERS_KEY = "frameNumbers";

    /**
     * Slice number key
     */
    public static final String SLICE_NUMBER_KEY = "sliceNumber";

    /**
     * Slice numbers key
     */
    public static final String SLICE_NUMBERS_KEY = "sliceNumbers";

    /**
     * Channel numbers key
     */
    public static final String CHANNEL_NUMBERS_KEY = "channelNumbers";
    
    /**
     * Channel details key
     */
    public static final String CHANNEL_DETAILS_KEY = "channelDetails";

    /**
     * Record id key
     */
    public static final String RECORD_ID_KEY = "recordid";

    /**
     * Site number key
     */
    public static final String SITE_NUMBER_KEY = "siteNumber";

    /**
     * Attachment name key
     */
    public static final String ATTACHMENT_NAME_KEY = "attachmentName";

    /**
     * Project ID key
     */
    public static final String PROJECT_ID_KEY = "projectID";

    /**
     * Field name key
     */
    public static final String FIELD_NAME_KEY = "fieldName";

    /**
     * Field type key
     */
    public static final String FIELD_TYPE_KEY = "fieldType";

    /**
     * Conditions key
     */
    public static final String CONDITIONS_KEY = "conditions";

    /**
     * Min key
     */
    public static final String MIN_KEY = "min";

    /**
     * Max key
     */
    public static final String MAX_KEY = "max";

    /**
     * Notes key
     */
    public static final String NOTES_KEY = "notes";

    /**
     * attachment key
     */
    public static final String ATTACHMENT_FILE_KEY = "attachmentFile";

    /**
     * Project names key
     */
    public static final String PROJECT_NAMES_KEY = "projectNames";

    /**
     * Key for name of the overlay
     */
    public static final String OVERLAY_NAME_KEY = "overlay";

    /**
     * Visual objects key
     */
    public static final String VISUAL_OBJECTS_KEY = "visualObjects";

    /**
     * Key for a search query
     */
    public static final String SEARCH_KEY = "q";

    /**
     * Key for a list of records ids
     */
    public static final String RECORD_IDS_KEY = "recordids";

    /**
     * Channel number key
     */
    public static final String CHANNEL_NUMBER_KEY = "channelNumber";

    /**
     * Colour key
     */
    public static final String COLOUR_KEY = "colour";

    /**
     * Is grey scale or not
     */
    public static final String IS_GREY_SCALE = "isGreyScale";

    /**
     * Is Z-stacked or not
     */
    public static final String IS_Z_STACKED = "isZStacked";

    /**
     * Is Mosaic or not
     */
    public static final String IS_MOSAIC = "isMosaic";

    /**
     * Height key
     */
    public static final String HEIGHT_KEY = "height";

    /**
     * Width key
     */
    public static final String WIDTH_KEY = "width";

    /**
     * LUT name key
     */
    public static final String LUT_KEY = "lut";

    /**
     * Channel name key
     */
    public static final String CHANNEL_NAME_KEY = "channelName";

    /**
     * X tile key
     */
    public static final String XTILE_KEY = "x";

    /**
     * Y tile key
     */
    public static final String YTILE_KEY = "y";

    /**
     * Zoom level key
     */
    public static final String ZOOM_KEY = "zoom";

    /**
     * Key for names of overlays
     */
    public static final String OVERLAY_NAMES_KEY = "overlays";
    
    /**
     * Key for names of tile_window_x
     */
    public static final String WINDOW_X_KEY = "window_x";
    
    /**
     * Key for names of tile_window_y
     */
    public static final String WINDOW_Y_KEY = "window_y";
    
    /**
     * Key for names of tile_window_width
     */
    public static final String WINDOW_WIDTH_KEY = "window_width";

    /**
     * Key for names of tile_window_height
     */
    public static final String WINDOW_HEIGHT_KEY = "window_height";
    
    /**
     * key for GUID 
     */
    public static final String GUID_KEY = "guid";
    
    /**
     * key for TaskId 
     */
    public static final String TASKID_KEY = "taskId";
    /**
     * task input guids
     */
    public static final String TASK_INPUT_GUIDS = "task_input_guids";
    /**
     * task output guids
     */
    public static final String TASK_OUTPUT_GUIDS = "task_output_guids";
    
    /**
     * key for whether the movie is on frame or slice . 
     */
    public static final String ONFRAME_KEY ="frame";
    /**
     * key for whether to use channel colors while playing movie
     */
     public static final String USE_CHANNEL_COLOR="useChannelColor";
     /**
      * Key for ith Image 
      */
     public static final String IMAGE_NO ="ithImage";
     
     /**
      * Thread sleep time while requesting the next Image
      */
     public static final long  TIME_OUT = 2000;
     /**
      *  BookMark path
      */
     public static final String PATH="path";
     /**
      * folder name 
      */
     public static final String FOLDER_NAME="folderName";
     /**
      * key for Bookmark Name
      */
     public static final String BOOKMARK_NAME="bookmarkName";
     /**
      * key for absolute path for bookmark folder from root
      */
     public static final String BOOKMARK_PATH = "bookmarkPath";
     /**
      * key for bookmark is key or not
      */
     public static final String BOOKMARK_IS_LEAF = "isLeaf";
     /**
      * key for history type
      */
     public static final String HISTORY_TYPE_KEY = "historytype";

     public static final String HISTORY_FROM_DATE = "fromDate";
     
     public static final String HISTORY_TO_DATE = "toDate";

	public static final String SCALEBAR = "scalebar";
	
	/**
	 * key for library used to draw shapes on canvas
	 */
	public static final String DRAW_TYPE="drawType";
	/**
	 * key for default library used to draw shapes on canvas
	 */
	public static final String DEFAULT_DRAW_TYPE="kinetic";
}   

