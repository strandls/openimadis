package com.strandgenomics.imaging.iviewer.va;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.combobox.FontChooserPanel;

public class TextEditorPane extends JTabbedPane {
	private Font f;
	
	private Color textColor;
	private Color bkgColor;
	private String text;
	
	private JTextField textField;

	private JLabel previewLabel;
	private JLabel previewLabelInsert;
	
	public TextEditorPane(Font f, Color textColor, Color bkgColor, String text) {
		this.f = f;
		this.text = text;
		this.textColor = textColor;
		this.bkgColor = bkgColor;
		
		insertTab("Text", null, getInsertComponent(), "Add/Edit Text", 0);
		insertTab("Style", null, getStyleComponent(), "Change Font/Color", 1);
		
		setMaximumSize(new Dimension(300, 300));
	}	
	private Component getStyleComponent() {
		
		previewLabel = new JLabel(text);
		previewLabel.setFont(f);
		previewLabel.setOpaque(true);
		previewLabel.setForeground(textColor);
		previewLabel.setBackground(bkgColor);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel fontPanel = new JPanel();
		fontPanel.setLayout(new BoxLayout(fontPanel, BoxLayout.X_AXIS));
		fontPanel.setPreferredSize(new Dimension(150, 25));
		
		FontChooserPanel fontChooserPanel = new FontChooserPanel();
		
		JComboBox fontChooser = new JComboBox(fontChooserPanel.getFontNameComboBoxModel());
		fontChooser.setPreferredSize(new Dimension(72, 25));
		fontChooser.setSelectedItem(f.getName());
		
		fontChooser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String newFontName = (String)((JComboBox) e.getSource()).getSelectedItem();
				f = new Font(newFontName, Font.PLAIN, f.getSize());
				previewLabel.setFont(f);
				
				previewLabelInsert.setFont(f);
			}
		});
		
		final JSpinner fontSize = new JSpinner(new SpinnerNumberModel(f.getSize(), 4, 100, 1));
		fontSize.setMaximumSize(new Dimension(10, 25));

		fontSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				f = f.deriveFont((Integer)fontSize.getValue()*1.0f);
				previewLabel.setFont(f);
				
				previewLabelInsert.setFont(f);
			}
		});
		
		fontChooser.add(Box.createVerticalStrut(5));
		fontPanel.add(new JLabel("Font "));
		fontPanel.add(Box.createHorizontalGlue());
		fontPanel.add(fontChooser);
//		fontPanel.add(Box.createHorizontalGlue());
		fontPanel.add(fontSize);
		fontChooser.add(Box.createVerticalStrut(5));
		
		
		
		final ColorComboBox textColorChooser = new ColorComboBox();
		textColorChooser.setSelectedColor(textColor);
		textColorChooser.setPreferredSize(new Dimension(40, 25));
		
		textColorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textColor = ((ColorComboBox) e.getSource()).getSelectedColor();
					
				if(textColor == null){
					textColor = Color.black;
					textColorChooser.setSelectedColor(textColor);
				}
				previewLabel.setForeground(textColor);
				
				previewLabelInsert.setForeground(textColor);
			}
		});
		
		JPanel textColorPanel = new JPanel();
		textColorPanel.setLayout(new BoxLayout(textColorPanel, BoxLayout.X_AXIS));
		textColorPanel.setPreferredSize(new Dimension(150, 25));
		
		textColorPanel.add(new JLabel("Text Color "));
		textColorPanel.add(Box.createHorizontalStrut(40));
		textColorPanel.add(textColorChooser);
		textColorPanel.add(Box.createHorizontalGlue());
		
		
		
		ColorComboBox bkgColorChooser = new ColorComboBox();
		bkgColorChooser.setPreferredSize(new Dimension(60, 25));
		bkgColorChooser.setSelectedColor(bkgColor);
		
		bkgColorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bkgColor = ((ColorComboBox) e.getSource()).getSelectedColor();
				
				if(bkgColor == null){
					previewLabel.setOpaque(false);
					previewLabel.setBackground(bkgColor);
					
					previewLabelInsert.setOpaque(false);
					previewLabelInsert.setBackground(bkgColor);
				}
				else {
					previewLabel.setOpaque(true);
					previewLabel.setBackground(bkgColor);
					
					previewLabelInsert.setOpaque(true);
					previewLabelInsert.setBackground(bkgColor);
				}
			}
		});
		
		JPanel textBkgColorPanel = new JPanel();
		textBkgColorPanel.setLayout(new BoxLayout(textBkgColorPanel, BoxLayout.X_AXIS));
		textBkgColorPanel.setPreferredSize(new Dimension(150, 25));
		
		textBkgColorPanel.add(new JLabel("Background Color "));
		textBkgColorPanel.add(Box.createHorizontalGlue());
		textBkgColorPanel.add(bkgColorChooser);
		textBkgColorPanel.add(Box.createHorizontalGlue());
		
		JPanel previewPanel = new JPanel();
		previewPanel.setPreferredSize(new Dimension(300, 100));
		previewPanel.setMaximumSize(new Dimension(300, 100));
		previewPanel.add(previewLabel);
		previewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		panel.add(fontPanel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(textColorPanel);
//		panel.add(Box.createVerticalStrut(5));
//		panel.add(textBkgColorPanel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(previewPanel);
		
		return panel;
	}
	
	public Font getF() {
		return f;
	}
	public void setF(Font f) {
		this.f = f;
	}
	public Color getTextColor() {
		return textColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	public Color getBkgColor() {
		return bkgColor;
	}
	public void setBkgColor(Color bkgColor) {
		this.bkgColor = bkgColor;
	}
	public String getText() {
		return textField.getText();
	}
	public void setText(String text) {
		textField.setText(text);
	}
	private Component getInsertComponent() {
		previewLabelInsert = new JLabel(text);
		previewLabelInsert.setFont(f);
		previewLabelInsert.setOpaque(true);
		previewLabelInsert.setForeground(textColor);
		previewLabelInsert.setBackground(bkgColor);
		
		JPanel previewPanel = new JPanel();
		previewPanel.setPreferredSize(new Dimension(300, 100));
		previewPanel.setMaximumSize(new Dimension(300, 100));
		previewPanel.add(previewLabelInsert);
		previewPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());
		
		textField = new JTextField(20);
		textField.setText(text);
		
		textField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
				String text = textField.getText();
				previewLabel.setText(text);
				previewLabelInsert.setText(text);
			}
			public void keyPressed(KeyEvent e) {
			}
		});
		
		textField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				textField.selectAll();
			}
		});
		
		textPanel.add(new JLabel("Edit Text "));
		textPanel.add(textField);
		
		panel.add(textPanel);
		panel.add(Box.createVerticalStrut(15));
		panel.add(previewPanel);
		
		return panel;
	}
}
