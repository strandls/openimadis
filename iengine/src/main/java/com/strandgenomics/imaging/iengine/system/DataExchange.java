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

package com.strandgenomics.imaging.iengine.system;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.util.EncryptionUtil;
import com.strandgenomics.imaging.icore.util.Util;

/**
 * Enumerates the controllers available with the DataExchange Servlet
 * @author arunabha
 *
 */
public enum DataExchange {
	
	/**
	 * URL for uploading thumb-nail for record
	 */
	THUMBNAIL_UPLOAD ("uploadThumbnail"),
    /**
	 * URL for downloading thumb-nail for record
	 */
	THUMBNAIL_DOWNLOAD ("downloadThumbnail"),
	/**
	 * URL for downloading Raw PixelArray
	 */
	RAW_PIXEL_ARRAY("rawPixelData"),
	/**
	 * URL for uploading Raw PixelArray
	 */
	UPLOAD_RAW_DATA("uploadRawData"),
	/**
	 * URL for downloading PixelData image
	 */
	PIXEL_DATA("pixelDataImage"),
	/**
	 * URL for downloading Raw PixelArray for Tile
	 */
	RAW_TILE_ARRAY("rawTileData"),
	/**
	 * URL for downloading image for a Tile
	 */
	TILE("tileImage"),
	/**
	 * URL for downloading image for an overlay
	 */
	OVERLAY("overlayImage"),
	/**
	 * URL for downloading image for an overlay
	 */
	OVERLAY_RESCALE("overlayImageRescale"),
	/**
	 * URL for downloading channel-overlaid images for all slices
	 */
	CHANNEL_OVERLAID_SLICES ("channelOverlaidSlices"),
	/**
	 * URL to download specific attachment
	 */
	DOWNLOAD_ATTACHMENT ("downloadAttachment"),
	/**
	 * URL to upload specific attachment
	 */
	UPLOAD_ATTACHMENT ("uploadAttachment"),
	/**
	 * URL to download specific archive
	 */
	DOWNLOAD_ARCHIVE ("downloadArchive"),
	/**
	 * URL to upload specific archive
	 */
	UPLOAD_ARCHIVE ("uploadArchive"),	
	/**
	 * URL to upload task log
	 */
	UPLOAD_TASK_LOG ("uploadTaskLog"),
	/**
	 * URL to download overlay pixel data with specified contrast
	 */
	OVERLAYWITHCONTRAST("overlayImageWithContrast"),
	/**
	 * URL to download mosaic image;
	 */
	MOSAIC("mosaic");
	
	/**
	 * name of the URL parameter that will carry the parameter values
	 */
	public static final String URL_PARAMETER = "id"; 

	/**
	 * name of the controller in the DataExchange Servlet
	 */
	private String controllerName = null;
	/**
	 * the logger
	 */
	protected Logger logger;

	private DataExchange(String name)
	{
		this.controllerName = name;
		this.logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
	}
	
	@Override
	public String toString()
	{
		return controllerName;
	}
	
	/**
	 * Returns the values from the parameter string encrypted with the server public key
	 * @param encryptedParameter the parameter string
	 * @return the extracted values
	 */
	public static String[] getValues(String encryptedParameter)
	{
		byte[] encryptedData = Util.hexToByteArray(encryptedParameter);
		byte[] originalData = EncryptionUtil.decryptAsymmetric(
				encryptedData, SysManagerFactory.getSecurityManager().getPrivateKey());

		String parameter = new String(originalData, 0, originalData.length);
		return parameter.split(",");
	}
	
	/**
	 * Returns the generated URL for the specified
	 * @param controllerName name of the controller in the data exchange servlet
	 * @param parameter parameters for the controller
	 * @return the generated URL
	 */
	public String generateURL(String actorLogin, String clientIP, long requestTime, Object ... parameters)
	{
		StringBuilder buffer = new StringBuilder();
		//fixed parameters
		buffer.append(clientIP).append(',');
		buffer.append(requestTime).append(',');
		buffer.append(actorLogin);
		//dynamic parameters
		if(parameters != null)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				buffer.append(',');
				buffer.append( parameters[i].toString() );
			}
		}

		String parameter = buffer.toString();
		logger.logp(Level.FINE, "DataExchange", "generateURL", "creating url for "+controllerName +" with (" + parameter + ")");

		byte[] source = null;
		try 
		{
			source = parameter.getBytes("UTF-8");
		} 
		catch (UnsupportedEncodingException e)
		{
			logger.logp(Level.SEVERE, "DataExchange", "generateURL", "unable to encode with UTF-8", e);
		}

		byte[] encryptedData = EncryptionUtil.encryptAsymmetric(source, SysManagerFactory.getSecurityManager().getPublicKey());
		String encryptedParam = Util.toHexString(encryptedData);

		buffer = new StringBuilder();
		
        String baseURL = Constants.getExchangeURLPrefix();
        if(!baseURL.endsWith("/")) baseURL = baseURL +"/";
        
        buffer.append(baseURL);
		buffer.append(controllerName);
		buffer.append('?').append(URL_PARAMETER).append('=');
		buffer.append(encryptedParam);

		String generatedURL = buffer.toString();
		logger.logp(Level.INFO, "DataExchange", "generateURL", "generated URL " + generatedURL);

		return generatedURL;
	}
}
