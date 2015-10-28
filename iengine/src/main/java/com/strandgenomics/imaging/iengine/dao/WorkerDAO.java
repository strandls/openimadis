package com.strandgenomics.imaging.iengine.dao;

import java.util.List;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.worker.Service;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * 
 * @author navneet
 *
 */
public interface WorkerDAO {
	
	/**
	 * insert the service status
	 * @param serviceType
	 * @param serviceStatus
	 * @throws DataAccessException 
	 */
	public void insertServieStatus(ServiceType serviceType ,ServiceStatus serviceStatus) throws DataAccessException;
	
	/**
	 * update the service status
	 * @param serviceType
	 * @param serviceStatus
	 * @throws DataAccessException 
	 */
	public void updateServiceStatus(ServiceType serviceType ,ServiceStatus serviceStatus) throws DataAccessException;
	
	/**
	 * get service status
	 * @param serviceType
	 * @return 
	 * @throws DataAccessException 
	 */
	public Service getServiceStatus(ServiceType serviceType) throws DataAccessException;
	
	/**
	 * get time stamp when last updated
	 * @param serviceType
	 * @param serviceStatus
	 * @return
	 * @throws DataAccessException
	 */
	public long getLastModificationTime(ServiceType serviceType) throws DataAccessException;
	
	/**
	 * get status of all services running across all workers
	 * @return
	 * @throws DataAccessException
	 */
	public List<Service> getAllServiceStatus() throws DataAccessException;
	
	/**
	 * get if service has to be restarted
	 * @param serviceType
	 * @return
	 * @throws DataAccessException 
	 */
	public boolean getToBeRestarted(ServiceType serviceType) throws DataAccessException;
	
	/**
	 * set if service has to be restarted for particular worker
	 * @param serviceType
	 * @return
	 */
	public void setToBeRestarted(int workerId, ServiceType serviceType, boolean toBeRestarted) throws DataAccessException;
	
	/**
	 * set if service to be restarted 
	 * @param serviceType
	 * @param toBeRestarted
	 * @throws DataAccessException
	 */
	public void setToBeRestarted(ServiceType serviceType, boolean toBeRestarted)
			throws DataAccessException;
	
	/**
	 * remove service and status
	 * @param serviceType
	 * @throws DataAccessException
	 */
	public void removeService(ServiceType serviceType) throws DataAccessException;
	
	/**
	 * remove service and status for specified worker
	 * @param serviceType
	 * @throws DataAccessException
	 */
	public void removeService(int workerId, ServiceType serviceType) throws DataAccessException;

}
