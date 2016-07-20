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

package com.strandgenomics.imaging.iengine.worker;

import com.strandgenomics.imaging.icore.Storable;

/**
 * represents the service running on worker
 * @author navneet
 *
 */
public class Service implements Storable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Service(ServiceType serviceType, ServiceStatus serviceStatus) {
		super();
		this.serviceType = serviceType;
		this.serviceStatus = serviceStatus;
	}
	/**
	 * identifier of worker on which service is running
	 */
	private int workerId;

	public Service(int workerId, ServiceType serviceType,
			ServiceStatus serviceStatus) {
		super();
		this.setWorkerId(workerId);
		this.serviceType = serviceType;
		this.serviceStatus = serviceStatus;
	}
	/**
	 * type of service
	 */
	private ServiceType serviceType;
	
	/**
	 * status of service
	 */
	private ServiceStatus serviceStatus;

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(ServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public int getWorkerId() {
		return workerId;
	}

	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}
	
}
