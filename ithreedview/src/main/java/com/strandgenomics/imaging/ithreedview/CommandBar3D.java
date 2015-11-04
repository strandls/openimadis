package com.strandgenomics.imaging.ithreedview;

import ij.ImageStack;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingWorker;

import com.jidesoft.action.CommandBar;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;
import com.strandgenomics.imaging.ithreedview.slicing.SliceConfigurer;
import com.strandgenomics.imaging.ithreedview.slicing.SliceDisplay;
import com.strandgenomics.imaging.ithreedview.utils.UIUtils;

public class CommandBar3D extends CommandBar {

	private JButton start;
	private JButton stop;
	
	private final static String startIconEnabled = "PlayNormal.png";
	private final static String startIconDisabled = "PlayDisabled.png";
	private final static String stopIconEnabled = "StopNormal.png";
	private final static String stopIconDisabled = "StopDisabled.png";
	private final static String startRotation = "start_rotation.png";
	private final static String stopRotation = "stop_rotation.png";
	private final static String zoomInIcon = "zoomin.png";
	private final static String zoomOutIcon = "zoomout.png";
	
	private Canvas s;
	private SliceDisplay slicePane;
	private RotationController rotator;
	private ContentController content;
	private ZoomController zoom;

	private boolean playing = true;

	private JideToggleButton coordinates;
	private JideButton exportImage;
	private JideToggleButton startVideoCapture;
	private JideToggleButton stopVideoCapture;

	private JideButton sliceVolume;
	private JProgressBar progressBar;

	private int maxFrames;
	private int frameWidth;
	private int frameHeight;
	private int maxFramesDef = 500;
	private int frameWidthDef = 500;
	private int frameHeightDef = 500;

	private int slicePlane;
	private float sliceAmount;

	private int slicePlaneDef = 1;
	private float sliceAmountDef = 1.0f;

	JFrame axisDialog;

	private JideButton zoomIn;
	private JideButton zoomOut;
	private float zoomStep = 0.5f;

	private JideButton brightnessUp;
	private JideButton brightnessDown;
	private int brightnessStep = 1;

	private JideButton resetButton;

	private JRootPane rootPane;
	private MenuBar3D menuBar;
	public SliceConfigurer.SlicePlane currSlicePlane = SliceConfigurer.SlicePlane.YZPLANE;
	public float currSlicePercent;

	public CommandBar3D() {

		// boolean value = (Boolean)
		// ConfigurationManager.getValue(PRAM_3D_CONF_ID, "COORDINATES");
		coordinates = UIUtils.createCommandBarToggleButton("3D-axes.png",
				"3D-axes.png", "Show/Hide axis", "Show/Hide axis", true);

		exportImage = UIUtils.createCommandBarButton("snapshot.png", "Export 3D Snapshot");

		sliceVolume =  UIUtils.createCommandBarButton("slice.png", "Volume Slicing");

		startVideoCapture = UIUtils.createCommandBarToggleButton(startIconEnabled,
				startIconEnabled, "Start Video Capture", "Start Video Capture", true);

		stopVideoCapture = UIUtils.createCommandBarToggleButton(stopIconEnabled,
				stopIconEnabled, "Stop Video Capture", "Stop Video Capture", true);

		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(32, 16));

		maxFrames = maxFramesDef;
		frameWidth = frameWidthDef;
		frameHeight = frameHeightDef;

		sliceAmount = sliceAmountDef;
		slicePlane = slicePlaneDef;

		start = UIUtils.createCommandBarButton(stopRotation, "Start/Stop Rotation");
		start.setEnabled(true);

		resetButton = UIUtils.createCommandBarButton("reset.gif", "Reset View");

		zoomIn = UIUtils.createCommandBarButton(zoomInIcon, "Zoom In");
		zoomOut = UIUtils.createCommandBarButton(zoomOutIcon, "Zoom Out");

		brightnessUp = UIUtils.createCommandBarButton("increase_brightness.gif", "Increase Brightness");
		brightnessDown = UIUtils.createCommandBarButton("decrease_brightness.gif", "Decrease Brightness");

		add(coordinates);

