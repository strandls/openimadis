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
