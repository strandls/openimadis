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
