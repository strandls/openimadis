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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.strandgenomics.imaging.iengine.models.MosaicParameters;
import com.strandgenomics.imaging.iengine.models.MosaicResource;
//import com.strandgenomics.imaging.iclient.impl.ws.ispace.MosaicResource;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.image.LUT;
import com.strandgenomics.imaging.icore.image.PixelArray;
import com.strandgenomics.imaging.icore.image.PixelDepth;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.system.DataExchange;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.zoom.TileNotReadyException;

/**
 * the Servlet handling file exchange between client and server.
 * Typically the things that are exchanged are
 * 1. Archive (upload and download)
 * 2. Attachment (upload and download
 * 3. Custom Thumb-nail (upload and downloads)
 * 4. Raw Pixel Data (as an array of integer, for downloading only)
 * 5. images of various kinds - for downloading only
 * @see com.strandgenomics.imaging.iengine.system.DataExchange
 */
@SuppressWarnings("serial")
public class DataExchangeServlet extends MultiplexingServlet {

    public void init() throws ServletException 
    {
    	SysManagerFactory.getUserManager(); //initializes the users
        logger = Logger.getLogger("com.strandgenomics.imaging.iserver.impl.web");
    }
    
    /**
     * validate the request and returns the parameter values if it is a valid request
     * @param req the request
     * @param res the response
     * @return the parameter values if it is a valid request, else return null
     * @throws ServletException
     * @throws IOException
     */
    private String[] validateRequest(HttpServletRequest req, HttpServletResponse res)
    		throws ServletException, IOException 
    {
		String clientIP = getClientAddress(req);
		logger.logp(Level.INFO, "DataExchangeServlet", "validateRequest", "received request from " + clientIP);
		
		// extract request parameters
		String encryptedParameter = req.getParameter(DataExchange.URL_PARAMETER);
		logger.logp(Level.FINEST, "DataExchangeServlet", "validateRequest", "received encrypted parameters " + encryptedParameter);
		
		String[] values = DataExchange.getValues(encryptedParameter);
		
		for(String v : values){
			System.out.println(v+" ");
		}
		
		logger.logp(Level.INFO, "DataExchangeServlet", "validateRequest", "received decrypted parameters " + values);
		
		String requestIP = null;
		long requestTime = -1;
		String actorLogin = null;
		
		try
		{
			requestIP = values[0];
			requestTime = Long.parseLong(values[1]);
			actorLogin = values[2];
		}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "validateRequest", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
	        return null;
	    } 
		
//		if (!clientIP.equals(requestIP))
//		{
//			logger.logp(Level.WARNING, "DataExchangeServlet", "validateRequest",
//					"received request from different IP expected IP="+ requestIP + " actual IP=" + clientIP);
//			res.sendError(HttpServletResponse.SC_FORBIDDEN, "forbidden this url is not valid from ip " + clientIP);
//			return null;
//		}
		
