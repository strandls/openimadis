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

package com.strandgenomics.imaging.ithreedview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.jidesoft.action.CommandBar;
import com.strandgenomics.imaging.iclient.ImageSpaceObject;
import com.strandgenomics.imaging.iclient.dialogs.ProgressDialog;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;
import com.strandgenomics.imaging.ithreedview.listeners.IImageCreatedListener;
import com.strandgenomics.imaging.ithreedview.listeners.ImageCreatedEvent;
import com.strandgenomics.imaging.ithreedview.slicing.SliceConfigurer;
import com.strandgenomics.imaging.ithreedview.slicing.SliceDisplay;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class LauncherApplet extends JApplet implements PropertyChangeListener,
		IImageCreatedListener {

	private static LauncherApplet launcher;
	private SliceDisplay slicePanel = null;

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private Canvas s;
	private boolean dataInitialized = false;

	private IRecord record;
	private int site;
	private int frame;
	private float scale;
	private List<IChannel> channels;

	private CommandBar3D controls = null;
	private MenuBar3D menu = null;

	private GraphicsConfiguration graphicsConfiguration;

	private RotationController rotator = null;
	private ContentController content = null;

	private Point topLeftPoint;
	private int xSize;
	private int ySize;
	private ProgressDialog progressDialog;

	// /{{{ getInstance
	public static LauncherApplet getInstance() {
		if (launcher == null) {
			launcher = new LauncherApplet();
		}
		return launcher;
	}

	// /}}}

	// /{{{constructor
	public void init() {
		 //Execute a job on the event-dispatching thread; creating this applet's GUI.
		com.jidesoft.utils.Lm.verifyLicense("Strand Life Science", "avadis",
		"BDhYK:qTCkBmgfpIFrqclqSmzUdXtXh2");

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    try {
                    	long guid = Long.parseLong(getURLParameter("guid"));
                    	int site = Integer.parseInt(getURLParameter("site"));
                    	int frame = Integer.parseInt(getURLParameter("frame"));
                    	String authcode = getURLParameter("authcode");
                    	String host = getCodeBase().getHost();
                		int port = getCodeBase().getPort();
                		String clientID = "onW7Eczizs3VdSCPIVkVG9Um5FEIiibKse5YodqI";
//                    	authenticateUser(user);
                		System.out.println(host+" "+port);
                		if(port < 0)
                			port = 8080;
                    	authenticateApplication(host, port, clientID, authcode);
                    	
                    	System.out.println(guid+" "+site+" "+frame);
						setupLauncher();
						
						setValue(guid, site, frame);
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
	}
	
	protected void authenticateApplication(String host, int port, String clientID, String authCode)
	{
		if(!ImageSpaceObject.getImageSpace().login(false, host, port, clientID, authCode))
		{
			JOptionPane.showMessageDialog(null, "Application not authorized", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public String getURLParameter(String name){
		String url = getDocumentBase().toString();
		
		String paramaters = url.substring(url.indexOf("?") + 1);
		String params[] = paramaters.split("&");
		for(int i=0;i<params.length;i++){
			String pair[] = params[i].split("=");
			if(pair[0].equals(name))
				return pair[1];
		}
		return "";
	}
	
	// /{{{setupLauncher
	private void setupLauncher() throws URISyntaxException {
		addWindowListener();
		graphicsConfiguration = SimpleUniverse.getPreferredConfiguration();
		topLeftPoint = new Point(0, 0);
		xSize = 700;
		ySize = 600;
	}
	
	public void setValue(long guid, int site, int frame){
		IRecord remoteRecord = ImageSpaceObject.getImageSpace().findRecordForGUID(guid);
		List<IChannel>channels = new ArrayList<IChannel>();
		for(int i=0;i<remoteRecord.getChannelCount();i++){
			channels.add(remoteRecord.getChannel(i));
		}
		setValue(remoteRecord, site, frame, 1.0f, channels);
	}

	public void setValue(IRecord record, int site, int frame, float scale, List<IChannel> channels) 
	{
		this.record = record;
		this.site = site;
		this.frame = frame;
		this.channels = channels;
		this.scale = scale;

		progressDialog = new ProgressDialog(new JFrame(), "Progress");
		progressDialog.setMessage("Loading data for 3D");

		// this.recordHolder = recordHolder;
		MyWorker worker = new MyWorker(this);
		worker.execute();

		progressDialog.setVisible(true);

		// this.initialize();

		DynamicMenuUpdater.getInstance().threeDOpened();
		if (launcher != null) {
			this.setVisible(true);
			this.setSize(xSize, ySize);
			this.setLocation(topLeftPoint);
		}
	}
	// /}}}

	// /{{{addWindowListener
	private void addWindowListener() {
	}

	// /}}}

	public void repaintAll() {
		// System.out.println("Repainting frame");
		jContentPane.repaint();
	}

	// /{{{ initialize
	public void initialize() {
		dataInitialized = false;
		if (s != null) {
			xSize = this.getWidth();
			ySize = this.getHeight();
			topLeftPoint = this.getLocationOnScreen();
		}
		this.setVisible(false);
		this.setSize(xSize, ySize);
		this.setLocation(topLeftPoint);

		// If view is getting updated, save the configuration values and clear
		// old data
		if (s != null) {
			if (true) {
				// progressModel.setMessage("Removing 3D View...");
				s.removePropertyChangeListener(this);
				content.removeImageCreatedListener(this);
				s.clearData();
				this.jContentPane = null;
			} else {
				cleanupLauncher();
				return;
			}
		}

		if (true) {
			// progressModel.setMessage("Launching 3D View...");
			JPanel contentPane = getJContentPane(scale);
			if (contentPane != null) {
				this.setContentPane(getJContentPane(scale));
			} else {
				// cleanupLauncher();
				return;
			}
		} else {
			cleanupLauncher();
			return;
		}

		if (true) {
			this.loadConfiguration();
		} else {
			cleanupLauncher();
			return;
		}

		if (true) {
			menu = new MenuBar3D(s, scale, getRootPane());
			this.setJMenuBar(menu.getJMenuBar());
			this.add(createControlsCommandBar(), BorderLayout.NORTH);
			menu.setCommandBar(this.controls);
			menu.setSlicePane(this.slicePanel);
		} else {
			cleanupLauncher();
			return;
		}

		if (true) {
			s.addPropertyChangeListener(this);
//			this.setTitle(" 3D view");
		} else {
			cleanupLauncher();
			return;
		}
		dataInitialized = true;
	}

	// /}}}

	public void cleanup() {
		if (content != null) {

			content.removeImageCreatedListener(launcher);
		}
		if (s != null) {
			s.removePropertyChangeListener(launcher);
			s.cleanup();
		}

		channels = null;
		if (controls != null) {
			controls.removePropertyChangeListener(launcher);
			controls.clearup();
			controls = null;
		}
		launcher = null;
		graphicsConfiguration = null;
		System.gc();
	}

	// /{{{cleanupLauncher
	public void cleanupLauncher() {
		// saveConfiguration();
		cleanup();
	}

	// /}}}

	// /{{{loadConfiguration
	private void loadConfiguration() {
		content.addCoordinates();
		rotator.startRotation();
		rotator.setRotationSpeed(4000);
	}

	// /}}}

	// /{{{createCommandBar
	private CommandBar createControlsCommandBar() {
		controls = new CommandBar3D();
		controls.setRootPane(getRootPane());
		controls.setMenuBar(menu);
		controls.setCanvas(s);
		controls.setSlicePane(slicePanel);
		controls.addPropertyChangeListener(this);
		return controls;
	}

	// /}}}

	// /{{{handleprogressbarupdates
	public void imageCreated(ImageCreatedEvent evt) {
		if (true) {
			float percent = (float) evt.getCurrImageIndex()
					/ (float) evt.getTotalImageCount();
			int percentage = (int) (percent * 100.0);
		} else {
			// cannot call cleanupLauncher here because it will hamper the
			// initialize thread.
			content.removeImageCreatedListener(this);
			// cleanupLauncher();
		}
	}

	// /}}}

	// /{{{propertyChangelisteners
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("startCapture")) {
			boolean startCapture = (Boolean) evt.getNewValue();
			if (startCapture) {

				xSize = this.getWidth();
				ySize = this.getHeight();
				topLeftPoint = this.getLocationOnScreen();

				this.setVisible(false); // these two lines are needed to effect
				// the modality change
				this.setVisible(true);

				this.setSize(xSize, ySize);
				this.setLocation(topLeftPoint);
			} else {

				xSize = this.getWidth();
				ySize = this.getHeight();
				topLeftPoint = this.getLocationOnScreen();

				this.setVisible(false); // needed for modality changes to take
				// effect

				this.setVisible(true);

				this.setSize(xSize, ySize);
				this.setLocation(topLeftPoint);
			}
		} else if (evt.getPropertyName().equals("setVisible")) {
			boolean setVisible = (Boolean) evt.getNewValue();
			if (setVisible) {
				this.setVisible(true);

				this.setSize(xSize, ySize);
				this.setLocation(topLeftPoint);
			}
		} else if (evt.getPropertyName().equals("frameLimitReached")) {
			boolean frameLimitReached = (Boolean) evt.getNewValue();
			if (frameLimitReached) {
				controls.clickStopRecord("Maximum Recording Limit Reached",
						"Limit Reached");
			}
		} else if (evt.getPropertyName().equals("recordingTerminated")) {
			boolean recordingTerminated = (Boolean) evt.getNewValue();
			if (recordingTerminated) {
				controls.clickStopRecord("Recording Has Been Terminated",
						"Terminated");
			}
		} else if (evt.getPropertyName().equals("memoryLimitReached")) {
			boolean memoryLimitReached = (Boolean) evt.getNewValue();
			if (memoryLimitReached) {
				controls.clickStopRecord(
						"Memory Limit Reached. Recording Will Be Stopped",
						"Limit Reached");
			}
		} else if (evt.getPropertyName().equals("imageCaptureDone")) {
			boolean imageCaptureDone = (Boolean) evt.getNewValue();
			if (imageCaptureDone) {
				controls.saveFrame();
			}
		} else if (evt.getPropertyName().equals("SliceVolume")) {
			s.getContentController().sliceVolume(slicePanel.getCurrentView());
		} else if (evt.getPropertyName().equals("ResetVolume")) {
			s.getContentController().resetVolume();
		} else if (evt.getPropertyName().equals("SliceAll")) {
			s.getContentController().sliceAll(slicePanel.getYZValue(),
					slicePanel.getXZValue(), slicePanel.getXYValue(),
					slicePanel.getSliceAllView());
		} else if (evt.getPropertyName().equals("YZUpdate")) {
			s.getContentController().displaySlicePlane(
					SliceConfigurer.SlicePlane.YZPLANE,
					slicePanel.getYZValue(), 1.0f - slicePanel.getXZValue(),
					slicePanel.getXYValue());
		} else if (evt.getPropertyName().equals("XZUpdate")) {
			s.getContentController().displaySlicePlane(
					SliceConfigurer.SlicePlane.XZPLANE,
					slicePanel.getYZValue(), 1.0f - slicePanel.getXZValue(),
					slicePanel.getXYValue());
		} else if (evt.getPropertyName().equals("XYUpdate")) {

			s.getContentController().displaySlicePlane(
					SliceConfigurer.SlicePlane.XYPLANE,
					slicePanel.getYZValue(), 1.0f - slicePanel.getXZValue(),
					slicePanel.getXYValue());
		}

	}

	// /{{{ getjcontentpane
	private JPanel getJContentPane(float scale) {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());

			try {
				s = new Canvas(graphicsConfiguration);
				rotator = s.getRotationController();
				content = s.getContentController();

				// String value = (String) ConfigurationManager.getValue(
				// TOOL_3D_CONF_ID, "Image Size");
				String value = "1";
				int newDimension = 128;
				if (value.equals("2"))
					newDimension = 256;
				if (value.equals("3"))
					newDimension = 512;
				// s.setMaxDimension(newDimension, recordId);
				int numSlices = 8;
				s.setMaxDimension(newDimension, numSlices);

				if (content.getVolumeData().getMaxSupportedDimension() != newDimension) {
					// we were unable to alloc memory for this large a
					// dimension, revert to minimum

					// ConfigurationManager.setValue(TOOL_3D_CONF_ID,
					// "Image Size", "1");
				}

				boolean dataDepthSupported = s.setDataDepth(numSlices, content
						.getVolumeData().getMaxSupportedDimension());

				float autoScale = content.getAutoScale(record, site, frame,
						channels);

				// Launcher will be notified when images are created from
				// records
				content.addImageCreatedListener(this);
				// remember threshold and brightness

				// int brightness = (Integer) ConfigurationManager.getValue(
				// PRAM_3D_CONF_ID, "BRIGHTNESSTHRESHOLD");
				int brightness = 0;
				int threshold = 0;

				float scalenew = 1.0f;
				s.setRecordID(record, site, frame, channels, scalenew
						* autoScale, threshold, brightness);

				try {
					s.initialize();
				} catch (MalformedURLException ex) {
					ex.printStackTrace();
				}

				s.getCanvas().addMouseMotionListener(new MouseMotionListener() {

					@Override
					public void mouseDragged(MouseEvent e) {
						// TODO Auto-generated method stub
						s.setZoomParameter(s.getCurrentZoom());
					}

					@Override
					public void mouseMoved(MouseEvent e) {
						// TODO Auto-generated method stub

					}

				});
				jContentPane.add(s.getCanvas(), BorderLayout.CENTER);

				Volume3DData vol3d = s.getContentController().getVolumeData();
				ShapedTexture3D texture = s.getContentController()
						.get3DVolume();

				slicePanel = new SliceDisplay(vol3d,
						texture.getCurrentBrightness(),
						texture.getCurrentThreshold());
				slicePanel.setRootPane(getRootPane());
				JPanel slicedPanel = slicePanel.getSliceImagePanel();
				slicedPanel.addPropertyChangeListener(this);
				slicedPanel.setPreferredSize(new Dimension(200, 200));
				slicedPanel.setBorder(BorderFactory.createEtchedBorder());
				slicePanel.setDefaultControls();

				jContentPane.add(slicedPanel, BorderLayout.LINE_START);
				slicedPanel.repaint();

				JPanel controlPanel = slicePanel.getSliceControlPanel();

				jContentPane.add(controlPanel, BorderLayout.PAGE_END);

//				this.pack();

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jContentPane;
	}

	// /}}}

	// /{{{worker
	private class MyWorker extends SwingWorker {

		LauncherApplet instance;

		public MyWorker(LauncherApplet instance) {
			// super(frame, false, false); // parent, isIndeterminate,
			// disableCancel
			this.instance = instance;
		}

		@Override
		protected Object doInBackground() throws Exception {
			instance.initialize();

			progressDialog.setVisible(false);
			progressDialog.dispose();

			return null;
		}

		// public void task() {
		// progressDialog.setTitle("");
		// progressModel.setMessage("");
		// progressModel.setProgressValue(0);
		// if (progressModel.canContinue()) {
		// instance.initialize(progressModel);
		// }
		// }
	}

	//
	// // /}}}
	//
	private class CleanupWorker extends SwingWorker {
		public CleanupWorker() {
		}

		@Override
		protected Object doInBackground() throws Exception {
			cleanup();
			return null;
		}
	}

	public static void main(String... args)
	{
		LauncherApplet l = new LauncherApplet();
	}
}
