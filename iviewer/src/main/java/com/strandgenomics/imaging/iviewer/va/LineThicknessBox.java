package com.strandgenomics.imaging.iviewer.va;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class LineThicknessBox {
	private class LineThicknessRenderer implements ListCellRenderer{
		public LineThicknessRenderer() {
	    }
		
		public Component getListCellRendererComponent(final JList list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			return new JPanel() {
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					//setOpaque(true);
					setBackground(isSelected?Color.blue:Color.white);
					
					float thickness = linewidth;
					if(index!=-1){
						thickness = thicknessArray[index];
					}
					BasicStroke stroke = new BasicStroke(thickness);
					
					Graphics2D g2D = (Graphics2D) g;
					g2D.setStroke(stroke);
					Line2D line1 = new Line2D.Float(10f, 8f, 160f, 8f);
					g2D.draw(line1);
					
					repaint();
				}

				public Dimension getPreferredSize() {
					return new Dimension(50,15);
				}
			};
		}
	}
	
	private class LineCanvas extends Canvas{
		float width; 
		public LineCanvas(float width){
			this.width = width;
			
			setSize(250, 20);
		} 
		
		public void setWidth(float width){
			this.width = width;
		}
		
		public void paint(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;

			BasicStroke stroke = new BasicStroke(width);
			g2D.setStroke(stroke);
			Line2D line1 = new Line2D.Float(50f, 5f, 250f, 5f);
			g2D.draw(line1);
		}
	}
	
	private float linewidth;
	private Float[] thicknessArray = {0.5f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f};
	
	public LineThicknessBox(float width){
		this.linewidth = width;
	}
	
	public JPanel createDialog(){
		JLabel thicknessLabel = new JLabel("Line Thickness ");
		
		final JComboBox thicknessBox = new JComboBox(thicknessArray);
		thicknessBox.setMaximumSize(new Dimension(200, 25));
		thicknessBox.setRenderer(new LineThicknessRenderer());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		final LineCanvas lc = new LineCanvas(linewidth);
		JPanel previewPanel = new JPanel();
		previewPanel.add(lc);
		previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
		
		thicknessBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				linewidth = (Float)thicknessBox.getSelectedItem();
				
				thicknessBox.repaint();
				lc.setWidth(linewidth);
				lc.repaint();
			}
		});
		
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.add(Box.createHorizontalStrut(5));
		upperPanel.add(thicknessLabel);
		upperPanel.add(Box.createHorizontalStrut(10));
		upperPanel.add(thicknessBox);
		upperPanel.add(Box.createHorizontalStrut(5));
		
		panel.add(Box.createVerticalStrut(5));
		panel.add(upperPanel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(previewPanel);
		
//		SimpleDialog lineTB = new SimpleDialog();
//		lineTB.setContent(panel);
//		lineTB.setSize(300, 170);
//		lineTB.setResizable(false);
//		lineTB.setTitle("Thickness Control");
		
		return panel;
	}
	
	public float getLineWidth() {
		return linewidth;
	}
}
