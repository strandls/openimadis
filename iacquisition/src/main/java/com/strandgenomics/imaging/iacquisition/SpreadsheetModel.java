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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import com.strandgenomics.imaging.iacquisition.Spreadsheet.MyListSelectionListener;
import com.strandgenomics.imaging.iacquisition.selection.IRecordListener;
import com.strandgenomics.imaging.iacquisition.selection.RecordsChangedEvent;
import com.strandgenomics.imaging.iclient.local.RawExperiment;
import com.strandgenomics.imaging.iclient.local.RawRecord;
import com.strandgenomics.imaging.icore.IRecord;

public class SpreadsheetModel extends AbstractTableModel implements IRecordListener {

	private static final long serialVersionUID = 7772030397955391524L;
	/**
	 * Records that feed the spreadsheet model ..
	 */
	private List<IRecord> records = null;
	/**
	 * Fixed column headers
	 */
	private LinkedHashMap<SpreadSheetFixedColumns, String> fixedColumnHeaders;
	/**
	 * number of fixed columns
	 */
	private int fixedColumnCount;
	/**
	 * User annotations
	 */
	private List<String> userAnnotationFields = null;
	/**
	 * checkbox model
	 */
	private Map<IRecord, Boolean> selections;
	private MyListSelectionListener selectionListener;

	public SpreadsheetModel(List<IRecord> records)
	{
		this(records, new HashMap<IRecord, String>());
	}

	public SpreadsheetModel(List<IRecord> records, Map<IRecord, String> uploadStatus)
	{
		this.records = records;
		
		selections = new HashMap<IRecord, Boolean>();
		for(IRecord record:records)
		{
			selections.put(record, false);
		}

		initFixedColumnHeaders();

		userAnnotationFields = new ArrayList<String>();
		for (IRecord record : records)
		{
			Map<String, Object> annotations = record.getUserAnnotations();
			for (Object key : annotations.keySet())
			{
				if (!userAnnotationFields.contains(key))
					userAnnotationFields.add(key.toString());
			}
		}
	}

