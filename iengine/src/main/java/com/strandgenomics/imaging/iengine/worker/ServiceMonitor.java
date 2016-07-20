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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;

/**
 * class used by services running on worker to update 
 * their status.
 * @author navneet
 *
 */
public abstract class ServiceMonitor {
	
	/**
	 * timer to execute updates at fixed intervals
	 */
	 private Timer updateTimer;
	 
	 /**
	  * timer to check at regular intervals if service has to
	  * be restarted 
	  */
	 private Timer restartTimer;
	 
	 /**
	  * the time interval for the update timer task
	  */
	 public static int updatePeriod = 5000;
	 
	 /**
	  * time interval for restart timer task
	  */
	 public static int restartPeriod = 10000;
	 
	 /**
	  * the status of service
	  */
	 private ServiceStatus serviceStatus;
	 
	 /**
	  * type of service
	  */
	 private ServiceType serviceType;
	 
	 public ServiceMonitor() {
		
	 }
	 
	 /**
	  * start the task which will update status of service at regular intervals
	  */
	 public void startServiceMonitor(){
		 startUpdateTimer();
		 startRestartTimer();
	 }
	 
	 /**
	  * stop the timer task
	 * @throws DataAccessException 
	  */
	 public void stopServiceMonitor() throws DataAccessException{
		 stopUpdateTimer();
		 stopRestartTimer();
		 
		 removeService();
	 }
	 
	 /**
	  * start update timer task
	  */
	 public void startUpdateTimer(){
		updateTimer = new Timer(true); 
		
		TimerTask monitor = new UpdateTimerTask();
		updateTimer.scheduleAtFixedRate(monitor, 0, updatePeriod);
	 }
	 
	 /**
	  * start restart timer task
	  */
	 public void startRestartTimer(){
		restartTimer = new Timer(true);
		 
		TimerTask monitor = new RestartTimerTask();
		restartTimer.scheduleAtFixedRate(monitor, 0, restartPeriod);

	 }
	
	 /**
	  * stop the timer task performing updates
	  */
	 public void stopUpdateTimer(){
		 
		 updateTimer.cancel();
		 updateTimer.purge();		 
	 }
	 
	 /**
	  * stop the timer task which checks if service
	  * has to be restarted
	  */
	 public void stopRestartTimer(){
		 
		 restartTimer.cancel();
		 restartTimer.purge();		 
	 }
	 
	 /**
	  * set service type
	  */
	public void setServiceType(ServiceType serviceType){
		this.serviceType = serviceType;
	}
	
	 /**
	  * set the status for the service
	  */
	public void setServiceStatus(ServiceStatus serviceStatus){
		this.serviceStatus = serviceStatus; 
	}
	
	/**
	 * update the status of service
	 * @throws DataAccessException 
	 */
	private void updateServiceStatus() throws DataAccessException{
		
		ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().updateServiceStatus(serviceType, serviceStatus);
	}
	
	/**
	 * @throws DataAccessException 
	 * 
	 */
	private boolean shouldRestartService() throws DataAccessException{
		
		return ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().getToBeRestarted(serviceType);
	}
	
	/**
	 * update that service has been restarted
	 * @throws DataAccessException
	 */
	private void restartedService() throws DataAccessException{
		
		ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().setToBeRestarted(serviceType,false);
	}
	
	/**
	 * remove the service and its status
	 * @throws DataAccessException
	 */
	private void removeService() throws DataAccessException{
		
		ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().removeService(serviceType);
	}
	
	/**
	 * to be implemented by the service which has to be monitored
	 * gets status from service
	 * @return
	 */
	abstract public ServiceStatus getServiceStatus();
	
	/**
	 * to be implemented by the service which has to be monitored
	 * gets the type from service
	 * @return
	 */
	abstract public ServiceType getServiceType();
	
	/**
	 * restart the service
	 */
	abstract public void restart();

	class UpdateTimerTask extends TimerTask
	{
		@Override
		public void run() {
			
			setServiceStatus(getServiceStatus());
			setServiceType(getServiceType());
			
			try {
				updateServiceStatus();
				
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class RestartTimerTask extends TimerTask
	{
		@Override
		public void run() {
				try {
					if(shouldRestartService()){
						
						stopUpdateTimer();
						
						restart();
						
						restartedService();
						
						startUpdateTimer();
						
					}
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
}
