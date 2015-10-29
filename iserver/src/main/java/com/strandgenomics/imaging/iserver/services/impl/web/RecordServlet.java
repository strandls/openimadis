/*
 * RecordServlet.java
 *
 * Product:  faNGS
 * Next Generation Sequencing
 *
 * Copyright 2007-2012, Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal,
 * Bangalore 560024
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */
package com.strandgenomics.imaging.iserver.services.impl.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.UserComment;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.HistoryObject;
import com.strandgenomics.imaging.iengine.models.HistoryType;
import com.strandgenomics.imaging.iengine.models.LengthUnit;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;
import com.strandgenomics.imaging.iengine.system.HistoryManager;
import com.strandgenomics.imaging.iengine.system.ImageManager;
import com.strandgenomics.imaging.iengine.system.RecordManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.ThumbnailManager;
import com.strandgenomics.imaging.iengine.system.UserManager;
import com.strandgenomics.imaging.iengine.system.UserPreference;
import com.strandgenomics.imaging.iserver.services.impl.web.util.VisualObjectTransformer;
import com.strandgenomics.imaging.iserver.services.impl.web.util.VisualObjectsFactory;

/**
 * Servlet for Record related queries
 * 
 * @author santhosh
 */
public class RecordServlet extends ApplicationServlet {

    /**
     * Key for max intensity
     */
    private static final String MAX_INTENSITY_KEY = "maxIntensity";

    /**
     * Key for min intensity
     */
    private static final String MIN_INTENSITY_KEY = "minIntensity";

    /**
     * key for comment parameter
     */
    private static final String COMMENT_KEY = "comment";

    /**
     * 
     */
    private static final long serialVersionUID = 3140501875669219935L;

    /**
     * key for gamma
     */
    private static final String GAMMA_KEY = "gamma";

    /**
     * Date formatter
     */
    private static DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    /**
     * Create new record servlet
     */
    public RecordServlet() {
    }
    
    /**
     * Get status of tile viewer tasks
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getTileStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        logger.logp(Level.INFO, "RecordServlet", "getTileStatus", " user name="+userName);
        
        RowSet<Object[]> status =  ImageSpaceDAOFactory.getDAOFactory().getTileDAO().getEstimatedTime();
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        int SECOND = 1000;
		int MINUTE = 60 * SECOND;
		int HOUR = 60 * MINUTE;
		int DAY = 24 * HOUR;
        if(status != null)
        {
        	for(Object[] row: status.getRows())
        	{
        		Map<String, Object> nameValue = new HashMap<String, Object>();
        		long ms = (long) Double.parseDouble(row[1].toString());
        		//System.out.println("text:"+timeVal);
        		nameValue.put("recordId", row[0].toString());
        		if(ms == 100){
        			nameValue.put("estimatedTime", "Calculating..");
        		}else{
        			StringBuffer timeVal = new StringBuffer("");
            		if (ms > DAY) {
            			timeVal.append(ms / DAY).append(" D ");
            			ms %= DAY;
            		}
            		if (ms > HOUR) {
            			timeVal.append(ms / HOUR).append(" H ");
            			ms %= HOUR;
            		}
            		if (ms > MINUTE) {
            			timeVal.append(ms / MINUTE).append(" M ");
            			ms %= MINUTE;
            		}
            		if (ms > SECOND) {
            			timeVal.append(ms / SECOND).append(" Sec ");
            			ms %= SECOND;
            		}
            		timeVal.append(ms + " ms");
        			nameValue.put("estimatedTime", timeVal.toString());
        		}
        		nameValue.put("progress", (int) Math.round( (Double.parseDouble(row[2].toString()) / Double.parseDouble(row[1].toString()) ) * 100 )+"%");
        		nameValue.put("size", (int) Math.round(( Double.parseDouble(row[3].toString()) )));
        		
        		logger.logp(Level.INFO, "RecordServlet", "getHistoryTypes", " recordId="+row[0].toString()+" estimatedTime="+row[1].toString()
        				+" progress="+row[2].toString()+" size="+row[3].toString());
        		
        		ret.add(nameValue);
        	}
        }
        writeJSON(resp, ret);
    }
    
    /**
     * Get all user comments for a particular record
     * 
     * @param req
     * 				recordId : specified record
     * @param resp
     * 				name : user who added the comment
     * 				date : the timestamp of the comment
     * 				comment : the actual comment
     * @throws ServletException
     * @throws IOException
     */
    public void getComments(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        UserManager userManager = SysManagerFactory.getUserManager();
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        
        int count = 0;
        List<UserComment> userComments = recordManager.getUserComments(userName, recordID);
		if (userComments != null && userComments.size() > 0)
		{
			int start = 0;
	        int end = userComments.size();
	        
	        try
			{
	        	String startString = getOptionalParam("start", req);
				start = Integer.parseInt(startString);
			}
			catch (Exception e)
			{
				start = 0;
			}
	        
	        try
			{
				String limitString = getOptionalParam("limit", req);
				int limit = Integer.parseInt(limitString);
				
				if(start+limit<end)
					end = start + limit;
			}
			catch (Exception e)
			{}
	     
	        count = userComments.size();
	        List<UserComment> filteredComments = userComments.subList(start, end);
			for (UserComment userComment : filteredComments)
			{
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("name", userManager.getUser(userComment.getUserLogin()).getName());
				item.put("date", dateFormat.format(userComment.getCreationDate()));
				item.put("comment", userComment.getNotes());
				item.put("commentid", userComment.getCommentId());
				ret.add(item);
			}
		}
        
		writeJSON(resp, Util.createMap("items",ret, "total", count));
    }
    
