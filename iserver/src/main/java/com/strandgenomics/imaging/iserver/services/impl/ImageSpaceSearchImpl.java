/*
 * ImageSpaceSearchImpl.java
 *
 * AVADIS Image Management System
 * Web Service Implementation
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

package com.strandgenomics.imaging.iserver.services.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.NavigationBin;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.system.SolrSearchManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iserver.services.ws.search.ImageSpaceSearch;
import com.strandgenomics.imaging.iserver.services.ws.search.SearchCondition;
import com.strandgenomics.imaging.iserver.services.ws.search.SearchField;
import com.strandgenomics.imaging.iserver.services.ws.search.SearchNode;

/**
 * Implementation of search ispace search services
 * @author arunabha
 *
 */
public class ImageSpaceSearchImpl implements ImageSpaceSearch, Serializable {
	
	private static final long serialVersionUID = 3400047511420264020L;
	private transient Logger logger = null;
	
	public ImageSpaceSearchImpl()
	{
		//initialize the system properties and logger
		Config.getInstance();
		logger = Logger.getLogger("com.strandgenomics.imaging.iserver.services.impl");
	}

    /**
     * Returns an array of system meta data names for the specified project 
     * @param the access token required to make this call
     * @param project the project under consideration
     * @return list of system annotation specifications (name and type)
     * @throws RemoteException
     */
	@Override
    public SearchField[] getAvailableDynamicMetaData(String accessToken, String projectName) 
    		throws RemoteException
	{
		String actorLogin = Service.SEARCH.validateAccessToken(accessToken);
		
		// TODO Auto-generated method stub
		return null;
	}
    
    /**
     * Returns an array of annotation (meta data) names for the specified project 
     * @param the access token required to make this call
     * @param project the project under consideration
     * @return list of user annotation specifications (name and type)
     * @throws RemoteException
     */
	@Override
    public SearchField[] getAvailableUserAnnotations(String accessToken, String projectName) 
    		throws RemoteException
	{
		String actorLogin = Service.SEARCH.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceSearchImpl", "getAvailableUserAnnotations", "finding user annotation for project "+ projectName);
			
			Collection<SearchColumn> fields = SysManagerFactory.getProjectManager().getUserAnnotationFields(actorLogin, projectName);
			return CoercionHelper.toRemoteSearchField(fields);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceSearchImpl", "getAvailableUserAnnotations", "error finding user annotations for project "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns a list of meta-data names that are navigable
     * @param the access token required to make this call
     * @param projectName the project under consideration
     * @return a list of meta-data names that are navigable
     */
	@Override
    public SearchField[] getNavigableFields(String accessToken, String projectName)
    		throws RemoteException
	{
		String actorLogin = Service.SEARCH.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceSearchImpl", "getNavigableFields", "finding navigable fields for project "+ projectName);
			
			Collection<SearchColumn> fields = SysManagerFactory.getProjectManager().getNavigableFields(actorLogin, projectName);
			return CoercionHelper.toRemoteSearchField(fields);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceSearchImpl", "getNavigableFields", "error finding navigable fields for project "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Search AVADIS IMG Server for records matching the specified search text
     * @param the access token required to make this call
     * @param freeText the search text
     * @param projectNames list of projects where the search is restricted (can be null)
     * @param filters list of search conditions
     * @param maxResult maximum number of result to fetch
     * @return a list of records (their guid)
     * @throws RemoteException
     */
	@Override
    public long[] search(String accessToken, String freeText, String[] projectNames, SearchCondition[] filters, int maxResult) 
    		throws RemoteException
	{
		String actorLogin = Service.SEARCH.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceSearchImpl", "search", "searching projects with "+ freeText);
			
	    	SolrSearchManager engine = SysManagerFactory.getSolrSearchEngine();
    		List<String> projects = projectNames == null ? null : Arrays.asList(projectNames);
    		Collection<com.strandgenomics.imaging.icore.SearchCondition> fList = CoercionHelper.toSearchConditions(filters);
    		
    		return engine.searchWithFilters(actorLogin, freeText, projects, fList, maxResult);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceSearchImpl", "getNavigableFields", "error searching with "+freeText, ex);
			throw new RemoteException(ex.getMessage());
		}
	}

    /**
     * List the list of records matching the specified navigation condition 
     * @param the access token required to make this call
     * @param parentProject the record under consideration
     * @param conditions the list of search conditions (joined by AND) 
     * @return list of records matching the specified navigation condition 
     */
	@Override
    public long[] findRecords(String accessToken, String projectName, SearchCondition[] conditions)
    		throws RemoteException
	{
		String actorLogin = Service.SEARCH.validateAccessToken(accessToken);
		try
		{
			logger.logp(Level.INFO, "ImageSpaceSearchImpl", "findRecords", "finding matching record guids within project "+ projectName);
			
			Set<com.strandgenomics.imaging.icore.SearchCondition> search = CoercionHelper.toSearchConditions(conditions);
			
			long[] guids = null;
			if(search == null || search.isEmpty())
			{
				guids = SysManagerFactory.getRecordManager().getGUIDs(actorLogin, projectName);
			}
			else
			{
				guids = SysManagerFactory.getSearchEngine().getRecords(actorLogin, projectName, search);
			}
			return guids;
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceSearchImpl", "findRecords", "error finding matching record guids within project "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
    
    /**
     * Returns the list of navigation nodes given the existing state and the specified variable
     * @param the access token required to make this call
     * @param projectName the project under consideration
     * @param preConditions a collection of existing navigation conditions
     * @param currentCondition a new (not existing in the preConditions) variable to navigate on
     * @return a list of bins (nodes)
     */
	@Override
    public SearchNode[] getNavigationBins(String accessToken, String projectName, 
    		SearchCondition[] preConditions, SearchCondition currentCondition) throws RemoteException
	{
		String actorLogin = Service.SEARCH.validateAccessToken(accessToken);
		
		try
		{
			logger.logp(Level.INFO, "ImageSpaceSearchImpl", "getNavigationBins", "finding navigable bins for project "+ projectName);
			
		  	Set<com.strandgenomics.imaging.icore.SearchCondition> searchCondition = CoercionHelper.toSearchConditions(preConditions);
	    	com.strandgenomics.imaging.icore.SearchCondition cc = CoercionHelper.toSearchCondition(currentCondition);

			List<NavigationBin> bins = SysManagerFactory.getSearchEngine().getNavigationBins(actorLogin, projectName, searchCondition, cc.fieldName, cc.getLowerLimit(), cc.getUpperLimit());
			return CoercionHelper.toSearchNodes(bins);
		}
		catch(Exception ex)
		{
			logger.logp(Level.WARNING, "ImageSpaceSearchImpl", "getNavigationBins", "error finding navigable bins for project "+projectName, ex);
			throw new RemoteException(ex.getMessage());
		}
	}
}
