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
