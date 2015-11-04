package com.strandgenomics.imaging.ithreedview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;

import com.strandgenomics.imaging.ithreedview.slicing.SliceConfigurer;
import com.strandgenomics.imaging.ithreedview.slicing.SliceDisplay;

//import com.strandgenomics.cube.framework.event.EventMulticaster;

public class MenuBar3D {

	Canvas s = null;
	JRootPane rootPane = null;
	SliceDisplay slicePane = null;

	JMenuBar menuBar = null;

	JMenu rotatemenu = null;
	JCheckBox rotateStart = null;
	JMenu rotateAxis = null;
	JMenuItem axissubItem1 = null;
	JMenuItem axissubItem2 = null;
	JMenuItem axissubItem3 = null;

	JMenu rotateSpeed = null;
	JMenuItem speedsubItem1 = null;
	JMenuItem speedsubItem2 = null;
	JMenuItem speedsubItem3 = null;

	JMenu thresholdMenu = null;
	JMenuItem percent0 = null;
	JMenuItem percent10 = null;
	JMenuItem percent20 = null;
	JMenuItem percent30 = null;
	JMenuItem percent40 = null;
	JMenuItem percent50 = null;
	JMenuItem percent60 = null;
	JMenuItem percent70 = null;
	JMenuItem percent80 = null;
	JMenuItem percent90 = null;
	JMenuItem percent100 = null;

	JMenu ZAspectMenu = null;
	JMenuItem aspect0 = null;
	JMenuItem aspect1 = null;
	JMenuItem aspect2 = null;
	JMenuItem aspect3 = null;
	JMenuItem aspect4 = null;
	JMenuItem aspect5 = null;
	JMenuItem aspect6 = null;
	JMenuItem aspect7 = null;
	JMenuItem aspect8 = null;
	JMenuItem aspect9 = null;
	// JMenuItem aspect10 = null0

	JMenu exportMenu = null;
	JMenuItem export3D = null;
	JMenu exportSlice = null;
	JMenuItem exportYZImage = null;
	JMenuItem exportXZImage = null;
	JMenuItem exportXYImage = null;
	JMenu exportVideo = null;
	JMenuItem startExport = null;
	JMenuItem stopExport = null;

	JMenu viewMenu = null;
	JCheckBox coordinates = null;
	JMenu zoomMenu = null;
	JMenuItem zoomIncrease = null;
	JMenuItem zoomDecrease = null;
	JMenuItem zoomDefault = null;
	JMenu brightnessMenu = null;
	JMenuItem brightnessIncrease = null;
	JMenuItem brightnessDecrease = null;
	JMenuItem brightnessDefault = null;
	JMenuItem reset = null;

	CommandBar3D controls = null;

