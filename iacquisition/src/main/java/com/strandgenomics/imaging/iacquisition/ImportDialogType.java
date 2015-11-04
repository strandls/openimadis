package com.strandgenomics.imaging.iacquisition;

import javax.swing.JFileChooser;

/**
 * import dialog types
 * 
 * @author Anup Kulkarni
 */
public enum ImportDialogType {

	/**
	 * import dialog used for direct uploading without record creation
	 */
	DIRECT_UPLOAD_IMPORT_DIALOG(JFileChooser.DIRECTORIES_ONLY, "Upload using background service"), 
	
	/**
	 * import dialog used when records will be created in acquisition client
	 */
	NORMAL_IMPORT_DIALOG(JFileChooser.FILES_AND_DIRECTORIES, "Check for duplicate before import");
	
	/**
	 * filechooser option to allow files/directories to be chosen
	 */
	private int filechooserOption;
	/**
	 * text to be displayed on the checkbox
	 */
	private String checkboxString;

	private ImportDialogType(int filechooserOption, String checkboxString)
	{
		this.filechooserOption = filechooserOption;
		this.checkboxString = checkboxString;
	}

	/**
	 * returns the value of filechooser
	 * @return the value of filechooser
	 */
	public int getFilechooserOption()
	{
		return filechooserOption;
	}

	/**
	 * returns the value of checkbox text
	 * @return the value of checkbox text
	 */
	public String getCheckboxString()
	{
		return checkboxString;
	}
}
