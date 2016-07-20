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

/*
 * ComputeServlet.java
 *
 * Product:  AvadisIMG Server
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.app.ApplicationSpecification;
import com.strandgenomics.imaging.icore.app.ListConstraints;
import com.strandgenomics.imaging.icore.app.Parameter;
import com.strandgenomics.imaging.icore.app.Priority;
import com.strandgenomics.imaging.icore.app.RangeConstraints;
import com.strandgenomics.imaging.icore.app.State;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.util.Archiver;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.Service;
import com.strandgenomics.imaging.iengine.auth.IAccessToken;
import com.strandgenomics.imaging.iengine.compute.Publisher;
import com.strandgenomics.imaging.iengine.compute.Task;
import com.strandgenomics.imaging.iengine.dao.AuthCodeDAO;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.ProjectDAO;
import com.strandgenomics.imaging.iengine.models.AuthCode;
import com.strandgenomics.imaging.iengine.models.Client;
import com.strandgenomics.imaging.iengine.models.NotificationMessageType;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.system.AuthorizationManager;
import com.strandgenomics.imaging.iengine.system.ComputeEngine;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.TaskManager;
import com.strandgenomics.imaging.iengine.system.UserManager;

/**
 * Servlet for all compute related actions.
 * 
 * @author santhosh
 * 
 */
