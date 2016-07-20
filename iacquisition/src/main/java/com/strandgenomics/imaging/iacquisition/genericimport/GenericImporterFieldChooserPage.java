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

package com.strandgenomics.imaging.iacquisition.genericimport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.wizard.AbstractWizardPage;
import com.strandgenomics.imaging.icore.bioformats.custom.FieldType;

/**
 * Generic Importer Wizard page to associate field values with file name tokens
 * 
 * @author Anup Kulkarni
 */
public class GenericImporterFieldChooserPage extends AbstractWizardPage{

	private static final long serialVersionUID = -8863608130520353649L;
	/**
	 * List of interpreted fields 
	 */
	private FieldType[] items = FieldType.values();
	/**
	 * model driving wizard
	 */
	private GenericImporterWizardModel model;
	private JPanel mainPanel;
	
	public GenericImporterFieldChooserPage(String title, String desciption, GenericImporterWizardModel model) {
		super(title, desciption);
		
		this.model = model;
	}
	
	@Override
	public JComponent createWizardContent()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(getContentThinBorder());
        return mainPanel;
	}

	public void updatePanel()
	{
		mainPanel.removeAll();
		
		JPanel selectedFileName = new JPanel();
		selectedFileName.setMaximumSize(new Dimension(450, 20));
		selectedFileName.setLayout(new BoxLayout(selectedFileName, BoxLayout.X_AXIS));
		
		selectedFileName.add(new JLabel("Selected File :"));
		selectedFileName.add(Box.createHorizontalStrut(10));
		JTextField tf = new JTextField(model.getSelectedFile());
		tf.setEditable(false);
		selectedFileName.add(tf);
		
		mainPanel.add(selectedFileName);
		mainPanel.add(Box.createVerticalStrut(10));
		
		List<String> fieldNames = model.tokenizeFieldNames();
		
		JPanel fieldsPane = new JPanel(new GridLayout(fieldNames.size()+1,2,10,10));
		fieldsPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
	    final Map<Integer, FieldType> positionToFieldMap = model.getPositioToFieldMap();
		
		for(int i=0;i<fieldNames.size();i++)
		{
			final JComboBox fieldType = new JComboBox(items);
			fieldType.setActionCommand(String.valueOf(i));
			fieldType.setSelectedItem(FieldType.IGNORE);
			if(positionToFieldMap.containsKey(i))
				fieldType.setSelectedItem(positionToFieldMap.get(i));
			
			String name = fieldNames.get(i);
			
	        JLabel label = new JLabel(name+":");
	        fieldsPane.add(label);
	        fieldsPane.add(fieldType);
			
			fieldType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					Collection<FieldType> selectedValues = positionToFieldMap.values();
					if(fieldType.getSelectedIndex()!=0 && selectedValues.contains((FieldType)fieldType.getSelectedItem()))
					{
						JOptionPane.showMessageDialog(mainPanel, "This field is already chosen", "Error", JOptionPane.ERROR_MESSAGE);
						fieldType.setSelectedIndex(0);
						return;
					}
					positionToFieldMap.put(Integer.parseInt(e.getActionCommand()), (FieldType)fieldType.getSelectedItem());
				}
			});
			
			//default value
			positionToFieldMap.put(i, (FieldType)fieldType.getSelectedItem());
		}
		
		final JComboBox multiImageFieldType = new JComboBox(items);
		multiImageFieldType.setActionCommand("multiimage");
		multiImageFieldType.setSelectedItem(FieldType.IGNORE);
		multiImageFieldType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Collection<FieldType> selectedValues = positionToFieldMap.values();
				if(multiImageFieldType.getSelectedIndex()!=0 && selectedValues.contains((FieldType)multiImageFieldType.getSelectedItem()))
				{
					JOptionPane.showMessageDialog(mainPanel, "This field is already chosen", "Error", JOptionPane.ERROR_MESSAGE);
					multiImageFieldType.setSelectedIndex(0);
					return;
				}
				
				model.setMultiImageDimenstion((FieldType)multiImageFieldType.getSelectedItem());
			}
		});
		
        JLabel label = new JLabel("multi-image mapping:");
        fieldsPane.add(label);
        fieldsPane.add(multiImageFieldType);
		
		JPanel dummyParentPanel = new JPanel(new BorderLayout());
		JPanel dummyCenterPanel = new JPanel(new BorderLayout());

		dummyParentPanel.add(fieldsPane, BorderLayout.NORTH);
		dummyParentPanel.add(dummyCenterPanel, BorderLayout.CENTER);
			
		mainPanel.add(new JScrollPane(dummyParentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		mainPanel.add(Box.createVerticalStrut(10));
	}

	@Override
	public void setupWizardButtons()
	{
		fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
        fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
        fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
        
        updatePanel();
	}
}
