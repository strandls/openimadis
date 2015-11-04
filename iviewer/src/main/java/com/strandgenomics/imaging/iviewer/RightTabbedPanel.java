package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jidesoft.swing.JideTabbedPane;

public class RightTabbedPanel extends JPanel {

	private JideTabbedPane tabbedPane;

	public RightTabbedPanel(ImageViewerApplet imageViewerApplet,
			ChannelSiteControlPanel cspanel,
			ImageMetaDataPanel imageMetadataPanel,
			RecordDynamicDataPanel recordMetadataPanel,
			CommentsPanel commentsPanel, UserAttachmentPanel attachmentPanel) {
		setLayout(new BorderLayout());

		tabbedPane = new JideTabbedPane();
		tabbedPane.setTabPlacement(JideTabbedPane.BOTTOM);
		tabbedPane.setBorder(BorderFactory.createEmptyBorder());

		createGUI(cspanel, imageMetadataPanel, recordMetadataPanel,
				commentsPanel, attachmentPanel);

		add(tabbedPane);
		setMinimumSize(new Dimension());
	}

	private void createGUI(ChannelSiteControlPanel cspanel,
			ImageMetaDataPanel imageMetadataPanel,
			RecordDynamicDataPanel recordMetadataPanel,
			CommentsPanel commentsPanel, UserAttachmentPanel attachmentPanel) {
		tabbedPane.addTab(null, UIUtils.getIcon("Channel_Site_control.png"),
				cspanel, "Channel Site Control Panel");
		tabbedPane.addTab(null, UIUtils.getIcon("image-metadata.png"),
				imageMetadataPanel, "Image MetaData Panel");
		tabbedPane.addTab(null, UIUtils.getIcon("record-metadata.png"),
				recordMetadataPanel, "Record MetaData Panel");
		tabbedPane.addTab(null, UIUtils.getIcon("attachment.gif"), attachmentPanel,
				"User Attachments Panel");
		tabbedPane.addTab(null, UIUtils.getIcon("Comment.png"), commentsPanel,
				"User Comments Panel");

	}

}
