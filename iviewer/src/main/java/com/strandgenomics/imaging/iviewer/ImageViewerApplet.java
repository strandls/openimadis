package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.ithreedview.Launcher;
import com.strandgenomics.imaging.iviewer.ImageViewerState.VAState;
import com.strandgenomics.imaging.iviewer.image.ImageEvent;
import com.strandgenomics.imaging.iviewer.image.ImageEventListener;
import com.strandgenomics.imaging.iviewer.va.VAObject;
import com.strandgenomics.imaging.iviewer.va.VisualAnnotationControlPanel;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

@SuppressWarnings("serial")
public class ImageViewerApplet extends JPanel {

	private ImagePropertiesControlPanel toolbarPanel;
	private FrameSliceControlPanel fspanel;
	private ImageViewerPanel ivp;
	private ChannelSiteControlPanel cspanel;
	private VisualAnnotationControlPanel vaPanel;
	private RightTabbedPanel rightPanel;
	private ImageMetaDataPanel imageMetadataPanel;
	private RecordDynamicDataPanel recordMetadataPanel;
	private CommentsPanel commentsPanel;
	private UserAttachmentPanel attachmentPanel;
	
	private ImageViewerState imageViewerState;

	private boolean init_done = false;
	protected MovieWorker movieThread;

	public ImageViewerApplet()
	{
		init();
	}

	public void init() 
	{
		if(!init_done)
		{
			init_done = true;
			
			setSize(700, 700);
			setLayout(new BorderLayout());

			imageViewerState = new ImageViewerState();
			createGUI();
			
			imageViewerState.addImageEventListener(new ImageEventListener() 
			{
			      public void imageIsReady(ImageEvent evt) 
			      {
			    	  ivp.setImages(evt.getPiccoloLayer());
			    	  validate();
			      }
			});

			validate();
		}
	}
	
	private void createGUI()
	{
		ivp = new ImageViewerPanel();
		fspanel = new FrameSliceControlPanel(this);
		toolbarPanel = new ImagePropertiesControlPanel(ivp, this);
		cspanel = new ChannelSiteControlPanel(this);
		imageMetadataPanel = new ImageMetaDataPanel(this);
		vaPanel = new VisualAnnotationControlPanel(this);
		recordMetadataPanel = new RecordDynamicDataPanel(this);
		commentsPanel = new CommentsPanel(this);
		attachmentPanel = new UserAttachmentPanel(this);
		
		imageViewerState.setImagePanel(ivp);
		
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new BorderLayout());		
		leftPane.setPreferredSize(new Dimension(450, 10));
		leftPane.add(ivp, BorderLayout.CENTER);
		leftPane.add(fspanel, BorderLayout.SOUTH);
		
		rightPanel = new RightTabbedPanel(this, cspanel,
				imageMetadataPanel, recordMetadataPanel, commentsPanel, attachmentPanel);
		
		JPanel rightPane = new JPanel(new BorderLayout());
		rightPane.add(rightPanel, BorderLayout.CENTER);
		leftPane.add(toolbarPanel, BorderLayout.PAGE_START);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane,
				rightPane);
		splitPane.setResizeWeight(0.5);
		