	public MenuBar3D(Canvas s, float scale, JRootPane rootPane) {

		this.s = s;
		this.rootPane = rootPane;

		menuBar = new JMenuBar();

		exportMenu = new JMenu("Export");
		export3D = new JMenuItem("3D Snapshot");
		exportSlice = new JMenu("Slice");
		exportYZImage = new JMenuItem("YZ Slice");
		exportXZImage = new JMenuItem("XZ Slice");
		exportXYImage = new JMenuItem("XY Slice");
		exportMenu.add(export3D);
		exportMenu.add(exportSlice);
		exportSlice.add(exportYZImage);
		exportSlice.add(exportXZImage);
		exportSlice.add(exportXYImage);

		exportVideo = new JMenu("Video");
		startExport = new JMenuItem("Start Capture");
		stopExport = new JMenuItem("Stop Capture");
		exportVideo.add(startExport);
		exportVideo.add(stopExport);
		exportMenu.add(exportVideo);

		// boolean rotationValue = (Boolean)
		// ConfigurationManager.getValue(PRAM_3D_CONF_ID, "ROTATION_VALUE");
		boolean rotationValue = true;
		rotatemenu = new JMenu("Rotation");
		// rotationRadio = new JRadioButton("Rotation On/Off");
		// rotationRadio.setSelected(rotationValue);
		rotateStart = new JCheckBox("Start/Stop", rotationValue);
		rotateAxis = new JMenu("Axis");
		axissubItem1 = new JRadioButtonMenuItem("X Axis");
		axissubItem2 = new JRadioButtonMenuItem("Y Axis");
		axissubItem3 = new JRadioButtonMenuItem("Z Axis");
		axissubItem2.setSelected(true);
		axissubItem1.setSelected(false);
		axissubItem3.setSelected(false);
		// rotateAxis.setEnabled(rotationValue);
		rotateAxis.add(axissubItem1);
		rotateAxis.add(axissubItem2);
		rotateAxis.add(axissubItem3);
		rotatemenu.add(rotateStart);
		rotatemenu.add(rotateAxis);
		rotateSpeed = new JMenu("Speed");
		speedsubItem1 = new JRadioButtonMenuItem("Slow");
		speedsubItem2 = new JRadioButtonMenuItem("Medium");
		speedsubItem3 = new JRadioButtonMenuItem("Fast");

		// int rotationSpeed = (Integer)
		// ConfigurationManager.getValue(PRAM_3D_CONF_ID, "ROTATION_SPEED");
		int rotationSpeed = 1;
		setSpeedMenuItem(rotationSpeed);
		rotateSpeed.add(speedsubItem1);
		rotateSpeed.add(speedsubItem2);
		rotateSpeed.add(speedsubItem3);
		rotatemenu.add(rotateSpeed);
		rotateSpeed.setEnabled(rotationValue);

		thresholdMenu = new JMenu("Threshold");
		percent0 = new JRadioButtonMenuItem("0%");
		percent10 = new JRadioButtonMenuItem("10%");
		percent20 = new JRadioButtonMenuItem("20%");
		percent30 = new JRadioButtonMenuItem("30%");
		percent40 = new JRadioButtonMenuItem("40%");
		percent50 = new JRadioButtonMenuItem("50%");
		percent60 = new JRadioButtonMenuItem("60%");
		percent70 = new JRadioButtonMenuItem("70%");
		percent80 = new JRadioButtonMenuItem("80%");
		percent90 = new JRadioButtonMenuItem("90%");
		percent100 = new JRadioButtonMenuItem("100%");

		thresholdMenu.add(percent0);
		thresholdMenu.add(percent10);
		thresholdMenu.add(percent20);
		thresholdMenu.add(percent30);
		thresholdMenu.add(percent40);
		thresholdMenu.add(percent50);
		thresholdMenu.add(percent60);
		thresholdMenu.add(percent70);
		thresholdMenu.add(percent80);
		thresholdMenu.add(percent90);
		thresholdMenu.add(percent100);

		// setSelectedThresholdIndex(0);

		// int threshold = (Integer)
		// ConfigurationManager.getValue(PRAM_3D_CONF_ID, "VOLUMETHRESHOLD");
		int threshold = 0;

		setSelectedThresholdIndex(getThresholdIndex(threshold));

		ZAspectMenu = new JMenu("Z-Aspect");
		aspect0 = new JRadioButtonMenuItem("1.0");
		aspect1 = new JRadioButtonMenuItem("2.0");
		aspect2 = new JRadioButtonMenuItem("3.0");
		aspect3 = new JRadioButtonMenuItem("4.0");
		aspect4 = new JRadioButtonMenuItem("5.0");
		aspect5 = new JRadioButtonMenuItem("6.0");
		aspect6 = new JRadioButtonMenuItem("7.0");
		aspect7 = new JRadioButtonMenuItem("8.0");
		aspect8 = new JRadioButtonMenuItem("9.0");
		aspect9 = new JRadioButtonMenuItem("10.0");
		// aspect10 = new JRadioButtonMenuItem("3.0");

		ZAspectMenu.add(aspect0);
		ZAspectMenu.add(aspect1);
		ZAspectMenu.add(aspect2);
		ZAspectMenu.add(aspect3);
		ZAspectMenu.add(aspect4);
		ZAspectMenu.add(aspect5);
		ZAspectMenu.add(aspect6);
		ZAspectMenu.add(aspect7);
		ZAspectMenu.add(aspect8);
		ZAspectMenu.add(aspect9);
		// ZAspectMenu.add(aspect10);

		// float scalenew = (Float)
		// ConfigurationManager.getValue(PRAM_3D_CONF_ID, "ZASPECT");
		float scalenew = 1.0f;

		int scaleIndex = (int) ((scalenew - 1.0f));
		setSelectedAspectIndex(scaleIndex);

		viewMenu = new JMenu("View");

		// boolean value = (Boolean)
		// ConfigurationManager.getValue(PRAM_3D_CONF_ID, "COORDINATES");
		boolean value = true;
		coordinates = new JCheckBox("Coordinates On/Off", value);
		viewMenu.add(coordinates);
		zoomMenu = new JMenu("Zoom");
		zoomIncrease = new JMenuItem("Increase");
		zoomDecrease = new JMenuItem("Decrease");
		zoomDefault = new JMenuItem("Default");
		zoomMenu.add(zoomIncrease);
		zoomMenu.add(zoomDecrease);
		zoomMenu.add(zoomDefault);

		viewMenu.add(zoomMenu);

		brightnessMenu = new JMenu("Brightness");
		brightnessIncrease = new JMenuItem("Increase");
		brightnessDecrease = new JMenuItem("Decrease");
		brightnessDefault = new JMenuItem("Default");
		brightnessMenu.add(brightnessIncrease);
		brightnessMenu.add(brightnessDecrease);
		brightnessMenu.add(brightnessDefault);
		viewMenu.add(brightnessMenu);
		reset = new JMenuItem("Reset");
		viewMenu.add(reset);
		addListeners();

	}

