package com.strandgenomics.imaging.iengine.movie;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * service used to prefetch images contributing to a movie
 * 
 * @author Anup Kulkarni
 */
public interface MovieService extends Remote {
	/**
	 * submits request for image for existing movie
	 * @param actor logged in user
	 * @param movieTicket specified movie
	 * @param ithImage requested image's slice/frame
	 */
	public void submitImageRequest(String actor, MovieTicket movieTicket, int ithImage) throws RemoteException;
	
	/**
	 * submits raw data prefetching request 
	 * @param actor logged in user
	 * @param cacheRequest specfied cache request
	 * @param ithImage starting index for prefetching raw data
	 * @throws RemoteException
	 */
	public void submitCacheRequest(String actor, CacheRequest cacheRequest, int ithImage) throws RemoteException;
	
	/**
	 * submits request for deleting existing movie
	 * @param actor logged in user
	 * @param movieID specified movie
	 */
	public void deleteMovie(String actor, long movieID) throws RemoteException;
}
