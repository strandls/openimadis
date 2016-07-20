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

package com.strandgenomics.imaging.ithreedview.slicing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.strandgenomics.imaging.ithreedview.dataobjects.Volume3DData;
import com.strandgenomics.imaging.ithreedview.imagedisplay.ImagePanel;
import com.strandgenomics.imaging.ithreedview.utils.ImageUtils;



public class SliceDisplay {
	private int defaultDisplaySize = 128;
	private SliceImageGenerator slicer = null;
	private Volume3DData vol3d;
	private JPanel imagePanel = null;
	private JPanel controlPanel = null;

	private ImagePanel yzPanel = null;
	private ImagePanel xzPanel = null;
	private ImagePanel xyPanel = null;

	private JLabel yzLabel = null;
	private JLabel xzLabel = null;
	private JLabel xyLabel = null;

	private JPanel yzlabelledPanel = null;
	private JPanel xzlabelledPanel = null;
	private JPanel xylabelledPanel = null;

	private JLabel yzSliderLabel = null;
	private JLabel xySliderLabel = null;
	private JLabel xzSliderLabel = null;

	private JSlider yzControl = null;
	private JSlider xzControl = null;
	private JSlider xyControl = null;

	private JPanel yzSliderPanel = null;
	private JPanel xzSliderPanel = null;
	private JPanel xySliderPanel = null;

	private BufferedImage xySliceImage = null;
	private BufferedImage yzSliceImage = null;
	private BufferedImage xzSliceImage = null;

	private JButton sliceVolume = null;
	private JButton sliceAllVolume = null;
	private JButton resetVolume = null;
	private JButton toggleSlice = null;
	// private JButton sliceXYVolume = null;

	private int bufferSpace = 150;
	private int launcherHeight = 600;
	private int launcherWidth = 200;

	private float yzSlicePercent = 0.5f;
	private float xzSlicePercent = 0.5f;
	private float xySlicePercent = 0.5f;

	private SliceConfigurer.SliceView defaultSliceView = SliceConfigurer.SliceView.FRONTAL;
	private SliceConfigurer.SliceView sliceView = defaultSliceView;

	private SliceConfigurer.SliceView[] sliceAllView;

	private JRootPane rootPane;

	private Box.Filler filler = null;

	private int toggleAll = 0;
	private String toggleEvent = "SliceVolume";
	private Border emptyBorder;
	private Border lineBorder;

	public SliceDisplay(Volume3DData vol3d, int brightness, int threshold) {
		this.vol3d = vol3d;
		this.slicer = new SliceImageGenerator(vol3d);
		slicer.setBrightness(brightness);
		slicer.setThreshold(threshold); // threshold does not affect slice

		yzPanel = new ImagePanel(0, 0);
		xyPanel = new ImagePanel(0, 0);
		xzPanel = new ImagePanel(0, 0);

		yzLabel = new JLabel();
		xzLabel = new JLabel();
		xyLabel = new JLabel();

		yzlabelledPanel = new JPanel();
		xylabelledPanel = new JPanel();
		xzlabelledPanel = new JPanel();

		imagePanel = new JPanel();
		yzSliderLabel = new JLabel("YZ Slice Control::");
		xySliderLabel = new JLabel("XY Slice Control::");
		xzSliderLabel = new JLabel("XZ Slice Control::");

		yzControl = new JSlider(0, 100, 50);
		xzControl = new JSlider(0, 100, 50);
		xyControl = new JSlider(0, 100, 50);

		yzSliderPanel = new JPanel();
		xzSliderPanel = new JPanel();
		xySliderPanel = new JPanel();

		controlPanel = new JPanel();

		sliceVolume = new JButton("Slice");
		sliceAllVolume = new JButton("Slice All");
		resetVolume = new JButton("Reset");
		toggleSlice = new JButton("Toggle");
		// sliceXYVolume = new JButton("XY Volume Slice");

		addListeners();
		addComponentListeners();

		sliceAllView = new SliceConfigurer.SliceView[3];

		emptyBorder = BorderFactory.createEmptyBorder();
		lineBorder = BorderFactory.createLineBorder(Color.BLACK);
	}

	public SliceConfigurer.SliceView[] getSliceAllView() {
		return sliceAllView;
	}

	public SliceConfigurer.SliceView getCurrentView() {
		return sliceView;
	}

