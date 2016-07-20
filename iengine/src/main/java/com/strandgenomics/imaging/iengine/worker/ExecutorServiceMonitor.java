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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

/**
 * helps to monitor executor services
 * @author navneet
 *
 */
public class ExecutorServiceMonitor {
	
	private static Object padLock = new Object();
	/**
	 * list of future of submitted tasks
	 */
	private List<Future> submittedTasks;
	
	/**
	 * timer used to check if submitted tasks are completed 
	 * checks at regular intervals
	 */
	private Timer timer = null;
	
	/**
	 * interval at which timer task executes
	 */
	private int period = 1000;
	
	public ExecutorServiceMonitor() {
		
		submittedTasks = new ArrayList<Future>();
		
		timer = new Timer(true);
		
		checkTaskCompletion task = new checkTaskCompletion();
		timer.scheduleAtFixedRate(task, 0, period);
		
	}
	
	class checkTaskCompletion extends TimerTask{

		@Override
		public void run() {
			synchronized (padLock) {
				
				Iterator<Future> iterator = submittedTasks.iterator();
				
				while(iterator.hasNext()){
					
					Future<?> future = iterator.next();
					
					  if(future.isDone())
						  iterator.remove();
				}
			}
		}
		
	}
	
	/**
	 * add future to the submitted task queue
	 * @param future
	 */
	public void addToExecutorServiceMonitor(Future future){
		
		synchronized (padLock) {
			submittedTasks.add(future);
		}
	}
	
	/**
	 * get size of executor queue
	 * @return
	 */
	public int getExecutorServiceQueueSize(){
		
		synchronized (padLock) {
			return submittedTasks.size();
		}
	}
	
	/**
	 * clean the monitor
	 */
	public void cleanExecutorServiceMonitor(){
		
		synchronized (padLock) {
			submittedTasks.clear();
		}
	}
	
	public void stopExecutorServiceMonitor(){
		
		timer.cancel();
		timer.purge();
		
		synchronized (padLock) {
			submittedTasks.clear();
		}
		
	}
}
