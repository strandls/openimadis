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
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Permission;
import com.strandgenomics.imaging.icore.Rank;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.system.ErrorCode;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.models.Project;
import com.strandgenomics.imaging.iengine.models.User;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Authentication servlet
 * 
 * @author santhosh
 * 
 */
public class AuthenticationServlet extends MultiplexingServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * URL for login page
	 */
	static final String LOGIN_URL = "../home/login.html";
	
	/**
	 * URL to redirect to if authentication fails
	 */
	static final String RETRY_URL = "../home/retry.html";
	
	/**
	 * URL to redirect to if user is not member of any project
	 */
	static final String EMPTY_PRJ_RETRY_PATH = "/emptyProjectRetry.html";
	
	/**
	 * URL to redirect to if user is suspended/deleted
	 */
	static final String USER_SUSPENDED_PATH = "/userSuspended.html";
	
	/**
	 * URL to redirect to if admin authentication fails
	 */
	static final String ADMIN_RETRY_URL = "../home/adminRetry.html";
	
	/**
	 * Key for password in the request
	 */
	private static final String PASSWORD = "loginPassword";
	
	/**
	 * Key for username in the request and session
	 */
	static final String USERNAME = "loginUsername";
	
	/**
	 * Key for the name of the user
	 */
	static final String NAME = "loginName";
	
	/**
	 * Key for success url in request
	 */
	private static final String SUCCESS_URL_KEY = "success";

	/**
	 * Login method
	 * 
	 * URL Parameters:
	 * loginUsername : name of the user
	 * loginPassword : password of the user
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = "";

		String username = getRequiredParam(USERNAME, request);
		String password = getRequiredParam(PASSWORD, request);
		Boolean checkAdmin = Boolean.parseBoolean(getOptionalParam("checkAdmin", request));
		String client = getOptionalParam("client", request);
		
		// Login should be a post request
		if (request.getMethod().equals("GET"))
		{
			result = "INVALID REQUEST";
		}
		else
		{
			try
			{
				boolean authenticate = false;
				if (checkAdmin)
				{
					authenticate = SysManagerFactory.getUserManager().authenticate(new Application(Constants.getWebAdminApplicationName(), Constants.getWebApplicationVersion()), username, password);
				}
				else
				{
					authenticate = SysManagerFactory.getUserManager().authenticate(new Application(Constants.getWebApplicationName(), Constants.getWebApplicationVersion()), username, password);
				}

				if (authenticate)
				{
					if (checkAdmin && !isAdmin(username))
					{
						result = "You are not allowed to log in to Admin Page.";
					}
					else
					{
						User user = SysManagerFactory.getUserManager().getUser(username);
						List<Project> projects = SysManagerFactory.getUserPermissionManager().listProjects(user.login, Permission.Read);
						if (projects.size() > 0 || isAdmin(username))
						{
							HttpSession session = request.getSession();
							session.setAttribute(USERNAME, username);
							session.setAttribute(NAME, user.getName());
							result = SUCCESS_URL_KEY;
						}
						else
						{
							result = "You are not member of any project. Kindly contact Administrator.";
						}
					}
				}
				else
				{
					result = "Wrong username or password. Please try again.";
				}
			}
			catch (ImagingEngineException e)
			{
				logger.logp(Level.WARNING, "AuthenticationServlet", "login", "user authentication failed", e);
				if (e.getErrorCode().getCode() == ErrorCode.ImageSpace.LOGIN_DISABLED)
				{
					result = "Your account is Suspended. Kindly Contact Administrator.";
				}
				else if (e.getErrorCode().getCode() == ErrorCode.ImageSpace.LOGIN_DELETED)
				{
					result = "Your account is Deleted. Kindly Contact Administrator.";
				}
				else if (e.getErrorCode().getCode() == ErrorCode.ImageSpace.NOT_IN_VALID_LDAP_GROUP)
				{
					result = "You are not part of valid LDAP group. Kindly Contact Administrator.";
				}
				else
				{
					result = "Authentication failed due to server error.";
				}
			}
			System.out.println(client);
		}

		
		if (client != null && client.compareToIgnoreCase("mobile") == 0)
		{
			response.sendRedirect("../m");
		}
		else
		{
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(result);
			out.close();
		}
	}

	private boolean isAdmin(String user) {
		boolean showAdmin = false;
		Rank rank = SysManagerFactory.getUserManager().getUser(user).getRank();
		switch (rank) {
			case Administrator:
			case FacilityManager:
				showAdmin = true;
				break;
			case TeamLeader:
			case TeamMember:
				List<Project> projects = null;
				try {
					projects = SysManagerFactory.getProjectManager().getAllProjectsByManager(user);
				} catch (DataAccessException e) {
					e.printStackTrace();
				}
				if (projects!=null && projects.size()>0) {
					showAdmin = true;
				}
				break;
		}
		return showAdmin;
	}

	/**
	 * Logout method
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute(USERNAME) != null) {
			session.removeAttribute(USERNAME);
		}
		String location = getOptionalParam("success", request);
		location = "../" + (location!=null ? location : "");
		response.sendRedirect(location);
	}
}