		add(zoomIn);
		add(zoomOut);
		add(start);
		add(brightnessUp);
		add(brightnessDown);

		// add(sliceVolume);

		add(exportImage);

		add(startVideoCapture);
		add(progressBar);
		add(stopVideoCapture);

		add(resetButton);

		addListeners();
	}

	public void addListeners() {

		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float currZoom = s.getCurrentZoom();
				float maxZoom = zoom.getMaxZoom();
				if (currZoom < maxZoom) {
					if (currZoom + zoomStep > maxZoom)
						s.setZoomParameter(maxZoom);
					else
						s.setZoomParameter(currZoom + zoomStep);
				}
			}
		});

		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float currZoom = s.getCurrentZoom();

				float minZoom = zoom.getMinZoom();

				if (currZoom > minZoom) {
					if (currZoom - zoomStep > minZoom)
						s.setZoomParameter(currZoom - zoomStep);
					else
						s.setZoomParameter(minZoom);
				}
			}
		});
		brightnessUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currBrightness = s.getCurrentBrightness();
				int maxbrightness = 10; // where everything above 0 gets set to
										// 255
				if (currBrightness + 1 > maxbrightness) {
					s.setBrightness(maxbrightness, rootPane);
					 slicePane.setBrightness(maxbrightness);
				} else {
					s.setBrightness(currBrightness + brightnessStep, rootPane);
					 slicePane.setBrightness(currBrightness+brightnessStep);
				}
			}
		});
		brightnessDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currBrightness = s.getCurrentBrightness();
				int minbrightness = 0; // where everything above 0 gets set to
										// 255
				if (currBrightness - brightnessStep < minbrightness) {
					s.setBrightness(minbrightness, rootPane);
					slicePane.setBrightness(minbrightness);
				} else {
					s.setBrightness(currBrightness - brightnessStep, rootPane);
					slicePane.setBrightness(currBrightness - brightnessStep);
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.resetView(rootPane);

				// slicePane.setBrightness(0);
				slicePane.reset();
				menuBar.selectRotationAxis(Canvas.AxisSet.YAXIS, 2);
				menuBar.setSpeedMenuItem(0);
				menuBar.setSelectedThresholdIndex(0);
				menuBar.setSelectedAspectIndex(0);
				coordinates.setSelected(false);
			}
		});

		coordinates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (coordinates.isSelected())
					content.addCoordinates();
				else
					content.removeCoordinates();
			}
		});

		sliceVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//				 configureSliceGeneration();

			}
		});

		exportImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				s.getCanvas().stopRenderer();
				boolean rotationOn = s.getRotationController().isRotationOn();
				if (rotationOn) {
					s.getRotationController().pauseRotation();
				}
				ImageCapture t = new ImageCapture(rootPane);
				t.run();
