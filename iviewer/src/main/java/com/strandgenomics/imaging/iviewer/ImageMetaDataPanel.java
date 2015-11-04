package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.strandgenomics.imaging.icore.Dimension;
import com.strandgenomics.imaging.icore.IChannel;
import com.strandgenomics.imaging.icore.IPixelData;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.dataobjects.TransposedDOTableModel;

@SuppressWarnings("serial")
public class ImageMetaDataPanel extends JPanel{

	private JTable table;
	private JScrollPane scrollPane;
	private boolean recievedModelChange = false;
	private TableModel tableModel;
	private ImageViewerApplet iviewer;

	public ImageMetaDataPanel(ImageViewerApplet iviewer) {
		this.iviewer = iviewer;
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane = new JScrollPane(table);
		
		table.addAncestorListener(new AncestorListener() {
			public void ancestorRemoved(AncestorEvent event) {
			}
			public void ancestorMoved(AncestorEvent event) {
			}
			public void ancestorAdded(AncestorEvent event) {
				if (recievedModelChange)
					update();
			}
		});
		table.setDefaultRenderer(Object.class, new DecimalFormatRenderer());
		
		
		setLayout(new BorderLayout());
		
		add(UIUtils.newTitlePanel(" Image Metadata", true),
				BorderLayout.BEFORE_FIRST_LINE);
		add(scrollPane);
	}

	public void updatePanel() {
		update();
	}

	private synchronized void update() {

		List<IRecord> records = (List<IRecord>) iviewer.getRecords();

		IRecord record = records.get(0); //TODO: handle multi-sites later

		int frame = (Integer) iviewer.getFrame();
		int slice = (Integer) iviewer.getSlice();
		int site = (Integer) iviewer.getSite();
		List<IChannel> channels = iviewer.getChannels();
		List<IChannel> selectedChannels = iviewer.getSelectedChannels();

		List ids = new ArrayList();
		String[] headers = new String[channels.size() + 1];
		headers[0] = "Field";

		for (int i = 0; i < channels.size(); i++) 
		{
			if (selectedChannels.contains(channels.get(i))) 
			{
				IPixelData pixelData;
				try
				{
					pixelData = record.getPixelData(new Dimension(frame, slice, i, site));
					ids.add(pixelData);
					headers[i + 1] = channels.get(i).getName();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}

		tableModel = new ImageTableModel(ids, headers);
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(120);
		table.revalidate();
		
	}

	private final static class ImageTableModel extends TransposedDOTableModel {
		final String[] headers;

		public ImageTableModel(final List objectData, final String[] headers) {
			super(objectData, new String[] {"visualOverlays", "connectionManager", "rawData","intensityDistribution", "containingRecord", "intensityDistibution", "parent" });
			this.headers = new String[headers.length];
			System.arraycopy(headers, 0, this.headers, 0, headers.length);
		}

		public synchronized String getColumnName(int column) {
			return headers[column];
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object value = super.getValueAt(rowIndex, columnIndex);
			if(value!=null && value.equals("dimension"))
				return "{frame,slice,channel}";
			return value;
		}
	}
	
	static class DecimalFormatRenderer extends DefaultTableCellRenderer {
		private static final DecimalFormat formatter = new DecimalFormat("#.0000");

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if(value != null && value instanceof Double)
				value = formatter.format((Double) value);
			return super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
		}
	}
}
