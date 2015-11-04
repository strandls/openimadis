/**
 * 
 */
package com.strandgenomics.imaging.iacquisition;

import java.util.ArrayList;
import java.util.Date;
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

public class Spreadsheet extends JTable implements IRecordSelectionListener {

	private static final long serialVersionUID = -5328798855600059985L;

	private SpreadsheetRenderer renderer;
	List<IRecord> selectedRecords;

	private List<IRecordSelectionListener> selectionListeners = new ArrayList<IRecordSelectionListener>();

	public boolean processingEvent = false;

	public void addSelectionListener(IRecordSelectionListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionListener(IRecordSelectionListener listener)
	{
		selectionListeners.remove(listener);
	}

	private void fireSelectionEvent(List<IRecord> selectedRecords)
	{
		RecordSelectionEvent event = new RecordSelectionEvent(selectedRecords, this);
		for (IRecordSelectionListener listener : selectionListeners)
		{
			listener.selectionChanged(event);
		}
	}

	public Spreadsheet(SpreadsheetModel model)
	{
		super(model);
		renderer = new SpreadsheetRenderer();
		setDefaultRenderer(String.class, renderer);
		setDefaultRenderer(Date.class, renderer);
		setDefaultRenderer(Double.class, renderer);
		setDefaultRenderer(Integer.class, renderer);
		setDefaultRenderer(Object.class, renderer);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setCellSelectionEnabled(false);

		MyListSelectionListener selectionListener =  new MyListSelectionListener();
		getSelectionModel().addListSelectionListener(selectionListener);
		model.setSelectionListener(selectionListener);

		setAutoscrolls(true);
		setAutoCreateRowSorter(true);
	}

	public void selectionChanged(RecordSelectionEvent selectionEvent)
	{
		if (selectionEvent.getSource().equals(this))
		{
			return;
		}
		selectedRecords = selectionEvent.getRecords();
		if (selectedRecords != null)
		{
			for (IRecord record : selectedRecords)
			{
				int index = ((SpreadsheetModel) getModel()).getRowIndex(record);
				if (index >= 0)
				{
					index = this.getRowSorter().convertRowIndexToView(index);
					getSelectionModel().addSelectionInterval(index, index);
				}
			}
		}
		drawSelection();
		scrollRectToVisible(getCellRect(getSelectedRow(), 0, true));
	}

	private void drawSelection()
	{
		processingEvent = true; // disable events till we process this
		
		// clear existing selection
		getSelectionModel().clearSelection();
		
		// draw new selection
		if (selectedRecords != null)
		{
			for (IRecord record : selectedRecords)
			{
				int index = ((SpreadsheetModel) getModel()).getRowIndex(record);
				if (index >= 0)
				{
					index = this.getRowSorter().convertRowIndexToView(index);
					getSelectionModel().addSelectionInterval(index, index);
				}
			}
		}
		processingEvent = false;
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		if (e.getLastRow() < e.getFirstRow())
			return;

		super.tableChanged(e);
		drawSelection();
		repaint();
	}

	class MyListSelectionListener implements ListSelectionListener {

		MyListSelectionListener()
		{
		}
		
		public void recordsChecked(List<IRecord> records)
		{
			if(records!=null)
			{
				System.out.println("*************** records changed");
				for(IRecord record:records)
				{
					if(!selectedRecords.contains(record))
						selectedRecords.add(record);
				}
				for(IRecord record: selectedRecords)
					System.out.println(record);
				fireSelectionEvent(selectedRecords);
			}
		}

		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if (processingEvent || e.getValueIsAdjusting())
			{
				return;
			}
			final int first = e.getFirstIndex();
			final int last = e.getLastIndex();
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					updateSelectionModel(first, last);
				}
			});
		}

		private void updateSelectionModel(int firstRow, int lastRow)
		{
			if (firstRow > lastRow || firstRow < 0 || lastRow < 0)
			{
				return;
			}

			selectedRecords = new ArrayList<IRecord>();
			int rows = getRowCount();
			for (int i = 0; i < rows; i++)
			{
				if (isRowSelected(i))
				{
					selectedRecords.add(((SpreadsheetModel) getModel()).getRecordAt(getRowSorter().convertRowIndexToModel(i)));
				}
			}
			fireSelectionEvent(selectedRecords);
		}
	}

}
