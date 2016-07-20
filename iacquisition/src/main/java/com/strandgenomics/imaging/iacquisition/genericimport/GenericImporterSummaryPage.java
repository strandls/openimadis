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

package com.strandgenomics.imaging.iacquisition.genericimport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.swing.JideScrollPaneConstants;
import com.jidesoft.wizard.AbstractWizardPage;
import com.strandgenomics.imaging.iclient.local.genericimport.ImportFilter;
import com.strandgenomics.imaging.iclient.local.genericimport.Importer;
import com.strandgenomics.imaging.icore.bioformats.custom.FieldType;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;
import com.strandgenomics.imaging.icore.util.ProgressListener;

public class GenericImporterSummaryPage  extends AbstractWizardPage {

	/**
	 * model driving the wizard
	 */
	private GenericImporterWizardModel model;
	/**
	 * model driving the summary table
	 */
	private GenericImporterSummaryTableModel dm;
	/**
	 * model driving the file listing table
	 */
	private GenericImporterFileTableModel fm;
	
	public GenericImporterSummaryPage(String title, String description, GenericImporterWizardModel model) {
		super(title, description);
		this.model = model;
	}

	@Override
	public JComponent createWizardContent()
	{
		JSplitPane panel = createSummaryPanel();
        panel.setBorder(getContentThinBorder());
        return panel;
	}
	
	private JSplitPane createSummaryPanel()
	{
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(createLabelPane());
		splitPane.add(createFileListPane());
		
		splitPane.setDividerLocation(150);
		return splitPane;
	}

	private JScrollPane createFileListPane()
	{
		fm = new GenericImporterFileTableModel();
		JTable fileTable = new JTable(fm);
		
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(fm);
		fileTable.setRowSorter(sorter);
		
		JScrollPane scrollPanel = new JScrollPane(fileTable);
		scrollPanel.setVerticalScrollBarPolicy(JideScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		return scrollPanel;
	}

	private JScrollPane createLabelPane()
	{
		dm = new GenericImporterSummaryTableModel();
		final JTable summaryTable = new JTable(dm);
		summaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		summaryTable.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e)
			{
				model.setSelectedRecords(dm.getSelectedRecords());
			}
		});
		
		summaryTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		summaryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int firstIndex = summaryTable.getSelectedRow();
				RecordMetaData record = dm.getRecord(firstIndex);

				List<String>filenames = new ArrayList<String>();
				for(File imageFile : record.getFiles())
				{
					filenames.add(imageFile.getAbsolutePath());
				}
				
				fm.setData(filenames);
			}
		});
		
		JScrollPane scrollPanel = new JScrollPane(summaryTable);
		scrollPanel.setVerticalScrollBarPolicy(JideScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setHorizontalScrollBarPolicy(JideScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		return scrollPanel;
	}

	@Override
	public void setupWizardButtons()
	{
		fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
        fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.NEXT);
        fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.FINISH);
        
        extractRecordSpecifications();
	}

	private void updatePanel()
	{
		dm.setRecords(model.getRecords());
	}
	
	private GenericImporterProgressDialog progress = null;
	
	public void extractRecordSpecifications()
	{
		progress = new GenericImporterProgressDialog(model.getParent(), "Validating files");

		SwingWorker validator = new SwingWorker()
		{
			public Object doInBackground()
			{
				Collection<RecordMetaData> records = importRecords();
				model.setRecords(records);
				return null;
			}

			///called in the event dispatch thread, to report status
			public void done()
			{
				if(progress != null)
				{
					progress.setProgress(100);
					progress.setVisible(false);
				}
				progress = null;
				updatePanel();
			}
		};
		
		validator.execute();
		progress.setVisible(true);
	}
	
	private Collection<RecordMetaData> importRecords()
	{
		ProgressListener progressReport = new ProgressListener()
		{
			@Override
			public void reportProgress(String message, int min, int max, int value)
			{
				if(progress != null)
				{
					progress.setMessage(message);
					int p = (int)(value*100.0/max);
					progress.setProgress(p);
				}
			}
		};
		
		ImportFilter importFilter = new ImportFilter("Some Name", model.getSeparator(), model.getPositioToFieldMap());
		Importer importer = new Importer(importFilter);
		importer.addProgressListener(progressReport);
		
		boolean doValidation = model.doValidation();
		FieldType multiImageDimension = model.getMultiImageDimension();
		
		String seedFile = model.getSelectedFile();
		Collection<RecordMetaData> records = importer.generateRecords(new File(model.getRootDirectory()), seedFile, doValidation, multiImageDimension);
		return records;
	}
}
