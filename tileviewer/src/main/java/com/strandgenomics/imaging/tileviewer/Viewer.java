/**
 * 
 */
package com.strandgenomics.imaging.tileviewer;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.strandgenomics.imaging.iclient.AuthenticationException;
import com.strandgenomics.imaging.iclient.ImageSpaceFactory;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.ImageSpaceSystem;
import com.strandgenomics.imaging.iclient.Record;
import com.strandgenomics.imaging.iclient.impl.ws.ispace.Area;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.Histogram;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.tileviewer.VisualObjectsTranformer.VisualObjectTransformer;
import com.strandgenomics.imaging.tileviewer.VisualObjectsTranformer.VisualObjectsFactory;
import com.strandgenomics.imaging.tileviewer.system.RecordParameters;
import com.strandgenomics.imaging.tileviewer.system.TileManager;
import com.strandgenomics.imaging.tileviewer.system.TileNotReadyException;
import com.strandgenomics.imaging.tileviewer.system.TileParameters;
import com.strandgenomics.imaging.tileviewer.system.TileRequestCancelledException;

/**
 * @author aritra
 * 
 */
public class Viewer {

	private String appId;
	private File storageRoot;
	private Map<Long, String> contrastMap;

	/**
	 * @param context
	 */
	public Viewer(ServletContext context)
	{
		Properties prop = new Properties();
		try
		{
			//System.out.println(context.getRealPath("WEB-INF/client.properties"));
			prop.load(new FileInputStream(context.getRealPath("WEB-INF/client.properties")));
			appId = prop.getProperty("clientId");
			System.out.println("clientid="+prop.getProperty("clientId"));
			String storagePath = prop.getProperty("cacheDir");
			this.storageRoot = new File(storagePath);
			
			System.setProperty("cache_storage_root", this.storageRoot.getAbsolutePath());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		contrastMap = new HashMap<Long, String>();
	}
	
	public void getTileTest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException{
		JSONObject json = new JSONObject();
		System.out.println("getTileTest");
		json.put("status", "empty");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(json.toString());
		out.close();		
	}
	
	
	public void getVisualOverlays (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException{
		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;		
		
		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);
		
		VODimension coordinate = new VODimension(0,0,0);
		
		Collection<IVisualOverlay> visualOverlays = iSpace.getVisualOverlays(recordid, coordinate);
		
		List<String> visualOverlaysNames = new ArrayList<String>();
		
		if(visualOverlays!=null){
			for(IVisualOverlay ivo : visualOverlays){
				visualOverlaysNames.add(ivo.getName());
			}
		}
		
        Gson gson = new Gson();
		String json = gson.toJson(visualOverlaysNames);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
	}
	
	/**
	 * create new overlay
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void createOverlay (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		System.out.println("createOverlay");
		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;
		
		String overlayname = request.getParameter("overlayName");

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);
		
		iSpace.createVisualOverlays(recordid,0, overlayname);
	}
	
	/**
	 * delete overlay in record
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteOverlay (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		System.out.println("deleteOverlay");
		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;
		
		String overlayname = request.getParameter("overlayName");

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);
		
		iSpace.deleteVisualOverlays(recordid,0, overlayname);
	}
	
	/**
	 * search overlay
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void searchOverlay (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		System.out.println("searchOverlay");
		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;
		
		String overlayname = request.getParameter("overlayName");
		
		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);
		
		VODimension coordinate = new VODimension(0,0,0);
		//iSpace.get
		IVisualOverlay Overlay = iSpace.getVisualOverlay(recordid, coordinate, overlayname);
		Collection<VisualObject> visualObjects = Overlay.getVisualObjects();
		
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
        if (visualObjects != null) {
        	VisualObjectTransformer instance = VisualObjectsFactory.getVisualObjectTransformer("kinetic");
            for (VisualObject vo : visualObjects){
            	Map<String, Object> DataObject = (Map<String, Object>)instance.encode(vo);
                DataObject.put("zoom_level", vo.getZoomLevel());
                ret.add(DataObject);
            }
        }
        
        Gson gson = new Gson();
		String json = gson.toJson(ret);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
	}
	
	/**
	 * get visual objects in a specified area
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void getOverlays (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException{
		System.out.println("getOverlays");
		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;
		
		int areaX = Integer.valueOf(request.getParameter("areaX"));
		int areaY =  Integer.valueOf(request.getParameter("areaY"));
		int areaW =  Integer.valueOf(request.getParameter("areaW"));
		int areaH =  Integer.valueOf(request.getParameter("areaH"));
		String overlayname = request.getParameter("overlayName");

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);
		
		VODimension coordinate = new VODimension(0,0,0);
		
		Area a = new Area(areaH, areaW, areaX, areaY);
		
		System.out.println(areaX+" "+areaY+" "+areaW+" "+areaH);
		
		Collection<VisualObject> visualObjects = iSpace.findVisualObjects(recordid, coordinate, overlayname, a);
		
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		
        if (visualObjects != null) {
        	VisualObjectTransformer instance = VisualObjectsFactory.getVisualObjectTransformer("kinetic");
            for (VisualObject vo : visualObjects){
            	Map<String, Object> DataObject = (Map<String, Object>)instance.encode(vo);
            	DataObject.put("zoom_level", vo.getZoomLevel());
            	ret.add(DataObject);
            }
        }
        
       // System.out.println("No of object="+visualObjects.size());
        
        Gson gson = new Gson();
		String json = gson.toJson(ret);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
	}
	
	/**
	 * save the visual objects drawn in a specified area
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void saveOverlays(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException
	{
		System.out.println("saveOverlays");
		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;
		
		int areaX = Integer.valueOf(request.getParameter("areaX"));
		int areaY =  Integer.valueOf(request.getParameter("areaY"));
		int areaW =  Integer.valueOf(request.getParameter("areaW"));
		int areaH =  Integer.valueOf(request.getParameter("areaH"));
		String overlayname = request.getParameter("overlayName");

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);
		
		VODimension coordinate = new VODimension(0,0,0);
		
		Area a = new Area(areaH, areaW, areaX, areaY);
		
		System.out.println(areaX+" "+areaY+" "+areaW+" "+areaH);
		
		Collection<VisualObject> visualObjectsinArea = iSpace.findVisualObjects(recordid, coordinate, overlayname, a);
		
		if(visualObjectsinArea!=null){
			iSpace.deleteVisualObjects(recordid, visualObjectsinArea, overlayname, coordinate);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String visualObjectsString = "";
        if(br != null){
        	visualObjectsString = br.readLine();
        }
		List<VisualObject> visualObjects = new ArrayList<VisualObject>();
		
		List<Map<String, Object>> overlays = new ObjectMapper().readValue(visualObjectsString, List.class);
		
		if(visualObjectsString!=null){
			VisualObjectTransformer instance = VisualObjectsFactory.getVisualObjectTransformer("kinetic");
            for (Map<String, Object> overlay : overlays)
            { 
            	VisualObject obj = instance.decode(overlay);
            	obj.setZoomLevel((Integer)overlay.get("zoom_level"));
            	System.out.println("zoom_level:"+obj.getZoomLevel());
            	System.out.println("type:"+obj.getType());
                visualObjects.add(obj);
            }
		}
		
		iSpace.addVisualObjects(recordid, visualObjects, overlayname, coordinate);
		
		System.out.println("DONE");
	}

	/**
	 * fetches tile from server cache for certain zoom level and then generates
	 * the tile for rest
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void getTile (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException
	{		
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);

		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));

		int frameNumber = Integer.parseInt(Helper.getOptionalParam(Helper.FRAME_NUMBER, request, "0"));
		int sliceNumber = Integer.parseInt(Helper.getOptionalParam(Helper.SLICE_NUMBER, request, "0"));
		int channelCount = Integer.parseInt(Helper.getOptionalParam(Helper.CHANNEL_NUMBERS, request, "1"));

		boolean isGrayScale = Integer.parseInt(Helper.getOptionalParam(Helper.GREY_SCALE, request, "0")) == 1;
		boolean isZStacked = Integer.parseInt(Helper.getOptionalParam(Helper.Z_STACKED, request, "0")) == 1;
		
		int imageWidth = Integer.parseInt(Helper.getOptionalParam(Helper.IMAGEWIDTH, request, "0"));
		int imageHeight = Integer.parseInt(Helper.getOptionalParam(Helper.IMAGEHEIGHT, request, "0"));
		
		int[] channelContrasts = null;
		/*
		 * try { JSONObject contrastSettings = new JSONObject(new
		 * JSONTokener(Helper.getOptionalParam(Helper.CONTRAST_SETTINGS,
		 * request, contrastMap.get(recordid)))); channelContrasts =
		 * getContrastArray(contrastSettings); } catch (JSONException e) {
		 * System.err.println("JSON (http): " + e.getMessage()); }
		 */

		// map tile dimension
		int tileDimension = Integer.parseInt(Helper.getRequiredParam(Helper.TILE_DIM, request));

		// x index of map tile
		int tileX = Integer.parseInt(Helper.getRequiredParam(Helper.TILE_X, request));

		// y index of map tile
		int tileY = Integer.parseInt(Helper.getRequiredParam(Helper.TILE_Y, request));

		// the zoom level of map
		// the zoom obtained here is zoom reverse i.e (maxZoom-zoom)
		int tileZ = Integer.parseInt(Helper.getRequiredParam(Helper.TILE_ZOOM, request));

		//Record r = iSpace.findRecordForGUID(recordid);

		// the scale of image to be fetched wrt to the map tile
		// tileZ is zoom reverse
		int scale = (int) Math.pow(2, tileZ);

		int scaledDimension = scale *tileDimension;
		
		// no of tiles along x direction is original image width divided by map
		// tile width
		int numberOfTilesX = (int) Math.ceil((double) imageWidth / scaledDimension );

		// no of tiles along x direction is original image width divided by map
		// tile width
		int numberOfTilesY = (int) Math.ceil((double) imageHeight / scaledDimension);

		RecordParameters recordParameters = new RecordParameters(frameNumber, sliceNumber, channelCount, isGrayScale, isZStacked, channelContrasts);
		recordParameters.setImageHeight(imageHeight);
		recordParameters.setImageWidth(imageWidth);
		
		TileParameters params = new TileParameters(recordid, tileX, tileY, tileZ, tileDimension, recordParameters);

		BufferedImage tile = null;
		JSONObject json = new JSONObject();

			try
			{
				
				if ((tileX >= 0 && tileY >= 0) && (tileX < numberOfTilesX && tileY < numberOfTilesY))
				{
					tile = TileManager.getInstance().getTile(iSpace, params);
					
					response.setContentType("image/png");
					BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
					try
					{
						ImageIO.write(tile, "png", outputStream);
					}
					catch (Exception e)
					{
						System.out.println("Creation Failed: " + e.getMessage());
					}
					finally
					{
						outputStream.close();
					}
				}
				else
				{
					json.put("status", "empty");
					response.setContentType("application/json");
					PrintWriter out = response.getWriter();
					out.println(json.toString());
					out.close();	
				}

			}
			catch (TileRequestCancelledException e)
			{
				System.out.println("cancelled for " + tileX + " " + tileY + " " + tileZ);
				json.put("status", "cancelled");
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.println(json.toString());
				out.close();
			}
			catch (TileNotReadyException e)
			{
				long x= System.currentTimeMillis();
				json.put("status", "notready");
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				out.println(json.toString());
				out.close();
			}
	}

