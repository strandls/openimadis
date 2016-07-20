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

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.strandgenomics.imaging.iclient.local.RawExperiment;

/**
 * table model for direct upload table
 * 
 * @author Anup Kulkarni
 */
public class DirectUploadTableModel extends DefaultTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1985227648605890037L;

	/**
	 * list of source files to import
	 */
	private List<RawExperiment> sourceFiles;

	/**
	 * the columns shown in Upload table
	 */
	String columnNames[] = { "Source File", "Size" };

	/**
	 * holds the status of each file import 
	 */
	private List<ImportStatus> status;

	/**
	 * true if some imports are stopped
	 */
	private boolean stopEvent = false;

	/**
	 * true if some imports are scheduled to be reindexed
	 */
	private boolean reindexEvent = false;
	
	/**
	 * holds the rows selected for stopping import
	 */
	private int[] selected;

	public DirectUploadTableModel()
	{
		sourceFiles = new ArrayList<RawExperiment>();
		status = new ArrayList<ImportStatus>();
	}

	/**
	 * custom implementation of addRow(data) method of table model
	 * 
	 * @param experimentsToUpload
	 *            the experiment to be uploaded
	 */
	public void addExperiment(List<RawExperiment> experiments)
	{
		if (experiments == null || experiments.size() == 0)
			return;
		
		sourceFiles.addAll(experiments);
		
		for (RawExperiment exp : experiments)
		{
			status.add(ImportStatus.Queued);
		}
		
		fireTableDataChanged();
	}
	
	/**
	 * sets status of import
	 * @param sourceName
	 * @param status
	 */
	public void setStatus(File sourceName, ImportStatus status)
	{	
		int index = 0;
		for(RawExperiment filename : sourceFiles)
		{
			if(sourceName.equals(filename) && this.status.get(index) != ImportStatus.Stopped)
				break;
			index++;
		}
		
		if(index >= this.status.size())
			return;
		
		if(this.status.get(index) == ImportStatus.Duplicate )
			return;
		
		if((this.status.get(index) == ImportStatus.Indexed && status == ImportStatus.Failed) || (this.status.get(index) == ImportStatus.Failed && status == ImportStatus.Indexed))
			status = ImportStatus.Partial;
		
		if(this.status.size() >= index)
			this.status.set(index, status);
		
		fireTableDataChanged();
	}
	
	/**
	 * cancel selected imports
	 * @param selectedRows
	 */
	public void cancelSelected(int[] selectedRows)
	{
		if (selectedRows == null || selectedRows.length == 0)
			return;
		
		stopEvent = true;
		this.selected = selectedRows;
		
		for (int selectedRow : selectedRows)
		{
			if(status.get(selectedRow) == ImportStatus.Started)
				status.set(selectedRow, ImportStatus.Stopping);
			else
				status.set(selectedRow, ImportStatus.Stopped);
		}
		
		fireTableChanged(new TableModelEvent(this));
	}
	
	public void cancelAll()
	{
		ArrayList<Integer> selected = new ArrayList<Integer>();
		
		int i = 0;
		for(ImportStatus st:status)
		{
			if(st == ImportStatus.Queued || st == ImportStatus.Started)
				selected.add(i);
			i++;
		}
		
		int selectedIndices[] = new int[selected.size()];
		for(i=0;i<selectedIndices.length;i++)
		{
			selectedIndices[i] = selected.get(i);
		}
		
		cancelSelected(selectedIndices);
	}
	
	public List<RawExperiment> getSelectedImports()
	{
		if(selected == null || selected.length == 0)
			return null;
		
		List<RawExperiment> selectedNames = new ArrayList<RawExperiment>();
		for(int index : selected)
		{
			RawExperiment f = sourceFiles.get(index);
			selectedNames.add(f);
		}
		
		return selectedNames;
	}
	
	public boolean isStopped()
	{
		return stopEvent;
	}
	
	public boolean isReindexed()
	{
		return reindexEvent;
	}
	
	public void setStopped(boolean value)
	{
		stopEvent = value;
	}
	
	public void setReindexed(boolean b)
	{
		this.reindexEvent = b;
	}

	@Override
	public int getRowCount() {
		if (sourceFiles == null)
			return 0;
		return sourceFiles.size();
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
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (sourceFiles == null)
			return 0;
		if (columnIndex == 0){
			return sourceFiles.get(rowIndex).getRootDirectory();
		}
		if (columnIndex == 1)
		{
			long filesize = sourceFiles.get(rowIndex).getSeedFile().length();
			return AcqclientUIUtils.readableFileSize(filesize);
		}
		else
			return null;
	}

	public void uploadSelected(int[] selectedRows)
	{
		List<RawExperiment> selected = new ArrayList<RawExperiment>();
		for(int index:selectedRows)
		{
		}
	}
}
