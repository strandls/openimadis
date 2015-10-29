package com.strandgenomics.imaging.iserver.services.impl.web;


import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.movie.MovieTicket;
import com.strandgenomics.imaging.iengine.system.MovieManager;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * 
 * functions for movie creation and playing
 * 
 * @author veerareddy
 * 
 */
public class MovieServlet extends ApplicationServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String MOVIE_ID = "movieID";

    /**
     * Max number of tries before giving up
     */
    private static final int MAX_TRIES = 100;

    /**
     * start playing the movie from the currentFrame or current Slice depending
     * on the boolean OnFrame . Params guid specified record site specified site
     * onFrame true if movie is to be played on frames; false if movie is played
     * on slices channels selected channels overlays selected overlays start the
     * starting co-ordinate of movie firstRequest
     * 
     * 
     * @param req
     * 			guid : record id
     * 			siteNumber : specified site
     * 			frame : true if movie is run on frame, false if movie is run on slices
     * 			sliceNumber : specified slice
     * 			frameNumber : specified frame
     * 			useChannelColor : if channel LUT is applied, false for GRAY
     * 			channelNumbers : list of selected channels
     * 			overlayNames : list of selected overlays, optional parameter
     * @param resp get movie identifier as return
     * 			movieID : movie identifier
     * @throws ServletException
     * @throws IOException
     * 
     */
    public void start(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);
        MovieManager movieManager = SysManagerFactory.getMovieManager();
        long guid = Long.parseLong(getRequiredParam(RequestConstants.GUID_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        boolean onFrame = Boolean.parseBoolean(getRequiredParam(RequestConstants.ONFRAME_KEY, req));

        int sliceNumber = Integer.parseInt(req.getParameter(RequestConstants.SLICE_NUMBER_KEY));
        int frameNumber = Integer.parseInt(req.getParameter(RequestConstants.FRAME_NUMBER_KEY));
        logger.logp(Level.INFO, "MovieServlet", "start", "frame number  " + frameNumber);

        int fixedCoorinate;
        if (onFrame == true) {
            fixedCoorinate = sliceNumber;
        } else {
            fixedCoorinate = frameNumber;
        }
        String useChannelColor = req.getParameter(RequestConstants.USE_CHANNEL_COLOR);
        Boolean channelColor = useChannelColor == null ? true : Boolean.parseBoolean(useChannelColor);

        List<Integer> channelNumbers = gson.fromJson(getRequiredParam(RequestConstants.CHANNEL_NUMBERS_KEY, req)
                .toString(), new TypeToken<List<Integer>>() {
        }.getType());

        String overlayNamesString = getRequiredParam("overlayNames", req);
        logger.logp(Level.INFO, "MovieServlet", "start", "overlays: " + overlayNamesString);
        List<String> overlayNames = gson.fromJson(overlayNamesString, new TypeToken<List<String>>() {
        }.getType());

        logger.logp(Level.INFO, "MovieServlet", "start", "frame: " + frameNumber + " slice: " + sliceNumber + " site: "
                + siteNumber + " guid: " + guid + " channel color: " + useChannelColor + " use frame: " + onFrame
                + " channels: " + channelNumbers + " overlays: " + overlayNames);

        String zStack = req.getParameter("zstacked");
        Boolean zStacked = zStack == null ? false : Boolean.parseBoolean(zStack);
        MovieTicket movie = movieManager.createMovieImages(userName, guid, siteNumber, onFrame, fixedCoorinate, channelColor, zStacked, channelNumbers, overlayNames);
        long movieId = movie.getMovieid();

        logger.logp(Level.INFO, "MovieServlet", "start", "Got movie id: " + movieId);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put(MOVIE_ID, movieId+"");
        writeJSON(resp, ret);
    }
    
    /**
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void createVideoMovie(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = getCurrentUser(req);

        MovieManager movieManager = SysManagerFactory.getMovieManager();
        
        // record
        long guid = Long.parseLong(getRequiredParam(RequestConstants.GUID_KEY, req));
        
        // record dimensions
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));
        
        int sliceNumber = Integer.parseInt(req.getParameter(RequestConstants.SLICE_NUMBER_KEY));
        int frameNumber = Integer.parseInt(req.getParameter(RequestConstants.FRAME_NUMBER_KEY));
        boolean onFrame = Boolean.parseBoolean(getRequiredParam(RequestConstants.ONFRAME_KEY, req));
        int fixedCoorinate;
        if (onFrame == true) {
            fixedCoorinate = sliceNumber;
        } else {
            fixedCoorinate = frameNumber;
        }

        // channels
        List<Integer> channelNumbers = gson.fromJson(getRequiredParam(RequestConstants.CHANNEL_NUMBERS_KEY, req)
                .toString(), new TypeToken<List<Integer>>() {
        }.getType());

        String useChannelColor = req.getParameter(RequestConstants.USE_CHANNEL_COLOR);
        Boolean channelColor = useChannelColor == null ? true : Boolean.parseBoolean(useChannelColor);
        
        // overlays
        String overlayNamesString = getRequiredParam("overlayNames", req);
        List<String> overlayNames = gson.fromJson(overlayNamesString, new TypeToken<List<String>>() {
        }.getType());
        
        // expirty time
        long expiryTime = Long.parseLong(getRequiredParam("validity", req));
        
        Double fps = null;
        String fpsString = getOptionalParam("fps", req);
		try
		{
			fps = Double.parseDouble(fpsString);
		}
		catch (Exception e)
		{}
		
		String movieName=req.getParameter(RequestConstants.MOVIE_NAME_KEY);
        
        logger.logp(Level.INFO, "MovieServlet", "createVideoMovie", "frame: " + frameNumber + " slice: " + sliceNumber + " site: "
                + siteNumber + " guid: " + guid + " channel color: " + useChannelColor + " use frame: " + onFrame
                + " channels: " + channelNumbers + " overlays: " + overlayNames+" expiry date"+expiryTime+ "movie Name:"+movieName);

        String zStack = req.getParameter("zstacked");
        Boolean zStacked = zStack == null ? false : Boolean.parseBoolean(zStack);        
        
        MovieTicket movie = movieManager.createVideoMovie(userName, guid, siteNumber, onFrame, fixedCoorinate, channelColor, zStacked, channelNumbers, overlayNames, expiryTime, fps, movieName);
        long movieId = movie.getMovieid();       

        logger.logp(Level.INFO, "MovieServlet", "createVideoMovie", "Got movie id: " + movieId);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        ret.put(MOVIE_ID, movieId+"");
        writeJSON(resp, ret);
    }
    
    /**
     * delete the specified movie
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteMovie(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String loginUser = getCurrentUser(req);
    	
    	long requestId = Long.parseLong(getRequiredParam("requestId", req));
    	logger.logp(Level.INFO, "MovieServlet", "deleteMovie", "loginUser=" + loginUser);
    	
    	SysManagerFactory.getMovieManager().removeMovieRequest(loginUser, requestId);
    	
    	Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("success", true);
        writeJSON(resp, ret);
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
    	String loginUser = getCurrentUser(req);
    	
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

    /**
     * Get image with the given index.
     * 
     * @param req
     * 			ithImage : image number in specified movie
     * 			movieID : movie identifier
     * @param resp gets png image as content type image/png
     * @throws ServletException
     * @throws IOException
     */
    public void getNextImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);

        int ithImage = Integer.parseInt(getRequiredParam(RequestConstants.IMAGE_NO, req));
        long movieId = Long.parseLong(getRequiredParam(MOVIE_ID, req));
        
        
        MovieManager movieManager = SysManagerFactory.getMovieManager();
        
        //Check for etag
        
        String token = movieManager.getEtag(userName, movieId, ithImage);
        resp.setHeader("ETag", token); // always store the ETag in the header
    	
      	String previousToken = req.getHeader("If-None-Match");
      	
    	if (previousToken != null && previousToken.equals(token)) { // compare previous token with current one
    		logger.logp(Level.INFO, "MovieServlet", "getNextImage", "ETag match: returning 304 Not Modified");
            resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
     		// use the same date we sent when we created the ETag the first time though
        	resp.setHeader("Last-Modified", req.getHeader("If-Modified-Since"));
        	return;
     	} 
    	else  { 		// first time through - set last modified time to now 
     		Calendar cal = Calendar.getInstance();
     		cal.set(Calendar.MILLISECOND, 0);
     		Date lastModified = cal.getTime();
     		resp.setDateHeader("Last-Modified", lastModified.getTime());
     		logger.logp(Level.INFO, "ProjectServlet", "getImage", "ETag not match: writing body content");
            
     		File imageFile;
            int tries = 0;
            while ((imageFile = movieManager.getNextImage(userName, movieId, ithImage)) == null) {
                try {
                    if (++tries > MAX_TRIES) {
                        writeFailure(resp, "Image generation failed");
                        return;
                    }
                    logger.logp(Level.INFO, "MovieServlet", "getNextImage",
                            "frame number  still returning null for ith image =  " + ithImage);
                    Thread.sleep(RequestConstants.TIME_OUT);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            resp.setContentType("image/png");
			resp.addHeader("Cache-Control", "must-revalidate,s-maxage=0;");
            BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
            try
            {
            	BufferedImage image = ImageIO.read(imageFile);

                String heightString = req.getParameter(RequestConstants.HEIGHT_KEY);
                Integer requiredHeight = heightString == null ? null : Integer.parseInt(heightString);
                
                if (requiredHeight != null) {
                	image = getScaledImage(requiredHeight, image);
                }
                
                ImageIO.write(image, "PNG", outputStream);
            }
            catch(Exception e)
            {}
            finally
            {
            	outputStream.close();
            }
     	}

    }
    
    /**
     * Get scaled image, preserving the aspect ratio
     * 
     * @param requiredWidth
     * @param requiredHeight
     * @param image
     * @return
     */
    private BufferedImage getScaledImage(Integer requiredHeight, BufferedImage image) {
        float imgRatio = (float) image.getWidth() / (float) image.getHeight();
        int width = (int) (imgRatio * requiredHeight);
        image = Util.resizeImage(image, width, requiredHeight);
        return image;
    }
    /**
     * Check whether requested image exists
     * UI calls this method to before making getNextImage call. 
     * 
     * @param req
     * 			ithImage : index of image in the movie
     * 			movieID : specified movie
     * @param resp
     * 			exists : true if image exists, false otherwise
     * @throws ServletException
     * @throws IOException
     */
    public void isImageExists(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);

        int ithImage = Integer.parseInt(getRequiredParam(RequestConstants.IMAGE_NO, req));
        long movieId = Long.parseLong(getRequiredParam(MOVIE_ID, req));

        MovieManager movieManager = SysManagerFactory.getMovieManager();
        if(movieManager.getNextImage(userName, movieId, ithImage) != null){
        	writeJSON(resp, Util.createMap("success",true,"exists",true));
     		logger.logp(Level.INFO, "ProjectServlet", "isImageExists", "exists:true");
        }
        else{
        	writeJSON(resp, Util.createMap("success",true,"exists",false));
     		logger.logp(Level.INFO, "ProjectServlet", "isImageExists", "exists:false");
        }
    }

}
