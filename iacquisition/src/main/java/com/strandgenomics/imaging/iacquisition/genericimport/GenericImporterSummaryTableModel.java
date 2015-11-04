package com.strandgenomics.imaging.iacquisition.genericimport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;

public class GenericImporterSummaryTableModel extends DefaultTableModel {

	private String[] columns =
		{
			"",
			"Label", 
			"Frames", 
			"Slices", 
			"Channels", 
			"Sites",
			"Image Width",
			"Image Height",
			"Image Depth",
			"Image Type",
			"Pixel Size X",
			"Pixel Size Y",
			"Pixel Size Z"
		};
	
	private boolean[] checked = null;
	private List<RecordMetaData> records = null;
	
	public GenericImporterSummaryTableModel()
	{
		records = new ArrayList<RecordMetaData>();
		checked = null;
	}
	
	public List<RecordMetaData> getSelectedRecords()
	{
		if(checked == null) return null;
		
		List<RecordMetaData> selection = new ArrayList<RecordMetaData>();
		for(int row=0;row < getRowCount();row++)
		{
			if(checked[row])
			{
				selection.add(records.get(row));
			}
		}
		return selection;
	}

	@Override
	public String getColumnName(int columnIndex) 
	{
		return columns[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) 
	{
		if(columnIndex == 0)
			return Boolean.class;
		
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return (columnIndex == 0);
	}

	@Override
	public int getRowCount()
	{
		return records == null ? 0 : records.size();
	}

	@Override
	public int getColumnCount()
	{
		return columns.length;
	}
	
	@Override
	public void setValueAt(Object aValue, int row, int column) 
	{
		if(isCellEditable(row, column))
		{
			checked[row] = (Boolean) aValue;
			fireTableCellUpdated(row, column);
		}
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		RecordMetaData record = records.get(rowIndex);
		
		if(columnIndex == 0)
		{
			return checked[rowIndex];
		}
		else if(columnIndex == 1)
		{
			return record.name;
		}
		else if(columnIndex == 2)
		{
			return record.getFrameCount();
		}
		else if(columnIndex == 3)
		{
			return record.getSliceCount();
		}
		else if(columnIndex == 4)
		{
			return record.getChannelCount();
		}
		else if(columnIndex == 5)
		{
			return record.getSiteCount();
		}
		else if(columnIndex == 6)
		{
			return record.getImageWidth();
		}
		else if(columnIndex == 7)
		{
			return record.getImageHeight();
		}
		else if(columnIndex == 8)
		{
			return record.getPixelDepth().toString();
		}
		else if(columnIndex == 9)
		{
			return record.getImageType().name();
		}
		else if(columnIndex == 10)
		{
			return record.getPixelSizeX();
		}
		else if(columnIndex == 11)
		{
			return record.getPixelSizeY();
		}
		else if(columnIndex == 12)
		{
			return record.getPixelSizeY();
		}
		
		return null;
	}

	public RecordMetaData getRecord(int row)
	{
		return records.get(row);
	}

	public void setRecords(Collection<RecordMetaData> recordList)
	{
		this.records.clear();
		if(recordList != null) 
		{
			records.addAll(recordList);
			checked = new boolean[recordList.size()];
			for(int i=0;i<recordList.size();i++)
			{
				checked[i] = true;
			}
		}
		fireTableDataChanged();
	}
}
