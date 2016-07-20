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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class DOTableModel extends DefaultTableModel {
	
	LinkedHashMap<String,Integer> columnHeaders;
	List<String> columnTypes;
	List<List> columnList;
	int rows;
	
	String[] fieldsToSkip;
	
	public DOTableModel(String[] fieldsToSkip){
		this.fieldsToSkip=fieldsToSkip;
		columnHeaders=new LinkedHashMap<String,Integer>();
		columnList=new ArrayList<List>();
		columnTypes=new ArrayList<String>();
		rows = 0;
	}
	
	public DOTableModel(List dataObjectList) {
		this(dataObjectList,null);
	}

	public DOTableModel(List dataObjectList, String[] fieldsToSkip) {
		this.fieldsToSkip=fieldsToSkip;
		
		columnHeaders=new LinkedHashMap<String,Integer>();
		columnList=new ArrayList<List>();
		columnTypes=new ArrayList<String>();
		rows=dataObjectList.size(); 
		
		int rowindex=0;
		for(Object dataObject: dataObjectList){
			addDataObjects(dataObject,rowindex);
			rowindex++;
		}
	}
	
	
	protected void addDataObjectsList(List dataObjectList) {

		int rowindex=rows;
		rows += dataObjectList.size(); 
		
		// In all the columns add some dummy entries
		for(int colIndex =0; colIndex< columnList.size();colIndex++){
			List column = columnList.get(colIndex);
			int columnSize = column.size();
			for(int i=columnSize; i<rows;i++)
				column.add(null);
		}
		
		for(Object dataObject: dataObjectList){
			addDataObjects(dataObject,rowindex);
			rowindex++;
		}
		
	}
	
	protected void addDataObjects(Object dataObject,int rowIndex) {
		
		DOIterator iterator=new DOIterator(dataObject,fieldsToSkip);
		while(iterator.hasNext()){
			Triplet triplet=iterator.next();
			String key=triplet.getKey();
			List column;			
			if(columnHeaders.containsKey(key)){
				int colIndex=columnHeaders.get(key);
				column=columnList.get(colIndex);
			}
			else{
				column=new ArrayList(rows);
				for(int i = 0; i < rows; i++){
					column.add(null);
				}
				int colIndex=columnList.size();
				columnHeaders.put(key, colIndex);
				columnTypes.add(triplet.getType());
				columnList.add(column);				
			}			
			column.set(rowIndex,triplet.getValue());
		}
	}
	
	protected String changeToCamelCase(String key){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<key.length();i++){
			String c = key.substring(i, i+1);
			if(i==0){
				sb.append(c.toUpperCase());
			} else if(c == c.toUpperCase()){
				sb.append(" ");
				sb.append(c);
			} else
				sb.append(c);
		}
		return sb.toString();
	}

	@Override
	public int getRowCount() {		
		return rows;
	}

	@Override
	public int getColumnCount() {		
		return columnList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {		
		return columnList.get(columnIndex).get(rowIndex);
	}

	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();		
		for(String header:columnHeaders.keySet()){
			sb.append(header+":");
		}
		sb.append("\n");
		for(int i=0;i<getRowCount();i++){
			for (int j = 0; j < getColumnCount(); j++) {
				sb.append(getValueAt(i, j)+":");
			}
			sb.append("\n");
		}
		return sb.toString();
	} 
	
	@Override
	public void addColumn(Object columnName,
            Object[] columnData){
		//super.addColumn(columnName, columnData);
		int colIndex=columnList.size();
		columnHeaders.put((String)columnName, colIndex);
		ArrayList column = new ArrayList();
		column.addAll(Arrays.asList(columnData));
		columnList.add(column);
		columnTypes.add("String");
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub		
		return (String)columnHeaders.keySet().toArray()[column];
	}
	
	public List getColumn(String key){
		List column = null;
		if(columnHeaders.containsKey(key)){
			int colIndex=columnHeaders.get(key);
			column=columnList.get(colIndex);
		}
		return column;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		String columnType=columnTypes.get(columnIndex);
		if(columnType.equals(Triplet.TYPE_INT)){
			return Integer.class;
		}
		if(columnType.equals(Triplet.TYPE_FLOAT)){
			return Float.class;
		}
		else if(columnType.equals(Triplet.TYPE_LONG)){
			return Long.class;
		}
		if(columnType.equals(Triplet.TYPE_STRING)
			|| columnType.equals(Triplet.TYPE_USER_ANNOTATION)
			||columnType.equals(Triplet.TYPE_UNINTERPRETED_FIELD)
			){
			return String.class;
		}
		return Object.class;
	} 
	
	public String getColumnType(int columnIndex){
		return columnTypes.get(columnIndex);
	}
	
	protected void clearDataObjectsList() {
		rows = 0;
		columnHeaders.clear();
		columnList.clear();
		columnTypes.clear();
		rows = 0;
	}
	
}