    /**
     * Get history types
     * 
     * @param req
     * @param resp
     * 				see {@link com.strandgenomics.imaging.iengine.models.HistoryType}
     * @throws ServletException
     * @throws IOException
     */
    public void getHistoryTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        logger.logp(Level.INFO, "RecordServlet", "getHistoryTypes", " user name="+userName);
        
        HistoryManager historyManager = SysManagerFactory.getHistoryManager();
        HistoryType[] types = historyManager.getHistoryTypes();
        
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if(types!=null && types.length>0)
        {
        	for(HistoryType type:types)
        	{
        		Map<String, Object> nameValue = new HashMap<String, Object>();
        		nameValue.put("name", type.toString());
        		nameValue.put("value", type.name());
        		
        		logger.logp(Level.INFO, "RecordServlet", "getHistoryTypes", " name="+type.toString()+" value="+type.name());
        		
        		ret.add(nameValue);
        	}
        }
        writeJSON(resp, ret);
    }
    
    /**
     * Get history for a particular record
     * 
     * @param req
     * 				recordId : specified record id
     * 				fromDate : filter from date, optional
     * 				toDate : filter to date, optional
     * 				historytype : type of the history
     * @param resp
     * 				user : the user who did the operation
     * 				type : the type of history
     * 				date : history timestamp
     * 				descrition : description of the history
     * @throws ServletException
     * @throws IOException
     */
    public void getHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        logger.logp(Level.INFO, "RecordServlet", "getHistory", " getHistoryRequest for user="+userName);
        
        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));

        HistoryManager historyManager = SysManagerFactory.getHistoryManager();
        UserManager userManager = SysManagerFactory.getUserManager();
        
        String targetUser = null;
		HistoryType type = null;
		Date fromDate = null;
		Date toDate = null;
		
		String fromString = req.getParameter(RequestConstants.HISTORY_FROM_DATE);
		try
		{
			fromDate = new Date(Long.parseLong(fromString));
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "RecordServlet", "getHistory", "error parsing fromDate="+ fromString);
		}

		String toString = req.getParameter(RequestConstants.HISTORY_TO_DATE);
		logger.logp(Level.FINE, "RecordServlet", "getHistory", "toDate="+ toString);
		try
		{
			toDate = new Date(Long.parseLong(toString));
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "RecordServlet", "getHistory", "error parsing toDate="+ toString);
		}

		String strType = req.getParameter(RequestConstants.HISTORY_TYPE_KEY);
		try
		{
			type = HistoryType.valueOf(strType);
		}
		catch(Exception e)
		{
			logger.logp(Level.WARNING, "RecordServlet", "getHistory", "error parsing strType="+ strType);
		}
		
		logger.logp(Level.INFO, "RecordServlet", "getHistory", " getHistoryRequest for user="+userName+" guid="+recordID+" type="+type+" target-user="+targetUser+" fromDate="+fromDate+" toDate="+toDate);
		
		List<HistoryObject> recordHistory = historyManager.getRecordHistory(userName, recordID, targetUser, type, fromDate, toDate);
		
		logger.logp(Level.FINEST, "RecordServlet", "getHistory", " got history "+recordHistory);
		
		int count = 0;
		List<Map<String, Object>> historys = new ArrayList<Map<String, Object>>();
		if (recordHistory != null && recordHistory.size() > 0)
		{
			count = recordHistory.size();
			
			int start = 0;
			int end = recordHistory.size();
			
			String startString = getOptionalParam("start", req);
			try
			{
				start = Integer.parseInt(startString);
			}
			catch (Exception e)
			{
				start = 0;
			}
			
			String limitString = getOptionalParam("limit", req); 
			try
			{
				int limit = Integer.parseInt(limitString);
				
				end = start+limit;
				if(end>=recordHistory.size())
					end = recordHistory.size();
			}
			catch (Exception e)
			{}
			
			List<HistoryObject> paginatedHistory = recordHistory.subList(start, end);
			for (HistoryObject historyItem : paginatedHistory)
			{
				logger.logp(Level.FINEST, "RecordServlet", "getHistory", " got history "+historyItem.getDescription());
				
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("user", userManager.getUser(historyItem.getModifiedBy()).getName());
				item.put("type", historyItem.getType().toString());
				item.put("date", dateFormat.format(historyItem.getModificationTime()));
				item.put("description", historyItem.getDescription());
				historys.add(item);
			}
		}
		
		writeJSON(resp, Util.createMap("items",historys, "total", count));
    }

    /**
     * Add a new comment for a particular record
     * 
     * @param req
     * 				recordid : identifier of the record
     * 				comment : actual comment to be added
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getMethod().equals("POST")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not post request");
            return;
        }

        String userName = getCurrentUser(req);

        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));

        if (req.getParameter(COMMENT_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String comment = req.getParameter(COMMENT_KEY);

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        recordManager.addUserComment(getWebApplication(), userName, null, recordID, comment);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }

    /**
     * Get record data
     * 
     * @param req
     * 				recordid : specified record
     * @param resp
     * 				gson representation of {@link com.strandgenomics.imaging.iengine.models.Record}
     * @throws ServletException
     * @throws IOException
     */
    public void getRecordData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        Record record = recordManager.findRecord(userName, recordID);
        if (record == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "record not found");
            return;
        }
        writeJSON(resp, getRecordMap(record, userName));
    }

    /**
     * Get all visual overlays available for a given record
     * 
     * @param req
     * 				recordid : specified record
     * 				frameNumber : selected frame
     * 				sliceNumber : selected slice
     * 				siteNumber : selected site
     * @param resp
     * 				gson representation of {@link com.strandgenomics.imaging.iengine.models.VisualOverlay}
     * @throws ServletException
     * @throws IOException
     */
    public void getVisualOverlays(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));

        logger.logp(Level.INFO, "RecordServlet", "getVisualOverlays", "recordid " + recordID + " frame: " + frameNumber
                + " slice: " + sliceNumber + " site: " + siteNumber);

        VODimension dimension = new VODimension(frameNumber, sliceNumber, siteNumber);
        RecordManager recordManager = SysManagerFactory.getRecordManager();
        List<VisualOverlay> ret = new ArrayList<VisualOverlay>();
        List<VisualOverlay> visualOverlays = recordManager.getVisualOverlays(userName, recordID, dimension);
        if (visualOverlays != null)
            ret.addAll(visualOverlays);
        writeJSON(resp, ret);
    }

    /**
     * Get all visual objects which form the overlay with the given name
     * 
     * @param req
     * 				recordid : specified record
     * 				frameNumber : frame
     * 				sliceNumber : slice
     * 				siteNumber : site
     * 				overlayName : name of the overlay
     * @param resp
     * 				visual objects are raphael objects
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void getVisualObjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        String overlayName = getRequiredParam(RequestConstants.OVERLAY_NAME_KEY, req);
        String transformerType = getOptionalParam(RequestConstants.DRAW_TYPE, req);
        
        if(transformerType == null){
        	transformerType = RequestConstants.DEFAULT_DRAW_TYPE;
        }

        logger.logp(Level.INFO, "RecordServlet", "getVisualOverlays", "recordid " + recordID + " frame: " + frameNumber
                + " slice: " + sliceNumber + " site: " + siteNumber + " overlay: " + overlayName);

        VODimension dimension = new VODimension(frameNumber, sliceNumber, siteNumber);

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        List<VisualObject> visualObjects = recordManager.getVisualObjects(userName, recordID, dimension, overlayName);
        
        if (visualObjects != null) {
        	VisualObjectTransformer instance = VisualObjectsFactory.getVisualObjectTransformer(transformerType);
            for (VisualObject vo : visualObjects){
                ret.add((Map<String, Object>) instance.encode(vo));
            }
        }
        
        writeJSON(resp, ret);
    }
    
    /**
     * gives locations where visual objects for the overlay are present
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getVisualOverlayLocations(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String userName = getCurrentUser(req);
    	int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
    	long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
    	String overlayName = getRequiredParam(RequestConstants.OVERLAY_NAME_KEY, req);
    	
    	RecordManager recordManager = SysManagerFactory.getRecordManager();
    	List<VODimension> locations=recordManager.findOverlayLocation(userName, recordID, siteNumber, overlayName);
    	
    	if(locations!=null){
    		HashSet<VODimension> hs = new HashSet<VODimension>();
    		hs.addAll(locations);
    		locations.clear();
    		locations.addAll(hs);
    	}
    	
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
    	
    	if(locations!=null){
    		for(VODimension vo: locations){
    			Map<String, Object> value = new HashMap<String, Object>();
    			value.put("frameNo", vo.frameNo+1);
    			value.put("sliceNo", vo.sliceNo+1);
    			ret.add(value);
    		}
    	}
    	
    	writeJSON(resp, ret);
    }
    
    /**
     * Delete the chosen overlay.
     * 
     * @param req
     * 				recordId : specified record
     * 				siteNumber : selected site
     * 				overlayName : name of the overlay to be deleted
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteOverlay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        String overlayName = getRequiredParam(RequestConstants.OVERLAY_NAME_KEY, req);

        logger.logp(Level.INFO, "RecordServlet", "deleteOverlay", "recordid " + recordID + " overlay: " + overlayName);

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        recordManager.deleteVisualOverlays(getWebApplication(), userName, null, recordID, siteNumber, overlayName);

        writeJSON(resp, successResponse);
    }

    /**
     * Add new overlay with the given name
     * 
     * @param req
     * 				recordId : specified record
     * 				siteNumber : specified site
     * 				overlayName : name of the overlay
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addOverlay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        String overlayName = getRequiredParam(RequestConstants.OVERLAY_NAME_KEY, req);
        logger.logp(Level.INFO, "RecordServlet", "addOverlay", "recordid " + recordID + " site: " + siteNumber
                + " overlay: " + overlayName);

        RecordManager manager = SysManagerFactory.getRecordManager();
        List<String> visualOverlays = manager.getAvailableVisualOverlays(userName, recordID, siteNumber);
        if (visualOverlays != null) {
            if (visualOverlays.indexOf(overlayName) != -1) {
                // Overlay name already used. send error message
                writeFailure(resp, "Overlay name exists. Choose a different name");
                return;
            }
        }

        manager.createVisualOverlays(getWebApplication(), userName, null, recordID, siteNumber, overlayName);

        writeJSON(resp, successResponse);
    }

    /**
     * Save the overlay with the given name and visual objects to the record
     * with given id
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void saveOverlay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        String frameNumbersString = getRequiredParam(RequestConstants.FRAME_NUMBERS_KEY, req);
        String sliceNumbersString = getRequiredParam(RequestConstants.SLICE_NUMBERS_KEY, req);
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        String overlayName = getRequiredParam(RequestConstants.OVERLAY_NAME_KEY, req);
        String visualObjectsString = getRequiredParam(RequestConstants.VISUAL_OBJECTS_KEY, req);
        String transformerType = getOptionalParam(RequestConstants.DRAW_TYPE, req);
        
        if(transformerType == null){
        	transformerType = RequestConstants.DEFAULT_DRAW_TYPE;
        }

        List<Integer> frameNumbers = gson.fromJson(frameNumbersString, (new TypeToken<List<Integer>>() {
        }).getType());
        List<Integer> sliceNumbers = gson.fromJson(sliceNumbersString, (new TypeToken<List<Integer>>() {
        }).getType());
        logger.logp(Level.INFO, "RecordServlet", "saveOverlay", "recordid " + recordID + " frame: " + frameNumbers
                + " slice: " + sliceNumbers + " site: " + siteNumber + " overlay: " + overlayName);

        logger.logp(Level.INFO, "RecordServlet", "saveOverlay", "visual objects: " + visualObjectsString);

        List<VODimension> dimensions = new ArrayList<VODimension>();
        for (int i = 0; i < frameNumbers.size(); ++i)
            for (int j = 0; j < sliceNumbers.size(); ++j)
                dimensions.add(new VODimension(frameNumbers.get(i), sliceNumbers.get(j), siteNumber));

        List<VisualObject> visualObjects = new ArrayList<VisualObject>();

        List<Map<String, Object>> overlays = new ObjectMapper().readValue(visualObjectsString, List.class);

        if (overlays != null) {
        	VisualObjectTransformer transformer = VisualObjectsFactory.getVisualObjectTransformer(transformerType);
            for (Map<String, Object> overlay : overlays)
            {
            	logger.logp(Level.INFO, "RecordServlet", "saveOverlay", "visual objects: " + transformer.decode(overlay));
                visualObjects.add(transformer.decode(overlay));
            }
        }
        
        RecordManager manager = SysManagerFactory.getRecordManager();
        VisualOverlay visualOverlay = manager.getVisualOverlay(userName, recordID, dimensions.get(0), overlayName);
        if (visualOverlay == null) {
            manager.createVisualOverlays(getWebApplication(), userName, null, recordID, siteNumber, overlayName);
        }
        manager.setVisualObjects(getWebApplication(), userName, null, recordID, visualObjects, overlayName, dimensions.toArray(new VODimension[0]));
        writeJSON(resp, successResponse);
    }

    /**
     * Get contrast histogram data for the given record and dimension.
     * 
     * @param req
     * 				recordid : record identifier
     * 				frame : selected frame
     * 				slice : selected slice
     * 				site : selected site
     * 				channel : selected channel
     * 				isZStacked : if the image is z stacked
     * @param resp
     * 				imageSrc : the histogram image
     * 				currentMin : value of min
     * 				currentMax : value of max
     * 				currentGamme : value of gamma
     * 				pixelDepth : pixel depth of record 
     * @throws ServletException
     * @throws IOException
     */
    public void getContrastHistogram(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        int channelNumber = Integer.parseInt(getRequiredParam(RequestConstants.CHANNEL_NUMBER_KEY, req));
        boolean zStackOn = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));
        Dimension dimension = new Dimension(frameNumber, sliceNumber, channelNumber, siteNumber);

        logger.logp(Level.INFO, "RecordServlet", "getContrastHistogram", "get contrast image: recordid " + recordID
                + " frame: " + frameNumber + " slice: " + sliceNumber + " site: " + siteNumber + " channel: "
                + channelNumber);

        ImageManager manager = SysManagerFactory.getImageManager();
        Histogram intensity = manager.getIntensityDistibution(userName, recordID, dimension, zStackOn, null);
        UserPreference pref = SysManagerFactory.getUserPreference();
        VisualContrast channelContrast = pref.getChannelContrast(userName, recordID, dimension, zStackOn);

        Map<String, Object> intensityMap = transformIntensity(intensity);
        intensityMap.put("imageSrc", "../record/getContrastImage?recordid=" + recordID + "&frameNumber=" + frameNumber
                + "&sliceNumber=" + sliceNumber + "&siteNumber=" + siteNumber + "&channelNumber=" + channelNumber
                + "&isZStacked=" + zStackOn);
        intensityMap.put("currentMin", channelContrast.getMinIntensity());
        intensityMap.put("currentMax", channelContrast.getMaxIntensity());
        intensityMap.put("currentGamma", channelContrast.getGamma());
        intensityMap.put("pixelDepth", intensity.pixelDepth.getByteSize());

        writeJSON(resp, intensityMap);
    }

    /**
     * Get contrast settings data for the given record and dimension.
     * 
     * @param req
     * 				recordId : specified record
     * 				isZStacked : is image z stacked
     * @param resp
     * 				min : value of min intensity
     * 				max : value of max intensity
     * 				gamma : value of gamma
     * @throws ServletException
     * @throws IOException
     */
    public void getContrastSettings(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        boolean zStackOn = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));
        
        logger.logp(Level.INFO, "RecordServlet", "getContrastSettings", "recordid " + recordID);
        
        List<Map<String,String>> contrastSettingList = new ArrayList<Map<String,String>>();
        
        RecordManager recordManager=SysManagerFactory.getRecordManager();
        UserPreference pref = SysManagerFactory.getUserPreference();

        for(int channelNo=0; channelNo<recordManager.findRecord(userName,recordID).numberOfChannels; channelNo++){
        	VisualContrast channelContrast = pref.getChannelContrast(userName, recordID, channelNo, zStackOn);
        	Map<String,String> contrastSettings= new HashMap<String, String>();
    		contrastSettings.put("channelNumber", String.valueOf(channelNo));
        	if(channelContrast != null) {
            	contrastSettings.put("min", String.valueOf(channelContrast.getMinIntensity()));
                contrastSettings.put("max", String.valueOf(channelContrast.getMaxIntensity()));
                contrastSettings.put("gamma", String.valueOf(channelContrast.getGamma()));
            }
        	contrastSettingList.add(contrastSettings);
        }
        
        writeJSON(resp, contrastSettingList);        
    }
    
    /**
     * Get contrast histogram image.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getContrastImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        int channelNumber = Integer.parseInt(getRequiredParam(RequestConstants.CHANNEL_NUMBER_KEY, req));
        boolean zStackOn = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));
        Dimension dimension = new Dimension(frameNumber, sliceNumber, channelNumber, siteNumber);

        Integer minIntensity = req.getParameter(MIN_INTENSITY_KEY) == null ? null : Integer.parseInt(req
                .getParameter(MIN_INTENSITY_KEY));
        Integer maxIntensity = req.getParameter(MAX_INTENSITY_KEY) == null ? null : Integer.parseInt(req
                .getParameter(MAX_INTENSITY_KEY));

        logger.logp(Level.INFO, "RecordServlet", "getContrastImage", "get contrast image: recordid " + recordID
                + " frame: " + frameNumber + " slice: " + sliceNumber + " site: " + siteNumber + " channel: "
                + channelNumber);

        logger.logp(Level.INFO, "RecordServlet", "getContrastImage", "get contrast image: minIntensity " + minIntensity
                + " maxIntensity: " + maxIntensity);

        ImageManager manager = SysManagerFactory.getImageManager();
        Histogram intensity = manager.getIntensityDistibution(userName, recordID, dimension, zStackOn, null);
        ServletOutputStream outputStream = resp.getOutputStream();
        try{
        	ImageIO.write(intensity.createImage(new java.awt.Dimension(200, 200), minIntensity, maxIntensity, Color.WHITE,
                    Color.RED, 2), "PNG", outputStream);
        }
        catch (IOException e) {
			outputStream.close();
		}
        outputStream.close();
    }

    /**
     * Set thumbnail for the record based on image generated using the dimension
     * provided.
     * 
     * @param req
     * 				recordId : specified record
     * 				frameNumber : selected frame
     * 				sliceNumber : selected slice
     * 				siteNumber : selected site
     * 				channelNumbers : selected channels
     * 				isGrayScale : is gray scale
     * 				isZStacked : is Z Stacked
     * 				isMosaic : is channel mosaic
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void setThumbnail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));

        String channelNumbersString = getRequiredParam(RequestConstants.CHANNEL_NUMBERS_KEY, req);
        List<Integer> channels = gson.fromJson(channelNumbersString, (new TypeToken<List<Integer>>() {
        }).getType());

        boolean useChannelColor = !Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_GREY_SCALE, req));
        boolean isZStacked = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));
        boolean isMosaic = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_MOSAIC, req));

        logger.logp(Level.INFO, "RecordServlet", "setThumbnail", "setThumbnail: recodid: " + recordID + " frame: "
                + frameNumber + " slice: " + sliceNumber + " site: " + siteNumber + " channels: " + channels
                + " greyscale: " + useChannelColor + " zstack: " + isZStacked + " mosaic: " + isMosaic);

        // StorageManager sm = SysManagerFactory.getStorageManager();
        // sm.setThumbnail(userName, recordID, frameNumber, sliceNumber,
        // siteNumber, channels, useChannelColor,
        // isZStacked, isMosaic);
        ThumbnailManager tm = SysManagerFactory.getThumbnailManager();
        tm.setThumbnail(userName, recordID, frameNumber, sliceNumber, siteNumber, channels, useChannelColor,
                isZStacked, isMosaic);

        writeJSON(resp, successResponse);
    }

    /**
     * List all the sites for the given record. This list also includes a
     * thumbnail for every site.
     * 
     * @param req
     * 				
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getSiteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));

        RecordManager rm = SysManagerFactory.getRecordManager();
        Record record = rm.findRecord(userName, recordID);
        List<Site> sites = record.getSites();

        List<Map<String, Object>> sitesList = new ArrayList<Map<String, Object>>();
        for (int siteNo = 0; siteNo < sites.size(); ++siteNo) {
            Site site = sites.get(siteNo);
            Map<String, Object> nextSite = new HashMap<String, Object>();
            nextSite.put("recordid", recordID);
            nextSite.put("name", site.getName());
            nextSite.put("siteNumber", siteNo);
            nextSite.put("sliceCount", record.numberOfSlices);
            nextSite.put("frameCount", record.numberOfFrames);
            String thumbnailURL = "../project/getDefaultImage?recordid=" + recordID + "&height=120&siteNumber="
                    + siteNo;
            nextSite.put("thumbnail", thumbnailURL);
            sitesList.add(nextSite);
        }
        writeJSON(resp, sitesList);
    }

    /**
     * Save contrast settings based on the values given.
     * 
     * @param req
     * 				recordId : specified record
     * 				channelNumber : selected channel
     * 				minIntensity : value of min intensity
     * 				maxIntensity : value of max intensity
     * 				gamma : value of gamma
     * 				isZStacked : is Z Stacked
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void saveContrastSettings(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int channelNumber = Integer.parseInt(getRequiredParam(RequestConstants.CHANNEL_NUMBER_KEY, req));
        int minIntensity = Integer.parseInt(getRequiredParam(MIN_INTENSITY_KEY, req));
        int maxIntensity = Integer.parseInt(getRequiredParam(MAX_INTENSITY_KEY, req));
        double gamma = Double.parseDouble(getRequiredParam(GAMMA_KEY, req));
        boolean zStackOn = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));

        UserPreference pref = SysManagerFactory.getUserPreference();
        pref.setCustomContrast(userName, recordID, channelNumber, zStackOn, minIntensity, maxIntensity, gamma);

        writeJSON(resp, successResponse);
    }

    private Map<String, Object> transformIntensity(Histogram intensity) {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put(MAX_INTENSITY_KEY, intensity.getMax());
        ret.put(MIN_INTENSITY_KEY, intensity.getMin());
        ret.put("maxFrequency", intensity.getMaxFrequency());
        return ret;
    }

    /**
     * Set the chosen colour for the channel of the given record
     * 
     * @param req
     * 				recordId : selected record
     * 				channelNumber : selected channel
     * 				lut : selected LUT
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void setChannelLUT(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int channelNo = Integer.parseInt(getRequiredParam(RequestConstants.CHANNEL_NUMBER_KEY, req));
        String lut = getRequiredParam(RequestConstants.LUT_KEY, req);

        UserPreference preference = SysManagerFactory.getUserPreference();
        preference.setChannelLUT(userName, recordID, channelNo, lut);

        writeJSON(resp, successResponse);
    }

    /**
     * Set new name for a channel.
     * 
     * @param req
     * 				recordId : selected record
     * 				channelNumber : selected channel
     * 				channelName : selected name for channel
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void setChannelName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int channelNo = Integer.parseInt(getRequiredParam(RequestConstants.CHANNEL_NUMBER_KEY, req));
        String channelName = getRequiredParam(RequestConstants.CHANNEL_NAME_KEY, req);

        RecordManager manager = SysManagerFactory.getRecordManager();
        manager.setRecordChannelName(userName, recordID, channelNo, channelName);

        writeSuccess(resp);
    }

    /**
     * Get the channels for a given record
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getChannels(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        List<Map<String, Object>> channelMaps = getChannelMaps(recordID, userName);
        writeJSON(resp, channelMaps);
    }

    /**
     * Get the image associated with a given LUT
     * 
     * @param req
     * 				lut : specified LUT name
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getLUTImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lutName = getRequiredParam(RequestConstants.LUT_KEY, req);

        LutLoader loader = LutLoader.getInstance();
        BufferedImage lutImage = loader.getLUTImage(lutName);

        resp.setContentType("image/jpeg");

        BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
        try{
        	ImageIO.write(lutImage, "JPG", outputStream);
        }
        catch(IOException e){
        	outputStream.close();
        }
        outputStream.close();
    }

    /**
     * Get a list of LUTs available
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAvailableLUTs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        LutLoader loader = LutLoader.getInstance();
        List<String> availableLUTs = loader.getAvailableLUTs();
        for (String lut : availableLUTs) {
            Map<String, Object> next = new HashMap<String, Object>();
            next.put("name", lut);
            next.put("url", "../record/getLUTImage?lut=" + lut);
            ret.add(next);
        }
        writeJSON(resp, ret);
    }

    /**
     * TODO : how to do this neatly? Should not be done here, but where to do
     * the appropriate stringfy-ings (date instances)?
     * 
     * @param record
     * @return
     * @throws DataAccessException
     */
    private Map<String, Object> getRecordMap(Record record, String user) throws DataAccessException {
    	
    	LengthUnit lengthUnit = record.getLengthUnit();
    	
        Map<String, Object> recordMap = new LinkedHashMap<String, Object>();

        recordMap.put("Record ID", record.guid);
        recordMap.put("Machine IP", record.machineIP);
        recordMap.put("Machine MAC", record.macAddress);

        recordMap.put("Upload Time", dateFormat.format(new Date(record.uploadTime)));
        recordMap.put("Source Time", dateFormat.format(new Date(record.sourceTime)));
        recordMap.put("Creation Time", dateFormat.format(new Date(record.creationTime)));
        recordMap.put("Acquired Time", "");
        if(record.acquiredTime != null)
        	recordMap.put("Acquired Time", dateFormat.format(new Date(record.acquiredTime)));

        recordMap.put("Archive Signature", record.archiveSignature);
        recordMap.put("Slice Count", record.numberOfSlices);
        recordMap.put("Frame Count", record.numberOfFrames);
        recordMap.put("Channel Count", record.numberOfChannels);
        recordMap.put("Site Count", record.numberOfSites);

        recordMap.put("Image Width", record.imageWidth);
        recordMap.put("Image Height", record.imageHeight);
        recordMap.put("Image Depth", record.imageDepth.toString());
        
        DecimalFormat df = new DecimalFormat("#.####");
        
        recordMap.put("Pixel Size X", df.format(record.getXPixelSize())+" "+lengthUnit);
        recordMap.put("Pixel Size Y", df.format(record.getYPixelSize())+" "+lengthUnit);
        recordMap.put("Pixel Size Z", df.format(record.getZPixelSize())+" "+lengthUnit);

        User u = SysManagerFactory.getUserManager().getUser(record.uploadedBy);
        String uploadedBy = (u==null || u.getName()==null || u.getName().isEmpty()) ? record.uploadedBy : u.getName();
        recordMap.put("Uploaded By", uploadedBy);

        recordMap.put("Source Type", record.getSourceFormat().name);

        recordMap.put("Source File", record.sourceFilename);
        recordMap.put("Source Folder", record.sourceFolder);
        recordMap.put("Image Type", record.imageType.name());

        recordMap.put("Sites", record.sites);
        recordMap.put("Channels", getChannelMaps(record.guid, user));

        return recordMap;
    }

    /**
     * Channel colours are got from {@link UserPreference}. Merging the details
     * of color before sending to client.
     * 
     * @param channels
     * @return
     * @throws DataAccessException
     */
    private List<Map<String, Object>> getChannelMaps(long guid, String user) throws DataAccessException {
        UserPreference preference = SysManagerFactory.getUserPreference();
        List<Channel> channels = preference.getChannels(user, guid);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (int channelNo = 0; channelNo < channels.size(); ++channelNo) {
            Map<String, Object> next = new HashMap<String, Object>();
            next.put("channelNumber", channelNo);
            next.put("recordid", guid);
            next.put("name", channels.get(channelNo).getName());
            next.put("lut", channels.get(channelNo).getLut());
            next.put("wavelength", channels.get(channelNo).getWavelength());
            ret.add(next);
        }
        return ret;
    }

    /**
     * Get all the annotations for a particular record. Includes system
     * annotations and user annotations.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAllAnnotations(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String user = getCurrentUser(req);
        String recordIDString = getRequiredParam(RequestConstants.RECORD_ID_KEY, req);
        long recordID = Long.parseLong(recordIDString);

        logger.logp(Level.INFO, "AnnotationServlet", "getAllAnnotations", "Get all annotations for record: " + recordID);

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        List<MetaData> userAnnotations = recordManager.getUserAnnotations(user, recordID);
        Map<String, Object> userMap = new HashMap<String, Object>();
        if (userAnnotations != null) {
            for (MetaData meta : userAnnotations) {
                userMap.put(meta.getName(), meta.stringValue());
            }
        }
        Record record = recordManager.findRecord(user, recordID);
        Map<String, Object> systemMap = getRecordMap(record, user);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("user", userMap);
        ret.put("system", systemMap);
        writeJSON(resp, ret);
    }
    
    /**
     * delete the comments of a record
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteComments(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException
    {
    	String user = getCurrentUser(req);
    	String commentid = getRequiredParam("commentid", req);
    	String projectName = getRequiredParam("projectName", req); 
    	String recordIdStiring = getRequiredParam(RequestConstants.RECORD_ID_KEY, req); 
    	
    	long commentId=Long.parseLong(commentid);
    	long recordId=Long.parseLong(recordIdStiring);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
    	RecordManager recordManager = SysManagerFactory.getRecordManager();
    	
    	if(recordManager.deleteComment(getWebApplication(),recordId, commentId, projectName, user)){
    		ret.put("deleted", true);           
    	}
    	else{
    		ret.put("deleted", false);
    	}
    	
    	writeJSON(resp, ret);
    }

}
