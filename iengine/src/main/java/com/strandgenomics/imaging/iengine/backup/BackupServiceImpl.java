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

package com.strandgenomics.imaging.iengine.backup;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.system.Config;
import com.strandgenomics.imaging.iengine.worker.BackupServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * service handles project archival and restoration. each request is put in a queue.
 * 
 * @author Anup Kulkarni
 */
public class BackupServiceImpl extends ServiceMonitor implements BackupService {

	private static Object lock = new Object();
	
	private static BackupServiceImpl singleton = null;
	
	/**
	 * list of backup requests that are currently being processed
	 */
	private Map<Integer, BackupTask> backupRequests;
	
	/**
	 * list of restoration requests that are currently being processed
	 */
	private Map<Integer, RestorationTask> restorationRequests;
	
	/**
     * service to clean up the cache and db periodically.
     */
    private ScheduledThreadPoolExecutor cleanupService = null;
	
	/**
	 * backup service
	 */
	private ExecutorService projectBackupService = null;
	
	/**
     * How frequently should the cache be cleaned
     */
    private static final int CACHE_CLEAN_FREQUENCY = 24 * 60 * 60;
    
    private BackupServiceStatus serviceStatus = null;
    
    private ServiceType serviceType = ServiceType.BACKUP_SERVICE;
    
    private static Object padLock = new Object();
    
    private boolean isCleaning = false;
    
    /**
     * logger
     */
    private Logger logger;
	
	private BackupServiceImpl()
	{
		// single queue
		projectBackupService = Executors.newFixedThreadPool(1);
		
		backupRequests = new HashMap<Integer, BackupTask>();
		restorationRequests = new HashMap<Integer, RestorationTask>();
		
		cleanupService = new ScheduledThreadPoolExecutor(1);
		schedule();
				
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
		
		serviceStatus = new BackupServiceStatus();
		
		startServiceMonitor();
	}
	
	/**
	 * schedule the timer task for cleanup service
	 */
	private void schedule(){
		
		cleanupService.scheduleWithFixedDelay(new RequestCleaner(), CACHE_CLEAN_FREQUENCY, CACHE_CLEAN_FREQUENCY, TimeUnit.SECONDS);

	}
	
	/**
	 * returns the singleton instance of movie service
	 * @return the singleton instance of movie service
	 */
	public static BackupServiceImpl getInstance()
	{
		if(singleton == null)
		{
			synchronized(lock)
			{
				if(singleton == null)
				{
					Config.getInstance();
					singleton = new BackupServiceImpl();
				}
			}
		}
		
		return singleton;
	}

	@Override
	public void submitArchivalRequest(String actor, int projectId, String location) throws RemoteException
	{
		System.out.println("submitting archival request "+projectId);
		logger.logp(Level.INFO, "BackupServiceImpl", "submitArchivalRequest", "submitting archival request "+projectId);
		if(!backupRequests.containsKey(projectId))
		{
			BackupTask task = new BackupTask(actor, projectId, location);
			projectBackupService.submit(task);
			
			backupRequests.put(projectId, task);
		}
	}

	@Override
	public void cancelArchivalRequest(String actor, int projectId) throws RemoteException
	{
		logger.logp(Level.INFO, "BackupServiceImpl", "cancelArchivalRequest", "cancelling archival request "+projectId);
		if(backupRequests.containsKey(projectId))
		{
			BackupTask task = backupRequests.get(projectId);
			
			task.cancelTask();
		}
	}

	@Override
	public void submitRestorationRequest(String actorLogin, int projectId, String location, String sign) throws RemoteException
	{
		logger.logp(Level.INFO, "BackupServiceImpl", "submitRestorationRequest", "submitting restoration request "+projectId);
		if(!restorationRequests.containsKey(projectId))
		{
			RestorationTask task = new RestorationTask(actorLogin, projectId, location, sign);
			projectBackupService.submit(task);
			
			restorationRequests.put(projectId, task);
		}
	}

	@Override
	public void cancelRestorationRequest(String actorLogin, int projectId) throws RemoteException
	{
		logger.logp(Level.INFO, "BackupServiceImpl", "cancelRestorationRequest", "cancelling restoration request "+projectId);
		if(restorationRequests.containsKey(projectId))
		{
			RestorationTask task = restorationRequests.get(projectId);
			
			task.cancelTask();
		}
	}
	
	/**
	 * deletes the request which are finished (completed or failed)
	 * 
	 * @author Anup Kulkarni
	 */
	private class RequestCleaner extends Thread {
		@Override
		public void run()
		{
			synchronized (padLock) {
				isCleaning = true;
			}
			
			logger.logp(Level.INFO, "BackupServiceImpl", "RequestCleaner", "start cleaning requests");
			try
			{
				List<Integer>completedTasks = new ArrayList<Integer>();
				for(Entry<Integer, BackupTask> entry :backupRequests.entrySet())
				{
					BackupTask task = entry.getValue();
					if(task.isFinished())
					{
						completedTasks.add(entry.getKey());
					}
				}
				
				for(Integer task:completedTasks)
				{
					backupRequests.remove(task);
				}
				
				completedTasks.clear();
				for(Entry<Integer, RestorationTask> entry :restorationRequests.entrySet())
				{
					RestorationTask task = entry.getValue();
					if(task.isFinished())
					{
						completedTasks.add(entry.getKey());
					}
				}
				
				for(Integer task:completedTasks)
				{
					restorationRequests.remove(task);
				}
			}
			catch (Exception e)
			{
				logger.logp(Level.WARNING, "BackupServiceImpl", "RequestCleaner", "start cleaning requests", e);
			}
			
			synchronized (padLock) {
				isCleaning = false;
			}
		}
	}

	@Override
	public void cleanArchivalCache(int projectId) throws RemoteException
	{
		backupRequests.remove(projectId);
	}

	@Override
	public void cleanRestoreCache(int projectId) throws RemoteException
	{
		restorationRequests.remove(projectId);
	}

	@Override
	public ServiceStatus getServiceStatus() {
		
		synchronized (padLock) {
			serviceStatus.setCleaningUp(isCleaning);
		}
		
		serviceStatus.setNumberOfBackupRequests(backupRequests.size());
		serviceStatus.setNumberOfRestorationRequests(restorationRequests.size());
		
		return serviceStatus;
	}

	@Override
	public ServiceType getServiceType() {
		return serviceType;
	}

	@Override
	public void restart() {
		
		if(projectBackupService!=null){
			projectBackupService.shutdownNow();
			projectBackupService = Executors.newFixedThreadPool(1);
		}
		
		if(cleanupService!=null){
			cleanupService.shutdownNow();
			cleanupService = new ScheduledThreadPoolExecutor(1);
			schedule();
		}
		
		backupRequests.clear();
		restorationRequests.clear();
		
		synchronized (padLock) {
			isCleaning = false;
		}
		
	}
	
	/**
	 * shutdown cleaning service
	 * @throws DataAccessException
	 */
	public void shutdown() throws DataAccessException {
		
		stopServiceMonitor();
		
		if(projectBackupService!=null){
			projectBackupService.shutdownNow();
			projectBackupService = null;
		}
		
		if(cleanupService!=null){
			cleanupService.shutdownNow();
			cleanupService = null;
		}
		
		backupRequests.clear();
		restorationRequests.clear();
		
		synchronized (padLock) {
			isCleaning = false;
		}
		
	}
}
