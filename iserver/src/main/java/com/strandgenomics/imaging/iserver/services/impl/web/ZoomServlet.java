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

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.system.ZoomManager;

/**
 * 
 * functions for serving zoom requests
 * 
 * @author devendra
 * 
 */
public class ZoomServlet extends ApplicationServlet {

	private static final long serialVersionUID = 2210468199660668472L;

	private static final String ZOOM_ID = "zoomId";
	
	private static final String ZOOM_LEVEL = "zoomLevel";
	
	private static final String X_TILE = "xTile";
	
	private static final String Y_TILE = "yTile";

    /**
     * Max number of tries before giving up
     */
    private static final int MAX_TRIES = 500;

    /**
     * Initiate zoom request for specified guid, slice, frame, site, channels 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void start(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);
        ZoomManager zoomManager= SysManagerFactory.getZoomManager();
        
        long guid = Long.parseLong(getRequiredParam(RequestConstants.GUID_KEY, req));
        int siteNumber = Integer.parseInt(getRequiredParam(RequestConstants.SITE_NUMBER_KEY, req));

        int sliceNumber = Integer.parseInt(req.getParameter(RequestConstants.SLICE_NUMBER_KEY));
        int frameNumber = Integer.parseInt(req.getParameter(RequestConstants.FRAME_NUMBER_KEY));
       
        String useChannelColor = req.getParameter(RequestConstants.USE_CHANNEL_COLOR);
        Boolean channelColor = useChannelColor == null ? true : Boolean.parseBoolean(useChannelColor);

        List<Integer> channelNumbers = gson.fromJson(getRequiredParam(RequestConstants.CHANNEL_NUMBERS_KEY, req)
                .toString(), new TypeToken<List<Integer>>() {
        }.getType());

		String overlayNamesString = getRequiredParam("overlayNames", req);
		logger.logp(Level.INFO, "ZoomServlet", "start", "overlays: "+ overlayNamesString);
		List<String> overlayNames = gson.fromJson(overlayNamesString, new TypeToken<List<String>>() {}.getType());

        logger.logp(Level.INFO, "ZoomServlet", "start", "frame: " + frameNumber + " slice: " + sliceNumber + " site: "
                + siteNumber + " guid: " + guid + " channel color: " + useChannelColor + " channels: " + channelNumbers);
        
        try{
        	long zoomId= zoomManager.registerZoomRequest(userName, guid, frameNumber, sliceNumber, channelNumbers, siteNumber, channelColor, overlayNames);
        	logger.logp(Level.INFO, "ZoomServlet", "start", "Got zoom id: " + zoomId);
            writeJSON(resp, Util.createMap("success", true, ZOOM_ID, String.valueOf(zoomId)));
        }
        catch (Exception e) {
        	writeFailure(resp, "Server Busy");
		}
    }

    /**
     * Get image with the given index.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getTileImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);
        
        long zoomId= Long.parseLong(getRequiredParam(ZOOM_ID, req));

        int xTile = Integer.parseInt(getRequiredParam(X_TILE, req));
        int yTile = Integer.parseInt(getRequiredParam(Y_TILE, req));
        
        int zoomLevel = Integer.parseInt(getRequiredParam(ZOOM_LEVEL, req));
        
        ZoomManager zoomManager= SysManagerFactory.getZoomManager();
        
        File imageFile;
        int tries = 0;
        while ((imageFile = zoomManager.getImage(userName, zoomId, zoomLevel, xTile, yTile) ) == null) {
            try {
                if (++tries > MAX_TRIES) {
                    writeFailure(resp, "Image generation failed");
                    return;
                }
                logger.logp(Level.INFO, "ZoomServlet", "getTileImage",
                		"zoomId:"+ zoomId + "zoomLevel:"+ zoomLevel + "xTile:"+ xTile + "yTile:"+ yTile );
                Thread.sleep(RequestConstants.TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        resp.setContentType("image/jpeg");
        BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());
        try
		{
        	BufferedImage image = ImageIO.read(imageFile);
            ImageIO.write(image, "JPG", outputStream);
		}
		catch (Exception e)
		{ }
        finally 
        {
        	outputStream.close();
        }
    }
    
    /**
     * Check image with the given index.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void waitForTileImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);
        
        long zoomId= Long.parseLong(getRequiredParam(ZOOM_ID, req));

        int xTile = Integer.parseInt(getRequiredParam(X_TILE, req));
        int yTile = Integer.parseInt(getRequiredParam(Y_TILE, req));
        
        int zoomLevel = Integer.parseInt(getRequiredParam(ZOOM_LEVEL, req));
        
        ZoomManager zoomManager= SysManagerFactory.getZoomManager();
        int tries = 0;
        while (zoomManager.getImage(userName, zoomId, zoomLevel, xTile, yTile)  == null) {
            try {
                if (++tries > MAX_TRIES) {
                    writeFailure(resp, "Image generation failed");
                    return;
                }
                logger.logp(Level.INFO, "ZoomServlet", "waitForTileImage",
                		"zoomId:"+ zoomId + "zoomLevel:"+ zoomLevel + "xTile:"+ xTile + "yTile:"+ yTile );
                Thread.sleep(1*1000);//poll after 1 Sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeJSON(resp, Util.createMap("success",true,"exists",true));
    }
    
    /**
     * Wait for thumbnail to be generated.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void waitForThumbnail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);
        
        long zoomId= Long.parseLong(getRequiredParam(ZOOM_ID, req));
        
        ZoomManager zoomManager= SysManagerFactory.getZoomManager();
        int tries = 0;
        while (zoomManager.getThumbnail(userName, zoomId)  == null) {
            try {
                if (++tries > MAX_TRIES) {
                    writeFailure(resp, "Image generation failed");
                    return;
                }
                logger.logp(Level.INFO, "ZoomServlet", "waitForThumbnail",
                		"zoomId:"+ zoomId );
                Thread.sleep(1*1000);//poll after 1 Sec
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeJSON(resp, Util.createMap("success",true,"exists",true));
    }
    
    /**
     * Get image with the given index.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void getThumbnail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = getCurrentUser(req);
        
        long zoomId= Long.parseLong(getRequiredParam(ZOOM_ID, req));
        
        ZoomManager zoomManager= SysManagerFactory.getZoomManager();
        
        File imageFile;
        int tries = 0;
        while ((imageFile = zoomManager.getThumbnail(userName, zoomId) ) == null) {
            try {
                if (++tries > MAX_TRIES) {
                    writeFailure(resp, "Image generation failed");
                    return;
                }
                logger.logp(Level.INFO, "ZoomServlet", "getTileImage",
                		"zoomId:"+ zoomId);
                Thread.sleep(RequestConstants.TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        resp.setContentType("image/jpeg");
        BufferedOutputStream outputStream = new BufferedOutputStream(resp.getOutputStream());

        try
        {
        	BufferedImage image = ImageIO.read(imageFile);
            ImageIO.write(image, "JPG", outputStream);
        }
        catch (Exception e) {}
        finally
        {
        	outputStream.close();
        }
    }
}
