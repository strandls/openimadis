package com.strandgenomics.imaging.iviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.swing.PScrollPane;

@SuppressWarnings("serial")
public class ImageViewerPanel extends JPanel implements HierarchyBoundsListener{
	Image img;
	ImageCanvas imageCanvas;
	
	private PText loadingNode = null;
	
	public ImageViewerPanel(){
		setSize(700, 700);
		setLayout(new BorderLayout());
		
		imageCanvas = new ImageCanvas();
		PScrollPane scrollPane = new PScrollPane(imageCanvas);
		scrollPane.setVisible(true);
		add(scrollPane);
		
		addHierarchyBoundsListener(this);
		
		validate();
	}

	public void setImage(Image img2) {
		this.img = img2;
		if(img2!=null){
			imageCanvas.addImage(img2);
		}
		
		imageCanvas.setFitWidth();
	}

	public void setZoom(double value) {
		imageCanvas.setZoom(value);
	}
	
	public void setFitWidth(){
		imageCanvas.setFitWidth();
	}
	
	public void toggleFitWidth(){
		imageCanvas.toggleFitWidth();
	}

	public void setImages(PLayer layer) 
	{
		removeLoadingNode();
		imageCanvas.addPLayer(layer);
		AcqProgressBar.doneProgressBar();
	}

	public void ancestorMoved(HierarchyEvent e) {
	}

	public void ancestorResized(HierarchyEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				imageCanvas.setFitWidth();
			}
		});
	}
	
	public void addLoadingNode(){
		if(loadingNode==null){
			loadingNode = new PText("loading...");
			loadingNode.setPickable(false);
			loadingNode.setPaint(Color.white);
			loadingNode.setTextPaint(Color.red);
			loadingNode.setOffset(0, 0);
			
			imageCanvas.getCamera().addChild(loadingNode);
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run()
				{
					repaint();
				}
			});
			
			System.out.println("added loading node");
		}
	}
	
	public void removeLoadingNode(){
		for (int i = 0; i < imageCanvas.getCamera().getChildrenCount(); i++) {
			if (imageCanvas.getCamera().getChild(i).equals(loadingNode)) {
				System.out.println("loading node removed");
				imageCanvas.getCamera().removeChild(i);
				break;
			}
		}
		loadingNode = null;
		repaint();
	}
}
