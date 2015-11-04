package com.strandgenomics.imaging.iacquisition.genericimport;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import com.jidesoft.dialog.PageList;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.wizard.AbstractWizardPage;
import com.jidesoft.wizard.WizardDialog;
import com.jidesoft.wizard.WizardIconsFactory;
import com.strandgenomics.imaging.icore.bioformats.custom.RecordMetaData;

/**
 * Dialog which will ask for sample file, sample filename, separator
 * 
 * @author Anup Kulkarni
 */
public class GenericImporterWizard {
	/**
	 * model driving generic importer wizard
	 */
	private GenericImporterWizardModel model;
	
	String rootDirectory = null;
	
	public GenericImporterWizard()
	{ 
		model = new GenericImporterWizardModel();
	}
	
	public List<RecordMetaData> showImporterWizard(JFrame parent, String title)
	{
		final WizardDialog importerWizard = new WizardDialog(parent, title);
		model.setParent(importerWizard);
		importerWizard.setDefaultGraphic(WizardIconsFactory.getImageIcon(WizardIconsFactory.Metal.INFO).getImage());
		
		// create wizard pages
		final AbstractWizardPage page1 = new GenericImporterFileSelectionPage("Select File and Separator", "Select a sample file and specify field separator in the filename. For multi-image tiffs, only first image will be selected.", model);
		final AbstractWizardPage page2 = new GenericImporterFieldChooserPage("Specify Field Mapping", "Map field(s) parsed from filename to the record meta data fields", model);
		final AbstractWizardPage page3 = new GenericImporterSummaryPage("Summary", "Summary of record(s) found in selected directory", model);

		final List<RecordMetaData> result = new ArrayList<RecordMetaData>();
		
		importerWizard.setFinishAction(new AbstractAction("Finish") 
        {
			private static final long serialVersionUID = 8829533121997978210L;

			public void actionPerformed(ActionEvent e) 
            {
				if(importerWizard.closeCurrentPage())
				{
					result.clear();
					
					List<RecordMetaData> selectedRecords = model.getSelectedRecords();
					if(selectedRecords!=null && selectedRecords.size()>0)
					{
						result.addAll(selectedRecords);
					}
	                importerWizard.dispose();
				}
            }
        });
        
		// setup model
		PageList model = new PageList();
		model.append(page1);
		model.append(page2);
		model.append(page3);
		importerWizard.setPageList(model);

		importerWizard.pack();
		importerWizard.setResizable(true);
        JideSwingUtilities.globalCenterWindow(importerWizard);
        importerWizard.setVisible(true);
        
        return result;
	}
	
	public static void main(String... args)
	{
		//jide license
		com.jidesoft.utils.Lm.verifyLicense("Strand Life Science", "avadis", "BDhYK:qTCkBmgfpIFrqclqSmzUdXtXh2");
		GenericImporterWizard gimp = new GenericImporterWizard();
		System.out.println(gimp.showImporterWizard(null, "Generic Importer Wizard"));
	}
}
