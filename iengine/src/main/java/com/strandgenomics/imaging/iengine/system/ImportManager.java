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
