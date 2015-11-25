/*
 * AcquisitionUI.java
 *
 * AVADIS Image Management System
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */

package com.strandgenomics.imaging.iacquisition;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.strandgenomics.imaging.iacquisition.genericimport.GenericImporterWizard;
import com.strandgenomics.imaging.iacquisition.script.ScriptEditor;
import com.strandgenomics.imaging.iacquisition.selection.IRecordSelectionListener;
import com.strandgenomics.imaging.iacquisition.selection.RecordSelectionEvent;
import com.strandgenomics.imaging.iacquisition.thumbnail.ThumbnailTable;
import com.strandgenomics.imaging.iacquisition.thumbnail.ThumbnailTableModel;
import com.strandgenomics.imaging.iclient.AcquisitionProfile;
import com.strandgenomics.imaging.iclient.ImageSpaceException;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.Project;
import com.strandgenomics.imaging.iclient.daemon.UploadDaemonService;
import com.strandgenomics.imaging.iclient.daemon.UploadSpecification;
import com.strandgenomics.imaging.iclient.daemon.UserSessionSpecification;
import com.strandgenomics.imaging.iclient.dialogs.LoginDialog;
import com.strandgenomics.imaging.iclient.dialogs.ProgressDialog;
import com.strandgenomics.imaging.iclient.local.DirectUploadExperiment;
import com.strandgenomics.imaging.iclient.local.IndexerListener;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.RawRecord;
import com.strandgenomics.imaging.iclient.local.UploadStatus;
import com.strandgenomics.imaging.iclient.util.ConnectionPreferences;
import com.strandgenomics.imaging.iclient.util.Uploader;
import com.strandgenomics.imaging.icore.AnnotationType;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.IProject;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.ISourceReference;
import com.strandgenomics.imaging.icore.SearchField;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.bioformats.BioRecord;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;
import com.strandgenomics.imaging.icore.image.LutLoader;
import com.strandgenomics.imaging.iviewer.ImageViewerApplet;
import com.strandgenomics.imaging.iviewer.ImageViewerState;
import com.strandgenomics.imaging.iviewer.RecordPropertyManager;
import com.strandgenomics.imaging.iviewer.UIUtils;


/**
 * Main UI class for the acquisition client.
 * 
 * @author nimisha
 *
 */
@SuppressWarnings("serial")
public class AcquisitionUI implements IndexerListener, ActionListener, IRecordSelectionListener, DirectUploadListener{

	private Logger logger = Logger.getRootLogger();
	
	private Context context;
	
	private SessionManager sessionManager;
	
	private boolean indexingInProgress = false;
	
	private Boolean computeSignature = false;
	
	private JTabbedPane bottomPane;
	private UploadTableModel uploaderModel;
	private ImageViewerApplet imagePanel;
	
	private ProgressDialog progressDialog;
	
	RollingFileAppender fileAppender;

	private UploaderService uploaderService;
	
	private IndexingService indexingService;

	private ThumbnailTable thumbPlot;

	private ImportTableModel importerModel;

	private DirectUploadTableModel directUploadTableModel;

//	private String acqVersion = "1.20";
//	private boolean maskLoginField = false;
	
	private static JFrame frame = new JFrame();
	
	// Set up frame  .. Set Menubar and commandbar .. 
	
