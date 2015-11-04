package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.dataobjects.TransposedDOTableModel;

public class RecordDynamicDataPanel extends JPanel{

	private JScrollPane scrollPane;
	private JTable table;
	private ImageViewerApplet iviewer;
	
	private Map<String, String> nameMaps;

	public RecordDynamicDataPanel(ImageViewerApplet iviewer) 
	{
		nameMaps = new HashMap<String, String>();
		
		initNameMap();
		this.iviewer = iviewer;
		table = new JTable();
		scrollPane = new JScrollPane(table);
		
		setLayout(new BorderLayout());
		add(UIUtils.newTitlePanel(" Record Metadata", true),
				BorderLayout.BEFORE_FIRST_LINE);
		add(scrollPane);
		table.setDefaultRenderer(Object.class, new DecimalFormatRenderer());
	}
	
	private void initNameMap()
	{}

	public void cleanup() {
		table.removeAll();
	}

	public String getTitle() {
		return "Record MetaData";
	}

	public void updatePanel() {
		List<IRecord> records = (List<IRecord>) iviewer.getRecords();
		table.setModel(new RecordTableModel(records));
	}

	private final static class RecordTableModel extends TransposedDOTableModel 
	{

		public RecordTableModel(List objectData)
		{
			super(objectData, new String[] { "sites", "channels", "imagedata", "thumbnail", "signature", "dynamicMetaData", "userAnnotations", "visualOverlays", "attachments", "connectionManager",
					"experiment", "parentProject", "comments" });
		}

		public String getColumnName(int column)
		{
			if (column == 0)
			{
				return "Field";
			}
			else
			{
				if (getColumnCount() == 2)
				{
					return "Value ";
				}
				else
				{
					return "Site " + column;
				}
			}
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
