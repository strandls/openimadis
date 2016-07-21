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

package com.strandgenomics.imaging.icore.util;

import java.io.File;

/**
 * UI component interested with the current state of the upload
 * @author arunabha
 *
 */
public interface UploadObserver{

	/**
	 * This method is called when the given file is getting uploaded 
	 * @param source the file which is being uploaded
	 * @param totalBytes total bytes to transfer
	 * @param bytesUploaded number of bytes actually transferred
	 */
	public void reportProgress(File source, long totalBytes, long bytesUploaded);

	/**
	 * This method is called to check if current upload is cancelled
	 * @return true if current upload is cancelled; false otherwise
	 */
	public boolean isCancelled();

	/**
	 * This method is called when record user fields are uploaded
	 * @param progress value of the progress
	 * @param message associated with current upload
	 */
	public void reportProgress(int progress, String message);
	
}
