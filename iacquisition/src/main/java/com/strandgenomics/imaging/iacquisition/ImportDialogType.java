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
