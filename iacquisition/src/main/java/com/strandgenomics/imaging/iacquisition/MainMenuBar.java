package com.strandgenomics.imaging.iacquisition;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.swing.JideMenu;
import com.strandgenomics.imaging.iacquisition.memory.MemoryMonitor;
import com.strandgenomics.imaging.icore.IRecord;
import com.strandgenomics.imaging.iviewer.UIUtils;

public class MainMenuBar extends CommandMenuBar {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2112261336375328692L;
	private Context context;


	public MainMenuBar(Context context, ActionListener listener) {
		this.context = context;
		setOpaque(true);
		setVisible(true);
		createGUI(listener);
		
	}
	
	private void createGUI(ActionListener listener) {
		
		JMenuItem rootDir = new JMenuItem("Import File(s)");
		rootDir.setActionCommand("index");
		rootDir.addActionListener(listener);
		
		JMenuItem directImport = new JMenuItem("Direct Upload");
		directImport.setActionCommand("directImport");
		directImport.addActionListener(listener);
		
		JMenuItem genericImport = new JMenuItem("Import Using Filename(s)");
		genericImport.setActionCommand("genericImport");
		genericImport.addActionListener(listener);
		
		JMenuItem rootFile = new JMenuItem("Custom");
		rootFile.setActionCommand("indexFile");
		rootFile.addActionListener(listener);
		
		JMenuItem changeProject = new JMenuItem("Change Project");
		changeProject.setActionCommand("changeProject");
		changeProject.addActionListener(listener);

		JMenuItem stopIndex = new JMenuItem("Stop Import");
		stopIndex.setActionCommand("stopIndexing");
		stopIndex.addActionListener(listener);
		
		JMenuItem saveSession = new JMenuItem("Save Session");
		saveSession.setActionCommand("saveSession");
		saveSession.addActionListener(listener);

		JMenuItem exit = new JMenuItem("Exit");
		exit.setActionCommand("exit");
		exit.addActionListener(listener);

		JideMenu indexMenu = new JideMenu("Import");
		indexMenu.add(rootDir);
		indexMenu.add(genericImport);
		indexMenu.add(new JSeparator());
	    indexMenu.add(stopIndex);
		indexMenu.add(new JSeparator());
		indexMenu.add(saveSession);
		indexMenu.add(new JSeparator());
		indexMenu.add(changeProject);
		indexMenu.add(new JSeparator());
		indexMenu.add(exit);

		JMenuItem upload = new JMenuItem("Upload Records");
		upload.setActionCommand("upload");
		upload.addActionListener(listener);
		
		JMenuItem uploadDaemon = new JMenuItem("Upload using Service");
		uploadDaemon.setActionCommand("uploadDaemon");
		uploadDaemon.addActionListener(listener);
		
		JMenuItem uploadSettings = new JMenuItem("Configure Service");
		uploadSettings.setActionCommand("uploadSettings");
		uploadSettings.addActionListener(listener);

		JMenuItem logoutMenu = new JMenuItem("Logout");
		logoutMenu.setActionCommand("logout");
		logoutMenu.addActionListener(listener);


		JideMenu uploadMenu = new JideMenu("Upload");
		uploadMenu.add(upload);
		uploadMenu.add(directImport);
		uploadMenu.add(uploadDaemon);
		uploadMenu.add(uploadSettings);

		/************* Records Menu *****************/
		JMenuItem selectAll = new JMenuItem("Select All Records");
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(context.getRecordCount()==0){
					showNoRecordsError();
					return;
				}
				context.setSelectedRecords(context.getRecords());
			}
		});

		JMenuItem clearSelection = new JMenuItem("Clear Selection");
		clearSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(context.getRecordCount()==0){
					showNoRecordsError();
					return;
				}
				context.setSelectedRecords(new ArrayList<IRecord>());
			}
		});

		JMenuItem invertSelection = new JMenuItem("Invert Selection");
		invertSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(context.getRecordCount()==0){
					showNoRecordsError();
					return;
				}
				List<IRecord> selectedRecords = context.getCheckedRecords();
				List<IRecord> allRecords = context.getRecords();
				ArrayList<IRecord> invertedSelection = new ArrayList<IRecord>();
				for (IRecord record: allRecords) {
					if(!selectedRecords.contains(record))
						invertedSelection.add(record);
				}
				context.setSelectedRecords(invertedSelection);
			}
		});
		
		JMenuItem merge = new JMenuItem("Merge Records");
		merge.setActionCommand("mergeRecords");
		merge.addActionListener(listener);
		
		JMenuItem delete = new JMenuItem("Remove Records");
		delete.setActionCommand("delete");
		delete.addActionListener(listener);
		
		JMenuItem locateOnDisk = new JMenuItem("Locate Record on Disk");
		locateOnDisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(context.getRecordCount()==0){
					showNoRecordsError();
					return;
				}
				if(context.getSelectedRecordCount()<1)
					
					JOptionPane.showMessageDialog(null, "Please select a record to locate on disk." , "Error", JOptionPane.ERROR_MESSAGE);
				else {
					try {
						if(Desktop.isDesktopSupported()){
							IRecord selectedRecord = context.getSelectedRecords().get(0);
							Desktop.getDesktop().open(new File(selectedRecord.getRootDirectory()));
						}
						else
							JOptionPane.showMessageDialog(null,
									"This operation is not supported on this system.",
									"Error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null,
								"Unable to open record in explorer/finder.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		JMenuItem copyChannel = new JMenuItem("Copy Channel LUT");
		copyChannel.setActionCommand("copyChannel");
		copyChannel.addActionListener(listener);
		
		JMenuItem pasteChannel = new JMenuItem("Paste Channel LUT");
		pasteChannel.setActionCommand("pasteChannel");
		pasteChannel.addActionListener(listener);
		
		JMenuItem resetChannel = new JMenuItem("Reset Channel LUT");
		resetChannel.setActionCommand("resetChannel");
		resetChannel.addActionListener(listener);


		JMenuItem setThumbnail = new JMenuItem("Set Thumbnail");
		setThumbnail.setActionCommand("setThumbnail");
		setThumbnail.addActionListener(listener);
		
		JMenuItem editProfile = new JMenuItem("Edit Acquisition Parameters");
		editProfile.setActionCommand("editProfile");
		editProfile.addActionListener(listener);
		
		JideMenu recordsMenu = new JideMenu("Records");
		recordsMenu.add(selectAll);
		recordsMenu.add(clearSelection);
		recordsMenu.add(invertSelection);
		recordsMenu.add(new JSeparator());
		recordsMenu.add(locateOnDisk);
		recordsMenu.add(delete);
		recordsMenu.add(merge);
		recordsMenu.add(new JSeparator());
		recordsMenu.add(copyChannel);
		recordsMenu.add(pasteChannel);
		recordsMenu.add(resetChannel);
		recordsMenu.add(new JSeparator());
		recordsMenu.add(editProfile);
		recordsMenu.add(new JSeparator());
		recordsMenu.add(setThumbnail);
		recordsMenu.add(new JSeparator());
		
		JMenuItem addProperty = new JMenuItem("Add Property");
		addProperty.setActionCommand("addProperty");
		addProperty.addActionListener(listener);

		JMenuItem removeProperty = new JMenuItem("Remove Property");
		removeProperty.setActionCommand("removeProperty");
		removeProperty.addActionListener(listener);

		recordsMenu.add(addProperty);
		recordsMenu.add(removeProperty);

		/************** Tools Menu ********************/

		JMenuItem memoryMenu = new JMenuItem("Memory Monitor");
		memoryMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemoryMonitor m = new MemoryMonitor();
				m.setVisible(true);
			}
		});
		
		JMenuItem scriptMenu = new JMenuItem("Script Editor");
		scriptMenu.setActionCommand("openEditor");
		scriptMenu.addActionListener(listener);
		
		JideMenu toolsMenu = new JideMenu("Tools");
		toolsMenu.add(scriptMenu);

		/************** Help Menu ********************/

		JMenuItem aboutMenu = new JMenuItem("About");
		aboutMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialogBox a = new AboutDialogBox(null);
				a.setName("iManage - Acquisition Client");
				a.setProductIcon(UIUtils.getIcon("largeicon.png"));
				a.setVersion("System Release");
				a.setBuild(context.getVersion());
				// TODO: add build info
				a.show();
			}
		});

		JMenuItem userGuide = new JMenuItem("User's Guide");
		userGuide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//TODO: add html file here
					String helpPath = System.getProperty("helpFilePath");
					Desktop.getDesktop().browse(new URI(helpPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		JideMenu helpMenu = new JideMenu("Help");
		helpMenu.add(aboutMenu);
		helpMenu.add(userGuide);
		helpMenu.add(memoryMenu);

		add(indexMenu);
		add(uploadMenu);
		add(recordsMenu);
//		add(toolsMenu);
	    add(helpMenu);	
	}

	protected void showNoRecordsError(){
		JOptionPane.showMessageDialog(
			AcquisitionUI.getFrame(),
			"Please Import File(s) or Change Project.",
			"No Records", JOptionPane.ERROR_MESSAGE);
	}


}

