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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.jidesoft.swing.JideButton;
import com.strandgenomics.imaging.icore.IRecord;

public class UserAttachmentPanel extends JPanel{
	
	private ImageViewerApplet imageViewerApplet;
	private boolean uploaded = true;

	public UserAttachmentPanel(ImageViewerApplet iviewer) {
		this.imageViewerApplet = iviewer;
		
		setLayout(new BorderLayout());
	}

	public void updatePanel() {
		removeAll();
		
		List<IRecord> records = imageViewerApplet.getRecords();
		if(records==null || records.size()<=0 || records.get(0)==null)
			return;
		
		JPanel p = new JPanel();
		p.setBackground(Color.white);
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		updateAttachmentPanel(p);

		add(p);
		revalidate();
	}
	
	private JPanel newJPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Color.white);

		return p;
	}

	private void updateAttachmentPanel(JPanel p) {
		final Map<String, File> attachmentsMap = imageViewerApplet.getAttachments();
		final Map<String, String> notesMap = imageViewerApplet.getAttachmentNotes();
		
		JideButton newVaButton = UIUtils.createCommandBarButton("expandall.png", "Click to Create New Attachment ");
		newVaButton.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		newVaButton.setActionCommand("OPEN");
		newVaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewAttachment();
			}
		});

		JPanel overlayTitle = UIUtils.newTitlePanel(" Attachment    ", true);
		overlayTitle.add(newVaButton);
		overlayTitle.add(Box.createHorizontalStrut(5));

		JPanel attachmentChooser = newJPanel();
		
		p.add(overlayTitle);
		p.add(Box.createVerticalStrut(10));
		p.add(attachmentChooser);
		
		Iterator<String> it = attachmentsMap.keySet().iterator();
		while(it.hasNext()){
			String absname = it.next();
			
//			File f = attachmentsMap.get(absname);
			
			String displayName = absname;
			if(displayName.indexOf("#")>0)
				displayName = absname.split("#")[1];
//			if(f!=null)
//				name = f.getName();
			
			String notes = notesMap.get(absname);
			
			JPanel bp = newButtonPanel();
			JLabel displayLabel = new JLabel(displayName);
			bp.add(displayLabel);
			bp.setToolTipText(notes);
			bp.add(Box.createHorizontalGlue());
			
			JideButton openButton = UIUtils.createCommandBarButton("openproject.png", "Click to Open Attachment : " + displayName);
			openButton.setActionCommand(absname);
			openButton.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			openButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String absname = e.getActionCommand();
					try {
						if(Desktop.isDesktopSupported())
							Desktop.getDesktop().open(attachmentsMap.get(absname));
						else
							JOptionPane.showMessageDialog(null,
									"This operation is not supported on this system.",
									"Error", JOptionPane.ERROR_MESSAGE);

						
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Default attachment viewer is not configured.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
//			openButton.setSelected(uploaded);

			JideButton saveButton = UIUtils.createCommandBarButton("save.png", "Click to Save Attachment : " + displayName);
			saveButton.setActionCommand(absname);
			saveButton.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			saveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String absname = e.getActionCommand();
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setDialogTitle("Select folder to save attachment");
					//fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
					int ret = fileChooser.showSaveDialog(null);
					if (ret == JFileChooser.APPROVE_OPTION) {
						File selectedDir = fileChooser.getSelectedFile();
						File inputFile = attachmentsMap.get(absname);
						File outputFile = new File(selectedDir + File.separator
								+ inputFile.getName());
						try {
							FileReader in = new FileReader(inputFile);
							FileWriter out = new FileWriter(outputFile);
							int c;
							while ((c = in.read()) != -1)
								out.write(c);
							in.close();
							out.close();
							JOptionPane.showMessageDialog(null, "Saved attachment in " + selectedDir +"." , "Info", JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Unable to save attachment.", "Error", JOptionPane.ERROR_MESSAGE);
						}
						// Save file here
					}
				}
			});
//			saveButton.setSelected(uploaded);

			JideButton deleteAttachmentButton = UIUtils.createCommandBarButton("delete.png", "Click to Delete Attachment : " + displayName);
			deleteAttachmentButton.setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			deleteAttachmentButton.setActionCommand(absname);
			deleteAttachmentButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					imageViewerApplet.deleteUserAttachment(e.getActionCommand());
				}
			});
			
			bp.add(openButton);
			bp.add(Box.createHorizontalStrut(5));
			bp.add(saveButton);
			bp.add(Box.createHorizontalStrut(5));
			bp.add(deleteAttachmentButton);
			bp.add(Box.createHorizontalStrut(5));

			attachmentChooser.add(bp);
		}
		attachmentChooser.add(Box.createVerticalGlue());
	}
	
	protected void addNewAttachment() {
		Map<File, String> attachment = RecordPropertyManager.addAttachments();
		Iterator<File> it = attachment.keySet().iterator();
		while(it.hasNext()){
			File selectedFile = it.next();
			String notes = attachment.get(selectedFile);
			imageViewerApplet.addUserAttachment(selectedFile, notes);
		}
	}

	private JPanel newButtonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBackground(Color.white);
		p.setMaximumSize(new Dimension(800, 30));
		return p;
	}
}
