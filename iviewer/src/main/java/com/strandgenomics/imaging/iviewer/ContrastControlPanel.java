package com.strandgenomics.imaging.iviewer;

import javax.swing.JSlider;

public class ContrastControlPanel {

	/*
	@SuppressWarnings("serial")
	private final static class HistogramPanel extends JComponent {

		private Color channelColor;
		private int contrastMax;
		private int contrastMin;
		private int[] histogram;
		private int maxY;

		private void drawDashedLineAt(final Graphics g, final int xLine,
				final int h) {
			final int x = xLine;
			for (int y = 0; y < h - 1; y += 8) {
				g.drawLine(x, y, x, y + 4);
			}
		}

		@Override
		protected void paintComponent(final Graphics g) {
			
			if (histogram == null || histogram.length == 0)
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

			final double xScale = (double) w / (double) histogram.length;
			final double yScale = (double) h / (double) maxY;

			g2.drawLine((int) (contrastMin * xScale + 0.5), h,
					(int) (contrastMax * xScale + 0.5), 0);

			int xFrom = 0;
			int yFrom = h - (int) (histogram[0] * yScale + 0.5);
			int xTo;
			int yTo;

			g2.setColor(channelColor);

			for (int i = 1; i < histogram.length; i++) {
				xTo = (int) (xScale * i + 0.5);
				yTo = h - (int) (histogram[i] * yScale + 0.5);
				g2.drawLine(xFrom, yFrom, xTo, yTo);
				xFrom = xTo;
				yFrom = yTo;
			}
			g2.dispose();
		}

	}

	private static final long serialVersionUID = 9077276393059073310L;

	private final JButton autoallButton;
	private final JButton setButton;
	private final JButton copyButton;
	private final JButton closeButton;

	private final ListComboBox channelChooser;
	private final ColorComboBox channelColorChooser;
	private List<Channel> channels;
	private String currentRecordId;
	private int depth;
	private boolean disabled;
	private final HistogramPanel histogramPanel;
	private final JLabel maxLabel;
	private final JLabel minLabel;

	private final JLabel maxAllowedLabel;
	private final JLabel minAllowedLabel;
	private final JLabel maxAllowedLabel2;
	private final JLabel minAllowedLabel2;

	private final JPanel panel;
	private final RangeSlider displayRange;

	private PixelData pixels;
	private final RangeSlider rangeSlider;
	private final DoubleSlider gammaSlider;
	private final JLabel gammaLabel;
	private int selectedChannel = 0;
	private final JidePopup popup;

	private boolean updating = false;
	private final JLabel channelTitleLabel;
	
	private JDialog contrastDialog;


	public ContrastControlPanel() {

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

		channelChooser = new ListComboBox();
		channelChooser.setEditable(false);

		channelChooser.setAlignmentX(Component.CENTER_ALIGNMENT);
		channelChooser.setMaximumSize(new Dimension(160, 25));
		channelChooser.setPreferredSize(new Dimension(160, 25));

		JPanel channelChooserPanel = new JPanel();
		channelChooserPanel.setLayout(new BoxLayout(channelChooserPanel,
				BoxLayout.X_AXIS));
		channelChooserPanel.setBackground(Color.white);
		channelChooserPanel.add(channelChooser);
		channelChooserPanel.add(Box.createHorizontalGlue());

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

		displayRange = new RangeSlider();
		displayRange.setMinorTickSpacing(1);
		displayRange.setMinimumSize(new Dimension(200, 40));
		displayRange.setPreferredSize(new Dimension(200, 40));
		displayRange.setFocusable(false);

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

		popupPanel.add(displayRange);
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
		setButton = newButton("Range");
		closeButton = newButton("Close");

		copyButton = newButton("Copy");

		JPanel histogramBtnpanel = new JPanel();
		histogramBtnpanel.setLayout(new BoxLayout(histogramBtnpanel, BoxLayout.X_AXIS));
		histogramBtnpanel.setMinimumSize(new Dimension(180, 40));
		histogramBtnpanel.add(Box.createHorizontalGlue());
		histogramBtnpanel.add(setButton);
		histogramBtnpanel.setOpaque(true);
		histogramBtnpanel.setBackground(Color.white);
		histogramBtnpanel.add(Box.createHorizontalGlue());
		
		JPanel btnpanel = new JPanel();
		btnpanel.setLayout(new BoxLayout(btnpanel, BoxLayout.X_AXIS));
		btnpanel.add(Box.createHorizontalGlue());
		btnpanel.add(autoallButton);
		btnpanel.add(copyButton);
		btnpanel.add(closeButton);
		btnpanel.add(Box.createHorizontalGlue());
		btnpanel.setMinimumSize(new Dimension(160, 40));
		btnpanel.setBackground(Color.white);

		JPanel copybtnpanel = new JPanel();
		copybtnpanel.setLayout(new BoxLayout(copybtnpanel, BoxLayout.X_AXIS));
		copybtnpanel.setMinimumSize(new Dimension(160, 40));
		copybtnpanel.setPreferredSize(new Dimension(160, 40));
		copybtnpanel.setBackground(Color.white);

		JPanel l1 = newTitlePanel("Select Channel", true);

		JLabel l2 = new JLabel("Choose color: ");
		l2.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JLabel l3 = new JLabel("Set contrast:  ");
		l3.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JPanel gammaLabelPanel = new JPanel();
		gammaLabelPanel.setLayout(new BoxLayout(gammaLabelPanel,
				BoxLayout.X_AXIS));

		gammaLabel = new JLabel();

		gammaLabelPanel.add(Box.createVerticalGlue());
		gammaLabel.setFont(monoFont);
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

		panel1.add(l1);
		panel1.add(channelChooserPanel);
		panel1.add(channelLabelPanel);
		panel1.add(Box.createVerticalStrut(5));
		panel1.add(histogramPanel);
		panel1.add(labelAllowedPanel);
		panel1.add(Box.createVerticalStrut(5));
		panel1.add(newTitlePanel(" Contrast", true));
		panel1.add(Box.createVerticalStrut(5));
		panel1.add(rangeSlider);
		panel1.add(labelPanel);
		panel1.add(histogramBtnpanel);
		panel1.add(Box.createVerticalStrut(5));
		
		panel2.add(newTitlePanel(" Gamma", true));
		panel2.add(Box.createVerticalStrut(5));
		panel2.add(gammaSlider);
		panel2.add(gammaTicksPanel);
		panel2.add(gammaLabelPanel);
		panel2.add(Box.createVerticalStrut(5));
		
		popup = new JidePopup();
		popup.setMovable(false);
		popup.getContentPane().add(popupPanel);
		popup.setDefaultFocusComponent(popupPanel);
		popup.setOwner(setButton);

		panel.add(panel1);
		panel.add(panel2);
		panel.add(btnpanel);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.addAncestorListener(new AncestorListener() {
			public void ancestorRemoved(AncestorEvent event) {
			}
			public void ancestorMoved(AncestorEvent event) {
			}
			public void ancestorAdded(AncestorEvent event) {
				update();
			}
		});

	}
	
	public JPanel newTitlePanel(String title, boolean hasGlue) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setMaximumSize(new Dimension(800, 15));
		JLabel l = new JLabel(title);
		Font f = l.getFont();
		l.setFont(new Font(f.getName(), Font.BOLD, f.getSize()+1 ));
		p.add(l);
		if(hasGlue){
			p.add(Box.createHorizontalGlue());
		}
		return p;
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

	public void setSelectedChannel(int index) {
		channelChooser.setSelectedIndex(index);
	}
	
	public JDialog createContrastDialog() {
		contrastDialog = new JDialog(Tool.getToolFrame(), false);
		contrastDialog.setTitle("Contrast control");
		contrastDialog.setLocation(300, 30);

		contrastDialog.add(this.getComponent());
		contrastDialog.pack();
		contrastDialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				contrastDialog.setVisible(false);
			}
		});
		return contrastDialog;
	}

	public void addChangeListeners() {
		rangeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				if (!updating && !disabled) {
					Contrast contrast = new Contrast(depth, rangeSlider
							.getLowValue(), rangeSlider.getHighValue());
					channels.get(selectedChannel).setContrast(contrast);
					
					imageViewer.setProperty("channels", channels);
				}
			}
		});

		gammaSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				if (!updating && !disabled) {
					double gammaval = gammaSlider.getScaledValue();
					for (Channel channel : channels) {
						channel.getContrast().setGamma(
								new Gamma(depth, gammaval));
					}

					imageViewer.setProperty("channels", channels);
				}
			}
		});

		displayRange.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (!updating && !disabled) {
					updateContrasts();
				}

			}
		});

		channelChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!updating && !disabled) {
					updating = true;
					int temp = getSelectedChannel();
					if (selectedChannel != temp) {
						selectedChannel = temp;

						channelColorChooser.setSelectedColor(new Color(channels
								.get(selectedChannel).getColor()));
						updateContrasts();
					}
					updating = false;
				}
			}
		});

		channelColorChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!updating && !disabled) {
					updating = true;
					int temp = channelColorChooser.getSelectedColor().getRGB();

					if (temp != channels.get(selectedChannel).getColor()) {
						channels.get(selectedChannel).setColor(temp);
						
						imageViewer.setProperty("channels", channels);
					}
					updating = false;

				}

			}
		});

		setButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.updateUI();

				popup.setResizable(false);
				popup.setMovable(false);
				if (popup.isPopupVisible()) {
					popup.hidePopup();
				} else {
					popup.showPopup();
				}
			}
		});

		autoallButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ImageViewer) imageViewer).actionAutoContrast();
			}
		});

		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((ImageViewer) imageViewer).actionCopyContrast();
			}
		});
		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contrastDialog.setVisible(false);
			}
		});
	}

	public void disableAll() {
		channelChooser.setEnabled(false);
		channelColorChooser.setEnabled(false);
		rangeSlider.setEnabled(false);
		gammaSlider.setEnabled(false);
		disabled = true;
	}

	public void enableAll() {
		channelChooser.setEnabled(true);
		channelColorChooser.setEnabled(true);
		rangeSlider.setEnabled(true);
		gammaSlider.setEnabled(true);
		disabled = false;
	}

	public Component getComponent() {
		final JScrollPane scrollPane = new JScrollPane(panel);
		// // do not force hiding is avoidable
		// scrollPane
		// .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}

	public int getSelectedChannel() {
		for (int i = 0; i < channels.size(); i++) {

			if (channels.get(i).getName()
					.equals(channelChooser.getSelectedItem())) {
				return i;
			}
		}
		return 0;
	}

	public String getTitle() {
		return "Contrast/Color";
	}

	public void modelChanged(ModelEvent e) {
		if (panel.isShowing()) update();
	}

	private void update() {
		updating = true;

		int site = (Integer) imageViewer.getProperty("site");

		List<IRecordVO> records = (List<IRecordVO>) imageViewer
				.getProperty("records");
		IRecordVO record;
		if (records.size() == 1) {
			record = records.get(0);
		} else {
			record = records.get(site);
		}

		channels = (List<Channel>) imageViewer.getProperty("channels");

		int channelChooserState = channelChooser.getSelectedIndex();
		channelChooser.removeAllItems();

		int count = 0;
		for (int i = 0; i < channels.size(); i++) {
			channelChooser.addItem(channels.get(i).getName());
			count++;
		}

		if (count == 0) {
			disableAll();
			updating = false;
			return;
		} else {
			enableAll();
		}

		depth = record.getDepth();
		currentRecordId = record.getId();

		if (channelChooserState != -1 && channelChooserState < count) {
			channelChooser.setSelectedIndex(channelChooserState);
		} else {
			channelChooser.setSelectedIndex(0);
		}

		selectedChannel = getSelectedChannel();
		channelColorChooser.setSelectedColor(new Color(channels.get(
				selectedChannel).getColor()));

		updateContrasts();

		updating = false;

	}

	public void updateContrasts() {

		// split slider update from histogram update;
		UpdateContrastCommand command = new UpdateContrastCommand();
		try {
			command.doInBackground();
		} catch (Exception e) {
			e.printStackTrace();
		}
		command.done();
//		CommandQueue.getInstance().enqueue(command);
		
	}

	class UpdateContrastCommand extends BasicCommand{
		
		public static final String UPDATE_CONTRAST = "update_contrast";
		private int maxContrast;
		private int minContrast;
		private ImageStatistics ha;
		private double gammaValue;
		
		
		public UpdateContrastCommand() {
			super(UPDATE_CONTRAST);
			// TODO Auto-generated constructor stub
		}
		
		protected Object doInBackground() throws Exception {
			updating = true;
			// TODO Auto-generated method stub
			ImageModel iModel = (ImageModel) model;
			selectedChannel = getSelectedChannel();
			
			pixels = IndexAccessDelegate.getPixelData(currentRecordId, iModel.getSite(),
					iModel.getFrame(), iModel.getSlice(), selectedChannel, iModel.getRecordVO(currentRecordId));
			
			if (pixels == null) {
				return null;
			}
			
			Contrast contrast = channels.get(selectedChannel).getContrast();

			maxContrast = (Integer) contrast.getContrastMax();
			minContrast = (Integer) contrast.getContrastMin();
			gammaValue = contrast.getGamma().getGammaValue();

			ha = new ImageStatistics(pixels);
			return null;
		}
		
		@Override
		protected void done() {
			
			if(pixels == null){
				updating = false;
				disableAll();
				return;
			}
			
			displayRange.setMaximum(pixels.defaultMax());

			int max = displayRange.getHighValue();
			int min = displayRange.getLowValue();

			if (maxContrast > max) {
				max = maxContrast;
				displayRange.setHighValue(max);
			}
			if (minContrast < min) {
				min = minContrast;
				displayRange.setLowValue(min);
			}

			maxLabel.setText("W: " + maxContrast);
			minLabel.setText("B: " + minContrast);

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

			histogramPanel.contrastMax = maxContrast - min;
			histogramPanel.contrastMin = minContrast - min;
			histogramPanel.histogram = ImageStatistics.sliceHistogram(
					ha.getHistorgam(), max, min);
			histogramPanel.maxY = ha.getMaxFrequency();
			histogramPanel.channelColor = new Color(channels.get(selectedChannel)
					.getColor());

			histogramPanel.repaint();
			rangeSlider.revalidate();
			gammaSlider.revalidate();
			
			updating = false;
		}
	}*/
}

class DoubleSlider extends JSlider{
	
	private static final long serialVersionUID = -4813447914149447924L;
	
	private final int scale;
	
	public DoubleSlider(int min, int max, int value, int scale) {
		super(min*scale, max*scale, value*scale);
		this.scale = scale;
	}
	
    public void setScaledValue(double value) {
    	super.setValue((int) (value * this.scale) );
	}

	public double getScaledValue() {
        return ((double)super.getValue()) / this.scale;
    }

}

