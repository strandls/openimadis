/*
 * ImageSpaceSearchService.java
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

package com.strandgenomics.imaging.iserver.services.def.search;

import java.rmi.RemoteException;


/**
 * Search and Navigation APIs
 * @author arunabha
 *
 */
public interface ImageSpaceSearch {
	
    /**
     * Returns an array of system meta data names for the specified project 
     * @param the access token required to make this call
     * @param project the project under consideration
     * @return list of system annotation specifications (name and type)
     * @throws RemoteException
     */
    public SearchField[] getAvailableDynamicMetaData(String accessToken, String projectName) 
    		throws RemoteException;
    
    /**
     * Returns an array of annotation (meta data) names for the specified project 
     * @param the access token required to make this call
     * @param project the project under consideration
     * @return list of user annotation specifications (name and type)
     * @throws RemoteException
     */
    public SearchField[] getAvailableUserAnnotations(String accessToken, String projectName) 
    		throws RemoteException;
    
    /**
     * Returns a list of meta-data names that are navigable
     * @param the access token required to make this call
     * @param projectName the project under consideration
     * @return a list of meta-data names that are navigable
     */
    public SearchField[] getNavigableFields(String accessToken, String projectName)
    		throws RemoteException;
    
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
    public long[] search(String accessToken, String freeText, String[] projectNames, SearchCondition[] filters, int maxResult) 
    		throws RemoteException;

    /**
     * List the list of records matching the specified navigation condition 
     * @param the access token required to make this call
     * @param parentProject the record under consideration
     * @param conditions the list of search conditions (joined by AND) 
     * @return list of records matching the specified navigation condition 
     */
    public long[] findRecords(String accessToken, String projectName, SearchCondition[] conditions)
    		throws RemoteException;
    
    /**
     * Returns the list of navigation nodes given the existing state and the specified variable
     * @param the access token required to make this call
     * @param projectName the project under consideration
     * @param preConditions a collection of existing navigation conditions
     * @param currentCondition a new (not existing in the preConditions) variable to navigate on
     * @return a list of bins (nodes)
     */
    public SearchNode[] getNavigationBins(String accessToken, String projectName, SearchCondition[] preConditions,
    		SearchCondition currentCondition) throws RemoteException;

}
