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
