package com.strandgenomics.imaging.tileviewer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author aritra
 *
 */
public class Helper {
	
	public static final String SCHEME = "scheme";
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String AUTH_CODE = "authcode";
	
	public static final String TOKEN = "t";
	
	public static final String RECORD_ID = "r";
	public static final String FRAME_NUMBER = "fn";
	public static final String SLICE_NUMBER= "sn";
	public static final String CHANNEL_NUMBERS = "cn";
	
	public static final String GREY_SCALE = "gs";
	public static final String Z_STACKED = "zs";
	public static final String CONTRAST_SETTINGS = "cs";
	public static final String IMAGEWIDTH = "iw";
	public static final String IMAGEHEIGHT = "ih";
	
	public static final String TILE_DIM = "d";
	public static final String TILE_X = "x";
	public static final String TILE_Y = "y";
	public static final String TILE_ZOOM = "z";
	
	/**
	 * Access a required parameter. Throws {@link ServletException} if its missing.
	 * 
	 * @param paramName
	 * 				name of parameter
	 * @param request
	 * 				request object to use
	 * @return
	 * 				the value string if parameter is present
	 * @throws ServletException
	 * 				if parameter is absent
	 */
	public static String getRequiredParam(String paramName, HttpServletRequest request) throws ServletException {
		if (request.getParameter(paramName) == null) {
			throw new ServletException("Required parameter missing: " + paramName);
		}
		return request.getParameter(paramName);
	}

	/**
	 * Access an optional parameter. Returns null if the parameter is not present
	 * 
	 * @param paramName
	 * 				name of parameter
	 * @param request
	 * 				request object to use
	 * @return
	 * 				the value string if parameter is present
	 * @throws ServletException
	 * 				if parameter is absent
	 */
	public static String getOptionalParam(String paramName, HttpServletRequest request, String defaultValue) throws ServletException {
		if (request.getParameter(paramName) == null) {
			return defaultValue;
		}
		return request.getParameter(paramName);
	}
	
	/**
	 * Private constructor to prevent creation of instance.
	 */
	private Helper() {
		
	}
}
