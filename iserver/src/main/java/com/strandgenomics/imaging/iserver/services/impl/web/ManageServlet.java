/*
 * AdminServlet.java
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.auth.IAccessToken;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.compute.Publisher;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.export.ExportRequest;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfile;
import com.strandgenomics.imaging.iengine.models.AcquisitionProfileType;
import com.strandgenomics.imaging.iengine.models.AuthenticationType;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.models.Job;
import com.strandgenomics.imaging.iengine.models.LengthUnit;
import com.strandgenomics.imaging.iengine.models.LicenseIdentifier;
import com.strandgenomics.imaging.iengine.models.LoginHistoryObject;
import com.strandgenomics.imaging.iengine.models.MicroscopeObject;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.ProjectAccess;
import com.strandgenomics.imaging.iengine.models.ProjectAccessKey;
import com.strandgenomics.imaging.iengine.models.ProjectStatus;
import com.strandgenomics.imaging.iengine.models.SearchColumn;
import com.strandgenomics.imaging.iengine.models.TimeUnit;
import com.strandgenomics.imaging.iengine.models.Unit;
import com.strandgenomics.imaging.iengine.models.UnitAssociation;
import com.strandgenomics.imaging.iengine.models.UnitType;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.models.UserMembership;
import com.strandgenomics.imaging.iengine.models.UserStatus;
import com.strandgenomics.imaging.iengine.system.AuthorizationManager;
import com.strandgenomics.imaging.iengine.system.ProjectManager;
import com.strandgenomics.imaging.iengine.system.RecordCreationRequest;
import com.strandgenomics.imaging.iengine.system.StorageManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.TicketManager;
import com.strandgenomics.imaging.iengine.system.UnitManager;
import com.strandgenomics.imaging.iengine.system.UserManager;
import com.strandgenomics.imaging.iengine.system.UserPermissionManager;
import com.strandgenomics.imaging.iengine.worker.Service;
import com.strandgenomics.imaging.iengine.worker.ServiceAnnotation;
import com.strandgenomics.imaging.iengine.worker.ServiceParameter;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * List of admin related functionalities
 * 
 * @author santhosh
 * 
 */
