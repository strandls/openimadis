package com.strandgenomics.imaging.iacquisition;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.strandgenomics.imaging.icore.IExperiment;
import com.strandgenomics.imaging.icore.ISourceReference;

/**
 * UI class used for different dialog boxes showed by Acq Client 
 * @author Anup Kulkarni
 */
public class AcqclientUIUtils {
	/**
	 * Shows Directory Chooser Dialog Box
	 * @return selected root directories
	 */
	
	static boolean checkBoxValue =false;
	
	@SuppressWarnings("serial")
	public static File[] showDirectoryChooser(JFrame parent ,String title, ImportDialogType type){

		String currentDirectory = System.getProperty("imanage.user.dir");
		if(currentDirectory == null)
			currentDirectory = System.getProperty("user.home");
		FileSystemView fsv = FileSystemView.getFileSystemView();
		
		ImportDialog importDialog = new ImportDialog(parent, title, currentDirectory, fsv, type);
		checkBoxValue = importDialog.isCheckBoxSelected();

		return importDialog.getImportFiles();
	}
	
	public static File showFileChooser(String title){
		JFileChooser fileChooser = null;
		String currentDirectory = System.getProperty("imanage.user.dir");
		if(currentDirectory == null)
			currentDirectory = System.getProperty("user.dir");
		FileSystemView fsv = FileSystemView.getFileSystemView();
		if (currentDirectory == null && fsv == null)
			fileChooser = new JFileChooser();
		else if (currentDirectory != null && fsv == null)
			fileChooser = new JFileChooser(currentDirectory);
		else if (currentDirectory == null && fsv != null)
			fileChooser = new JFileChooser(fsv);
		else
			fileChooser = new JFileChooser(currentDirectory, fsv);

		fileChooser.setDialogTitle(title);
		int retValue = fileChooser.showSaveDialog(null);
		
		if(retValue == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			return file;
		}
		
		return null;
	}
	
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	
	/**
	 * Shows File Chooser Dialog Box
	 * @return selected File
	 */
	public static File showFileChooser(){
		JFileChooser fileChooser = null;
		String currentDirectory = System.getProperty("user.dir");
		FileSystemView fsv = FileSystemView.getFileSystemView();
		if (currentDirectory == null && fsv == null)
			fileChooser = new JFileChooser();
		else if (currentDirectory != null && fsv == null)
			fileChooser = new JFileChooser(currentDirectory);
		else if (currentDirectory == null && fsv != null)
			fileChooser = new JFileChooser(fsv);
		else
			fileChooser = new JFileChooser(currentDirectory, fsv);

		fileChooser.setDialogTitle("Custom File Import");
		int selectionMode = JFileChooser.FILES_ONLY;
		fileChooser.setFileSelectionMode(selectionMode);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Zeiss LSM 710 (.lsm)","lsm"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Zeiss Vision Image (.zvi)","zvi"));
	    int ret = fileChooser.showOpenDialog(null);
	    if (ret == JFileChooser.APPROVE_OPTION) 
	        return fileChooser.getSelectedFile();
	    else
	    	return null;
	}
	
	/**
	 * shows dialog box for uploading the records
	 * @param experimentsToUpload 
	 */
	/*public static boolean showUploadDialog(JFrame parent, List<IExperiment> experimentsToUpload) {
		List<Project> activeProjects = ImageSpaceObject.getConnectionManager().getActiveProjects();
		if(activeProjects == null || activeProjects.isEmpty()){
			JOptionPane.showMessageDialog(null, "You are not member of any project", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		UploadDialog uploadDialog = new UploadDialog(parent, experimentsToUpload, activeProjects);
		uploadDialog.setVisible(true);
		
		return uploadDialog.returnValue();
	}*/

	/**
	 * shows dialog box for deleting the records
	 * @param experimentsToDelete 
	 * @return final list of experiments to be deleted
	 */
	public static List<IExperiment> showDeleteDialog(JFrame frame,
			Set<IExperiment> experimentsToDelete) {
		DeleteDialog deleteDialog = new DeleteDialog(frame, experimentsToDelete);
		deleteDialog.setVisible(true);
		
		return deleteDialog.getSelectedExperiments();
	}
	
	/**
	 * returns the size of experiment source files
	 * @param expr
	 * @return
	 */
	public static String getExperimentSize(IExperiment expr){
		long totalSize = 0;
		List<ISourceReference> sourceFiles = expr.getReference();
		for(int i=0;i<sourceFiles.size();i++){
			totalSize += sourceFiles.get(i).getSize();
		}
		
		return readableFileSize(totalSize);
	}
	
	/**
	 * converts long size to human readable format
	 * @param size
	 * @return
	 */
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static Boolean getCheckBoxValue() {
		return checkBoxValue;
	}
}