public class ComputeServlet extends ApplicationServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -9072179639970249320L;
    
    /**
     * 
     * @param req
     * @param resp
     * @throws NumberFormatException
     * @throws ServletException
     * @throws IOException
     */
    public void addFavouriteFolder(HttpServletRequest req, HttpServletResponse resp) throws NumberFormatException, ServletException, IOException{
    	
    	String user = getCurrentUser(req);
    	
    	String projectName=getRequiredParam("projectName", req);
    	long parentID=Long.valueOf(getRequiredParam("parentID", req));
    	String folderName = getRequiredParam("folderName", req);
    	
    	ProjectDAO projectDAO=ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
    	int projectID=projectDAO.findProject(projectName).getID();
    	
    	Map<String, Object> ret = new HashMap<String, Object>();

    	try
    	{
    		SysManagerFactory.getClientManager().createNewFolder(user, projectID, folderName, parentID);
            ret.put("success", true);
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
        writeJSON(resp, ret);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws NumberFormatException
     * @throws ServletException
     * @throws IOException
     */
    public void removeFavouriteFolder(HttpServletRequest req, HttpServletResponse resp) throws NumberFormatException, ServletException, IOException
    {
    	String user = getCurrentUser(req);
    	
    	String projectName=getRequiredParam("projectName", req);
    	long folderID=Long.valueOf(getRequiredParam("folderID", req));
    	
    	ProjectDAO projectDAO=ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
    	int projectID=projectDAO.findProject(projectName).getID();
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
    	
    	try
    	{
    		String[] clients=SysManagerFactory.getClientManager().getClientIDsInFolder(user, projectID, folderID);
        	
        	if(clients!=null){
        		for(String client: clients){
        			SysManagerFactory.getClientManager().removeClient(user, projectID, client);
        		}
        	}
        	
        	SysManagerFactory.getClientManager().removeFolder(user, projectID, folderID);
        	ret.put("success", true);
    	}
        catch(Exception e)
        {
        	ret.put("success", false);	
        }
        
        writeJSON(resp, ret);
    }
    
    /**
     * Get all client for a project
     * @param req
     * @param resp
     * @throws ServletException 
     * @throws NumberFormatException 
     * @throws IOException 
     */
    public void getProjectClients(HttpServletRequest req, HttpServletResponse resp) throws NumberFormatException, ServletException, IOException
    {
    	
		String user = getCurrentUser(req);

		String projectName = getOptionalParam("projectName", req);
		if(projectName==null || projectName.isEmpty())
		{
			writeFailure(resp, "Project name not found");
			return;
		}

		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		int projectID = projectDAO.findProject(projectName).getID();

		logger.logp(Level.INFO, "ComputeServlet", "getProjectClients", "listing all applications in project" + projectID);

		String actorName = getCurrentUser(req);

		List<ApplicationSpecification> allApps = SysManagerFactory.getComputeEngine().listAllApplications(actorName);
		List<String> allAppsID = new ArrayList<String>();

		for (ApplicationSpecification spec : allApps)
		{
			allAppsID.add(spec.getID());
		}

		List<Long> rootFolders = SysManagerFactory.getClientManager().getRootFolders(user, projectID);
		Map<String, List<ApplicationSpecification>> categoryToApp = new HashMap<String, List<ApplicationSpecification>>();
		Map<String, Long> categoryToId = new HashMap<String, Long>();

		if (rootFolders != null)
		{
			for (Long folderID : rootFolders)
			{

				String categoryName = SysManagerFactory.getClientManager().getFolderName(user, projectID, folderID);

				if (!categoryToApp.containsKey(categoryName))
				{
					String[] appIDs = SysManagerFactory.getClientManager().getClientIDsInFolder(user, projectID, folderID);
					List<ApplicationSpecification> catApps = new ArrayList<ApplicationSpecification>();

					if (appIDs != null)
					{
						for (String clientID : appIDs)
						{
							if (allAppsID.contains(clientID))
							{
								int index = allAppsID.indexOf(clientID);
								ApplicationSpecification app = allApps.get(index);
								catApps.add(app);
							}
						}
					}

					categoryToApp.put(categoryName, catApps);
					categoryToId.put(categoryName, folderID);
				}
			}
		}

		List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
		for (Entry<String, List<ApplicationSpecification>> entry : categoryToApp.entrySet())
		{
			String catName = entry.getKey();
			long folderID = categoryToId.get(catName);

			List<ApplicationSpecification> apps = entry.getValue();

			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ApplicationSpecification app : apps)
			{
				Map<String, Object> applicationMap = new HashMap<String, Object>();

				applicationMap.put("name", app.getName());
				applicationMap.put("version", app.getVersion());
				applicationMap.put("description", app.getDescription());
				applicationMap.put("id", app.getID());

				mapList.add(applicationMap);
			}
			Map<String, Object> cat = new HashMap<String, Object>();
			cat.put("name", catName);
			cat.put("folderID", folderID);
			cat.put("workflows", mapList);

			logger.logp(Level.INFO, "ComputeServlet", "getProjectClients", "folders in project" + projectID + " folder=" + cat);

			all.add(cat);
		}

		writeJSON(resp, all);
    }
    
    /**
     * Add a user client in a specific project
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
     public void addProjectClient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
    	String user = getCurrentUser(req);
    	 
    	String projectName=getRequiredParam("projectName", req);
        String clientID = getRequiredParam("clientID", req);
        long folderID = Long.valueOf(getRequiredParam("folderID", req));
        
    	ProjectDAO projectDAO=ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
    	int projectID=projectDAO.findProject(projectName).getID();

    	Map<String, Object> ret = new HashMap<String, Object>();
    	try
    	{
    		SysManagerFactory.getClientManager().addClient(user, projectID, clientID, folderID);
    		ret.put("success", true);
    	}
    	catch(Exception e)
    	{
    		ret.put("success", false);
    	}
         	
        writeJSON(resp, ret);
    }
    
    
    /**
     * Remove a client in a specific project
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	public void removeProjectClient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String user = getCurrentUser(req);

		String projectName = getRequiredParam("projectName", req);
		String clientID = getRequiredParam("clientID", req);

		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		int projectID = projectDAO.findProject(projectName).getID();

		try
		{
			SysManagerFactory.getClientManager().removeClient(user, projectID, clientID);
			writeJSON(resp, successResponse);
		}
		catch (Exception e)
		{
			writeFailure(resp, "Failed to remove client from project");
		}
	}
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAvailableWebClients(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String actor = getCurrentUser(req);

    	List<Client> allClients = SysManagerFactory.getAuthorizationManager().getAllClients();
    	List<Map<String, Object>> all = new ArrayList<Map<String,Object>>();
    	
    	List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
    	if(allClients!=null)
    	{
    		for(Client client:allClients)
    		{
    			String url = client.getWebClientUrl();
    			if(url!=null && !url.isEmpty())
    			{
    				Map<String, Object> obj = new HashMap<String, Object>();
    				obj.put("name", client.getName());
    				obj.put("version", client.getVersion());
    				obj.put("id", client.getClientID());
    				obj.put("url", url);
    				obj.put("description", client.getDescription());
    				
    				mapList.add(obj);
    			}
    		}
    	}
    	
    	Map<String, Object> cat = Util.createMap("name", "WebClients", "webApplication", mapList);
		all.add(cat);
    	
    	writeJSON(resp, all);
    }
    
    public void launchWebApplication(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);
        
        String clientid = getRequiredParam("clientid", req);

        logger.logp(Level.INFO, "ComputeServlet", "generateAuthCode", "access acq client: " + loginUser);
        long expiryTime = System.currentTimeMillis() + Constants.getAcqClientAuthCodeValidity() * 24 * 3600 * 1000;

        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        Client client = authManager.getClient(clientid);
        
        String authCode = authManager.generateAuthCode(loginUser, clientid, Service.values(), expiryTime, null);
        
        String clientUrl = client.getWebClientUrl();
        if(!clientUrl.startsWith("http"))
        	clientUrl = "http://"+clientUrl;
        if(!clientUrl.endsWith("/"))
        	clientUrl = clientUrl + "/";
        
        String gsonGuids = getRequiredParam("guids", req);
        String url = clientUrl + "?authcode=" + authCode +"&guids="+gsonGuids+"&host="+req.getServerName()+"&port="+req.getServerPort()+"&scheme="+req.getScheme();
        
        Map<String, Object> cat = Util.createMap("success", "true", "url", url);
        writeJSON(resp, cat);
    }
    
    /**
     * get workflows available to a project
     * which are not present in its favourites
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
	public void getAvailableWorkflows(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		logger.logp(Level.INFO, "ComputeServlet", "getAvailableWorkflows", "listing available applications");

		String actor = getCurrentUser(req);
		
		String projectName = getOptionalParam("projectName", req);
		if(projectName==null || projectName.isEmpty())
		{
			writeFailure(resp, "Project name not found");
			return;
		}
		
		ProjectDAO projectDAO = ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
		int projectID = projectDAO.findProject(projectName).getID();

		List projectApps = new ArrayList<String>();

		String[] projectClients = SysManagerFactory.getClientManager().getAllClients(actor, projectID);
		if (projectClients != null)
		{
			projectApps = Arrays.asList(projectClients);
		}

		String actorName = getCurrentUser(req);

		List<ApplicationSpecification> allApps = SysManagerFactory.getComputeEngine().listAllApplications(actorName);
		Map<String, List<ApplicationSpecification>> categoryToApp = new HashMap<String, List<ApplicationSpecification>>();
		
		List<String> clientIds=new ArrayList<String>();

		for (ApplicationSpecification app : allApps)
		{
			if(clientIds.contains(app.getID()))
				continue;
			
			clientIds.add(app.getID());
			
			String categoryName = app.getCategory();
			if (!categoryToApp.containsKey(categoryName))
			{
				List<ApplicationSpecification> catApps = new ArrayList<ApplicationSpecification>();
				categoryToApp.put(categoryName, catApps);
			}

			List<ApplicationSpecification> catApps = categoryToApp.get(categoryName);
			catApps.add(app);
		}

		List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
		for (Entry<String, List<ApplicationSpecification>> entry : categoryToApp.entrySet())
		{
			String catName = entry.getKey();
			List<ApplicationSpecification> apps = entry.getValue();

			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (ApplicationSpecification app : apps)
			{
				Map<String, Object> applicationMap = new HashMap<String, Object>();

				applicationMap.put("name", app.getName());
				applicationMap.put("version", app.getVersion());
				applicationMap.put("description", app.getDescription());
				applicationMap.put("id", app.getID());
				if (projectApps.contains(app.getID()))
					applicationMap.put("selected", true);
				else
					applicationMap.put("selected", false);

				mapList.add(applicationMap);
			}

			Map<String, Object> cat = Util.createMap("name", catName, "workflows", mapList);
			all.add(cat);
		}

		writeJSON(resp, all);
	}

    /**
     * Add a new client
     * 
     * URL Parameters:
     * name : name of the client
     * version : version of the client
     * description : description of the client
     * isWorkflow : true if its workflow link, false otherwise (this is optional parameter default value is false)
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addClient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);

        String name = getRequiredParam("name", req);
        String version = getRequiredParam("version", req);
        String tags = getRequiredParam("tags", req);
        String description = getRequiredParam("description", req);
        String isWorkflowString = getOptionalParam("isWorkflow", req);
        boolean isWorkflow = (isWorkflowString == null) ? false : Boolean.parseBoolean(isWorkflowString);
        
        String isWebClientString = getOptionalParam("isWebClient", req);
        boolean isWebClient = (isWebClientString == null) ? false : Boolean.parseBoolean(isWebClientString);
        
        String url = getOptionalParam("url", req);
        if(url == null || url.isEmpty())
        	url = null;

        logger.logp(Level.INFO, "ComputeServlet", "addClient", "Adding new client: " + name + " version: " + version
                + " description: " + description + " workflow: " + isWorkflow);

        AuthorizationManager authorizationManager = SysManagerFactory.getAuthorizationManager();
        String clientID = authorizationManager.registerClient(name, version, description, loginUser, isWorkflow, url);

        String tagArray[]=tags.split(",");
        
        authorizationManager.addClientTags(clientID, tagArray);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("clientID", clientID);
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * For a given tag get all clients
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getAllClientsForTag(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	String tag = getOptionalParam("tag", req);
    	if(tag==null || tag.isEmpty())
    	{
    		writeFailure(resp, "Tag not found");
    		return;
    	}
    	
    	logger.logp(Level.INFO, "ComputeServlet", "getAllClientsForTag", "tag="+tag);
    	
    	String actorName = getCurrentUser(req);
    	
    	String projectName=getRequiredParam("projectName", req);
    	ProjectDAO projectDAO=ImageSpaceDAOFactory.getDAOFactory().getProjectDAO();
    	int projectID=projectDAO.findProject(projectName).getID();
    	List projectApps=new ArrayList<String>();
    	
    	String[] projectClients=SysManagerFactory.getClientManager().getAllClients(actorName, projectID);
    	if(projectClients!=null){
    		projectApps=Arrays.asList(projectClients);
    	}
    	
    	List<ApplicationSpecification> allApps = SysManagerFactory.getComputeEngine().listAllApplications(actorName);

    	List<String> clientIdsList=null;
    	String[] clientIds=ImageSpaceDAOFactory.getDAOFactory().getClientTagsDAO().getClientIdsByTag(tag);    	
    	
    	if(clientIds!=null){
    		clientIdsList= Arrays.asList(clientIds); 
    	}
    	
    	logger.logp(Level.INFO, "ComputeServlet", "getAllClientsForTag", "clientIds="+clientIdsList);
    	
    	List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
    	
    	if(clientIdsList!=null){
    		for(ApplicationSpecification app:allApps){
    			if(clientIdsList.contains(app.getID())){   				
    				Map<String, Object> value = new HashMap<String, Object>();
    				value.put("name", app.getName());
    				value.put("version", app.getVersion());
    				value.put("description", app.getDescription());
    				value.put("id",app.getID());
        			if(projectApps.contains(app.getID()))
        				value.put("selected",true);
        			else
        				value.put("selected",false);
        			
    				ret.add(value);
    			}
    		}
    	}
    	
        writeJSON(resp, ret);
    }
    
    /**
     * Add new publisher
     * 
     * URL Parameters :
     * name : name of the publisher
     * description : description of the publisher
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addPublisher(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String loginUser = getCurrentUser(req);

        String name = getRequiredParam("name", req);
        String description = getRequiredParam("description", req);

        logger.logp(Level.INFO, "ComputeServlet", "addPublisher", "Adding new publisher: " + name + " description: " + description);

        ComputeEngine computeEngine = SysManagerFactory.getComputeEngine();
        String publisherCode = computeEngine.registerPublisher(loginUser, name, description);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("publisherCode", publisherCode);
        ret.put("success", true);
        writeJSON(resp, ret);
    }

    /**
     * gets all the authorized publishers
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getPublishers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String loginUser = getCurrentUser(req);

        logger.logp(Level.INFO, "ComputeServlet", "getPublishers", "get clients: " + loginUser);
        
        ComputeEngine computeEngine = SysManagerFactory.getComputeEngine();
        Collection<Publisher> publishers = computeEngine.getPublisher(loginUser); 
        
        writeJSON(resp, publishers);
    }
    
    /**
     * Get all the clients for a particular user.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getUserClients(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);

        logger.logp(Level.INFO, "ComputeServlet", "getUserClients", "get clients: " + loginUser);
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        List<Client> clients = authManager.getClients(loginUser);
        // TODO: filter on workflow or not?
        writeJSON(resp, clients);
    }

    /**
     * Remove a client given its id.
     * 
     * URL Parameters:
     * clientID : id of the client to be removed
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeClient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);
        String clientID = getRequiredParam("clientID", req);
        logger.logp(Level.INFO, "ComputeServlet", "removeClient", "remove clients: " + loginUser + " id: " + clientID);
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        authManager.removeClient(loginUser, clientID);
        writeSuccess(resp);
    }

    /**
     * Get all the clients in the system which are not workflows.
     * 
     * @param req URL Parameters: 
     * 			  isWorkflow : true if its workflow link, false otherwise (this is optional parameter default value is false)
     * 			  
     * @param resp
     * 				response contains gson object of list representation of the clients
     * @throws ServletException
     * @throws IOException
     */
    public void getClients(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String loginUser = getCurrentUser(req);
        String isWorkflowStr = req.getParameter("isWorkflow");
        Boolean isWorkflow=null;
        if(isWorkflowStr!=null && isWorkflowStr.trim().equals("")==false){
        	isWorkflow= Boolean.parseBoolean(isWorkflowStr);
        }
        logger.logp(Level.INFO, "ComputeServlet", "getClients", "get clients " + loginUser+ "isWorkflow="+isWorkflowStr);
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        List<Client> ret = new ArrayList<Client>();
        List<Client> clients = authManager.getAllClients();
        if(isWorkflow == null){
        	ret.addAll(clients);
        }
        else{
        	for (Client client : clients) {
                if (client.isWorkflow()== isWorkflow)
                    ret.add(client);
            }
        }
        writeJSON(resp, ret);
    }

    /**
     * Get the list of services exposed by the engine.
     * 
     * @param req
     * @param resp
     * 				contains name, value representation of requested services
     * @throws ServletException
     * @throws IOException
     */
    public void getServices(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);

        logger.logp(Level.INFO, "ComputeServlet", "getServices", "get all the services: " + loginUser);

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        Service[] services = Service.values();
        for (Service service : services) {
            Map<String, Object> next = new HashMap<String, Object>();
            next.put("name", service.toString());
            next.put("value", service.name());
            ret.add(next);
        }
        writeJSON(resp, ret);
    }

    /**
     * Generate new auth code.
     * 
     * @param req	URL Parameters:
     * 				clientID : requested client id
     * 				services : services
     * 				expiryTime : time till which the authcode is valid
     * @param resp
     * 				authCode : the authentication code
     * 				success : true
     * @throws ServletException
     * @throws IOException
     */
    public void generateAuthCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loginUser = getCurrentUser(req);

        String clientID = getRequiredParam("clientID", req);
        String servicesString = getRequiredParam("services", req);
        int numberOfTokens = 0 ;
        if(getOptionalParam("numberOfTokens", req)==null)
        	numberOfTokens = 1;
        else
        	numberOfTokens = Integer.parseInt(getOptionalParam("numberOfTokens", req));

        long expiryTime = Long.parseLong(getRequiredParam("expiryTime", req));

        String[] authCode = new String[numberOfTokens] ;
        for (int i = 0 ; i < numberOfTokens ; i++ ){                  
        	authCode[i] = getAuthCode(loginUser, clientID, servicesString, expiryTime);
        }
        
        AuthCodeDAO authCodeDAO = ImageSpaceDAOFactory.getDAOFactory().getAuthCodeDAO();
        IAccessToken token = authCodeDAO.getAuthCode(authCode[0]);
    	UserManager userManager = SysManagerFactory.getUserManager();
        User userInstance = userManager.getUser(loginUser);
        
		Set<User> receivers = new HashSet<User>();
		receivers.add(userManager.getUser(token.getUser()));
		String args = new String();
		for (int j = 0; j < numberOfTokens; j++)
		{
			if (j < numberOfTokens - 2)
				args += authCode[j] + " , ";
			else if (j == numberOfTokens - 2)
				args += authCode[j] + " and ";
			else if (j == numberOfTokens - 1)
				args += authCode[j];
		}
		
		String clientName = SysManagerFactory.getAuthorizationManager().getClient(clientID).getName();
		String expirationDate = authCodeDAO.getAuthCode(authCode[0]).getExpiryTime().toLocaleString();

		SysManagerFactory.getNotificationMessageManager().sendNotificationMessage("iManage Administrator", receivers, null, NotificationMessageType.AUTHCODE_GENERATED, clientName, expirationDate,args);
                
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("authCode", authCode);
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    private String getAuthCode(String loginUser, String clientID, String servicesString, long expiryTime) throws DataAccessException
    {
        List<String> servicesStringList = gson.fromJson(servicesString, new TypeToken<List<String>>() {
        }.getType());
        Service[] services = new Service[servicesStringList.size()];
        for (int i = 0; i < servicesStringList.size(); ++i) {
            services[i] = Service.valueOf(servicesStringList.get(i));
        }

        logger.logp(Level.INFO, "ComputeServlet", "generateAuthCode", "generate auth code: client: " + clientID
                + " services: " + Arrays.toString(services) + " duration: " + expiryTime);
        
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        String authCode = authManager.generateAuthCode(loginUser, clientID, services, expiryTime, null);
        
        return authCode;
    }
    
    /**
     * schedule an application to run
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void scheduleApplication(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
        
        String appName = getRequiredParam("appName", req);
        String appVersion = getRequiredParam("appVersion", req);
        
        
        long waitTime; 
        String scheduledWhen = getRequiredParam("when", req);
        
        if(scheduledWhen.equals("now")){
        	waitTime=0;
        }
        else{
        	long scheduleTime = Long.parseLong(getRequiredParam("scheduledOn", req));
            waitTime = scheduleTime - System.currentTimeMillis();
        }
        
        Priority priority = Priority.valueOf(getRequiredParam("priority", req));
        String projectName = getRequiredParam("projectName", req);

    	boolean isMonitored = false;
    	if("on".equals(req.getParameter("isMonitored"))){
    		isMonitored = true;
    	}
    	if(appVersion!=null && appVersion.trim().equals("")==true){
    		appVersion=null;
    	}
    	
        String gsonParameters = getRequiredParam("paramValues", req);
        Map<String, String> parameters = gson.fromJson(gsonParameters, new TypeToken<Map<String, String>>() { }.getType());
        
        String gsonGuids = getRequiredParam("guids", req);
        List<Long> guids = gson.fromJson(gsonGuids, new TypeToken<List<Long>>() { }.getType());
        long ids[] = new long[guids.size()];
        for(int i=0;i<guids.size();i++)
        {
        	ids[i] = guids.get(i);
        }

        // inject auth code for the application
        String clientID = SysManagerFactory.getComputeEngine().getClientID(appName, appVersion);
        long expiryTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // a validity for one week
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        
        AuthCode authCode = authManager.getAuthCode(loginUser, clientID, Service.values(), expiryTime, null);
        
        SysManagerFactory.getComputeEngine().scheduleApplication(loginUser, new Application(appName, appVersion), authCode, 
        		priority, projectName, isMonitored, waitTime, parameters, ids);
        
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
    }
    
    /**
     * returns list of output records for given task
     * @param req URL Parameters:
     * 					TaskID : id of the task 
     * @param resp	The requested output records are received in resp as list:
     * 				<Output, recordid>
     * 					
     * @throws ServletException
     * @throws IOException 
     */
    public void getOutputRecords(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	logger.logp(Level.INFO, "ComputeServlet", "getOutputRecords", "returning output records for given task");
    	String actorName = getCurrentUser(req);
    	
    	long taskID = Long.parseLong(getRequiredParam("TaskID", req));
    	long[] outputs = SysManagerFactory.getComputeEngine().getTaskOutputs(actorName, taskID);
    	
    	List<Map<String, Long>> outputGuids = new ArrayList<Map<String, Long>>();
    	for(long output:outputs)
    	{
    		Map<String, Long> ret = new HashMap<String, Long>();
    		ret.put("Output", output);
    		
    		outputGuids.add(ret);
    	}
        
        writeJSON(resp, outputGuids);
    }

    /**
     * Get workflows available arranged in categories.
     * 
     * @param req
     * @param resp gson representation of applications for every workflow
     * 				{name : "name of the workflow", workflows : {name : "name of application", version : "version of application", description : "description of application"}}
     * @throws ServletException
     * @throws IOException
     */
    public void getWorkflows(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
    	logger.logp(Level.INFO, "ComputeServlet", "getWorkflows", "listing all applications");

    	String actorName = getCurrentUser(req);
    	
    	List<ApplicationSpecification> allApps = SysManagerFactory.getComputeEngine().listAllApplications(actorName);
    	Map<String, List<ApplicationSpecification>> categoryToApp = new HashMap<String, List<ApplicationSpecification>>();
    	
    	for(ApplicationSpecification app:allApps)
    	{
    		String categoryName = app.getCategory();
    		if(!categoryToApp.containsKey(categoryName))
    		{
    			List<ApplicationSpecification> catApps = new ArrayList<ApplicationSpecification>();
    			categoryToApp.put(categoryName, catApps);
    		}
    		
    		List<ApplicationSpecification> catApps = categoryToApp.get(categoryName);
    		catApps.add(app);
    	}
    	
    	List<Map<String, Object>> all = new ArrayList<Map<String, Object>>();
    	for(Entry<String, List<ApplicationSpecification>> entry :categoryToApp.entrySet())
    	{
    		String catName = entry.getKey();
    		List<ApplicationSpecification> apps = entry.getValue();
    		
    		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
    		for(ApplicationSpecification app:apps)
    		{
    			Map<String, Object> applicationMap = new HashMap<String, Object>();
    			
    			applicationMap.put("name", app.getName());
    			applicationMap.put("version", app.getVersion());
    			applicationMap.put("description", app.getDescription());
    			applicationMap.put("id",app.getID());
    			
    			mapList.add(applicationMap);
    			logger.logp(Level.INFO, "ComputeServlet", "getWorkflows", "listing applications " + app.getName());
    		}
    		
    		Map<String, Object> cat = Util.createMap("name", catName, "workflows", mapList);
    		all.add(cat);
    	}
    	
    	writeJSON(resp, all);
    }
    
    /**
     * gets description of parameters for specified application
     * @param req URL Parameters :
     * 				AppName : name of the application
     * 				AppVersion : version of the application
     * @param resp gson representation of paramerers
     * 				name : name of the parameter
     * 				label : label of the parameter
     * 				type : type of the parameter
     * 				defaultValue : default value of the parameter
     * 				values : list of allowed values, optional
     * 				min : min value, optional
     * 				max : max value, optional
     * @throws ServletException 
     * @throws IOException 
     */
    public void getApplicationParameters(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
    	String appName = getRequiredParam("AppName", req);
    	String appVersion = getRequiredParam("AppVersion", req);
    	
    	logger.logp(Level.INFO, "ComputeServlet", "getApplicationParameters", "getApplicationParameters " + appName);
    	
    	List<Map<String, Object>> parametersList = getApplicationParameters(user, appName, appVersion);
    	writeJSON(resp, parametersList);
    }
    
    /**
     * Fetches the application parameters and converts them to the custom json format for UI
     * @param user
     * @param appName
     * @param appVersion
     * @return
     */
    private List<Map<String, Object>> getApplicationParameters(String user, String appName, String appVersion)
    {
    	Set<Parameter> parameters = SysManagerFactory.getComputeEngine().getParameters(user, appName, appVersion);
    	
    	List<Map<String, Object>> parametersList = new ArrayList<Map<String,Object>>(); 
    	if(parameters != null)
    	{
    		for(Parameter parameter:parameters)
        	{
        		Map<String, Object> paramMap = new HashMap<String, Object>();
        		
        		paramMap.put("name", parameter.name);
        		paramMap.put("label", parameter.name);
        		paramMap.put("type", parameter.type.toString());
        		paramMap.put("description", parameter.description);
        		paramMap.put("default", parameter.defaultValue);
        		
        		if(parameter.constraints instanceof ListConstraints)
        		{
        			paramMap.put("values", ((ListConstraints)parameter.constraints).getValidValues());
        		}
        		else if(parameter.constraints instanceof RangeConstraints)
        		{
        			paramMap.put("min", ((RangeConstraints)parameter.constraints).lowerLimit);
        			paramMap.put("max", ((RangeConstraints)parameter.constraints).upperLimit);
        		}
        		
        		parametersList.add(paramMap);
        	}
    	}
    	return parametersList;
    }

    /**
     * List access tokens on a per user basis. If the user requesting has
     * Administrator rank, all the user tokens are listed. Else only the logged
     * in user's tokens are listed.
     * 
     * @param req
     * 				user : name of the targetted user for which access tokens are requested
     * @param resp
     * 				id : authentication token id
     * 				client : client id
     * 				services : the services which can be accessed by this token
     * 				creationTime : the time of creation of this access token
     * 				expiryTime : the time of expiry for this access token
     * 				accessTime : the last access time for this access token
     * 				filters : url/ip filters, if any
     * @throws ServletException
     * @throws IOException
     */
    public void listTokens(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        String forUser = getRequiredParam("user", req);
        logger.logp(Level.INFO, "ComputeServlet", "listTokens", "List tokens: " + user);
        AuthorizationManager authorizationManager = SysManagerFactory.getAuthorizationManager();
        List<IAccessToken> allTokens = authorizationManager.listAllTokens(user, forUser);

        List<Map<String, Object>> tokens = new ArrayList<Map<String, Object>>();
        for (IAccessToken iAccessToken : allTokens) {
            tokens.add(getAccessTokenMap(iAccessToken));
        }

        writeJSON(resp, tokens);
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
        tokenMap.put("id", token.getInternalID());
        tokenMap.put("client", authManager.getClient(token.getClientID()).toString());
        List<String> serviceNames = new ArrayList<String>();
        for (Service service : token.getAllowedServices())
            serviceNames.add(service.name());
        tokenMap.put("services", serviceNames);
        tokenMap.put("creationTime", token.getCreationTime().getTime());
        tokenMap.put("expiryTime", token.getExpiryTime().getTime());
        tokenMap.put("accessTime", token.getLastAccessTime().getTime());
        tokenMap.put("filters", token.getFilters());
        return tokenMap;
    }

    /**
     * Remove a particular token.
     * 
     * @param req
     * 				id : authentication token id
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void removeToken(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        long authID = Long.parseLong(getRequiredParam("id", req));

        logger.logp(Level.INFO, "ComputeServlet", "removeToken", "Remove token with id: " + authID + " user: " + user);
        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        authManager.removeToken(user, authID);
        writeSuccess(resp);
    }

    /**
     * Update a particular token.
     * 
     * @param req
     * 				id : authentication token id
     * 				services : new list of services accessed by this token
     * 				expiryTime : new expiry time for this token
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void updateToken(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        long authID = Long.parseLong(getRequiredParam("id", req));

        logger.logp(Level.INFO, "ComputeServlet", "updateToken", "update token with id: " + authID + " user: " + user);

        String servicesString = getRequiredParam("services", req);
        long expiryTime = Long.parseLong(getRequiredParam("expiryTime", req));

        List<String> servicesStringList = gson.fromJson(servicesString, new TypeToken<List<String>>() {
        }.getType());
        Service[] services = new Service[servicesStringList.size()];
        for (int i = 0; i < servicesStringList.size(); ++i) {
            services[i] = Service.valueOf(servicesStringList.get(i));
        }

        logger.logp(Level.INFO, "ComputeServlet", "updateToken",
                "update auth code: " + " services: " + Arrays.toString(services) + " duration: " + expiryTime);

        AuthorizationManager authManager = SysManagerFactory.getAuthorizationManager();
        authManager.updateToken(user, authID, services, new Date(expiryTime), null);

        writeSuccess(resp);
    }

    /**
     * List users whose tokens/clients can be manipulated by the logged in user.
     * If the logged in user is admin, all the users are returned. Else only
     * self.
     * 
     * @param req
     * @param resp
     * 			name : user login
     * @throws ServletException
     * @throws IOException
     */
    public void listUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = getCurrentUser(req);
        logger.logp(Level.INFO, "ComputeServlet", "listUsers", "listUsers:  user: " + user);

        UserManager userManager = SysManagerFactory.getUserManager();
        User userInstance = userManager.getUser(user);
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        if (userInstance.getRank().equals(Rank.Administrator)) {
            // All users
            List<User> listUsers = userManager.listUsers(user);
            for (User next : listUsers) {
                ret.add(Util.createMap("name", next.login));
            }
        } else {
            ret.add(Util.createMap("name", user));
        }
        writeJSON(resp, ret);
    }
    
    public void searchTasks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {
    	String user = getCurrentUser(req);
    	TaskManager taskManager = SysManagerFactory.getTaskManager();
    	
    	String owner=req.getParameter("owner");
    	if(owner!=null && owner.trim().equals("")==true){
    		owner=null;
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
    	Long fromDate=null;
    	if(fromDateString!=null && fromDateString.trim().equals("")==false){
    		fromDate = Long.parseLong(fromDateString);
    	}
    	
    	String toDateString=req.getParameter("toDate");
    	Long toDate=null;
    	if(toDateString!=null && toDateString.trim().equals("")==false){
    		toDate = Long.parseLong(toDateString);
    	}
    	
    	String projectName=req.getParameter("projectName");
    	Integer projectID=null;
    	if(projectName != null && projectName.trim().equals("")==false){
    		projectID=SysManagerFactory.getProjectManager().getProject(projectName).getID();
    	}
    	
    	String priorityStr=req.getParameter("priority");
    	Priority priority=null;
    	if(priorityStr != null && priorityStr.trim().equals("")==false){
    		priority=Priority.valueOf(priorityStr);
    	}
    	
    	String taskStateStr=req.getParameter("taskState");
    	State taskState=null;
    	if(taskStateStr != null && taskStateStr.trim().equals("")==false){
    		taskState=State.valueOf(taskStateStr);
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
    	
    	long count=taskManager.getCountOf(owner, priority, 
    			appName, appVersion, taskState, fromDate, 
				toDate, projectID);
    	
    	List<Task> result=taskManager.searchTask(owner, priority, 
    			appName, appVersion, taskState, fromDate, 
    				toDate, projectID, start, limit);
    	
    	List<Project> managedProject=SysManagerFactory.getUserPermissionManager().getManagedProjects(user);
    	
    	List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
    	
    	for(Task t: result){
    		tasks.add(getTaskMap(t, user, managedProject));
    	}
    	
    	writeJSON(resp, Util.createMap("items",tasks, "total", count));
    	logger.logp(Level.INFO, "ComputeServlet", "searchTasks", "matches "+count+" tasks");
    }    
    
    /**
     * returns the list of task monitored by current logged in user
     * @param req
     * @param resp
     * 			taskId : task identifier
     * 			owner : owner of the task
     * 			project : name of the project
     * 			priority : priority of the task
     * 			appName : name of the application
     * 			appVersion : version of the application
     * 			scheduledTime : time of scheduling the application
     * 			state : task state
     * 			progress : task progress
     * 			terminatePermission : true if allowed to terminate
     * @throws ServletException
     * @throws IOException
     */
	public void getMonitoredTasks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String user = getCurrentUser(req);
    	List<Task> result=SysManagerFactory.getComputeEngine().getMonitoredTasks(user);
    	List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
    	for(Task t: result){
    		tasks.add(getTaskMap(t));
    	}
    	writeJSON(resp, tasks);
    	logger.logp(Level.INFO, "ComputeServlet", "getMonitoredTasks", "for " + user);
    }
	
	/**
	 * returns the details of the task under execution
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getTaskExecutionDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String user = getCurrentUser(req);
    	long taskId = Long.parseLong(getRequiredParam(RequestConstants.TASKID_KEY, req));
    	Map<String,Object> taskDetails= SysManagerFactory.getComputeEngine().getTaskExecutionDetails(user, taskId);
    	taskDetails.put("parameters", 
    			getApplicationParameters(user, 
    					taskDetails.get("appName").toString(), 
    					taskDetails.get("appVersion").toString()));
    	
		writeJSON(resp, taskDetails);
		logger.logp(Level.INFO, "ComputeServlet", "getTaskExecutionDetails", "for taskId " + taskId);
	}
    
	/**
	 * clears the specified monitored task
	 * @param req
	 * 			taskId : id of the task to be cleared
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
    public void clearTaskMonitor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String user = getCurrentUser(req);
    	long taskId = Long.parseLong(getRequiredParam(RequestConstants.TASKID_KEY, req));
    	SysManagerFactory.getComputeEngine().clearTaskMonitor(user, taskId);
    	writeJSON(resp, Util.createMap("success",true));
    	logger.logp(Level.INFO, "ComputeServlet", "clearTaskMonitor", "for taskId " + taskId);
    }

    /**
	 * terminates the specified monitored task
	 * @param req
	 * 			taskId : id of the task to be terminated
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
    public void terminateTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String user = getCurrentUser(req);
    	long taskId = Long.parseLong(getRequiredParam(RequestConstants.TASKID_KEY, req));
    	SysManagerFactory.getComputeEngine().cancelTask(user, taskId);
    	writeJSON(resp, Util.createMap("success",true));
    	logger.logp(Level.INFO, "ComputeServlet", "terminateTask", "for taskId " + taskId);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getUpdatedState(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	String user = getCurrentUser(req);
    	String taskIdsString = getRequiredParam("taskIds", req);
    	List<Long> taskIds = gson.fromJson(taskIdsString, new TypeToken<List<Long>>() {
        }.getType());
    	
    	List<Map<String,Object>> taskStates=new ArrayList<Map<String,Object>>();
    	ComputeEngine computeEngine = SysManagerFactory.getComputeEngine();
    	for(Long id:taskIds){
    		State state = computeEngine.getJobState(user, id);
    		Map<String,Object>map = Util.createMap("taskId",id.toString(),"state",state);
    		if(state== State.EXECUTING){
    			map.put("progress", computeEngine.getTaskProgress(user, id));
    		}
    		taskStates.add(map);
    	}
    	writeJSON(resp, taskStates);
    	logger.logp(Level.INFO, "ComputeServlet", "getUpdatedState", "for taskIds " + taskIdsString);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getTaskLogs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String user = getCurrentUser(req);
    	long taskId = Long.parseLong(getRequiredParam(RequestConstants.TASKID_KEY, req));
    	
    	String archiveLocation = SysManagerFactory.getComputeEngine().getTaskLogArchive(taskId);
    	File tempFile = File.createTempFile("Log_", ".tar.gz");
    	Archiver.createTarRecursively(tempFile, true, new File(archiveLocation));
    	
    	resp.setContentType("application/octet-stream");
        resp.setContentLength((int) tempFile.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + tempFile.getName() + "\"");
        Util.transferData(new FileInputStream(tempFile), resp.getOutputStream(), true);
    	
    	tempFile.delete();
    }

	private Map<String, Object> getTaskMap(Task t) {
		Map<String, Object> taskMap = new HashMap<String, Object>();
		taskMap.put("taskId", String.valueOf(t.getID()));
		taskMap.put("owner", t.getOwner());
		taskMap.put("project", t.getProject().getName());
		taskMap.put("priority", t.getPriority().name());
		taskMap.put("appName", t.getApplication().getName());
		taskMap.put("appVersion", t.getApplication().getVersion());
		taskMap.put("scheduledTime", new Date(t.getScheduleTime()));
		taskMap.put("scheduledTimestamp", t.getScheduleTime());
		taskMap.put("state", t.getState().name());
		taskMap.put("progress", t.getProgress());
		taskMap.put("terminatePermission", true);
        return taskMap;
	}
	
	private Map<String, Object> getTaskMap(Task t, String user, List<Project> managedProject) {
		Map<String, Object> taskMap = getTaskMap(t);
		if(t.getOwner().equals(user)
			|| managedProject.contains(t.getProject())){
			taskMap.put("terminatePermission", true);
		}
		else{
			taskMap.put("terminatePermission", false);
		}
		return taskMap;
	}
}