	private void initFixedColumnHeaders()
	{
		fixedColumnHeaders = new LinkedHashMap<SpreadSheetFixedColumns, String>();
		fixedColumnHeaders.put(SpreadSheetFixedColumns.CREATION_TIME, "Creation Time");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.UPLOAD_TIME, "Upload Time");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.ACQUIRED_DATE, "Acquired Date");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.SERIES_NUMBER, "Series Number");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.SERIES_NAME, "Series Name");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.SLICE_COUNT, "Slice Z Count");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.FRAME_COUNT, "Frame T Count");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.CHANNEL_COUNT, "Channel λ Count");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.SITE_COUNT, "Site Count");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.IMAGE_WIDTH, "Image Width");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.IMAGE_HEIGHT, "Image Height");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.IMAGE_COUNT, "Image Count");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.PIXEL_DEPTH, "Image Depth");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.PIXEL_SIZE_X, "Pixel Size X");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.PIXEL_SIZE_Y, "Pixel Size Y");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.PIXEL_SIZE_Z, "Pixel Size Z");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.IMAGE_TYPE, "Image Type");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.SOURCE_TYPE, "Source Type");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.SOURCE_FILENAME, "Source Filename");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.ROOT_DIRECTORY, "Root Directory");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.PROJECT_NAME, "Project Name");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.MACHINE_IP, "Machine IP");
		fixedColumnHeaders.put(SpreadSheetFixedColumns.UPLOAD_STATUS, "Upload Status");
		fixedColumnCount = fixedColumnHeaders.size();
	}

	/**
	 * @param index
	 * @return record at specified row index
	 */
	public IRecord getRecordAt(int index)
	{
		try
		{
			return records.get(index);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Records are added. New columns can also get added.
	 */
	public void recordsAdded()
	{
		for(IRecord record: records)
		{
			if(!selections.containsKey(record))
			{
				selections.put(record, false);
			}
		}
		if (updateUserAnnotationNames())
			fireTableStructureChanged();
		else
			fireTableDataChanged();
	}

	/**
	 * Records are deleted .. columns can also get deleted.
	 */
	public void recordsDeleted()
	{
		userAnnotationFields.clear();
		// Update list of user annotations
		updateUserAnnotationNames();
		fireTableStructureChanged();
	}

	public boolean updateUserAnnotationNames()
	{
		boolean tableStructureChanged = false;
		for (IRecord record : records)
		{
			Map<String, Object> annotations = record.getUserAnnotations();
			for (Object key : annotations.keySet())
			{
				if (!userAnnotationFields.contains(key))
				{
					userAnnotationFields.add(key.toString());
					tableStructureChanged = true;
				}
			}
		}
		return tableStructureChanged;
	}

	public void updateUploadTime()
	{
		// fire change of appropriate column
		fireTableDataChanged();
	}

	public void updateUserAnnotation(String key)
	{
		if (!userAnnotationFields.contains(key))
		{
			userAnnotationFields.add(key.toString());
			fireTableStructureChanged();
		}
		else
			fireTableRowsUpdated(0, getRowCount() - 1);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		if (columnIndex == 0)
			return true;
		return false;
	}

	/**
	 * @param record
	 * @return row index for the record if it is in spreadsheet, else -1
	 */
	public int getRowIndex(IRecord record)
	{
		for (int i = 0; i < getRowCount(); i++)
		{
			if (records.get(i).equals(record))
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getRowCount()
	{
		return records.size();
	}

	@Override
	public int getColumnCount()
	{
		return (userAnnotationFields.size() + fixedColumnHeaders.size());
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		SpreadSheetFixedColumns col = SpreadSheetFixedColumns.values()[columnIndex];
		if (columnIndex < fixedColumnCount)
			return fixedColumnHeaders.get(col);
		else
			return userAnnotationFields.get(columnIndex - fixedColumnCount);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
//		if (columnIndex == 0)
//			return this.selections.get(records.get(rowIndex));
		if (columnIndex < fixedColumnCount)
			return getFixedRecordField(records.get(rowIndex), columnIndex);
		else
			return records.get(rowIndex).getUserAnnotations().get(getColumnName(columnIndex));
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int col)
	{
		if(col == 0)
		{
			boolean select = (Boolean) aValue;
			IRecord record = records.get(row);
			selections.put(record, select);
			
			List<IRecord> records = new ArrayList<IRecord>();
			for(Entry<IRecord, Boolean> entry:selections.entrySet())
			{
				if(entry.getValue())
					records.add(entry.getKey());
			}
			
			selectionListener.recordsChecked(records);
		}
	}

	private Object getFixedRecordField(IRecord record, int columnIndex)
	{
		SpreadSheetFixedColumns col = SpreadSheetFixedColumns.values()[columnIndex];
		switch (col)
		{
			case CREATION_TIME:
				return record.getCreationTime();
			case UPLOAD_TIME:
				return record.getUploadTime();
			case ACQUIRED_DATE:
				return record.getAcquiredDate();
			case SERIES_NUMBER:
				return record.getSite(0).getSeriesNo();
			case SERIES_NAME:
				return record.getSite(0).getName();
			case SLICE_COUNT:
				return record.getSliceCount();
			case FRAME_COUNT:
				return record.getFrameCount();
			case CHANNEL_COUNT:
				return record.getChannelCount();
			case SITE_COUNT:
				return record.getSiteCount();
			case IMAGE_WIDTH:
				return record.getImageWidth();
			case IMAGE_HEIGHT:
				return record.getImageHeight();
			case IMAGE_COUNT:
				return record.getImageCount();
			case PIXEL_DEPTH:
				return record.getPixelDepth().toString();
			case PIXEL_SIZE_X:
				return record.getPixelSizeAlongXAxis();
			case PIXEL_SIZE_Y:
				return record.getPixelSizeAlongYAxis();
			case PIXEL_SIZE_Z:
				return record.getPixelSizeAlongZAxis();
			case IMAGE_TYPE:
				return record.getImageType().toString();
			case SOURCE_TYPE:
				return record.getSourceType().toString();
			case SOURCE_FILENAME:
				return record.getSourceFilename();
			case ROOT_DIRECTORY:
				return record.getRootDirectory();
			case MACHINE_IP:
				return record.getOriginMachineIP();
			case PROJECT_NAME:
				if (record instanceof RawRecord)
				{
					return ((RawRecord) record).getParentProjectName();
				}
				return null;
			case UPLOAD_STATUS:
				return getUploadStatus(record);
			default:
				return null;
		}
	}

	private Object getUploadStatus(IRecord record)
	{
		if (record.getExperiment() instanceof RawExperiment)
			return ((RawExperiment) record.getExperiment()).getUploadStatus().toString();
		return null;
	}

	@Override
	public void recordsChanged(RecordsChangedEvent e)
	{
		switch (e.getType())
		{
			case RecordsChangedEvent.RECORDS_ADDED:
				recordsAdded();
				return;
			case RecordsChangedEvent.RECORDS_REMOVED:
				recordsDeleted();
				return;
			case RecordsChangedEvent.ANNOTATIONS_CHANGED:
				fireTableRowsUpdated(0, getRowCount() - 1);
				return;
			case RecordsChangedEvent.ANNOTATION_ADDED:
				updateUserAnnotation(e.getField());
				return;
			case RecordsChangedEvent.ANNOTATION_REMOVED:
				fireTableRowsUpdated(0, getRowCount() - 1);
				return;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		SpreadSheetFixedColumns col = SpreadSheetFixedColumns.values()[columnIndex];
		switch (col)
		{
			case CREATION_TIME:
			case UPLOAD_TIME:
			case ACQUIRED_DATE:
				return Date.class;
			case SERIES_NUMBER:
			case SLICE_COUNT:
			case FRAME_COUNT:
			case CHANNEL_COUNT:
			case SITE_COUNT:
			case IMAGE_WIDTH:
			case IMAGE_HEIGHT:
			case IMAGE_COUNT:
				return Integer.class;
			case PIXEL_SIZE_X:
			case PIXEL_SIZE_Y:
			case PIXEL_SIZE_Z:
				return Double.class;
			default:
				return String.class;
		}
	}

	public void setSelectionListener(MyListSelectionListener selectionListener)
	{
		this.selectionListener = selectionListener;
	}

}
