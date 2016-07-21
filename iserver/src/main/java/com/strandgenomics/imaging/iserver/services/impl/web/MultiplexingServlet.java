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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Multiplex requests to methods based on the URL. Used to handle multiple
 * related calls in a single servlet instead of creating a servlet separately
 * for each.
 * 
 * The URL is parsed and the appropriate method is called using reflection. The
 * request and response objects are passed as is to the method.
 * 
 * The methods need to be <b>public</b> for reflection to work.
 * 
 * <pre>
 * Example: 
 * "/AvadisIMGServer/mult" - Servlet URL root
 * "/AvadisIMGServer/mult/method1" - call method1
 * "/AvadisIMGServer/mult/method2?a=b&c=d" - call method2
 * "/AvadisIMGServer/mult/method1/action1" - call method1
 * </pre>
 * 
 * 
 * @author santhosh
 * 
 */
public class MultiplexingServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The logger
     */
    protected Logger logger = null;

    @Override
    public void init() throws ServletException {
        super.init();
        logger = Logger.getLogger("com.strandgenomics.imaging.iserver.impl.web");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String remainingURI = parseRequest(req);
        callMethod(remainingURI, req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String remainingURI = parseRequest(req);
        callMethod(remainingURI, req, resp);
    }

    /**
     * Parse the request to get the part relevant for multiplexing
     * 
     * @param req
     * @return
     */
    private String parseRequest(HttpServletRequest req) {
        String servletPath = req.getContextPath() + req.getServletPath();
        String requestURI = req.getRequestURI();
        String remainingURI = "";
        // Handle the case where there is no leading slash
        if (servletPath.length() < requestURI.length())
            remainingURI = requestURI.substring(servletPath.length() + 1, requestURI.length());
        return remainingURI;
    }

    /**
     * Call the method based on the first string before the "/" character. Note
     * that multiplexing is done only for one level. If no method with that name
     * exists, {@link #defaultResponse(HttpServletRequest, HttpServletResponse)}
     * is invoked.
     * 
     * @param remainingURI
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void callMethod(String remainingURI, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (remainingURI == null || remainingURI.length() == 0) {
            defaultResponse(req, resp);
            return;
        }
        String methodName = remainingURI.split("/")[0].trim();
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (SecurityException e) {
            throw new ServletException(e);
        } catch (NoSuchMethodException e) {
            logger.info("Method not found: calling default response: " + methodName);
            // Call the default response
            defaultResponse(req, resp);
        } catch (IllegalArgumentException e) {
            throw new ServletException(e);
        } catch (IllegalAccessException e) {
            throw new ServletException(e);
        } catch (InvocationTargetException e) {
            throw new ServletException(e.getCause());
        }
    }

    /**
     * Default response if a method with the given name is not present. Returns
     * 404 response.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void defaultResponse(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /**
     * Access a required parameter. Throws {@link ServletException} if its missing.
     * @param paramName name of parameter
     * @param req request object to use
     * @return the value string if parameter is present
     * @throws ServletException if parameter is absent
     */
    protected static String getRequiredParam(String paramName, HttpServletRequest req) throws ServletException {
        if (req.getParameter(paramName) == null)
            throw new ServletException("Required parameter: "+paramName+ " missing ");
        return req.getParameter(paramName);
    }
    
    /**
     * Access an optional parameter. Returns null if the parameter is not present
     * @param paramName name of parameter
     * @param req request object to use
     * @return the value string if parameter is present
     * @throws ServletException if parameter is absent
     */
    protected static String getOptionalParam(String paramName, HttpServletRequest req) throws ServletException {
        if (req.getParameter(paramName) == null)
            return null;
        return req.getParameter(paramName);
    }
}
