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
