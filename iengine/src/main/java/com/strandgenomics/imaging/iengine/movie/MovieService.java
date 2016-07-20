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
