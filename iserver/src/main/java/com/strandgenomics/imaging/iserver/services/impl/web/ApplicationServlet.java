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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.app.Application;
import com.strandgenomics.imaging.iengine.ImagingEngineException;

/**
 * 
 * Common base class for all the servlets which provide the manager apis. Takes
 * care of authentication and provides utilities to write json output.
 * 
 * @author santhosh
 * 
 */
public class ApplicationServlet extends MultiplexingServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default success response. Unmodifiable map
     */
    protected Map<String, Object> successResponse = null;

    /**
     * gson instance to use for json serialization
     */
    protected Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String, Object> successResponse = new HashMap<String, Object>();
        successResponse.put("success", true);
        this.successResponse = Collections.unmodifiableMap(successResponse);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute(AuthenticationServlet.USERNAME) != null) {
            try {
                super.doGet(req, resp);
            } catch (ServletException e) {
                // If the cause of exception is ImagingEngineException send
                // appropriate message.
                logger.logp(Level.INFO, "ApplicationServlet", "doGet", "failed", e);
                if (e.getCause() instanceof ImagingEngineException)
                    writeFailure(resp, ((ImagingEngineException) e.getCause()).getErrorCode().getErrorMessage());
                else
                    throw e;
            }
        } else {
            writeFailure(resp, "Invalid session or session timed out. Please logout and login again.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute(AuthenticationServlet.USERNAME) != null) {
            try {
                super.doPost(req, resp);
            } catch (ServletException e) {
                // If the cause of exception is ImagingEngineException send
                // appropriate message.
                logger.logp(Level.INFO, "ApplicationServlet", "doPost", "failed", e);
                if (e.getCause() instanceof ImagingEngineException)
                    writeFailure(resp, ((ImagingEngineException) e.getCause()).getErrorCode().getErrorMessage());
                else
                    throw e;
            }
        } else {
            writeFailure(resp, "Invalid session or session timed out. Please logout and login again.");
        }
    };

    /**
     * Get the current logged in user
     * 
     * @param req
     * @return the logged in user. If no user is logged in throws
     *         {@link ServletException}
     * @throws ServletException
     */
    protected String getCurrentUser(HttpServletRequest req) throws ServletException {
        HttpSession session = req.getSession();
        Object user = session.getAttribute(AuthenticationServlet.USERNAME);
        if (user == null)
            throw new ServletException("User attribute not present");
        return user.toString();
    }

    /**
     * Write json serialized object to the response
     * 
     * @param resp
     * @param objectToWrite
     * @throws IOException
     */
    protected void writeJSON(HttpServletResponse resp, Object objectToWrite) throws IOException {
        writeJSON(resp, objectToWrite, "application/json");
    }

    /**
     * Write json serialized object to the response with content type
     * 
     * @param resp
     * @param objectToWrite
     * @param contentType
     *            content type to use
     * @throws IOException
     */
    protected void writeJSON(HttpServletResponse resp, Object objectToWrite, String contentType) throws IOException {
        String json = gson.toJson(objectToWrite);
        resp.setContentType(contentType);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    /**
     * Write success response
     * 
     * @param resp
     * @param message
     * @throws IOException
     */
    protected void writeSuccess(HttpServletResponse resp) throws IOException {
        writeJSON(resp, successResponse);
    }

    /**
     * Write failure message
     * 
     * @param resp
     * @param message
     * @throws IOException
     */
    protected void writeFailure(HttpServletResponse resp, String message) throws IOException {
        Map<String, Object> failureResponse = new HashMap<String, Object>();
        failureResponse.put("success", false);
        failureResponse.put("message", message);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJSON(resp, failureResponse);
    }

    /**
     * Get GMT String of a particular unix time.
     * 
     * @param milliSeconds
     *            time to format
     * @return gmt time string
     */
    protected String getGMTTimeString(long milliSeconds) {
        // SimpleDateFormat is not thread-safe! Creating new instance everytime.
        SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'");
        return sdf.format(new Date(milliSeconds));
    }

    /**
     * Utility to get a short formatted date
     * 
     * @param date
     * @return formatted date
     */
    protected static String getShortDate(Date date) {
        // dateformat not thread-safe
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return format.format(date);
    }
    
    /**
     * returns the web application which is used by Manager APIs for history
     * @return the web application
     */
    protected static Application getWebApplication()
    {
    	return new Application(Constants.getWebApplicationName(), Constants.getWebApplicationVersion());
    }
}
