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
