package com.strandgenomics.imaging.iacquisition;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Cell renderer for upload status JProgressBar renderer
 * 
 * @author Anup Kulkarni
 */
public class UploaderRenderer extends DefaultTableCellRenderer {
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		int progress = 0;
		String msg = "";
		if(value instanceof JProgressBar){
			progress = ((JProgressBar)value).getValue();
			msg = ((JProgressBar)value).getString();
		}
		JProgressBar p = new JProgressBar();
		p.setStringPainted(true);
		p.setValue(progress);
		p.setString(msg);
		return p;
	}
}
