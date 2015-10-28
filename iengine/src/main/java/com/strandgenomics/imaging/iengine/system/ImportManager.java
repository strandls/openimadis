/*
 * ImportManager.java
 *
 * AVADIS Image Management System
 * Core Engine
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
package com.strandgenomics.imaging.iengine.system;

import java.util.ArrayList;
import java.util.List;

import com.strandgenomics.imaging.icore.Status;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ImportDAO;
import com.strandgenomics.imaging.iengine.dao.db.DBImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.models.Import;

/**
 * manages the import related funcitonalities
 * @author navneet
 *
 */
public class ImportManager extends SysManagerFactory {
	
	/**
	 * add request and ticket to imports
	 * @param request
	 * @return 
	 * @return
	 * @throws DataAccessException
	 */
	public final synchronized void insertImport(long ticketID, long requestTime, Status status, RecordCreationRequest request) throws DataAccessException
	{
		int projectId= DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findProject(request.getProject()).getID();
		ImportDAO importDAO = DBImageSpaceDAOFactory.getDAOFactory().getImportDAO();
		importDAO.insertImport(projectId, ticketID, requestTime, status, request);
	}
	
	/**
	 * get All imports for with complete status
	 * @throws DataAccessException 
	 */
	public List<Import> getCompleteImports(String projectName) throws DataAccessException{
		
		int projectId= DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findProject(projectName).getID();
		
		ImportDAO importDAO = DBImageSpaceDAOFactory.getDAOFactory().getImportDAO();
		
		List<Import> completeImports = new ArrayList<Import>();
		
		List<Import> success=	importDAO.getImportsForStatus(projectId, Status.SUCCESS);
		List<Import> failure=	importDAO.getImportsForStatus(projectId, Status.FAILURE);
		List<Import> duplicate=	importDAO.getImportsForStatus(projectId, Status.DUPLICATE);
		List<Import> expired=	importDAO.getImportsForStatus(projectId, Status.EXPIRED);
		
		if(success!=null)
			completeImports.addAll(success);
		if(failure!=null)
			completeImports.addAll(failure);
		if(duplicate!=null)
			completeImports.addAll(duplicate);
		if(expired!=null)
			completeImports.addAll(expired);
		
		return completeImports;
	}
	
	/**
	 * get All imports for with pending status
	 * @throws DataAccessException 
	 */
	public List<Import> getPendingImports(String projectName) throws DataAccessException{
		
		int projectId= DBImageSpaceDAOFactory.getDAOFactory().getProjectDAO().findProject(projectName).getID();
		
		ImportDAO importDAO = DBImageSpaceDAOFactory.getDAOFactory().getImportDAO();
		
		List<Import> pendingImports = new ArrayList<Import>();
		
		List<Import> queued=	importDAO.getImportsForStatus(projectId, Status.QUEUED);
		List<Import> uploading=	importDAO.getImportsForStatus(projectId, Status.UPLOADING);
		List<Import> waiting=	importDAO.getImportsForStatus(projectId, Status.WAITING);
		List<Import> executing=	importDAO.getImportsForStatus(projectId, Status.EXECUTING);
		
		if(executing!=null)
			pendingImports.addAll(executing);
		if(queued!=null)
			pendingImports.addAll(queued);
		if(uploading!=null)
			pendingImports.addAll(uploading);
		if(waiting!=null)
			pendingImports.addAll(waiting);
		
		return pendingImports;
	}
	
}
