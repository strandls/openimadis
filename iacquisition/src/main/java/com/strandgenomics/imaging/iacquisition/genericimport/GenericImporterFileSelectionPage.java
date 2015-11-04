package com.strandgenomics.imaging.iacquisition.genericimport;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.jidesoft.combobox.FileChooserComboBox;
import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.wizard.AbstractWizardPage;

/**
 * Generic Importer Wizard Page for selection of sample filename, and file separator
 * 
 * @author Anup Kulkarni
 */
public class GenericImporterFileSelectionPage extends AbstractWizardPage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7906201337685337779L;
	/**
	 * model driving wizard
	 */
	private GenericImporterWizardModel model;

	public GenericImporterFileSelectionPage(String title, String description, GenericImporterWizardModel model) {
		super(title, description);
		
		this.model = model;
	}

	@Override
	public JComponent createWizardContent()
	{
		JPanel panel = createFileSelectionPanel();
        panel.setBorder(getContentThinBorder());
        return panel;
	}
	
	private JPanel createFileSelectionPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		final FileChooserComboBox sampleFilename = new FileChooserComboBox() {
			protected void customizeFileChooser(JFileChooser fileChooser)
			{
				String currentDirectory = System.getProperty("imanage.user.dir");
				if(currentDirectory == null)
					currentDirectory = System.getProperty("user.dir");
				if(currentDirectory!=null)
					fileChooser.setCurrentDirectory(new File(currentDirectory));
				
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				fileChooser.setFileFilter(new FileFilter() {
					
					String[] acceptedExtensions = {"TIF", "TIFF", "JPG", "PNG", "DCM"};
					
					@Override
					public String getDescription()
					{
						StringBuffer sb = new StringBuffer();
						for(String ext:acceptedExtensions)
						{
							sb.append(ext);
							sb.append(" ");
						}
						
						return sb.toString();
					}
					
					@Override
					public boolean accept(File f)
					{
						if(f.isDirectory()) return true;
						
						if(!f.getName().contains("."))
							return false;
						
						String extension = f.getName().substring(f.getName().lastIndexOf(".")+1).toUpperCase();
						for(String ext:acceptedExtensions)
						{
							if(ext.equals(extension))
								return true;
						}
						
						return false;
					}
				});
			}
		};
		
		sampleFilename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				File item = (File) sampleFilename.getSelectedItem();
				if(item == null)
					return;
				String rootDir = item.getParent();
				String fileName = item.getName();
				
				model.setRootDirectory(rootDir);
				model.setSelectedFile(fileName);
				System.setProperty("imanage.user.dir", rootDir);
			}
		});
		
		final JTextField fieldSeparator = new JTextField();
		fieldSeparator.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e)
			{
				String separator = fieldSeparator.getText();
				model.setSeparator(separator);
			}
			
			@Override
			public void focusGained(FocusEvent e)
			{ }
		});
		
		final JCheckBox doValidationBox = new JCheckBox("Validate All Files For Consistency");
		doValidationBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				boolean value = doValidationBox.isSelected();
				if(value)
				{
					int retval = JOptionPane.showConfirmDialog(null, "File validation may take time depending upon number of files. Are you sure you want to validate?", "Source File Validation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if(retval == JOptionPane.NO_OPTION)
					{
						doValidationBox.setSelected(!value);
					}
				}
				model.setValidation(value);
			}
		});
		
		JPanel fileChooserPanel = new JPanel();
		fileChooserPanel.setMaximumSize(new Dimension(450, 50));
		fileChooserPanel.setLayout(new BoxLayout(fileChooserPanel, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel("Sample File :");
		fileChooserPanel.add(label);
		fileChooserPanel.add(Box.createHorizontalStrut(42));
		fileChooserPanel.add(Box.createHorizontalGlue());
		fileChooserPanel.add(sampleFilename);
		
		JPanel labelChooserPanel = new JPanel();
		labelChooserPanel.setMaximumSize(new Dimension(450, 90));
		labelChooserPanel.setLayout(new BoxLayout(labelChooserPanel, BoxLayout.X_AXIS));
		
		labelChooserPanel.add(new JLabel("Field Separator :"));
		labelChooserPanel.add(Box.createHorizontalStrut(20));
		labelChooserPanel.add(Box.createHorizontalGlue());
		labelChooserPanel.add(fieldSeparator);
		
		panel.add(fileChooserPanel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(labelChooserPanel);
		panel.add(Box.createVerticalGlue());
		panel.add(Box.createVerticalStrut(30));
		panel.add(doValidationBox);
		return panel;
	}

	@Override
	public void setupWizardButtons()
	{
		fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
        fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
        fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
	}
}
