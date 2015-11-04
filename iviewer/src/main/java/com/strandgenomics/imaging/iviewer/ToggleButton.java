package com.strandgenomics.imaging.iviewer;

import java.awt.Dimension;

import javax.swing.Icon;

import com.jidesoft.swing.JideToggleButton;

public class ToggleButton extends JideToggleButton {
	
	private Icon selectIcon;
	private Icon deselectIcon;
	private String selectToolTipText;
	private String deselectToolTipText;

	public ToggleButton(Icon selectIcon, Icon deselectIcon, 
		String selectToolTipText, String deselectToolTipText, boolean selected) {
		super();
		this.selectIcon = selectIcon;
		this.deselectIcon = deselectIcon;
		this.selectToolTipText = selectToolTipText;
		this.deselectToolTipText = deselectToolTipText;
		setSelected(selected);
		setBorderPainted(false);
		setPreferredSize(new Dimension(22, 22));
	}

	private void updateButton(boolean selected) {
		if (selected) {
			setBorderPainted(true);
			setIcon(selectIcon);
			setToolTipText(selectToolTipText);
		}
		else {
			setBorderPainted(false);
			setIcon(deselectIcon);
			setToolTipText(deselectToolTipText);
		}
	}

	public boolean isSelected() {
		boolean selected = super.isSelected();
		updateButton(selected);
		return selected;
	}

	public void setSelected(boolean selected) {
		super.setSelected(selected);
		updateButton(selected);
	}
}
