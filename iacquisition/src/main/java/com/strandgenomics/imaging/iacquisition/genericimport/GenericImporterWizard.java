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
