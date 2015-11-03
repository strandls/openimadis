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
