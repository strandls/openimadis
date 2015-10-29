package com.strandgenomics.imaging.iserver.services.impl.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.ImagingEngineException;
import com.strandgenomics.imaging.iengine.movie.MovieTicket;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * This servlet serves download related queries
 * The purpose of Download servlet is to facilitate download to external users as well.
 * This servlets doesn't looks for loginUsername at any point
 * It extends MultiplexingServlet servlet and overrides doGet and doPost
 * @author navneet
 *
 */
@SuppressWarnings("serial")
public class DownloadServlet extends MultiplexingServlet {
	
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
    	//This doGet method doesn't checks if loginUsername in session is null
    	//because this servlet is used by external user as well for downloading 
    	//Functions of this servlet can be called even if user is not logged in
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
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	//This doPost method doesn't checks if loginUsername in session is null
    	//because this servlet is used by external user as well for downloading 
    	//Functions of this servlet can be called even if user is not logged in
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
     * downloads the archive
     * @param req
     * 	requestId : the archive to be downloaded
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadArchive(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = Constants.ADMIN_USER;    	
    	
    	// get record id
        long requestId = Long.parseLong(getRequiredParam("requestId", req));
        
     	 logger.logp(Level.INFO, "DownloadServlet", "downloadArchive","requestId="+requestId);
        
        String filepath = SysManagerFactory.getExportManager().getDownloadURL(loginUser, requestId);

        File archive = new File(filepath);
        if (!archive.exists()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) archive.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + archive.getName() + "\"");
        Util.transferData(new FileInputStream(archive), resp.getOutputStream(), true);
    }
    
    /**
     * download the movie video
     * @param req
     * 			movieId: movie id
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void downloadMovie(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = Constants.ADMIN_USER; 
    	
    	// get record id
        long requestId = Long.parseLong(getRequiredParam("movieId", req));
        
        File videoFile = SysManagerFactory.getMovieManager().getVideo(loginUser, requestId);

        if (!videoFile.exists()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        MovieTicket movie=SysManagerFactory.getMovieManager().getVideoMovie(requestId);
        
        String movieExtension =  "";
        String name=videoFile.getName();
        
        int k = videoFile.getName().lastIndexOf(".");
        if(k != -1)
        	movieExtension = "."+name.substring(k + 1, name.length());
        
        resp.setContentType("application/octet-stream");
        resp.setContentLength((int) videoFile.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + movie.getMovieName() + movieExtension+ "\"");
        Util.transferData(new FileInputStream(videoFile), resp.getOutputStream(), true);
    }
}