public class ManageServlet extends ApplicationServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Get LoggedIn User Name
     */
    public void getCurrentUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	String name=SysManagerFactory.getUserManager().getUser(user).getName();
    	   	
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        ret.add(Util.createMap("name", name));

        writeJSON(resp, ret);
    }
    
    
    /**
     * Get list of users.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listUsers", "list users : " + user);

        UserManager userManager = SysManagerFactory.getUserManager();
        String query = req.getParameter(RequestConstants.SEARCH_KEY);

        List<User> listUsers = userManager.listUsers(user);
        
        Collections.sort(listUsers, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return (o1.getName().toLowerCase()).compareTo(o2.getName().toLowerCase());
			}
		});
        
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();          
        
		if (query != null)
		{
	        query = query.toLowerCase();
			for (User listUser : listUsers)
			{
				if (listUser.getName().toLowerCase().contains(query) || listUser.getLogin().toLowerCase().contains(query) || listUser.getEmail().toLowerCase().contains(query))
				{
					logger.logp(Level.INFO, "ManageServlet", "listUsers", "user=" + listUser.getName());
					ret.add(getUserMap(listUser));
				}
			}

			writeJSON(resp, ret);
			return;
		}
        
        
    	int count = 0;
		if (listUsers != null && listUsers.size() > 0)
		{
			count = listUsers.size();
			
			int start = 0;
			int end = 0;
			
			String startString = getOptionalParam("start", req);
			logger.logp(Level.INFO, "ManageServlet", "listUsers", "start="+startString);
			try
			{
				start = Integer.parseInt(startString);
			}
			catch (Exception e)
			{
				start = 0;
			}
			
			String limitString = getOptionalParam("limit", req); 
			logger.logp(Level.INFO, "ManageServlet", "listUsers", "limit="+limitString);
			try
			{
				int limit = Integer.parseInt(limitString);
				
				end = start+limit;
				if(end>=listUsers.size())
					end = listUsers.size();
			}
			catch (Exception e)
			{}
			
			List<User> paginatedListUsers = listUsers.subList(start, end);
			
	        if (paginatedListUsers != null) {
	            for (User listUser : paginatedListUsers) {
	            	ret.add(getUserMap(listUser));	            	
	            }
	        }
		}
        
		writeJSON(resp, Util.createMap("items",ret, "total", count));
    }
    
    /**
     * download list of user as csv
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadUserList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "downloadUserList", "download users : " + user);

        UserManager userManager = SysManagerFactory.getUserManager();

        List<User> listUsers = userManager.listUsers(user);
        
        File tempFile = File.createTempFile("UserList", ".csv");
        tempFile.deleteOnExit();
        
        PrintWriter pw = new PrintWriter(tempFile);
        for(User listUser: listUsers)
        {
        	StringBuffer sb = new StringBuffer();
        	sb.append(listUser.getName()+" , ");
        	sb.append(listUser.getLogin()+" , ");
        	sb.append(listUser.getEmail()+" , ");
        	sb.append(listUser.getRank().name()+" , ");
        	sb.append(listUser.getAuthenticationType().name()+" , ");
        	sb.append(listUser.getRank().toString()+" , ");
        	sb.append(listUser.getStatus().name());
        	
        	pw.println(sb.toString());
        }
        
        pw.close();
        
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) tempFile.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + tempFile.getName() + "\"");
        sendFile(resp.getOutputStream(), tempFile);
        tempFile.delete();
    }
    
    /**
     * download list of user as csv
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadProjectList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "downloadProjectList", "download projects : " + user);

        ProjectManager manager = SysManagerFactory.getProjectManager();
        List<Project> projects = manager.getAllProjectsByManager(user);
        
        File tempFile = File.createTempFile("ProjectList", ".csv");
        tempFile.deleteOnExit();
        
        PrintWriter pw = new PrintWriter(tempFile);
        for(Project listProject: projects)
        {
        	String notes = listProject.getNotes();
        	notes = notes.replaceAll("\"", "\"\"");
        	notes = "\""+notes+"\""; 
        	
        	String name = listProject.getName();
        	name = name.replaceAll("\"", "\"\"");
        	name = "\""+name+"\"";
        	
        	StringBuffer sb = new StringBuffer();
        	sb.append(name+",");
        	sb.append(notes+",");
        	sb.append(listProject.getStatus().toString()+",");
        	sb.append(listProject.getCreatedBy()+",");
        	sb.append(getShortDate(listProject.getCreationDate())+",");
        	sb.append(listProject.getNoOfRecords()+",");
        	
        	DecimalFormat formatter = new DecimalFormat("#.##"); 
            double size = Double.valueOf(formatter.format(listProject.getSpaceUsage()));
        	
            sb.append(size+",");
        	sb.append(listProject.getStorageQuota()+",");
        	
        	pw.println(sb.toString());
        }
        pw.close();
        
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) tempFile.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + tempFile.getName() + "\"");
        sendFile(resp.getOutputStream(), tempFile);
        tempFile.delete();
    }
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException3:05:37 PM
     */
    public void downloadMembershipList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        String projectName = getOptionalParam("projectName", req);
        String userLogin = getOptionalParam("userLogin", req);
        List<Map<String, Object>> memberships = new ArrayList<Map<String, Object>>();
        
        if("".equals(projectName)){
        	projectName=null;
        }

        if("".equals(userLogin)){
        	userLogin=null;
        }

        String query = req.getParameter(RequestConstants.SEARCH_KEY);
        if(query!=null)      	
        	query = query.toLowerCase();
        
        logger.logp(Level.INFO, "ManageServlet", "downloadMembers", "download members : " + userLogin + " for project: " + projectName);
        
        ProjectManager pm = SysManagerFactory.getProjectManager();
        UserManager um=SysManagerFactory.getUserManager();
        List<UserMembership> projectMembers;
		try
		{
			projectMembers = pm.getMemberships(user, projectName, userLogin);
		}
		catch (NullPointerException n)
		{
			projectMembers = (List<UserMembership>) Collections.EMPTY_LIST;
			logger.logp(Level.INFO, "ManageServlet", "downloadMembers", "gjhajgajteeaj");

			writeJSON(resp, memberships);
		}
		projectMembers = (projectMembers == null ? Collections.EMPTY_LIST : projectMembers);
		for (UserMembership membership : projectMembers)
		{
			Rank rank = um.getUser(membership.getUserLogin()).getRank();
			if (rank.equals(Rank.Administrator) || rank.equals(Rank.FacilityManager))
				continue;

			if (query != null && !(membership.user_login.toLowerCase().contains(query)))
			{
				continue;
			}

			Map<String, Object> next = new HashMap<String, Object>();
			next.put("user", membership.user_login);
			next.put("projectName", pm.getProject(membership.getProjectId()).getName());
			next.put("role", membership.getPermission());
			memberships.add(next);
		}
        
        File tempFile = File.createTempFile("MembershipList", ".csv");
        tempFile.deleteOnExit();
        
        PrintWriter pw = new PrintWriter(tempFile);
        for(Map<String , Object> listMembership: memberships)
        {
        	StringBuffer sb = new StringBuffer();
        	sb.append(listMembership.get("user") + " , ");
        	sb.append(listMembership.get("projectName")+" , ");
        	sb.append(listMembership.get("role") +" , ");
        	pw.println(sb.toString());
        }
        pw.close();
        
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) tempFile.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + tempFile.getName() + "\"");
        sendFile(resp.getOutputStream(), tempFile);
        tempFile.delete();
    }
    
    private void sendFile(OutputStream response, File archiveFile) throws IOException
	{
		BufferedOutputStream oStream = null;
		
		FileInputStream fStream = null;
		BufferedInputStream iStream = null;

		try
		{
			oStream = new BufferedOutputStream(response);
			logger.logp(Level.INFO, "DataExchangeServlet", "sendFile", "sending data "+archiveFile +" of length "+archiveFile.length());
			
			fStream = new FileInputStream(archiveFile);
			iStream = new BufferedInputStream(fStream);

			long dataLength = Util.transferData(iStream, oStream);
			logger.logp(Level.INFO, "DataExchangeServlet", "sendFile", "successfully send file " + dataLength);
		} 
		catch (IOException ex) 
		{
			throw ex;
		} 
		finally
		{
			Util.closeStream(oStream);
			Util.closeStream(fStream);
			Util.closeStream(iStream);
		}
	}
    
    /**
     * returns the list of log files
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listLogFiles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        
        String dateString = getOptionalParam("dateFilter", req);
        
		String startStr = getOptionalParam("start", req);
		int start = 0;
		if (startStr != null)
		{
			start = Integer.parseInt(startStr);
		}

		String limitStr = getOptionalParam("limit", req);
		int limit = 0;
		if (limitStr != null)
		{
			limit = Integer.parseInt(limitStr);
		}
        logger.logp(Level.INFO, "ManageServlet", "listLogFiles", "list listLogFiles : date filter=" + dateString);
        
        Date date = dateString == null ? null : new Date(Long.parseLong(dateString));

        List<String> allFiles = SysManagerFactory.getLogSearchManager().getLogFiles(user, date);
        List<String> logFiles = SysManagerFactory.getLogSearchManager().getLogFiles(user, date, start, limit);
        
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (logFiles != null) 
        {
            for (String file : logFiles) 
            {
                ret.add(Util.createMap("name", file, "value", file));
            }
        }
        
        Collections.sort(ret, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, Util.createMap("items",ret, "total", allFiles.size()));
//        writeJSON(resp, ret);
    }
    
    /**
     * Get list of users.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listLDAPUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listUsers", "list users : " + user);

        UserManager userManager = SysManagerFactory.getUserManager();

        List<User> listUsers = userManager.listUsers(user);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (listUsers != null) {
            for (User listUser : listUsers) {
                ret.add(getUserMap(listUser));
            }
        }
        
        Collections.sort(ret, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, ret);
    }
    
    /**
     * get permission for specified user
     * @param user
     * @param project
     */
    public void getUserPermission(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listUserRoles", "list user ranks : " + user);

        String userName = getCurrentUser(req);
        String projectName = req.getParameter("projectName");
        
        Permission permission = SysManagerFactory.getUserPermissionManager().getUserPermission(userName, projectName);
        
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        ret.add(Util.createMap("name", permission.toString(), "value", permission.ordinal()));

        writeJSON(resp, ret);
    }

    /**
     * Convert user object to json friendly map
     * 
     * @param listUser
     * @return
     */
    private Map<String, Object> getUserMap(User listUser) {
        Map<String, Object> userDetails = new HashMap<String, Object>();
        userDetails.put("name", listUser.getName());
        userDetails.put("login", listUser.getLogin());
        userDetails.put("emailID", listUser.getEmail());
        userDetails.put("rank", listUser.getRank().name());
        userDetails.put("type", listUser.getAuthenticationType().name());
        userDetails.put("rankString", listUser.getRank().toString());
        userDetails.put("status", listUser.getStatus().name());
        return userDetails;
    }

    /**
     * Add a new user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String loginName = getRequiredParam("login", req);
        String name = getRequiredParam("name", req);
        String emailID = getRequiredParam("emailID", req);
        String password = getRequiredParam("password", req);
        String rankString = getRequiredParam("rank", req);
        Rank rank = Rank.valueOf(rankString);

        logger.logp(Level.INFO, "ManageServlet", "addUser", "add user : " + user + " login: " + loginName + " name: "
                + name + " email: " + emailID + "rank: " + rank);

        UserManager manager = SysManagerFactory.getUserManager();
        try
        {
        	manager.createUser(user, loginName, password, AuthenticationType.Internal, rank, emailID, name);
        }
        catch(ImagingEngineException e)
    	{
        	System.out.println(e.getErrorCode().getErrorMessage());
        	writeFailure(resp, e.getErrorCode().getErrorMessage());
        	return;
    	}
        catch(Exception e)
        {
        	writeFailure(resp, e.getMessage());
        	return;
        }
        
        writeSuccess(resp);
    }
    
    /**
     * downloads the archive
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadLog(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	// get logfile name
        String filename = getRequiredParam("logfileName", req);

        File logfile = SysManagerFactory.getLogSearchManager().getLogFile(loginUser, filename);
        if (!logfile.exists()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) logfile.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + logfile.getName() + "\"");
        Util.transferData(new FileInputStream(logfile), resp.getOutputStream(), true);
    }
    

    /**
     * Edit details of an user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String loginName = getRequiredParam("login", req);

        String name = getOptionalParam("name", req);
        String emailID = getOptionalParam("emailID", req);
        String rankString = getOptionalParam("rank", req);
        String statusString = getOptionalParam("status", req);

        Rank rank = rankString == null ? null : Rank.valueOf(rankString);
        UserStatus status = statusString == null ? null : UserStatus.valueOf(statusString);

        logger.logp(Level.INFO, "ManageServlet", "editUser", "edit user : " + user + " login: " + loginName + " name: "
                + name + " email: " + emailID + "rank: " + rank + " status: " + status);

        UserManager manager = SysManagerFactory.getUserManager();
        manager.updateUserDetails(user, loginName, name, status, rank, emailID, null);
        writeSuccess(resp);
    }

    /**
     * Reset password for a particular user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String loginName = getRequiredParam("login", req);
        String password = getRequiredParam("password", req);

        logger.logp(Level.INFO, "ManageServlet", "resetPassword", "reset password : " + user + " login: " + loginName);
        UserManager manager = SysManagerFactory.getUserManager();
        try
        {
        	manager.updateUserPassword(user, loginName, password);
            writeSuccess(resp);
        }
        catch (ImagingEngineException e)
		{
        	logger.logp(Level.WARNING, "ManageServlet", "resetPassword", "falied to reset password : " + user + " login: " + user, e);
        	writeFailure(resp, "Failed to update password. "+e.getErrorCode().getErrorMessage());
		}
		catch (DataAccessException e)
		{
			logger.logp(Level.WARNING, "ManageServlet", "resetPassword", "falied to reset password : " + user + " login: " + user, e);
			writeFailure(resp, "Failed to update password.");
		}
    }
    
    /**
     * change password for a particular user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void changeUserPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        String oldPassword = getRequiredParam("currentPassword", req);
        String password = getRequiredParam("password", req);

        boolean authenticate = SysManagerFactory.getUserManager().authenticate(new Application(Constants.getWebApplicationName(), Constants.getWebApplicationVersion()), user, oldPassword);
        if(authenticate)
        {
        	logger.logp(Level.INFO, "ManageServlet", "resetPassword", "reset password : " + user + " login: " + user);
            UserManager manager = SysManagerFactory.getUserManager();
            try
			{
				manager.updateUserPassword(Constants.ADMIN_USER, user, password);
				writeSuccess(resp);
			}
            catch (ImagingEngineException e)
			{
            	logger.logp(Level.WARNING, "ManageServlet", "resetPassword", "falied to reset password : " + user + " login: " + user, e);
            	writeFailure(resp, "Failed to update password. "+e.getErrorCode().getErrorMessage());
			}
			catch (DataAccessException e)
			{
				logger.logp(Level.WARNING, "ManageServlet", "resetPassword", "falied to reset password : " + user + " login: " + user, e);
				writeFailure(resp, "Failed to update password.");
			}
        }
        else
        	writeFailure(resp, "Failed to update password");
    }
    
    /**
     * Reset password for a particular user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void changeAuthenticationType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String loginName = getRequiredParam("login", req);
        String type = getRequiredParam("type", req);
        
        AuthenticationType authType = AuthenticationType.valueOf(type);

        logger.logp(Level.INFO, "ManageServlet", "resetPassword", "reset password : " + user + " login: " + loginName);
        UserManager manager = SysManagerFactory.getUserManager();
        manager.updateUserDetails(user, loginName, null, null, null, null, authType);
        
        if(AuthenticationType.Internal == authType)
        {
        	String password = "CID"+loginName+"3#14";
        	manager.updateUserPassword(user, loginName, password);
        }
        writeSuccess(resp);
    }

    /**
     * Available list of ranks for users
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listUserRanks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listUserRanks", "list user ranks : " + user);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        ret.add(Util.createMap("name", Rank.FacilityManager.toString(), "value", Rank.FacilityManager.name()));
        ret.add(Util.createMap("name", Rank.TeamLeader.toString(), "value", Rank.TeamLeader.name()));
        ret.add(Util.createMap("name", Rank.TeamMember.toString(), "value", Rank.TeamMember.name()));
        writeJSON(resp, ret);
    }

    /**
     * Available list of status for users
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listUserStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listUserStatus", "list user status : " + user);

        UserStatus[] values = UserStatus.values();
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

        for (UserStatus status : values) {
            ret.add(Util.createMap("name", status.toString(), "value", status.name()));
        }
        writeJSON(resp, ret);
    }

    /**
     * Get list of projects which the currently logged in user can manipulate.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listProjects(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);

        logger.logp(Level.INFO, "ManageServlet", "listProjects", "list projects : " + user);
        ProjectManager manager = SysManagerFactory.getProjectManager();
        String query = req.getParameter(RequestConstants.SEARCH_KEY);     
        
        List<Project> projects = manager.getAllProjectsByManager(user);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		if (projects != null)
		{
			for (Project project : projects)
			{
				if (query == null)
				{
					ret.add(getProjectMap(project));
				}
				else
				{
					query = query.toLowerCase();
					if (project.getName().toLowerCase().contains(query))
					{
						ret.add(getProjectMap(project));
					}
				}
			}
		}
        
        Collections.sort(ret, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, ret);
    }
    
    /**
     * returns the list of clients requied for web admin
     * @param req
     * @param resp
     * 			name,
     * 			version,
     * 			description,
     * 			clientId
     * @throws ServletException
     * @throws IOException
     */
    public void listClients(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);

        logger.logp(Level.INFO, "ManageServlet", "list clients", "list clients : ");
        List<Client> allClients = SysManagerFactory.getAuthorizationManager().getAllClients();
        
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (allClients != null) 
        {
            for (Client client : allClients) 
            {
            	if(client != null && !client.getClientID().equals(AuthorizationManager.ACQ_CLIENT_ID))
            	{
            		Map<String, Object> map = new HashMap<String, Object>();
            		map.put("name", client.getName());
            		map.put("version", client.getVersion());
            		map.put("description", client.getDescription());
            		map.put("clientId", client.getClientID());
            		map.put("url", client.getWebClientUrl());
            		
            		ret.add(map);
            	}
            }
        }
        writeJSON(resp, ret);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listPublishers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);

        logger.logp(Level.INFO, "ManageServlet", "listPublishers", "listing Publishers");
        Collection<Publisher> allPublishers = SysManagerFactory.getComputeEngine().getPublisher(user);
        
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (allPublishers != null) 
        {
            for (Publisher publisher : allPublishers) 
            {
            	if(publisher != null)
            	{
            		Map<String, Object> map = new HashMap<String, Object>();
            		map.put("name", publisher.name);
            		map.put("description", publisher.description);
            		map.put("publisherCode", publisher.publisherCode);
            		
            		ret.add(map);
            	}
            }
        }
        writeJSON(resp, ret);
    }

    /**
     * Convert project object to json friendly map.
     * 
     * @param project
     * @return
     */
    private Map<String, Object> getProjectMap(Project project) {
        Map<String, Object> projectDetails = new HashMap<String, Object>();
        projectDetails.put("name", project.getName());
        projectDetails.put("notes", project.getNotes());
        projectDetails.put("status", project.getStatus().toString());
        projectDetails.put("createdBy", project.getCreatedBy());
        projectDetails.put("creationDate", getShortDate(project.getCreationDate()));
        projectDetails.put("noOfRecords", project.getNoOfRecords());
        projectDetails.put("spaceUsage", project.getSpaceUsage());
        projectDetails.put("storageQuota", project.getStorageQuota());

        return projectDetails;
    }

    /**
     * Add a new project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String name = getRequiredParam("name", req);
        String notes = getRequiredParam("notes", req);
        double quota = 0;

        logger.logp(Level.INFO, "ManageServlet", "addProject", "add project : " + user + " name: " + name + " notes: "
                + notes + "quota: " + quota);

        ProjectManager manager = SysManagerFactory.getProjectManager();
        manager.createNewProject(user, name, notes, quota);
        writeSuccess(resp);
    }

    /**
     * Edit project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editProject(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String name = getRequiredParam("name", req);
        String notes = getOptionalParam("notes", req);

        String quotaString = getOptionalParam("storageQuota", req);
        Double quota = quotaString == null ? null : Double.parseDouble(quotaString);

        String statusString = getOptionalParam("status", req);
        ProjectStatus status = statusString == null ? null : ProjectStatus.valueOf(statusString);

        logger.logp(Level.INFO, "ManageServlet", "editProject", "edit project : " + user + " name: " + name
                + " notes: " + notes + "quota: " + quota + " status: " + status);

        ProjectManager manager = SysManagerFactory.getProjectManager();
        manager.updateProjectDetails(user, name, notes, status, quota);
        writeSuccess(resp);
    }

    /**
     * Available list of status for projects
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listProjectStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listProjectStatus", "list project status : " + user);

        ProjectStatus[] values = ProjectStatus.values();
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

        for (ProjectStatus status : values) {
            ret.add(Util.createMap("name", status.toString(), "value", status.name()));
        }
        writeJSON(resp, ret);
    }

    /**
     * Get projects whose membership can be edited by the logged in user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getProjectsForUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "getProjectsForUser", "get projects for user : " + user);
        
        List<Project> projects = SysManagerFactory.getProjectManager().getProjects(user, ProjectStatus.Active);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if(projects!=null)
        {
        	for(Project projectName:projects)
        	{
        		ret.add(Util.createMap("name", projectName.getName(), "value", projectName.getName()));
        	}
        }
        
        Collections.sort(ret, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, ret);
    }

    /**
     * Available list of roles for users in projects
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listUserRoles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ManageServlet", "listUserRoles", "list user ranks : " + user);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        ret.add(Util.createMap("name", Permission.Manager.toString(), "value", Permission.Manager.name()));
        ret.add(Util.createMap("name", Permission.Upload.toString(), "value", Permission.Upload.name()));
        ret.add(Util.createMap("name", Permission.Write.toString(), "value", Permission.Write.name()));
        ret.add(Util.createMap("name", Permission.Export.toString(), "value", Permission.Export.name()));
        ret.add(Util.createMap("name", Permission.Read.toString(), "value", Permission.Read.name()));

        writeJSON(resp, ret);
    }


    /**
     * Get members of a project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
	public void getMembers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String projectName = getOptionalParam("projectName", req);
        String userLogin = getOptionalParam("userLogin", req);
        List<Map<String, Object>> memberships = new ArrayList<Map<String, Object>>();
        
        if("".equals(projectName)){
        	projectName=null;
        }

        if("".equals(userLogin)){
        	userLogin=null;
        }

        String query = req.getParameter(RequestConstants.SEARCH_KEY);
        
        if(query!=null)
        	query = query.toLowerCase();
        
        logger.logp(Level.INFO, "ManageServlet", "getMembers", "get members : " + userLogin + " for project: " + projectName);
        
        ProjectManager pm = SysManagerFactory.getProjectManager();
        UserManager um=SysManagerFactory.getUserManager();
        List<UserMembership> projectMembers;
		try
		{
			projectMembers = pm.getMemberships(user, projectName, userLogin);
		}
		catch (NullPointerException n)
		{
			projectMembers = (List<UserMembership>) Collections.EMPTY_LIST;
			writeJSON(resp, memberships);
		}
		projectMembers = (projectMembers == null ? Collections.EMPTY_LIST : projectMembers);
		for (UserMembership membership : projectMembers)
		{
			Rank rank = um.getUser(membership.getUserLogin()).getRank();
			if (rank.equals(Rank.Administrator) || rank.equals(Rank.FacilityManager))
				continue;

			if (query != null && !(membership.user_login.toLowerCase().contains(query)))
			{
				continue;
			}

			Map<String, Object> next = new HashMap<String, Object>();
			next.put("user", membership.user_login);
			next.put("projectName", pm.getProject(membership.getProjectId()).getName());
			next.put("role", membership.getPermission());
			memberships.add(next);
		}
        
        Collections.sort(memberships, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("user");
				String name2=(String) o2.get("user");
				return name1.compareTo(name2);
			}
		});
        
        writeJSON(resp, memberships);
    }
    
    /**
     * Get unit-project associations
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAssociations(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        String projectName = getOptionalParam("projectName", req);
        String unitName = getOptionalParam("unitName", req);

        String query = req.getParameter(RequestConstants.SEARCH_KEY);
        if(query!=null)
        	query = query.toLowerCase();
        
        logger.logp(Level.INFO, "ManageServlet", "getAssociations", "get members : " + user + " for project: " + projectName);

        List<Map<String, Object>> memberships = new ArrayList<Map<String, Object>>();
        
        List<UnitAssociation> associations = SysManagerFactory.getUnitManager().getUnitAssociations(user, unitName, projectName);
        ProjectManager pm = SysManagerFactory.getProjectManager();
		if (associations != null)
		{
			for (UnitAssociation member : associations)
			{
				if (query != null && !(member.unitName.toLowerCase().contains(query)))
				{
					continue;
				}

				Map<String, Object> next = new HashMap<String, Object>();
				next.put("unitName", member.unitName);

				String name = pm.getProject(member.projectId).getName();
				next.put("projectName", name);
				
				Unit unit = SysManagerFactory.getUnitManager().getUnit(user, member.unitName);

				next.put("storageContribution", member.storageSpace+"GB out of Total "+unit.getGlobalStorage()+" GB");
				memberships.add(next);
			}
		}
        writeJSON(resp, memberships);
    }

    /**
     * Get list of available users for a project.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAvailableMembers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String userLogin = getRequiredParam("userLogin", req);
        List<Map<String, Object>> memberships = new ArrayList<Map<String, Object>>();

        logger.logp(Level.INFO, "ManageServlet", "getAvailableMembers", "get available members : " + user
                + " for project: " + projectName +"  or for user : " + userLogin);
        
        if("".equals(projectName)){
        	projectName=null;
        }

        if("".equals(userLogin)){
        	userLogin=null;
        }
        String query = req.getParameter(RequestConstants.SEARCH_KEY);
        
        if(query!=null)
        	query = query.toLowerCase();

        
        Set<String> projectMemberLogins = new LinkedHashSet<String>();
        Set<String> userMemberProjects = new LinkedHashSet<String>();
        
        List<Map<String, Object>> availableMembers = new ArrayList<Map<String, Object>>();

        ProjectManager pm = SysManagerFactory.getProjectManager();
        UserManager um = SysManagerFactory.getUserManager();
        List<UserMembership> members;
        
        try{
        	members = pm.getMemberships(user , projectName, userLogin );
        } catch(NullPointerException n){
        	members = (List<UserMembership>)Collections.EMPTY_LIST;	
        }
         members = (members == null ? Collections.EMPTY_LIST : members);
      
		for (UserMembership membership : members)
		{
			Rank rank = um.getUser(membership.getUserLogin()).getRank();
			if (rank.equals(Rank.Administrator) || rank.equals(Rank.FacilityManager))
				continue;

			if (query != null && !(membership.user_login.toLowerCase().contains(query)))
			{
				continue;
			}

			Map<String, Object> next = new HashMap<String, Object>();
			next.put("user", membership.user_login);
			next.put("projectName", pm.getProject(membership.getProjectId()).getName());
			next.put("role", membership.getPermission());
			memberships.add(next);
		}
        
		if (projectName != null)
		{
			logger.logp(Level.INFO, "ManageServlet", "getAvailableMembers", "get available members for project");

			for (Map<String, Object> member : memberships)
			{
				projectMemberLogins.add((String) member.get("user"));
			}

			List<String> listLogins = um.listLogins(user);
			for (String string : listLogins)
			{
				if (!projectMemberLogins.contains(string))
					availableMembers.add(Util.createMap("name", string));
			}
		}
		else if (userLogin != null)
		{
			logger.logp(Level.INFO, "ManageServlet", "getAvailableMembers", "get available members for user ");

			for (Map<String, Object> member : memberships)
			{
				userMemberProjects.add((String) member.get("projectName"));
			}

			List<String> listProjectNames = new ArrayList<String>();
			List<Project> listProjects = SysManagerFactory.getUserPermissionManager().getManagedProjects(user); // projectManager.getActiveProjects(actorLogin)
																												// ;
			for (Project project : listProjects)
			{
				listProjectNames.add(project.getName());
			}
			for (String string : listProjectNames)
			{
				if (!userMemberProjects.contains(string))
					availableMembers.add(Util.createMap("name", string));
			}
		}
        
        	
        Collections.sort(availableMembers, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, availableMembers);
    }
    
    /**
     * Get list of available users for a project.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAvailableLDAPUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        
        logger.logp(Level.INFO, "ManageServlet", "getAvailableUsers", "get available ldap users : " + user);

        List<Map<String, Object>> availableLogins = new ArrayList<Map<String, Object>>();
        
        List<User> allLDAPUsers = SysManagerFactory.getLDAPManager().listLDAPUsers();
        List<String> internalUsers = SysManagerFactory.getUserManager().listLogins(user);

        if(allLDAPUsers!=null)
        {
        	for(User ldapUser : allLDAPUsers)
        	{
        		if(!internalUsers.contains(ldapUser.login))
        		{
        			availableLogins.add(Util.createMap("name", ldapUser.login, "email", ldapUser.getEmail()));
        		}
        	}
        }
        
        Collections.sort(availableLogins, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, availableLogins);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void associateUnits(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String unitNamesString = getRequiredParam("unitNames", req);
        
        String quotaString = getRequiredParam("quota", req);
        double quota = Double.parseDouble(quotaString);

		List<String> unitNames = gson.fromJson(unitNamesString,new TypeToken<List<String>>() {}.getType());
        
        logger.logp(Level.INFO, "ManageServlet", "associateUnits", "");
        
        try
        {
        	if(unitNames!=null)
        	{
        		for(String unitName:unitNames)
        		{
        			SysManagerFactory.getUnitManager().updateAssociation(user, projectName, unitName, quota);
        		}
        	}
        	writeSuccess(resp);
        }
        catch (Exception e) 
        {
        	logger.logp(Level.WARNING, "ManageServlet", "associateUnits", "error ",e);
			writeFailure(resp, e.getMessage());
		}
    }
    
    /**
     * removes the project-unit association
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeAssociation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String unitName = getRequiredParam("unitName", req);
        
        logger.logp(Level.INFO, "ManageServlet", "removeAssociation", "project="+projectName+" unit="+unitName);
        
        try
        {
        	SysManagerFactory.getUnitManager().removeUnitAssociation(user, unitName, projectName);
        }
        catch (Exception e) 
        {
        	logger.logp(Level.WARNING, "ManageServlet", "removeAssociation", "project="+projectName+" unit="+unitName, e);
			writeFailure(resp, e.getMessage());
		}
    }
    
    /**
     * Get list of available units
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAvailableUnits(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        logger.logp(Level.INFO, "ManageServlet", "getAvailableUnits", "get available units : " + user + " for project: " + projectName);

        Set<String> unitNames = new LinkedHashSet<String>();
        List<Map<String, Object>> availableUnits = new ArrayList<Map<String, Object>>();

        UnitManager manager = SysManagerFactory.getUnitManager();
        List<Unit> units = manager.listUnits(user);
        
        List<UnitAssociation> associations = manager.getUnitAssociations(user, null, projectName);
        Set<String> associatedUnits = new HashSet<String>();
        if(associations!=null)
        {
        	for(UnitAssociation u:associations)
            {
            	associatedUnits.add(u.unitName);
            }
        }
        
        if(units!=null)
        {
        	for (Unit member : units) 
            {
            	if(!associatedUnits.contains(member.unitName))
            	{
            		unitNames.add(member.unitName);
                    availableUnits.add(Util.createMap("name", member.unitName, "availableSpace", getAvailableSpace(user, member.unitName)));
            	}
            }
        }
        
        Collections.sort(availableUnits, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1=(String) o1.get("name");
				String name2=(String) o2.get("name");
				return name1.compareTo(name2);
			}
		}); 
        
        writeJSON(resp, availableUnits);
    }

    /**
     * Add new members to a project with a role.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addMembers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String namesString = getRequiredParam("names", req);
        String roleString = getRequiredParam("role", req);
        String userLogin = getRequiredParam("userLogin", req);
        Permission permission = Permission.valueOf(roleString);
        
        
        if("".equals(projectName)){
        	projectName=null;
        }

        if("".equals(userLogin)){
        	userLogin=null;
        }
        
        List<String> names = gson.fromJson(namesString, new TypeToken<List<String>>() {
        }.getType());
        logger.logp(Level.INFO, "ManageServlet", "addMembers", "add members : " + user + " for project: " + projectName
                +"of for user : " + userLogin + " members: " + names + " role: " + permission);

        ProjectManager pm = SysManagerFactory.getProjectManager();
        UserPermissionManager upm = SysManagerFactory.getUserPermissionManager();

        if(projectName != null){
        // Add new members
        	for (String loginName : names) {
        		if (upm.getUserPermission(loginName, projectName).equals(Permission.None)) {
        			// User has no permission.
        			pm.addProjectMembers(user, projectName, permission, loginName);
        		}
        	}
        }else if(userLogin!=null){
        	for (String projName : names) {
        		if (upm.getUserPermission(userLogin, projName).equals(Permission.None)) {
        			// User has no permission.
        			pm.addProjectMembers(user, projName, permission, userLogin);
        		}
        	}
        }
        writeSuccess(resp);
    }
  
    /**
     * Add ldap users
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addLDAPUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        
        
        String rankString = getRequiredParam("rank", req);
        Rank rank = Rank.valueOf(rankString);
        
        String loginNamesString = getRequiredParam("names", req);
        List<String> loginNames = gson.fromJson(loginNamesString, new TypeToken<List<String>>() {
        }.getType());
        
        String emailsString = getRequiredParam("emails", req);
        List<String> emails = gson.fromJson(emailsString, new TypeToken<List<String>>() {
        }.getType());
        
        logger.logp(Level.INFO, "ManageServlet", "addLDAPUsers", "add ldap users : " + user + " members: " + loginNames + " rank: " + rank);
        
        UserManager um = SysManagerFactory.getUserManager();
        
        // Add new members
        for (int i=0;i<loginNames.size();i++) 
        {
        	String password = "CID"+loginNames.get(i)+"3#14";
            um.createUser(user, loginNames.get(i), password, AuthenticationType.External, rank, emails.get(i), loginNames.get(i));
        }
        writeSuccess(resp);
    }

    /**
     * Change membership {@link Permission} for a user-project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String loginName = getRequiredParam("name", req);
        String roleString = getRequiredParam("role", req);

        Permission permission = Permission.valueOf(roleString);
        logger.logp(Level.INFO, "ManageServlet", "editMember", "edit membership: " + user + " for project: "
                + projectName + " member: " + loginName + " role: " + permission);

        ProjectManager pm = SysManagerFactory.getProjectManager();
        pm.setUserPermission(user, loginName, projectName, permission);

        writeSuccess(resp);
    }

    /**
     * Remove membership for a user from project.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String loginName = getRequiredParam("name", req);

        logger.logp(Level.INFO, "ManageServlet", "removeMember", "remove membership: " + user + " for project: "
                + projectName + " member: " + loginName);

        ProjectManager pm = SysManagerFactory.getProjectManager();
        pm.removeProjectMembers(user, projectName, loginName);

        writeSuccess(resp);
    }

    /**
     * Get all the user annotations for a project
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getUserAnnotations(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);

        logger.logp(Level.INFO, "ManageServlet", "getUserAnnotations", "get user annotations: " + user
                + " for project: " + projectName);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        Collection<SearchColumn> annotationFields = SysManagerFactory.getProjectManager().getUserAnnotationFields(user,
                projectName);
        for (SearchColumn column : annotationFields) {
            Map<String, Object> next = new HashMap<String, Object>();
            next.put("name", column.getField());
            next.put("type", column.getType().toString());
            next.put("projectName", projectName);
            ret.add(next);
        }

        writeJSON(resp, ret);
    }
    
    /**
     * Get history of user logins for the system
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    public void getLoginHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	
    	String user=req.getParameter("user");
    	if(user!=null && user.trim().equals("")==true){
    		user=null;
    	}
    	
    	String appName=req.getParameter("appName");
    	if(appName!=null && appName.trim().equals("")==true){
    		appName=null;
    	}
    	
    	String appVersion=req.getParameter("appVersion");
    	if(appVersion!=null && appVersion.trim().equals("")==true){
    		appVersion=null;
    	}
    	
    	String fromDateString=req.getParameter("fromDate");
    	Date fromDate=null;
    	if(fromDateString!=null && fromDateString.trim().equals("")==false){
    		fromDate = new Date(Long.parseLong(fromDateString));
    	}
    	
    	String toDateString=req.getParameter("toDate");
    	Date toDate=null;
    	if(toDateString!=null && toDateString.trim().equals("")==false){
    		toDate = new Date(Long.parseLong(toDateString));
    	}
    	
    	String startStr=req.getParameter("start");
    	long start=0;
    	if(startStr != null){
    		start=Long.parseLong(startStr);
    	}
    	
    	String limitStr=req.getParameter("limit");
    	long limit=0;
    	if(limitStr != null){
    		limit=Long.parseLong(limitStr);
    	}
    	
    	Application application=null;
    	if(appName!= null  && appVersion!=null){
    		application=new Application(appName, appVersion);
    	}
    	
    	UserManager userManager= SysManagerFactory.getUserManager();
    	List<LoginHistoryObject> loginHistoryObjects= userManager.getLoginHistory(application, 
    			user, fromDate, toDate, limit, start);
    	
    	long count = userManager.countLoginHistory(application, 
    			user, fromDate, toDate);
    	writeJSON(resp, Util.createMap("items",loginHistoryObjects, "total", count));
    	logger.logp(Level.INFO, "ManageServlet", "getLoginHistory", "matches "+count+" loginHistoryObjects");
    }
  public void getProjectLoginHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
	
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
        String user = getCurrentUser(req);
        
        
		String startStr = getOptionalParam("start", req);
		int start = 0;
		if (startStr != null)
		{
			start = Integer.parseInt(startStr);
		}

		String limitStr = getOptionalParam("limit", req);
		int limit = 0;
		if (limitStr != null)
		{
			limit = Integer.parseInt(limitStr);
		}
        

    	logger.logp(Level.INFO, "ManageServlet", "getProjectLoginHistory", "start = " + start  +"limit" + limit);

    	UserPermissionManager upm  = SysManagerFactory.getUserPermissionManager();
    	//upm.projectAccessMap ;
    	
    	Map <ProjectAccessKey , ProjectAccess> map = new TreeMap<ProjectAccessKey, ProjectAccess>();
    	map = upm.getProjectAccessMap();
    	
      	logger.logp(Level.INFO, "ManageServlet", "getProjectLoginHistory", "project access map current size :" +  map.size());
      	int i = 0; 
    	Iterator<Map.Entry<ProjectAccessKey, ProjectAccess>> entries = map.entrySet().iterator();
    	
    	
    	while (entries.hasNext()) {
    		
    		if(i < start ){
    			i++;
    			entries.next();    	
    			continue;
    		}
    		if(i> start + limit)break;
    		
    	    Map.Entry<ProjectAccessKey, ProjectAccess> entry = entries.next();
    		Map<String, Object> value = new HashMap<String, Object>();
            value.put("userLogin", entry.getKey().getUserLogin());
            value.put("projectName",entry.getKey().getProjectName());
    		value.put("accessTime",entry.getValue().getAccessTime().toString());
	 		value.put("accessType", entry.getValue().getUserAction().toString());
            
            ret.add(value);
            i++;
    	    
    	}

    	
    	logger.logp(Level.INFO, "ManageServlet", "getProjectLoginHistory", "map size = " + ret.size()  +":" +i);
    	writeJSON(resp, ret);
 
  }
    
    /**
     * Remove user annotation with the given name in the given project.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeUserAnnotation(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String user = getCurrentUser(req);
        String projectName = getRequiredParam("projectName", req);
        String columnName = getRequiredParam("name", req);

        logger.logp(Level.INFO, "ManageServlet", "removeUserAnnotation", "remove user annotations: " + columnName
                + " for project: " + projectName + " user: " + user);

        ProjectManager pm = SysManagerFactory.getProjectManager();
        Project project = pm.getProject(projectName);
        pm.removeUserAnnotation(getWebApplication(), user, null, project.getID(), columnName);

        writeSuccess(resp);
    }
    
    /**
     * lists unit types
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listUnitTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	 String user = getCurrentUser(req);
         logger.logp(Level.INFO, "ManageServlet", "listUnitTypes", "list user types : " + user);

         List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
         UnitType[] types = UnitType.values();
         if(types!=null)
         {
        	 for(UnitType type:types)
        	 {
        		 ret.add(Util.createMap("name", type.name(), "value", type.name()));
        	 }
         }
         writeJSON(resp, ret);
    }
    
    /**
     * returns list of all available units
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listUnits(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	 String user = getCurrentUser(req);

         logger.logp(Level.INFO, "ManageServlet", "listUnits", "list units : " + user);
         List<Unit> units = SysManagerFactory.getUnitManager().listUnits(user);
         
         List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
         
         if (units != null) 
         {
             for (Unit unit : units) 
             {
            	 Map<String, Object> unitDetails = new HashMap<String, Object>();
            	 
                 unitDetails.put("name", unit.unitName);
                 unitDetails.put("email", unit.getPointOfContact());
                 unitDetails.put("globalStorage", unit.getGlobalStorage());
                 unitDetails.put("type", unit.getType());
                 
                 ret.add(unitDetails);
             }
         }
         writeJSON(resp, ret);
    }
    
    /**
     * add new unit
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addUnit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
        String unitName = getRequiredParam("name", req);
        
        String type = getRequiredParam("type", req);
        UnitType unitType = UnitType.valueOf(type);
        
        String emailID = getRequiredParam("email", req);
        
        String quotaString = getRequiredParam("globalStorage", req);
        double quota = Double.parseDouble(quotaString);

        logger.logp(Level.INFO, "ManageServlet", "addUnit", "add unit");
        
        try
		{
        	SysManagerFactory.getUnitManager().createUnit(user, unitName, unitType, quota, emailID);
        	writeSuccess(resp);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ManageServlet", "addUnit", "failure in add unit", e);
			writeFailure(resp, "unable to add unit");
		}
    }
    
    /**
     * returns the amount of space allocated to projects from specified unit
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAllocatedSpace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
        String unitName = getRequiredParam("name", req);
        
        List<UnitAssociation> associations = SysManagerFactory.getUnitManager().getUnitAssociations(user, unitName, null);
        
        double allocated = 0.0;
        if(associations!=null)
        {
        	for(UnitAssociation association:associations)
        	{
        		allocated += association.storageSpace;
        	}
        }
        
		writeJSON(resp, Util.createMap("name", unitName, "allocatedQuota", allocated));
    }
    
    /**
     * returns free space under specified project
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getProjectUnusedSpace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String projectName = getRequiredParam("projectName", req);
    	
    	double usage = SysManagerFactory.getProjectManager().getProject(projectName).getSpaceUsage();
    	double quota = SysManagerFactory.getProjectManager().getProject(projectName).getStorageQuota();
    	
    	writeJSON(resp, Util.createMap("projectName", projectName, "unused", (quota - usage)));
    }
    
    /**
     * returns the space used by specified project
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getProjectSpaceUsage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String projectName = getRequiredParam("projectName", req);
    	
    	double usage = SysManagerFactory.getProjectManager().getProject(projectName).getSpaceUsage();
    	
    	writeJSON(resp, Util.createMap("projectName", projectName, "usage", (usage)));
    }
    
    /**
     * returns the free space available with unit 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getUnitMaxAvailable(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
    	String unitName = getRequiredParam("unitName", req);
    	
    	double available = getAvailableSpace(user, unitName);
    	
    	writeJSON(resp, Util.createMap("unitName", unitName, "available", (available)));
    }
    
    private double getAvailableSpace(String user, String unitName) throws DataAccessException
    {
    	List<UnitAssociation> associations = SysManagerFactory.getUnitManager().getUnitAssociations(user, unitName, null);
    	
    	double available = SysManagerFactory.getUnitManager().getUnit(user, unitName).getGlobalStorage();
    	if(associations!=null)
    	{
    		for(UnitAssociation association:associations)
    		{
    			available -= association.storageSpace;
    		}
    	}
    	
    	return available;
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void isSafeToDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
        String unitName = getRequiredParam("unitName", req);
        
        boolean safeToDelete = SysManagerFactory.getUnitManager().isSafeToDelete(user, unitName);
        
        writeJSON(resp, Util.createMap("unitName", unitName, "safeToDelete", (safeToDelete)));
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editAssociation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
    	
        String unitName = getRequiredParam("unitName", req);
        
        String projectName = getRequiredParam("projectName", req);
        
        String newSpaceString = getRequiredParam("globalStorage", req);
        Double newSpace = Double.parseDouble(newSpaceString);
        
        logger.logp(Level.WARNING, "ManageServlet", "editAssociation", "");
        
        try
		{
        	SysManagerFactory.getUnitManager().updateAssociation(user, projectName, unitName, newSpace);
        	writeSuccess(resp);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ManageServlet", "editAssociation", "failure in editAssociation", e);
			writeFailure(resp, "unable to add unit");
		}
    }
    
    /**
     * edit unit
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editUnit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
        String unitName = getRequiredParam("name", req);
        
        String type = getRequiredParam("type", req);
        UnitType unitType = UnitType.valueOf(type);
        
        String emailID = getRequiredParam("email", req);
        
        String quotaString = getRequiredParam("globalStorage", req);
        double quota = Double.parseDouble(quotaString);

        logger.logp(Level.INFO, "ManageServlet", "addUnit", "add unit");
        
        try
		{
        	SysManagerFactory.getUnitManager().updateUnitDetails(user, unitName, unitType, emailID);
        	SysManagerFactory.getUnitManager().updateUnitSpace(user, unitName, quota);
        	writeSuccess(resp);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ManageServlet", "addUnit", "failure in add unit", e);
			writeFailure(resp, "unable to add unit");
		}
    }
    
    /**
     * edit unit
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeUnit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
        String unitName = getRequiredParam("unitName", req);
        
        logger.logp(Level.INFO, "ManageServlet", "removeUnit", "removeUnit");
        
        try
		{
        	SysManagerFactory.getUnitManager().removeUnit(user, unitName);
        	writeSuccess(resp);
		}
		catch (Exception e)
		{
			logger.logp(Level.WARNING, "ManageServlet", "removeUnit", "failure in removeUnit", e);
			writeFailure(resp, "unable to add unit");
		}
    }

    /**
     * Delete given records.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteRecords(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String recordidsString = getRequiredParam(RequestConstants.RECORD_IDS_KEY, req);

        List<Long> recordIDs = gson.fromJson(recordidsString, (new TypeToken<List<Long>>() {
        }).getType());
        logger.logp(Level.INFO, "ManageServlet", "deleteRecords", "delete records: " + recordIDs + "user: " + user);

        StorageManager storageManager = SysManagerFactory.getStorageManager();
        for (long recordid : recordIDs) {
            storageManager.deleteRecord(getWebApplication(), user, null, recordid);
        }

        writeSuccess(resp);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws IOException
     */
    public void getWebAdminApplicationVersion(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
    	writeJSON(resp, Util.createMap("name",Constants.getWebAdminApplicationName(),
    			"version", Constants.getWebApplicationVersion()));
    }
    

    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listAllDownloads(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
     	logger.logp(Level.INFO, "MaganeServlet", "listAllDownloads", "All Users");
    	
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
    	
    	List<ExportRequest> exportRequests = SysManagerFactory.getExportManager().getAllExportRequsts(user);
    	if(exportRequests!=null) {
    		for(ExportRequest request:exportRequests){
        		Map<String, Object> value = new HashMap<String, Object>();
                value.put("name", request.name);
                value.put("format",request.format);
        		value.put("validity",new Date(request.validTill));
		 		value.put("status", request.getStatus().name().toLowerCase());
		 		value.put("id", request.requestId);
		 		value.put("link", request.getURL());
		 		
		 		DecimalFormat formatter = new DecimalFormat("#.##"); 
                double size = Double.valueOf(formatter.format(request.size/(1024.0*1024)));
                
                String formattedSize = size+" MB";
                value.put("size", formattedSize);
                
                ret.add(value);
        	}
    		
    	}

    	writeJSON(resp, ret);
    } 

    /**
     * Convert access token object to a map fit for transfer to client. The
     * transported map <b>should</b> not contain the id of the token in it.
     * 
     * @return map version of the token
     * @throws DataAccessException
     */
    private Map<String, Object> getAccessTokenMap(IAccessToken token) throws DataAccessException {
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        tokenMap.put("id", token.getId());
        tokenMap.put("client", authManager.getClient(token.getClientID()).toString());
        tokenMap.put("userLogin", token.getUser());
        tokenMap.put("creationTime", token.getCreationTime().getTime());
        tokenMap.put("expiryTime", token.getExpiryTime().getTime());
        tokenMap.put("accessTime", token.getLastAccessTime().getTime());
        String valid ;
        if(token.isValid()){
        	valid = "enabled" ;
        }else {
        	valid = "disabled" ;
        }
        tokenMap.put("validity", valid) ;
        return tokenMap;
    }
    
    /**
     * lists all the active auth codes
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void listAllAuthCodes (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	  String user = getCurrentUser(req);
          logger.logp(Level.INFO, "ManageServlet", "listTokens", "List tokens: ");
          AuthorizationManager authorizationManager = SysManagerFactory.getAuthorizationManager();
          List<IAccessToken> allTokens = authorizationManager.listTokens(user);

          List<Map<String, Object>> tokens = new ArrayList<Map<String, Object>>();
          for (IAccessToken iAccessToken : allTokens) {
              tokens.add(getAccessTokenMap(iAccessToken));
          }

          writeJSON(resp, tokens);
    }
    
    /**
     * lists all the active acquisition client licenses
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	public void listAllAcqLicenses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		logger.logp(Level.INFO, "ManageServlet", "listAllAcqLicenses", "List acquisition client licenses: ");

		List<LicenseIdentifier> allLicenses = SysManagerFactory.getMicroscopeManager().listActiveLicenses(user);
		List<Map<String, Object>> licenses = new ArrayList<Map<String, Object>>();
		for (LicenseIdentifier license : allLicenses)
		{
			licenses.add(getLicenseIdentifierMap(license));
			logger.logp(Level.INFO, "ManageServlet", "listAllAcqLicenses", "license issued for mac= "+license.macAddress);
		}

		writeJSON(resp, licenses);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getLicenseCounts(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		
		writeJSON(resp, SysManagerFactory.getMicroscopeManager().getLicenseCounts());
	}
	
	/**
	 * surrenders acquisition license associated with specified access token
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void surrenderAcqLicense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		logger.logp(Level.INFO, "ManageServlet", "listAllAcqLicenses", "List acquisition client licenses: ");

		String accessToken = getRequiredParam("accessToken", req);
		SysManagerFactory.getMicroscopeManager().surrenderAcquisitionLicense(accessToken);
		
		writeSuccess(resp);
	}
	
	/**
     * converts the LicenseIdentifier object to map of name value pair
     * @param license license identifier
     * @return map representation of the license identifier
     */
    private Map<String, Object> getLicenseIdentifierMap(LicenseIdentifier license)
    {
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	map.put("macAddress", license.macAddress);
    	map.put("ipAddress", license.ipAddress);
    	String user = "";
    	try
		{
    		user = SysManagerFactory.getAuthorizationManager().getUser(license.accessToken);
		}
		catch (Exception e)
		{}
    	map.put("user", user);
    	map.put("creationTime", license.timeOfIssue);
    	map.put("accessToken", license.accessToken);
    	
    	return map;
    }
    
    /**
     * disables specified auth code
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void disableAuthCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
		String user = getCurrentUser(req);
		String id = getRequiredParam("id", req);

		logger.logp(Level.INFO, "ManageServlet", "disableAuthCode", "with id: " + id);

		AuthorizationManager authorizationManager = SysManagerFactory.getAuthorizationManager();
		authorizationManager.disableAuthCode(id, user);
    }
    
    /**
     * attempts to disable upload ticket if possible
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void disableUploadTicket(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
		String id = getRequiredParam("id", req);
		
		long ticketId = Long.parseLong(id);

		logger.logp(Level.INFO, "ManageServlet", "disableUploadTicket", "with id: " + id);

		SysManagerFactory.getTicketManager().cancelUpload(user, ticketId);
    }
    
    /**
     * register new microscope
     * @param req
     * 			microscopeName : name of microscope
     * 			ipAddress : ip address of the microscope
     * 			macAddress : mac address of the microscope
     * 			licenses : number of acq licenses to be reserved for this microscope
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void registerMicroscope(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
    	String microscopName = getRequiredParam("microscopeName", req);
    	String ipAddress = getRequiredParam("ipAddress", req);
    	String macAddress = getRequiredParam("macAddress", req);
    	macAddress = macAddress.toLowerCase();
    	
    	String licenseString = getRequiredParam("licenses", req);
    	int licenses = Integer.parseInt(licenseString);
    	
    	try
		{
			SysManagerFactory.getMicroscopeManager().registerMicroscope(user, microscopName, ipAddress, macAddress, licenses);
		}
		catch (DataAccessException e)
		{
			writeFailure(resp, "Failed to register Microscope");
			return;
		}
    	
    	writeSuccess(resp);
    }
    
    /**
     * edit specified microscope to new values
     * @param req
     * 				microscopeName: old name
     * 				newName: new name
     * 				ipAddress: new ip address
     * 				macAddress: new mac address
     * 				licenses: new number of licenses to be reserved
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editMicroscope(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
    	String microscopName = getRequiredParam("microscopeName", req);
    	String newName = getRequiredParam("newName", req);
    	String ipAddress = getRequiredParam("ipAddress", req);
    	String macAddress = getRequiredParam("macAddress", req);
    	macAddress = macAddress.toLowerCase();
    	
    	String licenseString = getRequiredParam("licenses", req);
    	int licenses = Integer.parseInt(licenseString);
    	
    	try
    	{
    		SysManagerFactory.getMicroscopeManager().updateMicroscope(user, microscopName, new MicroscopeObject(newName, macAddress, ipAddress, licenses));
    	}
    	catch (DataAccessException e)
		{
			writeFailure(resp, "Failed to edit Microscope");
			return;
		}
    	
    	writeSuccess(resp);
    }
    
    /**
     * delete specified microscope from the system
     * @param req
     * 			microscopeName: name of the specified microscope
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteMicroscope(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	String microscopName = getRequiredParam("microscopeName", req);
    	
    	SysManagerFactory.getMicroscopeManager().deleteMicroscope(user, microscopName);
    	
    	writeSuccess(resp);
    }
    
    public void addAcquisitionProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
    	String profileName = getRequiredParam("profileName", req);
    	
    	String microscopName = getRequiredParam("microscopeName", req);
    	
    	String xPixelString = getOptionalParam("xPixelSize", req);
    	Double xPixelSize = null;
    	try
		{
    		xPixelSize = Double.parseDouble(xPixelString);
		}
		catch (Exception e)
		{}
    	
    	String yPixelString = getOptionalParam("yPixelSize", req);
    	Double yPixelSize = null;
    	try
		{
    		yPixelSize = Double.parseDouble(yPixelString);
		}
		catch (Exception e)
		{}
    	
    	String zPixelString = getOptionalParam("zPixelSize", req);
    	Double zPixelSize = null;
    	try
		{
    		zPixelSize = Double.parseDouble(zPixelString);
		}
		catch (Exception e)
		{}
    	
//    	suppressing sourceType information from acq profiles
//    	String sourceType = getRequiredParam("sourceType", req);
    			
    	String lengthUnit = getOptionalParam("lengthUnit", req);
    	LengthUnit lUnit = null;
    	try
		{
    		lUnit = LengthUnit.valueOf(lengthUnit);
		}
		catch (Exception e)
		{}
    	
    	String timeUnit = getOptionalParam("timeUnit", req);
    	TimeUnit tUnit = null;
    	try
		{
    		tUnit = TimeUnit.valueOf(timeUnit);
		}
		catch (Exception e)
		{}
    	
    	String exposureTimeUnit = getOptionalParam("exposureTimeUnit", req);
    	TimeUnit exposureTUnit = null;
    	try
		{
    		exposureTUnit = TimeUnit.valueOf(exposureTimeUnit);
		}
		catch (Exception e)
		{}
    	
    	logger.logp(Level.INFO, "ManageServlet", "addAcqProfiles", "profles : " +xPixelSize+" "+yPixelSize+" "+zPixelSize+" "+timeUnit+" "+lengthUnit);
    	
    	
    	AcquisitionProfileType xType=AcquisitionProfileType.VALUE;
    	String xTypeString = getOptionalParam("xType", req);
    	try
		{
    		xType = AcquisitionProfileType.valueOf(xTypeString);
		}
		catch (Exception e)
		{}
    	
     	AcquisitionProfileType yType=AcquisitionProfileType.VALUE;
    	String yTypeString = getOptionalParam("yType", req);
    	try
		{
    		yType = AcquisitionProfileType.valueOf(yTypeString);
		}
		catch (Exception e)
		{}
    	
    	AcquisitionProfileType zType=AcquisitionProfileType.VALUE;
    	String zTypeString = getOptionalParam("zType", req);
    	try
		{
    		zType = AcquisitionProfileType.valueOf(zTypeString);
		}
		catch (Exception e)
		{}
    	
    	logger.logp(Level.INFO, "ManageServlet", "addAcqProfiles", "profles : " +xTypeString+" "+yTypeString+" "+zTypeString);

    	try
    	{
    		SysManagerFactory.getMicroscopeManager().createAcquisitionProfile(profileName, microscopName, xPixelSize, xType, yPixelSize, yType, zPixelSize, zType, null, tUnit, exposureTUnit, lUnit);
    	}
    	catch(DataAccessException exception)
    	{
    		writeFailure(resp, exception.getMessage());
    		return;
    	}
    	
    	writeSuccess(resp);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void editAcquisitionProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
    	
    	String profileName = getRequiredParam("profileName", req);
    	
    	String microscopName = getRequiredParam("microscopeName", req);
    	
    	String xPixelString = getRequiredParam("xPixelSize", req);
    	Double xPixelSize = null;
    	try
		{
    		xPixelSize = Double.parseDouble(xPixelString);
		}
		catch (Exception e)
		{}
    	
    	String yPixelString = getRequiredParam("yPixelSize", req);
    	Double yPixelSize = null;
    	try
		{
    		yPixelSize = Double.parseDouble(yPixelString);
		}
		catch (Exception e)
		{}
    	
    	String zPixelString = getRequiredParam("zPixelSize", req);
    	Double zPixelSize = null;
    	try
		{
    		zPixelSize = Double.parseDouble(zPixelString);
		}
		catch (Exception e)
		{}
    	
//    	suppressing sourceType information from acq profiles
//    	String sourceType = getRequiredParam("sourceType", req);
    			
    	String lengthUnit = getRequiredParam("lengthUnit", req);
    	LengthUnit lUnit = null;
    	try
		{
    		lUnit = LengthUnit.valueOf(lengthUnit);
		}
		catch (Exception e)
		{}
    	
    	String timeUnit = getRequiredParam("timeUnit", req);
    	TimeUnit tUnit = null;
    	try
		{
    		tUnit = TimeUnit.valueOf(timeUnit);
		}
		catch (Exception e)
		{}
    	
    	String exposureTimeUnit = getRequiredParam("exposureTimeUnit", req);
    	TimeUnit exposureTUnit = null;
    	try
		{
    		exposureTUnit = TimeUnit.valueOf(exposureTimeUnit);
		}
		catch (Exception e)
		{}
    	
    	
    	AcquisitionProfileType xType=AcquisitionProfileType.VALUE;
    	String xTypeString = getRequiredParam("xType", req);
    	try
		{
    		xType = AcquisitionProfileType.valueOf(xTypeString);
		}
		catch (Exception e)
		{}
    	
     	AcquisitionProfileType yType=AcquisitionProfileType.VALUE;;
    	String yTypeString = getRequiredParam("yType", req);
    	try
		{
    		yType = AcquisitionProfileType.valueOf(yTypeString);
		}
		catch (Exception e)
		{}
    	
    	AcquisitionProfileType zType=AcquisitionProfileType.VALUE;;
    	String zTypeString = getRequiredParam("zType", req);
    	try
		{
    		zType = AcquisitionProfileType.valueOf(zTypeString);
		}
		catch (Exception e)
		{}
    	
    	try
    	{
    		SysManagerFactory.getMicroscopeManager().createAcquisitionProfile(profileName, microscopName, xPixelSize, xType, yPixelSize, yType, zPixelSize, zType, null, tUnit, exposureTUnit, lUnit);
    	}
    	catch(DataAccessException exception)
    	{
    		writeFailure(resp, exception.getMessage());
    		return;
    	}
    	
    	writeSuccess(resp);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteAcquisitionProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String user = getCurrentUser(req);
    	
    	String profileName = getRequiredParam("profileName", req);
    	
    	String microscopeName = getRequiredParam("microscopeName", req);
    	
    	try
    	{
    		SysManagerFactory.getMicroscopeManager().deleteAcquisitionProfile(user, profileName, microscopeName);
    	}
    	catch(DataAccessException exception)
    	{
    		writeFailure(resp, exception.getMessage());
    		return;
    	}
    	
    	writeSuccess(resp);
    }
    
    /**
     * returns list of microscopes in resp
     * @param req
     * @param resp
     * 			json representation of list of registered microscopes
     * 			microscopeName : name of microscope
     * 			ipAddress : ipAddress of microscope
     * 			macAddress : macAddress of the microscopes
     * 			licenses : number of acq licenses reserved for the microscope
     * @throws ServletException
     * @throws IOException
     */
	public void listMicroscopes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

		List<MicroscopeObject> microscopes = SysManagerFactory.getMicroscopeManager().listMicroscopes(user);

		if (microscopes != null)
		{
			for (MicroscopeObject microscope : microscopes)
			{

				Map<String, Object> value = new HashMap<String, Object>();
				value.put("microscopeName", microscope.microscope_name);
				value.put("ipAddress", microscope.ip_address);
				value.put("macAddress", microscope.mac_address);
				value.put("licenses", microscope.getAcquisitionLicenses());
				
				ret.add(value);
			}
		}
		writeJSON(resp, ret);
	}
	
	public void setAcqProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		
		String guids = getRequiredParam("guids", req);
        List<Long> recordIDs = gson.fromJson(guids, (new TypeToken<List<Long>>() {
        }).getType());
        
        String profiles = getRequiredParam("profileName", req);
        List<String> profileNames = gson.fromJson(profiles, (new TypeToken<List<String>>() {
        }).getType());
        String profileName="";
        for(String name: profileNames){
        	profileName+=name+" ";
        }
        
        String microscopeName = getRequiredParam("microscopeName", req);

        String xPixelSize = getOptionalParam("xPixelSize", req);       
        List<String> xPixelSizesString = gson.fromJson(xPixelSize, (new TypeToken<List<String>>() {
        }).getType());
        
        
        List<Double> xSizeArray = new ArrayList<Double>();
        try
		{
        	for(String x: xPixelSizesString)
        		xSizeArray.add(Double.parseDouble(x));
		}
		catch (Exception e)
		{}
        
        String yPixelSize = getOptionalParam("yPixelSize", req);
        List<String> yPixelSizesString = gson.fromJson(yPixelSize, (new TypeToken<List<String>>() {
        }).getType());
        
        List<Double> ySizeArray = new ArrayList<Double>();
        try
		{
        	for(String y: yPixelSizesString)
        		ySizeArray.add(Double.parseDouble(y));
		}
		catch (Exception e)
		{}
        
        
        String zPixelSize = getOptionalParam("zPixelSize", req);
        List<String> zPixelSizesString = gson.fromJson(zPixelSize, (new TypeToken<List<String>>() {
        }).getType());
        
        List<Double> zSizeArray = new ArrayList<Double>();
        try
		{
        	for(String z: zPixelSizesString)
        		zSizeArray.add(Double.parseDouble(z));
		}
		catch (Exception e)
		{}
        
