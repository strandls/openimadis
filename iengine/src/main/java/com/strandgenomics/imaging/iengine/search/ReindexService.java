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

package com.strandgenomics.imaging.iengine.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.solr.client.solrj.SolrServerException;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.cache.CacheKey;
import com.strandgenomics.imaging.iengine.cache.CacheKeyType;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;
import com.strandgenomics.imaging.iengine.worker.ReindexServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceMonitor;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * Populates Solr indices after regular intervals
 * 
 * @author Anup Kulkarni
 *
 */
public class ReindexService extends ServiceMonitor{
	
	/**
	 * logger
	 */
	private Logger logger;
	
	/**
	 * populator to populate indices
	 */
	public SolrPopulator populator;
	
	private ReindexServiceStatus serviceStatus = null;
	
	private ServiceType serviceType = ServiceType.REINDEX_SERVICE;
	
	/**
	 * keep track of when reindexing starts and end
	 */
	private boolean isReindexing = false;
	
	private static Object padLock = new Object();
	
	/**
	 * timer task
	 */
	public ScheduledThreadPoolExecutor timer = null;
	
	/**
	 * interval between two successive executions: default 2 Hours
	 */
	public static final long REINDEX_INTERVAL = Constants.getSolrReindexingInterval();
	
	public ReindexService() {
		logger = Logger.getLogger("com.strandgenomics.imaging.iengine.search");
		
		populator = new SolrPopulator();
		timer = new ScheduledThreadPoolExecutor(1);
		
		serviceStatus = new ReindexServiceStatus();
		
		startServiceMonitor();
	}
	
	public void schedule()
	{
		logger.logp(Level.INFO, "ReindexService", "schedule", "Starting service");
		
		try
		{
			timer.scheduleWithFixedDelay(new ReindexTask(populator), 0, REINDEX_INTERVAL, TimeUnit.MILLISECONDS);
		}
		catch(Exception e)
		{
			logger.logp(Level.INFO, "ReindexService", "schedule", e.getMessage());
		}
	}
	
	private class ReindexTask extends TimerTask{
		/**
		 * populator to populate indices
		 */
		private SolrPopulator populator;

		public ReindexTask(SolrPopulator populator)
		{
			this.populator = populator;
		}
		
		@Override
		public void run()
		{
			synchronized (padLock) {
				isReindexing = true;
			}
			
			CacheKey key = new CacheKey("Reindexing", CacheKeyType.Misc);
		
			boolean reindexing = false;
			try
			{
				System.out.println("******************** Started ****************");
				
				// check if any other worker node is already reindexing
				Object value = SysManagerFactory.getCacheManager().get(key);
				
				if(value == null || !((Boolean)value)) // no other worker node is reindexing
				{
					reindexing = true;
					
					// set appropriate key value indicating that this node is reindexing the solr indices
					SysManagerFactory.getCacheManager().set(key, true);
					
					logger.logp(Level.INFO, "ReindexTask", "schedule", "scheduling the task");
					// change solr core to Reindex
					populator.changeToReindex();
					
					// start populating fresh indices
					populator.start();
					
					// swap cores so that main core points to 
					populator.swapCores();
					
					// done reindexing
					SysManagerFactory.getCacheManager().set(key, false);
				}
				System.out.println("******************** Completed ****************");
				
				Thread.sleep(5000);
			}
			catch (MalformedURLException e)
			{
				logger.logp(Level.INFO, "ReindexTask", "changeToReindex", e.getMessage());
				e.printStackTrace();
			}
			catch (SolrServerException e)
			{
				logger.logp(Level.INFO, "ReindexTask", "run", e.getMessage());
				e.printStackTrace();
			}
			catch (IOException e)
			{
				logger.logp(Level.INFO, "ReindexTask", "run", e.getMessage());
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				logger.logp(Level.INFO, "ReindexTask", "run", e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				// done reindexing
				if(reindexing)
					SysManagerFactory.getCacheManager().set(key, false);
			}
			
			synchronized (padLock) {
				isReindexing = false;
			}
		}
	}
	
	public static void main(String args[]) throws IOException
	{
		if(true)
    	{
    		File f = new File("/home/anup/rep/imaging/iengine/trunk/solr-reindexer.properties");//iengine.properties
    		if(f.isFile())
    		{
    			System.out.println("loading system properties from "+f);
    			BufferedReader inStream = new BufferedReader(new FileReader(f));
	    		Properties props = new Properties();
	    		props.load(inStream);
	    		
	    		props.putAll(System.getProperties()); //copy existing properties, it is overwritten :-(
	    		props.list(System.out);
	    		
	    		System.setProperties(props);
	    		inStream.close();
    		}
    	}
    	
		ReindexService service = new ReindexService();
		
		service.schedule();
	}

	@Override
	public ServiceStatus getServiceStatus() {
		
		synchronized (padLock) {
			serviceStatus.setReindexing(isReindexing);
		}
		
		return serviceStatus;
	}

	@Override
	public ServiceType getServiceType() {
		return serviceType;
	}

	@Override
	public void restart() {
		
		if(timer!=null){
			timer.shutdownNow();
			timer = new ScheduledThreadPoolExecutor(1);
			schedule();
		}
		
		synchronized (padLock) {
			isReindexing = false;
		}
		
	}
	
	/**
	 * shutdown reindex service
	 * @throws DataAccessException 
	 */
	public void shutdown() throws DataAccessException{
		
		stopServiceMonitor();
		
		if(timer!=null){
			timer.shutdownNow();
			timer=null;
		}
		
		synchronized (padLock) {
			isReindexing = false;
		}
	}
}
