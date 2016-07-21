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

package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.UserMembership;

public class DBProjectDAO extends ImageSpaceDAO<Project> implements ProjectDAO {
	
	DBProjectDAO(ImageSpaceDAOFactory factory, ConnectionProvider connectionProvider) 
	{
		super(factory, connectionProvider, "ProjectDAO.xml");
	}
	
	@Override
	public Permission getRole(String login, int projectID) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBProjectDAO", "getRole", "userID="+login+", projectID="+projectID);
		
		 SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ROLE_FOR_USER");
	     sqlQuery.setValue("ProjectID", projectID, Types.INTEGER);
	     sqlQuery.setValue("UserID",    login,     Types.VARCHAR);
	     
	     String role = getString(sqlQuery);
	     return role == null ? null : Permission.valueOf(role);
	}

	@Override
	public Project findProject(int projectID) throws DataAccessException 
	{
        logger.logp(Level.FINE, "DBProjectDAO", "findProject", "projectID="+projectID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_ID");
        sqlQuery.setValue("ProjectID", projectID, Types.INTEGER);

        return fetchInstance(sqlQuery);
	}

	@Override
	public Project findProject(String name) throws DataAccessException 
	{
		if(name == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.FINE, "DBProjectDAO", "findProject", "name="+name);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_NAME");
        sqlQuery.setValue("ProjectName", name, Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}
	
	@Override
	public Project findByLocation(String newLocation) throws DataAccessException 
	{
		if(newLocation == null) throw new NullPointerException("unexpected null value");
        logger.logp(Level.INFO, "DBProjectDAO", "findByLocation", "newLocation="+newLocation);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_LOCATION");
        sqlQuery.setValue("Location", newLocation, Types.VARCHAR);

        return fetchInstance(sqlQuery);
	}
	
	@Override
	public List<Project> findByManager(String managerLogin) throws DataAccessException
	{
		if(managerLogin == null) throw new NullPointerException("unexpected null value");
		
        logger.logp(Level.INFO, "DBProjectDAO", "findByManager", "manager="+managerLogin);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_MANAGER");
        sqlQuery.setValue("Manager", managerLogin, Types.VARCHAR);

        RowSet<Object[]> result = executeQuery(sqlQuery);
        if(result!=null)
        {
        	List<Project> projects = new ArrayList<Project>(); 
        	for(Object[] row:result.getRows())
        	{
        		Project project = findProject(Util.getInteger(row[0]));
        		projects.add(project);
        	}
        	
        	return projects;
        }
        
        return null;
	}
	
	@Override
	public List<Project> findAllProjects() throws DataAccessException
	{
        logger.logp(Level.INFO, "DBProjectDAO", "findAllProjects", "");

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_PROJECTS");

        RowSet<Object[]> result = executeQuery(sqlQuery);
        if(result!=null)
        {
        	List<Project> projects = new ArrayList<Project>(); 
        	for(Object[] row:result.getRows())
        	{
        		Project project = findProject(Util.getInteger(row[0]));
        		projects.add(project);
        	}
        	
        	return projects;
        }
        
        return null;
	}
	
	@Override
    public List<Project> findProjectByStatus(ProjectStatus status) throws DataAccessException
    {
		logger.logp(Level.INFO, "DBProjectDAO", "findProject", "all projects for status "+status);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_STATUS");
        sqlQuery.setValue("ProjectStatus", status == null ? null : status.name(), Types.VARCHAR);

        RowSet<Project> result = find(sqlQuery);
        return result == null ? null : result.getRows();
    }
	
	@Override
	public List<Project> findProject(String login, ProjectStatus status) throws DataAccessException 
	{
		if(status == null) 
			throw new NullPointerException("unexpected null value");
		
		logger.logp(Level.INFO, "DBProjectDAO", "findProject", "all projects for user "+login +" for status "+status);
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_FOR_USER_AND_STATUS");
        sqlQuery.setValue("UserID",        login,         Types.VARCHAR);
        sqlQuery.setValue("ProjectStatus", status.name(), Types.VARCHAR);

        RowSet<Project> result = find(sqlQuery);
        return result == null ? null : result.getRows();
	}
	
	@Override
	public boolean deleteProject(int projectID) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBProjectDAO", "deleteProject", "projectID="+projectID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("DELETE_PROJECT");
        sqlQuery.setValue("ProjectID", projectID, Types.INTEGER);
        return updateDatabase(sqlQuery);
	}

	@Override
	public Project insertProjectEntries(String name, String notes, String createdBy, double quota, String location) 
			throws DataAccessException
	{
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_PROJECT");
        logger.logp(Level.INFO, "DBProjectDAO", "insertProjectEntries", "name="+name);
        
        sqlQuery.setValue("ProjectName", name,       				Types.VARCHAR);
        sqlQuery.setValue("Notes",       notes,      				Types.VARCHAR);
        sqlQuery.setValue("CreatedBy",   createdBy,  				Types.VARCHAR);
        sqlQuery.setValue("Quota",       quota,      				Types.DOUBLE);
        sqlQuery.setValue("Location",    location,                  Types.VARCHAR);

        updateDatabase(sqlQuery);
        
        return findProject(name);
	}


    public boolean updateProjectRecords(int projectID, int noOfRecords, double diskUsage)
    		throws DataAccessException
	{
        logger.logp(Level.INFO, "DBProjectDAO", "updateProjectRecords", "projectID="+projectID +",noOfRecords="+noOfRecords +" ,diskUsage="+diskUsage);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PROJECT_RECORD");
        sqlQuery.setValue("ProjectID",   projectID,   Types.INTEGER);
        sqlQuery.setValue("RecordCount", noOfRecords, Types.INTEGER);
        sqlQuery.setValue("DiskUsage",   diskUsage,   Types.DOUBLE);

        return updateDatabase(sqlQuery);
	}

	@Override
	public String[] getProjectMembers(int projectID, Permission role) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBProjectDAO", "getProjectMembers", "projectID="+projectID +", role = "+role);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_MEMBERS_FOR_ROLE");
        sqlQuery.setValue("ProjectID",    projectID,  Types.INTEGER);
        sqlQuery.setValue("UserRole",  role == null ? null : role.name(), Types.VARCHAR);

        return getRowsWithStringValues(sqlQuery);
	}
	
	@Override
	public List<UserMembership> getMemberships(Integer projectID , String userLogin) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBProjectDAO", "getProjectMemberships", "listing all user memeberships");

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_PROJECT_MEMBERSHIPS");
        sqlQuery.setValue("ProjectID",    projectID,  Types.INTEGER);

        sqlQuery.setValue("UserLogin",    userLogin,  Types.VARCHAR);
        
        RowSet<Object[]> result = executeQuery(sqlQuery);
        
        if(result == null || result.size() == 0) return null;
        
        List<UserMembership> memberships = new ArrayList<UserMembership>();
        for(Object[] entry:result.getRows())
        {
        	int project_id = Util.getInteger(entry[0]);
        	String user_login = Util.getString(entry[1]);
        	String role = Util.getString(entry[2]);
        	Permission permission = Permission.valueOf(role);
        	memberships.add(new UserMembership(project_id, user_login, permission));
        }
        
        return memberships;
	}
	


	@Override
	public boolean updateProjectStatus(int projectID, ProjectStatus status) throws DataAccessException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeProjectMembership(int projectID, String login) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBProjectDAO", "removeProjectMembership", "projectID="+projectID +",userID="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_PROJECT_MEMBER");
        
        sqlQuery.setValue("ProjectID", projectID, Types.INTEGER);
        sqlQuery.setValue("UserID",    login,     Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}

	@Override
	public boolean addProjectMembership(int projectID, String login, Permission role) throws DataAccessException 
	{
        logger.logp(Level.INFO, "DBProjectDAO", "addProjectMembership", "projectID="+projectID +",user="+login);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_PROJECT_MEMBER");
        sqlQuery.setValue("ProjectID", projectID,   Types.INTEGER);
        sqlQuery.setValue("UserID",    login,       Types.VARCHAR);
        sqlQuery.setValue("UserRole",  role.name(), Types.VARCHAR);

        return updateDatabase(sqlQuery);
	}
	
	
	@Override
	public Project createObject(Object[] columnValues) 
	{
		int projectID = Util.getInteger(columnValues[0]);
		String name   = (String)columnValues[1];
		String notes  = (String)columnValues[2];
		Timestamp creationDate = Util.getTimestamp(columnValues[3]);
		ProjectStatus status = ProjectStatus.valueOf((String)columnValues[4]);
		String createdBy = (String) columnValues[5];
		int noOfRecords = Util.getInteger(columnValues[6]);
		double spaceUsage = Util.getDouble(columnValues[7]);
		double storageQuota = Util.getDouble(columnValues[8]);
		String location  = (String)columnValues[9];
		
		return new Project(projectID, name, notes, creationDate, status, 
				createdBy, noOfRecords, spaceUsage, storageQuota, location);
	}
	
	@Override
	public void updateProjectDetails(int projectID, String notes, ProjectStatus status, double storageQuota) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBProjectDAO", "updateProjectDetails", "projectID="+projectID);

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_PROJECT_DETAILS");
        sqlQuery.setValue("ProjectID",   projectID,   Types.INTEGER);
        sqlQuery.setValue("Notes", notes, Types.VARCHAR);
        sqlQuery.setValue("Status", status.name(), Types.VARCHAR);
        sqlQuery.setValue("Quota",   storageQuota,   Types.DOUBLE);

        updateDatabase(sqlQuery);
	}
	
	@Override
	public List<UserMembership> getAllUserMemberships() throws DataAccessException
	{
		logger.logp(Level.INFO, "DBProjectDAO", "getAllUserMemberships", "listing all user memeberships");

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_USER_MEMBERSHIPS");

        RowSet<Object[]> result = executeQuery(sqlQuery);
        
        if(result == null || result.size() == 0) return null;
        
        List<UserMembership> memberships = new ArrayList<UserMembership>();
        for(Object[] entry:result.getRows())
        {
        	int project_id = Util.getInteger(entry[0]);
        	String user_login = Util.getString(entry[1]);
        	String role = Util.getString(entry[2]);
        	Permission permission = Permission.valueOf(role);
        	memberships.add(new UserMembership(project_id, user_login, permission));
        }
        
        return memberships;
	}
	

	@Override
	public void addUserPermission(int projectID, String userLogin, Permission permission) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBProjectDAO", "addUserPermission", "adding user permission for "+userLogin+" for "+projectID);
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("ADD_USER_PERMISSION");
		sqlQuery.setValue("ProjectID",   projectID,   Types.INTEGER);
		sqlQuery.setValue("UserID",   userLogin,   Types.VARCHAR);
		sqlQuery.setValue("UserRole",   permission.name(),   Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}

	@Override
	public void updateUserPermission(int projectID, String userLogin, Permission newPermission) throws DataAccessException
	{
		logger.logp(Level.INFO, "DBProjectDAO", "updateUserPermission", "updating user permission for "+userLogin+" for "+projectID);
		
		SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_USER_PERMISSION");
		sqlQuery.setValue("ProjectID",   projectID,   Types.INTEGER);
		sqlQuery.setValue("User",   userLogin,   Types.VARCHAR);
		sqlQuery.setValue("Permission",   newPermission.name(),   Types.VARCHAR);
		
		updateDatabase(sqlQuery);
	}
	
	public static void main(String ...args) throws DataAccessException, ClassNotFoundException, SQLException
	{}
}
