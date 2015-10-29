/*
 * AnnotationServlet.java
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

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.MetaData;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.models.LegendField;
import com.strandgenomics.imaging.iengine.models.LegendLocation;
import com.strandgenomics.imaging.iengine.models.LegendType;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.system.ProjectManager;
import com.strandgenomics.imaging.iengine.system.RecordManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.UserPreference;

/**
 * {@link Servlet} to handle user/system annotation related requests
 * 
 * @author santhosh
 * 
 */
public class AnnotationServlet extends ApplicationServlet {

    private static final String RECORDS_KEY = "records";

    /**
     * Number of unique values to return for a field
     */
    private static final int MAX_FIELD_VALUES = 20;

    /**
     * 
     */
    private static final long serialVersionUID = -6265487319025302887L;

    /**
     * Get all the fields applicable for a record. This is a combination of all
     * the fixed fields of the record and fields specific for a project.
     * 
     * URL parameters: 
     * projectName : name of the project
     * 
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getProjectFields(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        ProjectManager pm = SysManagerFactory.getProjectManager();

        Collection<SearchColumn> recordFixedFields = pm.getRecordFixedFields(userName, projectName);

        addFieldsToList(recordFixedFields, ret, false);
        Collection<SearchColumn> annotationFields = pm.getUserAnnotationFields(userName, projectName);

        addFieldsToList(annotationFields, ret, true);
        writeJSON(resp, ret);
    }

    /**
     * Get the fields chosen by a user in a project
     * 
     * URL parameters: 
     * projectName : name of the project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getFieldsChosen(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);

        List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> availableList = new ArrayList<Map<String, Object>>();

        Collection<SearchColumn> allFields = SysManagerFactory.getProjectManager().getNavigableFields(userName,
                projectName);

        List<SearchColumn> spreadsheetColumns = SysManagerFactory.getUserPreference().getSpreadSheetColumns(userName,
                projectName);
        if (spreadsheetColumns == null)
            spreadsheetColumns = ProjectServlet.getDefaultSpreadsheetColumns();

        List<SearchField> availableFields = new ArrayList<SearchField>();
        for (SearchField field : allFields) {
            if (spreadsheetColumns.indexOf(field) == -1)
                availableFields.add(field);
        }
        addFieldsToList(spreadsheetColumns, userList, false);
        addFieldsToList(availableFields, availableList, false);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("user", userList);
        ret.put("available", availableList);

        writeJSON(resp, ret);
    }
    
    /**
     * Get the fields chosen by a user in a project
     * 
     * URL parameters: 
     * projectName : name of the project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getLegendFieldsChosen(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);

        List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> availableList = new ArrayList<Map<String, Object>>();

        List<LegendField> allFields = SysManagerFactory.getUserPreference().getAllLegendFields();

        List<LegendField> chosenFields = SysManagerFactory.getUserPreference().getLegendFields(userName);
        if(chosenFields == null)
        	chosenFields = new ArrayList<LegendField>();

        List<LegendField> availableFields = new ArrayList<LegendField>();
        for (LegendField field : allFields) {
            if (chosenFields.indexOf(field) == -1)
                availableFields.add(field);
        }
        addFieldsToLegendList(chosenFields, userList, false);
        addFieldsToLegendList(availableFields, availableList, false);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("user", userList);
        ret.put("available", availableList);

        writeJSON(resp, ret);
    }
    
    private void addFieldsToLegendList(List<LegendField> chosenFields, List<Map<String, Object>> userList, boolean b)
	{
    	if (chosenFields == null || chosenFields.size() == 0)
            return;

        for (LegendField field : chosenFields) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put("name", field.legendName);
            entry.put("type", field.type);
            entry.put("isUserField", b);
            
            userList.add(entry);
        }
	}

	/**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void setLegendsChosen(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String actorLogin = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);
        
        String chosenFieldsString = getRequiredParam("chosenFields", req);
        List<String> chosenFields = gson.fromJson(chosenFieldsString, new TypeToken<List<String>>() {
        }.getType());
        
        List<LegendField> legendFields = convertToLegendFields(chosenFields);
        
        SysManagerFactory.getUserPreference().setLegendFields(actorLogin, legendFields);

        logger.logp(Level.INFO, "AnnotationServlet", "setLegendsChosen", "Chosen fields: " + chosenFields);
        
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getLegendLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);
        
        LegendLocation location = SysManagerFactory.getUserPreference().getLegendLocation(userName);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("xlocation", location.getX());
        ret.put("ylocation", location.getY());

        writeJSON(resp, ret);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void setLegendLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String actorLogin = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);
        
        String xLocation = req.getParameter("xLocation");
        String yLocation = req.getParameter("yLocation");
        
        LegendLocation location = LegendLocation.getLocation(xLocation, yLocation);
        
        SysManagerFactory.getUserPreference().setLegendLocation(actorLogin, location);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    private List<LegendField> convertToLegendFields(List<String> fieldNames)
    {
    	List<LegendField> legends = new ArrayList<LegendField>();
    	for(String name:fieldNames)
    	{
    		LegendField field = new LegendField(name, LegendType.IMAGE_METADATA);
    		legends.add(field);
    	}
    	
    	return legends;
    }

    /**
     * Get all the user defined fields for the given project
     * 
     * URL parameters: 
     * projectName : name of the project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getUserFields(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RequestConstants.PROJECT_NAME_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String projectName = req.getParameter(RequestConstants.PROJECT_NAME_KEY);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        Collection<SearchColumn> annotationFields = SysManagerFactory.getProjectManager().getUserAnnotationFields(
                userName, projectName);
        addFieldsToList(annotationFields, ret, true);
        writeJSON(resp, ret);
    }

    static void addFieldsToList(Collection<? extends SearchField> fields, List<Map<String, Object>> list,
            Boolean isUserField) {
        if (fields == null || fields.size() == 0)
            return;

        for (SearchField field : fields) {
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put("name", field.getField());
            entry.put("type", field.getType().name());
            entry.put("isUserField", isUserField);
            switch (field.getType()) {
            case Integer:
            case Real:
                entry.put("xtype", "numbercolumn");
                break;
            default:
                break;
            }
            list.add(entry);
        }
    }

    /**
     * Add new annotation for a set of records. Requires name, type and value
     * for the annotation.
     * 
     * URL parameters: 
     * records : gson list representation of records ids
     * name : name of the user annotation
     * type : type of the user annotation
     * value : value of the user annotation
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addAnnotation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RECORDS_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String recordsJSON = req.getParameter(RECORDS_KEY);
        List<Long> records = gson.fromJson(recordsJSON, new TypeToken<List<Long>>() {
        }.getType());

        if (req.getParameter("name") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String name = req.getParameter("name");
        if (req.getParameter("type") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String typeString = req.getParameter("type");
        if (req.getParameter("value") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String valueString = req.getParameter("value");
        List<MetaData> metadata = new ArrayList<MetaData>();
        AnnotationType type = AnnotationType.valueOf(typeString);
        switch (type) {
        case Integer:
            metadata.add(new MetaData(name, Long.parseLong(valueString)));
            break;
        case Real:
            metadata.add(new MetaData(name, Double.parseDouble(valueString)));
            break;
        case Text:
            metadata.add(new MetaData(name, valueString));
            break;
        case Time:
            metadata.add(new MetaData(name, new Timestamp(Long.parseLong(valueString))));
            break;
        }
        RecordManager recordManager = SysManagerFactory.getRecordManager();
        
        try{
        	
	        for (long recordid : records) {
	            recordManager.addUserAnnotation(getWebApplication(), userName, null, recordid, metadata);
	        }
	        Map<String, Object> ret = new HashMap<String, Object>();
	        ret.put("success", true);
	        writeJSON(resp, ret);
	        
        }
        catch (ImagingEngineException e)
		{
			logger.logp(Level.WARNING, "AnnotationServlet", "addAnnotation", "You dont have permission to add Annotation ", e);
			writeFailure(resp, "You dont have permission to add Annotation");
		}	
    }

    /**
     * Update user annotation with a specified name to a new value for a set of
     * records.
     * 
     * URL parameters: 
     * records : gson list representation of records ids
     * name : name of the user annotation
     * type : type of the user annotation
     * value : value of the user annotation
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void updateAnnotation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        if (req.getParameter(RECORDS_KEY) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String recordsJSON = req.getParameter(RECORDS_KEY);
        List<Long> records = gson.fromJson(recordsJSON, new TypeToken<List<Long>>() {
        }.getType());

        if (req.getParameter("name") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String name = req.getParameter("name");
        if (req.getParameter("type") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String typeString = req.getParameter("type");
        if (req.getParameter("value") == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String valueString = req.getParameter("value");

        Object value = null;

        AnnotationType type = AnnotationType.valueOf(typeString);
        switch (type) {
        case Integer:
            value = Long.parseLong(valueString);
            break;
        case Real:
            value = Double.parseDouble(valueString);
            break;
        case Text:
            value = valueString;
            break;
        case Time:
            value = new Timestamp(Long.parseLong(valueString));
            break;
        }

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        
        try{
	        for (long recordid : records) {
	            recordManager.setUserAnnotation(getWebApplication(), userName, null, recordid, name, value);
	        }
	        Map<String, Object> ret = new HashMap<String, Object>();
	        ret.put("success", true);
	        writeJSON(resp, ret);
        }
        catch (ImagingEngineException e)
		{
			logger.logp(Level.WARNING, "AnnotationServlet", "updateAnnotation", "You dont have permission to edit Annotation ", e);
			writeFailure(resp, "You dont have permission to edit Annotation");
		}

    }

    /**
     * Set the chosen fields for a user
     * 
     * URL Parameters: 
     * projectName : name of the project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void setFieldsChosen(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);

        String chosenFieldsString = getRequiredParam("chosenFields", req);
        List<String> chosenFields = gson.fromJson(chosenFieldsString, new TypeToken<List<String>>() {
        }.getType());

        Map<String, SearchColumn> allFieldsMap = new HashMap<String, SearchColumn>();

        Collection<SearchColumn> allFields = SysManagerFactory.getProjectManager().getNavigableFields(userName,
                projectName);
        for (SearchColumn fixedField : allFields)
            allFieldsMap.put(fixedField.getField(), fixedField);

        logger.logp(Level.INFO, "AnnotationServlet", "setFieldsChosen", "Chosen fields: " + chosenFields);

        List<SearchColumn> chosenOnes = new ArrayList<SearchColumn>();
        for (String fieldName : chosenFields) {
            if (allFieldsMap.containsKey(fieldName))
                chosenOnes.add(allFieldsMap.get(fieldName));
        }

        logger.logp(Level.INFO, "AnnotationServlet", "setFieldsChosen", "Chosen columns: " + chosenOnes);

        UserPreference userPreference = SysManagerFactory.getUserPreference();
        userPreference.setSpreadSheetColumns(userName, projectName, chosenOnes);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }

    /**
     * Get unique set of values for a given column. The number of results
     * returned is bounded by the constant {@link #MAX_FIELD_VALUES}
     * 
     * URL Parameter:
     * fieldName : name of the annotation
     * fieldType : type of the annotation
     * projectName : name of the projects
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getFieldValues(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String fieldName = getRequiredParam(RequestConstants.FIELD_NAME_KEY, req);
        String fieldType = getRequiredParam(RequestConstants.FIELD_TYPE_KEY, req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);

        logger.logp(Level.INFO, "AnnotationServlet", "getFieldValues", "Get unique values for field: " + fieldName
                + " of type: " + fieldType);

        AnnotationType type = AnnotationType.valueOf(fieldType);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (type.equals(AnnotationType.Time)) {
            // No prompted values for time field.
            writeJSON(resp, ret);
            return;
        }

        ProjectManager pm = SysManagerFactory.getProjectManager();
        Set<MetaData> values = pm.getUniqueUserAnnotationValues(user, projectName, fieldName, type, MAX_FIELD_VALUES);
        for (MetaData metaData : values) {
            ret.add(Util.createMap("value", metaData.stringValue()));
        }
        writeJSON(resp, ret);
    }

    /**
     * Get user annotations for a record
     * 
     * URL Parameter:
     * recordid : id of the record
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getUserAnnotations(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String user = getCurrentUser(req);
        long recordID = Long.parseLong(getRequiredParam(RequestConstants.RECORD_ID_KEY, req));

        logger.logp(Level.INFO, "AnnotationServlet", "getUserAnnotations", "Get user annotations for record : "
                + recordID);

        RecordManager recordManager = SysManagerFactory.getRecordManager();
        List<MetaData> userAnnotations = recordManager.getUserAnnotations(user, recordID);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (userAnnotations != null) {
            for (MetaData annotation : userAnnotations) {
                Map<String, Object> next = Util.createMap("name", annotation.getName(), "value",
                        annotation.stringValue());
                ret.add(next);
            }
        }
        writeJSON(resp, ret);
    }
}
