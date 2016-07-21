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

package com.strandgenomics.imaging.iserver.services.def.loader;

import java.rmi.RemoteException;

/**
 * Sample loader services
 * @author arunabha
 *
 */
public interface ImageSpaceLoader {
	
    /**
     * checks if the specified archive exist, then returns the relevant project
     * @param the access token required to make this call
     * @param archiveSignature archive signature
     * @return the project containing the archive, otherwise null
     * @throws RemoteException
     */
    public Archive findArchive(String accessToken, String archiveSignature) 
    		throws RemoteException;
    
	/**
	 * Returns the URL to download the specified archive
	 * @param the access token required to make this call
	 * @param archiveSignature archive signature
	 * @throws RemoteException 
	 */
	public String getArchiveDownloadURL(String accessToken, String archiveSignature) 
			throws RemoteException;
	
	/**
	 * Returns the URL to download the specified record as a tar.gz of tiff images and xml meta data
	 * @param the access token required to make this call
	 * @param guid the record GUID
	 * @throws RemoteException 
	 */
	public String getRecordDownloadURL(String accessToken, long guid) 
			throws RemoteException;
	
	/**
	 * Request for a ticket
	 * @param project the relevant project
	 * @param request the description of the request
	 * @return the ticket
	 * @throws RemoteException
	 */
	public UploadTicket recordCreationRequest(String accessToken, String projectName, CreationRequest request)
			throws RemoteException;
	
	/**
	 * Request for a ticket for direct upload, record will be indexed on server
	 * @param project the relevant project
	 * @param request the description of the request
	 * @return the ticket
	 * @throws RemoteException
	 */
	public UploadTicket directUploadCreationRequest(String accessToken, String projectName, CreationRequest request)
			throws RemoteException;
	
	/**
	 * Returns the status of the specified ticketID
	 * @param ticketID  ticket id
	 * @return status - WAITING, EXECUTING, SUCCESS, FAILURE
	 */
	public String getTicketStatus(String accessToken, long ticketID);
	
	/**
	 * registers record builder which represents specification of record under construction
	 * @param accessToken
	 * @param rb record builder object
	 * @return identifier for record under construction on server side
	 * @throws RemoteException
	 */
	public Long registerRecordBuilder(String accessToken, RecordBuilderObject rb) throws RemoteException;
	
	/**
	 * adds specified raw image data to specified record under construction along with the pixel metadata 
	 * @param accessToken
	 * @param guid
	 * @param pixelArr
	 * @param pixelMetadata
	 * @return url to upload image data
	 * @throws RemoteException
	 */
	public String addImageData(String accessToken, long guid, ImageIndex dim, Image pixelMetadata) throws RemoteException;
	
	/**
	 * commits the record construction process as a result of which new record is created from the supplied image data
	 * @param accessToken
	 * @param guid specified record identifier
	 * @throws RemoteException
	 */
	public void commitRecordCreation(String accessToken, long guid) throws RemoteException;
	
	/**
	 * aborts record creation process, all the work done till now will be undone
	 * @param accessToken
	 * @param guid specified record identifier
	 * @throws RemoteException
	 */
	public void abortRecordCreation(String accessToken, long guid) throws RemoteException;
}
