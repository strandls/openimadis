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

/*ClientManager.java*/

package com.strandgenomics.imaging.iengine.system;

import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ProjectClientDAO;
import com.strandgenomics.imaging.iengine.models.Project;


/**
 * Client related api
 * 
 * @author navneet
 */
public class ClientManager extends SystemManager {
	
	public synchronized void createNewFolder(String actor, int projectID, String name, long parentID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "createNewFolder", "creating folder="+ name);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        dao.createNewFolder(projectID, name, parentID);		
	}
	
	public synchronized void removeFolder(String actor, int projectID, long folderID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canDelete(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "removeFolder", "removing folder="+ folderID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        dao.removeFolder(projectID, folderID);		
	}	
	
	/**
	 * adds new favorite client in current project
	 * @param actor
	 * @param projectID
	 * @param clientID
	 * @param parentID
	 * @throws DataAccessException
	 */
	public synchronized void addClient(String actor, int projectID, String clientID, long parentID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canWrite(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
			
		logger.logp(Level.INFO, "ClientManager", "addClient", "adding client="+clientID+" to projectID="+ projectID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        dao.addClient(projectID,clientID,parentID);		
	}
	
	/**
	 * remove client from favourites list for a project
	 * @param actor
	 * @param projectID
	 * @param clientID
	 * @throws DataAccessException
	 */
	public synchronized void removeClient(String actor, int projectID, String clientID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canDelete(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "removeClient", "removeing client="+clientID+" from projectID="+ projectID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        dao.removeClient(projectID,clientID);		
	}
	
	/**
	 * 
	 * @param actor
	 * @param projectID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Long> getRootFolders(String actor, int projectID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "geRootFolders", "root folders for projectID="+ projectID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        return dao.getRootFolders(projectID);		
	}
	
	/**
	 * 
	 * @param actor
	 * @param projectID
	 * @param parentID
	 * @return
	 * @throws DataAccessException
	 */
	public List<Long> getSubFolders(String actor, int projectID, long parentID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "getSubFolders", "sub folders for folder="+ parentID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        return dao.getSubFolders(projectID,parentID);		
	}
	
	/**
	 * 
	 * @param actor
	 * @param projectID
	 * @param folderID
	 * @return
	 * @throws DataAccessException
	 */
	public String[] getClientIDsInFolder(String actor, int projectID, long folderID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "getClientIDsInFolder", "all clients in folder="+ folderID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        return dao.getClientIDsInFolder(projectID,folderID);		
	}
	
	/**
	 * 
	 * @param actor
	 * @param projectID
	 * @param folderID
	 * @return
	 * @throws DataAccessException
	 */
	public String getFolderName(String actor, int projectID, long folderID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "getFolderName", "name of folder="+ folderID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        return dao.getFolderName(projectID,folderID);		
	}
	
	/**
	 * returns all the clients which are marked as favorites for current project
	 * @param actor
	 * @param projectID
	 * @return
	 * @throws DataAccessException
	 */
	public String[] getAllClients(String actor, int projectID) throws DataAccessException
	{
		Project prj = SysManagerFactory.getProjectManager().getProject(projectID);
		if(!SysManagerFactory.getUserPermissionManager().canRead(actor, prj.getName()))
			throw new ImagingEngineException(new ErrorCode(ErrorCode.ImageSpace.UNAUTHORIZED_ACCESS));
		
		logger.logp(Level.INFO, "ClientManager", "getAllClients", "projectID="+ projectID);
        ImageSpaceDAOFactory factory = ImageSpaceDAOFactory.getDAOFactory();
        ProjectClientDAO dao  = factory.getProjectClientDAO();
        
        return dao.getAllClients(projectID);		
	}
}