	public void setBrightness(int brightness) {
		slicer.setBrightness(brightness); // for future events

		updateImagePanel(SliceConfigurer.SlicePlane.YZPLANE, yzSlicePercent);
		updateImagePanel(SliceConfigurer.SlicePlane.XZPLANE,
				1.0f - xzSlicePercent);
		updateImagePanel(SliceConfigurer.SlicePlane.XYPLANE, xySlicePercent);
	}

	public void setThreshold(int threshold) {
		slicer.setThreshold(threshold);
		updateImagePanel(SliceConfigurer.SlicePlane.YZPLANE, yzSlicePercent);
		updateImagePanel(SliceConfigurer.SlicePlane.XZPLANE,
				1.0f - xzSlicePercent);
		updateImagePanel(SliceConfigurer.SlicePlane.XYPLANE, xySlicePercent);

	}

	public JPanel getSliceImagePanel() {
		// default slices
		return getSliceImagePanel(0.5f, 0.5f, 0.5f);
	}

	private void getImagePanel(SliceConfigurer.SlicePlane slicePlane,
			float slicePercent) {

		slicer.generateSliceImage(slicePlane, slicePercent,
				SliceImageGenerator.Mode.SLICE);
		ImagePanel panel = null;
		if (slicePlane == SliceConfigurer.SlicePlane.YZPLANE) {
			panel = yzPanel;
			yzSliceImage = null;// reset any old image
			yzSliceImage = scaleImageForDisplay(slicer
					.getSliceAsBufferedImage());
			yzSlicePercent = slicePercent;

			panel.setImage(yzSliceImage);
			panel.setStartX((launcherWidth - yzSliceImage.getWidth()) / 2);
			panel.repaint();
		}
		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE) {
			panel = xzPanel;
			xzSliceImage = null;
			xzSliceImage = scaleImageForDisplay(slicer
					.getSliceAsBufferedImage());
			xzSlicePercent = slicePercent;

			panel.setImage(xzSliceImage);

			panel.setStartX((launcherWidth - xzSliceImage.getWidth()) / 2);

			panel.repaint();
		}
		if (slicePlane == SliceConfigurer.SlicePlane.XYPLANE) {
			panel = xyPanel;
			xySliceImage = null;
			xySliceImage = scaleImageForDisplay(slicer
					.getSliceAsBufferedImage());
			xySlicePercent = slicePercent;
			panel.setImage(xySliceImage);

			panel.setStartX((launcherWidth - xySliceImage.getWidth()) / 2);

			panel.repaint();
		}

		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.setPreferredSize(new Dimension(panel.preferredXSize,
				panel.preferredYSize));

	}

	private void updateImagePanel(SliceConfigurer.SlicePlane slicePlane,
			float slicePercent) {

		ImagePanel panel = null;
		JLabel label = null;
		String title = new String();

		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE) {

			slicer.generateSliceImage(slicePlane, 1.0f - slicePercent,
					SliceImageGenerator.Mode.SLICE);
		} else {
			slicer.generateSliceImage(slicePlane, slicePercent,
					SliceImageGenerator.Mode.SLICE);
		}
		if (slicePlane == SliceConfigurer.SlicePlane.YZPLANE) {
			label = yzLabel;
			title += "YZSlice ";
			panel = yzPanel;
			yzSliceImage = null;// reset any old image
			yzSliceImage = scaleImageForDisplay(slicer
					.getSliceAsBufferedImage());
			panel.setImage(yzSliceImage);
			yzSlicePercent = slicePercent;
		}
		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE) {
			label = xzLabel;
			title += "XZSlice ";
			panel = xzPanel;
			xzSliceImage = null;// reset any old image
			xzSliceImage = scaleImageForDisplay(slicer
					.getSliceAsBufferedImage());
			panel.setImage(xzSliceImage);
			xzSlicePercent = slicePercent;

		}
		if (slicePlane == SliceConfigurer.SlicePlane.XYPLANE) {
			label = xyLabel;
			title += "XYSlice ";
			panel = xyPanel;
			xySliceImage = null;// reset any old image
			xySliceImage = scaleImageForDisplay(slicer
					.getSliceAsBufferedImage());
			panel.setImage(xySliceImage);
			xySlicePercent = slicePercent;
		}

		int percent = (int) (slicePercent * 100.0);
		title += String.valueOf(percent);
		title += "%";
		label.setText(title);
		panel.repaint();

	}

	private void getLabelledPanel(SliceConfigurer.SlicePlane slicePlane,
			float slicePercent) {
		getImagePanel(slicePlane, slicePercent);
		JPanel labelledPanel = null;

		JPanel panel = null;
		JLabel label = null;
		String title = new String();
		JSlider slider = null;
		if (slicePlane == SliceConfigurer.SlicePlane.YZPLANE) {
			label = yzLabel;
			title += "YZSlice ";
			labelledPanel = yzlabelledPanel;
			panel = yzPanel;
			slider = yzControl;
		}
		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE) {
			label = xzLabel;
			title += "XZSlice ";
			labelledPanel = xzlabelledPanel;
			panel = xzPanel;
			slider = xzControl;
		}
		if (slicePlane == SliceConfigurer.SlicePlane.XYPLANE) {
			label = xyLabel;
			title += "XYSlice ";
			labelledPanel = xylabelledPanel;
			panel = xyPanel;
			slider = xyControl;
		}

		labelledPanel.setLayout(new BoxLayout(labelledPanel, BoxLayout.Y_AXIS));
		int percent = (int) (slicePercent * 100.0);
		title += String.valueOf(percent);
		title += "%";
		label.setText(title);
		JPanel newPanel = new JPanel();

		newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.X_AXIS));
		newPanel.add(label);
		newPanel.add(slider);
		slider.setPreferredSize(new Dimension(100, 5));

		labelledPanel.add(panel);
		labelledPanel.add(newPanel);
		newPanel.repaint();
		panel.repaint();
		labelledPanel.repaint();

	}

	public JPanel getSliceImagePanel(float yzpercent, float xzpercent,
			float xypercent) {
		imagePanel = null; // clear previous panel
		imagePanel = new JPanel();
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

		getLabelledPanel(SliceConfigurer.SlicePlane.YZPLANE, yzpercent);
		getLabelledPanel(SliceConfigurer.SlicePlane.XZPLANE, xzpercent);
		getLabelledPanel(SliceConfigurer.SlicePlane.XYPLANE, xypercent);

		int totalY = yzPanel.preferredYSize + xyPanel.preferredYSize
				+ xzPanel.preferredYSize + bufferSpace;
		int diff = (launcherHeight - totalY) / 2;

		Dimension minSize = new Dimension(100, diff);
		Dimension prefSize = new Dimension(100, diff);
		Dimension maxSize = new Dimension(100, diff);

		imagePanel.add(new Box.Filler(minSize, prefSize, maxSize));
		imagePanel.add(yzlabelledPanel);
		imagePanel.add(xzlabelledPanel);
		imagePanel.add(xylabelledPanel);

		imagePanel.add(new Box.Filler(minSize, prefSize, maxSize));

		imagePanel.repaint();
		return imagePanel;
	}

	public void setRootPane(JRootPane rootPane) {
		this.rootPane = rootPane;
	}

	public void updateFillerSize() {
		int dist = (rootPane.getWidth() - 300) / 2;
		Dimension minSize = new Dimension(dist, 10);
		Dimension prefSize = new Dimension(dist, 10);
		Dimension maxSize = new Dimension(dist, 10);
		filler.changeShape(minSize, prefSize, maxSize);
	}

	public JPanel getSliceControlPanel() {

		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		int dist = (rootPane.getWidth() - 200) / 2;
		Dimension minSize = new Dimension(100, 10);
		Dimension prefSize = new Dimension(dist, 10);
		Dimension maxSize = new Dimension(200, 10);

		filler = new Box.Filler(minSize, prefSize, maxSize);
		controlPanel.add(filler);

		controlPanel.add(sliceVolume);
		controlPanel.add(sliceAllVolume);

		minSize = new Dimension(10, 10);
		prefSize = new Dimension(10, 10);
		maxSize = new Dimension(10, 10);

		controlPanel.add(new Box.Filler(minSize, prefSize, maxSize));
		controlPanel.add(toggleSlice);
		controlPanel.add(resetVolume);
		sliceVolume.setEnabled(false);
		sliceAllVolume.setEnabled(false);
		toggleSlice.setEnabled(false);

		controlPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Slice Volume Controls"));

		// resetVolume.setEnabled(false);
		return controlPanel;
	}

	public void cleanup() {
		yzPanel = null;
		xyPanel = null;
		xzPanel = null;

		yzLabel = null;
		xzLabel = null;
		xyLabel = null;

		yzlabelledPanel = null;
		xylabelledPanel = null;
		xzlabelledPanel = null;

		imagePanel = null;
		yzSliderLabel = null;
		xySliderLabel = null;
		xzSliderLabel = null;

		yzControl = null;
		xzControl = null;
		xyControl = null;

		yzSliderPanel = null;
		xzSliderPanel = null;
		xySliderPanel = null;

		controlPanel = null;

		yzSliceImage = null;
		xzSliceImage = null;
		xySliceImage = null;

		sliceVolume = null;
		resetVolume = null;
		// sliceXZVolume = null;
		// sliceXYVolume = null;

		emptyBorder = null;
		lineBorder = null;

	}

	public void addListeners() {

		yzControl.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				yzlabelledPanel.setBorder(lineBorder);
				xzlabelledPanel.setBorder(emptyBorder);
				xylabelledPanel.setBorder(emptyBorder);
				int value = yzControl.getValue();
				final float percentage = (float) value / 100.0f;
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateImagePanel(SliceConfigurer.SlicePlane.YZPLANE, percentage);
						imagePanel.firePropertyChange("YZUpdate", false, true);
					}
				});
				
				
				sliceVolume.setEnabled(true);
				sliceAllVolume.setEnabled(true);
				resetVolume.setEnabled(true);

			}
		});
		xzControl.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				yzlabelledPanel.setBorder(emptyBorder);
				xzlabelledPanel.setBorder(lineBorder);
				xylabelledPanel.setBorder(emptyBorder);
				int value = xzControl.getValue();
				final float percentage = (float) value / 100.0f;
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateImagePanel(SliceConfigurer.SlicePlane.XZPLANE, percentage);

						imagePanel.firePropertyChange("XZUpdate", false, true);
					}
				});

				sliceVolume.setEnabled(true);
				sliceAllVolume.setEnabled(true);
				resetVolume.setEnabled(true);

			}
		});
		xyControl.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				yzlabelledPanel.setBorder(emptyBorder);
				xzlabelledPanel.setBorder(emptyBorder);
				xylabelledPanel.setBorder(lineBorder);
				int value = xyControl.getValue();
				final float percentage = (float) value / 100.0f;
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateImagePanel(SliceConfigurer.SlicePlane.XYPLANE, percentage);

						imagePanel.firePropertyChange("XYUpdate", false, true);
					}
				});
				

				sliceVolume.setEnabled(true);
				sliceAllVolume.setEnabled(true);
				resetVolume.setEnabled(true);

			}
		});
		sliceVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// sliceVolume.setEnabled(false);
				// sliceAllVolume.setEnabled(false);
				toggleEvent = "SliceVolume";
				toggleSlice.setEnabled(true);
				// resetVolume.setEnabled(false);

				// yzControl.setEnabled(false);
				// xyControl.setEnabled(false);
				// xzControl.setEnabled(false);
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fireEvent("SliceVolume");
					}
				});
				
			}
		});
		toggleSlice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				toggleSliceView();
				toggleSliceAllView();
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fireEvent(toggleEvent);
					}
				});
			}
		});

		resetVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setDefaultControls();
				sliceVolume.setEnabled(false);
				sliceAllVolume.setEnabled(false);
				toggleSlice.setEnabled(false);

				yzControl.setEnabled(true);
				xyControl.setEnabled(true);
				xzControl.setEnabled(true);

				yzlabelledPanel.setBorder(emptyBorder);
				xzlabelledPanel.setBorder(emptyBorder);
				xylabelledPanel.setBorder(emptyBorder);
				// resetVolume.setEnabled(false);
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fireEvent("ResetVolume");
					}
				});
				
			}
		});
		sliceAllVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleAll = 0;
				setSliceAllView(toggleAll);
				yzControl.setEnabled(false);
				xyControl.setEnabled(false);
				xzControl.setEnabled(false);
				sliceVolume.setEnabled(false);
				sliceAllVolume.setEnabled(false);
				toggleEvent = "SliceAll";
				toggleSlice.setEnabled(true);
				// resetVolume.setEnabled(false);
				
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fireEvent("SliceAll");
					}
				});
			}
		});

	}

	private void fireEvent(String slicePlane) {
		imagePanel.firePropertyChange(slicePlane, false, true);
	}

	public float getYZValue() {
		return (float) yzControl.getValue() / 100.0f;
	}

	public float getXZValue() {
		return (float) xzControl.getValue() / 100.0f;
	}

	public float getXYValue() {
		float val = (float) xyControl.getValue() / 100.0f;
		return (float) xyControl.getValue() / 100.0f;
	}

	private BufferedImage scaleImageForDisplay(BufferedImage tempImage) {
		int height = tempImage.getHeight();
		int width = tempImage.getWidth();
		int maxDim = Math.max(height, width);
		float scale = (float) defaultDisplaySize / (float) maxDim;
		int newheight = (int) ((float) height * scale);
		int newwidth = (int) ((float) width * scale);
		return ImageUtils.getScaledImage(tempImage, newheight, newwidth);
	}

	public void saveImage(SliceConfigurer.SlicePlane slicePlane,
			JRootPane rootPane) {
//		float percentage = 0.5f;
//
//		if (slicePlane == SliceConfigurer.SlicePlane.YZPLANE) {
//			percentage = yzSlicePercent;
//		}
//		if (slicePlane == SliceConfigurer.SlicePlane.XZPLANE) {
//			percentage = xzSlicePercent;
//		}
//		if (slicePlane == SliceConfigurer.SlicePlane.XYPLANE) {
//			percentage = xySlicePercent;
//		}
//
//		slicer.generateSliceImage(slicePlane, percentage,
//				SliceImageGenerator.Mode.SLICE);
//
//		BufferedImage origimg = slicer.getSliceAsBufferedImage();
//
//		List<String> filters = Arrays.asList("png", "jpeg", "jpg");
//		FileChooser chooser = new FileChooser(rootPane, filters);
//
//		int action = chooser.getChooserAction();
//		if (action == OFileChooser.APPROVE_OPTION) {
//			try {
//
//				ImageIO.write(origimg, chooser.getFormatName(),
//						chooser.getChosenFile());
//			} catch (Exception e) {
//				System.err.println("Not able to write 3D snapshot image");
//			}
//		} else {
//			// do nothing
//		}

	}

	public void setDefaultControls() {
		xyControl.setValue(50);
		xzControl.setValue(50);
		yzControl.setValue(50);
	}

	private void addComponentListeners() {
		controlPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				updateFillerSize();
			}
		});
	}

	public void reset() {
		// reset the slice display
		setBrightness(0);
		setThreshold(0);
		resetVolume.doClick();
	}

	public void toggleSliceView() {
		if (sliceView == SliceConfigurer.SliceView.FRONTAL) {
			sliceView = SliceConfigurer.SliceView.REVERSE;
		} else {
			if (sliceView == SliceConfigurer.SliceView.REVERSE) {
				sliceView = SliceConfigurer.SliceView.FRONTAL;
			}
		}
	}

	public void toggleSliceAllView() {
		toggleAll = toggleAll + 1;
		if (toggleAll > 7) {
			toggleAll = 0;
		}
		setSliceAllView(toggleAll);
	}

	public void setSliceAllView(int toggleAll) {
		if (toggleAll == 0 || toggleAll == 1 || toggleAll == 2
				|| toggleAll == 3) {
			sliceAllView[0] = SliceConfigurer.SliceView.FRONTAL;
		} else {
			sliceAllView[0] = SliceConfigurer.SliceView.REVERSE;
		}
		if (toggleAll == 0 || toggleAll == 1 || toggleAll == 4
				|| toggleAll == 5) {
			sliceAllView[1] = SliceConfigurer.SliceView.FRONTAL;
		} else {
			sliceAllView[1] = SliceConfigurer.SliceView.REVERSE;
		}
		if (toggleAll == 0 || toggleAll == 3 || toggleAll == 5
				|| toggleAll == 7) {
			sliceAllView[2] = SliceConfigurer.SliceView.FRONTAL;
		} else {
			sliceAllView[2] = SliceConfigurer.SliceView.REVERSE;
		}

	}

	public void resetSlicing() {
		resetVolume.doClick();
	}

}
