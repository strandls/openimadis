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
