package com.strandgenomics.imaging.iacquisition;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.strandgenomics.imaging.iclient.local.UploadStatus;

@SuppressWarnings("serial")
public class SpreadsheetRenderer extends DefaultTableCellRenderer {

	private static final DecimalFormat formatter = new DecimalFormat( "#.0000" ); 

	public SpreadsheetRenderer() {
	}


	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		//row = table.getRowSorter().convertRowIndexToModel(row);
		
		if(value != null && value instanceof Double)
			value = formatter.format((Double)value); 
		
		Component comp = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, col);
		
		String uploadStatus = (String) table.getModel().getValueAt(table.getRowSorter().convertRowIndexToModel(row), SpreadSheetFixedColumns.UPLOAD_STATUS.ordinal());
		if(table.isRowSelected(row))
			comp.setBackground(table.getSelectionBackground());
		else
			comp.setBackground(Color.white);
		 if(uploadStatus != null){
			 	if(uploadStatus.equals(UploadStatus.Queued.toString()) || uploadStatus.equals(UploadStatus.QueuedBackground.toString()) )
			 		comp.setForeground(Color.blue);
			 	else if(uploadStatus.equals(UploadStatus.Uploaded.toString()) || uploadStatus.equals(UploadStatus.Duplicate.toString()))
			 		comp.setForeground(Color.green);
			 	else comp.setForeground(Color.black);
			 	
		}else
			comp.setForeground(Color.black);
		return comp;
	}
}
