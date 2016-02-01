package com.strandgenomics.imaging.iviewer;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import com.strandgenomics.imaging.iclient.local.RawRecord;
import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IPixelDataOverlay;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.vo.VisualObject;
import com.strandgenomics.imaging.iviewer.dataobjects.AbstractAnnotationModel;
import com.strandgenomics.imaging.iviewer.dataobjects.CommentsModel;
import com.strandgenomics.imaging.iviewer.image.ImageConsumer;
import com.strandgenomics.imaging.iviewer.image.ImageEvent;
import com.strandgenomics.imaging.iviewer.image.ImageEventListener;
import com.strandgenomics.imaging.iviewer.image.ImagingService;
import com.strandgenomics.imaging.iviewer.va.VAObject;
import com.strandgenomics.imaging.iviewer.va.VAObject.TYPE;
import com.strandgenomics.imaging.iviewer.va.VOConverter;
import com.sun.org.apache.bcel.internal.generic.DMUL;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

public class ImageViewerState implements ImageConsumer {
	
	public static class ChannelScale 
	{
		public static final int COLOR = 0;
		public static final int GRAY = 1;
	}

	public static class SliceState 
	{
		public final static int SINGLE_SLICE = 0;
		public final static int Z_STACK = 1;
	}

	public static class ChannelState {
		public final static int MULTI_CHANNEL = 0;
		public final static int OVERLAY = 1;
	}

	public static class VAState {
		public final static int VA_OFF = 1;
		public final static int VA_ON = 0;
	}

	public static class ZoomSelectState {
		public final static int SELECT_STATE = 0;
		public final static int ZOOM_STATE = 1;
	}
	
	public static class SiteState {
		public final static int MULTI_SITE = 0;
		public final static int SINGLE_SITE = 1;
	}

	public static class MovieState {
		public final static int PLAYING = 0;
		public final static int STOPPED = 1;
	}

	private int channelScale;
	private int channelState;

	private int siteState;
	private int movieState;
	private int sliceState;
	private int zoomState;
	private int vaState = VAState.VA_OFF;

	private int maxSlice;
	private int maxFrame;
	private int maxChannels;
	private int maxSites;

	private int slice = 0;
	private int frame = 0;
	private int site = 0;

	private List<String> recordIds;
	private List<Site> sites;

	private List<IChannel> channels;
	private List<IChannel> selectedChannels;
	
	private String[] originalChannelLUTs;

	private List<String> voNames;
	private List<List<VAObject>> voDisplayed;
	private List<VAObject> voRecord;
	private List<Boolean> voEnabled;
	private Map<String, Boolean> voEnabledByName;
	private Map<String, IVisualOverlay> voByName;
	private String workingOverlayName;
	private HashMap<PNode, Integer> vaClipboard;

	private List<AbstractAnnotationModel> annotationList;
	private Map<String, String> attachmentNotesByName;
	private Map<String, File> attachmentFileByName;
	
	private ImageViewerPanel ivp;

	private List<IRecord> records;

	private VAObject pvo;

	public ImageViewerState() {

		channelState = ChannelState.OVERLAY;
		sliceState = SliceState.SINGLE_SLICE;
		vaState = VAState.VA_OFF;
		siteState = SiteState.SINGLE_SITE;
		movieState = MovieState.STOPPED;

		recordIds = new ArrayList<String>();

		voEnabledByName = new HashMap<String, Boolean>();
		voNames = new ArrayList<String>();
		voEnabled = new ArrayList<Boolean>();
		voRecord = new ArrayList<VAObject>();

		annotationList = new ArrayList<AbstractAnnotationModel>();
		attachmentNotesByName = new HashMap<String, String>();
		attachmentFileByName = new HashMap<String, File>();

		channels = new ArrayList<IChannel>();
		selectedChannels = new ArrayList<IChannel>();
		sites = new ArrayList<Site>();
	}

	/***********************************************************************/
	/** UPDATE RECORDS ON ANY CHANGE OF STATE OR CHANGE OF PARAMETER *****/
	/*********************************************************************/
	
