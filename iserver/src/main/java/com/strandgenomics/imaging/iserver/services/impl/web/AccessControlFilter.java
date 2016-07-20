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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter to give authorised access to all the resources under /iManage/* The
 * typical resources are client-jar.jar, javadoc for client apis, WSDLs for SOAP
 * based API etc.
 * 
 * @author Anup Kulkarni
 */
public class AccessControlFilter extends ApplicationServlet implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7919935602618820555L;
	private static final String CLIENT_JAR = "/iManage/client-jar/";
	private static final String CLIENT_API = "/iManage/client-api/";
	private static final String API = "/iManage/api";
	private static final String UPLOAD_SERVICE = "iManage/upload-service/";
	
	private static final String LOGIN_PAGE = "/iManage/standard/login.html?unauthorized";
	
	@Override
	public void destroy() {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest)req);
		String requestURI = httpRequest.getRequestURI();
		
		if(httpRequest.getSession().getAttribute("loginUsername") == null) {
			// not a logged in user
			if(requestURI.contains(CLIENT_API) || requestURI.contains(CLIENT_JAR) || requestURI.contains(UPLOAD_SERVICE) || requestURI.contains(API)) {
				System.out.println("unauthorized access: " + requestURI);
//				throw new IllegalAccessError("Not authorised to access the resource");
				((HttpServletResponse)resp).sendRedirect(LOGIN_PAGE);
				return;
			}
		}
		
		chain.doFilter(req, resp);
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
