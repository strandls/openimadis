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

package com.strandgenomics.imaging.iclient.daemon;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * apis required for record upload service 
 * 
 * @author Anup Kulkarni
 */
public interface UploadDaemonService extends Remote{
	/**
	 * submits upload request
	 * @param spec
	 * @throws RemoteException
	 * @throws Exception 
	 */
	public void submitUploadRequest(UploadSpecification spec) throws RemoteException, Exception;
	/**
	 * get the status of uploads
	 * @param userName name of user
	 * @param projectName name of target project
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UploadSpecification> getStatus(String userName, String projectName) throws RemoteException, Exception;
	/**
	 * clears the completed(either failed or successful) uploads from the daemon session
	 * @param user name of user
	 * @param projectName name of target project
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void clearCompleted(String user, String projectName) throws RemoteException, Exception;
	/**
	 * cancels the selected uploads
	 * @param userName
	 * @param uploadIds
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void cancelSelected(String userName, List<Long>uploadIds) throws RemoteException, Exception;
}
