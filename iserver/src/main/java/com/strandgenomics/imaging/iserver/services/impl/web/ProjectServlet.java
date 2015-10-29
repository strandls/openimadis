/*
 * ProjectServlet.java
 *
 * AVADIS Image Management System
 * Web Services
 *
 * Copyright 2007-2011, Strand Life Sciences
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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.bioformats.BioExperiment;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.util.ImageUtil;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.export.ExportFormat;
import com.strandgenomics.imaging.iengine.export.ExportRequest;
import com.strandgenomics.imaging.iengine.export.ExportStatus;
import com.strandgenomics.imaging.iengine.models.Attachment;
import com.strandgenomics.imaging.iengine.models.ImagePixelData;
import com.strandgenomics.imaging.iengine.models.Import;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.VisualOverlay;
import com.strandgenomics.imaging.iengine.movie.MovieTicket;
import com.strandgenomics.imaging.iengine.search.SearchCategories;
import com.strandgenomics.imaging.iengine.search.SearchResult;
import com.strandgenomics.imaging.iengine.system.AttachmentManager;
import com.strandgenomics.imaging.iengine.system.AuthorizationManager;
import com.strandgenomics.imaging.iengine.system.BookmarkManager;
import com.strandgenomics.imaging.iengine.system.ImageManager;
import com.strandgenomics.imaging.iengine.system.ImageReadersManager;
import com.strandgenomics.imaging.iengine.system.ProjectManager;
import com.strandgenomics.imaging.iengine.system.RecordManager;
import com.strandgenomics.imaging.iengine.system.SearchEngine;
import com.strandgenomics.imaging.iengine.system.SolrSearchManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.UserPreference;

/**
 * Servlet which serves project related queries
 * 
 * @author santhosh
 * 
 */
@SuppressWarnings("serial")
public class ProjectServlet extends ApplicationServlet {

    private static final String THUMBNAIL_BASE_URL = "../project/getThumbnail?recordid=";

    private static final int MAX_RECENT = 5;

    private static final int TILE_WIDTH = 128;

    private static final int TILE_HEIGHT = 128;

    /**
     * URL of the acq client
     */
    private static final String ACQ_CLIENT_URL = "../acquisition/acquisition.jsp";

    /**
     * URL of the acq client
     */
    private static final String THREED_CLIENT_URL = "../home/3d/index.html";

