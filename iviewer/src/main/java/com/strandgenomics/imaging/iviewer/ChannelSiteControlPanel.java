package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.swing.JideButton;
import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.icore.Site;
import com.strandgenomics.imaging.icore.VODimension;
import com.strandgenomics.imaging.icore.image.LutLoader;

@SuppressWarnings("serial")
public class ChannelSiteControlPanel extends JPanel{
	
	private static final int MAX_CHANNEL_NAME_LENGTH = 40;
	private String[] channelNames;
	private boolean[] enabledChannels;
	private List<IChannel> channels;

	private volatile boolean updating = false;
	private List<JCheckBox> channelButtonGroup;
	private List<JCheckBox> overlayButtonGroup;
	private ButtonGroup siteButtonGroup;

	private List<Site> sites;
	private String[] siteNames;
	private int site;
	
	private JDialog contrastDialog;

	private final ContrastSlidersPanel contrastPanel;
	
	private boolean moviePlayingState = true;
	private boolean editOverlayState = true;

	private int channel_state;
	private int slice_state;
	private int movie_state;
	
	private ImageViewerApplet imageViewerApplet;
	
	private boolean emptyImageView = false;
	

	public ChannelSiteControlPanel(ImageViewerApplet imageViewerApplet) {
		setLayout(new BorderLayout());
		
		this.imageViewerApplet = imageViewerApplet;

		update();
		
		contrastPanel = new ContrastSlidersPanel(imageViewerApplet);
		createContrastDialog();

		this.addAncestorListener(new AncestorListener() {
			public void ancestorRemoved(AncestorEvent event) {
			}
			public void ancestorMoved(AncestorEvent event) {
			}
			public void ancestorAdded(AncestorEvent event) {
					update();
			}
		});

	}

