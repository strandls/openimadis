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

package com.strandgenomics.imaging.iclient.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.strandgenomics.imaging.icore.IAttachment;
import com.strandgenomics.imaging.icore.IVisualOverlay;
import com.strandgenomics.imaging.icore.Signature;
import com.strandgenomics.imaging.icore.util.HttpUtil;

/**
 * class used for uploading data from Acq Client to Server
 * @author Anup Kulkarni
 */
public class UploadUtil {
	/**
     * upload the experiment tar file to server
     * @param uploadUrl
     * @param tarFile
	 * @return 
	 * @throws IOException 
     */
	public static boolean upload(URL uploadUrl, File tarFile) throws IOException
	{
		HttpUtil httpUtil = new HttpUtil(uploadUrl);
		return httpUtil.upload(tarFile);
	}
	
	/**
     * upload the attachment of a record to server
     * @param uploadUrl
     * @param signature of record on which file is attached
     * @param attachment attachment to be uploaded
     */
	public static void uploadAttachment(URL urlToUpload, Signature signature,
			IAttachment attachment) {
		//TODO
	}

	/**
	 * 
	 * @param urlToUpload
	 * @param signature of record on which file is attached
	 * @param key of user annotations
	 * @param value of user annotation
	 */
	public static void uploadUserAnnotation(URL urlToUpload, Signature signature,
			String key, Object value) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @param urlToUpload
	 * @param signature of record on which file is attached
	 * @param va visual overlay to be uploaded
	 */
	public static void uploadVisualAnnotation(URL urlToUpload,
			Signature signature, IVisualOverlay va) {
		// TODO Auto-generated method stub
		
	}
}
