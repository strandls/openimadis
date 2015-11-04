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
