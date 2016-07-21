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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.icore.SearchCondition;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.system.ProjectManager;
import com.strandgenomics.imaging.iengine.system.SearchEngine;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Servlet which serves navigator related queries
 * 
 * @author santhosh
 * 
 */
public class NavigatorServlet extends ApplicationServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get children call
     * 
     * @param req
     * 				projectName : name of the selected project
     * 				conditions : list of conditions
     * @param resp
     * 				gson representation of {@link com.strandgenomics.imaging.icore.NavigationBin}
     * @throws ServletException
     * @throws IOException
     */
    public void getChildren(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String conditionsString = getRequiredParam(RequestConstants.CONDITIONS_KEY, req);

        List<Map<String, String>> conditionsList = gson.fromJson(conditionsString,
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        Set<SearchCondition> conditions = ParseUtil.decodeConditions(conditionsList);

        logger.logp(Level.INFO, "NavigatorServlet", "getChildren", "Conditions: " + conditions);
        
        List<SearchColumn> navigatorColumns = SysManagerFactory.getUserPreference().getNavigationColumns(userName,
                projectName);
        if (navigatorColumns == null || navigatorColumns.size() <= conditions.size()) {
            // No more columns to navigate on. Return empty bins
            logger.logp(Level.INFO, "NavigatorServlet", "getChildren", "No more navigable fields. returning empty bins");
            List<NavigationBin> ret = new ArrayList<NavigationBin>();
            writeJSON(resp, ret);
            return;
        }
        
        logger.logp(Level.INFO, "NavigatorServlet", "getChildren", "Navigator columsn: " + navigatorColumns);
        SearchColumn nextColumn = navigatorColumns.get(conditions.size());
        String fieldName = nextColumn.getField();

        SearchEngine searchEngine = SysManagerFactory.getSearchEngine();
        List<NavigationBin> navigationBins = searchEngine.getNavigationBins(userName, projectName, conditions,
                fieldName, null, null);

        logger.logp(Level.INFO, "NavigatorServlet", "getChildren", "Navigation bins: " + navigationBins);

        writeJSON(resp, navigationBins);
    }

    /**
     * Get siblings for a previously binned node
     * 
     * @param req
     * 				projectName : name of the project
     * 				fieldName : name of the selected field
     * 				fieldType : type of the selected field
     * 				conditions : list of navigator conditionds
     * 				min : min value
     * 				max : max value
     * @param resp
     * 				gson representation of {@link com.strandgenomics.imaging.icore.NavigationBin}
     * @throws ServletException
     * @throws IOException
     */
    public void getSiblings(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);
        String fieldName = getRequiredParam(RequestConstants.FIELD_NAME_KEY, req);
        AnnotationType fieldType = AnnotationType.valueOf(getRequiredParam(RequestConstants.FIELD_TYPE_KEY, req));
        String conditionsString = getRequiredParam(RequestConstants.CONDITIONS_KEY, req);
        String minString = getRequiredParam(RequestConstants.MIN_KEY, req);
        String maxString = getRequiredParam(RequestConstants.MAX_KEY, req);

        List<Map<String, String>> conditionsList = gson.fromJson(conditionsString,
                new TypeToken<List<Map<String, String>>>() {
                }.getType());
        Set<SearchCondition> conditions = ParseUtil.decodeConditions(conditionsList);
        Object min = minString == null ? null : ParseUtil.decodeAnnotationObject(minString, fieldType);
        Object max = maxString == null ? null : ParseUtil.decodeAnnotationObject(maxString, fieldType);

        logger.logp(Level.INFO, "NavigatorServlet", "getChildren", "Conditions: " + conditions + " min: " + min
                + " max: " + max);

        SearchEngine searchEngine = SysManagerFactory.getSearchEngine();
        List<NavigationBin> navigationBins = searchEngine.getNavigationBins(userName, projectName, conditions,
                fieldName, min, max);

        logger.logp(Level.INFO, "NavigatorServlet", "getChildren", "Navigation bins: " + navigationBins);

        writeJSON(resp, navigationBins);
    }

    /**
     * Get the fields chosen by a user in a project for navigation
     * 
     * @param req
     * 				projectName : name of the project
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getFieldsChosen(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String userName = getCurrentUser(req);
        String projectName = getRequiredParam(RequestConstants.PROJECT_NAME_KEY, req);

        logger.logp(Level.INFO, "NavigatorServlet", "getFieldsChosen", "Get fields chosen for project: "+projectName);
        
        List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> availableList = new ArrayList<Map<String, Object>>();

        Collection<SearchColumn> allFields = SysManagerFactory.getProjectManager().getNavigableFields(userName, projectName);

        List<SearchColumn> navigatorColumns = SysManagerFactory.getUserPreference().getNavigationColumns(userName, projectName);
        
        if (navigatorColumns == null) 
        {
            // By default navigator columsn list is empty
            navigatorColumns = new ArrayList<SearchColumn>();
        }

        List<SearchField> availableFields = new ArrayList<SearchField>();
        for (SearchField field : allFields) {
            if (navigatorColumns.indexOf(field) == -1)
                availableFields.add(field);
        }
        AnnotationServlet.addFieldsToList(navigatorColumns, userList, false);
        AnnotationServlet.addFieldsToList(availableFields, availableList, false);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("user", userList);
        ret.put("available", availableList);

        writeJSON(resp, ret);
    }

    /**
     * Set the chosen fields for a user
     * 
     * @param req
     * 				projectName : selected project
     * 				chosenFields : selected navigation fields
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
        logger.logp(Level.INFO, "NavigatorServlet", "setFieldsChosen", "Chosen fields: " + chosenFields);

        List<SearchColumn> chosenOnes = getColumnsFromNames(userName, projectName, chosenFields);
        logger.logp(Level.INFO, "NavigatorServlet", "setFieldsChosen", "Chosen columns: " + chosenOnes);

        SysManagerFactory.getUserPreference().setNavigationColumns(userName, projectName, chosenOnes);

        writeJSON(resp, successResponse);
    }

    /**
     * Convert a list of column names to {@link SearchColumn} instances
     * 
     * @param userName
     * @param projectName
     * @param chosenFields
     * @return
     * @throws DataAccessException
     */
    private List<SearchColumn> getColumnsFromNames(String userName, String projectName, List<String> chosenFields)
            throws DataAccessException
    {
        Map<String, SearchColumn> allFieldsMap = new HashMap<String, SearchColumn>();
        ProjectManager pm = SysManagerFactory.getProjectManager();

        Collection<SearchColumn> allFields = pm.getNavigableFields(userName, projectName);
        for (SearchColumn fixedField : allFields)
            allFieldsMap.put(fixedField.fieldName, fixedField);

        logger.logp(Level.INFO, "NavigatorServlet", "setFieldsChosen", "Chosen fields: " + chosenFields);

        List<SearchColumn> chosenOnes = new ArrayList<SearchColumn>();
        for (String fieldName : chosenFields)
        {
            if (allFieldsMap.containsKey(fieldName))
                chosenOnes.add(allFieldsMap.get(fieldName));
        }
        return chosenOnes;
    }
}
