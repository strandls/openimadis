package com.strandgenomics.imaging.iengine.zoom;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * service used to prefetch images for zoom
 * 
 * @author Anup Kulkarni
 */
public interface ZoomService extends Remote {
	/**
	 * submits zoom request
	 * @param actor
	 * @param zDim
	 * @param zoomLevel
	 */
	public void submitZoomRequest(String actor, long requestID, ZoomDimenstion zDim, int zoomLevel, int xTile, int yTile) throws RemoteException;

	/**
	 * removes the specified request from worker
	 * @param requestID
	 */
	public void removeZoomRequest(long requestID) throws RemoteException;;

}
