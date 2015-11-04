package com.strandgenomics.imaging.iacquisition;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class DirectUploadPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7465056507716154683L;
	/**
	 * table model that holds the records being imported
	 */
	DirectUploadTableModel dataModel;

	public DirectUploadPanel(DirectUploadTableModel model) {
		setLayout(new BorderLayout());
		setName("importer");
		
		dataModel = model;

		createUI();
	}
	
	private void createUI() 
	{
		JScrollPane scrollPane = createScrollPane();
		add(scrollPane, BorderLayout.CENTER);
	}

	private JScrollPane createScrollPane() 
	{
		final JTable importPanel = new JTable();
		importPanel.setModel(dataModel);
		
		JButton cancelAll = new JButton("Stop All");
		cancelAll.setToolTipText("Stops All Imports");
		cancelAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModel.cancelAll();
			}
		});
		
		JButton cancelSelected = new JButton("Cancel");
		cancelSelected.setToolTipText("Stop Import ");
		cancelSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModel.cancelSelected(importPanel.getSelectedRows());
			}
		});
		
		JButton reindexSelected = new JButton("Upload");
		reindexSelected.setToolTipText("Start Upload");
		reindexSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModel.uploadSelected(importPanel.getSelectedRows());
			}
		});
		
		JPanel btnPanel = new JPanel();
//		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
//		btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//		btnPanel.add(Box.createHorizontalGlue());
//		btnPanel.add(cancelSelected);
//		btnPanel.add(Box.createHorizontalStrut(5));
//		btnPanel.add(cancelAll);
//		btnPanel.add(Box.createHorizontalStrut(5));
//		btnPanel.add(reindexSelected);
		
		add(btnPanel, BorderLayout.PAGE_END);
		
		JScrollPane scrollPane = new  JScrollPane(importPanel);
		return scrollPane;
	}
}
