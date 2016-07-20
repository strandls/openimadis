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

package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.swing.RangeSlider;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.VisualContrast;
import com.strandgenomics.imaging.icore.image.Histogram;

public class ContrastSlidersPanel {

	private final static class HistogramPanel extends JComponent {

		private Color channelColor;
		private int contrastMax;
		private int contrastMin;
//		private int[] histogram;
		private int[] intensity;
		private int[] freq;
		private int maxY;
		private int maxX;
		private int minX;
		public int startX;
		public int startY;

		private void drawDashedLineAt(final Graphics g, final int xLine,
				final int h) {
			final int x = xLine;
			for (int y = 0; y < h - 1; y += 8) {
				g.drawLine(x, y, x, y + 4);
			}
		}

		protected void paintComponent(final Graphics g) {
			
			if (intensity == null || intensity.length == 0)
				return;
			super.paintComponent(g);
			
			final Graphics2D g2 = (Graphics2D) g.create();

			final int h = getHeight();
			final int w = getWidth();

			if (channelColor.equals(Color.WHITE)) {
				g2.setColor(Color.BLACK);
			} else {
				g2.setColor(Color.WHITE);
			}

			g2.fillRect(0, 0, w, h);

			if (channelColor.equals(Color.WHITE)) {
				g2.setColor(Color.WHITE);
			} else {
				g2.setColor(Color.BLACK);
			}

			final double xScale = (double) w / (double) (maxX-minX);
			final double yScale = (double) h / (double) maxY;

			int x1 = (int) ((contrastMin-minX) * xScale + 0.5);
			int x2 = (int) ((contrastMax-minX) * xScale + 0.5);
			g2.drawLine(x1, h, x2 , 0);
			
			int xFrom = 0;
			int yFrom = h - (int) (0 * yScale + 0.5);//TODO: check this code
			int xTo;
			int yTo;

			g2.setColor(channelColor);

			for (int i = 1; i < intensity.length; i++) {
				xTo = (int) (xScale * (intensity[i]-minX) + 0.5);
				yTo = h - (int) (freq[i] * yScale + 0.5);
				g2.drawLine(xFrom, yFrom, xTo, yTo);
				xFrom = xTo;
				yFrom = yTo;
			}
			g2.dispose();
		}

	}

	private final JButton autoallButton;
	private final JButton closeButton;

	private final ColorComboBox channelColorChooser;
	private List<IChannel> channels;
	private boolean disabled;
	private final HistogramPanel histogramPanel;
	private final JLabel maxLabel;
	private final JLabel minLabel;

	private final JLabel maxAllowedLabel;
	private final JLabel minAllowedLabel;
	private final JLabel maxAllowedLabel2;
	private final JLabel minAllowedLabel2;

	private final JPanel panel;

	private final RangeSlider rangeSlider;
	private final DoubleSlider gammaSlider;
	private final JLabel gammaLabel;
	private int selectedChannel = 0;

	private boolean updating = false;
	private final JLabel channelTitleLabel;
	
	private JDialog contrastDialog;
	
	private ImageViewerApplet imageState;
	protected int rangeMaxValue;
	protected int rangeMinValue;
	private JTextField minRange;
	private JTextField maxRange;
	private boolean sliceState = false;

