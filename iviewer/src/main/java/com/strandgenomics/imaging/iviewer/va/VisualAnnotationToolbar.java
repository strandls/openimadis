package com.strandgenomics.imaging.iviewer.va;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSeparator;

import com.strandgenomics.imaging.iviewer.UIUtils;

public class VisualAnnotationToolbar extends JDialog implements ActionListener{
	
	private final String VAToolBarTitle = "Edit Overlay : ";

	private JButton rectangleButton;

	private JButton ellipseButton;

	private JButton lineButton;

	private JButton freehandButton;
	
	private JButton closedShapeButton;
	
	private JButton textButton;

	private JButton saveButton;
	
	private JButton removeButton;
	
	private JButton copyButton;
	
	private JButton pasteButton;
	
	private JButton colorChooserButton;

	public VAController vaController;

	private boolean vaModeON;

	private JSeparator separator1;

	private JSeparator separator2;

	private JButton thicknessButton;	
		
	public VisualAnnotationToolbar(VAController vaController){
		this.vaController = vaController;				
	}
	
	public void createUI(){
		rectangleButton = UIUtils.createCommandBarButton("shape_rect.png", "draw new rectangle");
		
		ellipseButton = UIUtils.createCommandBarButton("shape_elip.png", "draw new ellipse");
		
		lineButton = UIUtils.createCommandBarButton("shape_line.png", "draw new line");
		
		freehandButton = UIUtils.createCommandBarButton("freehand_drawing.png", "freehand drawing");
		
		closedShapeButton = UIUtils.createCommandBarButton("closedShape.gif", "outline drawing");
		
		textButton = UIUtils.createCommandBarButton("shape_text.png", "add text");
		
		saveButton = UIUtils.createCommandBarButton("save_overlay.png", "save overlay");
		
		removeButton = UIUtils.createCommandBarButton("remove_shape.png", "remove selected visual object");
		
		colorChooserButton = UIUtils.createCommandBarButton("color_wheel.png", "set color");
		
		copyButton = UIUtils.createCommandBarButton("copy.gif", "copy selection");
		
		pasteButton = UIUtils.createCommandBarButton("paste.gif", "paste selection");
		
		thicknessButton = UIUtils.createCommandBarButton("line_thickness.gif", "visual object thickness");
		
		separator1 = new JSeparator(JSeparator.VERTICAL);
		separator1.setPreferredSize(new Dimension(1, 25));
		
		separator2 = new JSeparator(JSeparator.VERTICAL);
		separator2.setPreferredSize(new Dimension(1, 25));
		
		setLayout(new FlowLayout());
		
		saveButton.setFocusPainted(false);
		
		add(saveButton);
		add(separator1);
		
		add(copyButton);
		add(pasteButton);
		add(removeButton);
		add(thicknessButton);
		add(colorChooserButton);
		add(separator2);
				
		add(rectangleButton);
		add(ellipseButton);
		add(lineButton);
		add(textButton);
		add(freehandButton);
		add(closedShapeButton);
		
		pack();
		setResizable(false);
		
		enableVAControls(true);
		
		addActionListeners();
		
		vaModeON = true;
	}
	
	public VAObject getWorkingOverlay(){
		return vaController.getWorkingOverlay();
	}

	public void cleanup(){
		remove(rectangleButton);
		remove(ellipseButton);
		remove(lineButton);
		remove(freehandButton);
		remove(closedShapeButton);
		remove(textButton);
		remove(copyButton);
		remove(pasteButton);
		remove(colorChooserButton);
		remove(removeButton);
		remove(saveButton);
		remove(thicknessButton);
		remove(separator1);
		remove(separator2);
		
		vaModeON = false;
	}
	
	public void enableVAControls(boolean value){
		boolean workingOVPresent = false;
		if(vaController.getAnnotationNode() != null)
			workingOVPresent = true;
		
		rectangleButton.setEnabled(workingOVPresent);
		ellipseButton.setEnabled(workingOVPresent);
		lineButton.setEnabled(workingOVPresent);
		textButton.setEnabled(workingOVPresent);
		copyButton.setEnabled(workingOVPresent);
		pasteButton.setEnabled(workingOVPresent);
		saveButton.setEnabled(workingOVPresent);
		removeButton.setEnabled(workingOVPresent);		
		freehandButton.setEnabled(workingOVPresent);
		closedShapeButton.setEnabled(workingOVPresent);
		thicknessButton.setEnabled(workingOVPresent);
		colorChooserButton.setEnabled(workingOVPresent);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(rectangleButton)){
			vaController.addRectangle();
		}
		else if(e.getSource().equals(ellipseButton)){
			vaController.addEllipse();
		}
		else if(e.getSource().equals(lineButton)){
			vaController.addLine();
		}
		else if(e.getSource().equals(freehandButton)){
			vaController.freehandDrawing(true);				
		}
		else if(e.getSource().equals(closedShapeButton)){
			vaController.closedShape(true);			
		}
		else if(e.getSource().equals(textButton)){
			vaController.addText();
		}
		else if(e.getSource().equals(saveButton)){
			vaController.saveOverlay();
		}
		else if(e.getSource().equals(removeButton)){
			vaController.removeDialog();
		}
		else if(e.getSource().equals(pasteButton)){
			vaController.pasteSelection();
		}
		else if(e.getSource().equals(colorChooserButton)){
			vaController.colorChooser();
		}
		else if(e.getSource().equals(thicknessButton)){
			vaController.thicknessControl(this);
		}
		else if(e.getSource().equals(copyButton)){
			vaController.copySelection();
		}
	}
	
	public void addActionListeners(){
		rectangleButton.addActionListener(this);
		ellipseButton.addActionListener(this);		
		lineButton.addActionListener(this);
		freehandButton.addActionListener(this);
		closedShapeButton.addActionListener(this);
		textButton.addActionListener(this);
		pasteButton.addActionListener(this);
		saveButton.addActionListener(this);
		removeButton.addActionListener(this);
		colorChooserButton.addActionListener(this);
		thicknessButton.addActionListener(this);
		copyButton.addActionListener(this);
	}

	public void update() {
		if (vaController.getWorkingOverlay() != null && vaModeON) {
			vaController.resetSelection();
			vaController.removeInputEventListeners();
			vaController.selectZoom();
			List<List<VAObject>> overlays = vaController.getOverlays();
			String name = vaController.setWorkingOverlay(overlays.get(0).get(
					vaController.getActiveOverlayIndex(vaController
							.getWorkingOverlay().getName())));
			setTitle(VAToolBarTitle + name);
		}
	}	
}
