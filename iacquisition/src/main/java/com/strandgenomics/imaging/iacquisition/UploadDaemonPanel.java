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

package com.strandgenomics.imaging.iacquisition;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class UploadDaemonPanel extends JPanel {
	/**
	 * table model that shows the experiments being uploaded
	 */
	private UploadDaemonTableModel dataModel;

	public UploadDaemonPanel(UploadDaemonTableModel uploaderModel) {
		setLayout(new BorderLayout());
		setName("uploadDaemon");
		
		dataModel = uploaderModel;

		createUI();
	}

	private void createUI() {
		JScrollPane scrollPane = createScrollPane();
		add(scrollPane, BorderLayout.CENTER);
	}

	private JScrollPane createScrollPane() {
		final JTable uploaderTable = new JTable();
		
		UploaderRenderer renderer = new UploaderRenderer();
		uploaderTable.setDefaultRenderer(JProgressBar.class, renderer);
		
		uploaderTable.setModel(dataModel);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setToolTipText("Refresh Spreadsheet");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModel.refresh();
			}
		});
		
		JButton clearButton = new JButton("Clear Completed");
		clearButton.setToolTipText("Clear Completed Experiments");
		clearButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					dataModel.clearUploaded();
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		
		JButton cancelSelected = new JButton("Cancel Selected");
		cancelSelected.setToolTipText("Cancel Seleceted Uploads");
		cancelSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModel.cancelSelected(uploaderTable.getSelectedRows());
			}
		});
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		btnPanel.add(Box.createHorizontalGlue());
		btnPanel.add(clearButton);
		btnPanel.add(cancelSelected);
		btnPanel.add(refreshButton);
		
		add(btnPanel, BorderLayout.PAGE_END);
		
		JScrollPane scrollPane = new  JScrollPane(uploaderTable);
		return scrollPane;
	}
}
