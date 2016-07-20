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

package com.strandgenomics.imaging.ithreedview.utils;

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