//				s.captureFrame(rootPane);

				if (rotationOn) {
					s.getRotationController().startRotation();
				}
			}
		});

		startVideoCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = configureVideoCapture();

				if (retVal) {
					progressBar.setIndeterminate(true);
					progressBar.setString("Capturing Video ...");
					// if we want to set a different capture limit, change the
					// argument to this function
					s.startCapture(maxFrames, frameWidth, frameHeight);
					startVideoCapture.setEnabled(false);
					startVideoCapture.setIcon(UIUtils.getIcon(startIconDisabled));
					stopVideoCapture.setEnabled(true);
					stopVideoCapture.setIcon(UIUtils.getIcon(stopIconEnabled));
					firePropertyChange("startCapture", false, true);
				}
			}
		});

		stopVideoCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ImageStack imgStack = null;
				try {
					imgStack = s.stopCapture();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				progressBar.setString(null);
				progressBar.setIndeterminate(false);

				startVideoCapture.setEnabled(true);
				startVideoCapture.setIcon(UIUtils.getIcon(startIconEnabled));
				stopVideoCapture.setEnabled(false);
				stopVideoCapture.setIcon(UIUtils.getIcon(stopIconDisabled));

				JFileChooser saveDialog = new JFileChooser();
//				List<String> filters = Arrays.asList("avi");
//				saveDialog.setFileFilter(filter);
				saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);

				saveDialog.setVisible(true);
				int retVal = saveDialog.showSaveDialog(rootPane);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					File f = saveDialog.getSelectedFile();
					System.out.println("File chosen : " + f.getPath());
					if (imgStack != null)
						s.saveVideo(f.getPath(), imgStack);

					String message = "Video Exported To :: " + f.getPath();
					JOptionPane.showMessageDialog(rootPane, message,
							"Video Save Success",
							JOptionPane.INFORMATION_MESSAGE);

				} else {
					System.out.println("Video capture cancelled ");
					s.cancelSave();

				}
				firePropertyChange("startCapture", true, false);
			}
		});

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!rotator.isRotationOn()) {
					rotator.startRotation();
					start.setIcon(UIUtils.getIcon(stopRotation));
					start.setEnabled(true);
					menuBar.rotationStarted();
				} else {
					rotator.pauseRotation();
					start.setIcon(UIUtils.getIcon(startRotation));
					start.setEnabled(true);
					menuBar.rotationPaused();
				}
			}
		});
	}

	public ImageIcon resizeIcon(int targetWidth, int targetHeight,
			ImageIcon inputIcon) {
		Image _image = inputIcon.getImage();
		int width = _image.getWidth(null) < targetWidth ? _image.getWidth(null)
				: targetWidth;
		int height = _image.getHeight(null) < targetHeight ? _image
				.getHeight(null) : targetHeight;
		// Image _scaledImage =
		// _image.getScaledInstance((int)(_image.getWidth(null)*scale),
		// (int)(_image.getHeight(null)*scale), Image.SCALE_FAST);
		Image _scaledImage = _image.getScaledInstance(width, height,
				Image.SCALE_FAST);
		return new ImageIcon(_scaledImage);
	}

	public void setCanvas(Canvas s) {
		this.s = s;
		this.rotator = s.getRotationController();
		this.content = s.getContentController();
		this.zoom = s.getZoomController();
	}

	public void clickStopRecord(String message, String title) {
		JOptionPane.showMessageDialog(rootPane, message, title,
				JOptionPane.WARNING_MESSAGE);
		stopVideoCapture.doClick();
	}

	public void clickStopRecord() {
		stopVideoCapture.doClick();
	}

	public void clickStartRecord() {
		startVideoCapture.doClick();
	}

	public void clickExportImage() {
		exportImage.doClick();
	}

	public void clickRotate() {
		start.doClick();
	}

	public void clickZoomDown() {
		zoomOut.doClick();
	}

	public void clickZoomUp() {
		zoomIn.doClick();
	}

	public void clickBrightnessUp() {
		brightnessUp.doClick();
	}

	public void clickBrightnessDown() {
		brightnessDown.doClick();
	}

	public void clickReset() {
		resetButton.doClick();
	}

	public void clickCoordinates() {
		coordinates.doClick();
	}

	public boolean configureVideoCapture() {
//		boolean retVal = false;
//		Map dialogProperties = new HashMap();
//		dialogProperties.put("title", "Configure Video Capture");
//		dialogProperties.put("hasCancel", true);
//		SimpleDialog configureVideoCaptureDlg = new SimpleDialog(rootPane,
//				dialogProperties);
//
//		Map maxFramesMap = new HashMap();
//		maxFramesMap.put("id", "maxFrames");
//		maxFramesMap.put("description", "Max Frames");
//		maxFramesMap.put("type", "int");
//		maxFramesMap.put("value", maxFrames);
//		IComponent c1 = OmegaFactory.createComponent(maxFramesMap);
//
//		// frameWidth = content.getXDimension();
//		Map frameWidthMap = new HashMap();
//		frameWidthMap.put("id", "frameWidth");
//		frameWidthMap.put("description", "Frame Width");
//		frameWidthMap.put("type", "int");
//		frameWidthMap.put("value", frameWidth);
//		IComponent c2 = OmegaFactory.createComponent(frameWidthMap);
//
//		// frameHeight = content.getYDimension();
//		Map frameHeightMap = new HashMap();
//		frameHeightMap.put("id", "frameHeight");
//		frameHeightMap.put("description", "Frame Height");
//		frameHeightMap.put("type", "int");
//		frameHeightMap.put("value", frameHeight);
//		IComponent c3 = OmegaFactory.createComponent(frameHeightMap);
//
//		Map grpMap = new HashMap();
//		grpMap.put("id", "grp");
//		grpMap.put("description", "");
//		grpMap.put("type", "group");
//		grpMap.put("components", Arrays.asList(c1, c2, c3));
//		IComponent grp = OmegaFactory.createComponent(grpMap);
//		configureVideoCaptureDlg.setContent(grp.getComponent());
//
//		configureVideoCaptureDlg.setPreferredSize(new Dimension(400, 200));
//		configureVideoCaptureDlg.pack();
//		configureVideoCaptureDlg.setVisible(true);
//		if (configureVideoCaptureDlg.isOk()) {
//			retVal = true;
//			try {
//				if (c1.getValue() != null && c2.getValue() != null
//						&& c3.getValue() != null) {
//					maxFrames = (Integer) c1.getValue();
//					frameWidth = (Integer) c2.getValue();
//					frameHeight = (Integer) c3.getValue();
//				}
//			} catch (AlgorithmException ex) {
//				// TBD : display a dialog if inputs are not correct.
//				ex.printStackTrace();
//
//			}
//		}
//
//		firePropertyChange("setVisible", false, true);
//		return retVal;
		return true;
	}

	public void setRootPane(JRootPane d) {
		this.rootPane = d;
	}

	public void setMenuBar(MenuBar3D menu) {
		this.menuBar = menu;
	}

	public void clearup() {

		s = null;
		rotator = null;
		content = null;
		zoom = null;

		coordinates = null;
		exportImage = null;
		startVideoCapture = null;
		stopVideoCapture = null;

		sliceVolume = null;
		progressBar = null;

		axisDialog = null;

		zoomIn = null;
		zoomOut = null;

		resetButton = null;
		rootPane = null;

	}

	public void saveFrame() {
//		OFileChooser saveDialog = new OFileChooser();
//		List<String> filters = Arrays.asList("png", "jpeg", "jpg");
//		saveDialog.addFilter(filters);
//		saveDialog.setDialogType(OFileChooser.SAVE_DIALOG);
//		saveDialog.setVisible(true);
//		int retVal = saveDialog.showSaveDialog(rootPane);
//		if (retVal == OFileChooser.APPROVE_OPTION) {
//			File f = saveDialog.getSelectedFile();
//			System.out.println("File chosen : " + f.getPath());
//			String formatName = saveDialog.getFileFilter().getDescription()
//					.split("\\s+")[0];
//
//			ImageSave t = new ImageSave(rootPane, f, formatName);
//			t.run();
//		} else {
//
//			System.out.println("Image capture cancelled");
//			s.cancelSave();
//
//		}
//		firePropertyChange("setVisible", false, true);
	}

	private class ImageCapture extends SwingWorker {

		public ImageCapture(JRootPane frame) {
		}

		@Override
		protected Object doInBackground() throws Exception {
			s.captureFrame(rootPane);
			return null;
		}
	}

//	private class ImageSave extends UIWorker {
//		File file;
//		String formatName;
//
//		public ImageSave(JDialog frame, File file, String formatName) {
//			super(frame, false, true); // parent, isIndeterminate, disableCancel
//			this.file = file;
//			this.formatName = formatName;
//		}
//
//		public void task() {
//			progressDialog.setTitle("");
//			progressModel.setMessage("Please wait .. saving in progress");
//			progressModel.setProgressValue(0);
//			if (progressModel.canContinue()) {
//
//				s.saveFrame(file, formatName);
//
//				String message = "Image Exported To :: " + file.getPath();
//				JOptionPane.showMessageDialog(rootPane, message,
//						"Image Save Success", JOptionPane.INFORMATION_MESSAGE);
//
//				progressModel.setProgressValue(100);
//			}
//		}
//	}

	public void setSlicePane(SliceDisplay slicePane) {
		this.slicePane = slicePane;
	}

}