	private void init(){
		maxFrame = 0;
		maxSlice = 0;
		maxChannels = 0;
		maxSites = 0;

		frame = 0;
		slice = 0;
		site = 0;

		channelState = ChannelState.OVERLAY;
		sliceState = SliceState.SINGLE_SLICE;
		vaState = VAState.VA_OFF;
		siteState = SiteState.SINGLE_SITE;
		movieState = MovieState.STOPPED;

		recordIds = new ArrayList<String>();

		voEnabledByName = new HashMap<String, Boolean>();
		voNames = new ArrayList<String>();
		voEnabled = new ArrayList<Boolean>();
		voRecord = new ArrayList<VAObject>();

		annotationList = new ArrayList<AbstractAnnotationModel>();
		
		attachmentNotesByName = new HashMap<String, String>();
		attachmentFileByName = new HashMap<String, File>();

		channels = new ArrayList<IChannel>();
		selectedChannels = new ArrayList<IChannel>();
		sites = new ArrayList<Site>();
	}

	private void updateRecordData() {
		IRecord record = records.get(0);
		
		if(records.size()<1||records.get(0)==null){
			init();
			return;
		}

		movieState = MovieState.STOPPED;
		
		maxFrame = record.getFrameCount();
		maxSlice = record.getSliceCount();
		maxChannels = record.getChannelCount();
		maxSites = record.getSiteCount();

		frame = 0;
		slice = 0;
		site = 0;

		resetOverlays();
		resetChannels();
		resetSites();
		resetUserComments();
		resetAttachments();
	}

	private void resetUserComments() {
		this.annotationList.clear();
		
		Map<String, Object> userAnnotations = records.get(0)
				.getUserAnnotations();
		String userComment = (String) userAnnotations
				.get(CommentsPanel.COMMENTS_KEY);
		CommentsModel comment = new CommentsModel(CommentsPanel.COMMENTS_KEY,
				userComment, "DEFAULT_USER");
		this.annotationList.add(comment);
	}

	private void resetAttachments() {
		attachmentFileByName.clear();
		attachmentNotesByName.clear();
		
		Collection<IAttachment> attachments = records.get(0).getAttachments();

		Iterator<IAttachment> it = attachments.iterator();
		while (it.hasNext()) {
			IAttachment temp = it.next();
			String name = temp.getName();
			attachmentFileByName.put(name, temp.getFile());
			attachmentNotesByName.put(name, temp.getNotes());
		}
	}

	private void resetChannels() 
	{
		IRecord record = records.get(0);
		this.channels = new ArrayList<IChannel>();
		
		for(int i = 0;i < record.getChannelCount(); i++)
			this.channels.add( record.getChannel(i) );
		
//		originalChannelColors = new int[this.channels.size()];
//		for(int i=0;i<channels.size();i++){
//			originalChannelColors[i] = channels.get(i).getColor();
//		}
		
		originalChannelLUTs = new String[this.channels.size()];
		for(int i=0;i<channels.size();i++){
			originalChannelLUTs[i] = channels.get(i).getLut();
		}
		
		this.selectedChannels = this.channels;
	}
	
	private void resetSites() 
	{
		IRecord record = records.get(0);
		this.sites = new ArrayList<Site>();
		
		for(int i = 0;i < record.getSiteCount(); i++)
		{
			this.sites.add( record.getSite(i) );
		}
	}

	private void resetOverlays() 
	{
		voRecord.clear();
		
		for(int currFrame = 0; currFrame < maxFrame; currFrame++ )
		{
			for (int currSlice = 0; currSlice < maxSlice; currSlice++ ) 
			{
				VODimension vod = new VODimension(currFrame, currSlice, site);
				
				if(records.get(0).getVisualOverlays(vod) == null)
					continue;
				
				for(IVisualOverlay vo : records.get(0).getVisualOverlays(vod))
				{
					pvo = VOConverter.convertToVAObject(vo, ivp.imageCanvas.getZoomMultiplier());
					voRecord.add(pvo);
				}
			}
		}

		voByName = getOverlayNames();
		voNames = new ArrayList<String>(voByName.keySet());
		voNames = getSorted(voNames);
		voEnabled = new ArrayList<Boolean>();

		for (int i = 0; i < voNames.size(); i++) {
			String lowerCaseName = voNames.get(i).toLowerCase();
			Boolean enabled = false;
			if (voEnabledByName.containsKey(lowerCaseName + getRecordId())) {
				enabled = voEnabledByName.get(lowerCaseName + getRecordId());
			}
			voEnabled.add(i, enabled);
		}
	}