	public void setCommandBar(CommandBar3D controls) {
		this.controls = controls;
	}

	public JMenuBar getJMenuBar() {
		// rotatemenu.add(rotationRadio);
		rotatemenu.add(rotateAxis);
		rotatemenu.add(rotateSpeed);
		menuBar.add(rotatemenu);

		menuBar.add(thresholdMenu);
		menuBar.add(ZAspectMenu);
		menuBar.add(exportMenu);
		menuBar.add(viewMenu);

		return menuBar;
	}

	public void rotationStarted() {
		rotateAxis.setEnabled(true);
		rotateSpeed.setEnabled(true);
	}

	public void rotationPaused() {
		// rotateAxis.setEnabled(false);
		rotateSpeed.setEnabled(false);
	}

	public void addListeners() {

		axissubItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectRotationAxis(Canvas.AxisSet.XAXIS, 1);
			}
		});
		rotateStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickRotate();
			}
		});

		axissubItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				selectRotationAxis(Canvas.AxisSet.YAXIS, 2);
			}
		});

		axissubItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectRotationAxis(Canvas.AxisSet.ZAXIS, 3);

			}
		});

		speedsubItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				(s.getRotationController()).setRotationSpeed(7000);
				setSpeedMenuItem(0);
			}
		});

		speedsubItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				(s.getRotationController()).setRotationSpeed(4000);
				setSpeedMenuItem(1);
			}
		});
		speedsubItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				(s.getRotationController()).setRotationSpeed(1000);
				setSpeedMenuItem(2);
			}
		});
		percent0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(0);
				thresholdVolume(0.0f);
			}
		});
		percent10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(1);
				thresholdVolume(0.1f);
			}
		});
		percent20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(2);
				thresholdVolume(0.2f);
			}
		});
		percent30.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(3);
				thresholdVolume(0.3f);
			}
		});
		percent40.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(4);
				thresholdVolume(0.4f);
			}
		});
		percent50.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(5);
				thresholdVolume(0.5f);
			}
		});

		percent60.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(6);
				thresholdVolume(0.6f);
			}
		});

		percent70.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(7);
				thresholdVolume(0.7f);
			}
		});
		percent80.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(8);
				thresholdVolume(0.8f);
			}
		});
		percent90.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(9);
				thresholdVolume(0.9f);
			}
		});
		percent100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSelectedThresholdIndex(10);
				thresholdVolume(1.0f);
			}
		});

		aspect0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(0);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(1.0f);
					 slicePane.resetSlicing();
				}
			}
		});
		aspect1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(1);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(2.0f);
					 slicePane.resetSlicing();
				}
			}
		});
		aspect2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(2);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(3.0f);
					 slicePane.resetSlicing();
				}
			}
		});
		aspect3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(3);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(4.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		aspect4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(4);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(5.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		aspect5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(5);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(6.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		aspect6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(6);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(7.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		aspect7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(7);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(8.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		aspect8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(8);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(9.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		aspect9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean retVal = setSelectedAspectIndex(9);
				ContentController content = s.getContentController();
				if (retVal) {
					content.setZAspect(10.0f);
				}
				 slicePane.resetSlicing();
			}
		});
		export3D.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickExportImage();
			}
		});
		exportYZImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 slicePane.saveImage(SliceConfigurer.SlicePlane.YZPLANE,
				 rootPane);
			}
		});
		exportXZImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				 slicePane.saveImage(SliceConfigurer.SlicePlane.XZPLANE,
				 rootPane);
			}
		});
		exportXYImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				 slicePane.saveImage(SliceConfigurer.SlicePlane.XYPLANE,
				 rootPane);
			}
		});

		zoomIncrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickZoomUp();
			}
		});
		zoomDecrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickZoomDown();
			}
		});
		brightnessIncrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickBrightnessUp();
			}
		});
		brightnessDecrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickBrightnessDown();
			}
		});
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickReset();
			}
		});
		brightnessDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ContentController content = s.getContentController();
				content.setBrightness(0, rootPane);

				 slicePane.setBrightness(0);
			}
		});
		zoomDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				s.resetZoom();
			}
		});

		coordinates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickCoordinates();
			}
		});
		startExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickStartRecord();
			}
		});
		stopExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controls.clickStopRecord();
			}
		});

	}

	public void selectRotationAxis(Canvas.AxisSet axis, int armedItem) {

		s.changeActiveAxis(axis);
		axissubItem1.setSelected(false);
		axissubItem2.setSelected(false);
		axissubItem3.setSelected(false);
		if (armedItem == 1)
			axissubItem1.setSelected(true);
		if (armedItem == 2)
			axissubItem2.setSelected(true);
		if (armedItem == 3)
			axissubItem3.setSelected(true);

	}

	public void setSpeedMenuItem(int selectedItem) {
		speedsubItem1.setSelected(false);
		speedsubItem2.setSelected(false);
		speedsubItem3.setSelected(false);
		if (selectedItem == 0)
			speedsubItem1.setSelected(true);
		if (selectedItem == 1)
			speedsubItem2.setSelected(true);
		if (selectedItem == 2)
			speedsubItem3.setSelected(true);

	}

	public void thresholdVolume(float amt) {
		int threshold = (new Double(255 * amt)).intValue();
		System.out.println("threshold value::" + threshold);
		s.getContentController().thresholdVolume(threshold, rootPane);
		slicePane.setThreshold(threshold);
	}

	public void setSelectedThresholdIndex(int selectedItem) {
		percent0.setSelected(false);
		percent10.setSelected(false);
		percent20.setSelected(false);
		percent30.setSelected(false);
		percent40.setSelected(false);
		percent50.setSelected(false);
		percent60.setSelected(false);
		percent70.setSelected(false);
		percent80.setSelected(false);
		percent90.setSelected(false);
		percent100.setSelected(false);
		if (selectedItem == 0)
			percent0.setSelected(true);
		if (selectedItem == 1)
			percent10.setSelected(true);
		if (selectedItem == 2)
			percent20.setSelected(true);
		if (selectedItem == 3)
			percent30.setSelected(true);
		if (selectedItem == 4)
			percent40.setSelected(true);
		if (selectedItem == 5)
			percent50.setSelected(true);
		if (selectedItem == 6)
			percent60.setSelected(true);
		if (selectedItem == 7)
			percent70.setSelected(true);
		if (selectedItem == 8)
			percent80.setSelected(true);
		if (selectedItem == 9)
			percent90.setSelected(true);
		if (selectedItem == 10)
			percent100.setSelected(true);
	}

	/*
	 * public boolean verifyNewAspect(int selectedItem) {
	 * 
	 * Volume3DData volume = s.getContentController().getVolumeData(); int
	 * stackSize =(int) volume.getStackSize();
	 * 
	 * float proposedScale = selectedItem*0.25f+0.5f; int newStackSize =
	 * (int)(proposedScale*autoScale*stackSize); if(newStackSize>255) {
	 * 
	 * JOptionPane.showMessageDialog(rootPane,"UnSupported: Z Aspect Too Large",
	 * "Cannot Scale",JOptionPane.WARNING_MESSAGE); return false; } return true;
	 * }
	 */

	private int getThresholdIndex(int threshold) {
		float val = (float) threshold / 255.0f;
		int retVal = 0;
		if (val <= 0.05)
			return 0;
		if (val > 0.05 && val <= 0.15)
			return 1;
		if (val > 0.15 && val <= 0.25)
			return 2;
		if (val > 0.25 && val <= 0.35)
			return 3;
		if (val > 0.35 && val <= 0.45)
			return 4;
		if (val > 0.45 && val <= 0.55)
			return 5;
		if (val > 0.55 && val <= 0.65)
			return 6;
		if (val > 0.65 && val <= 0.75)
			return 7;
		if (val > 0.75 && val <= 0.85)
			return 8;
		if (val > 0.85 && val <= 0.95)
			return 9;
		if (val > 0.95 && val <= 1.05)
			return 10;
		return retVal;

	}

	public boolean setSelectedAspectIndex(int selectedItem) {
		// verify the value first
		// boolean retVal = verifyNewAspect(selectedItem);

		boolean retVal = true;
		if (retVal) {
			aspect0.setSelected(false);
			aspect1.setSelected(false);
			aspect2.setSelected(false);
			aspect3.setSelected(false);
			aspect4.setSelected(false);
			aspect5.setSelected(false);
			aspect6.setSelected(false);
			aspect7.setSelected(false);
			aspect8.setSelected(false);
			aspect9.setSelected(false);
			// aspect10.setSelected(false);
		}
		if (selectedItem == 0)
			aspect0.setSelected(retVal);
		if (selectedItem == 1)
			aspect1.setSelected(retVal);
		if (selectedItem == 2)
			aspect2.setSelected(retVal);
		if (selectedItem == 3)
			aspect3.setSelected(retVal);
		if (selectedItem == 4)
			aspect4.setSelected(retVal);
		if (selectedItem == 5)
			aspect5.setSelected(retVal);
		if (selectedItem == 6)
			aspect6.setSelected(retVal);
		if (selectedItem == 7)
			aspect7.setSelected(retVal);
		if (selectedItem == 8)
			aspect8.setSelected(retVal);
		if (selectedItem == 9)
			aspect9.setSelected(retVal);
		// if(selectedItem==10) aspect10.setSelected(retVal);
		return retVal;
	}

	public void disableAll() {
		System.out.println("disabling all");
		rotatemenu.setEnabled(false);
		thresholdMenu.setEnabled(false);
		menuBar.setEnabled(false);
		ZAspectMenu.setEnabled(false);
	}

	public void enableAll() {
		ZAspectMenu.setEnabled(true);
		rotatemenu.setEnabled(true);
		thresholdMenu.setEnabled(true);
		menuBar.setEnabled(true);
	}

	public void setSlicePane(SliceDisplay sd) {
		this.slicePane = sd;
	}
}
