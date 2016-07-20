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

package com.strandgenomics.imaging.iacquisition.thumbnail;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.strandgenomics.imaging.iacquisition.selection.IRecordListener;
import com.strandgenomics.imaging.iacquisition.selection.RecordsChangedEvent;
import com.strandgenomics.imaging.icore.IRecord;

@SuppressWarnings("serial")
public class ThumbnailTableModel extends AbstractTableModel implements IRecordListener {

    private int colsPerRow = 1;
    private int rowCount;
    private List<IRecord> records;

    
    public ThumbnailTableModel(List<IRecord> records) {
        this.records = records;
        init();
    }

    private void init() {
        if (records == null || records.size() == 0)
            rowCount = 0;
        else
            rowCount = ((records.size() - 1) / colsPerRow) + 1;
    }

    public int getColumnCount() {
        return colsPerRow;
    }

    public int getRowCount() {
        return rowCount;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        int elementIndex = (rowIndex * colsPerRow) + columnIndex;

        if (elementIndex > (records.size() - 1))
            return null;
        try{
            return records.get(elementIndex);
        }catch (Exception e) {
            System.out.println("=== YYYYYYYYY" + e.getMessage());
        }
        return null;
    }

    public Class<?> getColumnClass(int c) {
        return String.class;
    }

    public void setColumnsPerRow(int num) {
    	if(colsPerRow==num)
    		return;
        colsPerRow = num;
        init();
        fireTableStructureChanged();
    }

    public int getColumnsPerRow() {
        return colsPerRow;
    }

    /**
     * cleans up the old table and creates a new containing {@link #records}
     */
    private void updateTable() {
    	init();
        fireTableDataChanged();
    }

	@Override
	public void recordsChanged(RecordsChangedEvent e) {
		// TODO Auto-generated method stub
		if(e.getType() == RecordsChangedEvent.RECORDS_ADDED || e.getType() == RecordsChangedEvent.RECORDS_REMOVED)
			updateTable();	
	}
}
