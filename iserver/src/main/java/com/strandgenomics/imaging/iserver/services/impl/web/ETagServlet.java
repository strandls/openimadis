package com.strandgenomics.imaging.iserver.services.impl.web;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ETagServlet extends ApplicationServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2088002199219220900L;
	
    public void getImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String token = "myconstanstEtag";
    	resp.setHeader("ETag", token); // always store the ETag in the header
    	
      	String previousToken = req.getHeader("If-None-Match");
      	
    	if (previousToken != null && previousToken.equals(token)) { // compare previous token with current one
        	logger.log(Level.INFO,"ETag match: returning 304 Not Modified");
        	resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
     		// use the same date we sent when we created the ETag the first time through
        	resp.setHeader("Last-Modified", req.getHeader("If-Modified-Since"));
     	} 
    	else  { 		// first time through - set last modified time to now 
     		Calendar cal = Calendar.getInstance();
     		cal.set(Calendar.MILLISECOND, 0);
     		Date lastModified = cal.getTime();
     		resp.setDateHeader("Last-Modified", lastModified.getTime());
     		logger.log(Level.INFO,"Writing body content");
     		
     		//resp.setContentType("application/octet-stream");
            //resp.setHeader("Content-Disposition", "attachment; filename=\"" + "Record.png" + "\"");
     		resp.setContentType("image/png");
     		
            FileInputStream is=new FileInputStream("/home/devendra/Desktop/Record.png");

            BufferedOutputStream os = new BufferedOutputStream(resp.getOutputStream());
            
     		byte[] buffer = new byte[4096]; // tweaking this number may increase performance  
     		int len;  
     		while ((len = is.read(buffer)) != -1)  
     		{  
     		    os.write(buffer, 0, len);  
     		}  
     		os.flush();  
     		is.close();  
     		os.close();
     	}
    }
}