	public ContrastSlidersPanel(ImageViewerApplet imageViewerState) {

		this.imageState = imageViewerState;
		
		panel = new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.white);
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.setBorder(BorderFactory
				.createTitledBorder("Selected Channel Properties"));

		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.white);
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBorder(BorderFactory
				.createTitledBorder("All Channel Properties"));

		channelColorChooser = new ColorComboBox();
		channelColorChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
		channelColorChooser.setMaximumSize(new Dimension(160, 25));
		channelColorChooser.setPreferredSize(new Dimension(160, 25));

		JPanel channelLabelPanel = new JPanel();
		channelLabelPanel.setLayout(new BoxLayout(channelLabelPanel,
				BoxLayout.X_AXIS));
		channelTitleLabel = new JLabel("Histogram");
		Font f = channelTitleLabel.getFont();
		channelTitleLabel.setFont(new Font(f.getName(), Font.BOLD,
				f.getSize() + 1));
		channelLabelPanel.add(channelTitleLabel);
		channelLabelPanel.add(Box.createHorizontalGlue());

		histogramPanel = new HistogramPanel();
		histogramPanel.setMinimumSize(new Dimension(200, 80));
		histogramPanel.setPreferredSize(new Dimension(200, 80));
		histogramPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		rangeSlider = new RangeSlider();
		rangeSlider.setMinorTickSpacing(1);
		rangeSlider.setMinimumSize(new Dimension(160, 40));
		rangeSlider.setPreferredSize(new Dimension(160, 40));
		rangeSlider.setBackground(Color.white);

		gammaSlider = new DoubleSlider(0, 3, 1, 100);
		gammaSlider.setMinorTickSpacing(1);
		gammaSlider.setMinimumSize(new Dimension(160, 40));
		gammaSlider.setPreferredSize(new Dimension(160, 40));
		gammaSlider.setBackground(Color.white);

		JPanel popupPanel = new JPanel();
		popupPanel.setLayout(new BorderLayout());

		Font monoFont = new Font("Monospaced", Font.PLAIN, 12);

		maxAllowedLabel2 = new JLabel();
		minAllowedLabel2 = new JLabel();
		minAllowedLabel2.setFont(monoFont);
		maxAllowedLabel2.setFont(monoFont);

		JPanel labelAllowedPanel2 = new JPanel();
		labelAllowedPanel2.setLayout(new BoxLayout(labelAllowedPanel2,
				BoxLayout.X_AXIS));
		labelAllowedPanel2.add(minAllowedLabel2);
		labelAllowedPanel2.add(Box.createHorizontalGlue());
		labelAllowedPanel2.add(maxAllowedLabel2);
		labelAllowedPanel2.setMinimumSize(new Dimension(200, 15));
		labelAllowedPanel2.setPreferredSize(new Dimension(200, 15));

		popupPanel.add(labelAllowedPanel2, BorderLayout.BEFORE_FIRST_LINE);

		maxLabel = new JLabel();
		minLabel = new JLabel();
		minLabel.setFont(monoFont);
		maxLabel.setFont(monoFont);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		labelPanel.add(minLabel);
		labelPanel.add(Box.createHorizontalGlue());
		labelPanel.add(maxLabel);
		labelPanel.setMinimumSize(new Dimension(160, 15));
		labelPanel.setPreferredSize(new Dimension(160, 15));
		labelPanel.setBackground(Color.white);

		maxAllowedLabel = new JLabel();
		minAllowedLabel = new JLabel();
		minAllowedLabel.setFont(monoFont);
		maxAllowedLabel.setFont(monoFont);

		JPanel labelAllowedPanel = new JPanel();
		labelAllowedPanel.setLayout(new BoxLayout(labelAllowedPanel,
				BoxLayout.X_AXIS));
		labelAllowedPanel.add(minAllowedLabel);
		labelAllowedPanel.add(Box.createHorizontalGlue());
		labelAllowedPanel.add(maxAllowedLabel);
		labelAllowedPanel.setMinimumSize(new Dimension(160, 15));
		labelAllowedPanel.setPreferredSize(new Dimension(160, 15));
		labelAllowedPanel.setBackground(Color.white);

		autoallButton = newButton("Auto");
		closeButton = newButton("Close");

		final JPanel setContrastPanel = new JPanel();
		setContrastPanel.setLayout(new BoxLayout(setContrastPanel, BoxLayout.X_AXIS));
		
		minRange = newTextField(String.valueOf(histogramPanel.contrastMin));
		minRange.setEnabled(false);
		maxRange = newTextField(String.valueOf(histogramPanel.contrastMax));
		maxRange.setEnabled(false);
		
		final JButton setRangeButton = newButton("set");
		setRangeButton.setEnabled(false);
		setRangeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int min = 0;
				int max = 0;
				try
				{
					min = Integer.parseInt(minRange.getText());
					max = Integer.parseInt(maxRange.getText());
				}
				catch(NumberFormatException ex)
				{
					String maxValue = String.valueOf(histogramPanel.contrastMax);
					String minValue = String.valueOf(histogramPanel.contrastMin);
					
					maxRange.setText(maxValue);
					minRange.setText(minValue);
				}
				
				
				IRecord record = imageState.getRecords().get(0);
				int depth = record.getPixelDepth().getBitSize();
				if(Math.pow(2, depth)<max)
				{
					String maxValue = String.valueOf(histogramPanel.contrastMax);
					String minValue = String.valueOf(histogramPanel.contrastMin);
					maxRange.setText(maxValue);
					minRange.setText(minValue);
					JOptionPane.showMessageDialog(null, "Can't set contrast above "+Math.pow(2, depth), "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(0>min || max<min)
				{
					String maxValue = String.valueOf(histogramPanel.contrastMax);
					String minValue = String.valueOf(histogramPanel.contrastMin);
					
					maxRange.setText(maxValue);
					minRange.setText(minValue);
					
					JOptionPane.showMessageDialog(null, "Illegal values for min and max. Min = "+min+" Max = "+max, "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(Math.pow(2, depth)>max)
				{
					rangeMaxValue = max;
					rangeMinValue = min;
					VisualContrast v = new VisualContrast(min, max);
					record.setCustomContrast(sliceState, selectedChannel, v);
					
					if(rangeSlider.getMaximum()>=max)
						rangeSlider.setHighValue(max);
					if(rangeSlider.getMinimum()<=min)
						rangeSlider.setLowValue(min);
					
					rangeSlider.revalidate();
					
					imageState.refresh();
					histogramPanel.repaint();
				}
			}
		});
		
		final JCheckBox setContrastCheck = new JCheckBox("Set");
		setContrastCheck.setSelected(false);
		setContrastCheck.setToolTipText("Explicitly set contrast range");
		setContrastCheck.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e)
			{
				if(!setContrastCheck.isSelected())
				{
					//enable graph based controls
					rangeSlider.setEnabled(true);
					rangeSlider.setToolTipText("range slider enabled to change contrast");
					
					//disable set contrast controls
					minRange.setEnabled(false);
					maxRange.setEnabled(false);
					setRangeButton.setEnabled(false);
				}
				else
				{
					//disable graph based controls
					rangeSlider.setEnabled(false);
					rangeSlider.setToolTipText("range slider disabled");
					
					//enable set contrast controls
					minRange.setEnabled(true);
					maxRange.setEnabled(true);
					setRangeButton.setEnabled(true);
				}
			}
		});
		
		setContrastPanel.add(setContrastCheck);
		setContrastPanel.add(setRangeButton);
		setContrastPanel.add(minRange);
		setContrastPanel.add(maxRange);

		JPanel histogramBtnpanel = new JPanel();
		histogramBtnpanel.setLayout(new BoxLayout(histogramBtnpanel, BoxLayout.X_AXIS));
		histogramBtnpanel.setMinimumSize(new Dimension(180, 40));
		histogramBtnpanel.add(Box.createHorizontalGlue());
		histogramBtnpanel.add(setContrastPanel);
		histogramBtnpanel.setOpaque(true);
		histogramBtnpanel.setBackground(Color.white);
		histogramBtnpanel.add(Box.createHorizontalGlue());
		
		JPanel btnpanel = new JPanel();
		btnpanel.setLayout(new BoxLayout(btnpanel, BoxLayout.X_AXIS));
		btnpanel.add(Box.createHorizontalGlue());
		btnpanel.add(autoallButton);
		btnpanel.add(closeButton);
		btnpanel.add(Box.createHorizontalGlue());
		btnpanel.setMinimumSize(new Dimension(160, 40));
		btnpanel.setBackground(Color.white);

		gammaLabel = new JLabel();
		gammaLabel.setFont(monoFont);
		
		JPanel gammaLabelPanel = new JPanel();
		gammaLabelPanel.setLayout(new BoxLayout(gammaLabelPanel,
				BoxLayout.X_AXIS));
		gammaLabelPanel.add(Box.createVerticalGlue());
		gammaLabelPanel.add(gammaLabel);
		gammaLabelPanel.add(Box.createHorizontalGlue());
		gammaLabelPanel.setMinimumSize(new Dimension(160, 10));
		gammaLabelPanel.setPreferredSize(new Dimension(160, 10));
		gammaLabelPanel.setMaximumSize(new Dimension(160, 10));
		gammaLabelPanel.setBackground(Color.white);

		JPanel gammaTicksPanel = new JPanel();
		gammaTicksPanel.setLayout(new BoxLayout(gammaTicksPanel,
				BoxLayout.X_AXIS));
		gammaTicksPanel.add(new JLabel("0.1"));
		gammaTicksPanel.add(Box.createHorizontalGlue());
		gammaTicksPanel.add(new JLabel("3.0"));
		gammaTicksPanel.setMinimumSize(new Dimension(160, 10));
		gammaTicksPanel.setPreferredSize(new Dimension(160, 10));
		gammaTicksPanel.setBackground(Color.white);

		panel1.add(Box.createVerticalStrut(5));
		panel1.add(histogramPanel);
		panel1.add(labelAllowedPanel);
		panel1.add(Box.createVerticalStrut(5));
		panel1.add(UIUtils.newTitlePanel(" Contrast", true));
		panel1.add(Box.createVerticalStrut(5));
		panel1.add(rangeSlider);
		panel1.add(labelPanel);
		panel1.add(histogramBtnpanel);
		panel1.add(Box.createVerticalStrut(5));
		
		panel2.add(UIUtils.newTitlePanel(" Gamma", true));
		panel2.add(Box.createVerticalStrut(5));
		panel2.add(gammaSlider);
		panel2.add(gammaTicksPanel);
		panel2.add(gammaLabelPanel);
		panel2.add(Box.createVerticalStrut(5));
		
		panel.add(panel1);
		panel.add(panel2);
		panel.add(btnpanel);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		addChangeListeners();
	}

	public void setTitle(String title) {
		channelTitleLabel.setText("Channel - '" + title + "'");
	}

	private JButton newButton(String text) {
		JButton button = new JButton(text);
		button.setToolTipText(text);
		button.setMinimumSize(new Dimension(80, 20));
		button.setPreferredSize(new Dimension(80, 20));
		return button;
	}
	
	private JTextField newTextField(String text) {
		JTextField tf = new JTextField(text);
		tf.setToolTipText(text);
		tf.setMinimumSize(new Dimension(80, 20));
		tf.setPreferredSize(new Dimension(80, 20));
		return tf;
	}

	public void setSelectedChannel(int index) {
		selectedChannel = index;
		sliceState  = imageState.getSliceState() == ImageViewerState.SliceState.Z_STACK ;

		if(channels == null)
			channels = imageState.getChannels();
		
		channelColorChooser.setSelectedColor(Color.black);
		updateContrasts();
		
	}
	
	public JDialog createContrastDialog() {
		contrastDialog = new JDialog();
		contrastDialog.setTitle("Contrast Control");
		contrastDialog.setLocation(300, 30);

		contrastDialog.add(this.getComponent());
		contrastDialog.pack();
		contrastDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				contrastDialog.setVisible(false);
			}
		});
		return contrastDialog;
	}
	
	public void addChangeListeners() {
		rangeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				if(!updating){
					IRecord record = imageState.getRecords().get(0);
					VisualContrast v = new VisualContrast(rangeSlider
							.getLowValue(), rangeSlider.getHighValue() + 1);
					record.setCustomContrast(sliceState, selectedChannel, v);
					
					imageState.refresh();
					
					histogramPanel.contrastMax = rangeSlider.getHighValue();
					histogramPanel.contrastMin = rangeSlider.getLowValue();
					
					maxLabel.setText("W: " + histogramPanel.contrastMax);
					minLabel.setText("B: " + histogramPanel.contrastMin);
					
					if(rangeSlider.isEnabled())
					{
						minRange.setText(String.valueOf(histogramPanel.contrastMin));
						maxRange.setText(String.valueOf(histogramPanel.contrastMax));
					}
					
					histogramPanel.repaint();
				}
			}
		});
		
					
		gammaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				if (!updating && !disabled) {
					double gammaval = gammaSlider.getScaledValue();
					IRecord record = imageState.getRecords().get(0);
					VisualContrast v = new VisualContrast(rangeSlider.getLowValue(),
							rangeSlider.getHighValue() + 1, gammaval);
					
					gammaLabel.setText("Gamma correction: " + gammaval);
					
					record.setCustomContrast(sliceState, selectedChannel, v);
					imageState.refresh();
				}
			}
		});

		autoallButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IRecord record = imageState.getRecords().get(0);
				record.setCustomContrast(sliceState, selectedChannel, null);
				
				int currSlice = imageState.getSlice();
				int currFrame = imageState.getFrame();
				int currSite = imageState.getSite();
				IPixelData pixels;
				try
				{
					pixels = record.getPixelData(new com.strandgenomics.imaging.icore.Dimension(
									currFrame, currSlice, selectedChannel, currSite));
					if (pixels != null)
					{
						Histogram his = pixels.getIntensityDistibution(sliceState);
						int autoMax = his.getMax();
						int autoMin = his.getMin();
						
						maxRange.setText(String.valueOf(autoMax));
						minRange.setText(String.valueOf(autoMin));
						
						if(rangeSlider.getMaximum()>=autoMax)
							rangeSlider.setHighValue(autoMax);
						if(rangeSlider.getMinimum()<=autoMin)
							rangeSlider.setLowValue(autoMin);
					}
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				
				imageState.refresh();
			}
		});

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contrastDialog.setVisible(false);
			}
		});
	}

	public void disableAll() {
		channelColorChooser.setEnabled(false);
		gammaSlider.setEnabled(false);
		disabled = true;
	}

	public void enableAll() {
		channelColorChooser.setEnabled(true);
		gammaSlider.setEnabled(true);
		disabled = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.strandgenomics.cube.framework.sidebar.ISidebar#getComponent()
	 */
	public Component getComponent() {
		final JScrollPane scrollPane = new JScrollPane(panel);
		return scrollPane;
	}

	public int getSelectedChannel() {
		return selectedChannel;
	}

	public String getTitle() {
		return "Contrast/Color";
	}

	public void updatePanel() {
		if (panel.isShowing()) 
			update();
	}

	private void update() {
		updating = true;

		channels = imageState.getChannels();

		if (channels.size() == 0) {
			disableAll();
			updating = false;
			return;
		} else {
			enableAll();
		}

		channelColorChooser.setSelectedColor(Color.black);

		updateContrasts();

		updating = false;

	}

	public void updateContrasts() {
		UpdateContrastCommand command = new UpdateContrastCommand();
		try {
			command.doInBackground();
		} catch (Exception e) {
			System.out.println("updateContrasts\n");
			//e.printStackTrace();
			if(contrastDialog.isVisible()){
				System.out.println("visible");
				contrastDialog.setVisible(false);
			}
			JOptionPane.showMessageDialog(null, "Image too large to be processed.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		command.done();
	}
	
	class UpdateContrastCommand{
		
		public static final String UPDATE_CONTRAST = "update_contrast";
		private int maxContrast;
		private int minContrast;
		private Histogram his;
		private double gammaValue = 1.0;
		private IPixelData pixels;
		private int actualMaxContrast = -1;
		private int actualMinContrast = -1;
		
		public UpdateContrastCommand() {
		}
		
		protected Object doInBackground() throws Exception {
			updating = true;
			
			IRecord record = imageState.getRecords().get(0);
			int currSlice = imageState.getSlice();
			int currFrame = imageState.getFrame();
			int currSite = imageState.getSite();
			pixels = record
					.getPixelData(new com.strandgenomics.imaging.icore.Dimension(
							currFrame, currSlice, selectedChannel, currSite));
			
			if (pixels == null) {
				return null;
			}
			
			try{
				his = pixels.getIntensityDistibution(sliceState);
			}
			catch(Exception e){
				System.out.println("his getIntensity dist");
				//e.printStackTrace();
				throw e;
			}
			maxContrast = his.getMax();
			minContrast = his.getMin();
			
			actualMaxContrast = maxContrast;
			actualMinContrast = minContrast;
			
			VisualContrast c = record.getChannel(selectedChannel).getContrast(sliceState);
			if(c!=null){
				if(maxContrast>=c.getMaxIntensity() && minContrast<=c.getMaxIntensity())
					maxContrast = c.getMaxIntensity();
				if(maxContrast>=c.getMinIntensity() && minContrast<=c.getMinIntensity())
					minContrast = c.getMinIntensity();
				actualMaxContrast = c.getMaxIntensity();
				actualMinContrast = c.getMinIntensity();
				System.out.println(actualMinContrast+" "+actualMaxContrast);
				gammaValue = c.getGamma(); 
			}
			
			return null;
		}
		
		protected void done() {
			if(pixels == null){
				updating = false;
				disableAll();
				return;
			}
			
			int max = his.getMax();
			int min = his.getMin();
			
			if (maxContrast > max) {
				max = maxContrast;
			}
			if (minContrast < min) {
				min = minContrast;
			}
			
			maxLabel.setText("W: " + maxContrast);
			minLabel.setText("B: " + minContrast);
			
			minRange.setText(String.valueOf(actualMinContrast));
			maxRange.setText(String.valueOf(actualMaxContrast));
			
			maxAllowedLabel.setText("" + max);
			minAllowedLabel.setText("" + min);

			maxAllowedLabel2.setText("" + max);
			minAllowedLabel2.setText("" + min);

			rangeSlider.setMaximum(max);
			rangeSlider.setMinimum(min);
			rangeSlider.setHighValue(maxContrast);
			rangeSlider.setLowValue(minContrast);
			
			gammaSlider.setScaledValue(gammaValue);

			gammaLabel.setText("Gamma correction: " + gammaValue);

			histogramPanel.contrastMax = maxContrast;
			histogramPanel.contrastMin = minContrast;
			
			try {
				histogramPanel.startX = min;
				histogramPanel.startY = max;
				histogramPanel.freq = his.getFrequencies();
				histogramPanel.intensity = his.getIntensities();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
			histogramPanel.maxX = max;
			histogramPanel.minX = min;
			histogramPanel.maxY = his.getMaxFrequency();
			histogramPanel.channelColor = Color.BLACK;

			histogramPanel.repaint();
			rangeSlider.revalidate();
			gammaSlider.revalidate();
			
			updating = false;
		}
	}
}