	/**
	 * aborts fetching of tiles
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void cancelTileFetching(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException
	{
		System.out.println("cancel tile fetching");
		TileManager.getInstance().cancelTileFetching();

	}

	/**
	 * convert JSON formatted channel contrast settings into a continuous array
	 * as acceptable by client API
	 * 
	 * @param json
	 * @return continuous array of contrast settings
	 */
	private int[] getContrastArray(JSONObject json)
	{
		int n = json.length();
		int[] contrastArray = new int[n * 2];
		for (int i = 0; i < n; i++)
		{
			try
			{
				JSONArray minmax = (JSONArray) json.get(i + "");
				contrastArray[2 * i] = (Integer) minmax.get(0);
				contrastArray[(2 * i) + 1] = (Integer) minmax.get(1);
			}
			catch (JSONException e)
			{
				System.err.println("JSON (data): " + e.getMessage());
			}
		}
		return contrastArray;
	}

	/**
	 * get record thumbnail via API call
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getThumbnail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);

		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		

		BufferedImage thumbnail = iSpace.getRecordThumbnail(recordid);
		
		response.setContentType("image/jpeg");
		BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		try
		{
			ImageIO.write(thumbnail, "jpg", outputStream);
		}
		catch (Exception e)
		{
			System.err.println("Thumbnail: " + e.getMessage());
		}
		finally
		{
			outputStream.close();
		}
	}

	/**
	 * get a certain set of data for a record
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getRecordData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String token = Helper.getRequiredParam(Helper.TOKEN, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;

		ImageSpaceSystem iSpace = null;
		iSpace = ImageSpaceObject.getImageSpace();
		iSpace.setAccessKey(scheme, host, port, token);

		long recordid = Long.parseLong(Helper.getRequiredParam(Helper.RECORD_ID, request));
		Record r = iSpace.findRecordForGUID(recordid);
		int pixelDepth = (int) Math.pow(2, r.getPixelDepth().getBitSize());

		JSONObject contrastSettings = null;
		if (contrastMap.containsKey(recordid))
		{
			try
			{
				contrastSettings = new JSONObject(new JSONTokener(contrastMap.get(recordid)));
			}
			catch (JSONException e)
			{
				System.err.println("JSON (data): " + e.getMessage());
			}
		}
		else
		{
			contrastSettings = getDefaultContrastSettings(iSpace, recordid);
			contrastMap.put(recordid, contrastSettings.toString());
		}

		JSONObject json = new JSONObject();
		try
		{
			json.put("Record ID", recordid);
			json.put("Image Height", r.getImageHeight());
			json.put("Image Width", r.getImageWidth());
			json.put("Slice Count", r.getSliceCount());
			json.put("Frame Count", r.getFrameCount());
			json.put("Channel Count", r.getChannelCount());
			json.put("Contrast", contrastSettings);
			json.put("Pixel Depth", pixelDepth);
		}
		catch (JSONException e)
		{
			System.err.println("JSON (data): " + e.getMessage());
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(json.toString());
		out.close();
	}

	/**
	 * log in to server using auth-code and client API
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ImageSpaceSystem iSpace = ImageSpaceObject.getImageSpace();
		String token = null;
		boolean login = false;
		JSONObject json = new JSONObject();

		String authCode = Helper.getRequiredParam(Helper.AUTH_CODE, request);
		String host = Helper.getRequiredParam(Helper.HOST, request);
		int port = Integer.parseInt(Helper.getOptionalParam(Helper.PORT, request, "80"));
		boolean scheme = Helper.getOptionalParam(Helper.SCHEME, request, "http").compareTo("https") == 0;

		try
		{
			for (int i = 0; i < 3 && !login; i++)
			{
				System.out.println("Login try: " + (i + 1));
				
				System.out.println("clientId="+appId+" dir="+storageRoot);
				login = iSpace.login(scheme, host, port, appId, authCode);
				token = iSpace.getAccessKey();
			}
		}
		catch (AuthenticationException e)
		{
			e.printStackTrace();
		}

		try
		{
			json.put("login", login);
			if (login)
			{
				json.put("token", token);
			}
		}
		catch (JSONException e)
		{
			System.err.println("JSON (login): " + e.getMessage());
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(json.toString());
		out.close();
	}

	/**
	 * get default contrast settings for each channel
	 * 
	 * @param iSpace
	 * @param recordid
	 * @return
	 */
	private JSONObject getDefaultContrastSettings(ImageSpaceSystem iSpace, long recordid)
	{
		Record r = iSpace.findRecordForGUID(recordid);

		List<Channel> channels = iSpace.getRecordChannels(recordid);
		int contrastTileWidth = r.getImageWidth() > 4096 ? 4096 : r.getImageWidth();
		int contrastTileHeight = r.getImageHeight() > 4096 ? 4096 : r.getImageHeight();

		JSONObject json = new JSONObject();
		for (int channel = 0; channel < channels.size(); channel++)
		{
			VisualContrast contrast = channels.get(channel).getContrast(false);
			Histogram his = iSpace.getIntensityDistibutionForTile(recordid, new Dimension(0, 0, channel, 0), new Rectangle(0, 0, contrastTileWidth,
					contrastTileHeight));

			int contrastMin = contrast == null ? his.getMin() : contrast.getMinIntensity();
			int contrastMax = contrast == null ? his.getMax() : contrast.getMaxIntensity();

			JSONArray minmax = new JSONArray();
			minmax.put(contrastMin);
			minmax.put(contrastMax);

			try
			{
				json.put(channel + "", minmax);
			}
			catch (JSONException e)
			{
				System.err.println("JSON (data): " + e.getMessage());
			}
		}

		return json;
	}
}
