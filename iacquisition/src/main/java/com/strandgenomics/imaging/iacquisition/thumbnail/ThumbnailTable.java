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

/**
 * 
 */
package com.strandgenomics.imaging.iacquisition.thumbnail;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import com.strandgenomics.imaging.iacquisition.selection.IRecordSelectionListener;
import com.strandgenomics.imaging.iacquisition.selection.RecordSelectionEvent;
import com.strandgenomics.imaging.icore.IRecord;


@SuppressWarnings("serial")
public class ThumbnailTable extends JTable implements IRecordSelectionListener{

    private ThumbnailTableModel tableModel;
    
    private ThumbnailCellRenderer renderer;
    private int width = 0;
    private static int THUMB_WIDTH = 120;
    private List<IRecordSelectionListener> selectionListeners = 
            new ArrayList<IRecordSelectionListener>();
    
    List <IRecord> selectedRecords;

	private boolean isMyResizing = false;
    
    public ThumbnailTable(ThumbnailTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
        tableModel.addTableModelListener(this);
        init();
    }

    private void init() {
        setRowHeight(THUMB_WIDTH);

        renderer = new ThumbnailCellRenderer();
        setDefaultRenderer(String.class, renderer);
        setCellSelectionEnabled(true);
        
        getSelectionModel().addListSelectionListener(new MyListSelectionListener());
        getColumnModel().getSelectionModel() .addListSelectionListener(new MyListSelectionListener());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        setTableHeader(null);
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Component#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        // 20 is magic number hack for scroll bar
        if (Math.abs(this.width - width) < 10) {
            return;
        }
        this.width = width;
        int columns = (width + 20) / THUMB_WIDTH;
        if (columns == 0)
            return;

        tableModel.setColumnsPerRow(columns);

    }
    

    public void addSelectionListener(IRecordSelectionListener listener) {
        selectionListeners.add(listener);
    }

    public void removeSelectionListener(IRecordSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    private void fireSelectionEvent(List <IRecord> selectedRecords) {
        RecordSelectionEvent event = new RecordSelectionEvent(selectedRecords,this);
        for(IRecordSelectionListener listener: selectionListeners) {
            listener.selectionChanged(event);
        }
    }
    
    @Override
    public void selectionChanged(RecordSelectionEvent selectionEvent) {
    	if(selectionEvent.getSource().equals(this)){
    	//	System.out.println("Selection has changed .. Thumbnail Generated " );
    		return;
    	}
    	 selectedRecords = selectionEvent.getRecords();
    	 drawSelection();
         scrollRectToVisible(getCellRect(
                getSelectedRow(), getSelectedColumn(), true));
        
    }
    
    
    public void drawSelection(){
        isMyResizing = true;    // disabling event till this selection is handled
        getSelectionModel().clearSelection();
        getColumnModel().getSelectionModel().clearSelection();
        int rows = getRowCount();
        int columns = getColumnCount();
		if (selectedRecords != null) {
			// XXX: This part I do not understand ..
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					IRecord record = (IRecord) getValueAt(i, j);
					if (selectedRecords.contains(record)) {
						getSelectionModel().addSelectionInterval(i, i);
						getColumnModel().getSelectionModel()
								.addSelectionInterval(j, j);
					}
				}
			}
		}
        isMyResizing = false;
    }
    
    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        drawSelection();
        repaint();
    }
    
    public void clearCacheForRecord(IRecord record){
    	renderer.clearCacheForRecord(record);
    	repaint();
    	revalidate();
    }
    
    class MyListSelectionListener implements ListSelectionListener {

        MyListSelectionListener() {
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
       // 	 System.out.println("Thumbnail list selection event is fired ..");
            if (isMyResizing || e.getValueIsAdjusting())
                return;
           // System.out.println("Thumbnail list selection value is changed ..");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateSelectionModel();
                }
            });
        }

        private void updateSelectionModel() {
           
        	selectedRecords = new ArrayList<IRecord>();
            int rows = getRowCount();
            int columns = getColumnCount();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (isCellSelected(i, j)) {
                        selectedRecords.add(((IRecord) getValueAt(i, j)));
                    }
                }
            }
            fireSelectionEvent(selectedRecords);
        }

    }

}
