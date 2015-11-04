package com.strandgenomics.imaging.iviewer.dataobjects;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TransposedDOTableModel extends AbstractTableModel{

	DOTableModel tableModel;
	
	public TransposedDOTableModel(List dataObjectList) {
		tableModel=new DOTableModel(dataObjectList);
	}
	
	public TransposedDOTableModel(List dataObjectList, String[] fieldsToSkip) {
		tableModel=new DOTableModel(dataObjectList,fieldsToSkip);
	}

	@Override
	public int getRowCount() {
		return tableModel.getColumnCount();
	}

	@Override
	public int getColumnCount() {
		return tableModel.getRowCount()+1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex==0){
			//show headers
			String colmName =  tableModel.getColumnName(rowIndex);
			if(colmName.equals("frameCount"))
				colmName = "frameTCount";
			if(colmName.equals("sliceCount"))
				colmName = "sliceZCount";
			if(colmName.equals("channelCount"))
				colmName = "channel λCount";
			if(colmName.equals("pixelDepth"))
				colmName = "imageDepth";
			return tableModel.changeToCamelCase(colmName);
		}else{
			return tableModel.getValueAt(columnIndex-1, rowIndex); 
		}
		
	}

	@Override
	public String toString() {		
		StringBuffer sb=new StringBuffer();		
		sb.append("\n");
		for(int i=0;i<getRowCount();i++){
			for (int j = 0; j < getColumnCount(); j++) {
				sb.append(getValueAt(i, j)+":");
			}
			sb.append("\n");
		}
		return sb.toString();
	} 
}