	private String getRecordId() {
		return records.get(0).getSignature().toString();
	}

	private Map<String, IVisualOverlay> getOverlayNames() {
		Map<String, IVisualOverlay> vos = null;

		IRecord record = records.get(0);
		vos = getOverlayNames(record);

		return vos;
	}

	private Map<String, IVisualOverlay> getOverlayNames(IRecord record) 
	{
		Map<String, IVisualOverlay> names = new HashMap<String, IVisualOverlay>();
		
		VODimension vod = new VODimension(frame, slice, site);//doesnt matter which frame and slice: names will be consistent

		Collection<IVisualOverlay> overlays = record.getVisualOverlays(vod);
		if (overlays == null)
			return names;

		for(IVisualOverlay vo : overlays)
		{
			String name = vo.getName();
			names.put(name, vo);
		}
		return names;
	}

	private long requestID = 1;

	/**
	 * UPDATE IMAGE ON ANY CHANGE OF STATE OR CHANGE OF PARAMETER
	 * 
	 * @return an instance of piccolo PLayer, a node that can be viewed directly
	 *         by multiple camera nodes
	 */
	public void updateImage()
	{
		if (records == null || records.size()<=0 || records.get(0)==null)
			return;

		System.out.println("sending request to generate image");
		requestID++;

		IRecord record = records.get(0);
		updateOverlays();
		
		List<VAObject> vos = voDisplayed.get(0);
		boolean useChannelColor = channelScale == ChannelScale.COLOR;

		ImagingService.getInstance().addRequest(record, frame, slice, site, getSelectedChannelDimensions(),
				vos, useChannelColor, channelState, sliceState, this);
	}

	@Override
	public long getID() 
	{
		return 1L;
	}

	public long getCurrentRequestID() 
	{
		return requestID;
	}