		if((System.currentTimeMillis() - requestTime) > Constants.getRequestTimeout())
		{
			logger.logp(Level.WARNING, "DataExchangeServlet", "validateRequest","expired request");
			res.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, "expired");
			return null;
		}
		
		logger.logp(Level.INFO, "DataExchangeServlet", "validateRequest", "received valid request from user " + actorLogin);
		return values;
    }
    
    /**
     * Handle for uploading thumb-nail for record
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     */
    public void uploadThumbnail(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadThumbnail", "processing request...");
    	
    	long guid = -1;
    	String actorLogin = null;
    	
    	try
    	{
    		actorLogin = values[2];
	        guid  = Long.parseLong(values[3]); 
	        logger.logp(Level.INFO, "DataExchangeServlet", "uploadThumbnail", "received request for GUID="+guid);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "uploadThumbnail", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown ticket request");
            return;
        }

    	try
    	{
    		BufferedImage thumbnail = readImage(new BufferedInputStream(req.getInputStream()));
    		SysManagerFactory.getThumbnailManager().setThumbnail(actorLogin, guid, thumbnail);
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadThumbnail", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
	}
    
    /**
     * Handle for uploading raw pixel data for record creation
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     */
    public void uploadRawData(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadRawData", "received request...");
    	
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadRawData", "processing request...");
    	
    	String actorLogin;
		long guid;
		int frame;
		int slice;
		int channel;
		int site;
		try
    	{
    		actorLogin = values[2];
    	    guid  = Long.parseLong(values[3]); 
    	    frame = Integer.parseInt(values[4]);
    	    slice = Integer.parseInt(values[5]);
    	    channel = Integer.parseInt(values[6]);
    	    site = Integer.parseInt(values[7]);
    	}
		catch (Exception ex) 
		{
			logger.logp(Level.WARNING, "DataExchangeServlet", "uploadRawData", "error processing request...");
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
	        return;
		}
    	
	    logger.logp(Level.INFO, "DataExchangeServlet", "uploadRawData", "received request for GUID= "+guid+" f "+frame+" z "+slice+" c "+channel+" s "+site);
	    try
    	{
	    	PixelArray rawData = PixelArray.read(req.getInputStream());
	    	SysManagerFactory.getRecordCreationManager().storeRawData(actorLogin, guid, new Dimension(frame,slice,channel,site), rawData);
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadRawData", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
	}
    
    /**
     * Handle for downloading thumb-nail for record
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     */
    public void downloadThumbnail(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadThumbnail", "processing request...");
    	
    	long guid = -1;
    	String actorLogin = null;
    	
    	try
    	{
    		actorLogin = values[2];
	        guid  = Long.parseLong(values[3]); 
	        logger.logp(Level.INFO, "DataExchangeServlet", "downloadThumbnail", "received request for GUID="+guid);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "downloadThumbnail", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown ticket request");
            return;
        }
    	
    	InputStream inStream = null;
    	OutputStream outStream = null;
    	try
    	{
    		inStream = SysManagerFactory.getThumbnailManager().getThumbnail(actorLogin, guid);
	    	outStream = new BufferedOutputStream(res.getOutputStream());
	    	
	    	long dataLength = Util.transferData(inStream, outStream);
	    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadThumbnail", "successfully transferred "+dataLength +" bytes");
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadThumbnail", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    	finally
    	{
    		Util.closeStreams(inStream, outStream);
    	}
	}
    
    /**
     * Handle for downloading Raw PixelArray
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     * @see com.strandgenomics.imaging.iengine.system.ImageManager#getRawPixelArrayDownloadURL
     */
    public void rawPixelData(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "rawPixelData", "processing request...");
    	
    	PixelArray rawData = null;

    	try
    	{
	        long guid  = Long.parseLong(values[3]); 
	        int frameNo = Integer.parseInt(values[4]);
	        int sliceNo = Integer.parseInt(values[5]);
	        int channelNo = Integer.parseInt(values[6]);
	        int siteNo = Integer.parseInt(values[7]);
	        
	        Dimension d = new Dimension(frameNo, sliceNo, channelNo, siteNo);
	        logger.logp(Level.INFO, "DataExchangeServlet", "rawPixelData", "received request image="+d +" request for GUID="+guid);
	        rawData = SysManagerFactory.getImageManager().getRawData(values[2], guid, d, null);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "rawPixelData", "received request from unknown record",e);
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    	
    	sendPixelArray(res, rawData);
	}
    
    /**
     * Handle for downloading PixelData image
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     * @see com.strandgenomics.imaging.iengine.system.ImageManager#getPixelDataDownloadURL
     */
    public void pixelDataImage(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "pixelDataImage", "processing request...");
    	
    	PixelArray image = null;

    	try
    	{
    		String actorLogin = values[2];
	        long guid  = Long.parseLong(values[3]); 
	        int frameNo = Integer.parseInt(values[4]);
	        int sliceNo = Integer.parseInt(values[5]);
	        int channelNo = Integer.parseInt(values[6]);
	        int siteNo = Integer.parseInt(values[7]);
	        boolean useChannelColor = Boolean.parseBoolean(values[8]);
	        
	        Dimension d = new Dimension(frameNo, sliceNo, channelNo, siteNo);
	        logger.logp(Level.INFO, "DataExchangeServlet", "pixelDataImage", "received request image="+d +" request for GUID="+guid);
	        image = SysManagerFactory.getImageDataDownloadManager().getRawImage(actorLogin, guid, d, useChannelColor);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "pixelDataImage", "received request from unknown record",e);
        	res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    	
    	sendImage(res, image.create8BitImage(), image.getWidth(), image.getHeight(), (LUT) image.getColorModel());
	}
        
	/**
     * Handle for downloading Raw PixelArray for Tile
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     * @see com.strandgenomics.imaging.iengine.system.ImageManager#getTilePixelArrayDownloadURL
     */
    public void rawTileData(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "rawTileData", "processing request...");
    	
       	PixelArray rawData = null;

    	try
    	{
	        long guid     = Long.parseLong(values[3]); 
	        int frameNo   = Integer.parseInt(values[4]);
	        int sliceNo   = Integer.parseInt(values[5]);
	        int channelNo = Integer.parseInt(values[6]);
	        int siteNo    = Integer.parseInt(values[7]);
	        
	        int x      = Integer.parseInt(values[8]);
	        int y      = Integer.parseInt(values[9]);
	        int width  = Integer.parseInt(values[10]); 
	        int height = Integer.parseInt(values[11]);
	        
	        Dimension d = new Dimension(frameNo, sliceNo, channelNo, siteNo);
	        logger.logp(Level.INFO, "DataExchangeServlet", "rawTileData", "received request image="+d +" request for GUID="+guid);
	        
	        rawData = SysManagerFactory.getImageManager().getRawData(values[2],guid, d, new Rectangle(x, y, width, height));
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "rawPixelData", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    
    	sendPixelArray(res, rawData);
	}

    /**
     * Handle for downloading image for a Tile
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     * @see com.strandgenomics.imaging.iengine.system.ImageManager#getTileDownloadURL
     */
    public void tileImage(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "tileImage", "processing request...");
    	
      	PixelArray image = null;

    	try
    	{
    		String actorLogin = values[2];
	        long guid     = Long.parseLong(values[3]); 
	        int frameNo   = Integer.parseInt(values[4]);
	        int sliceNo   = Integer.parseInt(values[5]);
	        int channelNo = Integer.parseInt(values[6]);
	        int siteNo    = Integer.parseInt(values[7]);
	        
	        boolean useChannelColor = Boolean.parseBoolean(values[8]);
	        
	        int x      = Integer.parseInt(values[9]);
	        int y      = Integer.parseInt(values[10]);
	        int width  = Integer.parseInt(values[11]); 
	        int height = Integer.parseInt(values[12]);
	        
	        Dimension d = new Dimension(frameNo, sliceNo, channelNo, siteNo);
	        logger.logp(Level.INFO, "DataExchangeServlet", "rawTileData", "received request image="+d +" request for GUID="+guid);
	        
	        image = SysManagerFactory.getImageDataDownloadManager().getRawTileImage(actorLogin, guid, d, useChannelColor, x, y, width, height);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "rawTileData", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    	
    	sendImage(res, image.create8BitImage(), image.getWidth(), image.getHeight(), (LUT) image.getColorModel());
	}
	
    /**
     * Handle for downloading image for an overlay
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.DataExchange
     *  @see com.strandgenomics.imaging.iengine.system.ImageManager#getPixelDataOverlayDownloadURL
     */
	public void overlayImage(HttpServletRequest req, HttpServletResponse res)
			throws TileNotReadyException, ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "overlayImage", "processing request...");
    	
      	PixelArray image = null;
      	boolean overlaid = false;

    	try
    	{
    		String actorLogin = values[2];
	        long guid     = Long.parseLong(values[3]); 
	        
	        int frameNo   = Integer.parseInt(values[4]);
	        int sliceNo   = Integer.parseInt(values[5]); 
	        int siteNo    = Integer.parseInt(values[6]); 
	        
	        boolean useChannelColor = Boolean.parseBoolean(values[7]);
	        boolean zStacked        = Boolean.parseBoolean(values[8]); 
	        boolean mosaic          = Boolean.parseBoolean(values[9]);
	        int x = Integer.parseInt(values[10]);
	        int y = Integer.parseInt(values[11]);
	        int width = Integer.parseInt(values[12]);
	        int height = Integer.parseInt(values[13]);
	        
	        int[] channels = new int[values.length-14];
	        for(int i = 0;i < channels.length; i++)
	        	channels[i] = Integer.parseInt(values[14+i]);
	        
	        overlaid = channels.length > 1;
	        
	        logger.logp(Level.INFO, "DataExchangeServlet", "overlayImage", "received overlay request for GUID="+guid);
	        
	        image = SysManagerFactory.getImageDataDownloadManager().createOverlayPixelArray(actorLogin, guid,
	    			sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, new Rectangle(x, y, width, height));
    	}
        catch(Exception e)
        {
        	//logger.logp(Level.WARNING, "DataExchangeServlet", "overlayImage", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    	
    	if(overlaid)
    		sendOverlayImage(res, image);
    	else
    		sendImage(res, image.create8BitImage(), image.getWidth(), image.getHeight(), (LUT) image.getColorModel());
	}
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void overlayImageRescale(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "overlayImageRescale", "processing request...");
    	
      	PixelArray image = null;
      	boolean overlaid = false;

    	try
    	{
    		String actorLogin = values[2];
	        long guid     = Long.parseLong(values[3]); 
	        
	        int frameNo   = Integer.parseInt(values[4]);
	        int sliceNo   = Integer.parseInt(values[5]); 
	        int siteNo    = Integer.parseInt(values[6]); 
	        
	        boolean useChannelColor = Boolean.parseBoolean(values[7]);
	        boolean zStacked        = Boolean.parseBoolean(values[8]); 
	        boolean mosaic          = Boolean.parseBoolean(values[9]);
	        int x = Integer.parseInt(values[10]);
	        int y = Integer.parseInt(values[11]);
	        int width = Integer.parseInt(values[12]);
	        int height = Integer.parseInt(values[13]);
	        int targetWidth = Integer.parseInt(values[14]);
	        int targetHeight = Integer.parseInt(values[15]);
	        
	        int[] channels = new int[values.length-16];
	        for(int i = 0;i < channels.length; i++)
	        	channels[i] = Integer.parseInt(values[16+i]);
	        
	        overlaid = channels.length > 1;
	        
	        logger.logp(Level.INFO, "DataExchangeServlet", "overlayImageRescale", "received overlay request for GUID="+guid);
	        
	        image = SysManagerFactory.getImageDataDownloadManager().createOverlayPixelArray(actorLogin, guid,
	    			sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, new Rectangle(x, y, width, height), new Rectangle(0, 0, targetWidth, targetHeight));
	        
	        logger.logp(Level.INFO, "DataExchangeServlet", "overlayImageRescale", "read image");
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "overlayImageRescale", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    	
    	logger.logp(Level.INFO, "DataExchangeServlet", "overlayImageRescale", "overlaid "+overlaid);
    	if(overlaid)
    		sendOverlayImage(res, image);
    	else
    	{
    		try
			{
    			sendImage(res, image.create8BitImage(), image.getWidth(), image.getHeight(), (LUT) image.getColorModel());
			}
			catch (Exception e)
			{
				logger.logp(Level.INFO, "DataExchangeServlet", "overlayImageRescale", "error in sending the image", e);
			}
    		
    	}
	}
	
	public void overlayImageWithContrast(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "overlayImage", "processing request...");
    	
      	PixelArray image = null;
      	boolean overlaid = false;

    	try
    	{
    		String actorLogin = values[2];
	        long guid     = Long.parseLong(values[3]); 
	        
	        int frameNo   = Integer.parseInt(values[4]);
	        int sliceNo   = Integer.parseInt(values[5]); 
	        int siteNo    = Integer.parseInt(values[6]); 
	        
	        boolean useChannelColor = Boolean.parseBoolean(values[7]);
	        boolean zStacked        = Boolean.parseBoolean(values[8]); 
	        boolean mosaic          = Boolean.parseBoolean(values[9]);
	        int x = Integer.parseInt(values[10]);
	        int y = Integer.parseInt(values[11]);
	        int width = Integer.parseInt(values[12]);
	        int height = Integer.parseInt(values[13]);
	        
	        int channelAndContrastNo = values.length-14;
	        int channelNos = channelAndContrastNo/3;
	        int[] channels = new int[channelNos];
	        for(int i = 0;i < channels.length; i++)
	        	channels[i] = Integer.parseInt(values[14+i]);
	        
	        int contrastNos = values.length - 14 - channelNos;
	        int contrasts[] = new int[contrastNos];
	        for(int i = 0;i < contrasts.length; i++)
	        	contrasts[i] = Integer.parseInt(values[14+channelNos+i]);
	        
	        overlaid = channels.length > 1;
	        
	        logger.logp(Level.INFO, "DataExchangeServlet", "overlayImage", "received overlay request for GUID="+guid);
	        
	        image = SysManagerFactory.getImageDataDownloadManager().createOverlayPixelArray(actorLogin, guid,
	    			sliceNo, frameNo, siteNo, channels, useChannelColor, zStacked, mosaic, new Rectangle(x, y, width, height), contrasts);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "overlayImage", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad request "+e.getMessage());
            return;
        }
    	
    	if(overlaid)
    		sendOverlayImageWithContrast(res, image);
    	else
    		sendImageWithContrast(res, image.create8BitImage(), image.getWidth(), image.getHeight(), (LUT) image.getColorModel(), image.getMinContrastSetting(), image.getMaxContrastSetting());
	}
    
    
    /**
     * Download the content of the channel overlaid slices for the specified frame and site
     * @param req the http request
     * @param res the http  response
     * @throws ServletException
     * @throws IOException
     * @see com.strandgenomics.imaging.iengine.system.ImageManager#getChannelOverlayForSlicesURL
     */
    public void channelOverlaidSlices(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
    {
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "channelOverlaidSlices", "processing request...");
		
		long guid = -1;
		int frameNo = -1, siteNo=-1, imageWidth=-1;
		boolean useChannelColor=false;
		String actorLogin = null;
		
		try
		{
			actorLogin = values[2];
			guid =  Long.parseLong(values[3]);
			
			frameNo = Integer.parseInt(values[4]);
			siteNo = Integer.parseInt(values[5]);
			imageWidth = Integer.parseInt(values[6]);
			useChannelColor = Boolean.parseBoolean(values[7]);
		}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "channelOverlaidSlices", "bad request",ex);
	        res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
	        return;
	    } 

		try 
		{
			SysManagerFactory.getImageDataDownloadManager().sendChannelOverlaidSlices(res.getOutputStream(), actorLogin, guid, frameNo, siteNo, imageWidth, useChannelColor);
		} 
		catch (Exception ex) 
		{
			logger.logp(Level.INFO, "DataExchangeServlet", "channelOverlaidSlices", "error serving request", ex);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,	ex.getMessage());
			return;
		}
    }
    
    /**
     * export out the archive (experiment)
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void downloadArchive(HttpServletRequest req, HttpServletResponse res) 
    		throws ServletException, IOException
    {
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadArchive", "processing request...");
    	
    	BigInteger archiveSignature = null;
    	try
    	{
    		archiveSignature = Util.toBigInteger(values[3]);
	        logger.logp(Level.INFO, "DataExchangeServlet", "downloadArchive", "received request for archiveHash="+archiveSignature);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "downloadArchive", "received request from unknown record",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown ticket request");
            return;
        }
    	
    	InputStream inStream = null;
    	OutputStream outStream = null;
    	try
    	{
	    	File archiveFile = SysManagerFactory.getStorageManager().getArchive(archiveSignature, false);
	    	inStream = new BufferedInputStream(new FileInputStream(archiveFile));
	    	outStream = new BufferedOutputStream(res.getOutputStream());
	    	
	    	long dataLength = Util.transferData(inStream, outStream);
	    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadArchive", "successfully transferred "+dataLength +" bytes");
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadArchive", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    	finally
    	{
    		Util.closeStreams(inStream, outStream);
    	}
    }
    
	/**
     * uploads record archive to the server
     */
    public void uploadArchive(HttpServletRequest req, HttpServletResponse res) 
    		throws ServletException, IOException
    {
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	
    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadArchive", "processing request...");
    	long ticketID = -1;
    	
    	try
    	{
	        ticketID  = Long.parseLong(values[3]); 
	        logger.logp(Level.INFO, "DataExchangeServlet", "uploadArchive", "received request for ticketID="+ticketID);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "uploadArchive", "received request from unknown ticket",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown ticket request");
            return;
        }
        
        if(!SysManagerFactory.getTicketManager().isValid(ticketID))
        {
        	logger.logp(Level.INFO, "DataExchangeServlet", "uploadArchive", "expired ticket...");
        	res.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT , "generated url is valid for one hour only");
            return;
        }

        try
        {
        	SysManagerFactory.getTicketManager().acceptRequest(req.getInputStream(), ticketID);
	    }
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadArchive", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    }
    
    /**
     * uploads log file generated from specified task
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void uploadTaskLog(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadTaskLog", "processing request...");
    	
    	long taskID = -1;
    	String logFileName = null;
    	
    	try
    	{
    		taskID = Long.parseLong(values[3]); 
    		logFileName = values[4].toString();
    		
	        logger.logp(Level.INFO, "DataExchangeServlet", "uploadTaskLog", "received request for taskid="+taskID +", logFileName="+logFileName);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "uploadTaskLog", "mal formed request",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown log file upload request");
            return;
        }

		logger.logp(Level.INFO, "DataExchangeServlet", "uploadTaskLog", "received request for logfile=" + logFileName + " for taskID="+taskID);

		try 
		{
			SysManagerFactory.getComputeEngine().addTaskLog(taskID, logFileName, req.getInputStream());
			// send the response
			res.setStatus(HttpServletResponse.SC_OK);
		} 
		catch (Exception ex) 
		{
			logger.logp(Level.INFO, "DataExchangeServlet", "uploadTaskLog","error serving request", ex);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,ex.getMessage());
			return;
		}
	}
    
    /**
     * upload attachment
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
	public void uploadAttachment(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "uploadAttachment", "processing request...");
    	
    	int projectID = -1; 
    	long guid = -1;
    	String attachmentName = null;
    	
    	try
    	{
    		projectID = Integer.parseInt(values[3]); 
    		guid  = Long.parseLong(values[4]); 
    		attachmentName = values[5].toString();
    		
	        logger.logp(Level.INFO, "DataExchangeServlet", "uploadAttachment", "received request for guid="+guid +", attachmentName="+attachmentName);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "uploadAttachment", "mal formed request",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown attachment upload request");
            return;
        }

		logger.logp(Level.INFO, "DataExchangeServlet", "uploadAttachment", "received request for attachment=" + attachmentName + " for GUID="+guid);

		try 
		{
			SysManagerFactory.getAttachmentManager().acceptAttachmentUploadRequest(values[2], req.getInputStream(), projectID, guid, attachmentName);
			// send the response
			res.setStatus(HttpServletResponse.SC_OK);
		} 
		catch (Exception ex) 
		{
			logger.logp(Level.INFO, "DataExchangeServlet", "uploadAttachment","error serving request", ex);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,ex.getMessage());
			return;
		}
	}
    
    /**
     * download attachment request
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
	public void downloadAttachment(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadAttachment", "processing request...");
    	
    	int projectID = -1; 
    	long guid = -1;
    	String attachmentName = null;
    	
    	try
    	{
    		projectID = Integer.parseInt(values[3]); 
    		guid  = Long.parseLong(values[4]); 
    		attachmentName = values[5].toString();
	        logger.logp(Level.INFO, "DataExchangeServlet", "downloadAttachment", "received request for guid="+guid +", attachmentName="+attachmentName);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "downloadAttachment", "malformed request",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown attachment upload request");
            return;
        }

		logger.logp(Level.INFO, "DataExchangeServlet", "downloadAttachment","received request for attachment=" + attachmentName	+ " for GUID=" + guid);

		try 
		{
			SysManagerFactory.getAttachmentManager().acceptAttachmentDownloadRequest(values[2], res.getOutputStream(), attachmentName, projectID, guid);
		} 
		catch (Exception ex) 
		{
			logger.logp(Level.INFO, "DataExchangeServlet", "downloadAttachment", "error serving request", ex);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,	ex.getMessage());
			return;
		}
	}
	
	/**
     * download log file
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
	public void downloadLogFile(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
    	String[] values = validateRequest(req, res);
    	if(values == null) return;
    	logger.logp(Level.INFO, "DataExchangeServlet", "downloadLogFile", "processing request...");
    	
    	String logfileName = null;
    	
    	try
    	{
    		logfileName = values[5].toString();
	        logger.logp(Level.INFO, "DataExchangeServlet", "downloadLogFile", "received request for logfilename="+logfileName);
    	}
        catch(Exception e)
        {
        	logger.logp(Level.WARNING, "DataExchangeServlet", "downloadLogFile", "malformed request",e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown attachment upload request");
            return;
        }

		logger.logp(Level.INFO, "DataExchangeServlet", "downloadLogFile","received request for logfilename=" + logfileName);

		try 
		{
			SysManagerFactory.getLogSearchManager().acceptRequest(values[2], res.getOutputStream(), logfileName);
		} 
		catch (Exception ex) 
		{
			logger.logp(Level.INFO, "DataExchangeServlet", "downloadLogFile", "error serving request", ex);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,	ex.getMessage());
			return;
		}
	}
	
    private BufferedImage readImage(InputStream inStream)
	{
		BufferedInputStream in = null;
		BufferedImage renderableImage = null;
		try
		{
			in = new BufferedInputStream(inStream);
			renderableImage = ImageIO.read(in);
		}
		catch(IOException ex)
		{
			logger.logp(Level.INFO, "DataExchangeServlet", "readImage", "successfully transferred Pixel Image...");
		}
		finally
		{
			try 
			{
				in.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return renderableImage;
	}
	
    private void sendPixelArray(HttpServletResponse res, PixelArray rawData) throws IOException
    {
    	res.setContentType("application/octet-stream");
    	OutputStream outStream = null;
    	try
    	{
    		long startTime = System.currentTimeMillis();
    		outStream = new BufferedOutputStream( new GZIPOutputStream(res.getOutputStream()){{
                def.setLevel(Deflater.DEFAULT_COMPRESSION);
             }});
    		
    		rawData.write(outStream);
	    	long endTime = System.currentTimeMillis();
			
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendPixelArray", "successfully transferred Raw Pixel Data..."+(endTime-startTime));
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendPixelArray", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    	finally
    	{
    		Util.closeStream(outStream);
    	}
    }
    
    private void sendOverlayImage(HttpServletResponse res, PixelArray image) throws IOException 
    {
    	res.setContentType("application/octet-stream");
    	DataOutputStream outStream = null;
    	try
    	{
    		long startTime = System.currentTimeMillis();
    		outStream = new DataOutputStream(new BufferedOutputStream( new GZIPOutputStream(res.getOutputStream()){{
                def.setLevel(Deflater.DEFAULT_COMPRESSION);
             }}));
    		
    		if(image instanceof PixelArray.Integer) //for overlaid images - RGB images
    		{
    			outStream.writeInt(PixelDepth.INT.getByteSize());
        		outStream.writeInt(image.getWidth());
        		outStream.writeInt(image.getHeight());
        		
        		int[] pixels = (int[])image.getPixelArray();
        		for(int i = 0; i < pixels.length; i++)
        			outStream.writeInt( pixels[i] );
    		}
    		else // for single image, with color model
    		{
    			throw new IllegalStateException("illegal call");
    		}

	    	long endTime = System.currentTimeMillis();
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "successfully transferred Pixel Image in "+(endTime-startTime) +" ms");
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    	finally
    	{
    		Util.closeStream(outStream);
    	}
	}
    
    private void sendOverlayImageWithContrast(HttpServletResponse res, PixelArray image) throws IOException 
    {
    	res.setContentType("application/octet-stream");
    	DataOutputStream outStream = null;
    	try
    	{
    		long startTime = System.currentTimeMillis();
    		outStream = new DataOutputStream(new BufferedOutputStream( new GZIPOutputStream(res.getOutputStream()){{
                def.setLevel(Deflater.DEFAULT_COMPRESSION);
             }}));
    		
    		if(image instanceof PixelArray.Integer) //for overlaid images - RGB images
    		{
    			outStream.writeInt(PixelDepth.INT.getByteSize());
        		outStream.writeInt(image.getWidth());
        		outStream.writeInt(image.getHeight());
        		outStream.writeInt(image.getMinContrastSetting());
        		outStream.writeInt(image.getMaxContrastSetting());
        		
        		int[] pixels = (int[])image.getPixelArray();
        		for(int i = 0; i < pixels.length; i++)
        			outStream.writeInt( pixels[i] );
    		}
    		else // for single image, with color model
    		{
    			throw new IllegalStateException("illegal call");
    		}

	    	long endTime = System.currentTimeMillis();
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "successfully transferred Pixel Image in "+(endTime-startTime) +" ms");
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    	finally
    	{
    		Util.closeStream(outStream);
    	}
	}
    
    private void sendImage(HttpServletResponse res, byte[] pixels, int width, int height, LUT cm) throws IOException 
    {
    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "sending image");
    	
    	res.setContentType("application/octet-stream");
    	DataOutputStream outStream = null;
    	try
    	{
    		long startTime = System.currentTimeMillis();
    		outStream = new DataOutputStream(new BufferedOutputStream( new GZIPOutputStream(res.getOutputStream()){{
                def.setLevel(Deflater.DEFAULT_COMPRESSION);
             }}));
    		
			outStream.writeInt(PixelDepth.BYTE.getByteSize());
    		outStream.writeInt(width);
    		outStream.writeInt(height);

    		for(int i = 0; i < pixels.length; i++)
    			outStream.writeByte( pixels[i] );

    		//write red, blue and green
    		outStream.write(cm.getBytes());
    		
	    	long endTime = System.currentTimeMillis();
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "successfully transferred Pixel Image in "+(endTime-startTime) +" ms");
    	}
	    catch(Exception ex)
	    {
	    	logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "error serving request",ex);
	        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
	        return;
	    } 
    	finally
    	{
    		Util.closeStream(outStream);
    	}
    }
    
    private void sendImageWithContrast(HttpServletResponse res, byte[] pixels, int width, int height, LUT cm, int min, int max) throws IOException 
    {
    	res.setContentType("application/octet-stream");
    	DataOutputStream outStream = null;
    	try
    	{
    		long startTime = System.currentTimeMillis();
    		outStream = new DataOutputStream(new BufferedOutputStream( new GZIPOutputStream(res.getOutputStream()){{
    			def.setLevel(Deflater.DEFAULT_COMPRESSION);
    		}}));
    		
    		outStream.writeInt(PixelDepth.BYTE.getByteSize());
    		outStream.writeInt(width);
    		outStream.writeInt(height);
    		outStream.writeInt(min);
    		outStream.writeInt(max);
    		
    		for(int i = 0; i < pixels.length; i++)
    			outStream.writeByte( pixels[i] );
    		
    		//write red, blue and green
    		outStream.write(cm.getBytes());
    		
    		long endTime = System.currentTimeMillis();
    		logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "successfully transferred Pixel Image in "+(endTime-startTime) +" ms");
    	}
    	catch(Exception ex)
    	{
    		logger.logp(Level.INFO, "DataExchangeServlet", "sendImage", "error serving request",ex);
    		res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    		return;
    	} 
    	finally
    	{
    		Util.closeStream(outStream);
    	}
    }
    
    /**
     * returns client ip address
     * @param req
     * @return client ip address
     */
    protected String getClientAddress(HttpServletRequest req)
    {
        String clientIP = req.getRemoteAddr();

        if(clientIP != null && (clientIP.equals("127.0.0.1") || clientIP.equalsIgnoreCase("localhost")))
        {
            clientIP = Util.getMachineIP();
        }
        
        if(clientIP == null)
        {
            clientIP = "";
        }
        
        return clientIP;
    }
    
    /**
     * gives mosaic image
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    public void mosaic(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	int k=3;
    	
    	String values[] = validateRequest(req, res);
    	
    	String actor = values[k];
    	k++;
    	
    	int numberOfRecordids = Integer.valueOf(values[k]);
    	k++;
    	
    	long recordids[] = new long[numberOfRecordids];
    	
    	int i=0;
    	for(i=0;i<numberOfRecordids;i++){
    		recordids[i] = Integer.valueOf(values[k+i]);
    	}
    	k=k+i;
    	
    	int anchor_left = Integer.valueOf(values[k]);
    	k++;
    	
    	int anchor_top = Integer.valueOf(values[k]);
    	k++;
    	
    	MosaicResource resource = new MosaicResource(recordids,anchor_left,anchor_top);
    	
    	int X = Integer.valueOf(values[k]);
    	k++;
    	
    	int Y = Integer.valueOf(values[k]);
    	k++;
    	
    	int width = Integer.valueOf(values[k]);
    	k++;
    	
    	int height = Integer.valueOf(values[k]);
    	k++;
    	
    	int tileWidth = Integer.valueOf(values[k]);
    	k++;
    	
    	int tileHeight = Integer.valueOf(values[k]);
    	k++;
    	
    	MosaicParameters params = new MosaicParameters();
    	params.setX(X);
    	params.setY(Y);
    	params.setWidth(width);
    	params.setHeight(height);
    	params.setTileWidth(tileWidth);
    	params.setTileHeight(tileHeight);
    	
    	
    	BufferedImage img = SysManagerFactory.getMosaicManager().getMosaicElement(actor, resource, params);
    
    	sendOverlayImage(res, PixelArray.toPixelArray(img));
    }
    
}
