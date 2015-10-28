package com.strandgenomics.imaging.iengine.zoom;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * service for image tiling
 * @author navneet
 *
 */
public interface TilingService extends Remote{
	
	/**
	 * submits the tiling task to executor service
	 * @param task
	 */
	public void submitTilingTask(ImageTilingTask task) throws RemoteException;
}