    /**
     * List all projects for the current logged in user
     * 
     * @param req
     * @param resp
     * 			items : gson representation of {@link com.strandgenomics.imaging.iengine.models.Project}
     * @throws ServletException
     * @throws IOException
     */
    public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        List<Project> projects = SysManagerFactory.getProjectManager().getProjects(userName, ProjectStatus.Active);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", projects);
        writeJSON(resp, ret);
    }

    /**
     * List the recent projects alone
     * 
     * @param req
     * @param resp
     * 			items : gson representation of {@link com.strandgenomics.imaging.iengine.models.Project}
     * 			thumbnails : urls for corresponding project thumbnails
     * @throws ServletException
     * @throws IOException
     */
    public void recentList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        logger.logp(Level.INFO, "ProjectServlet", "recentList", "userName: " + userName);

        ProjectManager projectManager = SysManagerFactory.getProjectManager();
        List<String> recentProjectNames = SysManagerFactory.getUserPreference().getRecentProjects(userName, MAX_RECENT);
        if (recentProjectNames == null)
            recentProjectNames = new ArrayList<String>();
        if (recentProjectNames.size() <= MAX_RECENT) {
            // If the required number of projects is not present in recent list,
            // fill up from the project listing
            List<Project> projects = projectManager.getProjects(userName, ProjectStatus.Active);
            if (projects != null) {
                int index = 0;
                while ((recentProjectNames.size() <= MAX_RECENT) && index < projects.size()) {
                    if (recentProjectNames.indexOf(projects.get(index).getName()) == -1)
                        recentProjectNames.add(projects.get(index).getName());
                    index += 1;
                }
            }
        }
        List<Project> recentProjects = new ArrayList<Project>();
        for (String projectName : recentProjectNames)
        {
        	Project project = null;
        	try
        	{
        		project = projectManager.getProject(userName, projectName);
        	}
        	catch(Exception e)
        	{
        		logger.logp(Level.WARNING, "ProjectServlet", "recentList", "error for userName: " + userName+" for project:"+projectName);
        	}
        	if(project!=null&&project.getStatus() == ProjectStatus.Active)
        		recentProjects.add(project);
        }
        List<String> thumbnailURLS = new ArrayList<String>();
        List<String> projectNames=new ArrayList<String>();
        List<String> records = new ArrayList<String>();
        
        for (Project project : recentProjects) {
            String projectThumbnailURL = getProjectThumbnailURL(userName, project.getName());
            thumbnailURLS.add(projectThumbnailURL);
            projectNames.add(project.getName());
            records.add(String.valueOf(getRepresentativeRecordForProject(userName, project.getName())));
        }
        
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("items", recentProjects);
        ret.put("thumbnails", thumbnailURLS);
        ret.put("records", records);
        ret.put("projectnames",projectNames);
        
        writeJSON(resp, ret);
    }

    /**
     * Get projects for the given project names
     * 
     * @param req
     * 			projectNames : gson list of project names
     * @param resp
     * 			gson representation of {@link com.strandgenomics.imaging.iengine.models.Project}
     * @throws ServletException
     * @throws IOException
     */
    public void getProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        logger.logp(Level.INFO, "ProjectServlet", "recentList", "userName: " + userName);
        String projectNamesString = getRequiredParam(RequestConstants.PROJECT_NAMES_KEY, req);
        List<String> projectNames = gson.fromJson(projectNamesString, new TypeToken<List<String>>() {
        }.getType());
        ProjectManager projectManager = SysManagerFactory.getProjectManager();
        List<Project> projects = new ArrayList<Project>();
        if (projectNames != null) {
            for (String projectName : projectNames) {
                Project project = projectManager.getProject(userName, projectName);
                if (project != null)
                    projects.add(project);
            }
        }
        writeJSON(resp, projects);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getProjectForRecord(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        logger.logp(Level.INFO, "ProjectServlet", "getProjectForRecord", "userName: " + userName);
        String recordidString = getRequiredParam("recordid", req);
        
        Long guid = null;
        try
        {
        	guid = Long.parseLong(recordidString);        	
        }
        catch(Exception e)
        {
        	writeFailure(resp, "invalid guid");
        	return;
        }

        if(!SysManagerFactory.getUserPermissionManager().canRead(userName, guid))
        {
        	writeFailure(resp, "not authorized to read record");
        	return;
        }
        		
        int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
        String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
        
        
        writeJSON(resp, Util.createMap("success", "true", "projectName", projectName));
    }

    /**
     * Called when a project is accessed. Uses one parameter, the name of the
     * project
     * 
     * @param req
     * 			projectName : name of the project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void projectAccessed(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        logger.logp(Level.INFO, "ProjectServlet", "projectAccessed", "projectName: " + projectName);

        SysManagerFactory.getUserPreference().markProjectUse(userName, projectName);
        writeJSON(resp, successResponse);
    }

    /**
     * Get thumbnail for a project. It is the thumbnail of the first record in
     * the project. If no record is available in the project, return a special
     * placeholder image.
     * 
     * @param userName
     * @param projectName
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private String getProjectThumbnailURL(String userName, String projectName) throws ServletException, IOException {
        int recordCount = SysManagerFactory.getProjectManager().getRecordCount(userName, projectName);
        if (recordCount > 0) {
            // There are records in this project. Get first record and use for
            // thumbnail
            Record record = SysManagerFactory.getRecordManager().getRecords(userName, projectName, 1).get(0);
            return getImageURL(record.guid, projectName);
        } else {
            return "";
        }
    }
    
    private long getRepresentativeRecordForProject(String userName, String projectName) throws ServletException, IOException {
    	int recordCount = SysManagerFactory.getProjectManager().getRecordCount(userName, projectName);
        if (recordCount > 0) {
            // There are records in this project. Get first record and use for
            // thumbnail
            Record record = SysManagerFactory.getRecordManager().getRecords(userName, projectName, 1).get(0);
            return record.guid;
        } else {
            return -1;
        }
    }

    /**
     * Get all the records for a given project. Requires one parameter
     * 'projectName' which is the name of the project for which the records are
     * needed.
     * 
     * @param req
     * 			projectName : name of the specified project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getRecordsForProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);

        Set<Long> recordSet = new HashSet<Long>();
        List<Record> records = SysManagerFactory.getRecordManager().getRecords(userName, projectName,
                RequestConstants.MAX_RECORDS);
        int recordCount = SysManagerFactory.getProjectManager().getRecordCount(userName, projectName);
        if (records != null) {
            for (Record record : records) {
                recordSet.add(record.guid);
            }
        }

        Map<String, Object> ret = getRecordData(userName, projectName, recordSet, recordCount);
        writeJSON(resp, ret);
    }

    private List<String> getFields(List<SearchColumn> spreadSheetColumns) {
        List<String> ret = new ArrayList<String>();
        ret.add("Record ID");
        
        if(spreadSheetColumns!=null){
	        for (SearchColumn searchColumn : spreadSheetColumns) {
	            ret.add(searchColumn.getField());
	        }
        }
        return ret;
    }
    
    private List<String> getFieldTypes(List<SearchColumn> spreadSheetColumns) {
        List<String> ret = new ArrayList<String>();
        ret.add(ExtJsFieldType.INT.toString());
        
        if(spreadSheetColumns!=null){
	        for (SearchColumn searchColumn : spreadSheetColumns) 
	        {
	        	ExtJsFieldType extType = toExtType(searchColumn.getType());
	            ret.add(extType.toString());
	        }
        }
        return ret;
    }
    
    /**
     * converts annotation type to extjs type
     * @param type
     * @return
     */
    private ExtJsFieldType toExtType(AnnotationType type)
    {
    	switch (type)
		{
			case Integer:
				return ExtJsFieldType.INT;
				
			case Real:
				return ExtJsFieldType.FLOAT;
			
			case Text:
				return ExtJsFieldType.STRING;
				
			case Time:
				return ExtJsFieldType.DATE;

			default:
				return ExtJsFieldType.AUTO;
		}
    }

    /**
     * TODO: Should be a sublist of
     * {@link ProjectManager#getRecordFixedFields(String)}
     * 
     * @return
     */
    static List<SearchColumn> getDefaultSpreadsheetColumns() {
        List<SearchColumn> fixedColumns = new ArrayList<SearchColumn>();

        fixedColumns.add(new SearchColumn("Uploaded By", AnnotationType.Text, "uploaded_by"));

        fixedColumns.add(new SearchColumn("Slice Count", AnnotationType.Integer, "number_of_slices"));
        fixedColumns.add(new SearchColumn("Frame Count", AnnotationType.Integer, "number_of_frames"));
        fixedColumns.add(new SearchColumn("Channel Count", AnnotationType.Integer, "number_of_channels"));
        fixedColumns.add(new SearchColumn("Site Count", AnnotationType.Integer, "number_of_sites"));

        fixedColumns.add(new SearchColumn("Image Width", AnnotationType.Integer, "image_width"));
        fixedColumns.add(new SearchColumn("Image Height", AnnotationType.Integer, "image_height"));

        fixedColumns.add(new SearchColumn("Source Folder", AnnotationType.Text, "source_folder"));
        fixedColumns.add(new SearchColumn("Source File", AnnotationType.Text, "source_filename"));

        return fixedColumns;
    }

    private List<Map<String, Object>> transformData(Map<Long, List<MetaData>> recordMetaData) {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if(recordMetaData !=null){
        	for (Long key : recordMetaData.keySet()) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("Record ID", key);
                List<MetaData> metaList = recordMetaData.get(key);
                for (MetaData metaData : metaList) {
                    if (metaData != null)
                        item.put(metaData.getName(), metaData.stringValue());
                }
                ret.add(item);
            }
        }
        
        return ret;
    }

    /**
     * Get the records within the given navigation bin.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getRecordsForBin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String fieldName = getRequiredParam(RequestConstants.FIELD_NAME_KEY, req);
        AnnotationType fieldType = AnnotationType.valueOf(getRequiredParam(RequestConstants.FIELD_TYPE_KEY, req));
        String conditionsString = getRequiredParam(RequestConstants.CONDITIONS_KEY, req);

        String minString = req.getParameter(RequestConstants.MIN_KEY);
        String maxString = req.getParameter(RequestConstants.MAX_KEY);

        List<Map<String, String>> conditionsList = gson.fromJson(conditionsString,
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        Set<SearchCondition> conditions = ParseUtil.decodeConditions(conditionsList);
        Object min = (minString == null) ? null : ParseUtil.decodeAnnotationObject(minString, fieldType);
        Object max = (maxString == null) ? null : ParseUtil.decodeAnnotationObject(maxString, fieldType);

        logger.logp(Level.INFO, "ProjectServlet", "getRecordsForBin", "conditions: " + conditions + " min: " + min
                + " max: " + max + " fieldName: " + fieldName + " fieldType: " + fieldType);

        NavigationBin bin = getNavigationBin(conditions, fieldName, min, max, fieldType);

        Set<Long> recordSet = new HashSet<Long>();
        long[] records = SysManagerFactory.getSearchEngine().getRecords(userName, projectName, bin.toFilter(),
                RequestConstants.MAX_RECORDS);
        
        if(records!=null)
        {
        	for (long l : records) {
                recordSet.add(l);
            }
        }
        
        Map<String, Object> ret = getRecordData(userName, projectName, recordSet, bin.getRecordCount());
        writeJSON(resp, ret);
    }
    
    /**
     * Get Input and Output Records for given task 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    
    public void getRecordsForTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	String userName = getCurrentUser(req);
    	long taskId = Long.parseLong(getRequiredParam(RequestConstants.TASKID_KEY, req));
    	
    	String projectName=SysManagerFactory.getComputeEngine().getTaskProject(userName, taskId);
    	
    	Map<String, Map<String,Object>> inputOutputRecordsMap = new HashMap<String, Map<String,Object>>();
    	
    	// task input guids
    	Set<Long> recordSet = new HashSet<Long>();
    	long[] records = SysManagerFactory.getComputeEngine().getTaskInputs(userName, taskId); 
    	for (long l : records) {
            recordSet.add(l);
        }
    	Map<String, Object> inputs = getRecordDataForTask(userName, projectName, recordSet, recordSet.size(), "Input");
    	inputOutputRecordsMap.put(RequestConstants.TASK_INPUT_GUIDS, inputs);
    	
    	// task output guids
        recordSet = new HashSet<Long>();
        
		long[] outputRecords = SysManagerFactory.getComputeEngine().getTaskOutputs(userName, taskId);
		for (long l : outputRecords)
		{
			recordSet.add(l);
		}
    	
        Map<String, Object> outputs = getRecordDataForTask(userName, projectName, recordSet, recordSet.size(), "Output");
        inputOutputRecordsMap.put(RequestConstants.TASK_OUTPUT_GUIDS, outputs);
        
        writeJSON(resp, inputOutputRecordsMap);
    }
    
    private Map<String, Object> getRecordDataForTask(String userName, String projectName, Set<Long> recordSet, int actualSize, String value) throws DataAccessException
    {
    	Map<String, Object> recordData = getRecordData(userName, projectName, recordSet, actualSize);
    	
    	// update fields
    	@SuppressWarnings("unchecked")
		List<String>fields = (List<String>) recordData.get("fields");
    	fields.add("Input/Output");
    	
    	// updata data
    	@SuppressWarnings("unchecked")
    	List<Map<String, Object>> data = (List<Map<String, Object>>) recordData.get("data");
    	for(Map<String, Object>entry:data)
    		entry.put("Input/Output", value);
    	
    	return recordData;
    }

    /**
     * Get record data for the given record set
     * 
     * @param userName
     * @param projectName
     * @param recordSet
     * @return
     * @throws DataAccessException
     */
    private Map<String, Object> getRecordData(String userName, String projectName, Set<Long> recordSet, int actualSize)
            throws DataAccessException {
        UserPreference userPreference = SysManagerFactory.getUserPreference();
        List<SearchColumn> spreadSheetColumns = userPreference.getSpreadSheetColumns(userName, projectName);

        if (spreadSheetColumns == null) {
            spreadSheetColumns = getDefaultSpreadsheetColumns();
        }

        // HACK: Fix this? Do a separate call to get image dimensions?
        if(!spreadSheetColumns.contains(new SearchColumn("Image Width", AnnotationType.Integer, "image_width")))
        	spreadSheetColumns.add(new SearchColumn("Image Width", AnnotationType.Integer, "image_width"));
        
        if(!spreadSheetColumns.contains(new SearchColumn("Image Height", AnnotationType.Integer, "image_height")))
        	spreadSheetColumns.add(new SearchColumn("Image Height", AnnotationType.Integer, "image_height"));
        
        if(!spreadSheetColumns.contains(new SearchColumn("Pixel Size X", AnnotationType.Real, "pixel_size_x")))
        	spreadSheetColumns.add(new SearchColumn("Pixel Size X", AnnotationType.Real, "pixel_size_x"));
        
        if(!spreadSheetColumns.contains(new SearchColumn("Pixel Size Y", AnnotationType.Real, "pixel_size_y")))
        	spreadSheetColumns.add(new SearchColumn("Pixel Size Y", AnnotationType.Real, "pixel_size_y"));
		
        Map<Long, List<MetaData>> recordMetaData = SysManagerFactory.getRecordManager().getRecordMetaData(userName, projectName,
                recordSet, spreadSheetColumns.toArray(new SearchColumn[0]));

        List<Map<String, Object>> data = transformData(recordMetaData);
        
        List<String> fields = getFields(spreadSheetColumns);
        List<String> types = getFieldTypes(spreadSheetColumns);
        
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("data", data);
        ret.put("fields", fields);
        ret.put("types", types);
        ret.put("count", actualSize);
        return ret;
    }
    
    private Map<String, Object> createEmptyRecordMap(String userName, String projectName) throws DataAccessException
    {
    	UserPreference userPreference = SysManagerFactory.getUserPreference();
        List<SearchColumn> spreadSheetColumns = userPreference.getSpreadSheetColumns(userName, projectName);
        
        List<String> fields = getFields(spreadSheetColumns);
        List<String> types = getFieldTypes(spreadSheetColumns);
        
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> emptyRecord = new HashMap<String, Object>();
        emptyRecord.put("Record ID", -1L);
        data.add(emptyRecord);
        
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("data", data);
        ret.put("fields", fields);
        ret.put("types", types);
        ret.put("count", 1);
        
        return ret;
    }
    
    

    /**
     * Get records with the given set of ids
     * 
     * @param req
     * 			projectName : name of the project
     * 			recordids: list of record ids
     * @param resp
     * 			data : gson representation of record metadata
     * 			fields : field values for the spreadsheet
     * 			count : number of fields 
     * @throws ServletException
     * @throws IOException
     */
    public void getRecords(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String recordIdsString = getRequiredParam(RequestConstants.RECORD_IDS_KEY, req);
        List<Long> recordIDs = gson.fromJson(recordIdsString, (new TypeToken<List<Long>>() {
        }).getType());

        Set<Long> recordSet = new LinkedHashSet<Long>(recordIDs);
        Map<String, Object> recordData = getRecordData(userName, projectName, recordSet, recordSet.size());
        writeJSON(resp, recordData);
    }   
    
    /**
     * Get all thumbnail information for a given project. Requires one parameter
     * 'projectName' which is the name of the project for which the thumbnails
     * are needed. Note: The response contains URLS for the thumbnails, which
     * are again loaded using
     * {@link #getThumbnail(HttpServletRequest, HttpServletResponse)} call.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getThumbnailsForProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        List<Record> records = SysManagerFactory.getRecordManager().getRecords(userName, projectName,
                RequestConstants.MAX_RECORDS);
        if (records != null) {
            for (Record record : records) {
                Map<String, String> next = new HashMap<String, String>();
                next.put("id", record.guid + "");
                next.put("imagesource", getImageURL(record.guid, projectName));
                ret.add(next);
            }
        }
        writeJSON(resp, ret);
    }

    /**
     * Get all thumbnail information for a given project. Requires one parameter
     * 'projectName' which is the name of the project for which the thumbnails
     * are needed. Note: The response contains URLS for the thumbnails, which
     * are again loaded using
     * {@link #getThumbnail(HttpServletRequest, HttpServletResponse)} call.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getThumbnailsForBin(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String fieldName = getRequiredParam(RequestConstants.FIELD_NAME_KEY, req);
        AnnotationType fieldType = AnnotationType.valueOf(getRequiredParam(RequestConstants.FIELD_TYPE_KEY, req));
        String conditionsString = getRequiredParam(RequestConstants.CONDITIONS_KEY, req);

        String minString = req.getParameter(RequestConstants.MIN_KEY);
        String maxString = req.getParameter(RequestConstants.MAX_KEY);

        List<Map<String, String>> conditionsList = gson.fromJson(conditionsString,
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        Set<SearchCondition> conditions = ParseUtil.decodeConditions(conditionsList);
        Object min = minString == null ? null : ParseUtil.decodeAnnotationObject(minString, fieldType);
        Object max = maxString == null ? null : ParseUtil.decodeAnnotationObject(maxString, fieldType);

        NavigationBin bin = getNavigationBin(conditions, fieldName, min, max, fieldType);
        SearchEngine searchEngine = SysManagerFactory.getSearchEngine();

        long[] recordIDs = searchEngine.getRecords(userName, projectName, bin.toFilter(), RequestConstants.MAX_RECORDS);

        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        if (recordIDs != null) {
            for (long record : recordIDs) {
                Map<String, String> next = new HashMap<String, String>();
                next.put("id", record + "");
                next.put("imagesource", getImageURL(record, projectName));
                ret.add(next);
            }
        }
        writeJSON(resp, ret);
    }

    private NavigationBin getNavigationBin(Set<SearchCondition> conditions, String fieldName, Object min, Object max,
            AnnotationType fieldType) {
        switch (fieldType) {
        case Integer:
        	if(min == null) min = Long.MIN_VALUE;
        	if(max == null) max = Long.MAX_VALUE;
            return new NavigationBin(conditions, fieldName, (Long) min, (Long) max);
        case Real:
        	if(min == null) min = Double.MIN_VALUE;
        	if(max == null) max = Double.MAX_VALUE;
            return new NavigationBin(conditions, fieldName, (Double) min, (Double) max);
        case Text:
        	if(min == null) min = "";
        	if(max == null) max = "";
            return new NavigationBin(conditions, fieldName, (String) min, (String) max);
        case Time:
            return new NavigationBin(conditions, fieldName, (Timestamp) min, (Timestamp) max);
        }
        return null;
    }

    /**
     * Get thumbnail for the given project and record id Requires two
     * parameters, projectName and recordid
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getThumbnail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        logger.logp(Level.INFO, "ProjectServlet", "getThumbnail", "get thumbnail for record: " + recordID);
        
      //Check for etag
        String token = SysManagerFactory.getThumbnailManager().getETag(userName, recordID);
        resp.setHeader("ETag", token); // always store the ETag in the header
    	
      	String previousToken = req.getHeader("If-None-Match");
      	
    	if (previousToken != null && previousToken.equals(token)) { // compare previous token with current one
    		logger.logp(Level.FINEST, "ProjectServlet", "getThumbnail", "ETag match: returning 304 Not Modified");
            resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
     		// use the same date we sent when we created the ETag the first time though
        	resp.setHeader("Last-Modified", req.getHeader("If-Modified-Since"));
        	return;
     	} 
    	else  { 		// first time through - set last modified time to now 
     		Calendar cal = Calendar.getInstance();
     		cal.set(Calendar.MILLISECOND, 0);
     		Date lastModified = cal.getTime();
     		resp.setDateHeader("Last-Modified", lastModified.getTime());
     		logger.logp(Level.FINEST, "ProjectServlet", "getThumbnail", "ETag not match: writing body content");
     		InputStream thumbnailStream = SysManagerFactory.getThumbnailManager().getThumbnail(userName, recordID);
            
            resp.setContentType("image/jpeg");
            resp.addHeader("Cache-Control", "must-revalidate,s-maxage=0,max-age=0;");
            Util.transferData(thumbnailStream, resp.getOutputStream(), true);
    	}	
        
    }

    /**
     * Prepare an archive for download. This creates an archive for the request
     * and returns if it is successful. Actual download will happen using the
     * {@link #getArchive(HttpServletRequest, HttpServletResponse)} call.
     * 
     * @param req
     * 				recordid : record id of the specified record
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void prepareArchive(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get record id
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        HttpSession session = req.getSession();

        // If a valid file for this record id is already present in the session,
        // send file ready signal
        Object attribute = session.getAttribute(recordID + "");
        if (attribute != null) {
            File archive = new File(attribute.toString());
            if (archive.exists()) {
                writeSuccessFile(resp, archive);
                return;
            }
        }

        // File not present, create archive and then send signal
        String userName = getCurrentUser(req);
        File archive = SysManagerFactory.getStorageManager().getArchive(userName, recordID, true);
        session.setAttribute(recordID + "", archive.getCanonicalPath());

        writeSuccessFile(resp, archive);
    }

    /**
     * Write file information if archiving is successful or if a file is already
     * present for this recordid
     * 
     * @param resp
     * @param file
     * @throws IOException
     */
    private void writeSuccessFile(HttpServletResponse resp, File file) throws IOException {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("name", file.getName());
        ret.put("size", file.length());
        writeJSON(resp, ret);
    }

    /**
     * Get the archive for the given recordid. Requires one parameter, the
     * recordID. Checks if a valid file is present in the session for this
     * recordid and then passes the same.
     * 
     * Note: {@link #prepareArchive(HttpServletRequest, HttpServletResponse)}
     * needs to be called for this call to work.
     * 
     * @param req
     * 				recordId : the specified record id
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getArchive(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get record id
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));

        // Check if session has a valid entry for this record id
        // If there is no valid file, return error. If valid file found,
        // transfer the file.
        HttpSession session = req.getSession();
        Object attribute = session.getAttribute(recordID + "");
        if (attribute == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        File archive = new File(attribute.toString());
        if (!archive.exists()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) archive.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + archive.getName() + "\"");
        Util.transferData(new FileInputStream(archive), resp.getOutputStream(), true);

        // Cleanup the file
        archive.delete();
    }

    /**
     * Get image for the given record id, and image attributes frameNumber,
     * sliceNumber, channelNumber
     * 
     * @param req
     * 				recordId : id of the specified record
     * 				frameNumber : specified frame
     * 				sliceNumber : specified slice
     * 				siteNumber : specified site
     * 				channelNumbers : list of selected channels
     * 				isGrayScale : true if GrayScale image is selected, false otherwise
     * 				isZStacked : true if ZStacked image is selected, false otherwise
     * 				isMosaic : true if Channel Mosaic is selected, false otherwise
     * 				height : height of the image
     * 				window_x : x co-ordinate of the tile (top, left)
     * 				window_y : y co-ordinate of the tile (top, left)
     * 				window_width : width of the tile
     * 				window_height : height of the tile
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String userName = getCurrentUser(req);
        final long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        
        String token = recordID+"";
      	String previousToken = req.getHeader("If-None-Match");
      	
		if (previousToken != null && previousToken.equals(token))
		{
			// compare previous token with current one
			// Check for etag

			// ETag is set recordID. This is because all the parameters for the
			// image are part of the url.
			// Also overlays are not part of getImage. And image data remains
			// unchanged.
			resp.setHeader("ETag", token); // always store the ETag in the header
			logger.logp(Level.FINEST, "ProjectServlet", "getImage", "ETag match: returning 304 Not Modified");
			resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			// use the same date we sent when we created the ETag the first time
			// though
			resp.setHeader("Last-Modified", req.getHeader("If-Modified-Since"));
			return;
		}		
		
		// fetch url parameters
        logger.logp(Level.INFO, "ProjectServlet", "getImage", "get image for record: " + recordID);
        final int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        final int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        final int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        
        List<Integer> channelNumbers = gson.fromJson(getRequiredParam(RequestConstants.CHANNEL_NUMBERS_KEY, req)
                .toString(), new TypeToken<List<Integer>>() {
        }.getType());
        
        boolean isGreyScale = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_GREY_SCALE, req));
        boolean isZStacked = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));
        boolean isMosaic = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_MOSAIC, req));

        String heightString = req.getParameter(RequestConstants.HEIGHT_KEY);
        Integer requiredHeight = heightString == null ? null : Integer.parseInt(heightString);

        String tileWindowXString = req.getParameter(RequestConstants.WINDOW_X_KEY);
        Integer tileWindowX = tileWindowXString == null ? null : Integer.parseInt(tileWindowXString);
        
        Rectangle displayRectangle=null; //new Rectangle(100,100,200,200)
        Rectangle engineRectangle=null;
        
		if (tileWindowX != null)
		{
			Rectangle[] rects = createRectangles(req);
			displayRectangle = rects[0];
			engineRectangle = rects[1];
		}
		
        BigInteger archiveSignature = SysManagerFactory.getRecordManager().findRecord(userName, recordID).archiveSignature;

        // immediately put request for prefetching and caching the raw data
        try
		{
        	Thread t = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						SysManagerFactory.getImageManager().submitRawDataPrefetchRequest(userName, recordID,
								new Dimension(frameNumber, sliceNumber, 0, siteNumber));
					}
					catch (Exception e)
					{
						logger.logp(Level.WARNING, "ProjectServlet", "getImage", "error in submitting the prefetch request", e);
					}
				}
			});
        	
        	t.start();
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ProjectServlet", "getImage", "error in submitting the prefetch request", e);
		}
        
        // if image reader is not available then return without generating images
		// client will poll for imagereader availability and then call getImage
		if (SysManagerFactory.getImageReadersManager().isReaderAvailable(archiveSignature) == false)
		{	
			// intentionally put content type as text so that <img> tag will
			// throw error.
			// error is catched for polling imagereader availability
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("text/plain");
			return;
		}

		// if image reader is available then generate the image and send it
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		Date lastModified = cal.getTime();
		resp.setHeader("ETag", token); // always store the ETag in the
										// header
		resp.setDateHeader("Last-Modified", lastModified.getTime());
		logger.logp(Level.FINEST, "ProjectServlet", "getImage", "ETag not match: writing body content");

		BufferedImage engineImage = getEngineImage(userName, recordID, sliceNumber, frameNumber, siteNumber, channelNumbers, isGreyScale, isZStacked,
				isMosaic, engineRectangle, null);

		BufferedImage displayImage = engineImage;
		if (displayRectangle != null)
		{
			displayImage = new BufferedImage(displayRectangle.width, displayRectangle.height, engineImage.getType());
			Graphics2D g = displayImage.createGraphics();
			g.drawImage(engineImage, 0, 0, engineImage.getWidth(), engineImage.getHeight(), 0, 0, engineImage.getWidth(), engineImage.getHeight(), null);
			g.dispose();
		}

		if (requiredHeight != null)
		{
			displayImage = getScaledImage(requiredHeight, displayImage);
		}

		String mode = req.getParameter("mode") == null ? "default" : req.getParameter("mode");
		BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
		if (mode.equals("download") || requiredHeight == null)
		{
			resp.setContentType("image/png");
			resp.addHeader("Cache-Control", "must-revalidate,s-maxage=0;");
			// resp.addHeader("Cache-Control", "maxage=1000;");
			// resp.addHeader("Expires",
			// System.currentTimeMillis()+30*1000+"");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + "Record.png" + "\"");
			try
			{
				ImageUtil.writeImage(displayImage, outputStream);
			}
			catch (Exception e)
			{
				logger.logp(Level.INFO, "ProjectServlet", "getImage", "failed writing the image " + e.getMessage());
			}
			finally
			{
				outputStream.close();
			}
		}
		else
		{
			resp.setContentType("image/jpeg");
			resp.addHeader("Cache-Control", "must-revalidate,s-maxage=0;");
			try
			{
				ImageIO.write(displayImage, "JPG", outputStream);
			}
			catch (Exception e)
			{
				logger.logp(Level.INFO, "ProjectServlet", "getImage", "failed writing the image " + e.getMessage());
			}
			finally
			{
				outputStream.close();
			}
		}
    }
    
	private BufferedImage getEngineImage(String userName, long recordID, int sliceNumber, int frameNumber, int siteNumber, List<Integer> channelNumbers, boolean isGreyScale, boolean isZStacked,
			boolean isMosaic, Rectangle engineRectangle, Map<Integer, Channel> channelDetailsMap) throws IOException
	{
		Record record = SysManagerFactory.getRecordManager().findRecord(userName, recordID);
		if(engineRectangle == null)
		{
			int width = record.imageWidth > Constants.MAX_TILE_WIDTH ? Constants.MAX_TILE_WIDTH : record.imageWidth;
			int height = record.imageWidth > Constants.MAX_TILE_HEIGHT ? Constants.MAX_TILE_HEIGHT : record.imageHeight;
			engineRectangle = new Rectangle(0, 0, width, height);
		}
		return SysManagerFactory.getImageManager().getPixelDataOverlay(userName, recordID, sliceNumber, frameNumber, siteNumber, channelNumbers, !isGreyScale, isZStacked, isMosaic, engineRectangle,
				channelDetailsMap);
	}

	private Rectangle[] createRectangles(HttpServletRequest req) throws DataAccessException, ServletException
	{
    	String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        
        String tileWindowXString = req.getParameter(RequestConstants.WINDOW_X_KEY);
        Integer tileWindowX = tileWindowXString == null ? null : Integer.parseInt(tileWindowXString);
        
    	String tileWindowYString = req.getParameter(RequestConstants.WINDOW_Y_KEY);
		Integer tileWindowY = tileWindowYString == null ? null : Integer.parseInt(tileWindowYString);

		String tileWindowWidthString = req.getParameter(RequestConstants.WINDOW_WIDTH_KEY);
		Integer tileWindowWidth = tileWindowWidthString == null ? null : Integer.parseInt(tileWindowWidthString);

		String tileWindowHeightString = req.getParameter(RequestConstants.WINDOW_HEIGHT_KEY);
		Integer tileWindowHeight = tileWindowHeightString == null ? null : Integer.parseInt(tileWindowHeightString);

		Rectangle displayRectangle = new Rectangle(tileWindowX, tileWindowY, tileWindowWidth, tileWindowHeight);

		Record record = SysManagerFactory.getRecordManager().findRecord(userName, recordID);

		int engineWidth = (tileWindowX + tileWindowWidth <= record.imageWidth) ? tileWindowWidth : record.imageWidth - tileWindowX;
		int engineHeight = (tileWindowY + tileWindowHeight <= record.imageHeight) ? tileWindowHeight : record.imageHeight - tileWindowY;

		Rectangle engineRectangle = new Rectangle(tileWindowX, tileWindowY, engineWidth, engineHeight);
		
		Rectangle[] rect = new Rectangle[2];
		rect[0] = displayRectangle;
		rect[1] = engineRectangle;
		
		return rect;
	}

	/**
     * checks if server has image reader available for image creation
     * 
     * @param req
     * 				recordId : selected record id
     * 				requestImageReader : true if image reader creation request is to be made, false otherwise
     * @param resp
     * 				trackerId : id of the server side image reader 
     * @throws ServletException
     * @throws IOExceptioncc
     */
    public void checkImageReaderAvailability(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        BigInteger archiveSignature = SysManagerFactory.getRecordManager().findRecord(userName, recordID).archiveSignature;
        boolean requestImageReader = Boolean.parseBoolean(req.getParameter("requestImageReader")); 
        long trackerId = 0;
        if(requestImageReader)
        {
        	//Request for image reader invocation
        	trackerId= SysManagerFactory.getImageReadersManager().initializeReader(userName, recordID, new Dimension(0, 0, 0, 0));
    	}
        
        boolean isReaderAvailable=SysManagerFactory.getImageReadersManager().isReaderAvailable(archiveSignature);
        writeJSON(resp, Util.createMap("available", isReaderAvailable,"trackerId",trackerId+""));
    }
    
    /**
     * cancel the image generation request
     * 
     * @param req
     * 				requestTrackerIDs : image generator ids which are to be cancelled
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void cancelImageReaderRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String requestTrackerIDsStr =req.getParameter("requestTrackerIDs");
    	List<String> requestTrackerIDs=gson.fromJson(requestTrackerIDsStr,new TypeToken<List<String>>() {
			}.getType());
    	ImageReadersManager imageReadersManager= SysManagerFactory.getImageReadersManager();
    	for(String trackedId: requestTrackerIDs){
    		long id=Long.parseLong(trackedId);
    		imageReadersManager.cancelRequest(id);
    	}
    }

    /**
     * Get overlay image for the given record id, and image attributes frameNumber,
     * sliceNumber, channelNumber
     * 
     * @param req
     * 				recordId : selected record 
     * 				frameNumber : selected frame
     * 				sliceNumber : selected slice
     * 				siteNumber : selected site
     * 				overlays : list of selected overlays
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getOverlayTransparency(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        logger.logp(Level.INFO, "ProjectServlet", "getOverlayTransparency", "get image for record: " + recordID);
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        
        String heightString = req.getParameter(RequestConstants.HEIGHT_KEY);
        Double requiredHeight = heightString == null ? null : Double.parseDouble(heightString);
        
        String overlayString = getOptionalParam(RequestConstants.OVERLAY_NAMES_KEY, req);
        List<String> overlays = null;
        if (overlayString != null) {
            overlays = gson.fromJson(overlayString, new TypeToken<List<String>>() {
            }.getType());
        }
        
        String scalebarValueString = getOptionalParam(RequestConstants.SCALEBAR, req);
        boolean scalebar = scalebarValueString == null ?  false : Boolean.parseBoolean(scalebarValueString);
        
        //Check for etag
        String token = SysManagerFactory.getImageManager().getEtag(
        		userName, recordID, sliceNumber, frameNumber, siteNumber, overlays, scalebar);
        resp.setHeader("ETag", token); // always store the ETag in the header
    	
      	String previousToken = req.getHeader("If-None-Match");
      	previousToken = String.valueOf(System.currentTimeMillis());
      	
    	if (previousToken != null && previousToken.equals(token)) { // compare previous token with current one
    		logger.logp(Level.INFO, "ProjectServlet", "getOverlayTransparency", "ETag match: returning 304 Not Modified");
            resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
     		// use the same date we sent when we created the ETag the first time though
        	resp.setHeader("Last-Modified", req.getHeader("If-Modified-Since"));
        	return;
     	} 
    	else  { 		// first time through - set last modified time to now 
     		Calendar cal = Calendar.getInstance();
     		cal.set(Calendar.MILLISECOND, 0);
     		Date lastModified = cal.getTime();
     		resp.setDateHeader("Last-Modified", lastModified.getTime());
     		logger.logp(Level.INFO, "ProjectServlet", "getOverlayTransparency", "ETag not match: writing body content");
     		
            BufferedImage image = SysManagerFactory.getImageManager().
            	getOverlayTransperancy(userName, recordID, frameNumber, sliceNumber, siteNumber, overlays, (int)Math.round(requiredHeight), scalebar);
            
            BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
			resp.setContentType("image/png");
			resp.addHeader("Cache-Control", "must-revalidate,s-maxage=0;");
			try
			{
				ImageUtil.writeImage(image, outputStream);
			}
			catch (Exception e)
			{
				logger.logp(Level.INFO, "ProjectServlet", "getImage", "failed writing the image " + e.getMessage());
			}
			finally
			{
				outputStream.close();
			}
     	}    
    }    
    
    /**
     * get xPixelSize for recordid
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getXPixelSize (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String actorLogin = getCurrentUser(req);
    	int guid = Integer.parseInt(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
    	
    	double xPixelSize= SysManagerFactory.getImageManager().getXPixelSize(actorLogin, guid);
    	String unit=SysManagerFactory.getImageManager().getLengthUnit(actorLogin, guid);
    	
    	Map<String,Object> ret=new HashMap<String,Object>();
    	ret.put("xPixelSize", xPixelSize);
    	ret.put("unit", unit);
    	
        writeJSON(resp, ret);  	
    }
    
    
    /**
     * get legends for image
     * @param req
     * 				recordId : id of the record
     * 				frame : frame number
     * 				slice : slice number
     * 				site : site number
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getLegends(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String actorLogin = getCurrentUser(req);
        long recordId = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        logger.logp(Level.INFO, "ProjectServlet", "getLegends", "get image for record: " + recordId);
        
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));
        
        String legendText = SysManagerFactory.getImageManager().getLegendText(actorLogin, recordId, frameNumber, sliceNumber, siteNumber);
        
    	writeJSON(resp, Util.createMap("legend", legendText));
    }

    /**
     * Tile based access to the image
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getTileImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));

        List<Integer> channelNumbers = gson.fromJson(getRequiredParam(RequestConstants.CHANNEL_NUMBERS_KEY, req)
                .toString(), new TypeToken<List<Integer>>() {
        }.getType());

        boolean isGreyScale = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_GREY_SCALE, req));
        boolean isZStacked = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_Z_STACKED, req));
        boolean isMosaic = Boolean.parseBoolean(getRequiredParam(RequestConstants.IS_MOSAIC, req));

        int xtile = Integer.parseInt(getRequiredParam(RequestConstants.XTILE_KEY, req));
        int ytile = Integer.parseInt(getRequiredParam(RequestConstants.YTILE_KEY, req));
        double zoomLevel = Double.parseDouble(getRequiredParam(RequestConstants.ZOOM_KEY, req));

        int scaledTileWidth = (int) (TILE_WIDTH / zoomLevel);
        int scaledTileHeight = (int) (TILE_WIDTH / zoomLevel);

        Rectangle tilePosition = new Rectangle(xtile * scaledTileWidth, ytile * scaledTileHeight, scaledTileWidth,
                scaledTileHeight);

        String userName = getCurrentUser(req);
        BufferedImage image = SysManagerFactory.getImageManager().getTileImage(userName, recordID, sliceNumber,
                frameNumber, siteNumber, channelNumbers, !isGreyScale, isZStacked, isMosaic, tilePosition);

        BufferedImage resizeImage = Util.resizeImage(image, TILE_WIDTH, TILE_HEIGHT);

        MemoryCacheImageOutputStream outputStream = new MemoryCacheImageOutputStream(resp.getOutputStream());
        try
        {
        	ImageIO.write(resizeImage, "JPG", outputStream);
        }
        catch(Exception e)
        { }
        resp.setContentType("image/jpeg");
        int cacheTime = 60 * 30; // 30 mins
        resp.setHeader("Expires", getGMTTimeString(System.currentTimeMillis() + cacheTime));
        resp.setHeader("Cache-Control", cacheTime + "");
        outputStream.close();
    }

    /**
     * Get the default image for a record. Takes the bare minimum required to
     * generate a high-res image for a record. Only recordid is required. Height
     * is an optional argument.
     * 
     * @param req
     * 				recordId : selected record
     * 				height : height of the image, optional parameter
     * 				siteNumber : selected site
     * 				sliceNumber : selected slice
     * 				frameNumber : selected frame
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getDefaultImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));

        String heightString = req.getParameter(RequestConstants.HEIGHT_KEY);
        Integer requiredHeight = heightString == null ? null : Integer.parseInt(heightString);

        String siteString = req.getParameter(RequestConstants.SITE_NUMBER_KEY);
        Integer siteNumber = siteString == null ? 0 : Integer.parseInt(siteString);

        String sliceString = req.getParameter(RequestConstants.SLICE_NUMBER_KEY);
        String frameString = req.getParameter(RequestConstants.FRAME_NUMBER_KEY);

        String includeOverlay = getOptionalParam("overlay", req);

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        Record record = recordManager.findRecord(userName, recordID);
        int channelCount = record.getChannelCount();
        List<Integer> allChannels = new ArrayList<Integer>();
        for (int i = 0; i < channelCount; ++i)
            allChannels.add(i);
        int sliceNumber = (sliceString == null) ? (record.numberOfSlices / 2) : Integer.parseInt(sliceString);
        int frameNumber = (frameString == null) ? (record.numberOfFrames / 2) : Integer.parseInt(frameString);
        boolean overlay = (includeOverlay == null) ? false : Boolean.parseBoolean(includeOverlay);

        logger.logp(Level.INFO, "ProjectServlet", "getDefaultImage", "Get default image for: recordid: " + recordID
                + " slice: " + sliceNumber + " frame: " + frameNumber + " channels: " + allChannels);
        BufferedImage image = null;
        // Include all the overlays in the image generated.
        if (overlay) {
            VODimension d = new VODimension(frameNumber, sliceNumber, siteNumber);
            List<VisualOverlay> visualOverlays = SysManagerFactory.getRecordManager().getVisualOverlays(userName,
                    recordID, d);
            List<String> overlayNames = new ArrayList<String>();
            if (visualOverlays != null) {
                for (VisualOverlay next : visualOverlays)
                    overlayNames.add(next.getName());
            }
            image = SysManagerFactory.getImageManager().getPixelDataOverlay(userName, recordID, sliceNumber,
                    frameNumber, siteNumber, allChannels, true, false, false, overlayNames, new Rectangle(record.imageWidth, record.imageHeight));
        } else {
            image = SysManagerFactory.getImageManager().getPixelDataOverlay(userName, recordID, sliceNumber,
                    frameNumber, siteNumber, allChannels, true, false, false, new Rectangle(record.imageWidth, record.imageHeight));
        }

        if (requiredHeight != null) {
            image = getScaledImage(requiredHeight, image);
        }

        resp.setContentType("image/jpeg");
        MemoryCacheImageOutputStream outputStream = new MemoryCacheImageOutputStream(resp.getOutputStream());
        try
        {
        	ImageIO.write(image, "JPG", outputStream);
        }
        catch(Exception e)
        {}
        finally
		{
			outputStream.close();
		}
    }

    /**
     * Get scaled image, preserving the aspect ratio
     * 
     * @param requiredWidth
     * @param requiredHeight
     * @param image
     * @return
     */
    private BufferedImage getScaledImage(Integer requiredHeight, BufferedImage image) {
        float imgRatio = (float) image.getWidth() / (float) image.getHeight();
        int width = (int) (imgRatio * requiredHeight);
        image = Util.resizeImage(image, width, requiredHeight);
        return image;
    }

    /**
     * Get thumbnail URL to access the given record's thumbnail
     * 
     * @param record
     * @return
     */
    private String getImageURL(long recordid, String projectName) {
        return THUMBNAIL_BASE_URL + recordid + "&projectName=" + projectName;
    }

    /**
     * Get attachments for a record
     * 
     * @param req
     * 			recordId : selected record
     * @param resp
     * 			gson representation of list of {@link com.strandgenomics.imaging.iengine.models.Attachment}
     * @throws ServletException
     * @throws IOException
     */
    public void getRecordAttachments(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));
        List<Attachment> recordAttachments = SysManagerFactory.getAttachmentManager().getRecordAttachments(userName,
                recordID);
        writeJSON(resp, recordAttachments);
    }
    
    /**
     * get user added attachments for a record
     * @param req
     * 				recordId : selected record
     * @param resp
     * 				gson representation of list of {@link com.strandgenomics.imaging.iengine.models.Attachment}
     * @throws ServletException
     * @throws IOException
     */
    public void getUserAttachments(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));
        
        AttachmentManager attManager = SysManagerFactory.getAttachmentManager();
        
        List<Attachment> recordAttachments = attManager.getRecordAttachments(userName,recordID);
        
        List<Attachment> userAttachments = new ArrayList<Attachment>();
        if(recordAttachments!=null)
        {
        	for(Attachment attachment:recordAttachments)
            {
            	if(!attManager.isSystemAttachment(attachment.getName()) &&  !attachment.getName().startsWith(BioExperiment.SeriesMetadata))
            		userAttachments.add(attachment);
            }
        }
        writeJSON(resp, userAttachments);
    }
    
    /**
     * get system generated attachments for a record
     * @param req
     * 				recordId : selected record
     * @param resp
     * 				gson representation of list of {@link com.strandgenomics.imaging.iengine.models.Attachment}
     * @throws ServletException
     * @throws IOException
     */
    public void getSystemAttachments(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));
        
        AttachmentManager attManager = SysManagerFactory.getAttachmentManager();
        
        List<Attachment> recordAttachments = attManager.getRecordAttachments(userName, recordID);
        
        List<Attachment> systemAttachments = new ArrayList<Attachment>();
        if(recordAttachments!=null)
        {
        	for(Attachment attachment:recordAttachments)
            {
            	if(attManager.isSystemAttachment(attachment.getName()) || attachment.getName().startsWith(BioExperiment.SeriesMetadata))
            		systemAttachments.add(attachment);
            }
        }
        writeJSON(resp, systemAttachments);
    }

    /**
     * Add attachment to a record
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void addRecordAttachments(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        Map postData = null;
        if (MultiPartDataHandler.isMulipartFormData(req)) {
            try {

                String boundary = MultiPartDataHandler.getMultipartFormBoundary(req);
                if (boundary == null) {
                    throw new ServletException("expecting multipart form data");
                }
                BufferedInputStream inStream = new BufferedInputStream(req.getInputStream());
                File tempFolder = new File(System.getProperty("java.io.tmpdir"));
                MultiPartDataHandler mpHandler = new MultiPartDataHandler(inStream, boundary, null, tempFolder);
                postData = mpHandler.extractMultipartData();
                logger.logp(Level.INFO, "DownloadServlet", "saveData", "extracted multipart Data " + postData);
                if (postData == null) {
                    throw new ServletException("parameters missing");
                }
            } catch (Exception ex) {
                logger.logp(Level.WARNING, "DownloadServlet", "saveData", "reading multipart for storing data " + ex);
                ex.printStackTrace();
            }

        }

        if (!postData.containsKey(RequestConstants.RECORD_ID_KEY)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong((String) postData.get(RequestConstants.RECORD_ID_KEY));

        if (!postData.containsKey(RequestConstants.NOTES_KEY)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String notes = (String) postData.get(RequestConstants.NOTES_KEY);

        if (!postData.containsKey(RequestConstants.ATTACHMENT_FILE_KEY)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        File file = (File) postData.get(RequestConstants.ATTACHMENT_FILE_KEY);
        
        try{
        	
			SysManagerFactory.getAttachmentManager().addAttachmentForRecord(getWebApplication(), null, recordID, file, notes, userName);
	        writeJSON(resp, successResponse, "text/html");
	        
        }catch (ImagingEngineException e)
		{
			logger.logp(Level.WARNING, "ProjectServlet", "addRecordAttachment", "You dont have permission to add attachment ", e);
			writeFailure(resp, "You dont have permission to add attachment");
		}	
    }
    
    /**
     * deletes the specified attachment from specified record
     * @param req
     * 				recordid : specified record id
     * 				name : name of the attachment to be deleted
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	public void deleteAttachment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		try
		{
			String userName = getCurrentUser(req);
			long guid = Long.parseLong(getRequiredParam("recordid", req));
			String name = getRequiredParam("name", req);
			
			logger.logp(Level.INFO, "ProjectServlet", "deleteAttachment", "deleting attachment user="+userName+" guid="+guid+" attachment="+name);
			
			SysManagerFactory.getAttachmentManager().deleteRecordAttachments(getWebApplication(), userName, null, guid, name);
			
			writeSuccess(resp);
		}
		catch (ImagingEngineException e)
		{
			logger.logp(Level.WARNING, "ProjectServlet", "deleteAttachment", "You dont have permission to delete attachment ", e);
			writeFailure(resp, "You dont have permission to delete attachment");
		}	
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ProjectServlet", "deleteAttachment", "error while deleting attachment", e);
			writeFailure(resp, "error while deleting attachment");
		}		
	}

    /**
     * Download the given attachment
     * 
     * @param req
     * 				recordId : selected record
     * 				attachmentName : name of the selected attachment
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadAttachment(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.RECORD_ID_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long recordID = Long.parseLong(req.getParameter(RequestConstants.RECORD_ID_KEY));
        if (req.getParameter(RequestConstants.ATTACHMENT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String attachmentName = req.getParameter(RequestConstants.ATTACHMENT_NAME_KEY);

        File attachment = SysManagerFactory.getAttachmentManager().findAttachment(userName, recordID, attachmentName);
        if (!attachment.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) attachment.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getName() + "\"");
        Util.transferData(new FileInputStream(attachment), resp.getOutputStream(), true);
    }

    /**
     * Get metadata for an image
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getImageMetaData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        int sliceNumber = Integer.parseInt(getRequiredParam(RequestConstants.SLICE_NUMBER_KEY, req));

        logger.logp(Level.INFO, "ProjectServlet", "getImageMetaData", "get image data for : record " + recordID
                + " frame: " + frameNumber + " site: " + siteNumber + " slice: " + sliceNumber);

        String userName = getCurrentUser(req);

        Record record = SysManagerFactory.getRecordManager().findRecord(userName, recordID);
        if (record == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ImageManager imageManager = SysManagerFactory.getImageManager();
        int channelCount = record.getChannelCount();

        Map<Integer, ImagePixelData> dataMap = new HashMap<Integer, ImagePixelData>();
        for (int channelNumber = 0; channelNumber < channelCount; ++channelNumber) {
            Dimension imageCoordinate = new Dimension(frameNumber, sliceNumber, channelNumber, siteNumber);
            ImagePixelData imageMetaData = imageManager.getImageMetaData(userName, recordID, imageCoordinate);

            logger.logp(Level.INFO, "ProjectServlet", "getImageMetaData", "for channel: " + channelNumber
                    + " got data: " + imageMetaData);

            if (imageMetaData != null) {
                dataMap.put(channelNumber, imageMetaData);
            }
        }

        writeJSON(resp, getImagePixelDataMap(record, dataMap, channelCount));
    }
    
    /**
     * lists the formats in which record can be exported
     * see {@link com.strandgenomics.imaging.iengine.export.ExportFormat}
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listExportFormats(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
    	
    	for(ExportFormat format:ExportFormat.values())
    	{
    		Map<String, Object> value = new HashMap<String, Object>();
    		value.put("name", format.name());
    		value.put("value", format.toString());
    		
    		ret.add(value);
    	}
    	
    	
        writeJSON(resp, ret);
    }
    
    /**
     * the projects accessible to current user
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getMemberProjectNames(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String userLogin = getCurrentUser(req);
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
    	
    	List<String> projects = SysManagerFactory.getProjectManager().getActiveProjects(userLogin);
    	for(String projectName:projects)
    	{
    		Map<String, Object> value = new HashMap<String, Object>();
    		if(SysManagerFactory.getUserPermissionManager().canUpload(userLogin, projectName))
    		{
    			value.put("name", projectName);
        		value.put("value", projectName);
        		
        		ret.add(value);
    		}
    	}
    	
    	
        writeJSON(resp, ret);
    }
    
    /**
     * submits the record export request
     * @param req
     * 				name: name given to the download
     * 				format : see {@link com.strandgenomics.imaging.iengine.export.ExportFormat}
     * 				validity : time till which the download is valid
     * 				guids : list of guids to be downloaded
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void submitDownload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String loginUser = getCurrentUser(req);

        String name = getRequiredParam("name", req);
        ExportFormat format = ExportFormat.valueOf(getRequiredParam("format", req));
        long validity = Long.parseLong(getRequiredParam("validity", req));
        
        String guids = getRequiredParam("guids", req);
        logger.logp(Level.INFO, "ProjectServlet", "submitDownload", "name=" + name + " format=" + format+" validity="+validity+" login user="+loginUser+" guids="+guids);
        
        List<Long> recordIDs = gson.fromJson(guids, (new TypeToken<List<Long>>() {
        }).getType());
        
        SysManagerFactory.getExportManager().registerExportRequest(loginUser, recordIDs, format, validity, name);
        
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    
    /**
     * share records across projects
     * @param req
     * 				targetProject: name of the target project
     * 				guids : list of guids to be downloaded
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void shareRecords(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);

        String name = getRequiredParam("targetProject", req);
        
        String guids = getRequiredParam("guids", req);
        List<Long> recordIDs = gson.fromJson(guids, (new TypeToken<List<Long>>() {
        }).getType());
        
        logger.logp(Level.INFO, "ProjectServlet", "shareRecords", "name=" + name + " guids="+guids);
        
        try 
        {
			SysManagerFactory.getStorageManager().shareRecords(getWebApplication(), loginUser, "", recordIDs, name);
			writeSuccess(resp);
		} 
        catch (IOException e) 
        {
            logger.logp(Level.WARNING, "ProjectServlet", "shareRecords", "error in name=" + name + " guids="+guids, e);
            writeFailure(resp, "");
		}
    }
    
    /**
     * share records across projects
     * @param req
     * 				targetProject: name of the target project
     * 				guids : list of guids to be downloaded
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void transferRecords(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);

        String name = getRequiredParam("targetProject", req);
        int targetProjectId = SysManagerFactory.getProjectManager().getProject(name).getID();
        
        String guids = getRequiredParam("guids", req);
        List<Long> recordIDs = gson.fromJson(guids, (new TypeToken<List<Long>>() {
        }).getType());
        
        logger.logp(Level.INFO, "ProjectServlet", "shareRecords", "name=" + name + " guids="+guids);
        
        int cnt = 0;
        String errorMessage = "";
        for(Long seedGuid: recordIDs)
        {
        	try
        	{
        		int sourceProjectId = SysManagerFactory.getRecordManager().findRecord(loginUser, seedGuid).projectID;
        		
        		SysManagerFactory.getStorageManager().transferArchive(getWebApplication(), loginUser, "", seedGuid, sourceProjectId, targetProjectId);
        	}
        	catch(ImagingEngineException e)
        	{
        		errorMessage = e.getErrorCode().getErrorMessage();
        		cnt++;
            	logger.logp(Level.WARNING, "ProjectServlet", "shareRecords", "error in name=" + name + " guids="+guids, e);
        	}
			catch (DataAccessException e)
			{
				errorMessage = e.getMessage();
				logger.logp(Level.WARNING, "ProjectServlet", "shareRecords", "error in name=" + name + " guids="+guids, e);
			}
			catch (IOException e)
			{
				errorMessage = "Failed to transfer records";
				logger.logp(Level.WARNING, "ProjectServlet", "shareRecords", "error in name=" + name + " guids="+guids, e);
			}
        }
		
        if(cnt > 0)
        {
            writeFailure(resp, errorMessage);
        }
        else
			writeSuccess(resp);
    }
    
    /**
     * returns the list of downloads for specified user
     * @param req
     * @param resp
     * 				list of downloads. each download object is map of following fields
     * 				name : name of the download
     * 				input_guids : guids
     * 				link : download link
     * 				project : name of the project
     * 				id : request id
     * 				status : status of the download
     * @throws ServletException
     * @throws IOException
     */
    public void getDownloads(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	logger.logp(Level.INFO, "ProjectServlet", "getDownloads", "loginUser=" + loginUser);
    	
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();

    	DecimalFormat formatter = new DecimalFormat("#.##");   	
    	
    	// record exports
    	List<ExportRequest> exportRequests = SysManagerFactory.getExportManager().getExportRequsts(loginUser);
    	if(exportRequests!=null)
    	{
    		for(ExportRequest request:exportRequests)
        	{
        		List<Long> guids = new ArrayList<Long>();
        		guids.addAll(request.getRecordIds());
        		
        		long guid = request.getRecordIds().get(0);
        		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
        		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
        		
        		Map<String, Object> value = new HashMap<String, Object>();
                value.put("name", request.name);
                value.put("input_guids", guids);
                value.put("link", request.getURL());
                value.put("project", projectName);
                value.put("id", request.requestId+"");
                value.put("status", request.getStatus().name().toLowerCase());
                value.put("isMovie", false);
                
                double size = Double.valueOf(formatter.format(request.size/(1024.0*1024)));
                
                Date date=null;
                SimpleDateFormat simpleDate=null;
                date=new Date(request.submittedOn);
                simpleDate= new SimpleDateFormat("dd/MM/yy h:m:s");
        		String creationDate=simpleDate.format(date);       		
        		
        		date=new Date(request.validTill);
                simpleDate = new SimpleDateFormat("dd/MM/yy");
        		String expiryDate=simpleDate.format(date);
        		
                value.put("creationDate", creationDate);
                value.put("expiryDate", expiryDate);
                
                String formattedSize = size+" MB";
                value.put("size", formattedSize);
                
                ret.add(value);
        	}
    	}
    	
    	// movie exports
        List<MovieTicket> userVideos = SysManagerFactory.getMovieManager().getVideoMovies(loginUser);
        
        if(userVideos!=null && !userVideos.isEmpty())
        {
        	 
        	for(MovieTicket video: userVideos)
        	{
        		List<Long> guids = new ArrayList<Long>();
        		guids.add(video.guid);
        		
        		long guid = video.guid;
        		int projectId = SysManagerFactory.getRecordManager().getProjectID(guid);
        		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
        		
        		Map<String, Object> value = new HashMap<String, Object>();              		

                value.put("name",video.getMovieName());
                value.put("input_guids", guids);
                value.put("project", projectName);
                value.put("id", video.getMovieid()+"");
                
                File file = SysManagerFactory.getMovieManager().getVideo(loginUser, video.ID);
                String link = "";
                String status = ExportStatus.SUBMITTED.name().toLowerCase();
                double fileSize = 0; 
                if(file != null)
                {
                	link = file.getAbsolutePath();
                	status = ExportStatus.SUCCESSFUL.name().toLowerCase();
                	fileSize = file.length();
                } 
                
                Date date=null;
                SimpleDateFormat simpleDate=null;
                date=new Date(video.getCreationTime());
                simpleDate= new SimpleDateFormat("dd/MM/yy h:m:s");
        		String creationDate=simpleDate.format(date);       		
        		
        		date=new Date(video.getExpiryTime());
                simpleDate = new SimpleDateFormat("dd/MM/yy");
        		String expiryDate=simpleDate.format(date);
                		
                value.put("link", link);
                value.put("status", status);
                value.put("isMovie", true);
                value.put("creationDate", creationDate);
                value.put("expiryDate", expiryDate);   
                
                double size = Double.valueOf(formatter.format(fileSize/(1024.0*1024)));
                String formattedSize = size+" MB";
                value.put("size", formattedSize);
                
                ret.add(value);
        	}
        }
    	
        writeJSON(resp, ret);
    }
    
    /**
     * deletes the export record
     * @param req
     * 			requestId : id for the specified export
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteExport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	long requestId = Long.parseLong(getRequiredParam("requestId", req));
    	logger.logp(Level.INFO, "ProjectServlet", "deleteExport", "loginUser=" + loginUser);
    	
    	SysManagerFactory.getExportManager().removeExportedRecord(loginUser, requestId);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * submits the project archival request
     * @param req
     * 			projectId: id for the specified project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void archiveProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	String projectName = getRequiredParam("projectName", req);
    	int projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
    	
    	logger.logp(Level.INFO, "ProjectServlet", "archiveProject", "loginUser=" + loginUser+"projectId="+projectId);
    	
    	SysManagerFactory.getBackupManager().submitProjectForArchival(loginUser, projectId);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * cancels the project archival request
     * @param req
     * 			projectId: id for the specified project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void cancelArchiveProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	String projectName = getRequiredParam("projectName", req);
    	int projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
    	
    	logger.logp(Level.INFO, "ProjectServlet", "cancelArchiveProject", "loginUser=" + loginUser+"projectId="+projectId);
    	
    	SysManagerFactory.getBackupManager().cancelProjectArchiving(loginUser, projectId);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * submits the project restoration request
     * @param req
     * 		projectId: id for the specified project
     * 		location : location from where to restore the project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void restoreProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	String projectName = getRequiredParam("projectName", req);
    	int projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
    	
    	logger.logp(Level.INFO, "ProjectServlet", "restoreProject", "loginUser=" + loginUser+"projectId="+projectId);
    	
    	SysManagerFactory.getBackupManager().submitProjectForRestoration(loginUser, projectId);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * cancels project restoration request
     * @param req
     * 			projectId: specified project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void cancelRestoreProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	String projectName = getRequiredParam("projectName", req);
    	int projectId = SysManagerFactory.getProjectManager().getProject(projectName).getID();
    	
    	logger.logp(Level.INFO, "ProjectServlet", "cancelRestoreProject", "loginUser=" + loginUser+"projectId="+projectId);
    	
    	SysManagerFactory.getBackupManager().cancelProjectRestoring(loginUser, projectId);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * downloads the archive
     * @param req
     * 				requestId : the archive to be downloaded
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadArchive(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	// get record id
        long requestId = Long.parseLong(getRequiredParam("requestId", req));
        
        String filepath = SysManagerFactory.getExportManager().getDownloadURL(loginUser, requestId);

        File archive = new File(filepath);
        if (!archive.exists()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) archive.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + archive.getName() + "\"");
        Util.transferData(new FileInputStream(archive), resp.getOutputStream(), true);
    }
    
    /**
     * Perform a simple text search with the text given
     * 
     * @param req
     * 				q : search query
     * 				advanceQuery : true if query takes care of Solr/Lucene query syntax
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        String text = getRequiredParam(RequestConstants.SEARCH_KEY, req);
        
        boolean adv = false;
        String advQuery = getOptionalParam("advanceQuery", req);
        if("1".equals(advQuery))
        	adv = true;

        SolrSearchManager searchManager = SysManagerFactory.getSolrSearchEngine();
        
        List<SearchResult> solrResults = searchManager.solrSearch(userName, text, null, null, adv);
        Map<String, List<Long>> resultMap = new TreeMap<String, List<Long>>();
        if(solrResults!=null)
        {
        	for(SearchResult result:solrResults)
        	{
        		int projectId = result.projectid;
        		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
        		long guid = result.guid;

				List<Long> guids = new ArrayList<Long>();
				if (resultMap.containsKey(projectName))
				{
					guids = resultMap.get(projectName);
				}
				if(!guids.contains(guid))
					guids.add(guid);

				resultMap.put(projectName, guids);
        	}
        	
        	for(SearchResult result:solrResults)
        	{
        		int projectId = result.projectid;
        		String projectName = SysManagerFactory.getProjectManager().getProject(projectId).getName();
        		long guid = result.guid;
        		
        		for(String matchedField:result.matched_fields)
        		{
        			String nodeName = projectName+"_"+SearchCategories.getSearchCategory(matchedField);
        			
        			List<Long> guids = new ArrayList<Long>();
            		if(resultMap.containsKey(nodeName))
            		{
            			guids = resultMap.get(nodeName);
            		}
            		if(!guids.contains(guid))
    					guids.add(guid);
            		
            		resultMap.put(nodeName, guids);
        		}
        	}
        }

        int count = 0;
//		if (resultMap != null)
//		{
//			for (String key : resultMap.keySet())
//				count += resultMap.get(key).size();
//		}
        if(solrResults != null)
        	count = solrResults.size();
		
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("count", count);
        ret.put("results", resultMap);
        writeJSON(resp, ret);
    }
    
    /**
     * Provides suggestions for the search
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     * @throws SolrServerException 
     */
    public void suggest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //validate request
    	getCurrentUser(req);
        
    	String text = getRequiredParam(RequestConstants.SEARCH_KEY, req);
        
        SolrSearchManager searchManager = SysManagerFactory.getSolrSearchEngine();
        List<String> results=searchManager.suggest(text);
        
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("results", results);
        writeJSON(resp, ret);
    }

    /**
     * Add the given record as a new bookmark.
     * @param  path -> folder path in which this record added as bookmark
     * @param name  -> name of the bookmark 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addBookmark(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getMethod().equals("POST")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not post request");
            return;
        }
        String userName = getCurrentUser(req);
        long recordid = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int projectID = SysManagerFactory.getRecordManager().getProjectID(recordid);
        
        String path = getRequiredParam(RequestConstants.PATH, req);
        logger.logp(Level.INFO, "ProjectServlet", "addBookmark", "Add new bookmark: " + recordid+"path : "+ path);

        BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();

        Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
        if (bookmarkRootFolderID == null) {
            // First time bookmark call. register this user.
            bmManager.registerNewUser(userName, projectID);
        }
        
        Map<String, Object> ret = new HashMap<String, Object>();
        
        if(bmManager.isBookmarkPresent(userName, projectID, path, recordid)){
        	ret.put("success", false);
        	ret.put("message", "BookMark already present in this folder");
        	
        }else{
        	 bmManager.addBookmarks(userName, projectID, path, recordid);
        	 ret.put("success",true);
        	 ret.put("message", "Successfully add to bookmarks");
        }
        
        writeJSON(resp, ret);
    }
    
    /**
     * Add the given record as a new bookmark.
     * @param  path -> folder path in which this record added as bookmark
     * @param name  -> name of the bookmark 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addBookmarks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getMethod().equals("POST")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not post request");
            return;
        }
        String userName = getCurrentUser(req);
        String recordidsString = getRequiredParam(RequestConstants.RECORD_IDS_KEY, req);

        List<Long> recordIDs = gson.fromJson(recordidsString, (new TypeToken<List<Long>>() {
        }).getType());
        
        if(recordIDs!=null && recordIDs.size()>0)
        {
        	int projectID = SysManagerFactory.getRecordManager().getProjectID(recordIDs.get(0));
            
            String path = getRequiredParam(RequestConstants.PATH, req);
            logger.logp(Level.INFO, "ProjectServlet", "addBookmark", "Add new bookmark: " + recordidsString+" path : "+ path);

            BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();

            Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
            if (bookmarkRootFolderID == null) {
                // First time bookmark call. register this user.
                bmManager.registerNewUser(userName, projectID);
            }
            
            Map<String, Object> ret = new HashMap<String, Object>();
            
            for(long recordid: recordIDs)
            {
            	if (bmManager.isBookmarkPresent(userName, projectID, path, recordid))
    			{
    				ret.put("success", false);
    				ret.put("message", "BookMark already present in this folder");
    			}
    			else
    			{
    				bmManager.addBookmarks(userName, projectID, path, recordid);
    				ret.put("success", true);
    				ret.put("message", "Successfully add to bookmarks");
    			}
            }
            
            writeJSON(resp, ret);
        }
    }
    
    /**
     * Delete the  folder in the given path
     * @param path  -> absolute path of the folder to be removed
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeBookmarkFolder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	if (!req.getMethod().equals("POST")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not post request");
            return;
        }
    	 String userName = getCurrentUser(req);
    	 String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
    	 int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
    	 
    	 String path = getRequiredParam(RequestConstants.PATH, req);
         logger.logp(Level.INFO, "ProjectServlet", "removeBookmarkFolder", "removing bookmark Folder at path " + path );
         BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
         Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
         if (bookmarkRootFolderID == null) {
             // First time bookmark call. register this user.
             bmManager.registerNewUser(userName, projectID);
         }
         bmManager.deleteBookmarkFolder(userName, projectID, path);
         
    }
    /**
     *  add  Folder to the given path
     * @param req
     * 				folderName : name of the new folder
     * 				path : path to which new folder is to be added
     * 				projectName : name of the project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    
    public void addBookmarkFolder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	if (!req.getMethod().equals("POST")) {
    		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not post request");
    		return;
    	}
    	String userName = getCurrentUser(req);
    	String folderName = getRequiredParam(RequestConstants.FOLDER_NAME, req);
    	String path = getRequiredParam(RequestConstants.PATH, req);
    	String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
   	 	int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
   	 
    	logger.logp(Level.INFO, "ProjectServlet", "addBookmarkFolder", "Adding new bookmark Folder to the path :  " + path +" FolderName :" + folderName);
    	BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
    	Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
    	if (bookmarkRootFolderID == null) {
    		// First time bookmark call. register this user.
    		bmManager.registerNewUser(userName, projectID);
    	}
    	Map<String, Object> ret = new HashMap<String, Object>();

    	if(isBookmarkFolderPresent(userName, projectID, path, folderName)){

    		ret.put("success", false);
    		ret.put("message", "Folder already present");
    	}else{
    		bmManager.addBookmarkFolder(userName, projectID, path, folderName);
    		ret.put("success",true);
    		ret.put("message", "Folder created Successfully");
    	}

    	writeJSON(resp, ret);


    }
    /**
     * Checks the weather the folderName exist or not 
     * @param userName  
     * @param path
     * @param folderName
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public boolean isBookmarkFolderPresent(String userName, int projectID, String path,String folderName)throws ServletException, IOException
    {
    	logger.logp(Level.INFO, "ProjectServlet", "isBookmarkFolderPresent", " checking folder exist in this path: " + path +" with folder name: " + folderName);
    	BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
    	List<String>subFolders = bmManager.getSubFolders(userName, projectID, path);
    	if(subFolders!=null &&subFolders.size()>0){
    		for(String folderPath:subFolders){
    			String name = folderPath.substring(folderPath.lastIndexOf(BookmarkManager.SEPARATOR) + 1);
    			if(name.equalsIgnoreCase(folderName)){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    /**
     * Remove the given record from bookmarks.
     * 
     * @param req
     * 				path : the path of the bookmark folder
     * 				recordid : id pf the record to be removed
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeBookmark(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        if (!req.getMethod().equals("POST")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not post request");
            return;
        }
        String userName = getCurrentUser(req);
        String path = getRequiredParam(RequestConstants.PATH, req);
        long recordid = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        
        int projectID = SysManagerFactory.getRecordManager().getProjectID(recordid);
        
        logger.logp(Level.INFO, "ProjectServlet", "removeBookmark", " Path : " + path + "record id:"+ recordid);

        BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
        bmManager.removeBookmark(userName, projectID, path, recordid);
        writeJSON(resp, successResponse);
    }

    /**
     * Check if the given record is bookmarked or not
     * 
     * @param req
     * 				recordid : record identifier
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void isBookmark(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // validate user
    	getCurrentUser(req);
        
    	long recordid = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        logger.logp(Level.INFO, "ProjectServlet", "isBookmark", "Is bookmark: " + recordid);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put("isBookmark", false);
        writeJSON(resp, ret);
    }
    
    /**
     * returns bookmark folder structure under given path NOT recursive
     * @param req
     * 				projectName : name of the project
     * 				path : path of the bookmark folder
     * @param resp
     * 				bookmark tree object
     * @throws ServletException
     * @throws IOException
     */
    public void getBookmarkFolders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String path = getRequiredParam(RequestConstants.BOOKMARK_PATH, req);
        
        Project project = SysManagerFactory.getProjectManager().getProject(projectName);
        
        if(project == null)
        {
        	Map<String, Object> recordData = new HashMap<String, Object>();
            writeJSON(resp, recordData);
            return;
        }
        
        int projectID = project.getID();
      	 
        logger.logp(Level.INFO, "ProjectServlet", "getBookMarkFolders", "Get bookmark folders for user: " + userName);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

        BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();

        Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
        if (bookmarkRootFolderID == null) {
            // First time bookmark call. register this user.
            bmManager.registerNewUser(userName, projectID);
        }

        String rootFolderPath = path;
        if(!path.startsWith(BookmarkManager.SEPARATOR))
        	rootFolderPath = BookmarkManager.SEPARATOR+rootFolderPath;
     
        int recordCount = bmManager.getBookmarksCount(userName, projectID, rootFolderPath);
        
        Map<String, Object> next = new HashMap<String, Object>();
        next.put("bookmarkName",projectName);
        next.put("leaf", false);
        next.put("recordCount", recordCount);
        next.put("children",getChildren(projectID, rootFolderPath, userName));
        next.put("text", projectName+" ["+recordCount+"]");
        next.put("qtip", "number of records "+recordCount);
        ret.add(next);
        writeJSON(resp, ret);
    }

    /**
     * retuns the folder structure(recursively) of the current user in JSON format
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getRecursiveBookmarkFolders(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        Project project = SysManagerFactory.getProjectManager().getProject(projectName);
        if(project == null)
        {
        	Map<String, Object> recordData = new HashMap<String, Object>();
            writeJSON(resp, recordData);
            return;
        }
        
   	 	int projectID = project.getID();
   	 
        logger.logp(Level.INFO, "ProjectServlet", "getBookMarkFolders", "Get bookmark folders for user: " + userName);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

        BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();

        Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
        if (bookmarkRootFolderID == null) {
            // First time bookmark call. register this user.
            bmManager.registerNewUser(userName, projectID);
        }

        String rootFolderPath = projectName;
        rootFolderPath = BookmarkManager.SEPARATOR+rootFolderPath;
     
        Map<String, Object> next = new HashMap<String, Object>();
        next.put("text","BookMarks");
        next.put("leaf", false);
        next.put("children",getChildrenRecursive(projectID, rootFolderPath,userName) );
        ret.add(next);
        writeJSON(resp, ret);
    }
    /**
     * returns subfolders<recursively having their own subfolders>  for a given path 
     * @param path
     * @param userName
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  List<Map<String, Object>>  getChildFoldersRecursive(int projectID, String path,String userName) throws ServletException, IOException
    {
    	List<Map<String, Object>>  items=new ArrayList<Map<String, Object>>();
    	BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
    	List<String> subfolders = bmManager.getSubFolders(userName, projectID, path);
    	
    	if(subfolders!=null && subfolders.size()>0){
    		for (String folderPath: subfolders) {
    			String name =folderPath.substring(folderPath.lastIndexOf(BookmarkManager.SEPARATOR) + 1);
    			Map<String, Object> next = new HashMap<String, Object>();
    			next.put("text",name);
    			next.put("leaf", false);
    			List<Map<String, Object>> children = getChildrenRecursive(projectID, folderPath,userName);
    			if(children.size()>0){
    				next.put("children", children);
    			}
    			items.add(next);
    		}
    	}
    	return items;
    }
    
    /**
     * returns (subfolders+ leafs)<recursively having their own (subfolders+leafs)>  for a given path 
     * @param path
     * @param userName
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  List<Map<String, Object>>  getChildrenRecursive(int projectID, String path,String userName)throws ServletException,
    IOException{
    	List<Map<String, Object>> children=new ArrayList<Map<String, Object>>() ;
    	
    	List<Map<String, Object>> subfolders = getChildFoldersRecursive(projectID, path,userName);
//    	List<Map<String, Object>> childBookmarks = getChildBookMarks(projectID, path,userName);
    	if(subfolders.size()>0)
    		children.addAll(subfolders);
//    	if(childBookmarks.size()>0)
//    		children.addAll(childBookmarks);
    	return children;
    }
    
    /**
     * returns subfolders for a given path NOT recursive 
     * @param path
     * @param userName
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  List<Map<String, Object>>  getChildren(int projectID, String path,String userName)throws ServletException, IOException
    {
    	logger.logp(Level.INFO, "ProjectServlet", "getChildren", "path: " + path);
    	
    	List<Map<String, Object>>  items=new ArrayList<Map<String, Object>>();
    	
    	BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
    	List<String> subfolders = bmManager.getSubFolders(userName, projectID, path);
    	
    	if(subfolders!=null && subfolders.size()>0)
    	{
    		for (String folderPath: subfolders) 
    		{
    			String name = folderPath.substring(folderPath.lastIndexOf(BookmarkManager.SEPARATOR) + 1);
    			
    			int recordCount = bmManager.getBookmarksCount(userName, projectID, folderPath);
    			
    			Map<String, Object> next = new HashMap<String, Object>();
    			next.put("bookmarkName",name);
    			next.put("recordCount", recordCount);
    			next.put("leaf", false);
    			next.put("qtip", "number of records "+recordCount);
    			next.put("text", name+" ["+recordCount+"]");
    			
    			items.add(next);
    		}
    	}
    	
    	return items;
    }
    
    /**
     * returns bookmarks (leafs) present in a given path 
     * @param path
     * @param userName
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public  List<Map<String, Object>>  getChildBookMarks(int projectID, String path,String userName) throws ServletException, IOException
    {
    	List<Map<String, Object>> items=new ArrayList<Map<String, Object>>() ;
    	
    	BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
    	ProjectManager projectManager = SysManagerFactory.getProjectManager();
        RecordManager recordManager = SysManagerFactory.getRecordManager();
        
        List<Object> object = bmManager.getBookmarks(userName, projectID, path);
        if(object!=null && object.size()>0)
        {
        	long[] bookmarks = (long[]) object.get(1);
        	String[] bookmarkNames = (String[]) object.get(0);
        	Record record;
        	Project project;

        	if(bookmarks!=null)
        	{
        		for(int i=0;i<bookmarks.length;i++)
        		{
        			Map<String, Object> next = new HashMap<String, Object>();
        			next.put("text",bookmarkNames[i]);
        			next.put("leaf",true);
        			next.put("recordId", bookmarks[i]);


        			record = recordManager.findRecord(userName, bookmarks[i]);
        			project = projectManager.getProject(record.projectID);
        			next.put("projectName",project.getName());

        			items.add(next);
        		}

        	}
        }
    	return items;
    }
    /**
     * returns the recently used folders for the current user .
     *    currently it just returns subfolders of root folder . Need to discuss 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getRecentFolders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
   	 	int projectID = SysManagerFactory.getProjectManager().getProject(projectName).getID();
   	 	
    	List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    	BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
    	String userName = getCurrentUser(req);
    	 logger.logp(Level.INFO, "ProjectServlet", "getRecentFolders", "Getting recent folders for user: " + userName);

        Long bookmarkRootFolderID = bmManager.getBookmarkRootFolderID(userName, projectID);
        if (bookmarkRootFolderID == null) {
            // First time bookmark call. register this user.
            bmManager.registerNewUser(userName, projectID);
        }

        String rootFolderPath = projectName;
        Map<String, Object> next = new HashMap<String, Object>();
        next.put("folderName",rootFolderPath);
		
		
        rootFolderPath = BookmarkManager.SEPARATOR+rootFolderPath;
    	next.put("folderPath",rootFolderPath);
    	
    	ret.add(next);
    	
        List<String> subfolders = bmManager.getSubFolders(userName, projectID, rootFolderPath);
        
        if(subfolders!=null && subfolders.size()>0){
        	for (String folderPath: subfolders) {
        		String name =folderPath.substring(folderPath.lastIndexOf(BookmarkManager.SEPARATOR) + 1);
        		 next = new HashMap<String, Object>();
        		next.put("folderName",name);
        		next.put("folderPath",folderPath);
        		ret.add(next);
        	}
        }
        next = new HashMap<String, Object>();
        next.put("folderName","Choose New Folder");
        next.put("folderPath","New");
        ret.add(next);
        writeJSON(resp, ret);
         
    }
    /**
     * Get records for a bookmark folder
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getRecordsForBookmarkFolder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String bookmarkPath = getRequiredParam(RequestConstants.BOOKMARK_PATH, req);
        String isLeafKey = getRequiredParam(RequestConstants.BOOKMARK_IS_LEAF, req);
        
        boolean isLeaf = Boolean.valueOf(isLeafKey);
        if(isLeaf)
        {
        	getRecordDataForBookmark(req, resp);
        }
        else
        {
        	ProjectManager projectManager = SysManagerFactory.getProjectManager();
            RecordManager recordManager = SysManagerFactory.getRecordManager();
            BookmarkManager bmManager = SysManagerFactory.getBookmarkManager();
            
            Project project = projectManager.getProject(projectName);
            if(project == null)
            {
            	Map<String, Object> recordData = new HashMap<String, Object>();
                writeJSON(resp, recordData);
                return;
            }
            int projectID = project.getID();
            logger.logp(Level.INFO, "ProjectServlet", "getRecordsForBookmarkFolder", "Get records for: " + projectName);

            Map<String, Object> recordData = createEmptyRecordMap(userName, projectName);
            
            List<Object> object = bmManager.getBookmarks(userName, projectID, bookmarkPath);
            if(object != null)
            {
            	long[] bookmarks = (long[]) object.get(1);
                logger.logp(Level.INFO, "ProjectServlet", "getRecordsForBookmarkFolder", "# bookmarks found = " + bookmarks.length);
                
                Set<Long> records = new LinkedHashSet<Long>();
                if (bookmarks != null && bookmarks.length > 0) {
                    for (long bookmark : bookmarks) {
                        Record record = recordManager.findRecord(userName, bookmark);
                        project = projectManager.getProject(record.projectID);
                        if (project.getName().equals(projectName))
                            records.add(bookmark);
                    }
                }
                
                recordData = getRecordData(userName, projectName, records, records.size());
            }
            
            writeJSON(resp, recordData);
        }
    }
    
    public void getRecordDataForBookmark(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
    IOException {
    	String userName = getCurrentUser(req);
    	String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
    	Long recordId = Long.parseLong( getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
    	logger.logp(Level.INFO, "ProjectServlet", "getRecordDataForBookmark", "Get record Data for Record -ID : " + recordId+ "For the projectName " + projectName);

    	ProjectManager projectManager = SysManagerFactory.getProjectManager();
    	RecordManager recordManager = SysManagerFactory.getRecordManager();

    	Set<Long> records = new LinkedHashSet<Long>();

    	Record record = recordManager.findRecord(userName, recordId);
    	Project project = projectManager.getProject(record.projectID);
    	if (project.getName().equals(projectName))
    		records.add(recordId);

    	Map<String, Object> recordData = getRecordData(userName, projectName, records, records.size());
    	writeJSON(resp, recordData);
}
    /**
     * Converting the data for easy consumption by extjs
     * @param record 
     * 
     * @param data
     * @param channelCount
     * @return
     */
    private static List<Map<String, Object>> getImagePixelDataMap(Record record, Map<Integer, ImagePixelData> data, int channelCount) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        Map<String, Object> next = new HashMap<String, Object>();
        next.put("field", "X Coordinate");
        next.put("id", mapList.size() + "");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getX());
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Y Coordinate");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getY());
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Z Coordinate");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getZ());
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Elapsed Time");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getElapsed_time()+" "+record.getElapsedTimeUnit());
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Exposure Time");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getExposureTime()+" "+record.getExposureTimeUnit());
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Timestamp");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getTimestamp());
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Slice Number");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getDimension().sliceNo);
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Frame Number");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getDimension().frameNo);
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Channel Number");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getDimension().channelNo);
        mapList.add(next);

        next = new HashMap<String, Object>();
        next.put("id", mapList.size() + "");
        next.put("field", "Site Number");
        for (int i = 0; i < channelCount; ++i)
            next.put(i + "", data.get(i).getDimension().siteNo);
        mapList.add(next);

        return mapList;
    }

    /**
     * Access acquisition client
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void launchAcqClient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);

        logger.logp(Level.INFO, "ComputeServlet", "generateAuthCode", "access acq client: " + loginUser);
        long expiryTime = System.currentTimeMillis() + Constants.getAcqClientAuthCodeValidity() * 24 * 3600 * 1000;

        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        String authCode = authManager.generateAuthCode(loginUser, "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI",
                Service.values(), expiryTime, null);
        String url = ACQ_CLIENT_URL + "?authcode=" + authCode + "&version="+Constants.getWebApplicationVersion();
        resp.sendRedirect(url);
    }

    /**
     * Launch 3d view
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void launch3dView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);
        long recordid = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));
        int frameNumber = Integer.parseInt(getRequiredParam(RequestConstants.FRAME_NUMBER_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));

        long expiryTime = System.currentTimeMillis() + 2 * 3600 * 1000; // TODO
        logger.logp(Level.INFO, "ComputeServlet", "launch3dView", "launch 3d view: " + loginUser + " record: "
                + recordid + " frame: " + frameNumber + " site: " + siteNumber);

        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        String authCode = authManager.generateAuthCode(loginUser, "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI",
                Service.values(), expiryTime, null);
        String url = THREED_CLIENT_URL + "?authcode=" + authCode + "&guid=" + recordid + "&frame=" + frameNumber
                + "&site=" + siteNumber;
        resp.sendRedirect(url);
    }
    
    /**
     * Get status of imports
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getImportStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
    	String statusRequested=getRequiredParam("status", req);
    	
    	logger.logp(Level.INFO, "ProjectServlet", "getImportStatus", "projectName="+projectName+" status requested="+statusRequested);
    	List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    	Map<String,Object> status= null;
    	
    	List<Import> imports=null;
    	
    	if(statusRequested.equals("complete")){
    		imports=SysManagerFactory.getImportManager().getCompleteImports(projectName);
    		Collections.sort(imports, new Comparator<Import>(){
				@Override
				public int compare(Import arg0, Import arg1) {
					if(arg0.getRequestTime()<arg1.getRequestTime())
						return 1;
					else
						return -1;
				}
    		});
    	}
    	else{
    		imports=SysManagerFactory.getImportManager().getPendingImports(projectName);
    	}
    	
  
    	int count = 0;
		if (imports != null && imports.size() > 0)
		{
			count = imports.size();
			
			int start = 0;
			int end = 0;
			
			String startString = getOptionalParam("start", req);
			logger.logp(Level.INFO, "ProjectServlet", "getImportStatus", "start="+startString);
			try
			{
				start = Integer.parseInt(startString);
			}
			catch (Exception e)
			{
				start = 0;
			}
			
			String limitString = getOptionalParam("limit", req); 
			logger.logp(Level.INFO, "ProjectServlet", "getImportStatus", "limit="+limitString);
			try
			{
				int limit = Integer.parseInt(limitString);
				
				end = start+limit;
				if(end>=imports.size())
					end = imports.size();
			}
			catch (Exception e)
			{}
			
			List<Import> paginatedImports = imports.subList(start, end);
    	
	    	if(paginatedImports!=null){
	    		for(Import im: paginatedImports){
	    			status= new HashMap<String,Object>();
	    			logger.logp(Level.INFO, "ProjectServlet", "getImportStatus", "user="+im.getRequest().getActor()+" status="+im.getJobStatus());
	    			status.put("status", im.getJobStatus());
	    			status.put("requestTime", new Date(im.getRequestTime()));
	    			status.put("user",im.getRequest().getActor());
	    			status.put("location", im.getRequest().getSourceDirectory());
	    			ret.add(status);
	    		}   		
	    	}
		}
		writeJSON(resp, Util.createMap("items",ret, "total", count));
    }
    
	public void getWebApplicationVersion(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		Application application = getWebApplication();
		writeJSON(resp, Util.createMap("name", application.getName(), "version", application.getVersion()));
	}
	
	
}