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

package com.strandgenomics.imaging.iacquisition.script;

import java.awt.event.ActionListener;

import javax.swing.JSeparator;

import com.jidesoft.action.CommandBar;
import com.jidesoft.swing.JideButton;
import com.strandgenomics.imaging.iviewer.UIUtils;

/**
 * Toolbar used for script editor
 * 
 * @author Anup Kulkarni
 */
@SuppressWarnings("serial")
public class ScriptEditorToolbar extends CommandBar{
	private ActionListener listener;

	public ScriptEditorToolbar(ActionListener listener) {
		this.listener = listener;
		
		createUI();
	}

	private void createUI()
	{
		JideButton openButton = UIUtils.createCommandBarButton("folder.png", "Open Script");
		openButton.addActionListener(listener);
		openButton.setActionCommand("openScript");
		
		JideButton saveButton = UIUtils.createCommandBarButton("save.png", "Run Script");
		saveButton.addActionListener(listener);
		saveButton.setActionCommand("saveScript");
		
		JideButton runButton = UIUtils.createCommandBarButton("PlayNormal.png", "Run Script");
		runButton.addActionListener(listener);
		runButton.setActionCommand("runScript");
		
		JideButton undoButton = UIUtils.createCommandBarButton("Back16.gif", "Undo");
		undoButton.addActionListener(listener);
		undoButton.setActionCommand("undoScript");
		
		JideButton redoButton = UIUtils.createCommandBarButton("Forward16.gif", "Redo");
		redoButton.addActionListener(listener);
		redoButton.setActionCommand("redoScript");
		
		JideButton clearButton = UIUtils.createCommandBarButton("table_refresh.png", "Clear Output");
		clearButton.addActionListener(listener);
		clearButton.setActionCommand("clearOutput");
		
		JideButton gotoButton = UIUtils.createCommandBarButton("navigation.png", "Goto Line");
		gotoButton.addActionListener(listener);
		gotoButton.setActionCommand("gotoLine");
		
		JideButton findButton = UIUtils.createCommandBarButton("zoom.png", "Find/Replace");
		findButton.addActionListener(listener);
		findButton.setActionCommand("findReplace");
		
		add(openButton);
		add(saveButton);
		add(new JSeparator(JSeparator.VERTICAL));
		add(runButton);
		add(new JSeparator(JSeparator.VERTICAL));
		add(undoButton);
		add(redoButton);
		add(new JSeparator(JSeparator.VERTICAL));
		add(gotoButton);
		add(findButton);
		add(new JSeparator(JSeparator.VERTICAL));
		add(clearButton);
	}
}