	private JPanel newJPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Color.white);
		p.setMaximumSize(new Dimension(50, 100));
		return p;
	}

	private JDialog createContrastDialog() {
		contrastDialog = contrastPanel.createContrastDialog();
		return contrastDialog;
	}

	public void initialize() {
		channel_state = imageViewerApplet.getChannelState();
		slice_state = imageViewerApplet.getSliceState();
		movie_state = imageViewerApplet.getMovieState();
	}

	public void addChangeListeners() {
	}

	synchronized void updateChannels(int index) {

		if (!updating) 
		{
			List<IChannel> selectedChannels = new ArrayList<IChannel>();
			boolean[] enabled = new boolean[channelNames.length];

			for (int i = 0; i < channelButtonGroup.size(); i++) {
				enabled[i] = channelButtonGroup.get(i).isSelected();
			}

			if (!Arrays.equals(enabledChannels, enabled)) {
				enabledChannels = enabled;

				for (int i = 0; i < channels.size(); i++) {
//					channels.get(i).setEnabled(enabledChannels[i]);
					if(enabledChannels[i])
						selectedChannels.add(channels.get(i));
				}

				imageViewerApplet.setSelectedChannels(selectedChannels);
			}
		}
	}

	public synchronized void updatePanel() {
		List<IRecord> records = imageViewerApplet.getRecords();
		
		if(records==null || records.size()<=0 || records.get(0)==null)
			emptyImageView = true;
		else
			emptyImageView = false;
		
		channel_state = imageViewerApplet.getChannelState();
		slice_state = imageViewerApplet.getSliceState();
		movie_state = imageViewerApplet.getMovieState();
		
		if (isShowing())
			update();
	}
	
	public synchronized void update() {
		updating = true;

		JPanel p = new JPanel();
		p.setBackground(Color.white);
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		// -- channel --
		updateChannelPanel(p);
		
		// -- site --
		updateSitePanel(p);
		
		// -- overlay --
		updateOverlayPanel(p);
		
		// -- info --
		updateInfoPanel(p);
		
		removeAll();
		
		add(p);
		revalidate();

		updating = false;
	}
	
	private void vaToggleAction(ActionEvent e) {
		openVA(e);
	}

	private void openVA(ActionEvent e) {
		String ovName = ((JButton) e.getSource()).getActionCommand();

		if (ovName.equals("OPEN")) {
			imageViewerApplet.createOverlay();
		} else {
			imageViewerApplet.actionEditOverlay(ovName);
		}
	}
	
	private boolean isVaStateOff() {
		return imageViewerApplet.getVAState() == ImageViewerState.VAState.VA_OFF;
	}
	
	private void updateOverlayPanel(JPanel p) {
		int vaState = imageViewerApplet.getVAState();
		final List<String> overlayNames = imageViewerApplet.getOverlayNames();
		boolean overlayEnabled = (vaState != ImageViewerState.VAState.VA_ON);
		List<Boolean> enabledOverlays = imageViewerApplet.getOverlaysEnabled();
		
		ArrayList<JButton> overlayRelatedButtons = new ArrayList<JButton>();
		
		JideButton newVaButton = UIUtils.createCommandBarButton("Visual_Annotation.png", "Click to Create New Overlay ");
		newVaButton.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		newVaButton.setActionCommand("OPEN");
		newVaButton.setEnabled(!emptyImageView);
		newVaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vaToggleAction(e);
			}
		});
		overlayRelatedButtons.add(newVaButton);

		JPanel overlayTitle = UIUtils.newTitlePanel(" Overlay    ", true);
		overlayTitle.add(newVaButton);
		overlayTitle.add(Box.createHorizontalStrut(5));

		JPanel overlayChooser = newJPanel();
		
		p.add(overlayTitle);
		p.add(Box.createVerticalStrut(10));
		JScrollPane jsp= new JScrollPane(overlayChooser);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(50, 40));
		
		p.add(jsp);
		
		overlayButtonGroup = new ArrayList<JCheckBox>();
		for (int i = 0; i < overlayNames.size(); i++) {
			String name = overlayNames.get(i);
			JPanel bp = newButtonPanel();
			JCheckBox b = new JCheckBox();
			b.setEnabled(overlayEnabled);
			b.setActionCommand(((Integer) i).toString());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateOverlays(new Integer(e.getActionCommand()), overlayNames);
				}
			});
			b.setSelected(enabledOverlays.get(i));
			b.setBackground(Color.white);
			b.setEnabled(moviePlayingState);
			bp.add(b);
			overlayButtonGroup.add(b);
			bp.add(new JLabel(name));
			bp.add(Box.createHorizontalGlue());

			JideButton editButton = UIUtils.createCommandBarButton("va3.png", "Click to Edit Overlay : " + name);
			editButton.setActionCommand(name);
			editButton.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vaToggleAction(e);
				}
			});

			editButton.setSelected(!isVaStateOff() && enabledOverlays.get(i));
			overlayRelatedButtons.add(editButton);

			JideButton deleteVaButton = UIUtils.createCommandBarButton("delete.png", "Click to Delete Overlay : " + name);
			deleteVaButton.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			deleteVaButton.setActionCommand(name);
			deleteVaButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String overlayname = ((JButton) e.getSource()).getActionCommand();
					if (overlayname != null) {
						imageViewerApplet.deleteOverlay(overlayname);
					}
				}
			});
			overlayRelatedButtons.add(deleteVaButton);
			
			JideButton searchVaButton = UIUtils.createCommandBarButton("search.png", "Click to Search Overlay : " + name);
			searchVaButton.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			searchVaButton.setActionCommand(name);
			searchVaButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String overlayname = ((JButton) e.getSource()).getActionCommand();
					if (overlayname != null) {
						List<VODimension> locations = imageViewerApplet.searchOverlay(overlayname);
						showLocationsDialog(locations);
					}
				}
			});
			overlayRelatedButtons.add(searchVaButton);
			
			bp.add(searchVaButton);
			bp.add(Box.createHorizontalStrut(10));
			bp.add(editButton);
			bp.add(Box.createHorizontalStrut(10));
			bp.add(deleteVaButton);
			bp.add(Box.createHorizontalStrut(5));

			overlayChooser.add(bp);
		}
		overlayChooser.add(Box.createVerticalGlue());

