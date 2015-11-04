package com.strandgenomics.imaging.ithreedview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.jidesoft.action.CommandBar;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;
import com.strandgenomics.imaging.ithreedview.listeners.IImageCreatedListener;
import com.strandgenomics.imaging.ithreedview.listeners.ImageCreatedEvent;
import com.strandgenomics.imaging.ithreedview.slicing.SliceConfigurer;
import com.strandgenomics.imaging.ithreedview.slicing.SliceDisplay;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Launcher extends JDialog implements PropertyChangeListener,
		IImageCreatedListener {

	private static Launcher launcher;
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

//	private RecordHolder recordHolder;

	// /{{{ getInstance
	public static Launcher getInstance() {
		if (launcher == null) {
			launcher = new Launcher();
		}
		return launcher;
	}

	// /}}}

	// /{{{constructor
	private Launcher() {
		super();
		setupLauncher();
	}

	// /}}}

	// /{{{setupLauncher
	private void setupLauncher() {
		this.setModal(false);
		addWindowListener();
		graphicsConfiguration = SimpleUniverse.getPreferredConfiguration();
		topLeftPoint = new Point(50, 50);
		xSize = 700;
		ySize = 600;
		// setIconImage(IconManager.getIcon("imaging.indexer:icons/largeicon.png").getImage());
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
        
//		this.recordHolder = recordHolder;
		MyWorker worker = new MyWorker(this);
		worker.execute();

		progressDialog.setVisible(true);
		 
//		this.initialize();

		DynamicMenuUpdater.getInstance().threeDOpened();
		if (launcher != null) {
			this.setVisible(true);
			this.setSize(xSize, ySize);
			this.setLocation(topLeftPoint);
		}
	}

	// /}}}
	
	public void setValue(long recordID, int site, int frame, float scale, List<IChannel> channels)
	{
		
	}

	// /{{{addWindowListener
	private void addWindowListener() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (dataInitialized) {
					saveConfiguration();
				}
				DynamicMenuUpdater.getInstance().threeDClosed();
				CleanupWorker cleanupWorker = new CleanupWorker();
				cleanupWorker.run();
			}

			public void windowActivated(WindowEvent e) {
				repaintAll();
				DynamicMenuUpdater.getInstance().threeDActivated();
			}
		});
	}

	// /}}}

	public void repaintAll() {
		// System.out.println("Repainting frame");
		jContentPane.repaint();
	}

	// /{{{saveConfiguration
	private void saveConfiguration() {
		// if(rotator!=null && content!=null){
		// ConfigurationManager.setValue(PRAM_3D_CONF_ID, "ROTATION_VALUE",
		// rotator.isRotationOn());
		// ConfigurationManager.setValue(PRAM_3D_CONF_ID, "COORDINATES",
		// content.coordinatePresent());
		//
		// int v = rotator.getRotationSpeed();
		// int value;
		// if (v == 7000) {
		// value = 0;
		// } else if (v == 4000) {
		// value = 1;
		// } else {
		// value = 2;
		// }
		//
		// ConfigurationManager.setValue(PRAM_3D_CONF_ID, "ROTATION_SPEED",
		// value);
		// ConfigurationManager.setValue(PRAM_3D_CONF_ID, "VOLUMETHRESHOLD",
		// content.get3DVolume().getCurrentThreshold());
		// ConfigurationManager.setValue(PRAM_3D_CONF_ID, "BRIGHTNESSTHRESHOLD",
		// content.get3DVolume().getCurrentBrightness());
		//
		// float deltascale = content.get3DVolume().getCurrentScale()
		// / content.getVolumeData().getAutoScale();
		// ConfigurationManager.setValue(PRAM_3D_CONF_ID, "ZASPECT",
		// deltascale);
		//
		// ConfigurationManager.save(PRAM_3D_CONF_ID);}
	}

	// /}}}

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
//				progressModel.setMessage("Removing 3D View...");
				saveConfiguration();
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
//			progressModel.setMessage("Launching 3D View...");
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
			this.setTitle(" 3D view");
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
		// boolean value = (Boolean) ConfigurationManager.getValue(
		// PRAM_3D_CONF_ID, "COORDINATES");
		// if (value) {
		content.addCoordinates();
		// } else {
		// content.removeCoordinates();
		// }
		//
		// value = (Boolean) ConfigurationManager.getValue(PRAM_3D_CONF_ID,
		// "ROTATION_VALUE");
		// if (value) {
		rotator.startRotation();
		// } else {
		// rotator.pauseRotation();
		// }
		//
		// int speed = (Integer) ConfigurationManager.getValue(PRAM_3D_CONF_ID,
		// "ROTATION_SPEED");
		// if (speed == 0) {
		// rotator.setRotationSpeed(7000);
		// } else if (speed == 1) {
		rotator.setRotationSpeed(4000);
		// } else {
		// rotator.setRotationSpeed(1000);
		//
		// }
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

				this.setAlwaysOnTop(true);
				this.setModal(true);
				this.setVisible(false); // these two lines are needed to effect
				// the modality change
				this.setVisible(true);

				this.setSize(xSize, ySize);
				this.setLocation(topLeftPoint);
			} else {

				xSize = this.getWidth();
				ySize = this.getHeight();
				topLeftPoint = this.getLocationOnScreen();

				this.setAlwaysOnTop(false);
				this.setModal(false);

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
//				s.setMaxDimension(newDimension, recordId);
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

				float autoScale = content.getAutoScale(record, site, frame, channels);

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
				/*
				 * if(!dataDepthSupported) { return null; }
				 */

				this.pack();

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

		Launcher instance;

		public MyWorker(Launcher instance) {
//			super(frame, false, false); // parent, isIndeterminate,
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

//		public void task() {
//			progressDialog.setTitle("");
//			progressModel.setMessage("");
//			progressModel.setProgressValue(0);
//			if (progressModel.canContinue()) {
//				instance.initialize(progressModel);
//			}
//		}
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

}