	/**
	 * called made by the image producer potentially in a non-ui thread
	 */
	public void consumeImage(final ImageEvent image)
	{
		if (image.getPiccoloLayer() == null)
			return;

		// fire the event in the ui thread
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() {
				fireImageReadyEvent(image);
			}
		});
	}

	protected EventListenerList listenerList = new EventListenerList();
	private int siteNumber;

	public void addImageEventListener(ImageEventListener listener) {
		listenerList.add(ImageEventListener.class, listener);
	}

	public void removeImageEventListener(ImageEventListener listener) {
		listenerList.remove(ImageEventListener.class, listener);
	}

	public void fireImageReadyEvent(ImageEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i + 2) {
			if (listeners[i] == ImageEventListener.class) {
				((ImageEventListener) listeners[i + 1]).imageIsReady(evt);
			}
		}
	}

	private PLayer createPLayer() {
		PLayer layer = new PLayer();
//
//		int xStart = 10;
//		int yStart = 10;
//
//		IRecord record = records.get(0);
//		BufferedImage bimage = null;
//
//		try {
//			Set<Dimension> imageDimensions = updateDimensions();
//			// expensive operation - do it in a separate thread
//			bimage = record.getOverlayedPixelData(imageDimensions).getImage(
//					channelScale == IChannel.Scale.COLOR);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		PImage image = new PImage(bimage);
//		image.setX(xStart);
//		image.setY(yStart);
//
//		List<VAObject> vos = voDisplayed.get(0);
//		for (VAObject overlay : vos) {
//			image.addChild(overlay);
//			overlay.setOffset(xStart, yStart);
//		}
//
//		PLayer imagesLayer = new PLayer();
//		imagesLayer.addChild(image);
//		layer.addChild(imagesLayer);
//
		return layer;
	}

	private Set<Integer> getSelectedChannelDimensions() 
	{
		Set<Integer> channelDimensions = new HashSet<Integer>();
		for (int i = 0; i < channels.size(); i++)
		{
			if (selectedChannels.contains(channels.get(i)))
			{
				channelDimensions.add(i);
			}
		}
		return channelDimensions;
	}

	/*******************************************************************/
	/**** UPDATE OVERLAY ON ANY CHANGE OF STATE OR CHANGE OF PARAMETER */
	/*******************************************************************/
	private void updateOverlays() {

		List<List<VAObject>> displayedOverlays = new ArrayList<List<VAObject>>();
		displayedOverlays.add(updateOverlayForChannel(records.get(0)));

		this.voDisplayed = displayedOverlays;
	}

	private List<VAObject> updateOverlayForChannel(IRecord record) {
		List<VAObject> overlay = new ArrayList<VAObject>();

		for (int i = 0; i < voNames.size(); i++) {
			if (voEnabled.get(i)) {
				for (VAObject vo : voRecord) {
					if (vo != null && vo.getFrame() == frame
							&& vo.getSlice() == slice
							&& vo.getSite() == site
							&& vo.getName().equals(voNames.get(i))) {
						vo.repaint();
						overlay.add(vo);
					}
				}
			}
		}
		return overlay;
	}

	public void addOverlay(String overlayName) {
		List<Dimension> imageDimensions = new ArrayList<Dimension>();
		for (int i = 0; i < maxFrame; i++) {
			for (int j = 0; j < maxSlice; j++) {
				VAObject vo = new VAObject();
				vo.setFrame(i);
				vo.setSlice(j);
				vo.setSite(site);
				vo.setName(overlayName);
				
				voRecord.add(vo);
			}
		}

		List<String> enabledNames = new ArrayList<String>();
		for (int i = 0; i < voNames.size(); i++) {
			if (voEnabled.get(i) == true)
				enabledNames.add(voNames.get(i));
		}

		voNames.add(overlayName);
		voNames = getSorted(voNames);

		voEnabled = new ArrayList<Boolean>();
		for (int i = 0; i < voNames.size(); i++) {
			String _temp = voNames.get(i);
			if (_temp.equals(overlayName) || enabledNames.contains(_temp))
				voEnabled.add(i, true);
			else
				voEnabled.add(i, false);
		}
		updateOverlays();
		updateImage();
	}

	public void saveOverlays() 
	{
		IRecord record = records.get(0);
		
		Map<String, IVisualOverlay> names = getOverlayNames(record);
		
		for (VAObject v : voRecord) 
		{
			int currframe = v.getFrame();
			int currslice = v.getSlice();
			int currsite = v.getSite();
			String name = v.getName();
			
			HashMap<PNode, TYPE> typeMap = v.getTypeMap();
			List<VisualObject> vObjects = new ArrayList<VisualObject>();
			
			for (int i = 0; i < v.getChildrenCount(); i++) 
			{
				PNode node = v.getChild(i);
				TYPE type = typeMap.get(node);
				VisualObject shape = VOConverter.convertPiccoloToShape(node,type, ivp.imageCanvas.getZoomMultiplier());
				vObjects.add(shape);
			}

			VODimension vod = new VODimension(currframe, currslice, currsite);
			if (names.containsKey(name)) 
			{
				//update the existing overlay
				Collection<VisualObject> oldobjects = record.getVisualOverlay(vod, name).getVisualObjects();
				List<VisualObject> oldobj = new ArrayList<VisualObject>();
				for(VisualObject obj :oldobjects)
				{
					oldobj.add(obj);
				}
				record.deleteVisualObjects(oldobj, name, vod);
				record.addVisualObjects(vObjects, name, vod);
			} 
			else 
			{
				//overlay created first time
				record.createVisualOverlays(vod.siteNo, name);
				record.addVisualObjects(vObjects, name, vod);
				names.put(name, record.getVisualOverlay(vod, name));
			}
		}
	}

	public void deleteOverlay(String overlayName)
	{
		IRecord record = records.get(0);

		int nameId = -1;
		for (int i = 0; i < voNames.size(); i++) {
			if (voNames.get(i).equals(overlayName)) {
				nameId = i;
			}
		}

		if (nameId == -1) {
			JOptionPane.showMessageDialog(null,
					"Overlay with given name doesn't exist", "Error",
					JOptionPane.ERROR_MESSAGE);
			updateImage();
			return;
		}

		record.deleteVisualOverlays(site, overlayName);

		voNames.remove(nameId);
		voEnabled.remove(nameId);

		updateImage();
	}
	
	public List<VODimension> searchOverlay(String overlayname)
	{
		List<VODimension> locations = new ArrayList<VODimension>();
		
		if(voRecord == null || voRecord.isEmpty() || overlayname == null)
			return locations;
		
		for(VAObject vo:voRecord)
		{
			if(vo.getName().equals(overlayname))
			{
				if(vo.getChildrenCount()>0)
				{
					VODimension dim = new VODimension(vo.getFrame(), vo.getSlice(), vo.getSite());
					locations.add(dim);
				}
			}
		}
		
		return locations;
	}

	private List<String> getSorted(List<String> names) {
		String[] ovSortedList = new String[names.size()];
		ovSortedList = names.toArray(ovSortedList);
		Arrays.sort(ovSortedList);

		names = new ArrayList<String>();
		for (int i = 0; i < ovSortedList.length; i++) {
			names.add(ovSortedList[i]);
		}
		return names;
	}

	/************************/
	/**** GETTER SETTERS ***/
	/***********************/

	public void setRecords(List<IRecord> records) {
		this.records = records;
		updateRecordData();

		updateImage();
	}
	public void VideoThread(final ImageViewerApplet imageViewerApplet, final String file,final int option,final double fps,final String notes, final boolean frame) {
		 Runnable r = new Runnable() {
	         public void run() {
	             createVideo(imageViewerApplet,file,option,fps,notes,frame);
	         }
	     };

	     new Thread(r).start();
	}
	public synchronized void createVideo(ImageViewerApplet imageViewerApplet, String file,int option,double fps,String notes, boolean isFrame) {

		IRecord record = records.get(0);
		List<VideoFrame> l = new ArrayList<VideoFrame>();
		int n = isFrame ? record.getFrameCount() : record.getSliceCount();
		BufferedImage bimage = null;
		int frameNumber = frame,sliceNumber = slice;
		if(isFrame){
			frameNumber = 0;
		}
		else{
			sliceNumber = 0;
		}
		for(int i =0;i < n; i++){
			try
			{
				//IPixelDataOverlay overlay = record.getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos);
				
				Set<Integer> channels = getSelectedChannelDimensions();
				int j = 0;
				int[] channelNos = new int[channels.size()];
				boolean useChannelColor =  channelScale == ChannelScale.COLOR;
				for(Integer channelNo : channels)
				{
					channelNos[j++] = channelNo;
				}
				
				if(isFrame){
					frameNumber = i;
				}
				else{
					sliceNumber = i;
				}
				IPixelDataOverlay overlay = record.getOverlayedPixelData(sliceNumber, frameNumber, site, channelNos);
				double elapsedtime = record.getPixelData(new Dimension(frameNumber, sliceNumber, channelNos[0], site)).getElapsedTime();
				System.out.println("elapsed time: "+  elapsedtime);
				boolean mosaic = channelState != ImageViewerState.ChannelState.OVERLAY;
				boolean zStack = sliceState == ImageViewerState.SliceState.Z_STACK;

				bimage = overlay.getImage(zStack, mosaic, useChannelColor, null);
				VideoFrame vf = new VideoFrame(bimage, elapsedtime);
				l.add(vf);
			} 
			catch(Exception e) 
			{
				e.printStackTrace(); //TODO
				return;
			}
			
		}
		File videoFile = null;
		Iterator<VideoFrame> it = l.iterator();
		try {
			if(option == 1)
				videoFile = createMovie(it, record.getImageWidth(), record.getImageHeight(), fps,file);
			else if(option == 2)
				videoFile = createMovie(it, record.getImageWidth(), record.getImageHeight(), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Movie Creation Done in -------------------- ns");
		try {
			Desktop.getDesktop().open(videoFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ret = JOptionPane.showConfirmDialog(null, "Do you want to attach this movie to the record?", "Success", JOptionPane.YES_NO_OPTION);
		if (ret == JOptionPane.YES_OPTION) {
			imageViewerApplet.addUserAttachment(videoFile, notes);
		}
	}
	public static File createMovie(Iterator<VideoFrame> inputFrames, int imageWidth, int imageHeight, String file) throws IOException
	{
		File outputFile = new File(file); 
		IMediaWriter writer = ToolFactory.makeWriter(outputFile.getAbsolutePath());
		
		int ret = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, imageWidth, imageHeight);
		if(ret < 0)// failed to create
			return null;
		
		long startTime = System.nanoTime();
		
		int index = 0;
		while(inputFrames.hasNext())
		{
			System.out.println("Fetching "+index+"ith image");
			VideoFrame frame = inputFrames.next();
			
			BufferedImage image = frame.getImage();
			image = convertToType(image, BufferedImage.TYPE_3BYTE_BGR);
			
			// from frame rate compute the timestamp of the current frame inside movie
//			 double timeStamp =  frame.getElapsedTime();
//			 long temp = (long) (timeStamp*1000*1000);
			long temp = (long) frame.getElapsedTime();
			// encode the image to stream #0
			writer.encodeVideo(0, image, temp, TimeUnit.SECONDS);
			
			index++;
			
		}

		// tell the writer to close and write the trailer if needed
		writer.close();
		
		System.out.println("Movie Creation Done in "+(System.nanoTime()-startTime)+" ns");

		return outputFile;
	}
	public  static File createMovie(Iterator<VideoFrame> inputImages, int imageWidth, int imageHeight, double frameRate, String file) throws IOException
	{
		File outputFile = new File(file); 
		IMediaWriter writer = ToolFactory.makeWriter(outputFile.getAbsolutePath());
		int ret = writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, imageWidth, imageHeight);
		if(ret < 0)// failed to create
			return null;
		
		long startTime = System.nanoTime();
		
		int index = 0;
		while(inputImages.hasNext())
		{
			System.out.println("Fetching "+index+"ith image");
			BufferedImage image = inputImages.next().getImage();
			
			image = convertToType(image, BufferedImage.TYPE_3BYTE_BGR);
			double temp = 1000/frameRate;
			// from frame rate compute the timestamp of the current frame inside movie
			long timeStamp = (long) (index * temp);
			// encode the image to stream #0
			writer.encodeVideo(0, image, timeStamp, TimeUnit.MILLISECONDS);
			
			index++;
		}

		// tell the writer to close and write the trailer if needed
		writer.close();
		
		System.out.println("Movie Creation Done in "+(System.nanoTime()-startTime)+" ns");
		
		return outputFile;
	}
	private static BufferedImage convertToType(BufferedImage sourceImage, int targetType)
	{
		BufferedImage image;
		// if the source image is already the target type, return the source
		// image
		if (sourceImage.getType() == targetType)
		{
			image = sourceImage;
		}
		// otherwise create a new image of the target type and draw the new
		// image
		else
		{
			image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	public List<String> getRecordIds() {
		return this.recordIds;
	}

	public void setChannelScale(int channelScale) {
		this.channelScale = channelScale;
		updateImage();
	}

	public int getChannelScale() {
		return this.channelScale;
	}

	public void setChannelState(int channelState) {
		this.channelState = channelState;
		updateImage();
	}

	public int getChannelState() {
		return this.channelState;
	}

	public void setMovieState(int movieState) {
		this.movieState = movieState;
	}

	public int getMovieState() {
		return this.movieState;
	}

	public void setSliceState(int sliceState) {
		this.sliceState = sliceState;
		updateImage();
	}

	public int getSliceState() {
		return this.sliceState;
	}

	public List<String> getOverlayNamesForAllSites() {
		// List<String> voNamesAllSites;
		// HashSet<String> overlayNames = new HashSet<String>();
		// for (IRecord record : records) {
		// Set<String> _overlayNames = record.getOverlayNames();
		// overlayNames.addAll(_overlayNames);
		// }
		// voNamesAllSites = new ArrayList<String>(overlayNames);
		// return voNamesAllSites;
		return voNames;
	}

	public void setZoomState(int zoomState) {
		this.zoomState = zoomState;
	}

	public int getZoomState() {
		return this.zoomState;
	}

	public void setMaxSlice(int maxSlice) {
		this.maxSlice = maxSlice;
	}

	public int getMaxSlice() {
		return this.maxSlice;
	}

	public void setMaxFrame(int maxFrame) {
		this.maxFrame = maxFrame;
	}

	public int getMaxFrame() {
		return maxFrame;
	}

	public void setFrame(int frame) {
		if (this.frame != frame) {
			this.frame = frame;
			updateImage();
		}
	}

	public int getSiteState() {
		return this.siteState;
	}

	public int getFrame() {
		return frame;
	}

	public void setSlice(int slice) {
		if (this.slice != slice) {
			this.slice = slice;
			updateImage();
		}
	}

	public int getSlice() {
		return this.slice;
	}

	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		if (this.site != site) {
			this.site = site;
			resetOverlays();
			updateImage();
		}
	}

	public void setChannels(List<IChannel> channels) 
	{
		this.channels = channels;
		setSelectedChannels(channels);// by default all channels are selected
	}

	public List<IChannel> getChannels() 
	{
		return channels;
	}

	public void setSelectedChannels(List<IChannel> selectedChannels) 
	{
		if (!this.selectedChannels.equals(selectedChannels)) 
		{
			this.selectedChannels = selectedChannels;
			updateImage();
		}
	}

	public List<IChannel> getSelectedChannels() 
	{
		return this.selectedChannels;
	}
	
	public String[] getOriginalChannelLUTs(){
		return originalChannelLUTs;
	}

	public List<String> getOverlaysNames() {
		return this.voNames;
	}

	public List<List<VAObject>> getOverlays() {
		return this.voDisplayed;
	}

	public List<Boolean> getOverlaysEnabled() {
		return this.voEnabled;
	}

	public int getVAState() {
		return this.vaState;
	}

	public void setVAState(int vaState) {
		this.vaState = vaState;
	}

	public void setOverlaysEnabled(List<Boolean> voEnabled) {
		for (int i = 0; i < voEnabled.size(); i++) {
			Boolean b = voEnabled.get(i);
			voEnabledByName.put(voNames.get(i) + getRecordId(), b);
			this.voEnabled.set(i, b);
		}
		updateImage();
	}

	public String getWorkingOverlayName() {
		return this.workingOverlayName;
	}

	public void setWorkingOverlayName(String ovName) {
		this.workingOverlayName = ovName;
	}

	public int getActiveOverlayIndex(String ovName) {
		int index = -1;
		List<Boolean> voE = getOverlaysEnabled();
		List<String> overlayNames = getOverlaysNames();
		for (int i = 0; i < voE.size(); i++) {
			if (voE.get(i) == true)
				index++;
			if (overlayNames.get(i).equals(ovName))
				break;
		}
		return index;
	}

	public List<IRecord> getRecords() {
		return records;
	}

	public HashMap<PNode, Integer> getClipboardObject() {
		return this.vaClipboard;
	}

	public void setClipboardObject(HashMap<PNode, Integer> clipboard) {
		this.vaClipboard = clipboard;
	}

	public List<VAObject> getOverlaysForRecord() {
		return voRecord;
	}

	public void setUserNotes(String notes) {
		IRecord record = records.get(0);
		if(record.getUploadTime()!=null){
			JOptionPane.showMessageDialog(null, "Cant Modify Read-only Record", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		record.addUserComments(notes);
	}

	public String getUserNotes() {
		IRecord record = records.get(0);
		if(record==null)
			return "";
		return ((RawRecord)record).getComments();
	}

	public void addUserAttachment(File selectedFile, String notes) {
		try {
			IRecord record = records.get(0);
			if(record.getUploadTime()!=null){
				JOptionPane.showMessageDialog(null, "Cant Modify Read-only Record", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			record.addAttachment(selectedFile, notes);
			attachmentFileByName.put(selectedFile.getAbsolutePath() + "#"
					+ selectedFile.getName(), selectedFile);
			attachmentNotesByName.put(selectedFile.getAbsolutePath() + "#"
					+ selectedFile.getName(), notes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteUserAttachment(String absoluteName) {
		IRecord record = records.get(0);
		if(record.getUploadTime()!=null){
			JOptionPane.showMessageDialog(null, "Cant Modify Read-only Record", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		record.removeAttachment(absoluteName);
		attachmentFileByName.remove(absoluteName);
		attachmentNotesByName.remove(absoluteName);
	}
	
	public Map<String, Object> getAnnotationList(){
		IRecord record = records.get(0);
		if(record==null)
			return null;
		return ((RawRecord)record).getUserAnnotations();
	}

	public Map<String, File> getAttachments() {
		return attachmentFileByName;
	}
	
	

	public Map<String, String> getAttachmentNotes() {
		return attachmentNotesByName;
	}

	public List<Site> getSites() {
		// TODO Auto-generated method stub
		return sites;
	}

	public void setImagePanel(ImageViewerPanel ivp) {
		this.ivp = ivp;		
	}
}