package com.strandgenomics.imaging.tileviewer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.strandgenomics.imaging.tileviewer.system.StorageManager;
import com.strandgenomics.imaging.tileviewer.system.TileManager;

/**
 * Servlet implementation class ViewServlet
 *
 * @author aritra
 *
 */
//@WebServlet("/Viewer/*")
public class ViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Viewer viewer;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		viewer = new Viewer(config.getServletContext());
		StorageManager.getInstance().populateFileLocator();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		callMethod(request, response);
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
	{       
		callMethod(request, response);
	}
	
	/**
	 * Call the method based on the first string before the "/" character. Note
	 * that multiplexing is done only for one level. If no method with that name
	 * exists, {@link #defaultResponse(HttpServletRequest, HttpServletResponse)}
	 * is invoked.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void callMethod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String remainingURI = parseRequest(request);
		if (remainingURI == null || remainingURI.length() == 0) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} else {
			String methodName = remainingURI.split("/")[0].trim();
			try {
				Method method = viewer.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
				method.invoke(viewer, request, response);
			} catch (SecurityException e) {
				throw new ServletException(e);
			} catch (NoSuchMethodException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IllegalArgumentException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			} catch (InvocationTargetException e) {
				throw new ServletException(e.getCause());
			}
		}
	}
	
	/**
	 * Parse the request to get the part relevant for multiplexing
	 * 
	 * @param request
	 * @return
	 */
	private static String parseRequest(HttpServletRequest request) {
		String servletPath = request.getContextPath() + request.getServletPath();
		String requestURI = request.getRequestURI();
		String remainingURI = "";
		// Handle the case where there is no leading slash
		if (servletPath.length() < requestURI.length()) {
			remainingURI = requestURI.substring(servletPath.length() + 1, requestURI.length());
		}
		return remainingURI;
	}
}
