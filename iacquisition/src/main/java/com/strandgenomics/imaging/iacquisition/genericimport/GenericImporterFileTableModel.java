package com.strandgenomics.imaging.iacquisition.genericimport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class GenericImporterFileTableModel extends AbstractTableModel{

	private List<String> fileList;
	public GenericImporterFileTableModel()
	{
		fileList = new ArrayList<String>();
	}
	
	public void setData(List<String> fileList)
	{
		Collections.sort(fileList);
		
		this.fileList = fileList;
		fireTableDataChanged();
	}
	
	@Override
	public int getRowCount()
	{
		return fileList.size();
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		return "Source File";
	}

	@Override
	public int getColumnCount()
	{
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return fileList.get(rowIndex);
	}

}
