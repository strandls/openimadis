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
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.jidesoft.combobox.FileChooserComboBox;
import com.jidesoft.combobox.FileChooserPanel;
import com.jidesoft.combobox.PopupPanel;
public class AttachmentDialog extends JDialog {

    private JButton btnAdd;
    private JButton btnCancel;
    private boolean succeeded = false;
    
	private FileChooserComboBox fileChooserComboBox;
	private JTextArea tfNotes;
	
	private JLabel lbFileName;
    private JLabel lbNotes;

    public AttachmentDialog(Frame parent) {
        super(parent, "Add Attachment", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
    	

        cs.fill = GridBagConstraints.BOTH;
    	cs.insets = new Insets(3, 3, 3, 3);
        
        lbFileName = new JLabel("File Name: ", SwingConstants.RIGHT);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(lbFileName, cs);
        
        fileChooserComboBox = createFileChooserComboBox();
        cs.gridx = 2;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(fileChooserComboBox, cs);
        
        lbNotes = new JLabel("Notes:  ", SwingConstants.RIGHT);
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(lbNotes, cs);
        
        tfNotes = new JTextArea();
        tfNotes.setEditable(true);
        tfNotes.setLineWrap(true);
        tfNotes.setRows(2);
        cs.gridx = 2;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(tfNotes, cs);

        btnAdd = new JButton("Add");

        btnAdd.addActionListener(new ActionListener() {

            @Override
			public void actionPerformed(ActionEvent e) {
            	
                if (getFile()!=null) {
                	succeeded=true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AttachmentDialog.this,
                            "Please select a file to add",
                            "Add",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            @Override
			public void actionPerformed(ActionEvent e) {
            	succeeded=false;
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnAdd);
        bp.add(btnCancel);
        
        getRootPane().setDefaultButton(btnAdd);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

	public FileChooserComboBox createFileChooserComboBox() {
		FileChooserComboBox fileComboBox = new FileChooserComboBox() {
			public AbstractButton createButtonComponent() {
				AbstractButton cusomButton = new JButton("Browse");
				return cusomButton;
			}

			public PopupPanel createPopupComponent() {
				FileChooserPanel panel = new FileChooserPanel() {
					protected JFileChooser createFileChooser() {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fileChooser.setMultiSelectionEnabled(false);
						return fileChooser;
					}
				};

				panel.setTitle("Choose a file");
				return panel;
			};
		};
		fileComboBox.getEditor().getEditorComponent()
				.setName("editor:textfield");
		fileComboBox.getPopupButton().setName("editor:button");
		return fileComboBox;
	}
    
    
    public String getNotes() {
        return tfNotes.getText().trim();
    }


    public File getFile() {
    	Object obj = fileChooserComboBox.getSelectedItem();
		return (obj == null) ? null : ((File) obj);
       // return tfFileName.getText().trim();
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}

