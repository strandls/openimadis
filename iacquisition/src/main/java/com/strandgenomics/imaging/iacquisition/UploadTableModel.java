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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.strandgenomics.imaging.icore.IExperiment;

/**
 * Table model for showing status of the current uploading tasks
 * 
 * @author Anup Kulkarni
 */
public class UploadTableModel extends DefaultTableModel {
	/**
	 * list of experiments to be uploaded
	 */
	private List<IExperiment> experiments;

	/**
	 * list of progress bars to be uploaded the progress value of each progress
	 * bar are listened from Uploader task of each experiment
	 */
	private List<JProgressBar> progressBar;

	/**
	 * the columns shown in Upload table
	 */
	String columnNames[] = { "Source File", "Source File Size", "No. of Records", "Target Project", "Status" };

	/**
	 * name of the target projects associated with every experiment
	 */
	private List<String> projectNames;

	/**
	 * holds the indices of rows selected in upload table
	 */
	private int[] selectedRows;
	
	private boolean cancelEvent = false;

	public UploadTableModel() {
		experiments = new ArrayList<IExperiment>();
		progressBar = new ArrayList<JProgressBar>();
		projectNames = new ArrayList<String>();
	}

	/**
	 * custom implementation of addRow(data) method of table model
	 * 
	 * @param experimentsToUpload
	 *            the experiment to be uploaded
	 */
	public void addExperiment(List<IExperiment> experimentsToUpload,
			List<JProgressBar> progressMonitors, List<String> projectName) {
		experiments.addAll(experimentsToUpload);
		progressBar.addAll(progressMonitors);
		projectNames.addAll(projectName);

		for (int i = 0; i < progressMonitors.size(); i++) {
			progressMonitors.get(i).addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					fireTableDataChanged();
				}
			});
		}

		fireTableChanged(new TableModelEvent(this));
	}

	@Override
	public int getRowCount() {
		if (experiments == null)
			return 0;
		return experiments.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return String.class;
		if (columnIndex == 1)
			return String.class;
		if (columnIndex == 2)
			return Integer.class;
		if (columnIndex == 3)
			return String.class;
		if (columnIndex == 4)
			return JProgressBar.class;
		else
			return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (experiments == null)
			return 0;
		IExperiment expr = experiments.get(rowIndex);
		if (columnIndex == 0){
			String fullSourceFileName = expr.getReference().get(0)
					.getSourceFile();
			String separator = File.separator;
			return fullSourceFileName.substring(fullSourceFileName
					.lastIndexOf(separator) + 1);
		}
		if (columnIndex == 1)
			return AcqclientUIUtils.getExperimentSize(expr);
		if (columnIndex == 2)
			return expr.getRecordSignatures().size();
		if (columnIndex == 3)
			return projectNames.get(rowIndex);
		if (columnIndex == 4)
			return progressBar.get(rowIndex);
		else
			return null;
	}

	/**
	 * clear the experiments which have completed their upload
	 */
	public void clearUploaded() {
		List<IExperiment> toClearExpr = new ArrayList<IExperiment>();
		List<JProgressBar> toClearProgress = new ArrayList<JProgressBar>();
		List<String> toClearProject = new ArrayList<String>();

		for (int i = 0; i < progressBar.size(); i++) {
			JProgressBar progress = progressBar.get(i);
			String projectName = projectNames.get(i);
			if (progress.getValue() == 100) {
				toClearExpr.add(experiments.get(i));
				toClearProgress.add(progress);
				toClearProject.add(projectName);
			}
		}

		experiments.removeAll(toClearExpr);
		progressBar.removeAll(toClearProgress);
		projectNames.removeAll(toClearProject);

		fireTableDataChanged();
	}

	/**
	 * attempts to cancel the selected upload tasks
	 * @param selectedRows 
	 */
	public void cancelSelected(int[] selectedRows) 
	{
		cancelEvent = true;
		this.selectedRows = selectedRows;
		
//		for (int i = 0; i < selectedRows.length; i++) {
//			int index = selectedRows[i];
//			JProgressBar progress = progressBar.get(index);
//			progress.setString("Cancelled");
//		}

		fireTableChanged(new TableModelEvent(this));
	}
	
	public boolean isCanceled(){
		return cancelEvent;
	}
	
	public void setCanceled(boolean value){
		cancelEvent = value;
	}
	
	/**
	 * retuns the selected row indices
	 * @return the selected row indices
	 */
	public List<String> getSelectedTasks()
	{
		List<String> selectedTasks = new ArrayList<String>();
		for(int i=0;i<selectedRows.length;i++)
		{
			if(experiments.size()<selectedRows[i])
				continue;
			String fullSourceFileName = experiments.get(selectedRows[i])
					.getReference().get(0).getSourceFile();
			String separator = File.separator;
			String name = fullSourceFileName.substring(fullSourceFileName
					.lastIndexOf(separator) + 1);
			selectedTasks.add(name);
		}
		
		return selectedTasks;
	}
	
	@Override 
    public void fireTableDataChanged() { 
        fireTableChanged(new TableModelEvent(this, //tableModel 
                                             0, //firstRow 
                                             getRowCount() - 1, //lastRow  
                                             TableModelEvent.ALL_COLUMNS, //column  
                                             TableModelEvent.UPDATE)); //changeType 
    } 

}