//        suppressing sourceType information
//        String sourceFormat = getRequiredParam("sourceFormat", req);
//        SourceFormat sourceType = new SourceFormat(sourceFormat);
        
        String timeUnit = getRequiredParam("timeUnit", req);
        List<String> timeUnitString = gson.fromJson(timeUnit, (new TypeToken<List<String>>() {
        }).getType());
        TimeUnit timeunit=null;
        if(timeUnitString.size()!=0)
        	timeunit=TimeUnit.valueOf(timeUnitString.get(timeUnitString.size()-1));
        
        String exposureTimeUnit = getOptionalParam("exposureTimeUnit", req);
        if(exposureTimeUnit==null)
        	exposureTimeUnit = timeUnit;
        List<String> exposureTimeUnitString = gson.fromJson(exposureTimeUnit, (new TypeToken<List<String>>() {
        }).getType());
        TimeUnit exposuretimeunit=null;
        if(timeUnitString.size()!=0)
        	exposuretimeunit=TimeUnit.valueOf(exposureTimeUnitString.get(exposureTimeUnitString.size()-1));
        
        
        String lengthUnit = getRequiredParam("lengthUnit", req);
        List<String> lengthUnitString = gson.fromJson(lengthUnit, (new TypeToken<List<String>>() {
        }).getType()); 
        
        LengthUnit lengthunit=null;
        if(lengthUnitString.size()!=0)
        	lengthunit=LengthUnit.valueOf(lengthUnitString.get(lengthUnitString.size()-1));
        
        //x
        String xType = getOptionalParam("xType", req);
        List<String> xTypeString = gson.fromJson(xType, (new TypeToken<List<String>>() {
        }).getType());
        
    	List<AcquisitionProfileType> xTypeAcq =new ArrayList<AcquisitionProfileType>(); 
    	for(String x : xTypeString){
    		if(x==null)
    			xTypeAcq.add(AcquisitionProfileType.VALUE);
    		xTypeAcq.add(AcquisitionProfileType.valueOf(x));
    	}
    	
    	//y
        String yType = getOptionalParam("yType", req);
        List<String> yTypeString = gson.fromJson(yType, (new TypeToken<List<String>>() {
        }).getType());
        
    	List<AcquisitionProfileType> yTypeAcq =new ArrayList<AcquisitionProfileType>(); 
    	for(String y : yTypeString){
    		if(y==null)
    			yTypeAcq.add(AcquisitionProfileType.VALUE);
    		yTypeAcq.add(AcquisitionProfileType.valueOf(y));
    	}
    	
    	//z
        String zType = getOptionalParam("zType", req);
        List<String> zTypeString = gson.fromJson(zType, (new TypeToken<List<String>>() {
        }).getType());
        
    	List<AcquisitionProfileType> zTypeAcq =new ArrayList<AcquisitionProfileType>(); 
    	for(String z : zTypeString){
    		if(z==null)
    			zTypeAcq.add(AcquisitionProfileType.VALUE);
    		zTypeAcq.add(AcquisitionProfileType.valueOf(z));
    	}
    	    	
    	
    	Map<String,Object> profileValues=null;
    	
    	Double xSize=null;
    	AcquisitionProfileType xtype=null;
    	if(xSizeArray.size()!=0){
	    	profileValues=SysManagerFactory.getRecordManager().getPixelProfile(xSizeArray, xTypeAcq);
    		xSize=(Double) profileValues.get("pixelSize");
    		xtype=(AcquisitionProfileType) profileValues.get("type");
    	}
    	
    		
        Double ySize=null;
        AcquisitionProfileType ytype=null;	
        if(ySizeArray.size()!=0){
	    	profileValues=SysManagerFactory.getRecordManager().getPixelProfile(ySizeArray, yTypeAcq);
	    	ySize=(Double) profileValues.get("pixelSize");
	    	ytype=(AcquisitionProfileType) profileValues.get("type");
		}
	    	
	    Double zSize=null;
	    AcquisitionProfileType ztype=null;
	    if(zSizeArray.size()!=0){
	    	profileValues=SysManagerFactory.getRecordManager().getPixelProfile(zSizeArray, zTypeAcq);
	    	zSize=(Double) profileValues.get("pixelSize");
	    	ztype=(AcquisitionProfileType) profileValues.get("type");
	    }
    	
    	logger.logp(Level.INFO, "ManageServlet", "setAcqProfiles", "profles : " +xSize+" "+ySize+" "+zSize+" "+xtype+" "+ytype+" "+ztype+" "+timeunit+" "+lengthunit);
    	
        AcquisitionProfile acqProfile = new AcquisitionProfile(profileName, microscopeName, xSize, xtype, ySize, ytype, zSize, ztype, null, timeunit, exposuretimeunit, lengthunit);
        
        for(Long guid:recordIDs)
        	SysManagerFactory.getRecordManager().setAcquisitionProfile(getWebApplication(), user, null, guid, acqProfile);
	}
	
	/**
	 * list all the time unit values
	 * @param req
	 * @param resp
	 * 			json representation of TimeUnit enum
	 * 			name : TimeUnit.name
	 * 			value : TimeUnit.toString()
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listTimeUnits(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		TimeUnit[] values = TimeUnit.values();
		if(values!=null)
		{
			for(TimeUnit value:values)
			{
				Map<String, Object> entry = new HashMap<String, Object>();
				
				entry.put("name", value.name());
				entry.put("value", value.toString());
				
				ret.add(entry);
			}
		}
		
		writeJSON(resp, ret);
	}
	
	/**
	 * list all the length unit values
	 * @param req
	 * @param resp
	 * 			json representation of LengthUnit enum
	 * 			name : LengthUnit.name
	 * 			value : LengthUnit.toString()
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listLengthUnits(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		LengthUnit[] values = LengthUnit.values();
		if(values!=null)
		{
			for(LengthUnit value:values)
			{
				Map<String, Object> entry = new HashMap<String, Object>();
				
				entry.put("name", value.name());
				entry.put("value", value.toString());
				
				ret.add(entry);
			}
		}
		
		writeJSON(resp, ret);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listAcqProfileTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		AcquisitionProfileType[] values = AcquisitionProfileType.values();
		if(values!=null)
		{
			for(AcquisitionProfileType value:values)
			{
				Map<String, Object> entry = new HashMap<String, Object>();
				
				entry.put("name", value.name());
				entry.put("value", value.toString());
				
				ret.add(entry);
			}
		}
		
		writeJSON(resp, ret);
	}
	
	/**
	 * list all the active acquisition licenses
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listActiveAcquisitionLicenses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		
		SysManagerFactory.getMicroscopeManager().listActiveLicenses(user);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listAcqProfiles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		String microscopeName = getOptionalParam("microscopeName", req);

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

		List<AcquisitionProfile> profiles = SysManagerFactory.getMicroscopeManager().listAcquisitionProfiles();

		if (profiles != null && microscopeName!= null)
		{
			for (AcquisitionProfile profile : profiles)
			{
				if(!microscopeName.equals(profile.getMicroscope()))
					continue;
				
				Map<String, Object> value = new HashMap<String, Object>();
				
				value.put("profileName", profile.getProfileName());
				value.put("microscopeName", profile.getMicroscope());
				value.put("xPixelSize", profile.getxPixelSize());
				value.put("yPixelSize", profile.getyPixelSize());
				value.put("zPixelSize", profile.getzPixelSize());
				value.put("timeUnit", profile.getElapsedTimeUnit());
				value.put("exposureTimeUnit", profile.getExposureTimeUnit());
				value.put("lengthUnit", profile.getLengthUnit());
				
				if(profile.getxPixelSize()!=null)
					value.put("xType", profile.getXType());
				else
					value.put("xType", null);
				
				if(profile.getyPixelSize()!=null)
					value.put("yType", profile.getYType());
				else
					value.put("yType", null);
				
				if(profile.getzPixelSize()!=null)
					value.put("zType", profile.getZType());
				else
					value.put("zType", null);
				//value.put("sourceType", profile.getSourceType().name);
				
				ret.add(value);
			}
		}
		writeJSON(resp, ret);
	}
	
	/**
	 * returns list of all the uploads 
	 * @param req
	 * @param resp
	 * 			userLogin : user who is uploading
	 * 			projectName : target project to which upload is happening
	 * 			status : status of the upload
	 * 			requestTime : time when the upload request started
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listAllUploads(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);
		logger.logp(Level.INFO, "ManageServlet", "listTokens", "List tokens: ");

		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

		TicketManager ticketManager = SysManagerFactory.getTicketManager();
		List<Job> uploadTickets = ticketManager.getTickets();

		if (uploadTickets != null)
		{
			for (Job ticket : uploadTickets)
			{
				RecordCreationRequest request = ticketManager.getRequest(ticket.getTicketID());
				if(request==null)
					continue;
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("id", ticket.getTicketID());
				value.put("userLogin", request.getActor());
				value.put("projectName", request.getProject());
				value.put("status", ticket.getJobStatus());
				value.put("requestTime", new Date(ticket.getRequestTime()));

				ret.add(value);
			}
		}

		writeJSON(resp, ret);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void getWorkerStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		List<Service> services = SysManagerFactory.getWorkerManager().getAllServiceStatus();
		
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		Reflections reflection=new Reflections("com.strandgenomics.imaging.iengine.worker");
		Set<Class<?>> classes = reflection.getTypesAnnotatedWith(ServiceAnnotation.class);
		
		List<Integer> workers = new ArrayList<Integer>();
		
		if(services!=null){
			for(Service service : services){
				if(!workers.contains(service.getWorkerId()))
					workers.add(service.getWorkerId());
			}
		}
		
		for(Integer worker : workers)
			logger.logp(Level.INFO, "ManageServlet", "getWorkerStatus", "workeId="+worker);
		
		
		if(services!=null){
			
			for(Integer worker : workers){
				
				Map<String, Object> workerData = new HashMap<String, Object>();
				
				workerData.put("workerId", worker);
				
				for(Service service : services){
					
					if(service.getWorkerId()!=worker)
						continue;
						
					Class serviceClass = null;
					for(Class<?> c : classes){
						if(c.getAnnotation(ServiceAnnotation.class).type().equals(service.getServiceType()))
						{
							serviceClass = c;
						}
					}
					
					Set<Method> methods = ReflectionUtils.getAllMethods(serviceClass);
					
					Object obj = serviceClass.cast(service.getServiceStatus());
					
					Field fields[] = serviceClass.getDeclaredFields();
					
					for(Method method : methods){
						
						if(method.getName().startsWith("get")){
							if(fields!=null){
								for(Field field : fields){
									if(field.isAnnotationPresent(ServiceParameter.class)){
										if(method.getName().toLowerCase().contains(field.getName().toLowerCase())){
											workerData.put(field.getAnnotation(ServiceParameter.class).name(),method.invoke(obj, null));
										}
									}
								}
							}
						}
					}
					
				}
				
				ret.add(workerData);
			}
		}
		
		writeJSON(resp, ret);
	}
	
	/**
	 * get meta data related to worker
	 * this meta data will be service names and the parameter names for those services
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getWorkerMetaData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		ServiceType serviceTypes[] = ServiceType.values();
		
		if(serviceTypes!=null){
			
			for(ServiceType type : serviceTypes){
				
				Map<String, Object> serviceData = new HashMap<String, Object>();
				
				serviceData.put("serviceName",type.getName());
				serviceData.put("serviceType",type.toString());
				serviceData.put("serviceParameters", SysManagerFactory.getWorkerManager().getServiceParameters(type));
				
				ret.add(serviceData);
			}
		}
		
		writeJSON(resp, ret);
	}
	
	/**
	 * restart the worker service
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void restartService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		int workerId = Integer.valueOf(getRequiredParam("workerId", req));
		String serviceName = getRequiredParam("serviceName", req);
		
		logger.logp(Level.INFO, "ManageServlet", "restartService", "workeId="+workerId+" serviceName="+serviceName);
		
		SysManagerFactory.getWorkerManager().restartService(workerId, ServiceType.valueOf(serviceName));
	}
	
	/**
	 * get the type and count of each cache key
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getCacheStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
		CacheKeyType[] types = CacheKeyType.values();
		
		if(types!=null){
			
			for(CacheKeyType type : types){
				
				Map<String, Object> cacheDataForType = new HashMap<String, Object>();
				cacheDataForType.put("type", type.toString());
				cacheDataForType.put("count", SysManagerFactory.getCacheManager().getCount(type));
				
				ret.add(cacheDataForType);
			}
		}
		
		writeJSON(resp, ret);
	}
	
	
	/**
	 * clean cache for given type of cache key type
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void cleanCache(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		CacheKeyType type = CacheKeyType.valueOf(getRequiredParam("type", req));
		
		SysManagerFactory.getCacheManager().removeAll(type);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void cleanFullCache(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		logger.logp(Level.INFO, "ManageServlet", "clean","");
		SysManagerFactory.getCacheManager().removeAll();
	}
	
}
