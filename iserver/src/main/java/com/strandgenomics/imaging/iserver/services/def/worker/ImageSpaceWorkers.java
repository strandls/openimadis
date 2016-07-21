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

package com.strandgenomics.imaging.iserver.services.def.worker;

import java.rmi.RemoteException;


/**
 * Compute Server system interface. This set of API allows a remote worker
 * to pull tasks to be executed
 */
public interface ImageSpaceWorkers {
	
    /**
     * the first thing a worker-manager is expected to do is to register itself
     * with the compute server as a trusted worker-manager with its name and
     * and the publisher-code obtained (using the web-client, the master application)
     * 
     * If the name and publisher-code matches, the server returns a valid access-token or permit
     *  
     * Once the worker-manager receives a permit or access-token, it is expected that
     * the worker-manager will continuously ping the server to maintain a heart-beat.
     * 
     * A valid WorkPermit is required to pull Task from the compute server 
     * (if available).
     * 
     * @param name name of the publisher
     * @param publisherCode the authorization code granted by the Administrator (from web-client)
     * @return the access token required to make calls if valid, null otherwise
     */
    public String register(String name, String publisherCode)
    	throws RemoteException;
    
    /**
     * The worker-system publishes a bunch of applications
     * @param accessToken the access token needed to make this call
     * @param apps a list of application supported by this worker-system
     * @return the directive from the compute-server to the worker
     */
    public Directive publishApplications(String accessToken, Application[] apps)
    		throws RemoteException;
    
    /**
     * The worker-system removes a bunch of published applications
     * @param accessToken the access token needed to make this call
     * @param apps a list of application no longer supported by this worker-system
     * @return the directive from the compute-server to the worker
     */
    public Directive removeApplications(String accessToken, Application[] apps)
    		throws RemoteException;
    
    /**
     * The worker-system ping the server periodically with its current status and 
     * picks up jobs and relevant directives from the server
     * @param accessToken the access token needed to make this call
     * @param request the worker request
     * @return the response of the server on such a request 
     */
    public Response ping(String accessToken, Request request)
    		throws RemoteException;
    
	//dummy api for exposing the constraints subclasses, remove if not needed anymore
    public StringListConstraints testMethod0(String appName) throws RemoteException;
	public DoubleListConstraints testMethod1(String appName) throws RemoteException;
	public DoubleRangeConstraints testMethod2(String appName) throws RemoteException;
	public LongListConstraints testMethod3(String appName) throws RemoteException;
	public LongRangeConstraints testMethod4(String appName) throws RemoteException;
}