//		setEditOverlayButtons(editOverlayState && moviePlayingState);
	}
	
	private void showLocationsDialog(List<VODimension> locations)
	{
		if(locations == null || locations.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Overlay is empty");
			return;
		}
		
		java.awt.List list = new java.awt.List();
		for(VODimension location:locations)
		{
			list.add("Frame="+(location.frameNo+1)+", Slice="+(location.sliceNo+1));
		}
		
		int val = JOptionPane.showConfirmDialog(null, list, "Result", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		if(val==JOptionPane.OK_OPTION)
		{
			String selected = list.getSelectedItem();
			String fields[] = selected.split(",");
			int frame = Integer.parseInt(fields[0].split("=")[1]);
			int slice = Integer.parseInt(fields[1].split("=")[1]);
			
			imageViewerApplet.setFrame(frame - 1);
			imageViewerApplet.setSlice(slice - 1);
		}
	}

	private void updateOverlays(Integer integer, List<String> overlayNames) {
		Boolean[] voEnabled = new Boolean[overlayNames.size()];

		for (int i = 0; i < overlayButtonGroup.size(); i++) {
			voEnabled[i] = overlayButtonGroup.get(i).isSelected();
		}

		List<Boolean> b = Arrays.asList(voEnabled);
		imageViewerApplet.setOverlaysEnabled(b);
	}
	
	private void updateSites(int index){
		if (!updating) {
			if (site != index) {
				site = index;
				imageViewerApplet.setSite(site);
			}
		}
	}

	private void updateInfoPanel(JPanel p) {
		JPanel infoTitlePanel = UIUtils.newTitlePanel("Some controls are disabled as ", true);
		JPanel infoPanel = newJPanel();
		
		String[] errAll = {"Movie is ON","Tile Sites is ON","Tile Channel is ON","Z-Projection is ON"};
		ArrayList<String> errCurr = new ArrayList<String>();
//		if(movie_state==MovieState.PLAYING)
//			errCurr.add(errAll[0]);
		if(channel_state==ImageViewerState.ChannelState.MULTI_CHANNEL)
			errCurr.add(errAll[2]);
		if(slice_state==ImageViewerState.SliceState.Z_STACK)
			errCurr.add(errAll[3]);
		
		infoPanel.add(new JList(errCurr.toArray(new String[errCurr.size()])));
		infoPanel.setVisible(!editOverlayState||!moviePlayingState);
		
		infoTitlePanel.add(Box.createHorizontalStrut(5));
		infoTitlePanel.setVisible(!editOverlayState||!moviePlayingState);
		
		p.add(infoTitlePanel);
		p.add(infoPanel);
	}
	
	private void updateSitePanel(final JPanel p) {

		int siteState = imageViewerApplet.getSiteState();
		int vaState = imageViewerApplet.getVAState();

		boolean siteEnabled = (siteState != ImageViewerState.SiteState.MULTI_SITE)
				&& (vaState != ImageViewerState.VAState.VA_ON);

		List<Site> sites = imageViewerApplet.getSites();
		site = imageViewerApplet.getSite();
		siteNames = new String[sites.size()];

		JPanel siteTitle = UIUtils.newTitlePanel(" Site          ", true);
		p.add(siteTitle);
		p.add(Box.createVerticalStrut(10));

		JPanel siteChooser = newJPanel();
//		siteChooser.setPreferredSize(new Dimension(50, 50));
		JScrollPane jsp= new JScrollPane(siteChooser);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(50, 40));
		p.add(jsp);
		
		siteButtonGroup = new ButtonGroup();
		for (int i = 0; i < sites.size(); i++) {
			siteNames[i] = sites.get(i).getName();
			String name = siteNames[i];
			JPanel bp = newButtonPanel();
			JCheckBox b = new JCheckBox();
			b.setEnabled(siteEnabled);
			b.setActionCommand(((Integer) i).toString());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateSites(new Integer(e.getActionCommand()));
				}
			});
			b.setSelected(i == site);
			b.setBackground(Color.white);
			bp.add(b);
			siteButtonGroup.add(b);
			bp.add(new JLabel(name));
			bp.add(Box.createHorizontalGlue());
			siteChooser.add(bp);
		}
		siteChooser.add(Box.createVerticalGlue());
	}

	private void updateChannelPanel(final JPanel parentPanel) 
	{
		JPanel channelChooser = newJPanel();

		JScrollPane scrollPane = new JScrollPane(channelChooser);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(50, 40));
		
		channels = imageViewerApplet.getChannels();
		List<IChannel> selectedChannels = imageViewerApplet.getSelectedChannels();
		channelNames = new String[channels.size()];
		enabledChannels = new boolean[channels.size()];

		for (int i = 0; i < channelNames.length; i++) {
			channelNames[i] = channels.get(i).getName();
			enabledChannels[i] = selectedChannels.contains(channelNames[i]);
		}

		int channelState = imageViewerApplet.getChannelState();
		boolean channelEnabled = channelState != ImageViewerState.ChannelState.MULTI_CHANNEL;
		
		JPanel channelTitle = UIUtils.newTitlePanel(" Channel   ", true);

		JButton resetContrast = UIUtils.createCommandBarButton("reset.gif", "Reset Channels");
		resetContrast.setBorder(BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		resetContrast.setToolTipText("Reset Contrast");
		resetContrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IRecord record = imageViewerApplet.getRecords().get(0);
				String[] originalLut = imageViewerApplet.getOriginalChannelLUTs();
				for (int i = 0; i < channels.size(); i++) {
					record.setCustomContrast(imageViewerApplet.getSliceState() == ImageViewerState.SliceState.Z_STACK, i, null);
					record.setChannelLUT(i, originalLut[i]);
				}
				imageViewerApplet.refresh();
				update();
			}
		});
		resetContrast.setEnabled(moviePlayingState && !emptyImageView);
		
		channelTitle.add(resetContrast);
		channelTitle.add(Box.createHorizontalStrut(5));
		parentPanel.add(channelTitle);
		parentPanel.add(Box.createVerticalStrut(10));
		parentPanel.add(scrollPane);
		
		channelButtonGroup = new ArrayList<JCheckBox>();
		
		for (int i = 0; i < channels.size(); i++) {

			final IChannel channel = channels.get(i);
			JPanel bp = newButtonPanel();
			final JCheckBox b = new JCheckBox();
			b.setEnabled(channelEnabled);

			b.setActionCommand(((Integer) i).toString());
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int cnt = 0;
					for(JCheckBox button:channelButtonGroup)
					{
						if(button.isSelected())
							cnt++;
					}
					if(cnt == 0)
					{
						b.setSelected(true);
						return;
					}
					
					updateChannels(new Integer(e.getActionCommand()));
				}
			});
			b.setSelected(selectedChannels.contains(channel));
			b.setBackground(Color.white);
			channelButtonGroup.add(b);
			bp.add(b);
			String channelName = channel.toString();
			if(channelName.length() > MAX_CHANNEL_NAME_LENGTH)
				channelName = "Channel " + i;
			final JTextField label = new JTextField(8);
			label.setPreferredSize(new Dimension(20, 16));
			label.setText(channelName);
			label.setToolTipText(label.getText());
			label.setEditable(false);
			
			LabelEditorListener labelListener = new LabelEditorListener(label, i);
			label.addKeyListener(labelListener);
			label.addMouseListener(labelListener);
			label.addFocusListener(labelListener);
			
			bp.add(label);
			bp.add(Box.createHorizontalStrut(5));
			
			b.setEnabled(moviePlayingState && channelState==ImageViewerState.ChannelState.OVERLAY);
			
			JButton contrast = UIUtils.createCommandBarButton("contrast.png", "Constrast");
			contrast.setBorder(BorderFactory
					.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
			contrast.setToolTipText("Contrast");
			contrast.setActionCommand(((Integer) i).toString());
			contrast.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = new Integer(e.getActionCommand());
					contrastDialog.setVisible(true);
					contrastPanel.setSelectedChannel(index);
				}
			});
			contrast.setEnabled(moviePlayingState && !emptyImageView);
			
			List<String> luts = LutLoader.getInstance().getAvailableLUTs();
			final String lutNames[] = new String[luts.size()];
			ImageIcon icons[] = new ImageIcon[luts.size()];
			int cnt = 0;
			int selectedIndex = 0;
			for(String lut:luts) 
			{
				lutNames[cnt] = lut;
				icons[cnt] = new ImageIcon(LutLoader.getInstance().getLUTImage(lut).getScaledInstance(64, 16, java.awt.Image.SCALE_DEFAULT));
				
				if(lut.equals(channel.getLut()))
					selectedIndex = cnt;
				cnt++;
			}
			
			final JComboBox lutComboBox = new JComboBox(icons);
			lutComboBox.setRenderer(new LUTComboBoxRenderer(lutNames, icons));
			lutComboBox.setActionCommand(((Integer) i).toString());
			lutComboBox.setSelectedIndex(selectedIndex);
			lutComboBox.setEnabled(moviePlayingState && channelState==ImageViewerState.ChannelState.OVERLAY);
			lutComboBox.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int lutIndex = lutComboBox.getSelectedIndex();

					System.out.println(lutIndex+" "+lutNames[lutIndex]);
					
					int index = new Integer(e.getActionCommand());

					imageViewerApplet.getRecords().get(0).setChannelLUT(index, lutNames[lutIndex]);
					imageViewerApplet.refresh();
				}
			});
			
			
			bp.add(contrast);
			bp.add(Box.createHorizontalStrut(5));
			bp.add(lutComboBox);
			bp.add(Box.createHorizontalStrut(5));

			channelChooser.add(bp);

		}
		channelChooser.add(Box.createVerticalGlue());
	}

	private JPanel newButtonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBackground(Color.white);
		p.setMaximumSize(new Dimension(800, 30));
		return p;
	}
	
	private ColorComboBox newColorButton(int c) {
		ColorComboBox box = new ColorComboBox();
		box.setAllowDefaultColor(true);
		box.setSelectedColor(new Color(c));
		box.setColorValueVisible(false);
		box.setMaximumSize(new Dimension(50, 25));
		box.setPreferredSize(new Dimension(50, 25));
		return box;
	}
	
	/**
	 * LUT Combobox renderer to display LUT image and LUT name as tooltip
	 * 
	 * @author Anup Kulkarni
	 */
	private class LUTComboBoxRenderer extends DefaultListCellRenderer {
		private String[] tooltips;
		private ImageIcon[] icons;

		public LUTComboBoxRenderer(String tooltips[], ImageIcon icons[]) {
			this.tooltips = tooltips;
			this.icons = icons;
			
			setSize(32, 16);
			setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			JComponent comp = (JComponent) super.getListCellRendererComponent(list,
	                value, index, isSelected, cellHasFocus);

	        if (-1 < index && null != value && null != tooltips) {
	                    list.setToolTipText(tooltips[index]);
	                }
	        return comp;

		}
	  }
	
	/**
	 * Listens to MouseEvent and KeyEvent. Labels are editable on double click.
	 * On Esc key original text is retained, on Enter new text is set
	 * 
	 * @author Anup Kulkarni
	 */
	private class LabelEditorListener extends MouseAdapter implements KeyListener, FocusListener{
		/**
		 * index in list; used for fetching from record
		 */
		int index = -1;
		/**
		 * source of events
		 */
		JTextField label; 
		/**
		 * original text before editing the label
		 */
		String originalText;
		/**
		 * channel name
		 */
		String channelName;
		
		public LabelEditorListener(JTextField label, int index) {
			IChannel channel = imageViewerApplet.getRecords().get(0).getChannel(index);
			this.channelName = channel.getName();
			this.label = label;
			this.index = index;
			originalText = "";
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			if(e.getClickCount()>1) 
			{
				IRecord record = imageViewerApplet.getRecords().get(0);
				originalText = label.getText();
				if(record.getUploadTime()!=null || (record.getParentProject()!=null && !record.getParentProject().getName().isEmpty())){
					JOptionPane.showMessageDialog(null, "Cant Modify Read-only Record", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				label.setText(channelName);
				label.setEditable(true);
			}
				
		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			if(!label.isEditable()) return;
			
			if(e.getKeyCode()==KeyEvent.VK_ENTER) 
			{
				saveChannelName();
			}
			else if(e.getKeyCode()==KeyEvent.VK_ESCAPE) 
			{
				restoreOriginal();
			}
		}

		@Override
		public void keyReleased(KeyEvent e){}
		
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void focusGained(FocusEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			if(!label.isEditable()) return;
			
			int value = JOptionPane.showConfirmDialog(null, "Do you want to change channel name?", "Channel Edit", JOptionPane.YES_NO_OPTION);
			if(value == JOptionPane.YES_OPTION)
				saveChannelName();
			else
				restoreOriginal();
		}
		
		private void restoreOriginal()
		{
			label.setText(originalText);
			label.setEditable(false);
		}
		
		private void saveChannelName()
		{
			IChannel channel = imageViewerApplet.getRecords().get(0).getChannel(index);
			if(channel instanceof Channel)
				((Channel)channel).setName(label.getText());
			label.setText(channel.toString());
			label.setEditable(false);
		}
	}

}