	public AcquisitionUI(String user, String version) {
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// XXX: This shouldn't create frame, instead launching method should
		// add this in tool's frame.
		
		frame.setTitle(getTitle("",user));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Thread.currentThread().getContextClassLoader().getResource(UIUtils.ICONS_PATH + "largeicon.png")));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeTool();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				closeTool();
			}
		});
		
		// Context contains records .. and selections ..
		context = Context.getInstance();
		context.setVersion(version);
		context.addDirectUploadListener(this);
		
		frame.setJMenuBar(new MainMenuBar(context,this));
		
		frame.getContentPane().setLayout(new BorderLayout());
		
		MainCommandBar commandBar = new MainCommandBar(context,this);
		frame.getContentPane().add(commandBar, BorderLayout.PAGE_START);

		JPanel panel = createPanel(user);
		frame.getContentPane().add(panel);
		
		//started upload service
		uploaderService = new UploaderService();
		uploaderService.start();
		
		//started import service
		indexingService = new IndexingService(context);
		indexingService.start();
		
		IndexingUtils engine = IndexingUtils.getInstance();
		engine.addIndexerListener(this);
		engine.addIndexerListener(indexingService);
		
		 // logs till authentication are written in a separate file
		String logFileName = user == null || user.isEmpty() ? "auth" : user;
        fileAppender = initFileLogger(logFileName, null);
	}

	// Launch the frame ..
	public void launch()
	{
		frame.pack();
		frame.setVisible(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() 
			{	
				try
				{
					System.out.println("calling shutdown hook");
					// this does not logout because access key may be transfered to background service
					
					// surrender acq client license
					ImageSpaceObject.getImageSpace().surrenderAcquisitionLicense();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * save the current session
	 */
	public void saveSession()
	{
		sessionManager.storeSession(context.getRecords());
	}
	
	
	public void login(boolean maskLoginField)
	{
//        showLoginDialog();
//        
//        if(user  == null){
//        	int retval = JOptionPane.showConfirmDialog(frame,"Do you want to try again?", "Authentication Failed", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
//        	if(retval == JOptionPane.YES_OPTION)
//        		login(maskLoginField);
//        	else
//        		System.exit(0);
//        }
//        fileAppender = initFileLogger(user, fileAppender );	
	}
	
	public void login(String host, int port, String appID, String authCode)
	{
		 ImageSpaceObject.getImageSpace().login(false, host, port, appID, authCode);
	}
	
//	private void showLoginDialog()
//	{
//		String authCode = JOptionPane.showInputDialog(null, "Enter Auth Code ");
//	}
	
	
	private void showLoginDialog(boolean maskLoginField) 
	{
		LoginDialog dialog = new LoginDialog(frame, "Server Connection Preferences", maskLoginField );
        dialog.setVisible(true);
        
        if(dialog.isCancelled())
    		System.exit(0);
        
        ConnectionPreferences preferences = ConnectionPreferences.getInstance();
        logger.info("User: " + preferences.getLoginName() + " is trying to log in.");
        
        progressDialog = new ProgressDialog(frame, "Progress");
        progressDialog.setMessage("Logging in user: " + preferences.getLoginName());
        
        SwingWorker<String, Void> loginworker = new SwingWorker<String, Void>() {

        	public String doInBackground() {
        	   ConnectionPreferences preferences = ConnectionPreferences.getInstance();
        	   ImageSpaceObject.getConnectionManager().login(false, preferences.getHostAddress(),  preferences.getHostPort(), preferences.getLoginName(), preferences.getPassword());	
        	   return ImageSpaceObject.getConnectionManager().getUser();
        	}
        	
        	public void done() {
        		String user = null;

				try {
					user = get();
					loginSuccessful(user);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loginFailed(e.getMessage());
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					loginFailed(e.getMessage());
				}finally{
	        		progressDialog.setVisible(false);
	        		progressDialog.dispose();
	        		progressDialog = null;
				}
        	}
        }; 
        loginworker.execute();
        progressDialog.setVisible(true);
	}
	
	
	protected void promptToImport(Project project) 
	{
		File[] selected = AcqclientUIUtils.showDirectoryChooser(frame,"Select File or Folder to Import in Project: " + project.getName(), ImportDialogType.NORMAL_IMPORT_DIALOG);
		computeSignature=AcqclientUIUtils.getCheckBoxValue();
		
		if (selected != null && selected.length>0) 
		{
			List<File> fileNames = new ArrayList<File>();
			for(File file:selected)
			{
				fileNames.add(file.getAbsoluteFile());
			}
			
			importerModel.addExperiment(fileNames);
			indexingService.addFilesToIndex(Arrays.asList(selected),computeSignature,true);
		}
		else
			showNoRecordsError();
	}
	
	protected void showNoRecordsError(){
		JOptionPane.showMessageDialog(
			frame,
			"Please Import File(s) or Change Project.",
			"No Records", JOptionPane.ERROR_MESSAGE);
	}
	
	protected void loginFailed(String error) {
		logger.error("Authentication failed: " + error);
	}

	protected void loginSuccessful(String user) {
		logger.info("User: " + user + " successfully logged in.");
	}

	private RollingFileAppender initFileLogger(String user, RollingFileAppender fileAppender) {
		if(fileAppender!=null)
			logger.removeAppender(fileAppender);
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			File logFile = new File(Constants.getConfigDirectory(), "imanage-acqclient-" + user +"-"+ dateFormat.format(date)+".log");
			 fileAppender = new RollingFileAppender(new PatternLayout(), logFile.getAbsolutePath());
			 
//			 PrintStream pso = new PrintStream(new FileOutputStream(new File(Constants.getConfigDirectory(), "imanage-acqclient-syso-" + user +"-"+ dateFormat.format(date)+".log"), true));
//			 System.setOut(pso);
//			 
//			 PrintStream pse = new PrintStream(new FileOutputStream(new File(Constants.getConfigDirectory(), "imanage-acqclient-syserr-" + user +"-"+ dateFormat.format(date)+".log"), true));
//			 System.setErr(pse);
			 
			logger.addAppender(fileAppender);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return fileAppender;
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	protected void closeTool() 
	{
		if (!uploaderService.isUploadQueueEmpty())
		{
			int value = JOptionPane.showConfirmDialog(frame, "Upload in progress. Exiting Acquisition Client will terminate the upload in progress. Do you really want to exit?", "",
					JOptionPane.YES_NO_OPTION);
			if (value == JOptionPane.NO_OPTION)
				return;
			try
			{
				shutdownUploadService();
			}
			catch(Exception e)
			{
				logger.info("error in shutting down uploads in progress");
			}
		}
		
		if (indexingInProgress || !indexingService.isImportQueueEmpty())
		{
			int value = JOptionPane.showConfirmDialog(frame, "Import in progress. Exiting Acquisition Client will terminate the import in progress. Do you really want to exit?", "",
					JOptionPane.YES_NO_OPTION);
			if (value == JOptionPane.NO_OPTION)
				return;
		}
		
		try
		{
			clearUploaded();
		}
		catch(Exception e)
		{
			logger.info("error in deleting uploaded data");
		}
		
		// write the session
		if(sessionManager!=null)
		{
			sessionManager.storeSession(context.getRecords());
		}
		
		// surrender acq license
		ImageSpaceObject.getImageSpace().surrenderAcquisitionLicense();
		
		System.exit(0);
	}
	
	/**
	 * clears all the uploaded/duplicate records from client
	 * @throws InterruptedException 
	 */
	private void clearUploaded()
	{
		List<IRecord> toDelete = new ArrayList<IRecord>();
		
		List<IRecord> records = context.getRecords();
		for(IRecord record:records)
		{
			IProject prj = record.getParentProject();
			if(prj != null)
			{
				toDelete.add(record);
			}
		}
		
		if (toDelete.size() > 0)
		{
			int value = JOptionPane.showConfirmDialog(frame, "Do you want to clear all uploaded records?", "", JOptionPane.YES_NO_OPTION);
			if (value == JOptionPane.YES_OPTION)
			{
				context.deleteRecords(toDelete);
			}
		}
	}
	
	/**
	 * clears the upload queue of upload service
	 */
	private void shutdownUploadService() {
		uploaderService.shutdown();
	}

	// Create the UI 
	protected JPanel createPanel(String user) {
		JPanel panel = new JPanel(new BorderLayout());

		StatusBar statusBar = new StatusBar(context);
		context.addRecordListener(statusBar);
		context.addSelectionListener(statusBar);
		
		panel.add(statusBar, BorderLayout.SOUTH);
        
		IndexingUtils engine = IndexingUtils.getInstance();
		engine.addIndexerListener(statusBar);
		
		ThumbnailTableModel thumbnailModel = new ThumbnailTableModel(context.getRecords());
		thumbPlot = new ThumbnailTable(thumbnailModel);
		context.addRecordListener(thumbnailModel);
		
		JScrollPane thumbPane = new JScrollPane(thumbPlot);
		thumbPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		imagePanel = new ImageViewerApplet();

		JSplitPane topPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				thumbPane, imagePanel);
		topPane.setDividerLocation(120);
		topPane.setDividerSize(5);

		bottomPane = UIUtils.getTabbedPane();

//		SpreadsheetModel spreadsheetModel = new SpreadsheetModel(context.getRecords(),context.getUploadStatus());
		SpreadsheetModel spreadsheetModel = new SpreadsheetModel(context.getRecords());
		Spreadsheet spreadsheet = new Spreadsheet(spreadsheetModel);
		context.addRecordListener(spreadsheetModel);
		
		JScrollPane scrollPane = new JScrollPane(spreadsheet);
		bottomPane.addTab("Summary", UIUtils.getIcon("dynamic_data.png"), scrollPane);

		LogPanel log = new LogPanel();
		bottomPane.addTab("Console", UIUtils.getIcon("console.png"),log);
		
		uploaderModel = new UploadTableModel();
		UploadPanel uploadPanel = new UploadPanel(uploaderModel);
		bottomPane.addTab("Uploads", UIUtils.getIcon("upload.gif"), uploadPanel);
		
		final UploadDaemonTableModel uploaderDaemonModel = new UploadDaemonTableModel(user);
		UploadDaemonPanel uploadDaemonPanel = new UploadDaemonPanel(uploaderDaemonModel);
		bottomPane.addTab("Background Uploads", uploadDaemonPanel);
		
		directUploadTableModel = new DirectUploadTableModel();
		DirectUploadPanel directUploadPanel = new DirectUploadPanel(directUploadTableModel);
		bottomPane.addTab("Direct Uploads", directUploadPanel);
		
		importerModel = new ImportTableModel();
		importerModel.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e)
			{
				if (importerModel.isStopped())
				{
					List<File> filesToCancel = importerModel.getSelectedImports();
					importerModel.setStopped(false);
					
					indexingService.stopSelected(filesToCancel);
				}
				
				if(importerModel.isReindexed())
				{
					List<File> fileNames = importerModel.getSelectedImports();
					importerModel.setReindexed(false);
					
					if(fileNames == null || fileNames.size()==0)
						return;
					
					List<File> fileList = new ArrayList<File>();
					for(File f : fileNames)
					{
						if(f.exists())
							fileList.add(f);
					}
					
					indexingService.addFilesToIndex(fileList, computeSignature,true);
				}
			}
		});
		
		ImportPanel importPanel = new ImportPanel(importerModel);
		bottomPane.addTab("Imports", UIUtils.getIcon("add.png"), importPanel);

		JSplitPane centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				topPane, bottomPane);
		centerPane.setDividerLocation(400);
		centerPane.setDividerSize(5);

		panel.add(centerPane, BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(800, 600));
		
		
		// Thumbanil and Spreadsheet notify context of any user initiated selection changes
		thumbPlot.addSelectionListener(context);
		spreadsheet.addSelectionListener(context);
		
		// Context updates everyone else .. It needs to fire events for clear, invert and select-all menu calls 
		context.addSelectionListener(this);
		context.addSelectionListener(thumbPlot);
		context.addSelectionListener(spreadsheet);
		return panel;
	}
	
	protected void setUpSession(Project project){
		
		if (sessionManager != null)
		{
			if (project.equals(context.getProject()))
				return;
			else
			{
				sessionManager.storeSession(context.getRecords());
			}
		}
		
		String userName = ImageSpaceObject.getConnectionManager().getUser();
		sessionManager = new SessionManager(userName + "_" + project.getName());
		
		List<IRecord> recordList = sessionManager.loadSession();
		context.setProject(project);
		frame.setTitle(getTitle(project.getName(),userName));
		if (recordList != null && recordList.size() > 0) {
			context.addRecords(recordList);
			context.setSelectedRecord(recordList.get(0));
			logger.info("updating upload status of records.");
			
			long stime = System.currentTimeMillis();
			context.updateUploadStatus(recordList);
			long etime = System.currentTimeMillis();
		}else
			promptToImport(project);
	}
	
	private String getTitle(String projectName, String user)
	{
		String microscopeName = ImageSpaceObject.getImageSpace().getMicroscope();
		String title = "iManage - Acquisition: " + projectName + " "+"[Logged in as "+user;
		if(microscopeName!=null && !microscopeName.isEmpty() && !"NA".equalsIgnoreCase(microscopeName))
			title += " on Microscope "+microscopeName;
		return title+"]";
	}

	
	/*
	 *  ActionListener methods
	 * 
	 */
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("index")){
			indexDirectories(true);
		} 
		else if(command.equals("directImport")){
			indexDirectories(false);
		} 
		else if(command.equals("genericImport")){
			runGenericImporter();
		} 
		else if(command.equals("stopIndexing")){
			if(indexingInProgress) {
				importerModel.cancelAll();
			}
			else
			JOptionPane.showMessageDialog(frame, "Import is already complete.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(command.equals("delete")){
			if(context.getRecordCount()==0){
				showNoRecordsError();
				return;
			}
			deleteExperiment();
		}
		else if(command.equals("upload")){
			if(context.getRecordCount()==0){
				showNoRecordsError();
				return;
			}
			if(uploadExperiment())
				bottomPane.setSelectedIndex(2);
		}
		else if(command.equals("uploadDaemon")){
			if(context.getRecordCount()==0){
				showNoRecordsError();
				return;
			}
			boolean status = uploadDaemon();
			if(status)
				bottomPane.setSelectedIndex(3);
		}
		else if(command.equals("uploadSettings")){
			showUploadSettingsDialog();
		}
		else if(command.equals("logout")){
			sessionManager.storeSession(context.getRecords());
		}
		else if(command.equals("exit")){
			closeTool();
		}
		else if(command.equals("addProperty")){
			if(context.getRecordCount()==0){
				showNoRecordsError();
				return;
			}
			addProperty();	
		}
		else if(command.equals("removeProperty")){
			if(context.getRecordCount()==0){
				showNoRecordsError();
				return;
			}
			removeProperty();	
		} else if(command.equals("mergeRecords")){
			if(context.getRecordCount()==0){
				showNoRecordsError();
				return;
			}
			mergeRecords();
		} else if(command.equals("changeProject")){
			if (!uploaderService.isUploadQueueEmpty()) 
			{
				JOptionPane.showMessageDialog(frame,
						"Cant change project: Upload in progress.", "",
						JOptionPane.ERROR_MESSAGE); 
				return;
			}
			if(indexingInProgress)
			{
				JOptionPane.showMessageDialog(frame,
						"Cant change project: Indexing in progress.", "",
						JOptionPane.ERROR_MESSAGE); 
				return;
			}
			Project project = selectProject();
			if(project != null){
				setUpSession(project);
			}
		} else if(command.equals("saveSession")){
			saveSession();
		} else if(command.equals("setThumbnail")) {
			int value = JOptionPane.showConfirmDialog(frame, "Current image will be saved as thumbnail for this record. Do you want to continue?", "Set Thumbnail", JOptionPane.YES_NO_OPTION);
			if(value == JOptionPane.NO_OPTION)
				return;
			
			List<IChannel> channels = imagePanel.getChannels();
			List<IChannel> selectedChannels = imagePanel.getSelectedChannels();
			int[] channelNos = new int[selectedChannels.size()];
			int cnt = 0;
			for(int i=0;i<channels.size();i++)
			{
				if(selectedChannels.contains(channels.get(i)))
					channelNos[cnt++] = i;
			}
			
			int frameNo = imagePanel.getFrame();
			int sliceNo = imagePanel.getSlice();
			int siteNo = imagePanel.getSite();
			boolean zStacked = imagePanel.getSliceState()==ImageViewerState.SliceState.Z_STACK;
			boolean mosaic = imagePanel.getChannelState() == ImageViewerState.ChannelState.MULTI_CHANNEL;
			boolean useChannelColor = imagePanel.getChannelScale()==ImageViewerState.ChannelScale.COLOR;
			
			if(context.getSelectedRecordCount()<=0)
				showNoRecordsError();
			else
			{
				IRecord record = context.getSelectedRecords().get(0);
				if(record instanceof BioRecord)
				{
					try
					{
						BufferedImage image = ((BioRecord)record).getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos).getImage(zStacked, mosaic, useChannelColor, null);
						File imageFile = File.createTempFile("thumbnail_"+record.getSourceFilename(), "png");
						imageFile.deleteOnExit();
						ImageIO.write(image, "PNG", imageFile);
						
						record.setThumbnail(imageFile);
						thumbPlot.clearCacheForRecord(record);
						imageFile.delete();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		} 
		else if(command.equals("editProfile"))
		{
			editProfile();
		}
		else if(command.equals("openEditor"))
		{
			ScriptEditor scriptEditor = new ScriptEditor();
			scriptEditor.launchScriptEditor();
		}
		else if(command.equals("exportImage"))
		{
			openExportImageDialog();
		}
		else if(command.equals("copyChannel"))
		{
			Map<String, String> channelClipboard = new HashMap<String, String>();
			if(context.getSelectedRecordCount()<1)
				JOptionPane.showMessageDialog(null, "Please select at least one record." , "Error", JOptionPane.ERROR_MESSAGE);
			IRecord record = context.getSelectedRecords().get(0);
			int chCnt = record.getChannelCount();
			for(int i=0;i<chCnt;i++)
			{
				String chName = record.getChannel(i).getName();
				String lutName = record.getChannel(i).getLut();
				channelClipboard.put(chName, lutName);
			}
			
			JOptionPane.showMessageDialog(frame, "Current channel properties successfully copied");
			context.setChannelClipboard(channelClipboard);
		}
		else if(command.equals("pasteChannel"))
		{
			if(context.getSelectedRecordCount()<1)
				JOptionPane.showMessageDialog(null, "Please select at least one record." , "Error", JOptionPane.ERROR_MESSAGE);
			Map<String, String> channelClipboard = context.getChannelClipboard();
			List<IRecord> records = context.getSelectedRecords();
			for(IRecord record:records)
			{
				int chCnt = record.getChannelCount();
				for(int i=0;i<chCnt;i++)
				{
					String chName = record.getChannel(i).getName();
					String lutName = channelClipboard.get(chName);
					if(lutName != null)
						record.setChannelLUT(i, lutName);
				}
			}
		}
		else if(command.equals("resetChannel"))
		{
			if(context.getSelectedRecordCount()<1)
				JOptionPane.showMessageDialog(null, "Please select at least one record." , "Error", JOptionPane.ERROR_MESSAGE);
			String[] primaryColorLUTs = LutLoader.getInstance().getPrimaryColorLUTs();
			int noOfColors = primaryColorLUTs.length;
			
			List<IRecord> records = context.getSelectedRecords();
			for(IRecord record:records)
			{
				int noOfChannels = record.getChannelCount();
				for(int i=0;i<noOfChannels;i++)
				{
					String lutName = noOfChannels == 1 ? LutLoader.STD_LUTS[0] : primaryColorLUTs[i%noOfColors];
					record.setChannelLUT(i, lutName);
				}
			}
		}
	}
	
	private void showUploadSettingsDialog()
	{
		String value = JOptionPane.showInputDialog(frame, "Upload Service Port Number", Context.getInstance().getUploaderPort());
		if(value==null)
			return;
		
		try
		{
			int port = Integer.parseInt(value);
			context.setUploaderPort(port);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(frame, "Illegal value for port", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void runGenericImporter()
	{
		GenericImporterWizard wiz = new GenericImporterWizard();
		List<RecordMetaData> records = wiz.showImporterWizard(null, "Import Using Filename(s)");
		
		if(records==null || records.isEmpty())
			return;
		
		File rootFolder = records.get(0).getRootDirectory();
		
		importerModel.addExperiment( Arrays.asList( new File[]{rootFolder} ));
		indexingService.addGenericImports(rootFolder, records);
	}

	private void openExportImageDialog()
	{
		File f = AcqclientUIUtils.showFileChooser("Export Image As");
		
		if(f == null)
			return;
		
		String ext = AcqclientUIUtils.getExtension(f);
		
		if(!ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("jpeg") && !ext.equalsIgnoreCase("png") && !ext.equalsIgnoreCase("tiff"))
		{
			JOptionPane.showMessageDialog(frame, "Output image format "+ext+" not supported", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		List<IChannel> channels = imagePanel.getChannels();
		List<IChannel> selectedChannels = imagePanel.getSelectedChannels();
		int[] channelNos = new int[selectedChannels.size()];
		int cnt = 0;
		for(int i=0;i<channels.size();i++)
		{
			if(selectedChannels.contains(channels.get(i)))
				channelNos[cnt++] = i;
		}
		
		int frameNo = imagePanel.getFrame();
		int sliceNo = imagePanel.getSlice();
		int siteNo = imagePanel.getSite();
		boolean zStacked = imagePanel.getSliceState()==ImageViewerState.SliceState.Z_STACK;
		boolean mosaic = imagePanel.getChannelState() == ImageViewerState.ChannelState.MULTI_CHANNEL;
		boolean useChannelColor = imagePanel.getChannelScale()==ImageViewerState.ChannelScale.COLOR;
		
		if(context.getCheckedRecordsCount()<=0)
			showNoRecordsError();
		else
		{
			IRecord record = context.getCheckedRecords().get(0);
			if(record instanceof BioRecord)
			{
				try
				{
					BufferedImage image = ((BioRecord)record).getOverlayedPixelData(sliceNo, frameNo, siteNo, channelNos).getImage(zStacked, mosaic, useChannelColor, null);
					ImageIO.write(image, ext, f);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Merge records with matching dimensions in a single record
	 */
	public void mergeRecords() {
		
		if(indexingInProgress){
			JOptionPane.showMessageDialog(frame,
					"Cannot merge while import is in progress.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (context.getCheckedRecordsCount() < 1) {
			JOptionPane.showMessageDialog(frame,
					"Please select record(s) to merge.",
					"No record selected", JOptionPane.ERROR_MESSAGE);
			return;
		}

			
		if (context.getSelectedRecordCount() == 1) {
			int retval = JOptionPane.showConfirmDialog(frame,
					"Do you want to merge records with similar dimensions and same source file as selected record?",
					"Merge Similar Records",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
			if(retval == JOptionPane.NO_OPTION)
				return;
		}else{
			int retval = JOptionPane.showConfirmDialog(frame,
					"You have seleteced " + context.getSelectedRecordCount() + " record(s). These records will be merged as site(s) in a single record.",
					"Merge Selected Records",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.INFORMATION_MESSAGE);
			if(retval == JOptionPane.CANCEL_OPTION)
				return;
		}
			
		logger.info("Merging Records ..");
		progressDialog = new ProgressDialog(frame, "Progress");
		progressDialog.setMessage("Merging Records");
		SwingWorker<String, Void> mergeworker = new SwingWorker<String, Void>() {
        	public String doInBackground() {
        		String error = null;
        		try{
        			context.mergeRecords(context.getCheckedRecords());
        		}catch(IllegalArgumentException e){
        			error = e.getMessage();	
        		}
        		return error;
         	}
         	
         	public void done() {
         		String error = null;
         		progressDialog.setVisible(false);
        		progressDialog.dispose();
        		progressDialog = null;
         		try {
					error = get();
					if(error != null)
						mergeFailed(error);
					else
						mergeSuccessful();
				} catch (InterruptedException e) {
					e.printStackTrace();
					mergeFailed(e.getMessage());
				} catch (ExecutionException e) {
					e.printStackTrace();
					mergeFailed(e.getMessage());
				}
         	}
		};
		mergeworker.execute();
		progressDialog.setVisible(true);
	}
	
	private void mergeFailed(String error) {
		final String message = error;
		logger.error("Merging Records Failed: " + message);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(frame,
						message,
						"Merge Failed", JOptionPane.ERROR_MESSAGE);
			}
		});
		
	}
	
	private void mergeSuccessful() {
		logger.info("Merging Records is Succcesful.");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(frame,
						"Records are successfully merged.",
						"Merge Successful", JOptionPane.INFORMATION_MESSAGE);
			}
		});	
	}
	
	private void editProfile()
	{
		if(context.getCheckedRecordsCount()<1)
		{
			JOptionPane.showMessageDialog(frame, "Please select at least one record." , "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ProfileDialog pd = new ProfileDialog();
		int value = JOptionPane.showConfirmDialog(frame, pd, "Add Profile", JOptionPane.PLAIN_MESSAGE);
		if(value != JOptionPane.OK_OPTION)
		{
			return;
		}
		
		AcquisitionProfile profile = pd.getAcquisitionProfile();
		List<IRecord> records = context.getCheckedRecords();
		for(IRecord record: records)
		{
			((RawRecord)record).setAcquisitionProfile(profile);
		}
	}

	public void addProperty() {
		
		if (context.getCheckedRecordsCount() < 1) {
			JOptionPane.showMessageDialog(frame,
					"Please select record(s) to add property.",
					"No record selected", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		List <SearchField> searchFields = context.getAllSearchFields();
		Collection<SearchField> fields = context.getProject().getUserAnnotationFields();
		if(fields!=null)
		{
			for(SearchField field: fields)
			{
				if(!searchFields.contains(field))
					searchFields.add(field);
			}
		}
		
		
		Map<String, Object> map = RecordPropertyManager.addUserAnnotations(searchFields);
		String key = (String)map.get("key");
		Object value = map.get("value");
		AnnotationType type = (AnnotationType)map.get("type");
		
		if(key == null || value == null || type == null)
			return;

		int count = context.updateAnnotations(context.getCheckedRecords(),
				key, value,type);
		JOptionPane.showMessageDialog(frame, "Added Property " + key + " to "
				+ count + " of " + context.getSelectedRecordCount()
				+ " selected record(s).", "Added Property",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void removeProperty() {
		System.out.println("Invokes");
		if (context.getCheckedRecordsCount() < 1) {
			JOptionPane.showMessageDialog(frame,
					"Please select record(s) to remove property.",
					"No record selected", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String key = RecordPropertyManager.removeUserAnnotations(context.getAllSearchFields());

		if (key == null || key.isEmpty())
			return;

		int count = context.removeAnnotations(context.getCheckedRecords(), key);

		JOptionPane.showMessageDialog(frame, "Removed Property " + key
				+ " from " + count + " of " + context.getSelectedRecordCount()
				+ " selected record(s).", "Removed Property",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void indexDirectories(boolean createRecords)
	{
		ImportDialogType type = ImportDialogType.NORMAL_IMPORT_DIALOG;
		if(!createRecords)
			type = ImportDialogType.DIRECT_UPLOAD_IMPORT_DIALOG;
			
		File[] selectedFile = AcqclientUIUtils.showDirectoryChooser(frame,"Select File or Folder to Import in Project: " + context.getProject().getName(), type);
		computeSignature=AcqclientUIUtils.getCheckBoxValue();
		
		if(selectedFile!=null && selectedFile.length>0)
		{
			try
			{
				List<File> fileNames = new ArrayList<File>();
				for(File file: selectedFile)
				{
					fileNames.add(file.getAbsoluteFile());
				}
				
				importerModel.addExperiment(fileNames);
				indexingService.addFilesToIndex(fileNames, computeSignature,createRecords);
			}
			catch (ImageSpaceException e)
			{}
		}
		bottomPane.setSelectedIndex(1);
	}
	
	
	public void deleteExperiment(){

		List<IRecord> recordsToDelete = context.getCheckedRecords();
		if(recordsToDelete.size()<=0){
			JOptionPane.showMessageDialog(frame, "Please select some record(s) to delete", "No record Selected", JOptionPane.ERROR_MESSAGE);
			return ;
		}
		
		Set<IExperiment> experimentsToDelete = new HashSet<IExperiment>();
		for(int i=0;i<recordsToDelete.size();i++){
			IRecord record = recordsToDelete.get(i);
			if(!context.canDeleteRecord(record))
				continue;
			
			IExperiment experiment = record.getExperiment();
				
			if(!experimentsToDelete.contains(experiment))
				experimentsToDelete.add(experiment);
		}
		if(experimentsToDelete.size()<=0){
			JOptionPane.showMessageDialog(frame, "All selected records are queued for upload.", "No Record(s) to Delete", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		List<IExperiment> toDelete = AcqclientUIUtils.showDeleteDialog(frame, experimentsToDelete);
		List<IRecord> finalRecordsDelete = new ArrayList<IRecord>();
		for(int i=0;i<toDelete.size();i++){
			IExperiment experiment = toDelete.get(i);
			Iterator<Signature> it = experiment.getRecordSignatures()
					.iterator();
			while (it.hasNext()) {
				Signature signature = it.next();
				IRecord record = experiment.getRecord(signature);
				finalRecordsDelete.add(record);
			}
		}
		
		if(finalRecordsDelete.size()<=0){
			JOptionPane.showMessageDialog(frame, "No selected record can be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		context.deleteRecords(finalRecordsDelete);	
		for(IRecord record: finalRecordsDelete)
			record.dispose();
		context.setSelectedRecords(new ArrayList<IRecord>());
	}
	
	public boolean uploadDaemon()
	{
		UploadDaemonService service = Context.getInstance().getServiceStub();
		if(service==null)
		{
			JOptionPane.showMessageDialog(frame, "Upload Daemon not running.", "Upload Daemon not running.", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		List<IRecord> recordsToUpload = context.getCheckedRecords();
		if(recordsToUpload.size()<=0){
			JOptionPane.showMessageDialog(frame, "Please select some record(s) to upload", "No record(s) Selected", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		long size = 0;
		List<IExperiment> experimentsToUpload = new ArrayList<IExperiment>();
		for(int i=0;i<recordsToUpload.size();i++)
		{
			IRecord record = recordsToUpload.get(i);
			if(!context.canUpdateRecord(record))
				continue;
			
			IExperiment experiment = record.getExperiment();
			
			Collection<IAttachment> attachments = record.getAttachments();
			if(attachments!=null)
			{
				for(IAttachment attachment:attachments)
				{
					File file = attachment.getFile();
					if(file!=null)
						size = size + file.length(); 
				}
			}
			for(ISourceReference ref:experiment.getReference())
			{
				// count size of all the selected source files
				size = size + ref.getSize();
			}
			if(!experimentsToUpload.contains(experiment))
				experimentsToUpload.add(experiment);
		}
		
		if(experimentsToUpload.size()<=0){
			JOptionPane.showMessageDialog(frame, "All selected records are either uploaded or queued for upload.", "No record(s) to upload", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		List<Project> activeProjects = getActiveProjects();

		if(activeProjects == null || activeProjects.isEmpty()){
			JOptionPane.showMessageDialog(frame, "You are not member of any project", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		UploadDialog uploadDialog = new UploadDialog(frame, experimentsToUpload, context.getProject());
		uploadDialog.setVisible(true);
		
		if(!uploadDialog.returnValue() || !uploadDialog.isSpaceAvailable(size)){
			return false;
		}	
			
		experimentsToUpload = uploadDialog.getSelectedExperiments();
		
		try
		{
			if(experimentsToUpload!=null)
			{
				for(IExperiment experiment:experimentsToUpload)
				{
					submitRequest(experiment, uploadDialog.getSelectedProject());
					
					context.setUploadStatus((RawExperiment)experiment, UploadStatus.QueuedBackground);
				}
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	private void submitRequest(IExperiment request, Project project) throws Exception
	{
        Registry registry = LocateRegistry.getRegistry(context.getUploaderPort());
        UploadDaemonService serviceStub = null;
		try 
		{
			serviceStub = (UploadDaemonService) registry.lookup(UploadDaemonService.class.getCanonicalName());
		} 
		catch (NotBoundException e)
		{
			throw new IOException(e);
		}
		
		String user = ImageSpaceObject.getImageSpace().getUser();
		String host = ImageSpaceObject.getImageSpace().getHost();
		int port = ImageSpaceObject.getImageSpace().getPort();
		boolean isSSL = ImageSpaceObject.getImageSpace().isSSL();
		
		UserSessionSpecification session = new UserSessionSpecification(user, project.getAccessKey(), project.getName(), host, port, isSSL);
		UploadSpecification spec = new UploadSpecification(session, (RawExperiment)request);
                    
        serviceStub.submitUploadRequest(spec);
	}
	
	public boolean uploadExperiment() 
	{
		List<IRecord> recordsToUpload = context.getSelectedRecords();
		if(recordsToUpload.size()<=0){
			JOptionPane.showMessageDialog(frame, "Please select some record(s) to upload", "No record(s) Selected", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		long size = 0;
		List<IExperiment> experimentsToUpload = new ArrayList<IExperiment>();
		for(int i=0;i<recordsToUpload.size();i++)
		{
			IRecord record = recordsToUpload.get(i);
			if(!context.canUpdateRecord(record))
				continue;
			
			IExperiment experiment = record.getExperiment();
			
			Collection<IAttachment> attachments = record.getAttachments();
			if(attachments!=null)
			{
				for(IAttachment attachment:attachments)
				{
					File file = attachment.getFile();
					if(file!=null)
						size = size + file.length(); 
				}
			}
			for(ISourceReference ref:experiment.getReference())
			{
				// count size of all the selected source files
				size = size + ref.getSize();
			}
			if(!experimentsToUpload.contains(experiment))
				experimentsToUpload.add(experiment);
		}
		
		if(experimentsToUpload.size()<=0){
			JOptionPane.showMessageDialog(frame, "All selected records are either uploaded or queued for upload.", "No record(s) to upload", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		List<Project> activeProjects = getActiveProjects();

		if(activeProjects == null || activeProjects.isEmpty()){
			JOptionPane.showMessageDialog(frame, "You are not member of any project", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		for(Project p : activeProjects)
		{
			if(context.getProject().getName().equals(p.getName()))
			{
				context.updateProjectDetails(p);
				break;
			}
		}
		
		UploadDialog uploadDialog = new UploadDialog(frame, experimentsToUpload, context.getProject());
		uploadDialog.setVisible(true);
		
		if(!uploadDialog.returnValue() || !uploadDialog.isSpaceAvailable(size)){
			return false;
		}	
			
		experimentsToUpload = uploadDialog.getSelectedExperiments();
		Project project = uploadDialog.getSelectedProject();
			
		// XXX: Some sequence of events lead to non-stop scrolling of thumbnails when large number of records are selected for upload
		// Thus clearing selection .. needs an elaborate fix for selection model though ..
		//spreadsheet.getSelectionModel().clearSelection();
		//logger.info("Cleared Selection .. ");
		List<String> projectName = new ArrayList<String>();
		List<JProgressBar> progressMonitors = new ArrayList<JProgressBar>();
		for(int i=0;i<experimentsToUpload.size();i++)
		{
			final JProgressBar progress = new JProgressBar(0,100);
			progressMonitors.add(progress);
			
			projectName.add(project.getName());
			
			final Uploader uploader = project.getUploader( (RawExperiment) experimentsToUpload.get(i));
			final UploadTask uploadTask = new UploadTask(uploader);
			prepareForUpload(uploader.getExperiment());
			uploadTask.addPropertyChangeListener(new PropertyChangeListener() 
			{
				@Override
				public void propertyChange(PropertyChangeEvent evt) 
				{
					if ("progress" == evt.getPropertyName() )
					{
						int value = uploadTask.getProgress();
						String msg = uploadTask.getMessage();
						UploadStatus uploadState = uploadTask.getUploadState();
						progress.setValue(value);
						progress.setString(msg);
						progress.revalidate();
						
						if(value == 100)
						{
							postUploadChanges(uploader.getExperiment(), uploadTask.getStatus(), uploadTask.getMessage(), uploadState);
						}
						
					}
				}
			});
			
			uploaderService.addTask(uploadTask);
		}
		
		uploaderModel.addExperiment(experimentsToUpload, progressMonitors, projectName);
		uploaderModel.addTableModelListener(new TableModelListener() 
		{
			public void tableChanged(TableModelEvent e) 
			{
				if(uploaderModel.isCanceled())
				{
					List<String> tasks = uploaderModel.getSelectedTasks();
					uploaderService.cancelSelectedTasks(tasks);
					
					uploaderModel.setCanceled(false);
				}
			}
		});
		
		return false;
	}
	
	public boolean directUpload(List<IExperiment> experimentsToUpload)
	{
		double size = 0;
		
		for(IExperiment exp:experimentsToUpload)
		{
			size += ( (RawExperiment) exp).getSeedFile().length();
		}
		
		Project project = context.getProject();
		double sizeInGB = (double)size/(double)(1024*1024*1024);
		if(project.getDiskQuota()< project.getSpaceUsage() + sizeInGB)
		{
			JOptionPane.showMessageDialog(frame, "Selected records will exceed storage quota assigned for selected project\nSelected records size = "+sizeInGB+" GB \nQuota assigned to selected project = "+project.getDiskQuota()+" GB", "Can't upload", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// XXX: Some sequence of events lead to non-stop scrolling of thumbnails when large number of records are selected for upload
		// Thus clearing selection .. needs an elaborate fix for selection model though ..
		//spreadsheet.getSelectionModel().clearSelection();
		//logger.info("Cleared Selection .. ");
		List<String> projectName = new ArrayList<String>();
		List<JProgressBar> progressMonitors = new ArrayList<JProgressBar>();
		for(int i=0;i<experimentsToUpload.size();i++)
		{
			final JProgressBar progress = new JProgressBar(0,100);
			progressMonitors.add(progress);
			
			projectName.add(project.getName());
			
			final Uploader uploader = project.getUploader( (RawExperiment) experimentsToUpload.get(i));
			final UploadTask uploadTask = new UploadTask(uploader);
			prepareForUpload(uploader.getExperiment());
			uploadTask.addPropertyChangeListener(new PropertyChangeListener() 
			{
				@Override
				public void propertyChange(PropertyChangeEvent evt) 
				{
					if ("progress" == evt.getPropertyName() )
					{
						int value = uploadTask.getProgress();
						String msg = uploadTask.getMessage();
						UploadStatus uploadState = uploadTask.getUploadState();
						progress.setValue(value);
						progress.setString(msg);
						progress.revalidate();
						
						if(value == 100)
						{
							postUploadChanges(uploader.getExperiment(), uploadTask.getStatus(), uploadTask.getMessage(), uploadState);
						}
						
					}
				}
			});
			
			uploaderService.addTask(uploadTask);
		}
		
		uploaderModel.addExperiment(experimentsToUpload, progressMonitors, projectName);
		
		uploaderModel.addTableModelListener(new TableModelListener() 
		{
			public void tableChanged(TableModelEvent e) 
			{
				if(uploaderModel.isCanceled())
				{
					List<String> tasks = uploaderModel.getSelectedTasks();
					uploaderService.cancelSelectedTasks(tasks);
					
					uploaderModel.setCanceled(false);
				}
			}
		});
		
		return false;
	}
	
	public boolean directUploadInBackground(List<IExperiment> experimentsToUpload)
	{
		double size = 0;
		
		for(IExperiment exp:experimentsToUpload)
		{
			size += ( (RawExperiment) exp).getSeedFile().length();
		}
		
		Project project = context.getProject();
		double sizeInGB = (double)size/(double)(1024*1024*1024);
		if(project.getDiskQuota()< project.getSpaceUsage() + sizeInGB)
		{
			JOptionPane.showMessageDialog(frame, "Selected records will exceed storage quota assigned for selected project\nSelected records size = "+sizeInGB+" GB \nQuota assigned to selected project = "+project.getDiskQuota()+" GB", "Can't upload", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		try
		{
			if(experimentsToUpload!=null)
			{
				for(IExperiment experiment:experimentsToUpload)
				{
					submitRequest(experiment, context.getProject());
					
					context.setUploadStatus((RawExperiment)experiment, UploadStatus.QueuedBackground);
				}
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Project selectProject(){

		List <Project> activeProjects = this.getActiveProjects();
		if(activeProjects == null){
			JOptionPane.showMessageDialog(frame, "You do not have access to a project on the server.", "No Project(s) Available", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		Object project = JOptionPane.showInputDialog(frame, "Project: ", "Select Project", JOptionPane.PLAIN_MESSAGE, null, (Project [])activeProjects.toArray(new Project[0]), activeProjects.get(0));
		return (Project)project;
		
	}
	
	public List<Project> getActiveProjects(){
		List<Project> activeProjects = null;
		System.out.println("Getting all the projects...\n");
		try
		{
			activeProjects = ImageSpaceObject.getConnectionManager().getActiveProjects();
		}
		catch (final ImageSpaceException e)
		{
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(frame, "Connection Failed","Error", JOptionPane.ERROR_MESSAGE);
					login(true);
				}
			});
		}
		return activeProjects;
	}
	
	/**
	 * modify the upload time of the records
	 * @param rawExperiment which is NOT successfully uploaded
	 */
	protected void prepareForUpload(RawExperiment rawExperiment){
		logger.info("[Upload]: Submitting " + rawExperiment.getSourceFilename() + " for upload.");
		context.setUploadStatus(rawExperiment, UploadStatus.Queued);
	}
	
	/**
	 * modify the upload time of the records which are NOT successfully uploaded
	 * @param rawExperiment which is NOT successfully uploaded
	 * @param b to indicate upload is successful or failed
	 * @param message logging message 
	 * @param stateValue state of upload (Success, Failure, Duplicate, Queued etc) 
	 */
	protected void postUploadChanges(RawExperiment rawExperiment, boolean b, String message, UploadStatus stateValue) 
	{
		
		Iterator<IRecord> it = rawExperiment.getRecords().iterator();
		
		while(it.hasNext()){
			BioRecord record = (BioRecord) it.next();
			if(!b){
				record.setUploadTime(null);
			}else{
				long time = System.currentTimeMillis();
				Date uploadTime = new Date(time);
				record.setUploadTime(uploadTime);
			}
			context.setUploadStatus(rawExperiment, stateValue);
			context.updateRecordUploadTime();
		}
		
		logger.info("[Upload]: " + rawExperiment.getSourceFilename() + " " + message);
	}
	
	
	/*
	 *  IndexerListener methods
	 * 
	 */
	
	@Override
    public void indexingStarted()
    {
		File sourceName = indexingService.getCurrentTask();
		importerModel.setStatus(sourceName, ImportStatus.Started);
		indexingInProgress = true;
    }
    
	@Override
    public void indexingComplete()
    {
		File sourceName = indexingService.getCurrentTask();
		if(sourceName!=null)
			importerModel.setStatus(sourceName, ImportStatus.Indexed);
		
		indexingInProgress = false;
    }

	@Override
	public void indexed(IRecord record) { }
	
	@Override
	public void indexingFailed(boolean serverError) 
	{
		File sourceName = indexingService.getCurrentTask();
		if(sourceName!=null)
			importerModel.setStatus(sourceName, ImportStatus.Failed);
		
		indexingInProgress = false;
		
		if(serverError)
		{
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					login(true);
				}
			});
		}
	}
	
	@Override
	public void ignoredDuplicate(IExperiment experiment)
	{
		File sourceName = indexingService.getCurrentTask();
		importerModel.setStatus(sourceName, ImportStatus.Duplicate);
	}
	
	@Override
	public void selectionChanged(RecordSelectionEvent selectionEvent) {
		List <IRecord> selectedRecords = selectionEvent.getRecords();
		if(selectedRecords.size()>0) {
			imagePanel.setRecord(selectedRecords.get(0));
//			RecordHolder.setSelectedRecord(selectedRecords.get(0));
		}
		else {
			imagePanel.setRecord(null);
//			RecordHolder.setSelectedRecord(null);
		}
	}
	
	private static void setSystemProperties(String host, String port, String protocol)
	{
		if(System.getProperty("iManageServerIP")==null)
			System.setProperty("iManageServerIP", host);
		
		if(System.getProperty("iManageServerPort")==null)
			System.setProperty("iManageServerPort", port);
		
		if(System.getProperty("helpFilePath")==null)
			System.setProperty("helpFilePath", protocol+"://" + System.getProperty("iManageServerIP") + ":" + System.getProperty("iManageServerPort") + "/iManage/help.html");
		
//		System.setProperty("sun.rmi.transport.tcp.responseTimeout", "1");
//		System.setProperty("sun.rmi.transport.tcp.readTimeout", "1");
//		System.setProperty("sun.rmi.transport.connectionTimeout", "1");
		
		ConnectionPreferences pref = ConnectionPreferences.getInstance();
		
		if(pref.getHostAddress() == null)
			pref.setServerAddress(System.getProperty("iManageServerIP"));
		if(pref.getHostPort() == null)
			pref.setServerPort(Integer.parseInt(System.getProperty("iManageServerPort")));
	}
	
	private static void launchAcqClient(final String host, final int port,final String clientID,final String authCode, final String version, final String protocol)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				boolean isSSL = "https".equals(protocol);
				if(!ImageSpaceObject.getImageSpace().login(isSSL, host, port, clientID, authCode))
				{
					JOptionPane.showMessageDialog(null, "Application not authorized", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				String user = ImageSpaceObject.getImageSpace().getUser();
				
				System.out.println("Successfully logged in as "+user);
				
				if (!ImageSpaceObject.getImageSpace().requestAcquisitionLicense(user))
				{
					JOptionPane.showMessageDialog(null, "Number of Acquisition Licenses are exhausted. Kindly contact administrator or try again later.", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				AcquisitionUI acquisitionUI = new AcquisitionUI(user, version);
				acquisitionUI.launch();
			
				Project project = acquisitionUI.selectProject();
				if(project == null)
					System.exit(0);
				acquisitionUI.setUpSession(project);
				
				// check if service is running
				Context.getInstance().getServiceStub();
			}
		});
	}
	
	public static void main(String... args) {
		
		if(args==null || args.length<3)
		{
			JOptionPane.showMessageDialog(null, "Application not authorized", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		//ACQ-CLIENT-ID
		String clientID = "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI";
		String authCode = args[0];
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		String protocol = args[4];
		
		String version = args[3];
		
		//jide license
		com.jidesoft.utils.Lm.verifyLicense("Strand Life Science", "avadis", "BDhYK:qTCkBmgfpIFrqclqSmzUdXtXh2");
		
		setSystemProperties(host, String.valueOf(port), protocol);
		launchAcqClient(host, port, clientID, authCode,version, protocol);
	}

	@Override
	public void experimentFound(List<RawExperiment> experimentList)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void experimentsAdded(List<RawExperiment> experimentList)
	{
		if(experimentList==null || experimentList.size()<0)
			return;
		
		
		List<IExperiment> toUpload = new ArrayList<IExperiment>();
		for(RawExperiment exp: experimentList)
		{
			toUpload.add(exp);
		}
		
		directUploadTableModel.addExperiment(experimentList);
		
		Boolean bkgUpload = AcqclientUIUtils.getCheckBoxValue();
		if(bkgUpload)
		{
			logger.info("Uploading using background service");
			directUploadInBackground(toUpload);
		}
		else
		{
			logger.info("Uploading using acquisition client");
			directUpload(toUpload);
		}
		
	}
}