//		add(toolbarPanel, BorderLayout.PAGE_START);
		add(splitPane, BorderLayout.CENTER);
	}

	public void setFitWidth() {
		ivp.imageCanvas.setFitWidth();
	}
	
	public void setRecord(IRecord record)
	{
		//XXX: NG: Handle null .. should set a blank image like the starting state ..  
		if(record == null)
			ivp.setImages(new PLayer());
		// If same record is set again .. don't update
		if(record !=null && imageViewerState.getRecords()!=null && imageViewerState.getRecords().size()>0){
			if(record.equals(imageViewerState.getRecords().get(0)))
				return;
		}
		
		if(getVAState() == VAState.VA_ON)
		{
			actionCloseVAToolBar();
		}
		
		if(commentsPanel.isUnsaved()){
			int n = JOptionPane.showConfirmDialog(null, "Do you want to save unsaved notes?",
					"Save", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				commentsPanel.saveNotes();
			} else if (n == 1) {
				commentsPanel.setNotesStatus(false);
			}
		}
		
		ArrayList<IRecord> records = new ArrayList<IRecord>();
		records.add(record);
		
		ivp.addLoadingNode();
		if(record == null)
			ivp.setImages(new PLayer());
		imageViewerState.setRecords(records);
		toggleMovie(imageViewerState.getMovieState(), true);
		//ivp.setImages(pLayer);
		
		fspanel.updateSliders();
		cspanel.updatePanel();
		imageMetadataPanel.updatePanel();
		recordMetadataPanel.updatePanel();
		commentsPanel.updatePanel();
		attachmentPanel.updatePanel();
		toolbarPanel.updatePanel();

		setFitWidth();
	}

	public void setParam(String text) {
//		try {
//			Image img = getImage(getCodeBase(), "q.JPG");
//			ivp.setImage(img);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void setChannelScale(int channelScale) 
	{
		imageViewerState.setChannelScale(channelScale);
		ivp.addLoadingNode();
	}

	public void setChannelState(int channelState) 
	{
		imageViewerState.setChannelState(channelState);
		cspanel.update();
		ivp.addLoadingNode();
	}

	public void setSliceState(int sliceState) 
	{
		imageViewerState.setSliceState(sliceState);
		ivp.addLoadingNode();
		fspanel.setSliceSliderEnabled(imageViewerState.getSliceState() == ImageViewerState.SliceState.SINGLE_SLICE);
	}
	
	public void setFrame(int newFrame) 
	{
		imageViewerState.setFrame(newFrame);
		ivp.addLoadingNode();
		
		fspanel.updateSliders();
		vaPanel.update();
		imageMetadataPanel.updatePanel();
	}

	public void setSlice(int newSlice) 
	{
		imageViewerState.setSlice(newSlice);
		ivp.addLoadingNode();
		
		fspanel.updateSliders();
		vaPanel.update();
		imageMetadataPanel.updatePanel();
	}
	
	public void refresh(){
		imageViewerState.updateImage();
		ivp.addLoadingNode();
	}

	public void setChannels(List<IChannel> channels) 
	{
		imageViewerState.setSelectedChannels(channels);
		ivp.addLoadingNode();
	}

	public void launch3D() 
	{
		Launcher l = Launcher.getInstance();
		IRecord record = imageViewerState.getRecords().get(0);
		
		List<IChannel> channels = new ArrayList<IChannel>();
		for(int i = 0;i < record.getChannelCount(); i++)
			channels.add( record.getChannel(i) );
		
		l.setValue(record, imageViewerState.getSite(), imageViewerState.getFrame(), 1.0f, channels);
	}

	public int getMaxFrame() {
		return imageViewerState.getMaxFrame();
	}

	public int getMaxSlice() {
		return imageViewerState.getMaxSlice();
	}

	public int getSliceState() {
		return imageViewerState.getSliceState();
	}

	public int getSlice() {
		return imageViewerState.getSlice();
	}
	
	public int getFrame() {
		return imageViewerState.getFrame();
	}

	public int getChannelState() {
		return imageViewerState.getChannelState();
	}
	
	public String[] getOriginalChannelLUTs(){
		return imageViewerState.getOriginalChannelLUTs();
	}
	
	public int getChannelScale() {
		return imageViewerState.getChannelScale();
	}

	public int getMovieState() {
		return imageViewerState.getMovieState();
	}
	
	public int getVAState() {
		return imageViewerState.getVAState();
	}

	public List<IChannel> getChannels() 
	{
		return imageViewerState.getChannels();
	}
	
	public void addOverlay(String overlayName)
	{
		imageViewerState.addOverlay(overlayName);
		//ivp.setImages();
		cspanel.update();
	}
	
	public void deleteOverlay(String overlayName)
	{
		imageViewerState.deleteOverlay(overlayName);
		//ivp.setImages();
		cspanel.update();
	}

	public List<String> getOverlayNames() {
		return imageViewerState.getOverlaysNames();
	}
	
	public List<Boolean> getOverlaysEnabled(){
		return imageViewerState.getOverlaysEnabled();
	}

	public void setOverlaysEnabled(List<Boolean> b) 
	{
		imageViewerState.setOverlaysEnabled(b);
		//ivp.setImages();
	}

	public double getZoomMultiplier() {
		return ivp.imageCanvas.getZoomMultiplier();
	}

	public void saveOverlays() {
		imageViewerState.saveOverlays();
	}

	public List<String> getOverlaysNames() {
		return imageViewerState.getOverlaysNames();
	}

	public Integer getZoomState() {
		return imageViewerState.getZoomState();
	}

	public List<String> getOverlayNamesForAllSites() {
		return imageViewerState.getOverlayNamesForAllSites();
	}

	public List<List<VAObject>> getOverlays() {
		return imageViewerState.getOverlays();
	}

	public void setWorkingOverlayName(String workingOverlayName) {
		imageViewerState.setWorkingOverlayName(workingOverlayName);
	}

	public List<String> getRecordIds() {
		return imageViewerState.getRecordIds();
	}

	public void setVAState(int vaState) {
		imageViewerState.setVAState(vaState);
	}
	
	public ImageCanvas getImagePanel(){
		return ivp.imageCanvas;
	}

	public void createOverlay() {
		if(getVAState() == ImageViewerState.VAState.VA_ON)
			actionCloseVAToolBar();
		
		if (!actionOpenVAToolBar())
			return;
		vaPanel.getToolbar().setVisible(false);
		
		String name = vaPanel.getVaCanvas().openOverlay();
		if (!name.isEmpty() && name != "") {
			actionEditOverlay(name);
		} else {
			actionCloseVAToolBar();
		}
	}
	
	public void actionEditOverlay(String ovName){
		if(getVAState() == ImageViewerState.VAState.VA_ON)
			actionCloseVAToolBar();
		
		if (!actionOpenVAToolBar())
			return;
		
		vaPanel.getToolbar().setVisible(false);
		
		List<String> overlayNames = getOverlaysNames();
		List<Boolean> voE = getOverlaysEnabled();
		
		Boolean[] voEnabled = new Boolean[overlayNames.size()];
		for (int i = 0; i < voEnabled.length; i++) {
			if (overlayNames.get(i).equals(ovName)){
				voEnabled[i] = true;
			}
			else
				voEnabled[i] = voE.get(i);
		}

		List<Boolean> b = Arrays.asList(voEnabled);
		setOverlaysEnabled(b);

		List<List<VAObject>> overlays = getOverlays();

		vaPanel.getToolbar().setVisible(true);
		vaPanel.getVaCanvas().setUnsavedVA(false);
		vaPanel.getVaCanvas().setWorkingOverlay(
				overlays.get(0).get(getActiveOverlayIndex(ovName)));

		vaPanel.getToolbar().setTitle("VA Toolbar "+ ovName);
		vaPanel.getToolbar().enableVAControls(true);
	}

	public int getActiveOverlayIndex(String ovName) {
		return imageViewerState.getActiveOverlayIndex(ovName);
	}

	public boolean actionOpenVAToolBar() {
		IRecord record = imageViewerState.getRecords().get(0);
		if(record.getUploadTime()!=null || (record.getParentProject()!=null && !record.getParentProject().getName().isEmpty())){
			JOptionPane.showMessageDialog(null, "Cant Modify Read-only Record", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		vaPanel.createVAToolBar();
		boolean b = vaPanel.openVAToolBar();
		if(b){
//			holderPanel.setVAToolbar(vaPanel.getToolbar());
//			setZoomSelectState(ZoomSelectState.SELECT_STATE);
//			zoomControlPanel.setZoomState(zoomSelectState);
		}
		return b;
	}

	public void actionCloseVAToolBar() {
		vaPanel.closeVAToolBar();
	}

	public List<IRecord> getRecords() {
		return imageViewerState.getRecords();
	}

	public HashMap<PNode, Integer> getClipboardObject() {
		return imageViewerState.getClipboardObject();
	}
	
	public void setClipboardObject(HashMap<PNode, Integer> clipboard) {
		imageViewerState.setClipboardObject(clipboard);
	}

	public List<IChannel> getSelectedChannels() 
	{
		return imageViewerState.getSelectedChannels();
	}

	public void setSelectedChannels(List<IChannel> selectedChannels)
	{
		ivp.addLoadingNode();
		imageViewerState.setSelectedChannels(selectedChannels);
		imageMetadataPanel.updatePanel();
	}

	public List<VAObject> getOverlaysForRecord() {
		return imageViewerState.getOverlaysForRecord();
	}
	
	public static void main(String args[]){
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ImageViewerApplet());
		frame.setSize(700, 700);
		frame.setVisible(true);
	}

	public void setUserNotes(String notes) {
		imageViewerState.setUserNotes(notes);
		commentsPanel.updatePanel();
	}
	
	public String getUserNotes(){
		return imageViewerState.getUserNotes();
	}

	public void addUserAttachment(File selectedFile, String notes) {
		imageViewerState.addUserAttachment(selectedFile, notes);
		attachmentPanel.updatePanel();
	}
	
	public void deleteUserAttachment(String absoluteName) {
		imageViewerState.deleteUserAttachment(absoluteName);
		attachmentPanel.updatePanel();
	}

	public Map<String, File> getAttachments() {
		return imageViewerState.getAttachments();
	}

	public Map<String, String> getAttachmentNotes() {
		return imageViewerState.getAttachmentNotes();
	}

	public void setMovieState(int movieState) {
		imageViewerState.setMovieState(movieState);
		toggleMovie(movieState, true);
	}
	
	public void setMovieState(int movieState, boolean movieOnFrame) {
		imageViewerState.setMovieState(movieState);
		toggleMovie(movieState, movieOnFrame);
	}

	protected void createMovieThread(boolean movieOnFrame) {
		movieThread = new MovieWorker(this, movieOnFrame);
	}
	
	private void toggleMovie(final int movieState, final boolean movieOnFrame){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(movieThread == null)
					createMovieThread(movieOnFrame);
				if(movieState == ImageViewerState.MovieState.PLAYING)
					movieThread.execute();
				else {
					movieThread.cancel(true);
					movieThread = null;
				}
			}
		});
	}

	public void setSite(int site) {
		if(getVAState() == VAState.VA_ON)
		{
			actionCloseVAToolBar();
		}
		
		imageViewerState.setSite(site);
		cspanel.update();
		ivp.addLoadingNode();
	}

	public int getSiteState() {
		// TODO Auto-generated method stub
		return imageViewerState.getSiteState();
	}

	public List<Site> getSites() {
		// TODO Auto-generated method stub
		return imageViewerState.getSites();
	}

	public int getSite() {
		// TODO Auto-generated method stub
		return imageViewerState.getSite();
	}

	public void playMovieOnSlice(int movieState) {
		setMovieState(movieState, false);
	}

	public void playMovieOnFrame(int movieState) {
		setMovieState(movieState, true);
	}

	public List<VODimension> searchOverlay(String overlayname)
	{
		return imageViewerState.searchOverlay(overlayname);
	}
}
